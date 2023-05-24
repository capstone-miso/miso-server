package capstone.miso.dishcovery.application.controller;

import capstone.miso.dishcovery.domain.store.dto.StoreDetailDTO;
import capstone.miso.dishcovery.domain.store.dto.StoreShortDTO;
import capstone.miso.dishcovery.domain.store.service.StoreService;
import capstone.miso.dishcovery.dto.PageRequestDTO;
import capstone.miso.dishcovery.dto.PageResponseDTO;
import capstone.miso.dishcovery.security.dto.MemberSecurityDTO;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.dao.InvalidDataAccessApiUsageException;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

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

    @Operation(summary = "가게 간략한 정보 리스트", description = "매장 리스트에 올라가는 간단한 매장 리스트 정보")
    @GetMapping(value = "", produces = "application/json;charset=UTF-8")
    @PreAuthorize("permitAll()")
    public PageResponseDTO<StoreShortDTO> loadStoreShort(@Valid PageRequestDTO pageRequestDTO,
                                                         @AuthenticationPrincipal MemberSecurityDTO member,
                                                         HttpServletRequest httpServletRequest) {
        pageRequestDTO = pageRequestDTO == null ? new PageRequestDTO() : pageRequestDTO;

        PageResponseDTO<StoreShortDTO> responseDTO;
        if (member == null) {
            responseDTO = storeService.listWithStoreShort(pageRequestDTO, null);
        } else {
            responseDTO = storeService.listWithStoreShort(pageRequestDTO, member.getMember());
        }
        if (responseDTO.getPage() > responseDTO.getEnd()) {
            throw new InvalidDataAccessApiUsageException(String.format("페이지 번호를 확인해주세요. 현재 페이지 번호: %s, 마지막 페이지 번호: %s", responseDTO.getPage(), responseDTO.getEnd()));
        }

        String requestURL = httpServletRequest.getRequestURL().toString();
        String queryString = httpServletRequest.getQueryString();

        // 쿼리 파라미터가 있다면 URL 추가
        if (queryString != null) {
            requestURL += "?" + queryString;
        }
        responseDTO.setPageLink(requestURL);

        return responseDTO;
    }

    @Operation(summary = "가게 자세한 정보", description = "매장의 자세한 정보 (키워드 분석 포함) 제공")
    @GetMapping(value = "/{id}", produces = "application/json;charset=UTF-8")
    @PreAuthorize("permitAll()")
    public StoreDetailDTO loadStoreDetail(@PathVariable(value = "id") Long sid,
                                          @AuthenticationPrincipal MemberSecurityDTO member) {
        if (member == null) {
            return storeService.getStoreDetail(sid, null);
        }
        return storeService.getStoreDetail(sid, member.getMember());
    }

    @Operation(summary = "가게의 비슷한 또갈집 찾기 API", description = "매장의 카테고리, 키워드 정보를 종합하여 인기있는 매장 추천")
    @GetMapping(value = "/{id}/similar", produces = "application/json;charset=UTF-8")
    @PreAuthorize("permitAll()")
    public List<StoreShortDTO> loadSimilarStoreShorts(@PathVariable("id") Long sid,
                                                      @AuthenticationPrincipal MemberSecurityDTO member) {
        if (member == null){
            return storeService.getSimilarStoreShorts(sid, null);
        }
        return storeService.getSimilarStoreShorts(sid, member.getMember());
    }
}
