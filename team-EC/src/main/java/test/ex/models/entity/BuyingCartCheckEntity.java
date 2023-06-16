package test.ex.models.entity;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;

import jakarta.persistence.Id;
import jakarta.persistence.IdClass;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data									// クラスに対して、Getter,Setter,toString,equals,hashCodeメソッドを自動生成する
@NoArgsConstructor						// 引数無しのデフォルトコンストラクタを自動生成する
@AllArgsConstructor						// 全ての引数を持つコンストラクタを自動生成する
@Entity									// エンティティクラスであることを示す
@IdClass(value=BuyingCartKey.class)	// 複数のフィールドまたはプロパティへマップされた複合プライマリキークラスを指定するアノテーション
public class BuyingCartCheckEntity {
	@Id
	@Column(name = "lesson_id")
	private Long lessonId;
	
	@Id
	@Column(name = "student_id")
	private Long studentId;
	

}
