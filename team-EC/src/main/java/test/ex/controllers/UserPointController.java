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
public class UserPointController {
    private final StudentDao studentDao;
    
    @Autowired
	HttpSession session;
    

    @Autowired
    public UserPointController(StudentDao studentDao){ 
        this.studentDao = studentDao;
    }

	//階層へ遷移用 
    @GetMapping("/user/pointsuccess")
    public String fix(Model model) {
    	//セッションからユーザーの情報を取得
        StudentEntity userList = (StudentEntity) session.getAttribute("student");
        //IDをつかってログインしているユーザーの情報を取得
        Long student_id = userList.getStudentId();
        StudentEntity student = studentDao.findByStudentId(student_id);
        //モデルに持っているポイントの情報を追加
        model.addAttribute("point", student.getPoint());
        //ユーザーのポイントチャージ画面に遷移
        return "user-point-charge.html";
    }
    
    //階層へ遷移用 今現在のポイント取得
    @GetMapping("/user/point")
    public String point(Model model) {
    	//セッションからユーザーの情報を取得
        StudentEntity userList = (StudentEntity) session.getAttribute("student");
        //IDをつかってログインしているユーザーの情報を取得
        Long student_id = userList.getStudentId();
        StudentEntity student = studentDao.findByStudentId(student_id);
        //モデルに持っているポイントの情報を追加
        model.addAttribute("point", student.getPoint());
        //ユーザーのポイントチャージ画面に遷移
        return "user-point-charge.html";
    }

    //ポイント追加処理
    @PostMapping("/user/point/charge")
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
        return "redirect:/user/pointsuccess";
    }
}