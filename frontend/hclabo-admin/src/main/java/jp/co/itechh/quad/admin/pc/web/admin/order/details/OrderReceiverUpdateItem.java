package jp.co.itechh.quad.admin.pc.web.admin.order.details;

import jp.co.itechh.quad.admin.annotation.converter.HCDate;
import jp.co.itechh.quad.admin.annotation.converter.HCHankaku;
import jp.co.itechh.quad.admin.annotation.converter.HCZenkaku;
import jp.co.itechh.quad.admin.annotation.converter.HCZenkakuKana;
import jp.co.itechh.quad.admin.annotation.validator.HVBothSideSpace;
import jp.co.itechh.quad.admin.annotation.validator.HVDate;
import jp.co.itechh.quad.admin.annotation.validator.HVSpecialCharacter;
import jp.co.itechh.quad.admin.base.constant.RegularExpressionsConstants;
import jp.co.itechh.quad.admin.base.constant.ValidatorConstants;
import jp.co.itechh.quad.admin.constant.type.HTypeIndividualDeliveryType;
import jp.co.itechh.quad.admin.constant.type.HTypeReceiverDateDesignationFlag;
import jp.co.itechh.quad.admin.constant.type.HTypeShipmentStatus;
import jp.co.itechh.quad.admin.constant.type.SpaceValidateMode;
import jp.co.itechh.quad.admin.pc.web.admin.common.validation.group.ConfirmGroup;
import lombok.Data;
import org.hibernate.validator.constraints.Length;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Component;

import javax.validation.Valid;
import javax.validation.constraints.NotEmpty;
import javax.validation.constraints.Pattern;
import java.io.Serializable;
import java.sql.Timestamp;
import java.util.List;
import java.util.Map;

/**
 * お届け先表示情報Item
 *
 * @author h_hakogi
 */
@Data
@Component
@Scope("prototype")
public class OrderReceiverUpdateItem implements Serializable {

    /** serialVersionUID */
    private static final long serialVersionUID = 1L;

    /************************************
     ** お届け先住所情報
     ************************************/

    /** 住所ID */
    private String addressId;

    /** 氏名（姓） */
    @NotEmpty(groups = ConfirmGroup.class)
    @Length(min = 1, max = 16, groups = ConfirmGroup.class)
    @HCZenkaku
    private String receiverLastName;

    /** 氏名（名） */
    @NotEmpty(groups = ConfirmGroup.class)
    @Length(min = 1, max = 16, groups = ConfirmGroup.class)
    @HCZenkaku
    private String receiverFirstName;

    /** フリガナ（セイ） */
    @NotEmpty(groups = ConfirmGroup.class)
    @Length(min = 1, max = 40, groups = ConfirmGroup.class)
    @Pattern(regexp = RegularExpressionsConstants.ZENKAKU_KANA_REGEX,
             message = "{HZenkakuKanaValidator.INVALID_detail}", groups = ConfirmGroup.class)
    @HCZenkakuKana
    private String receiverLastKana;

    /** フリガナ（メイ） */
    @NotEmpty(groups = ConfirmGroup.class)
    @Length(min = 1, max = 40, groups = ConfirmGroup.class)
    @Pattern(regexp = RegularExpressionsConstants.ZENKAKU_KANA_REGEX,
             message = "{HZenkakuKanaValidator.INVALID_detail}", groups = ConfirmGroup.class)
    @HCZenkakuKana
    private String receiverFirstKana;

    /** 郵便番号 */
    @NotEmpty(groups = ConfirmGroup.class)
    @Length(min = 1, max = 7, groups = ConfirmGroup.class)
    @Pattern(regexp = RegularExpressionsConstants.ZIP_CODE_REGEX, message = "{HZipCodeValidator.INVALID_detail}",
             groups = ConfirmGroup.class)
    @HCHankaku
    private String receiverZipCode;

    /** 都道府県 */
    @NotEmpty(message = "{HRequiredValidator.REQUIRED_detail}", groups = ConfirmGroup.class)
    private String receiverPrefecture;

    /** 市区郡 */
    @NotEmpty(groups = ConfirmGroup.class)
    @Length(min = 1, max = 50, groups = ConfirmGroup.class)
    @Pattern(regexp = RegularExpressionsConstants.ZENKAKU_REGEX, message = "{HZenkakuValidator.INVALID_detail}",
             groups = ConfirmGroup.class)
    @HCZenkaku
    private String receiverAddress1;

    /** 町村 */
    @NotEmpty(groups = ConfirmGroup.class)
    @Length(min = 1, max = 100, groups = ConfirmGroup.class)
    @Pattern(regexp = RegularExpressionsConstants.ZENKAKU_REGEX, message = "{HZenkakuValidator.INVALID_detail}",
             groups = ConfirmGroup.class)
    @HCZenkaku
    private String receiverAddress2;

    /** 番地・マンション・建物名 */
    @Length(min = 0, max = 200, groups = ConfirmGroup.class)
    @Pattern(regexp = RegularExpressionsConstants.ZENKAKU_REGEX, message = "{HZenkakuValidator.INVALID_detail}",
             groups = ConfirmGroup.class)
    @HCZenkaku
    public String receiverAddress3;

    /** 電話番号 */
    @NotEmpty(groups = ConfirmGroup.class)
    @Pattern(regexp = RegularExpressionsConstants.TELEPHONE_NUMBER_REGEX,
             message = "{HTelephoneNumberValidator.INVALID_detail}", groups = ConfirmGroup.class)
    @Length(min = 1, max = 11, groups = ConfirmGroup.class)
    @HCHankaku
    public String receiverTel;

    /** 郵便番号Ajax 返却メッセージ */
    private String receiverZipErrorMsg;

    /************************************
     ** 出荷情報
     ************************************/

    /** 出荷状態（必須） */
    private HTypeShipmentStatus shipmentStatus;

    /** 出荷日 */
    private Timestamp shipmentDate;

    /** 伝票番号 */
    @Pattern(regexp = ValidatorConstants.REGEX_DELIVERY_CODE, message = ValidatorConstants.MSGCD_REGEX_DELIVERY_CODE,
             groups = ConfirmGroup.class)
    @Length(min = 0, max = 40, groups = ConfirmGroup.class)
    @HCHankaku
    private String deliveryCode;

    /** 配送方法SEQ（必須） */
    private Integer deliveryMethodSeq;

    /** 配送方法名 */
    private String deliveryMethodName;

    /** お届け希望日指定フラグ **/
    private HTypeReceiverDateDesignationFlag receiverDateDesignationFlag = HTypeReceiverDateDesignationFlag.ON;

    /** お届け希望日 **/
    private Timestamp receiverDate;

    /** お届け時間帯 */
    private String receiverTimeZone;

    /** 配送方法備考 */
    @HVBothSideSpace(startWith = SpaceValidateMode.ALLOW_SPACE, endWith = SpaceValidateMode.ALLOW_SPACE,
                     groups = ConfirmGroup.class)
    @HVSpecialCharacter(allowPunctuation = true, groups = ConfirmGroup.class)
    @Length(min = 0, max = 400, message = "{HTextAreaValidator.LENGTH_detail}", groups = ConfirmGroup.class)
    private String deliveryNote;

    /** 配送別個別配送フラグ */
    private HTypeIndividualDeliveryType receiveIndividualDeliveryType = HTypeIndividualDeliveryType.OFF;

    /** 配送方法SEQ（入力） */
    @NotEmpty(message = "{HRequiredValidator.REQUIRED_detail}", groups = ConfirmGroup.class)
    private String updateDeliveryMethodSeq;

    /** 配送方法SEQアイテムリスト */
    private Map<Object, Object> updateDeliveryMethodSeqItems;

    /** お届け希望日（入力） */
    @HVDate(groups = ConfirmGroup.class)
    @HCDate
    private String updateReceiverDate;

    /** お届け時間帯アイテムリスト */
    private Map<String, String> receiverTimeZoneItems;

    /** 配送方法お届け時間帯存在チェック用リスト */
    private Map<Object, Object> deliveryTimeZoneList;

    /**
     * 配送メモ表示用<br/>
     * リカバリー時には配送メモは表示項目となる。<br/>
     * 配送メモには改行が入ることがあるので、@HCPreを付与したいが、<br/>
     * 既存のdeliveryNoteへ付与すると入力データ内の改行が<br/>
     * に変換されてしまう。<br/>
     * この問題を回避する目的で、表示専用フィールドとして追加した。<br/>
     */
    private String readOnlyDeliveryNote;

    /************************************
     ** 受注商品情報
     ************************************/

    /** 商品リスト */
    @Valid
    private List<OrderGoodsUpdateItem> orderGoodsUpdateItems;

    /**
     * お届け希望日の選択値判定<br/>
     * 注文時のお届け希望日指定ありか否か<br/>
     *
     * @return true..未指定 / false..指定
     */
    public boolean isReceiverDateNoSelected() {
        return receiverDate == null;
    }

}