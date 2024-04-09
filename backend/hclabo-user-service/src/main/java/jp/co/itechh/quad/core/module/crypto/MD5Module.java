/*
 * Project Name : HIT-MALL3
 *
 * Copyright (C) 2008 i-TEC HANKYU HANSHIN INC. All Rights Reserved.
 *
 */

package jp.co.itechh.quad.core.module.crypto;

import jp.co.itechh.quad.core.base.util.seasar.StringUtil;
import org.springframework.stereotype.Component;

import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;

/**
 * MD5計算クラス<br/>
 * MD5関連の処理を行う<br/>
 *
 * @author matsumoto
 * @version $Revision: 1.2 $
 *
 */
@Component
public class MD5Module {

    /**
     * MD5ハッシュ値を作成する<br/>
     * パラメータに null が指定された場合、 nullを返却します。
     * @param plaintext ハッシュ計算対象の文字列
     * @return MD5で求めたハッシュ値
     */
    public String createHash(String plaintext) {
        if (plaintext == null) {
            return null;
        }

        try {
            MessageDigest digest = java.security.MessageDigest.getInstance("MD5");
            digest.update(plaintext.getBytes());
            return StringUtil.toHex(digest.digest());
        } catch (NoSuchAlgorithmException e) {
            // エンコードはMD5固定なので基本的に発生しない。
            throw new RuntimeException(e);
        }
    }
}