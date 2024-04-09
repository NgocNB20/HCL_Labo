package jp.co.itechh.quad.batch.queue;

import jp.co.itechh.quad.product.presentation.api.param.GoodsStockDisplayUpsertRequest;
import lombok.Data;

/**
 * キューメッセージ
 *
 * @author Doan Thang (VJP)
 */
@Data
public class SyncUpsertGoodsStockDisplayQueueMessage extends BatchQueueMessage {
    /** 商品在庫表示テーブルアップサートリクエスト */
    GoodsStockDisplayUpsertRequest goodsStockDisplayUpsertRequest;
}