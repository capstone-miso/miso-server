package capstone.miso.dishcovery.security;

import capstone.miso.dishcovery.domain.member.Member;
import capstone.miso.dishcovery.domain.member.MemberRole;
import capstone.miso.dishcovery.domain.member.repository.MemberRepository;
import capstone.miso.dishcovery.security.dto.MemberSecurityDTO;
import lombok.RequiredArgsConstructor;
import lombok.extern.log4j.Log4j2;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.oauth2.client.registration.ClientRegistration;
import org.springframework.security.oauth2.client.userinfo.DefaultOAuth2UserService;
import org.springframework.security.oauth2.client.userinfo.OAuth2UserRequest;
import org.springframework.security.oauth2.core.OAuth2AuthenticationException;
import org.springframework.security.oauth2.core.user.OAuth2User;
import org.springframework.stereotype.Service;

import java.util.*;
import java.util.stream.Collectors;

/**
 * author        : duckbill413
 * date          : 2023-04-26
 * description   :
 **/
@Log4j2
@Service
@RequiredArgsConstructor
public class CustomOauth2UserService extends DefaultOAuth2UserService {
    private final MemberRepository memberRepository;
    private final PasswordEncoder passwordEncoder;

    @Override
    public OAuth2User loadUser(OAuth2UserRequest userRequest) throws OAuth2AuthenticationException {
        ClientRegistration clientRegistration = userRequest.getClientRegistration();
        String clientName = clientRegistration.getClientName();
        OAuth2User oAuth2User = super.loadUser(userRequest);

        Map<String, Object> paramMap = oAuth2User.getAttributes();
        Map<String, Object> socialUserDetails = switch (clientName) {
            case "kakao" -> getKakaoEmail(paramMap);
            case "naver" -> getNaverEmail(paramMap);
            default -> throw new OAuth2AuthenticationException("Unsupported Login Client");
        };

        return generateDTO(socialUserDetails, paramMap);
    }

    private MemberSecurityDTO generateDTO(Map<String, Object> socialUserDetails, Map<String, Object> paramMap) {
        String email = (String) socialUserDetails.get("email");
        String nickname = (String) socialUserDetails.get("nickname");
        Optional<Member> result = memberRepository.findByEmail(email);

        // 해당 이메일 사용자가 없으면 회원가입 진행
        if (result.isEmpty()) {
            String randomPassword = UUID.randomUUID().toString();

            Member member = Member.builder()
                    .email(email)
                    .password(passwordEncoder.encode(randomPassword))
                    .nickname(nickname)
                    .build();
            member.addRole(MemberRole.USER);
            Member savedMember = memberRepository.save(member);

            MemberSecurityDTO memberSecurityDTO = new MemberSecurityDTO(
                    savedMember.getEmail(),
                    savedMember.getPassword(),
                    savedMember.getNickname(),
                    Arrays.asList(new SimpleGrantedAuthority("ROLE_" + MemberRole.USER.name()))
            );
            memberSecurityDTO.setProps(paramMap);
            return memberSecurityDTO;
        } else {
            Member member = result.get();
            MemberSecurityDTO memberSecurityDTO = new MemberSecurityDTO(
                    member.getEmail(),
                    member.getPassword(),
                    member.getNickname(),
                    member.getRoleSet().stream().map(memberRole -> new SimpleGrantedAuthority("ROLE_" + memberRole.name())).collect(Collectors.toList())
            );
            return memberSecurityDTO;
        }
    }

    private Map<String, Object> getKakaoEmail(Map<String, Object> paramMap) {
        Object value = paramMap.get("kakao_account");
        LinkedHashMap accountMap = (LinkedHashMap) value;

        Object profileValue = ((LinkedHashMap<?, ?>) value).get("profile");
        LinkedHashMap profileMap = (LinkedHashMap) profileValue;

        return Map.of(
                "email", accountMap.get("email"),
                "nickname", profileMap.get("nickname")
        );
    }

    private Map<String, Object> getNaverEmail(Map<String, Object> paramMap) {
        Object value = paramMap.get("response");
        LinkedHashMap accountMap = (LinkedHashMap) value;

        return Map.of(
                "email", accountMap.get("email"),
                "nickname", accountMap.get("nickname")
        );
    }
}
