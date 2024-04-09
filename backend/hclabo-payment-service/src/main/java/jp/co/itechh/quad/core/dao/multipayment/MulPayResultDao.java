/*
 * Project Name : HIT-MALL4
 *
 * Copyright (C) 2021 i-TEC HANKYU HANSHIN INC. All Rights Reserved.
 *
 */

package jp.co.itechh.quad.core.dao.multipayment;

import jp.co.itechh.quad.core.entity.multipayment.MulPayResultEntity;
import org.seasar.doma.Dao;
import org.seasar.doma.Insert;
import org.seasar.doma.Select;
import org.seasar.doma.Update;
import org.seasar.doma.boot.ConfigAutowireable;

import java.util.List;

/**
 * マルチペイメント決済結果Daoクラス
 *
 * @author thang
 * @version $Revision: 1.0 $
 */
@Dao
@ConfigAutowireable
public interface MulPayResultDao {

    /**
     * エンティティ取得
     *
     * @param mulPayResultSeq
     * @return MulPayResultEntity
     */
    @Select
    MulPayResultEntity getEntity(Integer mulPayResultSeq);

    /**
     * インサート
     *
     * @param mulPayResultEntity マルチペイメント決済結果
     * @return 登録件数
     */
    @Insert(excludeNull = true)
    int insert(MulPayResultEntity mulPayResultEntity);

    /**
     * アップデート
     *
     * @param mulPayResultEntity マルチペイメント決済結果
     * @return 更新件数
     */
    @Update
    int update(MulPayResultEntity mulPayResultEntity);

    /**
     * 同一データ検索
     *
     * @param orderId                 オーダーID
     * @param status                  ステータス
     * @param ganbTotalTransferAmount バーチャル口座あおぞら 累計入金額
     * @param errCode                 エラーコード
     * @param errInfo                 エラー詳細コード
     * @return 入金処理済みフラグ
     */
    @Select
    String checkSameNotificationRecord(String orderId,
                                       String status,
                                       Integer ganbTotalTransferAmount,
                                       String errCode,
                                       String errInfo);

    /**
     * 決済結果リスト取得
     *
     * @param shopSeq ショップSEQ
     * @return マルチペイメント決済結果リスト
     */
    @Select
    List<MulPayResultEntity> getUnprocessedPaySuccessEntityList(Integer shopSeq);

    /**
     * 決済結果取得
     *
     * @param orderId オーダーID
     * @return マルチペイメント決済結果リスト
     */
    @Select
    MulPayResultEntity confirmDeposited(String orderId);
}