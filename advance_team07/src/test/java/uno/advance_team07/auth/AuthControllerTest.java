package uno.advance_team07.auth;

import static org.hamcrest.Matchers.containsString;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.content;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.view;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.webmvc.test.autoconfigure.AutoConfigureMockMvc;
import org.springframework.test.web.servlet.MockMvc;

@SpringBootTest
@AutoConfigureMockMvc
class AuthControllerTest {

  @Autowired
  private MockMvc mockMvc;

  @Test
  void 登録済みのユーザー名は重複登録できない() throws Exception {
    mockMvc.perform(post("/register")
        .param("username", "duplicate-user")
        .param("password", "password"))
        .andExpect(status().isOk())
        .andExpect(content().string(containsString("登録が完了しました")));

    mockMvc.perform(post("/register")
        .param("username", "duplicate-user")
        .param("password", "another-password"))
        .andExpect(status().isOk())
        .andExpect(content().string(containsString("ユーザー名またはパスワードが不正か、ユーザー名が登録済みです")));
  }

  @Test
  void 正しい認証情報なら待機部屋へ遷移する() throws Exception {
    mockMvc.perform(post("/register")
        .param("username", "login-user")
        .param("password", "password"));

    mockMvc.perform(post("/login")
        .param("username", "login-user")
        .param("password", "password"))
        .andExpect(status().isOk())
        .andExpect(view().name("login"))
        .andExpect(content().string(containsString("待機部屋へようこそ")));
  }

  @Test
  void 誤った認証情報なら待機部屋へ遷移しない() throws Exception {
    mockMvc.perform(post("/register")
        .param("username", "failed-login-user")
        .param("password", "password"));

    mockMvc.perform(post("/login")
        .param("username", "failed-login-user")
        .param("password", "wrong-password"))
        .andExpect(status().isOk())
        .andExpect(view().name("login"))
        .andExpect(content().string(containsString("ユーザー名またはパスワードが正しくありません")))
        .andExpect(content().string(org.hamcrest.Matchers.not(containsString("待機部屋へようこそ"))));
  }
}
