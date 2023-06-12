package test.ex.models.entity;

import java.time.LocalDateTime;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.NonNull;
import lombok.RequiredArgsConstructor;

@Data
@AllArgsConstructor
@RequiredArgsConstructor
@Entity
@Table(name="transaction_history")
public class TransactionHistoryEntity {


	@Id
	@Column(name="transaction_id")
	@GeneratedValue(strategy = GenerationType.AUTO)
	private Long transactionId;
	
	@Column(name="student_id")
	private Long studentId;
	

	
	@Column(name="transaction_date")
	private LocalDateTime transactionDate;

	public TransactionHistoryEntity(Long studentId,LocalDateTime transactionDate) {
		this.studentId = studentId;

		this.transactionDate = transactionDate;
	}
	
}
