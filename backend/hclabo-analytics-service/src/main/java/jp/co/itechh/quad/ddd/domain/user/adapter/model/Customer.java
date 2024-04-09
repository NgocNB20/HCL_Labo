package jp.co.itechh.quad.ddd.domain.user.adapter.model;

import lombok.Data;

import java.util.Date;

/**
 * 会員
 */
@Data
public class Customer {

    /**
     * 顧客ID
     */
    private Integer customerId;

    /**
     * お客様姓
     */
    private String customerLastName;

    /**
     * お客様名
     */
    private String customerFirstName;

    /**
     * お客様セイ
     */
    private String customerLastKana;

    /**
     * お客様メイ
     */
    private String customerFirstKana;

    /**
     * お客様電話番号
     */
    private String customerPhoneNumber;

    /**
     * お客様メールアドレス
     */
    private String customerMail;

    /**
     * お客様生年月日
     */
    private Date customerBirthday;

    /**
     * お客様性別
     */
    private String customerSex;

    /**
     * 顧客アドレスID
     */
    private String customerInfoAddressId;

}
