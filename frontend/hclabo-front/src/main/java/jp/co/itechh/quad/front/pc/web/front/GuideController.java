/*
 * Copyright (C) 2022 i-TEC HANKYU HANSHIN INC. All Rights Reserved.
 */

package jp.co.itechh.quad.front.pc.web.front;

import jp.co.itechh.quad.front.base.utility.ApplicationContextUtility;
import jp.co.itechh.quad.front.web.AbstractController;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;

/**
 * ご利用ガイド Controller
 *
 * @author
 * @version $Revision: 1.0 $
 *
 */
@RequestMapping("/guide")
@Controller
public class GuideController extends AbstractController {

    /**
     * ご利用ガイド 画面 : 初期処理
     *
     * @param model
     * @return ご利用ガイド 画面
     */
    @GetMapping(value = "")
    protected String doLoadIndex(Model model) {

        return "guide/index";
    }

    /**
     * ご利用ガイド 各画面
     *
     * @param model
     * @param page
     * @return 該当画面
     */
    @GetMapping(value = "{page}")
    protected String doLoadPage(Model model, @PathVariable String page) {
        if (this.checkTemplateExist(page)) {
            return "guide/".concat(page);
        }

        return "error";
    }

    /**
     * テンプレートを存在するかチェック
     *
     * @param viewName
     * @return 存在する：true、存在しない：false
     */
    private boolean checkTemplateExist(String viewName) {
        return ApplicationContextUtility.getApplicationContext()
                                        .getResource("classpath:/templates/guide/" + viewName + ".html")
                                        .exists();
    }
}