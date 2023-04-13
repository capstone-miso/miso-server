package capstone.miso.dishcovery.application.files.convertor;

import capstone.miso.dishcovery.application.files.FileData;
import capstone.miso.dishcovery.application.files.Files;
/**
 * author        : duckbill413
 * date          : 2023-04-06
 * description   :
 **/

public interface TextConvertor {
    void convertToText(Files files);
    void parseText();
    FileData parseFile();
}
