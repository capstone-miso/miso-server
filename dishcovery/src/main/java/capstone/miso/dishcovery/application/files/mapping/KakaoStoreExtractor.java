package capstone.miso.dishcovery.application.files.mapping;

import capstone.miso.dishcovery.application.files.FileData;
import capstone.miso.dishcovery.application.files.mapping.address.AddressDocument;
import capstone.miso.dishcovery.application.files.mapping.address.KakaoAddressSearchDTO;
import capstone.miso.dishcovery.application.files.mapping.search.KakaoStoreSearchDTO;
import capstone.miso.dishcovery.application.files.mapping.search.StoreDocument;
import capstone.miso.dishcovery.application.files.repository.FileDataRepository;
import capstone.miso.dishcovery.domain.department.Department;
import capstone.miso.dishcovery.domain.department.DepartmentRepository;
import capstone.miso.dishcovery.domain.store.Store;
import capstone.miso.dishcovery.domain.store.repository.StoreRepository;
import com.google.gson.Gson;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import lombok.extern.log4j.Log4j2;
import org.jsoup.Connection;
import org.jsoup.Jsoup;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;
import org.springframework.web.util.UriComponentsBuilder;

import java.io.IOException;
import java.net.URI;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;
import java.util.Optional;

@Log4j2
@Component
@RequiredArgsConstructor
public class KakaoStoreExtractor {
    private final FileDataRepository fileDataRepository;
    private final StoreRepository storeRepository;
    private final DepartmentRepository departmentRepository;
    private final static String KAKAO = "https://dapi.kakao.com/v2/local/search";
    @Value("${spring.security.oauth2.client.registration.kakao.client-id}")
    private String KAKAO_CLIENT_ID;
    private final static List<String> EXTRACTION_GROUP = List.of("음식점", "카페");
    private Double[] findKakaoCoordinates(String address) throws IOException {
        URI uri = UriComponentsBuilder.fromUriString(KAKAO)
                .path("/address")
                .build().toUri();
        Connection connect = Jsoup.connect(uri.toString())
                .data("query", address)
                .ignoreContentType(true);
        connect.header("Host", "dapi.kakao.com");
        connect.header("Authorization", "KakaoAK " + KAKAO_CLIENT_ID);
        String json = connect.execute().body();
        Gson gson = new Gson();
        KakaoAddressSearchDTO kakaoAddressSearchDTO = gson.fromJson(json, KakaoAddressSearchDTO.class);
        List<AddressDocument> documents = kakaoAddressSearchDTO.getDocuments();

        Double x = null;
        Double y = null;
        if (documents.size() > 0) {
            AddressDocument document = documents.get(0);
            x = document.getX();
            y = document.getY();
        }
        return new Double[]{x, y};
    }

    private Store findKakaoStore(String query, Double x, Double y) throws Exception {
        URI uri = UriComponentsBuilder.fromUriString(KAKAO)
                .path("/keyword")
                .build().toUri();
        Connection connect = Jsoup.connect(uri.toString())
                .data("query", query)
                .data("size", "1")
                .ignoreContentType(true);
        if (x != null && y != null) {
            connect.data("x", String.valueOf(x)).data("y", String.valueOf(y));
        }
        connect.header("Host", "dapi.kakao.com");
        connect.header("Authorization", "KakaoAK " + KAKAO_CLIENT_ID);
        String json = connect.execute().body();
        Gson gson = new Gson();
        KakaoStoreSearchDTO kakaoStoreSearchDTO = gson.fromJson(json, KakaoStoreSearchDTO.class);
        List<StoreDocument> documents = kakaoStoreSearchDTO.getDocuments();
        if (documents != null && documents.size() >= 1) {
            StoreDocument d = documents.get(0);
            if (!EXTRACTION_GROUP.contains(d.getCategoryGroupName())){
                return null;
            }
            return Store.builder()
                    .sid(d.getId())
                    .name(d.getPlaceName())
                    .lat(d.getY())
                    .lon(d.getX())
                    .address(d.getAddressName())
                    .category(d.getCategoryName())
                    .categoryKey(d.getCategoryGroupName())
                    .phone(d.getPhone())
                    .isExtracted(1)
                    .build();
        }
        return null;
    }
    @Transactional
    public Store findStore(FileData fileData) throws Exception {
        // 파일 데이터에 주소가 없는 경우 (부서로 검색)
        String storeName = Objects.requireNonNull(fileData.getStoreName());
        Double lon = null;
        Double lat = null;
        if (fileData.getStoreAddress() == null) {
            String fileDepartment = fileData.getFiles().getDepartment();
            Optional<Department> department = departmentRepository.findById(fileDepartment);
            if (department.isPresent()) {
                Department d = department.get();
                lat = d.getLat();
                lon = d.getLon();
            } else {
                Department d = departmentRepository.findById(fileData.getRegion()).orElseThrow(RuntimeException::new);
                lat = d.getLat();
                lon = d.getLon();
            }
        } // 파일 데이터에 주소가 있는 경우
        else {
            Double[] coordinates = findKakaoCoordinates(fileData.getStoreAddress());
            if (coordinates.length == 2) {
                lon = coordinates[0];
                lat = coordinates[1];
            }
        }
        return findKakaoStore(storeName, lon, lat);
    }
//    @Transactional
    public void storeExtraction(List<FileData> fileData) {
        int count = 0;
        int matched = 0;

        List<FileData> saveFileData = new ArrayList<>();
        for (FileData fileDatum : fileData) {
            count++;
            if (count % 1000 == 0) {
                log.info("Matching... count: " + count + "  matched: " + matched + " Now fileDataId: " + fileDatum.getFid());
                fileDataRepository.saveAll(saveFileData); // 데이터를 1000개 단위로 저장
                saveFileData = new ArrayList<>();
            }
            try {
                Store store = findStore(fileDatum);
                if (store == null)
                    continue;
                store.setSector(fileDatum.getRegion());
                fileDatum.setStore(store);
                saveFileData.add(fileDatum);
                matched++;
            } catch (Exception ignored) {
            }
        }
        fileDataRepository.saveAll(saveFileData);
    }
}
