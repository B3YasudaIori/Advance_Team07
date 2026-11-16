package uno.advance_team07.auth;

import static org.hamcrest.Matchers.containsString;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
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
class AuthViewTest {

  @Autowired
  private MockMvc mockMvc;

  @Test
  void 登録リンクから登録フォームを表示できる() throws Exception {
    mockMvc.perform(get("/"))
        .andExpect(status().isOk())
        .andExpect(view().name("login"))
        .andExpect(content().string(containsString("href=\"/register\"")));

    mockMvc.perform(get("/register"))
        .andExpect(status().isOk())
        .andExpect(view().name("register"))
        .andExpect(content().string(containsString("action=\"/register\"")));
  }

  @Test
  void 登録成功時はログイン画面に完了メッセージを表示する() throws Exception {
    mockMvc.perform(post("/register")
        .param("username", "view-register-user")
        .param("password", "password"))
        .andExpect(status().isOk())
        .andExpect(view().name("login"))
        .andExpect(content().string(containsString("登録が完了しました")));
  }

  @Test
  void 登録失敗時は登録画面にエラーを表示する() throws Exception {
    String username = "view-duplicate-user";
    mockMvc.perform(post("/register")
        .param("username", username)
        .param("password", "password"));

    mockMvc.perform(post("/register")
        .param("username", username)
        .param("password", "another-password"))
        .andExpect(status().isOk())
        .andExpect(view().name("register"))
        .andExpect(content().string(containsString("ユーザー名またはパスワードが不正か、ユーザー名が登録済みです")));
  }
}