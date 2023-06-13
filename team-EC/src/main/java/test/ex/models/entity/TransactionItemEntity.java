package test.ex.models.entity;



import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.RequiredArgsConstructor;

@Data
@AllArgsConstructor
@RequiredArgsConstructor
@Entity
@Table(name="transaction_item")
public class TransactionItemEntity {
	@Id
	@Column(name="id")
	@GeneratedValue(strategy = GenerationType.AUTO)
	private Long id;
	
	@Column(name="transaction_id")
	private Long transactionId;
	
	@Column(name="lesson_id")
	private Long lessonId;

	public TransactionItemEntity(Long transactionId, Long lessonId) {
		this.transactionId = transactionId;
		this.lessonId = lessonId;
	}
	
	
}
