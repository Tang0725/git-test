package top.tqx.week04.controller;


import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import top.tqx.week04.common.Result;
import top.tqx.week04.entity.User;

import java.time.LocalDateTime;

@RestController
@RequestMapping("/api/user")
public class UserController {

    @GetMapping("/info")
    public Result<User> getUserInfo(){
        User user = new User();
        user.setId(1234567890123456789L);
        user.setUsername("springmvc-student");
        user.setCreatedTime(LocalDateTime.now());
        return Result.success(user);
    }
}
