package uno.advance_team07.auth;

import org.springframework.dao.DuplicateKeyException;
import org.springframework.security.authentication.AuthenticationProvider;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.core.authority.AuthorityUtils;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import uno.advance_team07.player.Player;
import uno.advance_team07.player.PlayerMapper;

@Service
public class AuthService implements AuthenticationProvider {

  private final UserMapper userMapper;
  private final PasswordEncoder passwordEncoder = new BCryptPasswordEncoder();
  private final PlayerMapper playerMapper;

  public AuthService(UserMapper userMapper, PlayerMapper playerMapper) {
    this.userMapper = userMapper;
    this.playerMapper = playerMapper;
  }

  @Transactional
  public boolean register(String username, String password) {
    if (username == null || username.isBlank() || password == null || password.isBlank()) {
      return false;
    }
    try {
      if (userMapper.insert(username, passwordEncoder.encode(password)) != 1) {
        return false;
      }
      if (playerMapper.insert(new Player(username, 200, true)) != 1) {
        throw new IllegalStateException("Player registration failed");
      }
      return true;
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

  @Override
  public Authentication authenticate(Authentication authentication) throws AuthenticationException {
    String username = authentication.getName();
    String password = String.valueOf(authentication.getCredentials());
    if (!authenticate(username, password)) {
      throw new BadCredentialsException("Invalid username or password");
    }
    return new UsernamePasswordAuthenticationToken(
        username,
        null,
        AuthorityUtils.NO_AUTHORITIES);
  }

  @Override
  public boolean supports(Class<?> authentication) {
    return UsernamePasswordAuthenticationToken.class.isAssignableFrom(authentication);
  }
}
