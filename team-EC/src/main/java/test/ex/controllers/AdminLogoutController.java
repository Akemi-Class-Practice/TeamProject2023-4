package test.ex.controllers;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;

import jakarta.servlet.http.HttpSession;

@Controller
public class AdminLogoutController {
	@Autowired
	HttpSession session;

	// ログアウト処理---------------------------------------------------------------------------
	@GetMapping("/admin/logout")
	// セッションの情報を無効
	public String Logout() {
		session.invalidate();
		return "redirect:/admin/login";
	}

}
