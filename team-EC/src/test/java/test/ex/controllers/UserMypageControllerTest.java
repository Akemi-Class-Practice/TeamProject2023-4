package test.ex.controllers;

import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.model;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.redirectedUrl;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.view;

import java.util.ArrayList;
import java.util.List;

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

import test.ex.models.entity.BuyingHistoryEntity;
import test.ex.models.entity.StudentEntity;
import test.ex.service.BuyingHistoryService;
import test.ex.service.StudentService;

/**@SpringBootTest：Spring Bootアプリケーションのコンテキストをロードしてテストを実行するための注釈です。**/
@SpringBootTest
/**@AutoConfigureMockMvc：Spring MVCテストのためにMockMvcを自動的に設定するための注釈です。。**/
@AutoConfigureMockMvc
public class UserMypageControllerTest {
	@Autowired
	private MockMvc mockMvc;

	@MockBean
	private BuyingHistoryService buyingHistoryService;

	@MockBean
	private StudentService studentService;

	//テスト中にセッション属性を設定するために使用
	private MockHttpSession session;

	
	@BeforeEach
	//prepareData()メソッド: テストの前にデータを準備するためのメソッド
	public void prepareData() {
		//生徒情報を作成
		StudentEntity student = new StudentEntity();
		student.setStudentId(1L);
		student.setStudentName("John Doe");
		student.setPoint(100);
        //購入履歴情報を作成
		//2つのダミーデータを追加しています。
		List<BuyingHistoryEntity> buyingHistoryList = new ArrayList<>();
		buyingHistoryList.add(new BuyingHistoryEntity());
		buyingHistoryList.add(new BuyingHistoryEntity());

		//MockHttpSessionオブジェクトのインスタンスを作成し、session変数に割り当て
		session = new MockHttpSession();
		//sessionオブジェクトの属性に、"student"というキーで先ほど作成したstudentオブジェクトを設定
		session.setAttribute("student", student);

		//studentServiceのselectByStudentId()メソッドが引数1Lで呼び出された場合、
		//事前に準備したstudentオブジェクトを返すように設定
		when(studentService.selectByStudentId(1L)).thenReturn(student);
		//buyingHistoryServiceのgetBuyingHistory()メソッドが引数1Lで呼び出された場合、
		//事前に準備したbuyingHistoryListを返すように設定
		when(buyingHistoryService.getBuyingHistory(1L)).thenReturn(buyingHistoryList);
	}

	@Test
	public void testGetMypage() throws Exception {
		//MockMvcRequestBuildersクラスのget()メソッドを使用して、GETリクエストを作成
		///パスはuser/mypageであり、セッションオブジェクトsessionをセット
		RequestBuilder request = MockMvcRequestBuilders.get("/user/mypage")
				.session(session);

		mockMvc.perform(request)
		         //レスポンスのステータスコードがisOk()（HTTPステータスコード200）であることを検証
				.andExpect(status().isOk())
			 	 //レスポンスのビュー名が"user-mypage.html"であることを検証
				.andExpect(view().name("user-mypage.html"))
				//モデルに"point"、"student"、"listbuy"という属性が存在することを検証
				.andExpect(model().attributeExists("point", "student", "listbuy"))
				 //レスポンスのモデルの"point"属性が値100であることを検
				.andExpect(model().attribute("point", 100))
				//レスポンスのモデルの"student"属性がsessionオブジェクトの"student"属性と同じであることを検証
				.andExpect(model().attribute("student", session.getAttribute("student")))
				//レスポンスのモデルの"listbuy"属性がbuyingHistoryService.getBuyingHistory(1L)と同じであることを検証
				.andExpect(model().attribute("listbuy", buyingHistoryService.getBuyingHistory(1L)));
	}
	@Test
	public void testGetMypage_WithNullSessionAttribute_RedirectToLogin() throws Exception {
	    // セッションの "student" 属性を null に設定する
	    session.setAttribute("student", null);

	    mockMvc.perform(get("/user/mypage").session(session))
	             //レスポンスのステータスコードが3xxのリダイレクトであることを検証
	            .andExpect(status().is3xxRedirection())
	            // レスポンスが"/user/login"にリダイレクトされることを検証
	            .andExpect(redirectedUrl("/user/login"));
	}
	
}
