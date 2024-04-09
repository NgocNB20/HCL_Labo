/*
 * Project Name : HIT-MALL3
 *
 * Copyright (C) 2008 i-TEC HANKYU HANSHIN INC. All Rights Reserved.
 *
 */

package jp.co.itechh.quad.core.utility;

import org.springframework.stereotype.Component;

/**
 *
 * 管理者業務ヘルパークラス<br/>
 *
 */
@Component
public class AdministratorUtility {

    /**
     *
     * コンストラクタ<br/>
     *
     */
    public AdministratorUtility() {
        // nop
    }

    /**
     *
     * SHA-256ハッシュ値計算用文字列生成<br/>
     * ショップSEQ、管理者SEQ、パスワードを連結し、SHA-256ハッシュ化用文字列を生成する<br/>
     *
     * @param shopSeq ショップSEQ
     * @param administratorSeq 管理者SEQ
     * @param password パスワード
     * @return SHA-256ハッシュ値計算用文字列
     */
    public String createSHA256HashValue(Integer shopSeq, Integer administratorSeq, String password) {
        return shopSeq.toString() + administratorSeq.toString() + password;
    }
}
