package capstone.miso.dishcovery.application.files.service;

import capstone.miso.dishcovery.application.files.FileData;
import capstone.miso.dishcovery.application.files.mapping.KakaoStoreExtractor;
import capstone.miso.dishcovery.application.files.repository.FileDataRepository;
import capstone.miso.dishcovery.domain.store.repository.StoreRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.log4j.Log4j2;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;
@Log4j2
@Service
@RequiredArgsConstructor
public class KakaoFileDataMatchService {
    private final KakaoStoreExtractor kakaoStoreExtractor;
    private final FileDataRepository fileDataRepository;
    private final StoreRepository storeRepository;
    public void fileDataStoreMatcher(long after, long before){
//        Optional<List<FileData>> allByStoreIsNull = fileDataRepository.findAllByStoreIsNull();
        Optional<List<FileData>> allByStoreIsNull = fileDataRepository.findAllByStoreIsNullAndFidBetweenOrderByFidDesc(after, before);

        List<FileData> fileData = allByStoreIsNull.orElse(null);
        assert fileData != null;
        log.info("FileData Found : " + fileData.size());
        kakaoStoreExtractor.storeExtraction(fileData);
    }
}
