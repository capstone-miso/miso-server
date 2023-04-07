package capstone.miso.dishcovery.security;

import capstone.miso.dishcovery.domain.member.Member;
import capstone.miso.dishcovery.domain.member.repository.MemberRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.log4j.Log4j2;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

import java.util.Optional;
import java.util.stream.Collectors;

/**
 * author        : duckbill413
 * date          : 2023-03-05
 * description   :
 **/
@Service
@Log4j2
@RequiredArgsConstructor
public class APIUserDetailsService implements UserDetailsService {
    private final MemberRepository memberRepository;
    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
        log.info("-------------------APIUserDetailsService-----------------------");

        Optional<Member> result = memberRepository.findByEmail(username);

        Member member = result.orElseThrow(() -> new UsernameNotFoundException("Cannot find mid"));

//        Member dto = new Member(
//                apiUser.getMid(),
//                apiUser.getMpw(),
//                apiUser.getEmail(),
//                apiUser.isDeleted(),
//                apiUser.isSocial(),
//                apiUser.getRoleSet().stream().map(apiUserRole ->
//                        new SimpleGrantedAuthority("ROLE_"+apiUserRole.name())).collect(Collectors.toList())
//        );
        return null;
    }
}
