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
public class SubwayStop {

    @SerializedName("stationSimpleName")
    @Expose
    private String stationSimpleName;
    @SerializedName("exitNum")
    @Expose
    private String exitNum;
    @SerializedName("toExitDistance")
    @Expose
    private long toExitDistance;
    @SerializedName("subwayList")
    @Expose
    private List<SubwayLine> subwayList;
}
