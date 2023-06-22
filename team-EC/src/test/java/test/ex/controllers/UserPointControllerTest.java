package test.ex.controllers;

import static org.assertj.core.api.Assertions.assertThat;
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
import org.springframework.mock.web.MockHttpSession;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.RequestBuilder;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;

import test.ex.models.dao.StudentDao;
import test.ex.models.entity.StudentEntity;

/**@SpringBootTest：Spring Bootアプリケーションのコンテキストをロードしてテストを実行するための注釈です。**/
@SpringBootTest
/**@AutoConfigureMockMvc：Spring MVCテストのためにMockMvcを自動的に設定するための注釈です。。**/
@AutoConfigureMockMvc
public class UserPointControllerTest {
	@Autowired
	private MockMvc mockMvc;
	@MockBean
	private StudentDao studentDao;



	//テスト中にセッション属性を設定するために使用
	private MockHttpSession session;
	
	@BeforeEach
	//prepareData()メソッド: テストの前にデータを準備するためのメソッド
	public void prepareData() {
		//生徒情報を作成
		StudentEntity student = new StudentEntity();
		student.setStudentId(1L);
		student.setStudentName("John Doe");
		student.setPoint(0);


		//MockHttpSessionオブジェクトのインスタンスを作成し、session変数に割り当て
		session = new MockHttpSession();
		//sessionオブジェクトの属性に、"student"というキーで先ほど作成したstudentオブジェクトを設定
		session.setAttribute("student", student);

		//studentServiceのselectByStudentId()メソッドが引数1Lで呼び出された場合、
		//事前に準備したstudentオブジェクトを返すように設定
		when(studentDao.findByStudentId(1L)).thenReturn(student);


	}
	
	//ポイントチャージページへの遷移テスト
	@Test
	public void testGetPointpage() throws Exception {
		//MockMvcRequestBuildersクラスのget()メソッドを使用して、GETリクエストを作成
		///パスはuser/mypageであり、セッションオブジェクトsessionをセット
		RequestBuilder request = MockMvcRequestBuilders.get("/user/point")
				.session(session);
		mockMvc.perform(request)
			//modelの値をセット
			.andExpect(model().attribute("point", 0))
			//user-point-charge.htmlが表示されているか
			.andExpect(view().name("user-point-charge.html"));
	}
	
	//チャージ完了画面への遷移テスト
	@Test
	public void testGetPointSuccesspage() throws Exception {
		//MockMvcRequestBuildersクラスのget()メソッドを使用して、GETリクエストを作成
		///パスはuser/mypageであり、セッションオブジェクトsessionをセット
		RequestBuilder request = MockMvcRequestBuilders.get("/user/point/success")
				.session(session);
		mockMvc.perform(request)
		//modelの値をセット
		.andExpect(model().attribute("point", 0))
		//user-point-charge-finish.htmlが表示されているか
		.andExpect(view().name("user-point-charge-finish.html"));
	}
	
	
	//500の場合
	@Test
	public void testPostPointSuccess() throws Exception {

		//MockMvcRequestBuildersクラスのget()メソッドを使用して、GETリクエストを作成
		///パスはuser/mypageであり、セッションオブジェクトsessionをセット
		RequestBuilder request = MockMvcRequestBuilders.post("/user/point/charge")
				//パラメータをセット
				.param("pointAmount","500")
				.session(session);
		//"/user/point/success"にredirectできるか？
		mockMvc.perform(request).andExpect(redirectedUrl("/user/point/success")).andReturn();
	}
	
	//1500の場合
	@Test
	public void testPostPointSuccess_1500() throws Exception {
		//MockMvcRequestBuildersクラスのget()メソッドを使用して、GETリクエストを作成
		///パスはuser/mypageであり、セッションオブジェクトsessionをセット
		RequestBuilder request = MockMvcRequestBuilders.post("/user/point/charge")
				//パラメータをセット
				.param("pointAmount","1500")
				.session(session);
		//"/user/point/success"にredirectできるか？
		mockMvc.perform(request).andExpect(redirectedUrl("/user/point/success")).andReturn();
	}
	
	//3000の場合
	@Test
	public void testPostPointSuccess_3000() throws Exception {
		//MockMvcRequestBuildersクラスのget()メソッドを使用して、GETリクエストを作成
		///パスはuser/mypageであり、セッションオブジェクトsessionをセット
		RequestBuilder request = MockMvcRequestBuilders.post("/user/point/charge")
				//パラメータをセット
				.param("pointAmount","3000")
				.session(session);
		//"/user/point/success"にredirectできるか？
		mockMvc.perform(request).andExpect(redirectedUrl("/user/point/success")).andReturn();
	}

	
}









