package test.ex.controllers;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.mock.web.MockHttpSession;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;
import org.springframework.test.web.servlet.result.MockMvcResultMatchers;

@SpringBootTest
@AutoConfigureMockMvc
public class UserLogoutControllerTest {
    @Autowired
    private MockMvc mockMvc;

    //logoutが正しく機能するかどうか
    @Test
    public void testLogout() throws Exception {
        // セッションを作成
        MockHttpSession session = new MockHttpSession();

        // GETリクエストを送信してログアウト処理をテスト
        mockMvc.perform(MockMvcRequestBuilders.get("/user/logout").session(session))
                .andExpect(MockMvcResultMatchers.redirectedUrl("/user/login"));

    }
}