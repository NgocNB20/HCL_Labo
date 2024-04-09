/*
 * Project Name : HIT-MALL4
 *
 * Copyright (C) 2008 i-TEC HANKYU HANSHIN INC. All Rights Reserved.
 *
 */

package jp.co.itechh.quad.core.logic.shop.novelty;

import java.util.List;

/**
 * ノベルティプレゼント同梱商品エンティティ取得Logic
 *
 * @author PHAM QUANG DIEU (VJP)
 *
 */
public interface NoveltyPresentEnclosureGoodsCodeGetListLogic {

    /**
     * エンティティ取得
     *
     * @param noveltyPresentConditionSeq ノベルティプレゼント条件SEQ
     * @return エンティティ
     */
    List<String> execute(Integer noveltyPresentConditionSeq);

}
