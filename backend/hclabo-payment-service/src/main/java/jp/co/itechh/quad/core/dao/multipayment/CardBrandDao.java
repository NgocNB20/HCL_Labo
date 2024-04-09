/*
 * Project Name : HIT-MALL4
 *
 * Copyright (C) 2021 i-TEC HANKYU HANSHIN INC. All Rights Reserved.
 *
 */

package jp.co.itechh.quad.core.dao.multipayment;

import jp.co.itechh.quad.core.entity.multipayment.CardBrandEntity;
import org.seasar.doma.Dao;
import org.seasar.doma.Insert;
import org.seasar.doma.Select;
import org.seasar.doma.boot.ConfigAutowireable;

import java.util.List;

/**
 * クレジットカードブランドDaoクラス
 *
 * @author thang
 */
@Dao
@ConfigAutowireable
public interface CardBrandDao {

    /**
     * インサート
     *
     * @param cardBrandEntity カードブランド情報エンティティ
     * @return 処理件数
     */
    @Insert(excludeNull = true)
    int insert(CardBrandEntity cardBrandEntity);

    /**
     * カードブランドリスト取得
     *
     * @param isFront PC/MBサイトの場合はtrueを指定
     * @return カードブランド情報リスト
     */
    @Select
    List<CardBrandEntity> getCardBrandList(boolean isFront);

    /**
     * クレジットカード会社コードからカードブランド情報取得
     *
     * @param cardBrandCode クレジットカード会社コード
     * @return カードブランド情報エンティティ
     */
    @Select
    CardBrandEntity getEntityByCardBrandCode(String cardBrandCode);

    /**
     * 現在のMAXカードブランドSEQを取得
     * @return MAXカードブランドSEQ
     */
    @Select
    int getMaxCardBrandSeq();
}
