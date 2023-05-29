package capstone.miso.dishcovery.application.files.mapping.detail;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class SubwayLine {

    @SerializedName("subwayId")
    @Expose
    private String subwayId;
    @SerializedName("subwayName")
    @Expose
    private String subwayName;

}
