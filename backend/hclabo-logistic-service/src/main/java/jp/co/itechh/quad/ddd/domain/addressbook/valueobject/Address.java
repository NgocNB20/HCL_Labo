/*
 * Copyright (C) 2022 i-TEC HANKYU HANSHIN INC. All Rights Reserved.
 */

package jp.co.itechh.quad.ddd.domain.addressbook.valueobject;

import jp.co.itechh.quad.core.base.constant.RegularExpressionsConstants;
import jp.co.itechh.quad.core.base.utility.ApplicationContextUtility;
import jp.co.itechh.quad.core.service.zipcode.ZipCodeMatchingCheckService;
import jp.co.itechh.quad.ddd.exception.AssertChecker;
import jp.co.itechh.quad.ddd.exception.DomainException;
import lombok.Getter;
import org.apache.commons.lang3.StringUtils;

/**
 * 住所 値オブジェクト
 */
@Getter
public class Address {

    /** 半角数値チェックの正規表現 */
    public static final String HANKAKU_NUMBER_REGEX = RegularExpressionsConstants.HANKAKU_NUMBER_REGEX;

    /** 半角チェックの正規表現 */
    public static final String HANKAKU_REGEX = RegularExpressionsConstants.HANKAKU_REGEX;

    /** 全角チェックの正規表現 */
    private static final String ZENKAKU_REGEX = RegularExpressionsConstants.ZENKAKU_REGEX;

    /** 文字数の最大文字数 */
    public static final int LENGTH_TEL_MAXIMUM = 11;

    /** 文字数の最大文字数 */
    public static final int LENGTH_ZIPCODE_MAXIMUM = 7;

    /** 文字数の最大文字数 */
    public static final int LENGTH_PREFECTURE_MAXIMUM = 4;

    /** 文字数の最大文字数 */
    public static final int LENGTH_ADDRESS1_MAXIMUM = 50;

    /** 文字数の最大文字数 */
    public static final int LENGTH_ADDRESS2_MAXIMUM = 100;

    /** 文字数の最大文字数 */
    public static final int LENGTH_ADDRESS3_MAXIMUM = 200;

    /** 氏名(性＋名) */
    private FullName fullName;

    /** フリガナ(性＋名) */
    private Furigana furigana;

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

    /**
     * コンストラクタ
     *
     * @param fullName   氏名(性＋名)
     * @param furigana   フリガナ(性＋名)
     * @param tel        電話番号
     * @param zipCode    郵便番号
     * @param prefecture 都道府県
     * @param address1   住所1
     * @param address2   住所2
     * @param address3   住所3
     */
    public Address(FullName fullName,
                   Furigana furigana,
                   String tel,
                   String zipCode,
                   String prefecture,
                   String address1,
                   String address2,
                   String address3) {

        // 氏名(性＋名)のチェック
        AssertChecker.assertNotNull("fullName is null", fullName);

        // フリガナ(性＋名)のチェック
        AssertChecker.assertNotNull("furigana is null", furigana);

        // 電話番号のチェック
        AssertChecker.assertNotEmpty("tel is empty", tel);

        // 設定されている場合のチェック
        if (!tel.matches(HANKAKU_NUMBER_REGEX) || tel.length() > LENGTH_TEL_MAXIMUM) {
            throw new DomainException("LOGISTIC-TELA0001-E");
        }

        // 郵便番号のチェック
        AssertChecker.assertNotEmpty("zipCode is empty", zipCode);

        // 設定されている場合のチェック
        if (!zipCode.matches(HANKAKU_NUMBER_REGEX) || zipCode.length() != LENGTH_ZIPCODE_MAXIMUM) {
            throw new DomainException("LOGISTIC-ZIPA0001-E");
        }

        // 都道府県のチェック

        AssertChecker.assertNotEmpty("prefecture is empty", prefecture);
        // 設定されている場合のチェック
        if (!prefecture.matches(ZENKAKU_REGEX) || prefecture.length() > LENGTH_PREFECTURE_MAXIMUM) {
            throw new DomainException("LOGISTIC-PREF0001-E");
        }

        // 郵便番号と都道府県の整合性チェック
        ZipCodeMatchingCheckService service = ApplicationContextUtility.getBean(ZipCodeMatchingCheckService.class);
        // 整合性がNGの場合エラー
        if (!service.execute(zipCode, prefecture)) {
            throw new DomainException("LOGISTIC-ZAPR0001-E");
        }

        // 住所1のチェック
        AssertChecker.assertNotEmpty("address1 is empty", address1);
        // 設定されている場合のチェック
        if (!address1.matches(ZENKAKU_REGEX) || address1.length() > LENGTH_ADDRESS1_MAXIMUM) {
            throw new DomainException("LOGISTIC-ADDR0001-E");
        }

        // 住所2のチェック
        AssertChecker.assertNotEmpty("address2 is empty", address2);
        // 設定されている場合のチェック
        if (!address2.matches(ZENKAKU_REGEX) || address2.length() > LENGTH_ADDRESS2_MAXIMUM) {
            throw new DomainException("LOGISTIC-ADDR0002-E");
        }
        // 住所3のチェック
        // 設定されている場合のチェック
        if (!StringUtils.isBlank(address3)) {
            // 設定されている場合のチェック
            if (!address3.matches(ZENKAKU_REGEX) || address3.length() > LENGTH_ADDRESS3_MAXIMUM) {
                throw new DomainException("LOGISTIC-ADDR0003-E");
            }
        }

        // 設定
        this.fullName = fullName;
        this.furigana = furigana;
        this.tel = tel;
        this.zipCode = zipCode;
        this.prefecture = prefecture;
        this.address1 = address1;
        this.address2 = address2;
        this.address3 = address3;
    }

    /**
     * コンストラクタ<br/>
     * インフラ層用のコンストラクタのため他の層からは呼び出しNG
     *
     * @param fullName     氏名(性＋名)
     * @param furigana     フリガナ(性＋名)
     * @param tel          電話番号
     * @param zipCode      郵便番号
     * @param prefecture   都道府県
     * @param address1     住所1
     * @param address2     住所2
     * @param address3     住所3
     * @param customParams 案件用引数
     */
    public Address(FullName fullName,
                   Furigana furigana,
                   String tel,
                   String zipCode,
                   String prefecture,
                   String address1,
                   String address2,
                   String address3,
                   Object... customParams) {

        // 設定
        this.fullName = fullName;
        this.furigana = furigana;
        this.tel = tel;
        this.zipCode = zipCode;
        this.prefecture = prefecture;
        this.address1 = address1;
        this.address2 = address2;
        this.address3 = address3;
    }

}