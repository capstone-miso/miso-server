package capstone.miso.dishcovery.application.controller;

import capstone.miso.dishcovery.application.exception.dto.BindingErrorDTO;
import capstone.miso.dishcovery.application.exception.dto.ExceptionHandlerDTO;
import capstone.miso.dishcovery.application.exception.dto.ValidationExceptionHandlerDTO;
import capstone.miso.dishcovery.domain.member.exception.MemberValidationException;
import lombok.extern.log4j.Log4j2;
import org.apache.coyote.Response;
import org.springframework.dao.InvalidDataAccessApiUsageException;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.AccessDeniedException;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * author        : duckbill413
 * date          : 2023-04-03
 * description   :
 **/
@Log4j2
@RestControllerAdvice
public class CustomRestControllerAdvice {
    @ExceptionHandler(AccessDeniedException.class)
    public ResponseEntity<ExceptionHandlerDTO> handleAccessDeniedException(AccessDeniedException e) {

        return ResponseEntity.status(401).body(new ExceptionHandlerDTO("인증되지 않은 유저 입니다."));
    }

    @ExceptionHandler(MemberValidationException.class)
    public ResponseEntity<Map<String, Object>> handleMemberValidationException(MemberValidationException e) {
        Map<String, Object> errors = new HashMap<>();
        e.getBindingResult().getFieldErrors().forEach(fieldError -> errors.put(fieldError.getField(), fieldError.getDefaultMessage()));

        return ResponseEntity.badRequest().body(errors);
    }

    @ExceptionHandler({IllegalArgumentException.class, InvalidDataAccessApiUsageException.class})
    public ResponseEntity<ExceptionHandlerDTO> handleIllegalArgumentAndInvalidDataAccessException(Exception e) {
        return ResponseEntity.badRequest().body(new ExceptionHandlerDTO(e.getMessage()));
    }


    @ExceptionHandler(RuntimeException.class)
    public ResponseEntity<ExceptionHandlerDTO> handleRuntimeException(RuntimeException e) {
        e.printStackTrace();
        return ResponseEntity.badRequest().body(new ExceptionHandlerDTO(e.getMessage()));
    }

    @ExceptionHandler(MethodArgumentNotValidException.class)
    public ResponseEntity<ValidationExceptionHandlerDTO> handlerMethodArgumentValidException(MethodArgumentNotValidException e) {
        List<BindingErrorDTO> errors = new ArrayList<>();
        if (e.getBindingResult().hasErrors()) {
            e.getBindingResult().getFieldErrors().forEach(fieldError ->
                    errors.add(
                            new BindingErrorDTO(fieldError.getObjectName(), fieldError.getField(), fieldError.getDefaultMessage())
                    ));
        }
        return ResponseEntity.badRequest().body(new ValidationExceptionHandlerDTO(errors));
    }

    @ExceptionHandler(Exception.class)
    public ResponseEntity<ExceptionHandlerDTO> handlerAllException(Exception e) {
        e.printStackTrace();
        return ResponseEntity.badRequest().body(new ExceptionHandlerDTO(e.getMessage()));
    }
}
