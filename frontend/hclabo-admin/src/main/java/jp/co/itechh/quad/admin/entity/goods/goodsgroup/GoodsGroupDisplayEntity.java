/*
 * Project Name : HIT-MALL4
 *
 * Copyright (C) 2021 i-TEC HANKYU HANSHIN INC. All Rights Reserved.
 *
 */

package jp.co.itechh.quad.admin.entity.goods.goodsgroup;

import jp.co.itechh.quad.admin.constant.type.HTypeUnitManagementFlag;
import lombok.Data;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Component;

import java.io.Serializable;
import java.sql.Timestamp;
import java.util.List;

/**
 * 商品グループ表示クラス
 *
 * @author EntityGenerator
 * @version $Revision: 1.0 $
 */
@Data
@Component
@Scope("prototype")
public class GoodsGroupDisplayEntity implements Serializable {

    /** serialVersionUID */
    private static final long serialVersionUID = 1L;

    /** 商品グループSEQ (FK)（必須） */
    private Integer goodsGroupSeq;

    /** インフォメーションアイコンPC */
    private String informationIconPC;

    /** 商品検索キーワード */
    private String searchKeyword;

    /** 商品検索キーワード全角 */
    private String searchKeywordEm;

    /** 商品タグ */
    private List<String> goodsTagList;

    /** 規格管理フラグ（必須） */
    private HTypeUnitManagementFlag unitManagementFlag = HTypeUnitManagementFlag.OFF;

    /** 規格タイトル１ */
    private String unitTitle1;

    /** 規格タイトル２ */
    private String unitTitle2;

    /** meta-description */
    private String metaDescription;

    /** meta-keyword */
    private String metaKeyword;

    /** 商品納期 */
    private String deliveryType;

    /** 商品説明１ */
    private String goodsNote1;

    /** 商品説明２ */
    private String goodsNote2;

    /** 商品説明３ */
    private String goodsNote3;

    /** 商品説明４ */
    private String goodsNote4;

    /** 商品説明５ */
    private String goodsNote5;

    /** 商品説明６ */
    private String goodsNote6;

    /** 商品説明７ */
    private String goodsNote7;

    /** 商品説明８ */
    private String goodsNote8;

    /** 商品説明９ */
    private String goodsNote9;

    /** 商品説明１０ */
    private String goodsNote10;

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

    /** 登録日時（必須） */
    private Timestamp registTime;

    /** 更新日時（必須） */
    private Timestamp updateTime;
}