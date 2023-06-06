package test.ex.service;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import test.ex.models.dao.StudentDao;

import test.ex.models.entity.StudentEntity;

@Service
public class StudentService {
	@Autowired
	private StudentDao studentDao;

	// 登録内容を保存する処理--------------------------------------------
	public boolean FindDuplicateEmail(String studentEmail) {
		List<StudentEntity> duplicateEmail =  studentDao.findByStudentEmail(studentEmail);
		if(duplicateEmail.isEmpty()) {
			return true;
		}else {
			return false;
		}
		
	}

	public void insert(String studentName, String studentPassword,String keyword,String studentEmail) {
		studentDao.save(new StudentEntity(studentName, studentPassword,keyword,studentEmail));
	}

	// ログイン処理---------------------------------------------------------------------
	public StudentEntity selectByEmailAndPassword(String studentEmail, String studentPassword) {
		List<StudentEntity> studentList = studentDao.findByStudentEmailAndStudentPassword(studentEmail, studentPassword);
		if (studentList.isEmpty()) {
			return null;
		} else {
			return studentList.get(0);
		}
	}
	
	//emailとキーワードで検索
	public StudentEntity selectByEmailAndKeyword(String studentEmail, String keyword) {
		List<StudentEntity> studentList = studentDao.findByStudentEmailAndKeyword(studentEmail, keyword);
		if (studentList.isEmpty()) {
			return null;
		} else {
			return studentList.get(0);
		}
	}

	// 一覧を取得する処理-----------------------------------------------------------
	public List<StudentEntity> selectFindAll() {
		return studentDao.findAll();
	}

	// accountIdからデータを取得する------------------------------------------------
	public StudentEntity selectByStudentId(Long student_id) {
		return studentDao.findByStudentId(student_id);
	}

	// emailを取得---------------------------------------------------------------
	public List<StudentEntity> selectByEmail(String studentEmail) {
		return studentDao.findByStudentEmail(studentEmail);
	}
	// 内容をupdate-----------------------------------------------------------
	public void update(Long studentId,String studentName, String studentPassword,String keyword,String studentEmail,int point) {
		StudentEntity studentEntity = studentDao.findByStudentId(studentId);
		studentDao.save(new StudentEntity(studentId,studentName, studentPassword, keyword,studentEmail,point));
	}

	// 削除処理--------------------------------------------------------------
	public void delete(Long studentId) {
		studentDao.deleteById(studentId);
	}

}
