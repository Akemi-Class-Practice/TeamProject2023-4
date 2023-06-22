package test.ex.controllers;

import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.redirectedUrl;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.RequestBuilder;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;

import jakarta.servlet.http.HttpSession;
import test.ex.models.entity.StudentEntity;
import test.ex.service.LessonService;
@SpringBootTest
@AutoConfigureMockMvc
public class UserCartControllerTest {
	@Autowired
	private MockMvc mockMvc;
	@Autowired
	private LessonService lessonService;

	@Autowired
	HttpSession session;

	
	@BeforeEach
	public void perpareData() {
		
		
	}
	
	
	
	//MyPageに遷移しない(Loginしていない)
	@Test
	public void testGetUserRegisterPage_UnSuccessful() throws Exception {
		RequestBuilder request = MockMvcRequestBuilders.get("/user/cart/list");
		mockMvc.perform(request).andExpect(redirectedUrl("/user/login"));
	}
	
	//ユーザーがログインしている状態でカート一覧ページに遷移できるかどうか
	@Test
	public void testGetUserPage() throws Exception{
		StudentEntity userList = new StudentEntity();
		userList.setStudentId(1L);
		userList.setStudentName("mike");
		
		
	
	}
	
	
	
	
	
	
	
	
	

}
