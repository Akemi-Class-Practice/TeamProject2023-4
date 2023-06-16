package test.ex.models.dao;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
<<<<<<< Updated upstream
=======

import test.ex.models.entity.BuyingHistoryEntity;
import test.ex.models.entity.BuyingHistoryKey;

>>>>>>> Stashed changes

import test.ex.models.entity.BuyingCartCheckEntity;
import test.ex.models.entity.BuyingCartKey;
import test.ex.models.entity.BuyingHistoryEntity;
import test.ex.models.entity.BuyingHistoryKey;

public interface BuyingHistoryDao2 extends JpaRepository<BuyingCartCheckEntity,BuyingCartKey> {
<<<<<<< Updated upstream
=======

>>>>>>> Stashed changes

    @Query(value = "SELECT th.student_id, ti.lesson_id " +
                   "FROM transaction_history AS th " +
                   "JOIN transaction_item AS ti ON th.transaction_id = ti.transaction_id " +
                   "WHERE th.student_id = ?1",
                   nativeQuery = true)
<<<<<<< Updated upstream
    List<BuyingCartCheckEntity> findStudentIdAndLessonIdByStudentId(Long studentId);
=======


    List<BuyingCartCheckEntity> findStudentIdAndLessonIdByStudentId(Long studentId);

>>>>>>> Stashed changes
}