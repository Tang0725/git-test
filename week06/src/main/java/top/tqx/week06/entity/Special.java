package top.tqx.week06.entity;

import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableName;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;

import java.time.LocalDateTime;


@Data
@TableName("t_special")
public class Special {
    @Schema(description = "主键")
    private String id;

    @Schema(description = "标题")
    private String title;

    @Schema(description = "封面")
    @TableField("banner_url")
    private String banner;

    @Schema(description = "描述")
    private String introduction;

    @Schema(description = "是否关注")
    @TableField("is_following")
    private String isFollowing;

    @Schema(description = "关注者数量")
    @TableField("followers_count")
    private Integer followersCount;

    @Schema(description = "浏览数量")
    @TableField("view_count")
    private Integer viewCount;

    @Schema(description = "更新时间")
    @TableField("updated_date")
    private LocalDateTime updated;
}