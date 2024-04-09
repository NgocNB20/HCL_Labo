/*
 * Copyright (C) 2022 i-TEC HANKYU HANSHIN INC. All Rights Reserved.
 */

package jp.co.itechh.quad.hclabo.ddd.infrastructure.examination.repository;

import jp.co.itechh.quad.hclabo.ddd.domain.examination.entity.ExamKitEntity;
import jp.co.itechh.quad.hclabo.ddd.domain.examination.repository.IExamKitRepository;
import jp.co.itechh.quad.hclabo.ddd.domain.examination.valueobject.ExamKitCode;
import jp.co.itechh.quad.hclabo.ddd.domain.examination.valueobject.ExamResult;
import jp.co.itechh.quad.hclabo.ddd.infrastructure.examination.dao.ExamKitDao;
import jp.co.itechh.quad.hclabo.ddd.infrastructure.examination.dao.ExamResultsDao;
import jp.co.itechh.quad.hclabo.ddd.infrastructure.examination.dbentity.ExamKitDbEntity;
import jp.co.itechh.quad.hclabo.ddd.infrastructure.examination.dbentity.ExamResultsDbEntity;
import org.apache.commons.collections.CollectionUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

/**
 * 検査キットリポジトリ実装クラス
 *
 */
@Component
public class ExamKitRepositoryImpl implements IExamKitRepository {

    /** 検査キットDaoクラス */
    private final ExamKitDao examKitDao;

    /** 検査結果Daoクラス */
    private final ExamResultsDao examResultsDao;

    /** 検査キットリポジトリHelperクラス */
    private final ExamKitRepositoryHelper examKitRepositoryHelper;

    /**
     * コンストラクタ
     *
     * @param examKitDao              検査キットDaoクラス
     * @param examResultsDao          検査結果Daoクラス
     * @param examKitRepositoryHelper 検査キットリポジトリHelperクラス
     */
    @Autowired
    public ExamKitRepositoryImpl(ExamKitDao examKitDao, ExamResultsDao examResultsDao,
                                 ExamKitRepositoryHelper examKitRepositoryHelper) {
        this.examKitDao = examKitDao;
        this.examResultsDao = examResultsDao;
        this.examKitRepositoryHelper = examKitRepositoryHelper;
    }

    /**
     * 検査キット番号用連番取得
     *
     * @return 検査キット番号用連番
     */
    @Override
    public Integer getExamKitCodeSeq() {
        return examKitDao.getExamKitCodeSeq();
    }

    /**
     * 検査キット登録(検査キット)
     *
     * @param examKitEntity 検査キットエンティティ
     */
    @Override
    public void save(ExamKitEntity examKitEntity) {

        ExamKitDbEntity examKitDbEntity = examKitRepositoryHelper.toExamKitDbEntity(examKitEntity);
        examKitDao.insert(examKitDbEntity);
    }

    /**
     * 検査キット更新
     *
     * @param examKitEntity 検査キットエンティティ
     * @return 更新件数
     */
    @Override
    public int update(ExamKitEntity examKitEntity) {
        ExamKitDbEntity examKitDbEntity = examKitRepositoryHelper.toExamKitDbEntity(examKitEntity);
        return examKitDao.update(examKitDbEntity);
    }

    /**
     * 検査キット番号で取得<br/>
     *
     * @param examKitCode 検査キット番号
     * @return 検査キット
     */
    @Override
    public ExamKitEntity getByExamKitCode(String examKitCode) {

        ExamKitDbEntity examKitDbEntity = examKitDao.getByExamKitCode(examKitCode);
        if (examKitDbEntity == null) {
            return null;
        }
        // 検査キット番号にひもづく検査結果も取得し、検査結果リストにセットする
        List<ExamResultsDbEntity> examResultsDbEntityList = examResultsDao.getByExamKitCode(examKitCode);
        List<ExamResult> examResultList = examKitRepositoryHelper.toExamResultList(examResultsDbEntityList);

        return new ExamKitEntity(new ExamKitCode(examKitDbEntity.getExamKitCode()),
                examKitDbEntity.getReceptionDate(),
                examKitDbEntity.getSpecimenCode(),
                examKitDbEntity.getExamStatus(),
                examKitDbEntity.getSpecimenComment(),
                examKitDbEntity.getExamResultsPdf(),
                examKitDbEntity.getOrderItemId(),
                examKitDbEntity.getOrderCode(),
                examResultList
        );
    }

    /**
     * 注文商品IDで取得<br/>
     *
     * @param orderItemId 注文商品ID
     * @return 検査キット
     */
    @Override
    public ExamKitEntity getByOrderItemId(String orderItemId) {

        ExamKitDbEntity examKitDbEntity = examKitDao.getByOrderItemId(orderItemId);
        if (examKitDbEntity == null) {
            return null;
        }

        // 検査キット番号にひもづく検査結果も取得し、検査結果リストにセットする
        List<ExamResultsDbEntity> examResultsDbEntityList = examResultsDao.getByExamKitCode(examKitDbEntity.getExamKitCode());
        List<ExamResult> examResultList = examKitRepositoryHelper.toExamResultList(examResultsDbEntityList);

        return new ExamKitEntity(
                new ExamKitCode(examKitDbEntity.getExamKitCode()),
                examKitDbEntity.getReceptionDate(),
                examKitDbEntity.getSpecimenCode(),
                examKitDbEntity.getExamStatus(),
                examKitDbEntity.getSpecimenComment(),
                examKitDbEntity.getExamResultsPdf(),
                examKitDbEntity.getOrderItemId(),
                examKitDbEntity.getOrderCode(),
                examResultList
        );
    }

    /**
     * 注文商品IDリストで取得<br/>
     *
     * @param orderItemIdList 注文商品IDリスト
     * @return 検査キットリスト
     */
    @Override
    public List<ExamKitEntity> getExamKitEntityList(List<String> orderItemIdList) {
        List<ExamKitDbEntity> examKitDbEntityList = examKitDao.getExamKitEntityList(orderItemIdList);

        if (CollectionUtils.isEmpty(examKitDbEntityList)) {
            return null;
        }

        List<String> examKitCodeList = examKitDbEntityList.stream().map(ExamKitDbEntity::getExamKitCode)
                .collect(Collectors.toList());

        List<ExamResultsDbEntity> examResultsDbEntityList = examResultsDao.getExamResultsList(examKitCodeList);
        List<ExamResult> examResultList = examKitRepositoryHelper.toExamResultList(examResultsDbEntityList);

        return examKitDbEntityList.stream().map(examKitDbEntity -> new ExamKitEntity(
                new ExamKitCode(examKitDbEntity.getExamKitCode()),
                examKitDbEntity.getReceptionDate(),
                examKitDbEntity.getSpecimenCode(),
                examKitDbEntity.getExamStatus(),
                examKitDbEntity.getSpecimenComment(),
                examKitDbEntity.getExamResultsPdf(),
                examKitDbEntity.getOrderItemId(),
                examKitDbEntity.getOrderCode(),
                examResultList == null ? null : new ArrayList<>(examResultList.stream().filter(examResults ->
                                examResults.getExamKitCode().getValue().equals(examKitDbEntity.getExamKitCode()))
                                .collect(Collectors.toList())))
        ).collect(Collectors.toList());
    }
}