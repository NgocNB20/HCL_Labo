/*
 * Project Name : HIT-MALL4
 *
 * Copyright (C) 2021 i-TEC HANKYU HANSHIN INC. All Rights Reserved.
 *
 */

package jp.co.itechh.quad.front.validator;

import jp.co.itechh.quad.front.base.utility.ApplicationContextUtility;
import jp.co.itechh.quad.front.base.utility.DateUtility;
import lombok.Data;
import org.apache.commons.lang3.StringUtils;

import java.sql.Timestamp;

/**
 * <span class="logicName">【クレジットカード有効期限】</span>クレジットカード有効期限チェックバリデータ。<br />
 * <br />
 * 当月より前の月が入力されている場合エラー<br />
 * <br />
 * <pre>
 * ※ 当バリデータはhtmlが「月」「年」の順序で書かれていることを前提とする。
 *    順序が逆にされた場合は動作保障外とする。
 * </pre>
 * <br />
 * アノテーション利用を想定した実装ではないため、チェック時にはString入力項目が来る想定
 *
 * @author kimura
 *
 */
@Data
public class HCreditCardExpirationDateValidator {

    /** カードの有効期限が切れている場合 */
    public static final String EXPIRED_CARD_MESSAGE_ID = "{HCreditCardExpirationDateValidator.EXPIRED_CARD_detail}";

    /** カードの有効期限が不正値の場合 */
    public static final String EXPIRED_CARD_NOT_DATE_MESSAGE_ID = HDateValidator.NOT_DATE_MESSAGE_ID;

    /** 年の上2桁 */
    public static final String FIRST_TWO_DIGITS_YEAR = "20";

    /** メッセージID */
    private String messageId = EXPIRED_CARD_MESSAGE_ID;

    /**
     * バリデーション実行
     *
     * @return チェックエラーの場合 false
     */
    public boolean isValid(String monthValue, String yearValue) {

        // 日付関連Helper取得
        DateUtility dateUtility = ApplicationContextUtility.getBean(DateUtility.class);

        if (StringUtils.isEmpty(yearValue) && StringUtils.isEmpty(monthValue)) {
            // 両方に入力がなければチェックしない
            return true;
        }

        /*
         * 不正な入力への対応
         */
        // 年、月のどちらか一方でも入力がなければエラーとする
        if (StringUtils.isEmpty(yearValue) || StringUtils.isEmpty(monthValue)) {
            // メッセージID上書
            messageId = HDateValidator.NOT_DATE_MESSAGE_ID;
            return false;
        }

        // GMOでは年の下2桁のみ扱うため、上2桁を補完
        yearValue = FIRST_TWO_DIGITS_YEAR + yearValue;
        // 年月をDateに変換できない場合も入力エラーとする
        Timestamp timestampValue = dateUtility.toTimestamp(yearValue, monthValue);
        if (timestampValue == null) {
            // メッセージID上書
            messageId = HDateValidator.NOT_DATE_MESSAGE_ID;
            return false;
        }
        if (timestampValue != null) {
            Timestamp expiryDate = dateUtility.getEndOfMonth(0, timestampValue);

            /*
             * 本来の入力チェック
             */
            // カードの有効期限が過去日ではないか
            Timestamp currentMonthLastDay = dateUtility.getEndOfMonth(0, dateUtility.getCurrentDate());

            if (expiryDate.getTime() < currentMonthLastDay.getTime()) {
                // 有効期限切れ
                return false;
            }
        }
        return true;
    }
}