package capstone.miso.dishcovery.domain.store.service;

import capstone.miso.dishcovery.domain.store.dto.StoreSearchCondition;
import capstone.miso.dishcovery.domain.store.dto.StoreShortDTO;
import capstone.miso.dishcovery.domain.store.repository.StoreRepository;
import capstone.miso.dishcovery.dto.PageRequestDTO;
import capstone.miso.dishcovery.dto.PageResponseDTO;
import lombok.RequiredArgsConstructor;
import lombok.extern.log4j.Log4j2;
import org.modelmapper.ModelMapper;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

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
        StoreSearchCondition condition = new StoreSearchCondition(pageRequestDTO.getCategory(), pageRequestDTO.getKeyword(), pageRequestDTO.getSector());
        Pageable pageable = pageRequestDTO.getPageable("sid.desc");

        Page<StoreShortDTO> result = storeRepository.searchAllStoreShort(condition, pageable);

        return PageResponseDTO.<StoreShortDTO>builder()
                .pageRequestDTO(pageRequestDTO)
                .dtoList(result.getContent())
                .total((int) result.getTotalElements())
                .build();
    }
}
