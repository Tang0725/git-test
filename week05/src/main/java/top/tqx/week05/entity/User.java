package top.tqx.week05.entity;


import java.util.Date;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;

@Data
public class User {
    @Schema(description="主键")
    private Long id;
    @Schema(description="用户名")
    private String username;
    @Schema(description="密码")
    private String password;
    @Schema(description="年龄")
    private Integer age;
    @Schema(description="邮箱")
    private String email;
    @Schema(description="创建时间")
    private Date createTime;
}
