package capstone.miso.dishcovery.application.files.search;

import capstone.miso.dishcovery.application.files.FileData;
import capstone.miso.dishcovery.application.files.Files;
import capstone.miso.dishcovery.application.files.convertor.FileConvertor;
import lombok.RequiredArgsConstructor;
import org.jsoup.Connection;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;
import org.springframework.stereotype.Component;

import java.io.IOException;
import java.net.URI;
import java.net.URISyntaxException;
import java.util.ArrayList;
import java.util.List;

@Component
@RequiredArgsConstructor
public class SeoulOfficerFileDataComp implements FileConvertor {
    private final static String SECTOR_NAME = "서울";

    @Override
    public List<FileData> parseFileToFileData(Files files) {
        try {
            URI uri = new URI(files.getFileUrl());
            Connection connect = Jsoup.connect(uri.toString());
            Document document = connect.get();
            Elements elements = document.select("#scroll-content1 > table > tbody > tr");
            List<FileData> fileDataList = new ArrayList<>();
            String departure = null;
            for (Element element : elements) {
                Elements selected = element.select("td");
                try {

                    departure = selected.get(1).text();
                    String datetime = selected.get(2).text();
                    String store = selected.get(3).text();
                    String purpose = selected.get(4).text();
                    String cost = selected.get(5).text();
                    String participants = selected.get(6).text();
                    String paymentOption = selected.get(7).text();

                    // 가게명, 주소 분리
                    int end = store.lastIndexOf(')') != -1 ? store.lastIndexOf(')') : store.length();
                    int start = store.lastIndexOf('(') != -1 ? store.lastIndexOf('(') : store.indexOf(' ');

                    String storeName = store.substring(0, start).trim();
                    String address = store.substring(start + 1, end).trim();

                    String[] datetimeParts = datetime.split(" ");
                    FileData fileData = FileData.builder()
                            .date(datetimeParts[0])
                            .time(datetimeParts[1])
                            .storeName(storeName)
                            .storeAddress(address)
                            .purpose(purpose)
                            .participants(participants)
                            .cost(cost)
                            .paymentOption(paymentOption)
                            .region(SECTOR_NAME)
                            .files(files)
                            .build();

                    if (fileData != null)
                        fileDataList.add(fileData);
                } catch (Exception ignored) {
                }
            }

            files.setDepartment(departure);
            files.getFileDataList().addAll(fileDataList);
            return fileDataList;
        } catch (IOException | URISyntaxException e) {
            e.printStackTrace();
            throw new RuntimeException(e);
        }
    }
}