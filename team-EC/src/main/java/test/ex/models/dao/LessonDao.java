package test.ex.models.dao;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import test.ex.models.entity.LessonEntity;
import test.ex.models.entity.StudentEntity;

@Repository
public interface LessonDao extends JpaRepository<LessonEntity, Long> {
	// データの内容を保存
	LessonEntity save(LessonEntity lessonEntity);

	// 一覧取得
	List<LessonEntity> findAll();

	// idを見つける
	LessonEntity findByLessonId(Long lessonId);

	List<LessonEntity> deleteByLessonId(Long lessonId);
	
}
