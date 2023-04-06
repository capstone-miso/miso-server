package capstonedishcovery.data.application.config;

import io.swagger.v3.oas.models.Components;
import io.swagger.v3.oas.models.OpenAPI;
import io.swagger.v3.oas.models.info.Info;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

/**
 * author        : duckbill413
 * date          : 2023-03-01
 * description   :
 **/
@Configuration
public class SwaggerConfig {
    private final String JWT = "JWT";
    @Bean
    public OpenAPI openAPI(){
        return new OpenAPI()
                .components(new Components())
                .info(this.info());
    }

    private Info info(){
        Info info = new Info()
                .title("Boot 01 Project Swagger")
                .version("v0.0.1")
                .description("자바 웹 개발 워크북 실습 내용");
        return info;
    }

    /* Swagger JWT 적용
    @Bean
    public OpenAPI openAPI() {
        SecurityRequirement securityRequirement = new SecurityRequirement().addList(JWT); // 헤더에 토큰 포함
        Components components = new Components().addSecuritySchemes(jwt, new SecurityScheme()
                .name(JWT)
                .type(SecurityScheme.Type.HTTP)
                .scheme("bearer")
                .bearerFormat("JWT")
        );

        return new OpenAPI()
                .info(this.info())
                .addSecurityItem(securityRequirement)
                .components(components);
    }

     */
}
