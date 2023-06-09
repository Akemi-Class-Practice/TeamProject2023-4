package test.ex.controllers;



import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;


import jakarta.servlet.http.HttpSession;

import test.ex.models.entity.LessonEntity;

import test.ex.service.LessonService;

@Controller
public class LessonEditController {
	@Autowired
	private LessonService lessonService;

	@Autowired
	HttpSession session;

	// エディット画面の表示及び編集を行う記事情報の取得--------------------------------------------------------------

	@GetMapping("/lesson/edit/{lessonId}")
	public String getlessonEditPage(@PathVariable Long lessonId, Model model) {
		// lessonIdから編集を行いたい講座情報を取得（HTML内で使用）
		model.addAttribute("lessonList", lessonService.selectByLessonId(lessonId));
		return "lessonEditTest.html";
	}

	// lesson内容の更新---------------------------------------------------------------------

	@PostMapping("/lesson/update")
	public String lessonEdit(@RequestParam String lessonName, @RequestParam String content,
			@RequestParam int fee,@RequestParam Long lessonId) {

		LessonEntity lessonEntity = lessonService.selectByLessonId(lessonId);


		// 保存処理
		lessonService.update(lessonId,lessonName,content,fee,lessonEntity.getImageName(),lessonEntity.getAdminId());
		return "redirect:/admin/lesson/list";//暫定後で変える

	}
	@Transactional
	@PostMapping("/lesson/delete")
	public String lessonDelete(@RequestParam Long lessonId) {
		
		// 削除処理
		lessonService.delete(lessonId);
		return "redirect:/admin/lesson/list";//暫定後で削除完了画面に変える
		
	}

}
