package uno.advance_team07.auth;

import org.springframework.dao.DuplicateKeyException;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import uno.advance_team07.player.Player;
import uno.advance_team07.player.PlayerMapper;

@Service
public class AuthService {

  private final UserMapper userMapper;
  private final PasswordEncoder passwordEncoder = new BCryptPasswordEncoder();
  private final PlayerMapper playerMapper;

  public AuthService(UserMapper userMapper, PlayerMapper playerMapper) {
    this.userMapper = userMapper;
    this.playerMapper = playerMapper;
  }

  public boolean register(String username, String password) {
    if (username == null || username.isBlank() || password == null || password.isBlank()) {
      return false;
    }
    try {
      if (userMapper.insert(username, passwordEncoder.encode(password)) != 1) {
        return false;
      }
      return playerMapper.insert(new Player(username, 200, true)) == 1;
    } catch (DuplicateKeyException exception) {
      return false;
    }
  }

  public boolean authenticate(String username, String password) {
    String encodedPassword = userMapper.findPasswordByUsername(username);
    return encodedPassword != null && passwordEncoder.matches(password, encodedPassword);
  }

  public int getBalance(String username) {
    Player player = playerMapper.findByUsername(username);
    return player == null ? 0 : player.getBalance();
  }
}
