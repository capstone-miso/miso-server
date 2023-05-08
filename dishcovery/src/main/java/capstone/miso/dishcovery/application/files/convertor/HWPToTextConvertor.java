package capstone.miso.dishcovery.application.files.convertor;

import kr.dogfoot.hwplib.object.HWPFile;
import kr.dogfoot.hwplib.reader.HWPReader;
import kr.dogfoot.hwplib.tool.textextractor.TextExtractMethod;
import kr.dogfoot.hwplib.tool.textextractor.TextExtractor;
import org.springframework.beans.factory.annotation.Value;

/**
 * author        : duckbill413
 * date          : 2023-03-21
 * description   :
 **/
@Deprecated
public class HWPToTextConvertor {
    @Value("${gong.file.path}")
    private String path;
    public static void main(String[] args) {
        String path = "D:\\downloads\\";
        String fileName = "★업무추진비 집행내역(2022.8).hwp";
        try {
            HWPFile hwpFile = HWPReader.fromFile(path + fileName);
            String hwpText = TextExtractor.extract(hwpFile, TextExtractMethod.InsertControlTextBetweenParagraphText);
//            System.out.println(hwpText);
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }
}
