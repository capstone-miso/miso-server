package capstone.miso.dishcovery.domain.keyword.service;

import capstone.miso.dishcovery.domain.keyword.component.KeywordExtractorComp;
import com.google.gson.annotations.SerializedName;
import lombok.RequiredArgsConstructor;
import lombok.extern.log4j.Log4j2;
import org.springframework.stereotype.Service;

@Log4j2
@Service
@RequiredArgsConstructor
public class KeywordService {
    private final KeywordExtractorComp keywordExtractorComp;
    public void extractKeywordFromKeywordData(){
        keywordExtractorComp.keywordExtract();
    }
}
