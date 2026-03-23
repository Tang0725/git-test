package top.tqx.week03.config;

import lombok.Data;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.context.annotation.Configuration;
import org.springframework.stereotype.Component;

import java.util.List;

@Data
//@Component 不知道具体语义时使用Component
@Component
//@Configuration 告诉SpringBoot这是一个配置类
@ConfigurationProperties(prefix = "app")
public class AppProperties {
    private String appName;
    private String version;
    private String description;
    private Boolean published;
    private Author author;
    private List<String> features;

    @Data
    public static class Author {
        private String name;
        private String website;
        private String email;
    }
}

