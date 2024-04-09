/*
 * Project Name : HIT-MALL4
 *
 * Copyright (C) 2021 i-TEC HANKYU HANSHIN INC. All Rights Reserved.
 *
 */

package jp.co.itechh.quad.core.dto.goods.goods;

import jp.co.itechh.quad.core.constant.type.HTypeAlcoholFlag;
import jp.co.itechh.quad.core.constant.type.HTypeFreeDeliveryFlag;
import jp.co.itechh.quad.core.constant.type.HTypeGoodsSaleStatus;
import jp.co.itechh.quad.core.constant.type.HTypeGoodsTaxType;
import jp.co.itechh.quad.core.constant.type.HTypeIndividualDeliveryType;
import jp.co.itechh.quad.core.constant.type.HTypeOpenDeleteStatus;
import jp.co.itechh.quad.core.constant.type.HTypeSnsLinkFlag;
import jp.co.itechh.quad.core.constant.type.HTypeStockManagementFlag;
import jp.co.itechh.quad.core.constant.type.HTypeStockStatusType;
import jp.co.itechh.quad.core.constant.type.HTypeUnitManagementFlag;
import jp.co.itechh.quad.core.dto.icon.GoodsInformationIconDetailsDto;
import jp.co.itechh.quad.core.entity.goods.goodsgroup.GoodsGroupImageEntity;
import lombok.Data;
import org.seasar.doma.Entity;
import org.seasar.doma.Transient;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Component;

import java.io.Serializable;
import java.math.BigDecimal;
import java.sql.Timestamp;
import java.util.ArrayList;
import java.util.List;

/**
 * 商品詳細Dtoクラス
 *
 * @author DtoGenerator
 *
 */
@Entity
@Data
@Component
@Scope("prototype")
public class GoodsDetailsDto implements Serializable {

    /** serialVersionUID */
    private static final long serialVersionUID = 1L;

    /** 商品SEQ */
    private Integer goodsSeq;

    /** 商品グループSEQ */
    private Integer goodsGroupSeq;

    /** ショップSEQ */
    private Integer shopSeq;

    /** 更新カウンタ（必須） */
    private Integer versionNo;

    /** 登録日時（必須） */
    private Timestamp registTime;

    /** 更新日時（必須） */
    private Timestamp updateTime;

    /** 商品コード */
    private String goodsCode;

    /** 商品消費税区分 */
    private HTypeGoodsTaxType goodsTaxType;

    /** 税率 */
    private BigDecimal taxRate;

    /** 酒類フラグ */
    private HTypeAlcoholFlag alcoholFlag;

    // TODO 「GET /products/details : 商品詳細一覧取得」で取得しても常にNull。マスタで税込み価格を新たに保持するか、goodsPriceが常に税込みか商品サービスの修正が必要
    /** 商品単価（税込） */
    private BigDecimal goodsPriceInTax;

    /** 商品単価（税抜） */
    private BigDecimal goodsPrice;

    /** 商品納期 */
    private String deliveryType;

    /** 販売状態PC */
    private HTypeGoodsSaleStatus saleStatusPC;

    /** 販売開始日時PC */
    private Timestamp saleStartTimePC;

    /** 販売終了日時PC */
    private Timestamp saleEndTimePC;

    /** 規格管理 */
    private HTypeUnitManagementFlag unitManagementFlag;

    /** 在庫管理 */
    private HTypeStockManagementFlag stockManagementFlag;

    /** 商品個別配送 */
    private HTypeIndividualDeliveryType individualDeliveryType;

    /** 商品最大購入可能数 */
    private BigDecimal purchasedMax;

    /** 無料配送フラグ */
    private HTypeFreeDeliveryFlag freeDeliveryFlag;

    /** 表示順 */
    private Integer orderDisplay;

    /** 規格値１ */
    private String unitValue1;

    /** 規格値２ */
    private String unitValue2;

    /** JANコード */
    private String janCode;

    /** 販売可能在庫数 */
    private BigDecimal salesPossibleStock;

    /** 実在庫数 */
    private BigDecimal realStock;

    /** 注文確保在庫数 */
    private BigDecimal orderReserveStock;

    /** 残少表示在庫数 */
    private BigDecimal remainderFewStock;

    /** 発注点在庫数 */
    private BigDecimal orderPointStock;

    /** 安全在庫数 */
    private BigDecimal safetyStock;

    /** 商品グループコード */
    private String goodsGroupCode;

    /** 新着日付 */
    private Timestamp whatsnewDate;

    /** 商品公開状態PC */
    private HTypeOpenDeleteStatus goodsOpenStatusPC;

    /** 商品公開開始日時PC */
    private Timestamp openStartTimePC;

    /** 商品公開終了日時PC */
    private Timestamp openEndTimePC;

    /** 商品グループ名 */
    private String goodsGroupName;

    /** 規格タイトル1 */
    private String unitTitle1;

    /** 規格タイトル2 */
    private String unitTitle2;

    /** 商品グループ画像エンティティリスト */
    @Transient
    private List<GoodsGroupImageEntity> goodsGroupImageEntityList = new ArrayList<>();

    /** SNS連携フラグ */
    private HTypeSnsLinkFlag snsLinkFlag;

    /** meta-description */
    private String metaDescription;

    /** 在庫状態PC */
    private HTypeStockStatusType stockStatusPc;

    /** 商品アイコンリスト */
    @Transient
    private List<GoodsInformationIconDetailsDto> goodsIconList = new ArrayList<>();

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
}
