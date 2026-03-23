package top.tqx.week03.controller;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import top.tqx.week03.common.Result;

import java.util.Map;

@RestController
@RequestMapping("/config")
public class BaseConfigController {
    @Value("${server.port}")
    private Integer port;
    @Value("${server.servlet.context-path}")
    private String contextPath;
    @Value("${spring.application.name}")
    private String springName;
    @Value("${app.app-name}")
    private String appName;
    @Value("${app.version}")
    private String version;
    @Value("${app.description}")
    private String description;
    @Value("${app.published}")
    private Boolean published;

    @GetMapping
    public Result<Map<String,Object>> getBaseConfigInfo(){
        return Result.success(
                Map.of("port",port,
                        "contextPath",contextPath,
                        "springName",springName,
                        "appName",appName,
                        "version",version,
                        "description",description,
                        "published",published
                )
        );
    }


}
