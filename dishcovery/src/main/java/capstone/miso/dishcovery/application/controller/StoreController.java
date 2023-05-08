package capstone.miso.dishcovery.application.controller;

import capstone.miso.dishcovery.application.service.StoreAndPreferenceService;
import capstone.miso.dishcovery.domain.store.dto.StoreDetailDTO;
import capstone.miso.dishcovery.domain.store.dto.StoreShortDTO;
import capstone.miso.dishcovery.domain.store.service.StoreService;
import capstone.miso.dishcovery.dto.PageRequestDTO;
import capstone.miso.dishcovery.dto.PageResponseDTO;
import capstone.miso.dishcovery.security.dto.MemberSecurityDTO;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;

/**
 * author        : duckbill413
 * date          : 2023-04-27
 * description   :
 **/
@RestController
@RequestMapping("/api/store")
@RequiredArgsConstructor
@Tag(name = "가게", description = "Store Controller")
public class StoreController {
    private final StoreService storeService;
    private final StoreAndPreferenceService storeAndPreferenceService;

    @Operation(summary = "가게 간략한 정보", description = "매장 리스트에 올라가는 간단한 매장 리스트 정보")
    @GetMapping(value = "/list", produces = "application/json;charset=UTF-8")
    @PreAuthorize("permitAll()")
    public PageResponseDTO<StoreShortDTO> loadStoreShort(@Valid PageRequestDTO pageRequestDTO,
                                                         @AuthenticationPrincipal MemberSecurityDTO member) {
        pageRequestDTO = pageRequestDTO == null ? new PageRequestDTO() : pageRequestDTO;

        if (member == null)
            return storeService.listWithStoreShort(pageRequestDTO);
        return storeAndPreferenceService.listWithStoreShortWithPreference(pageRequestDTO, member.getMember());
    }

    @Operation(summary = "가게 자세한 정보", description = "매장의 자세한 정보 (키워드 분석 포함) 제공")
    @GetMapping(value = "/{id}", produces = "application/json;charset=UTF-8")
    @PreAuthorize("permitAll()")
    public StoreDetailDTO loadStoreDetail(@PathVariable(value = "id") Long sid,
                                          @AuthenticationPrincipal MemberSecurityDTO member) {
        if (member == null)
            return storeService.getStoreDetail(sid);
        return storeAndPreferenceService.getStoreDetailWithPreference(sid, member.getMember());
    }
}
