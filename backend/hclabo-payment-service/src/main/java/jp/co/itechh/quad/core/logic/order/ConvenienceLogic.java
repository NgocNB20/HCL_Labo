/*
 * Project Name : HIT-MALL3
 *
 * Copyright (C) 2008 i-TEC HANKYU HANSHIN INC. All Rights Reserved.
 *
 */

package jp.co.itechh.quad.core.logic.order;

import jp.co.itechh.quad.core.entity.conveni.ConvenienceEntity;

import java.util.List;

/**
 * コンビニ名称取得ロジック
 *
 * @author natume
 * @version $Revision: 1.1 $
 *
 */
public interface ConvenienceLogic {

    /**
     * コンビニ名称リスト取得処理
     * @return コンビニ名称エンティティリスト
     */
    List<ConvenienceEntity> getConvenienceList();
}
