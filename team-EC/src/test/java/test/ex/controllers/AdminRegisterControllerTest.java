package test.ex.controllers;

import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.doNothing;
import static org.mockito.Mockito.never;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.model;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.redirectedUrl;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.view;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.RequestBuilder;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;

import test.ex.service.AdminService;
import test.ex.service.StudentService;
@SpringBootTest                                           // Spring Bootアプリケーションのコンテキストをロードしてテストを実行するための注釈
@AutoConfigureMockMvc   
public class AdminRegisterControllerTest {

    @Autowired                                            // テスト対象のコントローラと対話するためのモックされたMVCモックオブジェクト
    private MockMvc mockMvc;

    @MockBean                                          // モックオブジェクトを作成して注入するための注釈 
    private AdminService adminService;
    
    // オブジェクトの設定とモックの設定 ----------------------------------------------------------------------------------------------
    @BeforeEach     // 各テストメソッドの実行前に実行されるメソッドを示す。つまり、各テストケースの前に共通の前準備を行うために使用される。
    public void prepareData() {

    	// メールが重複する場合＆メールが空白場合
        doNothing().when(adminService).insert(eq("apple"), eq("123"), eq("apple@apple"));
        doNothing().when(adminService).insert(eq("apple"), eq("123"), eq(""));
        // パスワードが空白の場合orユーザー名が空白の場合
        doNothing().when(adminService).insert(eq("banana"), eq(""), eq("banana@banana"));
        doNothing().when(adminService).insert(eq(""), eq("yes@yes"),eq(""));

    }               
    
    // 新規登録画面の表示をテスト---------------------------------------------------------------------------------------------------
    @Test
    public void testGetAdminRegisterPage() throws Exception {
        // /admin/registerへのgetリクエストの作成
        RequestBuilder request = MockMvcRequestBuilders.get("/admin/register");

        // ビュー名が"admin-register.html"であることを検証
        mockMvc.perform(request)
               .andExpect(view().name("admin-register.html"));
    }
    
    // 新規登録完了画面の表示をテスト----------------------------------------------------------------------------------------------
    @Test
    public void testGetAdminRegisterFinishPage() throws Exception {
        // /admin/register/finishへのgetリクエストの作成
        RequestBuilder request = MockMvcRequestBuilders.get("/admin/register/finish");

        // ビュー名が"admin-register-finish.html"であることを検証
        mockMvc.perform(request)
               .andExpect(view().name("admin-register-finish.html"));
    }
    
    // 正常なログインの成功を検証する--------------------------------------------------------------------------------------------  
    @Test
 // 未登録ユーザー名（apple)＆パスワード(123)＆キーワード(12345)＆ユーザーメール(apple@apple)で登録ボタンを押下
    public void testRegister_Successful() throws Exception {
       when(adminService.FindDuplicateEmail(anyString())).thenReturn(true);
        RequestBuilder request = MockMvcRequestBuilders.post("/admin/register")
        	    .param("username", "apple")
                .param("password", "123")
                .param("email", "apple@apple");
        // リクエストパラメーターを元にリクエストを実行
        mockMvc.perform(request)
        .andExpect(redirectedUrl("/admin/register/finish"));
        // adminServiceのinsertメソッドが指定された引数で1回呼び出されたことを検証する
        verify(adminService, times(1)).insert("apple", "123", "apple@apple");
    }
    // 重複なメールがある場合を検証する--------------------------------------------------------------------------------------------  
    @Test
    public void testRegister_DuplicateEmail() throws Exception {
        // リクエストの作成
        RequestBuilder request = MockMvcRequestBuilders.post("/admin/register")
                .param("username", "apple")
                .param("password", "123")
                .param("email", "apple@apple");

        // ビュー名が"user-register.html"であることを検証
        mockMvc.perform(request)
               .andExpect(view().name("admin-register.html"))
               .andExpect(model().attribute("error",true));

    
        // adminServiceのinsertメソッドが指定された引数で一回も呼び出さないことを検証する
  verify(adminService, never()).insert(eq("apple"), eq("123"), eq("apple@apple"));
    }
    
    @Test
    // パスワードが空白の場合を検証する----------------------------------------------------------------------------------------------
    public void testRegister_NullAdminPassword() throws Exception {
        // リクエストの作成
        RequestBuilder request = MockMvcRequestBuilders.post("/admin/register")
                .param("username", "banana")
                .param("password", "")
                .param("email", "banana@banana");
 
        // ビュー名が"admin-register.html"であることを検証
        mockMvc.perform(request)
               .andExpect(view().name("admin-register.html"))
               .andExpect(model().attribute("error", true));
        
        // adminServiceのinsertメソッドが指定された引数で一回も呼び出さないことを検証する
        verify(adminService, never()).insert(eq("banana"), eq(""), eq("banana@banana"));
   
    }
    
    @Test
    // E-mailが空白の場合を検証する----------------------------------------------------------------------------------------------
    public void testRegister_NullEmail() throws Exception {
        // リクエストの作成
        RequestBuilder request = MockMvcRequestBuilders.post("/admin/register")
                .param("username", "apple")
                .param("password", "123")
                .param("email", "");

        // ビュー名が"admin-register.html"であることを検証
        mockMvc.perform(request)
               .andExpect(view().name("admin-register.html"))
               .andExpect(model().attribute("error", true));
  
          // adminServiceのinsertメソッドが指定された引数で一回も呼び出さないことを検証する
        verify(adminService, never()).insert(eq("apple"), eq("1234"), eq(""));

    }
    @Test
    // Nameが空白の場合を検証する----------------------------------------------------------------------------------------------
    public void testRegister_NullName() throws Exception {
    	// リクエストの作成
    	RequestBuilder request = MockMvcRequestBuilders.post("/admin/register")
    			.param("username", "")
    			.param("password", "123")
    			.param("email", "apple@apple");
    	
    	// ビュー名が"admin-register.html"であることを検証
    	mockMvc.perform(request)
    	.andExpect(view().name("admin-register.html"))
    	.andExpect(model().attribute("error", true));
    	
    	// adminServiceのinsertメソッドが指定された引数で一回も呼び出さないことを検証する
    	verify(adminService, never()).insert(eq(""), eq("123"), eq("apple@apple"));
    	
    }
    @Test
    //すべてが空白の場合を検証する----------------------------------------------------------------------------------------------
    public void testRegister_AllNull() throws Exception {
    	// リクエストの作成
    	RequestBuilder request = MockMvcRequestBuilders.post("/admin/register")
    			.param("username", "")
    			.param("password", "")
    			.param("email", "");
    	
    	// ビュー名が"admin-register.html"であることを検証
    	mockMvc.perform(request)
    	.andExpect(view().name("admin-register.html"))
    	.andExpect(model().attribute("error", true));
    	
    	// adminServiceのinsertメソッドが指定された引数で一回も呼び出さないことを検証する
    	verify(adminService, never()).insert(eq(""), eq(""), eq(""));
    	
    }
    

}
