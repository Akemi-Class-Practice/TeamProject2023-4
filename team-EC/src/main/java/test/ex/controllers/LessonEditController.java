package test.ex.controllers;



import java.io.BufferedOutputStream;
import java.io.File;
import java.io.FileOutputStream;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.multipart.MultipartFile;

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
		return "lessonEdit.html";
	}

	// lesson内容の更新---------------------------------------------------------------------

	@PostMapping("/lesson/update")
	public String lessonEdit(@RequestParam String lessonName, @RequestParam String content,
			@RequestParam int fee,@RequestParam("imageName") MultipartFile imageName,@RequestParam Long lessonId) {

		LessonEntity lessonEntity = lessonService.selectByLessonId(lessonId);

		String fileName = imageName.getOriginalFilename();

		try {
			// 保存先の指定
			File lessonFile = new File("./images/" + fileName);
			// バイナリデータの取得
			byte[] bytes = imageName.getBytes();
			// 画像を保存するためのバッファを用意
			BufferedOutputStream out = new BufferedOutputStream(new FileOutputStream(lessonFile));
			// ファイルの書き出し
			out.write(bytes);
			// バッファを閉じることで書き出しを正常終了
			out.close();
		} catch (Exception e) {
			e.printStackTrace();
		}

		// 保存処理
		lessonService.update(lessonId,lessonName,content,fee,fileName,lessonEntity.getAdminId());
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
