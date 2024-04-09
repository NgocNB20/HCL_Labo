/*
 * Project Name : HIT-MALL4
 *
 * Copyright (C) 2021 i-TEC HANKYU HANSHIN INC. All Rights Reserved.
 *
 */

package jp.co.itechh.quad.front.pc.web.front.member.pass;

import jp.co.itechh.quad.front.annotation.validator.HVSpecialCharacter;
import jp.co.itechh.quad.front.base.constant.RegularExpressionsConstants;
import jp.co.itechh.quad.front.entity.memberinfo.MemberInfoEntity;
import jp.co.itechh.quad.front.web.AbstractModel;
import lombok.Data;

import javax.validation.constraints.NotEmpty;
import javax.validation.constraints.Pattern;

/**
 *
 * パスワード変更 Model
 *
 * @author PHAM QUANG DIEU (VJP)
 * @version $Revision: 1.0 $
 *
 */
@Data
public class MemberPassModel extends AbstractModel {

    /**
     * パスワード<br/>
     */
    @NotEmpty
    @HVSpecialCharacter(allowPunctuation = true)
    private String memberInfoPassWord;

    /**
     * 新しいパスワード<br/>
     */
    @NotEmpty
    @Pattern(regexp = RegularExpressionsConstants.PASSWORD_NUMBER_REGEX,
             message = "{HPasswordValidator.INVALID_detail}")
    private String memberInfoNewPassWord;

    /**
     * 会員情報エンティティ
     */
    private MemberInfoEntity memberInfoEntity;

}