package cl.nisum.usuarios.config;

import lombok.Data;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Configuration;

@Data
@Configuration
public class CredentialsConfig {

    @Value("${nissum.credentials.secret}")
    private String secret;

}
