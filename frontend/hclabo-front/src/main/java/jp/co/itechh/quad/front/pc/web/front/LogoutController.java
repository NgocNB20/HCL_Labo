/*
 * Project Name : HIT-MALL3
 *
 * Copyright (C) 2008 i-TEC HANKYU HANSHIN INC. All Rights Reserved.
 *
 */

package jp.co.itechh.quad.front.pc.web.front;

import jp.co.itechh.quad.front.web.AbstractController;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;

/**
 * ログアウト Controller
 *
 * @author Doan Thang (VJP)
 *
 */
@Controller
public class LogoutController extends AbstractController {

    /**
     * ログアウト完了処理
     * @return TOP画面
     */
    @RequestMapping(value = "/logout-completed", method = {RequestMethod.GET})
    protected String doLoadIndex() {

        return "redirect:/";
    }
}
