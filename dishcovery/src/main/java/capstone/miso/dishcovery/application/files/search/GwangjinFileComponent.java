package capstone.miso.dishcovery.application.files.search;

import capstone.miso.dishcovery.application.files.Files;
import lombok.extern.log4j.Log4j2;
import org.jsoup.Connection;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;
import org.springframework.stereotype.Component;
import org.springframework.web.util.UriComponentsBuilder;

import java.io.IOException;
import java.net.URI;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

/**
 * author        : duckbill413
 * date          : 2023-04-11
 * description   :
 **/
@Log4j2
@Component
public class GwangjinFileComponent implements OfficialDataComp {
    public List<Files> loadFile(Long page, String sdate, String edate) {
        sdate = Objects.isNull(sdate) ? "" : sdate;
        edate = Objects.isNull(edate) ? "" : edate;

        // 페이지 번호가 지정된 경우
        if (!Objects.isNull(page)) {
            try {
                return getFileData(page, sdate, edate);
            } catch (IOException e) {
                log.error("File search failed");
                e.printStackTrace();
            }
        }

        List<Files> files = new ArrayList<>();
        // 페이지 번호가 지정되지 않은 경우 전체 검색
        int totalPage = 0;
        try {
            totalPage = getTotalPage(sdate, edate);
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
        for (long i = 1; i <= totalPage; i++) {
            List<Files> pageFiles = new ArrayList<>();
            try {
                pageFiles = getFileData(i, sdate, edate);
            } catch (IOException e) {
                log.error("File search failed");
                e.printStackTrace();
            }
            files.addAll(pageFiles);
        }

        return files;
    }

    private int getTotalPage(String sdate, String edate) throws IOException {
        URI uri = UriComponentsBuilder.fromUriString("https://www.gwangjin.go.kr/")
                .path("/portal/bbs/B0000027/list.do")
                .queryParam("menuNo", 201646)
                .queryParam("sdate", sdate)
                .queryParam("edate", edate)
                .build()
                .toUri();

        Connection conn = Jsoup.connect(uri.toString());
        Document document = conn.get();
        int totalDocCnt = Integer.parseInt(document.selectXpath("//*[@id=\"content\"]/div[1]/strong").get(0).text());
        return (int) (Math.ceil(totalDocCnt / 10.0));
    }

    private List<Files> getFileData(Long pageIndex, String sdate, String edate) throws IOException {
        URI uri = UriComponentsBuilder.fromUriString("https://www.gwangjin.go.kr/")
                .path("/portal/bbs/B0000027/list.do")
                .queryParam("menuNo", 201646)
                .queryParam("pageIndex", pageIndex)
                .queryParam("sdate", sdate)
                .queryParam("edate", edate)
                .build()
                .toUri();

        Connection conn = Jsoup.connect(uri.toString());
        Document document = conn.get();

        //        files.forEach(System.out::println);
        return extractFileData(document);
    }

    private static List<Files> extractFileData(Document document) {
        List<Files> files = new ArrayList<>();

        Elements elements = document.select(".bdList li");
        for (Element element : elements) {
            try {
                String dept = element.select(".dept").text();
                String date = element.select(".date").text();

                Elements hr = element.select(".fileList a:not(.docview)");
                Elements tit = element.select(".fileList a:not(.docview)");
                int size = hr.size();
                for (int i = 0; i < size; i++) {
                    String href = hr.get(i).attr("href");
                    String title = tit.get(i).attr("title");

                    Files file = Files.builder()
                            .region("광진구")
                            .department(dept)
                            .fileName(title)
                            .fileUrl("https://www.gwangjin.go.kr/" + href)
                            .fileUploaded(LocalDate.parse(date))
                            .converted(false)
                            .build();

                    files.add(file);
                }
            } catch (Exception e) {
            }
        }
        return files;
    }
}
