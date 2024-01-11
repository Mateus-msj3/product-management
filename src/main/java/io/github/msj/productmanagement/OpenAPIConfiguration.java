package io.github.msj.productmanagement;

import io.swagger.v3.oas.annotations.enums.SecuritySchemeType;
import io.swagger.v3.oas.annotations.security.SecurityScheme;
import io.swagger.v3.oas.models.ExternalDocumentation;
import io.swagger.v3.oas.models.OpenAPI;
import io.swagger.v3.oas.models.info.Info;
import io.swagger.v3.oas.models.info.License;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
@SecurityScheme(
        name = "Bearer Authentication",
        type = SecuritySchemeType.HTTP,
        bearerFormat = "JWT",
        scheme = "bearer"
)
public class OpenAPIConfiguration {

//    @Bean
//    public OpenAPI customizeOpenAPI() {
//        final String securitySchemeName = "bearerAuth";
//        return new OpenAPI()
//                .addSecurityItem(new SecurityRequirement()
//                        .addList(securitySchemeName))
//                .components(new Components()
//                        .addSecuritySchemes(securitySchemeName, new SecurityScheme()
//                                .name(securitySchemeName)
//                                .type(SecurityScheme.Type.HTTP)
//                                .scheme("bearer")
//                                .bearerFormat("JWT")));
//    }

    @Bean
    public OpenAPI springOpenAPI() {
        final String securitySchemeName = "bearerAuth";
        return new OpenAPI()
                .info(new Info().title("SpringBoot API")
                        .description("SpringBoot sample application")
                        .version("v0.0.1")
                        .license(new License().name("Apache 2.0").url("http://springdoc.org")))
                .externalDocs(new ExternalDocumentation()
                        .description("SpringBoot Wiki Documentation")
                        .url("https://springboot.wiki.github.org/docs"));
    }

//    @Bean
//    public OpenAPI customOpenAPI() {
//        Map<String, Schema> schemas = new HashMap<>();
//        schemas.put("ProdutoFiltroDTO", new Schema<>()
//                .example(new Example().value(new ProdutoFiltroDTO(null, null, null,
//                        null, null, null, null, null,
//                        null, null, null, null, null)))).jsonSchemaImpl(ProdutoFiltroDTO.class);
//        return new OpenAPI().components(new Components().schemas(schemas));
//    }
}
