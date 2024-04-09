/*
 * Project Name : HIT-MALL4
 *
 * Copyright (C) 2021 i-TEC HANKYU HANSHIN INC. All Rights Reserved.
 *
 */

package jp.co.itechh.quad.core.dto.goods.goodsgroup;

import jp.co.itechh.quad.core.constant.type.HTypeFrontDisplayStatus;
import jp.co.itechh.quad.core.constant.type.HTypeGoodsTaxType;
import jp.co.itechh.quad.core.constant.type.HTypeOpenDeleteStatus;
import jp.co.itechh.quad.core.constant.type.HTypeStockStatusType;
import jp.co.itechh.quad.core.constant.type.HTypeUnitManagementFlag;
import lombok.Data;
import org.seasar.doma.Entity;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Component;

import java.io.Serializable;
import java.math.BigDecimal;
import java.sql.Array;
import java.sql.Timestamp;

/**
 * 商品グループDtoクラス
 *
 * @author DtoGenerator
 * @version $Revision: 1.0 $
 */
@Entity
@Data
@Component
@Scope("prototype")
public class GoodsGroupDetailsDto implements Serializable {

    /** serialVersionUID */
    private static final long serialVersionUID = 1L;

    /** 商品グループSEQ */
    private Integer goodsGroupSeq;

    /** 商品タグ */
    private Array goodsTag;

    /** 商品グループコード */
    private String goodsGroupCode;

    /** 商品単価 */
    private BigDecimal goodsPrice;

    /** 新着日付 */
    private Timestamp whatsnewDate;

    /** 商品公開状態PC */
    private HTypeOpenDeleteStatus goodsOpenStatusPC;

    /** 商品公開開始日時PC */
    private Timestamp openStartTimePC;

    /** 商品公開終了日時PC */
    private Timestamp openEndTimePC;

    /** ショップSEQ */
    private Integer shopSeq;

    /** 商品グループ名 */
    private String goodsGroupName;

    /** 商品消費税区分 */
    private HTypeGoodsTaxType goodsTaxType;

    /** 税率 */
    private BigDecimal taxRate;

    /** 更新カウンタ */
    private Integer versionNo;

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

    /** インフォメーションアイコンPC */
    private String informationIconPC;

    /** 商品検索キーワード */
    private String searchKeyword;

    /** 商品検索キーワード全角 */
    private String searchKeywordEm;

    /** 規格管理フラグ */
    private HTypeUnitManagementFlag unitManagementFlag;

    /** 規格タイトル１ */
    private String unitTitle1;

    /** 規格タイトル２ */
    private String unitTitle2;

    /** meta-description */
    private String metaDescription;

    /** meta-keyword */
    private String metaKeyword;

    /** 商品納期 */
    private String deliverytype;

    /** 在庫状態PC */
    private HTypeStockStatusType stockstatusPC;

    /** 人気カウント */
    private Integer popularityCount;

    /** フロント表示 */
    private HTypeFrontDisplayStatus frontDisplay;
}
