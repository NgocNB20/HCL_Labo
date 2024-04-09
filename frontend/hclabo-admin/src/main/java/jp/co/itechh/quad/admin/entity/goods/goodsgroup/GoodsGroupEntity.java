/*
 * Project Name : HIT-MALL4
 *
 * Copyright (C) 2021 i-TEC HANKYU HANSHIN INC. All Rights Reserved.
 *
 */

package jp.co.itechh.quad.admin.entity.goods.goodsgroup;

import jp.co.itechh.quad.admin.constant.type.HTypeAlcoholFlag;
import jp.co.itechh.quad.admin.constant.type.HTypeGoodsTaxType;
import jp.co.itechh.quad.admin.constant.type.HTypeNoveltyGoodsType;
import jp.co.itechh.quad.admin.constant.type.HTypeOpenDeleteStatus;
import jp.co.itechh.quad.admin.constant.type.HTypeSnsLinkFlag;
import lombok.Data;
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
@Data
@Component
@Scope("prototype")
public class GoodsGroupEntity implements Serializable {

    /** serialVersionUID */
    private static final long serialVersionUID = 1L;

    /** 商品グループSEQ（必須） */
    private Integer goodsGroupSeq;

    /** 商品グループコード（必須） */
    private String goodsGroupCode;

    /** 商品グループ名 */
    private String goodsGroupName;

    /** 商品単価 */
    private BigDecimal goodsPrice = BigDecimal.ZERO;

    /** 新着日付（必須） */
    private Timestamp whatsnewDate;

    /** 商品公開状態PC（必須） */
    private HTypeOpenDeleteStatus goodsOpenStatusPC = HTypeOpenDeleteStatus.NO_OPEN;

    /** 商品公開開始日時PC */
    private Timestamp openStartTimePC;

    /** 商品公開終了日時PC */
    private Timestamp openEndTimePC;

    /** 商品消費税種別（必須） */
    private HTypeGoodsTaxType goodsTaxType = HTypeGoodsTaxType.OUT_TAX;

    /** 税率（必須） */
    private BigDecimal taxRate;

    /** 酒類フラグ */
    private HTypeAlcoholFlag alcoholFlag = HTypeAlcoholFlag.NOT_ALCOHOL;

    /** ノベルティ商品フラグ */
    private HTypeNoveltyGoodsType noveltyGoodsType = HTypeNoveltyGoodsType.NORMAL_GOODS;

    /** SNS連携フラグ */
    private HTypeSnsLinkFlag snsLinkFlag = HTypeSnsLinkFlag.OFF;

    /** ショップSEQ（必須） */
    private Integer shopSeq;

    /** 更新カウンタ（必須） */
    private Integer versionNo = 0;

    /** 登録日時（必須） */
    private Timestamp registTime;

    /** 更新日時（必須） */
    private Timestamp updateTime;
}