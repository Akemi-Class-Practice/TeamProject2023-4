package test.ex.controllers;

import static org.junit.jupiter.api.Assertions.assertTrue;
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
import org.springframework.mock.web.MockHttpSession;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;
import org.springframework.web.context.WebApplicationContext;

import jakarta.servlet.http.HttpSession;
import test.ex.models.entity.LessonEntity;
import test.ex.models.entity.StudentEntity;

@SpringBootTest
@AutoConfigureMockMvc
public class UserCartControllerTest2{

    @Autowired
    private WebApplicationContext context;
    private MockMvc mockMvc;

    @Autowired
    HttpSession session;

    @BeforeEach
    public void perpareData() {
    	 // テスト実行前に、MockMvcをセットアップ
        mockMvc = MockMvcBuilders.webAppContextSetup(context).build();
    }

    //ユーザーがログインしていない状態でカート一覧ページに遷移できるか確認
    @Test
    public void testGetUserCartController() throws Exception {
        mockMvc.perform(MockMvcRequestBuilders.get("/user/cart/list"))
                .andExpect(status().is3xxRedirection())
                .andExpect(redirectedUrl("/user/login"));
    }

    //ユーザーがログインしている状態でカート一覧ページに遷移できるか確認
    @Test
    public void testGetUserCartPage() throws Exception{
        // テストに必要なデータ制作
        StudentEntity student = new StudentEntity();
        ArrayList<LessonEntity> cartList = new ArrayList<>();
        cartList.add(new LessonEntity());
        
        // テスト時にセッションを使用
        MockHttpSession session = new MockHttpSession();
        session.setAttribute("student", student);
        session.setAttribute("cart", cartList);

        // カート一覧ページにアクセスして、必要な情報が表示されることを確認　
        mockMvc.perform(MockMvcRequestBuilders.get("/user/cart/list").session(session))
        // モデル内に"cartList"属性が含まれていることを検証
            .andExpect(model().attributeExists("cartList"))
            // レスポンスのビュー名が"user-cart-list.html"か検証
            .andExpect(view().name("user-cart-list.html"));
    }
    
    //カートリストに存在する講座が削除されたか確認
    @Test
    public void testDeleteCart() throws Exception {
        // カートリストを作成し、削除する講座を追加
        ArrayList<LessonEntity> cartList = new ArrayList<>();
        LessonEntity lesson1 = new LessonEntity();
        lesson1.setLessonName("Lesson 1");
        cartList.add(lesson1);
        
        //cartListをセッションに設定
        session.setAttribute("cart", cartList);

        // 削除する講座名
        String lessonNameToDelete = "Lesson 1";

        // MockHttpSessionを作成してセッションを設定
        MockHttpSession mockSession = new MockHttpSession();
        mockSession.setAttribute("cart", cartList);

        // POSTリクエストを作成して削除処理を実行
        mockMvc.perform(MockMvcRequestBuilders.post("/user/cart/delete")
                .param("lessonName", lessonNameToDelete)
                .session(mockSession))    
        		//リダイレクト先が("/user/cart/list")であることを検証
                .andExpect(redirectedUrl("/user/cart/list"));

        // 削除されたかどうかを検証
        //mockSessionから更新されたカートリストを取得
        ArrayList<LessonEntity> updatedCartList = (ArrayList<LessonEntity>) mockSession.getAttribute("cart");
        
        //削除した講座がカートリストから削除されたか検証　同じ名前の講座がカートリストにないか確認しtrueを返す
        boolean isLessonDeleted = updatedCartList.stream()
                .noneMatch(lessonEntity -> lessonEntity.getLessonName().equals(lessonNameToDelete));
        
        //isLessonDeletedがtrueであることを検証
        assertTrue(isLessonDeleted);
    }
    
}