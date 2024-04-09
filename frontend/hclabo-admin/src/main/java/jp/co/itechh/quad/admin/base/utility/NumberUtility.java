/*
 * Project Name : HIT-MALL3
 *
 * Copyright (C) 2008 i-TEC HANKYU HANSHIN INC. All Rights Reserved.
 *
 */

package jp.co.itechh.quad.admin.base.utility;

import org.springframework.stereotype.Component;

/**
 * 数値関連ヘルパークラス
 *
 * @author negishi
 * @author Kaneko (itec) 2012/08/17 UtilからHelperへ変更
 *
 */
@Component
public class NumberUtility {

    /** コンストラクタの説明・概要 */
    public NumberUtility() {
    }

    /**
     * 数値チェック
     * @param value 対象文字列
     * @return true..数値 false..数値でない
     */
    public boolean isNumber(String value) {

        if (value == null || value.length() == 0) {
            return false;
        }

        char[] chars = value.toCharArray();
        boolean hasDecPoint = false;
        boolean foundDigit = false;

        int start = 0;
        if (chars[0] == '-' || chars[0] == '+') {
            start = 1;
        }

        for (int i = start; i < chars.length; i++) {
            if (chars[i] >= '0' && chars[i] <= '9') {
                foundDigit = true;

            } else if (chars[i] == '.') {
                if (hasDecPoint) {
                    return false;
                }
                hasDecPoint = true;

            } else {
                return false;
            }
        }

        return foundDigit;
    }
}
