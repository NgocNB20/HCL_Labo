/*
 * Project Name : HIT-MALL4
 *
 * Copyright (C) 2021 i-TEC HANKYU HANSHIN INC. All Rights Reserved.
 *
 */

package jp.co.itechh.quad.front.pc.web.front.member.history;

import jp.co.itechh.quad.front.base.utility.ApplicationContextUtility;
import jp.co.itechh.quad.front.constant.type.HTypeExamStatus;
import jp.co.itechh.quad.front.constant.type.HTypeGoodsTaxType;
import jp.co.itechh.quad.front.constant.type.HTypeOpenDeleteStatus;
import jp.co.itechh.quad.front.utility.GoodsUtility;
import lombok.Data;
import org.apache.commons.lang3.StringUtils;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Component;

import java.io.Serializable;
import java.math.BigDecimal;
import java.sql.Timestamp;
import java.util.List;

/**
 * 注文履歴詳細商品 ModelItem
 *
 * @author kimura
 */
@Data
@Component
@Scope("prototype")
public class HistoryModelGoodsItem implements Serializable {

    /** serialVersionUID */
    private static final long serialVersionUID = 1L;

    /** 商品コード */
    private String goodsCode;

    /** 商品名 */
    private String goodsGroupName;

    /** 規格タイトル1 */
    private String unitTitle1;

    /** 規格タイトル2 */
    private String unitTitle2;

    /** 規格値1 */
    private String unitValue1;

    /** 規格値2 */
    private String unitValue2;

    /** 商品単価（税抜） */
    private BigDecimal goodsPrice;

    /** 商品単価（税込） */
    private BigDecimal goodsPriceInTax;

    /** 商品数量 */
    private BigDecimal goodsCount;

    /** 小計（税抜） */
    private BigDecimal subTotalGoodsPrice;

    /** 小計（税込） */
    private BigDecimal subTotalGoodsPriceInTax;

    /** 税率 */
    private BigDecimal taxRate;

    /** 商品消費税種別*/
    private HTypeGoodsTaxType goodsTaxType;

    /** 商品画像アイテム */
    private List<String> goodsImageItems;

    /** 公開状態 */
    private HTypeOpenDeleteStatus goodsOpenStatus;

    /* ノベルティ商品フラグ */
    private String noveltyGoodsType;

    /** 検査キット番号 */
    private String examKitCode;

    /** 検査状態 */
    private HTypeExamStatus examStatus;

    /** 検査結果PDF */
    private String examResultsPdf;

    /** 公開開始日時 */
    private Timestamp openStartTime;

    /** 公開終了日時 */
    private Timestamp openEndTime;

    /**
     * 規格1の有無<br/>
     *
     * @return true=有、false=無
     */
    public boolean isUnit1() {
        return unitValue1 != null && unitValue1.length() != 0;
    }

    /**
     * 規格2の有無<br/>
     *
     * @return true=有、false=無
     */
    public boolean isUnit2() {
        return unitValue2 != null && unitValue2.length() != 0;
    }

    /**
     * 商品公開判定<br/>
     *
     * @return true=公開、false=公開でない
     */
    public boolean isGoodsOpen() {
        // 商品系Helper取得
        GoodsUtility goodsUtility = ApplicationContextUtility.getBean(GoodsUtility.class);

        return goodsUtility.isGoodsOpen(goodsOpenStatus, openStartTime, openEndTime);
    }

    /**
     *  検査結果ダウンロード判定<br/>
     *
     * @return true=利用可能、false=利用不可
     */
    public boolean isDownloadPdf() {
        return StringUtils.isNotEmpty(examResultsPdf);
    }

    /**
     * 商品コード<br/>
     *
     * @return 商品番号
     */
    public String getGcd() {
        return goodsCode;
    }

}
