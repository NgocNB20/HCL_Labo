/*
 * Project Name : HIT-MALL4
 *
 * Copyright (C) 2021 i-TEC HANKYU HANSHIN INC. All Rights Reserved.
 *
 */

package jp.co.itechh.quad.specialchargearea.presentation.api;

import jp.co.itechh.quad.core.base.utility.DateUtility;
import jp.co.itechh.quad.core.dto.shop.delivery.DeliverySpecialChargeAreaConditionDto;
import jp.co.itechh.quad.core.dto.shop.delivery.DeliverySpecialChargeAreaResultDto;
import jp.co.itechh.quad.core.entity.shop.delivery.DeliverySpecialChargeAreaEntity;
import jp.co.itechh.quad.specialchargearea.presentation.api.param.SpecialChargeAreaDeleteRequest;
import jp.co.itechh.quad.specialchargearea.presentation.api.param.SpecialChargeAreaListGetRequest;
import jp.co.itechh.quad.specialchargearea.presentation.api.param.SpecialChargeAreaListResponse;
import jp.co.itechh.quad.specialchargearea.presentation.api.param.SpecialChargeAreaRegistRequest;
import jp.co.itechh.quad.specialchargearea.presentation.api.param.SpecialChargeAreaResponse;
import org.apache.commons.lang3.StringUtils;
import org.springframework.stereotype.Component;

import java.util.ArrayList;
import java.util.List;

/**
 * 特別料金エリア Helper.
 *
 *  @author PHAM QUANG DIEU (VJP)
 *
 */
@Component
public class SpecialChargeAreaHelper {

    /** 日付ユーティリティ */
    private final DateUtility dateUtility;

    /**
     * コンストラクタ
     *
     * @param dateUtility 日付ユーティリティ
     */
    public SpecialChargeAreaHelper(DateUtility dateUtility) {
        this.dateUtility = dateUtility;
    }

    /**
     * 配送特別料金エリア検索条件Dtoに変換
     *
     * @param getRequest 特別料金エリア登録リクエスト
     * @param deliveryMethodSeq 配送方法SEQ
     * @return 配送特別料金エリア検索条件Dto
     */
    public DeliverySpecialChargeAreaConditionDto toDeliverySpecialChargeAreaConditionDtoForSearch(
                    SpecialChargeAreaListGetRequest getRequest,
                    Integer deliveryMethodSeq) {
        DeliverySpecialChargeAreaConditionDto deliverySpecialChargeAreaConditionDto =
                        new DeliverySpecialChargeAreaConditionDto();

        deliverySpecialChargeAreaConditionDto.setDeliveryMethodSeq(deliveryMethodSeq);
        if (StringUtils.isEmpty(getRequest.getPrefecture())) {
            deliverySpecialChargeAreaConditionDto.setPrefecture("");
        } else {
            deliverySpecialChargeAreaConditionDto.setPrefecture(getRequest.getPrefecture());
        }
        return deliverySpecialChargeAreaConditionDto;
    }

    /**
     * 配送特別料金エリアに変換
     *
     * @param registRequest 特別料金エリア登録リクエスト
     * @param deliveryMethodSeq 配送方法SEQ
     * @return 配送特別料金エリア
     */
    public DeliverySpecialChargeAreaEntity toDeliverySpecialChargeAreaEntity(SpecialChargeAreaRegistRequest registRequest,
                                                                             Integer deliveryMethodSeq) {

        DeliverySpecialChargeAreaEntity deliverySpecialChargeAreaEntity = new DeliverySpecialChargeAreaEntity();

        deliverySpecialChargeAreaEntity.setDeliveryMethodSeq(deliveryMethodSeq);
        deliverySpecialChargeAreaEntity.setZipCode(registRequest.getZipCode());
        deliverySpecialChargeAreaEntity.setCarriage(registRequest.getCarriage());
        deliverySpecialChargeAreaEntity.setRegistTime(dateUtility.getCurrentTime());
        deliverySpecialChargeAreaEntity.setUpdateTime(dateUtility.getCurrentTime());

        return deliverySpecialChargeAreaEntity;
    }

    /**
     * 配送特別料金エリアリストに変換
     *
     * @param deleteRequest 特別料金エリア登録リクエスト
     * @param deliveryMethodSeq 配送方法SEQ
     * @return 配送特別料金エリアリスト
     */
    public List<DeliverySpecialChargeAreaEntity> toDeliverySpecialChargeAreaEntity(SpecialChargeAreaDeleteRequest deleteRequest,
                                                                                   Integer deliveryMethodSeq) {
        List<DeliverySpecialChargeAreaEntity> deliverySpecialChargeAreaList = new ArrayList<>();

        if (deleteRequest.getDeleteList() != null && !deleteRequest.getDeleteList().isEmpty()) {
            for (String zipCodeItem : deleteRequest.getDeleteList()) {
                DeliverySpecialChargeAreaEntity deliverySpecialChargeAreaEntity = new DeliverySpecialChargeAreaEntity();

                deliverySpecialChargeAreaEntity.setDeliveryMethodSeq(deliveryMethodSeq);
                deliverySpecialChargeAreaEntity.setZipCode(zipCodeItem);

                deliverySpecialChargeAreaList.add(deliverySpecialChargeAreaEntity);
            }
        }

        return deliverySpecialChargeAreaList;
    }

    /**
     * 特別料金エリア一覧レスポンスに変換
     *
     * @param resultDtoList 配送特別料金エリア詳細Dto一覧
     * @return 特別料金エリア一覧レスポンス
     */
    public SpecialChargeAreaListResponse toSpecialChargeAreaListResponse(List<DeliverySpecialChargeAreaResultDto> resultDtoList) {
        List<SpecialChargeAreaResponse> specialChargeAreaResponses = toSpecialChargeAreaResponseList(resultDtoList);

        SpecialChargeAreaListResponse specialChargeAreaListResponse = new SpecialChargeAreaListResponse();

        if (specialChargeAreaResponses.size() > 0) {
            specialChargeAreaListResponse.setSpecialChargeAreaList(specialChargeAreaResponses);
        }

        return specialChargeAreaListResponse;
    }

    /**
     * 配送特別料金エリアレスポンスに変換
     *
     * @param entity 配送特別料金エリア
     * @return 特別料金エリアレスポンス
     */
    public SpecialChargeAreaResponse toSpecialChargeAreaResponse(DeliverySpecialChargeAreaEntity entity) {
        SpecialChargeAreaResponse specialChargeAreaResponse = new SpecialChargeAreaResponse();

        specialChargeAreaResponse.setDeliveryMethodSeq(entity.getDeliveryMethodSeq());
        specialChargeAreaResponse.setZipcode(entity.getZipCode());
        specialChargeAreaResponse.setCarriage(entity.getCarriage());
        specialChargeAreaResponse.setRegistTime(entity.getRegistTime());
        specialChargeAreaResponse.setUpdateTime(entity.getUpdateTime());

        return specialChargeAreaResponse;
    }

    /**
     * 特別料金エリア一覧レスポンスに変換
     *
     * @param resultDtoList 配送区分別送料エンティティリスト
     * @return 特別料金エリア一覧レスポンス
     */
    private List<SpecialChargeAreaResponse> toSpecialChargeAreaResponseList(List<DeliverySpecialChargeAreaResultDto> resultDtoList) {
        List<SpecialChargeAreaResponse> listSpecialChargeAreaResponse = new ArrayList<>();

        if (resultDtoList.size() > 0) {
            for (DeliverySpecialChargeAreaResultDto deliverySpecialChargeAreaResult : resultDtoList) {
                SpecialChargeAreaResponse specialChargeAreaResponse = new SpecialChargeAreaResponse();
                specialChargeAreaResponse.setDeliveryMethodSeq(deliverySpecialChargeAreaResult.getDeliveryMethodSeq());
                specialChargeAreaResponse.setZipcode(deliverySpecialChargeAreaResult.getZipcode());
                specialChargeAreaResponse.setCarriage(deliverySpecialChargeAreaResult.getCarriage());
                specialChargeAreaResponse.setPrefecture(deliverySpecialChargeAreaResult.getPrefecture());
                specialChargeAreaResponse.setCity(deliverySpecialChargeAreaResult.getCity());
                specialChargeAreaResponse.setTown(deliverySpecialChargeAreaResult.getTown());
                specialChargeAreaResponse.setNumbers(deliverySpecialChargeAreaResult.getNumbers());
                specialChargeAreaResponse.setAddressList(deliverySpecialChargeAreaResult.getAddressList());
                listSpecialChargeAreaResponse.add(specialChargeAreaResponse);
            }
        }
        return listSpecialChargeAreaResponse;
    }
}