package capstone.miso.dishcovery.application.files.mapping.detail;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

import java.util.List;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class PhotoGroup {

    @SerializedName("photoCount")
    @Expose
    private long photoCount;
    @SerializedName("categoryName")
    @Expose
    private String categoryName;
    @SerializedName("list")
    @Expose
    private List<PhotoUrl> list;
}
