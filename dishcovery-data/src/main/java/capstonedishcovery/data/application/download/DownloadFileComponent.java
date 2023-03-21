package capstonedishcovery.data.application.download;

import capstonedishcovery.data.application.convertor.PDFToTextConvertor;
import capstonedishcovery.data.application.files.service.FileListService;
import lombok.RequiredArgsConstructor;
import lombok.extern.log4j.Log4j2;
import org.springframework.stereotype.Component;

import java.io.*;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;
import java.time.LocalDate;
import java.util.UUID;

/**
 * author        : duckbill413
 * date          : 2023-03-20
 * description   :
 **/
@Component
@RequiredArgsConstructor
@Log4j2
public class DownloadFileComponent {
    private static final int BUFFER_SIZE = 4096;
    private final FileListService fileListService;

    public void saveFile() {
//        https://www.gwangjin.go.kr/portal/cmmn/file/fileDown.do?menuNo=201646&atchFileId=3348a0b16be7a8571d0c268c27c6ea38595959354d4af99528e3ee01bcc640d2&fileSn=1
//        https://www.gwangjin.go.kr/portal/cmmn/file/fileDown.do?menuNo=201646&atchFileId=3348a0b16be7a8571d0c268c27c6ea3806cffad79ee16d5feb1a66debb1217fe&fileSn=1
//        https://www.gwangjin.go.kr/portal/cmmn/file/fileDown.do?menuNo=201646&atchFileId=3348a0b16be7a8571d0c268c27c6ea38820f522fcfc0b8daca556e708f1d1479&fileSn=1
//        https://www.gwangjin.go.kr/portal/cmmn/file/fileDown.do?menuNo=201646&atchFileId=3348a0b16be7a8571d0c268c27c6ea38820f522fcfc0b8daca556e708f1d1479&fileSn=2

        String source = "https://www.gwangjin.go.kr/portal/cmmn/file/fileDown.do";
        try {
            String fileUrl = "https://www.gwangjin.go.kr/portal/cmmn/file/fileDown.do?menuNo=201646&atchFileId=3348a0b16be7a8571d0c268c27c6ea38bd0ac87c58096974266b15946c5361aa&fileSn=1";
            // 외부 URL에 요청하기
            URL url = new URL(fileUrl);
            HttpURLConnection conn = (HttpURLConnection) url.openConnection();
            conn.setRequestMethod("GET");
            conn.setRequestProperty("User-Agent", "Mozilla/5.0 (Windows NT 10.0; Win64; x64) AppleWebKit/537.36 (KHTML, like Gecko) Chrome/110.0.0.0 Whale/3.19.166.16 Safari/537.36");
            conn.setRequestProperty("Host", "www.gwangjin.go.kr");
            conn.setRequestProperty("Accept-Language", "ko-KR,ko;q=0.9,en-US;q=0.8,en;q=0.7,ja;q=0.6");
            conn.setRequestProperty("Accept", "text/html,application/xhtml+xml,application/xml;q=0.9,image/avif,image/webp,image/apng,*/*;q=0.8,application/signed-exchange;v=b3;q=0.7");
            conn.connect();
            int responseCode = conn.getResponseCode();

            if (responseCode == HttpURLConnection.HTTP_OK) {
                String fileName = "";
                String disposition = new String(conn.getHeaderField("Content-Disposition").getBytes("iso-8859-1"), "utf-8");
                // 파일 인코딩 확인
//                fileEncodingCheck(disposition);

                String contentType = conn.getContentType();

                if (disposition != null) {
                    String target = "filename=";
                    int index = disposition.indexOf(target);
                    if (index != -1) {
                        fileName = disposition.substring(index + target.length() + 1);
                    }
                } else {
                    fileName = UUID.randomUUID().toString() + LocalDate.now();
                }

                System.out.println("Content-Type: " + contentType);
                System.out.println("Content-Disposition: " + disposition);
                System.out.println("fileName: " + fileName);

                InputStream is = conn.getInputStream();
                String outputDir = "D:/downloads";
                FileOutputStream fos = new FileOutputStream(new File(outputDir, fileName));

                int bytesRead;
                byte[] buffer = new byte[BUFFER_SIZE];
                while ((bytesRead = is.read(buffer)) != -1) {
                    fos.write(buffer, 0, bytesRead);
                }
                fos.close();
                is.close();

                Long fileId = fileListService.saveDownloadedFileInfo(fileName, fileUrl).getFid();
                log.info(String.format("FILE DOWNLOADED: %s", fileName));
                try {
                    PDFToTextConvertor.convertPDFToText(fileName);
                    fileListService.changeFileConvertedStatus(fileId);
                    log.info("FILE CONVERTED SUCCESS");
                } catch (IOException e) {
                    log.info("FILE CONVERT FAILED");
                    throw new RuntimeException(e);
                }
            }
            conn.disconnect();
        } catch (IOException e) {
            log.error("FILE DOWNLOAD FAILED");
            e.printStackTrace();
            throw new RuntimeException(e);
        }
    }

    private static void fileEncodingCheck(String disposition) {
        String[] charSet = {"utf-8", "euc-kr", "ksc5601", "iso-8859-1", "x-windows-949"};
        for (int i = 0; i < charSet.length; i++) {
            for (int j = 0; j < charSet.length; j++) {
                try {
                    System.out.println("[" + charSet[i] + "," + charSet[j] + "]" + new String(disposition.getBytes(charSet[i]), charSet[j]));
                } catch (UnsupportedEncodingException e) {
                    e.printStackTrace();
                }
            }
        }
    }
}