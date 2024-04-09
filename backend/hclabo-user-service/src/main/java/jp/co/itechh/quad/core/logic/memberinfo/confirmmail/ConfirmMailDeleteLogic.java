/*
 * Project Name : HIT-MALL3
 *
 * Copyright (C) 2008 i-TEC HANKYU HANSHIN INC. All Rights Reserved.
 *
 */

package jp.co.itechh.quad.core.logic.memberinfo.confirmmail;

/**
 * 確認メール情報削除<br/>
 *
 * @author hasegawa(itec)
 *
 */
public interface ConfirmMailDeleteLogic {

    /**
     * 確認メール情報削除<br/>
     *
     * @param confirmMailPassword 確認メールパスワード
     * @return 削除件数
     */
    int execute(String confirmMailPassword);
}