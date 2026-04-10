package top.tqx.week05.controller;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import top.tqx.week05.common.Result;
import top.tqx.week05.entity.User;

import java.time.LocalDateTime;

@Tag(name = "测试接口", description = "测试接口")
@RestController
@RequestMapping("/api/test")
public class TestController {

    @Operation(summary = "获取测试用户", description = "获取测试用户数据接口")
    @GetMapping("/user")
    public Result<User> getUser() {
        User user = new User();
        user.setId(1L);
        user.setUsername("test");
        user.setPassword("123456");
        user.setAge(18);
        user.setEmail("test@qq.com");
        user.setCreateTime(LocalDateTime.now());
        return Result.success( "成功", user);
    }
}
