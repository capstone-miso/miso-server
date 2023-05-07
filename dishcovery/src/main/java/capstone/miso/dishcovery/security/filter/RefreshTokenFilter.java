package capstone.miso.dishcovery.security.filter;

import capstone.miso.dishcovery.security.exception.AccessTokenException;
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

import static org.springframework.web.bind.annotation.RequestMethod.POST;

/**
 * author        : duckbill413
 * date          : 2023-04-26
 * description   :
 **/
@Log4j2
@RequiredArgsConstructor
public class RefreshTokenFilter extends OncePerRequestFilter {
    private final JWTUtil jwtUtil;
    @Override
    protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain filterChain) throws ServletException, IOException {
        String path = request.getRequestURI();

        if (!path.equalsIgnoreCase("/refreshToken")){
            filterChain.doFilter(request, response);
            return;
        } else if (request.getMethod().equalsIgnoreCase("GET")) {
            response.getWriter().println("GET Method not support");
            filterChain.doFilter(request, response);
            return;
        }

        Map<String, String> tokens = parseRequestJSON(request);

        String accessToken = tokens.get("accessToken");
        String refreshToken = tokens.get("refreshToken");

        try {
            checkAccessToken(accessToken);
        } catch (RefreshTokenException re){
            re.sendResponseError(response);
            return;
        }
        Map<String, Object> refreshClaims = null;
        try {
            refreshClaims = checkRefreshToken(refreshToken);
        } catch (RefreshTokenException re){
            re.sendResponseError(response);
            return;
        }

        // Refresh token expire time check
        Integer exp = (Integer) refreshClaims.get("exp");

        Date expTime = new Date(Instant.ofEpochMilli(exp).toEpochMilli() * 1000);

        Date current = new Date(System.currentTimeMillis());

        //만료 시간과 현재 시간의 간격 계산
        //만일 3일 미만인 경우에는 Refresh Token도 다시 생성
        long gapTime = (expTime.getTime() - current.getTime());

        String email = (String) refreshClaims.get("email");

        //이상태까지 오면 무조건 AccessToken은 새로 생성
        String accessTokenValue = jwtUtil.generateToken(Map.of("email", email), 5);

        String refreshTokenValue = tokens.get("refreshToken");

        //RefreshToken이 EXPIRE_DATE 이내로 남았다면 RefreshToken도 재발급
        if(gapTime < ((1000 * 60 * 60 * 24) * 5) ){
            refreshTokenValue = jwtUtil.generateToken(Map.of("email", email), 30);
        }

        this.sendTokens(accessTokenValue, refreshTokenValue, response);
    }
    private Map<String, String> parseRequestJSON(HttpServletRequest request){
        try(Reader reader = new InputStreamReader(request.getInputStream())){
            Gson gson = new Gson();
            return gson.fromJson(reader, Map.class);
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }
    private void checkAccessToken(String accessToken) throws RefreshTokenException {
        try {
            jwtUtil.validateToken(accessToken);
        } catch (ExpiredJwtException expiredJwtException){
            throw new AccessTokenException(AccessTokenException.TOKEN_ERROR.EXPIRED);
        } catch (Exception exception){
            throw new RefreshTokenException(RefreshTokenException.ErrorCase.NO_ACCESS);
        }
    }
    private Map<String, Object> checkRefreshToken(String refreshToken) throws RefreshTokenException {
        try {
            Map<String, Object> values = jwtUtil.validateToken(refreshToken);
            return values;
        } catch (ExpiredJwtException expiredJwtException){
            throw new RefreshTokenException(RefreshTokenException.ErrorCase.OLD_REFRESH);
        } catch (Exception exception){
            throw new RefreshTokenException(RefreshTokenException.ErrorCase.NO_REFRESH);
        }
    }
    private void sendTokens(String accessTokenValue, String refreshTokenValue, HttpServletResponse response){
        response.setHeader("Authorization", "Bearer " + accessTokenValue);
        response.setHeader("Refresh-Token", refreshTokenValue);
    }
}
