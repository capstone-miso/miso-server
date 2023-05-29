package capstone.miso.dishcovery.application.files.dto;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class Photo {
    private String photoId;
    private String photoUrl;
}
