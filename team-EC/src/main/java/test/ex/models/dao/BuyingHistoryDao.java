package test.ex.models.dao;

/*****************************************************************************************************************************************************************/
/*                                                                                                                                                               */
/*                                         			　　　　　　　　　　　　購入履歴機能を補助するインターフェース		     				　　　　　　　　　　　　　　　　　	　　　　　　　　　　　　　　*/
/*                                                                                                                       　　　　　　　　　　　　　　　　　　　　　　　　　　　　　　　　　*/
/*                                                                                                                       　　　　　　　　　　　　　　　　　　　　　　　　　　　　　　　　　*/
/*****************************************************************************************************************************************************************/

// インポート -----------------------------------------------------------------------------------------------------------------------------------------------------
import java.util.List;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import test.ex.models.entity.BuyingHistoryEntity;
import test.ex.models.entity.BuyingHistoryKey;

// 用意した複合エンティティと複合キーのクラスを用いてSQL文を実行する ------------------------------------------------------------------------------------------------------------
public interface BuyingHistoryDao extends JpaRepository<BuyingHistoryEntity, BuyingHistoryKey> {

	// 3つのテーブルを結合してデータを抽出する
	@Query(value = "select a.student_id, a.transaction_date, b.lesson_id, c.lesson_name, c.lesson_fee, c.image_name\r\n"
			+ "from transaction_history AS a join transaction_item AS b on a.transaction_id = b.transaction_id join lesson AS c on b.lesson_id = c.lesson_id\r\n"
			+ "where student_id = ?1", nativeQuery = true)
	
	// BuyingHistoryEntityのIDに基づいて、該当するstudentIdを検索する
	List<BuyingHistoryEntity> findByStudentId(Long studentId);
}
