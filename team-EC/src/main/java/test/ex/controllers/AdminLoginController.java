package test.ex.controllers;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;

import jakarta.servlet.http.HttpSession;
import test.ex.models.entity.AdminEntity;

import test.ex.service.AdminService;

@Controller
public class AdminLoginController {

	@Autowired
	private AdminService adminService;

	@Autowired
	HttpSession session;

	// ログイン画面の表示-----------------------------------------------------
	@GetMapping("/admin/login")
	public String getLoginPage() {
		return "adminLogin.html";
	}

	// ログイン処理-----------------------------------------------------------------------------------

	@PostMapping("/admin/login/process")
	public String login(@RequestParam String email, @RequestParam String password, Model model) {
		// adminServiceクラスのfindByEmailAndPasswordメソッドを使用して、該当するユーザー情報を取得する。
		AdminEntity adminEntity = adminService.selectByEmailAndPassword(email, password);
		if (adminEntity == null) {
			// 入力されたメールアドレスまたはパスワードが存在しなかった場合
			//// errorをtrueにする（HTML側でエラーメッセージを出力するため）
			model.addAttribute("error", true);
			return "adminLogin.html";
		} else {
			// 入力されたメールアドレスまたはパスワードが存在した場合
			// adminEntityの内容をsessionに保存する
			session.setAttribute("admin", adminEntity);
			// errorをfalseにする（HTML側でエラーメッセージを出力させないため）
			model.addAttribute("error", false);
			return "redirect:/admin/lesson/list";//暫定的にレジスターに飛ばす後で変更
		}

	}

}
