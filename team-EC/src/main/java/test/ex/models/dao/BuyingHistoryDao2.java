package test.ex.models.dao;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
<<<<<<< HEAD
import test.ex.models.entity.BuyingHistoryEntity;
import test.ex.models.entity.BuyingHistoryKey;

public interface BuyingHistoryDao2 extends JpaRepository<BuyingHistoryEntity, BuyingHistoryKey> {
=======

import test.ex.models.entity.BuyingCartCheckEntity;
import test.ex.models.entity.BuyingCartKey;
import test.ex.models.entity.BuyingHistoryEntity;
import test.ex.models.entity.BuyingHistoryKey;

public interface BuyingHistoryDao2 extends JpaRepository<BuyingCartCheckEntity,BuyingCartKey> {
>>>>>>> d0ac03d18c02951988f34cbdb474ff0e360ce185

    @Query(value = "SELECT th.student_id, ti.lesson_id " +
                   "FROM transaction_history AS th " +
                   "JOIN transaction_item AS ti ON th.transaction_id = ti.transaction_id " +
                   "WHERE th.student_id = ?1",
                   nativeQuery = true)
<<<<<<< HEAD
    List<Object[]> findStudentIdAndLessonIdByStudentId(Long studentId);
=======
    List<BuyingCartCheckEntity> findStudentIdAndLessonIdByStudentId(Long studentId);
>>>>>>> d0ac03d18c02951988f34cbdb474ff0e360ce185
}