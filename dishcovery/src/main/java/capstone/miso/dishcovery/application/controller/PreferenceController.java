package capstone.miso.dishcovery.application.controller;

import capstone.miso.dishcovery.application.service.StoreAndPreferenceService;
import capstone.miso.dishcovery.domain.preference.dto.DeletePreferenceRes;
import capstone.miso.dishcovery.domain.preference.dto.SavePreferenceRes;
import capstone.miso.dishcovery.domain.store.dto.StoreShortDTO;
import capstone.miso.dishcovery.dto.PageResponseDTO;
import capstone.miso.dishcovery.dto.SimplePageRequestDTO;
import capstone.miso.dishcovery.security.dto.MemberSecurityDTO;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;

/**
 * author        : duckbill413
 * date          : 2023-05-04
 * description   :
 **/
@RestController
@RequiredArgsConstructor
@RequestMapping("/api/preference")
@Tag(name = "또갈집(찜)", description = "관심매장 Controller")
public class PreferenceController {
    private final StoreAndPreferenceService storeAndPreferenceService;
    @PostMapping(value = "/{storeId}")
    @Operation(summary = "또갈집 등록")
    @PreAuthorize("hasAnyRole('ROLE_ADMIN', 'ROLE_USER')")
    public ResponseEntity<SavePreferenceRes> savePreference(@PathVariable(value = "storeId") Long storeId,
                                                           @AuthenticationPrincipal MemberSecurityDTO member) {
        storeAndPreferenceService.savePreference(member.getMember(), storeId);
        return ResponseEntity.status(201).body(new SavePreferenceRes("또갈집 등록 성공!"));
    }
    @DeleteMapping(value = "/{storeId}")
    @Operation(summary = "또갈집 목록에서 삭제")
    @PreAuthorize("hasAnyRole('ROLE_ADMIN', 'ROLE_USER')")
    public ResponseEntity<DeletePreferenceRes> deletePreference(@PathVariable(value = "storeId") Long storeId,
                                              @AuthenticationPrincipal MemberSecurityDTO member) {
        storeAndPreferenceService.deletePreference(member.getMember(), storeId);
        return ResponseEntity.ok(new DeletePreferenceRes("또갈집 삭제 성공!"));
    }
    @GetMapping(value = "", produces = "application/json;charset=UTF-8")
    @Operation(summary = "나의 또갈집 조회", description = "내가 등록한 또갈집 매장목록 조회")
    @PreAuthorize("hasAnyRole('ROLE_ADMIN', 'ROLE_USER')")
    public PageResponseDTO<StoreShortDTO> findMyStores(@Valid SimplePageRequestDTO pageRequestDTO,
                                                       @AuthenticationPrincipal MemberSecurityDTO member,
                                                       HttpServletRequest httpServletRequest){
        pageRequestDTO = pageRequestDTO == null ? new SimplePageRequestDTO() : pageRequestDTO;
        PageResponseDTO<StoreShortDTO> responseDTO = storeAndPreferenceService.findMyPreferenceStores(member.getMember(), pageRequestDTO);

        setPageResponsePageLink(httpServletRequest, responseDTO);

        return responseDTO;
    }


    @GetMapping("/famous")
    @Operation(summary = "많이 또갈집", description = "다른 사람들이 많이 저장한 또갈집 리스트")
    @PreAuthorize("hasAnyRole('ROLE_ADMIN', 'ROLE_USER')")
    public PageResponseDTO<StoreShortDTO> famousStores(@Valid SimplePageRequestDTO pageRequestDTO,
                                                       @AuthenticationPrincipal MemberSecurityDTO member,
                                                       HttpServletRequest httpServletRequest){
        pageRequestDTO = pageRequestDTO == null ? new SimplePageRequestDTO() : pageRequestDTO;
        PageResponseDTO<StoreShortDTO> responseDTO = storeAndPreferenceService.famousStore(pageRequestDTO, member.getMember());

        setPageResponsePageLink(httpServletRequest, responseDTO);
        return responseDTO;
    }
    @GetMapping("/similar")
    @Operation(summary = "비슷한 또갈집", description = "나의 또갈집 키워드로 비슷한 또갈집 추천")
    @PreAuthorize("hasAnyRole('ROLE_ADMIN', 'ROLE_USER')")
    public PageResponseDTO<StoreShortDTO> similarStores(@Valid SimplePageRequestDTO pageRequestDTO,
                                                        @AuthenticationPrincipal MemberSecurityDTO member,
                                                        HttpServletRequest httpServletRequest){
        pageRequestDTO = pageRequestDTO == null ? new SimplePageRequestDTO() : pageRequestDTO;

        PageResponseDTO<StoreShortDTO> responseDTO = storeAndPreferenceService.similarStore(pageRequestDTO, member.getMember());
        setPageResponsePageLink(httpServletRequest, responseDTO);
        return responseDTO;
    }
    private void setPageResponsePageLink(HttpServletRequest httpServletRequest, PageResponseDTO<StoreShortDTO> responseDTO) {
        String requestURL = httpServletRequest.getRequestURL().toString();
        String queryString = httpServletRequest.getQueryString();

        // 쿼리 파라미터가 있다면 URL에 추가
        if (queryString != null) {
            requestURL += "?" + queryString;
        }
        responseDTO.setPageLink(requestURL);
    }
}
