/*
 * Project Name : HIT-MALL3
 *
 * Copyright (C) 2008 i-TEC HANKYU HANSHIN INC. All Rights Reserved.
 *
 */

package jp.co.itechh.quad.core.utility;

import org.springframework.stereotype.Component;

import java.math.BigDecimal;
import java.sql.Timestamp;
import java.text.DecimalFormat;
import java.text.NumberFormat;
import java.util.Calendar;

/**
 * 会員業務ヘルパークラス
 *
 * @author natume
 * @author Kaneko (itec) 2012/08/09 UtilからHelperへ変更
 */
@Component
public class MemberInfoUtility {

    /** 成人確認用　基準年齢 */
    protected static final int ADULT_AGE = 20;

    /**
     * コンストラクタ
     */
    public MemberInfoUtility() {
        // nop
    }

    /**
     * ショップユニークIDの作成
     * ※4桁0埋めのショップSEQ + メールアドレスの小文字
     *
     * @param shopSeq ショップSEQ
     * @param mail    メールアドレス
     * @return ショップユニークID
     */
    public String createShopUniqueId(Integer shopSeq, String mail) {
        if (shopSeq == null || mail == null) {
            return null;
        }

        // 4桁0埋めのショップSEQ + メールアドレスの小文字
        return new DecimalFormat("0000").format(shopSeq) + mail.toLowerCase();
    }

    /**
     * 表示金額への変換
     *
     * @param price 金額
     * @return 表示金額への変換
     */
    public String toString(BigDecimal price) {
        NumberFormat f = NumberFormat.getNumberInstance();
        return f.format(price);
    }

    /**
     *
     * 成年判定<br/>
     * 生年月日が成年であるかどうかを判定<br/>
     *
     * @param birthday 生年月日
     * @return 判定結果 true:成年, false:未成年
     */
    public boolean isAdult(Timestamp birthday) {
        // 生年月日(yyyyMMdd)
        Calendar birthDayYmd = Calendar.getInstance();
        // 現在年月日(yyyyMMdd)
        Calendar nowDayYmd = Calendar.getInstance();

        birthDayYmd.setTime(birthday);
        nowDayYmd.setTime(new Timestamp(System.currentTimeMillis()));
        // 生年月日に下限年齢を加算
        birthDayYmd.add(Calendar.YEAR, ADULT_AGE);

        if (nowDayYmd.compareTo(birthDayYmd) >= 0) {
            // 下限年齢以上の場合
            return true;
        }

        return false;
    }

}
