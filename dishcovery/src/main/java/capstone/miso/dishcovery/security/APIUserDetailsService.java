package capstone.miso.dishcovery.security;

import capstone.miso.dishcovery.domain.member.Member;
import capstone.miso.dishcovery.domain.member.repository.MemberRepository;
import capstone.miso.dishcovery.security.dto.MemberSecurityDTO;
import lombok.RequiredArgsConstructor;
import lombok.extern.log4j.Log4j2;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

/**
 * author        : duckbill413
 * date          : 2023-04-26
 * description   :
 **/
@Log4j2
@Service
@RequiredArgsConstructor
public class APIUserDetailsService implements UserDetailsService {
    private final MemberRepository memberRepository;
    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
        Optional<Member> result = memberRepository.findByEmail(username);
        Member member = result.orElseThrow(() -> new UsernameNotFoundException("Cannot find user email"));

        return new MemberSecurityDTO(
                member.getEmail(),
                member.getPassword(),
                member.getNickname(),
                member.getRoleSet().stream().map(
                        memberRole -> new SimpleGrantedAuthority("ROLE_" + memberRole.name())
                ).collect(Collectors.toList())
        );
    }
}
