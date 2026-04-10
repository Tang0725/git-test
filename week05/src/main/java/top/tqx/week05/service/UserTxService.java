package top.tqx.week05.service;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import top.tqx.week05.entity.User;
import top.tqx.week05.mapper.UserMapper;

@Service
@RequiredArgsConstructor
public class UserTxService {
    private final UserMapper userMapper;
    @Transactional
    public void addTwoUsers(User user1, User user2) {
        // 第一步：新增用户1
        userMapper.insert(user1);
        // 模拟异常：如果用户2的用户名为空，抛出运行时异常，触发事务回滚
        if (user2.getUsername() == null || user2.getUsername().isEmpty())
        {
            throw new RuntimeException("用户2姓名不能为空，事务回滚");
        }
        // 第二步：新增用户2（若上面抛出异常，此步骤不会执行，用户1也会被回滚）
        userMapper.insert(user2);
    }

}
