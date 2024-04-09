/*
 * Project Name : HIT-MALL4
 *
 * Copyright (C) 2021 i-TEC HANKYU HANSHIN INC. All Rights Reserved.
 *
 */

package jp.co.itechh.quad.specialchargearea.presentation.api;

import jp.co.itechh.quad.core.base.utility.ApplicationContextUtility;
import jp.co.itechh.quad.core.dto.shop.delivery.DeliverySpecialChargeAreaConditionDto;
import jp.co.itechh.quad.core.dto.shop.delivery.DeliverySpecialChargeAreaResultDto;
import jp.co.itechh.quad.core.entity.shop.delivery.DeliverySpecialChargeAreaEntity;
import jp.co.itechh.quad.core.logic.shop.delivery.DeliverySpecialChargeAreaGetLogic;
import jp.co.itechh.quad.core.service.shop.delivery.DeliverySpecialChargeAreaListDeleteService;
import jp.co.itechh.quad.core.service.shop.delivery.DeliverySpecialChargeAreaRegistService;
import jp.co.itechh.quad.core.service.shop.delivery.DeliverySpecialChargeAreaSearchService;
import jp.co.itechh.quad.core.web.AbstractController;
import jp.co.itechh.quad.core.web.PageInfoModule;
import jp.co.itechh.quad.specialchargearea.presentation.api.param.PageInfoRequest;
import jp.co.itechh.quad.specialchargearea.presentation.api.param.PageInfoResponse;
import jp.co.itechh.quad.specialchargearea.presentation.api.param.SpecialChargeAreaDeleteRequest;
import jp.co.itechh.quad.specialchargearea.presentation.api.param.SpecialChargeAreaListGetRequest;
import jp.co.itechh.quad.specialchargearea.presentation.api.param.SpecialChargeAreaListResponse;
import jp.co.itechh.quad.specialchargearea.presentation.api.param.SpecialChargeAreaRegistRequest;
import jp.co.itechh.quad.specialchargearea.presentation.api.param.SpecialChargeAreaResponse;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.RestController;

import javax.validation.Valid;
import javax.validation.constraints.NotNull;
import java.lang.reflect.InvocationTargetException;
import java.util.List;

/**
 * 特別料金エリア Controller.
 *
 * @author PHAM QUANG DIEU (VJP)
 */
@RestController
public class SpecialChargeAreaController extends AbstractController implements ShippingsApi {

    /**
     * 配送特別料金エリア検索サービス
     */
    private final DeliverySpecialChargeAreaSearchService deliverySpecialChargeAreaSearchService;

    /**
     * 配送特別料金エリア登録サービス
     */
    private final DeliverySpecialChargeAreaRegistService deliverySpecialChargeAreaRegistService;

    /**
     * 配送特別料金エリア削除サービス
     */
    private final DeliverySpecialChargeAreaListDeleteService deliverySpecialChargeAreaListDeleteService;

    /**
     * 特別料金エリア Helper
     */
    private final SpecialChargeAreaHelper specialChargeAreasHelper;

    /**
     * 配送特別料金エリアエンティティ取得Logic
     */
    private final DeliverySpecialChargeAreaGetLogic deliverySpecialChargeAreaGetLogic;

    /** メッセージコード：チェックボックスがチェックされていない */
    protected static final String MSGCD_NO_CHECK = "AYD000402";

    /**
     * ロガー
     */
    private static final Logger LOGGER = LoggerFactory.getLogger(SpecialChargeAreaController.class);

    /**
     * コンストラクタ
     *
     * @param deliverySpecialChargeAreaSearchService     配送特別料金エリア検索サービス
     * @param deliverySpecialChargeAreaRegistService     配送特別料金エリア登録サービス
     * @param deliverySpecialChargeAreaListDeleteService 配送特別料金エリア削除サービス
     * @param specialChargeAreasHelper                   特別料金エリア Helper
     * @param deliverySpecialChargeAreaGetLogic          配送特別料金エリアエンティティ取得Logic
     */
    public SpecialChargeAreaController(DeliverySpecialChargeAreaSearchService deliverySpecialChargeAreaSearchService,
                                       DeliverySpecialChargeAreaRegistService deliverySpecialChargeAreaRegistService,
                                       DeliverySpecialChargeAreaListDeleteService deliverySpecialChargeAreaListDeleteService,
                                       SpecialChargeAreaHelper specialChargeAreasHelper,
                                       DeliverySpecialChargeAreaGetLogic deliverySpecialChargeAreaGetLogic) {
        this.deliverySpecialChargeAreaSearchService = deliverySpecialChargeAreaSearchService;
        this.deliverySpecialChargeAreaRegistService = deliverySpecialChargeAreaRegistService;
        this.deliverySpecialChargeAreaListDeleteService = deliverySpecialChargeAreaListDeleteService;
        this.specialChargeAreasHelper = specialChargeAreasHelper;
        this.deliverySpecialChargeAreaGetLogic = deliverySpecialChargeAreaGetLogic;
    }

    /**
     * GET /shippings/methods/{deliveryMethodSeq}/special-charge-areas : 特別料金エリア一覧取得
     * 特別料金エリア一覧取得
     *
     * @param deliveryMethodSeq               配送方法SEQ (required)
     * @param specialChargeAreaListGetRequest 特別料金エリア一覧取得リクエスト (required)
     * @param pageInfoRequest                 ページ情報リクエスト（ページネーションのため） (optional)
     * @return 特別料金エリア一覧レスポンス (status code 200)
     * or その他エラー (status code 500)
     */
    @Override
    public ResponseEntity<SpecialChargeAreaListResponse> get(Integer deliveryMethodSeq,
                                                             @NotNull @Valid
                                                                             SpecialChargeAreaListGetRequest specialChargeAreaListGetRequest,
                                                             @Valid PageInfoRequest pageInfoRequest) {
        // 検索条件作成
        DeliverySpecialChargeAreaConditionDto conditionDto =
                        specialChargeAreasHelper.toDeliverySpecialChargeAreaConditionDtoForSearch(
                                        specialChargeAreaListGetRequest, deliveryMethodSeq);

        // ページング検索セットアップ
        PageInfoModule pageInfoModule = ApplicationContextUtility.getBean(PageInfoModule.class);
        pageInfoModule.setupPageInfo(conditionDto, pageInfoRequest.getPage(), pageInfoRequest.getLimit(),
                                     pageInfoRequest.getOrderBy(), pageInfoRequest.getSort()
                                    );

        List<DeliverySpecialChargeAreaResultDto> resultList =
                        deliverySpecialChargeAreaSearchService.execute(conditionDto);

        SpecialChargeAreaListResponse specialChargeAreaListResponse =
                        specialChargeAreasHelper.toSpecialChargeAreaListResponse(resultList);

        // ページ情報レスポンスを設定
        PageInfoResponse pageInfoResponse = new PageInfoResponse();
        try {
            pageInfoModule.setupResponsePager(conditionDto, pageInfoResponse);
        } catch (InvocationTargetException | IllegalAccessException e) {
            LOGGER.info("ページ情報レスポンスの設定異常： " + e.getMessage());
        }
        specialChargeAreaListResponse.setPageInfo(pageInfoResponse);

        return new ResponseEntity<>(specialChargeAreaListResponse, HttpStatus.OK);
    }

    /**
     * POST /shippings/methods/{deliveryMethodSeq}/special-charge-areas : 特別料金エリア登録
     * 特別料金エリア登録
     *
     * @param deliveryMethodSeq              配送方法SEQ (required)
     * @param specialChargeAreaRegistRequest 特別料金エリア登録リクエスト (required)
     * @return 特別料金エリアレスポンス (status code 200)
     * or その他エラー (status code 400)
     */
    @Override
    public ResponseEntity<SpecialChargeAreaResponse> regist(Integer deliveryMethodSeq,
                                                            @Valid
                                                                            SpecialChargeAreaRegistRequest specialChargeAreaRegistRequest) {

        DeliverySpecialChargeAreaEntity entity =
                        specialChargeAreasHelper.toDeliverySpecialChargeAreaEntity(specialChargeAreaRegistRequest,
                                                                                   deliveryMethodSeq
                                                                                  );
        deliverySpecialChargeAreaRegistService.execute(entity);
        DeliverySpecialChargeAreaEntity entityRes =
                        deliverySpecialChargeAreaGetLogic.execute(entity.getDeliveryMethodSeq(), entity.getZipCode());
        SpecialChargeAreaResponse specialChargeAreaResponse =
                        specialChargeAreasHelper.toSpecialChargeAreaResponse(entityRes);

        return new ResponseEntity<>(specialChargeAreaResponse, HttpStatus.OK);
    }

    /**
     * DELETE /shippings/methods/{deliveryMethodSeq}/special-charge-areas : 特別料金エリア削除
     * 特別料金エリア削除
     *
     * @param deliveryMethodSeq              配送方法SEQ (required)
     * @param specialChargeAreaDeleteRequest 特別料金エリア削除リクエスト (required)
     * @return 成功 (status code 200)
     * or その他エラー (status code 500)
     */
    @Override
    public ResponseEntity<Void> delete(Integer deliveryMethodSeq,
                                       @NotNull @Valid SpecialChargeAreaDeleteRequest specialChargeAreaDeleteRequest) {
        List<DeliverySpecialChargeAreaEntity> deleteList =
                        specialChargeAreasHelper.toDeliverySpecialChargeAreaEntity(specialChargeAreaDeleteRequest,
                                                                                   deliveryMethodSeq
                                                                                  );

        if ((deleteList == null) || deleteList.isEmpty()) {
            throwMessage(MSGCD_NO_CHECK);
        }

        deliverySpecialChargeAreaListDeleteService.execute(deleteList);

        return new ResponseEntity<>(HttpStatus.OK);
    }
}