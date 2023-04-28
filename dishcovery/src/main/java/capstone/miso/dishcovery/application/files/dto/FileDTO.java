package capstone.miso.dishcovery.application.files.dto;

import java.time.LocalDate;
import java.util.List;

/**
 * author        : duckbill413
 * date          : 2023-04-13
 * description   :
 **/

public record FileDTO(
        Long fid,
        String region,
        String department,
        String fileName,
        String fileUrl,
        LocalDate fileUploaded,
        boolean fileDownloaded,
        boolean converted,
        String convertResult,
        List<FileDataDTO> fileData
) {
}
