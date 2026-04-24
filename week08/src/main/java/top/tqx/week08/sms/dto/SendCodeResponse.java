package top.tqx.week08.sms.dto;

/**
 * 联调/演示可返回 codePlain，正式环境应调用短信通道且不返回验证码明文。
 */
public record SendCodeResponse(
        String phone,
        int ttlSeconds,
        String codePlain
) {
}