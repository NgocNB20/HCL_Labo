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
 * フリガナ(性＋名) 値オブジェクト
 */
@Getter
public class Furigana {

    /** フリガナ(姓) */
    private final String lastKana;

    /** フリガナ(名) */
    private final String firstKana;

    /** 全角カナチェックの正規表現 */
    private static final String ZENKAKU_KANA_REGEX = RegularExpressionsConstants.ZENKAKU_KANA_REGEX;

    /** 文字数の最大文字数 */
    public static final int LENGTH_FURIGANA_MAXIMUM = 40;

    /**
     * コンストラクタ
     *
     * @param lastKana  フリガナ(姓)
     * @param firstKana フリガナ(名)
     */
    public Furigana(String lastKana, String firstKana) {

        // チェック
        AssertChecker.assertNotEmpty("lastKana is empty", lastKana);
        AssertChecker.assertNotEmpty("firstKana is empty", firstKana);
        // 設定されている場合のチェック
        if (!StringUtils.isBlank(lastKana) && !StringUtils.isBlank(firstKana)) {
            // 全角カナでない場合エラー
            if (!lastKana.matches(ZENKAKU_KANA_REGEX)) {
                throw new DomainException("LOGISTIC-LKFU0001-E");
            }
            if (!firstKana.matches(ZENKAKU_KANA_REGEX)) {
                throw new DomainException("LOGISTIC-LKFU0001-E");
            }

            StringBuilder furigana = new StringBuilder();
            furigana.append(lastKana);
            furigana.append(firstKana);
            if (furigana.toString().length() > LENGTH_FURIGANA_MAXIMUM) {
                throw new DomainException("LOGISTIC-FUFU0001-E");
            }
        }

        // 設定
        this.lastKana = lastKana;
        this.firstKana = firstKana;
    }

}
