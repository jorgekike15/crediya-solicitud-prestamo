package co.com.pragma.config;

import io.swagger.v3.oas.models.OpenAPI;
import org.junit.jupiter.api.Test;
import io.swagger.v3.oas.models.info.Info;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;

class SwaggerConfigTest {
    @Test
    void customOpenAPIReturnsConfiguredOpenAPI() {
        SwaggerConfig config = new SwaggerConfig();
        OpenAPI openAPI = config.customOpenAPI();

        assertNotNull(openAPI);
        Info info = openAPI.getInfo();
        assertNotNull(info);
        assertEquals("API de Solicitudes", info.getTitle());
        assertEquals("0.0.1", info.getVersion());
        assertEquals("Documentación de la API de Solicitudes", info.getDescription());
        assertNotNull(info.getContact());
        assertEquals("jorgekike15@gmail.com", info.getContact().getEmail());
        assertEquals("Jorge Bejarano G", info.getContact().getName());
    }
}
