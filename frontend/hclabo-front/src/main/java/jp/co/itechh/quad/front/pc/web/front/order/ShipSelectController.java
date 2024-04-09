package jp.co.itechh.quad.front.pc.web.front.order;

import jp.co.itechh.quad.front.annotation.exception.HEHandler;
import jp.co.itechh.quad.front.base.exception.AppLevelListException;
import jp.co.itechh.quad.front.pc.web.front.order.common.OrderCommonModel;
import jp.co.itechh.quad.front.pc.web.front.order.common.SalesSlipUtility;
import jp.co.itechh.quad.front.pc.web.front.order.validation.group.ShipSelectGroup;
import jp.co.itechh.quad.front.web.AbstractController;
import jp.co.itechh.quad.method.presentation.api.ShippingMethodApi;
import jp.co.itechh.quad.method.presentation.api.param.SelectableShippingMethodListGetRequest;
import jp.co.itechh.quad.method.presentation.api.param.SelectableShippingMethodListResponse;
import jp.co.itechh.quad.shippingslip.presentation.api.ShippingSlipApi;
import jp.co.itechh.quad.shippingslip.presentation.api.param.ShippingSlipGetRequest;
import jp.co.itechh.quad.shippingslip.presentation.api.param.ShippingSlipMethodUpdateRequest;
import jp.co.itechh.quad.shippingslip.presentation.api.param.ShippingSlipResponse;
import org.apache.commons.lang3.ObjectUtils;
import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.util.CollectionUtils;
import org.springframework.validation.BindingResult;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.SessionAttributes;
import org.springframework.web.bind.support.SessionStatus;
import org.springframework.web.client.HttpClientErrorException;
import org.springframework.web.client.HttpServerErrorException;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import java.util.HashMap;
import java.util.Map;

/**
 * ご配送方法Controller
 *
 * @author PHAM QUANG DIEU (VJP)
 */
@RequestMapping("/order")
@Controller
@SessionAttributes({"shipSelectModel", "orderCommonModel"})
public class ShipSelectController extends AbstractController {

    /** ロガー */
    private static final Logger LOGGER = LoggerFactory.getLogger(ShipSelectController.class);

    /** エラーメッセージコード：不正操作 */
    protected static final String MSGCD_ILLEGAL_OPERATION = "AOX000501";

    /** エラーメッセージコード：利用可能配送方法が0件 */
    public static final String MSGCD_NO_DELIVERY_METHOD = "AOX000304";

    /** ご配送方法画面からのフラグ */
    public static final String FROM_SHIPSELECT = "fromShipSelect";

    /** 船の選択 Helper */
    private ShipSelectHelper shipSelectHelper;

    /** 販売伝票Helper */
    private SalesSlipUtility salesSlipUtility;

    /** 配送方法Api */
    private ShippingMethodApi shippingMethodApi;

    /** 配送伝票Api */
    private ShippingSlipApi shippingSlipApi;

    /** コンストラクタ */
    @Autowired
    public ShipSelectController(ShipSelectHelper shipSelectHelper,
                                SalesSlipUtility salesSlipUtility,
                                ShippingMethodApi shippingMethodApi,
                                ShippingSlipApi shippingSlipApi) {
        this.shipSelectHelper = shipSelectHelper;
        this.salesSlipUtility = salesSlipUtility;
        this.shippingMethodApi = shippingMethodApi;
        this.shippingSlipApi = shippingSlipApi;
    }

    /**
     * 配送方法設定画面：初期処理
     *
     * @param shipSelectModel
     * @param orderCommonModel
     * @param redirectAttributes
     * @param model
     * @return 配送方法設定画面
     */
    @GetMapping(value = "/shipselect")
    @HEHandler(exception = AppLevelListException.class, returnView = "order/shipselect")
    protected String doLoadShipSelect(ShipSelectModel shipSelectModel,
                                      BindingResult error,
                                      OrderCommonModel orderCommonModel,
                                      RedirectAttributes redirectAttributes,
                                      Model model) {

        // セッションが切れた後の操作、注文フローの画面以外から遷移された場合、取引IDが存在しないので、カート画面にリダイレクトさせる
        if (StringUtils.isBlank(orderCommonModel.getTransactionId())) {
            addMessage(MSGCD_ILLEGAL_OPERATION, redirectAttributes, model);
            return "redirect:/cart/";
        }

        // モデル初期化
        clearModel(ShipSelectModel.class, shipSelectModel, model);

        // 動的コンポーネント作成
        initComponentsDelivery(shipSelectModel, error, orderCommonModel);

        if (error.hasErrors()) {
            return "order/shipselect";
        }

        salesSlipUtility.getSaleSlips(orderCommonModel.getTransactionId(), model);

        return "order/shipselect";
    }

    /**
     * 配送方法設定画面：「この配送方法を使用する」ボタン押下処理
     *
     * @param shipSelectModel
     * @param error
     * @param orderCommonModel
     * @param sessionStatus
     * @param redirectAttributes
     * @param model
     * @return お支払い方法選択画面
     */
    @PostMapping(value = "/shipselect", params = "doConfirm")
    @HEHandler(exception = AppLevelListException.class, returnView = "order/shipselect")
    public String doConfirm(@Validated(ShipSelectGroup.class) ShipSelectModel shipSelectModel,
                            BindingResult error,
                            OrderCommonModel orderCommonModel,
                            SessionStatus sessionStatus,
                            RedirectAttributes redirectAttributes,
                            Model model) {

        if (error.hasErrors()) {
            return "order/shipselect";
        }

        // 選択された配送方法のお届け希望日、時間帯の選択値を設定
        setSelectedDeliveryDateTimeValue(shipSelectModel);

        try {
            ShippingSlipMethodUpdateRequest shippingSlipMethodUpdateRequest =
                            shipSelectHelper.toShippingSlipMethodUpdateRequest(shipSelectModel, orderCommonModel);
            shippingSlipApi.updateMethod(shippingSlipMethodUpdateRequest);

        } catch (HttpClientErrorException | HttpServerErrorException e) {
            LOGGER.error("例外処理が発生しました", e);
            // アイテム名調整マップ
            Map<String, String> itemNameAdjust = new HashMap<>();
            handleError(e.getStatusCode(), e.getResponseBodyAsString(), error, itemNameAdjust);
        }

        if (error.hasErrors()) {
            return "order/shipselect";
        }

        // 取引IDをFlashAttributesに保存
        redirectAttributes.addFlashAttribute(FROM_SHIPSELECT, orderCommonModel.getTransactionId());

        // Modelをセッションより破棄
        sessionStatus.setComplete();

        // お支払い方法選択画面へ遷移
        return "redirect:/order/confirm";
    }

    /**
     * 動的コンポーネント作成
     *
     * @param shipSelectModel
     */
    protected void initComponentsDelivery(ShipSelectModel shipSelectModel,
                                          BindingResult error,
                                          OrderCommonModel orderCommonModel) {
        createDeliveryMethodList(shipSelectModel, error, orderCommonModel);
    }

    /**
     * 配送方法リスト作成
     *
     * @param shipSelectModel
     * @param orderCommonModel
     */
    protected void createDeliveryMethodList(ShipSelectModel shipSelectModel,
                                            BindingResult error,
                                            OrderCommonModel orderCommonModel) {

        ShippingSlipResponse shippingSlipResponse = null;
        try {
            ShippingSlipGetRequest shippingSlipGetRequest = new ShippingSlipGetRequest();
            shippingSlipGetRequest.setTransactionId(orderCommonModel.getTransactionId());
            shippingSlipResponse = shippingSlipApi.get(shippingSlipGetRequest);

        } catch (HttpClientErrorException | HttpServerErrorException e) {
            LOGGER.error("例外処理が発生しました", e);
            // アイテム名調整マップ
            Map<String, String> itemNameAdjust = new HashMap<>();
            handleError(e.getStatusCode(), e.getResponseBodyAsString(), error, itemNameAdjust);
            return;
        }

        if (shippingSlipResponse != null) {
            shipSelectHelper.toShipSelectModel(shipSelectModel, shippingSlipResponse);
        }

        SelectableShippingMethodListResponse selectableShippingMethodListResponse = null;
        try {
            // 配送方法選択可能リスト取得サービス実行
            SelectableShippingMethodListGetRequest selectableShippingMethodListGetRequest =
                            new SelectableShippingMethodListGetRequest();
            selectableShippingMethodListGetRequest.setTransactionId(orderCommonModel.getTransactionId());
            selectableShippingMethodListResponse =
                            shippingMethodApi.getSelectable(selectableShippingMethodListGetRequest);

        } catch (HttpClientErrorException | HttpServerErrorException e) {
            LOGGER.error("例外処理が発生しました", e);
            // アイテム名調整マップ
            Map<String, String> itemNameAdjust = new HashMap<>();
            handleError(e.getStatusCode(), e.getResponseBodyAsString(), error, itemNameAdjust);
            return;
        }

        // 利用可能配送方法が０件の場合はエラーメセージを表示する
        if (selectableShippingMethodListResponse == null) {
            throwMessage(MSGCD_NO_DELIVERY_METHOD);
        } else {
            shipSelectHelper.updateShipSelectModel(shipSelectModel, selectableShippingMethodListResponse);
        }
    }

    /**
     * 選択された配送方法と紐づいているお届け希望日、時間帯を設定<br/>
     * 配送方法変更前の伝票情報が残っている場合はクリア
     *
     * @param shipSelectModel
     */
    protected void setSelectedDeliveryDateTimeValue(ShipSelectModel shipSelectModel) {

        // 選択した配送方法と一致する配送方法をリストから取得
        SelectableShippingMethodItem target = shipSelectModel.getSelectableShippingMethodList()
                                                             .stream()
                                                             .filter(item -> StringUtils.isNotEmpty(
                                                                             item.getShippingMethodId())
                                                                             && item.getShippingMethodId()
                                                                                    .equals(shipSelectModel.getShippingMethodId()))
                                                             .findFirst()
                                                             .orElse(null);
        if (ObjectUtils.isEmpty(target)) {
            throwMessage(MSGCD_ILLEGAL_OPERATION);
        }

        // お届け希望日の調整
        if (CollectionUtils.isEmpty(target.getReceiverDateList())) {
            // お届け希望日リストが存在しないが、お届け希望日が設定されている場合はクリア
            shipSelectModel.setReceiverDate(null);
        } else {
            //  お届け希望日リストが存在するが、選択したお届け希望日が含まれない場合はエラー
            if (!target.getReceiverDateList().contains(shipSelectModel.getReceiverDate())) {
                throwMessage(MSGCD_ILLEGAL_OPERATION);
            }
        }

        // お届け希望時間帯の調整
        if (CollectionUtils.isEmpty(target.getReceiverTimeZoneList())) {
            // お届け時間帯リストが存在しないが、お届け時間帯が設定されている場合はクリア
            shipSelectModel.setReceiverTimeZone(null);
        } else {
            //  お届け時間帯リストが存在するが、選択したお届け時間帯が含まれない場合はエラー
            if (!target.getReceiverTimeZoneList().contains(shipSelectModel.getReceiverTimeZone())) {
                throwMessage(MSGCD_ILLEGAL_OPERATION);
            }
        }
    }

}