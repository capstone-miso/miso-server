package capstone.miso.dishcovery.application.config.handler;

import capstone.miso.dishcovery.application.config.handler.SwaggerAuthorizationIntercepter;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.servlet.config.annotation.InterceptorRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;
@Configuration
@RequiredArgsConstructor
public class WebSwaggerMvcConfig implements WebMvcConfigurer {
    private final SwaggerAuthorizationIntercepter authorizationIntercepter;
    @Value("${springdoc.swagger-ui.path}")
    private String swaggerPath;

    @Override
    public void addInterceptors(InterceptorRegistry registry) {
        // MEMO: Swagger 실행 X 주석
//        registry.addInterceptor(authorizationIntercepter)
//                .addPathPatterns(swaggerPath)
//                .addPathPatterns("/v3/api-docs");
    }
}
