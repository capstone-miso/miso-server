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
public class Realtime {

    @SerializedName("holiday")
    @Expose
    private String holiday;
    @SerializedName("breaktime")
    @Expose
    private String breaktime;
    @SerializedName("open")
    @Expose
    private String open;
    @SerializedName("moreOpenOffInfoExists")
    @Expose
    private String moreOpenOffInfoExists;
    @SerializedName("datetime")
    @Expose
    private String datetime;
    @SerializedName("currentPeriod")
    @Expose
    private CurrentPeriod currentPeriod;
    @SerializedName("closedToday")
    @Expose
    private String closedToday;

}
