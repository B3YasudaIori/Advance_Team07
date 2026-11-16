package uno.advance_team07.auth;

import org.springframework.dao.DuplicateKeyException;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

@Service
public class AuthService {

  private final UserMapper userMapper;
  private final PasswordEncoder passwordEncoder = new BCryptPasswordEncoder();

  public AuthService(UserMapper userMapper) {
    this.userMapper = userMapper;
  }

  public boolean register(String username, String password) {
    if (username == null || username.isBlank() || password == null || password.isBlank()) {
      return false;
    }
    try {
      return userMapper.insert(username, passwordEncoder.encode(password)) == 1;
    } catch (DuplicateKeyException exception) {
      return false;
    }
  }

  public boolean authenticate(String username, String password) {
    String encodedPassword = userMapper.findPasswordByUsername(username);
    return encodedPassword != null && passwordEncoder.matches(password, encodedPassword);
  }
}
