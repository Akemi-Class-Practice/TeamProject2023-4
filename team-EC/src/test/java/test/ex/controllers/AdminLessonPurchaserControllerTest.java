package test.ex.controllers;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;
import org.springframework.test.web.servlet.result.MockMvcResultMatchers;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;
import org.springframework.ui.Model;

import jakarta.servlet.http.HttpSession;
import test.ex.models.dao.CoursePurchaserDao;
import test.ex.models.entity.AdminEntity;
import test.ex.models.entity.CoursePurchaserEntity;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

import java.util.ArrayList;
import java.util.List;

@SpringBootTest
@AutoConfigureMockMvc
public class AdminLessonPurchaserControllerTest{

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private CoursePurchaserDao coursePurchaserDao;

    @Autowired
    private HttpSession session;

    @BeforeEach
    public void perpareData() {

    }

    // ユーザーがログインしていない状態でアドミン購入者一覧ページに遷移できるか確認
    @Test
    public void testGetAdminLessonBuyControllerWithoutLoggedInUser() throws Exception {
    	//指定したURlに遷移
        mockMvc.perform(MockMvcRequestBuilders.get("/admin/lesson/purchaser/list/{lessonId}", 123L))
                // リダイレクト先のURLが"/admin/login"であることを検証
                .andExpect(redirectedUrl("/admin/login"));
    }

    // ユーザーがログインしている状態でアドミン購入者一覧ページに遷移できるか確認
    @Test
    public void testGetPurchaserListWithLoggedInAdmin() throws Exception {        
        // ログイン済みのアドミンユーザーを作成
        AdminEntity loggedInAdmin = new AdminEntity();
        
        //指定されたURLにGETリクエストを送信
       mockMvc.perform(MockMvcRequestBuilders.get("/admin/lesson/purchaser/list/{lessonId}", 123L)
                
        		//セッションにadminを追加
        		.sessionAttr("admin", loggedInAdmin))
       
       			//レスポンスのステータスコードが200であることを検証
                .andExpect(status().isOk())
                
            	// モデルに指定の属性が存在することを検証
                .andExpect(model().attributeExists("purchaserList"))
                
                // ビューの名前が指定されたものであることを検証
                .andExpect(view().name("admin-course-management.html"));
    }
}