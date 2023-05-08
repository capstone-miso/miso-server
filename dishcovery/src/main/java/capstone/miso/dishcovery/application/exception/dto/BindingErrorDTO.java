package capstone.miso.dishcovery.application.exception.dto;

/**
 * author        : duckbill413
 * date          : 2023-05-07
 * description   :
 **/

public record BindingErrorDTO(
        String objectName,
        String field,
        String message
) {
}
