package capstone.miso.dishcovery.security.exception;

import com.google.gson.Gson;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;

import java.io.IOException;
import java.util.Date;
import java.util.Map;

/**
 * author        : duckbill413
 * date          : 2023-03-06
 * description   :
 **/

public class RefreshTokenException extends Exception{
    private ErrorCase errorCase;

    public enum ErrorCase{
        NO_ACCESS, BAD_ACCESS, NO_REFRESH, OLD_REFRESH, BAD_REFRESH
    }
    public RefreshTokenException(ErrorCase errorCase){
        super(errorCase.name());
        this.errorCase = errorCase;
    }
    public void sendResponseError(HttpServletResponse response){
        response.setStatus(HttpStatus.UNAUTHORIZED.value());
        response.setContentType(MediaType.APPLICATION_JSON_VALUE);

        Gson gson = new Gson();

        String responseStr = gson.toJson(Map.of("msg", errorCase.name(),
                "time", new Date()));

        try {
            response.getWriter().println(responseStr);
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }
}
