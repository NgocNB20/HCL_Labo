/*
 * Copyright (C) 2024 i-TEC HANKYU HANSHIN INC. All Rights Reserved.
 */

package jp.co.itechh.quad.hclabo.ddd.infrastructure.examination.repository;

import jp.co.itechh.quad.hclabo.ddd.domain.examination.repository.IExamResultsRepository;
import jp.co.itechh.quad.hclabo.ddd.domain.examination.valueobject.ExamResult;
import jp.co.itechh.quad.hclabo.ddd.infrastructure.examination.dao.ExamResultsDao;
import jp.co.itechh.quad.hclabo.ddd.infrastructure.examination.dbentity.ExamResultsDbEntity;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.List;

/**
 * 検査キッリポジトリ実装クラス
 *
 */
@Component
public class ExamResultsRepositoryImpl implements IExamResultsRepository {

    /** 検査結果Daoクラス */
    private final ExamResultsDao examResultsDao;

    /** 検査結果リポジトリHelperクラス */
    private final ExamResultsRepositoryHelper examResultsRepositoryHelper;

    /**
     * コンストラクタ
     *
     * @param examResultsDao
     * @param examResultsRepositoryHelper
     */
    @Autowired
    public ExamResultsRepositoryImpl(ExamResultsDao examResultsDao,
                                     ExamResultsRepositoryHelper examResultsRepositoryHelper) {
        this.examResultsDao = examResultsDao;
        this.examResultsRepositoryHelper = examResultsRepositoryHelper;
    }

    /**
     * 検査結果リスト登録
     *
     * @param examResultList 検査結果リスト
     */
    @Override
    public void save(List<ExamResult> examResultList) {
        List<ExamResultsDbEntity> examResultsDbEntityList = examResultsRepositoryHelper.toExamResultsDbEntityListForRegist(examResultList);

        examResultsDbEntityList.forEach(dbEntity -> {
            examResultsDao.insert(dbEntity);
        });
    }

    /**
     * 試験キッ番号による検査結果削除
     *
     * @param examKitCode 検査キット番号
     * @return 削除の番号
     */
    @Override
    public int deleteByExamKitCode(String examKitCode) {
        return examResultsDao.deleteByExamKitCode(examKitCode);
    }

    /**
     * 検査キット番号で検査結果取得<br/>
     *
     * @param examKitCode 検査キット番号
     * @return 検査結果リスト
     */
    @Override
    public List<ExamResult> getByExamKitCode(String examKitCode) {
       List<ExamResultsDbEntity> examResultsDbEntityList = examResultsDao.getByExamKitCode(examKitCode);

       return examResultsRepositoryHelper.toExamResultList(examResultsDbEntityList);
    }
}