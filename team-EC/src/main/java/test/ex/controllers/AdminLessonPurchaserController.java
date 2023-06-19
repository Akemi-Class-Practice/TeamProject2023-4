package test.ex.controllers;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;

import jakarta.servlet.http.HttpSession;
import test.ex.models.dao.AdminDao;
import test.ex.models.dao.CoursePurchaserDao;
import test.ex.service.LessonService;

@Controller
public class AdminLessonPurchaserController {

	
	@Autowired
	private CoursePurchaserDao coursePurchaserDao;

	@Autowired
	HttpSession session;


	@GetMapping("/admin/lesson/purchaser/list/{lessonId}")
	public String getPurchaserList(@PathVariable Long lessonId,Model model) {
		//該当講座の購入者一覧を取得
		model.addAttribute("purchaserList",coursePurchaserDao.findByLessonId(lessonId));
		
		return "admin-course-management.html";
	}


}
