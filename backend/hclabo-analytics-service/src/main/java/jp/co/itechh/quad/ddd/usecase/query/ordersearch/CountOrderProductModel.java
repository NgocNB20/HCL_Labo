package jp.co.itechh.quad.ddd.usecase.query.ordersearch;

import lombok.Data;
import org.springframework.data.annotation.Id;

/**
 * カウント受注商品モデル
 */
@Data
public class CountOrderProductModel {

    /**
     * 商品グループ番号
     */
    @Id
    private String goodsGroupCode;

    /**
     * 商品カウント
     */
    private long goodsCount;
}
