package top.tqx.springbootweek02.controller;


import jakarta.annotation.Resource;
import org.springframework.web.bind.annotation.*;
import top.tqx.springbootweek02.dto.StudentDTO;
import top.tqx.springbootweek02.dto.StudentUpdateDTO;
import top.tqx.springbootweek02.services.StudentService;
import top.tqx.springbootweek02.vo.StudentVO;

import java.util.List;

@RestController
@RequestMapping("/api/v1/students")
public class StudentController {

    @Resource
    private StudentService studentService;

    @GetMapping("/values")
    public List<StudentVO> list() {
        return studentService.getAllStudents();
    }

    @GetMapping("/{id}")
    public List<StudentVO> getStudent(@PathVariable Long id) {
        return studentService.getStudentById(id);
    }

    @DeleteMapping("/{id}")
    public void deleteStudent(@PathVariable Long id) {
        studentService.deleteStudent(id);
    }

    @GetMapping()
    public List<StudentVO> getStudentByName(@RequestParam String name) {
        return studentService.getStudentByName(name);
    }

    @PostMapping()
    public String addStudent(@RequestBody StudentDTO studentAddDTO) {
        studentService.addStudent(studentAddDTO);
        return "添加成功";
    }

    @PutMapping("/{id}")
    public String updateStudent(@PathVariable Long id, @RequestBody StudentUpdateDTO studentUpdateDTO) {
        studentService.updateStudent(id, studentUpdateDTO);
        return "修改成功";
    }
}
