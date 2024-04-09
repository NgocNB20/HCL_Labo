/*
 * Project Name : HIT-MALL4
 *
 * Copyright (C) 2021 i-TEC HANKYU HANSHIN INC. All Rights Reserved.
 *
 */

package jp.co.itechh.quad.front.pc.web.front.login;

import jp.co.itechh.quad.front.web.AbstractModel;
import lombok.Data;

/**
 * 会員ログインModel<br/>
 *
 * @author kaneda
 */
@Data
public class LoginModel extends AbstractModel {

    /**
     * 初期値の設定
     */
    public LoginModel() {
        autoLoginFlg = true;
    }

    /**
     * 会員ID
     */
    private String memberInfoId;

    /**
     * パスワード
     */
    private String memberInfoPassWord;

    /**
     * 自動ログインフラグ
     */
    private boolean autoLoginFlg;

    /**
     * アカウントロック機能が利用可能かどうか<br/>
     *
     * @return true..利用可能
     */
    public boolean isAvailableAccountLock() {
        // MemberInfoUtility memberInfoUtility =
        // ApplicationContextUtility.getBean(MemberInfoUtility.class);
        // return memberInfoUtility.isAvailableAccountLock();
        return false;
    }

}