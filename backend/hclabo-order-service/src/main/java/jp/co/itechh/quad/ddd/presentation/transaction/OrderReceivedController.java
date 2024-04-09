/*
 * Copyright (C) 2022 i-TEC HANKYU HANSHIN INC. All Rights Reserved.
 */

package jp.co.itechh.quad.ddd.presentation.transaction;

import io.swagger.annotations.ApiParam;
import jp.co.itechh.quad.core.base.utility.ApplicationContextUtility;
import jp.co.itechh.quad.core.base.utility.ConversionUtility;
import jp.co.itechh.quad.core.web.AbstractController;
import jp.co.itechh.quad.core.web.HeaderParamsUtility;
import jp.co.itechh.quad.ddd.usecase.transaction.GetCustomerOrderHistoryListUseCase;
import jp.co.itechh.quad.ddd.usecase.transaction.GetOrderReceivedByOrderCodeUseCase;
import jp.co.itechh.quad.ddd.usecase.transaction.GetOrderReceivedByTransactionIdUseCase;
import jp.co.itechh.quad.ddd.usecase.transaction.GetOrderReceivedByTransactionIdUseCaseDto;
import jp.co.itechh.quad.ddd.usecase.transaction.GetOrderReceivedCountByCustomerIdUseCase;
import jp.co.itechh.quad.ddd.usecase.transaction.GetOrderReceivedUseCase;
import jp.co.itechh.quad.ddd.usecase.transaction.GetOrderReceivedUseCaseDto;
import jp.co.itechh.quad.ddd.usecase.transaction.ReNumberingOrderCodeUseCase;
import jp.co.itechh.quad.ddd.usecase.transaction.query.CustomerOrderHistoryModel;
import jp.co.itechh.quad.ddd.usecase.transaction.query.IGetTransactionForExpiredSessionQuery;
import jp.co.itechh.quad.orderreceived.presentation.api.OrderApi;
import jp.co.itechh.quad.orderreceived.presentation.api.param.CustomerHistoryListResponse;
import jp.co.itechh.quad.orderreceived.presentation.api.param.GetOrderReceivedCountRequest;
import jp.co.itechh.quad.orderreceived.presentation.api.param.GetOrderReceivedRequest;
import jp.co.itechh.quad.orderreceived.presentation.api.param.OrderCodeReNumberingRequest;
import jp.co.itechh.quad.orderreceived.presentation.api.param.OrderCodeReNumberingResponse;
import jp.co.itechh.quad.orderreceived.presentation.api.param.OrderReceivedCountResponse;
import jp.co.itechh.quad.orderreceived.presentation.api.param.OrderReceivedResponse;
import jp.co.itechh.quad.orderreceived.presentation.api.param.PageInfoRequest;
import jp.co.itechh.quad.orderreceived.presentation.api.param.TransactionIdResponse;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RestController;

import javax.validation.Valid;
import javax.validation.constraints.NotNull;

/**
 * 受注エンドポイント Controller
 *
 * @author yt23807
 */
@RestController
public class OrderReceivedController extends AbstractController implements OrderApi {

    /** 受顧客注文履歴一覧取得 ユースケース */
    private final GetCustomerOrderHistoryListUseCase getCustomerOrderHistoryListUseCase;

    /** 受注を取得する ユースケース */
    private final GetOrderReceivedUseCase getOrderReceivedUseCase;

    /** 受注番号に紐づく受注取得ユースケース */
    private final GetOrderReceivedByOrderCodeUseCase getOrderReceivedByOrderCodeUseCase;

    /** 取引にひもづく受注を取得する ユースケース */
    private final GetOrderReceivedByTransactionIdUseCase getOrderReceivedByTransactionIdUseCase;

    /** 顧客の受注件数を取得する ユースケース */
    private final GetOrderReceivedCountByCustomerIdUseCase getOrderReceivedCountByCustomerIdUseCase;

    /** 受注番号再発行ユースケース */
    private final ReNumberingOrderCodeUseCase reNumberingOrderCodeUseCase;

    /** 受注Helperクラス */
    private final OrderReceivedHelper orderReceivedHelper;

    /** ヘッダパラメーターユーティル */
    private final HeaderParamsUtility headerParamsUtil;

    /** 変換Utility */
    private final ConversionUtility conversionUtility;

    @Autowired
    public OrderReceivedController(GetCustomerOrderHistoryListUseCase getCustomerOrderHistoryListUseCase,
                                   GetOrderReceivedUseCase getOrderReceivedUseCase,
                                   GetOrderReceivedByOrderCodeUseCase getOrderReceivedByOrderCodeUseCase,
                                   GetOrderReceivedByTransactionIdUseCase getOrderReceivedByTransactionIdUseCase,
                                   GetOrderReceivedCountByCustomerIdUseCase getOrderReceivedCountByCustomerIdUseCase,
                                   ReNumberingOrderCodeUseCase reNumberingOrderCodeUseCase,
                                   OrderReceivedHelper orderReceivedHelper,
                                   HeaderParamsUtility headerParamsUtil,
                                   ConversionUtility conversionUtility) {
        this.getCustomerOrderHistoryListUseCase = getCustomerOrderHistoryListUseCase;
        this.getOrderReceivedUseCase = getOrderReceivedUseCase;
        this.getOrderReceivedByOrderCodeUseCase = getOrderReceivedByOrderCodeUseCase;
        this.getOrderReceivedByTransactionIdUseCase = getOrderReceivedByTransactionIdUseCase;
        this.getOrderReceivedCountByCustomerIdUseCase = getOrderReceivedCountByCustomerIdUseCase;
        this.reNumberingOrderCodeUseCase = reNumberingOrderCodeUseCase;
        this.orderReceivedHelper = orderReceivedHelper;
        this.headerParamsUtil = headerParamsUtil;
        this.conversionUtility = conversionUtility;
    }

    /**
     * GET /order/customer/histories : 顧客注文履歴一覧取得
     * 顧客用の注文履歴一覧取得をする
     *
     * @param pageInfoRequest ページング情報
     * @return 顧客注文履歴一覧レスポンス (status code 200)
     * or システムエラー (status code 500)
     */
    @Override
    public ResponseEntity<CustomerHistoryListResponse> getCustomerHistoryList(PageInfoRequest pageInfoRequest) {

        CustomerOrderHistoryModel customerOrderHistoryModel =
                        getCustomerOrderHistoryListUseCase.getCustomerOrderHistoryList(getCustomerId(),
                                                                                       pageInfoRequest
                                                                                      );
        CustomerHistoryListResponse customerHistoryListResponse =
                        orderReceivedHelper.toCustomerHistoryListResponse(customerOrderHistoryModel);

        return new ResponseEntity<>(customerHistoryListResponse, HttpStatus.OK);
    }

    /**
     * GET /order/orderreceived/{orderReceivedId} : 受注取得
     * 受注取得
     *
     * @param orderReceivedId 受注ID (required)
     * @return 受注レスポンス (status code 200)
     * or システムエラー (status code 500)
     */
    @Override
    public ResponseEntity<OrderReceivedResponse> getByOrderReceivedId(
                    @ApiParam(value = "受注ID", required = true) @PathVariable("orderReceivedId")
                                    String orderReceivedId) {
        // 受注取得
        GetOrderReceivedUseCaseDto getOrderReceivedUseCaseDto =
                        this.getOrderReceivedUseCase.getOrderReceived(orderReceivedId);
        return new ResponseEntity<>(
                        orderReceivedHelper.toOrderReceivedResponse(getOrderReceivedUseCaseDto), HttpStatus.OK);
    }

    /**
     * GET /order/orderreceived/find-by-ordercode/{orderCode}: 受注取得
     * 受注取得
     *
     * @param orderCode 受注番号 (required)
     * @return 受注レスポンス (status code 200)
     * or システムエラー (status code 500)
     */
    @Override
    public ResponseEntity<OrderReceivedResponse> getByOrderCode(
                    @ApiParam(value = "受注番号", required = true) @PathVariable("orderCode") String orderCode) {
        // 受注取得
        GetOrderReceivedUseCaseDto getOrderReceivedUseCaseDto =
                        this.getOrderReceivedByOrderCodeUseCase.getOrderReceivedByOrderCode(orderCode);
        return new ResponseEntity<>(
                        orderReceivedHelper.toOrderReceivedResponse(getOrderReceivedUseCaseDto), HttpStatus.OK);
    }

    /**
     * GET /order/orderreceived : 受注取得
     *
     * @param getOrderReceivedRequest 受注取得リクエスト (required)
     * @return 受注レスポンス (status code 200)
     * or システムエラー (status code 500)
     */
    @Override
    public ResponseEntity<OrderReceivedResponse> get(@NotNull @ApiParam(value = "受注取得リクエスト", required = true) @Valid
                                                                     GetOrderReceivedRequest getOrderReceivedRequest) {
        // 受注取得
        String transactionId = getOrderReceivedRequest.getTransactionId();
        GetOrderReceivedByTransactionIdUseCaseDto getOrderReceivedByTransactionIdUseCaseDto =
                        this.getOrderReceivedByTransactionIdUseCase.getOrderReceivedByTransactionId(transactionId);
        return new ResponseEntity<>(
                        orderReceivedHelper.toOrderReceivedResponse(getOrderReceivedByTransactionIdUseCaseDto),
                        HttpStatus.OK
        );
    }

    /**
     * GET /order/orderreceived/count : 顧客ごとの受注件数取得
     *
     * @param getOrderReceivedCountRequest 受注件数取得リクエスト (required)
     * @return 受注件数レスポンス (status code 200)
     * or システムエラー (status code 500)
     */
    @Override
    public ResponseEntity<OrderReceivedCountResponse> getOrderReceivedCount(
                    @NotNull @ApiParam(value = "受注件数取得リクエスト", required = true) @Valid
                                    GetOrderReceivedCountRequest getOrderReceivedCountRequest) {
        // 顧客ごとの受注件数を取得
        String customerId = getOrderReceivedCountRequest.getCustomerId();
        int orderReceivedCount =
                        this.getOrderReceivedCountByCustomerIdUseCase.getOrderReceivedCountByCustomerId(customerId);

        // レスポンス組み立て
        OrderReceivedCountResponse orderReceivedCountResponse = new OrderReceivedCountResponse();
        orderReceivedCountResponse.setOrderReceivedCount(orderReceivedCount);

        return new ResponseEntity<>(orderReceivedCountResponse, HttpStatus.OK);
    }

    /**
     * POST /order/orderreceived/ordercode/re-numbering : 受注番号再発行
     * 受注番号再発行
     *
     * @param orderCodeReNumberingRequest 受注番号再発行リクエスト (required)
     * @return 受注番号再発行レスポンス (status code 200)
     * or システムエラー (status code 200)
     */
    @Override
    public ResponseEntity<OrderCodeReNumberingResponse> reNumberingOrderCode(
                    @Valid OrderCodeReNumberingRequest orderCodeReNumberingRequest) {

        String orderCode = reNumberingOrderCodeUseCase.reNumberingOrderCode(
                        orderCodeReNumberingRequest.getTransactionId());

        // レスポンス組み立て
        OrderCodeReNumberingResponse orderCodeReNumberingResponse = new OrderCodeReNumberingResponse();
        orderCodeReNumberingResponse.setOrderCode(orderCode);

        return new ResponseEntity<>(orderCodeReNumberingResponse, HttpStatus.OK);
    }

    /**
     * GET /order/orderreceived/find-transaction-by-ordercode/{orderCode} : get transaction by ordercode latest
     * get transaction by ordercode latest
     *
     * @param orderCode 受注番号 (required)
     * @return 受注番号再発行レスポンス (status code 200)
     *         or Bad Request（バリデーションエラー、業務ルールエラー、妥当性チェックエラーの場合） (status code 400)
     *         or Internal Server Error（アサートエラー、予想外エラーの場合） (status code 500)
     *         or システムエラー (status code 200)
     */
    @Override
    public ResponseEntity<TransactionIdResponse> getTransactionIdByOrderCodeLatest(
                    @ApiParam(value = "受注番号", required = true) @PathVariable("orderCode") String orderCode) {

        IGetTransactionForExpiredSessionQuery query =
                        ApplicationContextUtility.getBean(IGetTransactionForExpiredSessionQuery.class);
        String transactionId = query.getTransactionIdByOrderCode(orderCode);

        TransactionIdResponse transactionIdResponse = new TransactionIdResponse();
        transactionIdResponse.setTransactionId(transactionId);

        return new ResponseEntity<>(transactionIdResponse, HttpStatus.OK);
    }

    /**
     * 顧客IDを取得する
     *
     * @return customerId 顧客ID
     */
    private String getCustomerId() {
        // PR層でチェックはしない。memberSeqがNullの場合、customerIdにNullが設定される
        return this.headerParamsUtil.getMemberSeq();
    }

    /**
     * 管理者SEQを取得する
     *
     * @return AdminSeq 管理者SEQ
     */
    private Integer getAdminSeq() {
        return this.conversionUtility.toInteger(this.headerParamsUtil.getAdministratorSeq());
    }
}