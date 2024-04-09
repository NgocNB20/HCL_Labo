/*
 * Copyright (C) 2022 i-TEC HANKYU HANSHIN INC. All Rights Reserved.
 *
 */

package jp.co.itechh.quad.front.pc.web.front.order;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;
import java.util.List;

/**
 * Google Analytics
 * eコマース用データ送信
 *
 * @author
 */
@Data
@NoArgsConstructor
@AllArgsConstructor
public class GoogleAnalyticsInfo {

    /**
     * 注文コード<br/>
     * 注文完了画面に表示
     */
    private String orderCode;

    /** ショップ名PC */
    private String shopNamePC;

    /** 支払い合計金額 */
    private BigDecimal billingAmount;

    /** 消費税 */
    private BigDecimal taxPrice;

    /** 送料 */
    private BigDecimal carriage;

    private List<GoogleAnalyticsSalesItem> goodsItemList;

}
