package test.ex.controllers;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.doNothing;
import static org.mockito.Mockito.never;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.RequestBuilder;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;

import test.ex.service.StudentService;


@SpringBootTest                                           // Spring Bootアプリケーションのコンテキストをロードしてテストを実行するための注釈
@AutoConfigureMockMvc                            // Spring MVCテストのためにMockMvcを自動的に設定するための注釈
public class UserRegisterControllerTest {

    @Autowired                                            // テスト対象のコントローラと対話するためのモックされたMVCモックオブジェクト
    private MockMvc mockMvc;

    @MockBean                                          // モックオブジェクトを作成して注入するための注釈 
    private StudentService studentService;
    
    // オブジェクトの設定とモックの設定 ----------------------------------------------------------------------------------------------
    @BeforeEach     // 各テストメソッドの実行前に実行されるメソッドを示す。つまり、各テストケースの前に共通の前準備を行うために使用される。
    public void prepareData() {
 /*   	
        // メールが重複する場合＆メールが空白場合
        when(studentService.insert(eq("apple"), eq("123"), eq("12345"), eq("apple@apple"))).thenReturn(true);
        when(studentService.insert(eq("apple"), eq("123"), eq("12345"), eq("apple@apple"))).thenReturn(false);
        when(studentService.insert(eq("apple"), eq("123"), eq("12345"), eq(""))).thenReturn(false);
        // パスワードが空白場合＆ユーザー名が空白場合＆キーワードが空白場合
        when(studentService.insert(eq("banana"), eq(""), eq("12345"), eq("banana@banana"))).thenReturn(false);
        when(studentService.insert(eq(""), eq("yes@yes"), eq("1234"), eq("12345"))).thenReturn(false);
        when(studentService.insert(eq("link"), eq("link@link"), eq("1234"), eq(""))).thenReturn(false);
     */   
    	
    	
    	
    	// メールが重複する場合＆メールが空白場合
        doNothing().when(studentService).insert(eq("apple"), eq("123"), eq("12345"), eq("apple@apple"));
        doNothing().when(studentService).insert(eq("apple"), eq("123"), eq("12345"), eq(""));
        // パスワードが空白場合＆ユーザー名が空白場合＆キーワードが空白場合
        doNothing().when(studentService).insert(eq("banana"), eq(""), eq("12345"), eq("banana@banana"));
        doNothing().when(studentService).insert(eq(""), eq("yes@yes"), eq("1234"), eq("12345"));
        doNothing().when(studentService).insert(eq("link"), eq("link@link"), eq("1234"), eq(""));
    }               
    
    // 新規登録画面の表示をテスト---------------------------------------------------------------------------------------------------
    @Test
    public void testGetUserRegisterPage() throws Exception {
        // /user/registerへのgetリクエストの作成
        RequestBuilder request = MockMvcRequestBuilders.get("/user/register");

        // ビュー名が"user-register.html"であることを検証
        mockMvc.perform(request)
               .andExpect(view().name("user-register.html"));
    }
    
    // 新規登録完了画面の表示をテスト----------------------------------------------------------------------------------------------
    @Test
    public void testGetUserRegisterFinishPage() throws Exception {
        // /user/register/finishへのgetリクエストの作成
        RequestBuilder request = MockMvcRequestBuilders.get("/user/register/finish");

        // ビュー名が"user-register-finish.html"であることを検証
        mockMvc.perform(request)
               .andExpect(view().name("user-register-finish.html"));
    }
    
    // 正常なログインの成功を検証する--------------------------------------------------------------------------------------------  
    @Test
 // 未登録ユーザー名（apple)＆パスワード(123)＆キーワード(12345)＆ユーザーメール(apple@apple)で登録ボタンを押下
    public void testRegister_Successful() throws Exception {
       when(studentService.FindDuplicateEmail(anyString())).thenReturn(true);
        RequestBuilder request = MockMvcRequestBuilders.post("/user/register")
        	    .param("userName", "apple")
                .param("password", "123")
                .param("keypassword", "12345")
                .param("email", "apple@apple");
        // リクエストパラメーターを元にリクエストを実行
        mockMvc.perform(request)
        .andExpect(redirectedUrl("/user/register/finish"));
        // studentServiceのinsertメソッドが指定された引数で1回呼び出されたことを検証する
        verify(studentService, times(1)).insert("apple", "123", "12345", "apple@apple");
    }
    // 重複なメールがある場合を検証する--------------------------------------------------------------------------------------------  
    @Test
    public void testRegister_DuplicateEmail() throws Exception {
        // リクエストの作成
        RequestBuilder request = MockMvcRequestBuilders.post("/user/register")
                .param("userName", "apple")
                .param("password", "123")
                .param("keypassword", "12345")
                .param("email", "apple@apple");

        // ビュー名が"user-register.html"であることを検証
        mockMvc.perform(request)
               .andExpect(view().name("user-register.html"))
               .andExpect(model().attribute("error",true));

    
        // studentServiceのinsertメソッドが指定された引数で一回も呼び出さないことを検証する
  verify(studentService, never()).insert(eq("apple"), eq("123"), eq("12345"), eq("apple@apple"));
    }
    
    @Test
    // パスワードが空白の場合を検証する----------------------------------------------------------------------------------------------
    public void testRegister_NullStudentPassword() throws Exception {
        // リクエストの作成
        RequestBuilder request = MockMvcRequestBuilders.post("/user/register")
                .param("userName", "banana")
                .param("password", "")
                .param("keypassword", "12345")
                .param("email", "banana@banana");
 
        // ビュー名が"user-register.html"であることを検証
        mockMvc.perform(request)
               .andExpect(view().name("user-register.html"))
               .andExpect(model().attribute("error", true));
        
        // studentServiceのinsertメソッドが指定された引数で一回も呼び出さないことを検証する
        verify(studentService, never()).insert(eq("banana"), eq(""), eq("12345"), eq("banana@banana"));
   
    }
    
    @Test
    // ユーザー名が空白の場合を検証する----------------------------------------------------------------------------------------------
    public void testRegister_NullStudentName() throws Exception {
        // リクエストの作成
        RequestBuilder request = MockMvcRequestBuilders.post("/user/register")
                .param("userName", "")
                .param("password", "1234")
                .param("keypassword", "12345")
                .param("email", "yes@yes");

        // ビュー名が"user-register.html"であることを検証
        mockMvc.perform(request)
               .andExpect(view().name("user-register.html"))
               .andExpect(model().attribute("error", true));
  
          // studentServiceのinsertメソッドが指定された引数で一回も呼び出さないことを検証する
        verify(studentService, never()).insert(eq(""), eq("1234"), eq("12345"), eq("yes@yes"));

    }
    
    @Test
    // キーワードが空白の場合を検証する----------------------------------------------------------------------------------------------
    public void testRegister_NullKeyword() throws Exception {
        // リクエストの作成
        RequestBuilder request = MockMvcRequestBuilders.post("/user/register")
                .param("userName", "link")
                .param("password", "1234")
                .param("keypassword", "")
                .param("email", "link@link");

        // ビュー名が"user-register.html"であることを検証
        mockMvc.perform(request)
               .andExpect(view().name("user-register.html"))
               .andExpect(model().attribute("error",true));
        // studentServiceのinsertメソッドが指定された引数で一回も呼び出さないことを検証する
        verify(studentService, never()).insert(eq("link"), eq("1234"), eq(""), eq("link@link"));
    }
}