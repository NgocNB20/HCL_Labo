package jp.co.itechh.quad.front.pc.web.front.member.change;

import jp.co.itechh.quad.front.annotation.converter.HCHankaku;
import jp.co.itechh.quad.front.annotation.converter.HCZenkaku;
import jp.co.itechh.quad.front.annotation.converter.HCZenkakuKana;
import jp.co.itechh.quad.front.annotation.validator.HVItems;
import jp.co.itechh.quad.front.annotation.validator.HVRItems;
import jp.co.itechh.quad.front.annotation.validator.HVRZipCode;
import jp.co.itechh.quad.front.annotation.validator.HVWindows31J;
import jp.co.itechh.quad.front.base.constant.RegularExpressionsConstants;
import jp.co.itechh.quad.front.constant.type.HTypeSexUnnecessaryAnswer;
import jp.co.itechh.quad.front.entity.memberinfo.MemberInfoEntity;
import jp.co.itechh.quad.front.web.AbstractModel;
import lombok.Data;
import org.hibernate.validator.constraints.Length;

import javax.validation.constraints.NotEmpty;
import javax.validation.constraints.Pattern;
import java.util.Map;

/**
 * 会員情報変更 Model
 *
 * @author kimura
 */
@Data
@HVRZipCode(targetLeft = "memberInfoZipCode1", targetRight = "memberInfoZipCode2")
@HVRItems(target = "memberInfoPrefecture", comparison = "memberInfoPrefectureItems")
public class MemberChangeModel extends AbstractModel {

    /** 更新対象の会員情報（画面項目外） */
    private MemberInfoEntity memberInfoEntity;

    /**
     * 氏名(姓)<br/>
     */
    @NotEmpty
    @HVWindows31J(checkJISX0208 = true)
    @Pattern(regexp = RegularExpressionsConstants.ZENKAKU_REGEX, message = "{HZenkakuValidator.INVALID_detail}")
    @Length(min = 1, max = 16)
    @HCZenkaku
    private String memberInfoLastName;

    /**
     * 氏名(名)<br/>
     */
    @NotEmpty
    @HVWindows31J(checkJISX0208 = true)
    @Pattern(regexp = RegularExpressionsConstants.ZENKAKU_REGEX, message = "{HZenkakuValidator.INVALID_detail}")
    @Length(min = 1, max = 16)
    @HCZenkaku
    private String memberInfoFirstName;

    /**
     * フリガナ(セイ)<br/>
     */
    @NotEmpty
    @Pattern(regexp = RegularExpressionsConstants.ZENKAKU_KANA_REGEX,
             message = "{HZenkakuKanaValidator.INVALID_detail}")
    @Length(min = 1, max = 40)
    @HCZenkakuKana
    private String memberInfoLastKana;

    /**
     * フリガナ(メイ)<br/>
     */
    @NotEmpty
    @Pattern(regexp = RegularExpressionsConstants.ZENKAKU_KANA_REGEX,
             message = "{HZenkakuKanaValidator.INVALID_detail}")
    @Length(min = 1, max = 40)
    @HCZenkakuKana
    private String memberInfoFirstKana;

    /**
     * 性別<br/>
     */
    @HVItems(target = HTypeSexUnnecessaryAnswer.class)
    @NotEmpty
    private String memberInfoSex;

    /**
     * 性別選択肢<br/>
     */
    private Map<String, String> memberInfoSexItems;

    /**
     * 生年月日(年)<br/>
     */
    private String memberInfoBirthdayYear;

    /**
     * 生年月日(月)<br/>
     */
    private String memberInfoBirthdayMonth;

    /**
     * 生年月日(日)<br/>
     */
    private String memberInfoBirthdayDay;

    /**
     * 電話番号<br/>
     */
    @NotEmpty
    @Pattern(regexp = RegularExpressionsConstants.TELEPHONE_NUMBER_REGEX,
             message = "{HTelephoneNumberValidator.INVALID_detail}")
    @Length(min = 1, max = 11)
    @HCHankaku
    private String memberInfoTel;

    /**
     * 郵便番号(上3桁)<br/>
     */
    @NotEmpty
    @Length(min = 1, max = 3)
    @HCHankaku
    private String memberInfoZipCode1;

    /**
     * 郵便番号(下4桁)<br/>
     */
    @NotEmpty
    @Length(min = 1, max = 4)
    @HCHankaku
    private String memberInfoZipCode2;

    /**
     * 都道府県<br/>
     */
    @NotEmpty
    private String memberInfoPrefecture;

    /**
     * 都道府県プルダウン用リスト
     */
    private Map<String, String> memberInfoPrefectureItems;

    /**
     * 住所(市区郡)<br/>
     */
    @NotEmpty
    @Pattern(regexp = RegularExpressionsConstants.ZENKAKU_REGEX, message = "{HZenkakuValidator.INVALID_detail}")
    @Length(min = 1, max = 50)
    @HCZenkaku
    private String memberInfoAddress1;

    /**
     * 住所(町村)<br/>
     */
    @NotEmpty
    @Pattern(regexp = RegularExpressionsConstants.ZENKAKU_REGEX, message = "{HZenkakuValidator.INVALID_detail}")
    @Length(min = 1, max = 100)
    @HCZenkaku
    private String memberInfoAddress2;

    /**
     * 住所(番地・ビル名)<br/>
     */
    @Pattern(regexp = RegularExpressionsConstants.ZENKAKU_REGEX, message = "{HZenkakuValidator.INVALID_detail}")
    @Length(min = 0, max = 200)
    @HCZenkaku
    private String memberInfoAddress3;

    /**
     * メルマガ希望<br/>
     */
    private boolean mailMagazine;

    /**
     * 変更前メルマガ希望<br/>
     */
    private boolean preMailMagazine;

    /** 住所ID（画面項目外） */
    private String preAddressId;

    /** 住所録.住所名（画面項目外） */
    private String addressName;

    /** 住所録.配送メモ（画面項目外） */
    private String shippingMemo;
}
