package jp.co.itechh.quad.admin.pc.web.admin.shop.settlement.registupdate;

import jp.co.itechh.quad.admin.annotation.converter.HCNumber;
import jp.co.itechh.quad.admin.annotation.validator.HVBothSideSpace;
import jp.co.itechh.quad.admin.annotation.validator.HVHtml;
import jp.co.itechh.quad.admin.annotation.validator.HVItems;
import jp.co.itechh.quad.admin.annotation.validator.HVNumber;
import jp.co.itechh.quad.admin.annotation.validator.HVSpecialCharacter;
import jp.co.itechh.quad.admin.base.utility.ApplicationContextUtility;
import jp.co.itechh.quad.admin.base.utility.ConversionUtility;
import jp.co.itechh.quad.admin.base.utility.NumberUtility;
import jp.co.itechh.quad.admin.base.utility.ZenHanConversionUtility;
import jp.co.itechh.quad.admin.constant.type.HTypeBillType;
import jp.co.itechh.quad.admin.constant.type.HTypeMailRequired;
import jp.co.itechh.quad.admin.constant.type.HTypeOpenDeleteStatus;
import jp.co.itechh.quad.admin.constant.type.HTypeSettlementMethodCommissionType;
import jp.co.itechh.quad.admin.constant.type.HTypeSettlementMethodPriceCommissionFlag;
import jp.co.itechh.quad.admin.constant.type.HTypeSettlementMethodType;
import jp.co.itechh.quad.admin.constant.type.SpaceValidateMode;
import jp.co.itechh.quad.admin.dto.shop.settlement.SettlementMethodDto;
import jp.co.itechh.quad.admin.entity.shop.settlement.SettlementMethodEntity;
import jp.co.itechh.quad.admin.entity.shop.settlement.SettlementMethodPriceCommissionEntity;
import jp.co.itechh.quad.admin.pc.web.admin.common.validation.group.ConfirmGroup;
import jp.co.itechh.quad.admin.web.AbstractModel;
import lombok.Data;
import lombok.EqualsAndHashCode;
import org.hibernate.validator.constraints.Length;

import javax.validation.Valid;
import javax.validation.constraints.DecimalMin;
import javax.validation.constraints.Digits;
import javax.validation.constraints.NotEmpty;
import java.sql.Timestamp;
import java.util.List;
import java.util.Map;

@EqualsAndHashCode(callSuper = false)
@Data
public class SettlementRegistUpdateModel extends AbstractModel {

    /**
     * 設定モード
     * 1=新規登録、2=更新
     */
    private Integer configMode;

    /**
     * 編集中の決済方法
     */
    private SettlementMethodDto settlementMethodDto;

    /** 修正されている項目名を保持するリスト */
    private List<String> modifiedList;

    /** 修正されている項目名を保持するリスト(金額別手数料) */
    private List<List<String>> modifiedPriceCommissionYenList;

    /**
     * 決済方法エンティティ（修正前情報格納）
     */
    private SettlementMethodEntity settlementMethodEntity;

    /**
     * 決済方法金額別エンティティリスト（修正前情報格納）
     */
    private List<SettlementMethodPriceCommissionEntity> settlementMethodPriceCommissionEntityList;

    /**
     * 確認からの遷移フラグ
     */
    private boolean fromConfirm;

    // --- 決済方法情報 ---
    /**
     * 決済方法SEQ
     */
    private Integer settlementMethodSeq;

    /**
     * 決済方法ID(SEQ)
     */
    private Integer settlementMethodId;

    /**
     * ショップSEQ
     */
    private Integer shopSeq;

    /**
     * 決済方法名称
     */
    @HVBothSideSpace(startWith = SpaceValidateMode.ALLOW_SPACE, endWith = SpaceValidateMode.ALLOW_SPACE,
                     groups = {ConfirmGroup.class})
    @HVSpecialCharacter(allowPunctuation = true, groups = {ConfirmGroup.class})
    @NotEmpty(groups = {ConfirmGroup.class})
    @Length(min = 1, max = 60, groups = {ConfirmGroup.class})
    private String settlementMethodName;

    /**
     * 決済方法表示名PC
     */
    @HVBothSideSpace(startWith = SpaceValidateMode.ALLOW_SPACE, endWith = SpaceValidateMode.ALLOW_SPACE,
                     groups = {ConfirmGroup.class})
    @HVSpecialCharacter(allowPunctuation = true, groups = {ConfirmGroup.class})
    private String settlementMethodDisplayNamePC;

    /**
     * 公開状態PC
     */
    @NotEmpty(message = "{HRequiredValidator.REQUIRED_detail}", groups = {ConfirmGroup.class})
    @HVItems(target = HTypeOpenDeleteStatus.class, groups = {ConfirmGroup.class})
    private String openStatusPC;

    /**
     * 公開状態PCアイテム
     */
    private Map<String, String> openStatusPCItems;

    /**
     * 公開状態携帯アイテム
     */
    private Map<String, String> openStatusMBItems;

    /**
     * 説明文PC
     */
    @HVBothSideSpace(startWith = SpaceValidateMode.ALLOW_SPACE, endWith = SpaceValidateMode.ALLOW_SPACE,
                     groups = {ConfirmGroup.class})
    @HVSpecialCharacter(allowPunctuation = true, groups = {ConfirmGroup.class})
    @Length(min = 0, max = 2000, message = "{HTextAreaValidator.LENGTH_detail}", groups = {ConfirmGroup.class})
    @HVHtml(innerHtml = true)
    private String settlementNotePC;

    /**
     * 決済方法種別
     */
    @NotEmpty(message = "{HRequiredValidator.REQUIRED_detail}", groups = {ConfirmGroup.class})
    @HVItems(target = HTypeSettlementMethodType.class, groups = {ConfirmGroup.class})
    private String settlementMethodType;

    /**
     * 決済方法種別ラベル表示用
     */
    private String settlementMethodTypeValue;

    /**
     * 決済方法種別アイテム<br/>
     * <p>
     * セレクトボックスに表示する値はこの画面でしか利用しないので、本来はデフォルトスコープで良い。<br/>
     * サブアプリケーションスコープを付与したのは、doLoadにて、リストに表示する値を編集したいからである。<br/>
     * スコープを付与しない場合、リストの値が毎回列挙型より生成されるため、上記の目的を果たせない。<br/>
     * 選択された値である決済方法種別とスコープを合わせる目的でサブアプリケーションスコープとしている。<br/>
     */
    private Map<String, String> settlementMethodTypeItems;

    /**
     * 決済方法手数料種別
     */
    @NotEmpty(message = "{HRequiredValidator.REQUIRED_detail}", groups = {ConfirmGroup.class})
    @HVItems(target = HTypeSettlementMethodCommissionType.class, groups = {ConfirmGroup.class})
    private String settlementMethodCommissionType;

    /**
     * 決済方法種別アイテム
     */
    private Map<String, String> settlementMethodCommissionTypeItems;

    /**
     * 請求種別
     */
    @NotEmpty(message = "{HRequiredValidator.REQUIRED_detail}", groups = {ConfirmGroup.class})
    @HVItems(target = HTypeBillType.class, groups = {ConfirmGroup.class})
    private String billType;

    /**
     * 請求種別ラベル表示用
     */
    private String billTypeValue;

    /**
     * 決済方法種別アイテム
     */
    private Map<String, String> billTypeItems;

    /**
     * 配送方法SEQ
     */
    private Integer deliveryMethodSeq;

    /**
     * 配送方法SEQアイテム
     */
    private Map<String, String> deliveryMethodSeqItems;

    /**
     * 配送方法名
     */
    private String deliveryMethodName;

    /**
     * 一律手数料
     */
    @SuppressWarnings("unused")
    private String equalsCommission;

    /**
     * 決済方法金額別手数料フラグ
     */
    private HTypeSettlementMethodPriceCommissionFlag settlementMethodPriceCommissionFlag;

    /**
     * 高額割引下限金額
     */
    @SuppressWarnings("unused")
    private String largeAmountDiscountPrice;

    /**
     * 高額割引手数料
     */
    @SuppressWarnings("unused")
    private String largeAmountDiscountCommission;

    /**
     * 表示順
     */
    private Integer orderDisplay;

    /**
     * 最大購入金額
     */
    @SuppressWarnings("unused")
    private String maxPurchasedPrice;

    /**
     * 最小購入金額
     */
    @HCNumber
    @HVNumber(groups = {ConfirmGroup.class})
    @Length(min = 1, max = 8, groups = {ConfirmGroup.class})
    @Digits(integer = 8, fraction = 0, groups = {ConfirmGroup.class})
    private String minPurchasedPrice = "0";

    /**
     * 決済関連メール要否フラグ
     */
    @NotEmpty(message = "{HRequiredValidator.REQUIRED_detail}", groups = {ConfirmGroup.class})
    @HVItems(target = HTypeMailRequired.class, groups = {ConfirmGroup.class})
    private String settlementMailRequired;

    /**
     * 決済関連メール要否フラグアイテム
     */
    private Map<String, String> settlementMailRequiredItems;

    /**
     * ｸﾚｼﾞｯﾄｶｰﾄﾞ登録機能有効化ﾌﾗｸﾞ
     */
    private boolean enableCardNoHolding;

    /**
     * ｸﾚｼﾞｯﾄ3Dｾｷｭｱ有効化ﾌﾗｸﾞ
     */
    private boolean enable3dSecure;

    /**
     * ｸﾚｼﾞｯﾄ分割支払有効化ﾌﾗｸﾞ
     */
    private boolean enableInstallment;

    /**
     * ｸﾚｼﾞｯﾄﾘﾎﾞ有効化ﾌﾗｸﾞ
     */
    private boolean enableRevolving;

    /**
     * ｸﾚｼﾞｯﾄｶｰﾄﾞ登録機能有効化ﾌﾗｸﾞ（表示用）
     */
    private String enableCardNoHoldingForDisp;

    /**
     * ｸﾚｼﾞｯﾄ3Dｾｷｭｱ有効化ﾌﾗｸﾞ（表示用）
     */
    private String enable3dSecureForDisp;
    /**
     * ｸﾚｼﾞｯﾄ分割支払有効化ﾌﾗｸﾞ（表示用）
     */
    private String enableInstallmentForDisp;

    /**
     * ｸﾚｼﾞｯﾄﾎﾞｰﾅｽ一括支払有効化ﾌﾗｸﾞ（表示用）
     */
    private String enableBonusSinglePaymentForDisp;

    /**
     * ｸﾚｼﾞｯﾄﾎﾞｰﾅｽ分割支払有効化ﾌﾗｸﾞ（表示用）
     */
    private String enableBonusInstallmentForDisp;

    /**
     * ｸﾚｼﾞｯﾄﾘﾎﾞ有効化ﾌﾗｸﾞ（表示用）
     */
    private String enableRevolvingForDisp;

    /**
     * 登録日時
     */
    private Timestamp registTime;

    /**
     * 更新日時
     */
    private Timestamp updateTime;

    // --- 決済方法金額別手数料 ---//

    /**
     * 決済方法金額別手数料リスト
     */
    @SuppressWarnings("unused")
    private List<SettlementRegistUpdateModelItem> priceCommissionItemList;

    // --- セッション保持 ---//

    /**
     * 決済方法手数料種別
     */
    private HTypeSettlementMethodCommissionType commissionType;

    /**
     * セッション保持用<br />
     * 決済方法金額別手数料リスト（円）
     */
    @Valid
    private List<SettlementRegistUpdateModelItem> priceCommissionYen;

    /**
     * セッション保持用<br />
     * 一律手数料（円）
     */
    @HCNumber
    @HVNumber(groups = {ConfirmGroup.class})
    @Length(min = 0, max = 8, groups = {ConfirmGroup.class})
    @Digits(integer = 8, fraction = 0, groups = {ConfirmGroup.class})
    private String equalsCommissionYen;

    /**
     * セッション保持用<br />
     * 高額割引下限金額（円）
     */
    @HCNumber
    @HVNumber(groups = {ConfirmGroup.class})
    @Length(min = 0, max = 8, groups = {ConfirmGroup.class})
    @Digits(integer = 8, fraction = 0, groups = {ConfirmGroup.class})
    @DecimalMin(value = "0", inclusive = false, groups = {ConfirmGroup.class})
    private String largeAmountDiscountPriceYen;

    /**
     * セッション保持用<br />
     * 高額割引手数料（円）
     */
    @HCNumber
    @HVNumber(groups = {ConfirmGroup.class})
    @Length(min = 0, max = 8, groups = {ConfirmGroup.class})
    @Digits(integer = 8, fraction = 0, groups = {ConfirmGroup.class})
    private String largeAmountDiscountCommissionYen;

    /**
     * セッション保持用<br />
     * 最大購入金額（円）
     */
    @HCNumber
    @HVNumber(groups = {ConfirmGroup.class})
    @Length(min = 0, max = 8, groups = {ConfirmGroup.class})
    @Digits(integer = 8, fraction = 0, groups = {ConfirmGroup.class})
    private String maxPurchasedPriceYen;

    /**
     * @return equalsCommission
     */
    public String getEqualsCommission() {
        if (HTypeSettlementMethodCommissionType.FLAT_YEN == commissionType) {
            return equalsCommissionYen;
        }
        return null;
    }

    /**
     * @param equalsCommission the equalsCommission to set
     */
    public void setEqualsCommission(String equalsCommission) {
        this.equalsCommission = toNumber(equalsCommission);
    }

    /**
     * 決済方法種別設定
     *
     * @param settlementMethodType 決済方法種別
     */
    public void setSettlementMethodType(String settlementMethodType) {

        this.settlementMethodType = settlementMethodType;

        if (HTypeSettlementMethodType.LINK_PAYMENT.getValue().equals(settlementMethodType)) {
            this.billType = HTypeBillType.PRE_CLAIM.getValue();
            this.billTypeValue = HTypeBillType.PRE_CLAIM.getValue();

        }
    }

    /**
     * @return largeAmountDiscountPrice
     */
    public String getLargeAmountDiscountPrice() {
        if (HTypeSettlementMethodCommissionType.FLAT_YEN == commissionType) {
            return largeAmountDiscountPriceYen;
        }
        return null;
    }

    /**
     * @param largeAmountDiscountPrice the largeAmountDiscountPrice to set
     */
    public void setLargeAmountDiscountPrice(String largeAmountDiscountPrice) {
        this.largeAmountDiscountPrice = toNumber(largeAmountDiscountPrice);
    }

    /**
     * @return largeAmountDiscountCommission
     */
    public String getLargeAmountDiscountCommission() {
        if (HTypeSettlementMethodCommissionType.FLAT_YEN == commissionType) {
            return largeAmountDiscountCommissionYen;
        }
        return null;
    }

    /**
     * @param largeAmountDiscountCommission the largeAmountDiscountCommission to set
     */
    public void setLargeAmountDiscountCommission(String largeAmountDiscountCommission) {
        this.largeAmountDiscountCommission = toNumber(largeAmountDiscountCommission);
    }

    /**
     * @return maxPurchasedPrice
     */
    public String getMaxPurchasedPrice() {
        if (HTypeSettlementMethodCommissionType.FLAT_YEN == commissionType) {
            return maxPurchasedPriceYen == null ? "0" : maxPurchasedPriceYen;
        }
        return maxPurchasedPrice;
    }

    /**
     * @param maxPurchasedPrice the maxPurchasedPrice to set
     */
    public void setMaxPurchasedPrice(String maxPurchasedPrice) {
        this.maxPurchasedPrice = toNumber(maxPurchasedPrice);
    }

    /**
     * @return true=新規登録, false=修正
     */
    public boolean isRegist() {
        return settlementMethodSeq == null;
    }

    /**
     * @return true=一律（円）
     */
    public boolean isFlatYen() {
        return settlementMethodCommissionType != null && settlementMethodCommissionType.equals(
                        HTypeSettlementMethodCommissionType.FLAT_YEN.getValue());
    }

    /**
     * @return true=金額別（円）
     */
    public boolean isEachAmountYen() {
        return settlementMethodCommissionType != null && settlementMethodCommissionType.equals(
                        HTypeSettlementMethodCommissionType.EACH_AMOUNT_YEN.getValue());
    }

    /**
     * @return true=決済方法種別がクレジット
     */
    public boolean isSettlementMethodTypeCredit() {
        return HTypeSettlementMethodType.CREDIT.getValue().equals(settlementMethodType);
    }

    /**
     * @return true=お支払い方法の種類はリンク決済です
     */
    public boolean isSettlementMethodTypeLinkPayment() {
        return HTypeSettlementMethodType.LINK_PAYMENT.getValue().equals(settlementMethodType);
    }

    /**
     * @return true=決済方法手数料種別選択済み
     */
    public boolean isCommissionTypeSelect() {
        return settlementMethodCommissionType != null;
    }

    /**
     * @return true=手数料フラグ一律
     */
    public boolean isFlat() {
        return settlementMethodPriceCommissionFlag != null
               && HTypeSettlementMethodPriceCommissionFlag.FLAT == settlementMethodPriceCommissionFlag;
    }

    /**
     * @return priceCommissionItemList
     */
    public List<SettlementRegistUpdateModelItem> getPriceCommissionItemList() {
        if (HTypeSettlementMethodCommissionType.EACH_AMOUNT_YEN == commissionType) {
            return priceCommissionYen;
        }
        return null;
    }

    /**
     * @param equalsCommissionYen the equalsCommissionYen to set
     */
    public void setEqualsCommissionYen(String equalsCommissionYen) {
        this.equalsCommissionYen = toNumber(equalsCommissionYen);
    }

    /**
     * @param largeAmountDiscountPriceYen the largeAmountDiscountPriceYen to set
     */
    public void setLargeAmountDiscountPriceYen(String largeAmountDiscountPriceYen) {
        this.largeAmountDiscountPriceYen = toNumber(largeAmountDiscountPriceYen);
    }

    /**
     * @param largeAmountDiscountCommissionYen the largeAmountDiscountCommissionYen to set
     */
    public void setLargeAmountDiscountCommissionYen(String largeAmountDiscountCommissionYen) {
        this.largeAmountDiscountCommissionYen = toNumber(largeAmountDiscountCommissionYen);
    }

    /**
     * @param maxPurchasedPriceYen the maxPurchasedPriceYen to set
     */
    public void setMaxPurchasedPriceYen(String maxPurchasedPriceYen) {
        this.maxPurchasedPriceYen = toNumber(maxPurchasedPriceYen);
    }

    /**
     * @param value 文字列
     * @return 数字の場、合数値として返す
     */
    protected String toNumber(String value) {
        // 全角、半角の変換Helper取得
        ZenHanConversionUtility zenHanConversionUtility =
                        ApplicationContextUtility.getBean(ZenHanConversionUtility.class);

        value = zenHanConversionUtility.toHankaku(value);

        // 数値関連Helper取得
        NumberUtility numberUtility = ApplicationContextUtility.getBean(NumberUtility.class);

        if (value != null && value.indexOf('.') < 0 && numberUtility.isNumber(value)) {
            // 変換Helper取得
            ConversionUtility conversionUtility = ApplicationContextUtility.getBean(ConversionUtility.class);

            return conversionUtility.toBigDecimal(value).toString();
        }
        return value;
    }
}