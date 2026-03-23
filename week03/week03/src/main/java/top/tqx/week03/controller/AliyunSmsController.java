package top.tqx.week03.controller;
import com.aliyun.dypnsapi20170525.models.SendSmsVerifyCodeResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import top.tqx.week03.common.Result;
import top.tqx.week03.service.AliyunSmsService;

import java.util.Map;
@RestController
@RequestMapping("/sms")
@RequiredArgsConstructor
public class AliyunSmsController {
    private final AliyunSmsService smsService;
    /**
     * 发送验证码（阿里云自动生成，支持核验）
     *
     * 接口地址：POST /sms/send-code
     *
     * 请求体：
     * {
     * "phone": "13800138000"
     * }
     *
     * 响应：
     * {
     * "code": 200,
     * "message": "success",
     * "data": {
     * "message": "验证码已发送",
     * "bizId": "1234567890",
     * "outId": "abc123"
     * }
     * }
     */
    @PostMapping("/send-code")
    public Result<Map<String, String>> sendCode(@RequestBody Map<String,
            String> body) {
        String phone = body.get("phone");
        // 参数校验
        if (phone == null || phone.isBlank()) {
            return Result.error(400, "手机号不能为空");
        }
        try {
            // 发送验证码
            SendSmsVerifyCodeResponse response = smsService.sendVerifyCodeAuto(phone);
            // 判断发送结果
            if (response.getBody() != null && "OK".equals(response.getBody().getCode())) {
                var model = response.getBody().getModel();
                String bizId = model != null ? model.getBizId() : "";
                String outId = model != null ? model.getOutId() : "";
                return Result.success(Map.of(
                        "message", "验证码已发送",
                        "bizId", bizId != null ? bizId : "",
                        "outId", outId != null ? outId : ""
                ));
            }
            return Result.error(500, response.getBody() != null ?
                            response.getBody().getMessage() : "发送失败");
        } catch (Exception e) {
            return Result.error(500, "发送失败：" + e.getMessage());
        }
    }
    /**
     * 核验验证码
     *
     * 接口地址：POST /sms/verify
     *
     * 请求体：
     * {
     * "phone": "13800138000",
     * "code": "123456",
     * "outId": "abc123"
     * }
     *
     * 响应：
     * {
     * "code": 200,
     * "message": "success",
     * "data": true
     * }
     */
    @PostMapping("/verify")
    public Result<Boolean> verify(@RequestBody Map<String, String> body)
    {
        String phone = body.get("phone");
        String code = body.get("code");
        // 参数校验
        if (phone == null || phone.isBlank() || code == null || code.isBlank()) {
        return Result.error(400, "手机号和验证码不能为空");
    }
        try {
            // 核验验证码
            boolean pass = smsService.checkVerifyCode(phone, code, body.get("outId"));
            return Result.success(pass);
        } catch (Exception e) {
            return Result.error(500, "核验失败：" + e.getMessage());
        }
    }
}