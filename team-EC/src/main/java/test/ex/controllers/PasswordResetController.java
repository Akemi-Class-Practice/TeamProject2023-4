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
	public String getResetPage() {
		return "userAccountReset.html";
	}

	// パスワードリセット処理-----------------------------------------------------------------------------------

	@PostMapping("/password/reset")
	public String passwordReset(@RequestParam String email, @RequestParam String keyword,@RequestParam String password, Model model) {
		// studentServiceクラスのfindByEmailAndkeywordメソッドを使用して、該当するユーザー情報を取得する。
		StudentEntity studentEntity = studentService.selectByEmailAndKeyword(email, keyword);
		if (studentEntity == null) {
			// 入力されたメールアドレスが存在しないまたはキーワードが間違っていた場合
			//// errorをtrueにする（HTML側でエラーメッセージを出力するため）
			model.addAttribute("error", true);
			return "userAccountReset.html";
		} else {
			// 入力されたメールアドレスとパスワードが存在した場合
			// studentEntityの内容をsessionに保存する
			session.setAttribute("admin", studentEntity);
			// errorをfalseにする（HTML側でエラーメッセージを出力させないため）
			model.addAttribute("error", false);
			//studentEntity内のdataと入力値で上書き
			studentService.update(studentEntity.getStudentId(),studentEntity.getStudentName(),password,keyword,email,studentEntity.getPoint());
			return "redirect:/student/login";
		}

	}

}
