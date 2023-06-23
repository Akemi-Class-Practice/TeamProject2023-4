package test.ex.controllers;



import static org.assertj.core.api.Assertions.assertThat;
import static org.hamcrest.CoreMatchers.nullValue;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertNull;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyInt;
import static org.mockito.ArgumentMatchers.anyList;
import static org.mockito.ArgumentMatchers.anyLong;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.doNothing;
import static org.mockito.Mockito.mock;
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

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import org.hamcrest.Matchers;
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
import org.springframework.test.web.servlet.result.MockMvcResultMatchers;

import test.ex.models.dao.BuyingHistoryDao2;
import test.ex.models.dao.TransactionHistoryDao;
import test.ex.models.dao.TransactionItemDao;
import test.ex.models.entity.BuyingCartCheckEntity;
import test.ex.models.entity.LessonEntity;
import test.ex.models.entity.StudentEntity;
import test.ex.models.entity.TransactionHistoryEntity;
import test.ex.models.entity.TransactionItemEntity;
import test.ex.service.StudentService;

//Spring Bootアプリケーションのコンテキストを作成し、テストを実行
@SpringBootTest
//MockMvcを自動的に設定してテストで使用できるようにする
@AutoConfigureMockMvc
public class UserCartConfirmationControllerTest {
	@Autowired
	private MockMvc mockMvc;

	@MockBean
	private TransactionHistoryDao transactionHistoryDao;

	//モックオブジェクトを作成し、テスト対象のクラス内で使用するBeanを置き換え
	@MockBean
	private StudentService studentService;

	@MockBean
	private TransactionItemDao transactionItemDao;

	@MockBean
	private BuyingHistoryDao2 buyingHistoryDao2;

	//テスト中にセッション属性を設定するために使用
	private MockHttpSession session;


	@BeforeEach
	public void setup() {
		// カートに追加するダミーレッスンエンティティを作成
		LessonEntity lesson1 = new LessonEntity();
		lesson1.setLessonFee(100);
		LessonEntity lesson2 = new LessonEntity();
		lesson2.setLessonFee(200);
		//MockHttpSessionオブジェクトのインスタンスを作成し、session変数に割り当て
		session = new MockHttpSession();
		// カートをセッションに設定
		ArrayList<LessonEntity> cartList = new ArrayList<>();
		cartList.add(lesson1);
		cartList.add(lesson2);
		session.setAttribute("cart", cartList);
	}

	@Test
	//GETリクエストを実行して、カートの確認画面が正常に表示されるかどうかを検証
	public void testCartConfirm() throws Exception {
		///user/confirmationに対してGETリクエストを実行
		mockMvc.perform(get("/user/confirmation").session(session))
		//レスポンスのステータスコードが200であることを検証
		.andExpect(status().isOk())
		//レスポンスのビュー名が"user-application-confirm.html"であることを検証
		.andExpect(view().name("user-application-confirm.html"))
		//レスポンスのモデルに"totalPoint"と"cartList"の属性が存在することを検証
		.andExpect(model().attributeExists("totalPoint", "cartList"))
		//レスポンスのモデルの"totalPoint"属性が300であることを検証
		.andExpect(model().attribute("totalPoint", 300))
		//レスポンスのモデルの"cartList"属性がセッションの"cart"属性と同じであることを検証
		.andExpect(model().attribute("cartList", session.getAttribute("cart")));
	}

	@Test
	//GETリクエストを実行して、申し込み完了画面が正常に表示されるかどうかを検証
	public void testCompleted() throws Exception {
		///user/completedに対してGETリクエストを実行
		mockMvc.perform(get("/user/completed"))
		//レスポンスのステータスコードが200であることを検証
		.andExpect(status().isOk())
		//レスポンスのビュー名が"user-application-completed.html"であることを検証
		.andExpect(view().name("user-application-completed.html"));
	}

	@Test
	//POSTリクエストを実行して、カートの申し込みを完了させる場合。
	//ユーザー情報、カート内の講座情報、ポイントなどの設定を行い、申し込みが正常に完了するかどうかを検証
	public void testCompletedcart() throws Exception {
		// セッションにダミーのユーザー情報を設定
		StudentEntity dummyUser = new StudentEntity();
		dummyUser.setStudentId(1L);
		session.setAttribute("student", dummyUser);

		// モックの処理を設定
		StudentEntity mockedStudentEntity = new StudentEntity();
		mockedStudentEntity.setStudentId(1L);
		mockedStudentEntity.setPoint(500);
		//studentService.selectByStudentId()メソッドのモック処理を設定し、指定された引数に基づいたユーザーエンティティを返す
		when(studentService.selectByStudentId(anyLong())).thenReturn(mockedStudentEntity);

		//カートの講座情報を作成し、セッションに設定
		ArrayList<LessonEntity> cartList = new ArrayList<>();
		LessonEntity lesson1 = new LessonEntity();
		lesson1.setLessonId(1L);
		lesson1.setLessonFee(100);
		cartList.add(lesson1);
		session.setAttribute("cart", cartList);

		///user/completedに対してPOSTリクエストを実行
		mockMvc.perform(post("/user/completed").session(session))
		//レスポンスのステータスコードが3xxリダイレクトであることを検証
		.andExpect(status().is3xxRedirection())
		//リダイレクト先のURLが"/user/completed"であることを検証
		.andExpect(redirectedUrl("/user/completed"));

		// 実際の処理が行われたかを検証
		//studentService.selectByStudentId()メソッドが指定の引数で呼び出されたことを検証
		verify(studentService).selectByStudentId(1L);
		//studentService.update()メソッドが指定の引数で呼び出されたことを検証
		verify(studentService).update(eq(1L), any(), any(), any(), any(), eq(400));
		//transactionHistoryDao.save()メソッドが指定の引数で呼び出されたことを検証
		verify(transactionHistoryDao).save(any(TransactionHistoryEntity.class));
		//transactionItemDao.save()メソッドが指定の引数で呼び出されたことを検証
		verify(transactionItemDao).save(any(TransactionItemEntity.class));
	}

	@Test
	//ポイントが不足している場合のテスト
	//ユーザー情報、カート内の講座情報、ポイントなどの設定を行い、ポイント不足エラーが正しく表示されるかどうかを検証
	public void testCompletedcart_InsufficientPoints() throws Exception {
		// セッションにダミーのユーザー情報を設定
		StudentEntity dummyUser = new StudentEntity();
		dummyUser.setStudentId(1L);
		session.setAttribute("student", dummyUser);

		// モックの処理を設定
		StudentEntity mockedStudentEntity = new StudentEntity();
		mockedStudentEntity.setStudentId(1L);
		mockedStudentEntity.setPoint(200); // studentPoint < totalPoint
		//studentService.selectByStudentId()メソッドのモック処理を設定し、指定された引数に基づいたユーザーエンティティを返す
		when(studentService.selectByStudentId(anyLong())).thenReturn(mockedStudentEntity);
        //カートの講座情報を作成し、セッションに設定
		ArrayList<LessonEntity> cartList = new ArrayList<>();
		LessonEntity lesson1 = new LessonEntity();
		lesson1.setLessonId(1L);
		lesson1.setLessonFee(300); // カートの合計金額を設定
		cartList.add(lesson1);
		session.setAttribute("cart", cartList);

		///user/completedに対してPOSTリクエストを実行
		mockMvc.perform(post("/user/completed").session(session))
		//レスポンスのステータスコードが200であることを検証
		.andExpect(status().isOk())
		//レスポンスのビュー名が"user-application-confirm.html"であることを検証
		.andExpect(view().name("user-application-confirm.html"))
		//レスポンスのモデルに"insufficientPoints"属性が存在することを検証
		.andExpect(model().attributeExists("insufficientPoints"))
		//レスポンスのモデルの"insufficientPoints"属性がtrueであることを検証
		.andExpect(model().attribute("insufficientPoints", true));
	}

	@Test
    //ポイントが十分な場合のテスト
	//ユーザー情報、カート内の講座情報、ポイントなどの設定を行い、申し込みが正常に完了するかどうかを検証
	public void testCompletedcart_SufficientPoints() throws Exception {
		// セッションにダミーのユーザー情報を設定
		StudentEntity dummyUser = new StudentEntity();
		dummyUser.setStudentId(1L);
		session.setAttribute("student", dummyUser);

		// モックの処理を設定
		StudentEntity mockedStudentEntity = new StudentEntity();
		mockedStudentEntity.setStudentId(1L);
		mockedStudentEntity.setPoint(500); // studentPoint >= totalPoint
		//studentService.selectByStudentId()メソッドのモック処理を設定し、指定された引数に基づいたユーザーエンティティを返す
		when(studentService.selectByStudentId(anyLong())).thenReturn(mockedStudentEntity);

		//カートの講座情報を作成し、セッションに設定
		ArrayList<LessonEntity> cartList = new ArrayList<>();
		LessonEntity lesson1 = new LessonEntity();
		lesson1.setLessonId(1L);
		lesson1.setLessonFee(200); // カートの合計金額を設定
		cartList.add(lesson1);
		session.setAttribute("cart", cartList);

		///user/completedに対してPOSTリクエストを実行
		mockMvc.perform(post("/user/completed").session(session))
		//レスポンスのステータスコードが3xxリダイレクトであることを検証
		.andExpect(status().is3xxRedirection())
		//リダイレクト先のURLが"/user/completed"であることを検証
		.andExpect(redirectedUrl("/user/completed"));

		// 実際の処理が行われたかを検証
		//各メソッドが指定の引数で呼び出されたことを検証し
		verify(studentService).selectByStudentId(1L);
		verify(studentService).update(eq(1L), any(), any(), any(), any(), eq(300));
		verify(transactionHistoryDao).save(any(TransactionHistoryEntity.class));
		verify(transactionItemDao).save(any(TransactionItemEntity.class));
	}


	@Test
	//既に購入済みの講座が含まれている場合のテスト
	//ユーザー情報、カート内の講座情報、既に購入済みの講座情報などの設定を行い、既に購入済みの講座が正しく表示されるかどうかを検証
	public void testCompletedcart_ExistingPurchasedLesson() throws Exception {
		// セッションにダミーのユーザー情報を設定
		StudentEntity dummyUser = new StudentEntity();
		dummyUser.setStudentId(1L);
		session.setAttribute("student", dummyUser);

		// モックの処理を設定
		StudentEntity mockedStudentEntity = new StudentEntity();
		mockedStudentEntity.setStudentId(1L);
		mockedStudentEntity.setPoint(500);
		//studentService.selectByStudentId()メソッドのモック処理を設定し、指定された引数に基づいたユーザーエンティティを返すようにする。
		//このユーザーエンティティには、studentIdが1L、pointが500に設定
		when(studentService.selectByStudentId(anyLong())).thenReturn(mockedStudentEntity);

		//カートに講座情報を追加
		ArrayList<LessonEntity> cartList = new ArrayList<>();
		LessonEntity lesson1 = new LessonEntity();
		lesson1.setLessonId(1L);
		lesson1.setLessonFee(100);
		cartList.add(lesson1);
		session.setAttribute("cart", cartList);

		// 購入済みの講座を追加
		BuyingCartCheckEntity purchasedLesson = new BuyingCartCheckEntity();
		purchasedLesson.setLessonId(1L);
		List<BuyingCartCheckEntity> studentIdAndLessonIds = new ArrayList<>();
		studentIdAndLessonIds.add(purchasedLesson);

		// モックの処理を設定
		//buyingHistoryDao2.findStudentIdAndLessonIdByStudentId()メソッドのモック処理を設定し、
		//指定された引数に基づいた購入済み講座情報を返す
		when(buyingHistoryDao2.findStudentIdAndLessonIdByStudentId(anyLong())).thenReturn(studentIdAndLessonIds);

		///user/completedに対してPOSTリクエストを実行
		mockMvc.perform(post("/user/completed").session(session))
		//レスポンスのステータスコードが200であることを検証
		.andExpect(status().isOk())
		//レスポンスのビュー名が"user-application-confirm.html"であることを検証
		.andExpect(view().name("user-application-confirm.html"))
		//レスポンスのモデルに"purchasedLessonIds"と"message"属性が存在することを検証
		.andExpect(model().attributeExists("purchasedLessonIds", "message"))
		//レスポンスのモデルの"purchasedLessonIds"属性がtrueであることを検証
		.andExpect(model().attribute("purchasedLessonIds", true))
		//レスポンスのモデルの"message"属性が、カートリストの最初の講座の名前に"は既に購入済みです"を追加した文字列であることを検証
		.andExpect(model().attribute("message", cartList.get(0).getLessonName()+"は既に購入済みです"));

	}
}