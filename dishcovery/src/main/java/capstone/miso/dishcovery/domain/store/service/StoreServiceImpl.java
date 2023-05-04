package capstone.miso.dishcovery.domain.store.service;

import capstone.miso.dishcovery.domain.menu.dto.MenuDTO;
import capstone.miso.dishcovery.domain.store.Store;
import capstone.miso.dishcovery.domain.store.dto.StoreDetailDTO;
import capstone.miso.dishcovery.domain.store.dto.StoreSearchCondition;
import capstone.miso.dishcovery.domain.store.dto.StoreShortDTO;
import capstone.miso.dishcovery.domain.store.repository.StoreRepository;
import capstone.miso.dishcovery.domain.image.Image;
import capstone.miso.dishcovery.dto.PageRequestDTO;
import capstone.miso.dishcovery.dto.PageResponseDTO;
import lombok.RequiredArgsConstructor;
import lombok.extern.log4j.Log4j2;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

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
    @Override
    public PageResponseDTO<StoreShortDTO> listWithStoreShort(PageRequestDTO pageRequestDTO) {
        StoreSearchCondition condition = new StoreSearchCondition(pageRequestDTO.getStoreId(), pageRequestDTO.getStoreName(),
                pageRequestDTO.getCategory(), pageRequestDTO.getKeyword(), pageRequestDTO.getSector(), pageRequestDTO.getLat(), pageRequestDTO.getLon(),
                pageRequestDTO.getMulti());
        Pageable pageable = pageRequestDTO.getPageable("sid.desc");

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
                .sid(store.getSid())
                .name(store.getName())
                .lat(store.getLat())
                .lon(store.getLon())
                .phone(store.getPhone())
                .address(store.getAddress())
                .category(store.getCategory())
                .sector(store.getSector())
                .build();

        List<String> onInfo = new ArrayList<>();
        List<String> offInfo = new ArrayList<>();
        List<String> keywords = new ArrayList<>();
        List<MenuDTO> menus = new ArrayList<>();
        List<String> images=new ArrayList<>();

        store.getStoreOnInfos().forEach(storeOnInfo -> onInfo.add(storeOnInfo.getInfo()));
        store.getStoreOffInfos().forEach(storeOffInfo -> offInfo.add(storeOffInfo.getInfo()));
        store.getKeywords().forEach(keyword -> keywords.add(keyword.getKeywordKeys()));
        store.getMenus().forEach(menu -> menus.add(new MenuDTO(menu.getMid(), menu.getName(), menu.getCost(), menu.getCost(), menu.getMenuImg())));
        List<Image> storeImages= store.getImages();
        String mainImage="";
        for (Image storeImage : storeImages) {
            if (storeImage.getPhotoId().equals("M")){
                mainImage=storeImage.getImageUrl();
            }
            images.add(storeImage.getImageUrl());
        }

        storeDetailDTO.setOnInfo(onInfo);
        storeDetailDTO.setOffInfo(offInfo);
        storeDetailDTO.setKeywords(keywords);
        storeDetailDTO.setMenus(menus);
        storeDetailDTO.setMainImage(mainImage);
        storeDetailDTO.setImages(images);

        return storeDetailDTO;
    }
}
