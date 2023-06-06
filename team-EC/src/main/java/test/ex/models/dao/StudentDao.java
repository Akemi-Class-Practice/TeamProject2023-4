package test.ex.models.dao;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import test.ex.models.entity.StudentEntity;

@Repository
public interface StudentDao extends JpaRepository<StudentEntity, Long> {
	// データの内容を保存
	StudentEntity save(StudentEntity studentEntity);

	// 一覧取得
	List<StudentEntity> findAll();

	// idを見つける
	StudentEntity findByStudentId(Long studentId);
	
	//e-mailを取得
	List<StudentEntity> findByStudentEmail(String studentEmail);

	// Emailとpasswordを使用したAnd検索
	List<StudentEntity> findByStudentEmailAndStudentPassword(String studentEmail, String studentPassword);
	
	// Emailとpasswordを使用したAnd検索
	List<StudentEntity> findByStudentEmailAndKeyword(String studentEmail, String keyword);

}
