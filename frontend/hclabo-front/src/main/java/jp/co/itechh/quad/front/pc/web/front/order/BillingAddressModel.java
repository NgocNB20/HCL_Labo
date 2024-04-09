package jp.co.itechh.quad.front.pc.web.front.order;

import jp.co.itechh.quad.front.annotation.converter.HCHankaku;
import jp.co.itechh.quad.front.annotation.converter.HCZenkaku;
import jp.co.itechh.quad.front.annotation.converter.HCZenkakuKana;
import jp.co.itechh.quad.front.annotation.validator.HVBothSideSpace;
import jp.co.itechh.quad.front.annotation.validator.HVRItems;
import jp.co.itechh.quad.front.annotation.validator.HVRZipCode;
import jp.co.itechh.quad.front.annotation.validator.HVSpecialCharacter;
import jp.co.itechh.quad.front.annotation.validator.HVWindows31J;
import jp.co.itechh.quad.front.base.constant.RegularExpressionsConstants;
import jp.co.itechh.quad.front.base.util.common.PropertiesUtil;
import jp.co.itechh.quad.front.base.utility.ApplicationContextUtility;
import jp.co.itechh.quad.front.constant.type.SpaceValidateMode;
import jp.co.itechh.quad.front.entity.memberinfo.MemberInfoEntity;
import jp.co.itechh.quad.front.pc.web.front.order.common.AddressBookModel;
import jp.co.itechh.quad.front.pc.web.front.order.validation.group.BillingAddressIdGroup;
import jp.co.itechh.quad.front.pc.web.front.order.validation.group.BillingGroup;
import jp.co.itechh.quad.front.utility.CommonInfoUtility;
import jp.co.itechh.quad.front.web.AbstractModel;
import lombok.Data;
import org.hibernate.validator.constraints.Length;

import javax.validation.constraints.NotEmpty;
import javax.validation.constraints.Pattern;
import java.util.List;
import java.util.Map;

/**
 * 請求先住所Model
 *
 * @author Pham Quang Dieu
 */
@Data
@HVRZipCode(targetLeft = "billingAddressZipCode1", targetRight = "billingAddressZipCode2",
            groups = {BillingGroup.class})
@HVRItems(target = "billingAddressPrefecture", comparison = "billingAddressPrefectureItems",
          groups = {BillingGroup.class})
public class BillingAddressModel extends AbstractModel {

    /** 会員情報エンティティ **/
    private MemberInfoEntity memberInfoEntity;

    @NotEmpty(groups = {BillingGroup.class})
    @Pattern(regexp = RegularExpressionsConstants.ZENKAKU_REGEX, message = "{HZenkakuValidator.INVALID_detail}",
             groups = {BillingGroup.class})
    @Length(min = 1, max = 20, groups = {BillingGroup.class})
    @HCZenkaku
    private String addressName;

    /** 氏名（姓） **/
    @NotEmpty(groups = {BillingGroup.class})
    @HVWindows31J(checkJISX0208 = true, groups = {BillingGroup.class})
    @Pattern(regexp = RegularExpressionsConstants.ZENKAKU_REGEX, message = "{HZenkakuValidator.INVALID_detail}",
             groups = {BillingGroup.class})
    @Length(min = 1, max = 16, groups = {BillingGroup.class})
    @HCZenkaku
    private String billingAddressLastName;

    /** 氏名（名） **/
    @NotEmpty(groups = {BillingGroup.class})
    @HVWindows31J(checkJISX0208 = true, groups = {BillingGroup.class})
    @Pattern(regexp = RegularExpressionsConstants.ZENKAKU_REGEX, message = "{HZenkakuValidator.INVALID_detail}",
             groups = {BillingGroup.class})
    @Length(min = 1, max = 16, groups = {BillingGroup.class})
    @HCZenkaku
    private String billingAddressFirstName;

    /** フリガナ（セイ） **/
    @NotEmpty(groups = {BillingGroup.class})
    @Pattern(regexp = RegularExpressionsConstants.ZENKAKU_KANA_REGEX,
             message = "{HZenkakuKanaValidator.INVALID_detail}", groups = {BillingGroup.class})
    @Length(min = 1, max = 40, groups = {BillingGroup.class})
    @HCZenkakuKana
    private String billingAddressLastKana;

    /** フリガナ（メイ） **/
    @NotEmpty(groups = {BillingGroup.class})
    @Pattern(regexp = RegularExpressionsConstants.ZENKAKU_KANA_REGEX,
             message = "{HZenkakuKanaValidator.INVALID_detail}", groups = {BillingGroup.class})
    @Length(min = 1, max = 40, groups = {BillingGroup.class})
    @HCZenkakuKana
    private String billingAddressFirstKana;

    /** 電話番号 **/
    @NotEmpty(groups = {BillingGroup.class})
    @Pattern(regexp = RegularExpressionsConstants.TELEPHONE_NUMBER_REGEX,
             message = "{HTelephoneNumberValidator.INVALID_detail}", groups = {BillingGroup.class})
    @Length(min = 1, max = 11, groups = {BillingGroup.class})
    @HCHankaku
    private String billingAddressTel;

    /** 郵便番号（左側） **/
    @NotEmpty(groups = {BillingGroup.class})
    @Length(min = 1, max = 3, groups = {BillingGroup.class})
    @HCHankaku
    private String billingAddressZipCode1;

    /** 郵便番号（右側） **/
    @NotEmpty(groups = {BillingGroup.class})
    @Length(min = 1, max = 4, groups = {BillingGroup.class})
    @HCHankaku
    private String billingAddressZipCode2;

    /** 都道府県 **/
    @NotEmpty(groups = {BillingGroup.class})
    private String billingAddressPrefecture;

    /** 配送メモ */
    @HVBothSideSpace(startWith = SpaceValidateMode.ALLOW_SPACE, endWith = SpaceValidateMode.ALLOW_SPACE,
                     groups = BillingGroup.class)
    @HVSpecialCharacter(allowPunctuation = true, groups = BillingGroup.class)
    @Length(min = 0, max = 400, message = "{HTextAreaValidator.LENGTH_detail}", groups = BillingGroup.class)
    private String shippingMemo;

    /** 都道府県プルダウン用リスト **/
    private Map<String, String> billingAddressPrefectureItems;

    /** 市区郡 **/
    @NotEmpty(groups = {BillingGroup.class})
    @Pattern(regexp = RegularExpressionsConstants.ZENKAKU_REGEX, message = "{HZenkakuValidator.INVALID_detail}",
             groups = {BillingGroup.class})
    @Length(min = 1, max = 50, groups = {BillingGroup.class})
    @HCZenkaku
    private String billingAddress1;

    /** 町村名 **/
    @NotEmpty(groups = {BillingGroup.class})
    @Pattern(regexp = RegularExpressionsConstants.ZENKAKU_REGEX, message = "{HZenkakuValidator.INVALID_detail}",
             groups = {BillingGroup.class})
    @Length(min = 1, max = 100, groups = {BillingGroup.class})
    @HCZenkaku
    private String billingAddress2;

    /** 番地・建物名 **/
    @Pattern(regexp = RegularExpressionsConstants.ZENKAKU_REGEX, message = "{HZenkakuValidator.INVALID_detail}",
             groups = {BillingGroup.class})
    @Length(min = 0, max = 200, groups = {BillingGroup.class})
    @HCZenkaku
    private String billingAddress3;

    @Pattern(regexp = RegularExpressionsConstants.ZENKAKU_REGEX, message = "{HZenkakuValidator.INVALID_detail}",
             groups = {BillingGroup.class})
    @Length(min = 0, max = 200, groups = {BillingGroup.class})
    @HCZenkaku
    private String deliveryMemo;

    /** 住所録情報リスト **/
    private List<AddressBookModel> addressBookModelList;

    /** 請求先住所ID */
    @NotEmpty(message = "{HRequiredValidator.REQUIRED_detail}", groups = {BillingAddressIdGroup.class})
    private String billingAddressId;

    /** ご注文内容確認画面からの遷移かを判断するフラグ **/
    private boolean fromConfirm;

    /**
     * ログインしてるかどうかを判定します。<br />
     * 画面表示のConditionとして使用。<br />
     * @return true..ログインしてる。false..ログインしてない。
     */
    public boolean isLogin() {
        // 共通情報Helper取得
        CommonInfoUtility commonInfoUtility = ApplicationContextUtility.getBean(CommonInfoUtility.class);
        return commonInfoUtility.isLogin(getCommonInfo());
    }

    /**
     * 請求先住所が最大件数に達しているか
     *
     * @return true..達している。false..達していない
     */
    public boolean isAddressBookFull() {
        // システム値取得
        int addressBookMaxCount = PropertiesUtil.getSystemPropertiesValueToInt("addressbook.max");
        if (addressBookModelList != null) {
            int totalCount = addressBookModelList.size();
            if (totalCount >= addressBookMaxCount) {
                return true;
            }
        }
        return false;
    }

}
