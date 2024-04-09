/*
 * Project Name : HIT-MALL3
 *
 * Copyright (C) 2008 i-TEC HANKYU HANSHIN INC. All Rights Reserved.
 *
 */

package jp.co.itechh.quad.core.service.memberinfo.temp;

import jp.co.itechh.quad.core.entity.memberinfo.confirmmail.ConfirmMailEntity;

/**
 * 仮会員登録サービス<br/>
 *
 * @author natume
 * @version $Revision: 1.2 $
 *
 */
public interface TempMemberInfoRegistService {

    /**
     * 確認メール情報が登録失敗<br/>
     * <code>MSGCD_CONFIRMMAIL_REGIST_FAIL</code>
     */
    String MSGCD_CONFIRMMAIL_REGIST_FAIL = "SMT000101";

    /**
     * 仮会員登録処理<br/>
     *
     * @param mail メールアドレス
     * @param orderSeq 受注SEQ
     * @return メール送信結果
     */
    boolean execute(String mail, Integer orderSeq);

    /**
     * 仮会員登録処理<br/>
     *
     * @param mail メールアドレス
     * @param orderSeq 受注SEQ
     * @return 確認メール情報
     */
    ConfirmMailEntity executeMemberRegist(String mail, Integer orderSeq);
}
