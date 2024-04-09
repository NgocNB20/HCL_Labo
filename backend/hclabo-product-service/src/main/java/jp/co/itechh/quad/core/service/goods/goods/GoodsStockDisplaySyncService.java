/*
 * Copyright (C) 2023 i-TEC HANKYU HANSHIN INC. All Rights Reserved.
 */

package jp.co.itechh.quad.core.service.goods.goods;

import java.util.List;

/**
 * 商品在庫表示サービス
 *
 * @author Pham Quang Dieu (VTI Japan Co., Ltd.)
 */
public interface GoodsStockDisplaySyncService {

    /**
     * 在庫情報アップサート
     *
     * @param goodsSeqList 商品SEQリスト
     * @return 件数
     */
    int syncUpsertStock(List<Integer> goodsSeqList);

}
