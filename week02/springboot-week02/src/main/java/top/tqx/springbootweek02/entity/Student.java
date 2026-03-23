package top.tqx.springbootweek02.entity;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import top.tqx.springbootweek02.enums.GenderEnum;

import java.sql.Time;
import java.time.LocalDate;
import java.time.LocalDateTime;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class Student {
    private Long id;
    private String name;
    private GenderEnum gender;
    private String avatar;
    private LocalDate birthday;
    private String mobile;
    private LocalDateTime createTime;
}
