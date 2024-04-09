/*
 * Copyright (C) 2023 i-TEC HANKYU HANSHIN INC. All Rights Reserved.
 */

package jp.co.itechh.quad.hclabo.ddd.domain.examination.repository;

import jp.co.itechh.quad.hclabo.ddd.domain.examination.entity.ExamKitEntity;

import java.util.List;

/**
 * 検査キットリポジトリー
 */
public interface IExamKitRepository {

    /**
     * 検査キット番号用連番取得
     *
     * @return 検査キット番号用連番
     */
    Integer getExamKitCodeSeq();

    /**
     * 検査キット登録(検査キット)
     *
     * @param examKitEntity 検査キットエンティティ
     */
    void save(ExamKitEntity examKitEntity);

    /**
     * 検査キット更新
     *
     * @param examKitEntity 検査キットエンティティ
     * @return 更新件数
     */
    int update(ExamKitEntity examKitEntity);

    /**
     * 検査キット番号で取得<br/>
     *
     * @param examKitCode 検査キット番号
     * @return 検査キット
     */
    ExamKitEntity getByExamKitCode(String examKitCode);

    /**
     * 注文商品IDで取得<br/>
     *
     * @param orderItemId 注文商品ID
     * @return 検査キット
     */
    ExamKitEntity getByOrderItemId(String orderItemId);

    /**
     * 注文商品IDリストで取得<br/>
     *
     * @param orderItemIdList 注文商品IDリスト
     * @return 検査キットリスト
     */
    List<ExamKitEntity> getExamKitEntityList(List<String> orderItemIdList);

}
