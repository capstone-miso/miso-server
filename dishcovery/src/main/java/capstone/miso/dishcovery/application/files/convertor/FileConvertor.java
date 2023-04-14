package capstone.miso.dishcovery.application.files.convertor;

import capstone.miso.dishcovery.application.files.FileData;
import capstone.miso.dishcovery.application.files.Files;

import java.util.List;

/**
 * author        : duckbill413
 * date          : 2023-04-06
 * description   :
 **/

public interface FileConvertor {
    List<FileData> parseFileToFileData(Files files);
}
