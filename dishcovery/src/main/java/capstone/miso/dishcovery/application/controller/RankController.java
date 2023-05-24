package capstone.miso.dishcovery.application.controller;

import capstone.miso.dishcovery.application.exception.ValidEnum;
import capstone.miso.dishcovery.application.service.StoreAndKeywordService;
import capstone.miso.dishcovery.application.service.StoreAndPreferenceService;
import capstone.miso.dishcovery.domain.keyword.KeywordSet;
import capstone.miso.dishcovery.domain.store.dto.StoreShortDTO;
import capstone.miso.dishcovery.dto.PageResponseDTO;
import capstone.miso.dishcovery.dto.SimplePageRequestDTO;
import capstone.miso.dishcovery.security.dto.MemberSecurityDTO;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/rank")
@RequiredArgsConstructor
@Tag(name = "랭킹", description = "Keyword 랭킹 Controller")
public class RankController {
    private final StoreAndKeywordService storeAndKeywordService;
    private final StoreAndPreferenceService storeAndPreferenceService;

    @GetMapping("")
    @PreAuthorize("permitAll()")
    @Operation(summary = "Keyword 랭킹으로 매장 조회")
    public PageResponseDTO<StoreShortDTO> findStoreByKeywordRank(@Valid SimplePageRequestDTO pageRequestDTO,
                                                                 @RequestParam(value = "keyword") @ValidEnum KeywordSet keyword,
                                                                 @AuthenticationPrincipal MemberSecurityDTO member,
                                                                 HttpServletRequest httpServletRequest) {
        PageResponseDTO<StoreShortDTO> result;
        if (member == null) {
            result = storeAndKeywordService.findStoreByKeywordRank(keyword, pageRequestDTO, null);
        } else {
            result = storeAndKeywordService.findStoreByKeywordRank(keyword, pageRequestDTO, member.getMember());
        }
        setPageResponsePageLink(httpServletRequest, result);
        return result;
    }

    private void setPageResponsePageLink(HttpServletRequest
                                                 httpServletRequest, PageResponseDTO<StoreShortDTO> responseDTO) {
        String requestURL = httpServletRequest.getRequestURL().toString();
        String queryString = httpServletRequest.getQueryString();

        // 쿼리 파라미터가 있다면 URL에 추가
        if (queryString != null) {
            requestURL += "?" + queryString;
        }
        responseDTO.setPageLink(requestURL);
    }
}
