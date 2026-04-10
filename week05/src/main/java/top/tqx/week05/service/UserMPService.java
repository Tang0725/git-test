package top.tqx.week05.service;
import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import top.tqx.week05.entity.User;
import top.tqx.week05.mapper.UserMPMapper;

@Service
@RequiredArgsConstructor
public class UserMPService {
    private final UserMPMapper userMPMapper;

    public Page<User> page(String username, Integer pageNum, Integer pageSize) {
        // 1. 构造分页参数：pageNum（页码，从 1 开始）、pageSize（每页条数）
        Page<User> page = new Page<>(pageNum, pageSize);
        
        // 2. 构造条件构造器：LambdaQueryWrapper（类型安全，避免字段名写错）
        LambdaQueryWrapper<User> wrapper = new LambdaQueryWrapper<>();
        // 条件：用户名不为空时，模糊查询（第一个参数为条件，满足才拼接）
        if (username != null && !username.isEmpty()) {
            wrapper.like(User::getUsername, username);
        }
        
        // 3. 调用 BaseMapper 的 selectPage 方法，实现条件分页
        Page<User> pageResult = userMPMapper.selectPage(page, wrapper);
        
        return pageResult;
    }

    public int add(User user) {
        return userMPMapper.insert(user);
    }

    public int delete(Long id) {
        return userMPMapper.deleteById(id);
    }
}

