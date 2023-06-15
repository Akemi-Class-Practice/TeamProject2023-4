package test.ex.models.entity;

/*************************************************************************************************************************/
/*                                                                                                                       */
/*                                         3つのテーブルのデータの紐づけによる購入履歴機能の作成										 */
/*                                                                                                                       */
/*                                                                                                                       */
/*************************************************************************************************************************/

// インポート ------------------------------------------------------------------------------------------------------------
import java.time.LocalDateTime;
import org.springframework.format.annotation.DateTimeFormat;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import jakarta.persistence.IdClass;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

// 紐づけ処理 ------------------------------------------------------------------------------------------------------------
@Data									// クラスに対して、Getter,Setter,toString,equals,hashCodeメソッドを自動生成する
@NoArgsConstructor						// 引数無しのデフォルトコンストラクタを自動生成する
@AllArgsConstructor						// 全ての引数を持つコンストラクタを自動生成する
@Entity									// エンティティクラスであることを示す
@IdClass(value=BuyingHistoryKey.class)	// 複数のフィールドまたはプロパティへマップされた複合プライマリキークラスを指定するアノテーション
public class BuyingHistoryEntity {

	@Id													// PKであることを示す
	@Column(name = "lesson_id")							// フィールド(lessonId) と DBのカラム(lesson_id)を紐づける
	private Long lessonId;								// ユーザーが購入したブログのID
	
	@Column(name = "student_id")						// フィールド(studentId) と DBのカラム(student_id)を紐づける
	private Long studentId;								// 講座を購入したユーザーのID
	
	@DateTimeFormat(pattern = "yyyy-mm-dd HH:mm:ss")	// 日付情報を指定したフォーマットに変換して表示するための補助クラス
	@Column(name = "transaction_date")					// フィールド(transactionDate) と DBのカラム(transaction_date)を紐づける
	private LocalDateTime transactionDate;				// 講座を購入した日付
	
	@Column(name = "lesson_name")						// フィールド(lessonName) と DBのカラム(lesson_name)を紐づける
	private String lessonName;							// 購入した講座の名前
	
	@Column(name = "lesson_fee")						// フィールド(lessonFee) と DBのカラム(lesson_fee)を紐づける
	private int lessonFee;								// 購入した講座の金額
	
	@Column(name = "image_name")						// フィールド(imageName) と DBのカラム(image_name)を紐づける
	private String imageName;							// 購入した講座の画像
	
}
