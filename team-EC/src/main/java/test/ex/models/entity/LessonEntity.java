package test.ex.models.entity;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.Table;

@Entity
@Table(name = "lesson")
public class LessonEntity {
	@Id
	@Column(name = "lesson_id")
	@GeneratedValue(strategy = GenerationType.AUTO)
	private Long lessonId;

	@Column(name = "lesson_name")
	private String lessonName;

	@Column(name = "lesson_detail")
	private String lessonDetail;

	@Column(name = "lesson_fee")
	private int lessonFee;

	@Column(name = "image_name")
	private String imageName;

	@Column(name = "admin_id")
	private Long adminId;

	public LessonEntity() {

	}

	public LessonEntity(Long lessonId, String lessonName, String lessonDetail, int lessonFee, String imageName,
			Long adminId) {

		this.lessonId = lessonId;
		this.lessonName = lessonName;
		this.lessonDetail = lessonDetail;
		this.lessonFee = lessonFee;
		this.imageName = imageName;
		this.adminId = adminId;
	}

	public LessonEntity(String lessonName, String lessonDetail, int lessonFee, String imageName, Long adminId) {
		super();
		this.lessonName = lessonName;
		this.lessonDetail = lessonDetail;
		this.lessonFee = lessonFee;
		this.imageName = imageName;
		this.adminId = adminId;
	}

	public Long getLessonId() {
		return lessonId;
	}

	public void setLessonId(Long lessonId) {
		this.lessonId = lessonId;
	}

	public String getLessonName() {
		return lessonName;
	}

	public void setLessonName(String lessonName) {
		this.lessonName = lessonName;
	}

	public String getLessonDetail() {
		return lessonDetail;
	}

	public void setLessonDetail(String lessonDetail) {
		this.lessonDetail = lessonDetail;
	}

	public int getLessonFee() {
		return lessonFee;
	}

	public void setLessonFee(int lessonFee) {
		this.lessonFee = lessonFee;
	}

	public String getImageName() {
		return imageName;
	}

	public void setImageName(String imageName) {
		this.imageName = imageName;
	}

	public Long getAdminId() {
		return adminId;
	}

	public void setAdminId(Long adminId) {
		this.adminId = adminId;
	}

}
