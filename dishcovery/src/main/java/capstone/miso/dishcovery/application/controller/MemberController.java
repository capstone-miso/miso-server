package capstone.miso.dishcovery.application.controller;

import capstone.miso.dishcovery.domain.member.exception.MemberValidationException;
import capstone.miso.dishcovery.domain.member.service.MemberService;
import capstone.miso.dishcovery.security.dto.MemberSecurityDTO;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import jakarta.validation.constraints.NotEmpty;
import lombok.RequiredArgsConstructor;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.validation.BindingResult;
import org.springframework.validation.FieldError;
import org.springframework.web.bind.annotation.*;

import java.util.Map;

/**
 * author        : duckbill413
 * date          : 2023-04-27
 * description   :
 **/
@RestController
@RequestMapping("/api/member")
@RequiredArgsConstructor
@Tag(name = "회원", description = "Member controller")
public class MemberController {
    private final MemberService memberService;

    @Operation(summary = "회원 닉네임 변경")
    @PatchMapping(value = "/nickname", produces = "application/json;charset=UTF-8")
    @PreAuthorize("isAuthenticated()")
    public String updateNickname(@RequestBody @Valid Map<String, @NotEmpty String> nickname,
                                 @AuthenticationPrincipal MemberSecurityDTO member,
                                 BindingResult bindingResult) {
        if (member.getNickname().equals(nickname.get("nickname"))) {
            bindingResult.addError(new FieldError("member", "nickname", "현재 닉네임과 같은 닉네임 입니다."));
        }
        if (memberService.checkNicknameExist(nickname.get("nickname"))) {
            bindingResult.addError(new FieldError("member", "nickname", "중복된 닉네임 입니다."));
        }
        if (bindingResult.hasErrors()) {
            throw new MemberValidationException(bindingResult);
        }

        memberService.updateNickname(member.getEmail(), nickname.get("nickname"));
        return "닉네임 업데이트 성공";
    }
}
