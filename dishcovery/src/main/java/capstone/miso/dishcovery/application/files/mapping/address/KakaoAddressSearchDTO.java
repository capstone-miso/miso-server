package capstone.miso.dishcovery.application.files.mapping.address;

import java.util.List;

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
public class KakaoAddressSearchDTO {
    @SerializedName("documents")
    @Expose
    private List<AddressDocument> documents;
}

