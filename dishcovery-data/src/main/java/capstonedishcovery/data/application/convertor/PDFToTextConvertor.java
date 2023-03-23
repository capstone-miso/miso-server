package capstonedishcovery.data.application.convertor;

import capstonedishcovery.data.application.files.FileData;
import lombok.extern.log4j.Log4j2;
import org.apache.pdfbox.pdmodel.PDDocument;
import org.apache.pdfbox.text.PDFTextStripper;

import java.io.File;
import java.io.IOException;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;

/**
 * author        : duckbill413
 * date          : 2023-03-20
 * description   :
 **/
public class PDFToTextConvertor {
    public static void convertPDFToText(String fileName) throws IOException {
        String path = "D:\\downloads\\";
        String testFileName = "업무추진비 집행내역 작성서식(2023년 2월).pdf";
        String src = path + testFileName;
        PDDocument document = PDDocument.load(new File(src));
        PDFTextStripper stripper = new PDFTextStripper();
        stripper.setWordSeparator(", ");
        String text = stripper.getText(document);
        System.out.println(text);
        parseString(text);
    }
    private static void parseString(String source){
        String [] lines = source.split("\\n");
        List<List<String>> row = new ArrayList<>();
        for (int i = 0; i < lines.length; i++) {
            String[] s = lines[i].split(", ");
            List<String> ss = Arrays.stream(s).collect(Collectors.toList());;

            if (ss.size() == 10)
                ss.remove(0);
            row.add(ss);
        }

        List<List<String>> result = new ArrayList<>();
        String department = "";
        for (int i=0; i<row.size(); i++){
            List<String> rowObj = row.get(i);
            if (rowObj.size() == 1){
                if (rowObj.get(0).contains("집행내역")){
                    department = rowObj.get(0);
                }
                continue;
            }
            if (rowObj.size() >= 9){
                result.add(rowObj);
            }
        }

        for (List<String> model : result){
            FileData fileData = saveFileData(department, model);
            if (fileData != null)
                System.out.println(fileData);
        }
    }

    private static FileData saveFileData(String department, List<String> rowObj){
        try {
            List<Integer> d = Arrays.stream(rowObj.get(1).split("\\D{1,3}")).map(Integer::parseInt).collect(Collectors.toList());
            List<Integer> t = Arrays.stream(rowObj.get(2).split("\\D{1,3}")).map(Integer::parseInt).collect(Collectors.toList());

            LocalDate date = LocalDate.of(d.get(0), d.get(1), d.get(2));
            LocalTime time = null;
            if (t.size() >= 3){
                time = LocalTime.of(t.get(0), t.get(1), t.get(2));
            } else {
                time = LocalTime.of(t.get(0), t.get(2));
            }
            LocalDateTime useDate = LocalDateTime.of(date, time);
            FileData fileData = FileData.builder()
                    .name(rowObj.get(0))
                    .useDate(useDate)
                    .department(department)
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

    public static void main(String[] args) throws IOException {
        String path = "D:\\downloads\\";
        String testFileName = "업무추진비 집행내역 작성서식(2023년 2월).pdf";
        String src = path + testFileName;
        PDDocument document = PDDocument.load(new File(src));
        PDFTextStripper stripper = new PDFTextStripper();
        stripper.setWordSeparator(", ");
        String text = stripper.getText(document);
        System.out.println(text);
        parseString(text);
    }
}
