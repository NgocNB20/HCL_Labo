/*
 * Project Name : HIT-MALL4
 *
 * Copyright (C) 2021 i-TEC HANKYU HANSHIN INC. All Rights Reserved.
 *
 */

package jp.co.itechh.quad.front.pc.web.front.mailmagazine;

import jp.co.itechh.quad.front.annotation.converter.HCHankaku;
import jp.co.itechh.quad.front.base.constant.RegularExpressionsConstants;
import jp.co.itechh.quad.front.web.AbstractModel;
import lombok.Data;
import org.hibernate.validator.constraints.Length;

import javax.validation.constraints.Email;
import javax.validation.constraints.NotEmpty;
import javax.validation.constraints.Pattern;

/**
 * メルマガ Model
 *
 * @author kimura
 */
@Data
public class MailmagazineModel extends AbstractModel {

    /** コンストラク */
    public MailmagazineModel() {
        htmlMail = true;
    }

    /** メールアドレス */
    @NotEmpty
    @Email
    @Pattern(regexp = RegularExpressionsConstants.HANKAKU_REGEX, message = "{HMailAddressValidator.NOT_HANKAKU_detail}")
    @Length(min = 1, max = 160)
    @HCHankaku
    private String mailAddress;

    /** HTMLメール */
    private boolean htmlMail;
}
