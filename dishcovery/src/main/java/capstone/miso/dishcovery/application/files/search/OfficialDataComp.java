package capstone.miso.dishcovery.application.files.search;

import capstone.miso.dishcovery.application.files.Files;

import java.util.List;

/**
 * author        : duckbill413
 * date          : 2023-04-12
 * description   :
 **/

public interface OfficialDataComp {
    List<Files> loadFile(Long page, String sdate, String edate);
}
