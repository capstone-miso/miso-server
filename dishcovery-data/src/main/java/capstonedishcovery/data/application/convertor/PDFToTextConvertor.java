package capstonedishcovery.data.application.convertor;

import capstonedishcovery.data.application.files.FileData;
import org.apache.commons.lang3.StringUtils;
import org.apache.pdfbox.pdmodel.PDDocument;
import org.apache.pdfbox.text.PDFTextStripper;
import org.springframework.cglib.core.Local;
import org.springframework.util.Assert;

import java.io.File;
import java.io.IOException;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;
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
public class PDFToTextConvertor {
    private static final String[] TITLE = {"사용내역", "집행내역"};

    public static void convertPDFToText(String fileName) throws IOException {
        String path = "D:\\downloads\\";
        String testFileName = "2020 1월 복지국 기관운영업무추진비 집행내역.pdf";
        String src = path + testFileName;
        PDDocument document = PDDocument.load(new File(src));
        PDFTextStripper stripper = new PDFTextStripper();
        stripper.setWordSeparator(", ");
        String text = stripper.getText(document);
        System.out.println(text);
        parsingString(text);
    }

    private static void parsingString(String source) {
        String[] strLines = source.split("\\n");

        List<List<String>> result = new ArrayList<>();
        int length = 0; // 칼럼의 개수

        for (String line : strLines) {
            List<String> strings = Arrays.stream(line.split("\\*\\*")).map(s -> s.trim()).collect(Collectors.toList());

            // 줄의 첫번째가 숫자이면 (연번) 삭제
            if (strings.get(0).matches("\\d+"))
                strings.remove(0);

            if (length < strings.size())
                length = strings.size();
            result.add(strings);
        }
        // 데이터 자료인 값을 객체화
        for (List<String> row : result) {
            FileData fileData = case1(row);
            if (fileData != null)
                System.out.println(fileData);
        }
    }

    private static FileData case1(List<String> row) {
        try {
            List<Integer> d = Arrays.stream(row.get(1).split("\\D{1,3}")).map(Integer::parseInt).collect(Collectors.toList());
            List<Integer> t = Arrays.stream(row.get(2).split("\\D{1,3}")).map(Integer::parseInt).collect(Collectors.toList());
            // 업무추진비 시간 구하기
            LocalDate date = LocalDate.now();
            LocalTime time = LocalTime.now();
            if (d.size() >= 3) {
                date = date.withYear(d.get(0));
                date = date.withMonth(d.get(1));
                date = date.withDayOfMonth(d.get(2));
            } else if (d.size() == 2) {
                date = date.withMonth(d.get(0));
                date = date.withDayOfMonth(d.get(0));
            }
            if (t.size() >= 3) {
                time = time.withHour(t.get(0));
                time = time.withMinute(t.get(1));
                time = time.withSecond(t.get(2));
            } else if (t.size() == 2) {
                time = time.withHour(t.get(0));
                time = time.withMinute(t.get(1));
            }

            // 집행목적
            String purpose = row.get(3);
            // 매장명
            String storeName = row.get(4);
            // 매장 주소
            String storeAddress = row.get(5);
            // 사용 금액
            int cost = Integer.parseInt(row.get(6).replaceAll("\\D", ""));
            // 인원 수
            int participants = row.get(7).contains("외") ? Integer.parseInt(row.get(7).replaceAll("\\D", "")) + 1 :
                    Integer.parseInt(row.get(7).replaceAll("\\D", ""));
            String paymentOption = row.get(8);

            FileData fileData = FileData.builder()
                    .date(date)
                    .time(time)
                    .purpose(purpose)
                    .storeName(storeName)
                    .storeAddress(storeAddress)
                    .cost(cost)
                    .participants(participants)
                    .paymentOption(paymentOption)
                    .build();

            return fileData;
        } catch (Exception e) {
        }

        return null;
    }

    private static FileData saveFileData(String department, List<String> rowObj) {
        try {
            List<Integer> d = Arrays.stream(rowObj.get(1).split("\\D{1,3}")).map(Integer::parseInt).collect(Collectors.toList());
            List<Integer> t = Arrays.stream(rowObj.get(2).split("\\D{1,3}")).map(Integer::parseInt).collect(Collectors.toList());

            LocalDate date = LocalDate.of(d.get(0), d.get(1), d.get(2));
            LocalTime time = null;
            if (t.size() >= 3) {
                time = LocalTime.of(t.get(0), t.get(1), t.get(2));
            } else {
                time = LocalTime.of(t.get(0), t.get(2));
            }
            LocalDateTime useDate = LocalDateTime.of(date, time);
            FileData fileData = FileData.builder()
                    .storeName(rowObj.get(3))
                    .purpose(rowObj.get(4))
                    .participants(Integer.parseInt(rowObj.get(5).replaceAll("\\D", "")))
                    .cost(Integer.parseInt(rowObj.get(6).replaceAll("\\D", "")))
                    .paymentOption(rowObj.get(7))
                    .expenditure(rowObj.get(8))
                    .build();
            return fileData;
        } catch (Exception e) {
        }
        return null;
    }

    private static <E> E getLastElement(ArrayList<E> list) {
        if ((list != null) && (list.isEmpty() == false)) {
            int lastIdx = list.size() - 1;
            E lastElement = list.get(lastIdx);
            return lastElement;
        } else return null;
    }

    public static void main(String[] args) throws IOException {
        String path = "D:\\downloads\\";
        String testFileName = "11월.pdf";
//        String testFileName = "11월.pdf";
        String src = path + testFileName;
        PDDocument document = PDDocument.load(new File(src));
        PDFTextStripper stripper = new PDFTextStripper();
        stripper.setLineSeparator("\n");
        stripper.setWordSeparator("**");
        String text = stripper.getText(document);
        System.out.println(text);
        parsingString(text);
    }
}
