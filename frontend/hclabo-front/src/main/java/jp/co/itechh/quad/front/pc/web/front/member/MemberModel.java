/*
 * Project Name : HIT-MALL4
 *
 * Copyright (C) 2021 i-TEC HANKYU HANSHIN INC. All Rights Reserved.
 *
 */

package jp.co.itechh.quad.front.pc.web.front.member;

import jp.co.itechh.quad.front.web.AbstractModel;
import lombok.Data;

/**
 * 会員メニューModel
 *
 * @author PHAM QUANG DIEU (VJP)
 *
 */
@Data
public class MemberModel extends AbstractModel {

    /**
     * 会員氏名(姓)<br/>
     */
    private String lastName;

    /**
     * 会員氏名(名)<br/>
     */
    private String firstName;

}
