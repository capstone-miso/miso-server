package capstone.miso.dishcovery.application.files.convertor;

import capstone.miso.dishcovery.application.files.FileData;
import capstone.miso.dishcovery.application.files.Files;
import lombok.extern.log4j.Log4j2;
import org.apache.pdfbox.pdmodel.PDDocument;
import org.apache.pdfbox.text.PDFTextStripper;
import org.springframework.stereotype.Component;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Objects;
import java.util.stream.Collectors;

/**
 * author        : duckbill413
 * date          : 2023-03-20
 * description   :
 **/
@Log4j2
@Component
public class PDFToTextConvertor {
    private final int CASE_OPTION_CNT = 13;

    private List<FileData> parsingString(String source, Files file) {
        String[] strLines = source.split("\\n");

        List<List<String>> result = new ArrayList<>();
        int length = 0; // 칼럼의 개수

        for (String line : strLines) {
            List<String> strings = Arrays.stream(line.split("\\*\\*")).map(s -> s.trim()).collect(Collectors.toList());

            // 줄의 첫번째가 숫자이면 (연번) 삭제
            if (strings.get(0).matches("\\d+")) strings.remove(0);

            if (length < strings.size()) length = strings.size();
            result.add(strings);
        }

        List<FileData> fileDatas = new ArrayList<>();
        int DOC_PATTERN = -1;
        String convertMsg = file.getConvertResult();

        for (List<String> row : result) {
            FileData fileData = null;
            try {
                fileData = parseFile(DOC_PATTERN, row, file);
            } catch (Exception e) {
                // 패턴이 이미 정해진 경우 스킵
                if (DOC_PATTERN != -1) {
                    continue;
                }
                int pattern = -1;
                for (int i = 0; i < CASE_OPTION_CNT; i++) {
                    try {
                        fileData = parseFile(i, row, file);
                        pattern = i;
                        break;
                    } catch (Exception ex) {
//                        ex.printStackTrace();
                        if (pattern == -1 && convertMsg == null) {
                            convertMsg = "파일 변환 실패";
                        }
                    }
                }
                if (pattern != -1) {
                    DOC_PATTERN = pattern;
                    convertMsg = "파일 변환 성공. 패턴No." + pattern;
                }
            }
            // null이 아닌 경우 추가
            if (Objects.nonNull(fileData))
                fileDatas.add(fileData);
        }
        // 파일 변환 성공 여부 입력
        if (DOC_PATTERN == -1)
            file.setConverted(false);
        else
            file.setConverted(true);
        // 파일 변환 성공 메시지
        file.setConvertResult(convertMsg);

        return fileDatas;
    }

    private FileData parseFile(int pattern, List<String> row, Files file) {
        switch (pattern) {
            case 0:
                return case0(row, file);
            case 1:
                return case1(row, file);
            case 2:
                return case2(row, file);
            case 3:
                return case3(row, file);
            case 4:
                return case4(row, file);
            case 5:
                return case5(row, file);
            case 6:
                return case6(row, file);
            case 7:
                return case7(row, file);
            case 8:
                return case8(row, file);
            case 9:
                return case9(row, file);
            case 10:
                return case10(row, file);
            case 11:
                return case11(row, file);
            case 12:
                return case12(row, file);
            default:
                throw new RuntimeException("Invalid pattern");
        }
    }

    private FileData case12(List<String> row, Files file) {
        List<String> d = Arrays.stream(row.get(0).split("\\D{1,3}")).toList();
        List<String> t = Arrays.stream(row.get(1).split("\\D{1,3}")).toList();
        // 업무추진비 시간 구하기
        String date = String.join("-", d);
        String time = String.join("-", t);

        String storeName = row.get(3);
        String storeAddress = row.get(4);
        String purpose = row.get(6);
        String participants = row.get(5);
        String cost = row.get(2);
        String paymentOption = row.get(7);
        String expenditure = null;

        FileData fileData = FileData.builder()
                .date(date)
                .time(time)
                .storeName(storeName)
                .storeAddress(storeAddress)
                .purpose(purpose)
                .participants(participants)
                .cost(cost)
                .paymentOption(paymentOption)
                .expenditure(expenditure)
                .files(file)
                .build();

        return fileData;
    }

    private FileData case11(List<String> row, Files file) {
        List<String> d = Arrays.stream(row.get(2).split("\\D{1,3}")).toList();
        List<String> t = Arrays.stream(row.get(3).split("\\D{1,3}")).toList();
        // 업무추진비 시간 구하기
        String date = String.join("-", d);
        String time = String.join("-", t);

        String storeName = row.get(5);
        String storeAddress = row.get(6);
        String purpose = row.get(4);
        String participants = row.get(8);
        String cost = row.get(7);
        String paymentOption = row.get(9);
        String expenditure = row.get(1);

        FileData fileData = FileData.builder()
                .date(date)
                .time(time)
                .storeName(storeName)
                .storeAddress(storeAddress)
                .purpose(purpose)
                .participants(participants)
                .cost(cost)
                .paymentOption(paymentOption)
                .expenditure(expenditure)
                .files(file)
                .build();

        return fileData;
    }

    private FileData case10(List<String> row, Files file) {
        String d = row.get(1);
        List<String> t = Arrays.stream(row.get(2).split("\\D{1,3}")).toList();
        // 업무추진비 시간 구하기
        String date = d.substring(0, 4) + "-" + d.substring(4, 6) + "-" + d.substring(6, 8);
        String time = String.join("-", t);

        String storeName = row.get(3);
        String storeAddress = null;
        String purpose = row.get(4);
        String participants = row.get(5);
        String cost = row.get(6);
        String paymentOption = row.get(7);
        String expenditure = row.get(8);

        FileData fileData = FileData.builder()
                .date(date)
                .time(time)
                .storeName(storeName)
                .storeAddress(storeAddress)
                .purpose(purpose)
                .participants(participants)
                .cost(cost)
                .paymentOption(paymentOption)
                .expenditure(expenditure)
                .files(file)
                .build();

        return fileData;
    }

    private FileData case9(List<String> row, Files file) {
        List<String> d = Arrays.stream(row.get(1).split("\\D{1,3}")).toList();
        List<String> t = Arrays.stream(row.get(2).split("\\D{1,3}")).toList();
        // 업무추진비 시간 구하기
        String date = String.join("-", d);
        String time = String.join("-", t);

        String storeName = row.get(4);
        String storeAddress = null;
        String purpose = row.get(5);
        String participants = row.get(6);
        String cost = row.get(7);
        String paymentOption = row.get(8);
        String expenditure = row.get(9);

        FileData fileData = FileData.builder()
                .date(date)
                .time(time)
                .storeName(storeName)
                .storeAddress(storeAddress)
                .purpose(purpose)
                .participants(participants)
                .cost(cost)
                .paymentOption(paymentOption)
                .expenditure(expenditure)
                .files(file)
                .build();

        return fileData;
    }

    private FileData case8(List<String> row, Files file) {
        List<String> d = Arrays.stream(row.get(0).split("\\D{1,3}")).toList();
        List<String> t = Arrays.stream(row.get(1).split("\\D{1,3}")).toList();
        // 업무추진비 시간 구하기
        String date = String.join("-", d);
        String time = String.join("-", t);

        String storeName = row.get(2);
        String storeAddress = null;
        String purpose = row.get(3);
        String participants = row.get(4);
        String cost = row.get(5);
        String paymentOption = row.get(6);
        String expenditure = row.get(7);

        FileData fileData = FileData.builder()
                .date(date)
                .time(time)
                .storeName(storeName)
                .storeAddress(storeAddress)
                .purpose(purpose)
                .participants(participants)
                .cost(cost)
                .paymentOption(paymentOption)
                .expenditure(expenditure)
                .files(file)
                .build();

        return fileData;
    }

    private FileData case7(List<String> row, Files file) {
        List<String> d = Arrays.stream(row.get(1).split("\\D{1,3}")).toList();
        List<String> t = Arrays.stream(row.get(2).split("\\D{1,3}")).toList();
        // 업무추진비 시간 구하기
        String date = String.join("-", d);
        String time = String.join("-", t);

        String storeName = row.get(3);
        String storeAddress = null;
        String purpose = row.get(4);
        String participants = row.get(5);
        String cost = row.get(6);

        String paymentOption = null;
        if (row.size() == 8)
            paymentOption = row.get(7);
        String expenditure = null;

        FileData fileData = FileData.builder()
                .date(date)
                .time(time)
                .storeName(storeName)
                .storeAddress(storeAddress)
                .purpose(purpose)
                .participants(participants)
                .cost(cost)
                .paymentOption(paymentOption)
                .expenditure(expenditure)
                .files(file)
                .build();

        return fileData;
    }

    private FileData case6(List<String> row, Files file) {
        List<String> d = Arrays.stream(row.get(1).split("\\D{1,3}")).toList();
        // 업무추진비 시간 구하기
        String date = String.join("-", d);

        String storeName = row.get(2);
        String storeAddress = null;
        String purpose = row.get(3);
        String participants = row.get(4);
        String cost = row.get(5);
        String paymentOption = row.get(6);
        String expenditure = row.get(7);

        FileData fileData = FileData.builder()
                .date(date)
                .storeName(storeName)
                .storeAddress(storeAddress)
                .purpose(purpose)
                .participants(participants)
                .cost(cost)
                .paymentOption(paymentOption)
                .expenditure(expenditure)
                .files(file)
                .build();

        return fileData;
    }

    private FileData case5(List<String> row, Files file) {
        List<String> d = Arrays.stream(row.get(1).split("\\D{1,3}")).toList();
        List<String> t = Arrays.stream(row.get(2).split("\\D{1,3}")).toList();
        // 업무추진비 시간 구하기
        String date = String.join("-", d);
        String time = String.join("-", t);

        String storeName = row.get(3);
        String storeAddress = null;
        String purpose = row.get(4);
        String participants = row.get(5);
        String cost = row.get(7);
        String paymentOption = row.get(8);
        String expenditure = row.get(9);

        FileData fileData = FileData.builder()
                .date(date)
                .time(time)
                .storeName(storeName)
                .storeAddress(storeAddress)
                .purpose(purpose)
                .participants(participants)
                .cost(cost)
                .paymentOption(paymentOption)
                .expenditure(expenditure)
                .files(file)
                .build();

        return fileData;
    }

    private FileData case4(List<String> row, Files file) {
        String[] dateTime = row.get(1).split(" ");
        List<String> d = Arrays.stream(dateTime[0].split("\\D{1,3}")).toList();
        List<String> t = Arrays.stream(dateTime[1].split("\\D{1,3}")).toList();
        // 업무추진비 시간 구하기
        String date = String.join("-", d);
        String time = String.join("-", t);

        String storeName = row.get(2);
        String storeAddress = null;
        String purpose = row.get(3);
        String participants = row.get(4);
        String cost = row.get(5);
        String paymentOption = row.get(6);
        String expenditure = row.get(7);

        FileData fileData = FileData.builder()
                .date(date)
                .time(time)
                .storeName(storeName)
                .storeAddress(storeAddress)
                .purpose(purpose)
                .participants(participants)
                .cost(cost)
                .paymentOption(paymentOption)
                .expenditure(expenditure)
                .files(file)
                .build();

        return fileData;
    }
    /*
    private FileData template(List<String> row, Files file){
        List<String> d = Arrays.stream(row.get().split("\\D{1,3}")).toList();
        List<String> t = Arrays.stream(row.get().split("\\D{1,3}")).toList();
        // 업무추진비 시간 구하기
        String date = String.join("-", d);
        String time = String.join("-", t);

        String storeName = null;
        String storeAddress = null;
        String purpose = null;
        String participants = null;
        String cost = null;
        String paymentOption = null;
        String expenditure = null;

        FileData fileData = FileData.builder()
                .date(date)
                .time(time)
                .storeName(storeName)
                .storeAddress(storeAddress)
                .purpose(purpose)
                .participants(participants)
                .cost(cost)
                .paymentOption(paymentOption)
                .expenditure(expenditure)
                .files(file)
                .build();

        return fileData;
    }
     */

    private FileData case2(List<String> row, Files file) {
        List<String> d = Arrays.stream(row.get(0).split("\\D{1,3}")).toList();
        List<String> t = Arrays.stream(row.get(1).split("\\D{1,3}")).toList();
        // 업무추진비 시간 구하기
        String date = String.join("-", d);
        String time = String.join("-", t);

        String purpose = row.get(2);
        String storeName = row.get(3);
        String storeAddress = row.get(4);
        String cost = row.get(5);
        String participants = row.get(6);
        String paymentOption = row.get(7);

        FileData fileData = FileData.builder()
                .date(date)
                .time(time)
                .purpose(purpose)
                .storeName(storeName)
                .storeAddress(storeAddress)
                .cost(cost)
                .participants(participants)
                .paymentOption(paymentOption)
                .files(file)
                .build();

        return fileData;
    }

    private FileData case3(List<String> row, Files file) {
        List<String> d = Arrays.stream(row.get(1).split("\\D{1,3}")).toList();
        List<String> t = Arrays.stream(row.get(2).split("\\D{1,3}")).toList();
        // 업무추진비 시간 구하기
        String date = String.join("-", d);
        String time = String.join("-", t);

        String storeName = row.get(3);
        String storeAddress = null;
        String purpose = row.get(4);
        String participants = row.get(5);
        String cost = row.get(6);
        String paymentOption = row.get(7);
        String expenditure = row.get(8);

        FileData fileData = FileData.builder()
                .date(date)
                .time(time)
                .storeName(storeName)
                .storeAddress(storeAddress)
                .purpose(purpose)
                .participants(participants)
                .cost(cost)
                .paymentOption(paymentOption)
                .files(file)
                .expenditure(expenditure)
                .build();

        return fileData;
    }

    private FileData case1(List<String> row, Files file) {
        List<String> d = Arrays.stream(row.get(1).split("\\D{1,3}")).toList();
        List<String> t = Arrays.stream(row.get(2).split("\\D{1,3}")).toList();
        // 업무추진비 시간 구하기
        String date = String.join("-", d);
        String time = String.join("-", t);

        String purpose = row.get(4); // 집행목적
        String storeName = row.get(5);
        String storeAddress = row.get(6);
        String cost = row.get(7);
        String participants = row.get(8);
        String paymentOption = row.get(9);

        FileData fileData = FileData.builder().files(file).date(date).time(time).purpose(purpose).storeName(storeName).storeAddress(storeAddress).cost(cost).participants(participants).paymentOption(paymentOption).build();

        return fileData;
    }

    private FileData case0(List<String> row, Files file) {
        List<String> d = Arrays.stream(row.get(1).split("\\D{1,3}")).toList();
        List<String> t = Arrays.stream(row.get(2).split("\\D{1,3}")).toList();
        // 업무추진비 시간 구하기
        String date = String.join("-", d);
        String time = String.join("-", t);

        // 집행목적
        String purpose = row.get(3);
        // 매장명
        String storeName = row.get(4);
        // 매장 주소
        String storeAddress = row.get(5);
        // 사용 금액
        String cost = row.get(6);
        // 인원 수
        String participants = row.get(7);
        String paymentOption = row.get(8);
        FileData fileData = FileData.builder().files(file).date(date).time(time).purpose(purpose).storeName(storeName).storeAddress(storeAddress).cost(cost).participants(participants).paymentOption(paymentOption).build();

        return fileData;
    }

    private static <E> E getLastElement(ArrayList<E> list) {
        if ((list != null) && (list.isEmpty() == false)) {
            int lastIdx = list.size() - 1;
            E lastElement = list.get(lastIdx);
            return lastElement;
        } else return null;
    }

    public List<FileData> convertFileToFileData(Files file) {
        String path = "D:\\downloads\\";
        String fileName = file.getFileName();
        String src = path + fileName;
        try (PDDocument document = PDDocument.load(new File(src))) {
            PDFTextStripper stripper = new PDFTextStripper();
            stripper.setLineSeparator("\n");
            stripper.setWordSeparator("**");
            String text = stripper.getText(document);

            List<FileData> result = parsingString(text, file);
            return result;
        } catch (IOException e) {
            e.printStackTrace();
        }
        return null;
    }
}
