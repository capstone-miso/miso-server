package capstone.miso.dishcovery.application.files.convertor;

import capstone.miso.dishcovery.application.files.FileData;
import capstone.miso.dishcovery.application.files.Files;
import org.apache.commons.lang3.StringUtils;
import org.apache.poi.ss.usermodel.*;
import org.springframework.stereotype.Component;

import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.util.*;

/**
 * author        : duckbill413
 * date          : 2023-03-21
 * description   :
 **/
@Component
public class EXCELToTextConvertor implements TextConvertor{
    private final static String[] date = {"일자", "집행일"};
    private final static String[] time = {"시간", "시각"};
    private final static String[] storeName = {"장소"};
    private final static String[] storeAddress = {"주소"};
    private final static String[] purpose = {"집행목적"};
    private final static String[] participants = {"대상인원수"};
    private final static String[] cost = {"금액", "집행액"};
    private final static String[] paymentOption = {"결제방법"};
    private final static String[] expenditure = {"비목"};
    public List<FileData> convertFileToFileData(Files file) {
        String path = "D:\\downloads\\";
        String testFileName = file.getFileName();
        String src = path + testFileName;

        try (FileInputStream fis = new FileInputStream(src)) {
            Workbook wb = WorkbookFactory.create(fis);
            int sheetCnt = wb.getNumberOfSheets();

            for (int k = 0; k < sheetCnt; k++) {
                Sheet sheet = wb.getSheetAt(k);
                if (sheet.getFirstRowNum() == sheet.getLastRowNum()) continue;

                int rows = sheet.getLastRowNum();
                int cols = sheet.getRow(0).getLastCellNum();
                if (rows < 0 || cols < 0){
                    continue;
                }

                String[] keys = new String[cols];
                HashMap<String, List<String>> data = new HashMap<>();

                for (int i = 0; i <= rows; i++) {
                    Row row = sheet.getRow(i);
                    if (row == null)
                        continue;

                    for (int j = 0; j < cols; j++) {
                        Cell cell = row.getCell(j);
                        if (cell == null && keys[j] == null) continue;
                        if (cell == null) {
                            data.get(keys[j]).add(null);
                            continue;
                        }

                        switch (cell.getCellType()) {
                            case STRING -> {
                                String tempStr = cell.getStringCellValue().trim().replaceAll("\n", "");
                                if (StringUtils.isBlank(tempStr))
                                    continue;

                                if (keys[j] == null) {
                                    if (tempStr.contains("집행내역")) {
                                        continue;
                                    }
                                    keys[j] = tempStr.replaceAll(" ", "");
                                    data.put(keys[j], new ArrayList<>());
                                    continue;
                                }

                                if (tempStr.matches("\\d{2,4}.{1,3}\\d{1,2}.{1,3}\\d{1,2}.") || tempStr.matches("\\d{2,4}.{1,3}\\d{1,2}.{1,3}\\d{1,2}") || tempStr.matches("\\d{1,2}.{1,3}\\d{1,2}.") || tempStr.matches("\\d{1,2}.{1,3}\\d{1,2}")) {
                                    List<String> array = Arrays.stream(tempStr.split("\\D{1,3}")).toList();

                                    String dateTime = String.join("-", array);
                                    data.get(keys[j]).add(dateTime);
                                } else {
                                    data.get(keys[j]).add(tempStr);
                                }
                            }
                            case NUMERIC -> {
                                if (DateUtil.isCellDateFormatted(cell)) {
                                    Date dateValue = cell.getDateCellValue();
                                    int year = dateValue.getYear() + 1900;
                                    int month = dateValue.getMonth() + 1;
                                    int day = dateValue.getDate();
                                    int hour = dateValue.getHours();
                                    int min = dateValue.getMinutes();
                                    int second = dateValue.getSeconds();

                                    if (keys[j].contains("일")) {
                                        data.get(keys[j]).add(String.join("-", new String[]{String.valueOf(year), String.valueOf(month), String.valueOf(day)}));
                                    } else {
                                        data.get(keys[j]).add(String.join("-", new String[]{String.valueOf(hour), String.valueOf(min), String.valueOf(second)}));
                                    }
                                } else {
                                    String numeric = String.valueOf((int) cell.getNumericCellValue());
                                    data.get(keys[j]).add(numeric);
                                }
                            }
                            default -> {
                                if (!Objects.isNull(data.get(keys[j]))) {
                                    data.get(keys[j]).add(null);
                                }
                            }
                        }
                    }
                }
                List<FileData> result = convertFileData(keyNameMapping(keys), data, file);
                return result;
            }
        } catch (FileNotFoundException e) {
            throw new RuntimeException(e);
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
        return null;
    }

    private static HashMap<String, String> keyNameMapping(String[] keys) {
        HashMap<String, String> keyMap = new HashMap<>();
        List<String> fieldNames = Arrays.asList(date[0], time[0], storeName[0], storeAddress[0],
                purpose[0], participants[0], cost[0], paymentOption[0], expenditure[0]);
        for (String key : keys) {
            if (fieldNames.contains(key)) {
                keyMap.put(key, key);
            }
        }
        return keyMap;
    }

    private List<FileData> convertFileData(HashMap<String, String> keys, HashMap<String, List<String>> data, Files file) {
        if (data.get(storeName[0]) == null){
            return null;
        }
        int size = data.get(storeName[0]).size();
        String d, t, sn, sa, pp, pt, c, po, et;
        d = keys.getOrDefault(date[0], null);
        t = keys.getOrDefault(time[0], null);
        sn = keys.getOrDefault(storeName[0], null);
        sa = keys.getOrDefault(storeAddress[0], null);
        pp = keys.getOrDefault(purpose[0], null);
        pt = keys.getOrDefault(participants[0], null);
        c = keys.getOrDefault(cost[0], null);
        po = keys.getOrDefault(paymentOption[0], null);
        et = keys.getOrDefault(expenditure[0], null);

        List<FileData> fileDatas = new ArrayList<>();
        for (int i = 0; i < size; i++) {
            try {
                FileData fileData = FileData.builder()
                        .date(Objects.nonNull(d) ? data.get(d).get(i) : null)
                        .time(Objects.nonNull(t) ? data.get(t).get(i) : null)
                        .storeName(Objects.nonNull(sn) ? data.get(sn).get(i) : null)
                        .storeAddress(Objects.nonNull(sa) ? data.get(sa).get(i) : null)
                        .purpose(Objects.nonNull(pp) ? data.get(pp).get(i) : null)
                        .participants(Objects.nonNull(pt) ? data.get(pt).get(i) : null)
                        .cost(Objects.nonNull(c) ? data.get(c).get(i) : null)
                        .paymentOption(Objects.nonNull(po) ? data.get(po).get(i) : null)
                        .expenditure(Objects.nonNull(et) ? data.get(et).get(i) : null)
                        .files(file)
                        .build();

                fileDatas.add(fileData);
            } catch (Exception e) {
            }
        }
        file.setConverted(true);
        file.setConvertResult("EXCEL Convert");
        return fileDatas;
    }
}
