/*
 * Copyright (C) 2022 i-TEC HANKYU HANSHIN INC. All Rights Reserved.
 */

package jp.co.itechh.quad.ddd.presentation.shipping;

import io.swagger.annotations.ApiParam;
import jp.co.itechh.quad.core.base.utility.ConversionUtility;
import jp.co.itechh.quad.core.web.AbstractController;
import jp.co.itechh.quad.core.web.HeaderParamsUtility;
import jp.co.itechh.quad.ddd.domain.shipping.entity.ShippingSlipEntity;
import jp.co.itechh.quad.ddd.domain.shipping.entity.UpdateShippingConditionDomainParam;
import jp.co.itechh.quad.ddd.usecase.shipping.CancelShippingSlipForRevisionUseCase;
import jp.co.itechh.quad.ddd.usecase.shipping.CheckShippingSlipForRevisionUseCase;
import jp.co.itechh.quad.ddd.usecase.shipping.CheckShippingSlipUseCase;
import jp.co.itechh.quad.ddd.usecase.shipping.GetShippingSlipByTransactionIdUseCase;
import jp.co.itechh.quad.ddd.usecase.shipping.GetShippingSlipForRevisionByTransactionRevisionIdUseCase;
import jp.co.itechh.quad.ddd.usecase.shipping.ModernizeShippingSlipUseCase;
import jp.co.itechh.quad.ddd.usecase.shipping.OpenShippingSlipForRevisionUseCase;
import jp.co.itechh.quad.ddd.usecase.shipping.OpenShippingSlipUseCase;
import jp.co.itechh.quad.ddd.usecase.shipping.PublishShippingSlipForRevisionUseCase;
import jp.co.itechh.quad.ddd.usecase.shipping.PublishShippingSlipUseCase;
import jp.co.itechh.quad.ddd.usecase.shipping.RegistShipmentResultForRevisionUseCase;
import jp.co.itechh.quad.ddd.usecase.shipping.SecureInventoryUseCase;
import jp.co.itechh.quad.ddd.usecase.shipping.SettingShippingAddressUseCase;
import jp.co.itechh.quad.ddd.usecase.shipping.SettingShippingMethodUseCase;
import jp.co.itechh.quad.ddd.usecase.shipping.UpdateShippingAddressForRevisionUseCase;
import jp.co.itechh.quad.ddd.usecase.shipping.UpdateShippingConditionForRevisionUseCase;
import jp.co.itechh.quad.shippingslip.presentation.api.ShippingsApi;
import jp.co.itechh.quad.shippingslip.presentation.api.param.ShippingSettingAddressForRevisionRequest;
import jp.co.itechh.quad.shippingslip.presentation.api.param.ShippingSlipAddressUpdateRequest;
import jp.co.itechh.quad.shippingslip.presentation.api.param.ShippingSlipCheckRequest;
import jp.co.itechh.quad.shippingslip.presentation.api.param.ShippingSlipCheckResponse;
import jp.co.itechh.quad.shippingslip.presentation.api.param.ShippingSlipForRevisionCancelRequest;
import jp.co.itechh.quad.shippingslip.presentation.api.param.ShippingSlipForRevisionCheckRequest;
import jp.co.itechh.quad.shippingslip.presentation.api.param.ShippingSlipForRevisionGetRequest;
import jp.co.itechh.quad.shippingslip.presentation.api.param.ShippingSlipForRevisionOpenRequest;
import jp.co.itechh.quad.shippingslip.presentation.api.param.ShippingSlipForRevisionPublishRequest;
import jp.co.itechh.quad.shippingslip.presentation.api.param.ShippingSlipForRevisionShipmentRequest;
import jp.co.itechh.quad.shippingslip.presentation.api.param.ShippingSlipGetRequest;
import jp.co.itechh.quad.shippingslip.presentation.api.param.ShippingSlipInventorySecureRequest;
import jp.co.itechh.quad.shippingslip.presentation.api.param.ShippingSlipMethodUpdateRequest;
import jp.co.itechh.quad.shippingslip.presentation.api.param.ShippingSlipModernizeRequest;
import jp.co.itechh.quad.shippingslip.presentation.api.param.ShippingSlipOpenRequest;
import jp.co.itechh.quad.shippingslip.presentation.api.param.ShippingSlipRegistRequest;
import jp.co.itechh.quad.shippingslip.presentation.api.param.ShippingSlipResponse;
import jp.co.itechh.quad.shippingslip.presentation.api.param.ShippingUpdateConditionForRevisionRequest;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

import javax.validation.Valid;
import javax.validation.constraints.NotNull;
import java.util.List;

/**
 * 配送伝票エンドポイント Controller
 *
 * @author kimura
 */
@RestController
public class ShippingSlipController extends AbstractController implements ShippingsApi {

    /** 配送伝票発行ユースケース */
    private final PublishShippingSlipUseCase publishShippingSlipUseCase;

    /** 配送伝票最新化ユースケース */
    private final ModernizeShippingSlipUseCase modernizeShippingSlipUseCase;

    /** 配送伝票チェックユースケース */
    private final CheckShippingSlipUseCase checkShippingSlipUseCase;

    /** 取引に紐づく配送伝票取得ユースケース */
    private final GetShippingSlipByTransactionIdUseCase getShippingSlipByTransactionIdUseCase;

    /** 配送先設定ユースケース */
    private final SettingShippingAddressUseCase settingShippingAddressUseCase;

    /** 配送方法設定ユースケース */
    private final SettingShippingMethodUseCase settingShippingMethodUseCase;

    /** 在庫確保ユースケース */
    private final SecureInventoryUseCase secureInventoryUseCase;

    /** 配送伝票確定ユースケース */
    private final OpenShippingSlipUseCase openShippingSlipUseCase;

    /** 配送伝票取消ユースケース */
    private final CancelShippingSlipForRevisionUseCase cancelShippingSlipUseCase;

    /** 出荷実績登録ユースケース */
    private final RegistShipmentResultForRevisionUseCase registShipmentResultUseCase;

    /** 改訂用配送伝票チェックユースケース */
    private final CheckShippingSlipForRevisionUseCase checkShippingSlipForRevisionUseCase;

    /** 改訂用取引に紐づく改訂用配送伝票取得ユースケース */
    private final GetShippingSlipForRevisionByTransactionRevisionIdUseCase
                    getShippingSlipForRevisionByTransactionRevisionIdUseCase;

    /** 改訂用配送伝票確定ユースケース */
    private final OpenShippingSlipForRevisionUseCase openShippingSlipForRevisionUseCase;

    /** 改訂用配送伝票発行ユースケース */
    private final PublishShippingSlipForRevisionUseCase publishShippingSlipForRevisionUseCase;

    /** 改訂用配送伝票の配送先更新 ユースケース */
    private final UpdateShippingAddressForRevisionUseCase updateShippingAddressForRevisionUseCase;

    /** 改訂用配送伝票の配送条件を更新する ユースケース */
    private final UpdateShippingConditionForRevisionUseCase updateShippingConditionForRevisionUseCase;

    /** ヘルパー */
    private final ShippingSlipHelper shippingSlipHelper;

    /** ヘッダパラメーターユーティル */
    private final HeaderParamsUtility headerParamsUtil;

    /** 変換Helper */
    private final ConversionUtility conversionUtility;

    public ShippingSlipController(PublishShippingSlipUseCase publishShippingSlipUseCase,
                                  ModernizeShippingSlipUseCase modernizeShippingSlipUseCase,
                                  CheckShippingSlipUseCase checkShippingSlipUseCase,
                                  GetShippingSlipByTransactionIdUseCase getShippingSlipByTransactionIdUseCase,
                                  SettingShippingAddressUseCase settingShippingAddressUseCase,
                                  SettingShippingMethodUseCase settingShippingMethodUseCase,
                                  SecureInventoryUseCase secureInventoryUseCase,
                                  OpenShippingSlipUseCase openShippingSlipUseCase,
                                  CancelShippingSlipForRevisionUseCase cancelShippingSlipUseCase,
                                  RegistShipmentResultForRevisionUseCase registShipmentResultUseCase,
                                  CheckShippingSlipForRevisionUseCase checkShippingSlipForRevisionUseCase,
                                  GetShippingSlipForRevisionByTransactionRevisionIdUseCase getShippingSlipForRevisionByTransactionRevisionIdUseCase,
                                  OpenShippingSlipForRevisionUseCase openShippingSlipForRevisionUseCase,
                                  PublishShippingSlipForRevisionUseCase publishShippingSlipForRevisionUseCase,
                                  UpdateShippingAddressForRevisionUseCase updateShippingAddressForRevisionUseCase,
                                  UpdateShippingConditionForRevisionUseCase updateShippingConditionForRevisionUseCase,
                                  ShippingSlipHelper shippingSlipHelper,
                                  HeaderParamsUtility headerParamsUtil,
                                  ConversionUtility conversionUtility) {
        this.publishShippingSlipUseCase = publishShippingSlipUseCase;
        this.modernizeShippingSlipUseCase = modernizeShippingSlipUseCase;
        this.checkShippingSlipUseCase = checkShippingSlipUseCase;
        this.getShippingSlipByTransactionIdUseCase = getShippingSlipByTransactionIdUseCase;
        this.settingShippingAddressUseCase = settingShippingAddressUseCase;
        this.settingShippingMethodUseCase = settingShippingMethodUseCase;
        this.secureInventoryUseCase = secureInventoryUseCase;
        this.openShippingSlipUseCase = openShippingSlipUseCase;
        this.cancelShippingSlipUseCase = cancelShippingSlipUseCase;
        this.registShipmentResultUseCase = registShipmentResultUseCase;
        this.checkShippingSlipForRevisionUseCase = checkShippingSlipForRevisionUseCase;
        this.getShippingSlipForRevisionByTransactionRevisionIdUseCase =
                        getShippingSlipForRevisionByTransactionRevisionIdUseCase;
        this.openShippingSlipForRevisionUseCase = openShippingSlipForRevisionUseCase;
        this.publishShippingSlipForRevisionUseCase = publishShippingSlipForRevisionUseCase;
        this.updateShippingAddressForRevisionUseCase = updateShippingAddressForRevisionUseCase;
        this.updateShippingConditionForRevisionUseCase = updateShippingConditionForRevisionUseCase;
        this.shippingSlipHelper = shippingSlipHelper;
        this.headerParamsUtil = headerParamsUtil;
        this.conversionUtility = conversionUtility;
    }

    /**
     * POST /shippings/shipping-slips : 配送伝票発行（取引開始）
     *
     * @param shippingSlipRegistRequest 取引開始リクエスト (required)
     * @return 成功 (status code 200) or システムエラー (status code 200)
     */
    @Override
    public ResponseEntity<Void> regist(@ApiParam(value = "取引開始リクエスト", required = true) @Valid @RequestBody
                                                       ShippingSlipRegistRequest shippingSlipRegistRequest) {

        this.publishShippingSlipUseCase.publishShippingSlip(
                        shippingSlipRegistRequest.getTransactionId(), getCustomerId());

        return new ResponseEntity<>(HttpStatus.OK);
    }

    /**
     * POST /shippings/shipping-slips/modernize : 配送伝票最新化
     *
     * @param shippingSlipModernizeRequest 配送伝票最新化リクエスト (required)
     * @return 成功 (status code 200) or システムエラー (status code 200)
     */
    @Override
    public ResponseEntity<Void> modernize(@ApiParam(value = "配送伝票最新化リクエスト", required = true) @Valid @RequestBody
                                                          ShippingSlipModernizeRequest shippingSlipModernizeRequest) {

        this.modernizeShippingSlipUseCase.modernizeShippingSlip(shippingSlipModernizeRequest.getTransactionId());

        return new ResponseEntity<>(HttpStatus.OK);
    }

    /**
     * POST /shippings/shipping-slips/check : 配送伝票チェック
     *
     * @param shippingSlipCheckRequest 配送伝票チェックリクエスト (required)
     * @return 成功 (status code 200) or システムエラー (status code 200)
     */
    @Override
    public ResponseEntity<Void> check(@ApiParam(value = "配送伝票チェックリクエスト", required = true) @Valid @RequestBody
                                                      ShippingSlipCheckRequest shippingSlipCheckRequest) {

        this.checkShippingSlipUseCase.checkShippingSlip(shippingSlipCheckRequest.getTransactionId());

        return new ResponseEntity<>(HttpStatus.OK);
    }

    /**
     * GET /shippings/shipping-slips : 配送伝票取得
     *
     * @param shippingSlipGetRequest 配送情報取得リクエスト (required)
     * @return 配送伝票レスポンス (status code 200) or システムエラー (status code 200)
     */
    @Override
    public ResponseEntity<ShippingSlipResponse> get(@NotNull @ApiParam(value = "配送伝票取得リクエスト", required = true) @Valid
                                                                    ShippingSlipGetRequest shippingSlipGetRequest) {

        ShippingSlipEntity entity = this.getShippingSlipByTransactionIdUseCase.getShippingSlipByTransactionId(
                        shippingSlipGetRequest.getTransactionId());

        return new ResponseEntity<>(this.shippingSlipHelper.toShippingSlipResponse(entity), HttpStatus.OK);
    }

    /**
     * PUT /shippings/shipping-slips/address : 配送先設定
     *
     * @param shippingSlipAddressUpdateRequest 配送先設定リクエスト (required)
     * @return 成功 (status code 200) or システムエラー (status code 200)
     */
    @Override
    public ResponseEntity<Void> updateAddress(@ApiParam(value = "配送先設定リクエスト", required = true) @Valid @RequestBody
                                                              ShippingSlipAddressUpdateRequest shippingSlipAddressUpdateRequest) {

        this.settingShippingAddressUseCase.settingShippingAddress(shippingSlipAddressUpdateRequest.getTransactionId(),
                                                                  shippingSlipAddressUpdateRequest.getShippingAddressId()
                                                                 );

        return new ResponseEntity<>(HttpStatus.OK);
    }

    /**
     * PUT /shippings/shipping-slips/methods : 配送方法設定
     *
     * @param shippingSlipMethodUpdateRequest 配送方法設定リクエスト (required)
     * @return 成功 (status code 200) or システムエラー (status code 200)
     */
    @Override
    public ResponseEntity<Void> updateMethod(@ApiParam(value = "配送方法設定リクエスト", required = true) @Valid @RequestBody
                                                             ShippingSlipMethodUpdateRequest shippingSlipMethodUpdateRequest) {

        this.settingShippingMethodUseCase.settingShippingMethod(shippingSlipMethodUpdateRequest.getTransactionId(),
                                                                shippingSlipMethodUpdateRequest.getShippingMethodId(),
                                                                this.conversionUtility.toDate(
                                                                                shippingSlipMethodUpdateRequest.getReceiverDate()),
                                                                shippingSlipMethodUpdateRequest.getReceiverTimeZone(),
                                                                setDefaultBoolean(
                                                                                shippingSlipMethodUpdateRequest.getInvoiceNecessaryFlag())
                                                               );

        return new ResponseEntity<>(HttpStatus.OK);
    }

    /**
     * POST /shippings/shipping-slips/inventories/secure : 配送商品在庫確保
     *
     * @param shippingSlipInventorySecureRequest 在庫確保リクエスト (required)
     * @return 成功 (status code 200) or システムエラー (status code 200)
     */
    @Override
    public ResponseEntity<Void> secureInventory(@ApiParam(value = "在庫確保リクエスト", required = true) @Valid @RequestBody
                                                                ShippingSlipInventorySecureRequest shippingSlipInventorySecureRequest) {

        List<Integer> stockChangedGoodsSeqList = secureInventoryUseCase.secureShippingProductInventory(
                        shippingSlipInventorySecureRequest.getTransactionId());
        registShipmentResultUseCase.asyncAfterProcess(stockChangedGoodsSeqList);
        return new ResponseEntity<>(HttpStatus.OK);
    }

    /**
     * POST /shippings/shipping-slips/open : 配送伝票確定
     *
     * @param shippingSlipOpenRequest 配送伝票確定リクエスト (required)
     * @return 成功 (status code 200) or システムエラー (status code 200)
     */
    @Override
    public ResponseEntity<Void> open(@ApiParam(value = "配送伝票確定リクエスト", required = true) @Valid @RequestBody
                                                     ShippingSlipOpenRequest shippingSlipOpenRequest) {

        this.openShippingSlipUseCase.openShippingSlip(shippingSlipOpenRequest.getTransactionId());

        return new ResponseEntity<>(HttpStatus.OK);
    }

    /**
     * POST /shippings/shipping-slips/for-revision/cancel : 改訂用配送伝票取消
     * 伝票取消のために、改訂用配送伝票へ取消の反映を行う
     *
     * @param shippingSlipForRevisionCancelRequest 改訂用配送伝票取消リクエスト (required)
     * @return 成功 (status code 200)
     * or システムエラー (status code 200)
     */
    @Override
    public ResponseEntity<Void> cancelForRevision(
                    @ApiParam(value = "改訂用配送伝票取消リクエスト", required = true) @Valid @RequestBody
                                    ShippingSlipForRevisionCancelRequest shippingSlipForRevisionCancelRequest) {

        List<Integer> stockChangedGoodsSeqList = cancelShippingSlipUseCase.cancelShippingSlipForRevision(
                        shippingSlipForRevisionCancelRequest.getTransactionRevisionId(),
                        Boolean.TRUE.equals(shippingSlipForRevisionCancelRequest.getRevisionOpenFlag())
                                                                                                        );
        cancelShippingSlipUseCase.asyncAfterProcess(stockChangedGoodsSeqList);

        return new ResponseEntity<>(HttpStatus.OK);
    }

    /**
     * POST /shippings/shipping-slips/for-revision/shipments : 改訂用配送伝票出荷実績登録
     * 改訂用配送伝票の出荷実績を登録する
     *
     * @param shippingSlipForRevisionShipmentRequest 配送伝票出荷リクエスト (required)
     * @return 成功 (status code 200)
     * or システムエラー (status code 200)
     */
    @Override
    public ResponseEntity<Void> shipmentForRevision(
                    @ApiParam(value = "配送伝票出荷リクエスト", required = true) @Valid @RequestBody
                                    ShippingSlipForRevisionShipmentRequest shippingSlipForRevisionShipmentRequest) {

        List<Integer> stockChangedGoodsSeqList = registShipmentResultUseCase.registShipmentResult(
                        shippingSlipForRevisionShipmentRequest.getTransactionRevisionId(),
                        shippingSlipForRevisionShipmentRequest.getShipmentStatusConfirmCode(),
                        shippingSlipForRevisionShipmentRequest.getCompleteShipmentDate(),
                        Boolean.TRUE.equals(shippingSlipForRevisionShipmentRequest.getRevisionOpenFlag())
                                                                                                 );
        registShipmentResultUseCase.asyncAfterProcess(stockChangedGoodsSeqList);

        return new ResponseEntity<>(HttpStatus.OK);
    }

    /**
     * POST /shippings/shipping-slips/for-revision/check : 改訂用配送伝票をチェックする
     * 改訂用配送伝票をチェックする
     *
     * @param shippingSlipForRevisionCheckRequest 改訂用配送伝票をチェックするリクエスト (required)
     * @return 成功 (status code 200)
     * or システムエラー (status code 200)
     */
    @Override
    public ResponseEntity<ShippingSlipCheckResponse> checkShippingSlipForRevision(
                    @ApiParam(value = "改訂用配送伝票をチェックするリクエスト", required = true) @Valid @RequestBody
                                    ShippingSlipForRevisionCheckRequest shippingSlipForRevisionCheckRequest) {

        ShippingSlipCheckResponse shippingSlipCheckResponse = new ShippingSlipCheckResponse();
        shippingSlipCheckResponse.setWarningMessage(checkShippingSlipForRevisionUseCase.checkShippingSlipForRevision(
                        shippingSlipForRevisionCheckRequest.getTransactionRevisionId()));

        return new ResponseEntity<>(shippingSlipCheckResponse, HttpStatus.OK);
    }

    /**
     * GET /shippings/shipping-slips/for-revision : 改訂用取引に紐づく改訂用配送伝票取得
     * 改訂用取引IDに紐づく改訂用配送伝票を取得する
     *
     * @param shippingSlipForRevisionGetRequest 改訂用取引IDに紐づく改訂用配送伝票を取得するクリクエスト (required)
     * @return 改訂用配送伝票スポンス (status code 200)
     * or システムエラー (status code 200)
     */
    @Override
    public ResponseEntity<ShippingSlipResponse> getForRevisionByTransactionRevisionId(
                    @NotNull @ApiParam(value = "改訂用取引IDに紐づく改訂用配送伝票を取得するクリクエスト", required = true) @Valid
                                    ShippingSlipForRevisionGetRequest shippingSlipForRevisionGetRequest) {

        ShippingSlipEntity shippingSlipByTransactionId =
                        getShippingSlipForRevisionByTransactionRevisionIdUseCase.getShippingSlipByTransactionId(
                                        shippingSlipForRevisionGetRequest.getTransactionRevisionId());
        ShippingSlipResponse shippingSlipResponse =
                        shippingSlipHelper.toShippingSlipResponse(shippingSlipByTransactionId);

        return new ResponseEntity<>(shippingSlipResponse, HttpStatus.OK);
    }

    /**
     * POST /shippings/shipping-slips/for-revision/open : 改訂用配送伝票確定
     * 改訂用配送伝票を確定する
     *
     * @param shippingSlipForRevisionOpenRequest 改訂用配送伝票を確定するリクエスト (required)
     * @return 成功 (status code 200)
     * or システムエラー (status code 200)
     */
    @Override
    public ResponseEntity<Void> openShippingSlipForRevision(
                    @ApiParam(value = "改訂用配送伝票を確定するリクエスト", required = true) @Valid @RequestBody
                                    ShippingSlipForRevisionOpenRequest shippingSlipForRevisionOpenRequest) {

        List<Integer> stockChangedGoodsSeqList = openShippingSlipForRevisionUseCase.openShippingSlipForRevision(
                        shippingSlipForRevisionOpenRequest.getTransactionRevisionId(),
                        !Boolean.FALSE.equals(shippingSlipForRevisionOpenRequest.getInventorySkipFlag())
                                                                                                               );
        openShippingSlipForRevisionUseCase.asyncAfterProcess(stockChangedGoodsSeqList);

        return new ResponseEntity<>(HttpStatus.OK);
    }

    /**
     * POST /shippings/shipping-slips/for-revision : 改訂用配送伝票発行
     * 改訂用配送伝票を発行する
     *
     * @param shippingSlipForRevisionPublishRequest 改訂用配送伝票発行リクエスト (required)
     * @return 成功 (status code 200)
     * or システムエラー (status code 200)
     */
    @Override
    public ResponseEntity<Void> publishShippingSlipForRevision(
                    @ApiParam(value = "改訂用配送伝票の配送先更新リクエスト", required = true) @Valid @RequestBody
                                    ShippingSlipForRevisionPublishRequest shippingSlipForRevisionPublishRequest) {

        publishShippingSlipForRevisionUseCase.publishShippingSlipForRevision(
                        shippingSlipForRevisionPublishRequest.getTransactionId(),
                        shippingSlipForRevisionPublishRequest.getTransactionRevisionId(), getCustomerId()
                                                                            );

        return new ResponseEntity<>(HttpStatus.OK);
    }

    /**
     * PUT /shippings/shipping-slips/for-revision/address : 改訂用配送伝票の配送先更新
     * 改訂用配送伝票の配送先更新
     *
     * @param shippingSettingAddressForRevisionRequest 改訂用配送伝票の配送先更新リクエスト (required)
     * @return 成功 (status code 200)
     * or システムエラー (status code 200)
     */
    @Override
    public ResponseEntity<Void> settingShippingAddressForRevision(
                    @ApiParam(value = "改訂用配送伝票の配送先更新リクエスト", required = true) @Valid @RequestBody
                                    ShippingSettingAddressForRevisionRequest shippingSettingAddressForRevisionRequest) {
        updateShippingAddressForRevisionUseCase.settingShippingAddress(
                        shippingSettingAddressForRevisionRequest.getTransactionRevisionId(),
                        shippingSettingAddressForRevisionRequest.getShippingAddressId()
                                                                      );

        return new ResponseEntity<>(HttpStatus.OK);
    }

    /**
     * PUT /shippings/shipping-slips/for-revision/shipping-condition : 改訂用配送伝票の配送条件を更新する
     * 改訂用配送伝票の配送条件を更新する
     *
     * @param shippingUpdateConditionForRevisionRequest 改訂用配送伝票の配送条件を更新するリクエスト (required)
     * @return 成功 (status code 200)
     * or システムエラー (status code 200)
     */

    @Override
    public ResponseEntity<Void> updateShippingConditionForRevision(
                    @ApiParam(value = "改訂用取引IDに紐づく改訂用配送伝票を取得するリクエスト", required = true) @Valid @RequestBody
                                    ShippingUpdateConditionForRevisionRequest shippingUpdateConditionForRevisionRequest) {

        UpdateShippingConditionDomainParam updateShippingConditionDomainParam =
                        shippingSlipHelper.toUpdateShippingConditionDomainParam(
                                        shippingUpdateConditionForRevisionRequest);

        updateShippingConditionForRevisionUseCase.updateShippingConditionForRevision(
                        shippingUpdateConditionForRevisionRequest.getTransactionRevisionId(),
                        updateShippingConditionDomainParam
                                                                                    );

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

    /**
     * Boolean(null)にboolean(false)をセット<br/>
     * Booleanで宣言されている項目がNullであり、UCメソッドでbooleanの場合に変換する
     *
     * @param target API引数に指定されたBoolean項目
     * @return targetがnullの場合false / 指定されている場合booleanに変換
     */
    private boolean setDefaultBoolean(Boolean target) {

        if (target == null) {
            return false;
        }

        return target;
    }

}