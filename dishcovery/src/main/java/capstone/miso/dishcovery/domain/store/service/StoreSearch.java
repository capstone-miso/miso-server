package capstone.miso.dishcovery.domain.store.service;

import capstone.miso.dishcovery.domain.store.dto.StoreSearchCondition;
import capstone.miso.dishcovery.domain.store.dto.StoreShortDTO;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

/**
 * author        : duckbill413
 * date          : 2023-04-27
 * description   :
 **/

public interface StoreSearch {
    Page<StoreShortDTO> searchAllStoreShort(StoreSearchCondition condition, Pageable pageable);
}