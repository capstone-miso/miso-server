package capstone.miso.dishcovery.application.files.mapping.detail;

import capstone.miso.dishcovery.application.files.dto.Bus;
import capstone.miso.dishcovery.application.files.dto.Subway;
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
public class Findway {

    @SerializedName("subway")
    @Expose
    private List<SubwayStop> subwayStop;
    @SerializedName("busstop")
    @Expose
    private List<Busstop> busstop;
    @SerializedName("roadmap")
    @Expose
    private String roadmap;
}
