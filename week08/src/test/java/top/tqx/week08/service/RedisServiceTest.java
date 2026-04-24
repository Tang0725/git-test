package top.tqx.week08.service;

import jakarta.annotation.Resource;
import lombok.extern.slf4j.Slf4j;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.data.redis.core.StringRedisTemplate;
import top.tqx.week08.entity.Address;
import top.tqx.week08.entity.User;

import java.util.concurrent.TimeUnit;

@Slf4j
@SpringBootTest
class RedisServiceTest {
    @Resource
    private StringRedisTemplate stringRedisTemplate;
    @Autowired
    private RedisTemplate<String, Object> redisTemplate;

    @Test
    void testStringTemplate() throws Exception {
        stringRedisTemplate.opsForValue().set("name", "tqx", 10, TimeUnit.SECONDS);
        stringRedisTemplate.opsForValue().set("code: 18451915164","1234");
        stringRedisTemplate.opsForValue().set("code: 18451915165","5678");

        String value = stringRedisTemplate.opsForValue().get("name");
        log.info("Redis 字符串测试结果：{}",value);
        String code = stringRedisTemplate.opsForValue().get("code: 18451915164");
        log.info("18451915164 验证码测试结果：{}",code);
        String code1 = stringRedisTemplate.opsForValue().get("code: 18451915165");
        log.info("18451915165 验证码测试结果：{}",code1);

        Address address = new Address();
        address.setCity("镇江市");
        address.setStreet("xxxxxxxxxxx");
        address.setZipCode("212000");

        User user = new User();
        user.setName("汤齐星");
        user.setAge("21");
        user.setEmail("3501822088@qq.com");
        user.setAddress(address);

        redisTemplate.opsForValue().set("user:001", user);

    }
}