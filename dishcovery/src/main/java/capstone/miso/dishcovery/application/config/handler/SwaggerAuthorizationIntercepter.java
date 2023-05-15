package capstone.miso.dishcovery.application.config.handler;

import capstone.miso.dishcovery.util.JWTUtil;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;
import org.springframework.web.servlet.HandlerInterceptor;

import java.util.ArrayList;
import java.util.Map;

@Component
@RequiredArgsConstructor
public class SwaggerAuthorizationIntercepter implements HandlerInterceptor {
    private final JWTUtil jwtUtil;
    private static final String ADMIN_ROLE = "ROLE_ADMIN";
    private static final String AUTHORIZATION_HEADER = "Authorization";

    @Override
    public boolean preHandle(HttpServletRequest request, HttpServletResponse response, Object handler) throws Exception {
        String authorizationHeader = request.getHeader(AUTHORIZATION_HEADER);
        if (authorizationHeader == null) {
            response.sendError(HttpServletResponse.SC_UNAUTHORIZED, "Unauthorized");
            return false;
        }
        String[] parts = authorizationHeader.split(" ");
        if (parts.length != 2) {
            response.sendError(HttpServletResponse.SC_UNAUTHORIZED, "Unauthorized");
            return false;
        }
        String tokenType = parts[0];
        String token = parts[1];
        if (!"Bearer".equals(tokenType)) {
            response.sendError(HttpServletResponse.SC_UNAUTHORIZED, "Unauthorized");
            return false;
        }
        try {
            Map<String, Object> claims = jwtUtil.validateToken(token);
            Object roles = claims.getOrDefault("roles", null);
            for (Object role : (ArrayList) roles) {
                if (role.equals(ADMIN_ROLE)) {
                    return true;
                }
            }
            return false;

        } catch (Exception e) {
            response.sendError(HttpServletResponse.SC_UNAUTHORIZED, "Unauthorized");
            return false;
        }
    }
}
