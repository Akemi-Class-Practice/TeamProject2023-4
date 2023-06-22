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
import test.ex.models.entity.AdminEntity;
import test.ex.models.entity.LessonEntity;

import test.ex.service.LessonService;
import test.ex.service.TransactionItemService;

@Controller
public class AdminLessonEditController {
	@Autowired
	private LessonService lessonService;

	@Autowired
	private TransactionItemService transactionItemService;

	@Autowired
	HttpSession session;

	// エディット画面の表示及び編集を行う記事情報の取得--------------------------------------------------------------

	@GetMapping("/admin/lesson/edit/{lessonId}")
	public String getlessonEditPage(@PathVariable Long lessonId, Model model) {
		AdminEntity userList = (AdminEntity) session.getAttribute("admin");
		// lessonIdから編集を行いたい講座情報を取得（HTML内で使用）
		model.addAttribute("lessonList", lessonService.selectByLessonId(lessonId));
		//ログインしているか判定ログインしていなければログインページへ遷移
		if(userList != null) {
			return "admin-lesson-edit.html";
		}else {
			return "redirect:/admin/login";
		}
	}

	// lesson内容の更新---------------------------------------------------------------------

	@PostMapping("/admin/lesson/edit")
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



		return "admin-lesson-edit-finish.html";


	}
	@Transactional
	@PostMapping("/admin/lesson/delete")
	public String lessonDelete(@RequestParam Long lessonId) {
		
		// 削除処理
		//1.transaction_itemテーブルの該当講座履歴を削除(lessonIdを外部参照しているため先に削除)
		transactionItemService.delete(lessonId);
		
		//2.lessonテーブルの該当講座履歴を削除
		lessonService.delete(lessonId);
		return "admin-lesson-deleted.html";
		
	}

}
