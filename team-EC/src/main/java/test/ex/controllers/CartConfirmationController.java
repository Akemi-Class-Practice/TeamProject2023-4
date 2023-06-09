package test.ex.controllers;


import java.util.ArrayList;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;

import jakarta.servlet.http.HttpSession;
import test.ex.models.dao.StudentDao;
import test.ex.models.entity.LessonEntity;
import test.ex.models.entity.StudentEntity;
import test.ex.service.LessonService;
import test.ex.service.StudentService;


@Controller
public class CartConfirmationController {

	@Autowired
	private StudentService studentService;
	@Autowired
	HttpSession session;
	
	
	
	
	
	
	
	 
	//階層へ遷移用   合計ポイントの表示
    @GetMapping("/student/confirmation")
    public String cartConfim(Model model){
		ArrayList<LessonEntity> cartList = (ArrayList<LessonEntity>) session.getAttribute("cart");		
		
        //ログインしているユーザーを取得
        StudentEntity userList = (StudentEntity) session.getAttribute("admin");
        Long student_id = userList.getStudentId();

        //合計ポイントを計算
        int totalPoint = 0;
		for(int i = 0;i<cartList.size();i++) {		
			LessonEntity entity = cartList.get(i);
			totalPoint+=entity.getLessonFee();
		}
	    model.addAttribute("totalPoint", totalPoint);
        model.addAttribute("cartList", cartList);
        
        return "userApplication.html";
    }
	//階層へ遷移用
    @GetMapping("/student/completed")
    public String completed(){
        return "userApplicationCompleted.html";
    }
    
    
    //講座購入処理
    @PostMapping("/student/completed")
    public String completedcart(Model model) {
        
        //ログインしているユーザーを取得
        StudentEntity userList = (StudentEntity) session.getAttribute("admin");
		int studentPoint = userList.getPoint();
		
		//カートの中身を取得
		ArrayList<LessonEntity> cartList = (ArrayList<LessonEntity>) session.getAttribute("cart");
		model.addAttribute("cartList",cartList);
		  StudentEntity student = (StudentEntity) session.getAttribute("admin");
		
        int totalPoint = 0;
		for(int i = 0;i<cartList.size();i++) {		
			LessonEntity entity = cartList.get(i);
			totalPoint+=entity.getLessonFee();
		}
		
	 studentPoint -= totalPoint;
	 
     // StudentEntityオブジェクトに新しいポイントを設定
     student.setPoint(studentPoint);

     // StudentEntityオブジェクトを更新してデータベースに保存
     //studentService.save(student);
	 
     //studentservice に新しくsaveを追加する　　パラメータはpointのみ
		
		
		
		
		
		
		
		
		// 成功した場合
        return "redirect:/student/completed";
    }
    
    
    
    
    
    
    
    
    
    
    
    
    
    
    
    
    
    
    
    
    
    
    
    
    
    

}
