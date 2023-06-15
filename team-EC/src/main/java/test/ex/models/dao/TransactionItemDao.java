package test.ex.models.dao;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;

import jakarta.transaction.Transactional;
<<<<<<< HEAD
import test.ex.models.entity.LessonEntity;
=======
>>>>>>> 83060e619750ba9b2fac99f592f4c968d3756525
import test.ex.models.entity.TransactionItemEntity;


public interface TransactionItemDao extends JpaRepository<TransactionItemEntity, Long> {
	TransactionItemEntity save(TransactionItemEntity transactionItemEntity);
	@Transactional
	List<TransactionItemEntity> deleteByTransactionId(Long transactionId);
<<<<<<< HEAD
	
	//削除処理
		List<TransactionItemEntity> deleteByLessonId(Long lessonId);
=======
>>>>>>> 83060e619750ba9b2fac99f592f4c968d3756525
}
