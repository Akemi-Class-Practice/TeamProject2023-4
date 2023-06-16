package test.ex.models.dao;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import test.ex.models.entity.BuyingHistoryEntity;
import test.ex.models.entity.BuyingHistoryKey;

public interface BuyingHistoryDao2 extends JpaRepository<BuyingHistoryEntity, BuyingHistoryKey> {

    @Query(value = "SELECT th.student_id, ti.lesson_id " +
                   "FROM transaction_history AS th " +
                   "JOIN transaction_item AS ti ON th.transaction_id = ti.transaction_id " +
                   "WHERE th.student_id = ?1",
                   nativeQuery = true)
    List<Object[]> findStudentIdAndLessonIdByStudentId(Long studentId);
}