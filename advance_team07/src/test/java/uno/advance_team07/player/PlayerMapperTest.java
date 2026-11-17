package uno.advance_team07.player;

import static org.assertj.core.api.Assertions.assertThat;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

@SpringBootTest
class PlayerMapperTest {

  @Autowired
  private PlayerMapper playerMapper;

  @Test
  void ユーザーごとの所持金をDBに保存して再取得できる() {
    playerMapper.insert(new Player("player-mapper-user", 200, true));

    Player player = playerMapper.findByUsername("player-mapper-user");

    assertThat(player).isNotNull();
    assertThat(player.getUsername()).isEqualTo("player-mapper-user");
    assertThat(player.getBalance()).isEqualTo(200);
    assertThat(player.isActive()).isTrue();
  }

  @Test
  void 別ユーザーの所持金はユーザー名で分離される() {
    playerMapper.insert(new Player("player-mapper-user-a", 200, true));
    playerMapper.insert(new Player("player-mapper-user-b", 500, true));

    assertThat(playerMapper.findByUsername("player-mapper-user-a").getBalance()).isEqualTo(200);
    assertThat(playerMapper.findByUsername("player-mapper-user-b").getBalance()).isEqualTo(500);
  }
}