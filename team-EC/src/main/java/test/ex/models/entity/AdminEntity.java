package test.ex.models.entity;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.Table;

@Entity
@Table(name = "admin")
public class AdminEntity {
	@Id
	@Column(name = "admin_id")
	@GeneratedValue(strategy = GenerationType.AUTO)
	private Long adminId;

	@Column(name = "admin_name")
	private String adminName;
	
	@Column(name = "admin_password")
	private String adminPassword;

	@Column(name = "admin_email")
	private String adminEmail;

	
	


	
	public AdminEntity() {

	}

	//登録用
	public AdminEntity(String adminName,String adminPassword,String adminEmail) {
		this.adminName = adminName;
		this.adminPassword = adminPassword;
		this.adminEmail = adminEmail;
	}

	//ログイン用
	public AdminEntity(String adminEmail, String adminPassword) {
		this.adminEmail = adminEmail;
		this.adminPassword = adminPassword;
	}

	
	//getter & setter
	public Long getAdminId() {
		return adminId;
	}

	public void setAdminId(Long adminId) {
		this.adminId = adminId;
	}

	public String getAdminName() {
		return adminName;
	}

	public void setAdminName(String adminName) {
		this.adminName = adminName;
	}

	public String getAdminEmail() {
		return adminEmail;
	}

	public void setAdminEmail(String adminEmail) {
		this.adminEmail = adminEmail;
	}

	public String getAdminPassword() {
		return adminPassword;
	}

	public void setAdminPassword(String adminPassword) {
		this.adminPassword = adminPassword;
	}

	

	
}
