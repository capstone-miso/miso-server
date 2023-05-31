package capstone.miso.dishcovery.domain.parkinglot.component;

import capstone.miso.dishcovery.domain.parkinglot.Parkinglot;
import capstone.miso.dishcovery.domain.parkinglot.dto.ParkinglotDTO;
import capstone.miso.dishcovery.domain.parkinglot.dto.Row;
import capstone.miso.dishcovery.domain.parkinglot.repository.ParkinglotRepository;
import com.google.gson.Gson;
import lombok.RequiredArgsConstructor;
import lombok.extern.log4j.Log4j2;
import org.jsoup.Connection;
import org.jsoup.Jsoup;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;
import org.springframework.web.util.UriComponentsBuilder;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

@Log4j2
@Component
@RequiredArgsConstructor
public class ParkinglotCollector {
    private final ParkinglotRepository parkinglotRepository;
    @Value("${key.seoul.secret}")
    private String SEOUL_KEY;
    private final static String SEOUL = "http://openapi.seoul.go.kr:8088/";

    public long collectSeoulParkinglot(long page) throws IOException {
        String parkUri = UriComponentsBuilder.fromUriString(SEOUL)
                .path(SEOUL_KEY)
                .path("/json")
                .path("/GetParkInfo")
                .path("/" + page)
                .path("/500/")
                .build().toUri().toString();

        Connection connection = Jsoup.connect(parkUri).ignoreContentType(true);
        String json = connection.execute().body();
        Gson gson = new Gson();
        ParkinglotDTO parkinglotDTO = gson.fromJson(json, ParkinglotDTO.class);

        List<Parkinglot> parkinglotList = new ArrayList<>();
        if (parkinglotDTO.getGetParkInfo() == null){
            return -1L;
        }
        Integer totalCount = parkinglotDTO.getGetParkInfo().getListTotalCount();
        System.out.println(page + "  " + totalCount);
        List<Row> row = parkinglotDTO.getGetParkInfo().getRow();
        for (Row r : row) {
            Parkinglot parkinglot = Parkinglot.builder()
                    .parkingCode(r.getParkingCode())
                    .parkingName(r.getParkingName())
                    .address(r.getAddr())
                    .phone(r.getTel())
                    .operationRule(r.getOperationRule())
                    .parkingType(r.getParkingType())
                    .lat(r.getLat())
                    .lon(r.getLng())
                    .build();
            parkinglotList.add(parkinglot);
        }
        System.out.println("save size: " + parkinglotList.size());
        parkinglotRepository.saveAll(parkinglotList);

        long pageSize = page * 500;
        if (pageSize < totalCount){
            return page + 1L;
        } else {
            return -1L;
        }
    }

    public void collectParkinglot(){
        long page = 1;
        while (page > 0){
            try {
                page = collectSeoulParkinglot(page);
            } catch (IOException e) {
                throw new RuntimeException(e);
            }
        }
    }
}
