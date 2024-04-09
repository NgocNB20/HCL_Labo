/*
 * Copyright (C) 2022 i-TEC HANKYU HANSHIN INC. All Rights Reserved.
 */

package jp.co.itechh.quad.ddd.domain.addressbook.valueobject;

import jp.co.itechh.quad.core.base.constant.RegularExpressionsConstants;
import jp.co.itechh.quad.ddd.exception.AssertChecker;
import jp.co.itechh.quad.ddd.exception.DomainException;
import lombok.Getter;
import org.apache.commons.lang3.StringUtils;

/**
 * 氏名(性＋名) 値オブジェクト
 */
@Getter
public class FullName {

    /** 氏名(姓) */
    private final String lastName;

    /** 氏名(名) */
    private final String firstName;

    /** 全角チェックの正規表現 */
    private static final String ZENKAKU_REGEX = RegularExpressionsConstants.ZENKAKU_REGEX;

    /** 文字数の最大文字数 */
    public static final int LENGTH_FULLNAME_MAXIMUM = 16;

    /**
     * コンストラクタ
     *
     * @param lastName  氏名(姓)
     * @param firstName 氏名(名)
     */
    public FullName(String lastName, String firstName) {

        // チェック
        AssertChecker.assertNotEmpty("lastName is empty", lastName);
        AssertChecker.assertNotEmpty("firstName is empty", firstName);
        // 設定されている場合のチェック
        if (!StringUtils.isBlank(lastName) && !StringUtils.isBlank(firstName)) {
            // 全角でない場合エラー
            if (!lastName.matches(ZENKAKU_REGEX)) {
                throw new DomainException("LOGISTIC-LNFN0001-E");
            }
            if (!firstName.matches(ZENKAKU_REGEX)) {
                throw new DomainException("LOGISTIC-LNFN0001-E");
            }

            StringBuilder fullName = new StringBuilder();
            fullName.append(lastName);
            fullName.append(firstName);
            if (fullName.toString().length() > LENGTH_FULLNAME_MAXIMUM) {
                throw new DomainException("LOGISTIC-FNFN0001-E");
            }
        }

        // 設定
        this.lastName = lastName;
        this.firstName = firstName;
    }

}