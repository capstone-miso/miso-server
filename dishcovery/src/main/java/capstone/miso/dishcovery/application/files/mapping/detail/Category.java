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
public class Category {

    @SerializedName("cateid")
    @Expose
    private String cateid;
    @SerializedName("catename")
    @Expose
    private String catename;
    @SerializedName("cate1name")
    @Expose
    private String cate1name;
    @SerializedName("fullCateIds")
    @Expose
    private String fullCateIds;

}
