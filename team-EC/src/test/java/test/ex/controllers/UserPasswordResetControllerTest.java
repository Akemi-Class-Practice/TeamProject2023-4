package test.ex.controllers;

import static org.mockito.ArgumentMatchers.anyInt;
import static org.mockito.ArgumentMatchers.anyLong;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.never;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.model;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.redirectedUrl;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.view;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertNull;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.mock.web.MockHttpSession;
import org.springframework.test.web.servlet.MockMvc;

import jakarta.servlet.http.HttpSession;
import org.springframework.test.web.servlet.RequestBuilder;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;
import test.ex.models.entity.StudentEntity;
import test.ex.service.StudentService;

@SpringBootTest
@AutoConfigureMockMvc
public class UserPasswordResetControllerTest {
    @Autowired
    private MockMvc mockMvc;

    @MockBean
    private StudentService studentService;

    @Autowired
    private HttpSession session;

    // オブジェクトのセットアップとモックの設定 ----------------------------------------------------------
    @BeforeEach
    public void prepareData(){
        StudentEntity studentEntity = new StudentEntity("test","testPassword","testKeyword","test@example.com");

        when(studentService.selectByEmailAndKeyword(eq("test@example.com"),eq("testKeyword"))).thenReturn(studentEntity);

        when(studentService.selectByEmailAndKeyword(eq("wrong@example.com"),eq("testKeyword"))).thenReturn(null);
        when(studentService.selectByEmailAndKeyword(eq("test@example.com"),eq("wrongKeyword"))).thenReturn(null);

    }
    //ページに遷移できる
    @Test
    public void testGetResetPage() throws Exception {
        mockMvc.perform(get("/user/password/reset"))
                .andExpect(status().isOk())
                .andExpect(view().name("user-password-reset.html"))
                .andExpect(model().attribute("error", false));
    }

    //ページに遷移できる
    @Test
    public void testGetPasswordCompletedPage() throws Exception {
        mockMvc.perform(get("/user/password/completed"))
                .andExpect(status().isOk())
                .andExpect(view().name("user-password-completed.html"));
    }

    //mailとkeyword両方当たる
    @Test
    public void testPasswordReset_WithValidData() throws Exception {
        // Prepare test data
        StudentEntity studentEntity = new StudentEntity();
        studentEntity.setStudentId(1L);
        studentEntity.setStudentName("John Doe");
        studentEntity.setPoint(100);

        when(studentService.selectByEmailAndKeyword(anyString(), anyString())).thenReturn(studentEntity);

        mockMvc.perform(post("/user/password/reset")
                        .param("keyword", "testKeyword")
                        .param("email", "test@example.com")
                        .param("password", "newPassword")
                        .param("password2", "newPassword"))
                .andExpect(status().is3xxRedirection())
                .andExpect(redirectedUrl("/user/password/completed"));

        verify(studentService, times(1)).update(eq(1L), eq("John Doe"), eq("newPassword"), eq("testKeyword"), eq("test@example.com"), eq(100));
    }

    //mail間違った、keyword正しい
    @Test
    public void testPasswordReset_WithInvalidData_KeywordMismatch() throws Exception{

        // リクエストを作成して実行し、ステータスが成功であることを検証する
        RequestBuilder request = post("/user/password/reset")
                .param("keyword", "wrongKeyword")
                .param("email", "test@example.com")
                .param("password", "newPassword")
                .param("password2", "newPassword");

        mockMvc.perform(request)
                .andExpect(view().name("user-password-reset.html"));

        HttpSession session = mockMvc.perform(MockMvcRequestBuilders.get("/user/password/reset"))
                .andReturn()
                .getRequest().getSession();

        StudentEntity studentEntity = (StudentEntity) session.getAttribute("testName");
        assertNull(studentEntity);

        verify(studentService, never()).update(anyLong(), anyString(), anyString(), anyString(), anyString(), anyInt());
    }


    //mail正しい、keyword間違った
    @Test
    public void testPasswordReset_WithInvalidData_EmailMismatch() throws Exception{

        RequestBuilder request = post("/user/password/reset")
                .param("keyword", "testKeyword")
                .param("email", "wrong@example.com")
                .param("password", "newPassword")
                .param("password2", "newPassword");

        mockMvc.perform(request)
                .andExpect(view().name("user-password-reset.html"));

        HttpSession session = mockMvc.perform(MockMvcRequestBuilders.get("/user/password/reset"))
                .andReturn()
                .getRequest().getSession();

        StudentEntity studentEntity = (StudentEntity) session.getAttribute("testName");
        assertNull(studentEntity);

        verify(studentService, never()).update(anyLong(), anyString(), anyString(), anyString(), anyString(), anyInt());
    }

    //2回目入力したパスワードが間違えた。
    @Test
    public void testPasswordReset_WithInvalidData_PasswordMismatch() throws Exception {
        StudentEntity studentEntity = new StudentEntity();
        studentEntity.setStudentId(1L);
        studentEntity.setStudentName("John Doe");
        studentEntity.setPoint(100);

        when(studentService.selectByEmailAndKeyword(anyString(), anyString())).thenReturn(studentEntity);

        mockMvc.perform(post("/user/password/reset")
                        .param("keyword", "testKeyword")
                        .param("email", "test@example.com")
                        .param("password", "newPassword")
                        .param("password2", "differentPassword"))
                .andExpect(status().isOk())
                .andExpect(view().name("user-password-reset.html"))
                .andExpect(model().attribute("error", true))
                .andExpect(model().attribute("errorMessage", "パスワードが一致していません"));

        verify(studentService, never()).update(anyLong(), anyString(), anyString(), anyString(), anyString(), anyInt());
    }
}