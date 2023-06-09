package test.ex.controllers;



import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;

import test.ex.service.AdminService;


@Controller
public class AdminRegisterController {
	@Autowired
	private AdminService adminService;

	// 新規登録画面の表示------------------------------------------
	@GetMapping("/admin/register")
	public String getRegisterPage() {
		return "admin-register.html";
	}
	// 新規登録画面完了の表示------------------------------------------
	@GetMapping("/admin/register/finish")
	public String getRegisterFinishPage() {
		return "admin-register-finish.html";
	}

	// 保存処理------------------------------------------------------
	@PostMapping("/admin/register")
	public String studentRegister(@RequestParam String username,@RequestParam String password,@RequestParam String email,Model model) {
		//重複したE-mailがないか探す
		boolean adminEntity = adminService.FindDuplicateEmail(email);
		if (adminEntity == true) {
			//ない場合DBに登録
			//エラー表示フラグをfalse
			model.addAttribute("error", false);
			adminService.insert(username, password,email);
			return "redirect:/admin/register/finish";
		}else {
			//あった場合エラーメッセージを出力
			model.addAttribute("error", true);
			return "admin-register.html";
		}
	}
}
