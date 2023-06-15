package test.ex.controllers;


import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.LinkedList;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
//import org.springframework.mail.MailException;
//import org.springframework.mail.SimpleMailMessage;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;

import jakarta.servlet.http.HttpSession;
//import lesson.com.model.entity.TransactionHistoryEntity;
import test.ex.models.dao.StudentDao;
import test.ex.models.dao.TransactionHistoryDao;
import test.ex.models.entity.LessonEntity;
import test.ex.models.entity.StudentEntity;
import test.ex.service.LessonService;
import test.ex.service.StudentService;


import test.ex.models.entity.TransactionHistoryEntity;
import test.ex.models.entity.TransactionItemEntity;
import test.ex.models.dao.TransactionItemDao;


@Controller
public class CartConfirmationController {
	
	@Autowired
	private TransactionHistoryDao transactionHistoryDao;
	
	@Autowired
	private StudentService studentService;
	
	@Autowired
	private TransactionItemDao transactionItemDao;
	@Autowired
	HttpSession session;
	
 
	//階層へ遷移用   合計ポイントの表示
    @GetMapping("/student/confirmation")
    public String cartConfim(Model model){
		ArrayList<LessonEntity> cartList = (ArrayList<LessonEntity>) session.getAttribute("cart");		
		

        //合計ポイントを計算
        int totalPoint = 0;
		for(int i = 0;i<cartList.size();i++) {		
			LessonEntity entity = cartList.get(i);
			totalPoint+=entity.getLessonFee();
		}
		//表示用
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
<<<<<<< HEAD
    	StudentEntity userList = (StudentEntity) session.getAttribute("student");
=======
        StudentEntity userList = (StudentEntity) session.getAttribute("student");
>>>>>>> 83060e619750ba9b2fac99f592f4c968d3756525
        
        //ログインしているユーザーidを格納
    	Long loggedInUserId = userList.getStudentId();
    	//持っているポイントを格納
		int studentPoint = userList.getPoint();
		
		//カートの中身を取得
		ArrayList<LessonEntity> cartList = (ArrayList<LessonEntity>) session.getAttribute("cart");
		model.addAttribute("cartList",cartList);
		 //StudentEntity student = (StudentEntity) session.getAttribute("user");
<<<<<<< HEAD


	    
//		// カート内の講座をチェックして重複や購入済みの講座を検証
//		for (LessonEntity lesson : cartList) {
//		    // 同じ講座がカート内に存在する場合
//		    if (isDuplicateLesson(lesson, cartList)) {
//		        model.addAttribute("error", "同じ講座がカートに複数存在します。");
//		        return "userApplication.html";
//		    }
//		    
//		    // 購入済みの講座をカートに追加しようとしている場合
//		    if (isLessonAlreadyPurchased(lesson, loggedInUserId)) {
//		        model.addAttribute("error", "既に購入済みの講座が含まれています。");
//		        return "userApplication.html";
//		    }
//		}
	    
=======
		
>>>>>>> 83060e619750ba9b2fac99f592f4c968d3756525
		//カートの合計ポイントを取得   
        int totalPoint = 0;
		for(int i = 0;i<cartList.size();i++) {		
			LessonEntity entity = cartList.get(i);
			totalPoint+=entity.getLessonFee();
		}
		//studentPointは自分が持っているポイント　　　totalPointはカートの合計金額
	    // studentPointがtotalPointよりも少ない場合、ポップアップを表示
	    if (studentPoint < totalPoint) {
	    	//insufficientPointsのポップアップは後ほど制作
	        model.addAttribute("insufficientPoints", true);
	        return "userApplication.html"; // とりあえず今の画面にとどまる
	    }else {
			//自分が持っているポイントからカートの合計金額を引く
			studentPoint -= totalPoint;
	    }
		//講座購入後のポイントをstudentデータベースに保存
		userList.setPoint(studentPoint);
		studentService.update(userList.getStudentId(), userList.getStudentName(), userList.getStudentPassword(), userList.getKeyword(), userList.getStudentEmail(), studentPoint);
		
		
		/////////////transaction_historyへの保存処理//////////////////////////////	
		//購入日付を取得
		LocalDateTime purchaseDate = LocalDateTime.now(); // 現在の日時を取得

		//TransactionHistoryEntityオブジェクトを作成して値をセット
		TransactionHistoryEntity transactionHistory = new TransactionHistoryEntity(loggedInUserId, purchaseDate);

		//transaction_historyのデータベースに保存
		transactionHistoryDao.save(transactionHistory);

		/////////////////transaction_itemへの保存処理///////////////////////////////  
		//トランザクションアイテムのリストを作成
		List<TransactionItemEntity> transactionItems = new ArrayList<>();

		//カート内の講座に対して繰り返し処理
		for (LessonEntity lesson : cartList) {
			// 新しいトランザクションアイテムを作成し
			TransactionItemEntity transactionItem = new TransactionItemEntity();

			// トランザクションIDをトランザクション履歴のIDに設定
			transactionItem.setTransactionId(transactionHistory.getTransactionId());

			// レッスンIDを現在の講座のIDに設定
			transactionItem.setLessonId(lesson.getLessonId());

			// トランザクションアイテムをリストに追加
			transactionItems.add(transactionItem);
		}
		//リスト内の各トランザクションアイテムをtransaction_itemテーブルに保存
		for (TransactionItemEntity transactionItem : transactionItems) {
			transactionItemDao.save(transactionItem);
		}			
	// 成功した場合
    return "redirect:/student/completed";
    }


}    