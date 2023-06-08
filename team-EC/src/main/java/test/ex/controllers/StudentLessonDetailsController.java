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

import test.ex.service.LessonService;

@Controller
public class StudentLessonDetailsController {
	@Autowired
	private LessonService lessonService;

	@Autowired
	HttpSession session;

	// 講座詳細画面の表示--------------------------------------------------------------

	@GetMapping("/lesson/detail/{lessonId}")
	public String getlessondetailPage(@PathVariable Long lessonId, Model model) {

		model.addAttribute("lessonList", lessonService.selectByLessonId(lessonId));
		return "course-details.html";
	}

	// カートに入れる---------------------------------------------------------------------

	@PostMapping("/add/cart")
	public String addcart(@RequestParam Long lessonId, Model model) {
		
		LessonEntity lessonEntity = lessonService.selectByLessonId(lessonId);
		
		//
		@SuppressWarnings("unchecked")
		ArrayList<LessonEntity> cartList = (ArrayList<LessonEntity>) session.getAttribute("cart");
		
		if(cartList == null) {							//一つ目のカート登録
			cartList = new ArrayList<LessonEntity>(); //ArrayListのオブジェクトを生成（入れ物）
			LessonEntity cart = lessonEntity;
			cartList.add(cart);	//Listにdataの追加
		}else {
			LessonEntity cart = lessonEntity;	//二つ目以降
			cartList.add(cart);
		}
		
		session.setAttribute("cart", cartList); //sessionに格納
	
		return "redirect:/student/cart";


	}

}
