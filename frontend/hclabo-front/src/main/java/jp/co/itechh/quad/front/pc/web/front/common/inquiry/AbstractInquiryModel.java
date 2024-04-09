/*
 * Copyright (C) 2022 i-TEC HANKYU HANSHIN INC. All Rights Reserved.
 */

package jp.co.itechh.quad.front.pc.web.front.common.inquiry;

import jp.co.itechh.quad.front.annotation.converter.HCZenkaku;
import jp.co.itechh.quad.front.annotation.validator.HVBothSideSpace;
import jp.co.itechh.quad.front.annotation.validator.HVSpecialCharacter;
import jp.co.itechh.quad.front.base.constant.ValidatorConstants;
import jp.co.itechh.quad.front.constant.type.HTypeInquiryStatus;
import jp.co.itechh.quad.front.constant.type.SpaceValidateMode;
import jp.co.itechh.quad.front.dto.inquiry.InquiryDetailsDto;
import jp.co.itechh.quad.front.web.AbstractModel;
import lombok.Data;
import org.hibernate.validator.constraints.Length;

import javax.validation.constraints.NotEmpty;
import java.sql.Timestamp;
import java.util.List;

/**
 * 問合せモデル基底クラス<br/>
 *
 * @author kn23834
 * @version $Revision: 1.0 $
 *
 */
@Data
public class AbstractInquiryModel extends AbstractModel {

    /**
     * URLパラメータ：お問い合わせ番号<br>
     * ゲスト問合せ認証画面にパラメータを引き継ぐ為RedirectScope
     */
    private String icd;

    /**
     * icd保管用変数
     * <pre>
     * icdがRedirectScopeだと、画面をリロードした時に保存されていないため
     * 再度認証画面に飛ばされる。
     * しかし、icdは認証画面に引き継ぎたい値のためRedirectScopeである必要がある。
     * </pre>
     */
    private String saveIcd;

    /** DB値を保持 */
    private InquiryDetailsDto reedOnlyDto;

    /** 問い合わせ内容入力値 */
    @NotEmpty
    @HVBothSideSpace(startWith = SpaceValidateMode.ALLOW_SPACE, endWith = SpaceValidateMode.ALLOW_SPACE)
    @HVSpecialCharacter(allowPunctuation = true)
    @Length(min = 1, max = ValidatorConstants.LENGTH_INQUIRYBODY_MAXIMUM,
            message = "{HTextAreaValidator.LENGTH_detail}")
    @HCZenkaku
    private String inputInquiryBody;

    /** 問い合わせ.お問い合わせ番号 */
    private String inquiryCode;

    /** 問い合わせ分類.問い合わせ分類名 */
    private String inquiryGroupName;

    /** 問い合わせ.問い合わせ状態 */
    private String inquiryStatus;

    /** 問い合わせ.問い合わせ状態.値 */
    private String inquiryStatusValue;

    /** 問い合わせ.初回問い合わせ日時 */
    private Timestamp firstInquiryTime;

    /** 問い合わせ.問い合わせ者氏名 */
    private String inquiryName;

    /** 問い合わせ.問い合わせ者氏名フリガナ */
    private String inquiryKana;

    /** 問い合わせ.問い合わせ者TEL */
    private String inquiryTel;

    /** 問い合わせ.問い合わせ者メールアドレス */
    private String inquiryMail;

    /** お問い合わせ詳細画面アイテムのリスト */
    private List<InquiryModelItem> inquiryModelItems;

    /**
     * 問合せの状態が完了しているか否か
     *
     * @return true：完了
     */
    public boolean isCompletion() {
        return HTypeInquiryStatus.COMPLETION.equals(reedOnlyDto.getInquiryEntity().getInquiryStatus());
    }

}
