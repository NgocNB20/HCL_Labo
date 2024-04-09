package jp.co.itechh.quad.core.logic.goods.goods;

import java.sql.Timestamp;

/**
 * 商品タグクリア
 *
 @author PHAM QUANG DIEU (VJP)
 */
public interface GoodsTagClearLogic {

    /**
     *
     * 商品タグクリア
     *
     * @param deleteTime クリア基準日時
     * @return 処理件数
     */
    int execute(Timestamp deleteTime);
}