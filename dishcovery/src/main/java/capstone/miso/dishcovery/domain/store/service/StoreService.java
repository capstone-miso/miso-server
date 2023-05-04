package capstone.miso.dishcovery.domain.store.service;

import capstone.miso.dishcovery.domain.store.dto.StoreDetailDTO;
import capstone.miso.dishcovery.domain.store.dto.StoreShortDTO;
import capstone.miso.dishcovery.dto.PageRequestDTO;
import capstone.miso.dishcovery.dto.PageResponseDTO;

/**
 * author        : duckbill413
 * date          : 2023-04-27
 * description   :
 **/
public interface StoreService {
    PageResponseDTO<StoreShortDTO> listWithStoreShort(PageRequestDTO pageRequestDTO);
    StoreDetailDTO getStoreDetail(Long sid);
}
