/*
 * Copyright (C) 2022 i-TEC HANKYU HANSHIN INC. All Rights Reserved.
 */

package jp.co.itechh.quad.ddd.presentation.order;

import io.swagger.annotations.ApiParam;
import jp.co.itechh.quad.core.base.utility.ConversionUtility;
import jp.co.itechh.quad.core.web.AbstractController;
import jp.co.itechh.quad.core.web.HeaderParamsUtility;
import jp.co.itechh.quad.ddd.domain.order.entity.OrderSlipEntity;
import jp.co.itechh.quad.ddd.domain.order.entity.OrderSlipForRevisionEntity;
import jp.co.itechh.quad.ddd.usecase.order.AddOrderItemForRevisionUseCase;
import jp.co.itechh.quad.ddd.usecase.order.AddOrderItemUseCase;
import jp.co.itechh.quad.ddd.usecase.order.CancelOrderSlipForRevisionUseCase;
import jp.co.itechh.quad.ddd.usecase.order.ChangeOrderItemCountForRevisionUseCaseParam;
import jp.co.itechh.quad.ddd.usecase.order.ChangeOrderItemCountUseCase;
import jp.co.itechh.quad.ddd.usecase.order.CheckDraftOrderSlipUseCase;
import jp.co.itechh.quad.ddd.usecase.order.CheckOrderSlipForRevisionUseCase;
import jp.co.itechh.quad.ddd.usecase.order.DeleteOrderItemForRevisionUseCase;
import jp.co.itechh.quad.ddd.usecase.order.DeleteOrderSlipUseCase;
import jp.co.itechh.quad.ddd.usecase.order.GetDraftOrderSlipUseCase;
import jp.co.itechh.quad.ddd.usecase.order.GetOrderSlipByTransactionIdUseCase;
import jp.co.itechh.quad.ddd.usecase.order.GetOrderSlipForRevisionUseCase;
import jp.co.itechh.quad.ddd.usecase.order.MergeOrderItemUseCase;
import jp.co.itechh.quad.ddd.usecase.order.ModernizeOrderSlipUseCase;
import jp.co.itechh.quad.ddd.usecase.order.OpenOrderSlipForRevisionUseCase;
import jp.co.itechh.quad.ddd.usecase.order.OpenOrderSlipUseCase;
import jp.co.itechh.quad.ddd.usecase.order.PublishOrderSlipForRevisionUseCase;
import jp.co.itechh.quad.ddd.usecase.order.StartTransactionUseCase;
import jp.co.itechh.quad.ddd.usecase.order.UpdateOrderSlipForRevisionUseCase;
import jp.co.itechh.quad.orderslip.presentation.api.PromotionsApi;
import jp.co.itechh.quad.orderslip.presentation.api.param.AddOrderItemForRevisionRequest;
import jp.co.itechh.quad.orderslip.presentation.api.param.CancelOrderSlipForRevisionRequest;
import jp.co.itechh.quad.orderslip.presentation.api.param.DeleteOrderItemForRevisionRequest;
import jp.co.itechh.quad.orderslip.presentation.api.param.GetOrderSlipForRevisionRequest;
import jp.co.itechh.quad.orderslip.presentation.api.param.OpenOrderSlipForRevisionRequest;
import jp.co.itechh.quad.orderslip.presentation.api.param.OrderItemCountListUpdateRequest;
import jp.co.itechh.quad.orderslip.presentation.api.param.OrderItemDeleteRequest;
import jp.co.itechh.quad.orderslip.presentation.api.param.OrderItemRegistRequest;
import jp.co.itechh.quad.orderslip.presentation.api.param.OrderSlipCheckRequest;
import jp.co.itechh.quad.orderslip.presentation.api.param.OrderSlipForRevisionCheckRequest;
import jp.co.itechh.quad.orderslip.presentation.api.param.OrderSlipForRevisionResponse;
import jp.co.itechh.quad.orderslip.presentation.api.param.OrderSlipGetRequest;
import jp.co.itechh.quad.orderslip.presentation.api.param.OrderSlipMergeRequest;
import jp.co.itechh.quad.orderslip.presentation.api.param.OrderSlipModernizeRequest;
import jp.co.itechh.quad.orderslip.presentation.api.param.OrderSlipOpenRequest;
import jp.co.itechh.quad.orderslip.presentation.api.param.OrderSlipResponse;
import jp.co.itechh.quad.orderslip.presentation.api.param.OrderSlipTransactionRegistRequest;
import jp.co.itechh.quad.orderslip.presentation.api.param.PublishOrderSlipForRevisionRequest;
import jp.co.itechh.quad.orderslip.presentation.api.param.UpdateOrderSlipForRevisionRequest;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestHeader;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.context.request.RequestContextHolder;
import org.springframework.web.context.request.ServletRequestAttributes;

import javax.servlet.http.HttpServletRequest;
import javax.validation.Valid;
import javax.validation.constraints.NotNull;
import java.util.Date;
import java.util.List;
import java.util.Objects;

/**
 * 注文票エンドポイント Controller
 *
 * @author kimura
 */
@RestController
public class OrderSlipController extends AbstractController implements PromotionsApi {

    /** 取引にひもづく注文票取得ユースケース */
    private final GetOrderSlipByTransactionIdUseCase getOrderSlipByTransactionIdUseCase;

    /** 注文商品追加ユースケース */
    private final AddOrderItemUseCase addOrderItemUseCase;

    /** 注文商品数量更新ユースケース */
    private final ChangeOrderItemCountUseCase changeOrderItemCountUseCase;

    /** 注文商品削除ユースケース */
    private final DeleteOrderSlipUseCase deleteOrderItemUseCase;

    /** 下書き注文票チェックユースケース */
    private final CheckDraftOrderSlipUseCase checkDraftOrderSlipUseCase;

    /** 下書き注文票取得ユースケース */
    private final GetDraftOrderSlipUseCase getDraftOrderSlipUseCase;

    /** 取引開始ユースケース */
    private final StartTransactionUseCase startTransactionUseCase;

    /** 注文票最新化ユースケース */
    private final ModernizeOrderSlipUseCase modernizeOrderSlipUseCase;

    /** 注文票確定ユースケース */
    private final OpenOrderSlipUseCase openOrderSlipUseCase;

    /** 下書き注文票の注文商品統合ユースケース */
    private final MergeOrderItemUseCase mergeOrderItemUseCase;

    /** 改訂用注文票注文商品追加ユースケース */
    private final AddOrderItemForRevisionUseCase addOrderItemForRevisionUseCase;

    /** 改訂用注文票取消ユースケース */
    private final CancelOrderSlipForRevisionUseCase cancelOrderSlipForRevisionUseCase;

    /** 改訂用注文商品削除ユースケース */
    private final DeleteOrderItemForRevisionUseCase deleteOrderItemForRevisionUseCase;

    /** 改訂用注文票確定ユースケース */
    private final OpenOrderSlipForRevisionUseCase openOrderSlipForRevisionUseCase;

    /** 改訂用注文票発行ユースケース */
    private final PublishOrderSlipForRevisionUseCase publishOrderSlipForRevisionUseCase;

    /** 改訂用注文票チェックユースケース */
    private final CheckOrderSlipForRevisionUseCase checkOrderSlipForRevisionUseCase;

    /** 改訂用注文票取得ユースケース */
    private final GetOrderSlipForRevisionUseCase getOrderSlipForRevisionUseCase;

    /** 改訂用注文票更新ユースケース */
    private final UpdateOrderSlipForRevisionUseCase updateOrderSlipForRevisionUseCase;

    /** ヘルパー */
    private final OrderSlipHelper orderSlipHelper;

    /** ヘッダパラメーターユーティル */
    private final HeaderParamsUtility headerParamsUtil;

    /** 変換Helper */
    private final ConversionUtility conversionUtility;

    /** コンストラクタ */
    @Autowired
    public OrderSlipController(GetOrderSlipByTransactionIdUseCase getOrderSlipByTransactionIdUseCase,
                               AddOrderItemUseCase addOrderItemUseCase,
                               ChangeOrderItemCountUseCase changeOrderItemCountUseCase,
                               DeleteOrderSlipUseCase deleteOrderItemUseCase,
                               CheckDraftOrderSlipUseCase checkDraftOrderSlipUseCase,
                               ModernizeOrderSlipUseCase modernizeOrderSlipUseCase,
                               CheckOrderSlipForRevisionUseCase checkOrderSlipForRevisionUseCase,
                               GetDraftOrderSlipUseCase getDraftOrderSlipUseCase,
                               StartTransactionUseCase startTransactionUseCase,
                               OpenOrderSlipUseCase openOrderSlipUseCase,
                               MergeOrderItemUseCase mergeOrderItemUseCase,
                               AddOrderItemForRevisionUseCase addOrderItemForRevisionUseCase,
                               CancelOrderSlipForRevisionUseCase cancelOrderSlipForRevisionUseCase,
                               DeleteOrderItemForRevisionUseCase deleteOrderItemForRevisionUseCase,
                               OpenOrderSlipForRevisionUseCase openOrderSlipForRevisionUseCase,
                               PublishOrderSlipForRevisionUseCase publishOrderSlipForRevisionUseCase,
                               GetOrderSlipForRevisionUseCase getOrderSlipForRevisionUseCase,
                               UpdateOrderSlipForRevisionUseCase updateOrderSlipForRevisionUseCase,
                               OrderSlipHelper orderSlipHelper,
                               HeaderParamsUtility headerParamsUtil,
                               ConversionUtility conversionUtility) {
        this.getOrderSlipByTransactionIdUseCase = getOrderSlipByTransactionIdUseCase;
        this.addOrderItemUseCase = addOrderItemUseCase;
        this.changeOrderItemCountUseCase = changeOrderItemCountUseCase;
        this.deleteOrderItemUseCase = deleteOrderItemUseCase;
        this.modernizeOrderSlipUseCase = modernizeOrderSlipUseCase;
        this.checkOrderSlipForRevisionUseCase = checkOrderSlipForRevisionUseCase;
        this.checkDraftOrderSlipUseCase = checkDraftOrderSlipUseCase;
        this.getDraftOrderSlipUseCase = getDraftOrderSlipUseCase;
        this.startTransactionUseCase = startTransactionUseCase;
        this.openOrderSlipUseCase = openOrderSlipUseCase;
        this.mergeOrderItemUseCase = mergeOrderItemUseCase;
        this.addOrderItemForRevisionUseCase = addOrderItemForRevisionUseCase;
        this.cancelOrderSlipForRevisionUseCase = cancelOrderSlipForRevisionUseCase;
        this.deleteOrderItemForRevisionUseCase = deleteOrderItemForRevisionUseCase;
        this.openOrderSlipForRevisionUseCase = openOrderSlipForRevisionUseCase;
        this.publishOrderSlipForRevisionUseCase = publishOrderSlipForRevisionUseCase;
        this.getOrderSlipForRevisionUseCase = getOrderSlipForRevisionUseCase;
        this.updateOrderSlipForRevisionUseCase = updateOrderSlipForRevisionUseCase;
        this.orderSlipHelper = orderSlipHelper;
        this.headerParamsUtil = headerParamsUtil;
        this.conversionUtility = conversionUtility;
    }

    /**
     * GET /promotions/order-slips : 注文票取得
     *
     * @param orderSlipGetRequest 注文票取得リクエスト
     * @return 注文票レスポンス (status code 200) or システムエラー (status code 500)
     */
    @Override
    public ResponseEntity<OrderSlipResponse> get(
                    @ApiParam(value = "注文票取得リクエスト", required = true) @Valid OrderSlipGetRequest orderSlipGetRequest) {

        OrderSlipEntity entity = this.getOrderSlipByTransactionIdUseCase.getOrderSlipByTransactionId(
                        orderSlipGetRequest.getTransactionId());

        return new ResponseEntity<>(this.orderSlipHelper.toOrderSlipResponse(entity), HttpStatus.OK);
    }

    /**
     * POST /promotions/order-slips/items : 注文商品追加
     *
     * @param orderItemRegistRequest 注文商品追加リクエスト (required)
     * @param customerBirthday       注文票リクエストヘッダー(顧客生年月日)
     * @return 成功 (status code 200) or システムエラー (status code 500)
     */
    @Override
    public ResponseEntity<Void> registOrderItem(@ApiParam(value = "注文商品追加リクエスト", required = true) @Valid @RequestBody
                                                                OrderItemRegistRequest orderItemRegistRequest,
                                                @ApiParam("注文票リクエストヘッダー<br/>暫定対応のため、顧客生年月日のみ定義<br/>フェーズ3では、IDトークンでその他の顧客情報も含まれる想定")
                                                @RequestHeader(value = "customerBirthday", required = false)
                                                @DateTimeFormat(iso = DateTimeFormat.ISO.DATE_TIME)
                                                                Date customerBirthday) {

        this.addOrderItemUseCase.addOrderItem(getCustomerId(), orderItemRegistRequest.getItemId(),
                                              orderItemRegistRequest.getItemCount(), customerBirthday
                                             );

        return new ResponseEntity<>(HttpStatus.OK);
    }

    /**
     * PUT /promotions/order-slips/items/count : 注文商品数量一括更新
     *
     * @param orderItemCountListUpdateRequest 注文商品数量更新リクエスト (required)
     * @return 成功 (status code 200) or システムエラー (status code 500)
     */
    @Override
    public ResponseEntity<Void> updateOrderItemCount(@ApiParam("注文商品数量更新リクエスト") @Valid @RequestBody(required = false)
                                                                     OrderItemCountListUpdateRequest orderItemCountListUpdateRequest) {

        this.changeOrderItemCountUseCase.changeOrderItem(
                        getCustomerId(), this.orderSlipHelper.toChangeOrderCountUseCaseParam(
                                        orderItemCountListUpdateRequest));

        return new ResponseEntity<>(HttpStatus.OK);
    }

    /**
     * DELETE /promotions/order-slips/items : 注文商品削除
     *
     * @param orderItemDeleteRequest 注文商品削除リクエスト (required)
     * @return 成功 (status code 200) or システムエラー (status code 500)
     */
    @Override
    public ResponseEntity<Void> deleteOrderItem(@NotNull @ApiParam(value = "注文商品削除リクエスト", required = true) @Valid
                                                                OrderItemDeleteRequest orderItemDeleteRequest) {

        this.deleteOrderItemUseCase.deleteOrderItem(getCustomerId(), orderItemDeleteRequest.getItemId());

        return new ResponseEntity<>(HttpStatus.OK);
    }

    /**
     * POST /promotions/order-slips/draft/check : 下書き注文票チェック
     *
     * @param customerBirthday      注文票リクエストヘッダー(顧客生年月日)
     * @param orderSlipCheckRequest 注文票チェックリクエスト
     * @return 成功 (status code 200) or システムエラー (status code 500)
     */
    @Override
    public ResponseEntity<Void> checkDraft(
                    @ApiParam("注文票リクエストヘッダー<br/>暫定対応のため、顧客生年月日のみ定義<br/>フェーズ3では、IDトークンでその他の顧客情報も含まれる想定")
                    @RequestHeader(value = "customerBirthday", required = false)
                    @DateTimeFormat(iso = DateTimeFormat.ISO.DATE_TIME) Date customerBirthday,
                    @ApiParam("注文票チェックリクエスト") @Valid OrderSlipCheckRequest orderSlipCheckRequest) {
        // 注文票リクエストは送信元から渡されないこともある⇒渡された場合のみ、取引IDを取得する
        String transactionId = orderSlipCheckRequest == null ? null : orderSlipCheckRequest.getTransactionId();
        this.checkDraftOrderSlipUseCase.checkDraftOrderSlip(getCustomerId(), transactionId, customerBirthday);

        return new ResponseEntity<>(HttpStatus.OK);
    }

    /**
     * GET /promotions/order-slips/draft : 下書き注文票取得
     *
     * @param orderSlipGetRequest 注文票取得リクエスト
     * @return 注文票レスポンス (status code 200) or システムエラー (status code 500)
     */
    @Override
    public ResponseEntity<OrderSlipResponse> getDraft(
                    @ApiParam("注文票取得リクエスト") @Valid OrderSlipGetRequest orderSlipGetRequest) {

        OrderSlipEntity entity = this.getDraftOrderSlipUseCase.getDraftOrderSlip(getCustomerId(),
                                                                                 orderSlipGetRequest.getOrderSlipId(),
                                                                                 orderSlipGetRequest.getTransactionId()
                                                                                );

        return new ResponseEntity<>(this.orderSlipHelper.toOrderSlipResponse(entity), HttpStatus.OK);
    }

    /**
     * POST /promotions/order-slips/modernize : 注文票最新化
     *
     * @param orderSlipModernizeRequest 注文票最新化リクエスト (required)
     * @return 成功 (status code 200) or システムエラー (status code 500)
     */
    @Override
    public ResponseEntity<Void> modernize(@ApiParam(value = "注文票最新化リクエスト", required = true) @Valid @RequestBody
                                                          OrderSlipModernizeRequest orderSlipModernizeRequest) {

        this.modernizeOrderSlipUseCase.modernizeOrderSlip(orderSlipModernizeRequest.getTransactionId());

        return new ResponseEntity<>(HttpStatus.OK);
    }

    /**
     * POST /promotions/order-slips/transactions : 取引開始
     *
     * @param customerBirthday                  注文票リクエストヘッダー(顧客生年月日) (required)
     * @param orderSlipTransactionRegistRequest 取引開始リクエスト (required)
     * @return 成功 (status code 200) or システムエラー (status code 500)
     */
    @Override
    public ResponseEntity<Void> registTransaction(@ApiParam(value = "注文票リクエストヘッダー(顧客生年月日)", required = true)
                                                  @RequestHeader(value = "customerBirthday", required = true)
                                                  @DateTimeFormat(iso = DateTimeFormat.ISO.DATE_TIME)
                                                                  Date customerBirthday,
                                                  @ApiParam(value = "取引開始リクエスト", required = true) @Valid @RequestBody
                                                                  OrderSlipTransactionRegistRequest orderSlipTransactionRegistRequest) {

        this.startTransactionUseCase.startTransaction(
                        getCustomerId(), orderSlipTransactionRegistRequest.getTransactionId(), customerBirthday);

        return new ResponseEntity<>(HttpStatus.OK);
    }

    /**
     * POST /promotions/order-slips/open : 注文票確定
     *
     * @param orderSlipOpenRequest 注文票確定リクエスト (required)
     * @return 成功 (status code 200) or システムエラー (status code 500)
     */
    @Override
    public ResponseEntity<Void> open(@ApiParam(value = "注文票確定リクエスト", required = true) @Valid @RequestBody
                                                     OrderSlipOpenRequest orderSlipOpenRequest) {

        // リクエストからデバイス情報を判定
        HttpServletRequest request = ((ServletRequestAttributes) Objects.requireNonNull(
                        RequestContextHolder.getRequestAttributes())).getRequest();
        String userAgent = request.getHeader("User-Agent");

        this.openOrderSlipUseCase.openOrderSlip(orderSlipOpenRequest.getTransactionId(), userAgent);

        return new ResponseEntity<>(HttpStatus.OK);
    }

    /**
     * POST /promotions/order-slips/draft/merge : 下書き注文票統合
     *
     * @param orderSlipMergeRequest 注文票統合リクエスト (required)
     * @return 成功 (status code 200) or システムエラー (status code 500)
     */
    @Override
    public ResponseEntity<Void> merge(@ApiParam(value = "注文票統合リクエスト", required = true) @Valid @RequestBody
                                                      OrderSlipMergeRequest orderSlipMergeRequest) {

        this.mergeOrderItemUseCase.mergeOrderItem(
                        orderSlipMergeRequest.getCustomerIdFrom(), orderSlipMergeRequest.getCustomerIdTo());

        return new ResponseEntity<>(HttpStatus.OK);
    }

    /**
     * POST /promotions/order-slips/for-revision/items : 改訂用注文票注文商品追加
     * 改訂用注文票注文商品追加
     *
     * @param addOrderItemForRevisionRequest 改訂用注文票注文商品追加リクエスト (required)
     * @return 成功 (status code 200)
     * or システムエラー (status code 200)
     */
    @Override
    public ResponseEntity<Void> addOrderItemForRevision(
                    @ApiParam(value = "改訂用注文票注文商品追加リクエスト", required = true) @Valid @RequestBody
                                    AddOrderItemForRevisionRequest addOrderItemForRevisionRequest) {

        this.addOrderItemForRevisionUseCase.addOrderItemForRevision(
                        addOrderItemForRevisionRequest.getTransactionRevisionId(),
                        addOrderItemForRevisionRequest.getItemId(), addOrderItemForRevisionRequest.getOrderCount()
                                                                   );

        return new ResponseEntity<>(HttpStatus.OK);
    }

    /**
     * POST /promotions/order-slips/for-revision/cancel : 改訂用注文票取消反映
     * 改訂用注文票へ取消を反映する
     *
     * @param cancelOrderSlipForRevisionRequest 改訂用注文票取消リクエスト (required)
     * @return 成功 (status code 200)
     * or システムエラー (status code 200)
     */
    @Override
    public ResponseEntity<Void> cancelOrderSlipForRevision(
                    @ApiParam(value = "改訂用注文票取消リクエスト", required = true) @Valid @RequestBody
                                    CancelOrderSlipForRevisionRequest cancelOrderSlipForRevisionRequest) {

        this.cancelOrderSlipForRevisionUseCase.cancelOrderSlipForRevision(
                        cancelOrderSlipForRevisionRequest.getTransactionRevisionId());

        return new ResponseEntity<>(HttpStatus.OK);
    }

    /**
     * DELETE /promotions/order-slips/for-revision/items : 改訂用注文商品削除
     * 改訂用注文商品削除
     *
     * @param deleteOrderItemForRevisionRequest 改訂用注文商品削除リクエスト (required)
     * @return 成功 (status code 200)
     * or システムエラー (status code 200)
     */
    @Override
    public ResponseEntity<Void> deleteOrderItemForRevision(
                    @ApiParam(value = "改訂用注文商品削除リクエスト", required = true) @Valid @RequestBody
                                    DeleteOrderItemForRevisionRequest deleteOrderItemForRevisionRequest) {

        this.deleteOrderItemForRevisionUseCase.deleteOrderItemForRevision(
                        deleteOrderItemForRevisionRequest.getTransactionRevisionId(),
                        deleteOrderItemForRevisionRequest.getOrderItemSeq()
                                                                         );

        return new ResponseEntity<>(HttpStatus.OK);
    }

    /**
     * POST /promotions/order-slips/for-revision/open : 改訂用注文票確定
     * 改訂用注文票確定
     *
     * @param openOrderSlipForRevisionRequest 改訂用注文票確定リクエスト (required)
     * @return 成功 (status code 200)
     * or システムエラー (status code 200)
     */
    @Override
    public ResponseEntity<Void> openOrderSlipForRevision(
                    @ApiParam(value = "改訂用注文票確定リクエスト", required = true) @Valid @RequestBody
                                    OpenOrderSlipForRevisionRequest openOrderSlipForRevisionRequest) {

        this.openOrderSlipForRevisionUseCase.openOrderSlipForRevision(
                        openOrderSlipForRevisionRequest.getTransactionRevisionId());

        return new ResponseEntity<>(HttpStatus.OK);
    }

    /**
     * POST /promotions/order-slips/for-revision : 改訂用注文票発行
     * 改訂用注文票発行
     *
     * @param publishOrderSlipForRevisionRequest 改訂用注文票発行リクエスト (required)
     * @return 成功 (status code 200)
     * or システムエラー (status code 200)
     */
    @Override
    public ResponseEntity<Void> publishOrderSlipForRevision(
                    @ApiParam(value = "改訂用注文票発行リクエスト", required = true) @Valid @RequestBody
                                    PublishOrderSlipForRevisionRequest publishOrderSlipForRevisionRequest) {

        this.publishOrderSlipForRevisionUseCase.publishOrderSlipForRevision(
                        publishOrderSlipForRevisionRequest.getTransactionId(),
                        publishOrderSlipForRevisionRequest.getTransactionRevisionId()
                                                                           );

        return new ResponseEntity<>(HttpStatus.OK);
    }

    /**
     * POST /promotions/order-slips/for-revision/check : 改訂用注文票をチェックする
     * 改訂用注文票をチェックする
     *
     * @param orderSlipForRevisionCheckRequest 改訂用注文票をチェックするリクエスト (required)
     * @return 成功 (status code 200)
     * or システムエラー (status code 200)
     */
    @Override
    public ResponseEntity<Void> checkOrderSlipForRevision(
                    @ApiParam(value = "改訂用注文票をチェックするリクエスト", required = true) @Valid @RequestBody
                                    OrderSlipForRevisionCheckRequest orderSlipForRevisionCheckRequest) {

        checkOrderSlipForRevisionUseCase.checkOrderSlipForRevision(
                        orderSlipForRevisionCheckRequest.getTransactionRevisionId());

        return new ResponseEntity<>(HttpStatus.OK);
    }

    /**
     * GET /promotions/order-slips/for-revision : 改訂用注文票取得
     * 改訂用注文票取得
     *
     * @param getOrderSlipForRevisionByTransactionRevisionId 取引に紐づく改訂用注文票を取得する (required)
     * @return 改訂用注文票レスポンス (status code 200)
     * or システムエラー (status code 200)
     */
    @Override
    public ResponseEntity<OrderSlipForRevisionResponse> getOrderSlipForRevision(
                    @NotNull @ApiParam(value = "取引に紐づく改訂用注文票を取得する", required = true) @Valid
                                    GetOrderSlipForRevisionRequest getOrderSlipForRevisionByTransactionRevisionId) {

        OrderSlipForRevisionEntity orderSlipForRevisionEntity =
                        this.getOrderSlipForRevisionUseCase.getOrderSlipForRevision(
                                        getOrderSlipForRevisionByTransactionRevisionId.getTransactionRevisionId());

        OrderSlipForRevisionResponse orderSlipForRevisionResponse =
                        orderSlipHelper.toOrderSlipForRevisionResponse(orderSlipForRevisionEntity);

        return new ResponseEntity<>(orderSlipForRevisionResponse, HttpStatus.OK);
    }

    /**
     * PUT /promotions/order-slips/for-revision/items : 改訂用注文票商品一括更新
     * 改訂用注文票更新
     *
     * @param updateOrderSlipForRevisionRequest 改訂用注文票更新リクエスト (required)
     * @return 成功 (status code 200)
     * or システムエラー (status code 200)
     */
    @Override
    public ResponseEntity<Void> updateOrderSlipForRevision(
                    @ApiParam(value = "改訂用注文票更新リクエスト", required = true) @Valid @RequestBody
                                    UpdateOrderSlipForRevisionRequest updateOrderSlipForRevisionRequest) {

        List<ChangeOrderItemCountForRevisionUseCaseParam> changeItemList =
                        this.orderSlipHelper.toChangeOrderCountUseCaseParam(updateOrderSlipForRevisionRequest);

        this.updateOrderSlipForRevisionUseCase.updateOrderSlipForRevision(
                        updateOrderSlipForRevisionRequest.getTransactionRevisionId(), changeItemList);

        return new ResponseEntity<>(HttpStatus.OK);
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

}