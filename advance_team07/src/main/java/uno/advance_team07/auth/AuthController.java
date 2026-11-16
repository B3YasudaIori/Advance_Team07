package uno.advance_team07.auth;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;

@Controller
public class AuthController {

  private final AuthService authService;

  public AuthController(AuthService authService) {
    this.authService = authService;
  }

  @GetMapping({ "/", "/login" })
  public String loginPage() {
    return "login";
  }

  @PostMapping("/register")
  public String register(@RequestParam String username, @RequestParam String password, Model model) {
    if (authService.register(username, password)) {
      model.addAttribute("message", "登録が完了しました。ログインしてください。");
    } else {
      model.addAttribute("error", "ユーザー名またはパスワードが不正か、ユーザー名が登録済みです。");
    }
    return "login";
  }

  @PostMapping("/login")
  public String login(@RequestParam String username, @RequestParam String password, Model model) {
    if (authService.authenticate(username, password)) {
      model.addAttribute("username", username);
      return "login";
    }
    model.addAttribute("error", "ユーザー名またはパスワードが正しくありません。");
    return "login";
  }

  @Configuration
  static class SecurityConfiguration {

    @Bean
    SecurityFilterChain securityFilterChain(HttpSecurity http) throws Exception {
      return http
          .csrf(csrf -> csrf.disable())
          .authorizeHttpRequests(authorize -> authorize.anyRequest().permitAll())
          .build();
    }
  }
}
