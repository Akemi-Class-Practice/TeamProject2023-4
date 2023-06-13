package test.ex.service;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import test.ex.models.dao.BuyingHistoryDao;
import test.ex.models.entity.BuyingHistoryEntity;

@Service
public class BuyingHistoryService {

	@Autowired
	BuyingHistoryDao buyingHistoryDao;
	public List<BuyingHistoryEntity> getBuyingHistory(Long studentId){
		return buyingHistoryDao.findByStudentId(studentId);
	}
}
