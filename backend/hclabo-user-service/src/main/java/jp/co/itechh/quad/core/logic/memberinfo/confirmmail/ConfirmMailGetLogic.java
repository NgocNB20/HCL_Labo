/*
 * Project Name : HIT-MALL3
 *
 * Copyright (C) 2008 i-TEC HANKYU HANSHIN INC. All Rights Reserved.
 *
 */

package jp.co.itechh.quad.core.logic.memberinfo.confirmmail;

import jp.co.itechh.quad.core.constant.type.HTypeConfirmMailType;
import jp.co.itechh.quad.core.entity.memberinfo.confirmmail.ConfirmMailEntity;

/**
 * 確認メール情報取得<br/>
 *
 * @author natume
 *
 */
public interface ConfirmMailGetLogic {
    /**
     * 確認メール情報取得<br/>
     *
     * @param password メールパスワード
     * @param confirmMailType 確認メール種別
     * @return 確認メールエンティティ
     */
    ConfirmMailEntity execute(String password, HTypeConfirmMailType confirmMailType);
}
