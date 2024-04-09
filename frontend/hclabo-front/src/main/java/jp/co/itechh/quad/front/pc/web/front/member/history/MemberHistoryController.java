/*
 * Project Name : HIT-MALL4
 *
 * Copyright (C) 2021 i-TEC HANKYU HANSHIN INC. All Rights Reserved.
 *
 */

package jp.co.itechh.quad.front.pc.web.front.member.history;

import jp.co.itechh.quad.addressbook.presentation.api.AddressBookApi;
import jp.co.itechh.quad.addressbook.presentation.api.param.AddressBookAddressResponse;
import jp.co.itechh.quad.billingslip.presentation.api.BillingSlipApi;
import jp.co.itechh.quad.billingslip.presentation.api.param.BillingSlipGetRequest;
import jp.co.itechh.quad.billingslip.presentation.api.param.BillingSlipResponse;
import jp.co.itechh.quad.convenience.presentation.api.ConvenienceApi;
import jp.co.itechh.quad.convenience.presentation.api.param.ConvenienceListResponse;
import jp.co.itechh.quad.coupon.presentation.api.CouponApi;
import jp.co.itechh.quad.coupon.presentation.api.param.CouponResponse;
import jp.co.itechh.quad.examination.presentation.api.ExaminationApi;
import jp.co.itechh.quad.examination.presentation.api.param.ConfigInfoResponse;
import jp.co.itechh.quad.examination.presentation.api.param.ExamKitListResponse;
import jp.co.itechh.quad.front.annotation.exception.HEHandler;
import jp.co.itechh.quad.front.application.commoninfo.impl.HmFrontUserDetails;
import jp.co.itechh.quad.front.base.constant.ValidatorConstants;
import jp.co.itechh.quad.front.base.exception.AppLevelListException;
import jp.co.itechh.quad.front.base.utility.ApplicationContextUtility;
import jp.co.itechh.quad.front.base.utility.ConversionUtility;
import jp.co.itechh.quad.front.constant.type.HTypeSettlementMethodType;
import jp.co.itechh.quad.front.dto.goods.goods.GoodsDetailsDto;
import jp.co.itechh.quad.front.utility.CommonInfoUtility;
import jp.co.itechh.quad.front.web.AbstractController;
import jp.co.itechh.quad.front.web.DownloadFileStreamingResponseBody;
import jp.co.itechh.quad.front.web.PageInfoModule;
import jp.co.itechh.quad.method.presentation.api.SettlementMethodApi;
import jp.co.itechh.quad.method.presentation.api.ShippingMethodApi;
import jp.co.itechh.quad.method.presentation.api.param.PaymentMethodResponse;
import jp.co.itechh.quad.method.presentation.api.param.ShippingMethodResponse;
import jp.co.itechh.quad.mulpay.presentation.api.MulpayApi;
import jp.co.itechh.quad.mulpay.presentation.api.param.MulPayBillRequest;
import jp.co.itechh.quad.mulpay.presentation.api.param.MulPayBillResponse;
import jp.co.itechh.quad.orderreceived.presentation.api.OrderReceivedApi;
import jp.co.itechh.quad.orderreceived.presentation.api.param.CustomerHistoryListResponse;
import jp.co.itechh.quad.orderreceived.presentation.api.param.OrderReceivedResponse;
import jp.co.itechh.quad.orderreceived.presentation.api.param.PageInfoRequest;
import jp.co.itechh.quad.orderreceived.presentation.api.param.PageInfoResponse;
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
import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.util.CollectionUtils;
import org.springframework.util.ObjectUtils;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.SessionAttributes;
import org.springframework.web.bind.support.SessionStatus;
import org.springframework.web.client.HttpClientErrorException;
import org.springframework.web.client.HttpServerErrorException;
import org.springframework.web.servlet.mvc.method.annotation.StreamingResponseBody;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import java.io.File;
import java.math.BigDecimal;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.Comparator;
import java.util.HashMap;
import java.util.LinkedHashSet;
import java.util.List;
import java.util.Map;
import java.util.regex.Pattern;

/**
 * 注文履歴 Controller
 *
 * @author kimura
 */
@RequestMapping("/member/history")
@Controller
@SessionAttributes(value = "memberHistoryModel")
public class MemberHistoryController extends AbstractController {

    /** ロガー */
    private static final Logger LOGGER = LoggerFactory.getLogger(MemberHistoryController.class);

    /** 注文履歴一覧：デフォルトページ番号 */
    public static final String DEFAULT_HISTORY_PNUM = "1";

    /** 注文履歴一覧：１ページ当たりのデフォルト最大表示件数 */
    public static final int DEFAULT_HISTORY_LIMIT = 5;

    /** モデルクリア時のクリア対象外フィールド */
    public static final String[] CLEAR_EXCLUDED_FIELDS = {"pnum", "limit"};

    /** メッセージコード：不正遷移 */
    public static final String MSGCD_REFERER_FAIL = "AMH000201";

    /** メッセージコード：セッション情報無し、ブラウザバックなど */
    protected static final String MSGCD_ILLEGAL_OPERATION = "PKG-3556-005-A-";

    /** 決済方法：コンビニ */
    public static final String CONVENIENCE_TYPE = "3";

    /** 商品API */
    private final ProductApi productApi;

    /** 受注API */
    private final OrderReceivedApi orderReceivedApi;

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

    /** 決済方法API */
    private final ConvenienceApi convenienceApi;

    /** 配送方法API */
    private final ShippingMethodApi shippingMethodApi;

    /** マルペイAPI */
    private final MulpayApi mulpayApi;

    /** クーポンAPI */
    private final CouponApi couponApi;

    /** 検査API */
    private final ExaminationApi examinationApi;

    /** 注文履歴Helper */
    private MemberHistoryHelper memberHistoryHelper;

    /** 変換Utility */
    private final ConversionUtility conversionUtility;

    /** 共通情報ヘルパークラス */
    private final CommonInfoUtility commonInfoUtility;

    /** コンストラクタ */
    @Autowired
    public MemberHistoryController(ProductApi productApi,
                                   OrderReceivedApi orderReceivedApi,
                                   ShippingSlipApi shippingSlipApi,
                                   BillingSlipApi billingSlipApi,
                                   SalesSlipApi salesSlipApi,
                                   OrderSlipApi orderSlipApi,
                                   AddressBookApi addressBookApi,
                                   SettlementMethodApi settlementMethodApi,
                                   ShippingMethodApi shippingMethodApi,
                                   MulpayApi mulpayApi,
                                   CouponApi couponApi,
                                   ConvenienceApi convenienceApi,
                                   ExaminationApi examinationApi, MemberHistoryHelper memberHistoryHelper,
                                   ConversionUtility conversionUtility,
                                   CommonInfoUtility commonInfoUtility) {
        this.productApi = productApi;
        this.orderReceivedApi = orderReceivedApi;
        this.shippingSlipApi = shippingSlipApi;
        this.billingSlipApi = billingSlipApi;
        this.salesSlipApi = salesSlipApi;
        this.orderSlipApi = orderSlipApi;
        this.addressBookApi = addressBookApi;
        this.settlementMethodApi = settlementMethodApi;
        this.shippingMethodApi = shippingMethodApi;
        this.mulpayApi = mulpayApi;
        this.couponApi = couponApi;
        this.convenienceApi = convenienceApi;
        this.examinationApi = examinationApi;
        this.memberHistoryHelper = memberHistoryHelper;
        this.conversionUtility = conversionUtility;
        this.commonInfoUtility = commonInfoUtility;
    }

    /**
     * 一覧画面：初期処理
     *
     * @param memberHistoryModel 会員注文履歴Model
     * @param model              Model
     * @return 一覧画面
     */
    @GetMapping(value = "/")
    @HEHandler(exception = AppLevelListException.class, returnView = "member/history/index")
    protected String doLoadIndex(MemberHistoryModel memberHistoryModel, Model model) {

        // ページング情報初期化
        if (memberHistoryModel.getPnum() == null) {
            memberHistoryModel.setPnum(DEFAULT_HISTORY_PNUM);
        }
        if (memberHistoryModel.getLimit() == 0) {
            memberHistoryModel.setLimit(DEFAULT_HISTORY_LIMIT);
        }

        // モデル初期化
        clearModel(MemberHistoryModel.class, memberHistoryModel, CLEAR_EXCLUDED_FIELDS, model);

        // 注文履歴一覧の検索
        searchHistoryList(memberHistoryModel, model);

        return "member/history/index";
    }

    /**
     * 詳細画面：初期処理
     *
     * @param ocd                　注文履歴URLパラメータ
     * @param memberHistoryModel 注文履歴Model
     * @param redirectAttributes
     * @param sessionStatus
     * @param model
     * @return 詳細画面
     */
    @GetMapping(value = "/detail")
    @HEHandler(exception = AppLevelListException.class, returnView = "redirect:/member/history/")
    protected String doLoadDetail(@RequestParam(required = false) String ocd,
                                  MemberHistoryModel memberHistoryModel,
                                  BindingResult error,
                                  RedirectAttributes redirectAttributes,
                                  SessionStatus sessionStatus,
                                  Model model) {

        if (StringUtils.isEmpty(ocd)) {
            // URLパラメータが不足した遷移の場合（session破棄ができないため、こちらで制御）
            addMessage(getMsgcdRefererFail(), redirectAttributes, model);
            return getBackpageClass();
        }

        // 認証画面から遷移した時や、URLにクエリーがついている場合は ocd に値が存在するが
        // 当画面でリロードを行うと ocd にはないため、保管用の変数から取得する。
        String orderCode = memberHistoryModel.getOcd() == null ?
                        memberHistoryModel.getSaveOcd() :
                        memberHistoryModel.getOcd();

        // orderCodeがnullの場合は、明示的にエラーページへ遷移させる
        if (StringUtils.isEmpty(orderCode)) {
            return "redirect:/error";
        }

        if (!Pattern.matches(ValidatorConstants.REGEX_ORDER_CODE, orderCode)) {
            addMessage(getMsgcdRefererFail(), redirectAttributes, model);
            return getBackpageClass();
        }
        // 初期値
        memberHistoryModel.setExamResultsListNotEmpty(false);

        return doLoadOrder(orderCode, memberHistoryModel, error, redirectAttributes, sessionStatus, model);
    }

    /**
     * 検査結果ダウンロード
     *
     * @param examResultsPdf           検査結果PDF
     * @param memberHistoryModel    注文履歴 Model
     */
    @PostMapping(value = "/detail", params = "doExamResultsDownload")
    @HEHandler(exception = AppLevelListException.class, returnView = "/member/history/detail")
    public ResponseEntity<StreamingResponseBody> doExamResultsDownload(@RequestParam String examResultsPdf,
                                                                       MemberHistoryModel memberHistoryModel) {

        HmFrontUserDetails userDetails = commonInfoUtility.getFrontUserDetailsFromSpringSecurity();
        Integer memberInfoSeq = userDetails.getMemberInfoEntity().getMemberInfoSeq();

        if (memberInfoSeq == null) {
            LOGGER.error("会員SEQの取得に失敗しました。");
            throwMessage("EXAM-0001-001-A-E");
        }

        String path = memberHistoryModel.getExamresultsPdfStoragePath() + File.separator + memberInfoSeq
                      + File.separator + examResultsPdf;

        if (Files.notExists(Paths.get(path))) {
            LOGGER.error("検査結果PDFが存在しません。ファイルパス：" + path);
            throwMessage("EXAM-0001-001-A-E");
        }

        DownloadFileStreamingResponseBody responseBody = new DownloadFileStreamingResponseBody(path);

        HttpHeaders headers = setHttpHeadersDownload(examResultsPdf);

        return new ResponseEntity<>(responseBody, headers, HttpStatus.OK);
    }

    /**
     * 診療・診察時のお願い
     *
     * @param memberHistoryModel    注文履歴 Model
     */
    @PostMapping(value = "/detail", params = "doExaminationRuleDownload")
    @HEHandler(exception = AppLevelListException.class, returnView = "/member/history/detail")
    public ResponseEntity<StreamingResponseBody> doExaminationRuleDownload(MemberHistoryModel memberHistoryModel) {

        String path = memberHistoryModel.getExaminationRulePdfPath();

        if (Files.notExists(Paths.get(path))) {
            LOGGER.error("「診療・診察時のお願い」PDFが存在しません。ファイルパス：" + path);
            throwMessage("EXAM-0001-001-A-E");
        }

        DownloadFileStreamingResponseBody responseBody = new DownloadFileStreamingResponseBody(memberHistoryModel.getExaminationRulePdfPath());

        HttpHeaders headers = setHttpHeadersDownload("examinationrule.pdf");

        return new ResponseEntity<>(responseBody, headers, HttpStatus.OK);
    }

    /**
     * 注文履歴詳細表示
     *
     * @param orderCode          受注番号
     * @param memberHistoryModel 注文履歴Model
     * @param redirectAttributes
     * @param sessionStatus
     * @param model
     * @return 遷移先画面
     */
    private String doLoadOrder(String orderCode,
                               MemberHistoryModel memberHistoryModel,
                               BindingResult error,
                               RedirectAttributes redirectAttributes,
                               SessionStatus sessionStatus,
                               Model model) {

        // 各種伝票情報と注文時の情報を取得
        OrderReceivedResponse orderReceivedResponse = null;
        ShippingSlipResponse shippingSlipResponse = null;
        BillingSlipResponse billingSlipResponse = null;
        SalesSlipResponse salesSlipResponse = null;
        OrderSlipResponse orderSlipResponse = null;
        ExamKitListResponse examKitListResponse = null;
        AddressBookAddressResponse shippingAddressResponse = null;
        AddressBookAddressResponse billingAddressResponse = null;
        PaymentMethodResponse paymentMethodResponse = null;
        ShippingMethodResponse shippingMethodResponse = null;
        MulPayBillResponse mulPayBillResponse = null;
        ConvenienceListResponse convenienceListResponse = null;
        CouponResponse couponResponse = null;
        List<GoodsDetailsDto> goodsDtoList = null;
        ConfigInfoResponse configInfoResponse = null;
        try {
            // 受注番号から取引IDを取得
            orderReceivedResponse = this.orderReceivedApi.getByOrderCode(orderCode);

            if (ObjectUtils.isEmpty(orderReceivedResponse)) {
                // 一覧画面にリダイレクト
                throwMessage(MSGCD_REFERER_FAIL);
            }
            // 注文履歴情報の会員情報についてチェック
            if (checkOrderMember(orderReceivedResponse)) {
                addMessage(MSGCD_REFERER_FAIL, redirectAttributes, model);
                return "redirect:/error";
            }

            ShippingSlipGetRequest shippingSlipGetRequest = new ShippingSlipGetRequest();
            shippingSlipGetRequest.setTransactionId(orderReceivedResponse.getLatestTransactionId());
            shippingSlipResponse = this.shippingSlipApi.get(shippingSlipGetRequest);

            BillingSlipGetRequest billingSlipGetRequest = new BillingSlipGetRequest();
            billingSlipGetRequest.setTransactionId(orderReceivedResponse.getLatestTransactionId());
            billingSlipResponse = this.billingSlipApi.get(billingSlipGetRequest);

            SalesSlipGetRequest salesSlipGetRequest = new SalesSlipGetRequest();
            salesSlipGetRequest.setTransactionId(orderReceivedResponse.getLatestTransactionId());
            salesSlipResponse = this.salesSlipApi.get(salesSlipGetRequest);

            OrderSlipGetRequest orderSlipSlipGetRequest = new OrderSlipGetRequest();
            orderSlipSlipGetRequest.setTransactionId(orderReceivedResponse.getLatestTransactionId());
            orderSlipResponse = this.orderSlipApi.get(orderSlipSlipGetRequest);

            // 伝票情報が存在しない場合はエラー
            if (ObjectUtils.isEmpty(shippingSlipResponse) || ObjectUtils.isEmpty(billingSlipResponse)
                || ObjectUtils.isEmpty(salesSlipResponse) || ObjectUtils.isEmpty(orderSlipResponse)) {
                // 一覧画面にリダイレクト
                throwMessage(MSGCD_REFERER_FAIL);
            }
            // 検査キット情報の取得
            examKitListResponse = examinationApi.getExamKitList(memberHistoryHelper.toExamKitRequest(orderSlipResponse.getItemList()));

            configInfoResponse = examinationApi.getConfigInfo();

            // お届け先住所情報の取得
            shippingAddressResponse = this.addressBookApi.getAddressById(shippingSlipResponse.getShippingAddressId());

            // 請求先住所情報の取得
            billingAddressResponse = this.addressBookApi.getAddressById(billingSlipResponse.getBillingAddressId());

            // 決済方法の取得
            paymentMethodResponse = this.settlementMethodApi.getBySettlementMethodSeq(
                            this.conversionUtility.toInteger(billingSlipResponse.getPaymentMethodId()));

            // 配送方法の取得
            shippingMethodResponse = this.shippingMethodApi.getByDeliveryMethodSeq(
                            this.conversionUtility.toInteger(shippingSlipResponse.getShippingMethodId()));

            // 各種情報が存在しない場合はエラー
            if (ObjectUtils.isEmpty(shippingAddressResponse) || ObjectUtils.isEmpty(billingAddressResponse)
                || ObjectUtils.isEmpty(paymentMethodResponse) || ObjectUtils.isEmpty(shippingMethodResponse)
                || ObjectUtils.isEmpty(shippingMethodResponse.getDeliveryMethodResponse())) {
                //請求先住所情報、配送先住所情報、決済方法、配送方法が取得できない場合
                // 一覧画面にリダイレクト
                throwMessage(MSGCD_REFERER_FAIL);
            }

            if (HTypeSettlementMethodType.LINK_PAYMENT.getValue()
                                                      .equals(paymentMethodResponse.getSettlementMethodType())) {
                if (!ObjectUtils.isEmpty(billingSlipResponse.getPaymentLinkResponse()) && CONVENIENCE_TYPE.equals(
                                billingSlipResponse.getPaymentLinkResponse().getPayType())) {
                    convenienceListResponse = this.convenienceApi.get();
                }
            }

            // マルチペイメント情報を取得
            MulPayBillRequest mulPayBillRequest = new MulPayBillRequest();
            mulPayBillRequest.setOrderCode(orderCode);

            mulPayBillResponse = this.mulpayApi.getByOrderCode(mulPayBillRequest);

            // クーポンが適用されている場合
            if (salesSlipResponse.getCouponSeq() != null) {
                // クーポン情報を取得
                couponResponse = this.couponApi.getByCouponSeq(salesSlipResponse.getCouponSeq());
            }

            // 商品サービスから商品詳細リストを取得
            goodsDtoList = getGoodsDetailsDtoList(orderSlipResponse);

        } catch (HttpClientErrorException | HttpServerErrorException e) {
            LOGGER.error("例外処理が発生しました", e);
            // アイテム名調整マップ
            Map<String, String> itemNameAdjust = new HashMap<>();
            handleError(e.getStatusCode(), e.getResponseBodyAsString(), error, itemNameAdjust);

            // handleErrorが何らかの理由で捌ききれなかった場合
            throwMessage(MSGCD_REFERER_FAIL);
        }

        // ページへの反映
        this.memberHistoryHelper.toPageForLoadDetail(orderReceivedResponse, shippingSlipResponse, billingSlipResponse,
                                                     salesSlipResponse, orderSlipResponse, examKitListResponse,
                                                     shippingAddressResponse, billingAddressResponse,
                                                     paymentMethodResponse, shippingMethodResponse, mulPayBillResponse,
                                                     convenienceListResponse, couponResponse, goodsDtoList,
                                                     configInfoResponse, memberHistoryModel
                                                    );

        // pnum保持のためにセッション破棄をやめる
        // Modelをセッションより破棄
        // sessionStatus.setComplete();
        memberHistoryModel.setPnum(null);
        memberHistoryModel.setLimit(0);

        setReducedTaxRate(memberHistoryModel);

        return prerender(memberHistoryModel, redirectAttributes, model);
    }

    /**
     * 軽減税率対象商品があるかのを判断
     *
     * @param memberHistoryModel 注文履歴 Model
     */
    protected void setReducedTaxRate(MemberHistoryModel memberHistoryModel) {
        for (HistoryModelGoodsItem historyModelGoodsItem : memberHistoryModel.getOrderDeliveryItem()
                                                                             .getGoodsListItems()) {
            if (BigDecimal.valueOf(8).equals(historyModelGoodsItem.getTaxRate())) {
                memberHistoryModel.setReducedTaxRate(true);
                break;
            }
        }
    }

    /**
     * 注文履歴一覧の検索<br/>
     *
     * @param memberHistoryModel 注文履歴Model
     */
    protected void searchHistoryList(MemberHistoryModel memberHistoryModel, Model model) {

        // ページング検索セットアップ
        PageInfoModule pageInfoModule = ApplicationContextUtility.getBean(PageInfoModule.class);

        // リクエスト用のページャーを生成
        PageInfoRequest pageInfoRequest = new PageInfoRequest();
        // リクエスト用のページャー項目をセット
        pageInfoModule.setupPageRequest(pageInfoRequest, this.conversionUtility.toInteger(memberHistoryModel.getPnum()),
                                        memberHistoryModel.getLimit(), null, true
                                       );

        CustomerHistoryListResponse response = this.orderReceivedApi.getCustomerHistoryList(pageInfoRequest);

        if (response == null || CollectionUtils.isEmpty(response.getCustomerHistoryList())) {
            return;
        }

        // ページャーにレスポンス情報をセット
        PageInfoResponse pageInfoResponse = response.getPageInfo();
        pageInfoModule.setupPageInfo(memberHistoryModel, pageInfoResponse.getPage(), pageInfoResponse.getLimit(),
                                     pageInfoResponse.getNextPage(), pageInfoResponse.getPrevPage(),
                                     pageInfoResponse.getTotal(), pageInfoResponse.getTotalPages(), null, null, null,
                                     false, null
                                    );

        // ページャーをセット
        pageInfoModule.setupViewPager(memberHistoryModel.getPageInfo(), model);

        this.memberHistoryHelper.toPageForLoad(response.getCustomerHistoryList(), memberHistoryModel);
    }

    /**
     * 注文履歴情報の会員情報についてチェックを行う
     * <pre>
     * アクセス中の会員の注文履歴かをチェック
     * </pre>
     *
     * @param response 受注レスポンス
     * @return true：上記チェックでエラーがある場合
     */
    protected boolean checkOrderMember(OrderReceivedResponse response) {

        Integer memberInfoSeq = getCommonInfo().getCommonInfoUser().getMemberInfoSeq();

        if (StringUtils.isNotEmpty(response.getCustomerId()) && !response.getCustomerId()
                                                                         .equals(memberInfoSeq.toString())) {
            return true;
        }
        return false;
    }

    /**
     * @return 戻り先ページ取得
     */
    public String getBackpageClass() {
        return "redirect:/member/history/";
    }

    /**
     * エラーメッセージ取得
     *
     * @return MSGCD_REFERER_FAIL
     */
    public String getMsgcdRefererFail() {
        return MSGCD_REFERER_FAIL;
    }

    /**
     * 表示前処理
     *
     * @param memberHistoryModel
     * @param redirectAttributes
     * @param model
     * @return 自画面
     */
    public String prerender(MemberHistoryModel memberHistoryModel, RedirectAttributes redirectAttributes, Model model) {

        if (isSessionCheck(memberHistoryModel)) {
            // 注文情報が取得できない場合、画面の表示制御ができないのでエラー画面に飛ばす
            addMessage(MSGCD_ILLEGAL_OPERATION, redirectAttributes, model);
            return "redirect:/error";
        }

        return "member/history/detail";
    }

    /**
     * ブラウザバックなどで戻った場合に必要なセッション情報が消えているかチェックをする<br/>
     *
     * @return true:エラーあり  false:エラー無し
     */
    protected boolean isSessionCheck(MemberHistoryModel memberHistoryModel) {

        if (StringUtils.isEmpty(memberHistoryModel.getOrderCode())) {
            // 注文番号が取得できない場合エラー
            return true;
        }
        return false;
    }

    /**
     * 注文票をもとに、商品サービスから商品詳細リストを取得する<br/>
     * 商品サービスから取得した商品詳細リストは、注文商品リストの商品ID順に並び変える
     *
     * @param orderSlipResponse 注文票レスポンス
     * @return ソート後の商品詳細Dtoリスト
     */
    private List<GoodsDetailsDto> getGoodsDetailsDtoList(OrderSlipResponse orderSlipResponse) {

        // 注文商品リストから商品IDリストを作成
        LinkedHashSet<Integer> idHashSet = new LinkedHashSet<>();
        for (int i = 0; i < orderSlipResponse.getItemList().size(); i++) {
            idHashSet.add(this.conversionUtility.toInteger(orderSlipResponse.getItemList().get(i).getItemId()));
        }
        List<Integer> idList = new ArrayList<>(idHashSet);


        // 商品サービスから商品詳細リストを取得
        ProductDetailListGetRequest request = new ProductDetailListGetRequest();
        request.setGoodsSeqList(idList);
        ProductDetailListResponse response = this.productApi.getDetails(request, null);

        // 商品詳細リストが取得できない場合はエラー
        if (ObjectUtils.isEmpty(response) || CollectionUtils.isEmpty(response.getGoodsDetailsList())) {
            // 一覧画面にリダイレクト
            throwMessage(MSGCD_REFERER_FAIL);
        }

        List<GoodsDetailsResponse> goodsDetailsResponseList = response.getGoodsDetailsList();
        goodsDetailsResponseList.sort(Comparator.comparing(item -> idList.indexOf(item.getGoodsSeq())));

        return this.memberHistoryHelper.toProductDetailList(response, orderSlipResponse);
    }

    /**
     * HTTPレスポンスのヘッダー情報を生成する
     *
     * @param filename ファイル名
     * @return HTTPHeaders
     */
    private HttpHeaders setHttpHeadersDownload(String filename) {
        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.APPLICATION_OCTET_STREAM);
        headers.set("responseType", "Async");
        headers.add(HttpHeaders.CONTENT_DISPOSITION, "attachment;filename=\""+ filename + "\"");
        return headers;
    }

}