/*
 * Project Name : HIT-MALL4
 *
 * Copyright (C) 2021 i-TEC HANKYU HANSHIN INC. All Rights Reserved.
 *
 */

package jp.co.itechh.quad.core.entity.goods.goodsgroup;

import jp.co.itechh.quad.core.constant.type.HTypeUnitManagementFlag;
import lombok.Data;
import org.seasar.doma.Column;
import org.seasar.doma.Entity;
import org.seasar.doma.Id;
import org.seasar.doma.Table;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Component;

import java.io.Serializable;
import java.sql.Timestamp;

/**
 * 商品グループ表示クラス
 *
 * @author EntityGenerator
 * @version $Revision: 1.0 $
 */
@Entity
@Table(name = "GoodsGroupDisplay")
@Data
@Component
@Scope("prototype")
public class GoodsGroupDisplayEntity implements Serializable {

    /** serialVersionUID */
    private static final long serialVersionUID = 1L;

    /** 商品グループSEQ (FK)（必須） */
    @Column(name = "goodsGroupSeq")
    @Id
    private Integer goodsGroupSeq;

    /** インフォメーションアイコンPC */
    @Column(name = "informationIconPC")
    private String informationIconPC;

    /** 商品検索キーワード */
    @Column(name = "searchKeyword")
    private String searchKeyword;

    /** 商品検索キーワード全角 */
    @Column(name = "searchKeywordEm")
    private String searchKeywordEm;

    /** 規格管理フラグ（必須） */
    @Column(name = "unitManagementFlag")
    private HTypeUnitManagementFlag unitManagementFlag = HTypeUnitManagementFlag.OFF;

    /** 規格タイトル１ */
    @Column(name = "unitTitle1")
    private String unitTitle1;

    /** 規格タイトル２ */
    @Column(name = "unitTitle2")
    private String unitTitle2;

    /** meta-description */
    @Column(name = "metaDescription")
    private String metaDescription;

    /** meta-keyword */
    @Column(name = "metaKeyword")
    private String metaKeyword;

    /** 商品納期 */
    @Column(name = "deliveryType")
    private String deliveryType;

    /** 商品説明１ */
    @Column(name = "goodsNote1")
    private String goodsNote1;

    /** 商品説明２ */
    @Column(name = "goodsNote2")
    private String goodsNote2;

    /** 商品説明３ */
    @Column(name = "goodsNote3")
    private String goodsNote3;

    /** 商品説明４ */
    @Column(name = "goodsNote4")
    private String goodsNote4;

    /** 商品説明５ */
    @Column(name = "goodsNote5")
    private String goodsNote5;

    /** 商品説明６ */
    @Column(name = "goodsNote6")
    private String goodsNote6;

    /** 商品説明７ */
    @Column(name = "goodsNote7")
    private String goodsNote7;

    /** 商品説明８ */
    @Column(name = "goodsNote8")
    private String goodsNote8;

    /** 商品説明９ */
    @Column(name = "goodsNote9")
    private String goodsNote9;

    /** 商品説明１０ */
    @Column(name = "goodsNote10")
    private String goodsNote10;

    /** 受注連携設定１ */
    @Column(name = "orderSetting1")
    private String orderSetting1;

    /** 受注連携設定２ */
    @Column(name = "orderSetting2")
    private String orderSetting2;

    /** 受注連携設定３ */
    @Column(name = "orderSetting3")
    private String orderSetting3;

    /** 受注連携設定４ */
    @Column(name = "orderSetting4")
    private String orderSetting4;

    /** 受注連携設定５ */
    @Column(name = "orderSetting5")
    private String orderSetting5;

    /** 受注連携設定６ */
    @Column(name = "orderSetting6")
    private String orderSetting6;

    /** 受注連携設定７ */
    @Column(name = "orderSetting7")
    private String orderSetting7;

    /** 受注連携設定８ */
    @Column(name = "orderSetting8")
    private String orderSetting8;

    /** 受注連携設定９ */
    @Column(name = "orderSetting9")
    private String orderSetting9;

    /** 受注連携設定１０ */
    @Column(name = "orderSetting10")
    private String orderSetting10;

    /** 登録日時（必須） */
    @Column(name = "registTime")
    private Timestamp registTime;

    /** 更新日時（必須） */
    @Column(name = "updateTime")
    private Timestamp updateTime;
}
