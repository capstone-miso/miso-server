package capstone.miso.dishcovery.application.controller;

import capstone.miso.dishcovery.domain.store.dto.StoreShortDTO;
import capstone.miso.dishcovery.domain.store.service.StoreService;
import capstone.miso.dishcovery.dto.PageRequestDTO;
import capstone.miso.dishcovery.dto.PageResponseDTO;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.PageRequest;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.*;

/**
 * author        : duckbill413
 * date          : 2023-04-27
 * description   :
 **/
@RestController
@RequestMapping("/store")
@RequiredArgsConstructor
@Tag(name = "가게", description = "Store Controller")
public class StoreController {
    private final StoreService storeService;

    @Operation(summary = "가게 간략한 정보", description = "매장 리스트에 올라가는 간단한 매장 리스트 정보")
    @GetMapping(value = "/short/list", produces = "application/json;charset=UTF-8")
    public PageResponseDTO<StoreShortDTO> loadStoreShort(PageRequestDTO pageRequestDTO) {
        if (pageRequestDTO == null)
            pageRequestDTO = new PageRequestDTO();

        PageResponseDTO<StoreShortDTO> result = storeService.listWithStoreShort(pageRequestDTO);
        return result;
    }
}
