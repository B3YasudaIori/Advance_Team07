package uno.advance_team07.player;

import static org.hamcrest.Matchers.containsString;
import static org.hamcrest.Matchers.not;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.content;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.webmvc.test.autoconfigure.AutoConfigureMockMvc;
import org.springframework.mock.web.MockHttpSession;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.MvcResult;

@SpringBootTest
@AutoConfigureMockMvc
class PlayerBalanceTest {

  @Autowired
  private MockMvc mockMvc;

    @Autowired
    private PlayerMapper playerMapper;

  @Test
  void 登録後にログインすると初期所持金200が表示され再ログイン後も保持される() throws Exception {
    mockMvc.perform(post("/register")
        .param("username", "player-balance-flow-user")
        .param("password", "password"))
        .andExpect(status().isOk());

    mockMvc.perform(post("/login")
        .param("username", "player-balance-flow-user")
        .param("password", "password"))
        .andExpect(status().isOk())
        .andExpect(content().string(containsString("所持金:")))
        .andExpect(content().string(containsString(">200</span>")));

    mockMvc.perform(post("/login")
        .param("username", "player-balance-flow-user")
        .param("password", "password"))
        .andExpect(status().isOk())
        .andExpect(content().string(containsString(">200</span>")));
  }

  @Test
  void ログイン中のユーザーには別ユーザーの所持金が表示されない() throws Exception {
    mockMvc.perform(post("/register")
        .param("username", "player-balance-user-a")
        .param("password", "password"))
        .andExpect(status().isOk());
    mockMvc.perform(post("/register")
        .param("username", "player-balance-user-b")
        .param("password", "password"))
        .andExpect(status().isOk());

    playerMapper.updateBalance("player-balance-user-b", 500);

    MvcResult loginResult = mockMvc.perform(post("/login")
        .param("username", "player-balance-user-a")
        .param("password", "password"))
        .andExpect(status().isOk())
        .andExpect(content().string(containsString(">200</span>")))
        .andReturn();

    MockHttpSession session = (MockHttpSession) loginResult.getRequest().getSession(false);
    mockMvc.perform(get("/")
        .session(session)
        .param("username", "player-balance-user-b"))
        .andExpect(status().isOk())
        .andExpect(content().string(containsString("player-balance-user-a")))
        .andExpect(content().string(containsString(">200</span>")))
        .andExpect(content().string(not(containsString(">500</span>"))));

    mockMvc.perform(post("/login")
        .param("username", "player-balance-user-b")
        .param("password", "password"))
        .andExpect(status().isOk())
        .andExpect(content().string(containsString("player-balance-user-b")))
        .andExpect(content().string(containsString(">500</span>")));
  }
}
