package test.ex.controllers;

import java.io.BufferedOutputStream;
import java.io.File;
import java.io.FileOutputStream;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
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
public class LessonImageEditController {
	@Autowired
	private LessonService lessonService;

	@Autowired
	HttpSession session;

	//講座登録画面の表示--------------------------------------------
	@GetMapping("/lesson/image/edit/{lessonId}")
	public String getImageEditPage(@PathVariable Long lessonId, Model model) {
		LessonEntity lessonEntity = lessonService.selectByLessonId(lessonId);
		model.addAttribute("lessonList",lessonEntity);
		return "LessonEditImage.html";
	}

	// 講座の投稿---------------------------------------------------------------------------
	
	@PostMapping("/lesson/image/edit")
	public String ImageEdit(@RequestParam Long lessonId,@RequestParam("imageName") MultipartFile imageName, Model model) {

		LessonEntity lessonEntity = lessonService.selectByLessonId(lessonId);
		model.addAttribute("lessonList",lessonEntity);
		// 画像ファイル名を取得
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

		// 更新処理
		lessonService.update(lessonEntity.getLessonId(),lessonEntity.getLessonName(),lessonEntity.getLessonDetail(),lessonEntity.getLessonFee(),fileName, lessonEntity.getAdminId());
		return "redirect:/admin/lesson/list";//後で変更完了画面に変更する

	}

}
