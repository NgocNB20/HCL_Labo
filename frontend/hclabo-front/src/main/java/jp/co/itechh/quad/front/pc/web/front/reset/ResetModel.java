/*
 * Project Name : HIT-MALL4
 *
 * Copyright (C) 2021 i-TEC HANKYU HANSHIN INC. All Rights Reserved.
 *
 */

package jp.co.itechh.quad.front.pc.web.front.reset;

import jp.co.itechh.quad.front.annotation.converter.HCDate;
import jp.co.itechh.quad.front.annotation.converter.HCHankaku;
import jp.co.itechh.quad.front.annotation.validator.HVRSeparateDate;
import jp.co.itechh.quad.front.base.constant.RegularExpressionsConstants;
import jp.co.itechh.quad.front.web.AbstractModel;
import lombok.Data;
import org.hibernate.validator.constraints.Length;

import javax.validation.constraints.Email;
import javax.validation.constraints.NotEmpty;
import javax.validation.constraints.Pattern;

/**
 * パスワードリセット Model
 *
 * @author kn23834
 * @version $Revision: 1.0 $
 *
 */
@Data
@HVRSeparateDate(targetYear = "memberInfoBirthdayYear", targetMonth = "memberInfoBirthdayMonth",
                 targetDate = "memberInfoBirthdayDay")
public class ResetModel extends AbstractModel {

    /**
     * 会員メール<br/>
     */
    @NotEmpty
    @Email
    @Pattern(regexp = RegularExpressionsConstants.HANKAKU_REGEX, message = "{HMailAddressValidator.NOT_HANKAKU_detail}")
    @Length(min = 1, max = 160)
    @HCHankaku
    private String memberInfoMail;

    /**
     * 生年月日(年)<br/>
     */
    @NotEmpty
    @Length(min = 1, max = 4)
    @HCDate(pattern = "yyyy")
    private String memberInfoBirthdayYear;

    /**
     * 生年月日(月)<br/>
     */
    @NotEmpty
    @Length(min = 1, max = 2)
    @HCDate(pattern = "MM")
    private String memberInfoBirthdayMonth;

    /**
     * 生年月日(日)<br/>
     */
    @NotEmpty
    @Length(min = 1, max = 2)
    @HCDate(pattern = "dd")
    private String memberInfoBirthdayDay;

}