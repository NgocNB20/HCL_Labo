/*
 * Project Name : HIT-MALL3
 *
 * Copyright (C) 2008 i-TEC HANKYU HANSHIN INC. All Rights Reserved.
 *
 */

package jp.co.itechh.quad.admin.pc.web.admin.order.details;

import jp.co.itechh.quad.addressbook.presentation.api.AddressBookApi;
import jp.co.itechh.quad.addressbook.presentation.api.param.AddressBookAddressRegistRequest;
import jp.co.itechh.quad.addressbook.presentation.api.param.AddressBookAddressRegistResponse;
import jp.co.itechh.quad.addressbook.presentation.api.param.AddressBookAddressResponse;
import jp.co.itechh.quad.admin.annotation.exception.HEHandler;
import jp.co.itechh.quad.admin.base.exception.AppLevelListException;
import jp.co.itechh.quad.admin.base.exception.seasar.EmptyRuntimeException;
import jp.co.itechh.quad.admin.base.util.common.CollectionUtil;
import jp.co.itechh.quad.admin.base.util.common.DiffUtil;
import jp.co.itechh.quad.admin.base.util.seasar.StringUtil;
import jp.co.itechh.quad.admin.base.utility.ApplicationContextUtility;
import jp.co.itechh.quad.admin.base.utility.ConversionUtility;
import jp.co.itechh.quad.admin.base.utility.DateUtility;
import jp.co.itechh.quad.admin.constant.type.HTypeCouponLimitTargetType;
import jp.co.itechh.quad.admin.constant.type.HTypeEmergencyFlag;
import jp.co.itechh.quad.admin.constant.type.HTypeInvoiceAttachmentFlag;
import jp.co.itechh.quad.admin.constant.type.HTypeNoveltyPresentJudgmentStatus;
import jp.co.itechh.quad.admin.constant.type.HTypeOrderStatus;
import jp.co.itechh.quad.admin.constant.type.HTypePrefectureType;
import jp.co.itechh.quad.admin.constant.type.HTypeSettlementMethodType;
import jp.co.itechh.quad.admin.dto.common.CheckMessageDto;
import jp.co.itechh.quad.admin.dto.goods.goods.GoodsDetailsDto;
import jp.co.itechh.quad.admin.dto.order.OrderMessageDto;
import jp.co.itechh.quad.admin.pc.web.admin.common.ajax.MessageUtils;
import jp.co.itechh.quad.admin.pc.web.admin.common.ajax.ValidatorMessage;
import jp.co.itechh.quad.admin.pc.web.admin.common.validation.group.ConfirmGroup;
import jp.co.itechh.quad.admin.pc.web.admin.order.details.validation.OrderGoodsCountValidator;
import jp.co.itechh.quad.admin.pc.web.admin.order.details.validation.detailsupdate.group.OnceOrderCancelGroup;
import jp.co.itechh.quad.admin.pc.web.admin.order.details.validation.detailsupdate.group.OrderGoodsDeleteGroup;
import jp.co.itechh.quad.admin.pc.web.admin.order.details.validation.detailsupdate.group.OrderGoodsModifyGroup;
import jp.co.itechh.quad.admin.pc.web.admin.order.details.validation.detailsupdate.group.ReCalculateGroup;
import jp.co.itechh.quad.admin.util.common.EnumTypeUtil;
import jp.co.itechh.quad.admin.web.HeaderParamsHelper;
import jp.co.itechh.quad.bill.presentation.api.BillApi;
import jp.co.itechh.quad.billingslip.presentation.api.BillingSlipApi;
import jp.co.itechh.quad.billingslip.presentation.api.param.BillingSlipForRevisionByTransactionRevisionIdResponse;
import jp.co.itechh.quad.billingslip.presentation.api.param.GetBillingSlipForRevisionByTransactionRevisionIdRequest;
import jp.co.itechh.quad.convenience.presentation.api.ConvenienceApi;
import jp.co.itechh.quad.convenience.presentation.api.param.ConvenienceListResponse;
import jp.co.itechh.quad.coupon.presentation.api.CouponApi;
import jp.co.itechh.quad.coupon.presentation.api.param.CouponResponse;
import jp.co.itechh.quad.coupon.presentation.api.param.CouponVersionNoRequest;
import jp.co.itechh.quad.customer.presentation.api.CustomerApi;
import jp.co.itechh.quad.customer.presentation.api.param.CustomerResponse;
import jp.co.itechh.quad.examination.presentation.api.ExaminationApi;
import jp.co.itechh.quad.examination.presentation.api.param.ExamKitListResponse;
import jp.co.itechh.quad.examination.presentation.api.param.ExamKitRequest;
import jp.co.itechh.quad.method.presentation.api.SettlementMethodApi;
import jp.co.itechh.quad.method.presentation.api.ShippingMethodApi;
import jp.co.itechh.quad.method.presentation.api.param.DeliveryMethodResponse;
import jp.co.itechh.quad.method.presentation.api.param.PaymentMethodResponse;
import jp.co.itechh.quad.method.presentation.api.param.ShippingMethodListResponse;
import jp.co.itechh.quad.method.presentation.api.param.ShippingMethodResponse;
import jp.co.itechh.quad.mulpay.presentation.api.MulpayApi;
import jp.co.itechh.quad.mulpay.presentation.api.param.MulPayBillRequest;
import jp.co.itechh.quad.mulpay.presentation.api.param.MulPayBillResponse;
import jp.co.itechh.quad.orderreceived.presentation.api.OrderReceivedApi;
import jp.co.itechh.quad.orderreceived.presentation.api.param.OrderReceivedResponse;
import jp.co.itechh.quad.orderslip.presentation.api.OrderSlipApi;
import jp.co.itechh.quad.orderslip.presentation.api.param.GetOrderSlipForRevisionRequest;
import jp.co.itechh.quad.orderslip.presentation.api.param.OrderItemRevisionResponse;
import jp.co.itechh.quad.orderslip.presentation.api.param.OrderSlipForRevisionResponse;
import jp.co.itechh.quad.orderslip.presentation.api.param.OrderSlipGetRequest;
import jp.co.itechh.quad.orderslip.presentation.api.param.OrderSlipResponse;
import jp.co.itechh.quad.product.presentation.api.ProductApi;
import jp.co.itechh.quad.product.presentation.api.param.ProductDetailListGetRequest;
import jp.co.itechh.quad.product.presentation.api.param.ProductDetailListResponse;
import jp.co.itechh.quad.salesslip.presentation.api.SalesSlipApi;
import jp.co.itechh.quad.salesslip.presentation.api.param.GetSalesSlipForRevisionByTransactionRevisionIdRequest;
import jp.co.itechh.quad.salesslip.presentation.api.param.GetSalesSlipForRevisionByTransactionRevisionIdResponse;
import jp.co.itechh.quad.salesslip.presentation.api.param.SalesSlipGetRequest;
import jp.co.itechh.quad.salesslip.presentation.api.param.SalesSlipResponse;
import jp.co.itechh.quad.shippingslip.presentation.api.ShippingSlipApi;
import jp.co.itechh.quad.shippingslip.presentation.api.param.ShippingSlipForRevisionGetRequest;
import jp.co.itechh.quad.shippingslip.presentation.api.param.ShippingSlipResponse;
import jp.co.itechh.quad.transaction.presentation.api.TransactionApi;
import jp.co.itechh.quad.transaction.presentation.api.param.ApplyOriginCommissionAndCarriageForRevisionRequest;
import jp.co.itechh.quad.transaction.presentation.api.param.CancelTransactionForRevisionRequest;
import jp.co.itechh.quad.transaction.presentation.api.param.CancelTransactionForRevisionResponse;
import jp.co.itechh.quad.transaction.presentation.api.param.CheckTransactionForRevisionRequest;
import jp.co.itechh.quad.transaction.presentation.api.param.CheckTransactionForRevisionResponse;
import jp.co.itechh.quad.transaction.presentation.api.param.DeleteOrderItemFromTransactionForRevisionRequest;
import jp.co.itechh.quad.transaction.presentation.api.param.DisableCouponOfTransactionForRevisionRequest;
import jp.co.itechh.quad.transaction.presentation.api.param.EnableCouponOfTransactionForRevisionRequest;
import jp.co.itechh.quad.transaction.presentation.api.param.GetTransactionForRevisionRequest;
import jp.co.itechh.quad.transaction.presentation.api.param.RegistInputContentToSuspendedTransactionForRevisionRequest;
import jp.co.itechh.quad.transaction.presentation.api.param.RegistInputContentToTransactionForRevisionRequest;
import jp.co.itechh.quad.transaction.presentation.api.param.StartTransactionReviseRequest;
import jp.co.itechh.quad.transaction.presentation.api.param.StartTransactionReviseResponse;
import jp.co.itechh.quad.transaction.presentation.api.param.TransactionForRevisionResponse;
import jp.co.itechh.quad.transaction.presentation.api.param.WarningContent;
import org.apache.commons.collections.MapUtils;
import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
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
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.SessionAttributes;
import org.springframework.web.bind.support.SessionStatus;
import org.springframework.web.client.HttpClientErrorException;
import org.springframework.web.client.HttpServerErrorException;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import java.math.BigDecimal;
import java.sql.Timestamp;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.Objects;
import java.util.Optional;

/**
 * 受注修正 コントローラ<br/>
 */
@RequestMapping("/order/detailsupdate")
@Controller
@SessionAttributes({"detailsUpdateModel", "detailsUpdateCommonModel"})
@PreAuthorize("hasAnyAuthority('ORDER:8')")
public class DetailsUpdateController extends AbstractOrderDetailsController {

    /** ロガー */
    private static final Logger LOGGER = LoggerFactory.getLogger(DetailsUpdateController.class);

    /** メッセージ用 ：配送種別＝都道府県別で、お届け先が未設定の都道府県、且つ特別料金エリアにも設定が無い場合 */
    private static final String MSGCD_PREFECTURE_ERROR = "LOX000212";

    /** メッセージ用 ：商品の組み合わせにより選択可能な配送方法が無い場合 */
    private static final String MSGCD_SELECT_DELIVERYMETHOD_ZERO = "LOX000206";

    /** メッセージ用 ：商品の組み合わせにより選択可能な決済方法が無い場合 */
    private static final String MSGCD_SELECT_SETTLEMENTMETHOD_ZERO = "LOX000207";

    /** メッセージ用 ：処理対象が請求決済エラーの場合 */
    private static final String MSGCD_EMERGENCY_ERROR = "SOO002207";

    /** メッセージ用 ：処理対象が請求エラーかつ受注状態が出荷完了ではない場合 */
    private static final String MSGCD_EMERGENCY_BEFOLE_SHIPMENT_ERROR = "PKG-4177-003-A-W";

    /** フロントチェック：不正操作エラー */
    private static final String MSGCD_PARAM_ERROR = "AOX002201";

    /** フロントチェック：郵便番号と都道府県とが一致しない */
    private static final String MSGCD_PREFECTURE_CONSISTENCY = "AOX001011";

    /** フロントチェック：受注商品が未選択 */
    private static final String MSGCD_DELETE_FAIL = "AOX001012";

    /** フロントチェック：請求決済エラー時はカード決済選択不可 */
    private static final String MSGCD_SETTLEMENT_METHOD_FAIL = "AOX001013";

    /** フロントチェック：割引対象金額がクーポン適用金額の条件を満たしていない場合エラー */
    private static final String MSGCD_NOTFULL_COUPONDISCOUNTCONDITION = "PKG-4243-001-A-";

    /** フロントチェック：住所情報の非同期登録失敗 */
    private static final String MSGCD_TMPREGIST_ADDRESS = "AOX002302";

    /** フロントチェック：住所情報の非同期登録失敗 */
    private static final String MSGCD_AJAX_TMPREGIST_ADDRESS = "AOX002303";

    /** フロントチェック：改訂伝票への入力情報非同期登録失敗 */
    private static final String MSGCD_AJAX_TMPREGIST_INPUT = "AOX002304";

    /**
     * 処理件数マップ　ワーニングメッセージ
     * <code>WARNING_MESSAGE</code>
     */
    public static final String WARNING_MESSAGE = "WarningMessage";

    /** 対象商品チェック警告*/
    private static final String TARGET_GOODS_CHECK_MSGCD = "PRICE-PLANNING-TARG0002-W";

    /** 対象金額チェック警告 */
    private static final String COUPON_LOWER_PRICE_CHECK_MSGCD = "PRICE-PLANNING-IPPT0003-W";

    /** お届け希望日のチェックの警告メッセージコード */
    public static final String WCD_SELECTED_DELIVERY_DATE = "LOGISTIC-ENDD0001-W";

    /** マスタ最新値変更エラー：比較結果判定用Map */
    private static final Map<String, String> DIFF_ERROR_ITEM_MAP;

    static {
        Map<String, String> map = new LinkedHashMap<>();
        // 手数料
        map.put("commission", "ORDER-COMMISSION-001-");
        // 送料
        map.put("carriage", "ORDER-CARRIAGE-001-");
        DIFF_ERROR_ITEM_MAP = map;
    }

    /** 表示モード(md):list 検索画面の再検索実行 */
    private static final String MODE_LIST = "list";

    /** 表示モード:「list」の場合 再検索 */
    public static final String FLASH_MD = "md";

    /** 受注修正 helper */
    private final DetailsUpdateHelper detailsupdateHelper;

    /** 受注API */
    private final OrderReceivedApi orderReceivedApi;

    /** 取引API */
    private final TransactionApi transactionApi;

    /** 配送伝票API */
    private final ShippingSlipApi shippingSlipApi;

    /** 請求伝票API */
    private final BillingSlipApi billingSlipApi;

    /** 販売伝票API */
    private final SalesSlipApi salesSlipApi;

    /** 注文票API */
    private final OrderSlipApi orderSlipApi;

    /** 住所録API */
    private final AddressBookApi addressBookApi;

    /** 決済方法API */
    private final SettlementMethodApi settlementMethodApi;

    /** 配送方法API */
    private final ShippingMethodApi shippingMethodApi;

    /** 顧客API */
    private final CustomerApi customerApi;

    /** 商品API */
    private final ProductApi productApi;

    /** マルペイAPI */
    private final MulpayApi mulpayApi;

    /** マルペイAPI */
    private final ConvenienceApi convenienceApi;

    /** クーポンAPI */
    private final CouponApi couponApi;

    /** 請求Api */
    private final BillApi billApi;

    /** 検査API */
    private final ExaminationApi examinationApi;

    /** ヘッダパラメーターヘルパー */
    private final HeaderParamsHelper headerParamsHelper;

    /** 受注商品数量バリデータ */
    private final OrderGoodsCountValidator orderGoodsCountValidator;

    /** コンストラクター */
    @Autowired
    public DetailsUpdateController(DetailsUpdateHelper detailsupdateHelper,
                                   ConvenienceApi convenienceApi,
                                   OrderDetailsHelper orderDetailsHelper,
                                   CustomerApi customerApi,
                                   ProductApi productApi,
                                   OrderReceivedApi orderReceivedApi,
                                   TransactionApi transactionApi,
                                   ShippingSlipApi shippingSlipApi,
                                   BillingSlipApi billingSlipApi1,
                                   SalesSlipApi salesSlipApi,
                                   OrderSlipApi orderSlipApi,
                                   AddressBookApi addressBookApi,
                                   SettlementMethodApi settlementMethodApi,
                                   ShippingMethodApi shippingMethodApi,
                                   MulpayApi mulpayApi,
                                   CouponApi couponApi,
                                   BillApi billApi,
                                   ExaminationApi examinationApi,
                                   HeaderParamsHelper headerParamsHelper,
                                   OrderGoodsCountValidator orderGoodsCountValidator) {
        this.detailsupdateHelper = detailsupdateHelper;
        this.customerApi = customerApi;
        this.productApi = productApi;
        this.orderReceivedApi = orderReceivedApi;
        this.transactionApi = transactionApi;
        this.shippingSlipApi = shippingSlipApi;
        this.billingSlipApi = billingSlipApi1;
        this.salesSlipApi = salesSlipApi;
        this.orderSlipApi = orderSlipApi;
        this.addressBookApi = addressBookApi;
        this.convenienceApi = convenienceApi;
        this.settlementMethodApi = settlementMethodApi;
        this.shippingMethodApi = shippingMethodApi;
        this.mulpayApi = mulpayApi;
        this.couponApi = couponApi;
        this.billApi = billApi;
        this.examinationApi = examinationApi;
        this.headerParamsHelper = headerParamsHelper;
        this.orderGoodsCountValidator = orderGoodsCountValidator;
    }

    @InitBinder(value = "detailsUpdateModel")
    public void initBinder(WebDataBinder error) {
        // 受注修正の動的バリデータをセット
        error.addValidators(orderGoodsCountValidator);
    }

    /**
     * 受注修正画面：初期処理<br/>
     * ※ md=confirm 以外場合は新規改訂開始となる
     *
     * @return 受注修正画面
     */
    @GetMapping(value = "")
    @HEHandler(exception = AppLevelListException.class, returnView = "order/details/detailsupdate")
    public String doLoadIndex(@RequestParam(required = false) Optional<String> md,
                              @RequestParam(required = false) Optional<String> orderCode,
                              DetailsUpdateModel detailsUpdateModel,
                              BindingResult error,
                              DetailsUpdateCommonModel detailsUpdateCommonModel,
                              RedirectAttributes redirectAttrs,
                              Model model) { // ,

        // モード
        String mdParam = md.isPresent() ? md.get() : "";

        if (mdParam.equalsIgnoreCase("err")) {
            // 不正操作エラーの場合は、セッションをそのまま表示
            return "order/details/detailsupdate";
        }

        // 画面モデルクリア
        clearModel(DetailsUpdateModel.class, detailsUpdateModel, model);

        // コンポーネント値初期化
        initComponentValue(detailsUpdateModel);

        // 受注コード設定
        if (orderCode.isPresent()) {
            detailsUpdateCommonModel.setOrderCode(orderCode.get());
        }

        // 不正操作チェック
        illegalOperationCheck(true, detailsUpdateModel, detailsUpdateCommonModel, redirectAttrs, model);

        // mdがconfirm以外の場合は受注改訂開始を行う
        if (!mdParam.equals("confirm")) {

            // 受注取引情報取得
            OrderReceivedResponse orderReceivedResponse =
                            orderReceivedApi.getByOrderCode(detailsUpdateCommonModel.getOrderCode());

            // 受注を取得できなかった場合はエラー
            if (orderReceivedResponse == null) {
                addMessage(DetailsUpdateController.MSGCD_PARAM_ERROR, redirectAttrs, model);
                return "redirect:/error";
            }

            // 共通Modelへ取引IDを設定
            detailsUpdateCommonModel.setTransactionId(orderReceivedResponse.getLatestTransactionId());

            // 改訂用取引開始
            StartTransactionReviseRequest startTransactionReviseRequest = new StartTransactionReviseRequest();
            startTransactionReviseRequest.setTransactionId(detailsUpdateCommonModel.getTransactionId());
            StartTransactionReviseResponse startTransactionReviseResponse = null;
            try {
                startTransactionReviseResponse = transactionApi.startTransactionRevise(startTransactionReviseRequest);
            }
            // APIからエラーコードが返ってきた場合は、エラー画面へリダイレクトさせる
            // HEHandlerのreturnViewは自画面遷移なので、共通メソッドを呼び出さず、こちらでエラーハンドリングをする
            catch (HttpClientErrorException ce) {
                LOGGER.error("例外処理が発生しました", ce);
                // 想定内のエラー（400番台）の場合
                createClientErrorMessage(ce.getResponseBodyAsString(), error, redirectAttrs, model);
                return "redirect:/error";

            } catch (HttpServerErrorException se) {
                LOGGER.error("例外処理が発生しました", se);
                // 想定外のエラー（500番台）の場合
                createServerErrorMessage(se.getResponseBodyAsString(), redirectAttrs, model);
                return "redirect:/error";
            }

            // 改訂用取引開始レスポンスが取得できなかった場合はエラー
            if (startTransactionReviseResponse == null) {
                addMessage(DetailsUpdateController.MSGCD_PARAM_ERROR, redirectAttrs, model);
                return "redirect:/error";
            }

            // 共通Modelへ改訂用取引IDを設定
            detailsUpdateCommonModel.setTransactionRevisionId(
                            startTransactionReviseResponse.getTransactionRevisionId());
        }

        // 受注改訂チェック
        CheckTransactionForRevisionRequest checkTransactionForRevisionRequest =
                        new CheckTransactionForRevisionRequest();
        checkTransactionForRevisionRequest.setTransactionRevisionId(
                        detailsUpdateCommonModel.getTransactionRevisionId());
        try {
            CheckTransactionForRevisionResponse checkResponse =
                            transactionApi.checkTransactionForRevision(checkTransactionForRevisionRequest);

            if (MapUtils.isNotEmpty(checkResponse.getWarningMessage()) && checkResponse.getWarningMessage()
                                                                                       .containsKey(WARNING_MESSAGE)) {
                checkResponse.getWarningMessage().get(WARNING_MESSAGE).forEach(message -> {
                    addWarnMessage(message.getCode(), null, redirectAttrs, model);
                });
            }
        } catch (HttpClientErrorException ce) {
            LOGGER.error("例外処理が発生しました", ce);
            // 想定内のエラー（400番台）の場合
            // 妥当性チェック用のエラーハンドリングのため、このタイミングでは画面遷移をさせず、後続処理を実行する ※妥当性チェックでは料金計算の再取得も行われるため
            createClientErrorMessage(ce.getResponseBodyAsString(), error, redirectAttrs, model);

            if (isExistsErrCode(ce.getResponseBodyAsString(), EXCLUSIVE_CONTROL_ERR)) {
                return "redirect:/order/details/?orderCode=" + detailsUpdateCommonModel.getOrderCode() + "&from=order";
            }
        } catch (HttpServerErrorException se) {
            LOGGER.error("例外処理が発生しました", se);
            // 想定外のエラー（500番台）の場合
            createServerErrorMessage(se.getResponseBodyAsString(), redirectAttrs, model);
            return "redirect:/error";
        }

        // 詳細情報取得
        OrderDetailsRevisionResponseModel orderDetailsRevisionResponseModel = null;
        try {
            orderDetailsRevisionResponseModel = getOrderDetail(detailsUpdateCommonModel.getOrderCode(),
                                                               detailsUpdateCommonModel.getTransactionRevisionId()
                                                              );
        } catch (NullPointerException ne) {
            LOGGER.error("例外処理が発生しました", ne);
            // 情報が欠陥しているのでエラー画面にリダイレクト
            addMessage(DetailsUpdateController.MSGCD_PARAM_ERROR, redirectAttrs, model);
            return "redirect:/error";
        }
        // APIからエラーコードが返ってきた場合は、エラー画面へリダイレクトさせる
        // HEHandlerのreturnViewは自画面遷移なので、AbstractControllerの共通メソッドを呼び出さず、こちらでエラーハンドリングをする
        catch (HttpClientErrorException ce) {
            LOGGER.error("例外処理が発生しました", ce);
            // 想定内のエラー（400番台）の場合
            createClientErrorMessage(ce.getResponseBodyAsString(), error, redirectAttrs, model);
            return "redirect:/error";

        } catch (HttpServerErrorException se) {
            LOGGER.error("例外処理が発生しました", se);
            // 想定外のエラー（500番台）の場合
            createServerErrorMessage(se.getResponseBodyAsString(), redirectAttrs, model);
            return "redirect:/error";
        }

        // 受注キャンセルチェック
        if ("CANCEL".equals(orderDetailsRevisionResponseModel.getOrderReceivedResponse().getOrderStatus())) {
            // 受注詳細ページへリダイレクト
            return "redirect:/order/details/?md=cou&orderCode=" + detailsUpdateCommonModel.getOrderCode();
        }

        // TODO チェック処理の暫定対応としてOrderMessageDtoを利用
        //   ※toPageの中で、OrderMessageDtoの利用箇所は上記TODOコメントを付与済み
        //   toPage以降のチェック処理でインスタンスを参照するので、ここで生成しておかないとエラーになる
        //   また、上記Dtoを利用しているチェック処理自体は、現在未実装
        OrderMessageDto orderMessageDto = ApplicationContextUtility.getBean(OrderMessageDto.class);
        detailsUpdateModel.setOrderMessageDto(orderMessageDto);

        // 画面Modelへ反映する
        this.toPage(detailsUpdateModel, orderDetailsRevisionResponseModel, redirectAttrs, model);

        // mdがnew（初期表示）の場合のみ、マスタ値比較を行う
        if ("new".equals(mdParam)) {
            this.createDiffMessage(orderDetailsRevisionResponseModel, redirectAttrs, model);
        }

        // 請求決済エラーの場合はメッセージを表示
        if ((detailsUpdateModel.getEmergencyFlag() == HTypeEmergencyFlag.ON)) {
            // 受注状態が出荷完了以外の場合
            if (detailsUpdateModel.getOrderStatus() != HTypeOrderStatus.SHIPMENT_COMPLETION) {
                addMessage(MSGCD_EMERGENCY_BEFOLE_SHIPMENT_ERROR, redirectAttrs, model);
            }
            addMessage(MSGCD_EMERGENCY_ERROR, redirectAttrs, model);
        }

        // 「受注修正確認」画面から戻ってきた時に「決済手数料ダイアログ選択フラグ」をリセット
        detailsUpdateModel.setCommissionSelected(false);

        return "order/details/detailsupdate";
    }

    /**
     * 受注詳細情報取得
     *
     * @return OrderDetailsRevisionResponseModel
     */
    private OrderDetailsRevisionResponseModel getOrderDetail(String orderCode, String transactionRevisionId) {

        // 戻り値
        OrderDetailsRevisionResponseModel orderDetailsRevisionResponseModel = new OrderDetailsRevisionResponseModel();
        GetTransactionForRevisionRequest getTransactionForRevisionRequest = new GetTransactionForRevisionRequest();
        getTransactionForRevisionRequest.setTransactionRevisionId(transactionRevisionId);
        TransactionForRevisionResponse transactionForRevisionResponse =
                        transactionApi.getTransactionForRevision(getTransactionForRevisionRequest);
        /* 伝票情報取得 */
        // 受注取引情報取得
        OrderReceivedResponse orderReceivedResponse = orderReceivedApi.getByOrderCode(orderCode);
        // 受注を取得できなかった場合はエラー
        if (ObjectUtils.isEmpty(orderReceivedResponse)) {
            throw new NullPointerException();
        }

        // 改訂元取引ID
        String transactionId = orderReceivedResponse.getLatestTransactionId();

        // 改訂用配送伝票取得
        ShippingSlipForRevisionGetRequest shippingSlipForRevisionGetRequest = new ShippingSlipForRevisionGetRequest();
        shippingSlipForRevisionGetRequest.setTransactionRevisionId(transactionRevisionId);
        ShippingSlipResponse shippingSlipForRevisionResponse =
                        shippingSlipApi.getForRevisionByTransactionRevisionId(shippingSlipForRevisionGetRequest);

        // 改訂用請求伝票取得
        GetBillingSlipForRevisionByTransactionRevisionIdRequest billingSlipRevisionIdRequest =
                        new GetBillingSlipForRevisionByTransactionRevisionIdRequest();
        billingSlipRevisionIdRequest.setTransactionRevisionId(transactionRevisionId);
        BillingSlipForRevisionByTransactionRevisionIdResponse billingSlipForRevisionResponse =
                        billingSlipApi.getBillingSlipForRevisionByTransactionRevisionId(billingSlipRevisionIdRequest);

        // 改訂用販売伝票取得
        GetSalesSlipForRevisionByTransactionRevisionIdRequest salesSlipForRevisionGetRequest =
                        new GetSalesSlipForRevisionByTransactionRevisionIdRequest();
        salesSlipForRevisionGetRequest.setTransactionRevisionId(transactionRevisionId);
        GetSalesSlipForRevisionByTransactionRevisionIdResponse salesSlipForRevisionResponse =
                        salesSlipApi.getSalesSlipForRevisionByTransactionRevisionId(salesSlipForRevisionGetRequest);

        // 改訂元販売伝票取得
        SalesSlipGetRequest salesSlipGetRequest = new SalesSlipGetRequest();
        salesSlipGetRequest.setTransactionId(transactionId);
        SalesSlipResponse salesSlipResponse = salesSlipApi.get(salesSlipGetRequest);

        // 改訂用注文票取得
        GetOrderSlipForRevisionRequest getOrderSlipForRevisionRequest = new GetOrderSlipForRevisionRequest();
        getOrderSlipForRevisionRequest.setTransactionRevisionId(transactionRevisionId);
        OrderSlipForRevisionResponse orderSlipForRevisionResponse =
                        orderSlipApi.getOrderSlipForRevision(getOrderSlipForRevisionRequest);
        // 伝票情報が存在しない場合はエラー
        if (ObjectUtils.isEmpty(shippingSlipForRevisionResponse) || ObjectUtils.isEmpty(billingSlipForRevisionResponse)
            || ObjectUtils.isEmpty(salesSlipForRevisionResponse) || ObjectUtils.isEmpty(orderSlipForRevisionResponse)) {
            throw new NullPointerException();
        }

        /* マスタ情報取得 */
        // 顧客情報取得
        CustomerResponse customerResponse =
                        customerApi.getByMemberinfoSeq(Integer.parseInt(orderReceivedResponse.getCustomerId()));

        // お届け先住所情報の取得
        AddressBookAddressResponse shippingAddressResponse =
                        addressBookApi.getAddressById(shippingSlipForRevisionResponse.getShippingAddressId());

        // 請求先住所情報の取得
        AddressBookAddressResponse billingAddressResponse =
                        addressBookApi.getAddressById(billingSlipForRevisionResponse.getBillingAddressId());

        // 決済方法の取得
        PaymentMethodResponse paymentMethodResponse = settlementMethodApi.getBySettlementMethodSeq(
                        Integer.valueOf(billingSlipForRevisionResponse.getPaymentMethodId()));

        // 配送方法の取得
        ShippingMethodResponse shippingMethodResponse = shippingMethodApi.getByDeliveryMethodSeq(
                        Integer.parseInt(shippingSlipForRevisionResponse.getShippingMethodId()));

        // マルチペイメント情報を取得
        MulPayBillRequest mulPayBillRequest = new MulPayBillRequest();
        mulPayBillRequest.setOrderCode(orderCode);

        MulPayBillResponse mulPayBillResponse = mulpayApi.getByOrderCode(mulPayBillRequest);

        ConvenienceListResponse convenienceListResponse = null;
        if (EnumTypeUtil.getEnumFromValue(
                        HTypeSettlementMethodType.class, paymentMethodResponse.getSettlementMethodType())
            == HTypeSettlementMethodType.LINK_PAYMENT) {
            if ("3".equals(billingSlipForRevisionResponse.getPayType())) {
                // コンビニ一覧
                convenienceListResponse = convenienceApi.get();
            }
        }

        // クーポンが適用されている場合
        CouponResponse couponResponse = null;
        if (salesSlipForRevisionResponse.getApplyCouponResponse() != null
            && salesSlipForRevisionResponse.getApplyCouponResponse().getCouponCode() != null) {
            // クーポン情報を取得
            CouponVersionNoRequest couponVersionNoRequest = new CouponVersionNoRequest();
            couponVersionNoRequest.setCouponSeq(salesSlipForRevisionResponse.getApplyCouponResponse().getCouponSeq());
            couponVersionNoRequest.setCouponVersionNo(
                            salesSlipForRevisionResponse.getApplyCouponResponse().getCouponVersionNo());

            couponResponse = couponApi.getByCouponVersionNo(couponVersionNoRequest);

            if (ObjectUtils.isEmpty(couponResponse)) {
                // 公開後のクーポン情報は削除不可だが、取得できない場合はエラー
                throw new NullPointerException();
            }
        }

        /* 商品サービスから商品詳細リストを取得 */
        // 注文商品リストから商品SEQリストを作成
        List<Integer> goodsSeqList = new ArrayList<>();
        List<String> orderItemIdList = new ArrayList<>();
        for (OrderItemRevisionResponse orderItemRevisionResponse : orderSlipForRevisionResponse.getOrderItemRevisionList()) {
            goodsSeqList.add(Integer.parseInt(orderItemRevisionResponse.getItemId()));
            orderItemIdList.add(orderItemRevisionResponse.getOrderItemId());
        }

        // 商品詳細リスト取得
        ProductDetailListGetRequest productDetailListGetrequest = new ProductDetailListGetRequest();
        productDetailListGetrequest.setGoodsSeqList(goodsSeqList);
        ProductDetailListResponse productDetailListresponse = productApi.getDetails(productDetailListGetrequest, null);
        List<GoodsDetailsDto> goodsDtoList = null;
        if (!ObjectUtils.isEmpty(productDetailListresponse) && !CollectionUtils.isEmpty(
                        productDetailListresponse.getGoodsDetailsList())) {
            goodsDtoList = detailsupdateHelper.toProductDetailList(productDetailListresponse,
                                                                   orderSlipForRevisionResponse
                                                                  );
        }

        // 注文商品IDリストにひもづく検査キット情報の取得
        ExamKitRequest examKitRequest = new ExamKitRequest();
        examKitRequest.setOrderItemIdList(orderItemIdList);
        ExamKitListResponse examKitListResponse = examinationApi.getExamKitList(examKitRequest);
        orderDetailsRevisionResponseModel.setExamKitListResponse(examKitListResponse);

        // 各種マスタ情報が存在しない場合はエラー
        if (ObjectUtils.isEmpty(customerResponse) || ObjectUtils.isEmpty(shippingAddressResponse)
            || ObjectUtils.isEmpty(billingAddressResponse) || ObjectUtils.isEmpty(paymentMethodResponse)
            || ObjectUtils.isEmpty(shippingMethodResponse) || ObjectUtils.isEmpty(
                        shippingMethodResponse.getDeliveryMethodResponse()) || CollectionUtils.isEmpty(goodsDtoList)) {
            throw new NullPointerException();
        }

        // 取引ID,改訂用取引IDを設定
        orderDetailsRevisionResponseModel.setTransactionId(transactionId);
        orderDetailsRevisionResponseModel.setTransactionRevisionId(transactionRevisionId);
        orderReceivedResponse.setAdminMemo(transactionForRevisionResponse.getAdminMemo());
        orderReceivedResponse.setNotificationFlag(transactionForRevisionResponse.getNotificationFlag());
        orderReceivedResponse.setNoveltyPresentJudgmentStatus(
                        transactionForRevisionResponse.getNoveltyPresentJudgmentStatus());
        // 伝票情報取得結果を設定
        orderDetailsRevisionResponseModel.setOrderReceivedResponse(orderReceivedResponse);
        orderDetailsRevisionResponseModel.setShippingSlipForRevisionResponse(shippingSlipForRevisionResponse);
        orderDetailsRevisionResponseModel.setBillingSlipForRevisionResponse(billingSlipForRevisionResponse);
        orderDetailsRevisionResponseModel.setSalesSlipForRevisionResponse(salesSlipForRevisionResponse);
        orderDetailsRevisionResponseModel.setSalesSlipResponse(salesSlipResponse);
        orderDetailsRevisionResponseModel.setOrderSlipForRevisionResponse(orderSlipForRevisionResponse);


        // マスタ情報を設定
        orderDetailsRevisionResponseModel.setCustomerResponse(customerResponse);
        orderDetailsRevisionResponseModel.setShippingAddressResponse(shippingAddressResponse);
        orderDetailsRevisionResponseModel.setBillingAddressResponse(billingAddressResponse);
        orderDetailsRevisionResponseModel.setPaymentMethodResponse(paymentMethodResponse);
        orderDetailsRevisionResponseModel.setShippingMethodResponse(shippingMethodResponse);
        orderDetailsRevisionResponseModel.setMulPayBillResponse(mulPayBillResponse);
        orderDetailsRevisionResponseModel.setCouponResponse(couponResponse);
        orderDetailsRevisionResponseModel.setGoodsDtoList(goodsDtoList);
        orderDetailsRevisionResponseModel.setConvenienceListResponse(convenienceListResponse);

        return orderDetailsRevisionResponseModel;
    }

    /**
     * 画面項目を改訂伝票に保存する<br/>
     * ※ajax等で使用
     */
    @PostMapping(value = "doRegistToRevision")
    @ResponseBody
    public ResponseEntity<?> doRegistToRevision(
                    @Validated(OrderGoodsModifyGroup.class) DetailsUpdateModel detailsUpdateModel,
                    BindingResult error,
                    DetailsUpdateCommonModel detailsUpdateCommonModel,
                    RedirectAttributes redirectAttrs,
                    Model model) {

        // 実行前処理
        String check = preDoAction(detailsUpdateModel, detailsUpdateCommonModel, redirectAttrs, model);
        if (StringUtils.isNotEmpty(check)) {
            return ResponseEntity.badRequest().body(check);
        }

        if (error.hasErrors()) {
            List<ValidatorMessage> mapError = MessageUtils.getMessageErrorFromBindingResult(error);
            return ResponseEntity.badRequest().body(mapError);
        }

        // 住所に変更があった場合に新規住所を登録して画面Modelに設定する
        // 他の箇所でも下記メソッドを呼び出しており、エラー時は下記メソッド内部でthrowを投げるが、ここではAjax用のエラーハンドリングで対応
        try {
            setNewAddressIdIfChanged(detailsUpdateModel, error);
        } catch (Exception e) {
            LOGGER.error("例外処理が発生しました", e);
            List<ValidatorMessage> messageList = new ArrayList<>();
            MessageUtils.getAllMessage(messageList, MSGCD_AJAX_TMPREGIST_ADDRESS, null);
            return ResponseEntity.badRequest().body(messageList);
        }

        // 入力内容を改訂用取引へ反映する
        RegistInputContentToTransactionForRevisionRequest registInputContentToTransactionForRevisionRequest =
                        detailsupdateHelper.toRegistInputContentToTransactionForRevisionRequest(detailsUpdateModel,
                                                                                                detailsUpdateCommonModel
                                                                                               );
        try {
            transactionApi.registInputContentToTransactionForRevision(
                            registInputContentToTransactionForRevisionRequest);
        } catch (HttpClientErrorException ce) {
            LOGGER.error("例外処理が発生しました", ce);
            // 想定内のエラー（400番台）の場合
            List<ValidatorMessage> messageList = new ArrayList<>();
            MessageUtils.getAllMessage(messageList, MSGCD_AJAX_TMPREGIST_INPUT, null);
            return ResponseEntity.badRequest().body(messageList);
        } catch (HttpServerErrorException se) {
            LOGGER.error("例外処理が発生しました", se);
            // 想定外のエラー（500番台）の場合
            createServerErrorMessage(se.getResponseBodyAsString(), redirectAttrs, model);
            return ResponseEntity.badRequest().body("redirect:/error");
        }

        return ResponseEntity.ok(new ArrayList<>());
    }

    /**
     * 受注詳細修正AJAX呼出<br/>
     *
     * @return お届け時間帯
     */
    @GetMapping(value = "/ajax")
    @ResponseBody
    public ResponseEntity<String> doChangeTimeZone(@RequestParam(required = false) Optional<String> deliveryMethodSeq,
                                                   DetailsUpdateModel detailsUpdateModel) {

        // トップ画面アラートから遷移したとき、商品SEQを取得する。
        if (!deliveryMethodSeq.isPresent()) {
            return ResponseEntity.ok("選択不可");
        }

        // 変換Helper取得
        ConversionUtility conversionUtility = ApplicationContextUtility.getBean(ConversionUtility.class);

        String requestDeliveryMethodSeq = deliveryMethodSeq.get();
        Integer iDeliveryMethodSeq = conversionUtility.toInteger(requestDeliveryMethodSeq);

        ShippingMethodResponse shippingMethodResponse = shippingMethodApi.getByDeliveryMethodSeq(iDeliveryMethodSeq);
        if (shippingMethodResponse == null) {
            return ResponseEntity.ok("選択不可");
        }

        List<String> list = new ArrayList<>();
        DeliveryMethodResponse deliveryMethodResponse = shippingMethodResponse.getDeliveryMethodResponse();
        if (shippingMethodResponse.getDeliveryMethodResponse().getReceiverTimeZone1() != null) {
            list.add(deliveryMethodResponse.getReceiverTimeZone1());
        }
        if (deliveryMethodResponse.getReceiverTimeZone2() != null) {
            list.add(deliveryMethodResponse.getReceiverTimeZone2());
        }
        if (deliveryMethodResponse.getReceiverTimeZone3() != null) {
            list.add(deliveryMethodResponse.getReceiverTimeZone3());
        }
        if (deliveryMethodResponse.getReceiverTimeZone4() != null) {
            list.add(deliveryMethodResponse.getReceiverTimeZone4());
        }
        if (deliveryMethodResponse.getReceiverTimeZone5() != null) {
            list.add(deliveryMethodResponse.getReceiverTimeZone5());
        }
        if (deliveryMethodResponse.getReceiverTimeZone6() != null) {
            list.add(deliveryMethodResponse.getReceiverTimeZone6());
        }
        if (deliveryMethodResponse.getReceiverTimeZone7() != null) {
            list.add(deliveryMethodResponse.getReceiverTimeZone7());
        }
        if (deliveryMethodResponse.getReceiverTimeZone8() != null) {
            list.add(deliveryMethodResponse.getReceiverTimeZone8());
        }
        if (deliveryMethodResponse.getReceiverTimeZone9() != null) {
            list.add(deliveryMethodResponse.getReceiverTimeZone9());
        }
        if (deliveryMethodResponse.getReceiverTimeZone10() != null) {
            list.add(deliveryMethodResponse.getReceiverTimeZone10());
        }

        StringBuilder strbuff = new StringBuilder(200);
        strbuff.append("<select id=\"receiverTimeZone\" name=\"orderReceiverItem.receiverTimeZone\" value=\"orderReceiverItem.receiverTimeZone\" class=\"c-form-control w160px\">");
        strbuff.append("<option value=\"\">選択してください</option>");

        for (String str : list) {
            strbuff.append("<option value=\"");
            strbuff.append(str);
            strbuff.append("\">");
            strbuff.append(str);
            strbuff.append("</option>");
        }
        strbuff.append("</select>");

        // 画面Modelへも反映する
        detailsupdateHelper.setTimeZoneItem(detailsUpdateModel, shippingMethodResponse.getDeliveryMethodResponse());

        return ResponseEntity.ok(strbuff.toString());
    }

    /**
     * マスタ値変更アラートメッセージ表示<br/>
     * マスタ変更により、受注情報が変更された場合、アラートを表示する<br/>
     *
     * @param orderDetailsRevisionResponseModel
     * @param redirectAttributes
     * @param model
     */
    private void createDiffMessage(OrderDetailsRevisionResponseModel orderDetailsRevisionResponseModel,
                                   RedirectAttributes redirectAttributes,
                                   Model model) {
        // 比較用プロパティマップを取得
        Map<String, Object> salesSlipResponseMap =
                        DiffUtil.getPropertyMap(orderDetailsRevisionResponseModel.getSalesSlipResponse());
        Map<String, Object> salesSlipForRevisionResponseMap =
                        DiffUtil.getPropertyMap(orderDetailsRevisionResponseModel.getSalesSlipForRevisionResponse());

        // 比較結果判定用Mapに定義した項目を順に比較し、値が変更された項目はメッセージを出力
        for (String key : DIFF_ERROR_ITEM_MAP.keySet()) {
            List<String> orderInfoDiff =
                            DiffUtil.diff(salesSlipResponseMap.get(key), salesSlipForRevisionResponseMap.get(key));
            if (orderInfoDiff != null && orderInfoDiff.size() > 0) {
                addMessage(DIFF_ERROR_ITEM_MAP.get(key), redirectAttributes, model);
            }
        }
    }

    /**
     * 再計算アクション
     *
     * @return 自画面
     */
    @PostMapping(value = "/", params = "doReCalculate")
    @HEHandler(exception = AppLevelListException.class, returnView = "order/details/detailsupdate")
    public String doReCalculate(@Validated(ReCalculateGroup.class) DetailsUpdateModel detailsUpdateModel,
                                BindingResult error,
                                DetailsUpdateCommonModel detailsUpdateCommonModel,
                                Model model,
                                RedirectAttributes redirectAttrs) {

        // 実行前処理
        String check = preDoAction(detailsUpdateModel, detailsUpdateCommonModel, redirectAttrs, model);
        if (StringUtils.isNotEmpty(check)) {
            return check;
        }

        if (error.hasErrors()) {
            return "order/details/detailsupdate";
        }

        // 住所に変更があった場合に新規住所を登録して画面Modelに設定する
        setNewAddressIdIfChanged(detailsUpdateModel, error);
        if (error.hasErrors()) {
            // setNewAddressIdIfChangedで項目バリデーションエラーが発生した場合は、こちらでエラーハンドリングを実行
            return "order/details/detailsupdate";
        }

        // 入力内容を改訂用取引へ反映する
        RegistInputContentToTransactionForRevisionRequest registInputContentToTransactionForRevisionRequest =
                        detailsupdateHelper.toRegistInputContentToTransactionForRevisionRequest(detailsUpdateModel,
                                                                                                detailsUpdateCommonModel
                                                                                               );
        try {
            transactionApi.registInputContentToTransactionForRevision(
                            registInputContentToTransactionForRevisionRequest);
        } catch (HttpClientErrorException | HttpServerErrorException e) {
            LOGGER.error("例外処理が発生しました", e);
            // アイテム名調整マップ
            Map<String, String> itemNameAdjust = new HashMap<>();
            handleError(e.getStatusCode(), e.getResponseBodyAsString(), error, itemNameAdjust);

            if (error.hasErrors()) {
                return "order/details/detailsupdate";
            }
        }
        // 受注詳細修正ページへ遷移
        return "redirect:/order/detailsupdate/?md=confirm";
    }

    /**
     * 商品削除アクション
     *
     * @return 自画面
     */
    @PostMapping(value = "/", params = "doOrderGoodsDelete")
    @HEHandler(exception = AppLevelListException.class, returnView = "order/details/detailsupdate")
    public String doOrderGoodsDelete(@Validated(OrderGoodsDeleteGroup.class) DetailsUpdateModel detailsUpdateModel,
                                     BindingResult error,
                                     DetailsUpdateCommonModel detailsUpdateCommonModel,
                                     Model model,
                                     RedirectAttributes redirectAttrs) {

        // 実行前処理
        String check = preDoAction(detailsUpdateModel, detailsUpdateCommonModel, redirectAttrs, model);
        if (StringUtils.isNotEmpty(check)) {
            return check;
        }

        if (error.hasErrors()) {
            return "order/details/detailsupdate";
        }

        // 商品が未選択の場合はエラーメッセージを出力
        if (!isGoodsSelect(detailsUpdateModel)) {
            throwMessage(MSGCD_DELETE_FAIL);
        }

        // 改訂元注文票取得
        OrderSlipGetRequest orderSlipGetRequest = new OrderSlipGetRequest();
        orderSlipGetRequest.setTransactionId(detailsUpdateCommonModel.getTransactionId());
        OrderSlipResponse orderSlipOriginalResponse = orderSlipApi.get(orderSlipGetRequest);
        // 伝票情報が存在しない場合はエラー
        if (orderSlipOriginalResponse == null) {
            addMessage(DetailsUpdateController.MSGCD_PARAM_ERROR, redirectAttrs, model);
            return "redirect:/error";
        }

        // 新規追加商品の削除リスト
        List<Integer> deleteItemSeqList = new ArrayList<>();

        for (OrderGoodsUpdateItem orderGoodsDispItem : detailsUpdateModel.getOrderReceiverItem()
                                                                         .getOrderGoodsUpdateItems()) {

            // 画面で対象チェックされている商品
            if (orderGoodsDispItem.isGoodsCheck()) {

                if (CollectionUtil.isNotEmpty(orderSlipOriginalResponse.getItemList())
                    && orderSlipOriginalResponse.getItemList()
                                                .stream()
                                                .filter(orderItemOriginal -> orderItemOriginal.getOrderItemSeq() != null
                                                                             && orderItemOriginal.getOrderItemSeq()
                                                                                                 .equals(orderGoodsDispItem.getOrderDisplay()))
                                                .findFirst()
                                                .isPresent()) {
                    // 改訂元商品に存在する場合
                    orderGoodsDispItem.setGoodsCount(BigDecimal.ZERO);
                    orderGoodsDispItem.setUpdateGoodsCount("0");
                } else {
                    // 改訂元商品に存在しない場合、削除用リストに注文商品連番を追加
                    deleteItemSeqList.add(orderGoodsDispItem.getOrderDisplay());
                }
            }
        }

        /* 入力内容反映 */
        // 住所に変更があった場合に新規住所を登録して画面Modelに設定する
        setNewAddressIdIfChanged(detailsUpdateModel, error);
        if (error.hasErrors()) {
            // setNewAddressIdIfChangedで項目バリデーションエラーが発生した場合は、こちらでエラーハンドリングを実行
            return "order/details/detailsupdate";
        }

        // 入力内容を改訂用取引へ反映する
        RegistInputContentToTransactionForRevisionRequest registInputContentToTransactionForRevisionRequest =
                        detailsupdateHelper.toRegistInputContentToTransactionForRevisionRequest(detailsUpdateModel,
                                                                                                detailsUpdateCommonModel
                                                                                               );
        try {
            transactionApi.registInputContentToTransactionForRevision(
                            registInputContentToTransactionForRevisionRequest);
        } catch (HttpClientErrorException | HttpServerErrorException e) {
            LOGGER.error("例外処理が発生しました", e);
            // アイテム名調整マップ
            Map<String, String> itemNameAdjust = new HashMap<>();
            handleError(e.getStatusCode(), e.getResponseBodyAsString(), error, itemNameAdjust);

            if (error.hasErrors()) {
                return "order/details/detailsupdate";
            }
        }

        // 注文商品削除処理（新規追加分）
        if (!CollectionUtils.isEmpty(deleteItemSeqList)) {
            DeleteOrderItemFromTransactionForRevisionRequest deleteOrderItemFromTransactionForRevisionRequest =
                            new DeleteOrderItemFromTransactionForRevisionRequest();
            deleteOrderItemFromTransactionForRevisionRequest.setTransactionRevisionId(
                            detailsUpdateCommonModel.getTransactionRevisionId());
            deleteOrderItemFromTransactionForRevisionRequest.setItemSeqList(deleteItemSeqList);
            try {
                transactionApi.deleteOrderItemFromTransactionForRevision(
                                deleteOrderItemFromTransactionForRevisionRequest);
            } catch (HttpClientErrorException | HttpServerErrorException e) {
                LOGGER.error("例外処理が発生しました", e);
                // アイテム名調整マップ
                Map<String, String> itemNameAdjust = new HashMap<>();
                handleError(e.getStatusCode(), e.getResponseBodyAsString(), error, itemNameAdjust);

                if (error.hasErrors()) {
                    return "order/details/detailsupdate";
                }
            }
        }

        return "redirect:/order/detailsupdate/?md=confirm";
    }

    /**
     * クーポン利用状況変更アクション
     *
     * @return 自画面
     */
    @PostMapping(value = "/", params = "doCouponLimitTarget")
    @HEHandler(exception = AppLevelListException.class, returnView = "order/details/detailsupdate")
    public String doCouponLimitTarget(DetailsUpdateModel detailsUpdateModel,
                                      BindingResult error,
                                      DetailsUpdateCommonModel detailsUpdateCommonModel,
                                      RedirectAttributes redirectAttrs,
                                      Model model) {

        // 実行前処理
        String check = preDoAction(detailsUpdateModel, detailsUpdateCommonModel, redirectAttrs, model);
        if (StringUtils.isNotEmpty(check)) {
            return check;
        }

        try {
            if (HTypeCouponLimitTargetType.OFF.getValue().equals(detailsUpdateModel.getCouponLimitTargetTypeValue())) {
                // 無効化の場合
                DisableCouponOfTransactionForRevisionRequest disableCouponOfTransactionForRevisionRequest =
                                new DisableCouponOfTransactionForRevisionRequest();
                disableCouponOfTransactionForRevisionRequest.setTransactionRevisionId(
                                detailsUpdateCommonModel.getTransactionRevisionId());
                transactionApi.disableCouponOfTransactionForRevision(disableCouponOfTransactionForRevisionRequest);
            } else {
                // 有効化の場合
                EnableCouponOfTransactionForRevisionRequest enableCouponOfTransactionForRevisionRequest =
                                new EnableCouponOfTransactionForRevisionRequest();
                enableCouponOfTransactionForRevisionRequest.setTransactionRevisionId(
                                detailsUpdateCommonModel.getTransactionRevisionId());
                transactionApi.enableCouponOfTransactionForRevision(enableCouponOfTransactionForRevisionRequest);
            }
        } catch (HttpClientErrorException | HttpServerErrorException e) {
            LOGGER.error("例外処理が発生しました", e);
            // アイテム名調整マップ
            Map<String, String> itemNameAdjust = new HashMap<>();
            handleError(e.getStatusCode(), e.getResponseBodyAsString(), error, itemNameAdjust);

            if (error.hasErrors()) {
                return "order/details/detailsupdate";
            }
        }

        // 住所に変更があった場合に新規住所を登録して画面Modelに設定する
        setNewAddressIdIfChanged(detailsUpdateModel, error);
        if (error.hasErrors()) {
            // setNewAddressIdIfChangedで項目バリデーションエラーが発生した場合は、こちらでエラーハンドリングを実行
            return "order/details/detailsupdate";
        }

        // 入力内容を改訂用取引へ反映する
        RegistInputContentToTransactionForRevisionRequest registInputContentToTransactionForRevisionRequest =
                        detailsupdateHelper.toRegistInputContentToTransactionForRevisionRequest(detailsUpdateModel,
                                                                                                detailsUpdateCommonModel
                                                                                               );
        try {
            transactionApi.registInputContentToTransactionForRevision(
                            registInputContentToTransactionForRevisionRequest);
        } catch (HttpClientErrorException | HttpServerErrorException e) {
            LOGGER.error("例外処理が発生しました", e);
            // アイテム名調整マップ
            Map<String, String> itemNameAdjust = new HashMap<>();
            handleError(e.getStatusCode(), e.getResponseBodyAsString(), error, itemNameAdjust);

            if (error.hasErrors()) {
                return "order/details/detailsupdate";
            }
        }

        return "redirect:/order/detailsupdate/?md=confirm";
    }

    /**
     * 受注詳細修正確認画面へ遷移
     *
     * @return 確認画面
     */
    @PostMapping(value = "/", params = "doConfirm")
    @HEHandler(exception = AppLevelListException.class, returnView = "order/details/detailsupdate")
    public String doConfirm(@Validated(ConfirmGroup.class) DetailsUpdateModel detailsUpdateModel,
                            BindingResult error,
                            DetailsUpdateCommonModel detailsUpdateCommonModel,
                            RedirectAttributes redirectAttributes,
                            Model model) {

        // 実行前処理
        String check = preDoAction(detailsUpdateModel, detailsUpdateCommonModel, redirectAttributes, model);
        if (StringUtils.isNotEmpty(check)) {
            return check;
        }

        if (error.hasErrors()) {
            return "order/details/detailsupdate";
        }

        // 住所に変更があった場合に新規住所を登録して画面Modelに設定する
        setNewAddressIdIfChanged(detailsUpdateModel, error);
        if (error.hasErrors()) {
            // setNewAddressIdIfChangedで項目バリデーションエラーが発生した場合は、こちらでエラーハンドリングを実行
            return "order/details/detailsupdate";
        }

        // 請求決済エラーがOFFの場合のみ、通常の受注修正を実行
        if (!detailsUpdateModel.isEmergency()) {

            // 入力内容を改訂用取引へ反映する
            RegistInputContentToTransactionForRevisionRequest registInputContentToTransactionForRevisionRequest =
                            detailsupdateHelper.toRegistInputContentToTransactionForRevisionRequest(detailsUpdateModel,
                                                                                                    detailsUpdateCommonModel
                                                                                                   );
            try {
                transactionApi.registInputContentToTransactionForRevision(
                                registInputContentToTransactionForRevisionRequest);
            } catch (HttpClientErrorException | HttpServerErrorException e) {
                LOGGER.error("例外処理が発生しました", e);
                // アイテム名調整マップ
                Map<String, String> itemNameAdjust = new HashMap<>();
                handleError(e.getStatusCode(), e.getResponseBodyAsString(), error, itemNameAdjust);

                if (error.hasErrors()) {
                    return "order/details/detailsupdate";
                }
            }
        } else {

            // 請求決済エラーがONの場合、管理メモと改訂用請求伝票の決済代行連携解除更新のみ可能
            try {
                RegistInputContentToSuspendedTransactionForRevisionRequest
                                registInputContentToSuspendedTransactionForRevisionRequest =
                                detailsupdateHelper.toRegistInputContentToSuspendedTransactionForRevisionRequest(
                                                detailsUpdateModel, detailsUpdateCommonModel);
                transactionApi.registInputContentToSuspendedTransactionForRevision(
                                registInputContentToSuspendedTransactionForRevisionRequest);
            } catch (HttpClientErrorException | HttpServerErrorException e) {
                LOGGER.error("例外処理が発生しました", e);
                // アイテム名調整マップ
                Map<String, String> itemNameAdjust = new HashMap<>();
                handleError(e.getStatusCode(), e.getResponseBodyAsString(), error, itemNameAdjust);

                if (error.hasErrors()) {
                    return "order/details/detailsupdate";
                }
            }
        }

        // 確認ボタン押下時の共通チェック処理
        String resultTargetPage =
                        confirmCheck(detailsUpdateModel, error, detailsUpdateCommonModel, redirectAttributes, model);

        // 最新の改訂用販売伝票取得
        GetSalesSlipForRevisionByTransactionRevisionIdRequest salesSlipForRevisionGetRequest =
                        new GetSalesSlipForRevisionByTransactionRevisionIdRequest();
        salesSlipForRevisionGetRequest.setTransactionRevisionId(detailsUpdateCommonModel.getTransactionRevisionId());
        GetSalesSlipForRevisionByTransactionRevisionIdResponse salesSlipForRevisionResponse =
                        salesSlipApi.getSalesSlipForRevisionByTransactionRevisionId(salesSlipForRevisionGetRequest);

        detailsupdateHelper.updatePaymentInformation(detailsUpdateModel, salesSlipForRevisionResponse);

        if (StringUtils.isNotBlank(resultTargetPage)) {
            return resultTargetPage;
        }

        // 出荷済みの場合に、送料/手数料が変更になった場合はアラートダイアログを表示する
        if (HTypeOrderStatus.SHIPMENT_COMPLETION == detailsUpdateModel.getOrderStatus()) {

            // 修正前後の手数料/送料を設定
            int originCommission = StringUtils.isBlank(detailsUpdateModel.getOrgCommissionDisp()) ?
                            0 :
                            Integer.parseInt(detailsUpdateModel.getOrgCommissionDisp());
            int modifiedCommission = salesSlipForRevisionResponse.getCommission() == null ?
                            0 :
                            salesSlipForRevisionResponse.getCommission();
            int originalCarriage = StringUtils.isBlank(detailsUpdateModel.getOrgCarriageDisp()) ?
                            0 :
                            Integer.parseInt(detailsUpdateModel.getOrgCarriageDisp());
            int modifiedCarriage = salesSlipForRevisionResponse.getCarriage() == null ?
                            0 :
                            salesSlipForRevisionResponse.getCarriage();

            // 手数料が変わる場合
            if (originCommission != modifiedCommission) {
                // 自画面で手数料確認ダイアログを表示
                detailsUpdateModel.setCommissionDialogDisplay(true);
            }
            // 送料が変わる場合
            if (originalCarriage != modifiedCarriage) {
                // 送料ダイアログ表示
                detailsUpdateModel.setCarriageDialogDisplay(true);
            }
        }

        CouponResponse couponResponse = null;
        // クーポンが使用されている場合
        if ("true".equals(detailsUpdateModel.getUseCouponFlg())) {
            // クーポン情報を取得
            CouponVersionNoRequest couponVersionNoRequest = new CouponVersionNoRequest();
            couponVersionNoRequest.setCouponSeq(detailsUpdateModel.getCouponSeq());
            couponVersionNoRequest.setCouponVersionNo(detailsUpdateModel.getCouponVersionNo());

            couponResponse = couponApi.getByCouponVersionNo(couponVersionNoRequest);

            if (ObjectUtils.isEmpty(couponResponse)) {
                // 公開後のクーポン情報は削除不可だが、取得できない場合はエラー画面にリダイレクト
                addMessage(MSGCD_PARAM_ERROR, redirectAttributes, model);
                return "redirect:/error";
            }

            try {
                // クーポンダイアログ出力チェック
                this.checkCouponDialog(detailsUpdateCommonModel, detailsUpdateModel, couponResponse);
            } catch (HttpClientErrorException ce) {
                // 想定内のエラー（400番台）の場合
                // 妥当性チェック用のエラーハンドリングのため、このタイミングでは画面遷移をさせず、後続処理を実行する ※妥当性チェックでは料金計算の再取得も行われるため
                createClientErrorMessage(ce.getResponseBodyAsString(), error, redirectAttributes, model);

                if (isExistsErrCode(ce.getResponseBodyAsString(), EXCLUSIVE_CONTROL_ERR)) {
                    return "redirect:/order/details/?orderCode=" + detailsUpdateCommonModel.getOrderCode()
                           + "&from=order";
                }
            } catch (HttpServerErrorException se) {
                // 想定外のエラー（500番台）の場合
                createServerErrorMessage(se.getResponseBodyAsString(), redirectAttributes, model);
                return "redirect:/error";
            }

            // 対象商品がすべてキャンセルされている場合
            if (detailsUpdateModel.isCouponTargetGoodsCancelMessgeFlg()) {
                detailsupdateHelper.setCouponTargetGoodsForJs(detailsUpdateModel, couponResponse);
            }

        }

        if (detailsUpdateModel.isCommissionDialogDisplay() || detailsUpdateModel.isCarriageDialogDisplay()
            || detailsUpdateModel.isCouponCancelDialogDisplay()) {
            return "order/details/detailsupdate";
        }

        // Modelをクリア
        clearModel(DetailsUpdateModel.class, detailsUpdateModel, model);

        return "redirect:/order/detailsupdate/confirm";
    }

    /**
     * 手数料確認ダイアログからのリクエスト
     *
     * @return 受注詳細修正確認画面
     */
    @PostMapping(value = "/", params = "doConfirmCommissionDialog")
    @HEHandler(exception = AppLevelListException.class, returnView = "order/details/detailsupdate")
    public String doConfirmCommissionDialog(DetailsUpdateModel detailsUpdateModel,
                                            BindingResult error,
                                            DetailsUpdateCommonModel detailsUpdateCommonModel,
                                            RedirectAttributes redirectAttributes,
                                            Model model) {

        // 実行前処理
        String check = preDoAction(detailsUpdateModel, detailsUpdateCommonModel, redirectAttributes, model);
        if (StringUtils.isNotEmpty(check)) {
            return check;
        }

        // 送料/クーポンのダイアログ表示が必要な場合
        if (detailsUpdateModel.isCarriageDialogDisplay() || detailsUpdateModel.isCouponCancelDialogDisplay()) {
            detailsUpdateModel.setCommissionSelected(true);
            return "order/details/detailsupdate";
        }

        // 改訂用取引の改訂前手数料/送料の適用フラグ設定
        ApplyOriginCommissionAndCarriageForRevisionRequest applyOriginCommissionAndCarriageForRevisionRequest =
                        new ApplyOriginCommissionAndCarriageForRevisionRequest();
        applyOriginCommissionAndCarriageForRevisionRequest.setTransactionRevisionId(
                        detailsUpdateCommonModel.getTransactionRevisionId());
        applyOriginCommissionAndCarriageForRevisionRequest.setOriginCommissionApplyFlag(
                        detailsUpdateModel.isCommissionCalcFlag());
        applyOriginCommissionAndCarriageForRevisionRequest.setOriginCarriageApplyFlag(false);

        transactionApi.applyOriginCommissionAndCarriageForRevision(applyOriginCommissionAndCarriageForRevisionRequest);

        String resultTargetPage =
                        confirmCheck(detailsUpdateModel, error, detailsUpdateCommonModel, redirectAttributes, model);
        if (StringUtils.isNotBlank(resultTargetPage)) {
            return resultTargetPage;
        }

        // Modelをクリア
        clearModel(DetailsUpdateModel.class, detailsUpdateModel, model);

        return "redirect:/order/detailsupdate/confirm";
    }

    /**
     * 送料確認ダイアログからのリクエスト
     *
     * @return 受注詳細修正確認画面
     */
    @PostMapping(value = "/", params = "doConfirmCarriageDialog")
    @HEHandler(exception = AppLevelListException.class, returnView = "order/details/detailsupdate")
    public String doConfirmCarriageDialog(DetailsUpdateModel detailsUpdateModel,
                                          BindingResult error,
                                          DetailsUpdateCommonModel detailsUpdateCommonModel,
                                          RedirectAttributes redirectAttributes,
                                          Model model) {

        // 実行前処理
        String check = preDoAction(detailsUpdateModel, detailsUpdateCommonModel, redirectAttributes, model);
        if (StringUtils.isNotEmpty(check)) {
            return check;
        }
        // クーポンキャンセルダイアログ表示の場合
        if (detailsUpdateModel.isCouponCancelDialogDisplay()) {
            detailsUpdateModel.setCarriageSelected(true);
            return "order/details/detailsupdate";
        }

        // 改訂用取引の改訂前手数料/送料の適用フラグ設定
        ApplyOriginCommissionAndCarriageForRevisionRequest applyOriginCommissionAndCarriageForRevisionRequest =
                        new ApplyOriginCommissionAndCarriageForRevisionRequest();
        applyOriginCommissionAndCarriageForRevisionRequest.setTransactionRevisionId(
                        detailsUpdateCommonModel.getTransactionRevisionId());
        applyOriginCommissionAndCarriageForRevisionRequest.setOriginCommissionApplyFlag(
                        detailsUpdateModel.isCommissionCalcFlag());
        applyOriginCommissionAndCarriageForRevisionRequest.setOriginCarriageApplyFlag(
                        detailsUpdateModel.isCarriageCalcFlag());

        transactionApi.applyOriginCommissionAndCarriageForRevision(applyOriginCommissionAndCarriageForRevisionRequest);

        String resultTargetPage =
                        confirmCheck(detailsUpdateModel, error, detailsUpdateCommonModel, redirectAttributes, model);
        if (StringUtils.isNotBlank(resultTargetPage)) {
            return resultTargetPage;
        }

        // Modelをクリア
        clearModel(DetailsUpdateModel.class, detailsUpdateModel, model);

        return "redirect:/order/detailsupdate/confirm";
    }

    /**
     * クーポンキャンセルダイアログからのリクエスト
     *
     * @return 受注詳細修正確認画面
     */
    @PostMapping(value = "/", params = "doConfirmCouponDialog")
    @HEHandler(exception = AppLevelListException.class, returnView = "order/details/detailsupdate")
    public String doConfirmCouponDialog(DetailsUpdateModel detailsUpdateModel,
                                        BindingResult error,
                                        DetailsUpdateCommonModel detailsUpdateCommonModel,
                                        RedirectAttributes redirectAttributes,
                                        Model model) {

        // 実行前処理
        String check = preDoAction(detailsUpdateModel, detailsUpdateCommonModel, redirectAttributes, model);
        if (StringUtils.isNotEmpty(check)) {
            return check;
        }

        try {
            if (HTypeCouponLimitTargetType.OFF.getValue().equals(detailsUpdateModel.getCouponLimitTargetTypeValue())) {
                // 無効化の場合
                DisableCouponOfTransactionForRevisionRequest disableCouponOfTransactionForRevisionRequest =
                                new DisableCouponOfTransactionForRevisionRequest();
                disableCouponOfTransactionForRevisionRequest.setTransactionRevisionId(
                                detailsUpdateCommonModel.getTransactionRevisionId());
                transactionApi.disableCouponOfTransactionForRevision(disableCouponOfTransactionForRevisionRequest);
            } else {
                // 有効化の場合
                EnableCouponOfTransactionForRevisionRequest enableCouponOfTransactionForRevisionRequest =
                                new EnableCouponOfTransactionForRevisionRequest();
                enableCouponOfTransactionForRevisionRequest.setTransactionRevisionId(
                                detailsUpdateCommonModel.getTransactionRevisionId());
                transactionApi.enableCouponOfTransactionForRevision(enableCouponOfTransactionForRevisionRequest);
            }
        } catch (HttpClientErrorException | HttpServerErrorException e) {
            LOGGER.error("例外処理が発生しました", e);
            // アイテム名調整マップ
            Map<String, String> itemNameAdjust = new HashMap<>();
            handleError(e.getStatusCode(), e.getResponseBodyAsString(), error, itemNameAdjust);

            if (error.hasErrors()) {
                return "order/details/detailsupdate";
            }
        }

        String resultTargetPage =
                        confirmCheck(detailsUpdateModel, error, detailsUpdateCommonModel, redirectAttributes, model);
        if (StringUtils.isNotBlank(resultTargetPage)) {
            return resultTargetPage;
        }

        // Modelをクリア
        clearModel(DetailsUpdateModel.class, detailsUpdateModel, model);

        return "redirect:/order/detailsupdate/confirm";
    }

    /**
     * 受注キャンセル実行アクション
     *
     * @return 受注詳細ページ
     */
    @PostMapping(value = "/", params = "doOnceOrderCancel")
    @HEHandler(exception = AppLevelListException.class, returnView = "order/details/detailsupdate")
    public String doOnceOrderCancel(@Validated(OnceOrderCancelGroup.class) DetailsUpdateModel detailsUpdateModel,
                                    BindingResult error,
                                    DetailsUpdateCommonModel detailsUpdateCommonModel,
                                    RedirectAttributes redirectAttributes,
                                    SessionStatus sessionStatus,
                                    Model model) {

        // 実行前処理
        String check = preDoAction(detailsUpdateModel, detailsUpdateCommonModel, redirectAttributes, model);
        if (StringUtils.isNotEmpty(check)) {
            return check;
        }

        if (error.hasErrors()) {
            return "order/details/detailsupdate";
        }

        // 改訂用取引にキャンセルを反映
        CancelTransactionForRevisionRequest cancelTransactionForRevisionRequest =
                        new CancelTransactionForRevisionRequest();
        cancelTransactionForRevisionRequest.setTransactionId(detailsUpdateCommonModel.getTransactionId());
        cancelTransactionForRevisionRequest.setAdminMemo(detailsUpdateModel.getMemo());
        // クーポンが有効の場合(クーポンが表示されていて、割引されている)場合
        if (detailsUpdateModel.isDisplayCouponDiscount() && detailsUpdateModel.isDisplayCouponPriceDiscount()) {
            cancelTransactionForRevisionRequest.setCouponDisableFlag(HTypeCouponLimitTargetType.OFF.getValue()
                                                                                                   .equals(detailsUpdateModel.getCouponLimitTargetTypeValue()));
        }
        try {
            CancelTransactionForRevisionResponse cancelTransactionForRevisionResponse =
                            transactionApi.cancelTransactionForRevision(cancelTransactionForRevisionRequest);
            if (MapUtils.isNotEmpty(cancelTransactionForRevisionResponse.getWarningMessage())) {

                cancelTransactionForRevisionResponse.getWarningMessage().forEach((msgKey, value) -> {
                    if (msgKey.equals(WARNING_MESSAGE)) {
                        value.forEach(warningContent -> {
                            addWarnMessage(warningContent.getCode(), null, redirectAttributes, model);
                        });
                    }
                });
            }
        } catch (HttpClientErrorException ce) {
            LOGGER.error("例外処理が発生しました", ce);
            // 想定内のエラー（400番台）の場合
            createClientErrorMessage(ce.getResponseBodyAsString(), error, redirectAttributes, model);
            return "redirect:/order/details/?orderCode=" + detailsUpdateCommonModel.getOrderCode() + "&from=order";
        } catch (HttpServerErrorException se) {
            LOGGER.error("例外処理が発生しました", se);
            handleServerError(se.getResponseBodyAsString());
        }

        // Modelをセッションより破棄
        sessionStatus.setComplete();

        return "redirect:/order/details/?orderCode=" + detailsUpdateCommonModel.getOrderCode() + "&md=list";
    }

    /**
     * 住所に変更があった場合に新規住所を登録して画面Modelに設定する
     */
    private void setNewAddressIdIfChanged(DetailsUpdateModel detailsUpdateModel, BindingResult bindingResult) {

        // 住所IDに紐づくお届け先住所情報取得
        AddressBookAddressResponse shippingAddressResponse =
                        addressBookApi.getAddressById(detailsUpdateModel.getOrderReceiverItem().getAddressId());

        if (ObjectUtils.isEmpty(shippingAddressResponse)) {
            throwMessage(MSGCD_PARAM_ERROR);
        }

        // 比較先お届け先画面データ
        AddressBookAddressResponse shippingAddressResponseScreen =
                        detailsupdateHelper.toReceiverAddressResponse(detailsUpdateModel.getOrderReceiverItem());
        // アドレス名は比較しないので同じ値を設定
        shippingAddressResponseScreen.setAddressName(shippingAddressResponse.getAddressName());

        // 差分比較
        List<String> shippingAddressDiff = DiffUtil.diff(shippingAddressResponse, shippingAddressResponseScreen);

        // お届け先住所差分あり
        if (!CollectionUtils.isEmpty(shippingAddressDiff)) {

            // 非公開で住所登録する
            AddressBookAddressRegistRequest shippingAddressRegistRequest =
                            detailsupdateHelper.toBillingAddressRegistRequest(detailsUpdateModel.getOrderReceiverItem(),
                                                                              true
                                                                             );
            // 会員SEQをヘッダーに設定
            this.headerParamsHelper.setHeader(
                            this.addressBookApi.getApiClient(), detailsUpdateModel.getMemberInfoSeq().toString());
            // 住所登録
            AddressBookAddressRegistResponse shippingAddressRegistResponse = null;
            try {
                shippingAddressRegistResponse = addressBookApi.regist(shippingAddressRegistRequest);
            } catch (HttpClientErrorException | HttpServerErrorException e) {
                LOGGER.error("例外処理が発生しました", e);
                // アイテム名調整マップ
                Map<String, String> itemNameAdjust = new HashMap<>();
                handleError(e.getStatusCode(), e.getResponseBodyAsString(), bindingResult, itemNameAdjust);

                // 呼び出し元でエラーハンドリングする
                if (bindingResult.hasErrors()) {
                    return;
                }
            }
            if (ObjectUtils.isEmpty(shippingAddressRegistResponse)) {
                throwMessage(MSGCD_TMPREGIST_ADDRESS);
            }

            // お届け先住所IDを更新
            detailsUpdateModel.getOrderReceiverItem().setAddressId(shippingAddressRegistResponse.getAddressId());

            // 請求先住所とお届け先変更後住所が同じ場合、請求先IDはお届け先住所IDを流用する
            boolean isSame = true;
            if (!detailsUpdateModel.getOrderBillingLastName()
                                   .equals(detailsUpdateModel.getOrderReceiverItem().getReceiverLastName()))
                isSame = false;
            if (!detailsUpdateModel.getOrderBillingFirstName()
                                   .equals(detailsUpdateModel.getOrderReceiverItem().getReceiverFirstName()))
                isSame = false;
            if (!detailsUpdateModel.getOrderBillingLastKana()
                                   .equals(detailsUpdateModel.getOrderReceiverItem().getReceiverLastKana()))
                isSame = false;
            if (!detailsUpdateModel.getOrderBillingFirstKana()
                                   .equals(detailsUpdateModel.getOrderReceiverItem().getReceiverFirstKana()))
                isSame = false;
            if (!detailsUpdateModel.getOrderBillingZipCode()
                                   .equals(detailsUpdateModel.getOrderReceiverItem().getReceiverZipCode()))
                isSame = false;
            if (!detailsUpdateModel.getOrderBillingPrefecture()
                                   .equals(detailsUpdateModel.getOrderReceiverItem().getReceiverPrefecture()))
                isSame = false;
            if (!detailsUpdateModel.getOrderBillingAddress1()
                                   .equals(detailsUpdateModel.getOrderReceiverItem().getReceiverAddress1()))
                isSame = false;
            if (!detailsUpdateModel.getOrderBillingAddress2()
                                   .equals(detailsUpdateModel.getOrderReceiverItem().getReceiverAddress2()))
                isSame = false;
            if (!Objects.equals(detailsUpdateModel.getOrderBillingAddress3(),
                                detailsUpdateModel.getOrderReceiverItem().getReceiverAddress3()
                               ))
                isSame = false;
            if (!detailsUpdateModel.getOrderBillingTel()
                                   .equals(detailsUpdateModel.getOrderReceiverItem().getReceiverTel()))
                isSame = false;
            if (isSame) {
                detailsUpdateModel.setOrderBillingAddressId(shippingAddressRegistResponse.getAddressId());
                return;
            }
        }

        // 住所IDに紐づく請求先住所情報取得
        AddressBookAddressResponse billingAddressResponse =
                        addressBookApi.getAddressById(detailsUpdateModel.getOrderBillingAddressId());

        if (ObjectUtils.isEmpty(billingAddressResponse)) {
            throwMessage(MSGCD_PARAM_ERROR);
        }

        // 比較先画面データ
        AddressBookAddressResponse billingAddressResponseScreen =
                        detailsupdateHelper.toBillingAddressResponse(detailsUpdateModel);
        // アドレス名は比較しないので同じ値を設定
        billingAddressResponseScreen.setAddressName(billingAddressResponse.getAddressName());

        // 差分比較
        List<String> billingAddressDiff = DiffUtil.diff(billingAddressResponse, billingAddressResponseScreen);

        // 請求先住所差分あり
        if (!CollectionUtils.isEmpty(billingAddressDiff)) {

            // 非公開で住所登録
            AddressBookAddressRegistRequest billingAddressRegistRequest =
                            detailsupdateHelper.toBillingAddressRegistRequest(detailsUpdateModel, true);
            // 会員SEQをヘッダーに設定
            this.headerParamsHelper.setHeader(
                            this.addressBookApi.getApiClient(), detailsUpdateModel.getMemberInfoSeq().toString());
            // 住所登録
            AddressBookAddressRegistResponse billingAddressRegistResponse = null;
            try {
                billingAddressRegistResponse = addressBookApi.regist(billingAddressRegistRequest);
            } catch (HttpClientErrorException | HttpServerErrorException e) {
                LOGGER.error("例外処理が発生しました", e);
                // アイテム名調整マップ
                Map<String, String> itemNameAdjust = new HashMap<>();
                handleError(e.getStatusCode(), e.getResponseBodyAsString(), bindingResult, itemNameAdjust);

                // 呼び出し元でエラーハンドリングする
                if (bindingResult.hasErrors()) {
                    return;
                }
            }
            if (ObjectUtils.isEmpty(billingAddressRegistResponse)) {
                throwMessage(MSGCD_TMPREGIST_ADDRESS);
            } else {
                // 請求先先住所IDを更新
                detailsUpdateModel.setOrderBillingAddressId(billingAddressRegistResponse.getAddressId());
            }
        }
    }

    /**
     * クーポンダイアログ出力チェック
     *
     * @param detailsUpdateCommonModel 受注詳細修正画面共通Model
     * @param detailsUpdateModel 受注詳細修正画面Model
     * @param couponResponse クーポンResponse
     */
    private void checkCouponDialog(DetailsUpdateCommonModel detailsUpdateCommonModel,
                                   DetailsUpdateModel detailsUpdateModel,
                                   CouponResponse couponResponse) {
        // 受注改訂チェック
        CheckTransactionForRevisionRequest req = new CheckTransactionForRevisionRequest();
        req.setTransactionRevisionId(detailsUpdateCommonModel.getTransactionRevisionId());
        req.setContractConfirmFlag(true); // すでに1度計算・画面反映しているので再計算させない

        CheckTransactionForRevisionResponse checkTransactionResponse = transactionApi.checkTransactionForRevision(req);

        // クーポンダイアログ表示確認
        if (checkTransactionResponse != null && MapUtils.isNotEmpty(checkTransactionResponse.getWarningMessage())
            && checkTransactionResponse.getWarningMessage().containsKey(WARNING_MESSAGE)) {

            // 警告メッセージIDにて判定を行う
            List<WarningContent> warningList = checkTransactionResponse.getWarningMessage().get(WARNING_MESSAGE);
            for (WarningContent content : warningList) {
                if (TARGET_GOODS_CHECK_MSGCD.equals(content.getCode())) {
                    // クーポンダイアログ表示
                    detailsUpdateModel.setCouponCancelDialogDisplay(true);
                    // クーポン対象商品がなくなった場合
                    detailsUpdateModel.setCouponTargetGoodsCancelMessgeFlg(true);
                    break;
                } else if (COUPON_LOWER_PRICE_CHECK_MSGCD.equals(content.getCode())) {
                    // クーポンダイアログ表示
                    detailsUpdateModel.setCouponCancelDialogDisplay(true);
                    // クーポン適用金額に割引対象金額が満たなかった場合
                    detailsupdateHelper.setGoodsPriceTotalDisp(detailsUpdateModel, couponResponse);
                    detailsUpdateModel.setCouponDiscountLowerOrderPriceMessgeFlg(true);
                    break;
                }
            }
        }
    }

    /**
     * 確認ボタン押下時の共通チェック処理
     *
     * @return 遷移先画面 ※空文字の場合は遷移先無し
     */
    private String confirmCheck(DetailsUpdateModel detailsUpdateModel,
                                BindingResult error,
                                DetailsUpdateCommonModel detailsUpdateCommonModel,
                                RedirectAttributes redirectAttributes,
                                Model model) {

        // お届け希望日チェック
        checkReceiverDate(detailsUpdateModel);

        // 受注修正チェック
        CheckTransactionForRevisionRequest checkTransactionForRevisionRequest =
                        new CheckTransactionForRevisionRequest();
        checkTransactionForRevisionRequest.setTransactionRevisionId(
                        detailsUpdateCommonModel.getTransactionRevisionId());
        try {
            CheckTransactionForRevisionResponse transactionCheckResponse =
                            transactionApi.checkTransactionForRevision(checkTransactionForRevisionRequest);
            if (MapUtils.isNotEmpty(transactionCheckResponse.getWarningMessage())) {

                transactionCheckResponse.getWarningMessage().forEach((msgKey, value) -> {
                    if (msgKey.equals(WARNING_MESSAGE)) {
                        value.forEach(warningContent -> {
                            // 確認画面だけメッセージ出している
                            if (WCD_SELECTED_DELIVERY_DATE.equals(warningContent.getCode())) {
                                addWarnMessage(warningContent.getCode(), null, redirectAttributes, model);
                            }
                        });
                    }
                });
            }
        } catch (HttpClientErrorException ce) {
            LOGGER.error("例外処理が発生しました", ce);
            // 想定内のエラー（400番台）の場合
            createClientErrorMessage(ce.getResponseBodyAsString(), error, redirectAttributes, model);

            if (isExistsErrCode(ce.getResponseBodyAsString(), EXCLUSIVE_CONTROL_ERR)) {
                return "redirect:/order/details/?orderCode=" + detailsUpdateCommonModel.getOrderCode() + "&from=order";
            }
            return "order/details/detailsupdate";
        } catch (HttpServerErrorException se) {
            LOGGER.error("例外処理が発生しました", se);
            // 想定外のエラー（500番台）の場合
            createServerErrorMessage(se.getResponseBodyAsString(), redirectAttributes, model);
            return "redirect:/error";
        }

        return "";
    }

    /**
     * お届け希望日チェック<br/>
     */
    private void checkReceiverDate(DetailsUpdateModel detailsUpdateModel) {

        // TODO バックエンドチェックへの移行
        // 変換Helper取得
        ConversionUtility conversionUtility = ApplicationContextUtility.getBean(ConversionUtility.class);
        DateUtility dateUtility = ApplicationContextUtility.getBean(DateUtility.class);
        // 注文日時を取得
        Timestamp orderDate = detailsUpdateModel.getOrderTime();
        // 入力されたお届け希望日を取得
        String orderHms = dateUtility.format(orderDate, DateUtility.HMS);

        if (!StringUtil.isEmpty(detailsUpdateModel.getOrderReceiverItem().getUpdateReceiverDate())) {
            Timestamp inputDate = conversionUtility.toTimeStamp(
                            detailsUpdateModel.getOrderReceiverItem().getUpdateReceiverDate(), orderHms);
            // 注文日より過去の日付を入力された場合エラー
            if (inputDate.compareTo(orderDate) < 0) {
                throwMessage("AOX001020");
            }
        }
    }

    /**
     * 受注詳細修正画面へ反映
     */
    private void toPage(DetailsUpdateModel detailsUpdateModel,
                        OrderDetailsRevisionResponseModel orderDetailsRevisionResponseModel,
                        RedirectAttributes redirectAttrs,
                        Model model) {

        // 受注詳細情報ページ反映
        if (orderDetailsRevisionResponseModel != null) {
            detailsupdateHelper.toPage(detailsUpdateModel, orderDetailsRevisionResponseModel);
        }

        // 受注キャンセル、確認ボタンの非活性にするか判定
        detailsUpdateModel.setOrderCancelModifyPossible(true);

        // お客様情報表示欄
        detailsUpdateModel.setDisplayMemberInfo(true);

        // TODO チェック処理の暫定対応としてOrderMessageDtoを利用
        OrderMessageDto orderMessageDto = detailsUpdateModel.getOrderMessageDto();

        // 配送方法エラーチェック
        boolean deliveryerror = false;
        // TODO チェック処理の暫定対応としてOrderMessageDtoを利用
        List<CheckMessageDto> cmDtoList = orderMessageDto.getOrderMessageList();
        if (cmDtoList != null && !cmDtoList.isEmpty()) {
            for (CheckMessageDto cmDto : cmDtoList) {
                // 配送方法エラーが存在するかチェック
                if (MSGCD_SELECT_DELIVERYMETHOD_ZERO.equals(cmDto.getMessageId()) || MSGCD_PREFECTURE_ERROR.equals(
                                cmDto.getMessageId())) {
                    deliveryerror = true;
                }
            }
        }

        if (!deliveryerror) {
            try {
                // TODO ボトムアップAPIが不足 deliveryMethodListGetService.execute(modified); ➡deliveryMethodSelectListGetLogic.execute(conditionDto, null, freeDeliveryFlag, available);
                ShippingMethodListResponse shippingMethodListResponse = shippingMethodApi.get();

                if (shippingMethodListResponse != null) {
                    // 配送方法リストセット
                    detailsupdateHelper.setDeliveryList(
                                    detailsUpdateModel, shippingMethodListResponse.getShippingMethodListResponse());

                    if (!CollectionUtils.isEmpty(shippingMethodListResponse.getShippingMethodListResponse())) {
                        for (ShippingMethodResponse shippingMethodResponse : shippingMethodListResponse.getShippingMethodListResponse()) {

                            if (!ObjectUtils.isEmpty(shippingMethodResponse.getDeliveryMethodResponse())
                                && shippingMethodResponse.getDeliveryMethodResponse().getDeliveryMethodSeq() != null
                                && shippingMethodResponse.getDeliveryMethodResponse()
                                                         .getDeliveryMethodSeq()
                                                         .toString()
                                                         .equals(detailsUpdateModel.getOrderReceiverItem()
                                                                                   .getUpdateDeliveryMethodSeq())) {
                                // お届け希望時間帯リスト設定
                                detailsupdateHelper.setTimeZoneItem(
                                                detailsUpdateModel, shippingMethodResponse.getDeliveryMethodResponse());
                                break;
                            }
                        }
                    }
                }
            } catch (EmptyRuntimeException e) {
                LOGGER.error("例外処理が発生しました", e);
                // 処理なし⇒リスト空もしくは元の配送方法リストを利用
            }
        } else {
            detailsupdateHelper.setDeliveryList(detailsUpdateModel, new ArrayList<>());
        }

        // エラーメッセージを表示
        // TODO チェック処理の暫定対応としてOrderMessageDtoを利用
        if (cmDtoList != null && !cmDtoList.isEmpty()) {
            for (CheckMessageDto cmDto : cmDtoList) {
                this.addMessage(cmDto.getMessageId(), cmDto.getArgs(), redirectAttrs, model);
            }
        }
        // 商品系エラーの確認
        // TODO チェック処理の暫定対応としてOrderMessageDtoを利用
        if (orderMessageDto.hasGoodsErrorMessage()) {
            this.addMessage("AOX001001", redirectAttrs, model);
        }
        // フロントエンドチェック：商品点数チェック（合計商品が０の場合エラー）
        if (BigDecimal.ZERO.equals(detailsUpdateModel.getOrderGoodsCountTotal())) {
            this.addMessage("AOX001009", redirectAttrs, model);
        }
    }

    /**
     * 郵便番号検索ボタンのIDに連番を付ける<br/>
     * ※連番は通常ボタンとAjax用ボタンの切り替えに使用
     *
     * @return 自画面
     */
    @PostMapping(value = "/", params = "doZipCodeSearchToReceiverAjax")
    public String doZipCodeSearchToReceiverAjax(DetailsUpdateModel detailsUpdateModel,
                                                DetailsUpdateCommonModel detailsUpdateCommonModel,
                                                RedirectAttributes redirectAttributes,
                                                Model model) {
        // 実行前処理
        String check = preDoAction(detailsUpdateModel, detailsUpdateCommonModel, redirectAttributes, model);
        if (StringUtils.isNotEmpty(check)) {
            return check;
        }
        return "order/details/detailsupdate";
    }

    /**
     * アクション実行前処理
     *
     * @return
     */
    private String preDoAction(DetailsUpdateModel detailsUpdateModel,
                               DetailsUpdateCommonModel detailsUpdateCommonModel,
                               RedirectAttributes redirectAttributes,
                               Model model) {
        // 不正操作チェック
        return illegalOperationCheck(false, detailsUpdateModel, detailsUpdateCommonModel, redirectAttributes, model);
    }

    /**
     * 不正操作チェック
     *
     * @param isLoadAction ロードアクションフラグ
     */
    private String illegalOperationCheck(boolean isLoadAction,
                                         DetailsUpdateModel detailsUpdateModel,
                                         DetailsUpdateCommonModel detailsUpdateCommonModel,
                                         RedirectAttributes redirectAttributes,
                                         Model model) {

        String orderCode = detailsUpdateCommonModel.getOrderCode();
        if (orderCode == null) {
            addMessage(MSGCD_PARAM_ERROR, redirectAttributes, model);
            return "redirect:/error";
        }

        if (!isLoadAction) {
            if (StringUtils.isBlank(detailsUpdateCommonModel.getTransactionRevisionId())) {
                redirectAttributes.addFlashAttribute(FLASH_MD, MODE_LIST);
                addMessage(MSGCD_PARAM_ERROR, redirectAttributes, model);
                return "redirect:/order/";
            }
        }
        return "";
    }

    /**
     * 商品選択チェック<br/>
     *
     * @return true:選択あり false:選択なし
     */
    private boolean isGoodsSelect(DetailsUpdateModel detailsUpdateModel) {

        List<OrderGoodsUpdateItem> goodsItems = detailsUpdateModel.getOrderReceiverItem().getOrderGoodsUpdateItems();
        for (Iterator<OrderGoodsUpdateItem> goodsIte = goodsItems.iterator(); goodsIte.hasNext(); ) {
            OrderGoodsUpdateItem goodsItem = goodsIte.next();
            if (goodsItem.isGoodsCheck()) {
                return true;
            }
        }
        return false;
    }

    /**
     * コンポーネント値初期化
     *
     * @param detailsUpdateModel
     */
    private void initComponentValue(DetailsUpdateModel detailsUpdateModel) {
        detailsUpdateModel.setUpdateInvoiceAttachmentFlagItems(
                        EnumTypeUtil.getEnumMap(HTypeInvoiceAttachmentFlag.class));
        detailsUpdateModel.setOrderBillingPrefectureItems(EnumTypeUtil.getEnumMap(HTypePrefectureType.class));
        detailsUpdateModel.setReceiverPrefectureItems(EnumTypeUtil.getEnumMap(HTypePrefectureType.class));
        detailsUpdateModel.setNoveltyPresentJudgmentStatusItems(
                        EnumTypeUtil.getEnumMap(HTypeNoveltyPresentJudgmentStatus.class));
    }

}
