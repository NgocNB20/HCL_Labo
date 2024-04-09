/*
 * Project Name : HIT-MALL4
 *
 * Copyright (C) 2021 i-TEC HANKYU HANSHIN INC. All Rights Reserved.
 *
 */

package jp.co.itechh.quad.admin.dto.goods.goods;

import jp.co.itechh.quad.admin.annotation.csv.CsvColumn;
import jp.co.itechh.quad.admin.annotation.validator.HVBothSideSpace;
import jp.co.itechh.quad.admin.annotation.validator.HVDate;
import jp.co.itechh.quad.admin.annotation.validator.HVHtml;
import jp.co.itechh.quad.admin.annotation.validator.HVSpecialCharacter;
import jp.co.itechh.quad.admin.base.constant.ValidatorConstants;
import jp.co.itechh.quad.admin.base.utility.DateUtility;
import jp.co.itechh.quad.admin.constant.type.HTypeAlcoholFlag;
import jp.co.itechh.quad.admin.constant.type.HTypeFreeDeliveryFlag;
import jp.co.itechh.quad.admin.constant.type.HTypeGoodsSaleStatus;
import jp.co.itechh.quad.admin.constant.type.HTypeIndividualDeliveryType;
import jp.co.itechh.quad.admin.constant.type.HTypeNoveltyGoodsType;
import jp.co.itechh.quad.admin.constant.type.HTypeOpenDeleteStatus;
import jp.co.itechh.quad.admin.constant.type.HTypeSnsLinkFlag;
import jp.co.itechh.quad.admin.constant.type.HTypeStockManagementFlag;
import jp.co.itechh.quad.admin.constant.type.HTypeUnitManagementFlag;
import jp.co.itechh.quad.admin.constant.type.SpaceValidateMode;
import jp.co.itechh.quad.admin.utility.CsvUtility;
import jp.co.itechh.quad.admin.utility.SupplyDateTimeUtility;
import lombok.Data;
import org.hibernate.validator.constraints.Length;
import org.hibernate.validator.constraints.Range;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Component;

import javax.validation.constraints.NotEmpty;
import javax.validation.constraints.Pattern;
import java.io.Serializable;
import java.math.BigDecimal;
import java.sql.Timestamp;

/**
 * 商品CSVダウンロードDtoクラス
 *
 * @author Pham Quang Dieu (VTI Japan Co., Ltd.)
 */
@Data
@Component
@Scope("prototype")
public class GoodsCsvDto implements Serializable {

    /** serialVersionUID */
    private static final long serialVersionUID = 1L;

    /** 商品SEQ */
    private Integer goodsSeq;

    /** 商品グループコード */
    @CsvColumn(order = 10, columnLabel = "商品管理番号")
    @NotEmpty
    @Pattern(regexp = ValidatorConstants.REGEX_UL_GOODS_GROUP_CODE, message = "{PKG-3680-013-S-W}")
    private String goodsGroupCode;

    /** 商品名 */
    @CsvColumn(order = 20, columnLabel = "商品名")
    @Length(min = 0, max = 120)
    private String goodsGroupName;

    /** 商品公開状態 */
    @CsvColumn(order = 30, columnLabel = "公開状態", enumOutputType = "value")
    private HTypeOpenDeleteStatus goodsOpenStatusPC;

    /** 商品公開開始日時 */
    @CsvColumn(order = 40, columnLabel = "公開開始日時", dateFormat = DateUtility.YMD_SLASH_HMS,
               supplyDateType = SupplyDateTimeUtility.TYPE_START)
    @HVDate(pattern = CsvUtility.UPLOAD_TIME_FORMAT, message = "{PKG-3680-002-S-W}")
    private Timestamp openStartTimePC;

    /** 商品公開終了日時 */
    @CsvColumn(order = 50, columnLabel = "公開終了日時", dateFormat = DateUtility.YMD_SLASH_HMS,
               supplyDateType = SupplyDateTimeUtility.TYPE_END)
    @HVDate(pattern = CsvUtility.UPLOAD_TIME_FORMAT, message = "{PKG-3680-002-S-W}")
    private Timestamp openEndTimePC;

    /** 新着日付 */
    @CsvColumn(order = 60, columnLabel = "新着日付", dateFormat = DateUtility.YMD_SLASH)
    @HVDate(message = "{PKG-3680-003-S-W}")
    private Timestamp whatsnewDate;

    /** 商品納期 */
    @CsvColumn(order = 70, columnLabel = "商品納期")
    @HVBothSideSpace
    @HVSpecialCharacter(allowCharacters = {})
    @Length(min = 0, max = 50)
    private String deliveryType;

    /** 商品説明01 */
    @CsvColumn(order = 80, columnLabel = "商品説明01")
    @HVSpecialCharacter(allowPunctuation = true)
    @HVHtml(innerHtml = true)
    @Length(min = 0, max = 4000)
    private String goodsNote1;

    /** 商品説明02 */
    @CsvColumn(order = 90, columnLabel = "商品説明02")
    @HVSpecialCharacter(allowPunctuation = true)
    @HVHtml(innerHtml = true)
    @Length(min = 0, max = 4000)
    private String goodsNote2;

    /** 商品説明03 */
    @CsvColumn(order = 100, columnLabel = "商品説明03")
    @HVSpecialCharacter(allowPunctuation = true)
    @HVHtml(innerHtml = true)
    @Length(min = 0, max = 4000)
    private String goodsNote3;

    /** 商品説明04 */
    @CsvColumn(order = 110, columnLabel = "商品説明04")
    @HVSpecialCharacter(allowPunctuation = true)
    @HVHtml(innerHtml = true)
    @Length(min = 0, max = 4000)
    private String goodsNote4;

    /** 商品説明05 */
    @CsvColumn(order = 120, columnLabel = "商品説明05")
    @HVSpecialCharacter(allowPunctuation = true)
    @HVHtml(innerHtml = true)
    @Length(min = 0, max = 4000)
    private String goodsNote5;

    /** 商品説明06 */
    @CsvColumn(order = 130, columnLabel = "商品説明06")
    @HVSpecialCharacter(allowPunctuation = true)
    @HVHtml(innerHtml = true)
    @Length(min = 0, max = 4000)
    private String goodsNote6;

    /** 商品説明07 */
    @CsvColumn(order = 140, columnLabel = "商品説明07")
    @HVSpecialCharacter(allowPunctuation = true)
    @HVHtml(innerHtml = true)
    @Length(min = 0, max = 4000)
    private String goodsNote7;

    /** 商品説明08 */
    @CsvColumn(order = 150, columnLabel = "商品説明08")
    @HVSpecialCharacter(allowPunctuation = true)
    @HVHtml(innerHtml = true)
    @Length(min = 0, max = 4000)
    private String goodsNote8;

    /** 商品説明09 */
    @CsvColumn(order = 160, columnLabel = "商品説明09")
    @HVSpecialCharacter(allowPunctuation = true)
    @HVHtml(innerHtml = true)
    @Length(min = 0, max = 4000)
    private String goodsNote9;

    /** 商品説明10 */
    @CsvColumn(order = 170, columnLabel = "商品説明10")
    @HVSpecialCharacter(allowPunctuation = true)
    @HVHtml(innerHtml = true)
    @Length(min = 0, max = 4000)
    private String goodsNote10;

    /** カテゴリーID */
    @CsvColumn(order = 180, columnLabel = "カテゴリーID")
    @Pattern(regexp = "^(|" + ValidatorConstants.REGEX_UL_CATEGORY_ID + "(/" + ValidatorConstants.REGEX_UL_CATEGORY_ID
                      + ")*)$", message = "{PKG-3680-006-S-W}")
    private String categoryGoodsSetting;

    /** カテゴリー名 */
    @CsvColumn(order = 190, columnLabel = "カテゴリー名")
    private String categoryName;

    /** 商品タグ */
    @CsvColumn(order = 195, columnLabel = "商品タグ")
    private String goodsTagSetting;

    /** アイコンID */
    @CsvColumn(order = 200, columnLabel = "アイコンID")
    @Pattern(regexp = "^(|[0-9]{8}(/[0-9]{8}){0,15})$", message = "{PKG-3680-007-S-W}")
    private String informationIconPC;

    /** アイコン名  */
    @CsvColumn(order = 210, columnLabel = "アイコン名")
    private String iconName;

    /** ノベルティ商品フラグ */
    @CsvColumn(order = 215, columnLabel = "ノベルティ商品フラグ", enumOutputType = "value")
    private HTypeNoveltyGoodsType noveltyGoodsType;

    /** 酒類フラグ */
    @CsvColumn(order = 220, columnLabel = "酒類フラグ", enumOutputType = "value")
    private HTypeAlcoholFlag alcoholFlag;

    /** SNS連携フラグ */
    @CsvColumn(order = 230, columnLabel = "SNS連携フラグ", enumOutputType = "value")
    private HTypeSnsLinkFlag snsLinkFlag;

    /** 単価 */
    @CsvColumn(order = 240, columnLabel = "価格_税抜")
    @Range(min = 0, max = 99999999)
    private BigDecimal goodsPrice;

    /** 税率 */
    @CsvColumn(order = 250, columnLabel = "税率")
    @Range(min = 0, max = 99)
    private BigDecimal taxRate;

    /** 受注連携設定01 */
    @CsvColumn(order = 260, columnLabel = "受注連携設定01")
    @HVSpecialCharacter(allowPunctuation = true)
    @Length(min = 0, max = 4000)
    private String orderSetting1;

    /** 受注連携設定02 */
    @CsvColumn(order = 270, columnLabel = "受注連携設定02")
    @HVSpecialCharacter(allowPunctuation = true)
    @Length(min = 0, max = 4000)
    private String orderSetting2;

    /** 受注連携設定03 */
    @CsvColumn(order = 280, columnLabel = "受注連携設定03")
    @HVSpecialCharacter(allowPunctuation = true)
    @Length(min = 0, max = 4000)
    private String orderSetting3;

    /** 受注連携設定04 */
    @CsvColumn(order = 290, columnLabel = "受注連携設定04")
    @HVSpecialCharacter(allowPunctuation = true)
    @Length(min = 0, max = 4000)
    private String orderSetting4;

    /** 受注連携設定05 */
    @CsvColumn(order = 300, columnLabel = "受注連携設定05")
    @HVSpecialCharacter(allowPunctuation = true)
    @Length(min = 0, max = 4000)
    private String orderSetting5;
    /** 受注連携設定06 */
    @CsvColumn(order = 310, columnLabel = "受注連携設定06")
    @HVSpecialCharacter(allowPunctuation = true)
    @Length(min = 0, max = 4000)
    private String orderSetting6;

    /** 受注連携設定07 */
    @CsvColumn(order = 320, columnLabel = "受注連携設定07")
    @HVSpecialCharacter(allowPunctuation = true)
    @Length(min = 0, max = 4000)
    private String orderSetting7;

    /** 受注連携設定08 */
    @CsvColumn(order = 330, columnLabel = "受注連携設定08")
    @HVSpecialCharacter(allowPunctuation = true)
    @Length(min = 0, max = 4000)
    private String orderSetting8;

    /** 受注連携設定09 */
    @CsvColumn(order = 340, columnLabel = "受注連携設定09")
    @HVSpecialCharacter(allowPunctuation = true)
    @Length(min = 0, max = 4000)
    private String orderSetting9;

    /** 受注連携設定10 */
    @CsvColumn(order = 350, columnLabel = "受注連携設定10")
    @HVSpecialCharacter(allowPunctuation = true)
    @Length(min = 0, max = 4000)
    private String orderSetting10;

    /** 検索キーワード */
    @CsvColumn(order = 360, columnLabel = "商品検索キーワード")
    @HVSpecialCharacter(allowPunctuation = true)
    @Length(min = 0, max = 1000)
    private String searchKeyword;

    /** meta-description */
    @CsvColumn(order = 370, columnLabel = "メタ説明文")
    @HVSpecialCharacter(
                    allowCharacters = {'!', '#', '%', '(', ')', '*', '+', ',', '-', '.', '/', ':', ';', '=', '@', '[',
                                    ']', '_', '|'})
    @Length(min = 0, max = 400)
    private String metaDescription;

    /** meta-keyword */
    @CsvColumn(order = 380, columnLabel = "メタキーワード")
    @HVSpecialCharacter(
                    allowCharacters = {'!', '#', '%', '(', ')', '*', '+', ',', '-', '.', '/', ':', ';', '=', '@', '[',
                                    ']', '_', '|'})
    @Length(min = 0, max = 200)
    private String metaKeyword;

    /** 関連商品_商品管理番号 */
    @CsvColumn(order = 390, columnLabel = "関連商品_商品管理番号")
    @Pattern(regexp = "^(|" + ValidatorConstants.REGEX_UL_GOODS_GROUP_CODE + "(/"
                      + ValidatorConstants.REGEX_UL_GOODS_GROUP_CODE + ")*)$", message = "{PKG-3680-009-S-W}")
    private String goodsRelationGoodsGroupCode;

    /** 規格管理フラグ */
    @CsvColumn(order = 400, columnLabel = "規格表示", enumOutputType = "value")
    private HTypeUnitManagementFlag unitManagementFlag;

    /** 規格1表示名 */
    @CsvColumn(order = 410, columnLabel = "規格1表示名")
    @Length(min = 0, max = 100)
    @HVBothSideSpace(startWith = SpaceValidateMode.DENY_SPACE, endWith = SpaceValidateMode.DENY_SPACE)
    private String unitTitle1;

    /** 規格2表示名 */
    @CsvColumn(order = 420, columnLabel = "規格2表示名")
    @Length(min = 0, max = 100)
    @HVBothSideSpace(startWith = SpaceValidateMode.DENY_SPACE, endWith = SpaceValidateMode.DENY_SPACE)
    private String unitTitle2;

    // 【規格情報】==================================================================================================
    /** 規格表示順 */
    @CsvColumn(order = 430, columnLabel = "規格表示順")
    @Range(min = 1, max = 9999)
    private Integer orderDisplay;

    /** 商品番号 */
    @CsvColumn(order = 440, columnLabel = "商品番号")
    @NotEmpty
    @Pattern(regexp = ValidatorConstants.REGEX_UL_GOODS_CODE, message = "{PKG-3680-010-S-W}")
    private String goodsCode;

    /** 規格1 */
    @CsvColumn(order = 450, columnLabel = "規格1")
    @Length(min = 0, max = 100)
    private String unitValue1;

    /** 規格2 */
    @CsvColumn(order = 460, columnLabel = "規格2")
    @Length(min = 0, max = 100)
    private String unitValue2;

    /** 注文上限 */
    @CsvColumn(order = 470, columnLabel = "注文上限")
    @Range(min = 1, max = 9999)
    private BigDecimal purchasedMax;

    /** 販売状態 */
    @CsvColumn(order = 480, columnLabel = "販売状態", enumOutputType = "value")
    private HTypeGoodsSaleStatus saleStatusPC;

    /** JANコード */
    @CsvColumn(order = 490, columnLabel = "JANコード")
    @Pattern(regexp = ValidatorConstants.REGEX_UL_JAN_CODE, message = "{PKG-3680-012-S-W}")
    private String janCode;

    /** 販売開始日時 */
    @CsvColumn(order = 500, columnLabel = "販売開始日時", dateFormat = DateUtility.YMD_SLASH_HMS,
               supplyDateType = SupplyDateTimeUtility.TYPE_START)
    @HVDate(pattern = CsvUtility.UPLOAD_TIME_FORMAT, message = "{PKG-3680-002-S-W}")
    private Timestamp saleStartTimePC;

    /** 販売終了日時 */
    @CsvColumn(order = 510, columnLabel = "販売終了日時", dateFormat = DateUtility.YMD_SLASH_HMS,
               supplyDateType = SupplyDateTimeUtility.TYPE_END)
    @HVDate(pattern = CsvUtility.UPLOAD_TIME_FORMAT, message = "{PKG-3680-002-S-W}")
    private Timestamp saleEndTimePC;

    /** 個別配送 */
    @CsvColumn(order = 520, columnLabel = "個別配送", enumOutputType = "value")
    private HTypeIndividualDeliveryType individualDeliveryType;

    /** 送料込／送料別 */
    @CsvColumn(order = 530, columnLabel = "送料区分", enumOutputType = "value")
    private HTypeFreeDeliveryFlag freeDeliveryFlag;

    /** 在庫管理フラグ */
    @CsvColumn(order = 540, columnLabel = "在庫設定", enumOutputType = "value")
    private HTypeStockManagementFlag stockManagementFlag;

    /** 安全在庫数 */
    @CsvColumn(order = 550, columnLabel = "安全在庫数")
    @Range(min = 0, max = 999999)
    private BigDecimal safetyStock;

    /** 残少表示在庫数 */
    @CsvColumn(order = 560, columnLabel = "残少表示在庫数")
    @Range(min = 0, max = 999999)
    private BigDecimal remainderFewStock;

    /** 発注点在庫数 */
    @CsvColumn(order = 570, columnLabel = "発注点在庫数")
    @Range(min = 0, max = 999999)
    private BigDecimal orderPointStock;

    /** 販売可能在庫数 */
    @CsvColumn(order = 580, columnLabel = "販売可能在庫数", isOnlyDownload = true)
    private BigDecimal salesPossibleStock;

    /** 実在個数 */
    @CsvColumn(order = 590, columnLabel = "実在庫数", isOnlyDownload = true)
    private BigDecimal realStock;

    /** 受注確保在庫数 */
    @CsvColumn(order = 600, columnLabel = "受注確保在庫数", isOnlyDownload = true)
    private BigDecimal orderReserveStock;

    /** 入庫数 */
    @CsvColumn(order = 610, columnLabel = "入庫数")
    @Range(min = -999999, max = 999999)
    private BigDecimal supplementCount;
}