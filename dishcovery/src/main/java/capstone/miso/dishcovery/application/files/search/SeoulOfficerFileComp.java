package capstone.miso.dishcovery.application.files.search;

import capstone.miso.dishcovery.application.files.Files;
import lombok.RequiredArgsConstructor;
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
import java.net.URL;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

@Log4j2
@Component
@RequiredArgsConstructor
public class SeoulOfficerFileComp {
    private final static String SEOUL = "https://opengov.seoul.go.kr";
    private final static String SECTOR_NAME = "서울";
    public List<Files> findFiles(Integer year, Integer month) {
        URI pageUri = UriComponentsBuilder.fromUriString(SEOUL)
                .path("/expense")
                .path("/list")
                .queryParam("items_per_page", 5000)
                .queryParam("ym[year]", year)
                .queryParam("ym[month]", month)
                .build().toUri();
        try {
            URL url = pageUri.toURL();

            Connection connect = Jsoup.connect(url.toString());
            connect.header("Host", "opengov.seoul.go.kr");
            connect.header("Accept", "text/html,application/xhtml+xml,application/xml;q=0.9,image/avif,image/webp,image/apng,*/*;q=0.8,application/signed-exchange;v=b3;q=0.7");
            Document document = connect.get();
            Elements elements = document.select("#content > div > div.view-content > div > table > tbody > tr");

            List<Files> files = new ArrayList<>();
            for (Element element : elements) {
                try {
                    // Extract individual data points
                    String dataTitle = Objects.requireNonNull(element.selectFirst(".data-title")).text();
                    String dataDate = Objects.requireNonNull(element.selectFirst(".data-date")).text();
                    String href = SEOUL + element.select("a").attr("href");
                    // Parse to File
                    Files file = Files.builder()
                            .fileName(dataTitle)
                            .fileUploaded(LocalDate.parse(dataDate))
                            .fileUrl(href)
                            .region(SECTOR_NAME)
                            .fileFormat("html")
                            .fileDownloaded(true)
                            .build();
                    files.add(file);
                } catch (NullPointerException ignored) {
                }
            }
            return files;
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }
}
