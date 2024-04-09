/*
 * Project Name : HIT-MALL4
 *
 * Copyright (C) 2021 i-TEC HANKYU HANSHIN INC. All Rights Reserved.
 *
 */

package jp.co.itechh.quad.front.pc.web.front.member.abook;

import jp.co.itechh.quad.front.annotation.converter.HCHankaku;
import jp.co.itechh.quad.front.annotation.converter.HCZenkaku;
import jp.co.itechh.quad.front.annotation.converter.HCZenkakuKana;
import jp.co.itechh.quad.front.annotation.validator.HVBothSideSpace;
import jp.co.itechh.quad.front.annotation.validator.HVRZipCode;
import jp.co.itechh.quad.front.annotation.validator.HVSpecialCharacter;
import jp.co.itechh.quad.front.annotation.validator.HVWindows31J;
import jp.co.itechh.quad.front.base.constant.RegularExpressionsConstants;
import jp.co.itechh.quad.front.base.util.common.PropertiesUtil;
import jp.co.itechh.quad.front.constant.type.SpaceValidateMode;
import jp.co.itechh.quad.front.web.AbstractModel;
import lombok.Data;
import org.hibernate.validator.constraints.Length;

import javax.validation.constraints.NotEmpty;
import javax.validation.constraints.Pattern;
import java.util.List;
import java.util.Map;

/**
 * アドレス帳画面 Model
 *
 * @author kn23834
 * @version $Revision: 1.0 $
 *
 */
@Data
@HVRZipCode(targetLeft = "zipCode1", targetRight = "zipCode2")
public class MemberAbookModel extends AbstractModel {

    /** アドレス帳一覧情報 */
    private List<MemberAbookModelItem> addressBookItems;

    /** アドレス帳一覧インデックス */
    private int addressBookIndex;

    /** アドレス帳一覧：ページ番号 */
    private String pnum;

    /** アドレス帳一覧：最大表示件数 */
    private int limit;

    /** 総件数 */
    private int totalCount;

    /** アドレス帳SEQ（URLパラメタ） */
    private Integer abid;

    /** アドレス帳SEQ */
    private String addressBookSeq;

    /** 会員SEQ */
    private Integer memberInfoSeq;

    /** アドレス帳名称 */
    @NotEmpty
    @Length(max = 20)
    private String addressBookName;

    /** 氏名（姓） */
    @NotEmpty
    @HVWindows31J(checkJISX0208 = true)
    @Pattern(regexp = RegularExpressionsConstants.ZENKAKU_REGEX, message = "{HZenkakuValidator.INVALID_detail}")
    @Length(min = 1, max = 16)
    @HCZenkaku
    private String lastName;

    /** 氏名（名） */
    @NotEmpty
    @HVWindows31J(checkJISX0208 = true)
    @Pattern(regexp = RegularExpressionsConstants.ZENKAKU_REGEX, message = "{HZenkakuValidator.INVALID_detail}")
    @Length(min = 1, max = 16)
    @HCZenkaku
    private String firstName;

    /** フリガナ（セイ） */
    @NotEmpty
    @Pattern(regexp = RegularExpressionsConstants.ZENKAKU_KANA_REGEX,
             message = "{HZenkakuKanaValidator.INVALID_detail}")
    @Length(min = 1, max = 40)
    @HCZenkakuKana
    private String lastKana;

    /** フリガナ（メイ） */
    @NotEmpty
    @Pattern(regexp = RegularExpressionsConstants.ZENKAKU_KANA_REGEX,
             message = "{HZenkakuKanaValidator.INVALID_detail}")
    @Length(min = 1, max = 40)
    @HCZenkakuKana
    private String firstKana;

    /** 電話番号 */
    @NotEmpty
    @Pattern(regexp = RegularExpressionsConstants.TELEPHONE_NUMBER_REGEX,
             message = "{HTelephoneNumberValidator.INVALID_detail}")
    @Length(min = 1, max = 11)
    @HCHankaku
    private String tel;

    /** 郵便番号（前） */
    @NotEmpty
    @Length(min = 1, max = 3)
    @HCHankaku
    private String zipCode1;

    /** 郵便番号（後） */
    @NotEmpty
    @Length(min = 1, max = 4)
    @HCHankaku
    private String zipCode2;

    /** 都道府県 */
    @NotEmpty(message = "{HRequiredValidator.REQUIRED_detail}")
    @Length(min = 1, max = 4)
    private String prefecture;

    /** 都道府県アイテム */
    private Map<String, String> prefectureItems;

    /** 住所１、市区郡 */
    @NotEmpty
    @Pattern(regexp = RegularExpressionsConstants.ZENKAKU_REGEX, message = "{HZenkakuValidator.INVALID_detail}")
    @Length(min = 1, max = 50)
    @HCZenkaku
    private String address1;

    /** 住所２、町村名 */
    @NotEmpty
    @Pattern(regexp = RegularExpressionsConstants.ZENKAKU_REGEX, message = "{HZenkakuValidator.INVALID_detail}")
    @Length(min = 1, max = 100)
    @HCZenkaku
    private String address2;

    /** 住所３、番地・建物名 */
    @Pattern(regexp = RegularExpressionsConstants.ZENKAKU_REGEX, message = "{HZenkakuValidator.INVALID_detail}")
    @Length(min = 0, max = 200)
    @HCZenkaku
    private String address3;

    /** 配送メモ */
    @HVBothSideSpace(startWith = SpaceValidateMode.ALLOW_SPACE, endWith = SpaceValidateMode.ALLOW_SPACE)
    @HVSpecialCharacter(allowPunctuation = true)
    @Length(min = 0, max = 400, message = "{HTextAreaValidator.LENGTH_detail}")
    private String shippingMemo;

    /**
     * アドレス帳の登録有無<br/>
     *
     * @return true..登録無 / false..登録有
     */
    public boolean isAddressBookEmpty() {
        if (this.addressBookItems == null) {
            return true;
        }
        return this.addressBookItems.isEmpty();
    }

    /**
     *
     * Method to check whether the address book max limit exceeded<br/>
     *
     * @return true: if address book is full false: if address book is not full
     *
     */
    public boolean isAddressBookFull() {
        // システム値取得
        int addressBookMaxCount = Integer.parseInt(PropertiesUtil.getSystemPropertiesValue("addressbook.max"));
        if (totalCount >= addressBookMaxCount) {
            return true;
        }
        return false;
    }

}
