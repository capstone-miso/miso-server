package capstone.miso.dishcovery.domain.member.repository;

import capstone.miso.dishcovery.domain.member.Member;
import capstone.miso.dishcovery.domain.member.MemberRole;
import org.jeasy.random.EasyRandom;
import org.jeasy.random.EasyRandomParameters;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import java.util.List;
import java.util.Set;
import java.util.stream.IntStream;

import static org.junit.jupiter.api.Assertions.*;

@SpringBootTest
class MemberRepositoryTest {
    @Autowired
    private MemberRepository memberRepository;

    @Test
    @DisplayName("Insert Dummy Member test")
    public void insertDummyMember() {
        EasyRandomParameters parameters = new EasyRandomParameters()
                .stringLengthRange(30, 50)
                .randomizationDepth(10)
                .seed(System.currentTimeMillis());

        EasyRandom generator = new EasyRandom(parameters);
        List<Member> list = IntStream.range(0, 1000)
                .parallel()
                .mapToObj(i -> {
                    Member member = generator.nextObject(Member.class);
                    member.setRoleSet(Set.of(MemberRole.USER));
                    return member;
                }).toList();

        memberRepository.saveAll(list);
    }
}