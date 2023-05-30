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
public class Menu {

    @SerializedName("price")
    @Expose
    private String price;
    @SerializedName("recommend")
    @Expose
    private boolean recommend;
    @SerializedName("menu")
    @Expose
    private String menu;
    @SerializedName("desc")
    @Expose
    private String desc;
    @SerializedName("img")
    @Expose
    private String img;
}
