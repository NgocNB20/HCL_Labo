package jp.co.itechh.quad.admin.pc.web.admin.shop.delivery.regisupdate;

import jp.co.itechh.quad.admin.annotation.converter.HCHankaku;
import jp.co.itechh.quad.admin.annotation.converter.HCNumber;
import jp.co.itechh.quad.admin.annotation.validator.HVBothSideSpace;
import jp.co.itechh.quad.admin.annotation.validator.HVItems;
import jp.co.itechh.quad.admin.annotation.validator.HVNumber;
import jp.co.itechh.quad.admin.annotation.validator.HVRRequiredAllOrNothing;
import jp.co.itechh.quad.admin.annotation.validator.HVSpecialCharacter;
import jp.co.itechh.quad.admin.base.util.seasar.StringUtil;
import jp.co.itechh.quad.admin.constant.type.HTypeDeliveryMethodType;
import jp.co.itechh.quad.admin.constant.type.HTypeDispDeliveryMethodType;
import jp.co.itechh.quad.admin.constant.type.HTypeOpenDeleteStatus;
import jp.co.itechh.quad.admin.constant.type.SpaceValidateMode;
import jp.co.itechh.quad.admin.dto.shop.delivery.DeliveryMethodDetailsDto;
import jp.co.itechh.quad.admin.entity.shop.delivery.DeliveryMethodEntity;
import jp.co.itechh.quad.admin.entity.shop.delivery.DeliveryMethodTypeCarriageEntity;
import jp.co.itechh.quad.admin.pc.web.admin.common.validation.group.ConfirmGroup;
import jp.co.itechh.quad.admin.web.AbstractModel;
import lombok.Data;
import lombok.EqualsAndHashCode;
import org.hibernate.validator.constraints.Length;
import org.hibernate.validator.constraints.URL;

import javax.validation.constraints.Digits;
import javax.validation.constraints.NotEmpty;
import java.util.List;
import java.util.Map;

@EqualsAndHashCode(callSuper = false)
@Data
@HVRRequiredAllOrNothing(fields = {"deliveryChaseURL", "deliveryChaseURLDisplayPeriod"}, groups = {ConfirmGroup.class})
public class DeliveryRegistUpdateModel extends AbstractModel {

    /** メッセージコード：公開状態モバイルが「削除」になってて公開状態PCが「削除」になってない。*/
    protected static final String MSGCD_OPEN_STATUS_MB_DELETE = "AYD000202W";

    /** メッセージコード：公開状態PCが「削除」になってて公開状態モバイルが「削除」になってない。*/
    protected static final String MSGCD_OPEN_STATUS_PC_DELETE = "AYD000203W";

    /** 配送方法SEQ。URLクエリーから。 */
    private String dmcd;

    /** 配送方法SEQ(画面用) */
    private Integer scDeliveryMethodSeq;

    /** 不正操作を判断するためのフラグ */
    private boolean normality;

    /** 修正フラグ */
    private boolean editFlag;

    /** 配送方法詳細DTO */
    private DeliveryMethodDetailsDto deliveryMethodDetailsDto;

    /** 配送方法エンティティ（入力情報を格納。確認画面へ引き継ぐ）*/
    private DeliveryMethodEntity deliveryMethodEntity;

    /** 配送区分別送料エンティティリスト（入力情報を格納。確認画面へ引き継ぐ）*/
    private List<DeliveryMethodTypeCarriageEntity> deliveryMethodTypeCarriageEntityList;

    /** 配送SEQ(ID) */
    private String deliveryMethodId;

    /** 配送名称 */
    @HVBothSideSpace(startWith = SpaceValidateMode.ALLOW_SPACE, endWith = SpaceValidateMode.ALLOW_SPACE,
                     groups = {ConfirmGroup.class})
    @HVSpecialCharacter(allowPunctuation = true, groups = {ConfirmGroup.class})
    @NotEmpty(groups = {ConfirmGroup.class})
    @Length(min = 1, max = 60, groups = {ConfirmGroup.class})
    private String deliveryMethodName;

    /** 配送方法種別リスト */
    private Map<String, String> deliveryMethodTypeItems;

    /** 配送方法種別 */
    @NotEmpty(message = "{HRequiredValidator.REQUIRED_detail}", groups = {ConfirmGroup.class})
    @HVItems(target = HTypeDispDeliveryMethodType.class, groups = {ConfirmGroup.class})
    private String deliveryMethodType;

    /** 配送方法種別名 */
    private String deliveryMethodTypeName;

    /** 公開状態PCリスト */
    private Map<String, String> openStatusPCItems;

    /** 公開状態PC */
    @NotEmpty(message = "{HRequiredValidator.REQUIRED_detail}", groups = {ConfirmGroup.class})
    @HVItems(target = HTypeOpenDeleteStatus.class, groups = {ConfirmGroup.class})
    private String openStatusPC;

    /** 公開状態MBリスト */
    //    @PageScope
    //    @HUItemList(component = "valuetype.openDeleteStatus")
    private Map<String, String> openStatusMBItems;

    /** 配送方法表示名PC */
    private String deliveryMethodDisplayNamePC;

    /** 配送方法説明文PC */
    @HVBothSideSpace(startWith = SpaceValidateMode.ALLOW_SPACE, endWith = SpaceValidateMode.ALLOW_SPACE,
                     groups = {ConfirmGroup.class})
    @HVSpecialCharacter(allowPunctuation = true, groups = {ConfirmGroup.class})
    @Length(min = 0, max = 2000, message = "{HTextAreaValidator.LENGTH_detail}", groups = {ConfirmGroup.class})
    private String deliveryNotePC;

    /** リードタイム */
    @HVNumber(groups = {ConfirmGroup.class})
    @Length(min = 0, max = 3, groups = {ConfirmGroup.class})
    @Digits(integer = 3, fraction = 0, groups = {ConfirmGroup.class})
    private String deliveryLeadTime;

    /** 選択可能日数 */
    @HVNumber(groups = {ConfirmGroup.class})
    @Length(min = 0, max = 3, groups = {ConfirmGroup.class})
    @Digits(integer = 3, fraction = 0, groups = {ConfirmGroup.class})
    private String possibleSelectDays;

    /** 配送追跡URL */
    @HVSpecialCharacter(
                    allowCharacters = {'!', '"', '#', '$', '%', '&', '\'', '(', ')', '*', '+', ',', '-', '.', '/', ':',
                                    ';', '<', '=', '>', '?', '@', '[', '\\', ']', '^', '_', '`', '{', '|', '}', '~'},
                    groups = {ConfirmGroup.class})
    @URL(groups = {ConfirmGroup.class})
    @Length(min = 0, max = 200, groups = {ConfirmGroup.class})
    @HCHankaku
    private String deliveryChaseURL;

    /** 配送追跡URL表示期間 */
    @HVNumber(groups = {ConfirmGroup.class})
    @Length(min = 0, max = 3, groups = {ConfirmGroup.class})
    @Digits(integer = 3, fraction = 0, groups = {ConfirmGroup.class})
    private String deliveryChaseURLDisplayPeriod;

    /** お届け時間帯1 */
    @HVBothSideSpace(startWith = SpaceValidateMode.ALLOW_SPACE, endWith = SpaceValidateMode.ALLOW_SPACE,
                     groups = {ConfirmGroup.class})
    @HVSpecialCharacter(allowPunctuation = true, groups = {ConfirmGroup.class})
    @Length(min = 0, max = 100, groups = {ConfirmGroup.class})
    private String receiverTimeZone1;

    /** お届け時間帯2 */
    @HVBothSideSpace(startWith = SpaceValidateMode.ALLOW_SPACE, endWith = SpaceValidateMode.ALLOW_SPACE,
                     groups = {ConfirmGroup.class})
    @HVSpecialCharacter(allowPunctuation = true, groups = {ConfirmGroup.class})
    @Length(min = 0, max = 100, groups = {ConfirmGroup.class})
    private String receiverTimeZone2;

    /** お届け時間帯3 */
    @HVBothSideSpace(startWith = SpaceValidateMode.ALLOW_SPACE, endWith = SpaceValidateMode.ALLOW_SPACE,
                     groups = {ConfirmGroup.class})
    @HVSpecialCharacter(allowPunctuation = true, groups = {ConfirmGroup.class})
    @Length(min = 0, max = 100, groups = {ConfirmGroup.class})
    private String receiverTimeZone3;

    /** お届け時間帯4 */
    @HVBothSideSpace(startWith = SpaceValidateMode.ALLOW_SPACE, endWith = SpaceValidateMode.ALLOW_SPACE,
                     groups = {ConfirmGroup.class})
    @HVSpecialCharacter(allowPunctuation = true, groups = {ConfirmGroup.class})
    @Length(min = 0, max = 100, groups = {ConfirmGroup.class})
    private String receiverTimeZone4;

    /** お届け時間帯5 */
    @HVBothSideSpace(startWith = SpaceValidateMode.ALLOW_SPACE, endWith = SpaceValidateMode.ALLOW_SPACE,
                     groups = {ConfirmGroup.class})
    @HVSpecialCharacter(allowPunctuation = true, groups = {ConfirmGroup.class})
    @Length(min = 0, max = 100, groups = {ConfirmGroup.class})
    private String receiverTimeZone5;

    /** お届け時間帯6 */
    @HVBothSideSpace(startWith = SpaceValidateMode.ALLOW_SPACE, endWith = SpaceValidateMode.ALLOW_SPACE,
                     groups = {ConfirmGroup.class})
    @HVSpecialCharacter(allowPunctuation = true, groups = {ConfirmGroup.class})
    @Length(min = 0, max = 100, groups = {ConfirmGroup.class})
    private String receiverTimeZone6;

    /** お届け時間帯7 */
    @HVBothSideSpace(startWith = SpaceValidateMode.ALLOW_SPACE, endWith = SpaceValidateMode.ALLOW_SPACE,
                     groups = {ConfirmGroup.class})
    @HVSpecialCharacter(allowPunctuation = true, groups = {ConfirmGroup.class})
    @Length(min = 0, max = 100, groups = {ConfirmGroup.class})
    private String receiverTimeZone7;

    /** お届け時間帯8 */
    @HVBothSideSpace(startWith = SpaceValidateMode.ALLOW_SPACE, endWith = SpaceValidateMode.ALLOW_SPACE,
                     groups = {ConfirmGroup.class})
    @HVSpecialCharacter(allowPunctuation = true, groups = {ConfirmGroup.class})
    @Length(min = 0, max = 100, groups = {ConfirmGroup.class})
    private String receiverTimeZone8;

    /** お届け時間帯9 */
    @HVBothSideSpace(startWith = SpaceValidateMode.ALLOW_SPACE, endWith = SpaceValidateMode.ALLOW_SPACE,
                     groups = {ConfirmGroup.class})
    @HVSpecialCharacter(allowPunctuation = true, groups = {ConfirmGroup.class})
    @Length(min = 0, max = 100, groups = {ConfirmGroup.class})
    private String receiverTimeZone9;

    /** お届け時間帯10 */
    @HVBothSideSpace(startWith = SpaceValidateMode.ALLOW_SPACE, endWith = SpaceValidateMode.ALLOW_SPACE,
                     groups = {ConfirmGroup.class})
    @HVSpecialCharacter(allowPunctuation = true, groups = {ConfirmGroup.class})
    @Length(min = 0, max = 100, groups = {ConfirmGroup.class})
    private String receiverTimeZone10;

    /** 一律送料 */
    @HCNumber
    @HVNumber(groups = {ConfirmGroup.class})
    @Length(min = 0, max = 8, groups = {ConfirmGroup.class})
    @Digits(integer = 8, fraction = 0, groups = {ConfirmGroup.class})
    private String equalsCarriage;

    /** 高額割引下限金額（動的バリデータ） */
    @HCNumber
    private String largeAmountDiscountPrice;

    /** 高額割引送料（動的バリデータ） */
    @HCNumber
    private String largeAmountDiscountCarriage;

    /** 不足金額表示 */
    private boolean shortfallDisplayFlag;

    // 都道府県別送料 <<
    /**
     * 都道府県別送料アイテムリスト<br/>
     */
    private List<DeliveryPrefectureCarriageItem> deliveryPrefectureCarriageItems;

    /** 都道府県別送料の比較用リスト */
    private List<DeliveryPrefectureCarriageItem> deliveryPrefectureCarriageList;

    // 金額別送料 <<
    /**
     * 金額別送料アイテムリスト<br/>
     */
    private List<DeliveryAmountCarriageItem> deliveryAmountCarriageItems;

    /** 配送特別料金エリア件数 */
    private int deliverySpecialChargeAreaCount;

    /** 配送不可能エリア件数 */
    private int deliveryImpossibleAreaCount;

    /** 修正されている項目名を保持するリスト */
    private List<String> modifiedList;

    /** 修正されている項目名を保持するリスト（都道府県別） */
    private List<List<String>> modifiedPrefectureList;

    /**
     * コンディション<br/>
     * 登録モードかどうか
     *
     * @return true..登録モード / false..更新モード
     */
    public boolean isRegistMode() {
        return deliveryMethodDetailsDto.getDeliveryMethodEntity() == null;
    }

    /**
     * コンディション<br/>
     * 「配送種別」が選択されているかどうか
     *
     * @return true..選択されている / false..選択されていない
     */
    public boolean isSelectedType() {
        return !StringUtil.isEmpty(deliveryMethodType);
    }

    /**
     * コンディション<br/>
     * 選択された配送種別が「都道府県別」かどうか
     *
     * @return true..都道府県別 / false..都道府県別でない
     */
    public boolean isPrefectureType() {
        if (!isSelectedType()) {
            return false;
        }
        return deliveryMethodType.equals(HTypeDeliveryMethodType.PREFECTURE.getValue());
    }

    /**
     * コンディション<br/>
     * 選択された配送種別が「全国一律」かどうか
     *
     * @return true..全国一律 / false..全国一律でない
     */
    public boolean isFlatType() {
        if (!isSelectedType()) {
            return false;
        }
        return deliveryMethodType.equals(HTypeDeliveryMethodType.FLAT.getValue());
    }

    /**
     * コンディション<br/>
     * 選択された配送種別が「金額別」かどうか
     *
     * @return true..金額別 / false..金額別でない
     */
    public boolean isAmountType() {
        if (!isSelectedType()) {
            return false;
        }
        return deliveryMethodType.equals(HTypeDeliveryMethodType.AMOUNT.getValue());
    }

    /**
     * コンディション<br/>
     * 選択可能日数が設定されているかどうか
     *
     * @return true..されている / false..されていない
     */
    public boolean isExistPossibleSelectDays() {
        return !StringUtil.isEmpty(this.possibleSelectDays);
    }

    /**
     * コンディション<br/>
     * リードタイムが設定されているかどうか
     *
     * @return true..されている / false..されていない
     */
    public boolean isExistDeliveryLeadTime() {
        return !StringUtil.isEmpty(this.deliveryLeadTime);
    }

    /**
     * コンディション<br/>
     * 高額割引情報が設定されているかどうか
     *
     * @return true..されている / false..されていない
     */
    public boolean isExistLargeAmountDiscount() {
        if (largeAmountDiscountPrice == null || largeAmountDiscountPrice.equals("0")) {
            return false;
        }

        return true;
    }
}