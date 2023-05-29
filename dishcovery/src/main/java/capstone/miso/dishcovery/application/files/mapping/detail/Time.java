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
public class Time {

    @SerializedName("timeName")
    @Expose
    private String timeName;
    @SerializedName("timeSE")
    @Expose
    private String timeSE;
    @SerializedName("dayOfWeek")
    @Expose
    private String dayOfWeek;

}
