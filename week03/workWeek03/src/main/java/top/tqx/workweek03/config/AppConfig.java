package top.tqx.workweek03.config;

import jakarta.validation.constraints.NotBlank;
import lombok.Data;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.stereotype.Component;


@Data
@Component
@ConfigurationProperties(prefix = "app")
//@Validate
public class AppConfig {
//    @NotBlank
    private String name;
    private String version;
    private String description;
}
