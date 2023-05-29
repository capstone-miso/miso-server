package capstone.miso.dishcovery.application.files.dto;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class Bus {
    private String busStopName;
    private Long distance;
    private List<String> busNames;
}
