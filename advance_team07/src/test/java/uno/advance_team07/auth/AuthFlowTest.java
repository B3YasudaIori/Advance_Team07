package uno.advance_team07.auth;

import static org.hamcrest.Matchers.containsString;
import static org.hamcrest.Matchers.not;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.content;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.redirectedUrl;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.view;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.webmvc.test.autoconfigure.AutoConfigureMockMvc;
import org.springframework.test.web.servlet.MockMvc;

@SpringBootTest
@AutoConfigureMockMvc
class AuthFlowTest {

  @Autowired
  private MockMvc mockMvc;

  @Test
  void 登録成功後にログインできる() throws Exception {
    mockMvc.perform(post("/register")
        .param("username", "auth-flow-register-user")
        .param("password", "password"))
        .andExpect(status().isOk())
        .andExpect(view().name("login"))
        .andExpect(content().string(containsString("登録が完了しました")));

    mockMvc.perform(post("/login")
        .param("username", "auth-flow-register-user")
        .param("password", "password"))
        .andExpect(status().isOk())
        .andExpect(view().name("login"))
        .andExpect(content().string(containsString("待機部屋へようこそ")));
  }

  @Test
  void 登録済みユーザーは重複登録できない() throws Exception {
    mockMvc.perform(post("/register")
        .param("username", "auth-flow-duplicate-user")
        .param("password", "password"))
        .andExpect(status().isOk())
        .andExpect(view().name("login"))
        .andExpect(content().string(containsString("登録が完了しました")));

    mockMvc.perform(post("/register")
        .param("username", "auth-flow-duplicate-user")
        .param("password", "another-password"))
        .andExpect(status().isOk())
        .andExpect(view().name("register"))
        .andExpect(content().string(containsString("ユーザー名またはパスワードが不正か、ユーザー名が登録済みです")));
  }

  @Test
  void 誤ったパスワードではログインできない() throws Exception {
    mockMvc.perform(post("/register")
        .param("username", "auth-flow-failed-login-user")
        .param("password", "password"))
        .andExpect(status().isOk())
        .andExpect(view().name("login"))
        .andExpect(content().string(containsString("登録が完了しました")));

    mockMvc.perform(post("/login")
        .param("username", "auth-flow-failed-login-user")
        .param("password", "wrong-password"))
        .andExpect(status().isOk())
        .andExpect(view().name("login"))
        .andExpect(content().string(containsString("ユーザー名またはパスワードが正しくありません")))
        .andExpect(content().string(not(containsString("待機部屋へようこそ"))));
  }

  @Test
  void ログアウトするとログイン画面へリダイレクトされる() throws Exception {
    mockMvc.perform(post("/logout"))
        .andExpect(status().is3xxRedirection())
        .andExpect(redirectedUrl("/login?logout"));
  }
}
