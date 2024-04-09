/*
 * Project Name : HIT-MALL4
 *
 * Copyright (C) 2021 i-TEC HANKYU HANSHIN INC. All Rights Reserved.
 *
 */

package jp.co.itechh.quad.method.presentation.api;

import io.swagger.annotations.ApiParam;
import jp.co.itechh.quad.core.base.utility.ApplicationContextUtility;
import jp.co.itechh.quad.core.constant.type.HTypeFreeDeliveryFlag;
import jp.co.itechh.quad.core.dto.shop.delivery.DeliveryDto;
import jp.co.itechh.quad.core.dto.shop.delivery.DeliveryMethodDetailsDto;
import jp.co.itechh.quad.core.dto.shop.delivery.DeliverySearchForDaoConditionDto;
import jp.co.itechh.quad.core.entity.shop.delivery.DeliveryMethodEntity;
import jp.co.itechh.quad.core.logic.shop.delivery.DeliveryMethodSelectListGetLogic;
import jp.co.itechh.quad.core.service.shop.delivery.AllDeliveryMethodListGetService;
import jp.co.itechh.quad.core.service.shop.delivery.DeliveryMethodDataCheckService;
import jp.co.itechh.quad.core.service.shop.delivery.DeliveryMethodDetailsGetService;
import jp.co.itechh.quad.core.service.shop.delivery.DeliveryMethodRegistService;
import jp.co.itechh.quad.core.service.shop.delivery.DeliveryMethodUpdateService;
import jp.co.itechh.quad.core.util.common.EnumTypeUtil;
import jp.co.itechh.quad.core.web.AbstractController;
import jp.co.itechh.quad.ddd.usecase.method.GetSelectableShippingMethodListUseCase;
import jp.co.itechh.quad.ddd.usecase.method.GetSelectableShippingMethodListUseCaseDto;
import jp.co.itechh.quad.method.presentation.api.param.CarriageShippingMethodListGetRequest;
import jp.co.itechh.quad.method.presentation.api.param.CarriageShippingMethodListResponse;
import jp.co.itechh.quad.method.presentation.api.param.SelectableShippingMethodListGetRequest;
import jp.co.itechh.quad.method.presentation.api.param.SelectableShippingMethodListResponse;
import jp.co.itechh.quad.method.presentation.api.param.ShippingMethodCheckRequest;
import jp.co.itechh.quad.method.presentation.api.param.ShippingMethodListResponse;
import jp.co.itechh.quad.method.presentation.api.param.ShippingMethodListUpdateRequest;
import jp.co.itechh.quad.method.presentation.api.param.ShippingMethodRegistRequest;
import jp.co.itechh.quad.method.presentation.api.param.ShippingMethodResponse;
import jp.co.itechh.quad.method.presentation.api.param.ShippingMethodUpdateRequest;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.RestController;

import javax.validation.Valid;
import javax.validation.constraints.NotNull;
import java.util.ArrayList;
import java.util.List;

/**
 * 配送方法　Controller
 *
 * @author PHAM QUANG DIEU (VJP)
 */
@RestController
public class MethodController extends AbstractController implements ShippingsApi {

    /** 配送方法詳細取得サービス */
    private final DeliveryMethodDetailsGetService deliveryMethodDetailsGetService;

    /** 配送方法　Helper */
    private final MethodHelper methodHelper;

    /** 配送方法登録サービス */
    private final DeliveryMethodRegistService deliveryMethodRegistService;

    /** 配送方法データチェックサービス */
    private final DeliveryMethodDataCheckService deliveryMethodDataCheckService;

    /** 配送方法更新サービス */
    private final DeliveryMethodUpdateService deliveryMethodUpdateService;

    /** 全配送方法エンティティリスト取得サービス */
    private AllDeliveryMethodListGetService allDeliveryMethodListGetService;

    /** ここからDDD設計範囲 */

    /** 選択可能配送方法一覧取得ユースケース */
    private final GetSelectableShippingMethodListUseCase getSelectableShippingMethodListUseCase;

    /** 配送方法別送料リスト取得ロジック */
    private final DeliveryMethodSelectListGetLogic deliveryMethodSelectListGetLogic;

    /** ここまでDDD設計範囲 */

    /**
     * 配送方法コンストラクター
     *
     * @param methodHelper                           配送方法　Helper
     * @param deliveryMethodDetailsGetService        配送方法詳細取得サービス
     * @param deliveryMethodRegistService            配送方法登録サービス
     * @param deliveryMethodDataCheckService         配送方法データチェックサービス
     * @param deliveryMethodUpdateService            配送方法更新サービス
     * @param allDeliveryMethodListGetService        全配送方法エンティティリスト取得サービス
     * @param getSelectableShippingMethodListUseCase 選択可能配送方法一覧取得ユースケース
     * @param deliveryMethodSelectListGetLogic       配送方法別送料リスト取得ロジック
     */
    public MethodController(MethodHelper methodHelper,
                            DeliveryMethodDetailsGetService deliveryMethodDetailsGetService,
                            DeliveryMethodRegistService deliveryMethodRegistService,
                            DeliveryMethodDataCheckService deliveryMethodDataCheckService,
                            DeliveryMethodUpdateService deliveryMethodUpdateService,
                            AllDeliveryMethodListGetService allDeliveryMethodListGetService,
                            GetSelectableShippingMethodListUseCase getSelectableShippingMethodListUseCase,
                            DeliveryMethodSelectListGetLogic deliveryMethodSelectListGetLogic) {
        this.methodHelper = methodHelper;
        this.deliveryMethodDetailsGetService = deliveryMethodDetailsGetService;
        this.deliveryMethodRegistService = deliveryMethodRegistService;
        this.deliveryMethodDataCheckService = deliveryMethodDataCheckService;
        this.deliveryMethodUpdateService = deliveryMethodUpdateService;
        this.allDeliveryMethodListGetService = allDeliveryMethodListGetService;
        this.getSelectableShippingMethodListUseCase = getSelectableShippingMethodListUseCase;
        this.deliveryMethodSelectListGetLogic = deliveryMethodSelectListGetLogic;
    }

    /**
     * GET /shippings/methods/{deliveryMethodSeq} : 配送方法取得
     * 配送方法取得
     *
     * @param deliveryMethodSeq 配送方法SEQ (required)
     * @return 配送方法レスポンス (status code 200)
     * or その他エラー (status code 500)
     */
    @Override
    public ResponseEntity<ShippingMethodResponse> getByDeliveryMethodSeq(Integer deliveryMethodSeq) {

        // 配送方法詳細取得サービス実行
        DeliveryMethodDetailsDto deliveryMethodDetailsDto = deliveryMethodDetailsGetService.execute(deliveryMethodSeq);
        ShippingMethodResponse shippingMethodResponse = methodHelper.toShippingMethodResponse(deliveryMethodDetailsDto);

        return new ResponseEntity<>(shippingMethodResponse, HttpStatus.OK);
    }

    /**
     * GET /shippings/methods : 配送方法一覧取得
     * 配送方法一覧取得
     *
     * @return 配送方法一覧レスポンス (status code 200)
     * or その他エラー (status code 200)
     */
    @Override
    public ResponseEntity<ShippingMethodListResponse> get() {
        Integer shopSeq = 1001;

        // 配送方法エンティティリスト取得サービス実行
        List<DeliveryMethodEntity> deliveryMethodEntityList = allDeliveryMethodListGetService.execute(shopSeq);

        ShippingMethodListResponse shippingMethodListResponse = new ShippingMethodListResponse();

        if (deliveryMethodEntityList != null && !deliveryMethodEntityList.isEmpty()) {
            shippingMethodListResponse = methodHelper.toShippingMethodListResponse(deliveryMethodEntityList);
        }
        return new ResponseEntity<>(shippingMethodListResponse, HttpStatus.OK);
    }

    /**
     * POST /shippings/methods : 配送方法登録
     * 配送方法登録
     *
     * @param shippingMethodRegistRequest 配送方法登録リクエスト (required)
     * @return 配送方法レスポンス (status code 200)
     * or その他エラー (status code 500)
     */
    @Override
    public ResponseEntity<ShippingMethodResponse> regist(
                    @Valid ShippingMethodRegistRequest shippingMethodRegistRequest) {
        // 配送方法の詳細Dtoに変換する
        DeliveryMethodDetailsDto deliveryMethodDetailsDto =
                        methodHelper.toDeliveryMethodDetailsDto(shippingMethodRegistRequest);

        // 配送方法登録サービス実行
        deliveryMethodRegistService.execute(deliveryMethodDetailsDto);

        DeliveryMethodDetailsDto newDeliveryMethodDetailsDto =
                        ApplicationContextUtility.getBean(DeliveryMethodDetailsDto.class);

        if (deliveryMethodDetailsDto.getDeliveryMethodEntity().getDeliveryMethodSeq() != null) {
            int deliveryMethodSeq = deliveryMethodDetailsDto.getDeliveryMethodEntity().getDeliveryMethodSeq();
            newDeliveryMethodDetailsDto = deliveryMethodDetailsGetService.execute(deliveryMethodSeq);
        }

        ShippingMethodResponse shippingMethodResponse =
                        methodHelper.toShippingMethodResponse(newDeliveryMethodDetailsDto);

        return new ResponseEntity<>(shippingMethodResponse, HttpStatus.OK);
    }

    /**
     * POST /shippings/methods/check : 配送方法データチェック
     * 配送方法データチェック
     *
     * @param shippingMethodCheckRequest 配送方法リクエスト (required)
     * @return 成功 (status code 200)
     * or その他エラー (status code 500)
     */
    @Override
    public ResponseEntity<Void> check(@Valid ShippingMethodCheckRequest shippingMethodCheckRequest) {
        // 配送方法エンティティに変換
        DeliveryMethodEntity deliveryMethodEntity = methodHelper.toDeliveryMethodEntity(shippingMethodCheckRequest);

        // 配送方法データチェックサービス実行
        deliveryMethodDataCheckService.execute(deliveryMethodEntity);

        return new ResponseEntity<>(HttpStatus.OK);
    }

    /**
     * PUT /shippings/methods/{deliveryMethodSeq} : 配送方法更新
     * 配送方法更新
     *
     * @param deliveryMethodSeq           配送方法SEQ (required)
     * @param shippingMethodUpdateRequest 配送方法更新リクエスト (required)
     * @return 配送方法更新リクエスト (status code 200)
     * or その他エラー (status code 500)
     */
    @Override
    public ResponseEntity<ShippingMethodResponse> update(Integer deliveryMethodSeq,
                                                         @Valid
                                                                         ShippingMethodUpdateRequest shippingMethodUpdateRequest) {
        // 配送方法の更新リクエストを配送方法詳細Dtoに変換する
        DeliveryMethodDetailsDto deliveryMethodDetailsDto =
                        methodHelper.updateRequestToDeliveryMethodDetailsDto(shippingMethodUpdateRequest);

        // 配送方法更新サービス実行
        deliveryMethodUpdateService.execute(deliveryMethodDetailsDto);

        DeliveryMethodDetailsDto deliveryMethodDetailsDtoUpdated =
                        deliveryMethodDetailsGetService.execute(deliveryMethodSeq);
        ShippingMethodResponse shippingMethodResponse =
                        methodHelper.toShippingMethodResponse(deliveryMethodDetailsDtoUpdated);

        return new ResponseEntity<>(shippingMethodResponse, HttpStatus.OK);
    }

    /**
     * PUT /shippings/methods : 配送方法一覧更新
     * 配送方法一覧更新
     *
     * @param shippingMethodListUpdateRequest 配送方法一覧更新リクエスト (required)
     * @return 配送方法一覧レスポンス (status code 200)
     * or その他エラー (status code 500)
     */
    @Override
    public ResponseEntity<ShippingMethodListResponse> updateList(
                    @Valid ShippingMethodListUpdateRequest shippingMethodListUpdateRequest) {
        // 返却用のリストを作成
        ShippingMethodListResponse shippingMethodListResponse = new ShippingMethodListResponse();

        // 配送方法エンティティリストに変換
        List<DeliveryMethodEntity> deliveryMethodEntityList =
                        methodHelper.toDeliveryMethodEntityList(shippingMethodListUpdateRequest);

        // 配送方法更新サービス実行
        deliveryMethodUpdateService.execute(deliveryMethodEntityList);

        for (DeliveryMethodEntity deliveryMethodEntity : deliveryMethodEntityList) {
            DeliveryMethodDetailsDto deliveryMethodDetailsDto =
                            deliveryMethodDetailsGetService.execute(deliveryMethodEntity.getDeliveryMethodSeq());
            ShippingMethodResponse shippingMethodResponse =
                            methodHelper.toShippingMethodResponse(deliveryMethodDetailsDto);
            if (shippingMethodListResponse.getShippingMethodListResponse() == null) {
                shippingMethodListResponse.setShippingMethodListResponse(new ArrayList<>());
            }
            shippingMethodListResponse.getShippingMethodListResponse().add(shippingMethodResponse);
        }

        return new ResponseEntity<>(shippingMethodListResponse, HttpStatus.OK);
    }

    /** ここからDDD設計範囲 */

    /**
     * GET /shippings/methods/selectable : 選択可能配送方法一覧取得
     *
     * @param selectableShippingMethodListGetRequest 選択可能配送方法一覧取得リクエスト (required)
     * @return 選択可能配送方法一覧レスポンス (status code 200) or システムエラー (status code 200)
     */
    @Override
    public ResponseEntity<SelectableShippingMethodListResponse> getSelectable(
                    @NotNull @ApiParam(value = "選択可能配送方法一覧取得リクエスト", required = true) @Valid
                                    SelectableShippingMethodListGetRequest selectableShippingMethodListGetRequest) {

        List<GetSelectableShippingMethodListUseCaseDto> dtoList =
                        this.getSelectableShippingMethodListUseCase.getSelectableShippingMethodList(
                                        selectableShippingMethodListGetRequest.getTransactionId());

        return new ResponseEntity<>(this.methodHelper.toSelectableShippingMethodListResponse(dtoList), HttpStatus.OK);
    }

    /**
     * GET /shippings/methods/carriage : 選択可能配送方法一覧取得
     * お届け希望日/お届け時間帯一覧を含む選択可能配送方法情報を取得
     *
     * @param carriageShippingMethodListGetRequest 選択可能配送方法一覧取得リクエスト (required)
     * @return 選択可能配送方法一覧レスポンス (status code 200)
     * or Bad Request（バリデーションエラー、業務ルールエラー、妥当性チェックエラーの場合） (status code 400)
     * or Internal Server Error（アサートエラー、予想外エラーの場合） (status code 500)
     * or その他エラー (status code 200)
     */
    @Override
    public ResponseEntity<CarriageShippingMethodListResponse> getCarriage(
                    @NotNull @ApiParam(value = "選択可能配送方法一覧取得リクエスト", required = true) @Valid
                                    CarriageShippingMethodListGetRequest carriageShippingMethodListGetRequest) {

        DeliverySearchForDaoConditionDto deliverySearchForDaoConditionDto =
                        methodHelper.toDeliverySearchForDaoConditionDto(carriageShippingMethodListGetRequest);
        List<Integer> deliveryMethodSeqList = new ArrayList<>();

        deliveryMethodSeqList.add(carriageShippingMethodListGetRequest.getDeliveryMethodSeq());

        List<DeliveryDto> deliveryDtos = deliveryMethodSelectListGetLogic.execute(deliverySearchForDaoConditionDto,
                                                                                  deliveryMethodSeqList,
                                                                                  EnumTypeUtil.getEnumFromValue(
                                                                                                  HTypeFreeDeliveryFlag.class,
                                                                                                  carriageShippingMethodListGetRequest.getFreeDeliveryFlag()
                                                                                                               )
                                                                                 );

        CarriageShippingMethodListResponse carriageShippingMethodListResponse =
                        methodHelper.toCarriageShippingMethodListResponse(deliveryDtos);

        return new ResponseEntity<>(carriageShippingMethodListResponse, HttpStatus.OK);
    }

    /** ここまでDDD設計範囲 */

}