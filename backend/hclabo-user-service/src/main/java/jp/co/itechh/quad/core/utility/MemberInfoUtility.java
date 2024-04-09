/*
 * Project Name : HIT-MALL3
 *
 * Copyright (C) 2008 i-TEC HANKYU HANSHIN INC. All Rights Reserved.
 *
 */

package jp.co.itechh.quad.core.utility;

import org.springframework.stereotype.Component;

import java.math.BigDecimal;
import java.text.DecimalFormat;
import java.text.NumberFormat;

/**
 * 会員業務ヘルパークラス<br/>
 *
 * @author natume
 * @author Kaneko (itec) 2012/08/09 UtilからHelperへ変更
 *
 */
@Component
public class MemberInfoUtility {

    /**
     * コンストラクタ<br/>
     */
    public MemberInfoUtility() {
        // nop
    }

    /**
     * ショップユニークIDの作成<br/>
     * ※4桁0埋めのショップSEQ + メールアドレスの小文字<br/>
     *
     * @param shopSeq ショップSEQ
     * @param mail メールアドレス
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
     * 表示金額への変換<br/>
     *
     * @param price 金額
     * @return 表示金額への変換
     */
    public String toString(BigDecimal price) {
        NumberFormat f = NumberFormat.getNumberInstance();
        return f.format(price);
    }

    /**
     * 会員SEQが会員を表しているかを判定
     *
     * @param memberInfoSeq 会員SEQ
     * @return true：会員の場合
     */
    public boolean isMember(Integer memberInfoSeq) {
        return !isGuest(memberInfoSeq);
    }

    /**
     * 会員SEQがゲストを表しているかを判定
     *
     * @param memberInfoSeq 会員SEQ
     * @return true：ゲストの場合
     */
    public boolean isGuest(Integer memberInfoSeq) {
        return memberInfoSeq == null || memberInfoSeq == 0;
    }

    /**
     *
     * SHA-256ハッシュ値計算用文字列生成<br/>
     * ショップSEQ、会員SEQ、パスワードを連結し、SHA-256ハッシュ化用文字列を生成する<br/>
     *
     * @param shopSeq ショップSEQ
     * @param memberInfoSeq 会員SEQ
     * @param password パスワード
     * @return SHA-256ハッシュ値計算用文字列
     */
    public String createSHA256HashValue(Integer shopSeq, Integer memberInfoSeq, String password) {
        return shopSeq.toString() + memberInfoSeq.toString() + password;
    }

}