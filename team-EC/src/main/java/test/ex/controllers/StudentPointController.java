package test.ex.controllers;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;

import jakarta.servlet.http.HttpSession;
import test.ex.models.dao.StudentDao;
import test.ex.models.entity.StudentEntity;


@Controller
public class StudentPointController {
    private final StudentDao studentDao;
    
    @Autowired
	HttpSession session;
    

    @Autowired
    public StudentPointController(StudentDao studentDao){ 
        this.studentDao = studentDao;
    }

	//階層へ遷移用 今現在のポイント取得
    @GetMapping("/pointsuccess")
    public String fix(Model model) {
        StudentEntity userList = (StudentEntity) session.getAttribute("student");
        Long student_id = userList.getStudentId();
        StudentEntity student = studentDao.findByStudentId(student_id);
        model.addAttribute("point", student.getPoint());
        return "userPointChargeFinish.html";
    }

	//階層へ遷移用 今現在のポイント取得
    @GetMapping("/point")
    public String point(Model model) {
        StudentEntity userList = (StudentEntity) session.getAttribute("student");
        Long student_id = userList.getStudentId();
        StudentEntity student = studentDao.findByStudentId(student_id);
        model.addAttribute("point", student.getPoint());
        return "userPoint.html";
    }
    
    
    
    
    //ポイント追加処理
    @PostMapping("/point")
    public String addPoint(@RequestParam("pointAmount") int pointAmount,Model model) {
        
        //ログインしているユーザーを取得
        StudentEntity userList = (StudentEntity) session.getAttribute("student");
		Long student_id = userList.getStudentId();

        // ユーザの情報をデータベースから取得  後ほど修正かも
        StudentEntity student = studentDao.findByStudentId(student_id);


        // 現在のポイントを取得
        int currentPoint = student.getPoint();

        // ポイントを増やす
        int newPoint = currentPoint + pointAmount;
        student.setPoint(newPoint);
     

        // データベースに変更を保存
        studentDao.save(student);

        // 成功した場合
        return "redirect:/pointsuccess";
    }
}