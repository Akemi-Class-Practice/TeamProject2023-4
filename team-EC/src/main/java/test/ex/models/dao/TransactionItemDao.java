package test.ex.models.dao;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;

import jakarta.transaction.Transactional;
import test.ex.models.entity.LessonEntity;
import test.ex.models.entity.LessonEntity;
import test.ex.models.entity.TransactionItemEntity;


public interface TransactionItemDao extends JpaRepository<TransactionItemEntity, Long> {
	TransactionItemEntity save(TransactionItemEntity transactionItemEntity);
	@Transactional
	List<TransactionItemEntity> deleteByTransactionId(Long transactionId);
	//削除処理
		List<TransactionItemEntity> deleteByLessonId(Long lessonId);
}
