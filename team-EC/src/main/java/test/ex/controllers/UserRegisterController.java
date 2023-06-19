package test.ex.controllers;



import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;


import test.ex.service.StudentService;

@Controller
public class UserRegisterController {
	@Autowired
	private StudentService studentService;

	// 新規登録画面の表示------------------------------------------
	@GetMapping("/user/register")
	public String getRegisterPage() {
		return "user-register.html";
	}
	
	// 新規登録完了画面の表示------------------------------------------
	@GetMapping("/user/register/finish")
	public String getRegisterFinishPage() {
		return "user-register-finish.html";
	}
	
	
	

	// 保存処理------------------------------------------------------

	@PostMapping("/user/register")
	public String studentRegister(@RequestParam String userName,@RequestParam String password,@RequestParam String keypassword,@RequestParam String email,Model model) {
		//重複したE-mailがないか探す
		boolean studentEntity = studentService.FindDuplicateEmail(email);
		if (studentEntity == true) {
			//ない場合DBに登録
			//エラー表示フラグをfalse
			model.addAttribute("error", false);

			studentService.insert(userName, password,keypassword,email);
			return "redirect:/user/register/finish";
		}else {
			//あった場合エラーメッセージを出力
			model.addAttribute("error", true);
			return "user-register.html";
		}

	}
}
