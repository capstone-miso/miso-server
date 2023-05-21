package capstone.miso.dishcovery.domain.store.service;

import capstone.miso.dishcovery.application.files.repository.FileDataJdbcRepository;
import capstone.miso.dishcovery.domain.image.Image;
import capstone.miso.dishcovery.domain.keyword.Keyword;
import capstone.miso.dishcovery.domain.keyword.KeywordData;
import capstone.miso.dishcovery.domain.menu.dto.MenuDTO;
import capstone.miso.dishcovery.domain.store.Store;
import capstone.miso.dishcovery.domain.store.StoreOffInfo;
import capstone.miso.dishcovery.domain.store.StoreOnInfo;
import capstone.miso.dishcovery.domain.store.dto.StoreDetailDTO;
import capstone.miso.dishcovery.domain.store.dto.StoreSearchCondition;
import capstone.miso.dishcovery.domain.store.dto.StoreShortDTO;
import capstone.miso.dishcovery.domain.store.repository.StoreRepository;
import capstone.miso.dishcovery.dto.PageRequestDTO;
import capstone.miso.dishcovery.dto.PageResponseDTO;
import lombok.RequiredArgsConstructor;
import lombok.extern.log4j.Log4j2;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.*;

/**
 * author        : duckbill413
 * date          : 2023-04-27
 * description   :
 **/
@Log4j2
@Service
@RequiredArgsConstructor
public class StoreServiceImpl implements StoreService {
    private final StoreRepository storeRepository;
    private final FileDataJdbcRepository fileDataJdbcRepository;
    @Override
    public PageResponseDTO<StoreShortDTO> listWithStoreShort(PageRequestDTO pageRequestDTO) {
        StoreSearchCondition condition = StoreSearchCondition.builder()
                .storeName(pageRequestDTO.getStoreName())
                .category(pageRequestDTO.getCategory())
                .keyword(pageRequestDTO.getKeyword())
                .sector(pageRequestDTO.getSector())
                .lat(pageRequestDTO.getLat())
                .lon(pageRequestDTO.getLon())
                .multi(pageRequestDTO.getMulti()).build();
        condition.setStoreId(pageRequestDTO.getStoreId());

        Pageable pageable = pageRequestDTO.getPageable("updatedAt.desc");

        Page<StoreShortDTO> result = storeRepository.searchAllStoreShort(condition, pageable);

        return PageResponseDTO.<StoreShortDTO>builder()
                .pageRequestDTO(pageRequestDTO)
                .dtoList(result.getContent())
                .total((int) result.getTotalElements())
                .build();
    }

    @Override
    @Transactional
    public StoreDetailDTO getStoreDetail(Long sid) {
        Optional<Store> result = storeRepository.findById(sid);
        Store store = result.orElseThrow(RuntimeException::new);
        StoreDetailDTO storeDetailDTO = StoreDetailDTO.builder()
                .id(store.getSid())
                .storeName(store.getName())
                .lat(store.getLat())
                .lon(store.getLon())
                .phone(store.getPhone())
                .address(store.getAddress())
                .category(store.getCategory())
                .sector(store.getSector())
                .build();

        List<String> onInfo = store.getStoreOnInfos().stream().map(StoreOnInfo::getInfo).toList();
        List<String> offInfo = store.getStoreOffInfos().stream().map(StoreOffInfo::getInfo).toList();
        List<MenuDTO> menus = store.getMenus().stream().map(menu -> new MenuDTO(menu.getMid(), menu.getName(), menu.getCost(), menu.getCost(), menu.getMenuImg())).toList();
        List<String> images = store.getImages().stream().map(Image::getImageUrl).toList();
        String mainImage = store.getImages().stream().filter(image1 -> image1.getPhotoId().equals("M")).findFirst().orElse(
                store.getImages().stream().findFirst().orElse(new Image())
        ).getImageUrl();
        // 매장 키워드 조회
        List<Keyword> storeKeywords = store.getKeywords();
        List<String> keywords = storeKeywords.stream().map(keyword -> keyword.getKeyword().toString()).toList();

        storeDetailDTO.setOnInfo(onInfo);
        storeDetailDTO.setOffInfo(offInfo);
        storeDetailDTO.setMenus(menus);
        storeDetailDTO.setMainImage(mainImage);
        storeDetailDTO.setImages(images);
        storeDetailDTO.setKeywords(keywords);
        storeDetailDTO.setVisitedTime(fileDataJdbcRepository.getStoreTimeTableDTO(sid));

        return storeDetailDTO;
    }
}
