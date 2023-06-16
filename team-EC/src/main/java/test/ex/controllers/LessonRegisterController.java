package test.ex.controllers;

import java.io.BufferedOutputStream;
import java.io.File;
import java.io.FileOutputStream;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.multipart.MultipartFile;

import jakarta.servlet.http.HttpSession;
import test.ex.models.entity.AdminEntity;
import test.ex.service.LessonService;

@Controller
public class LessonRegisterController {
	@Autowired
	private LessonService lessonService;

	@Autowired
	HttpSession session;

	//講座登録画面の表示--------------------------------------------
	@GetMapping("/lesson/register")
	public String getLessonRegisterPage() {
		return "adminLessonRegister.html";
	}

	// 講座の投稿---------------------------------------------------------------------------
	
	@PostMapping("/lesson/register")
	public String LessonRegister(@RequestParam String lessonTitle,
							   @RequestParam String content,
							   @RequestParam int fee,
							   @RequestParam("imageName") MultipartFile imageName, Model model) {

		// ログイン中のユーザ情報を取得
		AdminEntity userList = (AdminEntity) session.getAttribute("admin");
		Long admin_id = userList.getAdminId();
		
//		Long admin_id = (long) 1;  //test用後で消して上記コードのコメントアウト解除

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

		// 保存処理
		lessonService.insert(lessonTitle,content,fee,fileName, admin_id);
		return "adminLessonRegisterFinish.html";

	}

}
