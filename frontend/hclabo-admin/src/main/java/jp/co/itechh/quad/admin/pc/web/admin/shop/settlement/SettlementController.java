package jp.co.itechh.quad.admin.pc.web.admin.shop.settlement;

import jp.co.itechh.quad.admin.annotation.exception.HEHandler;
import jp.co.itechh.quad.admin.base.exception.AppLevelListException;
import jp.co.itechh.quad.admin.entity.shop.settlement.SettlementMethodEntity;
import jp.co.itechh.quad.admin.pc.web.admin.common.validation.group.DisplayBottomGroup;
import jp.co.itechh.quad.admin.pc.web.admin.common.validation.group.DisplayDownGroup;
import jp.co.itechh.quad.admin.pc.web.admin.common.validation.group.DisplayTopGroup;
import jp.co.itechh.quad.admin.pc.web.admin.common.validation.group.DisplayUpGroup;
import jp.co.itechh.quad.admin.web.AbstractController;
import jp.co.itechh.quad.method.presentation.api.SettlementMethodApi;
import jp.co.itechh.quad.method.presentation.api.ShippingMethodApi;
import jp.co.itechh.quad.method.presentation.api.param.DeliveryMethodResponse;
import jp.co.itechh.quad.method.presentation.api.param.PaymentMethodListResponse;
import jp.co.itechh.quad.method.presentation.api.param.PaymentMethodListUpdateRequest;
import jp.co.itechh.quad.method.presentation.api.param.ShippingMethodListResponse;
import jp.co.itechh.quad.method.presentation.api.param.ShippingMethodResponse;
import org.apache.commons.collections.CollectionUtils;
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
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.SessionAttributes;
import org.springframework.web.client.HttpClientErrorException;
import org.springframework.web.client.HttpServerErrorException;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Optional;

/**
 * SettlementController Class
 *
 * @author Pham Quang Dieu (VTI Japan Co., Ltd.)
 */
@RequestMapping("/settlement")
@Controller
@SessionAttributes(value = "settlementModel")
@PreAuthorize("hasAnyAuthority('SETTING:4')")
public class SettlementController extends AbstractController {

    /** ロガー */
    private static final Logger LOGGER = LoggerFactory.getLogger(SettlementController.class);

    /**
     * メッセージコード：表示順の保存に成功
     */
    protected static final String MSGCD_SAVE_SUCCESS = "AYC000102";

    /**
     * リスト画面から
     */
    public static final String FLASH_FROM_LIST = "fromList";

    /**
     * 決済方法設定
     */
    private final SettlementHelper settlementHelper;

    /**
     * 決済方法Api
     */
    private final SettlementMethodApi settlementMethodApi;

    /**
     * 配送方法Api
     */
    private final ShippingMethodApi shippingMethodApi;

    /**
     * 決済コントローラー
     * <p>
     * settlementHelper 決済方法設定
     * settlementMethodApi 決済方法Api
     */
    @Autowired
    public SettlementController(SettlementMethodApi settlementMethodApi,
                                SettlementHelper settlementHelper,
                                ShippingMethodApi shippingMethodApi) {
        this.settlementMethodApi = settlementMethodApi;
        this.settlementHelper = settlementHelper;
        this.shippingMethodApi = shippingMethodApi;
    }

    /**
     * 画面ロード処理<br/>
     *
     * @return 自画面
     */
    @GetMapping(value = "/")
    @HEHandler(exception = AppLevelListException.class, returnView = "settlement/index")
    protected String doLoadIndex(@RequestParam(required = false) Optional<String> md,
                                 SettlementModel settlementModel,
                                 BindingResult error,
                                 Model model) {
        if (!model.containsAttribute(FLASH_FROM_LIST)) {
            clearModel(SettlementModel.class, settlementModel, model);
        }
        try {
            PaymentMethodListResponse paymentMethodListResponse = settlementMethodApi.get();
            List<SettlementMethodEntity> resultList = new ArrayList<>();
            if (paymentMethodListResponse.getPaymentMethodListResponse() != null) {
                resultList = settlementHelper.toSettlementMethodEntity(
                                paymentMethodListResponse.getPaymentMethodListResponse());
            }

            Map<Integer, String> deliveryMethodMap = this.getDeliveryMethodMap();
            settlementHelper.toPage(settlementModel, resultList, deliveryMethodMap);
            settlementModel.setOrderDisplay(null);
        } catch (HttpServerErrorException | HttpClientErrorException e) {
            LOGGER.error("例外処理が発生しました", e);
            Map<String, String> itemNameAdjust = new HashMap<>();
            handleError(e.getStatusCode(), e.getResponseBodyAsString(), error, itemNameAdjust);
        }
        return "settlement/index";
    }

    /**
     * 1つ上に移動<br />
     *
     * @return 自画面
     */
    @PreAuthorize("hasAnyAuthority('SETTING:8')")
    @PostMapping(value = "/", params = "doOrderDisplayUp")
    @HEHandler(exception = AppLevelListException.class, returnView = "settlement/index")
    public String doOrderDisplayUp(@Validated(DisplayUpGroup.class) SettlementModel settlementModel,
                                   BindingResult error) {
        if (error.hasErrors()) {
            return "settlement/index";
        }

        settlementHelper.changeDisplay(settlementModel, 0);
        return "settlement/index";
    }

    /**
     * 1つ下に移動<br />
     *
     * @return 自画面
     */
    @PreAuthorize("hasAnyAuthority('SETTING:8')")
    @PostMapping(value = "/", params = "doOrderDisplayDown")
    @HEHandler(exception = AppLevelListException.class, returnView = "settlement/index")
    public String doOrderDisplayDown(@Validated(DisplayDownGroup.class) SettlementModel settlementModel,
                                     BindingResult error) {
        if (error.hasErrors()) {
            return "settlement/index";
        }

        settlementHelper.changeDisplay(settlementModel, 1);
        return "settlement/index";
    }

    /**
     * 先頭に移動<br />
     *
     * @return 自画面
     */
    @PreAuthorize("hasAnyAuthority('SETTING:8')")
    @PostMapping(value = "/", params = "doOrderDisplayTop")
    @HEHandler(exception = AppLevelListException.class, returnView = "settlement/index")
    public String doOrderDisplayTop(@Validated(DisplayTopGroup.class) SettlementModel settlementModel,
                                    BindingResult error) {
        if (error.hasErrors()) {
            return "settlement/index";
        }

        settlementHelper.changeDisplay(settlementModel, 2);
        return "settlement/index";
    }

    /**
     * 末尾に移動<br />
     *
     * @return 自画面
     */
    @PreAuthorize("hasAnyAuthority('SETTING:8')")
    @PostMapping(value = "/", params = "doOrderDisplayBottom")
    @HEHandler(exception = AppLevelListException.class, returnView = "settlement/index")
    public String doOrderDisplayBottom(@Validated(DisplayBottomGroup.class) SettlementModel settlementModel,
                                       BindingResult error) {
        if (error.hasErrors()) {
            return "settlement/index";
        }

        settlementHelper.changeDisplay(settlementModel, 3);
        return "settlement/index";
    }

    /**
     * 表示順を保存<br />
     *
     * @return 自画面
     */
    @PreAuthorize("hasAnyAuthority('SETTING:8')")
    @PostMapping(value = "/", params = "doOnceOrderDisplayUpdate")
    @HEHandler(exception = AppLevelListException.class, returnView = "settlement/index")
    public String doOnceOrderDisplayUpdate(SettlementModel settlementModel,
                                           BindingResult error,
                                           RedirectAttributes redirectAttributes,
                                           Model model) {
        // 決済方法リスト取得
        PaymentMethodListUpdateRequest settlementMethodList =
                        settlementHelper.getSettlementMethodEntityList(settlementModel);

        // 更新処理実行
        try {
            settlementMethodApi.updateList(settlementMethodList);
        } catch (HttpServerErrorException | HttpClientErrorException e) {
            LOGGER.error("例外処理が発生しました", e);
            Map<String, String> itemNameAdjust = new HashMap<>();
            handleError(e.getStatusCode(), e.getResponseBodyAsString(), error, itemNameAdjust);
        }

        if (error.hasErrors()) {
            return "settlement/index";
        }

        addInfoMessage(MSGCD_SAVE_SUCCESS, null, redirectAttributes, model);
        redirectAttributes.addFlashAttribute(FLASH_FROM_LIST, true);
        return "redirect:/settlement/";
    }

    /**
     * 新規登録画面遷移<br/>
     *
     * @return 決済方法詳細設定画面
     */
    @PreAuthorize("hasAnyAuthority('SETTING:8')")
    @PostMapping(value = "/", params = "doRegist")
    public String doRegist() {
        return "redirect:/settlement/registupdate?mode=new";
    }

    /**
     * 配送方法名取得
     * @return 配送方法名
     */
    private Map<Integer, String> getDeliveryMethodMap() {

        Map<Integer, String> deliveryMethodMap = new HashMap<>();

        ShippingMethodListResponse shippingMethodListResponse = shippingMethodApi.get();

        if (CollectionUtils.isEmpty(shippingMethodListResponse.getShippingMethodListResponse())) {
            return deliveryMethodMap;
        }

        for (ShippingMethodResponse item : shippingMethodListResponse.getShippingMethodListResponse()) {
            DeliveryMethodResponse deliveryMethod = item.getDeliveryMethodResponse();
            if (deliveryMethod != null) {
                deliveryMethodMap.put(deliveryMethod.getDeliveryMethodSeq(), deliveryMethod.getDeliveryMethodName());
            }
        }

        return deliveryMethodMap;
    }

}