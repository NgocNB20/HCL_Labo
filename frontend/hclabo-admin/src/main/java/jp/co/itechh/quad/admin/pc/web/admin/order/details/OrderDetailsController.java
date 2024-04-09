/*
 * Project Name : HIT-MALL3
 *
 * Copyright (C) 2008 i-TEC HANKYU HANSHIN INC. All Rights Reserved.
 *
 */

package jp.co.itechh.quad.admin.pc.web.admin.order.details;

import jp.co.itechh.quad.addressbook.presentation.api.AddressBookApi;
import jp.co.itechh.quad.addressbook.presentation.api.param.AddressBookAddressResponse;
import jp.co.itechh.quad.admin.annotation.exception.HEHandler;
import jp.co.itechh.quad.admin.base.exception.AppLevelListException;
import jp.co.itechh.quad.admin.base.util.common.PropertiesUtil;
import jp.co.itechh.quad.admin.base.utility.ApplicationContextUtility;
import jp.co.itechh.quad.admin.base.utility.ConversionUtility;
import jp.co.itechh.quad.admin.constant.type.HTypeMailTemplateType;
import jp.co.itechh.quad.admin.constant.type.HTypePaymentStatus;
import jp.co.itechh.quad.admin.constant.type.HTypeSettlementMethodType;
import jp.co.itechh.quad.admin.dto.common.CheckMessageDto;
import jp.co.itechh.quad.admin.dto.goods.goods.GoodsDetailsDto;
import jp.co.itechh.quad.admin.dto.mail.MailDto;
import jp.co.itechh.quad.admin.dto.mail.MailSendDto;
import jp.co.itechh.quad.admin.service.download.impl.DownloadFileStreamingResponseBody;
import jp.co.itechh.quad.admin.util.common.EnumTypeUtil;
import jp.co.itechh.quad.bill.presentation.api.BillApi;
import jp.co.itechh.quad.billingslip.presentation.api.BillingSlipApi;
import jp.co.itechh.quad.billingslip.presentation.api.param.BillingSlipGetRequest;
import jp.co.itechh.quad.billingslip.presentation.api.param.BillingSlipResponse;
import jp.co.itechh.quad.convenience.presentation.api.ConvenienceApi;
import jp.co.itechh.quad.convenience.presentation.api.param.ConvenienceListResponse;
import jp.co.itechh.quad.coupon.presentation.api.CouponApi;
import jp.co.itechh.quad.coupon.presentation.api.param.CouponResponse;
import jp.co.itechh.quad.coupon.presentation.api.param.CouponVersionNoRequest;
import jp.co.itechh.quad.customer.presentation.api.CustomerApi;
import jp.co.itechh.quad.customer.presentation.api.param.CustomerResponse;
import jp.co.itechh.quad.examination.presentation.api.ExaminationApi;
import jp.co.itechh.quad.examination.presentation.api.param.ConfigInfoResponse;
import jp.co.itechh.quad.examination.presentation.api.param.ExamKitListResponse;
import jp.co.itechh.quad.method.presentation.api.SettlementMethodApi;
import jp.co.itechh.quad.method.presentation.api.param.PaymentMethodResponse;
import jp.co.itechh.quad.mulpay.presentation.api.MulpayApi;
import jp.co.itechh.quad.mulpay.presentation.api.param.MulPayBillRequest;
import jp.co.itechh.quad.mulpay.presentation.api.param.MulPayBillResponse;
import jp.co.itechh.quad.orderreceived.presentation.api.OrderReceivedApi;
import jp.co.itechh.quad.orderreceived.presentation.api.param.GetOrderReceivedCountRequest;
import jp.co.itechh.quad.orderreceived.presentation.api.param.OrderReceivedCountResponse;
import jp.co.itechh.quad.orderreceived.presentation.api.param.OrderReceivedResponse;
import jp.co.itechh.quad.orderslip.presentation.api.OrderSlipApi;
import jp.co.itechh.quad.orderslip.presentation.api.param.OrderSlipGetRequest;
import jp.co.itechh.quad.orderslip.presentation.api.param.OrderSlipResponse;
import jp.co.itechh.quad.product.presentation.api.ProductApi;
import jp.co.itechh.quad.product.presentation.api.param.GoodsDetailsResponse;
import jp.co.itechh.quad.product.presentation.api.param.ProductDetailListGetRequest;
import jp.co.itechh.quad.product.presentation.api.param.ProductDetailListResponse;
import jp.co.itechh.quad.salesslip.presentation.api.SalesSlipApi;
import jp.co.itechh.quad.salesslip.presentation.api.param.SalesSlipGetRequest;
import jp.co.itechh.quad.salesslip.presentation.api.param.SalesSlipResponse;
import jp.co.itechh.quad.shippingslip.presentation.api.ShippingSlipApi;
import jp.co.itechh.quad.shippingslip.presentation.api.param.ShippingSlipGetRequest;
import jp.co.itechh.quad.shippingslip.presentation.api.param.ShippingSlipResponse;
import jp.co.itechh.quad.transaction.presentation.api.TransactionApi;
import jp.co.itechh.quad.transaction.presentation.api.param.CanceledTransactionUpdateRequest;
import jp.co.itechh.quad.transaction.presentation.api.param.TransactionReAuthRequest;
import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.util.CollectionUtils;
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

import java.io.File;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Optional;

import static jp.co.itechh.quad.admin.pc.web.admin.order.details.AbstractOrderDetailsModel.CANCEL;

/**
 * 受注詳細コントローラー
 *
 * @author kimura
 */
@RequestMapping("/order/details")
@Controller
@SessionAttributes(value = "orderDetailsModel")
@PreAuthorize("hasAnyAuthority('ORDER:4')")
public class OrderDetailsController extends AbstractOrderDetailsController {

    /** ロガー */
    private static final Logger LOGGER = LoggerFactory.getLogger(OrderDetailsController.class);

    /** 不正操作 */
    public static final String MSGCD_REFERER_FAIL = "AOX000901";

    /** 再オーソリ成功 */
    public static final String MSGCD_REAUTHORY_SUCCESS = "AOX000903";

    /** キャンセル受注更新モードメッセージ */
    public static final String MSGCD_UPDATE_CANCELED_ORDER = "AOX000905";

    /** キャンセル受注更新成功メッセージ */
    public static final String MSGCD_UPDATE_CANCELED_ORDER_SUCCESS = "AOX000906";

    /** リンク決済(後日払い)は手動返金してください。 */
    public static final String MSGCD_LINKPAY_CANCEL_PAYLATER = "AOX000907";

    /** リンク決済(即時払い)のキャンセル期限が切れています。手動返金してください */
    public static final String MSGCD_LINKPAY_CANCEL_PASS_GMO_CANCEL_DEADLINE = "AOX000908";

    /** リンク決済(即時払い)は GMOショップ管理サイトより返金してください。 */
    public static final String MSGCD_LINKPAY_CANCEL_IN_GMO_CANCEL_DEADLINE = "AOX000909";

    /** 同一商品、税率混在メッセージ */
    public static final String MSGCD_MULTIPLE_TAX_RATE_IN_SINGLE_GOODS = "PKG-4081-001-A-";

    /** 顧客API */
    private final CustomerApi customerApi;

    /** 商品API */
    private final ProductApi productApi;

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

    /** マルペイAPI */
    private final MulpayApi mulpayApi;

    /** クーポンAPI */
    private final CouponApi couponApi;

    /** 請求Api */
    private final BillApi billApi;

    /** コンビニApi */
    private final ConvenienceApi convenienceApi;

    /** 検査API */
    private final ExaminationApi examinationApi;

    /** helper */
    private final OrderDetailsHelper orderDetailsHelper;

    /** 変換Utility */
    private final ConversionUtility conversionUtility;

    /** コンストラクタ */
    @Autowired
    public OrderDetailsController(CustomerApi customerApi,
                                  ProductApi productApi,
                                  OrderReceivedApi orderReceivedApi,
                                  TransactionApi transactionApi,
                                  ShippingSlipApi shippingSlipApi,
                                  BillingSlipApi billingSlipApi,
                                  SalesSlipApi salesSlipApi,
                                  OrderSlipApi orderSlipApi,
                                  AddressBookApi addressBookApi,
                                  SettlementMethodApi settlementMethodApi,
                                  MulpayApi mulpayApi,
                                  CouponApi couponApi,
                                  BillApi billApi,
                                  ConvenienceApi convenienceApi,
                                  ExaminationApi examinationApi, OrderDetailsHelper orderDetailsHelper,
                                  ConversionUtility conversionUtility) {
        this.customerApi = customerApi;
        this.productApi = productApi;
        this.orderReceivedApi = orderReceivedApi;
        this.transactionApi = transactionApi;
        this.shippingSlipApi = shippingSlipApi;
        this.billingSlipApi = billingSlipApi;
        this.salesSlipApi = salesSlipApi;
        this.orderSlipApi = orderSlipApi;
        this.addressBookApi = addressBookApi;
        this.settlementMethodApi = settlementMethodApi;
        this.mulpayApi = mulpayApi;
        this.couponApi = couponApi;
        this.billApi = billApi;
        this.convenienceApi = convenienceApi;
        this.examinationApi = examinationApi;
        this.orderDetailsHelper = orderDetailsHelper;
        this.conversionUtility = conversionUtility;
    }

    /**
     * 入力画面：初期処理
     *
     * @param orderCode
     * @param from
     * @param md
     * @param orderDetailsModel
     * @param error
     * @param redirectAttributes
     * @param model
     * @return
     */
    @GetMapping(value = "")
    @HEHandler(exception = AppLevelListException.class, returnView = "order/details/details")
    protected String doLoadIndex(@RequestParam(required = false) Optional<String> orderCode,
                                 @RequestParam(required = false) Optional<String> from,
                                 @RequestParam(required = false) Optional<String> md,
                                 OrderDetailsModel orderDetailsModel,
                                 BindingResult error,
                                 RedirectAttributes redirectAttributes,
                                 Model model) {

        String orderCodeData = null;
        clearModel(OrderDetailsModel.class, orderDetailsModel, new String[] {"orderCode", "from", "md"}, model);

        if (orderCode.isPresent()) {
            orderCodeData = orderCode.get();
        } else if (model.containsAttribute(DetailsUpdateModel.FLASH_ORDERCODE)) {
            orderCodeData = (String) model.getAttribute(DetailsUpdateModel.FLASH_ORDERCODE);
        }

        if (orderCodeData == null) {
            addMessage(MSGCD_REFERER_FAIL, redirectAttributes, model);
            return "redirect:/error";
        }

        if (from.isPresent()) {
            orderDetailsModel.setFrom(from.get());
            orderDetailsModel.setMd("list");
        }

        OrderDetailsCommonModel orderDetailsCommonModel = new OrderDetailsCommonModel();

        // 各種伝票情報と注文時の情報を取得
        try {
            // 受注番号から取引IDを取得
            OrderReceivedResponse orderReceivedResponse = this.orderReceivedApi.getByOrderCode(orderCodeData);

            if (orderReceivedResponse == null) {
                addMessage(MSGCD_REFERER_FAIL, redirectAttributes, model);
                return "redirect:/error";
            }

            ShippingSlipGetRequest shippingSlipGetRequest = new ShippingSlipGetRequest();
            shippingSlipGetRequest.setTransactionId(orderReceivedResponse.getLatestTransactionId());
            ShippingSlipResponse shippingSlipResponse = this.shippingSlipApi.get(shippingSlipGetRequest);

            BillingSlipGetRequest billingSlipGetRequest = new BillingSlipGetRequest();
            billingSlipGetRequest.setTransactionId(orderReceivedResponse.getLatestTransactionId());
            BillingSlipResponse billingSlipResponse = this.billingSlipApi.get(billingSlipGetRequest);

            SalesSlipGetRequest salesSlipGetRequest = new SalesSlipGetRequest();
            salesSlipGetRequest.setTransactionId(orderReceivedResponse.getLatestTransactionId());
            SalesSlipResponse salesSlipResponse = this.salesSlipApi.get(salesSlipGetRequest);

            OrderSlipGetRequest orderSlipSlipGetRequest = new OrderSlipGetRequest();
            orderSlipSlipGetRequest.setTransactionId(orderReceivedResponse.getLatestTransactionId());
            OrderSlipResponse orderSlipResponse = this.orderSlipApi.get(orderSlipSlipGetRequest);

            ExamKitListResponse examKitListResponse = examinationApi.getExamKitList(
                    orderDetailsHelper.toExamKitRequest(orderSlipResponse.getItemList()));

            ConfigInfoResponse configInfoResponse = examinationApi.getConfigInfo();

            CustomerResponse customerResponse = this.customerApi.getByMemberinfoSeq(
                            this.conversionUtility.toInteger(orderReceivedResponse.getCustomerId()));

            // 伝票情報、顧客情報が存在しない場合はエラー
            if (shippingSlipResponse == null || billingSlipResponse == null || salesSlipResponse == null
                || orderSlipResponse == null || customerResponse == null) {
                addMessage(MSGCD_REFERER_FAIL, redirectAttributes, model);
                return "redirect:/error";
            }

            // 顧客受注件数
            GetOrderReceivedCountRequest getOrderReceivedCountRequest = new GetOrderReceivedCountRequest();
            getOrderReceivedCountRequest.setCustomerId(
                            this.conversionUtility.toString(customerResponse.getMemberInfoSeq()));
            OrderReceivedCountResponse orderReceivedCountResponse =
                            this.orderReceivedApi.getOrderReceivedCount(getOrderReceivedCountRequest);

            // 顧客住所情報の取得
            AddressBookAddressResponse customerAddressResponse =
                            this.addressBookApi.getAddressById(customerResponse.getMemberInfoAddressId());
            // お届け先住所情報の取得
            AddressBookAddressResponse shippingAddressResponse =
                            this.addressBookApi.getAddressById(shippingSlipResponse.getShippingAddressId());
            // 請求先住所情報の取得
            AddressBookAddressResponse billingAddressResponse =
                            this.addressBookApi.getAddressById(billingSlipResponse.getBillingAddressId());

            // 決済方法の取得
            PaymentMethodResponse paymentMethodResponse = this.settlementMethodApi.getBySettlementMethodSeq(
                            this.conversionUtility.toInteger(billingSlipResponse.getPaymentMethodId()));

            if (orderReceivedCountResponse == null || customerAddressResponse == null || shippingAddressResponse == null
                || billingAddressResponse == null || paymentMethodResponse == null) {
                // 顧客受注件数、顧客住所情報の取得、配送先住所情報、請求先住所情報、決済方法、配送方法が取得できない場合
                addMessage(MSGCD_REFERER_FAIL, redirectAttributes, model);
                return "redirect:/error";
            }

            // マルチペイメント情報を取得
            MulPayBillRequest mulPayBillRequest = new MulPayBillRequest();
            mulPayBillRequest.setOrderCode(orderCodeData);
            MulPayBillResponse mulPayBillResponse = this.mulpayApi.getByOrderCode(mulPayBillRequest);

            ConvenienceListResponse convenienceListResponse = null;
            if (EnumTypeUtil.getEnumFromValue(
                            HTypeSettlementMethodType.class, paymentMethodResponse.getSettlementMethodType())
                == HTypeSettlementMethodType.LINK_PAYMENT && billingSlipResponse.getPaymentLinkResponse() != null) {
                if ("3".equals(billingSlipResponse.getPaymentLinkResponse().getPayType())) {
                    // コンビニ一覧
                    convenienceListResponse = convenienceApi.get();
                }
            }

            // クーポンが適用されている場合
            CouponResponse couponResponse = null;
            if (salesSlipResponse.getCouponSeq() != null) {
                // クーポン情報を取得
                CouponVersionNoRequest couponVersionNoRequest = new CouponVersionNoRequest();
                couponVersionNoRequest.setCouponSeq(salesSlipResponse.getCouponSeq());
                couponVersionNoRequest.setCouponVersionNo(salesSlipResponse.getCouponVersionNo());

                couponResponse = couponApi.getByCouponVersionNo(couponVersionNoRequest);
            }

            // 商品サービスから商品詳細リストを取得
            List<GoodsDetailsDto> goodsDtoList = getGoodsDetailsDtoList(orderSlipResponse);

            if (CollectionUtils.isEmpty(goodsDtoList)) {
                // 商品情報が存在しない場合
                addMessage(MSGCD_REFERER_FAIL, redirectAttributes, model);
                return "redirect:/error";
            }

            // 共通モデルに各レスポンス結果を格納し、セッションに保存
            orderDetailsCommonModel.setOrderReceivedResponse(orderReceivedResponse);
            orderDetailsCommonModel.setShippingSlipResponse(shippingSlipResponse);
            orderDetailsCommonModel.setBillingSlipResponse(billingSlipResponse);
            orderDetailsCommonModel.setSalesSlipResponse(salesSlipResponse);
            orderDetailsCommonModel.setOrderSlipResponse(orderSlipResponse);
            orderDetailsCommonModel.setCustomerResponse(customerResponse);
            orderDetailsCommonModel.setOrderReceivedCountResponse(orderReceivedCountResponse);
            orderDetailsCommonModel.setCustomerAddressResponse(customerAddressResponse);
            orderDetailsCommonModel.setShippingAddressResponse(shippingAddressResponse);
            orderDetailsCommonModel.setBillingAddressResponse(billingAddressResponse);
            orderDetailsCommonModel.setPaymentMethodResponse(paymentMethodResponse);
            orderDetailsCommonModel.setMulPayBillResponse(mulPayBillResponse);
            orderDetailsCommonModel.setCouponResponse(couponResponse);
            orderDetailsCommonModel.setGoodsDtoList(goodsDtoList);
            orderDetailsCommonModel.setConvenienceListResponse(convenienceListResponse);
            orderDetailsCommonModel.setExamKitListResponse(examKitListResponse);
            orderDetailsCommonModel.setConfigInfoResponse(configInfoResponse);

            orderDetailsModel.setOrderDetailsCommonModel(orderDetailsCommonModel);

        }
        // APIからエラーコードが返ってきた場合は、エラー画面へリダイレクトさせる
        // HEHandlerのreturnViewは自画面遷移なので、AbstractControllerの共通メソッドを呼び出さず、こちらでエラーハンドリングをする
        catch (HttpClientErrorException ce) {
            LOGGER.error("例外処理が発生しました", ce);
            // 想定内のエラー（400番台）の場合
            createClientErrorMessage(ce.getResponseBodyAsString(), error, redirectAttributes, model);
            return "redirect:/error";

        } catch (HttpServerErrorException se) {
            LOGGER.error("例外処理が発生しました", se);
            // 想定外のエラー（500番台）の場合
            createServerErrorMessage(se.getResponseBodyAsString(), redirectAttributes, model);
            return "redirect:/error";
        }

        // 同一商品内に複数の税率が混在するかチェック
        // TODO https://app.clickup.com/t/2tz2q4q
        //　List<CheckMessageDto> checkMessageDtoList = orderGoodsMixedTaxCheckLogic.checkOrderGoodsMixedTax(receiveOrderDto, MSGCD_MULTIPLE_TAX_RATE_IN_SINGLE_GOODS);
        List<CheckMessageDto> checkMessageDtoList = new ArrayList<>();

        // 複数の税率が混在する場合、警告を表示
        for (CheckMessageDto errorList : checkMessageDtoList) {
            addInfoMessage(errorList.getMessageId(), errorList.getArgs(), redirectAttributes, model);
        }

        // 受注詳細情報ページ反映
        this.orderDetailsHelper.toPage(orderDetailsModel, orderDetailsCommonModel);

        // キャンセル受注の場合、修正項目を設定
        setCancelOrderEditValue(orderDetailsModel, redirectAttributes, model);

        if (orderDetailsModel.getFrom() == null) {
            orderDetailsModel.setFrom("order");
            // fromが「order」でない場合、検索条件を破棄する為、mdに空文字をセット
        } else if (!orderDetailsModel.getFrom().equals("order")) {
            orderDetailsModel.setMd("");
        }

        return "order/details/details";
    }

    /**
     * キャンセル受注を更新する<br/>
     *
     * @param orderDetailsModel
     * @param redirectAttributes
     * @param model
     * @return 受注詳細画面（自分）
     */
    @PostMapping(value = "/", params = "doOnceUpdateCanceledOrder")
    @HEHandler(exception = AppLevelListException.class, returnView = "order/details/details")
    public String doOnceUpdateCanceledOrder(@Validated OrderDetailsModel orderDetailsModel,
                                            BindingResult error,
                                            RedirectAttributes redirectAttributes,
                                            Model model) {

        // エラーチェック
        if (error.hasErrors()) {
            return "order/details/details";
        }

        // 更新（管理メモのみ更新可能）
        CanceledTransactionUpdateRequest canceledTransactionUpdateRequest = new CanceledTransactionUpdateRequest();
        canceledTransactionUpdateRequest.setTransactionId(orderDetailsModel.getTransactionId());
        canceledTransactionUpdateRequest.setAdminMemo(orderDetailsModel.getEditMemo());
        try {
            this.transactionApi.updateCanceledTransaction(canceledTransactionUpdateRequest);
        } catch (HttpClientErrorException | HttpServerErrorException e) {
            LOGGER.error("例外処理が発生しました", e);
            // アイテム名調整マップ
            Map<String, String> itemNameAdjust = new HashMap<>();
            handleError(e.getStatusCode(), e.getResponseBodyAsString(), error, itemNameAdjust);

            if (error.hasErrors()) {
                return "order/details/detailsupdate";
            }
        }

        // キャンセル受注更新モード解除
        orderDetailsModel.setMd("list");
        addInfoMessage(MSGCD_UPDATE_CANCELED_ORDER_SUCCESS, null, redirectAttributes, model);

        redirectAttributes.addFlashAttribute(DetailsUpdateModel.FLASH_ORDERCODE, orderDetailsModel.getOrderCode());
        return "redirect:/order/details/";
    }

    /**
     * メールテンプレート選択画面に遷移
     *
     * @param orderDetailsModel
     * @param redirectAttributes
     * @param model
     * @return 遷移先
     */
    @PostMapping(value = "/", params = "doMailTemplate")
    public String doMailTemplate(@Validated OrderDetailsModel orderDetailsModel,
                                 BindingResult error,
                                 RedirectAttributes redirectAttributes,
                                 Model model) {

        // エラーチェック
        if (error.hasErrors()) {
            return "order/details/details";
        }

        // セッションの受注情報取得
        OrderDetailsCommonModel orderDetailsCommonModel = orderDetailsModel.getOrderDetailsCommonModel();

        // 受注情報がない場合 エラーページ
        if (orderDetailsCommonModel == null || orderDetailsCommonModel.getOrderReceivedResponse() == null
            || orderDetailsCommonModel.getCustomerResponse() == null) {
            addMessage(MSGCD_REFERER_FAIL, redirectAttributes, model);
            return "redirect:/error";
        }

        // メール情報を作成 Dxo
        MailSendDto mailSendDto = ApplicationContextUtility.getBean(MailSendDto.class);
        mailSendDto.setApplication(MailSendDto.ORDER);
        mailSendDto.setDisplayName("受注詳細");
        mailSendDto.setMailDtoList(new ArrayList<>());
        mailSendDto.setAvailableTemplateTypeList(new ArrayList<>());
        mailSendDto.setOrderCode(orderDetailsModel.getOrderCode());
        mailSendDto.setTransactionId(orderDetailsModel.getTransactionId());

        // 通常注文：テンプレートタイプを設定 注文確認/出荷/コンビニ督促/コンビニ期限切れ/受注汎用/汎用
        List<HTypeMailTemplateType> availableTemplateTypeList = new ArrayList<>();

        availableTemplateTypeList.add(HTypeMailTemplateType.ORDER_CONFIRMATION);
        availableTemplateTypeList.add(HTypeMailTemplateType.SHIPMENT_NOTIFICATION);
        availableTemplateTypeList.add(HTypeMailTemplateType.SETTLEMENT_REMINDER);
        availableTemplateTypeList.add(HTypeMailTemplateType.SETTLEMENT_EXPIRATION_NOTIFICATION);
        availableTemplateTypeList.add(HTypeMailTemplateType.GENERAL_ORDER);
        availableTemplateTypeList.add(HTypeMailTemplateType.GENERAL);
        availableTemplateTypeList.add(HTypeMailTemplateType.EXAM_RESULTS_NOTIFICATION);

        mailSendDto.setAvailableTemplateTypeList(availableTemplateTypeList);

        // 受注情報から作成
        MailDto mailDto = ApplicationContextUtility.getBean(MailDto.class);
        mailDto.setToList(Collections.singletonList(orderDetailsCommonModel.getCustomerResponse().getMemberInfoMail()));
        mailSendDto.getMailDtoList().add(mailDto);

        redirectAttributes.addFlashAttribute("mailSendDto", mailSendDto);

        return "redirect:/mailtemplate/send/select";
    }

    /**
     * 再オーソリボタン押下時処理<br/>
     *
     * <pre>
     * ①再オーソリ文言（yyyy/mm/dd　再オーソリを実行）を管理用メモに設定する
     * ②支払方法や、利用金額など全く変更がない状態で受注修正を行う
     *  注文チェック、料金計算は不要⇒注文時点の商品情報/金額で再オーソリをかける必要がある為。
     * </pre>
     *
     * @param orderDetailsModel
     * @param redirectAttributes
     * @param model
     * @return 受注詳細画面
     */
    @PreAuthorize("hasAnyAuthority('ORDER:8')")
    @PostMapping(value = "/", params = "doReAuthory")
    @HEHandler(exception = AppLevelListException.class, returnView = "order/details/details")
    public String doReAuthory(@Validated OrderDetailsModel orderDetailsModel,
                              BindingResult error,
                              RedirectAttributes redirectAttributes,
                              Model model) {

        // エラーチェック
        if (error.hasErrors()) {
            return "order/details/details";
        }

        // セッションの受注情報取得
        OrderDetailsCommonModel orderDetailsCommonModel = orderDetailsModel.getOrderDetailsCommonModel();

        // 受注情報がない場合 エラーページ
        if (orderDetailsCommonModel == null || orderDetailsCommonModel.getCustomerResponse() == null) {
            addMessage(MSGCD_REFERER_FAIL, redirectAttributes, model);
            return "redirect:/error";
        }

        // TODO 別途課題 バックエンドへチェック移行 請求情報の不整合チェック
        // // 請求情報の不整合チェック処理の初期処理
        // AuthorityCheckRequest authorityCheckRequest = new AuthorityCheckRequest();
        // authorityCheckRequest.setOrderCode(orderDetailsCommonModel.getOrderReceivedResponse().getOrderCode());
        // try {
        //     this.billApi.check(authorityCheckRequest);
        // } catch (HttpClientErrorException | HttpServerErrorException e) {
        //     // アイテム名調整マップ
        //     Map<String, String> itemNameAdjust = new HashMap<>();
        //     handleError(e.getStatusCode(), e.getResponseBodyAsString(), error, itemNameAdjust);
        //
        //     if (error.hasErrors()) {
        //         return "order/details/detailsupdate";
        //     }
        // }
        //
        // // 後続処理のためリクエスト初期化
        // authorityCheckRequest = new AuthorityCheckRequest();
        try {
            // 再オーソリ処理の実行
            TransactionReAuthRequest transactionReAuthRequest = new TransactionReAuthRequest();
            transactionReAuthRequest.setTransactionId(orderDetailsModel.getTransactionId());
            this.transactionApi.doReAuth(transactionReAuthRequest);

            // TODO 別途課題 バックエンドへチェック移行 請求情報の不整合チェック
            // // 請求情報の不整合チェック
            // this.billApi.check(authorityCheckRequest);

            // 再オーソリ成功メッセージ表示
            addInfoMessage(MSGCD_REAUTHORY_SUCCESS, null, redirectAttributes, model);

        } catch (HttpClientErrorException ce) {
            LOGGER.error("例外処理が発生しました", ce);
            // 想定内のエラー（400番台）の場合
            createClientErrorMessage(ce.getResponseBodyAsString(), error, redirectAttributes, model);

            // TODO 別途課題 バックエンドへチェック移行 請求情報の不整合チェック
            // // 請求情報用のエラー情報を生成する
            // List<String> errorInfo = createBillCheckErrorInfo(ce.getResponseBodyAsString());
            // if (CollectionUtils.isEmpty(errorInfo)) {
            //     // 請求情報の不整合チェック
            //     authorityCheckRequest.setErrInfo(StringUtils.join(errorInfo.iterator(), "\r\n"));
            //     this.billApi.check(authorityCheckRequest);
            // }

            return "order/details/details";

        } catch (HttpServerErrorException se) {
            LOGGER.error("例外処理が発生しました", se);
            // 想定外のエラー（500番台）の場合
            createServerErrorMessage(se.getResponseBodyAsString(), redirectAttributes, model);
            return "redirect:/error";
        }

        redirectAttributes.addFlashAttribute(DetailsUpdateModel.FLASH_ORDERCODE, orderDetailsModel.getOrderCode());
        return "redirect:/order/details/";
    }

    /**
     * 検査結果ダウンロードイベント<br/>
     *
     * @param orderDetailsModel 受注詳細モデル
     * @return ダウンロードファイル
     */
    @PreAuthorize("hasAnyAuthority('ORDER:8')")
    @PostMapping(value = "/", params = "doExamResultsDownload")
    @HEHandler(exception = AppLevelListException.class, returnView = "order/details/details")
    public ResponseEntity<DownloadFileStreamingResponseBody> doExamResultsDownload(OrderDetailsModel orderDetailsModel) {

        // 検査結果PDFの格納場所
        String examResultPdfStoragePath = orderDetailsModel.getExamresultsPdfStoragePath();

        // 会員SEQ
        Integer memberInfoSeq = orderDetailsModel.getMemberInfoSeq();

        // ダウンロード用検査結果PDF
        String examResultsDownloadFileName = orderDetailsModel.getExamResultsDownloadFileName();

        if (StringUtils.isEmpty(examResultPdfStoragePath) || memberInfoSeq == null || StringUtils.isEmpty(
                        examResultsDownloadFileName)) {
            LOGGER.error("PDF格納場所の取得に失敗しました。");
            throwMessage("EXAM-0001-001-A-E");
        }

        // 検査結果PDFのダウンロードパス
        String pdfFilePath = examResultPdfStoragePath + memberInfoSeq + File.separator + examResultsDownloadFileName;
        File pdfFile = new File(pdfFilePath);

        if (!pdfFile.exists() || pdfFile.isDirectory()) {
            LOGGER.error("検査結果PDFが存在しません。ファイルパス：" + pdfFilePath);
            throwMessage("EXAM-0001-001-A-E");
        }

        DownloadFileStreamingResponseBody responseBody = new DownloadFileStreamingResponseBody(pdfFilePath);
        HttpHeaders responseHeaders = getHttpHeaders(pdfFile.getName());

        return new ResponseEntity<>(responseBody, responseHeaders, HttpStatus.OK);
    }

    /**
     *  診療・診察時PDFダウンロードイベント<br/>
     *
     * @param orderDetailsModel 受注詳細モデル
     * @return ダウンロードファイル
     */
    @PreAuthorize("hasAnyAuthority('ORDER:8')")
    @PostMapping(value = "/", params = "doExaminationRuleDownload")
    @HEHandler(exception = AppLevelListException.class, returnView = "order/details/details")
    public ResponseEntity<DownloadFileStreamingResponseBody> doExaminationRuleDownload(
                    OrderDetailsModel orderDetailsModel) {

        // 診療・診察時のお願い」のPDFのファイル名（フルパス）
        String pdfFilePath =  orderDetailsModel.getExaminationRulePdfPath();

        if (StringUtils.isEmpty(pdfFilePath)) {
            LOGGER.error("PDF格納場所の取得に失敗しました。");
            throwMessage("EXAM-0001-001-A-E");
        }

        File pdfFile = new File(pdfFilePath);

        if (!pdfFile.exists() || pdfFile.isDirectory()) {
            LOGGER.error("「診療・診察時のお願い」PDFが存在しません。ファイルパス：" + pdfFilePath);
            throwMessage("EXAM-0001-001-A-E");
        }

        DownloadFileStreamingResponseBody responseBody = new DownloadFileStreamingResponseBody(pdfFilePath);
        HttpHeaders responseHeaders = getHttpHeaders(pdfFile.getName());

        return new ResponseEntity<>(responseBody, responseHeaders, HttpStatus.OK);
    }

    /**
     * キャンセル受注の修正可能項目に値を設定する
     *
     * @param orderDetailsModel
     * @param redirectAttributes
     * @param model
     */
    protected void setCancelOrderEditValue(OrderDetailsModel orderDetailsModel,
                                           RedirectAttributes redirectAttributes,
                                           Model model) {

        OrderDetailsCommonModel orderDetailsCommonModel = orderDetailsModel.getOrderDetailsCommonModel();
        // キャンセル受注の更新かどうか("cou"は CanceledOrderUpdateの略)
        if (CANCEL.equals(orderDetailsCommonModel.getOrderReceivedResponse().getOrderStatus())
            && orderDetailsModel.getMd().equals("cou")) {
            orderDetailsModel.setCanceledOrderUpdate(true);
            if (StringUtils.isNotBlank(orderDetailsCommonModel.getOrderReceivedResponse().getAdminMemo())) {
                orderDetailsModel.setEditMemo(orderDetailsCommonModel.getOrderReceivedResponse().getAdminMemo());
            }
            addInfoMessage(MSGCD_UPDATE_CANCELED_ORDER, null, redirectAttributes, model);
        }

        if (CANCEL.equals(orderDetailsCommonModel.getOrderReceivedResponse().getOrderStatus())) {
            // リンク決済の場合以下を表示
            if (orderDetailsModel.isLinkPay()) {
                // 後日払い
                if (orderDetailsModel.isConveni() || orderDetailsModel.isPayEasy()
                    || orderDetailsModel.isBankTransferAozora()) {
                    // 未入金以外
                    if (!HTypePaymentStatus.NOTHING_MONEY.getValue().equals(orderDetailsModel.getPaymentStatus())) {
                        addInfoMessage(MSGCD_LINKPAY_CANCEL_PAYLATER, null, redirectAttributes, model);
                    }
                } else if (orderDetailsModel.isPassGmoCancelDeadline()) {
                    // 即時払い(現在時刻がGMO即時払いキャンセル期限を過ぎている)
                    addInfoMessage(MSGCD_LINKPAY_CANCEL_PASS_GMO_CANCEL_DEADLINE, null, redirectAttributes, model);
                } else {
                    // 即時払い(現在時刻がGMO即時払いキャンセル期限内)
                    addInfoMessage(MSGCD_LINKPAY_CANCEL_IN_GMO_CANCEL_DEADLINE,
                                   new Object[] {PropertiesUtil.getSystemPropertiesValue("gmo.shop.management.link")},
                                   redirectAttributes, model
                                  );
                }
            }
        } else {
            orderDetailsModel.setCanceledOrderUpdate(false);
        }
    }

    /**
     * 注文票をもとに、商品サービスから商品詳細リストを取得する<br/>
     * 商品サービスから取得した商品詳細リストは、注文商品リストの商品ID順に並び変える
     *
     * @param orderSlipResponse
     * @return ソート後の商品詳細Dtoリスト
     */
    private List<GoodsDetailsDto> getGoodsDetailsDtoList(OrderSlipResponse orderSlipResponse) {

        // 注文商品リストから商品IDリストを作成
        List<Integer> idList = new ArrayList<>();
        for (int i = 0; i < orderSlipResponse.getItemList().size(); i++) {
            idList.add(this.conversionUtility.toInteger(orderSlipResponse.getItemList().get(i).getItemId()));
        }

        // 商品サービスから商品詳細リストを取得
        ProductDetailListGetRequest request = new ProductDetailListGetRequest();
        request.setGoodsSeqList(idList);
        ProductDetailListResponse response = this.productApi.getDetails(request, null);

        // 商品詳細リストが取得できない場合はスキップし、呼び出し元でエラーハンドリングする
        if (response == null || CollectionUtils.isEmpty(response.getGoodsDetailsList())) {
            return null;
        }

        List<GoodsDetailsResponse> goodsDetailsResponseList = response.getGoodsDetailsList();
        goodsDetailsResponseList.sort(Comparator.comparing(item -> idList.indexOf(item.getGoodsSeq())));

        return this.orderDetailsHelper.toProductDetailList(response, orderSlipResponse);
    }

    /**
     * HTTPレスポンスのヘッダー情報を生成する
     *
     * @param filename ファイル名
     * @return HTTPHeaders
     */
    private HttpHeaders getHttpHeaders(String filename) {
        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.APPLICATION_OCTET_STREAM);
        headers.set("responseType", "Async");
        headers.add(HttpHeaders.CONTENT_DISPOSITION, "attachment;filename=\""+ filename + "\"");
        return headers;
    }


}