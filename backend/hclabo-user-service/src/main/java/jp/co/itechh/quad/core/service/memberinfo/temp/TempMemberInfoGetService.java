/*
 * Project Name : HIT-MALL3
 *
 * Copyright (C) 2008 i-TEC HANKYU HANSHIN INC. All Rights Reserved.
 *
 */

package jp.co.itechh.quad.core.service.memberinfo.temp;

import jp.co.itechh.quad.core.dto.memberinfo.TempMemberInfoDto;

/**
 * 仮会員情報取得サービス<br/>
 *
 * @author natume
 *
 */
public interface TempMemberInfoGetService {

    /**
     * 有効な確認メール取得エラー<br/>
     * <code>MSGCD_CONFIRMMAILENTITYDTO_NULL</code>
     */
    String MSGCD_CONFIRMMAILENTITYDTO_NULL = "SMT000201";

    /**
     * 仮会員情報を取得する<br/>
     *
     * @param password パスワード
     * @return 会員エンティティ
     */
    TempMemberInfoDto execute(String password);
}
