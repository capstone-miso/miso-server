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
public class BasicInfo {

    @SerializedName("cid")
    @Expose
    private long cid;
    @SerializedName("placenamefull")
    @Expose
    private String placenamefull;
    @SerializedName("mainphotourl")
    @Expose
    private String mainphotourl;
    @SerializedName("phonenum")
    @Expose
    private String phonenum;
    @SerializedName("category")
    @Expose
    private Category category;
    @SerializedName("openHour")
    @Expose
    private OpenHour openHour;
    @SerializedName("tags")
    @Expose
    private List<String> tags;
}
