package demo.book.mapper;
import demo.book.entity.User; import org.apache.ibatis.annotations.*;
/** 用户查询与注册映射。 */
@Mapper public interface UserMapper {
 @Select("SELECT * FROM users WHERE username=#{u}") User byName(String u);
 @Insert("INSERT INTO users(username,password,role) VALUES(#{username},#{password},'USER')") int add(User u);
}
