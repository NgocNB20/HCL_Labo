package jp.co.itechh.quad.ddd.domain.payment.adapter.model;

import lombok.Data;

/**
 * マルチペイメント
 */
@Data
public class MulpayBill {

    /**
     * 仕向先コード
     */
    private String forward;

    /**
     * 支払先コンビニコード
     */
    private String convenience;

}

