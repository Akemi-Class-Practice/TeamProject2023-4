package test.ex.controllers;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

import org.junit.jupiter.api.Test;
import org.mockito.Mock;
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

    @Test
    // ログイン画面の表示をテストする
    public void testGetLoginPage() throws Exception {
        mockMvc.perform(get("/user/login"))
                .andExpect(view().name("user-login.html"));  // 表示するビューの名前が"user-login.html"であることを検証
    }
    
    @Test
    //正しいユーザーメール(melon@melon)＆正しいユーザパスワード(123)を入力してテスト
    public void testLogin_Successful() throws Exception {
        // モックの戻り値を設定
        StudentEntity studentEntity = new StudentEntity();
        when(studentService.selectByEmailAndPassword(eq("melon@melon"), eq("123")))
                .thenReturn(studentEntity);

        // リクエストを作成して実行し、リダイレクト先のurlを/user/lesson/listかどうかをテスト
        RequestBuilder request = post("/user/login")
                .param("email", "melon@melon")
                .param("password", "123");

        mockMvc.perform(request)
                .andExpect(redirectedUrl("/user/lesson/list"));
    }


    @Test
    // 間違ったユーザーメール(melon@123)＆正しいユーザパスワード(123)を入力してテスト
    public void testLogin_Failed_InvalidEmail() throws Exception {
        // モックの戻り値を設定
        when(studentService.selectByEmailAndPassword(any(), any())).thenReturn(null);

        // リクエストを作成して実行し、エラー状態を検証
        RequestBuilder request = post("/user/login")
                .param("email", "melon@123")
                .param("password", "123");

        mockMvc.perform(request)
                .andExpect(status().isOk())
                .andExpect(view().name("user-login.html"))
                .andExpect(model().attribute("error", true));
    }
    
    @Test
    // ユーザーメールが空白&正しいユーザパスワード(123)に入力してテスト
    public void testLogin_Failed_EmptyEmail() throws Exception {
        // モックの戻り値を設定
        when(studentService.selectByEmailAndPassword(any(), any())).thenReturn(null);

        // リクエストを作成して実行し、エラー状態を検証
        RequestBuilder request = post("/user/login")
                .param("email", "")
                .param("password", "123");

        mockMvc.perform(request)
                .andExpect(view().name("user-login.html"))
                .andExpect(model().attribute("error", true));
    }
    
    @Test
    // 正しいユーザーメール(melon@melon)&間違ったユーザーパスワード(1111)を入力してテスト
    public void testLogin_Failed_InvalidPassword() throws Exception {
        // モックの戻り値を設定
        when(studentService.selectByEmailAndPassword(any(), any())).thenReturn(null);

        // リクエストを作成して実行し、エラー状態を検証
        RequestBuilder request = post("/user/login")
                .param("email", "melon@melon")
                .param("password", "1111");

        mockMvc.perform(request)
                .andExpect(view().name("user-login.html"))
                .andExpect(model().attribute("error", true));
    }
    
    @Test
    // 正しいユーザーメール(melon@melon)&パスワードが空白の状態のテスト
    public void testLogin_Failed_EmptyEmailAndPassword() throws Exception {
        // モックの戻り値を設定
        when(studentService.selectByEmailAndPassword(any(), any())).thenReturn(null);

        // リクエストを作成して実行し、エラー状態を検証
        RequestBuilder request = post("/user/login")
                .param("email", "")
                .param("password", "");

        mockMvc.perform(request)
                .andExpect(view().name("user-login.html"))
                .andExpect(model().attribute("error", true));
    }
    
    
    @Test
    // ユーザーメール&パスワードが空白の状態でログインボタン押下ユーザーメールとパスワードが空白でテスト
    public void testLogin_Failed_EmptyEmailAndEmptyPassword() throws Exception {
        // モックの戻り値を設定
        when(studentService.selectByEmailAndPassword(any(), any())).thenReturn(null);

        // リクエストを作成して実行し、エラー状態を検証
        RequestBuilder request = post("/user/login")
                .param("email", "")
                .param("password", "");

        mockMvc.perform(request)
                .andExpect(model().attribute("error", true));
    }
    
    
    @Test
    // 間違ったユーザーメール(melon@123)&間違ったユーザーパスワード(123aaa)でログインボタンを押下テスト
    public void testLogin_Failed_InvalidEmailAndInvalidPassword() throws Exception {
        // モックの戻り値を設定
        when(studentService.selectByEmailAndPassword(any(), any())).thenReturn(null);

        // リクエストを作成して実行し、エラー状態を検証
        RequestBuilder request = post("/user/login")
                .param("email", "melon@123")
                .param("password", "123aaa");

        mockMvc.perform(request)
                .andExpect(view().name("user-login.html"))
                .andExpect(model().attribute("error", true));
    }
}