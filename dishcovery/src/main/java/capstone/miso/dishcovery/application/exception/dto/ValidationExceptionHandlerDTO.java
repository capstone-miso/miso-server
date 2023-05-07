package capstone.miso.dishcovery.application.exception.dto;

import java.util.List;

/**
 * author        : duckbill413
 * date          : 2023-05-07
 * description   :
 **/

public record ValidationExceptionHandlerDTO(
        List<BindingErrorDTO> errors
) {
}
