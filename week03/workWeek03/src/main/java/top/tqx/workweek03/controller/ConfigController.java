package top.tqx.workweek03.controller;


import org.springframework.beans.factory.annotation.Value;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.Mapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import top.tqx.workweek03.resault.Result;

import java.util.Map;

@RestController
@RequestMapping("/config")
public class ConfigController {
    @Value("${app.name}")
    private String name;
    @Value("${app.version}")
    private String version;
    @Value("${app.description}")
    private String description;
    @Value("${app.database.host}")
    private String host;

    @Value("${server.port}")
    private String port;
    @Value("${server.servlet.context-path}")
    private String contextPath;

    @Value("${spring.application.name}")
    private String applicationName;

    @GetMapping("/app-info")
    public Result<Map<String, Object>> getConfig() {
        Map<String, Object> map = Map.of(
                "name", name,
                "version", version,
                "description", description,
                "port", port,
                "contextPath", contextPath,
                "applicationName", applicationName
        );
        return Result.success(map);
    }

    @GetMapping("/db-info")
    public Result<Map<String, Object>> getDbInfo() {
        Map<String, Object> map = Map.of(
                "host", host
        );
        return Result.success(map);
    }


}
