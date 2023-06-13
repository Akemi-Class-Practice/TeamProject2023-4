package test.ex.models.entity;


import java.time.LocalDateTime;

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
@IdClass(value=BuyingHistoryKey.class)
public class BuyingHistoryEntity {

	@Id
	@Column(name = "lesson_id")
	private Long lessonId;
	
	@Column(name = "student_id")
	private Long studentId;
	
	@DateTimeFormat(pattern = "yyyy-mm-dd HH:mm:ss")
	@Column(name = "transaction_date")
	private LocalDateTime transactionDate;
	
	@Column(name = "lesson_name")
	private String lessonName;
	
	@Column(name = "lesson_fee")
	private int lessonFee;
	
	@Column(name = "image_name")
	private String imageName;
	
}
