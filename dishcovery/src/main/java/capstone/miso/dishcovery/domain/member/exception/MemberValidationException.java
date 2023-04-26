package capstone.miso.dishcovery.domain.member.exception;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.Getter;
import org.springframework.validation.BindingResult;

/**
 * author        : duckbill413
 * date          : 2023-04-27
 * description   :
 **/
@Getter
public class MemberValidationException extends RuntimeException{
    private BindingResult bindingResult;
    public MemberValidationException(BindingResult bindingResult) {
        this.bindingResult = bindingResult;
    }
}
