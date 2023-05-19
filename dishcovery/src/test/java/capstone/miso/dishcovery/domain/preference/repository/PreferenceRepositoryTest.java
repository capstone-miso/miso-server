package capstone.miso.dishcovery.domain.preference.repository;

import capstone.miso.dishcovery.domain.member.Member;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.data.domain.PageRequest;

import java.util.List;

import static org.junit.jupiter.api.Assertions.*;
@SpringBootTest
class PreferenceRepositoryTest {
    @Autowired
    private PreferenceRepository preferenceRepository;
    @Test
    @DisplayName("Find Similar Preference Store")
    public void findSimilarPreferenceStoreTest() {
        PageRequest pageRequest = PageRequest.of(0, 10);
        List<Long> storeInMyInterest = preferenceRepository.findStoreInMyInterest(Member.builder().email("dishcovery@gmail.com").build(),
                pageRequest);

        System.out.println(storeInMyInterest);
    }
}