package jp.co.itechh.quad.front.pc.web.front.order;

import jp.co.itechh.quad.billingslip.presentation.api.BillingSlipApi;
import jp.co.itechh.quad.billingslip.presentation.api.param.BillingSlipResponse;
import jp.co.itechh.quad.card.presentation.api.CardApi;
import jp.co.itechh.quad.card.presentation.api.param.CardInfoResponse;
import jp.co.itechh.quad.coupon.presentation.api.CouponApi;
import jp.co.itechh.quad.coupon.presentation.api.param.CouponResponse;
import jp.co.itechh.quad.customer.presentation.api.CustomerApi;
import jp.co.itechh.quad.customer.presentation.api.param.CustomerResponse;
import jp.co.itechh.quad.front.annotation.exception.HEHandler;
import jp.co.itechh.quad.front.base.exception.AppLevelListException;
import jp.co.itechh.quad.front.base.util.common.CollectionUtil;
import jp.co.itechh.quad.front.constant.type.HTypeExpirationDateMonth;
import jp.co.itechh.quad.front.constant.type.HTypePaymentType;
import jp.co.itechh.quad.front.constant.type.HTypeSettlementMethodType;
import jp.co.itechh.quad.front.dto.shop.settlement.SettlementDetailsDto;
import jp.co.itechh.quad.front.dto.shop.settlement.SettlementDto;
import jp.co.itechh.quad.front.entity.memberinfo.MemberInfoEntity;
import jp.co.itechh.quad.front.pc.web.front.order.common.OrderCommonModel;
import jp.co.itechh.quad.front.pc.web.front.order.common.SalesSlipModel;
import jp.co.itechh.quad.front.pc.web.front.order.common.SalesSlipUtility;
import jp.co.itechh.quad.front.pc.web.front.order.validation.PaySelectValidator;
import jp.co.itechh.quad.front.pc.web.front.order.validation.group.ApplyCouponGroup;
import jp.co.itechh.quad.front.pc.web.front.order.validation.group.PaySelectGroup;
import jp.co.itechh.quad.front.util.common.EnumTypeUtil;
import jp.co.itechh.quad.front.utility.OrderUtility;
import jp.co.itechh.quad.front.web.AbstractController;
import jp.co.itechh.quad.method.presentation.api.PaymentMethodApi;
import jp.co.itechh.quad.method.presentation.api.param.SelectablePaymentMethodListGetRequest;
import jp.co.itechh.quad.method.presentation.api.param.SelectablePaymentMethodListResponse;
import jp.co.itechh.quad.mulpay.presentation.api.MulpayApi;
import jp.co.itechh.quad.mulpay.presentation.api.param.MulPayShopIdResponse;
import jp.co.itechh.quad.salesslip.presentation.api.SalesSlipApi;
import jp.co.itechh.quad.salesslip.presentation.api.param.SalesSlipGetRequest;
import jp.co.itechh.quad.salesslip.presentation.api.param.SalesSlipResponse;
import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.util.ObjectUtils;
import org.springframework.validation.BindingResult;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.WebDataBinder;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.InitBinder;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.SessionAttributes;
import org.springframework.web.bind.support.SessionStatus;
import org.springframework.web.client.HttpClientErrorException;
import org.springframework.web.client.HttpServerErrorException;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * お支払い方法選択 Controller
 *
 * @author Pham Quang Dieu
 */
@RequestMapping("/order")
@Controller
@SessionAttributes({"paySelectModel", "orderCommonModel", "salesSlip"})
public class PaySelectController extends AbstractController {

    /** ロガー */
    private static final Logger LOGGER = LoggerFactory.getLogger(PaySelectController.class);

    /** エラーメッセージコード：不正操作 */
    protected static final String MSGCD_ILLEGAL_OPERATION = "AOX000501";

    /** エラーメッセージコード：利用可能お支払い方法が０件 */
    public static final String MSGCD_NO_SETTLEMENT_METHOD = "AOX000406";

    /** クーポン適用メッセージ */
    public static final String MSGCD_CAQN_COUPON_USED = "AOX000408";

    /** クーポン取消メッセージ */
    public static final String MSGCD_CAQN_COUPON_CANCELED = "AOX000410";

    /** 全額クーポンでのお支払いSEQ **/
    public static final String ALL_COUPON_SETTLEMENT_METHOD_SEQ = "3000";

    /** お支払い画面からのフラグ */
    public static final String FROM_PAYMENT = "fromPayment";

    /** クーポン適用フラグ */
    public static final String FROM_COUPON_CHECK = "fromCouponCheck";

    /** 新規入力カード番号 */
    public static final String NEW_CARD_MASK_NO = "newCardMaskNo";

    /** エラーメッセージコード：存在しないお支払い方法が選択された */
    public static final String MSGCD_SELECT_NO_SETTLEMENT_METHOD = "AOX000407";

    /** お支払い方法選択画面 Helper **/
    private final PaySelectHelper paySelectHelper;

    /** 受注共通処理 */
    private final OrderUtility orderUtility;

    /** マルペイAPI */
    private final MulpayApi mulpayApi;

    /** 決済方法取得API */
    private final PaymentMethodApi paymentMethodApi;

    /** クレジットカードエンドポイントAPI */
    private final CardApi cardApi;

    /** 請求伝票API */
    private final BillingSlipApi billingSlipApi;

    /** 販売伝票API */
    private final SalesSlipApi salesSlipApi;

    /** ユーザAPI */
    private final CustomerApi customerApi;

    /** クーポンエンドポイントAPI */
    private final CouponApi couponApi;

    /** 販売伝票Helper */
    private final SalesSlipUtility salesSlipUtility;

    /** 決済方法用動的バリデータ */
    private final PaySelectValidator paySelectValidator;

    /** コンストラクタ */
    @Autowired
    public PaySelectController(PaySelectHelper paySelectHelper,
                               SalesSlipUtility salesSlipUtility,
                               OrderUtility orderUtility,
                               MulpayApi mulpayApi,
                               PaymentMethodApi paymentMethodApi,
                               CardApi cardApi,
                               BillingSlipApi billingSlipApi,
                               SalesSlipApi salesSlipApi,
                               CustomerApi customerApi,
                               CouponApi couponApi,
                               PaySelectValidator paySelectValidator) {
        this.paySelectHelper = paySelectHelper;
        this.salesSlipUtility = salesSlipUtility;
        this.orderUtility = orderUtility;
        this.mulpayApi = mulpayApi;
        this.paymentMethodApi = paymentMethodApi;
        this.cardApi = cardApi;
        this.billingSlipApi = billingSlipApi;
        this.salesSlipApi = salesSlipApi;
        this.customerApi = customerApi;
        this.couponApi = couponApi;
        this.paySelectValidator = paySelectValidator;
    }

    @InitBinder
    public void initBinder(WebDataBinder error) {
        // お支払方法選択画面の動的バリデータをセット
        error.addValidators(paySelectValidator);
    }

    /**
     * お支払方法選択画面：初期処理
     *
     * @param paySelectModel     お支払い方法選択Model
     * @param orderCommonModel   注文フロー共通Model
     * @param model              モデル
     * @param redirectAttributes リダイレクトアトリビュート
     * @return お支払方法選択画面
     */
    @GetMapping(value = "/payselect")
    @HEHandler(exception = AppLevelListException.class, returnView = "order/payselect")
    protected String doLoadPayment(PaySelectModel paySelectModel,
                                   BindingResult error,
                                   OrderCommonModel orderCommonModel,
                                   Model model,
                                   RedirectAttributes redirectAttributes) {

        // モデル初期化
        clearModel(PaySelectModel.class, paySelectModel, model);

        // セッションが切れた後の操作、注文フローの画面以外から遷移された場合、取引IDが存在しないので、カート画面にリダイレクトさせる
        if (StringUtils.isBlank(orderCommonModel.getTransactionId())) {
            addMessage(MSGCD_ILLEGAL_OPERATION, redirectAttributes, model);
            return "redirect:/cart/";
        }

        if (!model.containsAttribute(FROM_COUPON_CHECK)) {
            clearModel(PaySelectModel.class, paySelectModel, model);
        }

        // カード情報を取得
        paySelectModel.setResultCard(getRegistedCardInfo(paySelectModel, error));

        if (error.hasErrors()) {
            return "order/payselect";
        }

        // 注文内容確認画面から遷移してきた場合に、前回の設定値を表示しない為に初期化
        paySelectModel.setPaySelectModelItems(new ArrayList<>());

        // 動的コンポーネント作成
        initComponentsPayment(paySelectModel, error, orderCommonModel, redirectAttributes, model);

        if (error.hasErrors()) {
            return "order/payselect";
        }

        MulPayShopIdResponse mulPayShopIdResponse = null;
        try {
            mulPayShopIdResponse = mulpayApi.get();
        } catch (HttpClientErrorException | HttpServerErrorException e) {
            LOGGER.error("例外処理が発生しました", e);
            // アイテム名調整マップ
            Map<String, String> itemNameAdjust = new HashMap<>();
            handleError(e.getStatusCode(), e.getResponseBodyAsString(), error, itemNameAdjust);
        }

        if (error.hasErrors()) {
            return "order/payselect";
        }

        if (!ObjectUtils.isEmpty(mulPayShopIdResponse)) {
            paySelectModel.setGmoApiKey(mulPayShopIdResponse.getMulPayShopId());
        }

        // 販売伝票取得
        salesSlipUtility.getSaleSlips(orderCommonModel.getTransactionId(), model);

        // クーポン適用したかどうかチェック
        SalesSlipModel salesSlipModel = (SalesSlipModel) model.getAttribute(SalesSlipModel.ATTRIBUTE_NAME_KEY);
        if (salesSlipModel != null && salesSlipModel.getCouponName() != null
            && salesSlipModel.getCouponPaymentPrice() != null) {
            paySelectModel.setCouponName(salesSlipModel.getCouponName());
            paySelectModel.setCouponDiscountPrice(salesSlipModel.getCouponPaymentPrice());
        }

        return "order/payselect";
    }

    /**
     * 「ご注文内容確認」「ご注文内容確認画面へ戻る」ボタン押下処理
     *
     * @param paySelectModel     お支払い方法選択Model
     * @param orderCommonModel   注文フロー共通Model
     * @param error              BindingResult
     * @param redirectAttributes リダイレクトアトリビュート
     * @param model              モデル
     * @param sessionStatus
     * @return 注文内容確認画面
     */
    @PostMapping(value = "/payselect", params = "doConfirmPayment")
    @HEHandler(exception = AppLevelListException.class, returnView = "redirect:/order/payselect")
    public String doConfirmPayment(@Validated(PaySelectGroup.class) PaySelectModel paySelectModel,
                                   BindingResult error,
                                   OrderCommonModel orderCommonModel,
                                   RedirectAttributes redirectAttributes,
                                   SessionStatus sessionStatus,
                                   Model model) {

        if (error.hasErrors()) {
            return "order/payselect";
        }

        paySelectModel.getPaySelectModelItems().forEach(paySelectModelItem -> {
            if (HTypeSettlementMethodType.CREDIT.equals(paySelectModelItem.getSettlementMethodType())) {
                if (StringUtils.isNotEmpty(paySelectModelItem.getCardNumber())
                    && !paySelectModel.isUseRegistCardFlg()) {
                    redirectAttributes.addFlashAttribute(NEW_CARD_MASK_NO, paySelectModelItem.getCardNumber());
                } else {
                    redirectAttributes.addFlashAttribute(NEW_CARD_MASK_NO, null);
                }
            }
        });

        // 選択決済の不正操作チェック
        checkOperationSettlementMethod(paySelectModel);

        try {
            billingSlipApi.updateMethod(
                            paySelectHelper.toBillingSlipMethodUpdateRequest(orderCommonModel.getTransactionId(),
                                                                             paySelectModel
                                                                            ));

        } catch (HttpClientErrorException | HttpServerErrorException e) {
            LOGGER.error("例外処理が発生しました", e);
            // アイテム名調整マップ
            Map<String, String> itemNameAdjust = new HashMap<>();
            itemNameAdjust.put("paymentMethodId", "settlementMethod");
            itemNameAdjust.put("paymentToken", "token");
            itemNameAdjust.put("maskedCardNo", "cardNumber");
            itemNameAdjust.put("expirationMonth", "expirationDateMonth");
            itemNameAdjust.put("expirationYear", "expirationDateYear");
            itemNameAdjust.put("registCardFlag", "saveFlg");
            itemNameAdjust.put("useRegistedCardFlag", "useRegistCardFlg");
            handleError(e.getStatusCode(), e.getResponseBodyAsString(), error, itemNameAdjust);
        }

        if (error.hasErrors()) {
            return "redirect:/order/payselect";
        }

        // セキュリティコードを注文フロー共通Modelへ設定
        orderCommonModel.setSecurityCode(null);
        for (PaySelectModelItem payselectModelItem : paySelectModel.getPaySelectModelItems()) {
            if (HTypeSettlementMethodType.CREDIT.equals(payselectModelItem.getSettlementMethodType())
                && payselectModelItem.getSettlementMethodValue().equals(paySelectModel.getSettlementMethod())) {
                orderCommonModel.setSecurityCode(payselectModelItem.getSecurityCode());
            }
        }

        // 取引IDをFlashAttributesに保存
        redirectAttributes.addFlashAttribute(FROM_PAYMENT, orderCommonModel.getTransactionId());

        return "redirect:/order/confirm";
    }

    /**
     * クーポン「適用」ボタン押下処理。<br />
     * クーポンコードが利用可能な場合はクーポン適用後の金額を返す。
     * クーポンが利用できない場合は再計算した値を返す。
     *
     * @param paySelectModel     お支払い方法選択Model
     * @param orderCommonModel   注文フロー共通Model
     * @param error              BindingResult
     * @param redirectAttributes リダイレクトアトリビュート
     * @param model              モデル
     * @return 自画面
     */
    @PostMapping(value = "/payselect", params = "doApplyCoupon")
    @HEHandler(exception = AppLevelListException.class, returnView = "order/payselect")
    public String doApplyCoupon(@Validated(ApplyCouponGroup.class) PaySelectModel paySelectModel,
                                BindingResult error,
                                OrderCommonModel orderCommonModel,
                                RedirectAttributes redirectAttributes,
                                Model model) {

        if (error.hasErrors()) {
            return "order/payselect";
        }

        try {
            salesSlipApi.applyCoupon(paySelectHelper.toSalesSlipCouponApplyRequest(orderCommonModel.getTransactionId(),
                                                                                   paySelectModel.getCouponCode()
                                                                                  ));
        } catch (HttpClientErrorException | HttpServerErrorException e) {
            LOGGER.error("例外処理が発生しました", e);
            // アイテム名調整マップ
            Map<String, String> itemNameAdjust = new HashMap<>();
            handleError(e.getStatusCode(), e.getResponseBodyAsString(), error, itemNameAdjust);
        }

        if (error.hasErrors()) {
            return "order/payselect";
        }

        // 販売伝票取得
        salesSlipUtility.getSaleSlips(orderCommonModel.getTransactionId(), model);

        SalesSlipModel salesSlipModel = (SalesSlipModel) model.getAttribute(SalesSlipModel.ATTRIBUTE_NAME_KEY);

        addInfoMessage(MSGCD_CAQN_COUPON_USED, new String[] {salesSlipModel.getCouponName()}, redirectAttributes,
                       model
                      );

        // クーポン適用を反映
        // 動的コンポーネントの作成
        initComponentsPayment(paySelectModel, error, orderCommonModel, redirectAttributes, model);

        if (error.hasErrors()) {
            return "order/payselect";
        }

        SalesSlipGetRequest salesSlipGetRequest = new SalesSlipGetRequest();
        salesSlipGetRequest.setTransactionId(orderCommonModel.getTransactionId());

        SalesSlipResponse salesSlipResponse = null;
        CouponResponse couponResponse = null;
        try {
            salesSlipResponse = salesSlipApi.get(salesSlipGetRequest);
            couponResponse = couponApi.getByCouponSeq(salesSlipResponse.getCouponSeq());

        } catch (HttpClientErrorException | HttpServerErrorException e) {
            LOGGER.error("例外処理が発生しました", e);
            // アイテム名調整マップ
            Map<String, String> itemNameAdjust = new HashMap<>();
            handleError(e.getStatusCode(), e.getResponseBodyAsString(), error, itemNameAdjust);
        }

        if (error.hasErrors()) {
            return "order/payselect";
        }

        paySelectHelper.toPageForLoadForCoupon(paySelectModel, couponResponse, salesSlipResponse);

        // クーポン適用により、支払方法が「全額クーポン支払」のみの場合、選択済みの支払方法をクリア
        if (paySelectModel.getPaySelectModelItems().size() == 1 && ALL_COUPON_SETTLEMENT_METHOD_SEQ.equals(
                        paySelectModel.getPaySelectModelItems().get(0).getSettlementMethodValue())) {
            paySelectModel.setSettlementMethod(
                            paySelectModel.getPaySelectModelItems().get(0).getSettlementMethodValue());
        }

        return "order/payselect";
    }

    /**
     * 適用済みのクーポンを取消す
     *
     * @param paySelectModel     お支払い方法選択Model
     * @param orderCommonModel   注文フロー共通Model
     * @param redirectAttributes リダイレクトアトリビュート
     * @param model              モデル
     * @return 自画面
     */
    @PostMapping(value = "/payselect", params = "doCancelCoupon")
    @HEHandler(exception = AppLevelListException.class, returnView = "order/payselect")
    public String doCancelCoupon(PaySelectModel paySelectModel,
                                 BindingResult error,
                                 OrderCommonModel orderCommonModel,
                                 RedirectAttributes redirectAttributes,
                                 Model model) {

        try {
            salesSlipApi.cancelCoupon(
                            paySelectHelper.toSalesSlipCouponCancelRequest(orderCommonModel.getTransactionId()));

        } catch (HttpClientErrorException | HttpServerErrorException e) {
            LOGGER.error("例外処理が発生しました", e);
            // アイテム名調整マップ
            Map<String, String> itemNameAdjust = new HashMap<>();
            handleError(e.getStatusCode(), e.getResponseBodyAsString(), error, itemNameAdjust);
        }

        if (error.hasErrors()) {
            return "order/payselect";
        }

        addInfoMessage(MSGCD_CAQN_COUPON_CANCELED, new String[] {paySelectModel.getCouponName()}, redirectAttributes,
                       model
                      );

        // 販売伝票取得
        salesSlipUtility.getSaleSlips(orderCommonModel.getTransactionId(), model);

        // 動的コンポーネントの作成
        initComponentsPayment(paySelectModel, error, orderCommonModel, redirectAttributes, model);

        if (error.hasErrors()) {
            return "order/payselect";
        }

        paySelectModel.setCouponDiscountPrice(BigDecimal.ZERO);
        paySelectModel.setCouponCode("");

        // 支払方法に「全額クーポン支払」を選択していた場合、クリア
        if (ALL_COUPON_SETTLEMENT_METHOD_SEQ.equals(paySelectModel.getSettlementMethod())) {
            paySelectModel.setSettlementMethod(null);
        }

        return "order/payselect";
    }

    /**
     * 「別のカードを使う」処理<br/>
     *
     * @param paySelectModel     お支払い方法選択Model
     * @param salesSlip          販売伝票Model
     * @param model              お支払い方法選択Model
     * @param redirectAttributes リダイレクトアトリビュート
     * @return 自画面
     */
    @PostMapping(value = "/payselect", params = "doChangeOtherCard")
    public String doChangeOtherCard(PaySelectModel paySelectModel,
                                    SalesSlipModel salesSlip,
                                    Model model,
                                    RedirectAttributes redirectAttributes) {

        // Modelのカード情報をクリア
        paySelectHelper.toPageForChangeOtherCard(paySelectModel);

        return "order/payselect";
    }

    /**
     * 「前回利用したカード」処理<br/>
     *
     * @param paySelectModel     お支払い方法選択Model
     * @param salesSlip          販売伝票Model
     * @param model              モデル
     * @param redirectAttributes リダイレクトアトリビュート
     * @return 自画面
     */
    @PostMapping(value = "/payselect", params = "doChangeRegistedCard")
    public String doChangeRegistedCard(PaySelectModel paySelectModel,
                                       BindingResult error,
                                       SalesSlipModel salesSlip,
                                       Model model,
                                       RedirectAttributes redirectAttributes) {

        // カード情報を取得
        paySelectModel.setDisplayCredit(true);
        paySelectModel.setUseRegistCardFlg(true);
        paySelectModel.setResultCard(getRegistedCardInfo(paySelectModel, error));

        // Modelに反映
        paySelectHelper.toPageForChangeRegistedCard(paySelectModel);

        // トークン初期化
        paySelectModel.setToken(null);
        paySelectModel.setPreCreditInformationFlag(false);

        return "order/payselect";
    }

    /**
     * 動的コンポーネント作成
     *
     * @param paySelectModel     お支払い方法選択Model
     * @param orderCommonModel   注文フロー共通Model
     * @param redirectAttributes リダイレクトアトリビュート
     * @param model              モデル
     */
    protected void initComponentsPayment(PaySelectModel paySelectModel,
                                         BindingResult error,
                                         OrderCommonModel orderCommonModel,
                                         RedirectAttributes redirectAttributes,
                                         Model model) {
        // 決済方法ラジオボタン作成
        createSettlementMethodRadio(paySelectModel, error, orderCommonModel, redirectAttributes, model);
    }

    /**
     * 決済方法ラジオボタン作成
     *
     * @param paySelectModel     お支払い方法選択Model
     * @param orderCommonModel   注文フロー共通Model
     * @param redirectAttributes リダイレクトアトリビュート
     * @param model              モデル
     */
    protected void createSettlementMethodRadio(PaySelectModel paySelectModel,
                                               BindingResult error,
                                               OrderCommonModel orderCommonModel,
                                               RedirectAttributes redirectAttributes,
                                               Model model) {

        SelectablePaymentMethodListResponse selectable = null;
        try {
            // 決済方法選択可能リスト取得サービス実行
            SelectablePaymentMethodListGetRequest selectablePaymentMethodListGetRequest =
                            paySelectHelper.toSelectPayMethodListGetRequest(orderCommonModel.getTransactionId());
            selectable = paymentMethodApi.getSelectable(selectablePaymentMethodListGetRequest);

        } catch (HttpClientErrorException | HttpServerErrorException e) {
            LOGGER.error("例外処理が発生しました", e);
            // アイテム名調整マップ
            Map<String, String> itemNameAdjust = new HashMap<>();
            handleError(e.getStatusCode(), e.getResponseBodyAsString(), error, itemNameAdjust);
            return;
        }

        List<SettlementDto> settlementDtoList = paySelectHelper.toSettlementDto(selectable, paySelectModel);

        // クーポン適用後の場合、決済方法リストを退避する
        List<PaySelectModelItem> bkPaySelectModelItems = null;
        if (paySelectModel.getPaySelectModelItems() != null) {
            bkPaySelectModelItems = paySelectModel.getPaySelectModelItems();
        }

        paySelectModel.setPaySelectModelItems(new ArrayList<>());
        paySelectModel.setSettlementDtoMap(new HashMap<>());
        PaySelectModelItem paySelectModelItem = null;

        Integer freeSettlementSeq = orderUtility.getFreeSettlementMethodSeq();

        for (SettlementDto settlementDto : settlementDtoList) {

            if (!ObjectUtils.isEmpty(settlementDto.getSettlementDetailsDto())) {
                if (settlementDto.getSettlementDetailsDto().getSettlementMethodSeq() != null) {
                    // 決済Dtoをマップに設定
                    paySelectModel.getSettlementDtoMap()
                                  .put(settlementDto.getSettlementDetailsDto()
                                                    .getSettlementMethodSeq()
                                                    .toString(), settlementDto);
                }

                // 無料決済は出力しない
                if (!ObjectUtils.isEmpty(settlementDto.getSettlementDetailsDto()) && freeSettlementSeq.equals(
                                settlementDto.getSettlementDetailsDto().getSettlementMethodSeq())) {
                    continue;
                }

                // 決済方法詳細DTO取得
                SettlementDetailsDto settlementDetailsDto = settlementDto.getSettlementDetailsDto();

                // 決済方法選択画面表示用Dtoを作成
                paySelectModelItem = new PaySelectModelItem();

                // 決済方法名設定
                paySelectModelItem.setSettlementMethodLabel(settlementDetailsDto.getSettlementMethodName());
                // 決済方法SEQ設定
                paySelectModelItem.setSettlementMethodValue(settlementDetailsDto.getSettlementMethodSeq().toString());
                // 決済タイプ設定
                paySelectModelItem.setSettlementMethodType(settlementDetailsDto.getSettlementMethodType());
                // 決済タイプ名設定
                paySelectModelItem.setSettlementMethodTypeName(
                                settlementDetailsDto.getSettlementMethodType().toString());
                // 請求タイプ設定
                paySelectModelItem.setBillType(settlementDetailsDto.getBillType());
                // 決済方法説明文PC設定
                paySelectModelItem.setSettlementNotePC(settlementDetailsDto.getSettlementNotePC());

                // ｸﾚｼﾞｯﾄ分割支払有効化ﾌﾗｸﾞ設定
                paySelectModelItem.setEnableInstallment(settlementDetailsDto.getEnableInstallment());
                // ｸﾚｼﾞｯﾄﾘﾎﾞ有効化ﾌﾗｸﾞ設定
                paySelectModelItem.setEnableRevolving(settlementDetailsDto.getEnableRevolving());

                // 決済方法リストに追加
                paySelectModel.getPaySelectModelItems().add(paySelectModelItem);

                BillingSlipResponse billingSlipResponse = null;
                try {
                    billingSlipResponse = billingSlipApi.get(
                                    paySelectHelper.toBillingSlipGetRequest(orderCommonModel.getTransactionId()));

                } catch (HttpClientErrorException | HttpServerErrorException e) {
                    LOGGER.error("例外処理が発生しました", e);
                    // アイテム名調整マップ
                    Map<String, String> itemNameAdjust = new HashMap<>();
                    handleError(e.getStatusCode(), e.getResponseBodyAsString(), error, itemNameAdjust);
                    return;
                }

                if (!ObjectUtils.isEmpty(billingSlipResponse)) {
                    paySelectModel.setSettlementMethod(billingSlipResponse.getPaymentMethodId());
                }

                if (HTypeSettlementMethodType.CREDIT.equals(settlementDetailsDto.getSettlementMethodType())) {

                    paySelectModelItem.setDividedNumber(
                                    billingSlipResponse != null && billingSlipResponse.getCreditResponse() != null ?
                                                    billingSlipResponse.getCreditResponse().getDividedNumber() :
                                                    null);
                    // 決済方法がクレジットの場合、お支払い方法のを初期値(一括)を設定する
                    paySelectModelItem.setPaymentType(HTypePaymentType.SINGLE.getValue());
                    // 有効期限：月のプルダウンを取得
                    Map<String, String> expirationDateMonthItems =
                                    EnumTypeUtil.getEnumMap(HTypeExpirationDateMonth.class);
                    // 有効期限（年）のプルダウン作成
                    Map<String, String> expirationDateYearItems = orderUtility.createExpirationDateYearItems();
                    // お支払い回数のプルダウン作成
                    // クレジット情報作成
                    paySelectModel.setExpirationDateMonthItems(expirationDateMonthItems);
                    paySelectModel.setExpirationDateYearItems(expirationDateYearItems);

                    if (paySelectModel.getResultCard() != null && CollectionUtil.isNotEmpty(
                                    paySelectModel.getResultCard().getCardList())
                        && !paySelectModel.isPreCreditInformationFlag()) {
                        // カード登録情報があり別のカードを使うボタンがおされていない場合、初期値として今回登録済みカードを使用する。
                        paySelectModel.setUseRegistCardFlg(true);
                        paySelectModel.setRegistCredit(true);
                        CardInfo cardInfo = (CardInfo) paySelectModel.getResultCard().getCardList().get(0);
                        paySelectModel.setDisplayCredit(true);
                        // カード番号
                        paySelectModelItem.setCardNumber(cardInfo.getCardNo());
                        // 有効期限：月
                        paySelectModelItem.setExpirationDateMonth(cardInfo.getExpire().substring(0, 2));
                        // 有効期限：年
                        paySelectModelItem.setExpirationDateYear(cardInfo.getExpire().substring(4));
                    } else if (paySelectModel.isPreCreditInformationFlag()) {
                        // 登録されているクレジットカード以外を使用し、注文確認画面から修正した場合の処理
                        // カード登録情報が存在する為
                        paySelectModel.setUseRegistCardFlg(true);

                        // カード情報を取得
                        paySelectModel.setResultCard(getRegistedCardInfo(paySelectModel, error));

                        // カード情報をModelにセット
                        paySelectHelper.setupRegistedCardInfo(paySelectModel, paySelectModel.getResultCard());

                        // 今回記入したクレジット情報は引き継がない
                        paySelectHelper.toPageForChangeOtherCard(paySelectModel);
                    }
                }
                // 選択候補が1つの場合は、ラジオボタンをチェック済みにしておく。
                if (settlementDtoList.size() == 1) {
                    paySelectModel.setSettlementMethod(settlementDetailsDto.getSettlementMethodSeq().toString());
                }
            }
        }

        // 利用可能な決済方法が１つもないときエラーメッセージを表示する
        if (paySelectModel.getSettlementDtoMap().isEmpty()) {
            addMessage(MSGCD_NO_SETTLEMENT_METHOD, redirectAttributes, model);
        }

        // 退避した決済方法リストが有る場合、決済方法SEQが一致する情報に入力情報を設定する
        paySelectHelper.restorePaymentPageItem(paySelectModel.getPaySelectModelItems(), bkPaySelectModelItems);
    }

    /**
     * GMOと通信しカード情報を取得する<br/>
     *
     * @param paySelectModel お支払い方法選択Model
     * @return SearchCardOutput GMOカード照会結果
     */
    protected SearchCardOutput getRegistedCardInfo(PaySelectModel paySelectModel, BindingResult error) {

        // 会員SEQ取得
        Integer memberInfoSeq = getCommonInfo().getCommonInfoUser().getMemberInfoSeq();

        try {
            // 会員情報取得
            CustomerResponse customerResponse = customerApi.getByMemberinfoSeq(memberInfoSeq);
            MemberInfoEntity memberEntity = paySelectHelper.toMemberInfoEntity(customerResponse);

            // Modelにセット
            paySelectModel.setMemberInfoEntity(memberEntity);

        } catch (HttpClientErrorException | HttpServerErrorException e) {
            LOGGER.error("例外処理が発生しました", e);
            // アイテム名調整マップ
            Map<String, String> itemNameAdjust = new HashMap<>();
            handleError(e.getStatusCode(), e.getResponseBodyAsString(), error, itemNameAdjust);
            return null;
        }

        CardInfoResponse cardInfoResponse = null;
        try {
            // GMOカード取得
            cardInfoResponse = cardApi.get();
        } catch (HttpClientErrorException | HttpServerErrorException e) {
            LOGGER.error("例外処理が発生しました", e);
            // アイテム名調整マップ
            Map<String, String> itemNameAdjust = new HashMap<>();
            handleError(e.getStatusCode(), e.getResponseBodyAsString(), error, itemNameAdjust);
            return null;
        }

        return paySelectHelper.toSearchCardOutput(cardInfoResponse);
    }

    /**
     * 不正操作が無いかチェックする<br/>
     *
     * @param paySelectModel お支払い方法選択Model
     */
    protected void checkOperationSettlementMethod(PaySelectModel paySelectModel) {
        // マップが存在しないかチェック
        if (paySelectModel.getSettlementDtoMap() == null) {
            throwMessage(MSGCD_ILLEGAL_OPERATION);
        }

        // マップに存在しない決済方法が指定されている
        if (!paySelectModel.getSettlementDtoMap().containsKey(paySelectModel.getSettlementMethod())) {
            throwMessage(MSGCD_SELECT_NO_SETTLEMENT_METHOD);
        }
    }
}