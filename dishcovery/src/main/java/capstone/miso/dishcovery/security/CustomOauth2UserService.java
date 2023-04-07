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
 * date          : 2023-03-08
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
        log.info("-------------------CustomOauth2UserService-----------------------");
        ClientRegistration clientRegistration = userRequest.getClientRegistration();
        String clientName = clientRegistration.getClientName();
        OAuth2User oAuth2User = super.loadUser(userRequest);

        Map<String, Object> paramMap = oAuth2User.getAttributes();
        System.out.println("-----------------");
        System.out.println(paramMap);

        Map<String, String> socialUserDetails = switch (clientName) {
            case "kakao" -> getKakaoEmail(paramMap);
            case "naver" -> getNaverEmail(paramMap);
            default -> null;
        };

        return generateDTO(socialUserDetails, paramMap);
    }

    private MemberSecurityDTO generateDTO(Map<String, String> socialUserDetails, Map<String, Object> paramMap) {
        String email = socialUserDetails.get("email");
        Optional<Member> result = memberRepository.findByEmail(email);

        // 데이터베이스에 해당 이메일 사용자가 없다면
        if (result.isEmpty()) {
            String randomPassword = UUID.randomUUID().toString();

            Member member = Member.builder()
                    .email(email)
                    .password(passwordEncoder.encode(randomPassword))
                    .nickname((String) paramMap.get("nickname"))
                    .build();
            member.addRole(MemberRole.USER);
            memberRepository.save(member);

            // APIUserSecurityDTO 구성 및 반환
            MemberSecurityDTO memberSecurityDTO = new MemberSecurityDTO(
                    member.getMid(), member.getEmail(), member.getPassword(), member.getNickname(),
                    Arrays.asList(new SimpleGrantedAuthority("ROLE_USER")));
            memberSecurityDTO.setProps(paramMap);

            return memberSecurityDTO;
        } else {
            Member member = result.get();
            MemberSecurityDTO memberSecurityDTO = new MemberSecurityDTO(
                    member.getMid(), member.getEmail(), member.getPassword(), member.getNickname(),
                    member.getRoleSet().stream().map(apiUserRole -> new SimpleGrantedAuthority("ROLE_" + apiUserRole.name())).collect(Collectors.toList()));

            return memberSecurityDTO;
        }
    }

    private Map<String, String> getKakaoEmail(Map<String, Object> paramMap) {
        Object value = paramMap.get("kakao_account");
        LinkedHashMap accountMap = (LinkedHashMap) value;

        return Map.of(
                "email", (String) accountMap.get("email")
        );
    }

    private Map<String, String> getNaverEmail(Map<String, Object> paramMap) {
        Object value = paramMap.get("response");
        LinkedHashMap accountMap = (LinkedHashMap) value;

        return Map.of(
                "email", (String) accountMap.get("email"),
                "nickname", (String) accountMap.get("nickname")
        );
    }
}
