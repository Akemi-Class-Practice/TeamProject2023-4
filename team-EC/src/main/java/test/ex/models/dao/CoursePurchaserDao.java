package test.ex.models.dao;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import test.ex.models.entity.CoursePurchaserEntity;
import test.ex.models.entity.CoursePurchaserKeyEntity;

@Repository
public interface CoursePurchaserDao extends JpaRepository<CoursePurchaserEntity, CoursePurchaserKeyEntity> {


	@Query(value = "SELECT student.student_id,student.student_name,student.student_email,transaction_history.transaction_date\r\n"
			+ "FROM student\r\n" + "JOIN transaction_history ON student.student_id = transaction_history.student_id\r\n"
			+ "JOIN transaction_item ON transaction_history.transaction_id = transaction_item.transaction_id\r\n"
			+ "where transaction_item.lesson_id = ?1", nativeQuery = true)
	List<CoursePurchaserEntity> findByLessonId(Long lessonId);
}
