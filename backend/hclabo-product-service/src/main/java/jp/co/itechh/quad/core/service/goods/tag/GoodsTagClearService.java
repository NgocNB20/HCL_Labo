/*
 * Project Name : HIT-MALL4
 *
 * Copyright (C) 2022 i-TEC HANKYU HANSHIN INC. All Rights Reserved.
 *
 */

package jp.co.itechh.quad.core.service.goods.tag;

import java.sql.Timestamp;

/**
 * 商品タグクリア
 *
 * @author Pham Quang Dieu
 */
public interface GoodsTagClearService {

    /**
     *
     * 商品タグクリア
     *
     * @param deleteTime クリア基準日時
     * @return 処理件数
     */
    int execute(Timestamp deleteTime);

}
