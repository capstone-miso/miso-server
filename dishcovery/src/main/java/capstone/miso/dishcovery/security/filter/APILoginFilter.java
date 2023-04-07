package capstone.miso.dishcovery.security.filter;

import com.google.gson.Gson;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.extern.log4j.Log4j2;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.web.authentication.AbstractAuthenticationProcessingFilter;

import java.io.IOException;
import java.io.InputStreamReader;
import java.io.Reader;
import java.util.Map;

/**
 * author        : duckbill413
 * date          : 2023-03-05
 * description   :
 **/
@Log4j2
public class APILoginFilter extends AbstractAuthenticationProcessingFilter {
    public APILoginFilter(String defaultFilterProcessUrl){
        super(defaultFilterProcessUrl);
    }
    @Override
    public Authentication attemptAuthentication(HttpServletRequest request, HttpServletResponse response) throws AuthenticationException, IOException, ServletException {
        log.info("-------------------APILoginFilter-----------------------");

        if(request.getMethod().equalsIgnoreCase("GET")){
            log.info("GET METHOD NOT SUPPORT");
            return null;
        }

        log.info("-----------------------------------------");
        log.info(request.getMethod());

        Map<String, String> jsonData = parseRequestJSON(request);

        log.info("jsonData: "+jsonData);

        UsernamePasswordAuthenticationToken authenticationToken
                = new UsernamePasswordAuthenticationToken(
                jsonData.get("mid"),
                jsonData.get("mpw"));

        return getAuthenticationManager().authenticate(authenticationToken);
    }

    private Map<String, String> parseRequestJSON(HttpServletRequest request){
        // JSON 데이터를 분석해서 mid, mpw 전달 값을 Map 으로 처리
        try(Reader reader = new InputStreamReader(request.getInputStream())) {
            Gson gson = new Gson();

            return gson.fromJson(reader, Map.class);
        } catch (IOException e) {
//            throw new RuntimeException(e);
            log.error(e.getMessage());
        }

        return null;
    }
}
