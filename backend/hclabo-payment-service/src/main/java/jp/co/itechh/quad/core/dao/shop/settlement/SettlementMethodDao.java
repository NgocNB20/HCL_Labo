/*
 * Project Name : HIT-MALL4
 *
 * Copyright (C) 2021 i-TEC HANKYU HANSHIN INC. All Rights Reserved.
 *
 */

package jp.co.itechh.quad.core.dao.shop.settlement;

import jp.co.itechh.quad.core.constant.type.HTypeOpenStatus;
import jp.co.itechh.quad.core.constant.type.HTypeSettlementMethodType;
import jp.co.itechh.quad.core.dto.shop.settlement.SettlementDetailsDto;
import jp.co.itechh.quad.core.dto.shop.settlement.SettlementSearchForDaoConditionDto;
import jp.co.itechh.quad.core.entity.linkpayment.SettlementMethodLinkEntity;
import jp.co.itechh.quad.core.entity.shop.settlement.SettlementMethodEntity;
import org.seasar.doma.Dao;
import org.seasar.doma.Insert;
import org.seasar.doma.Script;
import org.seasar.doma.Select;
import org.seasar.doma.Update;
import org.seasar.doma.boot.ConfigAutowireable;

import java.math.BigDecimal;
import java.sql.Timestamp;
import java.util.List;

/**
 * 決済方法Daoクラス
 *
 * @author thang
 * @version $Revision: 1.0 $
 */
@Dao
@ConfigAutowireable
public interface SettlementMethodDao {

    /**
     * インサート
     *
     * @param settlementMethodEntity 決済方法エンティティ
     * @return 処理件数
     */
    @Insert(excludeNull = true)
    int insert(SettlementMethodEntity settlementMethodEntity);

    /**
     * アップデート
     *
     * @param settlementMethodEntity 決済方法エンティティ
     * @return 処理件数
     */
    @Update
    int update(SettlementMethodEntity settlementMethodEntity);

    /**
     * エンティティ取得
     *
     * @param settlementMethodSeq 決済方法SEQ
     * @return 決済方法エンティティ
     */
    @Select
    SettlementMethodEntity getEntity(Integer settlementMethodSeq);

    /**
     * 決済方法の詳細情報と金額別の手数料を返却
     * <pre>
     * 金額別の手数料については、該当する上限金額の内、最安値の上限金額の手数料を返却
     * </pre>
     *
     * @param settlementMethodSeq 決済方法SEQ
     * @param judgePrice 金額別手数料判定用の金額
     * @return 決済詳細Dto
     */
    @Select
    SettlementDetailsDto getSettlementDetails(Integer settlementMethodSeq, BigDecimal judgePrice);

    /**
     * 決済方法の一覧と金額別の手数料を返却
     * <pre>
     * 金額別の手数料については、該当する上限金額の内、最安値の上限金額の手数料を返却
     * </pre>
     *
     * @param conditionDto 検索条件
     * @return 決済詳細Dtoリスト
     */
    @Select
    List<SettlementDetailsDto> getSearchSettlementDetailsList(SettlementSearchForDaoConditionDto conditionDto);

    /**
     * エンティティリスト取得
     *
     * @param shopSeq ショップSEQ
     * @return 決済方法エンティティリスト(表示順昇順)
     */
    @Select
    List<SettlementMethodEntity> getSettlementMethodList(Integer shopSeq);

    /**
     * テーブルロック
     */
    @Script
    void updateLockTableShareModeNowait();

    /**
     * 決済方法表示順更新
     *
     * @param settlementMethodSeq 決済方法SEQ
     * @param shopSeq ショップSEQ
     * @param orderDisplay 表示順
     * @param updateTime 更新日時
     * @return 処理件数
     */
    @Update(sqlFile = true)
    int updateOrderDisplay(Integer settlementMethodSeq, Integer shopSeq, Integer orderDisplay, Timestamp updateTime);

    /**
     *
     * 決済方法エンティティ取得
     *
     * @param settlementMethodSeq 決済方法SEQ
     * @param shopSeq ショップSEQ
     * @return 決済方法エンティティ
     */
    @Select
    SettlementMethodEntity getSettlementMethodEntity(Integer settlementMethodSeq, Integer shopSeq);

    /**
     * 表示順の最大値を取得
     *
     * @param shopSeq ショップSEQ
     * @return 表示順の最大値
     */
    @Select
    Integer getMaxOrderDisplay(Integer shopSeq);

    /**
     *
     * 決済方法同名件数取得
     *
     * @param settlementMethodSeq 決済方法SEQ
     * @param shopSeq ショップSEQ
     * @param settlementMethodName 決済方法名
     * @return 同名の件数（指定決済方法SEQを省く）
     */
    @Select
    int getSameNameCount(Integer settlementMethodSeq, Integer shopSeq, String settlementMethodName);

    /**
     * 決済方法区分をキーとしてエンティティ取得
     *
     * @param shopSeq ショップSEQ
     * @param settlementMethodType 決済方法種別
     * @param openStatus 公開状態
     * @return 決済方法エンティティ
     */
    @Select
    SettlementMethodEntity getEntityBySettlementMethodTypeAndOpenStatus(Integer shopSeq,
                                                                        HTypeSettlementMethodType settlementMethodType,
                                                                        HTypeOpenStatus openStatus);

    /**
     * リンク決済一覧取得
     *
     * @return リンク決済個別決済手段
     */
    @Select
    List<SettlementMethodLinkEntity> getPaymentMethodLink();

}
