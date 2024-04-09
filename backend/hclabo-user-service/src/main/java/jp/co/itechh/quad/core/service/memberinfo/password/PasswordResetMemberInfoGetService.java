/*
 * Project Name : HIT-MALL3
 *
 * Copyright (C) 2008 i-TEC HANKYU HANSHIN INC. All Rights Reserved.
 *
 */

package jp.co.itechh.quad.core.service.memberinfo.password;

import jp.co.itechh.quad.core.entity.memberinfo.MemberInfoEntity;

/**
 * パスワード再設定会員情報取得サービス<br/>
 *
 * @author natume
 * @version $Revision: 1.1 $
 *
 */
public interface PasswordResetMemberInfoGetService {

    // SMP0003

    /**
     * 確認メール情報取得失敗エラー<br/>
     * <code>MSGCD_CONFIRMMAILENTITYDTO_NULL</code>
     */
    String MSGCD_CONFIRMMAILENTITYDTO_NULL = "SMP000301";

    /**
     * 会員情報取得失敗エラー<br/>
     * <code>MSGCD_MEMBERINFOENTITYDTO_NULL</code>
     */
    String MSGCD_MEMBERINFOENTITYDTO_NULL = "SMP000302";

    /**
     * パスワード再設定会員情報取得処理<br/>
     *
     * @param password パスワード
     * @return 会員エンティティ
     */
    MemberInfoEntity execute(String password);
}
