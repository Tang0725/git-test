package top.tqx.springbootweek02.services;

import org.springframework.stereotype.Service;
import top.tqx.springbootweek02.dto.StudentDTO;
import top.tqx.springbootweek02.dto.StudentUpdateDTO;
import top.tqx.springbootweek02.entity.Student;
import top.tqx.springbootweek02.enums.GenderEnum;
import top.tqx.springbootweek02.vo.StudentVO;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

@Service

public class StudentService {
    private static final Map<Long, Student> STUDENT_DATA = new ConcurrentHashMap<>();

    static {

        Student stu1 = Student.builder()
                .id(1001L)
                .name("张三")
                .gender(GenderEnum.MALE)
                .birthday(LocalDate.of(2000, 1, 1))
                .mobile("12345678911")
                .createTime(LocalDateTime.now())
                .build();
        Student stu2 = Student.builder()
                .id(1002L)
                .name("李四")
                .gender(GenderEnum.FEMALE)
                .birthday(LocalDate.of(2000, 1, 2))
                .mobile("12345678910")
                .createTime(LocalDateTime.now())
                .build();
        STUDENT_DATA.put(stu1.getId(), stu1);
        STUDENT_DATA.put(stu2.getId(), stu2);
    }

    public List<StudentVO> getAllStudents() {
        List<StudentVO> list = new ArrayList<>();
        STUDENT_DATA.values().forEach(student -> {
            list.add(StudentVO.builder()
                    .id(student.getId())
                    .name(student.getName())
                    .gender(student.getGender())
                    .createTime(student.getCreateTime())
                    .build());
        });
        return list;
    }

    public void addStudent(StudentDTO studentDTO) {
        Student student = Student.builder()
                .id(System.currentTimeMillis())
                .name(studentDTO.getName())
                .gender(studentDTO.getGender())
                .birthday(studentDTO.getBirthday())
                .mobile(studentDTO.getMobile())
                .createTime(LocalDateTime.now())
                .build();
        STUDENT_DATA.put(student.getId(), student);
    }

    public List<StudentVO> getStudentById(Long id) {
        List<StudentVO> list = new ArrayList<>();
        STUDENT_DATA.values().forEach(student -> {
            if (student.getId().equals(id)) {
                list.add(StudentVO.builder()
                        .id(student.getId())
                        .name(student.getName())
                        .gender(student.getGender())
                        .createTime(student.getCreateTime())
                        .build());
            }
        });
        return list;
    }
    public List<StudentVO> getStudentByName(String name) {
        List<StudentVO> list = new ArrayList<>();
        STUDENT_DATA.values().forEach(student -> {
            if (student.getName().contains(name)) {
                list.add(StudentVO.builder()
                        .id(student.getId())
                        .name(student.getName())
                        .gender(student.getGender())
                        .createTime(student.getCreateTime())
                        .build());
            }
        });
        return list;
    }

    public void updateStudent(Long id, StudentUpdateDTO studentUpdateDTO) {
        Student student = STUDENT_DATA.get(id);
        student.setName(studentUpdateDTO.getName());
        student.setMobile(studentUpdateDTO.getMobile());
        student.setAvatar(studentUpdateDTO.getAvatar());
    }

    public void deleteStudent(Long id) {
        STUDENT_DATA.remove(id);
    }

}
