/*
 * Project Name : HIT-MALL4
 *
 * Copyright (C) 2021 i-TEC HANKYU HANSHIN INC. All Rights Reserved.
 *
 */

package jp.co.itechh.quad.front.pc.web.front.member;

import jp.co.itechh.quad.front.web.AbstractController;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.SessionAttributes;

/**
 * 会員メニューController<br/>
 *
 * @author PHAM QUANG DIEU (VJP)
 *
 */
@SessionAttributes(value = "memberModel")
@RequestMapping("/member")
@Controller
public class MemberController extends AbstractController {

    /**
     * メニュー画面：初期処理<br/>
     * @param memberModel
     * @param model
     * @return メニュー画面
     */
    @GetMapping(value = "/")
    protected String doLoadIndex(MemberModel memberModel, Model model) {

        // 初期化処理
        clearModel(MemberModel.class, memberModel, model);

        memberModel.setLastName(getCommonInfo().getCommonInfoUser().getMemberInfoLastName());
        memberModel.setFirstName(getCommonInfo().getCommonInfoUser().getMemberInfoFirstName());

        return "member/index";
    }
}
