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
public class Holiday {
    @SerializedName("holidayName")
    @Expose
    private String holidayName;
    @SerializedName("weekAndDay")
    @Expose
    private String weekAndDay;
    @SerializedName("temporaryHolidays")
    @Expose
    private String temporaryHolidays;
}
