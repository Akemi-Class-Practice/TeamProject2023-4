package test.ex.models.entity;

import java.time.LocalDate;

import org.springframework.format.annotation.DateTimeFormat;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import jakarta.persistence.IdClass;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Entity
@IdClass(value=CoursePurchaserKeyEntity.class)
public class CoursePurchaserEntity {


	@Id
	@Column(name="student_id")
	private Long studentId;
	
	@Column(name = "student_name")
	private String studentName;

	@Column(name = "student_email")
	private String studentEmail;
	
	@DateTimeFormat(pattern = "yyyy-MM-dd")
	@Column(name="transaction_date")
	private LocalDate transactionDate;
}
