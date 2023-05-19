package capstone.miso.dishcovery.application.controller;

import capstone.miso.dishcovery.application.exception.ValidEnum;
import capstone.miso.dishcovery.application.service.StoreAndKeywordService;
import capstone.miso.dishcovery.application.service.StoreAndPreferenceService;
import capstone.miso.dishcovery.domain.keyword.KeywordSet;
import capstone.miso.dishcovery.domain.store.dto.StoreShortDTO;
import capstone.miso.dishcovery.security.dto.MemberSecurityDTO;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@RequestMapping("/api/rank")
@RequiredArgsConstructor
@Tag(name = "랭킹", description = "Keyword 랭킹 Controller")
public class RankController {
    private final StoreAndKeywordService storeAndKeywordService;
    private final StoreAndPreferenceService storeAndPreferenceService;

    @GetMapping("")
    @PreAuthorize("permitAll()")
    public List<StoreShortDTO> findStoreByKeywordRank(@RequestParam(value = "keyword") @ValidEnum KeywordSet keyword,
                                                      @RequestParam(value = "page", required = false, defaultValue = "0") int page,
                                                      @RequestParam(value = "size", required = false, defaultValue = "10") int size,
                                                      @AuthenticationPrincipal MemberSecurityDTO memberSecurityDTO) {
        List<StoreShortDTO> storeShortDTOS = storeAndKeywordService.findStoreByKeywordRank(keyword, page, size);
        if (memberSecurityDTO == null) {
            return storeShortDTOS;
        }
        storeShortDTOS.forEach(storeShortDTO -> storeAndPreferenceService.setMyStorePreference(memberSecurityDTO.getMember(), storeShortDTO));
        return storeShortDTOS;
    }
}
