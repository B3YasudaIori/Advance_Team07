package uno.advance_team07.auth;

import org.apache.ibatis.annotations.Insert;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Select;

@Mapper
public interface UserMapper {

  @Insert("INSERT INTO users (username, password) VALUES (#{username}, #{password})")
  int insert(String username, String password);

  @Select("SELECT password FROM users WHERE username = #{username}")
  String findPasswordByUsername(String username);
}
