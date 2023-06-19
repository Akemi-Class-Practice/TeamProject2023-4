package test.ex.controllers;



import java.util.ArrayList;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;


import jakarta.servlet.http.HttpSession;

import test.ex.models.entity.LessonEntity;
import test.ex.models.entity.StudentEntity;
import test.ex.service.LessonService;

@Controller
public class UserLessonDetailsController {
	@Autowired
	private LessonService lessonService;

	@Autowired
	HttpSession session;

	// 講座詳細画面の表示--------------------------------------------------------------

	@GetMapping("/user/lesson/detail/{lessonId}")
	public String getlessondetailPage(@PathVariable Long lessonId, Model model) {

		model.addAttribute("lessonList", lessonService.selectByLessonId(lessonId));
		return "user-lesson-details.html";
	}
	//カート重複処理メソッド
		public boolean isLessonExist(Long lessonId,ArrayList<LessonEntity> list) {
			for(LessonEntity lesson : list ) {
				if(lesson.getLessonId().equals(lessonId)) {
				return true;	
				}
			}
			return false;
			
			
		}
		
	// カートに入れる---------------------------------------------------------------------

	@PostMapping("/user/cart/add")
	public String addcart(@RequestParam Long lessonId, Model model) {
		
		LessonEntity lessonEntity = lessonService.selectByLessonId(lessonId);
		
		//ログインしているユーザーを取得
    	StudentEntity userList = (StudentEntity) session.getAttribute("student");
        
    	if(userList != null) {
			//
			@SuppressWarnings("unchecked")
			ArrayList<LessonEntity> cartList = (ArrayList<LessonEntity>) session.getAttribute("cart");
			
			if(cartList == null) {							//一つ目のカート登録
				cartList = new ArrayList<LessonEntity>(); //ArrayListのオブジェクトを生成（入れ物）
				LessonEntity cart = lessonEntity;
				cartList.add(cart);	//Listにdataの追加
			}else {
				if(isLessonExist(lessonId,cartList)) {
				}else {
					LessonEntity cart = lessonEntity;	//二つ目以降
					cartList.add(cart);
				}
			}
			
			session.setAttribute("cart", cartList); //sessionに格納
		
			return "redirect:/user/cart/list";
    	}else {
    		return "redirect:/user/login";
    	}

	}

}
