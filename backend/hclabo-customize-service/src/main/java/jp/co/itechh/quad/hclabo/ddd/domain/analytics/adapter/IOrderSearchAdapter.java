/*
 * Copyright (C) 2022 i-TEC HANKYU HANSHIN INC. All Rights Reserved.
 */

package jp.co.itechh.quad.hclabo.ddd.domain.analytics.adapter;

/**
 * 受注検索アダプター
 */
public interface IOrderSearchAdapter {

    /**
     * 分析マイクロサービス<br/>
     * 受注検索情報を登録・更新する
     *
     * @param orderReceivedId 受注ID
     */
    void registUpdateOrderSearch(String orderReceivedId);
}
