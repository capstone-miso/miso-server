package capstone.miso.dishcovery.domain.preference.service;

import capstone.miso.dishcovery.domain.member.Member;
import capstone.miso.dishcovery.domain.preference.dto.DeletePreferenceRes;
import capstone.miso.dishcovery.domain.preference.dto.SavePreferenceRes;
import capstone.miso.dishcovery.domain.preference.repository.PreferenceRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.log4j.Log4j2;
import org.springframework.stereotype.Service;

/**
 * author        : duckbill413
 * date          : 2023-05-04
 * description   :
 **/
@Log4j2
@Service
@RequiredArgsConstructor
public class PreferenceService {
    private final PreferenceRepository preferenceRepository;

}
