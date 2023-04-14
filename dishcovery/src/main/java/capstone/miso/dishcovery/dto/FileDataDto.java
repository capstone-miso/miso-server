package capstone.miso.dishcovery.dto;

import java.time.LocalDate;
import java.time.LocalTime;

/**
 * author        : duckbill413
 * date          : 2023-04-13
 * description   :
 **/
public record FileDataDto(
        Long fid,
        LocalDate date,
        LocalTime time,
        String storeName,
        String storeAddress,
        int participants,
        int cost
) {
}
