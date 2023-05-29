package capstone.miso.dishcovery.application.files.mapping.address;
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
public class AddressDocument {
    @SerializedName("address_name")
    @Expose
    private String addressName;
    @SerializedName("address_type")
    @Expose
    private String addressType;
    @SerializedName("x")
    @Expose
    private Double x;
    @SerializedName("y")
    @Expose
    private Double y;
}
