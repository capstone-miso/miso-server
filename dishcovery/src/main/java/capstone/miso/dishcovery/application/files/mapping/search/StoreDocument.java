package capstone.miso.dishcovery.application.files.mapping.search;
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
public class StoreDocument {
    @SerializedName("address_name")
    @Expose
    private String addressName;
    @SerializedName("category_group_code")
    @Expose
    private String categoryGroupCode;
    @SerializedName("category_group_name")
    @Expose
    private String categoryGroupName;
    @SerializedName("category_name")
    @Expose
    private String categoryName;
    @SerializedName("distance")
    @Expose
    private Long distance;
    @SerializedName("id")
    @Expose
    private Long id;
    @SerializedName("phone")
    @Expose
    private String phone;
    @SerializedName("place_name")
    @Expose
    private String placeName;
    @SerializedName("place_url")
    @Expose
    private String placeUrl;
    @SerializedName("road_address_name")
    @Expose
    private String roadAddressName;
    @SerializedName("x")
    @Expose
    private Double x;
    @SerializedName("y")
    @Expose
    private Double y;
}
