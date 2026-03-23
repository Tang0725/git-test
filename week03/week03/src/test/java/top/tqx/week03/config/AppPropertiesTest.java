package top.tqx.week03.config;

import jakarta.annotation.Resource;
import lombok.extern.slf4j.Slf4j;
import org.junit.jupiter.api.Test;
import org.springframework.boot.test.context.SpringBootTest;

import static org.junit.jupiter.api.Assertions.*;

@Slf4j
@SpringBootTest
class AppPropertiesTest {

    //注入 AppConfig 实例
    @Resource
    private AppProperties appConfig;

    @Test
    public void getAppConfigInfo() {
        log.info("appName: {}", appConfig.getAppName());
        log.info("version: {}", appConfig.getVersion());
        log.info("description: {}", appConfig.getDescription());
        log.info("published: {}", appConfig.getPublished());
        log.info("author: {}", appConfig.getAuthor().getName());
        log.info("features: {}", appConfig.getFeatures());

    }
}