/*
 * Project Name : HIT-MALL4
 *
 * Copyright (C) 2021 i-TEC HANKYU HANSHIN INC. All Rights Reserved.
 *
 */

package jp.co.itechh.quad.front.pc.web.front.member.mail;

import jp.co.itechh.quad.front.annotation.validator.HVRStringNotEqual;
import jp.co.itechh.quad.front.base.constant.RegularExpressionsConstants;
import jp.co.itechh.quad.front.web.AbstractModel;
import lombok.Data;
import org.hibernate.validator.constraints.Length;

import javax.validation.constraints.Email;
import javax.validation.constraints.NotEmpty;
import javax.validation.constraints.Pattern;

/**
 * メールアドレス変更 Model
 *
 * @author kn23834
 * @version $Revision: 1.0 $
 *
 */
@Data
@HVRStringNotEqual(target = "memberInfoNewMail", comparison = "memberInfoMail",
                   message = "{HStringNotEqualValidator.EQUAL_MAIL_detail}")
public class MemberMailModel extends AbstractModel {

    /**
     * 現メールアドレス<br/>
     */
    private String memberInfoMail;

    /**
     * 新しいメールアドレス<br/>
     */
    @NotEmpty
    @Email
    @Pattern(regexp = RegularExpressionsConstants.HANKAKU_REGEX, message = "{HMailAddressValidator.NOT_HANKAKU_detail}")
    @Length(min = 1, max = 160)
    private String memberInfoNewMail;

}