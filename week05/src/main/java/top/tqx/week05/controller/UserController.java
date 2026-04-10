package top.tqx.week05.controller;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;
import top.tqx.week05.common.Result;
import top.tqx.week05.entity.User;
import top.tqx.week05.service.UserService;
import java.util.List;

@Tag(name = "用户接口", description = "用户接口")
@RestController
@RequestMapping("/api/user")
@RequiredArgsConstructor
public class UserController {
    private final UserService userService;

    @Operation(summary = "新增用户", description = "新增用户接口")
    @PostMapping
    public Result<String> add(@RequestBody User user) {
        int row = userService.add(user);
        if (row != 1) {
            return Result.error("添加失败");
        }
        return Result.success("添加成功");
    }

    @Operation(summary = "获取用户", description = "根据 ID 获取用户接口")
    @GetMapping("/{id}")
    public Result<User> get(@PathVariable Long id) {
        return Result.success(userService.getById(id));
    }

    @Operation(summary = "获取用户列表", description = "获取所有用户列表接口")
    @GetMapping("/list")
    public Result<List<User>> list() {
        return Result.success(userService.list());
    }

    @Operation(summary = "更新用户", description = "更新用户接口")
    @PutMapping
    public Result<String> update(@RequestBody User user) {
        int row = userService.update(user);
        if (row != 1) {
            return Result.error("更新失败");
        }
        return Result.success("更新成功");
    }

    @Operation(summary = "删除用户", description = "根据 ID 删除用户接口")
    @DeleteMapping("/{id}")
    public Result<String> delete(@PathVariable Long id) {
        int row = userService.delete(id);
        if (row != 1) {
            return Result.error("删除失败");
        }
        return Result.success("删除成功");
    }

    @Operation(summary = "搜索用户", description = "根据用户名和最小年龄搜索用户接口")
    @GetMapping("/search")
    public List<User> search(
            @RequestParam(required = false) String username,
            @RequestParam(required = false) Integer minAge) {
        return userService.search(username, minAge);
    }


}
