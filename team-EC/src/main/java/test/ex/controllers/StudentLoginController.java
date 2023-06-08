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
public class StudentLoginController {

	@Autowired
	private StudentService studentService;

	@Autowired
	HttpSession session;

	// ログイン画面の表示-----------------------------------------------------
	@GetMapping("/student/login")
	public String getLoginPage() {
		return "userLogin.html";
	}

	// ログイン処理-----------------------------------------------------------------------------------

	@PostMapping("/student/login")
	public String login(@RequestParam String email, @RequestParam String password, Model model) {
		// studentServiceクラスのfindByEmailAndPasswordメソッドを使用して、該当するユーザー情報を取得する。
		StudentEntity studentEntity = studentService.selectByEmailAndPassword(email, password);
		if (studentEntity == null) {
			// 入力されたメールアドレスまたはパスワードが存在しなかった場合
			//// errorをtrueにする（HTML側でエラーメッセージを出力するため）
			model.addAttribute("error", true);
			return "userLogin.html";
		} else {
			// 入力されたメールアドレスまたはパスワードが存在した場合
			// studentEntityの内容をsessionに保存する
			session.setAttribute("admin", studentEntity);
			// errorをfalseにする（HTML側でエラーメッセージを出力させないため）
			model.addAttribute("error", false);
			return "redirect:/point";//暫定的にレジスターに飛ばす後で変更
		}

	}

}
