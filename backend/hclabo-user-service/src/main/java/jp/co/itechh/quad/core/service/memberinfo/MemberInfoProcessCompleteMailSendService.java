/*
 * Project Name : HIT-MALL3
 *
 * Copyright (C) 2008 i-TEC HANKYU HANSHIN INC. All Rights Reserved.
 *
 */

package jp.co.itechh.quad.core.service.memberinfo;

import jp.co.itechh.quad.core.constant.type.HTypeMailTemplateType;

/**
 * 会員処理完了メール送信サービス<br/>
 *
 * @author natume
 * @version $Revision: 1.1 $
 *
 */
public interface MemberInfoProcessCompleteMailSendService {

    /**
     * 会員情報取得失敗エラー<br/>
     * <code>MSGCD_MEMBERINFOENTITYDTO_NULL</code>
     */
    String MSGCD_MEMBERINFOENTITYDTO_NULL = "SMM001001";

    /**
     * 会員登録完了メール送信処理<br/>
     *
     * @param memberInfoSeq 会員SEQ
     * @param mailTemplateType メールテンプレートタイプ(会員登録完了/会員退会完了/会員メールアドレス変更)
     * @return メール送信結果
     */
    boolean execute(Integer memberInfoSeq, HTypeMailTemplateType mailTemplateType);
}
