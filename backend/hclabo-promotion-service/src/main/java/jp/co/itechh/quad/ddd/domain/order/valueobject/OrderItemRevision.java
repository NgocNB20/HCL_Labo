/*
 * Copyright (C) 2022 i-TEC HANKYU HANSHIN INC. All Rights Reserved.
 */

package jp.co.itechh.quad.ddd.domain.order.valueobject;

import jp.co.itechh.quad.core.constant.type.HTypeNoveltyGoodsType;
import lombok.Getter;

/**
 * 改訂用注文商品 値オブジェクト
 */
@Getter
public class OrderItemRevision extends OrderItem {

    /**
     * コンストラクタ
     *
     * @param itemId           商品ID
     * @param orderItemSeq     注文商品連番
     * @param orderCount       注文数量
     * @param itemName         商品名
     * @param unitTitle1       規格タイトル1
     * @param unitValue1       規格値1
     * @param unitTitle2       規格タイトル2
     * @param unitValue2       規格値2
     * @param janCode          JANコード
     * @param noveltyGoodsType ノベルティ商品フラグ
     * @param orderItemId       注文商品ID
     */
    public OrderItemRevision(String itemId,
                             OrderItemSeq orderItemSeq,
                             OrderCount orderCount,
                             String itemName,
                             String unitTitle1,
                             String unitValue1,
                             String unitTitle2,
                             String unitValue2,
                             String janCode,
                             HTypeNoveltyGoodsType noveltyGoodsType,
                             OrderItemId orderItemId) {
        super(itemId, orderItemSeq, orderCount, itemName, unitTitle1, unitValue1, unitTitle2, unitValue2, janCode,
              noveltyGoodsType, orderItemId
             );
    }

    /**
     * メッセージ用の商品情報を作成
     * @return メッセージ用の商品情報
     */
    @Override
    public String createItemNameInfo() {
        // int number = getOrderItemSeq().getValue();
        //
        // // 商品規格１と商品規格２の両方とも情報が存在する場合
        // if (StringUtils.isNotBlank(getUnitValue1()) && StringUtils.isNotBlank(getUnitValue2())) {
        //     return "受注商品" + number + "行目：「" + getItemName() + "（規格1:" + getUnitValue1() + " / 規格2:" + getUnitValue2() + "）」";
        // }
        // // 商品規格１のみの場合
        // else if (StringUtils.isNotBlank(getUnitValue1())) {
        //     return "受注商品" + number + "行目：「" + getItemName() + "（規格1:" + getUnitValue1() + "）」";
        // }
        // // 商品名のみの場合
        // else {
        //     return "受注商品" + number + "行目：「" + getItemName() + "」";
        // }

        // 上記コメントアウトに対する補足
        // =======================================================
        // orderItemSeqは、注文商品の追加／削除を繰り返すことで飛び番が発生しうる
        // ⇒飛び番が発生しうる時点で、上記コメントアウト内で組み立てているエラーメッセージ文言上の
        //  【受注商品〇行目】　という部分は破綻している（正確な行数が表現できない）
        //   　　↓
        //  【受注商品〇行目】の表記は、あくまで便利機能であるため、表記無しの仕様とする
        //      ↓
        //   行数を表記無しとするならば、親クラス（OrderItem#createItemNameInfo()）の表記と同じになるため、
        //   ⇒よって上記メッセージ組み立て処理はすべてコメントアウトし、親クラスのメソッドを呼び出すようにする

        return super.createItemNameInfo();
    }
}