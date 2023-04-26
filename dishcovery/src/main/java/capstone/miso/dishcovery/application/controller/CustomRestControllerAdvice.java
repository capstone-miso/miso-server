package capstone.miso.dishcovery.application.controller;

import capstone.miso.dishcovery.domain.member.exception.MemberValidationException;
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
    public ResponseEntity<Map<String, Object>> handleAccessDeniedException(AccessDeniedException e){
        Map<String, Object> errors = new HashMap<>();
        errors.put(e.getMessage(), "로그인을 확인 해주세요.");
        return ResponseEntity.badRequest().body(errors);
    }
    @ExceptionHandler(MemberValidationException.class)
    public ResponseEntity<Map<String, Object>> handleMemberValidationException(MemberValidationException e){
        Map<String, Object> errors = new HashMap<>();
        e.getBindingResult().getFieldErrors().forEach(fieldError -> errors.put(fieldError.getField(), fieldError.getDefaultMessage()));

        return ResponseEntity.badRequest().body(errors);
    }
}
