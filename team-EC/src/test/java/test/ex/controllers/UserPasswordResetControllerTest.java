package test.ex.Controllers;

import static org.mockito.ArgumentMatchers.anyInt;
import static org.mockito.ArgumentMatchers.anyLong;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.never;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
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

import jakarta.servlet.http.HttpSession;
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

    @BeforeEach
    public void setup() {
        session.invalidate();//sessionを無効化、各テストのメソット独立して実行する
    }

    @Test
    public void testGetResetPage() throws Exception {
        mockMvc.perform(get("/user/password/reset"))
                .andExpect(status().isOk())
                .andExpect(view().name("user-password-reset.html"))
                .andExpect(model().attribute("error", false));
    }

    @Test
    public void testGetPasswordCompletedPage() throws Exception {
        mockMvc.perform(get("/user/password/completed"))
                .andExpect(status().isOk())
                .andExpect(view().name("user-password-completed.html"));
    }

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

    @Test
    public void testPasswordReset_WithInvalidData_KeyMismatch() throws Exception{
        StudentEntity studentEntity = new StudentEntity();
        studentEntity.setStudentId(1L);
        studentEntity.setStudentName("John Doe");
        studentEntity.setPoint(100);

        when(studentService.selectByEmailAndKeyword(anyString(), anyString())).thenReturn(studentEntity);

        mockMvc.perform(post("/user/password/reset")
                        .param("keyword", "wrongKeyword")
                        .param("email", "test@example.com")
                        .param("password", "newPassword")
                        .param("password2", "differentPassword"))

                .andExpect(view().name("user-password-reset.html"));

        verify(studentService, never()).update(anyLong(), anyString(), anyString(), anyString(), anyString(), anyInt());
    }


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