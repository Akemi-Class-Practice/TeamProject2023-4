package controller;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.web.servlet.MockMvc;

import jakarta.servlet.http.HttpSession;
import test.ex.models.entity.StudentEntity;
import test.ex.service.LessonService;

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
	
	//ユーザーがログインしている状態でカート一覧ページに遷移できるかどうか
	@Test
	public void testGetUserPage() throws Exception{
		StudentEntity userList = new StudentEntity();
		userList.setStudentId(1L);
		userList.setStudentName("mike");
		
		
	
	}
	
	
	
	
	
	
	
	
	

}
