/*
 * Project Name : HIT-MALL4
 *
 * Copyright (C) 2021 i-TEC HANKYU HANSHIN INC. All Rights Reserved.
 *
 */

package jp.co.itechh.quad.core.dao.multipayment;

import jp.co.itechh.quad.core.entity.multipayment.MulPayBillEntity;
import jp.co.itechh.quad.ddd.usecase.card.consumer.CreditLineReleaseTargetDto;
import org.seasar.doma.Dao;
import org.seasar.doma.Insert;
import org.seasar.doma.Select;
import org.seasar.doma.boot.ConfigAutowireable;

import java.sql.Timestamp;
import java.util.List;

/**
 * マルチペイメント請求Daoクラス
 *
 * @author thang
 */
@Dao
@ConfigAutowireable
public interface MulPayBillDao {

    /**
     * インサート
     *
     * @param mulPayBillEntity マルチペイメント請求
     * @return 登録件数
     */
    @Insert(excludeNull = true)
    // @Transactional(propagation = Propagation.REQUIRES_NEW)
    int insert(MulPayBillEntity mulPayBillEntity);

    /**
     * 受注SEQからエンティティの取得<br/>
     * 指定された受注の最新のトランザクション履歴を取得<br/>
     *
     * @param orderPaymentId 注文決済ID
     * @return マルチペイメント請求エンティティ
     */
    @Select
    MulPayBillEntity getLatestEntity(String orderPaymentId);

    /**
     * 取引ID、取引パスワードからエンティティの取得
     * 指定された取引の最新のトランザクション履歴を取得
     *
     * @param accessId   取引ID
     * @param accessPass 取引パスワード
     * @return マルチペイメント請求エンティティ
     */
    @Select
    MulPayBillEntity getLatestEntityByAccessInfo(String accessId, String accessPass);

    /**
     * オーダーIDからエンティティの取得
     * 指定のオーダーIDから最新のトランザクション履歴を取得
     *
     * @param orderId オーダーID
     * @return マルチペイメント請求エンティティ
     */
    @Select
    MulPayBillEntity getLatestEntityByOrderId(String orderId);

    /**
     * オーダーIDからエンティティの取得
     * 指定のオーダーIDからエラー情報がない最新のトランザクション履歴を取得
     *
     * @param orderId オーダーID
     * @return マルチペイメント請求エンティティ
     */
    @Select
    MulPayBillEntity getLatestNoErrorEntityByOrderId(String orderId);

    /**
     * オーダーID・取引IDからエンティティの取得
     * 指定のオーダーID・取引IDから最新のトランザクション履歴を取得
     *
     * @param orderId  オーダーID
     * @param accessId 取引ID
     * @return マルチペイメント請求エンティティ
     */
    @Select
    MulPayBillEntity getLatestEntityByOrderIdAndAccessId(String orderId, String accessId);

    /**
     * 与信枠確保未解放受注リスト取得
     *
     * @param thresholdTime 現在時間-登録からの経過時間
     * @param specifiedDay  現在時間-登録からの経過時間
     * @return 受注請求リスト
     */
    @Select
    List<CreditLineReleaseTargetDto> getReserveCreditLineMulPayBillList(Timestamp thresholdTime,
                                                                        Timestamp specifiedDay);

    /**
     * 取引IDからエンティティの取得
     * 指定の取引IDから最新のトランザクション履歴を取得
     *
     * @param accessId 取引ID
     * @return マルチペイメント請求エンティティ
     */
    @Select
    MulPayBillEntity getLatestEntityByAccessId(String accessId);

    /**
     * 与信枠確保未解放受注リスト取得
     *
     * @param orderId  オーダーID
     * @return 受注請求
     */
    @Select
    CreditLineReleaseTargetDto getReserveCreditLineMulPayBillByOrderId(String orderId);
}