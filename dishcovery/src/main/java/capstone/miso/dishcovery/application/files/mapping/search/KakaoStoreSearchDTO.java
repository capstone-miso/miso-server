package capstone.miso.dishcovery.application.files.mapping.search;

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
public class KakaoStoreSearchDTO {
    @SerializedName("documents")
    @Expose
    private List<StoreDocument> documents;
}
