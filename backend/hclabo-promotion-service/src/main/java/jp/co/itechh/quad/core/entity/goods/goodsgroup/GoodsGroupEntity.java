/*
 * Project Name : HIT-MALL4
 *
 * Copyright (C) 2021 i-TEC HANKYU HANSHIN INC. All Rights Reserved.
 *
 */

package jp.co.itechh.quad.core.entity.goods.goodsgroup;

import jp.co.itechh.quad.core.constant.type.HTypeAlcoholFlag;
import jp.co.itechh.quad.core.constant.type.HTypeGoodsTaxType;
import jp.co.itechh.quad.core.constant.type.HTypeOpenDeleteStatus;
import jp.co.itechh.quad.core.constant.type.HTypeSnsLinkFlag;
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
 * 商品グループクラス
 *
 * @author EntityGenerator
 * @version $Revision: 1.0 $
 */
@Entity
@Table(name = "GoodsGroup")
@Data
@Component
@Scope("prototype")
public class GoodsGroupEntity implements Serializable {

    /** serialVersionUID */
    private static final long serialVersionUID = 1L;

    /** 商品グループSEQ（必須） */
    @Column(name = "goodsGroupSeq")
    @Id
    private Integer goodsGroupSeq;

    /** 商品グループコード（必須） */
    @Column(name = "goodsGroupCode")
    private String goodsGroupCode;

    /** 商品グループ名 */
    @Column(name = "goodsGroupName")
    private String goodsGroupName;

    /** 商品単価 */
    @Column(name = "goodsPrice")
    private BigDecimal goodsPrice = BigDecimal.ZERO;

    /** 商品単価（税込） */
    private BigDecimal goodsPriceInTax;

    /** 新着日付（必須） */
    @Column(name = "whatsnewDate")
    private Timestamp whatsnewDate;

    /** 商品公開状態PC（必須） */
    @Column(name = "goodsOpenStatusPC")
    private HTypeOpenDeleteStatus goodsOpenStatusPC = HTypeOpenDeleteStatus.NO_OPEN;

    /** 商品公開開始日時PC */
    @Column(name = "openStartTimePC")
    private Timestamp openStartTimePC;

    /** 商品公開終了日時PC */
    @Column(name = "openEndTimePC")
    private Timestamp openEndTimePC;

    /** 商品消費税種別（必須） */
    @Column(name = "goodsTaxType")
    private HTypeGoodsTaxType goodsTaxType = HTypeGoodsTaxType.OUT_TAX;

    /** 税率（必須） */
    @Column(name = "taxRate")
    private BigDecimal taxRate;

    /** 酒類フラグ */
    @Column(name = "alcoholFlag")
    private HTypeAlcoholFlag alcoholFlag = HTypeAlcoholFlag.NOT_ALCOHOL;

    /** SNS連携フラグ */
    @Column(name = "snsLinkFlag")
    private HTypeSnsLinkFlag snsLinkFlag = HTypeSnsLinkFlag.OFF;

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
