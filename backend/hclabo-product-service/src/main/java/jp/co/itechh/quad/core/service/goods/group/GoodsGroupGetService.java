/*
 * Project Name : HIT-MALL3
 *
 * Copyright (C) 2008 i-TEC HANKYU HANSHIN INC. All Rights Reserved.
 *
 */

package jp.co.itechh.quad.core.service.goods.group;

import jp.co.itechh.quad.core.constant.type.HTypeSiteType;
import jp.co.itechh.quad.core.dto.goods.goodsgroup.GoodsGroupDto;

/**
 * 商品グループ取得
 *
 * @author hirata
 * @version $Revision: 1.4 $
 */
public interface GoodsGroupGetService {

    /**
     * 実行メソッド
     *
     * @param goodsGroupCode 商品グループコード
     * @return 商品グループDto
     */
    GoodsGroupDto execute(String goodsGroupCode, Integer shopSeq, HTypeSiteType siteType);

}
