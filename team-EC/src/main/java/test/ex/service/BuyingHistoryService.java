package test.ex.service;

/****************************************************************************************************************************************/
/*                                                                                                                                      */
/*                                                 講座の購入履歴に対するロジック、アルゴリズム                                                    */
/*                                                                                                                                      */
/*                                                                                                                                      */
/****************************************************************************************************************************************/

// インポート ------------------------------------------------------------------------------------------------------------
import java.util.List;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import test.ex.models.dao.BuyingHistoryDao;
import test.ex.models.entity.BuyingHistoryEntity;

// ブログ履歴処理 --------------------------------------------------------------------------------------------------------
@Service				// カプセル化された状態でモデル内に単独で存在するインターフェースとして提供される操作として最初に定義されたことを示す
public class BuyingHistoryService {

	@Autowired							// 自動でインスタンスの紐づけを行う
	BuyingHistoryDao buyingHistoryDao;	// 3つのテーブルにアクセスして操作するため
	
	/***************************************************************************************/
	/*                         全ての講座から購入した講座のユーザーを取得するメソッド                   */
	/***************************************************************************************/	
	public List<BuyingHistoryEntity> getBuyingHistory(Long studentId){
		return buyingHistoryDao.findByStudentId(studentId);
	}
}
