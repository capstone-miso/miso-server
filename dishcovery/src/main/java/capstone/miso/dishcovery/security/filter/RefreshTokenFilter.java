package capstone.miso.dishcovery.security.filter;

import capstone.miso.dishcovery.security.exception.RefreshTokenException;
import capstone.miso.dishcovery.util.JWTUtil;
import com.google.gson.Gson;
import io.jsonwebtoken.ExpiredJwtException;
import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;
import lombok.extern.log4j.Log4j2;
import org.springframework.http.MediaType;
import org.springframework.web.filter.OncePerRequestFilter;

import java.io.IOException;
import java.io.InputStreamReader;
import java.io.Reader;
import java.time.Instant;
import java.util.Date;
import java.util.Map;

/**
 * author        : duckbill413
 * date          : 2023-03-06
 * description   :
 **/
@Log4j2
@RequiredArgsConstructor
public class RefreshTokenFilter extends OncePerRequestFilter {
    private final int ACCESS_EXPIRE_DATE;
    private final int REFRESH_EXPIRE_DATE;
    private final int EXPIRE_DAY;
    private final String refreshPath;
    private final JWTUtil jwtUtil;

    @Override
    protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain filterChain) throws ServletException, IOException {
        log.info("-------------------RefreshTokenFilter-----------------------");
        String path = request.getRequestURI();

        if (!path.equals(refreshPath)) {
            filterChain.doFilter(request, response);
            return;
        }

        Map<String, String> tokens = parseRequestJSON(request);

        String accessToken = tokens.get("accessToken");
        String refreshToken = tokens.get("refreshToken");

        try {
            checkAccessToken(accessToken);
        } catch (RefreshTokenException e) {
            e.sendResponseError(response);
            return;
        }

        Map<String, Object> refreshClaims = null;
        try {
            refreshClaims = checkRefreshToken(refreshToken);
        } catch (RefreshTokenException e) {
            e.sendResponseError(response);
            return;
        }
        //Refresh Token의 유효시간이 얼마 남지 않은 경우
        Integer exp = (Integer) refreshClaims.get("exp");

        Date expTime = new Date(Instant.ofEpochMilli(exp).toEpochMilli() * 1000);

        Date current = new Date(System.currentTimeMillis());

        //만료 시간과 현재 시간의 간격 계산
        //만일 3일 미만인 경우에는 Refresh Token도 다시 생성
        long gapTime = (expTime.getTime() - current.getTime());

        String mid = (String) refreshClaims.get("mid");

        //이상태까지 오면 무조건 AccessToken은 새로 생성
        String accessTokenValue = jwtUtil.generateToken(Map.of("mid", mid), ACCESS_EXPIRE_DATE);

        String refreshTokenValue = tokens.get("refreshToken");

        //RefreshToken이 EXPIRE_DATE 이내로 남았다면 RefreshToken도 재발급
        if(gapTime < ((1000 * 60 * 60 * 24) * EXPIRE_DAY) ){
            refreshTokenValue = jwtUtil.generateToken(Map.of("mid", mid), REFRESH_EXPIRE_DATE);
        }

        this.sendTokens(accessTokenValue, refreshTokenValue, response);
    }

    private Map<String, String> parseRequestJSON(HttpServletRequest request) {
        try (Reader reader = new InputStreamReader(request.getInputStream())) {
            Gson gson = new Gson();
            return gson.fromJson(reader, Map.class);
        } catch (IOException e) {
            log.error(e.getMessage());
        }
        return null;
    }

    private void checkAccessToken(String accessToken) throws RefreshTokenException {

        try {
            jwtUtil.validateToken(accessToken);
        } catch (ExpiredJwtException expiredJwtException) {
            log.info("Access Token has expired");
        } catch (Exception exception) {
            throw new RefreshTokenException(RefreshTokenException.ErrorCase.NO_ACCESS);
        }
    }

    private Map<String, Object> checkRefreshToken(String refreshToken) throws RefreshTokenException {

        try {
            Map<String, Object> values = jwtUtil.validateToken(refreshToken);

            return values;

        } catch (ExpiredJwtException expiredJwtException) {
            throw new RefreshTokenException(RefreshTokenException.ErrorCase.OLD_REFRESH);
        } catch (Exception exception) {
            exception.printStackTrace();
            new RefreshTokenException(RefreshTokenException.ErrorCase.NO_REFRESH);
        }
        return null;
    }

    private void sendTokens(String accessTokenValue, String refreshTokenValue, HttpServletResponse response) {

        response.setContentType(MediaType.APPLICATION_JSON_VALUE);

        Gson gson = new Gson();

        String jsonStr = gson.toJson(Map.of("accessToken", accessTokenValue,
                "refreshToken", refreshTokenValue));

        try {
            response.getWriter().println(jsonStr);
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }
}