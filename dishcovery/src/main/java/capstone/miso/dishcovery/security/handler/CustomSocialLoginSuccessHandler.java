package capstone.miso.dishcovery.security.handler;

import capstone.miso.dishcovery.security.dto.MemberSecurityDTO;
import capstone.miso.dishcovery.util.JWTUtil;
import com.google.gson.Gson;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;
import lombok.extern.log4j.Log4j2;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.core.env.Environment;
import org.springframework.http.MediaType;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.authentication.AuthenticationSuccessHandler;
import org.springframework.security.web.authentication.SimpleUrlAuthenticationSuccessHandler;
import org.springframework.web.util.UriComponentsBuilder;

import java.io.IOException;
import java.util.Map;

/**
 * author        : duckbill413
 * date          : 2023-04-26
 * description   :
 **/
@Log4j2
@RequiredArgsConstructor
public class CustomSocialLoginSuccessHandler extends SimpleUrlAuthenticationSuccessHandler {
    @Value("${gong.react.domain}")
    private String REACT_DOMAIN;
    private final JWTUtil jwtUtil;

    @Override
    public void onAuthenticationSuccess(HttpServletRequest request, HttpServletResponse response, Authentication authentication) throws IOException, ServletException {
        response.setContentType(MediaType.APPLICATION_JSON_VALUE);
        log.info("REACT_DOMAIN: " + REACT_DOMAIN);
        Map<String, Object> claim = Map.of("email", authentication.getName(),
                "roles", authentication.getAuthorities().stream().map(GrantedAuthority::getAuthority).toList());

        // Access token
        String accessToken = jwtUtil.generateToken(claim, 5);
        // Refresh token
        String refreshToken = jwtUtil.generateToken(claim, 30);

        response.getWriter().println(authentication.getName() + " Login Success");
        response.setHeader("Authorization", "Bearer " + accessToken);
        response.setHeader("Refresh-Token", refreshToken);
        if (response.isCommitted()) {
            logger.debug("Response has already been committed. Unable to redirect.");
            return;
        }
        String frontPage = UriComponentsBuilder.fromUriString(REACT_DOMAIN)
                .queryParam("Authorization",accessToken)
                .queryParam("Refresh-Token", refreshToken)
                .build().toUriString();
//        String frontPage = UriComponentsBuilder.fromUriString("http://localhost:3000/auth")
//                .queryParam("Authorization",accessToken)
//                .queryParam("Refresh-Token", refreshToken)
//                .build().toUriString();
        getRedirectStrategy().sendRedirect(request, response, frontPage);
    }
}
