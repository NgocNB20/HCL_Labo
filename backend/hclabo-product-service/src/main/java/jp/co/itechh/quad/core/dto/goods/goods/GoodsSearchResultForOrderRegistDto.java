/*
 * Project Name : HIT-MALL4
 *
 * Copyright (C) 2021 i-TEC HANKYU HANSHIN INC. All Rights Reserved.
 *
 */

package jp.co.itechh.quad.core.dto.goods.goods;

import jp.co.itechh.quad.core.constant.type.HTypeFreeDeliveryFlag;
import jp.co.itechh.quad.core.constant.type.HTypeGoodsTaxType;
import jp.co.itechh.quad.core.constant.type.HTypeIndividualDeliveryType;
import jp.co.itechh.quad.core.constant.type.HTypeStockManagementFlag;
import jp.co.itechh.quad.core.constant.type.HTypeUnitManagementFlag;
import lombok.Data;
import org.seasar.doma.Entity;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Component;

import java.io.Serializable;
import java.math.BigDecimal;

/**
 * 受注修正_追加商品検索結果Dtoクラス
 *
 * @author DtoGenerator
 */
@Entity
@Data
@Component
@Scope("prototype")
public class GoodsSearchResultForOrderRegistDto implements Serializable {

    /** serialVersionUID */
    private static final long serialVersionUID = 1L;

    /** 商品SEQ */
    private Integer goodsSeq;

    /** 商品コード */
    private String goodsCode;

    /** JANコード */
    private String janCode;

    /** 商品名 */
    private String goodsGroupName;

    /** 税率 */
    private BigDecimal taxRate;

    /** 商品単価（税抜き） */
    private BigDecimal goodsPrice;

    /** 商品消費税種別 */
    private HTypeGoodsTaxType goodsTaxType;

    /** 無料配送フラグ */
    private HTypeFreeDeliveryFlag freeDeliveryFlag;

    /** 規格値１ */
    private String unitValue1;

    /** 規格値２ */
    private String unitValue2;

    /** 商品グループコード */
    private String goodsGroupCode;

    /** 商品個別配送種別 */
    private HTypeIndividualDeliveryType individualDeliveryType;

    /** 在庫管理フラグ */
    private HTypeStockManagementFlag stockManagementFlag;

    /** 販売可能在庫数 */
    private BigDecimal salesPossibleStock;

    /** 規格管理フラグ */
    private HTypeUnitManagementFlag unitManagementFlag;

    /** 商品納期 */
    private String deliveryType;

    /** 受注連携設定１ */
    private String orderSetting1;

    /** 受注連携設定２ */
    private String orderSetting2;

    /** 受注連携設定３ */
    private String orderSetting3;

    /** 受注連携設定４ */
    private String orderSetting4;

    /** 受注連携設定５ */
    private String orderSetting5;

    /** 受注連携設定６ */
    private String orderSetting6;

    /** 受注連携設定７ */
    private String orderSetting7;

    /** 受注連携設定８ */
    private String orderSetting8;

    /** 受注連携設定９ */
    private String orderSetting9;

    /** 受注連携設定１０ */
    private String orderSetting10;
}