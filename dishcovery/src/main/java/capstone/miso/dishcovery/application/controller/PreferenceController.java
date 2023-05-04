package capstone.miso.dishcovery.application.controller;

import capstone.miso.dishcovery.application.service.StoreAndPreferenceService;
import capstone.miso.dishcovery.domain.preference.dto.DeletePreferenceReq;
import capstone.miso.dishcovery.domain.preference.dto.DeletePreferenceRes;
import capstone.miso.dishcovery.domain.preference.dto.SavePreferenceReq;
import capstone.miso.dishcovery.domain.preference.dto.SavePreferenceRes;
import capstone.miso.dishcovery.domain.preference.service.PreferenceService;
import capstone.miso.dishcovery.domain.store.dto.StoreShortDTO;
import capstone.miso.dishcovery.security.dto.MemberSecurityDTO;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
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
    private final PreferenceService preferenceService;
    @PostMapping(value = "/save")
    @Operation(summary = "또갈집 등록")
    @PreAuthorize("hasAnyRole('ROLE_ADMIN', 'ROLE_USER')")
    public SavePreferenceRes savePreference(@RequestBody SavePreferenceReq savePreferenceReq,
                                            @AuthenticationPrincipal MemberSecurityDTO member) {
        return storeAndPreferenceService.savePreference(member.getMember(), savePreferenceReq.storeId());
    }
    @PostMapping(value = "/delete")
    @Operation(summary = "또갈집 목록에서 삭제")
    @PreAuthorize("hasAnyRole('ROLE_ADMIN', 'ROLE_USER')")
    public DeletePreferenceRes savePreference(@RequestBody DeletePreferenceReq deletePreferenceReq,
                                              @AuthenticationPrincipal MemberSecurityDTO member) {
        return storeAndPreferenceService.deletePreference(member.getMember(), deletePreferenceReq.storeId());
    }
    @GetMapping(value = "", produces = "application/json;charset=UTF-8")
    @Operation(summary = "My 또갈집 조회", description = "내가 등록한 또갈집 매장목록 조회")
    @PreAuthorize("hasAnyRole('ROLE_ADMIN', 'ROLE_USER')")
    public List<StoreShortDTO> findMyStores(@RequestParam Double lat,
                                            @RequestParam Double lon,
                                            @AuthenticationPrincipal MemberSecurityDTO member){
        return storeAndPreferenceService.findMyStores(member.getMember(), lat, lon);
    }

    @GetMapping("/famous")
    public  List<StoreShortDTO> famousStores(@RequestParam(value = "page",required = false, defaultValue = "0") int page,
                                             @RequestParam(value = "size", required = false, defaultValue = "10") int size){
        return storeAndPreferenceService.famousStore(page, size);
    }
}
