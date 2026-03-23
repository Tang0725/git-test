package top.tqx.springbootweek02.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import top.tqx.springbootweek02.enums.GenderEnum;

import java.time.LocalDate;
@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class StudentDTO {
    private String name;
    private String mobile;
    private GenderEnum gender;
    private String avatar;
    private LocalDate birthday;
}
