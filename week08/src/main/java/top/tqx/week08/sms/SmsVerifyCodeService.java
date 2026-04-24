package top.tqx.week08.sms;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import top.tqx.week08.sms.dto.SendCodeResponse;
import top.tqx.week08.util.RedisUtil;

import java.util.Objects;
import java.util.concurrent.ThreadLocalRandom;
import java.util.concurrent.TimeUnit;

/**
 * 短信验证码：使用 Redis String 存验证码，并设置过期时间，校验成功即删 key。
 * <p>
 * 设计说明与 HTTP 约定见项目文档：{@code docs/Redis场景设计-短信验证码.md}。
 */
@Service
@RequiredArgsConstructor
public class SmsVerifyCodeService {

    /** 验证码有效时间（秒） */
    public static final int DEFAULT_TTL_SECONDS = 5 * 60;

    private final RedisUtil redisUtil;

    /**
     * 生成验证码并写入 Redis。生产环境应在调用短信平台发送后或发送成功后再落库/写缓存，此处为模拟发短信
     */
    public SendCodeResponse sendCode(String phone) {
        String code = String.format("%06d", ThreadLocalRandom.current().nextInt(1_000_000));
        String key = SmsRedisKey.ofPhone(phone);
        redisUtil.set(key, code, DEFAULT_TTL_SECONDS, TimeUnit.SECONDS);
        return new SendCodeResponse(phone, DEFAULT_TTL_SECONDS, code);
    }

    /**
     * 验证码正确则返回 true 并删除 key；错误或未过期/不存在则返回 false
     */
    public boolean validateCode(String phone, String input) {
        String key = SmsRedisKey.ofPhone(phone);
        Object raw = redisUtil.get(key);
        if (raw == null) {
            return false;
        }
        String cached = Objects.toString(raw, "");
        if (cached.isEmpty() || !cached.equals(input)) {
            return false;
        }
        redisUtil.delete(key);
        return true;
    }
}