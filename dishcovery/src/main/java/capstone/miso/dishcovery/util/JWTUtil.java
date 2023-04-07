package capstone.miso.dishcovery.util;

import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureAlgorithm;
import lombok.extern.log4j.Log4j2;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

import java.sql.Date;
import java.time.ZonedDateTime;
import java.util.HashMap;
import java.util.Map;

/**
 * author        : duckbill413
 * date          : 2023-03-05
 * description   :
 **/
@Component
@Log4j2
public class JWTUtil {
    @Value(value = "${key.workbook.jwt.secret}")
    private String key;

    public String generateToken(Map<String, Object> valueMap, int days){
        // header
        Map<String, Object> headers = new HashMap<>();
        headers.put("typ", "JWT");
        headers.put("alg", "HS256");

        // payload 부분 설정
        Map<String, Object> payloads = new HashMap<>();
        payloads.putAll(valueMap);

        // 유효 시간 (단위: DAY)
        int time = (24 * 60) * days;

        String jwtStr = Jwts.builder()
                .setHeader(headers)
                .setClaims(payloads)
                .setIssuedAt(Date.from(ZonedDateTime.now().toInstant()))
                .setExpiration(Date.from(ZonedDateTime.now().plusMinutes(time).toInstant()))
                .signWith(SignatureAlgorithm.HS256, key.getBytes())
                .compact();

        log.info("generated JWT KEY: " + jwtStr);
        return jwtStr;
    }
    public Map<String, Object> validateToken(String token){
        Map<String, Object> claim = null;

        claim = Jwts.parser()
                .setSigningKey(key.getBytes()) // Set Key
                .parseClaimsJws(token) // 파싱 및 검증, 실패 시 에러
                .getBody();
        return claim;
    }
}
