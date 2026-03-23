package top.tqx.springbootweek02.services;

import jakarta.annotation.Resource;
import lombok.extern.slf4j.Slf4j;
import org.junit.jupiter.api.Test;
import org.springframework.boot.test.context.SpringBootTest;
import top.tqx.springbootweek02.dto.StudentDTO;
import top.tqx.springbootweek02.dto.StudentUpdateDTO;
import top.tqx.springbootweek02.enums.GenderEnum;
import top.tqx.springbootweek02.vo.StudentVO;

import java.time.LocalDate;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

@SpringBootTest
@Slf4j
class StudentServiceTest {

    @Resource
    public StudentService studentService;

    @Test
    void getAllStudents() {
        List<StudentVO> allStudents = studentService.getAllStudents();
        allStudents.forEach(studentVO -> log.info("{}", studentVO));
    }

    @Test
    void addStudent() {
        studentService.addStudent(StudentDTO.builder()
                .name("mqxu")
                .mobile("12345678901")
                .gender(GenderEnum.MALE)
                .avatar("https://mqxu.top/avatar.jpg")
                .birthday(LocalDate.of(1999, 1, 1))
                .build());
        log.info("添加成功");
        List<StudentVO> allStudents = studentService.getAllStudents();
        allStudents.forEach(studentVO -> log.info("{}", studentVO));
    }

    @Test
    void getStudent() {
        List<StudentVO> studentVO = studentService.getStudentById(1001L);
        log.info("{}", studentVO);
    }

    @Test
    void getStudentByName() {
        List<StudentVO> studentVO = studentService.getStudentByName("张");
        studentVO.forEach(studentV01 -> log.info("{}", studentV01));
    }

    @Test
    void updateStudent() {
        studentService.updateStudent(1001L, StudentUpdateDTO.builder()
                .name("张三111")
                .mobile("12345678901")
                .avatar("https://mqxu.top/new.jpg")
                .build());
        log.info("修改成功");
        List<StudentVO> studentVO = studentService.getStudentById(1001L);
        log.info("{}", studentVO);
    }

    @Test
    void deleteStudent() {
        studentService.deleteStudent(1001L);
        log.info("删除成功");
        List<StudentVO> allStudents = studentService.getAllStudents();
        allStudents.forEach(studentVO -> log.info("{}", studentVO));
    }
}