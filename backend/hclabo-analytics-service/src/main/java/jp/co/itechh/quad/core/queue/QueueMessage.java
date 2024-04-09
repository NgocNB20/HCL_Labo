package jp.co.itechh.quad.core.queue;

import jp.co.itechh.quad.ddd.usecase.query.ordersearch.OrderSearchQueryCondition;
import jp.co.itechh.quad.ddd.usecase.query.reports.GoodsSaleQueryCondition;
import jp.co.itechh.quad.ddd.usecase.query.reports.searchordersales.OrderSalesSearchQueryCondition;
import jp.co.itechh.quad.ordersearch.presentation.api.param.OrderSearchCsvGetOptionRequest;
import lombok.Data;

/**
 * キューメッセージ
 *
 * @author Pham Quang Dieu (VTI Japan Co., Ltd.)
 */
@Data
public class QueueMessage {

    /**
     * 受注ID
     */
    private String orderReceivedId;

    /**
     * 受注検索条件
     */
    private OrderSearchQueryCondition orderSearchQueryCondition;

    /**
     * 管理者ID
     */
    private Integer adminId;

    /**
     * Csvダウンロードオプションコンテンツ
     */
    private OrderSearchCsvGetOptionRequest option;

    /**
     * ダウンロードファイルタイプ
     */
    private String type;

    /**
     * 受注・売上集計用検索条件
     */
    private OrderSalesSearchQueryCondition orderSalesSearchQueryCondition;

    /**
     * 商品販売クエリ条件
     */
    private GoodsSaleQueryCondition goodsSaleQueryCondition;

    /**
     * 改訂後取引ID
     */
    private String transactionRevisionId;

    /**
     * 改訂前取引ID
     */
    private String transactionId;

}
