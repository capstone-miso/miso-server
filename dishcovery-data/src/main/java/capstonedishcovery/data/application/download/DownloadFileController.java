package capstonedishcovery.data.application.download;

import java.io.*;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.Map;
import java.util.UUID;

/**
 * author        : duckbill413
 * date          : 2023-03-20
 * description   :
 **/
public class DownloadFileController {
    public static void main(String[] args) {
        String fileUrl = "https://www.gwangjin.go.kr/portal/cmmn/file/fileDown.do?menuNo=200179&atchFileId=3348a0b16be7a8571d0c268c27c6ea3865da0265250a1a043c15ad8488ea19aa&fileSn=1";
        try {
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

                final int BUFFER_SIZE = 4096;
                int bytesRead;
                byte[] buffer = new byte[BUFFER_SIZE];
                while ((bytesRead = is.read(buffer)) != -1) {
                    fos.write(buffer, 0, bytesRead);
                }
                fos.close();
                is.close();
                System.out.println("FILE DOWNLOADED");
            }
            conn.disconnect();
        } catch (MalformedURLException e) {
            e.printStackTrace();
            throw new RuntimeException(e);
        } catch (IOException e) {
            e.printStackTrace();
            throw new RuntimeException(e);
        }
    }

    private static void fileEncodingCheck(String disposition) {
        String[] charSet = {"utf-8", "euc-kr", "ksc5601", "iso-8859-1", "x-windows-949"};
        for(int i = 0; i<charSet.length; i++){
            for(int j = 0; j<charSet.length; j++){
                try{
                    System.out.println("[" + charSet[i] + "," + charSet[j] + "]" + new String(disposition.getBytes(charSet[i]), charSet[j]));
                } catch (UnsupportedEncodingException e){
                    e.printStackTrace();
                }
            }
        }
    }
}