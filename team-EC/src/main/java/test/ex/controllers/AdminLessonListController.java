package test.ex.controllers;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;

import jakarta.servlet.http.HttpSession;

import test.ex.service.LessonService;

@Controller
public class AdminLessonListController {
	@Autowired
	private LessonService lessonService;

	@Autowired
	HttpSession session;

	// トップ画面の表示及びログインユーザー・すべてのブログ記事の情報を取得------------------------------------
	@GetMapping("/admin/lesson/list")
	public String getList(Model model) {
		// 登録されているすべての記事の情報を取得（HTML内で使用）
		model.addAttribute("lessonList", lessonService.selectFindAll());
		return "course-list.html";
	}


}
