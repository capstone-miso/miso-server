package capstone.miso.dishcovery.application.files.download;

import capstone.miso.dishcovery.application.files.Files;
import lombok.extern.log4j.Log4j2;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

import java.io.*;
import java.net.HttpURLConnection;
import java.net.URL;
import java.time.LocalDate;
import java.util.UUID;

/**
 * author        : duckbill413
 * date          : 2023-03-20
 * description   :
 **/
@Log4j2
@Component
public class KWANGJINFileDownloader {
    @Value("${gong.file.path}")
    private String path;
    private final static int BUFFER_SIZE = 4096;

    public Files downloadFile(Files files) {
        try {
            URL url = new URL(files.getFileUrl());
            // 외부 URL에 요청하기
            HttpURLConnection conn = (HttpURLConnection) url.openConnection();
            conn.setRequestMethod("GET");
            conn.setRequestProperty("User-Agent", "Mozilla/5.0 (Windows NT 10.0; Win64; x64) AppleWebKit/537.36 (KHTML, like Gecko) Chrome/110.0.0.0 Whale/3.19.166.16 Safari/537.36");
//            conn.setRequestProperty("Host", "www.gwangjin.go.kr");
            conn.setRequestProperty("Accept-Language", "ko-KR,ko;q=0.9,en-US;q=0.8,en;q=0.7,ja;q=0.6");
            conn.setRequestProperty("Accept", "text/html,application/xhtml+xml,application/xml;q=0.9,image/avif,image/webp,image/apng,*/*;q=0.8,application/signed-exchange;v=b3;q=0.7");
            conn.connect();
            int responseCode = conn.getResponseCode();

            if (responseCode == HttpURLConnection.HTTP_OK) {
                String fileName = "";
                String fileFormat = "";
                String disposition = new String(conn.getHeaderField("Content-Disposition").getBytes("iso-8859-1"), "utf-8");
                // 파일 인코딩 확인
//                fileEncodingCheck(disposition);

                String contentType = conn.getContentType();

                String target = "filename=";
                int index = disposition.indexOf(target);
                if (index != -1) {
                    fileName = UUID.randomUUID() + "_" + disposition.substring(index + target.length() + 1);

                    int dotIndex = fileName.lastIndexOf(".");
                    if (dotIndex == -1){
                        throw new IllegalArgumentException("Invalid file name: " + fileName);
                    }

                    fileFormat = fileName.substring(dotIndex + 1);
                }

                InputStream is = conn.getInputStream();
                String outputDir = path;
                FileOutputStream fos = new FileOutputStream(new File(outputDir, fileName));

                int bytesRead;
                byte[] buffer = new byte[BUFFER_SIZE];
                while ((bytesRead = is.read(buffer)) != -1) {
                    fos.write(buffer, 0, bytesRead);
                }
                fos.close();
                is.close();

                files.setFileName(fileName);
                files.setFileFormat(fileFormat);
            }
            conn.disconnect();
            files.setFileDownloaded(true);
            return files;
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
                    log.info("[" + charSet[i] + "," + charSet[j] + "]" + new String(disposition.getBytes(charSet[i]), charSet[j]));
                } catch (UnsupportedEncodingException e) {
                    e.printStackTrace();
                }
            }
        }
    }
}