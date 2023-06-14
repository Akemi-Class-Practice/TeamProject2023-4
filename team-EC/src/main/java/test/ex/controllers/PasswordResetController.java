package test.ex.controllers;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;

import jakarta.servlet.http.HttpSession;
import test.ex.models.entity.StudentEntity;
import test.ex.service.StudentService;

@Controller
public class PasswordResetController {

	@Autowired
	private StudentService studentService;

	@Autowired
	HttpSession session;

	// リセット画面の表示-----------------------------------------------------
	@GetMapping("/password/reset")
	public String getResetPage(Model model) {
		model.addAttribute("error", false);
		return "userAccountReset.html";
	}
	
	// パスワード変更完了画面の表示-----------------------------------------------------
	@GetMapping("/password/completed")
	public String getpasswordcompleted(Model model) {
		return "userPasswordCompleted.html";
	}

	// パスワードリセット処理-----------------------------------------------------------------------------------

	@PostMapping("/password/reset")
	public String passwordReset(@RequestParam String keyword,@RequestParam String email, @RequestParam String password,@RequestParam String password2, Model model) {
		// studentServiceクラスのfindByEmailAndkeywordメソッドを使用して、該当するユーザー情報を取得する。
		StudentEntity studentEntity = studentService.selectByEmailAndKeyword(email, keyword);
		if (studentEntity == null) {
            model.addAttribute("error", true);
            model.addAttribute("errorMessage", "入力していない内容があります");
			return "userAccountReset.html";
		} else {
	        if (!password.equals(password2)) {
	            // パスワードとパスワード確認が一致しない場合
	            model.addAttribute("error", true);
	            model.addAttribute("errorMessage", "パスワードが一致していません");
	            return "userAccountReset.html";
	        }

	        // 入力されたメールアドレスとパスワードが存在した場合
	        // studentEntityの内容をsessionに保存する
	        session.setAttribute("admin", studentEntity);
	        //studentEntity内のdataと入力値で上書き
	        studentService.update(studentEntity.getStudentId(), studentEntity.getStudentName(), password, keyword, email, studentEntity.getPoint());
	        return "redirect:/password/completed";
	    }

	}

}
