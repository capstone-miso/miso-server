package capstone.miso.dishcovery.security.handler;

import capstone.miso.dishcovery.util.JWTUtil;
import com.google.gson.Gson;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;
import lombok.extern.log4j.Log4j2;
import org.springframework.http.MediaType;
import org.springframework.security.core.Authentication;
import org.springframework.security.web.authentication.AuthenticationSuccessHandler;

import java.io.IOException;
import java.util.Map;

/**
 * author        : duckbill413
 * date          : 2023-03-05
 * description   :
 **/
@Log4j2
@RequiredArgsConstructor
public class APILoginSuccessHandler implements AuthenticationSuccessHandler {
    private final int ACCESS_EXPIRE_DATE;
    private final int REFRESH_EXPIRE_DATE ;
    private final JWTUtil jwtUtil;
    @Override
    public void onAuthenticationSuccess(HttpServletRequest request, HttpServletResponse response, Authentication authentication) throws IOException, ServletException {
        log.info("-------------------APILoginSuccessHandler-----------------------");
        response.setContentType(MediaType.APPLICATION_JSON_VALUE);

        Map<String, Object> claim = Map.of("mid", authentication.getName());
        // Access Token
        String accessToken = jwtUtil.generateToken(claim, ACCESS_EXPIRE_DATE);
        // Refresh Token
        String refreshToken = jwtUtil.generateToken(claim, REFRESH_EXPIRE_DATE);

        Gson gson = new Gson();
        Map<String, String> keyMap = Map.of(
                "accessToken", accessToken,
                "refreshToken", refreshToken
        );

        String jsonStr = gson.toJson(keyMap);
        response.getWriter().println(jsonStr);
    }
}
