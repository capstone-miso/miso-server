package capstone.miso.dishcovery.application.files.mapping.detail;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class Busstop {

    @SerializedName("busStopId")
    @Expose
    private String busStopId;
    @SerializedName("busStopName")
    @Expose
    private String busStopName;
    @SerializedName("toBusstopDistance")
    @Expose
    private long toBusstopDistance;
    @SerializedName("busInfo")
    @Expose
    private List<BusInfo> busInfo;
}
