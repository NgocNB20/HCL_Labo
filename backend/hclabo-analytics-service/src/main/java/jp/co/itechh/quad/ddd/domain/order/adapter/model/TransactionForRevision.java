package jp.co.itechh.quad.ddd.domain.order.adapter.model;

import lombok.Data;

import java.util.Date;

/**
 * 改訂用取引
 */
@Data
public class TransactionForRevision {

    /**
     * 出荷済みフラグ
     */
    private Boolean shippedFlag;

    /**
     * 処理日時
     */
    private Date processTime;
}