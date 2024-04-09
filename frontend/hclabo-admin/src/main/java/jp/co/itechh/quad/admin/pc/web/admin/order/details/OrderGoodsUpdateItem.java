/*
 * Project Name : HIT-MALL3
 *
 * Copyright (C) 2008 i-TEC HANKYU HANSHIN INC. All Rights Reserved.
 *
 */

package jp.co.itechh.quad.admin.pc.web.admin.order.details;

import jp.co.itechh.quad.admin.annotation.converter.HCNumber;
import jp.co.itechh.quad.admin.annotation.validator.HVNumber;
import jp.co.itechh.quad.admin.base.utility.ApplicationContextUtility;
import jp.co.itechh.quad.admin.base.utility.ConversionUtility;
import jp.co.itechh.quad.admin.base.utility.NumberUtility;
import jp.co.itechh.quad.admin.constant.type.HTypeExamStatus;
import jp.co.itechh.quad.admin.constant.type.HTypeFreeDeliveryFlag;
import jp.co.itechh.quad.admin.constant.type.HTypeIndividualDeliveryType;
import jp.co.itechh.quad.admin.constant.type.HTypeNoveltyGoodsType;
import jp.co.itechh.quad.admin.pc.web.admin.common.validation.group.ConfirmGroup;
import jp.co.itechh.quad.admin.pc.web.admin.order.details.validation.detailsupdate.group.AdditionalChargeGroup;
import jp.co.itechh.quad.admin.pc.web.admin.order.details.validation.detailsupdate.group.OrderGoodsDeleteGroup;
import jp.co.itechh.quad.admin.pc.web.admin.order.details.validation.detailsupdate.group.OrderGoodsModifyGroup;
import jp.co.itechh.quad.admin.pc.web.admin.order.details.validation.detailsupdate.group.ReCalculateGroup;
import lombok.Data;
import org.apache.commons.lang3.StringUtils;
import org.hibernate.validator.constraints.Range;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Component;

import javax.validation.constraints.Digits;
import javax.validation.constraints.NotEmpty;
import java.io.Serializable;
import java.math.BigDecimal;

/**
 * 受注詳細商品アイテムクラス<br/>
 *
 * @author Pham Quang Dieu (VTI Japan Co., Ltd.)
 */
@Data
@Component
@Scope("prototype")
public class OrderGoodsUpdateItem implements Serializable {

    /** serialVersionUID */
    private static final long serialVersionUID = 1L;

    /** 正規表現エラー(制限なし) */
    public static final String MSGCD_REGULAR_EXPRESSION_ERR = "AOX001015W";
    /** 正規表現エラー(全角のみ) */
    public static final String MSGCD_REGULAR_EXPRESSION_EM_SIZE_ERR = "AOX001016W";
    /** 正規表現エラー(半角英数のみ) */
    public static final String MSGCD_REGULAR_EXPRESSION_AN_CHAR_ERR = "AOX001017W";
    /** 正規表現エラー(半角英字のみ) */
    public static final String MSGCD_REGULAR_EXPRESSION_A_CHAR_ERR = "AOX001018W";
    /** 正規表現エラー(半角数字のみ) */
    public static final String MSGCD_REGULAR_EXPRESSION_N_CHAR_ERR = "AOX001019W";

    /**
     * goodsCheck<br />
     */
    private boolean goodsCheck;

    /**
     * goodsStyle<br />
     */
    private String diffGoodsClass;

    /**
     * errContent<br />
     */
    private String errContent;

    /** 商品SEQ（必須） */
    private Integer goodsSeq;

    /** 表示順（必須） */
    private Integer orderDisplay;

    /** 商品コード（必須） */
    private String goodsCode;

    /** JANコード */
    private String janCode;

    /** 商品名（必須） */
    private String goodsGroupName;

    /** 税率 */
    private BigDecimal taxRate = BigDecimal.ZERO;

    /** 商品単価（税抜き）（必須） */
    private BigDecimal goodsPrice = BigDecimal.ZERO;

    /** 商品単価（税込み）（必須） */
    private BigDecimal goodsPriceInTax = BigDecimal.ZERO;

    /** 商品数量（必須） */
    private BigDecimal goodsCount = BigDecimal.ZERO;

    /** 商品数量（入力） */
    @NotEmpty(groups = {ConfirmGroup.class, ReCalculateGroup.class, AdditionalChargeGroup.class,
                    OrderGoodsDeleteGroup.class, OrderGoodsModifyGroup.class})
    @HVNumber(groups = {ConfirmGroup.class, ReCalculateGroup.class, AdditionalChargeGroup.class,
                    OrderGoodsDeleteGroup.class, OrderGoodsModifyGroup.class})
    @Digits(integer = 4, fraction = 0, message = "{HNumberLengthValidator.FRACTION_MAX_ZERO_detail}",
            groups = {ConfirmGroup.class, ReCalculateGroup.class, AdditionalChargeGroup.class,
                            OrderGoodsDeleteGroup.class, OrderGoodsModifyGroup.class})
    @Range(min = 0, max = 9999, groups = {ConfirmGroup.class, ReCalculateGroup.class, AdditionalChargeGroup.class,
                    OrderGoodsDeleteGroup.class, OrderGoodsModifyGroup.class})
    @HCNumber
    private String updateGoodsCount;

    /** 商品数量変更前（入力） */
    @NotEmpty(groups = {ConfirmGroup.class, ReCalculateGroup.class, AdditionalChargeGroup.class,
                    OrderGoodsDeleteGroup.class, OrderGoodsModifyGroup.class})
    @HVNumber(groups = {ConfirmGroup.class, ReCalculateGroup.class, AdditionalChargeGroup.class,
                    OrderGoodsDeleteGroup.class, OrderGoodsModifyGroup.class})
    @Digits(integer = 4, fraction = 0, message = "{HNumberLengthValidator.FRACTION_MAX_ZERO_detail}",
            groups = {ConfirmGroup.class, ReCalculateGroup.class, AdditionalChargeGroup.class,
                            OrderGoodsDeleteGroup.class, OrderGoodsModifyGroup.class})
    @Range(min = 0, max = 9999, groups = {ConfirmGroup.class, ReCalculateGroup.class, AdditionalChargeGroup.class,
                    OrderGoodsDeleteGroup.class, OrderGoodsModifyGroup.class})
    @HCNumber
    private String originalUpdateGoodsCount;

    /** 商品小計 */
    private BigDecimal postTaxOrderGoodsPrice = BigDecimal.ZERO;

    /** 無料配送フラグ（必須） */
    private HTypeFreeDeliveryFlag freeDeliveryFlag = HTypeFreeDeliveryFlag.OFF;

    /** 規格値１ */
    private String unitValue1;

    /** 規格値２ */
    private String unitValue2;

    /** 商品個別配送種別（必須） */
    private HTypeIndividualDeliveryType individualDeliveryType = HTypeIndividualDeliveryType.OFF;

    /** ショップSEQ（必須） */
    private Integer shopSeq;

    /** 商品グループコード（必須） */
    private String goodsGroupCode;

    /** 商品数量変更前（必須） */
    private BigDecimal originGoodsCount;

    /** 検査キット番号 */
    private String examKitCode;

    /** ノベルティ商品フラグ */
    private HTypeNoveltyGoodsType noveltyGoodsType;

    /** 検査状態 */
    private HTypeExamStatus examStatus;

    /** 検査結果PDF */
    private String examResultsPdf;

    /**
     * @return the orderGoodsPrice
     */
    public BigDecimal getOrderGoodsPrice() {
        return goodsPrice.multiply(goodsCount);
    }

    /**
     * クーポン対象商品<br/>
     * クーポン利用時に対象商品を表示する<br/>
     * <p>
     * true..クーポン対象商品
     */
    private boolean couponTargetGoodsFlg = false;

    /**
     * @return updateGoodsCount
     */
    public String getUpdateGoodsCount() {
        // 数値関連Helper取得
        NumberUtility numberUtility = ApplicationContextUtility.getBean(NumberUtility.class);
        if (updateGoodsCount != null && updateGoodsCount.indexOf('.') < 0 && numberUtility.isNumber(updateGoodsCount)) {
            // 変換Helper取得
            ConversionUtility conversionUtility = ApplicationContextUtility.getBean(ConversionUtility.class);
            return conversionUtility.toBigDecimal(updateGoodsCount).toString();
        }
        return updateGoodsCount;
    }

    /**
     * @param updateGoodsCount the updateGoodsCount to set
     */
    public void setUpdateGoodsCount(String updateGoodsCount) {
        this.updateGoodsCount = updateGoodsCount;
        // 数値関連Helper取得
        NumberUtility numberUtility = ApplicationContextUtility.getBean(NumberUtility.class);
        if (updateGoodsCount == null || updateGoodsCount.isEmpty()) {
            goodsCount = BigDecimal.ZERO;
        } else if (updateGoodsCount.indexOf('.') < 0 && numberUtility.isNumber(updateGoodsCount)
                   && Long.parseLong(updateGoodsCount) >= 0L) {
            // 変換Helper取得
            ConversionUtility conversionUtility = ApplicationContextUtility.getBean(ConversionUtility.class);
            goodsCount = conversionUtility.toBigDecimal(updateGoodsCount);
        }
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
     * @return the individualDelivery is ON
     */
    public boolean isIndividualDelivery() {
        return HTypeIndividualDeliveryType.ON.equals(individualDeliveryType);
    }
}