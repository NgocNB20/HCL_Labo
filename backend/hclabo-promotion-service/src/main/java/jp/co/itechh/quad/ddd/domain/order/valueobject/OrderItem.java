/*
 * Copyright (C) 2022 i-TEC HANKYU HANSHIN INC. All Rights Reserved.
 */

package jp.co.itechh.quad.ddd.domain.order.valueobject;

import jp.co.itechh.quad.core.constant.type.HTypeNoveltyGoodsType;
import jp.co.itechh.quad.ddd.exception.AssertChecker;
import jp.co.itechh.quad.ddd.exception.DomainException;
import lombok.Getter;
import org.apache.commons.lang3.StringUtils;

/**
 * 注文商品 値オブジェクト
 */
@Getter
public class OrderItem {

    /** 商品ID（商品サービスの商品SEQ） */
    private final String itemId;

    /** 注文商品連番 */
    private final OrderItemSeq orderItemSeq;

    /** 注文数量 */
    private final OrderCount orderCount;

    /** 商品名 */
    private final String itemName;

    /** 規格タイトル1 */
    private final String unitTitle1;

    /** 規格値1 */
    private final String unitValue1;

    /** 規格タイトル2 */
    private final String unitTitle2;

    /** 規格値2 */
    private final String unitValue2;

    /** JANコード */
    private final String janCode;

    /** ノベルティ商品フラグ */
    private final HTypeNoveltyGoodsType noveltyGoodsType;

    /** 注文商品ID */
    private final OrderItemId orderItemId;

    /**
     * コンストラクタ
     *
     * @param itemId       商品ID
     * @param orderItemSeq 注文商品連番
     * @param orderCount   注文数量
     * @param orderItemId  注文商品ID
     */
    public OrderItem(String itemId, OrderItemSeq orderItemSeq, OrderCount orderCount, OrderItemId orderItemId) {

        // チェック
        AssertChecker.assertNotEmpty("itemId is Exist", itemId);
        AssertChecker.assertNotNull("orderItemSeq is Exist", orderItemSeq);
        AssertChecker.assertNotNull("orderCount is Exist", orderCount);

        // 設定
        this.itemId = itemId;
        this.orderItemSeq = orderItemSeq;
        this.orderCount = orderCount;
        this.itemName = null;
        this.unitTitle1 = null;
        this.unitValue1 = null;
        this.unitTitle2 = null;
        this.unitValue2 = null;
        this.janCode = null;
        this.noveltyGoodsType = HTypeNoveltyGoodsType.NORMAL_GOODS;
        this.orderItemId = orderItemId;
    }

    /**
     * コンストラクタ<br/>
     * 商品マスタ情報を設定するための暫定用コンストラクタ<br/>
     *
     * @param itemId       商品ID
     * @param orderItemSeq 注文商品連番
     * @param orderCount   注文数量
     * @param itemName     商品名
     * @param unitTitle1   規格タイトル1
     * @param unitValue1   規格値1
     * @param unitTitle2   規格タイトル2
     * @param unitValue2   規格値2
     * @param janCode      JANコード
     * @param noveltyGoodsType  ノベルティ商品
     * @param orderItemId  注文商品ID
     */
    public OrderItem(String itemId,
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

        // チェック
        AssertChecker.assertNotEmpty("itemId is Exist", itemId);
        AssertChecker.assertNotNull("orderItemSeq is Exist", orderItemSeq);
        AssertChecker.assertNotNull("orderCount is Exist", orderCount);
        // 規格1情報が未設定だが、規格2情報が設定されている場合はエラー
        if ((StringUtils.isBlank(unitTitle1) && StringUtils.isNotBlank(unitTitle2)) || (StringUtils.isBlank(unitValue1)
                                                                                        && StringUtils.isNotBlank(
                        unitValue2))) {
            throw new DomainException("PROMOTION-ODER0026-E");
        }
        // 規格のタイトル＋値がセットで設定されていない場合はエラー
        if ((StringUtils.isBlank(unitTitle1) && StringUtils.isNotBlank(unitValue1)) || (
                        StringUtils.isNotBlank(unitTitle1) && StringUtils.isBlank(unitValue1))) {
            throw new DomainException("PROMOTION-ODER0027-E");
        }
        if ((StringUtils.isBlank(unitTitle2) && StringUtils.isNotBlank(unitValue2)) || (
                        StringUtils.isNotBlank(unitTitle2) && StringUtils.isBlank(unitValue2))) {
            throw new DomainException("PROMOTION-ODER0027-E");
        }

        // 設定
        this.itemId = itemId;
        this.orderItemSeq = orderItemSeq;
        this.orderCount = orderCount;
        this.itemName = itemName;
        this.unitTitle1 = unitTitle1;
        this.unitValue1 = unitValue1;
        this.unitTitle2 = unitTitle2;
        this.unitValue2 = unitValue2;
        this.janCode = janCode;
        this.noveltyGoodsType = noveltyGoodsType;
        this.orderItemId = orderItemId;
    }

    /**
     * メッセージ用の商品情報を作成
     * @return メッセージ用の商品情報
     */
    public String createItemNameInfo() {

        // 商品規格１と商品規格２の両方とも情報が存在する場合
        if (StringUtils.isNotBlank(unitTitle1) && StringUtils.isNotBlank(unitValue1) && StringUtils.isNotBlank(
                        unitTitle2) && StringUtils.isNotBlank(unitValue2)) {
            return "「" + itemName + "（" + unitTitle1 + ":" + unitValue1 + " / " + unitTitle2 + ":" + unitValue2 + "）」";
        }
        // 商品規格１のみの場合
        else if (StringUtils.isNotBlank(unitTitle1) && StringUtils.isNotBlank(unitValue1)) {
            return "「" + itemName + "（" + unitTitle1 + ":" + unitValue1 + "）」";
        }
        // 商品名のみの場合
        else {
            return "「" + itemName + "」";
        }
    }

}