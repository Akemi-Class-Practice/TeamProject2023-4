package test.ex.service;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import test.ex.models.dao.AdminDao;
import test.ex.models.entity.AdminEntity;

@Service
public class AdminService {
	@Autowired
	private AdminDao adminDao;

	// 登録内容を保存する処理--------------------------------------------
	public boolean FindDuplicateEmail(String adminEmail) {
		List<AdminEntity> duplicateEmail =  adminDao.findByAdminEmail(adminEmail);
		if(duplicateEmail.isEmpty()) {
			return true;
		}else {
			return false;
		}
		
	}

	public void insert(String adminName, String adminPassword,String adminEmail) {
		adminDao.save(new AdminEntity(adminName, adminPassword,adminEmail));
	}

	// ログイン処理---------------------------------------------------------------------
	public AdminEntity selectByEmailAndPassword(String adminEmail, String adminPassword) {
		List<AdminEntity> adminList = adminDao.findByAdminEmailAndAdminPassword(adminEmail, adminPassword);
		if (adminList.isEmpty()) {
			return null;
		} else {
			return adminList.get(0);
		}
	}

	// 一覧を取得する処理-----------------------------------------------------------
	public List<AdminEntity> selectFindAll() {
		return adminDao.findAll();
	}

	// accountIdからデータを取得する------------------------------------------------
	public AdminEntity selectByAdminId(Long adminId) {
		return adminDao.findByAdminId(adminId);
	}

	// emailを取得---------------------------------------------------------------
	public List<AdminEntity> selectByEmail(String adminEmail) {
		return adminDao.findByAdminEmail(adminEmail);
	}

//	// 内容をupdate-----------------------------------------------------------
//	public void update(String ) {
//		adminDao.save(new AdminEntity());
//	}

	// 削除処理--------------------------------------------------------------
	public void delete(Long adminId) {
		adminDao.deleteById(adminId);
	}

}
