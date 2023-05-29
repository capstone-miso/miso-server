package capstone.miso.dishcovery.application.files.service;

import capstone.miso.dishcovery.application.files.mapping.KakaoStoreDetailExtractor;
import capstone.miso.dishcovery.application.files.mapping.detail.KakaoStoreDetail;
import capstone.miso.dishcovery.application.files.mapping.detail.PhotoGroup;
import capstone.miso.dishcovery.application.files.mapping.detail.PhotoUrl;
import capstone.miso.dishcovery.domain.image.Image;
import capstone.miso.dishcovery.domain.image.repository.ImageRepository;
import capstone.miso.dishcovery.domain.store.Store;
import capstone.miso.dishcovery.domain.store.repository.StoreRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.io.IOException;
import java.util.List;

@Service
@RequiredArgsConstructor
public class KakaoGetStoreImgService {
    private final KakaoStoreDetailExtractor kakaoStoreDetailExtractor;
    private final StoreRepository storeRepository;
    private final ImageRepository imageRepository;

    public void saveStoreImages(Store store, int storeCount) throws IOException {
        KakaoStoreDetail kakaoStoreDetail = kakaoStoreDetailExtractor.getKakaoStoreDetail(store.getSid());
        List<PhotoGroup> photoList = kakaoStoreDetail.getPhoto().getPhotoList();

        int count = 0;
        for (PhotoGroup photoGroup : photoList) {
            List<PhotoUrl> photoUrls = photoGroup.getList();
            for (PhotoUrl photoUrl : photoUrls) {
                String imageUrl = photoUrl.getOrgurl();
                String photoId = photoUrl.getPhotoid();

                // 이미지가 이미 저장되어 있는 경우 스킵
                if (imageRepository.existsByImageUrlAndPhotoId(imageUrl, photoId)) {
                    continue;
                }

                Image image = Image.builder()
                        .imageUrl(imageUrl)
                        .photoId(photoId)
                        .store(store)
                        .build();
                store.getImages().add(image);
                count++;

                if (count > storeCount) {
                    break;
                }
            }
            if (count > storeCount) {
                break;
            }
        }
        // 이미지 저장 cascade 사용
        storeRepository.save(store);
    }

    public void saveAllStoreImages() {
        List<Store> stores = storeRepository.findAll();
        for (Store store : stores) {
            try {
                saveStoreImages(store, 5);
            } catch (Exception ignored) {

            }
        }
    }
}
