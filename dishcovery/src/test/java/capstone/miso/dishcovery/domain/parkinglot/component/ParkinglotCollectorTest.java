package capstone.miso.dishcovery.domain.parkinglot.component;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import static org.junit.jupiter.api.Assertions.*;
@SpringBootTest
class ParkinglotCollectorTest {
    @Autowired
    private ParkinglotCollector parkinglotCollector;

    @Test
    @DisplayName("주차장 데이터 수집")
    public void collectParkinglotTest() {
        parkinglotCollector.collectParkinglot();
    }
}