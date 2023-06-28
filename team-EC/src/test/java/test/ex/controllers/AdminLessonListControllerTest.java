package test.ex.controllers;

import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.model;
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
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.RequestBuilder;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;

import test.ex.models.entity.LessonEntity;
import test.ex.service.LessonService;

// テスト処理 -------------------------------------------------------------------------------------------------------------
@SpringBootTest							// Spring Bootアプリケーションのコンテキストをロードしてテストを実行するための注釈
@AutoConfigureMockMvc					// Spring MVCテストのためにMockMvcを自動的に設定する為の注釈
public class AdminLessonListControllerTest {

	@Autowired
	private MockMvc mockMvc;			// テスト対象のコントロールと対話するためのモックされたMVCモックオブジェクト
	
	@MockBean							// モックオブジェクトを作成して注入するための注釈
	private LessonService lessonService;
	
	// オブジェクトの設定とモックの設定 -------------------------------------------------------------------------------------------
	@BeforeEach
	public void advanceData() {

		// 講座のインスタンス作成(講座の数分)
		List<LessonEntity> lessonList = new ArrayList<>();
		lessonList.add(new LessonEntity());
		lessonList.add(new LessonEntity());
		lessonList.add(new LessonEntity());
		lessonList.add(new LessonEntity());
		lessonList.add(new LessonEntity());

		// 一覧表示の成功
		when(lessonService.selectFindAll()).thenReturn(lessonList);
	}
	
	/********************************************************************************************************************/
	/*                                					講座一覧画面の表示テスト			                                    */
	/********************************************************************************************************************/	
	@Test
	public void testGetLessonListPage_Success() throws Exception{
		
		// /admin/lesson/listへのPOSTリクエストを作成する
		RequestBuilder request = MockMvcRequestBuilders.get("/admin/lesson/list");
		
		// mockMvcを使用してリクエストを実行する。
		mockMvc.perform(request)
		.andExpect(status().isOk())							// レスポンスのHTTPステータスが200 (OK) であることを検証する。
		.andExpect(view().name("admin-lesson-list.html"))	// レスポンスのビュー名が "admin-lesson-list.html"であることを検証する。 
		.andExpect(model().attributeExists("lessonList"));	// レスポンスのモデルに "lessonList" 属性が存在することを検証する。
	}
	
	
}
