/*
 * Copyright (C) 2022 i-TEC HANKYU HANSHIN INC. All Rights Reserved.
 */

package jp.co.itechh.quad.ddd.domain.pricecalculator.valueobject;

import lombok.Data;

/**
 * 商品販売金額合計ファクトリ パラメータ
 */
@Data
public class ItemSalesPriceTotalFactoryParam {

    /** 注文商品連番 */
    private Integer orderItemSeq;

    /** 注文商品ID（商品サービスの商品SEQ） */
    private String orderItemId;

    /** 注文商品数量 */
    private int orderItemCount;

    /** 既存商品 ※マスタを使用せず、トランデータの商品情報を適用する場合に設定 */
    private Item applyExistItem;

}
