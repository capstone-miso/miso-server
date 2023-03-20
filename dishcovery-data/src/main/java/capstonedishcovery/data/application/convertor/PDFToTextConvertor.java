package capstonedishcovery.data.application.convertor;

import org.apache.pdfbox.pdmodel.PDDocument;
import org.apache.pdfbox.text.PDFTextStripper;

import java.io.File;
import java.io.IOException;

/**
 * author        : duckbill413
 * date          : 2023-03-20
 * description   :
 **/

public class PDFToTextConvertor {
    public static void main(String[] args) {
        try {
            String src = "D:\\downloads\\업무추진비 집행내역 작성서식(2023년 2월).pdf";
            PDDocument document = PDDocument.load(new File(src));
            PDFTextStripper stripper = new PDFTextStripper();
            String text = stripper.getText(document);
            System.out.println(text);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
