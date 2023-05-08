package capstone.miso.dishcovery.domain.image.dto;

import jakarta.validation.Valid;
import lombok.Getter;
import lombok.Setter;

import java.util.List;

/**
 * author        : duckbill413
 * date          : 2023-04-14
 * description   :
 **/
@Getter
@Setter
public class PhotoViewer {
    public String placenamefull;
    public String type;
    public String basis;
    public String moreId;
    public Integer photoCnt;
    @Valid
    public List<Photo> list;
}
