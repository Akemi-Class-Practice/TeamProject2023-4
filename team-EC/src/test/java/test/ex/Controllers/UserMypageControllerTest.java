package test.ex.Controllers;

import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.when;
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
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.RequestBuilder;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;

import test.ex.models.entity.StudentEntity;
import test.ex.service.BuyingHistoryService;
import test.ex.service.StudentService;

/**@SpringBootTest：Spring Bootアプリケーションのコンテキストをロードしてテストを実行するための注釈です。**/
@SpringBootTest
/**@AutoConfigureMockMvc：Spring MVCテストのためにMockMvcを自動的に設定するための注釈です。。**/
@AutoConfigureMockMvc
public class UserMypageControllerTest {

	/**MockMvc mockMvc：テスト対象のコントローラと対話するためのモックされたMVCモックオブジェクトです。**/
	@Autowired
	private MockMvc mockMvc;
	
	/** @MockBean：モックオブジェクトを作成して注入するための注釈です。このテストでは、UserServiceのモックオブジェクトが作成されます。**/
	@MockBean
	private BuyingHistoryService buyingHistoryService;

	private StudentService studentService;
	
	private MockHttpSession session;
	
//	@BeforeEach//test前に実行されるアノテーション
//	public void prepareData() {
//		
//	}
	
	
	//MyPageに遷移しない(Loginしていない)
		@Test
		public void testGetUserRegisterPage_UnSuccessful() throws Exception {
			RequestBuilder request = MockMvcRequestBuilders.get("/user/mypage");
			mockMvc.perform(request).andExpect(redirectedUrl("/user/login"));
		}
		
	//MyPageに遷移(Loginしている)
		@Test
		public void testGetUserRegisterPage_Successful() throws Exception {
			
			StudentEntity studentEntity = new StudentEntity();
			studentEntity.setStudentId(1L);
			studentEntity.setPoint(0);
	
			session = new MockHttpSession();
			session.setAttribute("student", studentEntity);
			
			
			
			
			
//			
//			
//			StudentEntity student = (StudentEntity) session.getAttribute("student");
//			Long studentId = student.getStudentId();
//			StudentEntity studentEntity = studentService.selectByStudentId(studentId);
//			when(studentEntity.getPoint()).thenReturn(point);
			
			
			
			
			RequestBuilder request = MockMvcRequestBuilders.get("/user/mypage")
			 .session(session);
			mockMvc.perform(request).andExpect(model().attribute("point",studentEntity.getPoint()))
			.andExpect(view().name("user-mypage.html"));
		}
	
}
