package test.ex.controllers;

import static org.mockito.ArgumentMatchers.anyInt;
import static org.mockito.ArgumentMatchers.anyLong;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.Mockito.doNothing;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.model;
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
import org.springframework.test.web.servlet.RequestBuilder;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;


import test.ex.models.entity.AdminEntity;

import test.ex.models.entity.LessonEntity;

import test.ex.service.LessonService;
import test.ex.service.TransactionItemService;

/**@SpringBootTest：Spring Bootアプリケーションのコンテキストをロードしてテストを実行するための注釈です。**/
@SpringBootTest
/**@AutoConfigureMockMvc：Spring MVCテストのためにMockMvcを自動的に設定するための注釈です。。**/
@AutoConfigureMockMvc
public class AdminLessonEditControllerTest {

	@Autowired
	private MockMvc mockMvc;
	@MockBean
	private LessonService lessonService;

	@MockBean
	private TransactionItemService transactionItemService;
	
	//テスト中にセッション属性を設定するために使用
	private MockHttpSession session;
	
	@BeforeEach
	//prepareData()メソッド: テストの前にデータを準備するためのメソッド
	public void prepareData() {
		LessonEntity lesson = new LessonEntity();
		
		//admin情報を作成
		AdminEntity admin = new AdminEntity();
		admin.setAdminId(1L);


		//MockHttpSessionオブジェクトのインスタンスを作成し、session変数に割り当て
		session = new MockHttpSession();
		//sessionオブジェクトの属性に、"student"というキーで先ほど作成したstudentオブジェクトを設定
		session.setAttribute("admin", admin);
		
		//lessonServiceのselectByLessonId()メソッドが引数1Lで呼び出された場合、
		//事前に準備したlessonオブジェクトを返すように設定
		when(lessonService.selectByLessonId(1L)).thenReturn(lesson);
	
	}
	
	
	//講座編集画面にアクセス
		@Test
		public void testGetLessonEditPage() throws Exception {
			LessonEntity lesson = new LessonEntity();
			lesson.setLessonId(1L);;
			lesson.setLessonName("Test");;
			lesson.setLessonDetail("test");
			lesson.setLessonFee(10);
			when(lessonService.selectByLessonId(1L)).thenReturn(lesson);

			// テスト実行
			RequestBuilder request = MockMvcRequestBuilders.get("/admin/lesson/edit/{lessonId}", 1L)
					.session(session);
			mockMvc.perform(request)
			.andExpect(view().name("admin-lesson-edit.html"))
			.andExpect(model().attribute("lessonList",lesson));
		}
		
		//講座編集完了画面にアクセス
		@Test
		public void testgetlessonEditFinishPage() throws Exception {
			// テスト実行
			RequestBuilder request = MockMvcRequestBuilders.get("/admin/lesson/edit/finish");
			mockMvc.perform(request).andExpect(view().name("admin-lesson-edit-finish.html"));

		}
		
		//講座削除完了画面にアクセス
		@Test
		public void testgetlessonDeletedPage() throws Exception {
			// テスト実行
			RequestBuilder request = MockMvcRequestBuilders.get("/admin/lesson/deleted");
			mockMvc.perform(request).andExpect(view().name("admin-lesson-deleted.html"));
			
		}
		
		//sessionがnullだった場合
		@Test
		public void testGetLessonEditPage_WithNullSessionAttribute_RedirectToLogin() throws Exception {
			
		    // セッションの "student" 属性を null に設定する
		    session.setAttribute("admin", null);

		    mockMvc.perform(get("/admin/lesson/edit/{lessonId}",1L).session(session))
		             //レスポンスのステータスコードが3xxのリダイレクトであることを検証
		            .andExpect(status().is3xxRedirection())
		            // レスポンスが"/user/login"にリダイレクトされることを検証
		            .andExpect(redirectedUrl("/admin/login"));
		}
		
		//編集成功
		@Test
		public void LessonEdit_Succeed() throws Exception {
			
			doNothing().when(lessonService).update(anyLong(),anyString(),anyString(),anyInt(),anyString(),anyLong());
			//画像の仮情報をimageNameをセット
			MockMultipartFile imageName = new MockMultipartFile("imageName", "test-image.jpg", "image/jpeg", new byte[0]);
			// テスト実行
			mockMvc.perform(MockMvcRequestBuilders.multipart("/admin/lesson/edit")
					.file(imageName)
					//パラメータをセット
					.param("lessonName", "Test Title")
					.param("content", "Test Detail")
					.param("fee", "10")
					.param("lessonId", "1")
					.session(session))
			//redirectできるか検証
			.andExpect(redirectedUrl("/admin/lesson/edit/finish"));

		}
		
		//削除成功
		@Test
		public void LessonDelete_Succeed() throws Exception {
			// テストデータの準備
			long lessonId = 1L;
			// モックの動作設定
			doNothing().when(transactionItemService).delete(lessonId);
			doNothing().when(lessonService).delete(lessonId);
		
			mockMvc.perform(MockMvcRequestBuilders.multipart("/admin/lesson/delete")
					//パラメータをセット
					.param("lessonId", "1")
					.session(session))
			//redirectできるか検証
			.andExpect(redirectedUrl("/admin/lesson/deleted"));
			
		}
	
	
}
