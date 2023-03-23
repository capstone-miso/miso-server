package capstonedishcovery.data.application.convertor;

import capstonedishcovery.data.application.files.FileData;
import org.apache.poi.ss.usermodel.*;

import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.time.ZoneId;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;

/**
 * author        : duckbill413
 * date          : 2023-03-21
 * description   :
 **/

public class EXCELToTextConvertor {
    public static void main(String[] args) {
        String path = "D:\\downloads\\";
        String testFileName = "2023년 2월 자치행정과 업무추진비 집행내역.xls";
//        String testFileName = "업무추진비 집행내역(2월).xls";
        String src = path + testFileName;
        StringBuffer sb = new StringBuffer();
        List<ArrayList> result = new ArrayList<>();
        String department = "";

        try {
            FileInputStream fis = new FileInputStream(src);
            Workbook wb = WorkbookFactory.create(fis);
            int sheetCnt = wb.getNumberOfSheets();

            for (int i = 0; i < sheetCnt; i++) {
                Sheet sheet = wb.getSheetAt(i);

                int rowCnt = sheet.getPhysicalNumberOfRows();
                for (int j = 0; j < rowCnt; j++) {
                    Row row = sheet.getRow(j);
                    int cellCnt = row.getPhysicalNumberOfCells() + 1;
                    int cellRealCnt = 0;

                    ArrayList rowObj = new ArrayList<>();
                    for (int k = 0; k < cellCnt; k++) {
                        Cell cell = row.getCell(k);
                        if (cell == null)
                            continue;

                        switch (cell.getCellType()) {
                            case STRING -> {
                                cellRealCnt += 1;
                                String tempStr = cell.getStringCellValue().trim();
                                if (tempStr.contains("집행내역")) {
                                    department = tempStr.split(" ")[2];
                                }
                                if (tempStr.matches("\\d{2,4}.{1,3}\\d{1,2}.{1,3}\\d{1,2}.")) {
                                    List<Integer> arr = Arrays.stream(tempStr.split("\\D{1,3}"))
                                            .map(s -> Integer.parseInt(s)).collect(Collectors.toList());

                                    LocalDateTime dateTime = null;
                                    if (arr.size() == 3) {
                                        LocalDate date = LocalDate.of(arr.get(0), arr.get(1), arr.get(2));
                                        dateTime = LocalDateTime.of(date, LocalTime.now());
                                    } else if (arr.size() == 2) {
                                        LocalTime time = LocalTime.of(arr.get(1), arr.get(2));
                                        dateTime = LocalDateTime.of(LocalDate.now(), time);
                                    }
                                    rowObj.add(dateTime);
                                    sb.append(dateTime + " ");
                                } else {
                                    rowObj.add(tempStr);
                                    sb.append(tempStr + " ");
                                }
                            }
                            case NUMERIC -> {
                                cellRealCnt += 1;
                                if (DateUtil.isCellDateFormatted(cell)) {
                                    LocalDateTime dateTime = cell.getDateCellValue().toInstant()
                                            .atZone(ZoneId.systemDefault())
                                            .toLocalDateTime();
                                    rowObj.add(dateTime);
                                    sb.append(dateTime + " ");
                                } else {
                                    String numeric = String.valueOf(cell.getNumericCellValue()).replaceAll("\\D", "");
                                    rowObj.add(Integer.parseInt(numeric));
                                    sb.append(Integer.parseInt(numeric));
                                }
                            }
                            default -> {
                            }
                        }
                    }
                    if (cellRealCnt > 0)
                        sb.append("\n");

                    if (rowObj.size() >= 9) {
                        if (rowObj.size() == 10)
                            rowObj.remove(0);
                        result.add(rowObj);
                    }
                }
            }
        } catch (FileNotFoundException e) {
            throw new RuntimeException(e);
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
        for (ArrayList row : result) {
            FileData fileData = saveFileData(department, row);
            System.out.println(fileData);
        }
    }

    private static FileData saveFileData(String department, ArrayList rowObj) {
        try {
            LocalDateTime useDate = LocalDateTime.of(
                    ((LocalDateTime) rowObj.get(1)).toLocalDate(),
                    ((LocalDateTime) rowObj.get(2)).toLocalTime()
            );
            FileData fileData = FileData.builder()
                    .name((String) rowObj.get(0))
                    .useDate(useDate)
                    .department(department)
                    .storeName((String) rowObj.get(3))
                    .purpose((String) rowObj.get(4))
                    .participants((Integer) rowObj.get(5))
                    .cost((Integer) rowObj.get(6))
                    .paymentOption((String) rowObj.get(7))
                    .expenditure((String) rowObj.get(8))
                    .build();
            return fileData;

        } catch (java.lang.ClassCastException e) {
        }
        return null;
    }
}
