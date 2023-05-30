package capstone.miso.dishcovery.domain.preference.repository;

import capstone.miso.dishcovery.domain.member.Member;
import capstone.miso.dishcovery.domain.member.MemberRole;
import capstone.miso.dishcovery.domain.member.repository.MemberRepository;
import capstone.miso.dishcovery.domain.preference.Preference;
import capstone.miso.dishcovery.domain.store.Store;
import capstone.miso.dishcovery.domain.store.repository.StoreRepository;
import jakarta.transaction.Transactional;
import org.jeasy.random.EasyRandom;
import org.jeasy.random.EasyRandomParameters;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.test.annotation.Rollback;

import java.time.LocalDate;
import java.util.List;
import java.util.Random;
import java.util.Set;
import java.util.stream.IntStream;

import static org.junit.jupiter.api.Assertions.*;
@SpringBootTest
class PreferenceRepositoryTest {
    @Autowired
    private PreferenceRepository preferenceRepository;
    @Autowired
    private StoreRepository storeRepository;
    @Autowired
    private MemberRepository memberRepository;
    @Test
    @DisplayName("Find Similar Preference Store")
    public void findSimilarPreferenceStoreTest() {
        PageRequest pageRequest = PageRequest.of(0, 10);
        Page<Long> storeInMyInterest = preferenceRepository.findStoreInMyInterest(Member.builder().email("dishcovery@gmail.com").build().getEmail(),
                pageRequest);

        System.out.println(storeInMyInterest);
    }
    @Test
    @DisplayName("Preference Dummy Data insert Test")
    @Transactional
    @Rollback(value = false)
    public void insertPreferenceDummyData() throws Exception {
        EasyRandomParameters parameters = new EasyRandomParameters()
                .dateRange(LocalDate.of(2020, 3, 5), LocalDate.now())
                .randomize(Integer.class, () -> (int) (Math.random() * 40) + 2) // 2부터 10 사이의 정수 값 생성
                .randomizationDepth(10)
                .seed(System.currentTimeMillis());

        EasyRandom generator = new EasyRandom(parameters);
        List<Member> allMembers = memberRepository.findAll();
        List<Store> allStores = storeRepository.findAll();

        for (Member member : allMembers) {
            Integer pint = generator.nextObject(Integer.class);

            for (int i = 0; i < pint; i++) {
                int index = (int) (Math.random() * allStores.size()-1);
                Store store = allStores.get(index);
                preferenceRepository.save(Preference.builder().member(member).store(store)
                        .build());
            }
        }
    }
}