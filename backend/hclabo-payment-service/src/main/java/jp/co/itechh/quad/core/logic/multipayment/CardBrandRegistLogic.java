/*
 * Project Name : HIT-MALL3
 *
 * Copyright (C) 2008 i-TEC HANKYU HANSHIN INC. All Rights Reserved.
 *
 */

package jp.co.itechh.quad.core.logic.multipayment;

import jp.co.itechh.quad.core.entity.multipayment.CardBrandEntity;

/**
 * カードブランド情報登録ロジック
 *
 * @author ito
 * @version $Revision:$
 */
public interface CardBrandRegistLogic {

    /**
     * カードブランド情報を登録
     *
     * @param cardBrandEntity カードブランド情報エンティティ
     * @return 登録件数
     */
    int execute(CardBrandEntity cardBrandEntity);
}