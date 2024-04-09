/*
 * Copyright (C) 2022 i-TEC HANKYU HANSHIN INC. All Rights Reserved.
 *
 */

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
import jp.co.itechh.quad.front.pc.web.front.order.common.AddressBookModel;
import jp.co.itechh.quad.front.pc.web.front.order.validation.group.AddAddressBookGroup;
import jp.co.itechh.quad.front.pc.web.front.order.validation.group.AddressGroup;
import jp.co.itechh.quad.front.pc.web.front.order.validation.group.ShippingAddressIdGroup;
import jp.co.itechh.quad.front.utility.CommonInfoUtility;
import jp.co.itechh.quad.front.web.AbstractModel;
import lombok.Data;
import org.hibernate.validator.constraints.Length;

import javax.validation.constraints.NotEmpty;
import javax.validation.constraints.Pattern;
import java.util.List;
import java.util.Map;

/**
 * お届け先住所画面 Model
 *
 * @author Pham Quang Dieu
 */

@Data
@HVRItems(target = "addressAddressBook", comparison = "addressAddressBookItems", groups = {AddressGroup.class})
@HVRZipCode(targetLeft = "addressZipCode1", targetRight = "addressZipCode2", groups = {AddressGroup.class})
@HVRItems(target = "addressPrefecture", comparison = "addressPrefectureItems", groups = {AddressGroup.class})
public class AddressSelectModel extends AbstractModel {

    @NotEmpty(groups = {AddressGroup.class})
    @Pattern(regexp = RegularExpressionsConstants.ZENKAKU_REGEX, message = "{HZenkakuValidator.INVALID_detail}",
             groups = {AddressGroup.class})
    @Length(min = 1, max = 20, groups = {AddressGroup.class})
    @HCZenkaku
    private String addressName;

    @NotEmpty(groups = {AddressGroup.class})
    @HVWindows31J(checkJISX0208 = true, groups = {AddressGroup.class})
    @Pattern(regexp = RegularExpressionsConstants.ZENKAKU_REGEX, message = "{HZenkakuValidator.INVALID_detail}",
             groups = {AddressGroup.class})
    @Length(min = 1, max = 16, groups = {AddressGroup.class})
    @HCZenkaku
    private String addressLastName;

    @NotEmpty(groups = {AddressGroup.class}) //
    @HVWindows31J(checkJISX0208 = true, groups = {AddressGroup.class})
    @Pattern(regexp = RegularExpressionsConstants.ZENKAKU_REGEX, message = "{HZenkakuValidator.INVALID_detail}",
             groups = {AddressGroup.class})
    @Length(min = 1, max = 16, groups = {AddressGroup.class})
    @HCZenkaku
    private String addressFirstName;

    @NotEmpty(groups = {AddressGroup.class}) //
    @Pattern(regexp = RegularExpressionsConstants.ZENKAKU_KANA_REGEX,
             message = "{HZenkakuKanaValidator.INVALID_detail}", groups = {AddressGroup.class})
    @Length(min = 1, max = 40, groups = {AddressGroup.class})
    @HCZenkakuKana
    private String addressLastKana;

    @NotEmpty(groups = {AddressGroup.class}) //
    @Pattern(regexp = RegularExpressionsConstants.ZENKAKU_KANA_REGEX,
             message = "{HZenkakuKanaValidator.INVALID_detail}", groups = {AddressGroup.class})
    @Length(min = 1, max = 40, groups = {AddressGroup.class})
    @HCZenkakuKana
    private String addressFirstKana;

    @NotEmpty(groups = {AddressGroup.class})
    @Pattern(regexp = RegularExpressionsConstants.TELEPHONE_NUMBER_REGEX,
             message = "{HTelephoneNumberValidator.INVALID_detail}", groups = {AddressGroup.class})
    @Length(min = 1, max = 11, groups = {AddressGroup.class})
    @HCHankaku
    private String addressTel;

    @NotEmpty(groups = {AddressGroup.class})
    @Length(min = 1, max = 3, groups = {AddressGroup.class})
    @HCHankaku
    private String addressZipCode1;

    @NotEmpty(groups = {AddressGroup.class})
    @Length(min = 1, max = 4, groups = {AddressGroup.class})
    @HCHankaku
    private String addressZipCode2;

    /** 都道府県プルダウン用リスト */
    private Map<String, String> addressPrefectureItems;

    @NotEmpty(groups = {AddressGroup.class})
    private String addressPrefecture;

    @NotEmpty(groups = {AddressGroup.class})
    @Pattern(regexp = RegularExpressionsConstants.ZENKAKU_REGEX, message = "{HZenkakuValidator.INVALID_detail}",
             groups = {AddressGroup.class})
    @Length(min = 1, max = 50, groups = {AddressGroup.class})
    private String addressAddress1;

    @NotEmpty(groups = {AddressGroup.class})
    @Pattern(regexp = RegularExpressionsConstants.ZENKAKU_REGEX, message = "{HZenkakuValidator.INVALID_detail}",
             groups = {AddressGroup.class})
    @Length(min = 1, max = 100, groups = {AddressGroup.class})
    @HCZenkaku
    private String addressAddress2;

    @Pattern(regexp = RegularExpressionsConstants.ZENKAKU_REGEX, message = "{HZenkakuValidator.INVALID_detail}",
             groups = {AddressGroup.class})
    @Length(min = 0, max = 200, groups = {AddressGroup.class})
    @HCZenkaku
    private String addressAddress3;

    /** 配送メモ */
    @HVBothSideSpace(startWith = SpaceValidateMode.ALLOW_SPACE, endWith = SpaceValidateMode.ALLOW_SPACE,
                     groups = AddressGroup.class)
    @HVSpecialCharacter(allowPunctuation = true, groups = AddressGroup.class)
    @Length(min = 0, max = 400, message = "{HTextAreaValidator.LENGTH_detail}", groups = AddressGroup.class)
    private String shippingMemo;

    /** アドレス帳プルダウン用リスト */
    private Map<String, String> addressAddressBookItems;

    /** 「このお届け先を住所に登録する」ラジオボタン(判定用) */
    private boolean addressRegistFlg;

    /** アドレス帳プルダウン */
    @NotEmpty(message = "{HRequiredValidator.REQUIRED_detail}", groups = {AddAddressBookGroup.class})
    private String addressAddressBook;

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
     * Method to check whether the address book max limit exceeded<br/>
     * @return true: if address book is full false: if address book is not full
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

    /** 住所録情報リスト **/
    private List<AddressBookModel> addressBookModelList;

    /** 配送先住所ID */
    @NotEmpty(message = "{HRequiredValidator.REQUIRED_detail}", groups = {ShippingAddressIdGroup.class})
    private String shippingAddressId;

}
