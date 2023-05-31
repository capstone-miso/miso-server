package capstone.miso.dishcovery.domain.parkinglot.dto;

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
public class Result {

    @SerializedName("CODE")
    @Expose
    private String code;
    @SerializedName("MESSAGE")
    @Expose
    private String message;

}
