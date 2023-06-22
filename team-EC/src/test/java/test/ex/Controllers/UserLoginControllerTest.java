package test.ex.Controllers;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.RequestBuilder;

import jakarta.servlet.http.HttpSession;
import test.ex.models.entity.StudentEntity;
import test.ex.service.StudentService;

@SpringBootTest
@AutoConfigureMockMvc
public class UserLoginControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @MockBean
    private StudentService studentService;

    @Mock
    private HttpSession session;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
    }

    @Test
    // ログイン画面の表示をテストする
    public void testGetLoginPage() throws Exception {
        mockMvc.perform(get("/user/login"))
                .andExpect(status().isOk())
                .andExpect(view().name("user-login.html"));
    }
    
    @Test
    // ログイン成功時のテスト
    public void testLogin_Successful() throws Exception {
        StudentEntity studentEntity = new StudentEntity();
        when(studentService.selectByEmailAndPassword(eq("melon@melon"), eq("123")))
                .thenReturn(studentEntity);

        RequestBuilder request = post("/user/login")
                .param("email", "melon@melon")
                .param("password", "123");

        mockMvc.perform(request)
                .andExpect(redirectedUrl("/user/lesson/list"));
          
    }


    @Test
    // ログイン失敗時のテスト
    public void testLogin_Failed() throws Exception {
        when(studentService.selectByEmailAndPassword(any(), any())).thenReturn(null);

        RequestBuilder request = post("/user/login")
                .param("email", "test@test")
                .param("password", "123");

        mockMvc.perform(request)
                .andExpect(status().isOk())
                .andExpect(view().name("user-login.html"))
                .andExpect(model().attribute("error", true));
    }
    
    
}
