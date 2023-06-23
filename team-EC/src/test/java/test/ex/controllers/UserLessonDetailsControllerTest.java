package test.ex.controllers;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.model;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.redirectedUrl;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.view;

import java.util.ArrayList;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.mock.web.MockHttpSession;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.RequestBuilder;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;

import test.ex.controllers.UserLessonDetailsController;
import test.ex.models.entity.LessonEntity;
import test.ex.models.entity.StudentEntity;
import test.ex.service.LessonService;
import test.ex.service.StudentService;

//テスト処理 -------------------------------------------------------------------------------------------------------------
@SpringBootTest							// Spring Bootアプリケーションのコンテキストをロードしてテストを実行するための注釈
@AutoConfigureMockMvc					// Spring MVCテストのためにMockMvcを自動的に設定する為の注釈
public class UserLessonDetailsControllerTest {

	@Autowired
	private MockMvc mockMvc;			// テスト対象のコントロールと対話するためのモックされたMVCモックオブジェクト
	
	@MockBean							// モックオブジェクトを作成して注入するための注釈
	private StudentService studentService;
	
	@MockBean							// モックオブジェクトを作成して注入するための注釈
	private LessonService lessonService;
	
	@Autowired							// 自動でインスタンスの紐づけを行う
    private UserLessonDetailsController userLessonDetailsController;
	
	private MockHttpSession session;
	
	// セッションの設定 -----------------------------------------------------------------------------------------------------
	@BeforeEach
	public void advanceData(){

		// モックのHttpSessionを作成してセッションオブジェクトを取得する。
		session = new MockHttpSession();
		
		 // セッションにstudent属性として新しいStudentEntityオブジェクトを設定する。
		session.setAttribute("student", new StudentEntity());

		// 引数に603Lを渡すと、新しいLessonEntityオブジェクトを返すように設定する。
		when(lessonService.selectByLessonId(603L)).thenReturn(new LessonEntity());

	}
	
	/********************************************************************************************************************/
	/*                                				講座詳細情報画面の表示テスト			                                    */
	/********************************************************************************************************************/	
	@Test
	public void testGetLessonDetailPage_Success() throws Exception{
				
		// LessonEntity オブジェクトとして lesson を作成し、lessonId に 603Lを格納する。 
		LessonEntity lesson = new LessonEntity();
		lesson.setLessonId(603L);
		lesson.setLessonName("aaaa");

		// モックの動作設定(引数に603Lを渡すと、上で作成した lessonオブジェクトを返すように設定されている。)
		when(lessonService.selectByLessonId(603L)).thenReturn(lesson);	
		
		// テスト実行
		mockMvc.perform(get("/user/lesson/detail/{lessonId}", 603L))	// /user/lesson/detailへのリクエストを実行する
		.andExpect(status().isOk())										// レスポンスのHTTPステータスが200 (OK) であることを検証する。
		.andExpect(view().name("user-lesson-details.html"))				// レスポンスのビュー名が "user-lesson-details.html"であることを検証する。
		.andExpect(model().attributeExists("lessonList"))				// レスポンスのモデルに "lessonList" 属性が存在することを検証する。
		.andExpect(model().attribute("lessonList", lesson));			// モデルの "lessonList"属性が先ほど作成した lesson オブジェクトと同じであることを検証します。
		
		// lwssonService の selectByLessonId メソッドが引数に 603Lを渡して1回呼び出されたことを検証する。
		verify(lessonService, times(1)).selectByLessonId(603L);
	}
	
	/********************************************************************************************************************/
	/*                                				カートの重複テスト(講座が存在する場合)		                                */
	/********************************************************************************************************************/	
    @Test
    public void testIsLessonExist() throws Exception{
    	
    	// LessonEntity オブジェクトとして lesson を作成し、lessonId に 603Lを格納する。
        LessonEntity lesson1 = new LessonEntity();
        lesson1.setLessonId(603L);

        // テスト比較するためのデータ(lesson1)をlessonListに格納する
        ArrayList<LessonEntity> lessonList = new ArrayList<>();
        lessonList.add(lesson1);
        
        // テスト実行(603L(擬似指定Id)とlessonListに入っているlessonId(603L)を比較している)
        boolean result = userLessonDetailsController.isLessonExist(603L, lessonList);

        // 講座が存在する場合はtrueが返されることを検証する
        assertTrue(result);
    }

	/********************************************************************************************************************/
	/*                                				カートの重複テスト(講座が存在しない場合)		                                */
	/********************************************************************************************************************/	
    @Test
    public void testIsLessonNotExist() throws Exception{
  
    	// LessonEntity オブジェクトとして lesson を作成し、lessonId に 603Lを格納する。
        LessonEntity lesson1 = new LessonEntity();
        lesson1.setLessonId(603L);

        // テスト比較するためのデータ(lesson1)をlessonListに格納する
        ArrayList<LessonEntity> lessonList = new ArrayList<>();
        lessonList.add(lesson1);

        // テスト実行(604L(擬似指定Id)とlessonListに入っているlessonId(603L)を比較している)
        boolean result = userLessonDetailsController.isLessonExist(604L, lessonList);

        // 講座が存在しない場合はfalseが返されることを検証する
        assertFalse(result);
    }

	/********************************************************************************************************************/
	/*      						有効なセッション属性と共にカートにレッスンを追加し、リストにリダイレクトすることを検証するテスト			        */
	/********************************************************************************************************************/	
    @Test
	public void testAddcart_WithValidSession_RedirectToList() throws Exception {

    	///user/cart/addにPOSTリクエストを送信
		RequestBuilder request = MockMvcRequestBuilders.post("/user/cart/add")
		.param("lessonId", "603")												// パラメータとしてlessonId(603)を指定
		.session(session);														// セッションに有効なセッション属性が設定されていることを検証

		// テスト実行
		mockMvc.perform(request)
		.andExpect(status().is3xxRedirection())									// レスポンスのHTTPステータスコードが3xxのリダイレクトであることを検証
		.andExpect(redirectedUrl("/user/cart/list"));							// リダイレクト先のURLが"/user/cart/list"であることを検証
	}

	/********************************************************************************************************************/
	/*      								セッション属性がnullの場合に、ログインページにリダイレクトすることを検証するテスト			        */
	/********************************************************************************************************************/	
	@Test
	public void testAddcart_WithNullSessionAttribute_RedirectToLogin() throws Exception {

		//セッションの"student"属性をnullに設定
		session.setAttribute("student", null);
		
        ///user/cart/addにPOSTリクエストを送信
		mockMvc.perform(MockMvcRequestBuilders.post("/user/cart/add")
		.param("lessonId", "1")													// パラメータとしてlessonId(603)を指定
		.session(session))														// セッションに有効なセッション属性が設定されていることを検証
		
		.andExpect(status().is3xxRedirection())									// レスポンスのHTTPステータスコードが3xxのリダイレクトであることを検証
		.andExpect(redirectedUrl("/user/login"));								// リダイレクト先のURLが"/user/login"であることを検証
	}
    
	/********************************************************************************************************************/
	/*      					有効なセッションと新しいレッスンが与えられた場合に、カートにレッスンが追加されることを検証するテスト			        */
	/********************************************************************************************************************/	
	@Test
	public void testAddcart_WithValidSessionAndNewLesson_AddsLessonToCart() throws Exception {

    	// LessonEntity オブジェクトとして lesson を作成し、lessonId に 603Lを格納する。
	    LessonEntity lesson = new LessonEntity();
	    lesson.setLessonId(603L);
	    when(lessonService.selectByLessonId(603L)).thenReturn(lesson);

	    // 空のリストをカートとしてセッションに設定
	    ArrayList<LessonEntity> cartList = new ArrayList<>();
	    session.setAttribute("cart", cartList);
	    
	    // /user/cart/addにPOSTリクエストを送信
	    mockMvc.perform(post("/user/cart/add")
	            .param("lessonId", "603")
	            .session(session));
	    
	    // セッションに追加されたレッスンがカートに正しく追加されたかを検証
	    @SuppressWarnings("unchecked")
	    ArrayList<LessonEntity> updatedCartList = (ArrayList<LessonEntity>) session.getAttribute("cart");
	    assertEquals(1, updatedCartList.size());		//カートのリストのサイズが1であり、追加されたレッスンが既存のレッスンと同じであることを検証する
	    assertEquals(lesson, updatedCartList.get(0));	//カートに追加された最初のレッスンが、テストデータのレッスンと等しいことを検証する

	}

	/********************************************************************************************************************/
	/*      					有効なセッションと既存のレッスンが与えられた場合に、カートにレッスンが追加されないことを検証するテスト		        */
	/********************************************************************************************************************/	
	@Test
	public void testAddcart_WithValidSessionAndExistingLesson_DoesNotAddLessonToCart() throws Exception {

    	// LessonEntity オブジェクトとして lesson を作成し、lessonId に 603Lを格納する。
	    LessonEntity lesson = new LessonEntity();
	    lesson.setLessonId(603L);
	    when(lessonService.selectByLessonId(603L)).thenReturn(lesson);

	    // 既存の講座が追加されたカートリストをセッションに設定
	    ArrayList<LessonEntity> cartList = new ArrayList<>();
	    cartList.add(lesson);
	    session.setAttribute("cart", cartList);
	    
	    // /user/cart/addにPOSTリクエストを送信
	    mockMvc.perform(post("/user/cart/add")
	            .param("lessonId", "603")
	            .session(session));
	    
	    // セッションのカートにレッスンが追加されなかったことを検証
	    @SuppressWarnings("unchecked")
	    ArrayList<LessonEntity> updatedCartList = (ArrayList<LessonEntity>) session.getAttribute("cart");
	    assertEquals(1, updatedCartList.size());		// カートのリストのサイズが1であり、追加されたレッスンが既存のレッスンと同じであることを検証する
	    assertEquals(lesson, updatedCartList.get(0));	// カートに追加された最初のレッスンが、テストデータのレッスンと等しいことを検証する
	}
	
}
