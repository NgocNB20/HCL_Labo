/*
 * Project Name : HIT-MALL3
 *
 * Copyright (C) 2008 i-TEC HANKYU HANSHIN INC. All Rights Reserved.
 *
 */

package jp.co.itechh.quad.core.service.memberinfo;

import jp.co.itechh.quad.core.entity.memberinfo.MemberInfoEntity;

/**
 * 会員メールアドレス変更サービス(本登録)<br/>
 *
 * @author natume
 * @version $Revision: 1.1 $
 *
 */
public interface MemberInfoUpdateMailGetService {

    /* errorcode SMM0018 */

    /**
     * 確認メール情報取得エラー<br/>
     * <code>MSGCD_CONFIRMMAILENTITYDTO_NULL</code>
     */
    String MSGCD_CONFIRMMAILENTITYDTO_NULL = "SMM001801";

    /**
     * 会員情報取得エラー<br/>
     * <code>MSGCD_MEMBERINFOENTITYDTO_NULL</code>
     */
    String MSGCD_MEMBERINFOENTITYDTO_NULL = "SMM001802";

    /**
     * 会員メールアドレスが既に変更されている場合エラー<br/>
     * <code>MSGCD_ALREADY_MAIL_UPDATE_FAIL</code>
     */
    String MSGCD_ALREADY_MAIL_UPDATE_FAIL = "SMM001803";

    /**
     * 会員メールアドレス変更処理<br/>
     *
     * @param password パスワード
     * @return 会員エンティティ
     */
    MemberInfoEntity execute(String password);
}
