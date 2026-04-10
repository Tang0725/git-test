package top.tqx.week05.mapper;

import org.apache.ibatis.annotations.*;
import top.tqx.week05.entity.User;

import java.util.List;

@Mapper
public interface UserMapper {

    @Insert("INSERT INTO t_user(username,password,age,email) VALUES (#{username}, #{password}, #{age}, #{email})")
    @Options(useGeneratedKeys = true, keyProperty = "id")
    int insert(User record);
    @Select("SELECT * FROM t_user WHERE id = #{id}")
    User selectByPrimaryKey(Long id);
    @Select("SELECT * FROM t_user")
    List<User> selectList();
    @Update("UPDATE t_user SET username=#{username}, age=#{age}, email=#{email} WHERE id=#{id}")
    int updateByPrimaryKey(User record);
    @Delete("DELETE FROM t_user WHERE id=#{id}")
    int deleteByPrimaryKey(Long id);

    List<User> selectByCondition(String username, Integer minAge);

}
