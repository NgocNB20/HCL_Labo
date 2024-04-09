/*
 * Project Name : HIT-MALL3
 *
 * Copyright (C) 2008 i-TEC HANKYU HANSHIN INC. All Rights Reserved.
 *
 */

package jp.co.itechh.quad.admin.pc.web.admin.shop.shopinfo;

import jp.co.itechh.quad.admin.annotation.exception.HEHandler;
import jp.co.itechh.quad.admin.base.exception.AppLevelListException;
import jp.co.itechh.quad.admin.entity.shop.ShopEntity;
import jp.co.itechh.quad.admin.web.AbstractController;
import jp.co.itechh.quad.shop.presentation.api.ShopApi;
import jp.co.itechh.quad.shop.presentation.api.param.ShopResponse;
import jp.co.itechh.quad.shop.presentation.api.param.ShopUpdateRequest;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.SessionAttributes;
import org.springframework.web.client.HttpClientErrorException;
import org.springframework.web.client.HttpServerErrorException;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import java.util.HashMap;
import java.util.Map;

@RequestMapping("/shopinfo")
@Controller
@SessionAttributes(value = "shopInfoModel")
@PreAuthorize("hasAnyAuthority('SETTING:4')")
public class ShopInfoController extends AbstractController {

    /** ロガー */
    private static final Logger LOGGER = LoggerFactory.getLogger(ShopInfoController.class);

    /**
     * ショップ helper
     */
    private final ShopInfoHelper shopInfoHelper;

    /**
     * 店舗Api
     */
    private final ShopApi shopApi;

    public static final String MSGCD_ILLEGAL_OPERATION = "AOX000301";

    /**
     * エラーメッセージコード：時間帯の値をいじられた
     */
    public static final String MSGCD_ILLEGAL_TIME = "AOX000302";

    /**
     * ショップ情報 Controller
     * <p>
     * shopInfoHelper helper
     * shopApi 店舗Api
     */
    @Autowired
    public ShopInfoController(ShopInfoHelper shopInfoHelper, ShopApi shopApi) {
        this.shopInfoHelper = shopInfoHelper;
        this.shopApi = shopApi;
    }

    /**
     * 初期処理
     *
     * @return 自画面(エラー時 、 検索画面)
     */
    @GetMapping("/")
    @HEHandler(exception = AppLevelListException.class, returnView = "shopinfo/index")
    protected String doLoadIndex(ShopInfoModel shopInfoModel, BindingResult error, Model model) {
        clearModel(ShopInfoModel.class, shopInfoModel, model);
        try {
            ShopResponse shopResponse = shopApi.get();
            shopInfoModel.setShopEntity(shopInfoHelper.toShopEntity(shopResponse));
            shopInfoHelper.toPageForLoadUpdate(shopInfoModel, shopInfoModel.getShopEntity());
        } catch (HttpServerErrorException | HttpClientErrorException e) {
            LOGGER.error("例外処理が発生しました", e);
            // 取得失敗時、エラー画面へ遷移
            Map<String, String> itemNameAdjust = new HashMap<>();
            handleError(e.getStatusCode(), e.getResponseBodyAsString(), error, itemNameAdjust);
        }

        // ページ反映
        return "shopinfo/index";
    }

    /**
     * 店舗情報設定画面へ遷移
     *
     * @return 更新画面
     */
    @PreAuthorize("hasAnyAuthority('SETTING:8')")
    @PostMapping(value = "/update", params = "doUpdatePage")
    public String doUpdatePage() {
        return "redirect:/shopinfo/update";
    }

    @GetMapping(value = "/update")
    @HEHandler(exception = AppLevelListException.class, returnView = "shopinfo/update")
    @PreAuthorize("hasAnyAuthority('SETTING:8')")
    protected String doLoadUpdate(ShopInfoModel shopInfoModel, BindingResult error, Model model) {

        // ブラウザバックの場合、処理しない
        if (shopInfoModel.getShopEntity() == null) {
            return "redirect:/shopinfo/";
        }
        clearModel(ShopInfoModel.class, shopInfoModel, model);

        // ショップ情報・会社情報を取得しなおす
        try {
            ShopResponse shopResponse = shopApi.get();
            shopInfoModel.setShopEntity(shopInfoHelper.toShopEntity(shopResponse));
        } catch (HttpServerErrorException | HttpClientErrorException e) {
            LOGGER.error("例外処理が発生しました", e);
            // 取得失敗時、エラー画面へ遷移
            Map<String, String> itemNameAdjust = new HashMap<>();
            handleError(e.getStatusCode(), e.getResponseBodyAsString(), error, itemNameAdjust);
        }
        if (error.hasErrors()) {
            return "shopinfo/update";
        }

        // ページ反映
        shopInfoHelper.toPageForLoadUpdate(shopInfoModel, shopInfoModel.getShopEntity());
        return "shopinfo/update";
    }

    /**
     * 店舗情報更新処理
     * 正常終了後は店舗情報詳細画面へ遷移
     *
     * @return 店舗情報詳細画面
     */
    @PreAuthorize("hasAnyAuthority('SETTING:8')")
    @PostMapping(value = "/", params = "doOnceUpdate")
    @HEHandler(exception = AppLevelListException.class, returnView = "redirect:/error")
    public String doOnceUpdate(@Validated ShopInfoModel shopInfoModel,
                               BindingResult error,
                               RedirectAttributes redirectAttributes,
                               Model model) {

        if (error.hasErrors()) {
            return "shopinfo/update";
        }
        // 入力情報チェック
        //        checkData(shopInfoModel, model);
        // 不正遷移チェック
        checkPageDto(shopInfoModel, model);

        // 更新用Dtoを生成して画面にセット
        shopInfoHelper.toShopEntityForUpdate(shopInfoModel);

        ShopEntity shopEntity = shopInfoModel.getShopEntity();

        ShopUpdateRequest shopUpdateRequest = shopInfoHelper.toShopUpdateRequest(shopEntity);
        try {
            shopApi.update(shopUpdateRequest);
        } catch (HttpServerErrorException | HttpClientErrorException e) {
            LOGGER.error("例外処理が発生しました", e);
            Map<String, String> itemNameAdjust = new HashMap<>();
            handleError(e.getStatusCode(), e.getResponseBodyAsString(), error, itemNameAdjust);
        }

        if (error.hasErrors()) {
            return "shopinfo/update";
        }

        // 更新処理完了後、詳細画面に戻る
        return "redirect:/shopinfo/";
    }

    /**
     * キャンセル処理
     * 店舗情報詳細画面へ遷移
     *
     * @return 店舗情報詳細画面
     */
    @PreAuthorize("hasAnyAuthority('SETTING:8')")
    @PostMapping(value = "/", params = "doCancel")
    public String doCancel() {
        // 詳細画面に戻る
        return "redirect:/shopinfo/";
    }

    /**
     * 不正遷移かどうかをチェック<br/>
     */
    protected void checkPageDto(ShopInfoModel shopInfoModel, Model model) {
        if (shopInfoModel.getShopEntity() == null || shopInfoModel.getShopEntity().getShopSeq() == null) {
            throwMessage("ASC000102");
        }
    }

}