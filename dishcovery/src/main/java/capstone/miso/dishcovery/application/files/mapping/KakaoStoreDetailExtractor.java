package capstone.miso.dishcovery.application.files.mapping;

import capstone.miso.dishcovery.application.files.dto.Findway;
import capstone.miso.dishcovery.application.files.dto.Menu;
import capstone.miso.dishcovery.application.files.dto.MenuInfo;
import capstone.miso.dishcovery.application.files.dto.Photo;
import capstone.miso.dishcovery.application.files.dto.*;
import capstone.miso.dishcovery.application.files.mapping.detail.*;
import capstone.miso.dishcovery.domain.store.Store;
import com.google.gson.Gson;
import lombok.RequiredArgsConstructor;
import lombok.extern.log4j.Log4j2;
import org.jsoup.Connection;
import org.jsoup.Jsoup;
import org.springframework.stereotype.Component;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

@Log4j2
@Component
@RequiredArgsConstructor
public class KakaoStoreDetailExtractor {
    private final static String KAKAO = "https://place.map.kakao.com/main/v/";

    public KakaoStoreDetail getKakaoStoreDetail(Long storeId) throws IOException {
        Connection connect = Jsoup.connect(KAKAO + storeId);
        connect.header("Content-Type", "application/json");
        connect.header("charset", "UTF-8");
        connect.header("Accept", "*/*");
        connect.ignoreContentType(true);

        String json = connect.execute().body();
        Gson gson = new Gson();
        return gson.fromJson(json, KakaoStoreDetail.class);
    }

    public List<Photo> getStoreAllPhotos(Store store) throws IOException {
        KakaoStoreDetail kakaoStoreDetail = getKakaoStoreDetail(store.getSid());
        if (kakaoStoreDetail.getPhoto() == null) {
            return null;
        }
        List<PhotoGroup> photoList = kakaoStoreDetail.getPhoto().getPhotoList();

        List<Photo> photos = new ArrayList<>();
        for (PhotoGroup photoGroup : photoList) {
            for (PhotoUrl photoUrl : photoGroup.getList()) {
                String imageUrl = photoUrl.getOrgurl();
                String photoId = photoUrl.getPhotoid();

                photos.add(new Photo(photoId, imageUrl));
            }
        }
        return photos;
    }

    public KakaoStoreDetailDTO getKakaoStoreDetailDTO(Long storeId) throws IOException {
        KakaoStoreDetail kakaoStoreDetail = getKakaoStoreDetail(storeId);
        String mainphotourl = kakaoStoreDetail.getBasicInfo().getMainphotourl();
        List<String> openHour = new ArrayList<>();
        List<String> offDays = new ArrayList<>();
        if (kakaoStoreDetail.getBasicInfo().getOpenHour() != null) {
            Optional<List<Period>> periodListOptional = Optional.ofNullable(kakaoStoreDetail.getBasicInfo().getOpenHour().getPeriodList());
            if (periodListOptional.isPresent()) {
                List<Period> periodList = periodListOptional.get();
                // 영업시간
                periodList.forEach(period -> {
                    StringBuilder sb = new StringBuilder();
                    String periodName = period.getPeriodName();
                    sb.append(periodName).append(": ");
                    period.getTimeList().forEach(time -> {
                        sb.append(time.getDayOfWeek())
                                .append(" ")
                                .append(time.getTimeSE())
                                .append(" ");
                    });
                    openHour.add(sb.toString());
                });
            }
            // 휴무시간
            List<Holiday> offdayList = Optional.ofNullable(kakaoStoreDetail.getBasicInfo().getOpenHour().getOffdayList()).orElse(new ArrayList<>());
            offdayList.forEach(holiday -> {
                offDays.add(holiday.getWeekAndDay());
            });
        }
        // 태그
        List<String> tags = Optional.ofNullable(kakaoStoreDetail.getBasicInfo().getTags()).orElse(new ArrayList<>());
        Subway subwayDTO = null;
        // 지하철
        if (kakaoStoreDetail.getFindway().getSubwayStop() != null) {
            Optional<SubwayStop> subwayOptional = kakaoStoreDetail.getFindway().getSubwayStop().stream().findFirst();
            if (subwayOptional.isPresent()) {
                SubwayStop subway = subwayOptional.get();
                subwayDTO = new Subway(subway.getStationSimpleName(), subway.getExitNum(), subway.getToExitDistance());
            }
        }
        Bus busDTO = null;
        if (kakaoStoreDetail.getFindway().getBusstop() != null) {
            Optional<Busstop> busstopOptional = kakaoStoreDetail.getFindway().getBusstop().stream().findFirst();
            if (busstopOptional.isPresent()) {
                Busstop busstop = busstopOptional.get();
                busDTO = new Bus(busstop.getBusStopName(), busstop.getToBusstopDistance(),
                        busstop.getBusInfo().stream().map(BusInfo::getBusNames).toList());
            }
        }
        // 메뉴
        long menucount = 0L;
        List<Menu> menus = new ArrayList<>();
        if (kakaoStoreDetail.getMenuInfo() != null) {
            menucount = kakaoStoreDetail.getMenuInfo().getMenucount();
            if (kakaoStoreDetail.getMenuInfo().getMenuList() != null) {
                kakaoStoreDetail.getMenuInfo().getMenuList().forEach(m -> {
                    String price = m.getPrice();
                    String menu = m.getMenu();
                    String description = m.getDesc();
                    String img = m.getImg();

                    menus.add(Menu.builder()
                            .price(price)
                            .menu(menu)
                            .description(description)
                            .imgUrl(img)
                            .build());
                });
            }
        }
        // 매장 사진
        List<Photo> photos = new ArrayList<>();
        if (kakaoStoreDetail.getPhoto() != null) {
            if (kakaoStoreDetail.getPhoto().getPhotoList() != null) {
                if (kakaoStoreDetail.getPhoto().getPhotoList().size() > 0) {
                    PhotoGroup photoGroup = kakaoStoreDetail.getPhoto().getPhotoList().get(0);
                    photoGroup.getList().forEach(photoUrl -> {
                        photos.add(new Photo(photoUrl.getPhotoid(), photoUrl.getOrgurl()));
                    });
                }
            }
        }

        return KakaoStoreDetailDTO.builder()
                .mainPhotoUrl(mainphotourl)
                .openHour(openHour)
                .offDays(offDays)
                .tags(tags)
                .findway(Findway.builder().bus(busDTO).subway(subwayDTO).build())
                .menuInfo(new MenuInfo(menucount, menus))
                .photoList(photos)
                .build();
    }
}
