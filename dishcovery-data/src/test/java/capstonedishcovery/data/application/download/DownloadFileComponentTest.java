package capstonedishcovery.data.application.download;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import static org.junit.jupiter.api.Assertions.*;

/**
 * author        : duckbill413
 * date          : 2023-03-21
 * description   :
 **/
@SpringBootTest
class DownloadFileComponentTest {
    @Autowired
    private DownloadFileComponent downloadFileComponent;
    @DisplayName("file downdload test")
    @Test
    public void fileDownloadTest() {
        downloadFileComponent.saveFile();
    }

}