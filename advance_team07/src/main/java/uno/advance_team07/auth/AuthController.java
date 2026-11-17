package uno.advance_team07.auth;

import java.security.Principal;

import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.core.context.SecurityContext;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.web.context.SecurityContextRepository;
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
  private final AuthenticationManager authenticationManager;
  private final SecurityContextRepository securityContextRepository;

  public AuthController(
      AuthService authService,
      AuthenticationManager authenticationManager,
      SecurityContextRepository securityContextRepository) {
    this.authService = authService;
    this.authenticationManager = authenticationManager;
    this.securityContextRepository = securityContextRepository;
  }

  @GetMapping({ "/", "/login" })
  public String loginPage(Principal principal, Model model) {
    addPlayerInfo(principal, model);
    return "login";
  }

  @GetMapping("/register")
  public String registerPage() {
    return "register";
  }

  @PostMapping("/register")
  public String register(@RequestParam String username, @RequestParam String password, Model model) {
    if (authService.register(username, password)) {
      model.addAttribute("message", "登録が完了しました。ログインしてください。");
      return "login";
    } else {
      model.addAttribute("error", "ユーザー名またはパスワードが不正か、ユーザー名が登録済みです。");
      return "register";
    }
  }

  @PostMapping("/login")
  public String login(
      @RequestParam String username,
      @RequestParam String password,
      HttpServletRequest request,
      HttpServletResponse response,
      Model model) {
    try {
      Authentication authentication = authenticationManager.authenticate(
          new UsernamePasswordAuthenticationToken(username, password));
      SecurityContext context = SecurityContextHolder.createEmptyContext();
      context.setAuthentication(authentication);
      SecurityContextHolder.setContext(context);
      securityContextRepository.saveContext(context, request, response);
      model.addAttribute("username", authentication.getName());
      model.addAttribute("balance", authService.getBalance(authentication.getName()));
      return "login";
    } catch (AuthenticationException exception) {
      model.addAttribute("error", "ユーザー名またはパスワードが正しくありません。");
      return "login";
    }
  }

  private void addPlayerInfo(Principal principal, Model model) {
    if (principal != null) {
      model.addAttribute("username", principal.getName());
      model.addAttribute("balance", authService.getBalance(principal.getName()));
    }
  }

  @Configuration
  static class SecurityConfiguration {

    @Bean
    SecurityFilterChain securityFilterChain(HttpSecurity http) throws Exception {
      return http
          .csrf(csrf -> csrf.disable())
          .headers(headers -> headers
              .frameOptions(frame -> frame.sameOrigin()))
          .authorizeHttpRequests(authorize -> authorize.anyRequest().permitAll())
          .build();
    }

    @Bean
    AuthenticationManager authenticationManager(AuthService authService) {
      return authentication -> authService.authenticate(authentication);
    }

    @Bean
    SecurityContextRepository securityContextRepository() {
      return new org.springframework.security.web.context.HttpSessionSecurityContextRepository();
    }
  }
}
