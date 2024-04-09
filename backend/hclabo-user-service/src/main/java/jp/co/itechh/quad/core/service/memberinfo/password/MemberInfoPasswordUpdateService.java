/*
 * Project Name : HIT-MALL3
 *
 * Copyright (C) 2008 i-TEC HANKYU HANSHIN INC. All Rights Reserved.
 *
 */

package jp.co.itechh.quad.core.service.memberinfo.password;

import jp.co.itechh.quad.core.entity.memberinfo.MemberInfoEntity;

/**
 * 会員パスワード変更<br/>
 *
 * @author natume
 *
 */
public interface MemberInfoPasswordUpdateService {
    // SMP0001

    /**
     * パスワード照合エラー<br/>
     * <code>MSGCD_MEMBERINFOPASSWORD_MATCH_ERROR</code>
     */
    String MSGCD_MEMBERINFOPASSWORD_MATCH_ERROR = "SMP000101";
    /**
     * パスワード変更エラー<br/>
     * <code>MSGCD_MEMBERINFOPASSWORD_UPDATE_ERROR</code>
     */
    String MSGCD_MEMBERINFOPASSWORD_UPDATE_ERROR = "SMP000102";

    /**
     * 会員パスワード変更処理<br/>
     * パスワード変更用<br/>
     *
     * @param memberInfoEntity 会員情報エンティティ
     * @param password 現パスワード
     * @param newPassword 新パスワード
     * @return 更新件数
     */
    int execute(MemberInfoEntity memberInfoEntity, String password, String newPassword);

    /**
     * 会員パスワード変更処理<br/>
     * パスワードリセット用(確認メール)<br/>
     *
     * @param confirmMailPassword 確認メールパスワード
     * @param memberInfoEntity 会員エンティティ
     * @param newPassword 新パスワード
     * @return 更新件数
     */
    int execute(String confirmMailPassword, MemberInfoEntity memberInfoEntity, String newPassword);

}
