package test.ex.models.dao;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import jakarta.transaction.Transactional;
import test.ex.models.entity.TransactionHistoryEntity;



public interface TransactionHistoryDao extends JpaRepository<TransactionHistoryEntity, Long> {
	TransactionHistoryEntity save(TransactionHistoryEntity transactionHistoryEntity);
	@Query(value="select * from transaction_history where student_id =?1 order by transaction_id desc limit 1",
			nativeQuery = true)
	TransactionHistoryEntity findByStudentId(Long studentId);
	@Transactional
	List<TransactionHistoryEntity> deleteByTransactionId(Long transactionId);
}
