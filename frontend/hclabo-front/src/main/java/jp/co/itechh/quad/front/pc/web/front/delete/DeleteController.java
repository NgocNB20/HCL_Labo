/*
 * Copyright (C) 2022 i-TEC HANKYU HANSHIN INC. All Rights Reserved.
 */

package jp.co.itechh.quad.front.pc.web.front.delete;

import jp.co.itechh.quad.front.web.AbstractController;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;

/**
 * 登録解除 controller
 *
 * @author kimura
 */
@RequestMapping("/delete")
@Controller
public class DeleteController extends AbstractController {

    /**
     * 完了画面：画面表示処理<br/>
     * 認証外
     *
     * @return 完了画面
     */
    @GetMapping(value = "/complete")
    public String doLoadComplete() {

        return "delete/complete";
    }
}
