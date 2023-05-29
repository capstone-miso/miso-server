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
public class KakaoStoreDetail {
    @SerializedName("basicInfo")
    @Expose
    private BasicInfo basicInfo;
    @SerializedName("findway")
    @Expose
    private Findway findway;
    @SerializedName("menuInfo")
    @Expose
    private MenuInfo menuInfo;
    @SerializedName("photo")
    @Expose
    private Photo photo;
}