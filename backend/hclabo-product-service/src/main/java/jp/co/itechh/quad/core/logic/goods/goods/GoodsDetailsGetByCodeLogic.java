/*
 * Project Name : HIT-MALL3
 *
 * Copyright (C) 2008 i-TEC HANKYU HANSHIN INC. All Rights Reserved.
 *
 */

package jp.co.itechh.quad.core.logic.goods.goods;

import jp.co.itechh.quad.core.constant.type.HTypeOpenDeleteStatus;
import jp.co.itechh.quad.core.constant.type.HTypeSiteType;
import jp.co.itechh.quad.core.dto.goods.goods.GoodsDetailsDto;

/**
 *
 * 商品詳細情報取得クラス(商品コード)
 * 商品詳細情報取得クラス(商品コード)
 *
 * @author ozaki
 * @version $Revision: 1.1 $
 *
 */
public interface GoodsDetailsGetByCodeLogic {

    // LGG00011

    /**
     *
     * 商品詳細情報取得
     * 商品詳細情報取得
     *
     * @param shopSeq ショップSEQ
     * @param code 商品コード
     * @param siteType サイト区分
     * @param goodsOpenStatus 公開状態
     * @return 商品詳細DTO
     */
    GoodsDetailsDto execute(Integer shopSeq,
                            String code,
                            HTypeSiteType siteType,
                            HTypeOpenDeleteStatus goodsOpenStatus);
}