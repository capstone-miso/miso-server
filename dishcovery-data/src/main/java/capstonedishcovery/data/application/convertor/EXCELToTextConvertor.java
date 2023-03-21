package capstonedishcovery.data.application.convertor;

import org.apache.poi.hssf.usermodel.HSSFCell;
import org.apache.poi.hssf.usermodel.HSSFRow;
import org.apache.poi.hssf.usermodel.HSSFSheet;
import org.apache.poi.hssf.usermodel.HSSFWorkbook;
import org.apache.poi.ss.usermodel.*;

import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.time.LocalDateTime;
import java.util.Calendar;
import java.util.Date;

/**
 * author        : duckbill413
 * date          : 2023-03-21
 * description   :
 **/

public class EXCELToTextConvertor {
    public static void main(String[] args) {
        String path = "D:\\downloads\\";
        String testFileName = "2023년 2월 자치행정과 업무추진비 집행내역.xls";
        String src = path + testFileName;
        StringBuffer sb = new StringBuffer();
        try {
            FileInputStream fis = new FileInputStream(src);
            Workbook wb = WorkbookFactory.create(fis);
            int sheetCnt = wb.getNumberOfSheets();

            for (int i = 0; i < sheetCnt; i++) {
                Sheet sheet = wb.getSheetAt(i);

                int rowCnt = sheet.getPhysicalNumberOfRows();
                for (int j = 0; j < rowCnt; j++) {
                    Row row = sheet.getRow(j);
                    int cellCnt = row.getPhysicalNumberOfCells();

                    int cellRealCnt = 0;
                    for (int k = 0; k < cellCnt; k++) {
                        Cell cell = row.getCell(k);
                        if (cell == null)
                            continue;
                        switch (cell.getCellType()) {
                            case STRING -> {
                                cellRealCnt += 1;
                                sb.append(cell.getStringCellValue() + " ");
                            }
                            case NUMERIC -> {
                                cellRealCnt += 1;
                                if (DateUtil.isCellDateFormatted(cell)) {
                                    Calendar calendar = Calendar.getInstance();
                                    calendar.setTime(cell.getDateCellValue());
                                    sb.append(calendar.get(Calendar.HOUR_OF_DAY) + ":" + calendar.get(Calendar.MINUTE) + " ");
                                }
                                else {
                                    sb.append((int) cell.getNumericCellValue() + " ");
                                }
                            }
                            default -> {}
                        }
                    }
                    if (cellRealCnt > 0)
                        sb.append("\n");
                }
            }
        } catch (FileNotFoundException e) {
            throw new RuntimeException(e);
        } catch (IOException e) {
            throw new RuntimeException(e);
        }

        System.out.println(sb);
    }

}
