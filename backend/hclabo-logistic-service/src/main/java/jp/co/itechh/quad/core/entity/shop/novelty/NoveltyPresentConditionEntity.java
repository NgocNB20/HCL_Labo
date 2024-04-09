/*
 * Project Name : HIT-MALL4
 *
 * Copyright (C) 2021 i-TEC HANKYU HANSHIN INC. All Rights Reserved.
 *
 */

package jp.co.itechh.quad.core.entity.shop.novelty;

import jp.co.itechh.quad.core.constant.type.HTypeEnclosureUnitType;
import jp.co.itechh.quad.core.constant.type.HTypeGoodsPriceTotalFlag;
import jp.co.itechh.quad.core.constant.type.HTypeMagazineSendFlag;
import jp.co.itechh.quad.core.constant.type.HTypeNoveltyPresentState;
import lombok.Data;
import org.seasar.doma.Column;
import org.seasar.doma.Entity;
import org.seasar.doma.Id;
import org.seasar.doma.Table;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Component;

import java.io.Serializable;
import java.math.BigDecimal;
import java.sql.Timestamp;

/**
 *
 * ノベルティプレゼント条件クラス<br/>
 *
 * @author PHAM QUANG DIEU (VJP)
 *
 */
@Entity
@Table(name = "NoveltyPresentCondition")
@Data
@Component
@Scope("prototype")
public class NoveltyPresentConditionEntity implements Serializable {

    /** serialVersionUID */
    private static final long serialVersionUID = 1L;

    /** ノベルティプレゼント条件SEQ */
    @Column(name = "noveltyPresentConditionSeq")
    @Id
    private Integer noveltyPresentConditionSeq;

    /** ノベルティプレゼント条件名 */
    @Column(name = "noveltyPresentName")
    private String noveltyPresentName;

    /** 同梱単位区分 */
    @Column(name = "enclosureUnitType")
    private HTypeEnclosureUnitType enclosureUnitType;

    /** ノベルティプレゼント条件状態 */
    @Column(name = "noveltyPresentState")
    private HTypeNoveltyPresentState noveltyPresentState;

    /** ノベルティプレゼント条件開始日時 */
    @Column(name = "noveltyPresentStartTime")
    private Timestamp noveltyPresentStartTime;

    /** ノベルティプレゼント条件終了日時 */
    @Column(name = "noveltyPresentEndTime")
    private Timestamp noveltyPresentEndTime;

    /** 除外条件SEQ */
    @Column(name = "exclusionNoveltyPresentSeq")
    private String exclusionNoveltyPresentSeq;

    /** メールマガジン配信条件フラグ */
    @Column(name = "magazineSendFlag")
    private HTypeMagazineSendFlag magazineSendFlag;

    /** 入会開始日時 */
    @Column(name = "admissionStartTime")
    private Timestamp admissionStartTime;

    /** 入会終了日時 */
    @Column(name = "admissionEndTime")
    private Timestamp admissionEndTime;

    /** 商品管理番号 */
    @Column(name = "goodsGroupCode")
    private String goodsGroupCode;

    /** 商品番号 */
    @Column(name = "goodsCode")
    private String goodsCode;

    /** カテゴリーID */
    @Column(name = "categoryId")
    private String categoryId;

    /** アイコンID */
    @Column(name = "iconId")
    private String iconId;

    /** 商品名 */
    @Column(name = "goodsName")
    private String goodsName;

    /** 検索キーワード */
    @Column(name = "searchKeyword")
    private String searchKeyword;

    /** 商品金額合計 */
    @Column(name = "goodsPriceTotal")
    private BigDecimal goodsPriceTotal;

    /** 商品金額条件フラグ */
    @Column(name = "goodsPriceTotalFlag")
    private HTypeGoodsPriceTotalFlag goodsPriceTotalFlag;

    /** プレゼント数制限 */
    @Column(name = "prizeGoodsLimit")
    private Integer prizeGoodsLimit;

    /** 管理メモ */
    @Column(name = "memo")
    private String memo;

    /** 登録日時 */
    @Column(name = "registTime", updatable = false)
    private Timestamp registTime;

    /** 更新日時 */
    @Column(name = "updateTime")
    private Timestamp updateTime;

}