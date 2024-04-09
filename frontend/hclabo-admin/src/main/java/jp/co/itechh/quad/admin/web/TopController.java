/*
 * Project Name : HIT-MALL4
 *
 * Copyright (C) 2008 i-TEC HANKYU HANSHIN INC. All Rights Reserved.
 *
 */

package jp.co.itechh.quad.admin.web;

import jp.co.itechh.quad.admin.base.utility.ApplicationContextUtility;
import jp.co.itechh.quad.admin.utility.CheckPermissionUtility;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;

/**
 * TOPコントローラ
 *
 * @author kimura
 */
@Controller
public class TopController extends AbstractController {

    /**
     * 表示対象となる初期画面に遷移<br/>
     * 「admin/」でアクセスされた場合に、管理サイトはデフォルト画面が存在しないため、権限レベルごとの遷移先を指定する<br/>
     * ※管理サイトはログイン/ログアウト画面以外全て要認証のため、こちらのメソッド呼び出し直前もログイン認証が行われる
     *
     * @return リダイレクト先画面
     */
    @RequestMapping(value = "/", method = {RequestMethod.GET})
    public String doLoadIndex() {
        CheckPermissionUtility checkPermissionUtility = ApplicationContextUtility.getBean(CheckPermissionUtility.class);
        return "redirect:" + checkPermissionUtility.getTargetUrl();
    }

}
