/*
 * Copyright (C) 2022 i-TEC HANKYU HANSHIN INC. All Rights Reserved.
 */

package jp.co.itechh.quad.front.pc.web.front;

import jp.co.itechh.quad.freearea.presentation.api.FreeareaApi;
import jp.co.itechh.quad.freearea.presentation.api.param.FreeAreaGetRequest;
import jp.co.itechh.quad.freearea.presentation.api.param.FreeAreaResponse;
import jp.co.itechh.quad.front.annotation.exception.HEHandler;
import jp.co.itechh.quad.front.base.exception.AppLevelListException;
import jp.co.itechh.quad.front.entity.shop.FreeAreaEntity;
import jp.co.itechh.quad.front.web.AbstractController;
import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.client.HttpClientErrorException;
import org.springframework.web.client.HttpServerErrorException;

import java.util.HashMap;
import java.util.Map;

/**
 * 特集ページ Controller
 *
 * @author Pham Quang Dieu
 */
@RequestMapping("/")
@Controller
public class SpecialController extends AbstractController {

    /** ロガー */
    private static final Logger LOGGER = LoggerFactory.getLogger(SpecialController.class);

    /** 特集ページHelper */
    private final SpecialHelper specialHelper;

    /** フリーエリアAPI */
    private final FreeareaApi freeareaApi;

    /**
     * コンストラクタ
     *
     * @param specialHelper 特集ページHelper
     * @param freeareaApi   フリーエリアAPI
     */
    @Autowired
    public SpecialController(SpecialHelper specialHelper, FreeareaApi freeareaApi) {
        this.specialHelper = specialHelper;
        this.freeareaApi = freeareaApi;
    }

    /**
     * 特集ページ画面：初期処理
     *
     * @param specialModel
     * @param model
     * @return 特集ページ画面
     */
    @GetMapping(value = "/special")
    @HEHandler(exception = AppLevelListException.class, returnView = "special")
    protected String doLoadIndex(SpecialModel specialModel, BindingResult error, Model model) {

        // 特集ページ用フリーエリア取得
        FreeAreaEntity freeAreaEntity = null;

        // 通常モード
        String fKey = specialModel.getFkey();

        if (StringUtils.isNotEmpty(fKey)) {
            FreeAreaGetRequest freeAreaGetRequest = new FreeAreaGetRequest();
            freeAreaGetRequest.setFreeAreaKey(specialModel.getFkey());

            FreeAreaResponse freeAreaResponse = null;
            try {
                freeAreaResponse = freeareaApi.getByFreeAreaKey(freeAreaGetRequest);
            } catch (HttpClientErrorException | HttpServerErrorException e) {
                LOGGER.error("例外処理が発生しました", e);
                // アイテム名調整マップ
                Map<String, String> itemNameAdjust = new HashMap<>();
                itemNameAdjust.put("freeAreaKey", "fkey");
                handleError(e.getStatusCode(), e.getResponseBodyAsString(), error, itemNameAdjust);
            }

            if (error.hasErrors()) {
                return "special";
            }
            freeAreaEntity = specialHelper.toFreeAreaEntity(freeAreaResponse);
        }

        if (freeAreaEntity == null) {
            // 特集ページ用フリーエリアが非公開または存在しない
            LOGGER.debug("存在しない、もしくは公開されていない特集ページが選択されました");
            return "redirect:/error";
        }

        specialHelper.toPageForLoad(freeAreaEntity, specialModel);

        return "special";

    }
}