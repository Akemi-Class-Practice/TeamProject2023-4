package test.ex.models.dao;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import test.ex.models.entity.AdminEntity;



@Repository
public interface AdminDao extends JpaRepository<AdminEntity, Long> {
	// データの内容を保存
	AdminEntity save(AdminEntity studentEntity);

	// 一覧取得
	List<AdminEntity> findAll();

	// idを見つける
	AdminEntity findByAdminId(Long AdminId);
	
	//e-mailを取得
	List<AdminEntity> findByAdminEmail(String AdminEmail);

	// Emailとpasswordを使用したAnd検索
	List<AdminEntity> findByAdminEmailAndAdminPassword(String adminEmail, String adminPassword);

}
