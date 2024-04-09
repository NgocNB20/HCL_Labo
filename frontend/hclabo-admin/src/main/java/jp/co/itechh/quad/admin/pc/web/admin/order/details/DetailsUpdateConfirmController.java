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
import jp.co.itechh.quad.admin.base.util.common.CollectionUtil;
import jp.co.itechh.quad.admin.base.util.common.DiffUtil;
import jp.co.itechh.quad.admin.base.utility.ConversionUtility;
import jp.co.itechh.quad.admin.constant.type.HTypeNoveltyPresentJudgmentStatus;
import jp.co.itechh.quad.admin.constant.type.HTypeProcessType;
import jp.co.itechh.quad.admin.constant.type.HTypeSettlementMethodType;
import jp.co.itechh.quad.admin.dto.goods.goods.GoodsDetailsDto;
import jp.co.itechh.quad.bill.presentation.api.BillApi;
import jp.co.itechh.quad.billingslip.presentation.api.BillingSlipApi;
import jp.co.itechh.quad.billingslip.presentation.api.param.BillingSlipForRevisionByTransactionRevisionIdResponse;
import jp.co.itechh.quad.billingslip.presentation.api.param.BillingSlipGetRequest;
import jp.co.itechh.quad.billingslip.presentation.api.param.BillingSlipResponse;
import jp.co.itechh.quad.billingslip.presentation.api.param.GetBillingSlipForRevisionByTransactionRevisionIdRequest;
import jp.co.itechh.quad.convenience.presentation.api.ConvenienceApi;
import jp.co.itechh.quad.convenience.presentation.api.param.ConvenienceListResponse;
import jp.co.itechh.quad.coupon.presentation.api.CouponApi;
import jp.co.itechh.quad.coupon.presentation.api.param.CouponResponse;
import jp.co.itechh.quad.coupon.presentation.api.param.CouponVersionNoRequest;
import jp.co.itechh.quad.customer.presentation.api.CustomerApi;
import jp.co.itechh.quad.customer.presentation.api.param.CustomerResponse;
import jp.co.itechh.quad.method.presentation.api.SettlementMethodApi;
import jp.co.itechh.quad.method.presentation.api.param.PaymentMethodResponse;
import jp.co.itechh.quad.mulpay.presentation.api.MulpayApi;
import jp.co.itechh.quad.mulpay.presentation.api.param.MulPayBillRequest;
import jp.co.itechh.quad.mulpay.presentation.api.param.MulPayBillResponse;
import jp.co.itechh.quad.novelty.presentation.api.OrderNoveltyApi;
import jp.co.itechh.quad.novelty.presentation.api.param.NoveltyProductAutomaticGrantRequest;
import jp.co.itechh.quad.orderreceived.presentation.api.OrderReceivedApi;
import jp.co.itechh.quad.orderreceived.presentation.api.param.GetOrderReceivedRequest;
import jp.co.itechh.quad.orderreceived.presentation.api.param.OrderReceivedResponse;
import jp.co.itechh.quad.orderslip.presentation.api.OrderSlipApi;
import jp.co.itechh.quad.orderslip.presentation.api.param.GetOrderSlipForRevisionRequest;
import jp.co.itechh.quad.orderslip.presentation.api.param.OrderItemRevisionResponse;
import jp.co.itechh.quad.orderslip.presentation.api.param.OrderSlipForRevisionResponse;
import jp.co.itechh.quad.orderslip.presentation.api.param.OrderSlipGetRequest;
import jp.co.itechh.quad.orderslip.presentation.api.param.OrderSlipResponse;
import jp.co.itechh.quad.orderslip.presentation.api.param.OrderSlipResponseItemList;
import jp.co.itechh.quad.product.presentation.api.ProductApi;
import jp.co.itechh.quad.product.presentation.api.param.PageInfoRequest;
import jp.co.itechh.quad.product.presentation.api.param.ProductDetailListGetRequest;
import jp.co.itechh.quad.product.presentation.api.param.ProductDetailListResponse;
import jp.co.itechh.quad.salesslip.presentation.api.SalesSlipApi;
import jp.co.itechh.quad.salesslip.presentation.api.param.GetSalesSlipForRevisionByTransactionRevisionIdRequest;
import jp.co.itechh.quad.salesslip.presentation.api.param.GetSalesSlipForRevisionByTransactionRevisionIdResponse;
import jp.co.itechh.quad.salesslip.presentation.api.param.SalesSlipGetRequest;
import jp.co.itechh.quad.salesslip.presentation.api.param.SalesSlipResponse;
import jp.co.itechh.quad.shippingslip.presentation.api.ShippingSlipApi;
import jp.co.itechh.quad.shippingslip.presentation.api.param.ShippingSlipForRevisionGetRequest;
import jp.co.itechh.quad.shippingslip.presentation.api.param.ShippingSlipGetRequest;
import jp.co.itechh.quad.shippingslip.presentation.api.param.ShippingSlipResponse;
import jp.co.itechh.quad.transaction.presentation.api.TransactionApi;
import jp.co.itechh.quad.transaction.presentation.api.param.ApplyOriginCommissionAndCarriageForRevisionRequest;
import jp.co.itechh.quad.transaction.presentation.api.param.CheckTransactionForRevisionRequest;
import jp.co.itechh.quad.transaction.presentation.api.param.GetTransactionForRevisionRequest;
import jp.co.itechh.quad.transaction.presentation.api.param.OpenTransactionReviseRequest;
import jp.co.itechh.quad.transaction.presentation.api.param.TransactionForRevisionResponse;
import org.apache.commons.lang3.ObjectUtils;
import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.util.CollectionUtils;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.SessionAttributes;
import org.springframework.web.bind.support.SessionStatus;
import org.springframework.web.client.HttpClientErrorException;
import org.springframework.web.client.HttpServerErrorException;
import org.springframework.web.context.request.RequestContextHolder;
import org.springframework.web.context.request.ServletRequestAttributes;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import javax.servlet.http.HttpServletRequest;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Objects;

/**
 * 受注修正確認 コントローラ<br/>
 *
 * @author yt23807
 */
@RequestMapping("/order/detailsupdate/confirm")
@Controller
@SessionAttributes({"detailsUpdateConfirmModel", "detailsUpdateCommonModel"})
@PreAuthorize("hasAnyAuthority('ORDER:8')")
public class DetailsUpdateConfirmController extends AbstractOrderDetailsController {

    /** ロガー */
    protected static final Logger LOGGER = LoggerFactory.getLogger(DetailsUpdateConfirmController.class);

    /** パラメータエラー */
    private static final String MSGCD_PARAM_ERROR = "AOX002201";

    /** 表示モード:「list」の場合 再検索 */
    private static final String FLASH_MD = "md";

    /** 表示モード(md):list 検索画面の再検索実行 */
    private static final String MODE_LIST = "list";

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

    /** helper */
    private final DetailsUpdateConfirmHelper detailsUpdateConfirmHelper;

    /** 変換Utility */
    private final ConversionUtility conversionUtility;

    /** ノベルティ商品自動付与Api */
    private final OrderNoveltyApi orderNoveltyApi;

    /** コンビニ */
    private final String PAY_TYPE_CVS = "3";

    /** コンストラクタ */
    @Autowired
    public DetailsUpdateConfirmController(CustomerApi customerApi,
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
                                          DetailsUpdateConfirmHelper detailsUpdateConfirmHelper,
                                          ConversionUtility conversionUtility,
                                          OrderNoveltyApi orderNoveltyApi) {
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
        this.detailsUpdateConfirmHelper = detailsUpdateConfirmHelper;
        this.conversionUtility = conversionUtility;
        this.orderNoveltyApi = orderNoveltyApi;
    }

    /**
     * 受注確認画面表示
     *
     * @param detailsUpdateConfirmModel
     * @param detailsUpdateCommonModel
     * @param redirectAttributes
     * @param model
     * @return 表示画面テンプレートパス
     */
    @GetMapping(value = "")
    @HEHandler(exception = AppLevelListException.class, returnView = "order/details/confirm")
    public String doLoadConfirm(DetailsUpdateConfirmModel detailsUpdateConfirmModel,
                                BindingResult error,
                                DetailsUpdateCommonModel detailsUpdateCommonModel,
                                RedirectAttributes redirectAttributes,
                                Model model) {

        // 実行前処理
        String check = illegalOperationCheck(true, detailsUpdateConfirmModel, detailsUpdateCommonModel,
                                             redirectAttributes, model
                                            );
        if (StringUtils.isNotEmpty(check)) {
            return check;
        }
        // ※フェーズ１で行っていた各種事前チェック（以下コメント）は、フェーズ２では不要と判断
        // -----------------------------------
        // ・ブラウザバックの場合、処理しない
        // ・直アクセスチェック
        // ・不正操作チェック
        // -----------------------------------
        // 【理由】マイクロサービス化によってDB登録元のセッション管理情報が少なくなり、
        //        その結果、先頭の「実行前処理」だけで十分なチェックが実施できるため

        // 画面モデルクリア
        clearModel(DetailsUpdateConfirmModel.class, detailsUpdateConfirmModel, model);

        // 画面モデル設定のエラー判定用フラグ
        boolean errorFlag = false;
        // 改訂情報を取得し、モデルに設定
        try {
            errorFlag = setupRevisedOrder(detailsUpdateCommonModel, detailsUpdateConfirmModel);
            if (errorFlag) {
                // setupPreRevisionOrderで改訂前情報を取得できなかった場合
                addMessage(MSGCD_PARAM_ERROR, redirectAttributes, model);
                return "redirect:/error";
            }
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

        // 改訂前情報を取得し、モデル（※）に設定
        // ※ 改訂情報を設定したモデルとは別インスタンス
        DetailsUpdateConfirmModel preRevisionTmpModel = new DetailsUpdateConfirmModel();
        try {
            errorFlag = setupPreRevisionOrder(detailsUpdateCommonModel, preRevisionTmpModel);
            if (errorFlag) {
                // setupPreRevisionOrderで改訂前情報を取得できなかった場合
                addMessage(MSGCD_PARAM_ERROR, redirectAttributes, model);
                return "redirect:/error";
            }
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

        List<OrderGoodsUpdateItem> newOrderGoodsUpdateItems =
                        detailsUpdateConfirmModel.getOrderReceiverItem().getOrderGoodsUpdateItems();
        List<OrderGoodsUpdateItem> oldOrderGoodsUpdateItems =
                        preRevisionTmpModel.getOrderReceiverItem().getOrderGoodsUpdateItems();
        if (newOrderGoodsUpdateItems.size() != oldOrderGoodsUpdateItems.size()) {
            int diffSize = newOrderGoodsUpdateItems.size() - oldOrderGoodsUpdateItems.size();
            if (diffSize > 0) {
                for (int i = 0; i < diffSize; i++) {
                    oldOrderGoodsUpdateItems.add(0, new OrderGoodsUpdateItem());
                }
            }
        }

        // 項目差分リストを取得し、モデルに設定
        List<String> diffList = DiffUtil.diff(detailsUpdateConfirmModel, preRevisionTmpModel);
        // 受注商品の変更箇所の表示スタイル設定
        List<String> diffListGoods = detailsUpdateConfirmHelper.getOrderItemGoodsDiff(
                        DetailsUpdateConfirmModel.class.getSimpleName(),
                        detailsUpdateConfirmModel.getOrderReceiverItem().getOrderGoodsUpdateItems(),
                        preRevisionTmpModel.getOrderReceiverItem().getOrderGoodsUpdateItems()
                                                                                     );
        diffList.addAll(diffListGoods);

        // 受注追加料金アイテムの変更箇所の表示スタイル設定
        List<String> diffListAdd = detailsUpdateConfirmHelper.getOrderAdditionalChargeDiff(
                        DetailsUpdateConfirmModel.class.getSimpleName(),
                        detailsUpdateConfirmModel.getOrderAdditionalChargeItems(),
                        preRevisionTmpModel.getOrderAdditionalChargeItems()
                                                                                          );
        diffList.addAll(diffListAdd);

        detailsUpdateConfirmModel.setDiffList(diffList);

        // 商品点数チェック
        // お届け希望日チェック

        // ※1 督促/期限切れメール送信フラグを退避
        // 受注修正時は決済マスタの督促/期限切れメールの値は参照しない

        // ※2 督促/期限切れメール送信フラグ退避の復元
        // 受注修正時は決済マスタの督促/期限切れメールの値は参照しない

        // 同一商品内に複数の税率が混在するかチェック

        // 受注修正確認画面表示
        return "order/details/confirm";
    }

    /**
     * 「戻る」ボタン押下時の処理<br/>
     *
     * @return 受注詳細修正ページ
     */
    @PostMapping(value = "/", params = "doCancel")
    public String doBack(DetailsUpdateCommonModel detailsUpdateCommonModel) {

        // 改訂用取引の改訂前手数料/送料の適用フラグ設定(初期化)
        ApplyOriginCommissionAndCarriageForRevisionRequest applyOriginCommissionAndCarriageForRevisionRequest =
                        new ApplyOriginCommissionAndCarriageForRevisionRequest();
        applyOriginCommissionAndCarriageForRevisionRequest.setTransactionRevisionId(
                        detailsUpdateCommonModel.getTransactionRevisionId());
        applyOriginCommissionAndCarriageForRevisionRequest.setOriginCommissionApplyFlag(false);
        applyOriginCommissionAndCarriageForRevisionRequest.setOriginCarriageApplyFlag(false);

        transactionApi.applyOriginCommissionAndCarriageForRevision(applyOriginCommissionAndCarriageForRevisionRequest);

        // 受注詳細修正ページへ遷移
        return "redirect:/order/detailsupdate/?md=confirm";
    }

    /**
     * 修正内容を反映する<br/>
     * 修正処理実行
     *
     * @param detailsUpdateConfirmModel
     * @param error
     * @param detailsUpdateCommonModel
     * @param redirectAttributes
     * @param sessionStatus
     * @param model
     * @return 受注詳細ページ
     */
    @PostMapping(value = "/", params = "doOnceUpdate")
    @HEHandler(exception = AppLevelListException.class, returnView = "order/details/confirm")
    public String doOnceUpdate(DetailsUpdateConfirmModel detailsUpdateConfirmModel,
                               BindingResult error,
                               DetailsUpdateCommonModel detailsUpdateCommonModel,
                               RedirectAttributes redirectAttributes,
                               SessionStatus sessionStatus,
                               Model model) {

        // 実行前処理
        String check = illegalOperationCheck(false, detailsUpdateConfirmModel, detailsUpdateCommonModel,
                                             redirectAttributes, model
                                            );
        if (StringUtils.isNotEmpty(check)) {
            return check;
        }

        try {
            // 改訂用取引全体をチェックする
            checkOrderRevisedWithApi(detailsUpdateCommonModel);
        } catch (HttpClientErrorException | HttpServerErrorException e) {
            LOGGER.error("例外処理が発生しました", e);
            // 確定ボタン押下後のチェック処理や更新処理用APIでエラーが発生した場合

            if (isExistsErrCode(e.getResponseBodyAsString(), EXCLUSIVE_CONTROL_ERR)) {
                createClientErrorMessage(e.getResponseBodyAsString(), error, redirectAttributes, model);
                return "redirect:/order/details/?orderCode=" + detailsUpdateCommonModel.getOrderCode() + "&from=order";
            }

            // アイテム名調整マップ
            Map<String, String> itemNameAdjust = new HashMap<>();
            handleError(e.getStatusCode(), e.getResponseBodyAsString(), error, itemNameAdjust);
            if (error.hasErrors()) {
                return "order/details/confirm";
            }
        }

        // 取引改訂を確定する パラメータ設定
        HttpServletRequest request = ((ServletRequestAttributes) Objects.requireNonNull(
                        RequestContextHolder.getRequestAttributes())).getRequest();
        this.transactionApi.getApiClient().setUserAgent(request.getHeader("User-Agent"));
        OpenTransactionReviseRequest openRequest = new OpenTransactionReviseRequest();
        openRequest.setTransactionRevisionId(detailsUpdateCommonModel.getTransactionRevisionId());
        openRequest.setProcessType(HTypeProcessType.CHANGE.getValue());
        openRequest.setInventorySettlementSkipFlag(Boolean.FALSE);
        // 【API呼び出し】
        try {
            // 取引改訂を確定する
            this.transactionApi.openTransactionRevise(openRequest);
        } catch (HttpClientErrorException ce) {
            LOGGER.error("例外処理が発生しました", ce);
            // 想定内のエラー（400番台）の場合
            createClientErrorMessage(ce.getResponseBodyAsString(), error, redirectAttributes, model);
            return "redirect:/order/details/?orderCode=" + detailsUpdateCommonModel.getOrderCode() + "&from=order";
        } catch (HttpServerErrorException se) {
            LOGGER.error("例外処理が発生しました", se);
            handleServerError(se.getResponseBodyAsString());
        }

        // 再検索フラグをセット
        redirectAttributes.addFlashAttribute(FLASH_MD, MODE_LIST);

        // ノベルティ商品自動付与
        if (HTypeNoveltyPresentJudgmentStatus.UNJUDGMENT.equals(
                        detailsUpdateConfirmModel.getNoveltyPresentJudgmentStatus())) {
            try {
                NoveltyProductAutomaticGrantRequest noveltyProductAutomaticGrantRequest =
                                new NoveltyProductAutomaticGrantRequest();
                noveltyProductAutomaticGrantRequest.setOrderCode(detailsUpdateConfirmModel.getOrderCode());
                orderNoveltyApi.execute(noveltyProductAutomaticGrantRequest);
                addMessage("ORDER-NOVELTY-001-I", redirectAttributes, model);
            } catch (HttpClientErrorException | HttpServerErrorException e) {
                LOGGER.warn(e.getMessage());
            }
        }

        // Modelをセッションより破棄
        sessionStatus.setComplete();
        // 受注詳細へ遷移
        return "redirect:/order/details/?orderCode=" + detailsUpdateCommonModel.getOrderCode();
    }

    /***********************************************************
     * 内部メソッド（バックエンド通信無し）
     ***********************************************************/
    /**
     * 不正操作チェック
     *
     * @param isLoad
     * @param detailsUpdateConfirmModel
     * @param redirectAttributes
     * @param model
     */
    protected String illegalOperationCheck(boolean isLoad,
                                           DetailsUpdateConfirmModel detailsUpdateConfirmModel,
                                           DetailsUpdateCommonModel detailsUpdateCommonModel,
                                           RedirectAttributes redirectAttributes,
                                           Model model) {
        // 受注番号が設定されているかチェック
        String orderCode = detailsUpdateCommonModel.getOrderCode();
        if (StringUtils.isEmpty(orderCode)) {
            addMessage(MSGCD_PARAM_ERROR, redirectAttributes, model);
            return "redirect:/error";
        }

        // 処理対象受注番号が想定と異なる場合
        if (!isLoad) {
            if (!StringUtils.equals(orderCode, detailsUpdateConfirmModel.getOrderCode())) {
                redirectAttributes.addFlashAttribute(FLASH_MD, MODE_LIST);
                addMessage(MSGCD_PARAM_ERROR, redirectAttributes, model);
                return "redirect:/order/";
            }
        }

        // 受注詳細修正確認画面　⇒　処理履歴詳細画面　⇒　ブラウザバックでの遷移時
        // if (detailsUpdateModel.isHistoryDetailsFlag()) {
        //     redirectAttributes.addFlashAttribute(DetailsUpdateModel.FLASH_ORDERCODE, detailsUpdateModel.getOrderCode());
        //     addMessage(MSGCD_PARAM_ERROR, redirectAttributes, model);
        //     return "redirect:/order/details/";
        // }
        // ---------------------------------
        // ⇒フェーズ２では上記チェック不要
        //   【詳細】
        //      フェーズ１では、上記の画面遷移を行うと　処理履歴詳細画面の表示内容でDB更新が走ってしまったらしいが、
        //      フェーズ２では、セッション情報を使ったDB更新は行わないため、本事象は発生しない
        //
        //      よって、こちらのチェックは不要と判断

        return "";
    }

    /***********************************************************
     * 内部メソッド（バックエンド通信ありーデータ取得系）
     ***********************************************************/
    /**
     * 改訂後受注情報取得<br/>
     * 取得したレスポンス情報がnullまたは空の場合はフラグを返し、呼び出し元でエラー画面にリダイレクトさせる
     *
     * @param orderCommonModel 受注修正共通モデル
     * @param confirmModel     受注修正確認モデル
     * @return true ... NG
     */
    protected boolean setupRevisedOrder(DetailsUpdateCommonModel orderCommonModel,
                                        DetailsUpdateConfirmModel confirmModel) {
        // 改訂後レスポンス保持モデルを生成
        DetailsUpdateConfirmModel.RevisedResponseTmpModel tmpModel =
                        new DetailsUpdateConfirmModel.RevisedResponseTmpModel();

        // =======================================================
        // API呼出 START
        // =======================================================

        // 受注・伝票リソース >>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>
        // 受注取得（画面表示用に必要）
        OrderReceivedResponse orderReceivedResponse = getOrderReceivedFromApi(orderCommonModel);
        tmpModel.setOrderReceivedResponse(orderReceivedResponse);

        // 改訂用取引取得
        TransactionForRevisionResponse transactionForRevisionResponse =
                        getTransactionForRevisionFromApi(orderCommonModel);
        tmpModel.setTransactionForRevisionResponse(transactionForRevisionResponse);

        // 改訂用注文票取得
        OrderSlipForRevisionResponse orderSlipForRevisionResponse = getOrderSlipForRevisionFromApi(orderCommonModel);
        tmpModel.setOrderSlipForRevisionResponse(orderSlipForRevisionResponse);

        // 改訂用販売伝票取得
        GetSalesSlipForRevisionByTransactionRevisionIdResponse salesSlipForRevisionResponse =
                        getSalesSlipForRevisionFromApi(orderCommonModel);
        tmpModel.setSalesSlipForRevisionResponse(salesSlipForRevisionResponse);

        // 改訂用配送伝票取得
        ShippingSlipResponse shippingSlipForRevisionResponse = getShippingSlipForRevisionFromApi(orderCommonModel);
        tmpModel.setShippingSlipForRevisionResponse(shippingSlipForRevisionResponse);

        // 改訂用請求伝票取得
        BillingSlipForRevisionByTransactionRevisionIdResponse billingSlipForRevisionResponse =
                        getBillingSlipForRevisionFromApi(orderCommonModel);
        tmpModel.setBillingSlipForRevisionResponse(billingSlipForRevisionResponse);

        // 各種改訂用伝票情報が取得できなかった場合は後続処理をスキップ
        if (ObjectUtils.isEmpty(orderReceivedResponse) || ObjectUtils.isEmpty(transactionForRevisionResponse)
            || ObjectUtils.isEmpty(orderSlipForRevisionResponse) || ObjectUtils.isEmpty(salesSlipForRevisionResponse)
            || ObjectUtils.isEmpty(shippingSlipForRevisionResponse) || ObjectUtils.isEmpty(
                        billingSlipForRevisionResponse)) {
            return true;
        }

        // マルチペイメント請求 >>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>
        MulPayBillResponse mulPayBillResponse = getMulPayBillFromApi(orderCommonModel);
        tmpModel.setMulPayBillResponse(mulPayBillResponse);

        // マスタリソース >>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>
        // 顧客情報取得
        CustomerResponse customerResponse = getCustomerFromApi(transactionForRevisionResponse);
        tmpModel.setCustomerResponse(customerResponse);

        // 顧客情報が取得できなかった場合は後続処理をスキップ
        if (ObjectUtils.isEmpty(customerResponse)) {
            return true;
        }

        // 顧客住所取得
        AddressBookAddressResponse customerAddressResponse = getCustomerAddressFromApi(customerResponse);
        tmpModel.setCustomerAddressResponse(customerAddressResponse);
        // お届け先住所情報を取得
        AddressBookAddressResponse shippingAddressResponse = getShippingAddressFromApi(shippingSlipForRevisionResponse);
        tmpModel.setShippingAddressResponse(shippingAddressResponse);
        // ご請求先住所情報を取得
        AddressBookAddressResponse billingAddressBookAddressResponse =
                        getBillingAddressFromApi(billingSlipForRevisionResponse);
        tmpModel.setBillingAddressResponse(billingAddressBookAddressResponse);

        // 決済方法取得
        PaymentMethodResponse paymentMethodResponse = getPaymentMethodFromApi(billingSlipForRevisionResponse);
        tmpModel.setPaymentMethodResponse(paymentMethodResponse);

        // 各種注文情報が取得できなかった場合は後続処理をスキップ
        if (ObjectUtils.isEmpty(customerAddressResponse) || ObjectUtils.isEmpty(shippingAddressResponse)
            || ObjectUtils.isEmpty(billingAddressBookAddressResponse) || ObjectUtils.isEmpty(paymentMethodResponse)) {
            return true;
        }

        // クーポン取得
        CouponResponse couponResponse = getCouponFromApi(salesSlipForRevisionResponse);
        if (detailsUpdateConfirmHelper.needCoupon(salesSlipForRevisionResponse) && ObjectUtils.isEmpty(
                        couponResponse)) {
            // 公開後のクーポン情報は削除不可だが、取得できない場合は後続処理をスキップ
            return true;
        }
        tmpModel.setCouponResponse(couponResponse);

        // 商品詳細情報取得
        List<GoodsDetailsDto> goodsDtoList = getProductDetailListFromApi(orderSlipForRevisionResponse);
        tmpModel.setGoodsDtoList(goodsDtoList);

        if (paymentMethodResponse != null && HTypeSettlementMethodType.LINK_PAYMENT.getValue()
                                                                                   .equals(paymentMethodResponse.getSettlementMethodType())) {
            if (ObjectUtils.isNotEmpty(billingSlipForRevisionResponse) && PAY_TYPE_CVS.equals(
                            billingSlipForRevisionResponse.getPayType())) {
                ConvenienceListResponse convenienceListResponse = getConveniListFromApi();
                tmpModel.setConvenienceListResponse(convenienceListResponse);
            }

        }

        // 商品情報が取得できなかった場合は後続処理をスキップ
        if (CollectionUtils.isEmpty(goodsDtoList)) {
            return true;
        }

        // 受注詳細情報ページ反映
        this.detailsUpdateConfirmHelper.toPageForRevision(confirmModel, tmpModel);
        return false;
    }

    /**
     * 改訂前受注情報取得<br/>
     * 取得したレスポンス情報がnullまたは空の場合はフラグを返し、呼び出し元でエラー画面にリダイレクトさせる
     *
     * @param orderCommonModel 受注修正共通モデル
     * @param confirmModel     受注修正確認モデル
     * @return true ... NG
     */
    private boolean setupPreRevisionOrder(DetailsUpdateCommonModel orderCommonModel,
                                          DetailsUpdateConfirmModel confirmModel) {
        // 改訂前レスポンス保持モデルを生成
        DetailsUpdateConfirmModel.PreRevisionResponseTmpModel tmpModel =
                        new DetailsUpdateConfirmModel.PreRevisionResponseTmpModel();

        // =======================================================
        // API呼出 START
        // =======================================================

        // 受注・伝票リソース >>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>
        // 取引（受注）取得
        OrderReceivedResponse orderReceivedResponse = getOrderReceivedFromApi(orderCommonModel);
        tmpModel.setOrderReceivedResponse(orderReceivedResponse);

        // 注文票取得
        OrderSlipResponse orderSlipResponse = getOrderSlipFromApi(orderCommonModel);
        tmpModel.setOrderSlipResponse(orderSlipResponse);

        // 販売伝票取得
        SalesSlipResponse salesSlipResponse = getSalesSlipFromApi(orderCommonModel);
        tmpModel.setSalesSlipResponse(salesSlipResponse);

        // 配送伝票取得
        ShippingSlipResponse shippingSlipResponse = getShippingSlipFromApi(orderCommonModel);
        tmpModel.setShippingSlipResponse(shippingSlipResponse);

        // 請求伝票取得
        BillingSlipResponse billingSlipResponse = getBillingSlipFromApi(orderCommonModel);
        tmpModel.setBillingSlipResponse(billingSlipResponse);

        // 各種伝票情報が取得できなかった場合は後続処理をスキップ
        if (ObjectUtils.isEmpty(orderReceivedResponse) || ObjectUtils.isEmpty(orderSlipResponse) || ObjectUtils.isEmpty(
                        salesSlipResponse) || ObjectUtils.isEmpty(shippingSlipResponse) || ObjectUtils.isEmpty(
                        billingSlipResponse)) {
            return true;
        }

        // マルチペイメント請求 >>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>
        MulPayBillResponse mulPayBillResponse = getMulPayBillFromApi(orderCommonModel);
        tmpModel.setMulPayBillResponse(mulPayBillResponse);

        // マスタリソース >>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>
        // 顧客情報取得
        CustomerResponse customerResponse = getCustomerFromApi(orderReceivedResponse);
        tmpModel.setCustomerResponse(customerResponse);

        // 顧客情報が取得できなかった場合は後続処理をスキップ
        if (ObjectUtils.isEmpty(customerResponse)) {
            return true;
        }

        // 顧客住所取得
        AddressBookAddressResponse customerAddressResponse = getCustomerAddressFromApi(customerResponse);
        tmpModel.setCustomerAddressResponse(customerAddressResponse);
        // お届け先住所情報を取得
        AddressBookAddressResponse shippingAddressResponse = getShippingAddressFromApi(shippingSlipResponse);
        tmpModel.setShippingAddressResponse(shippingAddressResponse);
        // ご請求先住所情報を取得
        AddressBookAddressResponse billingAddressBookAddressResponse = getBillingAddressFromApi(billingSlipResponse);
        tmpModel.setBillingAddressResponse(billingAddressBookAddressResponse);

        // 決済方法取得
        PaymentMethodResponse paymentMethodResponse = getPaymentMethodFromApi(billingSlipResponse);
        tmpModel.setPaymentMethodResponse(paymentMethodResponse);

        // 各種注文情報が取得できなかった場合は後続処理をスキップ
        if (ObjectUtils.isEmpty(customerAddressResponse) || ObjectUtils.isEmpty(shippingAddressResponse)
            || ObjectUtils.isEmpty(billingAddressBookAddressResponse) || ObjectUtils.isEmpty(paymentMethodResponse)) {
            return true;
        }

        // クーポン取得
        CouponResponse couponResponse = getCouponFromApi(salesSlipResponse);
        if (detailsUpdateConfirmHelper.needCoupon(salesSlipResponse) && ObjectUtils.isEmpty(couponResponse)) {
            // 公開後のクーポン情報は削除不可だが、取得できない場合は後続処理をスキップ
            return true;
        }
        tmpModel.setCouponResponse(couponResponse);

        if (paymentMethodResponse != null && HTypeSettlementMethodType.LINK_PAYMENT.getValue()
                                                                                   .equals(paymentMethodResponse.getSettlementMethodType())) {
            if (ObjectUtils.isNotEmpty(billingSlipResponse.getPaymentLinkResponse()) && PAY_TYPE_CVS.equals(
                            billingSlipResponse.getPaymentLinkResponse().getPayType())) {
                ConvenienceListResponse convenienceListResponse = getConveniListFromApi();
                tmpModel.setConvenienceListResponse(convenienceListResponse);
            }

        }

        // 商品詳細情報取得
        List<GoodsDetailsDto> goodsDtoList = getProductDetailListFromApi(orderSlipResponse);
        tmpModel.setGoodsDtoList(goodsDtoList);

        // 商品情報が取得できなかった場合は後続処理をスキップ
        if (CollectionUtils.isEmpty(goodsDtoList)) {
            return true;
        }

        // 受注詳細情報ページ反映
        this.detailsUpdateConfirmHelper.toPage(confirmModel, tmpModel);
        return false;
    }

    // ===============================================================
    // API呼出メソッド
    // ===============================================================

    /**
     * 改訂用取引取得
     *
     * @param orderCommonModel 受注修正共通モデル
     * @return 改訂用取引APIレスポンス
     */
    private TransactionForRevisionResponse getTransactionForRevisionFromApi(DetailsUpdateCommonModel orderCommonModel) {
        // 改訂用取引IDにひもづく改訂用取引を取得
        GetTransactionForRevisionRequest getTransactionForRevisionRequest = new GetTransactionForRevisionRequest();
        getTransactionForRevisionRequest.setTransactionRevisionId(orderCommonModel.getTransactionRevisionId());
        // 【API呼び出し】
        return transactionApi.getTransactionForRevision(getTransactionForRevisionRequest);
    }

    /**
     * 改訂用注文票取得
     *
     * @param orderCommonModel 受注修正共通モデル
     * @return 改訂用注文票APIレスポンス
     */
    private OrderSlipForRevisionResponse getOrderSlipForRevisionFromApi(DetailsUpdateCommonModel orderCommonModel) {
        // 改訂用取引IDにひもづく改訂用注文票を取得
        GetOrderSlipForRevisionRequest orderSlipSlipGetRequest = new GetOrderSlipForRevisionRequest();
        orderSlipSlipGetRequest.setTransactionRevisionId(orderCommonModel.getTransactionRevisionId());
        // 【API呼び出し】
        return orderSlipApi.getOrderSlipForRevision(orderSlipSlipGetRequest);
    }

    /**
     * 改訂用販売伝票取得
     *
     * @param orderCommonModel 受注修正共通モデル
     * @return 改訂用販売伝票APIレスポンス
     */
    private GetSalesSlipForRevisionByTransactionRevisionIdResponse getSalesSlipForRevisionFromApi(
                    DetailsUpdateCommonModel orderCommonModel) {
        // 改訂用取引IDにひもづく改訂用販売伝票を取得
        GetSalesSlipForRevisionByTransactionRevisionIdRequest salesSlipGetRequest =
                        new GetSalesSlipForRevisionByTransactionRevisionIdRequest();
        salesSlipGetRequest.setTransactionRevisionId(orderCommonModel.getTransactionRevisionId());
        // 【API呼び出し】
        return salesSlipApi.getSalesSlipForRevisionByTransactionRevisionId(salesSlipGetRequest);
    }

    /**
     * 改訂用配送伝票取得
     *
     * @param orderCommonModel 受注修正共通モデル
     * @return 改訂用配送伝票APIレスポンス
     */
    private ShippingSlipResponse getShippingSlipForRevisionFromApi(DetailsUpdateCommonModel orderCommonModel) {
        // 改訂用取引IDにひもづく改訂用配送伝票を取得
        ShippingSlipForRevisionGetRequest shippingSlipGetRequest = new ShippingSlipForRevisionGetRequest();
        shippingSlipGetRequest.setTransactionRevisionId(orderCommonModel.getTransactionRevisionId());
        // 【API呼び出し】
        return shippingSlipApi.getForRevisionByTransactionRevisionId(shippingSlipGetRequest);
    }

    /**
     * 改訂用請求伝票取得
     *
     * @param orderCommonModel 受注修正共通モデル
     * @return 改訂用請求伝票APIレスポンス
     */
    private BillingSlipForRevisionByTransactionRevisionIdResponse getBillingSlipForRevisionFromApi(
                    DetailsUpdateCommonModel orderCommonModel) {
        // 改訂用取引IDにひもづく改訂用請求伝票を取得
        GetBillingSlipForRevisionByTransactionRevisionIdRequest billingSlipGetRequest =
                        new GetBillingSlipForRevisionByTransactionRevisionIdRequest();
        billingSlipGetRequest.setTransactionRevisionId(orderCommonModel.getTransactionRevisionId());
        // 【API呼び出し】
        return billingSlipApi.getBillingSlipForRevisionByTransactionRevisionId(billingSlipGetRequest);
    }

    /**
     * マルチペイメント請求情報を取得
     *
     * @param orderCommonModel 受注修正共通モデル
     * @return マルチペイメント請求APIレスポンス
     */
    private MulPayBillResponse getMulPayBillFromApi(DetailsUpdateCommonModel orderCommonModel) {
        // 【API呼び出し】
        MulPayBillRequest mulPayBillRequest = new MulPayBillRequest();
        mulPayBillRequest.setOrderCode(orderCommonModel.getOrderCode());
        return mulpayApi.getByOrderCode(mulPayBillRequest);
    }

    /**
     * 顧客情報取得
     *
     * @param transactionResponse 改訂用取引レスポンス
     * @return 顧客APIレスポンス
     */
    private CustomerResponse getCustomerFromApi(TransactionForRevisionResponse transactionResponse) {
        // 顧客IDをキーに顧客情報を取得（顧客IDは、必ず・数値で　設定されている前提）
        Integer customerId = conversionUtility.toInteger(transactionResponse.getCustomerId());
        // 【API呼び出し】
        return customerApi.getByMemberinfoSeq(customerId);
    }

    /**
     * 顧客住所取得
     *
     * @param customerResponse 顧客情報レスポンス
     * @return 住所APIレスポンス
     */
    private AddressBookAddressResponse getCustomerAddressFromApi(CustomerResponse customerResponse) {
        // 顧客住所IDに値が設定されている場合のみ、住所情報を取得
        if (detailsUpdateConfirmHelper.needCustomerAddress(customerResponse)) {
            // 【API呼び出し】
            return addressBookApi.getAddressById(customerResponse.getMemberInfoAddressId());
        }
        return null;
    }

    /**
     * お届け先住所取得
     *
     * @param shippingSlipResponse 改訂用配送伝票レスポンス
     * @return 住所APIレスポンス
     */
    private AddressBookAddressResponse getShippingAddressFromApi(ShippingSlipResponse shippingSlipResponse) {
        // 配送先住所IDに値が設定されている場合のみ、住所情報を取得
        if (detailsUpdateConfirmHelper.needShippingAddress(shippingSlipResponse)) {
            // 【API呼び出し】
            return addressBookApi.getAddressById(shippingSlipResponse.getShippingAddressId());
        }
        return null;
    }

    /**
     * 請求先住所取得
     *
     * @param billingSlipResponse 改訂用請求伝票レスポンス
     * @return 住所APIレスポンス
     */
    private AddressBookAddressResponse getBillingAddressFromApi(BillingSlipForRevisionByTransactionRevisionIdResponse billingSlipResponse) {
        // 配送先住所IDに値が設定されている場合のみ、住所情報を取得
        if (detailsUpdateConfirmHelper.needBillingAddress(billingSlipResponse)) {
            // 【API呼び出し】
            return addressBookApi.getAddressById(billingSlipResponse.getBillingAddressId());
        }
        return null;
    }

    /**
     * 決済方法取得
     *
     * @param billingSlipResponse 改訂用請求伝票レスポンス
     * @return 決済方法APIレスポンス
     */
    private PaymentMethodResponse getPaymentMethodFromApi(BillingSlipForRevisionByTransactionRevisionIdResponse billingSlipResponse) {
        if (detailsUpdateConfirmHelper.needPaymentMethod(billingSlipResponse)) {
            // 【API呼び出し】
            return settlementMethodApi.getBySettlementMethodSeq(
                            Integer.parseInt(billingSlipResponse.getPaymentMethodId()));
        }
        return null;
    }

    /**
     * クーポン取得
     *
     * @param salesSlipResponse 改訂用販売伝票レスポンス
     * @return クーポンAPIレスポンス
     */
    private CouponResponse getCouponFromApi(GetSalesSlipForRevisionByTransactionRevisionIdResponse salesSlipResponse) {
        if (detailsUpdateConfirmHelper.needCoupon(salesSlipResponse)) {
            // クーポン情報を取得
            CouponVersionNoRequest couponVersionNoRequest = new CouponVersionNoRequest();
            couponVersionNoRequest.setCouponSeq(salesSlipResponse.getApplyCouponResponse().getCouponSeq());
            couponVersionNoRequest.setCouponVersionNo(salesSlipResponse.getApplyCouponResponse().getCouponVersionNo());
            // 【API呼び出し】
            return couponApi.getByCouponVersionNo(couponVersionNoRequest);
        }
        return null;
    }

    /**
     * 商品情報リスト取得
     *
     * @param orderSlipResponse 改訂用注文票レスポンス
     * @return 商品情報リストAPIレスポンス
     */
    private List<GoodsDetailsDto> getProductDetailListFromApi(OrderSlipForRevisionResponse orderSlipResponse) {
        if (detailsUpdateConfirmHelper.needProductDetailList(orderSlipResponse)) {
            ProductDetailListGetRequest productDetailListGetRequest = new ProductDetailListGetRequest();
            List<Integer> goodsSeqList = new ArrayList<>();
            if (CollectionUtil.isNotEmpty(orderSlipResponse.getOrderItemRevisionList())) {
                for (OrderItemRevisionResponse item : orderSlipResponse.getOrderItemRevisionList()) {
                    goodsSeqList.add(conversionUtility.toInteger(item.getItemId()));
                }
            }
            productDetailListGetRequest.setGoodsSeqList(goodsSeqList);
            // 【API呼び出し】
            ProductDetailListResponse productDetailListResponse =
                            productApi.getDetails(productDetailListGetRequest, null);

            // 商品情報が取得できなかった場合は後続処理をスキップし、呼び出し元でエラー
            if (ObjectUtils.isEmpty(productDetailListResponse) || CollectionUtils.isEmpty(
                            productDetailListResponse.getGoodsDetailsList())) {
                return null;
            }
            // ソートは不要（そのまま画面に表示するものでもないから
            // List<GoodsDetailsResponse> goodsDetailsResponseList = productDetailListResponse.getGoodsDetailsList();
            // goodsDetailsResponseList.sort(Comparator.comparing(item -> idList.indexOf(item.getGoodsSeq())));

            return detailsUpdateConfirmHelper.toProductDetailList(productDetailListResponse, orderSlipResponse);
        }
        return null;
    }

    /**
     * 改訂前取引-受注取得
     *
     * @param orderCommonModel 受注修正共通モデル
     * @return 改訂前取引-受注APIレスポンス
     */
    private OrderReceivedResponse getOrderReceivedFromApi(DetailsUpdateCommonModel orderCommonModel) {
        // 取引IDにひもづく改訂前取引-受注を取得
        GetOrderReceivedRequest getOrderReceivedRequest = new GetOrderReceivedRequest();
        getOrderReceivedRequest.setTransactionId(orderCommonModel.getTransactionId());
        // 【API呼び出し】
        return orderReceivedApi.get(getOrderReceivedRequest);
    }

    /**
     * 改訂前注文票取得
     *
     * @param orderCommonModel 受注修正共通モデル
     * @return 改訂前注文票APIレスポンス
     */
    private OrderSlipResponse getOrderSlipFromApi(DetailsUpdateCommonModel orderCommonModel) {
        // 改訂前取引IDにひもづく改訂前注文票を取得
        OrderSlipGetRequest orderSlipSlipGetRequest = new OrderSlipGetRequest();
        orderSlipSlipGetRequest.setTransactionId(orderCommonModel.getTransactionId());
        // 【API呼び出し】
        return orderSlipApi.get(orderSlipSlipGetRequest);
    }

    /**
     * 改訂前販売伝票取得
     *
     * @param orderCommonModel 受注修正共通モデル
     * @return 改訂前販売伝票APIレスポンス
     */
    private SalesSlipResponse getSalesSlipFromApi(DetailsUpdateCommonModel orderCommonModel) {
        // 改訂前取引IDにひもづく改訂前販売伝票を取得
        SalesSlipGetRequest salesSlipGetRequest = new SalesSlipGetRequest();
        salesSlipGetRequest.setTransactionId(orderCommonModel.getTransactionId());
        // 【API呼び出し】
        return salesSlipApi.get(salesSlipGetRequest);
    }

    /**
     * 改訂前配送伝票取得
     *
     * @param orderCommonModel 受注修正共通モデル
     * @return 改訂前配送伝票APIレスポンス
     */
    private ShippingSlipResponse getShippingSlipFromApi(DetailsUpdateCommonModel orderCommonModel) {
        // 改訂前取引IDにひもづく改訂前配送伝票を取得
        ShippingSlipGetRequest shippingSlipGetRequest = new ShippingSlipGetRequest();
        shippingSlipGetRequest.setTransactionId(orderCommonModel.getTransactionId());
        // 【API呼び出し】
        return shippingSlipApi.get(shippingSlipGetRequest);
    }

    /**
     * 改訂前請求伝票取得
     *
     * @param orderCommonModel 受注修正共通モデル
     * @return 改訂前請求伝票APIレスポンス
     */
    private BillingSlipResponse getBillingSlipFromApi(DetailsUpdateCommonModel orderCommonModel) {
        // 改訂前取引IDにひもづく改訂前請求伝票を取得
        BillingSlipGetRequest billingSlipGetRequest = new BillingSlipGetRequest();
        billingSlipGetRequest.setTransactionId(orderCommonModel.getTransactionId());
        // 【API呼び出し】
        return billingSlipApi.get(billingSlipGetRequest);
    }

    /**
     * 顧客情報取得
     *
     * @param orderReceivedResponse 改訂元受注レスポンス
     * @return 顧客APIレスポンス
     */
    private CustomerResponse getCustomerFromApi(OrderReceivedResponse orderReceivedResponse) {
        // 顧客IDをキーに顧客情報を取得（顧客IDは、必ず・数値で　設定されている前提）
        Integer customerId = conversionUtility.toInteger(orderReceivedResponse.getCustomerId());
        // 【API呼び出し】
        return customerApi.getByMemberinfoSeq(customerId);
    }

    /**
     * コンビニ情報取得
     *
     * @return コンビニ一覧APIレスポンス
     */
    private ConvenienceListResponse getConveniListFromApi() {
        // 【API呼び出し】
        return convenienceApi.get();
    }

    // /**
    //  * お届け先住所取得
    //  *
    //  * @param shippingSlipResponse 改訂用配送伝票レスポンス
    //  * @return お届け先住所APIレスポンス
    //  */
    // private AddressBookAddressResponse getShippingAddressFromApi(ShippingSlipResponse shippingSlipResponse) {
    //     // 配送先住所IDに値が設定されている場合のみ、住所情報を取得
    //     if (detailsUpdateConfirmHelper.needShippingAddress(shippingSlipResponse)) {
    //         // 【API呼び出し】
    //         return addressBookApi.getAddressById(shippingSlipResponse.getShippingAddressId());
    //     }
    //     return null;
    // }

    /**
     * 請求先住所取得
     *
     * @param billingSlipResponse 請求伝票レスポンス
     * @return 請求先住所APIレスポンス
     */
    private AddressBookAddressResponse getBillingAddressFromApi(BillingSlipResponse billingSlipResponse) {
        // 配送先住所IDに値が設定されている場合のみ、住所情報を取得
        if (detailsUpdateConfirmHelper.needBillingAddress(billingSlipResponse)) {
            // 【API呼び出し】
            return addressBookApi.getAddressById(billingSlipResponse.getBillingAddressId());
        }
        return null;
    }

    /**
     * 決済方法取得
     *
     * @param billingSlipResponse 改訂用請求伝票レスポンス
     * @return 決済方法APIレスポンス
     */
    private PaymentMethodResponse getPaymentMethodFromApi(BillingSlipResponse billingSlipResponse) {
        if (detailsUpdateConfirmHelper.needPaymentMethod(billingSlipResponse)) {
            // 【API呼び出し】
            return settlementMethodApi.getBySettlementMethodSeq(
                            Integer.parseInt(billingSlipResponse.getPaymentMethodId()));
        }
        return null;
    }

    /**
     * クーポン取得
     *
     * @param salesSlipResponse 販売伝票レスポンス
     * @return クーポンAPIレスポンス
     */
    private CouponResponse getCouponFromApi(SalesSlipResponse salesSlipResponse) {
        if (detailsUpdateConfirmHelper.needCoupon(salesSlipResponse)) {
            // クーポン情報を取得
            CouponVersionNoRequest couponVersionNoRequest = new CouponVersionNoRequest();
            couponVersionNoRequest.setCouponSeq(salesSlipResponse.getCouponSeq());
            couponVersionNoRequest.setCouponVersionNo(salesSlipResponse.getCouponVersionNo());
            // 【API呼び出し】
            return couponApi.getByCouponVersionNo(couponVersionNoRequest);
        }
        return null;
    }

    /**
     * 商品情報リスト取得
     *
     * @param orderSlipResponse 注文票レスポンス
     * @return 商品情報リストAPIレスポンス
     */
    private List<GoodsDetailsDto> getProductDetailListFromApi(OrderSlipResponse orderSlipResponse) {

        if (detailsUpdateConfirmHelper.needProductDetailList(orderSlipResponse)) {

            ProductDetailListGetRequest productDetailListGetRequest = new ProductDetailListGetRequest();
            List<Integer> goodsSeqList = new ArrayList<>();
            if (CollectionUtil.isNotEmpty(orderSlipResponse.getItemList())) {
                for (OrderSlipResponseItemList item : orderSlipResponse.getItemList()) {
                    goodsSeqList.add(conversionUtility.toInteger(item.getItemId()));
                }
            }
            productDetailListGetRequest.setGoodsSeqList(goodsSeqList);

            PageInfoRequest pageInfoRequest = new PageInfoRequest();
            // 【API呼び出し】
            ProductDetailListResponse productDetailListResponse =
                            productApi.getDetails(productDetailListGetRequest, pageInfoRequest);

            // 商品情報が取得できなかった場合は後続処理をスキップし、呼び出し元でエラー
            if (ObjectUtils.isEmpty(productDetailListResponse) || CollectionUtils.isEmpty(
                            productDetailListResponse.getGoodsDetailsList())) {
                return null;
            }
            return detailsUpdateConfirmHelper.toProductDetailList(productDetailListResponse, orderSlipResponse);
        }
        return null;
    }

    /***********************************************************
     * 内部メソッド（バックエンド通信ありーデータ取得系）
     ***********************************************************/
    /**
     * 改訂用取引全体をチェックする<br/>
     *
     * @param detailsUpdateCommonModel
     */
    private void checkOrderRevisedWithApi(DetailsUpdateCommonModel detailsUpdateCommonModel) {
        // パラメータ設定
        CheckTransactionForRevisionRequest checkRequest = new CheckTransactionForRevisionRequest();
        checkRequest.setTransactionRevisionId(detailsUpdateCommonModel.getTransactionRevisionId());
        checkRequest.setContractConfirmFlag(true);
        // 【API呼び出し】
        this.transactionApi.checkTransactionForRevision(checkRequest);
    }

}