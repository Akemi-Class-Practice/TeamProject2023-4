package test.ex.controllers;



import java.util.ArrayList;


import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;

import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import jakarta.servlet.http.HttpSession;

import test.ex.models.entity.LessonEntity;

import test.ex.service.LessonService;

@Controller
public class CartController {
	@Autowired
	private LessonService lessonService;

	@Autowired
	HttpSession session;

	
	
	
	// カートリスト画面の表示--------------------------------------------------------------

	@GetMapping("/student/cart")
	public String getlessondetailPage(Model model) {

		ArrayList<LessonEntity> cartList = (ArrayList<LessonEntity>) session.getAttribute("cart");
		model.addAttribute("cartList",cartList);
		return "userCartList.html";
	}

	// カート内の講座を削除---------------------------------------------------------------------

	@PostMapping("/delete/cart")
	public String deleteCart(@RequestParam String lessonName) {
		
		//配列内の該当箇所を削除
//		ArrayList<LessonEntity> cartList = (ArrayList<LessonEntity>) session.getAttribute("cart");
//		Iterator<LessonEntity>ite = cartList.iterator();	//ぜんか
//		int idx = 0;
//		while(ite.hasNext()) {		//次の行を見る
//			LessonEntity entity = ite.next();
//			if(entity.getLessonName().equals(lessonName)) {
//				break;
//			}
//			idx++;
//		}
//		cartList.remove(idx);
		
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
		
		return "redirect:/student/cart";


	}

}
