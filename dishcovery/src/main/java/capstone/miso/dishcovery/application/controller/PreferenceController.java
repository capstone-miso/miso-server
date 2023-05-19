package capstone.miso.dishcovery.application.controller;

import capstone.miso.dishcovery.application.service.StoreAndPreferenceService;
import capstone.miso.dishcovery.domain.preference.dto.DeletePreferenceRes;
import capstone.miso.dishcovery.domain.preference.dto.SavePreferenceRes;
import capstone.miso.dishcovery.domain.store.dto.StoreShortDTO;
import capstone.miso.dishcovery.domain.store.service.StoreSearch;
import capstone.miso.dishcovery.dto.PageResponseDTO;
import capstone.miso.dishcovery.security.dto.MemberSecurityDTO;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.validation.Valid;
import jakarta.validation.constraints.Min;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;

import java.util.List;

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
    public PageResponseDTO<StoreShortDTO> findMyStores(@RequestParam(value = "page", defaultValue = "1", required = false) @Valid @Min(value = 1L, message = "페이지 번호는 1 이상 입니다.") int page,
                                                       @RequestParam(value = "size", defaultValue = "10", required = false) int size,
                                                       @AuthenticationPrincipal MemberSecurityDTO member,
                                                       HttpServletRequest httpServletRequest){
        PageResponseDTO<StoreShortDTO> responseDTO = storeAndPreferenceService.findMyPreferenceStores(member.getMember(), page, size);

        String requestURL = httpServletRequest.getRequestURL().toString();
        String queryString = httpServletRequest.getQueryString();

        // 쿼리 파라미터가 있다면 URL에 추가
        if (queryString != null) {
            requestURL += "?" + queryString;
        }
        responseDTO.setPageLink(requestURL);

        return responseDTO;
    }

    @GetMapping("/famous")
    @Operation(summary = "많이 또갈집", description = "다른 사람들이 많이 저장한 또갈집 리스트")
    @PreAuthorize("isAuthenticated()")
    public  List<StoreShortDTO> famousStores(@RequestParam(value = "page",required = false, defaultValue = "0") int page,
                                             @RequestParam(value = "size", required = false, defaultValue = "10") int size,
                                             @AuthenticationPrincipal MemberSecurityDTO member){
        return storeAndPreferenceService.famousStore(page, size, member.getMember());
    }
    @GetMapping("/similar")
    @Operation(summary = "비슷한 또갈집", description = "나의 또갈집 키워드로 비슷한 또갈집 추천")
    @PreAuthorize("hasAnyRole('ROLE_ADMIN', 'ROLE_USER')")
    public List<StoreShortDTO> similarStores(@RequestParam(value = "page",required = false, defaultValue = "0") int page,
                                             @RequestParam(value = "size", required = false, defaultValue = "10") int size,
                                             @AuthenticationPrincipal MemberSecurityDTO member){
        return storeAndPreferenceService.similarStore(page, size, member.getMember());
    }
}
