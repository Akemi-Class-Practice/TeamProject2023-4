package test.ex.controllers;



import java.util.ArrayList;


import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;

import org.springframework.web.bind.annotation.PostMapping;

import org.springframework.web.bind.annotation.RequestParam;


import jakarta.servlet.http.HttpSession;

import test.ex.models.entity.LessonEntity;
import test.ex.models.entity.StudentEntity;
import test.ex.service.LessonService;

@Controller
public class UserCartController {
	@Autowired
	private LessonService lessonService;

	@Autowired
	HttpSession session;

	
	
	
	// カートリスト画面の表示--------------------------------------------------------------

	@GetMapping("/user/cart/list")
	public String getCartPage(Model model) {
		StudentEntity userList = (StudentEntity) session.getAttribute("student");
        
    	if(userList != null) {
			ArrayList<LessonEntity> cartList = (ArrayList<LessonEntity>) session.getAttribute("cart");
			model.addAttribute("cartList",cartList);
			return "user-cart-list.html";
    	}else {
    		return "redirect:/user/login";
    	} 
	}

	// カート内の講座を削除---------------------------------------------------------------------

	@PostMapping("/user/cart/delete")
	public String deleteCart(@RequestParam String lessonName) {
		
		int idx = -1;	//初期値代入
		ArrayList<LessonEntity> cartList = (ArrayList<LessonEntity>) session.getAttribute("cart");
		LessonEntity foundEntity =null;
		for(int i = 0;i<cartList.size();i++) {		//カートの中身分ループ
			LessonEntity entity = cartList.get(i);	//cartListのi番目をentityに代入
			if(entity.getLessonName().equals(lessonName)) { //消去したい講座のlessonIdと合致した場合
				idx = i;					//-1　→　0
				foundEntity = entity;	//削除対象のエンティティに代入
				break;	//削除対象を発見したためループを抜ける
			}
		}
		if(idx>=0) {			//削除対象見つかった場合すなわちidxがー１ではなかったとき
			cartList.remove(foundEntity);	//削除
		}
		return "redirect:/user/cart/list";


	}

}
