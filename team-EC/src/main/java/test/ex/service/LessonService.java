package test.ex.service;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;


import test.ex.models.dao.LessonDao;

import test.ex.models.entity.LessonEntity;

@Service
public class LessonService {
	@Autowired
	private LessonDao lessonDao;

	// 登録内容を保存する処理----------------------------------------------------------------------
	public void insert(String lessonName, String lessonDetail, int lessonFee,String fileName, Long adminId) {
		lessonDao.save(new LessonEntity(lessonName, lessonDetail, lessonFee, fileName,adminId));
	}

	// 一覧を取得する処理-----------------------------------------------------------------------
	public List<LessonEntity> selectFindAll() {
		return lessonDao.findAll();
	}

	// lessonIdからデータを取得する--------------------------------------------------------------------
	public LessonEntity selectByLessonId(Long lessonId) {
		return lessonDao.findByLessonId(lessonId);
	}

	// 内容をupdate-----------------------------------------------------------------------------------
	public void update(Long lessonId,String lessonName, String lessonDetail,int lessonFee, String fileName, Long adminId) {
		LessonEntity blogEntity = lessonDao.findByLessonId(lessonId);
		if (fileName == null) {
			String newFileName = blogEntity.getImageName();
			lessonDao.save(new LessonEntity(lessonId, lessonName, lessonDetail,lessonFee, newFileName, adminId));
		} else {
			lessonDao.save(new LessonEntity(lessonId, lessonName, lessonDetail,lessonFee,fileName, adminId));
		}

	}

	// 削除処理-------------------------------------------------------------------
	public void delete(Long lessonId) {
		lessonDao.deleteByLessonId(lessonId);
	}

}
