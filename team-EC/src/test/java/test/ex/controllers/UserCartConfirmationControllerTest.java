package test.ex.controllers;

import static org.junit.jupiter.api.Assertions.assertNull;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.redirectedUrl;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import java.util.ArrayList;
import java.util.Collections;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.mock.web.MockHttpSession;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.web.context.WebApplicationContext;

import jakarta.servlet.http.HttpSession;
import test.ex.models.dao.BuyingHistoryDao2;
import test.ex.models.dao.TransactionHistoryDao;
import test.ex.models.dao.TransactionItemDao;
import test.ex.models.entity.LessonEntity;
import test.ex.models.entity.StudentEntity;
import test.ex.models.entity.TransactionHistoryEntity;
import test.ex.models.entity.TransactionItemEntity;
import test.ex.service.StudentService;

@SpringBootTest
@AutoConfigureMockMvc
public class UserCartConfirmationControllerTest {
  
	@Autowired
    private WebApplicationContext context;
	
    private MockMvc mockMvc;
	@Autowired
	private TransactionHistoryDao transactionHistoryDao;
	
	@Autowired
	private StudentService studentService;
	
	@Autowired
	private TransactionItemDao transactionItemDao;

	
    @Autowired
    private BuyingHistoryDao2 buyingHistoryDao2;

    
    @Autowired
    private StudentService StudentService;


    @Autowired
    HttpSession session;


    @Test
    public void testCompletedCartSuccess() throws Exception {
        // テストデータを準備
        Long mockUserId = 12345L;
        Long mockLessonId1 = 1L;
        Long mockLessonId2 = 2L;
        String mockLessonName1 = "Lesson 1";
        String mockLessonName2 = "Lesson 2";
        int mockLessonPrice1 = 500;
        int mockLessonPrice2 = 1000;

        // セッションオブジェクトを設定
        MockHttpSession mockSession = new MockHttpSession();
        
        // ユーザー情報をセット
        StudentEntity mockUser = new StudentEntity();
        mockUser.setStudentId(mockUserId);
        mockUser.setPoint(mockLessonPrice1 + mockLessonPrice2); // 購入に必要な金額以上のポイントを設定
        mockSession.setAttribute("student", mockUser);
        
        // カートに追加する講座情報をセット
        ArrayList<LessonEntity> mockCartList = new ArrayList<>();
        mockCartList.add(new LessonEntity(mockLessonId1, mockLessonName1, "instructor1", mockLessonPrice1, "img",1L));
        mockCartList.add(new LessonEntity(mockLessonId2, mockLessonName2, "instructor1", mockLessonPrice2, "img",1L));
        mockSession.setAttribute("cart", mockCartList);
        
        // モックの依存オブジェクトを設定//////修正必要///////////////////////////////////////////////////////
        when(studentService.selectByStudentId(mockUserId)).thenReturn(mockUser);
        when(buyingHistoryDao2.findStudentIdAndLessonIdByStudentId(mockUserId)).thenReturn(new ArrayList<>());
        
        // テスト実行
        mockMvc.perform(post("/user/completed")
                .session(mockSession))
                .andExpect(status().is3xxRedirection())
                .andExpect(redirectedUrl("/user/completed"));
        // ポイントが正しく更新されていることを検証
        verify(studentService).update(mockUserId, mockUser.getStudentName(), mockUser.getStudentPassword(), mockUser.getKeyword(), mockUser.getStudentEmail(), mockUser.getPoint() - (mockLessonPrice1 + mockLessonPrice2));
        // カートがリセットされていることを検証
        assertNull(mockSession.getAttribute("cart"));
        // トランザクション履歴とアイテムが正しく保存されていることを検証
        verify(transactionHistoryDao).save(any(TransactionHistoryEntity.class));
        verify(transactionItemDao, times(2)).save(any(TransactionItemEntity.class));
    }
}