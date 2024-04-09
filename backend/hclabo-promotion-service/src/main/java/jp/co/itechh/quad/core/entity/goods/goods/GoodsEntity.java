/*
 * Project Name : HIT-MALL4
 *
 * Copyright (C) 2021 i-TEC HANKYU HANSHIN INC. All Rights Reserved.
 *
 */

package jp.co.itechh.quad.core.entity.goods.goods;

import jp.co.itechh.quad.core.constant.type.HTypeFreeDeliveryFlag;
import jp.co.itechh.quad.core.constant.type.HTypeGoodsSaleStatus;
import jp.co.itechh.quad.core.constant.type.HTypeIndividualDeliveryType;
import jp.co.itechh.quad.core.constant.type.HTypeStockManagementFlag;
import jp.co.itechh.quad.core.constant.type.HTypeUnitManagementFlag;
import lombok.Data;
import org.seasar.doma.Column;
import org.seasar.doma.Entity;
import org.seasar.doma.Id;
import org.seasar.doma.Table;
import org.seasar.doma.Version;
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
@Entity
@Table(name = "Goods")
@Data
@Component
@Scope("prototype")
public class GoodsEntity implements Serializable {

    /** serialVersionUID */
    private static final long serialVersionUID = 1L;

    /** 商品SEQ（必須） */
    @Column(name = "goodsSeq")
    @Id
    private Integer goodsSeq;

    /** 商品グループSEQ (FK)（必須） */
    @Column(name = "goodsGroupSeq")
    private Integer goodsGroupSeq;

    /** 商品コード（必須） */
    @Column(name = "goodsCode")
    private String goodsCode;

    /** JANコード */
    @Column(name = "janCode")
    private String janCode;

    /** 販売状態PC（必須） */
    @Column(name = "saleStatusPC")
    private HTypeGoodsSaleStatus saleStatusPC = HTypeGoodsSaleStatus.NO_SALE;

    /** 販売開始日時PC */
    @Column(name = "saleStartTimePC")
    private Timestamp saleStartTimePC;

    /** 販売終了日時PC */
    @Column(name = "saleEndTimePC")
    private Timestamp saleEndTimePC;

    /** 商品個別配送種別（必須） */
    @Column(name = "individualDeliveryType")
    private HTypeIndividualDeliveryType individualDeliveryType = HTypeIndividualDeliveryType.OFF;

    /** 無料配送フラグ（必須） */
    @Column(name = "freeDeliveryFlag")
    private HTypeFreeDeliveryFlag freeDeliveryFlag = HTypeFreeDeliveryFlag.OFF;

    /** 規格管理フラグ（必須） */
    @Column(name = "unitManagementFlag")
    private HTypeUnitManagementFlag unitManagementFlag = HTypeUnitManagementFlag.OFF;

    /** 規格値１ */
    @Column(name = "unitValue1")
    private String unitValue1;

    /** 規格値２ */
    @Column(name = "unitValue2")
    private String unitValue2;

    /** 在庫管理フラグ（必須） */
    @Column(name = "stockManagementFlag")
    private HTypeStockManagementFlag stockManagementFlag = HTypeStockManagementFlag.OFF;

    /** 商品最大購入可能数（必須） */
    @Column(name = "purchasedMax")
    private BigDecimal purchasedMax = BigDecimal.ZERO;

    /** 表示順 */
    @Column(name = "orderDisplay")
    private Integer orderDisplay;

    /** ショップSEQ（必須） */
    @Column(name = "shopSeq")
    private Integer shopSeq;

    /** 更新カウンタ（必須） */
    @Version
    @Column(name = "versionNo")
    private Integer versionNo = 0;

    /** 登録日時（必須） */
    @Column(name = "registTime")
    private Timestamp registTime;

    /** 更新日時（必須） */
    @Column(name = "updateTime")
    private Timestamp updateTime;
}
