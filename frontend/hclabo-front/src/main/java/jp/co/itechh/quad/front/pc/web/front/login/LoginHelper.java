/*
 * Project Name : HIT-MALL4
 *
 * Copyright (C) 2021 i-TEC HANKYU HANSHIN INC. All Rights Reserved.
 *
 */
package jp.co.itechh.quad.front.pc.web.front.login;

import jp.co.itechh.quad.front.base.utility.ApplicationContextUtility;
import jp.co.itechh.quad.front.utility.CommonInfoUtility;
import org.springframework.stereotype.Component;

/**
 * ログインHelper<br/>
 *
 * @author kaneda
 *
 */
@Component
public class LoginHelper {

    /**
     * ページ変換、初期表示<br/>
     *
     * @param loginModel
     */
    public void toPageForLoad(LoginModel loginModel) {

        // 共通情報Helper取得
        CommonInfoUtility commonInfoUtility = ApplicationContextUtility.getBean(CommonInfoUtility.class);

        // ログインしている場合 idをセット
        if (commonInfoUtility.isLogin(loginModel.getCommonInfo())) {
            loginModel.setMemberInfoId(loginModel.getCommonInfo().getCommonInfoUser().getMemberInfoId());
        }
    }
}