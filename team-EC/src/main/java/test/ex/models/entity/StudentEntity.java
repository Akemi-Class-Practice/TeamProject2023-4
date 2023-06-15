package test.ex.models.entity;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.Table;

@Entity
@Table(name = "student")
public class StudentEntity {
	@Id
	@Column(name = "student_id")
	@GeneratedValue(strategy = GenerationType.AUTO)
	private Long studentId;

	@Column(name = "student_name")
	private String studentName;

	@Column(name = "student_email")
	private String studentEmail;

	@Column(name = "student_password")
	private String studentPassword;

	@Column(name = "keyword")
	private String keyword;
	
	@Column(name = "point")
	private int point;

	
	public StudentEntity() {

	}

	


	//ログイン用
	public StudentEntity(String studentEmail, String studentPassword) {
		this.studentEmail = studentEmail;
		this.studentPassword = studentPassword;
	}

	//登録用
	public StudentEntity(String studentName,  String studentPassword, String keyword,String studentEmail) {
		this.studentName = studentName;
		this.studentPassword = studentPassword;
		this.keyword = keyword;
		this.studentEmail = studentEmail;
	}

	//更新用
	public StudentEntity(Long studentId, String studentName, String studentPassword,String keyword,String studentEmail, 
			 int point) {

		this.studentId = studentId;
		this.studentName = studentName;
		this.studentPassword = studentPassword;
		this.keyword = keyword;
		this.studentEmail = studentEmail;
		this.point = point;
	}




	//getter & setter
	public Long getStudentId() {
		return studentId;
	}


	public void setStudentId(Long studentId) {
		this.studentId = studentId;
	}


	public String getStudentName() {
		return studentName;
	}


	public void setStudentName(String studentName) {
		this.studentName = studentName;
	}


	public String getStudentEmail() {
		return studentEmail;
	}


	public void setStudentEmail(String studentEmail) {
		this.studentEmail = studentEmail;
	}


	public String getStudentPassword() {
		return studentPassword;
	}


	public void setStudentPassword(String studentPassword) {
		this.studentPassword = studentPassword;
	}

	public String getKeyword() {
		return keyword;
	}

	public void setKeyword(String keyword) {
		this.keyword = keyword;
	}

	public int getPoint() {
		return point;
	}

	public void setPoint(int point) {
		this.point = point;
	}


}
