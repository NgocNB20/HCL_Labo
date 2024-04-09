/*
 * Project Name : HIT-MALL4
 *
 * Copyright (C) 2021 i-TEC HANKYU HANSHIN INC. All Rights Reserved.
 *
 */

package jp.co.itechh.quad.admin.util;

import jp.co.itechh.quad.admin.web.AbstractController;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;

/**
 * マルチタブ画面用コントローラー<br />
 *
 * @author kimura
 */
@RequestMapping("/syncerror")
@Controller
public class SyncerrorController extends AbstractController {

    /**
     * 初期処理
     *
     * @return マルチタブ画面
     */
    @GetMapping("/")
    protected String doLoad(Model model) {

        return "syncerror";
    }
}
