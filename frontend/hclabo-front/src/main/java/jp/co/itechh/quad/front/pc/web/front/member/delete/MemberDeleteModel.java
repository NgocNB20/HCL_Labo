/*
 * Copyright (C) 2022 i-TEC HANKYU HANSHIN INC. All Rights Reserved.
 */

package jp.co.itechh.quad.front.pc.web.front.member.delete;

import jp.co.itechh.quad.front.annotation.converter.HCHankaku;
import jp.co.itechh.quad.front.annotation.validator.HVSpecialCharacter;
import jp.co.itechh.quad.front.base.constant.RegularExpressionsConstants;
import jp.co.itechh.quad.front.web.AbstractModel;
import lombok.Data;
import org.hibernate.validator.constraints.Length;

import javax.validation.constraints.Email;
import javax.validation.constraints.NotEmpty;
import javax.validation.constraints.Pattern;

/**
 * 会員登録解除 model
 *
 * @author Pham Quang Dieu
 */
@Data
public class MemberDeleteModel extends AbstractModel {

    /**
     * 会員ID
     */
    @NotEmpty
    @Email
    @Pattern(regexp = RegularExpressionsConstants.HANKAKU_REGEX, message = "{HMailAddressValidator.NOT_HANKAKU_detail}")
    @Length(min = 1, max = 160)
    @HCHankaku
    private String memberInfoId;

    /**
     * 会員パスワード
     */
    @NotEmpty
    @HVSpecialCharacter(allowPunctuation = true)
    private String memberInfoPassWord;
}
