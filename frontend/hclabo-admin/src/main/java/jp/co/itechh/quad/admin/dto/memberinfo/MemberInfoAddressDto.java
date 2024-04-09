/*
 * Project Name : HIT-MALL3
 *
 * Copyright (C) 2008 i-TEC HANKYU HANSHIN INC. All Rights Reserved.
 *
 */

package jp.co.itechh.quad.admin.dto.memberinfo;

import lombok.Data;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Component;

import java.io.Serializable;

/**
 * 会員住所Dtoクラス
 *
 * @author kimura
 */
@Data
@Component
@Scope("prototype")
public class MemberInfoAddressDto implements Serializable {

    /** serialVersionUID */
    private static final long serialVersionUID = 1L;

    /** 住所ID */
    private String addressId;

    /** 住所名 */
    private String addressName;

    /** 氏名(姓) */
    private String lastName;

    /** 氏名(名) */
    private String firstName;

    /** フリガナ(姓) */
    private String lastKana;

    /** フリガナ(名) */
    private String firstKana;

    /** 電話番号 */
    private String tel;

    /** 郵便番号 */
    private String zipCode;

    /** 都道府県 */
    private String prefecture;

    /** 住所1 */
    private String address1;

    /** 住所2 */
    private String address2;

    /** 住所3 */
    private String address3;

    /** 配送メモ */
    private String shippingMemo;

}
