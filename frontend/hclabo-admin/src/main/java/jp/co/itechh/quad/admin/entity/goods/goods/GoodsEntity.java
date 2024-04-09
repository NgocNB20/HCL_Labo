/*
 * Project Name : HIT-MALL4
 *
 * Copyright (C) 2021 i-TEC HANKYU HANSHIN INC. All Rights Reserved.
 *
 */

package jp.co.itechh.quad.admin.entity.goods.goods;

import jp.co.itechh.quad.admin.constant.type.HTypeFreeDeliveryFlag;
import jp.co.itechh.quad.admin.constant.type.HTypeGoodsSaleStatus;
import jp.co.itechh.quad.admin.constant.type.HTypeIndividualDeliveryType;
import jp.co.itechh.quad.admin.constant.type.HTypeStockManagementFlag;
import jp.co.itechh.quad.admin.constant.type.HTypeUnitManagementFlag;
import lombok.Data;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Component;

import java.io.Serializable;
import java.math.BigDecimal;
import java.sql.Timestamp;

/**
 * 商品クラス
 * <pre>
 * 画面入力を行う必須項目を追加した場合はGoodsRegistLogicImpl#getGoodsEntityByOverwritedRequiredItem
 * </pre>
 * @author EntityGenerator
 * @version $Revision: 1.0 $
 */
@Data
@Component
@Scope("prototype")
public class GoodsEntity implements Serializable {

    /** serialVersionUID */
    private static final long serialVersionUID = 1L;

    /** 商品SEQ（必須） */
    private Integer goodsSeq;

    /** 商品グループSEQ (FK)（必須） */
    private Integer goodsGroupSeq;

    /** 商品コード（必須） */
    private String goodsCode;

    /** JANコード */
    private String janCode;

    /** 販売状態PC（必須） */
    private HTypeGoodsSaleStatus saleStatusPC = HTypeGoodsSaleStatus.NO_SALE;

    /** 販売開始日時PC */
    private Timestamp saleStartTimePC;

    /** 販売終了日時PC */
    private Timestamp saleEndTimePC;

    /** 商品個別配送種別（必須） */
    private HTypeIndividualDeliveryType individualDeliveryType = HTypeIndividualDeliveryType.OFF;

    /** 無料配送フラグ（必須） */
    private HTypeFreeDeliveryFlag freeDeliveryFlag = HTypeFreeDeliveryFlag.OFF;

    /** 規格管理フラグ（必須） */
    private HTypeUnitManagementFlag unitManagementFlag = HTypeUnitManagementFlag.OFF;

    /** 規格値１ */
    private String unitValue1;

    /** 規格値２ */
    private String unitValue2;

    /** 在庫管理フラグ（必須） */
    private HTypeStockManagementFlag stockManagementFlag = HTypeStockManagementFlag.OFF;

    /** 商品最大購入可能数（必須） */
    private BigDecimal purchasedMax = BigDecimal.ZERO;

    /** 表示順 */
    private Integer orderDisplay;

    /** ショップSEQ（必須） */
    private Integer shopSeq;

    /** 更新カウンタ（必須） */
    private Integer versionNo = 0;

    /** 登録日時（必須） */
    private Timestamp registTime;

    /** 更新日時（必須） */
    private Timestamp updateTime;
}