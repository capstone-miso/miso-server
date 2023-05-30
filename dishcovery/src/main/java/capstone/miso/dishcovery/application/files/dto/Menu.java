package capstone.miso.dishcovery.application.files.dto;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class Menu {
    private String price;
    private String menu;
    private String description;
    private String imgUrl;
}
