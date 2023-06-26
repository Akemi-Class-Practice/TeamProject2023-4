package test.ex.Controllers;

import static org.mockito.ArgumentMatchers.anyInt;
import static org.mockito.ArgumentMatchers.anyLong;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.redirectedUrl;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.view;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.mock.web.MockHttpSession;
import org.springframework.mock.web.MockMultipartFile;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;

import test.ex.models.entity.AdminEntity;
import test.ex.service.LessonService;

//テスト処理 -------------------------------------------------------------------------------------------------------------
@SpringBootTest							// Spring Bootアプリケーションのコンテキストをロードしてテストを実行するための注釈
@AutoConfigureMockMvc					// Spring MVCテストのためにMockMvcを自動的に設定する為の注釈

public class AdminLessonRegisterControllerTest {


    @Autowired
    private MockMvc mockMvc;			// テスト対象のコントロールと対話するためのモックされたMVCモックオブジェクト


    @MockBean							// モックオブジェクトを作成して注入するための注釈
    private LessonService lessonService;

    private MockHttpSession session;

    // セッションの設定 -----------------------------------------------------------------------------------------------------
    @BeforeEach
    public void advanceData(){

        // モックのHttpSessionを作成してセッションオブジェクトを取得する。
        session = new MockHttpSession();

        // ログイン中の管理者情報を設定
        AdminEntity admin = new AdminEntity();
        admin.setAdminId(1L);
        session.setAttribute("admin", admin); // sessionにadminをセット

    }

    /********************************************************************************************************************/
    /*      										管理者ログイン済みの場合の遷移テスト									        */
    /********************************************************************************************************************/
    @Test
    public void testAddcart_WithValidSession_RedirectToList() throws Exception {

        // ログイン済みのセッションを設定
        session.setAttribute("admin", new AdminEntity());

        // GETリクエストを送信し、結果を検証
        mockMvc.perform(get("/admin/lesson/register").session(session))
                .andExpect(status().isOk())									// レスポンスのHTTPステータスが200 (OK) であることを検証する。
                .andExpect(view().name("admin-lesson-register.html"));		// レスポンスのビュー名が "user-lesson-details.html"であることを検証する。
    }


    /********************************************************************************************************************/
    /*      										管理者ログイン無しの場合の遷移テスト									        */
    /********************************************************************************************************************/
    @Test
    public void testGetLessonRegisterPageNotLoggedIn() throws Exception {

        // ログインしていないセッションを設定
        session.setAttribute("admin", null);

        // GETリクエストを送信し、結果を検証
        mockMvc.perform(get("/admin/lesson/register").session(session))
                .andExpect(status().is3xxRedirection())						// レスポンスのHTTPステータスコードが3xxのリダイレクトであることを検証
                .andExpect(redirectedUrl("/admin/login"));					// リダイレクト先のURLが"/user/cart/list"であることを検証
    }

    /********************************************************************************************************************/
    /*                                 正常に講座が登録されるテスト(全パラメーター既存のブログと被りなし)                                */
    /********************************************************************************************************************/
    @Test
    public void testBlogRegister_Succeed() throws Exception {

        // ラストデータの準備
        MockMultipartFile file = new MockMultipartFile("imageName", "test.png", "image/png", new byte[0] ); // 画像のセット

        // テスト実行(テスト用のパラメーターを渡してあげる)
        mockMvc.perform(MockMvcRequestBuilders.multipart("/admin/lesson/register")
                        .file(file)
                        .param("lessonTitle", "tesTitle")
                        .param("content", "test content")
                        .param("fee", "100")
                        .session(session))
                .andExpect(status().isOk())                                          // レスポンスのHTTPステータスが200 (OK) であることを検証する。
                .andExpect(view().name("admin-lesson-register-finish.html"));        // レスポンスのビュー名が "admin-lesson-register-finish.html" であることを検証する。

        // mockのメソッドが正しく呼ばれたことを検証
        verify(lessonService, times(1)).insert(anyString(), anyString(), anyInt(), anyString(), anyLong());
    }

}





