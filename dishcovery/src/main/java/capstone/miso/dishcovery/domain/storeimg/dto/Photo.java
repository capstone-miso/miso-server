package capstone.miso.dishcovery.domain.storeimg.dto;

import lombok.Getter;
import lombok.Setter;

/**
 * author        : duckbill413
 * date          : 2023-04-14
 * description   :
 **/
@Getter
@Setter
public class Photo {
    public String photoid;
    public String title;
    public String summary;
    public String url;
    public String writer;
    public String link;
    public String reviewid;
    public String date;
}
