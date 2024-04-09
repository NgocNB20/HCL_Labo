/*
 * Copyright (C) 2022 i-TEC HANKYU HANSHIN INC. All Rights Reserved.
 *
 */

package jp.co.itechh.quad.front.pc.web.front.order;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import jp.co.itechh.quad.addressbook.presentation.api.AddressBookApi;
import jp.co.itechh.quad.addressbook.presentation.api.param.AddressBookAddressResponse;
import jp.co.itechh.quad.addressbook.presentation.api.param.ClientErrorResponse;
import jp.co.itechh.quad.addressbook.presentation.api.param.ErrorContent;
import jp.co.itechh.quad.billingslip.presentation.api.BillingSlipApi;
import jp.co.itechh.quad.billingslip.presentation.api.param.BillingSlipGetRequest;
import jp.co.itechh.quad.billingslip.presentation.api.param.BillingSlipResponse;
import jp.co.itechh.quad.billingslip.presentation.api.param.PaymentAgencyInterimEntrustRequest;
import jp.co.itechh.quad.billingslip.presentation.api.param.PaymentInterimRestoreRequest;
import jp.co.itechh.quad.front.annotation.exception.HEHandler;
import jp.co.itechh.quad.front.application.commoninfo.CommonInfo;
import jp.co.itechh.quad.front.base.application.AppLevelFacesMessage;
import jp.co.itechh.quad.front.base.application.HmMessages;
import jp.co.itechh.quad.front.base.exception.AppLevelListException;
import jp.co.itechh.quad.front.base.utility.ApplicationContextUtility;
import jp.co.itechh.quad.front.base.utility.ApplicationLogUtility;
import jp.co.itechh.quad.front.base.utility.ConversionUtility;
import jp.co.itechh.quad.front.constant.type.HTypeOpenDeleteStatus;
import jp.co.itechh.quad.front.constant.type.HTypePaymentLinkType;
import jp.co.itechh.quad.front.dto.goods.goods.GoodsDetailsDto;
import jp.co.itechh.quad.front.pc.web.front.order.common.OrderCommonModel;
import jp.co.itechh.quad.front.pc.web.front.order.common.SalesSlipUtility;
import jp.co.itechh.quad.front.pc.web.front.order.validation.ConfirmValidator;
import jp.co.itechh.quad.front.web.AbstractController;
import jp.co.itechh.quad.linkpay.presentation.api.LinkPayApi;
import jp.co.itechh.quad.linkpay.presentation.api.param.ReceiveLinkPaymentRequest;
import jp.co.itechh.quad.linkpay.presentation.api.param.ReceiveLinkPaymentResponse;
import jp.co.itechh.quad.method.presentation.api.SettlementMethodApi;
import jp.co.itechh.quad.method.presentation.api.param.PaymentMethodResponse;
import jp.co.itechh.quad.mulpay.presentation.api.MulpayApi;
import jp.co.itechh.quad.mulpay.presentation.api.param.MulPayBillRequest;
import jp.co.itechh.quad.mulpay.presentation.api.param.MulPayBillResponse;
import jp.co.itechh.quad.novelty.presentation.api.OrderNoveltyApi;
import jp.co.itechh.quad.novelty.presentation.api.param.NoveltyProductAutomaticGrantRequest;
import jp.co.itechh.quad.orderreceived.presentation.api.OrderReceivedApi;
import jp.co.itechh.quad.orderreceived.presentation.api.param.GetOrderReceivedRequest;
import jp.co.itechh.quad.orderreceived.presentation.api.param.OrderCodeReNumberingRequest;
import jp.co.itechh.quad.orderreceived.presentation.api.param.OrderCodeReNumberingResponse;
import jp.co.itechh.quad.orderreceived.presentation.api.param.OrderReceivedResponse;
import jp.co.itechh.quad.orderslip.presentation.api.OrderSlipApi;
import jp.co.itechh.quad.orderslip.presentation.api.param.OrderSlipGetRequest;
import jp.co.itechh.quad.orderslip.presentation.api.param.OrderSlipResponse;
import jp.co.itechh.quad.product.presentation.api.ProductApi;
import jp.co.itechh.quad.product.presentation.api.param.PageInfoRequest;
import jp.co.itechh.quad.product.presentation.api.param.ProductDetailListGetRequest;
import jp.co.itechh.quad.product.presentation.api.param.ProductDetailListResponse;
import jp.co.itechh.quad.salesslip.presentation.api.SalesSlipApi;
import jp.co.itechh.quad.salesslip.presentation.api.param.SalesSlipGetRequest;
import jp.co.itechh.quad.salesslip.presentation.api.param.SalesSlipResponse;
import jp.co.itechh.quad.shippingslip.presentation.api.ShippingSlipApi;
import jp.co.itechh.quad.shippingslip.presentation.api.param.ShippingSlipGetRequest;
import jp.co.itechh.quad.shippingslip.presentation.api.param.ShippingSlipResponse;
import jp.co.itechh.quad.transaction.presentation.api.TransactionApi;
import jp.co.itechh.quad.transaction.presentation.api.param.TransactionCheckRequest;
import jp.co.itechh.quad.transaction.presentation.api.param.TransactionConfirmOpenResponse;
import jp.co.itechh.quad.transaction.presentation.api.param.TransactionModernizeRequest;
import jp.co.itechh.quad.transaction.presentation.api.param.TransactionOpenRequest;
import jp.co.itechh.quad.transaction.presentation.api.param.TransactionRegistResponse;
import jp.co.itechh.quad.web.doublesubmit.annotation.DoubleSubmitCheck;
import org.apache.commons.collections.MapUtils;
import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.util.CollectionUtils;
import org.springframework.util.ObjectUtils;
import org.springframework.validation.BindingResult;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.WebDataBinder;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.InitBinder;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.SessionAttributes;
import org.springframework.web.bind.support.SessionStatus;
import org.springframework.web.client.HttpClientErrorException;
import org.springframework.web.client.HttpServerErrorException;
import org.springframework.web.context.request.RequestContextHolder;
import org.springframework.web.context.request.ServletRequestAttributes;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.BufferedReader;
import java.io.IOException;
import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.LinkedHashSet;
import java.util.List;
import java.util.Map;
import java.util.Objects;

/**
 * 注文内容確認 Controller
 *
 * @author Pham Quang Dieu
 */
@Controller
@SessionAttributes({"confirmModel", "orderCommonModel"})
public class ConfirmController extends AbstractController {

    /** ロガー */
    protected static final Logger LOGGER = LoggerFactory.getLogger(ConfirmController.class);

    /** エラーメッセージコード：不正操作 */
    public static final String MSGCD_ILLEGAL_OPERATION_CONFIRM = "AOX000501";

    /** エラーメッセージコード：不正操作 */
    protected static final String MSGCD_ILLEGAL_OPERATION_COMPLETE = "ORDER-COMPLETE-001-";

    /** エラーメッセージコード：正常にご注文を完了できませんでした。恐れ入りますが、もう一度ご注文をお願いします。 */
    protected static final String MSGCD_NETWORK_FAILURE = "ORDER-ERROR-001-";

    /** エラーメッセージコード：表示内容不正 */
    public static final String MSGCD_DISPLAY_ILLEGAL = "AOX000503";

    /** エラーメッセージコード：決済不正操作 */
    protected static final String MSGCD_ILLEGAL_PAYMENT_OPERATION = "AOX000601";

    /** エラーメッセージコード：3Dセキュアキャンセル後の受注番号再発行失敗 */
    protected static final String MSGCD_FAIL_RENUMBERING_ORDERCODE = "ORDER-COMPLETE-002-";

    /** MSDCD_FAIL_GMO_LINK_PAYMENT */
    private static final String MSDCD_FAIL_GMO_LINK_PAYMENT = "PAYMENT_LINKPAY-001-";

    /** 決済代行エラーメッセージコード */
    public static final String PAYMENT_AGENCY_ERROR = "LMC";

    /** 決済代行の取引ID */
    protected static final String PARAMETER_ACCESS_ID = "AccessId";

    /** ACCESSID値固定桁数 */
    protected static final int ACCESSID_MAX_LENGTH = 32;

    /** フラッシュスコープパラメータ：取引開始で発行された取引IDセット用 */
    public static final String TRANSACTION = "transactionId";

    /** リンク決済フラグ */
    private static final String LINK_PAYMENT = "linkPayment";

    /** カート画面からの遷移フラグ */
    public static final String FROM_CART = "fromCart";

    /** お届け先住所の選択画面からの遷移フラグ */
    public static final String FROM_ADDRESS_SELECT = "fromAddressselect";

    /** ご請求先住所の選択画面からの遷移フラグ */
    public static final String FROM_BILLING_SELECT = "fromBillingselect";

    /** 配送方法選択画面からの遷移フラグ */
    public static final String FROM_SHIPSELECT = "fromShipSelect";

    /** お支払い選択画面からの遷移フラグ */
    public static final String FROM_PAYMENT = "fromPayment";

    /** 新規入力カード番号 */
    public static final String NEW_CARD_MASK_NO = "newCardMaskNo";

    /** 商品API */
    private final ProductApi productApi;

    /** 取引API */
    private final TransactionApi transactionApi;

    /** 配送伝票API */
    private final ShippingSlipApi shippingSlipApi;

    /** 請求伝票API */
    private final BillingSlipApi billingSlipApi;

    /** 注文票API */
    private final OrderSlipApi orderSlipApi;

    /** 住所録API */
    private final AddressBookApi addressBookApi;

    /** 決済方法API */
    private final SettlementMethodApi settlementMethodApi;

    /** 販売伝票API */
    private final SalesSlipApi salesSlipApi;

    /** マルペイAPI */
    private final MulpayApi mulpayApi;

    /** 受注API */
    private final OrderReceivedApi orderReceivedApi;

    /** 注文内容確認 Helper */
    private final ConfirmHelper newConfirmHelper;

    /** 注文完了画面 Helper */
    private final CompleteHelper newCompleteHelper;

    /** 販売伝票Helper */
    private final SalesSlipUtility salesSlipUtility;

    /** 変換Helper */
    private final ConversionUtility conversionUtility;

    /** ノベルティ商品自動付与Api */
    private final OrderNoveltyApi orderNoveltyApi;

    /** LinkPayApi */
    private final LinkPayApi linkPayApi;

    /** 注文確認画面 動的バリデータ */
    private final ConfirmValidator confirmValidator;

    /** コンストラクタ */
    @Autowired
    public ConfirmController(ProductApi productApi,
                             TransactionApi transactionApi,
                             ShippingSlipApi shippingSlipApi,
                             BillingSlipApi billingSlipApi,
                             OrderSlipApi orderSlipApi,
                             AddressBookApi addressBookApi,
                             SettlementMethodApi settlementMethodApi,
                             SalesSlipApi salesSlipApi,
                             MulpayApi mulpayApi,
                             OrderReceivedApi orderReceivedApi,
                             ConfirmHelper newConfirmHelper,
                             CompleteHelper newCompleteHelper,
                             SalesSlipUtility salesSlipUtility,
                             ConversionUtility conversionUtility,
                             OrderNoveltyApi orderNoveltyApi,
                             LinkPayApi linkPayApi,
                             ConfirmValidator confirmValidator) {
        this.productApi = productApi;
        this.transactionApi = transactionApi;
        this.shippingSlipApi = shippingSlipApi;
        this.billingSlipApi = billingSlipApi;
        this.orderSlipApi = orderSlipApi;
        this.addressBookApi = addressBookApi;
        this.settlementMethodApi = settlementMethodApi;
        this.salesSlipApi = salesSlipApi;
        this.mulpayApi = mulpayApi;
        this.orderReceivedApi = orderReceivedApi;
        this.newConfirmHelper = newConfirmHelper;
        this.newCompleteHelper = newCompleteHelper;
        this.salesSlipUtility = salesSlipUtility;
        this.conversionUtility = conversionUtility;
        this.orderNoveltyApi = orderNoveltyApi;
        this.linkPayApi = linkPayApi;
        this.confirmValidator = confirmValidator;
    }

    @InitBinder
    public void initBinder(WebDataBinder error) {
        // お支払方法選択画面の動的バリデータをセット
        error.addValidators(confirmValidator);
    }

    /**
     * 注文内容確認画面：初期処理
     *
     * @param confirmModel
     * @param orderCommonModel
     * @param model
     * @return 注文内容確認画面
     */
    @GetMapping(value = "/order/confirm")
    @HEHandler(exception = AppLevelListException.class, returnView = "redirect:/cart/")
    public String doLoadConfirm(ConfirmModel confirmModel,
                                OrderCommonModel orderCommonModel,
                                RedirectAttributes redirectAttributes,
                                Model model) {

        // モデル初期化
        clearModel(ConfirmModel.class, confirmModel, model);

        if (model.containsAttribute(FROM_CART)) {

            // 注文フロー共通Model 初期化
            clearModel(OrderCommonModel.class, orderCommonModel, model);

            try {
                // 取引開始
                TransactionRegistResponse response =
                                transactionApi.regist(getCommonInfo().getCommonInfoUser().getMemberInfoBirthday(),
                                                      getCommonInfo().getCommonInfoUser().getMemberInfoAddressId()
                                                     );
                // 注文フロー共通Modelに発行された取引IDをセット
                orderCommonModel.setTransactionId(response.getTransactionId());
            } catch (HttpClientErrorException ce) {
                LOGGER.error("例外処理が発生しました", ce);
                // 取引の開始に失敗しているので、カート画面へリダイレクトさせる
                createClientErrorMessage(ce, redirectAttributes, model);
                return "redirect:/cart/";
            } catch (HttpServerErrorException se) {
                LOGGER.error("例外処理が発生しました", se);
                throwMessage(MSGCD_ILLEGAL_OPERATION_CONFIRM);
            }
        }
        if (model.containsAttribute(FROM_ADDRESS_SELECT)) {
            // 注文フロー共通Modelにお届け先住所の選択画面から引き渡された取引IDをセット
            orderCommonModel.setTransactionId((String) model.getAttribute(FROM_ADDRESS_SELECT));
        }

        if (model.containsAttribute(FROM_BILLING_SELECT)) {
            // 注文フロー共通Modelに請求先住所の選択画面から引き渡された取引IDをセット
            orderCommonModel.setTransactionId((String) model.getAttribute(FROM_BILLING_SELECT));
        }

        if (model.containsAttribute(FROM_PAYMENT)) {
            // 注文フロー共通Modelにお支払い選択画面から引き渡された取引IDをセット
            orderCommonModel.setTransactionId((String) model.getAttribute(FROM_PAYMENT));
        }

        if (model.containsAttribute(FROM_SHIPSELECT)) {
            // 注文フロー共通Modelに配送先選択画面から引き渡された取引IDをセット
            orderCommonModel.setTransactionId((String) model.getAttribute(FROM_SHIPSELECT));
        }

        if (model.containsAttribute(NEW_CARD_MASK_NO)) {
            // 注文フロー共通Modelにお支払い選択画面から引き渡された新規入力カード番号のマスク値をセット
            confirmModel.setNewCardMaskNo((String) model.getAttribute(NEW_CARD_MASK_NO));
        }

        // セッションが切れた後の操作、カートや注文フローの画面以外から遷移された場合、取引IDが存在しないので、カート画面にリダイレクトさせる
        if (StringUtils.isBlank(orderCommonModel.getTransactionId())) {
            throwMessage(MSGCD_ILLEGAL_OPERATION_CONFIRM);
        }

        /**
         * 注文用伝票のトランザクションデータを最新マスタの値に更新<br/>
         * 注文内容確認画面を表示するたびに、注文用伝票のトランザクションデータを更新させる。注文確定以降は注文用伝票のトランザクションデータで登録<br/>
         * ※エラーが発生してもここではハンドリングを行わない
         */
        TransactionModernizeRequest transactionModernizeRequest = new TransactionModernizeRequest();
        transactionModernizeRequest.setTransactionId(orderCommonModel.getTransactionId());
        transactionApi.modernize(transactionModernizeRequest);

        /** 注文の妥当性チェック */
        try {
            TransactionCheckRequest transactionCheckRequest = new TransactionCheckRequest();
            transactionCheckRequest.setTransactionId(orderCommonModel.getTransactionId());
            transactionApi.check(transactionCheckRequest, getCommonInfo().getCommonInfoUser().getMemberInfoBirthday());
        } catch (HttpClientErrorException ce) {
            LOGGER.error("注文妥当性チェックNG", ce);
            createClientErrorMessage(ce, redirectAttributes, model);

            // Modelへの変換処理
            setConfirmModel(confirmModel, orderCommonModel, model);
            return "order/confirm";

        } catch (HttpServerErrorException se) {
            LOGGER.error("例外処理が発生しました", se);
            throwMessage(MSGCD_ILLEGAL_OPERATION_CONFIRM);
        }

        // Modelへの変換処理
        setConfirmModel(confirmModel, orderCommonModel, model);
        setReducedTaxRate(confirmModel);

        return "order/confirm";
    }

    /**
     * 注文内容確認画面：「注文内容を確定する」ボタン押下処理
     *
     * @param confirmModel
     * @param orderCommonModel
     * @return 本人認証画面(3Dセキュア時)、自画面(購入不可商品が存在)、注文完了画面(それ以外)
     */
    @PostMapping(value = "order/confirm", params = "doOnceOrderRegist")
    @HEHandler(exception = AppLevelListException.class, returnView = "order/confirm")
    public String doOnceOrderRegist(@Validated ConfirmModel confirmModel,
                                    BindingResult error,
                                    OrderCommonModel orderCommonModel,
                                    RedirectAttributes redirectAttributes,
                                    Model model,
                                    SessionStatus sessionStatus) throws IOException {

        // 取引IDが存在しない場合は、カート画面にリダイレクトさせる ※完了画面からのブラウザバック対応
        if (StringUtils.isBlank(orderCommonModel.getTransactionId())) {
            addMessage(MSGCD_ILLEGAL_OPERATION_CONFIRM, redirectAttributes, model);
            return "redirect:/cart/";
        }

        if (error.hasErrors()) {
            if (error.getFieldErrors("securityCode").size() > 0
                || error.getFieldErrors("expirationDateYear").size() > 0) {
                confirmModel.setSecurityCode(null);
            }
            return "order/confirm";
        }

        try {
            TransactionCheckRequest transactionCheckRequest = new TransactionCheckRequest();
            transactionCheckRequest.setTransactionId(orderCommonModel.getTransactionId());
            transactionCheckRequest.setContractConfirmFlag(true);
            transactionApi.check(transactionCheckRequest, getCommonInfo().getCommonInfoUser().getMemberInfoBirthday());
        } catch (HttpClientErrorException | HttpServerErrorException e) {
            LOGGER.error("例外処理が発生しました", e);
            // アイテム名調整マップ
            Map<String, String> itemNameAdjust = new HashMap<>();
            handleError(e.getStatusCode(), e.getResponseBodyAsString(), error, itemNameAdjust);
        }

        // 取引確認業務開始
        try {
            ResponseEntity<Object> responseEntity = transactionApi.confirmOpenWithHttpInfo(
                            this.newConfirmHelper.toTransactionConfirmOpenRequest(confirmModel, orderCommonModel));

            // GMOから認証結果が返却された場合、3Dセキュア用の取引処理を開始する
            if (!ObjectUtils.isEmpty(responseEntity) && (HttpStatus.ACCEPTED.equals(responseEntity.getStatusCode())
                                                         || HttpStatus.CREATED.equals(responseEntity.getStatusCode()))
                && responseEntity.getBody() != null) {

                TransactionConfirmOpenResponse response = conversionUtility.toObject(new TypeReference<>() {
                }, responseEntity.getBody());
                HttpServletResponse httpServletResponse = ((ServletRequestAttributes) Objects.requireNonNull(
                                RequestContextHolder.getRequestAttributes())).getResponse();
                if (httpServletResponse != null) {
                    return "redirect:" + response.getRedirectUrl();
                }
            }

        } catch (HttpClientErrorException ce) {
            LOGGER.error("例外処理が発生しました", ce);
            try {
                // 途中決済を復元する
                restoreUnderSettlement(orderCommonModel.getTransactionId());
            } catch (HttpClientErrorException | HttpServerErrorException e) {
                LOGGER.error("例外処理が発生しました", e);
                // 復元過程でエラーが起きて、再度クレジット決済で注文されるとGMOに同じオーダーIDを渡し続けることになるので、ここではカート画面にリダイレクトさせる
                addMessage(MSGCD_FAIL_RENUMBERING_ORDERCODE, redirectAttributes, model);
                return "redirect:/cart/";
            }

            createClientErrorMessage(ce, redirectAttributes, model);
            if (model.containsAttribute(FLASH_MESSAGES)) {
                // 決済代行用のメッセージ（メッセージコードがLMC始まり）であり、トークンが設定されている場合は決済方法選択画面へリダイレクトさせる
                if (isPaymentAgency((HmMessages) model.getAttribute(FLASH_MESSAGES)) && StringUtils.isNotBlank(
                                confirmModel.getToken())) {
                    return "redirect:/order/payselect";
                } else {
                    // 上記以外は確認画面に戻す
                    return "order/confirm";
                }
            }

        } catch (HttpServerErrorException se) {
            handleServerError(se.getResponseBodyAsString());
        }

        OrderReceivedResponse orderReceivedResponse = null;
        try {
            // 確定処理を実行前に、取引IDより受注番号を取得する（確定処理内で改訂処理される可能性があるため）
            GetOrderReceivedRequest getOrderReceivedRequest = new GetOrderReceivedRequest();
            getOrderReceivedRequest.setTransactionId(orderCommonModel.getTransactionId());
            orderReceivedResponse = orderReceivedApi.get(getOrderReceivedRequest);
        } catch (Exception e) {
            LOGGER.warn("ノベルティ商品判定用受注番号取得に失敗しました", e);
        }

        // 取引確定業務開始
        try {
            // ユーザーエージェントを設定する
            HttpServletRequest request = ((ServletRequestAttributes) Objects.requireNonNull(
                            RequestContextHolder.getRequestAttributes())).getRequest();
            this.transactionApi.getApiClient().setUserAgent(request.getHeader("User-Agent"));

            TransactionOpenRequest transactionOpenRequest = new TransactionOpenRequest();
            transactionOpenRequest.setTransactionId(orderCommonModel.getTransactionId());
            transactionApi.open(transactionOpenRequest);

        } catch (HttpClientErrorException | HttpServerErrorException e) {
            // アイテム名調整マップ
            LOGGER.error(e.getMessage());
            addMessage(MSGCD_NETWORK_FAILURE, redirectAttributes, model);
            return "redirect:/cart/";
        }

        // ノベルティ商品自動付与
        if (orderReceivedResponse != null) {
            try {
                NoveltyProductAutomaticGrantRequest noveltyProductAutomaticGrantRequest =
                                new NoveltyProductAutomaticGrantRequest();
                noveltyProductAutomaticGrantRequest.setOrderCode(orderReceivedResponse.getOrderCode());
                orderNoveltyApi.execute(noveltyProductAutomaticGrantRequest);
            } catch (HttpClientErrorException | HttpServerErrorException e) {
                LOGGER.warn(e.getMessage());
            }
        }

        // Modelをセッションより破棄（コントローラー処理完了後に破棄される）
        sessionStatus.setComplete();

        // 取引IDを完了画面に引き渡す
        redirectAttributes.addFlashAttribute(TRANSACTION, orderCommonModel.getTransactionId());

        // 通常の注文完了画面に遷移
        return "redirect:/order/complete";
    }

    /**
     * リンク決済コールバック
     *
     * @param req
     * @param redirectAttributes
     * @param model
     * @return
     * @throws IOException
     */
    @PostMapping(value = "order/linkpay-callback")
    @DoubleSubmitCheck(disable = true) // トークンが飛んでこないため、チェック不可能
    public String linkPayCallBack(HttpServletRequest req, RedirectAttributes redirectAttributes, Model model)
                    throws IOException {

        BufferedReader br = new BufferedReader(req.getReader());
        String jsonText = br.readLine();

        // リンク決済結果を受取る
        ReceiveLinkPaymentResponse receiveLinkPaymentResponse = null;
        try {
            ReceiveLinkPaymentRequest request = new ReceiveLinkPaymentRequest();
            request.setLinkPayJsonText(jsonText);

            receiveLinkPaymentResponse = linkPayApi.receive(request);

            // 別ブラウザ対応（セッションの取引IDを復元) ※別ブラウザ対応だけでなく、各種不整合に対応するため常にGMOレスポンスから取得した取引IDを正とする
            String transactionId = receiveLinkPaymentResponse.getTransactionId();
            setTransactionIdOfSession(transactionId, model);

        } catch (HttpClientErrorException ce) {
            LOGGER.error("例外処理が発生しました", ce);
            // 想定内エラーの場合

            ClientErrorResponse clientError =
                            conversionUtility.toObject(ce.getResponseBodyAsString(), ClientErrorResponse.class);
            Map<String, List<ErrorContent>> messages = clientError.getMessages();
            // エラーメッセージ処理
            if (MapUtils.isNotEmpty(messages)) {

                // その他エラーの場合
                for (Map.Entry<String, List<ErrorContent>> entry : messages.entrySet()) {
                    List<ErrorContent> errorResponseList = entry.getValue();
                    for (ErrorContent errorContent : errorResponseList) {

                        // 別ブラウザ対応
                        if ("PAYMENT_LINK0001-I".equals(errorContent.getCode())) {
                            // セッションの取引IDを復元
                            String transactionId = errorContent.getMessage().replaceAll("\\[.+\\]", "");
                            setTransactionIdOfSession(transactionId, model);
                        } else {
                            addMessageApi(errorContent.getCode(), errorContent.getMessage(), redirectAttributes, model);
                        }
                    }
                }
            }

            // 途中決済から受注番号を再発行し、決済可能状態に復元する
            if (model != null) {
                OrderCommonModel orderCommonModel = ((OrderCommonModel) model.getAttribute("orderCommonModel"));
                if (orderCommonModel != null) {
                    restoreUnderSettlement(orderCommonModel.getTransactionId());
                }
            } else {
                // modelがない場合はメッセージのセット不可のためエラー画面に飛ばす
                LOGGER.error("決済可能状態に復元途中に例外処理が発生しました");
                return "redirect:/error";
            }

            return "redirect:/order/confirm";

        } catch (HttpServerErrorException se) {
            //想定外エラーの場合
            addMessage(MSGCD_ILLEGAL_PAYMENT_OPERATION, redirectAttributes, model);
            return "redirect:/cart/";
        }

        // 取引が確定可能か確認する
        try {
            transactionApi.confirmOpen(newConfirmHelper.toTransactionConfirmOpenRequest(
                            receiveLinkPaymentResponse.getTransactionId()));
        } catch (HttpClientErrorException | HttpServerErrorException e) {
            LOGGER.error("例外処理が発生しました", e);
            addMessage(MSDCD_FAIL_GMO_LINK_PAYMENT, redirectAttributes, model);

            return "redirect:/order/confirm";
        }

        // ノベルティ商品リクエストパラメータ用に受注番号取得
        OrderReceivedResponse orderReceivedResponse = null;
        try {
            // 確定処理を実行前に、取引IDより受注番号を取得する（確定処理内で改訂処理される可能性があるため）
            GetOrderReceivedRequest getOrderReceivedRequest = new GetOrderReceivedRequest();
            OrderCommonModel orderCommonModel = ((OrderCommonModel) model.getAttribute("orderCommonModel"));
            if (orderCommonModel != null) {
                getOrderReceivedRequest.setTransactionId(orderCommonModel.getTransactionId());
            }
            orderReceivedResponse = orderReceivedApi.get(getOrderReceivedRequest);
        } catch (Exception e) {
            LOGGER.warn("ノベルティ商品判定用受注番号取得に失敗しました", e);
        }

        // 確定処理を実行する
        try {
            // ユーザーエージェントを設定する
            HttpServletRequest request = ((ServletRequestAttributes) Objects.requireNonNull(
                            RequestContextHolder.getRequestAttributes())).getRequest();
            this.transactionApi.getApiClient().setUserAgent(request.getHeader("User-Agent"));

            TransactionOpenRequest transactionOpenRequest = new TransactionOpenRequest();
            transactionOpenRequest.setTransactionId(receiveLinkPaymentResponse.getTransactionId());
            this.transactionApi.open(transactionOpenRequest);
        } catch (HttpClientErrorException | HttpServerErrorException e) {
            LOGGER.error(e.getMessage());
            addMessage(MSGCD_NETWORK_FAILURE, redirectAttributes, model);
            return "redirect:/cart/";
        }

        // ノベルティ商品自動付与
        if (orderReceivedResponse != null) {
            try {
                NoveltyProductAutomaticGrantRequest noveltyProductAutomaticGrantRequest =
                                new NoveltyProductAutomaticGrantRequest();
                noveltyProductAutomaticGrantRequest.setOrderCode(orderReceivedResponse.getOrderCode());
                orderNoveltyApi.execute(noveltyProductAutomaticGrantRequest);
            } catch (HttpClientErrorException | HttpServerErrorException e) {
                LOGGER.warn(e.getMessage());
            }
        }

        // 取引IDを完了画面に引き渡す
        redirectAttributes.addFlashAttribute(TRANSACTION, receiveLinkPaymentResponse.getTransactionId());
        // For link-pay
        redirectAttributes.addFlashAttribute(LINK_PAYMENT, true);

        return "redirect:/order/complete";
    }

    /**  ***********　Secureredirect Start　*********** **/
    /**
     * 3Dセキュア認証済みかを判定してリダイレクトする画面<br/>
     * 本人認証後、注文登録処理が正常に行われた場合は注文完了<br/>
     *
     * @param confirmModel
     * @param orderCommonModel
     * @param AccessId           決済代行の取引ID
     * @param redirectAttributes
     * @param model
     * @return 認証OK:注文完了 / 認証NG:注文確認画面
     */
    @PostMapping(value = "order/secureredirect")
    @DoubleSubmitCheck(disable = true) // トークンが飛んでこないため、チェック不可能
    @HEHandler(exception = AppLevelListException.class, returnView = "redirect:/order/confirm")
    public String doRedirectSecure(ConfirmModel confirmModel,
                                   BindingResult error,
                                   OrderCommonModel orderCommonModel,
                                   @RequestParam(required = false, name = "AccessID") String AccessId,
                                   RedirectAttributes redirectAttributes,
                                   Model model,
                                   SessionStatus sessionStatus) {

        confirmModel.setSecureModel(new SecureModel());
        confirmModel.getSecureModel().setParam(new HashMap<>());
        confirmModel.getSecureModel().getParam().put("AccessId", AccessId);

        // アクセスログ出力
        accessLog(confirmModel);

        // 各種チェック処理
        if (checkExecute(confirmModel, redirectAttributes, model)) {
            return "redirect:/cart/";
        }

        try {
            // 認証後のレスポンス結果で決済サービスを呼び出し、3Dセキュア認証後の通信を実行する
            PaymentAgencyInterimEntrustRequest paymentAgencyInterimEntrustRequest =
                            new PaymentAgencyInterimEntrustRequest();
            paymentAgencyInterimEntrustRequest.setAccessId(AccessId);
            this.billingSlipApi.entrustInterimPaymentAgency(paymentAgencyInterimEntrustRequest);

        } catch (HttpClientErrorException ce) {
            LOGGER.error("例外処理が発生しました", ce);
            // キャンセル時や認証画面で通信が切れた場合など）の場合
            try {
                // 途中決済を復元する
                restoreUnderSettlement(orderCommonModel.getTransactionId());
            } catch (HttpClientErrorException | HttpServerErrorException e) {
                LOGGER.error("例外処理が発生しました", e);
                // 復元過程でエラーが起きて、再度クレジット決済で注文されるとGMOに同じオーダーIDを渡し続けることになるので、ここではカート画面にリダイレクトさせる
                addMessage(MSGCD_FAIL_RENUMBERING_ORDERCODE, redirectAttributes, model);
                return "redirect:/cart/";
            }

            createClientErrorMessage(ce, redirectAttributes, model);
            if (model.containsAttribute(FLASH_MESSAGES)) {
                // 決済代行用のメッセージ（メッセージコードがLMC始まり）であり、トークンが設定されている場合は決済方法選択画面へリダイレクトさせる
                if (isPaymentAgency((HmMessages) model.getAttribute(FLASH_MESSAGES)) && StringUtils.isNotBlank(
                                confirmModel.getToken())) {
                    return "redirect:/order/payselect";
                } else {
                    // 上記以外は確認画面に戻す
                    return "redirect:/order/confirm";
                }
            }

        } catch (HttpServerErrorException se) {
            LOGGER.error("例外処理が発生しました", se);
            addMessage(MSGCD_ILLEGAL_PAYMENT_OPERATION, redirectAttributes, model);
            return "redirect:/cart/";
        }

        // 取引確認業務開始
        try {
            // 3Dセキュア認証後の呼び出しのため、リクエスト情報は不要
            transactionApi.confirmOpen(
                            this.newConfirmHelper.toTransactionConfirmOpenRequest(confirmModel, orderCommonModel));
        } catch (HttpClientErrorException | HttpServerErrorException e) {
            LOGGER.error("例外処理が発生しました", e);
            // アイテム名調整マップ
            Map<String, String> itemNameAdjust = new HashMap<>();
            handleError(e.getStatusCode(), e.getResponseBodyAsString(), error, itemNameAdjust);

            if (error.hasErrors()) {
                return "redirect:/order/confirm";
            }
        }

        // ノベルティ商品リクエストパラメータ用に受注番号取得
        OrderReceivedResponse orderReceivedResponse = null;
        try {
            // 確定処理を実行前に、取引IDより受注番号を取得する（確定処理内で改訂処理される可能性があるため）
            GetOrderReceivedRequest getOrderReceivedRequest = new GetOrderReceivedRequest();
            getOrderReceivedRequest.setTransactionId(orderCommonModel.getTransactionId());
            orderReceivedResponse = orderReceivedApi.get(getOrderReceivedRequest);
        } catch (Exception e) {
            LOGGER.warn("ノベルティ商品判定用受注番号取得に失敗しました", e);
        }

        // 取引確定処理を実行する
        try {
            // ユーザーエージェントを設定する
            HttpServletRequest request = ((ServletRequestAttributes) Objects.requireNonNull(
                            RequestContextHolder.getRequestAttributes())).getRequest();
            this.transactionApi.getApiClient().setUserAgent(request.getHeader("User-Agent"));

            TransactionOpenRequest transactionOpenRequest = new TransactionOpenRequest();
            transactionOpenRequest.setTransactionId(orderCommonModel.getTransactionId());
            this.transactionApi.open(transactionOpenRequest);
        } catch (HttpClientErrorException | HttpServerErrorException e) {
            // アイテム名調整マップ
            LOGGER.error(e.getMessage());
            addMessage(MSGCD_NETWORK_FAILURE, redirectAttributes, model);
            return "redirect:/cart/";

        }

        // ノベルティ商品自動付与
        if (orderReceivedResponse != null) {
            try {
                NoveltyProductAutomaticGrantRequest noveltyProductAutomaticGrantRequest =
                                new NoveltyProductAutomaticGrantRequest();
                noveltyProductAutomaticGrantRequest.setOrderCode(orderReceivedResponse.getOrderCode());
                orderNoveltyApi.execute(noveltyProductAutomaticGrantRequest);
            } catch (HttpClientErrorException | HttpServerErrorException e) {
                LOGGER.warn(e.getMessage());
            }
        }

        // Modelをセッションより破棄（コントローラー処理完了後に破棄される）
        sessionStatus.setComplete();

        // 取引IDを完了画面に引き渡す
        redirectAttributes.addFlashAttribute(TRANSACTION, orderCommonModel.getTransactionId());
        return "redirect:/order/complete";
    }
    /**  ***********　Secureredirect End　*********** **/

    /**  ***********　Complete Start　*********** **/
    /**
     * 注文完了画面：初期処理
     *
     * @param confirmModel
     * @param redirectAttributes
     * @param model
     * @param sessionStatus
     * @return 注文完了画面
     */
    @GetMapping(value = "order/complete")
    public String doLoadComplete(ConfirmModel confirmModel,
                                 RedirectAttributes redirectAttributes,
                                 Model model,
                                 SessionStatus sessionStatus) throws JsonProcessingException {

        // Modelをセッションより破棄（コントローラー処理完了後に破棄される）
        // 完了画面へのリダイレクト前にクリアしているが、完了画面描画用に詰められたモデル情報を改めてクリアするために、ここでも呼び出す必要がある
        sessionStatus.setComplete();

        String transactionId = null;
        if (model.containsAttribute(TRANSACTION)) {
            // カート画面から注文フローへの導線ボタンを押下後に発行された取引IDをセット
            transactionId = (String) model.getAttribute(TRANSACTION);
        }

        // F5などをされた場合、取引IDが存在しないのでカート画面に遷移させる
        if (StringUtils.isBlank(transactionId)) {
            addMessage(MSGCD_ILLEGAL_OPERATION_COMPLETE, redirectAttributes, model);
            return "redirect:/cart/";
        }

        // 取引IDから受注情報を取得
        GetOrderReceivedRequest getOrderReceivedRequest = new GetOrderReceivedRequest();
        getOrderReceivedRequest.setTransactionId(transactionId);
        OrderReceivedResponse orderReceivedResponse = this.orderReceivedApi.get(getOrderReceivedRequest);

        // 受注情報が存在しない場合はカート画面に遷移させる
        if (ObjectUtils.isEmpty(orderReceivedResponse)) {
            addMessage(MSGCD_ILLEGAL_OPERATION_COMPLETE, redirectAttributes, model);
            return "redirect:/cart/";
        }

        // 販売伝票を取得
        SalesSlipGetRequest salesSlipGetRequest = new SalesSlipGetRequest();
        salesSlipGetRequest.setTransactionId(transactionId);
        SalesSlipResponse salesSlipResponse = this.salesSlipApi.get(salesSlipGetRequest);

        // 注文票を取得
        OrderSlipGetRequest orderSlipGetRequest = new OrderSlipGetRequest();
        orderSlipGetRequest.setTransactionId(transactionId);
        OrderSlipResponse orderSlipResponse = this.orderSlipApi.get(orderSlipGetRequest);

        // 請求伝票を取得
        BillingSlipGetRequest billingSlipGetRequest = new BillingSlipGetRequest();
        billingSlipGetRequest.setTransactionId(transactionId);
        BillingSlipResponse billingSlipResponse = this.billingSlipApi.get(billingSlipGetRequest);

        // 各種伝票が存在しない場合はカート画面に遷移させる
        if (ObjectUtils.isEmpty(salesSlipResponse) || ObjectUtils.isEmpty(billingSlipResponse) || ObjectUtils.isEmpty(
                        orderSlipResponse)) {
            addMessage(MSGCD_ILLEGAL_OPERATION_COMPLETE, redirectAttributes, model);
            return "redirect:/cart/";
        }

        // 注文商品リストから商品IDリストを作成

        LinkedHashSet<Integer> idHashSet = new LinkedHashSet<>();
        for (int i = 0; i < orderSlipResponse.getItemList().size(); i++) {
            idHashSet.add(this.conversionUtility.toInteger(orderSlipResponse.getItemList().get(i).getItemId()));
        }
        List<Integer> idList = new ArrayList<>(idHashSet);

        // 注文商品から商品情報を取得
        ProductDetailListGetRequest productDetailListGetRequest = new ProductDetailListGetRequest();
        productDetailListGetRequest.setGoodsSeqList(idList);
        ProductDetailListResponse productDetailListResponse =
                        this.productApi.getDetails(productDetailListGetRequest, null);

        // 商品情報が存在しない場合はカート画面に遷移させる
        if (ObjectUtils.isEmpty(productDetailListResponse) || CollectionUtils.isEmpty(
                        productDetailListResponse.getGoodsDetailsList())) {
            addMessage(MSGCD_ILLEGAL_OPERATION_COMPLETE, redirectAttributes, model);
            return "redirect:/cart/";
        }

        MulPayBillResponse mulpayBillResponse = null;
        /* 後日払い支払情報の取得 */
        if (model.containsAttribute(LINK_PAYMENT) && HTypePaymentLinkType.LATERDATEPAYMENT.getValue()
                                                                                          .equals(billingSlipResponse.getPaymentLinkResponse()
                                                                                                                     .getLinkPayType())) {

            // マルチペイメント請求情報を取得
            MulPayBillRequest mulPayBillRequest = new MulPayBillRequest();
            mulPayBillRequest.setOrderCode(orderReceivedResponse.getOrderCode());

            mulpayBillResponse = mulpayApi.getByOrderCode(mulPayBillRequest);
        }

        newCompleteHelper.toPageForLoad(confirmModel, orderReceivedResponse, orderSlipResponse,
                                        productDetailListResponse, salesSlipResponse, mulpayBillResponse,
                                        billingSlipResponse, model.containsAttribute(LINK_PAYMENT)
                                       );
        // Google Tag Manager eコマースタグ
        CommonInfo commonInfo = ApplicationContextUtility.getBean(CommonInfo.class);
        GoogleAnalyticsInfo googleAnalyticsInfo =
                        newCompleteHelper.toGoogleAnalyticsInfo(confirmModel, commonInfo.getCommonInfoShop());
        model.addAttribute("googleAnalyticsInfo", new ObjectMapper().writeValueAsString(googleAnalyticsInfo));

        return "order/complete";
    }

    /**  ***********　Complete End　*********** **/

    /**
     * モデルに確認画面の情報をセットする<br/>
     *
     * @param confirmModel
     * @param orderCommonModel
     * @param model
     */
    protected void setConfirmModel(ConfirmModel confirmModel, OrderCommonModel orderCommonModel, Model model) {
        // 各種伝票の取得
        this.getSlip(confirmModel, orderCommonModel);
        if (confirmModel.getSettlementMethodEntity() != null && HTypeOpenDeleteStatus.DELETED.equals(
                        confirmModel.getSettlementMethodEntity().getOpenStatusPC())) {
            throwMessage("AYC000203");
        }
    }

    /**
     * 軽減税率対象商品があるかのを判断
     *
     * @param confirmModel 注文内容確認 Model
     */
    protected void setReducedTaxRate(ConfirmModel confirmModel) {
        for (GoodsDetailsDto goodsDetailsDto : confirmModel.getGoodsDetailDtoList()) {
            if (BigDecimal.valueOf(8).equals(goodsDetailsDto.getTaxRate())) {
                confirmModel.setReducedTaxRate(true);
                break;
            }
        }
    }

    /**
     * 途中決済を復元<br/>
     * ※受注番号も再発行される
     *
     * @param transactionId
     */
    protected void restoreUnderSettlement(String transactionId) {
        // 受注番号を再発行する
        OrderCodeReNumberingRequest orderCodeReNumberingRequest = new OrderCodeReNumberingRequest();
        orderCodeReNumberingRequest.setTransactionId(transactionId);
        OrderCodeReNumberingResponse orderCodeReNumberingResponse =
                        this.orderReceivedApi.reNumberingOrderCode(orderCodeReNumberingRequest);
        // 途中決済を復元する
        PaymentInterimRestoreRequest paymentInterimRestoreRequest = new PaymentInterimRestoreRequest();
        paymentInterimRestoreRequest.setTransactionId(transactionId);
        paymentInterimRestoreRequest.setOrderCode(orderCodeReNumberingResponse.getOrderCode());
        this.billingSlipApi.restoreInterimPayment(paymentInterimRestoreRequest);
    }

    /**
     * セッション(orderCommonModel)のトランザクションIDを復元
     *
     * @param transactionId
     * @param model
     */
    protected void setTransactionIdOfSession(String transactionId, Model model) {
        if (!model.containsAttribute("orderCommonModel")) {
            OrderCommonModel orderCommonModel = new OrderCommonModel();
            orderCommonModel.setTransactionId(transactionId);
            model.addAttribute("orderCommonModel", orderCommonModel);
        } else {
            OrderCommonModel orderCommonModel = (OrderCommonModel) model.getAttribute("orderCommonModel");
            if (orderCommonModel != null) {
                orderCommonModel.setTransactionId(transactionId);
            }
        }
    }

    /**
     * アクセスログ出力
     *
     * @param confirmModel
     */
    protected void accessLog(ConfirmModel confirmModel) {
        ApplicationLogUtility applicationLogUtility = ApplicationContextUtility.getBean(ApplicationLogUtility.class);
        HttpServletRequest request =
                        ((ServletRequestAttributes) RequestContextHolder.getRequestAttributes()).getRequest();
        applicationLogUtility.writeFreeLog(
                        "3DSECURE", null, request.getMethod(), confirmModel.getSecureModel().getParam());
    }

    /**
     * 各種チェック処理
     *
     * @param confirmModel
     * @param redirectAttributes
     * @param model
     * @return チェック結果..true:チェックNG
     */
    protected boolean checkExecute(ConfirmModel confirmModel, RedirectAttributes redirectAttributes, Model model) {

        // リクエストのgetMethodをチェック
        if (checkRequestMethod(redirectAttributes, model)) {
            return true;
        }

        if (!checkParam(confirmModel)) {
            // パラメータが不正の場合はカートに戻す
            addMessage(MSGCD_ILLEGAL_PAYMENT_OPERATION, redirectAttributes, model);
            return true;
        }
        return false;
    }

    /**
     * リクエストのgetMethodをチェック
     *
     * @param redirectAttributes
     * @param model
     */
    protected boolean checkRequestMethod(RedirectAttributes redirectAttributes, Model model) {
        HttpServletRequest request =
                        ((ServletRequestAttributes) RequestContextHolder.getRequestAttributes()).getRequest();
        String method = request.getMethod();
        if (!"POST".equals(method)) {
            // POST以外で来たらカートに戻す
            addMessage(MSGCD_ILLEGAL_PAYMENT_OPERATION, redirectAttributes, model);
            return true;
        }
        return false;
    }

    /**
     * パラメータのチェック
     *
     * @param confirmModel
     * @return true:エラーなし / false:エラーあり
     */
    protected boolean checkParam(ConfirmModel confirmModel) {
        Map<String, String> param = confirmModel.getSecureModel().getParam();
        if (param == null) {
            return false;
        }
        String accessId = param.get(PARAMETER_ACCESS_ID);
        if (StringUtils.isEmpty(accessId)) {
            return false;
        }
        if (accessId.length() != ACCESSID_MAX_LENGTH) {
            return false;
        }
        return true;
    }

    /**
     * クライアントエラーのメッセージを生成<br/>
     * HEHandler指定画面以外の画面遷移を制御したい場合に利用
     *
     * @param ce                 クライアントエラー
     * @param redirectAttributes リダイレクトアトリビュート
     * @param model              モデル
     */
    protected void createClientErrorMessage(HttpClientErrorException ce,
                                            RedirectAttributes redirectAttributes,
                                            Model model) {

        ClientErrorResponse clientError =
                        conversionUtility.toObject(ce.getResponseBodyAsString(), ClientErrorResponse.class);
        Map<String, List<ErrorContent>> messages = clientError.getMessages();

        if (MapUtils.isNotEmpty(messages)) {
            for (Map.Entry<String, List<ErrorContent>> entry : messages.entrySet()) {
                List<ErrorContent> errorResponseList = entry.getValue();
                for (ErrorContent errorResponse : errorResponseList) {
                    addMessageApi(errorResponse.getCode(), errorResponse.getMessage(), redirectAttributes, model);
                }
            }
        }
    }

    /**
     * APIが返却したメッセージを追加<br/>
     *
     * @param messageCode        メッセージコード
     * @param message            メッセージ
     * @param redirectAttributes リダイレクトアトリビュート
     * @param model              モデル
     */
    protected void addMessageApi(String messageCode,
                                 String message,
                                 RedirectAttributes redirectAttributes,
                                 Model model) {

        //
        // コールスタックを漁って addMessage を行ったクラスを取得する
        //

        Throwable th = new Throwable();
        StackTraceElement ste = th.getStackTrace()[0];

        // AbstractController は発生元クラスとしない
        for (StackTraceElement tmp : th.getStackTrace()) {
            if (AbstractController.class.getName().equals(tmp.getClassName())) {
                continue;
            }
            ste = tmp;
            break;
        }

        // アプリケーションログ出力Helper取得
        ApplicationLogUtility applicationLogUtility = ApplicationContextUtility.getBean(ApplicationLogUtility.class);

        // addMessage のログを出力する
        applicationLogUtility.writeLogicErrorLog(
                        ste.getClassName() + "#" + ste.getMethodName(), messageCode + ":" + message);

        // FlashAttributeへメッセージセット
        HmMessages allMessages = getFlashMessages(redirectAttributes);

        AppLevelFacesMessage appLevelFacesMessage = new AppLevelFacesMessage(message);
        appLevelFacesMessage.setMessageCode(messageCode);
        allMessages.add(appLevelFacesMessage);

        // その後の遷移先がリダイレクトであってもなくても、うまく画面表示できるよう、
        // RedirectAttributes、Model両方にメッセージを設定しておく
        redirectAttributes.addFlashAttribute(FLASH_MESSAGES, allMessages);
        model.addAttribute(FLASH_MESSAGES, allMessages);
    }

    /**
     * 決済代行からのエラーが含まれているかどうか
     *
     * @param allMessages
     * @return true ... 有り / false ... 無し
     */
    protected boolean isPaymentAgency(HmMessages allMessages) {
        if (!allMessages.hasError()) {
            return false;
        }
        return allMessages.stream()
                          .filter(target -> (StringUtils.isNotBlank(target.getMessageCode()) ?
                                          target.getMessageCode() :
                                          "").startsWith(PAYMENT_AGENCY_ERROR))
                          .findFirst()
                          .orElse(null) != null;
    }

    /**
     * 各種伝票の取得<br/>
     *
     * @param confirmModel
     * @param orderCommonModel
     */
    public void getSlip(ConfirmModel confirmModel, OrderCommonModel orderCommonModel) {
        // 販売伝票取得
        SalesSlipGetRequest salesSlipGetRequest = new SalesSlipGetRequest();
        salesSlipGetRequest.setTransactionId(orderCommonModel.getTransactionId());
        SalesSlipResponse salesSlipResponse = salesSlipApi.get(salesSlipGetRequest);
        newConfirmHelper.toConfirmSalesSlip(salesSlipResponse, salesSlipUtility, confirmModel);

        // 配送伝票取得
        ShippingSlipGetRequest shippingSlipGetRequest = new ShippingSlipGetRequest();
        shippingSlipGetRequest.setTransactionId(orderCommonModel.getTransactionId());
        ShippingSlipResponse shippingSlipResponse = shippingSlipApi.get(shippingSlipGetRequest);
        newConfirmHelper.toConfirmShippingSlip(shippingSlipResponse, confirmModel);

        // お届け先住所情報を取得
        AddressBookAddressResponse addressBookAddressResponse = null;
        if (!ObjectUtils.isEmpty(shippingSlipResponse) && StringUtils.isNotBlank(
                        shippingSlipResponse.getShippingAddressId())) {
            addressBookAddressResponse = addressBookApi.getAddressById(shippingSlipResponse.getShippingAddressId());
            newConfirmHelper.toConfirmAddress(addressBookAddressResponse, confirmModel);
        }

        // 請求伝票取得
        BillingSlipGetRequest billingSlipGetRequest = new BillingSlipGetRequest();
        billingSlipGetRequest.setTransactionId(orderCommonModel.getTransactionId());
        BillingSlipResponse billingSlipResponse = billingSlipApi.get(billingSlipGetRequest);

        if (!ObjectUtils.isEmpty(billingSlipResponse) && billingSlipResponse.getBillingAddressId() != null
            && !ObjectUtils.isEmpty(shippingSlipResponse) && billingSlipResponse.getBillingAddressId()
                                                                                .equals(shippingSlipResponse.getShippingAddressId())) {
            newConfirmHelper.toConfirmBillingAddress(addressBookAddressResponse, confirmModel);
            confirmModel.setEqualAddress(true);
        } else {
            // ご請求先住所情報を取得
            if (!ObjectUtils.isEmpty(billingSlipResponse) && StringUtils.isNotBlank(
                            billingSlipResponse.getBillingAddressId())) {
                AddressBookAddressResponse billingAddressBookAddressResponse =
                                addressBookApi.getAddressById(billingSlipResponse.getBillingAddressId());
                newConfirmHelper.toConfirmBillingAddress(billingAddressBookAddressResponse, confirmModel);
            }
            confirmModel.setEqualAddress(false);
        }

        // 決済情報詳細取得
        PaymentMethodResponse paymentMethodResponse = null;
        if (!ObjectUtils.isEmpty(billingSlipResponse) && StringUtils.isNotBlank(
                        billingSlipResponse.getPaymentMethodId())) {
            paymentMethodResponse = settlementMethodApi.getBySettlementMethodSeq(
                            Integer.parseInt(billingSlipResponse.getPaymentMethodId()));
        }

        // 決済情報を確認Modelに変換
        newConfirmHelper.toConfirmPaymentMethod(billingSlipResponse, paymentMethodResponse, confirmModel);
        // 請求情報を確認Modelに変換
        newConfirmHelper.toConfirmModelFromBillingSlip(billingSlipResponse, confirmModel, orderCommonModel);

        // 下書き注文票取得
        OrderSlipGetRequest orderSlipSlipGetRequest = new OrderSlipGetRequest();
        orderSlipSlipGetRequest.setTransactionId(orderCommonModel.getTransactionId());
        OrderSlipResponse orderSlipResponse = orderSlipApi.getDraft(orderSlipSlipGetRequest);

        ProductDetailListGetRequest productDetailListGetRequest =
                        newConfirmHelper.toProductDetailListGetRequest(orderSlipResponse);

        // 最新の商品マスタを取得
        ProductDetailListResponse productDetailListResponse =
                        productApi.getDetails(productDetailListGetRequest, new PageInfoRequest());

        // 注文商品詳細情報取得
        newConfirmHelper.toConfirmProductDetail(productDetailListGetRequest.getGoodsSeqList(),
                                                productDetailListResponse, salesSlipResponse, orderSlipResponse,
                                                confirmModel
                                               );
    }

}
