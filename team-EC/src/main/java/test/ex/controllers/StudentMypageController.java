package test.ex.controllers;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;

import jakarta.servlet.http.HttpSession;
import test.ex.models.entity.BuyingHistoryEntity;
import test.ex.models.entity.StudentEntity;
import test.ex.service.BuyingHistoryService;

@Controller
public class StudentMypageController {

	@Autowired
	HttpSession session;

	@Autowired
	BuyingHistoryService buyingHistoryService;
	
	@GetMapping("/student/lesson/mypage")
	public String getMypage(Model model) {
		System.out.println("入っているよ！！！");
		
		if(session.getAttribute("student") == null) {
			return "redirect:/user/login";			
		}else {
			
			StudentEntity student = (StudentEntity) session.getAttribute("student");
			Long studentId = student.getStudentId();
			List<BuyingHistoryEntity> listbuy = buyingHistoryService.getBuyingHistory(studentId);
			model.addAttribute("listbuy", listbuy);
			
			return "userMypage.html";
		}
	}
}
