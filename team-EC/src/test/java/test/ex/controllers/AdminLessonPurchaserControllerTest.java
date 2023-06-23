package test.ex.controllers;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;
import org.springframework.ui.Model;

import jakarta.servlet.http.HttpSession;
import test.ex.models.dao.CoursePurchaserDao;
import test.ex.models.entity.AdminEntity;
import test.ex.models.entity.CoursePurchaserEntity;

import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

import java.util.ArrayList;
import java.util.List;

@SpringBootTest
@AutoConfigureMockMvc
public class AdminLessonPurchaserControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private CoursePurchaserDao coursePurchaserDao;

    @Autowired
    private HttpSession session;

    @BeforeEach
    public void perpareData() {
        // HttpSessionのモック設定
        AdminEntity adminEntity = new AdminEntity();
        adminEntity.setAdminId(1L);
        session.setAttribute("admin", adminEntity);
    }

    // ユーザーがログインしていない状態でアドミン購入者一覧ページに遷移できるか確認
    @Test
    public void testGetAdminLessonBuyControllerWithoutLoggedInUser() throws Exception {
        mockMvc.perform(MockMvcRequestBuilders.get("/admin/lesson/purchaser/list/{lessonId}", 123L))
                .andExpect(status().is3xxRedirection())
                .andExpect(redirectedUrl("/admin/login"));
    }

    // ユーザーがログインしている状態でアドミン購入者一覧ページに遷移できるか確認/////未完成
    @Test
    public void testGetAdminLessonBuyControllerWithLoggedInUser() throws Exception {
        // CoursePurchaserDaoのモック設定
        List<CoursePurchaserEntity> mockList = new ArrayList<>();
        when(coursePurchaserDao.findByLessonId(123L)).thenReturn(mockList);

        mockMvc.perform(MockMvcRequestBuilders.get("/admin/lesson/purchaser/list/{lessonId}", 123L))
                .andExpect(status().isOk())
                .andExpect(view().name("admin-course-management.html"))
                .andExpect(model().attributeExists("purchaserList"));
    }
}