package capstone.miso.dishcovery.application.files.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class KakaoStoreDetailDTO {
    private String mainPhotoUrl;
    private List<String> openHour;
    private List<String> offDays;
    private List<String> tags;
    private Findway findway;
    private MenuInfo menuInfo;
    private List<Photo> photoList;
}