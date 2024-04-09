/*
 * Project Name : HIT-MALL3
 *
 * Copyright (C) 2008 i-TEC HANKYU HANSHIN INC. All Rights Reserved.
 *
 */

package jp.co.itechh.quad.core.service.memberinfo.password;

import java.sql.Timestamp;

/**
 * パスワード再設定メール送信サービス<br/>
 *
 * @author natume
 * @version $Revision: 1.3 $
 *
 */
public interface PasswordResetSendMailService {

    // SMP0002

    /**
     * 会員情報取得失敗エラー<br/>
     * ユニークIdでの取得時エラー<br/>
     * <code>MSGCD_MEMBERINFOENTITYDTO_NULL</code>
     */
    String MSGCD_MEMBERINFOENTITYDTO_NULL = "SMP000201";

    /**
     * 確認メール情報登録失敗エラー<br/>
     * <code>MSGCD_CONFIRMMAIL_REGIST_FAIL</code>
     */
    String MSGCD_CONFIRMMAIL_REGIST_FAIL = "SMP000202";

    /**
     * 会員情報取得失敗エラー<br/>
     * 生年月日が違う場合のエラー<br/>
     * <code>MSGCD_MEMBERINFOENTITYDTO_NULL</code>
     */
    String MSGCD_MEMBERINFOBIRTHDAY_FAIL = "SMP000203";

    /**
     * パスワード再設定メール送信処理<br/>
     *
     * @param mail メールアドレス
     * @param memberInfoBirthDay 生年月日
     * @return メール送信結果
     */
    boolean execute(String mail, Timestamp memberInfoBirthDay);

}
