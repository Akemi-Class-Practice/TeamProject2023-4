package test.ex.service;



// インポート ------------------------------------------------------------------------------------------------------------

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import test.ex.models.dao.TransactionItemDao;


@Service				
public class TransactionItemService {

	@Autowired							
	TransactionItemDao transactionItemDao;

	// 削除処理-------------------------------------------------------------------
	public void delete(Long lessonId) {
		transactionItemDao.deleteByLessonId(lessonId);
	}
}
