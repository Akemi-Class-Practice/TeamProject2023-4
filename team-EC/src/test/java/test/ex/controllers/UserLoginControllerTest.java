package test.ex.Controllers;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertNull;

import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.when;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mock;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;

import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.MvcResult;
import org.springframework.test.web.servlet.RequestBuilder;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;

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
    
    // オブジェクトのセットアップとモックの設定 ----------------------------------------------------------
    @BeforeEach
    public void prepareData() {
        // StudentEntityオブジェクトを作成する
        StudentEntity studentEntity = new StudentEntity("melon@melon", "123");

        // selectByEmailAndPasswordメソッドに引数"melon@melon"と"123"が与えられた場合にstudentEntityを返すように設定する
        when(studentService.selectByEmailAndPassword(eq("melon@melon"), eq("123"))).thenReturn(studentEntity);

        // 他のパラメータのモックの設定
        when(studentService.selectByEmailAndPassword(eq("melon@123"), eq("123"))).thenReturn(null);
        when(studentService.selectByEmailAndPassword(eq(""), eq("123"))).thenReturn(null);
        when(studentService.selectByEmailAndPassword(eq("melon@melon"), eq("1111"))).thenReturn(null);
        when(studentService.selectByEmailAndPassword(eq("melon@melon"), eq(""))).thenReturn(null);
        when(studentService.selectByEmailAndPassword(eq(""), eq(""))).thenReturn(null);
        when(studentService.selectByEmailAndPassword(eq("melon@123"), eq("123aaa"))).thenReturn(null);
    }

    // ログインページの取得を検証する --------------------------------------------------------------------------------------------------
    @Test
    public void accessLoginPage_Succeed() throws Exception {
        // リクエストを作成して実行し、ビュー名が"user-login"であることを検証する
    	RequestBuilder request = MockMvcRequestBuilders.get("/user/login");
        mockMvc.perform(request).andExpect(view().name("user-login.html"));
    }

    // 正常なログインの成功を検証する（正しいユーザーメールとパスワードが提供された場合にログインが成功し、リダイレクトが正しく行われる）
    @Test
    public void testLogin_Successful() throws Exception {
        // リクエストを作成して実行し、リダイレクト先のURLが"/user/lesson/list"であることを検証する
        RequestBuilder request =MockMvcRequestBuilders.post("/user/login")
                .param("email", "melon@melon")
                .param("password", "123");

    
     		MvcResult result = mockMvc.perform(request).andExpect(redirectedUrl("/user/lesson/list")).andReturn();
     		
     		// セッションを取得する
     		HttpSession session = result.getRequest().getSession();
     		
     		// セッションからログインユーザーエンティティを取得する
     		StudentEntity loggedInUser = (StudentEntity)session.getAttribute("student");
     		assertNotNull(loggedInUser);										
     		assertEquals("melon@melon", loggedInUser.getStudentEmail());					
     		assertEquals("123", loggedInUser.getStudentPassword());			
     	}

    // 間違ったユーザーメール("melon@123")と正しいユーザーパスワード("123")を入力してログインボタンを押下
    @Test
    public void testLogin_wrongEmail() throws Exception {
        // リクエストを作成して実行し、ステータスが成功であることを検証する
        RequestBuilder request = post("/user/login")
                .param("email", "melon@123")
                .param("password", "123");

        mockMvc.perform(request)
        .andExpect(status().isOk())
        .andExpect(view().name("user-login.html"));
     // セッションを取得する
     		HttpSession session = mockMvc.perform(MockMvcRequestBuilders.get("/user/login")).andReturn().getRequest().getSession();

     		// セッションからログインユーザーエンティティを取得する
     		StudentEntity loggedInUser = (StudentEntity) session.getAttribute("student");
     		assertNull(loggedInUser);			// ログインユーザーエンティティがnullであることを検証する
     	}

    // ユーザーメールが空白で正しいユーザーパスワード("123")を入力してログインボタンを押下
    @Test
    public void testLogin_NullEmail() throws Exception {
        // リクエストを作成して実行し、ステータスが成功であることを検証する
        RequestBuilder request = post("/user/login")
                .param("email", "")
                .param("password", "123");

        mockMvc.perform(request)
        .andExpect(status().isOk())
        .andExpect(view().name("user-login.html"));
     // セッションを取得する
     		HttpSession session = mockMvc.perform(MockMvcRequestBuilders.get("/user/login")).andReturn().getRequest().getSession();

     		// セッションからログインユーザーエンティティを取得する
     		StudentEntity loggedInUser = (StudentEntity) session.getAttribute("student");
     		assertNull(loggedInUser);			// ログインユーザーエンティティがnullであることを検証する
     	}

    // 正しいユーザーメール("melon@melon")と間違ったユーザーパスワード("1111")を入力してログインボタンを押下
    @Test
    public void testLogin_wrongPasswordl() throws Exception {
        // リクエストを作成して実行し、ステータスが成功であることを検証する
        RequestBuilder request = post("/user/login")
                .param("email", "melon@melon")
                .param("password", "1111");

        mockMvc.perform(request)
        .andExpect(status().isOk())
        .andExpect(view().name("user-login.html"));
     // セッションを取得する
     		HttpSession session = mockMvc.perform(MockMvcRequestBuilders.get("/user/login")).andReturn().getRequest().getSession();

     		// セッションからログインユーザーエンティティを取得する
     		StudentEntity loggedInUser = (StudentEntity) session.getAttribute("student");
     		assertNull(loggedInUser);			// ログインユーザーエンティティがnullであることを検証する
     	}

    // 正しいユーザーメール("melon@melon")とパスワードが空白の状態でログインボタンを押下
    @Test
    public void testLogin_NullPassword() throws Exception {
        // リクエストを作成して実行し、ステータスが成功であることを検証する
        RequestBuilder request = post("/user/login")
                .param("email", "melon@melon")
                .param("password", "");

        mockMvc.perform(request)
        .andExpect(status().isOk())
        .andExpect(view().name("user-login.html"));
     // セッションを取得する
     		HttpSession session = mockMvc.perform(MockMvcRequestBuilders.get("/user/login")).andReturn().getRequest().getSession();

     		// セッションからログインユーザーエンティティを取得する
     		StudentEntity loggedInUser = (StudentEntity) session.getAttribute("student");
     		assertNull(loggedInUser);				// ログインユーザーエンティティがnullであることを検証する
     	}

    // ユーザーメールとパスワードが空白の状態でログインボタンを押下
    @Test
    public void testLogin_NullEmailAndNullPassword() throws Exception {
        // リクエストを作成して実行し、ステータスが成功であることを検証する
        RequestBuilder request = post("/user/login")
                .param("email", "")
                .param("password", "");

        mockMvc.perform(request)
        .andExpect(status().isOk())
        .andExpect(view().name("user-login.html"));
     // セッションを取得する
     		HttpSession session = mockMvc.perform(MockMvcRequestBuilders.get("/user/login")).andReturn().getRequest().getSession();

     		// セッションからログインユーザーエンティティを取得する
     		StudentEntity loggedInUser = (StudentEntity) session.getAttribute("student");
     		assertNull(loggedInUser);				// ログインユーザーエンティティがnullであることを検証する
     	}

    // 正しいユーザーメール("melon@123")と間違ったユーザーパスワード("123aaa")を入力してログインボタンを押下
    @Test
    public void testLogin_wrongEmailAndPassword() throws Exception {
        // リクエストを作成して実行し、ステータスが成功であることを検証する
        RequestBuilder request = post("/user/login")
                .param("email", "melon@123")
                .param("password", "123aaa");

        mockMvc.perform(request)
        .andExpect(status().isOk())
        .andExpect(view().name("user-login.html"));
     // セッションを取得する
     		HttpSession session = mockMvc.perform(MockMvcRequestBuilders.get("/user/login")).andReturn().getRequest().getSession();

     		// セッションからログインユーザーエンティティを取得する
     		StudentEntity loggedInUser = (StudentEntity) session.getAttribute("student");
     		assertNull(loggedInUser);				// ログインユーザーエンティティがnullであることを検証する
     	}
}
