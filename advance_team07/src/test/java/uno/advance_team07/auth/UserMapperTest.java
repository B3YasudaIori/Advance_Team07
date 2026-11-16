package uno.advance_team07.auth;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;

import org.junit.jupiter.api.Test;
import org.mybatis.spring.boot.test.autoconfigure.MybatisTest;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.DuplicateKeyException;

@MybatisTest
class UserMapperTest {

  @Autowired
  private UserMapper userMapper;

  @Test
  void ユーザー情報を登録してパスワードを取得できる() {
    userMapper.insert("mapper-user", "encoded-password");

    assertThat(userMapper.findPasswordByUsername("mapper-user")).isEqualTo("encoded-password");
  }

  @Test
  void ユーザー名の重複登録を拒否する() {
    userMapper.insert("duplicate-mapper-user", "encoded-password");

    assertThatThrownBy(() -> userMapper.insert("duplicate-mapper-user", "another-password"))
        .isInstanceOf(DuplicateKeyException.class);
  }
}
