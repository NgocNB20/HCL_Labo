/*
 * Project Name : HIT-MALL4
 *
 * Copyright (C) 2021 i-TEC HANKYU HANSHIN INC. All Rights Reserved.
 *
 */

package jp.co.itechh.quad.front.web;

import jp.co.itechh.quad.front.application.commoninfo.CommonInfo;
import jp.co.itechh.quad.front.base.utility.ApplicationContextUtility;
import jp.co.itechh.quad.wishlist.presentation.api.WishlistApi;
import jp.co.itechh.quad.wishlist.presentation.api.param.WishlistRegistResponse;
import org.apache.commons.lang3.ObjectUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.client.HttpClientErrorException;
import org.springframework.web.client.HttpServerErrorException;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * お気に入り商品登録コントローラ
 *
 * @author kimura
 */
@RestController
@RequestMapping("/")
public class WishlistRegistController extends AbstractController {

    /** ロガー */
    private static final Logger LOGGER = LoggerFactory.getLogger(WishlistRegistController.class);

    /**
     * お気に入りエンドポイント API
     **/
    private final WishlistApi wishlistApi;

    public WishlistRegistController(WishlistApi wishlistApi) {
        this.wishlistApi = wishlistApi;
    }

    /**
     * お気に入り商品登録
     *
     * @param gcd 商品コード
     * @return wishlistMapList
     */
    @GetMapping("/getWishlistRegisterResponse")
    @ResponseBody
    public List<Map<String, String>> getWishlistRegisterResponse(
                    @RequestParam(name = "gcd", required = false) String gcd) {
        List<Map<String, String>> wishlistMapList = new ArrayList<>();
        Map<String, String> wishlistMap = new HashMap<>();

        // CommonInfo取得
        CommonInfo commonInfo = ApplicationContextUtility.getBean(CommonInfo.class);

        Integer memberInfoSeq = commonInfo.getCommonInfoUser().getMemberInfoSeq();

        String result = "false";
        if (gcd != null && memberInfoSeq != null) {
            try {
                WishlistRegistResponse response = wishlistApi.regist(gcd);
                if (ObjectUtils.isNotEmpty(response)) {
                    result = "true";
                }
            } catch (HttpClientErrorException | HttpServerErrorException e) {
                LOGGER.error("例外処理が発生しました", e);
                result = "false";
            }
        }

        wishlistMap.put("wishlistRegistSuccess", result);
        wishlistMap.put("gcd", gcd);

        wishlistMapList.add(wishlistMap);
        return wishlistMapList;
    }
}