package capstone.miso.dishcovery.application.controller;

import capstone.miso.dishcovery.application.dto.IllegalArgRes;
import capstone.miso.dishcovery.application.dto.LoginFailedRes;
import capstone.miso.dishcovery.application.dto.RuntimeExceptionRes;
import capstone.miso.dishcovery.domain.member.exception.MemberValidationException;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.extern.log4j.Log4j2;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.AccessDeniedException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

import java.util.Arrays;
import java.util.HashMap;
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
    public ResponseEntity<LoginFailedRes> handleAccessDeniedException(AccessDeniedException e){

        return ResponseEntity.status(401).body(new LoginFailedRes("인증되지 않은 유저 입니다.", e.getLocalizedMessage()));
    }
    @ExceptionHandler(MemberValidationException.class)
    public ResponseEntity<Map<String, Object>> handleMemberValidationException(MemberValidationException e) throws JsonProcessingException {
        Map<String, Object> errors = new HashMap<>();
        e.getBindingResult().getFieldErrors().forEach(fieldError -> errors.put(fieldError.getField(), fieldError.getDefaultMessage()));

        return ResponseEntity.badRequest().body(errors);
    }
    @ExceptionHandler(IllegalArgumentException.class)
    public ResponseEntity<IllegalArgRes> handleIllegalArgumentException(IllegalArgumentException e){
        return ResponseEntity.badRequest().body(new IllegalArgRes(e.getMessage(), e.getLocalizedMessage()));
    }
    @ExceptionHandler(RuntimeException.class)
    public ResponseEntity<RuntimeExceptionRes> handleRuntimeException(RuntimeException e){
        return ResponseEntity.unprocessableEntity().body(new RuntimeExceptionRes(e.getMessage(), e.getLocalizedMessage()));
    }
}
