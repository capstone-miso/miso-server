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
public class Period {

    @SerializedName("periodName")
    @Expose
    private String periodName;
    @SerializedName("timeList")
    @Expose
    private List<Time> timeList;

}
