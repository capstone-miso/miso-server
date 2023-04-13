package capstone.miso.dishcovery.application.config;

import io.swagger.v3.oas.models.Components;
import io.swagger.v3.oas.models.ExternalDocumentation;
import io.swagger.v3.oas.models.OpenAPI;
import io.swagger.v3.oas.models.info.Info;
import io.swagger.v3.oas.models.info.License;
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

    private Info info(){
        Info info = new Info()
                .title("Dishcovery")
                .version("v0.0.1")
                .description("Capstone design team. miso's project");
        return info;
    }
    @Bean
    public OpenAPI openAPI(){
        return new OpenAPI()
                .components(new Components())
                .info(this.info())
                .externalDocs(new ExternalDocumentation()
                        .description("Springdoc-openapi 문서 보러가기")
                        .url("http://springdoc.org"));
    }

//    @Bean
//    public OpenAPI openAPI() {
//        SecurityRequirement securityRequirement = new SecurityRequirement().addList(JWT); // 헤더에 토큰 포함
//        Components components = new Components().addSecuritySchemes(JWT, new SecurityScheme()
//                .name(JWT)
//                .type(SecurityScheme.Type.HTTP)
//                .scheme("bearer")
//                .bearerFormat("JWT")
//        );
//
//        return new OpenAPI()
//                .info(this.info())
//                .addSecurityItem(securityRequirement)
//                .components(components);
//    }
}
