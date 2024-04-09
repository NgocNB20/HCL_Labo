/*
 * Copyright (C) 2023 i-TEC HANKYU HANSHIN INC. All Rights Reserved.
 */

package jp.co.itechh.quad.hclabo.ddd.domain.examination.repository;

import jp.co.itechh.quad.hclabo.ddd.domain.examination.valueobject.ExamResult;

import java.util.List;

/**
 * 検査結果トリポジトリー
 */
public interface IExamResultsRepository {

    /**
     * 検査結果リスト登録
     *
     * @param examResultList 検査結果リスト
     */
    void save(List<ExamResult> examResultList);

    /**
     * 検査キット番号による検査結果削除
     *
     * @param examKitCode 検査キット番号
     * @return 削除の番号
     */
    int deleteByExamKitCode(String examKitCode);

    /**
     * 検査キット番号で検査結果取得<br/>
     *
     * @param examKitCode 検査キット番号
     * @return 検査結果リスト
     */
    List<ExamResult> getByExamKitCode(String examKitCode);

}
