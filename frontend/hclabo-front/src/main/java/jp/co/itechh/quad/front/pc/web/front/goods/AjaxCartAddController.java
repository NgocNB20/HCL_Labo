/*
 * Copyright (C) 2022 i-TEC HANKYU HANSHIN INC. All Rights Reserved.
 *
 */

package jp.co.itechh.quad.front.pc.web.front.goods;

import jp.co.itechh.quad.front.base.application.AppLevelFacesMessage;
import jp.co.itechh.quad.front.base.application.HmMessages;
import jp.co.itechh.quad.front.base.utility.ApplicationContextUtility;
import jp.co.itechh.quad.front.base.utility.ConversionUtility;
import jp.co.itechh.quad.front.base.utility.NumberUtility;
import jp.co.itechh.quad.front.dto.cart.CartDto;
import jp.co.itechh.quad.front.dto.cart.ajax.CartResponseDto;
import jp.co.itechh.quad.front.dto.common.CheckMessageDto;
import jp.co.itechh.quad.front.pc.web.front.cart.CartModel;
import jp.co.itechh.quad.front.service.common.impl.HmFrontUserDetailsServiceImpl;
import jp.co.itechh.quad.front.utility.CommonInfoUtility;
import jp.co.itechh.quad.front.validator.HNumberValidator;
import jp.co.itechh.quad.front.web.AbstractController;
import jp.co.itechh.quad.front.web.HeaderParamsHelper;
import jp.co.itechh.quad.orderslip.presentation.api.OrderSlipApi;
import jp.co.itechh.quad.orderslip.presentation.api.param.ClientErrorResponse;
import jp.co.itechh.quad.orderslip.presentation.api.param.ErrorContent;
import jp.co.itechh.quad.orderslip.presentation.api.param.OrderItemRegistRequest;
import jp.co.itechh.quad.orderslip.presentation.api.param.OrderSlipResponse;
import org.apache.commons.lang.StringUtils;
import org.apache.commons.lang3.ObjectUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.client.HttpClientErrorException;
import org.springframework.web.client.HttpServerErrorException;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

/**
 * カート追加（Ajax）Controllerクラス<br/>
 *
 * @author Pham Quang Dieu
 */
@RestController
@RequestMapping("/")
public class AjaxCartAddController extends AbstractController {

    /** ロガー */
    private static final Logger LOGGER = LoggerFactory.getLogger(AjaxCartAddController.class);

    /** カート追加（Ajax）Helperクラス */
    private final AjaxCartAddJSONHelper jsonHelper;

    /** ヘッダパラメーターヘルパー */
    private final HeaderParamsHelper headerParamsHelper;

    /** ユーザー詳細サービス */
    private final HmFrontUserDetailsServiceImpl userDetailsService;

    /** 注文票API */
    private final OrderSlipApi orderSlipApi;

    /** 変換Helper */
    private final ConversionUtility conversionUtility;

    /** 共通情報Utility */
    private final CommonInfoUtility commonInfoUtility;

    /** バリデーションエラーメッセージ */
    protected static final String REQUIRED_MESSAGE_ID =
                    "jp.co.hankyuhanshin.itec.hmbase.validator.HRequiredValidator.INPUT_detail";
    protected static final String NUMBER_MESSAGE_ID =
                    "jp.co.hankyuhanshin.itec.hmbase.validator.HNumberValidator.NOT_NUMBER_detail";
    protected static final String FRACTION_MESSAGE_ID_MAX_ZERO =
                    "jp.co.hankyuhanshin.itec.hmbase.validator.HNumberLengthValidator.FRACTION_MAX_ZERO_detail";
    protected static final String NOT_IN_RANGE_MESSAGE_ID =
                    "jp.co.hankyuhanshin.itec.hmbase.validator.HNumberRangeValidator.NOT_IN_RANGE_detail";
    protected static final String MSGCD_GOODS_NOT_FOUND_ERROR = "AGX000201";

    /** グローバルエラーメッセージ用フィールド名 */
    public static final String GLOBAL_MESSAGE_FIELD_NAME = "common";

    /**
     * コンストラクタ
     * @param jsonHelper
     * @param orderSlipApi
     * @param conversionUtility
     */
    @Autowired
    public AjaxCartAddController(AjaxCartAddJSONHelper jsonHelper,
                                 HeaderParamsHelper headerParamsHelper,
                                 HmFrontUserDetailsServiceImpl userDetailsService,
                                 OrderSlipApi orderSlipApi,
                                 ConversionUtility conversionUtility,
                                 CommonInfoUtility commonInfoUtility) {
        this.jsonHelper = jsonHelper;
        this.headerParamsHelper = headerParamsHelper;
        this.userDetailsService = userDetailsService;
        this.orderSlipApi = orderSlipApi;
        this.conversionUtility = conversionUtility;
        this.commonInfoUtility = commonInfoUtility;
    }

    /**
     * カート画面へ遷移せずにカート追加する(Ajax通信)<br/>
     * @param cartAddModel
     * @return カート追加結果(CartResponseDto)
     */
    @GetMapping("/ajaxCartAdd")
    @ResponseBody
    public CartResponseDto ajaxCartAdd(AjaxCartAddModel cartAddModel) {

        // セッション切れ判定
        boolean okSession = preDoAjax();
        if (!okSession) {
            return jsonHelper.toCartIFResponse(
                            new ArrayList<CheckMessageDto>(), new ArrayList<CheckMessageDto>(), null, okSession);
        }

        // パラメータチェック
        List<CheckMessageDto> validatorErrorList = paramCheck(cartAddModel);

        List<CheckMessageDto> errorList = new ArrayList<>();
        if (validatorErrorList != null && validatorErrorList.isEmpty()) {
            // 未ログインの場合、顧客IDを取得する
            if (!this.commonInfoUtility.isLogin(getCommonInfo())) {
                // 顧客IDを取得し、APIヘッダーにセット
                // ※顧客ID未取得の場合は、このタイミングで新規でIDを払い出す
                String customerId = this.userDetailsService.getOrCreateCustomerId();
                headerParamsHelper.setMemberSeq(customerId);
            }

            // 注文票登録APIのリクエストを生成
            OrderItemRegistRequest orderItemRegistRequest = new OrderItemRegistRequest();
            orderItemRegistRequest.setItemId(cartAddModel.getGseq());
            orderItemRegistRequest.setItemCount(Integer.valueOf(cartAddModel.getGcnt()));

            try {
                // カート商品追加サービスの実行
                orderSlipApi.registOrderItem(
                                orderItemRegistRequest, conversionUtility.toDate(
                                                getCommonInfo().getCommonInfoUser().getMemberInfoBirthday()));
            }
            // サーバーエラーの場合、エラー画面へ遷移
            catch (HttpServerErrorException se) {
                LOGGER.error("例外処理が発生しました", se);
                handleServerError(se.getResponseBodyAsString());
            }
            // クライアントエラーの場合、エラーメッセージを取得する
            catch (HttpClientErrorException ce) {
                LOGGER.error("例外処理が発生しました", ce);
                ConversionUtility conversionUtility = ApplicationContextUtility.getBean(ConversionUtility.class);
                ClientErrorResponse clientError =
                                conversionUtility.toObject(ce.getResponseBodyAsString(), ClientErrorResponse.class);
                afterAddProcessApi(errorList, cartAddModel, clientError.getMessages());
            }
        }

        // カート商品リスト取得サービスの実行
        CartDto cartDto = new CartDto();
        cartDto.setGoodsTotalCount(getCartSumCount());

        // レスポンスの作成
        return jsonHelper.toCartIFResponse(validatorErrorList, errorList, cartDto, okSession);
    }

    /**
     * 注文商品数量を取得
     *
     */
    public BigDecimal getCartSumCount() {
        // カート商品数量（初期値ゼロ）
        BigDecimal cartGoodsSumCount = BigDecimal.ZERO;
        OrderSlipApi orderSlipApi = ApplicationContextUtility.getBean(OrderSlipApi.class);
        // 下書き注文票取得
        OrderSlipResponse orderSlipResponse = orderSlipApi.getDraft(null);
        if (!ObjectUtils.isEmpty(orderSlipResponse)) {
            // 注文商品数量を取得
            cartGoodsSumCount = new BigDecimal(orderSlipResponse.getTotalItemCount());
        }
        return cartGoodsSumCount;
    }

    /**
     * セッション切れ判定
     * @return セッションがただしいか true:正しい / false:不正
     */
    protected boolean preDoAjax() {
        if (null == this.getCommonInfo()) {
            return false;
        }
        return true;
    }

    /**
     * パラメータ―チェック処理
     * @param cartAddModel
     * @return バリデータエラーメッセージリスト
     */
    protected List<CheckMessageDto> paramCheck(AjaxCartAddModel cartAddModel) {
        List<CheckMessageDto> validatorErrorList = new ArrayList<>();

        /* 商品コード バリデータ */
        if (StringUtils.isEmpty(cartAddModel.getGseq()) || StringUtils.isEmpty(
                        cartAddModel.getGseq().replaceAll("^[\\s　]*", "").replaceAll("[\\s　]*$", ""))) {
            // 必須エラー
            validatorErrorList.add(toCheckMessageDto(MSGCD_GOODS_NOT_FOUND_ERROR, null, true));
        }

        /* 数量 バリデータ */
        // 数値関連Helper取得
        NumberUtility numberUtility = ApplicationContextUtility.getBean(NumberUtility.class);

        if (StringUtils.isEmpty(cartAddModel.getGcnt()) || StringUtils.isEmpty(
                        cartAddModel.getGcnt().replaceAll("^[\\s　]*", "").replaceAll("[\\s　]*$", ""))) {
            // 必須エラー
            validatorErrorList.add(toCheckMessageDto(REQUIRED_MESSAGE_ID, new String[] {"数量"}, true));
        } else if (!numberUtility.isNumber(cartAddModel.getGcnt())) {
            // 数値エラー
            validatorErrorList.add(toCheckMessageDto(NUMBER_MESSAGE_ID, new String[] {"数量"}, true));
        } else if (cartAddModel.getGcnt().matches("\\A[-][0-9]+\\z")) {
            // 数値エラー（正規表現 負の数）
            validatorErrorList.add(
                            toCheckMessageDto(HNumberValidator.MINUS_NUMBER_MESSAGE_ID, new String[] {"数量"}, true));
        } else if (cartAddModel.getGcnt().matches("\\A[-]?[0-9]+\\.[0-9]+\\z")) {
            // 数値エラー（正規表現 整数以外）
            validatorErrorList.add(toCheckMessageDto(FRACTION_MESSAGE_ID_MAX_ZERO, new String[] {"数量"}, true));
        } else if (!cartAddModel.getGcnt().matches("\\A[0-9]+\\z")) {
            // 数値エラー（正規表現）
            validatorErrorList.add(toCheckMessageDto(NUMBER_MESSAGE_ID, new String[] {"数量"}, true));
        } else if (Integer.valueOf(cartAddModel.getGcnt()).intValue() < 1
                   || Integer.valueOf(cartAddModel.getGcnt()).intValue() > 9999) {
            // 数値範囲エラー
            validatorErrorList.add(toCheckMessageDto(NOT_IN_RANGE_MESSAGE_ID, new String[] {"1", "9999", "数量"}, true));
        }

        return validatorErrorList;
    }

    /**
     * カート商品追加サービスの実行後の処理
     * @param errorList カート商品追加サービスの実行に返されるエラーメッセージ
     * @param cartAddModel
     */
    protected void afterAddProcess(List<CheckMessageDto> errorList, AjaxCartAddModel cartAddModel) {
        if (errorList != null && !errorList.isEmpty()) {
            // カート投入失敗
            for (int i = 0; i < errorList.size(); i++) {
                // エラーメッセージを整形
                CheckMessageDto errorMessage = errorList.get(i);
                errorMessage = toCheckMessageDto(errorMessage.getMessageId(), errorMessage.getArgs(),
                                                 errorMessage.isError()
                                                );
                errorList.set(i, errorMessage);
            }
        }
    }

    /**
     * エラー内容からメッセージリスト作成<br/>
     *
     * @param msgCode メッセージコード
     * @param args パラメータ
     * @param isError エラーフラグ
     * @return エラーメッセージDTO
     */
    protected CheckMessageDto toCheckMessageDto(String msgCode, Object[] args, boolean isError) {
        CheckMessageDto checkMessageDto = ApplicationContextUtility.getBean(CheckMessageDto.class);
        checkMessageDto.setMessageId(msgCode);
        checkMessageDto.setMessage(getMessage(msgCode, args));
        checkMessageDto.setArgs(args);
        checkMessageDto.setError(isError);
        return checkMessageDto;
    }

    /**
     * エラー内容からメッセージリスト作成<br/>
     *
     * @param msgCode メッセージコード
     * @param msg メッセージ
     * @param args パラメータ
     * @param isError エラーフラグ
     * @return エラーメッセージDTO
     */
    protected CheckMessageDto toCheckMessageDto(String msgCode, String msg, Object[] args, boolean isError) {
        CheckMessageDto checkMessageDto = ApplicationContextUtility.getBean(CheckMessageDto.class);
        checkMessageDto.setMessageId(msgCode);
        checkMessageDto.setMessage(msg);
        checkMessageDto.setArgs(args);
        checkMessageDto.setError(isError);
        return checkMessageDto;
    }

    /**
     * カート商品追加サービスの実行後の処理（API用）
     * @param errorList カート商品追加サービスの実行に返されるエラーメッセージ
     * @param cartAddModel
     * @param errorMap エラーメッセージマップ
     */
    protected void afterAddProcessApi(List<CheckMessageDto> errorList,
                                      AjaxCartAddModel cartAddModel,
                                      Map<String, List<ErrorContent>> errorMap) {

        List<ErrorContent> errorMsgList = errorMap.get(cartAddModel.getGseq());

        if (errorMsgList != null && !errorMsgList.isEmpty()) {
            // カート投入失敗
            for (ErrorContent errorContent : errorMsgList) {
                String code = errorContent.getCode();

                // @see CartController#addErrorInfo
                // エラーメッセージのねじかえ
                // ※バックエンドからはカート画面備考欄用のメッセージが返却されるため、
                //   メッセージをねじかえる必要がある
                if (code.indexOf(CartModel.MSGCD_OPEN_STATUS_HIKOUKAI) != -1
                    || code.indexOf(CartModel.MSGCD_OPEN_BEFORE) != -1
                    || code.indexOf(CartModel.MSGCD_OPEN_END) != -1) {
                    // 商品状態が非公開、公開中止、公開前、公開終了のいずれか場合
                    errorList.add(toCheckMessageDtoApi("AGX000201E"));
                } else if (code.indexOf(CartModel.MSGCD_SALE_STATUS_HIHANBAI) != -1
                           || code.indexOf(CartModel.MSGCD_SALE_BEFORE) != -1
                           || code.indexOf(CartModel.MSGCD_SALE_END) != -1) {
                    // 商品状態が非販売、販売前、販売終了のいずれか場合
                    errorList.add(toCheckMessageDtoApi("AGX000201E"));
                } else if (code.indexOf(CartModel.MSGCD_NO_STOCK) != -1) {
                    // 商品状態が在庫切れの場合
                    errorList.add(toCheckMessageDtoApi("AGX000205W"));
                } else if (code.indexOf(CartModel.MSGCD_LESS_STOCK) != -1) {
                    // 商品状態が在庫不足の場合
                    errorList.add(toCheckMessageDtoApi("AGX000213W"));
                } else if (code.indexOf(CartModel.MSGCD_ALCOHOL_CANNOT_BE_PURCHASED) != -1) {
                    // 酒類購入不可エラー
                    errorList.add(toCheckMessageDtoApi("AGX000214W"));
                } else {
                    // 上記以外の場合
                    errorList.add(toCheckMessageDtoApi("AGX000215W"));
                }
            }
        }
        addGlobalErrorInfo(errorList, errorMap);
    }

    /**
     * グローバルエラー
     *
     * @param errorList カート商品追加サービスの実行に返されるエラーメッセージ
     * @param errorMap エラーメッセージマップ
     */
    protected void addGlobalErrorInfo(List<CheckMessageDto> errorList, Map<String, List<ErrorContent>> errorMap) {

        if (errorMap == null || errorMap.isEmpty()) {
            return;
        }

        List<ErrorContent> checkMessageDtoList = errorMap.get(GLOBAL_MESSAGE_FIELD_NAME);
        if (checkMessageDtoList == null) {
            return;
        }

        for (ErrorContent errorContent : checkMessageDtoList) {
            // グローバルエラー追加
            errorList.add(toCheckMessageDto(errorContent.getCode(), errorContent.getMessage(), null, true));
        }
    }

    /**
     * エラー内容からメッセージリスト作成（API用）
     *
     * @param msgCode メッセージコード
     * @return エラーメッセージDTO
     */
    protected CheckMessageDto toCheckMessageDtoApi(String msgCode) {
        return toCheckMessageDto(msgCode, null, true);
    }

}
