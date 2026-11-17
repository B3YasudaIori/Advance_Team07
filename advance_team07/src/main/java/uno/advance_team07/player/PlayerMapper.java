package uno.advance_team07.player;

import org.apache.ibatis.annotations.Insert;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Select;
import org.apache.ibatis.annotations.Update;

@Mapper
public interface PlayerMapper {

  @Insert("INSERT INTO players (username, balance, active) VALUES (#{username}, #{balance}, #{active})")
  int insert(Player player);

  @Select("SELECT username, balance, active FROM players WHERE username = #{username}")
  Player findByUsername(String username);

  @Update("UPDATE players SET balance = #{balance} WHERE username = #{username}")
  int updateBalance(String username, int balance);
}
