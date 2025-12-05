package jsz.myapp.categorized_notes_app.config;

import io.swagger.v3.oas.annotations.OpenAPIDefinition;
import io.swagger.v3.oas.annotations.info.Contact;
import io.swagger.v3.oas.annotations.info.Info;
import io.swagger.v3.oas.annotations.info.License;
import org.springframework.context.annotation.Configuration;

@Configuration
@OpenAPIDefinition(
        info = @Info(
                title = "API de Notas con autenticación.",
                version = "1.0.0",
                description = "Documentación de la API para autenticación y gestión de notas con autenticación.",
                contact = @Contact(
                        name = "Juan Simón Zubigaray",
                        email = "zubigarayjuansimon@gmail.com",
                        url = "https://github.com/simon-zubigaray"
                ),
                license = @License(
                        name = "MIT License",
                        url = "https://opensource.org/licenses/MIT"
                )
        )
)
public class SwaggerConfig {
}