/*
 * Project Name : HIT-MALL3
 *
 * Copyright (C) 2008 i-TEC HANKYU HANSHIN INC. All Rights Reserved.
 *
 */

package jp.co.itechh.quad.admin.pc.web.admin.shop.delivery;

import jp.co.itechh.quad.admin.annotation.exception.HEHandler;
import jp.co.itechh.quad.admin.base.exception.AppLevelListException;
import jp.co.itechh.quad.admin.entity.shop.delivery.DeliveryMethodEntity;
import jp.co.itechh.quad.admin.web.AbstractController;
import jp.co.itechh.quad.method.presentation.api.ShippingMethodApi;
import jp.co.itechh.quad.method.presentation.api.param.ShippingMethodListResponse;
import jp.co.itechh.quad.method.presentation.api.param.ShippingMethodListUpdateRequest;
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

import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * 配送方法設定 Controller
 *
 * @author Pham Quang Dieu (VTI Japan Co., Ltd.)
 */
@RequestMapping("/delivery")
@Controller
@SessionAttributes(value = "deliveryModel")
@PreAuthorize("hasAnyAuthority('SETTING:4')")
public class DeliveryController extends AbstractController {

    /** ロガー */
    private static final Logger LOGGER = LoggerFactory.getLogger(DeliveryController.class);

    /**
     * メッセージコード：不正操作
     */
    protected static final String MSGCD_ILLEGAL_OPERATION = "AYD000101";

    /**
     * メッセージコード：表示順の保存に成功
     */
    protected static final String MSGCD_SAVE_SUCCESS = "AYD000102";

    /**
     * リスト画面から
     */
    public static final String FLASH_FROM_LIST = "fromList";

    /**
     * 配送方法設定 Helper
     */
    private DeliveryHelper deliveryHelper;

    /**
     * 配送方法Api
     */
    private ShippingMethodApi shippingMethodApi;

    /**
     * コンストラクタ
     *
     * @param deliveryHelper    配送方法設定 Helper
     * @param shippingMethodApi 配送方法Api
     */
    @Autowired
    public DeliveryController(DeliveryHelper deliveryHelper, ShippingMethodApi shippingMethodApi) {
        this.deliveryHelper = deliveryHelper;
        this.shippingMethodApi = shippingMethodApi;
    }

    /**
     * 配送方法設定:初期処理
     *
     * @param deliveryModel 配送方法設定画面モデル
     * @param model         Model
     * @return 配送方法設定画面
     */
    @GetMapping(value = "/")
    @HEHandler(exception = AppLevelListException.class, returnView = "delivery/index")
    public String doLoadIndex(DeliveryModel deliveryModel, BindingResult error, Model model) {
        if (!model.containsAttribute(FLASH_FROM_LIST)) {
            clearModel(DeliveryModel.class, deliveryModel, model);
        }
        try {
            // 配送方法エンティティリスト取得サービス実行
            ShippingMethodListResponse shippingMethodListResponse = shippingMethodApi.get();
            List<DeliveryMethodEntity> deliveryMethodEntityList =
                            deliveryHelper.toDeliveryMethodEntityList(shippingMethodListResponse);
            // 取得したリストをページに設定
            deliveryHelper.toPageForLoad(deliveryMethodEntityList, deliveryModel);
        } catch (HttpServerErrorException | HttpClientErrorException e) {
            LOGGER.error("例外処理が発生しました", e);
            Map<String, String> itemNameAdjust = new HashMap<>();
            handleError(e.getStatusCode(), e.getResponseBodyAsString(), error, itemNameAdjust);
        }
        return "delivery/index";
    }

    /**
     * 「一つ上へ移動」押下処理
     *
     * @param deliveryModel 配送方法設定画面モデル
     * @param model         Model
     * @return 配送方法設定画面
     */
    @PreAuthorize("hasAnyAuthority('SETTING:8')")
    @PostMapping(value = "/", params = "doMoveOneUp")
    @HEHandler(exception = AppLevelListException.class, returnView = "delivery/index")
    public String doMoveOneUp(@Validated DeliveryModel deliveryModel, BindingResult error, Model model) {

        if (error.hasErrors()) {
            return "delivery/index";
        }

        // セッションチェック
        checkSessionInfo(deliveryModel);
        // 入力値チェック
        checkSelectedValue(deliveryModel);

        List<DeliveryResultItem> deliveryMethodItems = deliveryModel.getDeliveryMethodItems();

        for (int i = 0; i < deliveryMethodItems.size(); i++) {
            DeliveryResultItem deliveryResultItem = deliveryMethodItems.get(i);
            // 選択された配送方法かどうか
            if (deliveryResultItem.getDeliveryMethodRadioValue() != null
                && deliveryResultItem.getDeliveryMethodRadioValue()
                                     .toString()
                                     .equals(deliveryModel.getDeliveryMethodRadio())) {
                // ２つ目以降の配送方法かどうか
                // 先頭の配送方法の場合は処理しない
                if (i > 0) {
                    // 選択された配送方法の１つ前の配送方法を取得
                    DeliveryResultItem previousDeliveryMethodItem = deliveryMethodItems.get(i - 1);
                    Integer previousOrderDisplay = previousDeliveryMethodItem.getOrderDisplay();

                    previousDeliveryMethodItem.setOrderDisplay(deliveryResultItem.getOrderDisplay());
                    deliveryResultItem.setOrderDisplay(previousOrderDisplay);
                    deliveryMethodItems.set(i, previousDeliveryMethodItem);
                    deliveryMethodItems.set(i - 1, deliveryResultItem);

                }
                break;
            }
        }
        return "delivery/index";
    }

    /**
     * 「一つ下へ移動」押下処理
     *
     * @param deliveryModel 配送方法設定画面モデル
     * @param model         Model
     * @return 配送方法設定画面
     */
    @PreAuthorize("hasAnyAuthority('SETTING:8')")
    @PostMapping(value = "/", params = "doMoveOneDown")
    @HEHandler(exception = AppLevelListException.class, returnView = "delivery/index")
    public String doMoveOneDown(@Validated DeliveryModel deliveryModel, BindingResult error, Model model) {

        if (error.hasErrors()) {
            return "delivery/index";
        }

        // セッションチェック
        checkSessionInfo(deliveryModel);
        // 入力値チェック
        checkSelectedValue(deliveryModel);

        List<DeliveryResultItem> deliveryMethodItems = deliveryModel.getDeliveryMethodItems();

        for (int i = 0; i < deliveryMethodItems.size(); i++) {
            DeliveryResultItem deliveryResultItem = deliveryMethodItems.get(i);
            // 選択された配送方法かどうか
            if (deliveryResultItem.getDeliveryMethodRadioValue() != null
                && deliveryResultItem.getDeliveryMethodRadioValue()
                                     .toString()
                                     .equals(deliveryModel.getDeliveryMethodRadio())) {
                // 選択された配送方法が最後の配送方法の場合処理しない
                if (i < deliveryMethodItems.size() - 1) {
                    // 選択された配送方法の１つ後の配送方法を取得
                    DeliveryResultItem nextDeliveryMethodItem = deliveryMethodItems.get(i + 1);
                    Integer nextOrderDisplay = nextDeliveryMethodItem.getOrderDisplay();

                    nextDeliveryMethodItem.setOrderDisplay(deliveryResultItem.getOrderDisplay());
                    deliveryResultItem.setOrderDisplay(nextOrderDisplay);
                    deliveryMethodItems.set(i, nextDeliveryMethodItem);
                    deliveryMethodItems.set(i + 1, deliveryResultItem);

                }
                break;
            }
        }
        return "delivery/index";
    }

    /**
     * 「一番上へ移動」押下処理
     *
     * @param deliveryModel 配送方法設定画面モデル
     * @param model         Model
     * @return 配送方法設定画面
     */
    @PreAuthorize("hasAnyAuthority('SETTING:8')")
    @PostMapping(value = "/", params = "doMoveHead")
    @HEHandler(exception = AppLevelListException.class, returnView = "delivery/index")
    public String doMoveHead(@Validated DeliveryModel deliveryModel, BindingResult error, Model model) {

        if (error.hasErrors()) {
            return "delivery/index";
        }

        // セッションチェック
        checkSessionInfo(deliveryModel);
        // 入力値チェック
        checkSelectedValue(deliveryModel);

        moveHead(deliveryModel);
        return "delivery/index";
    }

    /**
     * 「一番下へ移動」押下処理
     *
     * @param deliveryModel 配送方法設定画面モデル
     * @param model         Model
     * @return 配送方法設定画面
     */
    @PreAuthorize("hasAnyAuthority('SETTING:8')")
    @PostMapping(value = "/", params = "doMoveFoot")
    @HEHandler(exception = AppLevelListException.class, returnView = "delivery/index")
    public String doMoveFoot(@Validated DeliveryModel deliveryModel, BindingResult error, Model model) {

        if (error.hasErrors()) {
            return "delivery/index";
        }

        // セッションチェック
        checkSessionInfo(deliveryModel);
        // 入力値チェック
        checkSelectedValue(deliveryModel);

        Collections.reverse(deliveryModel.getDeliveryMethodItems());
        moveHead(deliveryModel);
        Collections.reverse(deliveryModel.getDeliveryMethodItems());
        return "delivery/index";
    }

    /**
     * 「表示順を保存する」押下処理
     *
     * @param deliveryModel 配送方法設定画面モデル
     * @param redirectAttrs RedirectAttributes
     * @param model         Model
     * @return 配送方法設定画面
     */
    @PreAuthorize("hasAnyAuthority('SETTING:8')")
    @PostMapping(value = "/", params = "doOnceSave")
    @HEHandler(exception = AppLevelListException.class, returnView = "delivery/index")
    public String doOnceSave(DeliveryModel deliveryModel,
                             BindingResult error,
                             RedirectAttributes redirectAttrs,
                             Model model) {
        // セッションチェック
        checkSessionInfo(deliveryModel);

        try {
            // 配送方法更新サービス実行
            ShippingMethodListUpdateRequest shippingMethodListUpdateRequest =
                            deliveryHelper.toDeliveryMethodEntityListForSave(deliveryModel);

            ShippingMethodListResponse shippingMethodListResponse =
                            shippingMethodApi.updateList(shippingMethodListUpdateRequest);

            addInfoMessage(MSGCD_SAVE_SUCCESS, null, redirectAttrs, model);
            redirectAttrs.addFlashAttribute(FLASH_FROM_LIST, true);
        } catch (HttpServerErrorException | HttpClientErrorException e) {
            LOGGER.error("例外処理が発生しました", e);
            Map<String, String> itemNameAdjust = new HashMap<>();
            handleError(e.getStatusCode(), e.getResponseBodyAsString(), error, itemNameAdjust);
        }
        if (error.hasErrors()) {
            return "delivery/index";
        }
        return "redirect:/delivery/";
    }

    /**
     * 入力値のチェック
     *
     * @param deliveryModel 配送方法設定画面モデル
     */
    protected void checkSelectedValue(DeliveryModel deliveryModel) {
        for (DeliveryResultItem deliveryResultItem : deliveryModel.getDeliveryMethodItems()) {
            if (deliveryResultItem.getDeliveryMethodRadioValue() != null
                && deliveryResultItem.getDeliveryMethodRadioValue()
                                     .toString()
                                     .equals(deliveryModel.getDeliveryMethodRadio())) {
                return;
            }
        }

        throwMessage(MSGCD_ILLEGAL_OPERATION);
    }

    /**
     * 選択された配送方法を先頭に移動します。
     *
     * @param deliveryModel 配送方法設定画面モデル
     */
    protected void moveHead(DeliveryModel deliveryModel) {
        // セッションチェック
        checkSessionInfo(deliveryModel);

        List<DeliveryResultItem> deliveryMethodItems = deliveryModel.getDeliveryMethodItems();
        Integer headOrderDisplay = 0;
        for (int i = 0; i < deliveryMethodItems.size(); i++) {
            DeliveryResultItem deliveryResultItem = deliveryMethodItems.get(i);
            if (i == 0) {
                headOrderDisplay = deliveryResultItem.getOrderDisplay();
            }
            // 選択された配送方法かどうか
            if (deliveryResultItem.getDeliveryMethodRadioValue() != null
                && deliveryResultItem.getDeliveryMethodRadioValue()
                                     .toString()
                                     .equals(deliveryModel.getDeliveryMethodRadio())) {
                // ２つ目以降の配送方法かどうか
                // 先頭の配送方法の場合は処理しない
                if (i > 0) {
                    // 選択された配送方法があった場所から消して、先頭に移す。
                    DeliveryResultItem selectedDeliveryMethodItem = deliveryMethodItems.remove(i);
                    selectedDeliveryMethodItem.setOrderDisplay(headOrderDisplay);
                    deliveryMethodItems.add(0, selectedDeliveryMethodItem);
                }
                break;

                // 選択された配送方法でない
            } else {
                // 最後の配送方法でない
                if (i < deliveryMethodItems.size() - 1) {
                    // １つ後の配送方法を取得
                    DeliveryResultItem nextDeliveryMethodItem = deliveryMethodItems.get(i + 1);
                    Integer nextOrderDisplay = nextDeliveryMethodItem.getOrderDisplay();
                    deliveryResultItem.setOrderDisplay(nextOrderDisplay);
                }
            }
        }
    }

    /**
     * セッション情報のチェック<br/>
     * 不正操作防止
     *
     * @param deliveryModel 配送方法設定画面モデル
     */
    protected void checkSessionInfo(DeliveryModel deliveryModel) {
        if (deliveryModel.getDeliveryMethodItems() == null) {
            throwMessage(MSGCD_ILLEGAL_OPERATION);
        }
    }
}