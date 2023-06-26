package test.ex.controllers;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertNull;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.redirectedUrl;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.view;

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
import test.ex.models.entity.AdminEntity;
import test.ex.service.AdminService;



@SpringBootTest
@AutoConfigureMockMvc
public class AdminLoginControllerTest {
    @Autowired
    private MockMvc mockMvc;

    @MockBean
    private AdminService adminService;

    @Mock
    private HttpSession session;
    
    // オブジェクトのセットアップとモックの設定 ----------------------------------------------------------
    @BeforeEach
    public void prepareData() {
        // AdminEntityオブジェクトを作成する
        AdminEntity adminEntity = new AdminEntity("aaa@aaa", "aaa");

        // selectByEmailAndPasswordメソッドに引数"aaa@aaa"と"aaa"が与えられた場合にadminEntityを返すように設定する
        when(adminService.selectByEmailAndPassword(eq("aaa@aaa"), eq("aaa"))).thenReturn(adminEntity);

        // 他のパラメータのモックの設定
        when(adminService.selectByEmailAndPassword(eq("bbb@bbb"), eq("bbb"))).thenReturn(null);
        when(adminService.selectByEmailAndPassword(eq(""), eq("aaa"))).thenReturn(null);
        when(adminService.selectByEmailAndPassword(eq("aaa@aaa"), eq("bbb"))).thenReturn(null);
        when(adminService.selectByEmailAndPassword(eq("aaa@aaa"), eq(""))).thenReturn(null);
        when(adminService.selectByEmailAndPassword(eq(""), eq(""))).thenReturn(null);
        when(adminService.selectByEmailAndPassword(eq("bbb@bbb"), eq("bbb"))).thenReturn(null);
    }

    // ログインページの取得を検証する --------------------------------------------------------------------------------------------------
    @Test
    public void accessLoginPage_Succeed() throws Exception {
        // リクエストを作成して実行し、ビュー名が"admin-login"であることを検証する
    	RequestBuilder request = MockMvcRequestBuilders.get("/admin/login");
        mockMvc.perform(request).andExpect(view().name("admin-login.html"));
    }

    // 正常なログインの成功を検証する（正しいユーザーメールとパスワードが提供された場合にログインが成功し、リダイレクトが正しく行われる）
    @Test
    public void testLogin_Successful() throws Exception {
        // リクエストを作成して実行し、リダイレクト先のURLが"/admin/lesson/list"であることを検証する
        RequestBuilder request =MockMvcRequestBuilders.post("/admin/login")
                .param("email", "aaa@aaa")
                .param("password", "aaa");

    
     		MvcResult result = mockMvc.perform(request).andExpect(redirectedUrl("/admin/lesson/list")).andReturn();
     		
     		// セッションを取得する
     		HttpSession session = result.getRequest().getSession();
     		
     		// セッションからログインユーザーエンティティを取得する
     		AdminEntity loggedInUser = (AdminEntity)session.getAttribute("admin");
     		assertNotNull(loggedInUser);										
     		assertEquals("aaa@aaa", loggedInUser.getAdminEmail());					
     		assertEquals("aaa", loggedInUser.getAdminPassword());			
     	}

    // 間違ったユーザーメール("bbb@bbb")と正しいユーザーパスワード("aaa")を入力してログインボタンを押下
    @Test
    public void testLogin_wrongEmail() throws Exception {
        // リクエストを作成して実行し、ステータスが成功であることを検証する
        RequestBuilder request = post("/admin/login")
                .param("email", "bbb@bbb")
                .param("password", "bbb");

        mockMvc.perform(request)
        .andExpect(status().isOk())
        .andExpect(view().name("admin-login.html"));
     // セッションを取得する
     		HttpSession session = mockMvc.perform(MockMvcRequestBuilders.get("/admin/login")).andReturn().getRequest().getSession();

     		// セッションからログインユーザーエンティティを取得する
     		AdminEntity loggedInUser = (AdminEntity) session.getAttribute("admin");
     		assertNull(loggedInUser);			// ログインユーザーエンティティがnullであることを検証する
     	}

    // ユーザーメールが空白で正しいユーザーパスワード("123")を入力してログインボタンを押下
    @Test
    public void testLogin_NullEmail() throws Exception {
        // リクエストを作成して実行し、ステータスが成功であることを検証する
        RequestBuilder request = post("/admin/login")
                .param("email", "")
                .param("password", "aaa");

        mockMvc.perform(request)
        .andExpect(status().isOk())
        .andExpect(view().name("admin-login.html"));
     // セッションを取得する
     		HttpSession session = mockMvc.perform(MockMvcRequestBuilders.get("/admin/login")).andReturn().getRequest().getSession();

     		// セッションからログインユーザーエンティティを取得する
     		AdminEntity loggedInUser = (AdminEntity) session.getAttribute("admin");
     		assertNull(loggedInUser);			// ログインユーザーエンティティがnullであることを検証する
     	}

    // 正しいユーザーメール("aaa@aaa")と間違ったユーザーパスワード("bbb")を入力してログインボタンを押下
    @Test
    public void testLogin_wrongPasswordl() throws Exception {
        // リクエストを作成して実行し、ステータスが成功であることを検証する
        RequestBuilder request = post("/admin/login")
                .param("email", "aaa@aaa")
                .param("password", "bbb");

        mockMvc.perform(request)
        .andExpect(status().isOk())
        .andExpect(view().name("admin-login.html"));
     // セッションを取得する
     		HttpSession session = mockMvc.perform(MockMvcRequestBuilders.get("/admin/login")).andReturn().getRequest().getSession();

     		// セッションからログインユーザーエンティティを取得する
     		AdminEntity loggedInUser = (AdminEntity) session.getAttribute("admin");
     		assertNull(loggedInUser);			// ログインユーザーエンティティがnullであることを検証する
     	}

    // 正しいユーザーメール("aaa@aaa")とパスワードが空白の状態でログインボタンを押下
    @Test
    public void testLogin_NullPassword() throws Exception {
        // リクエストを作成して実行し、ステータスが成功であることを検証する
        RequestBuilder request = post("/admin/login")
                .param("email", "aaa@aaa")
                .param("password", "");

        mockMvc.perform(request)
        .andExpect(status().isOk())
        .andExpect(view().name("admin-login.html"));
     // セッションを取得する
     		HttpSession session = mockMvc.perform(MockMvcRequestBuilders.get("/admin/login")).andReturn().getRequest().getSession();

     		// セッションからログインユーザーエンティティを取得する
     		AdminEntity loggedInUser = (AdminEntity) session.getAttribute("admin");
     		assertNull(loggedInUser);				// ログインユーザーエンティティがnullであることを検証する
     	}

    // ユーザーメールとパスワードが空白の状態でログインボタンを押下
    @Test
    public void testLogin_NullEmailAndNullPassword() throws Exception {
        // リクエストを作成して実行し、ステータスが成功であることを検証する
        RequestBuilder request = post("/admin/login")
                .param("email", "")
                .param("password", "");

        mockMvc.perform(request)
        .andExpect(status().isOk())
        .andExpect(view().name("admin-login.html"));
     // セッションを取得する
     		HttpSession session = mockMvc.perform(MockMvcRequestBuilders.get("/admin/login")).andReturn().getRequest().getSession();

     		// セッションからログインユーザーエンティティを取得する
     		AdminEntity loggedInUser = (AdminEntity) session.getAttribute("admin");
     		assertNull(loggedInUser);				// ログインユーザーエンティティがnullであることを検証する
     	}

    // 間違ったユーザーメール("bbb@bbb")と間違ったユーザーパスワード("bbb")を入力してログインボタンを押下
    @Test
    public void testLogin_wrongEmailAndPassword() throws Exception {
        // リクエストを作成して実行し、ステータスが成功であることを検証する
        RequestBuilder request = post("/admin/login")
                .param("email", "bbb@bbb")
                .param("password", "bbb");

        mockMvc.perform(request)
        .andExpect(status().isOk())
        .andExpect(view().name("admin-login.html"));
     // セッションを取得する
     		HttpSession session = mockMvc.perform(MockMvcRequestBuilders.get("/admin/login")).andReturn().getRequest().getSession();

     		// セッションからログインユーザーエンティティを取得する
     		AdminEntity loggedInUser = (AdminEntity) session.getAttribute("admin");
     		assertNull(loggedInUser);				// ログインユーザーエンティティがnullであることを検証する
     	}
}
