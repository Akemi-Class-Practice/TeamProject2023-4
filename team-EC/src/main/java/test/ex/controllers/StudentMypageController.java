package test.ex.controllers;

/********************************************************************************************************************************/
/*                                                                                                                              */
/*                                         画面遷移を制御したり、Service 層(メイン処理)の呼出を行う                                       */
/*                                                                                                                              */
/*                                                                                                                              */
/********************************************************************************************************************************/

// インポート ------------------------------------------------------------------------------------------------------------
import java.util.List;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import jakarta.servlet.http.HttpSession;
import test.ex.models.entity.BuyingHistoryEntity;
import test.ex.models.entity.StudentEntity;
import test.ex.service.BuyingHistoryService;

//ユーザーからの入力を View から受け取り、それをもとに Model に指示を伝える処理 -------------------------------------------------------
@Controller														// 画面遷移用のコントローラーに付与する
public class StudentMypageController {

	@Autowired													// 自動でインスタンスの紐づけを行う
	HttpSession session;										/* 同一のWebブラウザからの複数回のリクエストを、
																	同一のWebブラウザからのアクセスとして処理するため*/

	@Autowired													// 自動でインスタンスの紐づけを行う
	BuyingHistoryService buyingHistoryService;					// 購入履歴に必要な処理を実行するため呼び出す
	
	// マイページ画面の表示 -----------------------------------------------------------------------------------------------
	@GetMapping("/student/lesson/mypage")						// HTTP GETリクエストに対する紐づけ
	public String getMypage(Model model) {

		// セッションから現在のユーザー情報を取得し、nullだった場合はログイン画面に遷移させる
		if(session.getAttribute("student") == null) {
			return "redirect:/user/login";			
		}else {
			
			// studentにセッションから受け取った情報を格納している
			StudentEntity student = (StudentEntity) session.getAttribute("student");
			
			// studentIdに取得したstudentIdを格納している
			Long studentId = student.getStudentId();
			
			// リストにユーザーが購入した講座の情報を格納する
			List<BuyingHistoryEntity> listbuy = buyingHistoryService.getBuyingHistory(studentId);

			// コントローラーからビューに渡すためのデータを格納している
			model.addAttribute("student", student);
			model.addAttribute("listbuy", listbuy);
			return "userMypage.html";
		}
	}
}
