package uno.advance_team07.auth;

import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

@Service
public class AuthService {

  private final Map<String, String> users = new ConcurrentHashMap<>();
  private final PasswordEncoder passwordEncoder = new BCryptPasswordEncoder();

  public boolean register(String username, String password) {
    if (username == null || username.isBlank() || password == null || password.isBlank()) {
      return false;
    }
    return users.putIfAbsent(username, passwordEncoder.encode(password)) == null;
  }

  public boolean authenticate(String username, String password) {
    String encodedPassword = users.get(username);
    return encodedPassword != null && passwordEncoder.matches(password, encodedPassword);
  }
}
