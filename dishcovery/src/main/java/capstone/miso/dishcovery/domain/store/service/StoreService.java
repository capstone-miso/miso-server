package capstone.miso.dishcovery.domain.store.service;

import capstone.miso.dishcovery.domain.store.dto.StoreShortDTO;
import capstone.miso.dishcovery.domain.store.repository.StoreRepository;
import capstone.miso.dishcovery.dto.PageRequestDTO;
import capstone.miso.dishcovery.dto.PageResponseDTO;
import lombok.RequiredArgsConstructor;
import lombok.extern.log4j.Log4j2;
import org.springframework.stereotype.Service;

/**
 * author        : duckbill413
 * date          : 2023-04-27
 * description   :
 **/
public interface StoreService {
    PageResponseDTO<StoreShortDTO> listWithStoreShort(PageRequestDTO pageRequestDTO);
}
