package capstone.miso.dishcovery.application.exception;

import jakarta.validation.ConstraintValidator;
import jakarta.validation.ConstraintValidatorContext;

public class EnumValidator implements ConstraintValidator<ValidEnum, Enum<?>> {
    @Override
    public boolean isValid(Enum<?> value, ConstraintValidatorContext context) {
        if (value == null){
            return false; // 미입력 허용 안함
        }
        Enum<?>[] enumValues = value.getClass().getEnumConstants();
        for (Enum<?> enumValue : enumValues) {
            if (enumValue.name().equals(value.name()))
                return true;
        }
        return false;
    }
}
