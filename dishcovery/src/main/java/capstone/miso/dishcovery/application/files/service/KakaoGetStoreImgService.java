package capstone.miso.dishcovery.application.files.service;

import capstone.miso.dishcovery.application.files.mapping.KakaoStoreDetailExtractor;
import capstone.miso.dishcovery.application.files.mapping.detail.KakaoStoreDetail;
import capstone.miso.dishcovery.application.files.mapping.detail.PhotoGroup;
import capstone.miso.dishcovery.application.files.mapping.detail.PhotoUrl;
import capstone.miso.dishcovery.domain.store.Store;
import capstone.miso.dishcovery.domain.store.repository.StoreRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.log4j.Log4j2;
import org.springframework.stereotype.Service;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
@Log4j2
@Service
@RequiredArgsConstructor
public class KakaoGetStoreImgService {
    private final KakaoStoreDetailExtractor kakaoStoreDetailExtractor;
    private final StoreRepository storeRepository;
    public String findStoreMainPhoto(Store store) throws IOException {
        KakaoStoreDetail kakaoStoreDetail = kakaoStoreDetailExtractor.getKakaoStoreDetail(store.getSid());
        if (kakaoStoreDetail.getPhoto() == null) {
            return null;
        }
        List<PhotoGroup> photoList = kakaoStoreDetail.getPhoto().getPhotoList();
        String replacePhoto = null;
        for (PhotoGroup photoGroup : photoList) {
            List<PhotoUrl> photoUrls = photoGroup.getList();
            for (PhotoUrl photoUrl : photoUrls) {
                String imageUrl = photoUrl.getOrgurl();
                String photoId = photoUrl.getPhotoid();

                if (replacePhoto == null){
                    replacePhoto = imageUrl;
                }
                if (photoId.equals("M")){
                    return imageUrl;
                }
            }
        }
        return replacePhoto;
    }

    public void saveStoreMainPhoto(){
//        List<Store> allStores = storeRepository.findAll();
        List<Store> allStores = storeRepository.findByMainImageUrlIsNull();
        log.info("전체 Store 개수: " + allStores.size());
        int count = 0;

        List<Store> stores = new ArrayList<>();
        for (Store store : allStores) {
            if (++count % 1000 == 0){
                log.info("save store main img count: " + count);
                storeRepository.saveAll(stores);
                stores = new ArrayList<>();
            }
            try {
                String mainPhoto = findStoreMainPhoto(store);
                // 가게의 메인 이미지에 변화가 있을 경우 저장
                if (!storeRepository.existsBySidAndMainImageUrl(store.getSid(), mainPhoto)){
                    store.setMainImageUrl(mainPhoto);
                    stores.add(store);
                }
            } catch (IOException ignored) {
            }
        }
        storeRepository.saveAll(stores);
    }
}
