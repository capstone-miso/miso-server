package capstone.miso.dishcovery.application.controller;

import capstone.miso.dishcovery.domain.store.dto.StoreDetailDTO;
import capstone.miso.dishcovery.domain.store.dto.StoreShortDTO;
import capstone.miso.dishcovery.domain.store.service.StoreService;
import capstone.miso.dishcovery.dto.PageRequestDTO;
import capstone.miso.dishcovery.dto.PageResponseDTO;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

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
    @GetMapping(value = "/list", produces = "application/json;charset=UTF-8")
    public PageResponseDTO<StoreShortDTO> loadStoreShort(PageRequestDTO pageRequestDTO) {
        pageRequestDTO = pageRequestDTO == null ? new PageRequestDTO() : pageRequestDTO;

        PageResponseDTO<StoreShortDTO> result = storeService.listWithStoreShort(pageRequestDTO);
        return result;
    }

    @Operation(summary = "가게 자세한 정보", description = "매장의 자세한 정보 (키워드 분석 포함) 제공")
    @GetMapping(value = "", produces = "application/json;charset=UTF-8")
    public StoreDetailDTO loadStoreDetail(@RequestParam(value = "id") Long sid) {
        return storeService.getStoreDetail(sid);
    }
}
