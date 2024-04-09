/*
 * Copyright (C) 2022 i-TEC HANKYU HANSHIN INC. All Rights Reserved.
 */

package jp.co.itechh.quad.ddd.domain.addressbook.entity;

import jp.co.itechh.quad.core.base.constant.RegularExpressionsConstants;
import jp.co.itechh.quad.ddd.domain.addressbook.valueobject.Address;
import jp.co.itechh.quad.ddd.domain.addressbook.valueobject.AddressId;
import jp.co.itechh.quad.ddd.exception.AssertChecker;
import jp.co.itechh.quad.ddd.exception.DomainException;
import lombok.Getter;
import org.apache.commons.lang3.StringUtils;

import java.util.Date;

/**
 * 住所録 エンティティ
 */
@Getter
public class AddressBookEntity {

    /** 全角チェックの正規表現 */
    private static final String ZENKAKU_REGEX = RegularExpressionsConstants.ZENKAKU_REGEX;

    /** 全角チェックの正規表現 */
    private static final String ALLOW_PUNCTUATION_REGEX = RegularExpressionsConstants.ALLOW_PUNCTUATION_REGEX;

    /** 文字数の最大文字数 */
    public static final int LENGTH_SHIPPINGMEMO_MAXIMUM = 400;

    /** 文字数の最大文字数 */
    public static final int LENGTH_ADDRESSNAME_MAXIMUM = 20;

    /** 住所ID */
    private final AddressId addressId;

    /** 顧客ID */
    private final String customerId;

    /** 住所名 */
    private final String addressName;

    /** 住所 */
    private final Address address;

    /** 配送メモ */
    private final String shippingMemo;

    /** 登録日時 */
    private final Date registDate;

    /** 非表示フラグ */
    private boolean hideFlag;

    /** デフォルト指定フラグ */
    private boolean defaultFlag;

    /**
     * コンストラクタ
     *
     * @param customerId   顧客ID
     * @param addressName  住所名
     * @param address      住所
     * @param shippingMemo 配送メモ
     * @param registDate   登録日時
     */
    public AddressBookEntity(String customerId,
                             String addressName,
                             Address address,
                             String shippingMemo,
                             Date registDate) {

        // チェック
        // 顧客IDのチェック
        AssertChecker.assertNotEmpty("customerId is empty", customerId);
        // 住所のチェック
        AssertChecker.assertNotNull("address is null", address);
        // 配送メモのチェック ※両端スペース許容のため、未チェック
        // 設定されている場合のチェック
        if (!StringUtils.isBlank(shippingMemo)) {
            if (shippingMemo.length() > LENGTH_SHIPPINGMEMO_MAXIMUM) {
                throw new DomainException("LOGISTIC-SHIP0001-E");
            }
            // 特殊文字のチェック ※HM3.6では「句読文字とtab, 改行」は許可しており、他の特殊文字は禁止
            if (!shippingMemo.matches(ALLOW_PUNCTUATION_REGEX)) {
                throw new DomainException("LOGISTIC-SHIP0002-E");
            }
        }
        // 登録日時のチェック
        AssertChecker.assertNotNull("registDate is null", registDate);

        // 住所名のチェック
        if (StringUtils.isBlank(addressName)) {
            // 未設定の場合、氏名を設定する
            addressName = address.getFullName().getLastName() + address.getFullName().getFirstName();
        } else {
            // 設定されている場合のチェック
            if (addressName.length() > LENGTH_ADDRESSNAME_MAXIMUM) {
                throw new DomainException("LOGISTIC-ADDN0001-E");
            }
        }

        // 設定
        this.addressId = new AddressId();
        this.customerId = customerId;
        this.addressName = addressName;
        this.address = address;
        this.shippingMemo = shippingMemo;
        this.registDate = registDate;
        // デフォルト設定
        this.hideFlag = false;
        this.defaultFlag = false;
    }

    /**
     * 非公開にする
     */
    public void close() {

        // 非公開フラグをtrueに設定
        this.hideFlag = true;
    }

    /**
     * 初期選択される住所録にする
     */
    public void setDefault() {

        // デフォルト指定フラグをtrueに設定
        this.defaultFlag = true;
    }

    /**
     * DB取得値設定用コンストラクタ
     * ※ユースケース層での使用禁止
     *
     * @param customerId   顧客ID
     * @param addressId    住所ID
     * @param addressName  住所名
     * @param address      住所
     * @param shippingMemo 配送メモ
     * @param registDate   登録日時
     * @param hideFlag     非表示フラグ
     * @param defaultFlag  デフォルト指定フラグ
     */
    public AddressBookEntity(String customerId,
                             AddressId addressId,
                             String addressName,
                             Address address,
                             String shippingMemo,
                             Date registDate,
                             boolean hideFlag,
                             boolean defaultFlag) {

        // 設定
        this.customerId = customerId;
        this.addressId = addressId;
        this.addressName = addressName;
        this.address = address;
        this.shippingMemo = shippingMemo;
        this.registDate = registDate;
        this.hideFlag = hideFlag;
        this.defaultFlag = defaultFlag;
    }

}
