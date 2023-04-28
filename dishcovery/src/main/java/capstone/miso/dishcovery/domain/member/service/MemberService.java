package capstone.miso.dishcovery.domain.member.service;

import capstone.miso.dishcovery.domain.member.Member;
import capstone.miso.dishcovery.domain.member.repository.MemberRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.log4j.Log4j2;
import org.springframework.stereotype.Service;

/**
 * author        : duckbill413
 * date          : 2023-04-27
 * description   :
 **/
@Log4j2
@Service
@RequiredArgsConstructor
public class MemberService {
    private final MemberRepository memberRepository;
    public void updateNickname(String email, String nickname){
        Member member = memberRepository.findByEmail(email).get();
        member.setNickname(nickname);
        memberRepository.save(member);
    }
    public boolean checkNicknameExist(String nickname){
        return memberRepository.existsByNickname(nickname);
    }
}
