package jp.co.itechh.quad.front.pc.web.front.order.common;

import lombok.Data;

import java.io.Serializable;

/**
 * 住所録Model
 *
 * @author kimura
 */
@Data
public class AddressBookModel implements Serializable {

    /** 住所ID */
    private String addressId;

    /** 請求先住所ID */
    private String billingAddressId;

    /** 顧客ID */
    private String customerId;

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

    /** 並び順(同一顧客ID内の住所録数＋1) */
    private String sort;

    /** 非表示フラグ */
    private boolean hideFlag;
}
