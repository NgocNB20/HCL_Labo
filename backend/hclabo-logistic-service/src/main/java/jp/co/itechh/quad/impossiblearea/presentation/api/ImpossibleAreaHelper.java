package jp.co.itechh.quad.impossiblearea.presentation.api;

import jp.co.itechh.quad.core.base.utility.ApplicationContextUtility;
import jp.co.itechh.quad.core.base.utility.DateUtility;
import jp.co.itechh.quad.core.dto.shop.delivery.DeliveryImpossibleAreaConditionDto;
import jp.co.itechh.quad.core.dto.shop.delivery.DeliveryImpossibleAreaResultDto;
import jp.co.itechh.quad.core.entity.shop.delivery.DeliveryImpossibleAreaEntity;
import jp.co.itechh.quad.impossiblearea.presentation.api.param.ImpossibleAreaDeleteRequest;
import jp.co.itechh.quad.impossiblearea.presentation.api.param.ImpossibleAreaListGetRequest;
import jp.co.itechh.quad.impossiblearea.presentation.api.param.ImpossibleAreaListResponse;
import jp.co.itechh.quad.impossiblearea.presentation.api.param.ImpossibleAreaRegistRequest;
import jp.co.itechh.quad.impossiblearea.presentation.api.param.ImpossibleAreaResponse;
import org.apache.commons.collections.CollectionUtils;
import org.apache.commons.lang3.StringUtils;
import org.springframework.stereotype.Component;

import java.util.ArrayList;
import java.util.List;

/**
 * 配送不可能エリア Helper
 *
 * @author PHAM QUANG DIEU (VJP)
 *
 */
@Component
public class ImpossibleAreaHelper {

    /** 日付関連Utilityクラス */
    private final DateUtility dateUtility;

    /**
     * コンストラクター
     *
     * @param dateUtility 日付関連Utilityクラス
     */
    public ImpossibleAreaHelper(DateUtility dateUtility) {
        this.dateUtility = dateUtility;
    }

    /**
     * 配送特別料金エリア検索条件Dtoに変換します
     *
     * @param deliveryMethodSeq 配送方法SEQ
     * @param request 配送不可能エリア一覧リクエスト
     *
     * @return 配送特別料金エリア検索条件Dtoクラス
     */
    public DeliveryImpossibleAreaConditionDto toDeliveryImpossibleAreaConditionDto(Integer deliveryMethodSeq,
                                                                                   ImpossibleAreaListGetRequest request) {

        DeliveryImpossibleAreaConditionDto conditionDto = new DeliveryImpossibleAreaConditionDto();
        conditionDto.setDeliveryMethodSeq(deliveryMethodSeq);

        if (StringUtils.isEmpty(request.getPrefecture())) {
            conditionDto.setPrefecture("");
        } else {
            conditionDto.setPrefecture(request.getPrefecture());
        }
        return conditionDto;
    }

    /**
     * 画面入力値を配送特別料金エンティティに変換します
     *
     * @param impossibleAreaRegistRequest 配送不可能エリア登録リクエスト
     * @return 配送不可能エリアクラス
     */
    public DeliveryImpossibleAreaEntity toDeliveryImpossibleAreaEntity(ImpossibleAreaRegistRequest impossibleAreaRegistRequest,
                                                                       Integer deliveryMethodSeq) {
        DeliveryImpossibleAreaEntity entity = ApplicationContextUtility.getBean(DeliveryImpossibleAreaEntity.class);

        entity.setDeliveryMethodSeq(deliveryMethodSeq);
        entity.setZipCode(impossibleAreaRegistRequest.getZipCode());
        entity.setRegistTime(dateUtility.getCurrentTime());
        entity.setUpdateTime(dateUtility.getCurrentTime());

        return entity;
    }

    /**
     * レスポンスに変換
     *
     * @param entity 配送不可能エリアクラス
     * @return 配送不可能エリアレスポンス
     */
    public ImpossibleAreaResponse toImpossibleAreaResponse(DeliveryImpossibleAreaEntity entity) {
        ImpossibleAreaResponse impossibleAreaResponse = new ImpossibleAreaResponse();

        impossibleAreaResponse.setDeliveryMethodSeq(entity.getDeliveryMethodSeq());
        impossibleAreaResponse.setZipcode(entity.getZipCode());
        impossibleAreaResponse.setRegistTime(entity.getRegistTime());
        impossibleAreaResponse.setUpdateTime(entity.getUpdateTime());

        return impossibleAreaResponse;
    }

    /**
     * レスポンスに変換
     *
     * @param impossibleAreaResultDtoList 配送不可能エリア詳細Dtoリスト
     * @return 不可能なエリアリストのレスポンス
     */
    public ImpossibleAreaListResponse toImpossibleAreaListResponse(List<DeliveryImpossibleAreaResultDto> impossibleAreaResultDtoList) {
        List<ImpossibleAreaResponse> impossibleAreaResponses =
                        toImpossibleAreaResponseList(impossibleAreaResultDtoList);

        ImpossibleAreaListResponse impossibleAreaListResponse = new ImpossibleAreaListResponse();
        if (impossibleAreaResponses.size() > 0) {
            impossibleAreaListResponse.setImpossibleAreaList(impossibleAreaResponses);
        }

        return impossibleAreaListResponse;
    }

    /**
     * ImpossibleAreaDeleteRequestから削除対象リストを作成します
     *
     * @param impossibleAreaDeleteRequest 不可能なエリア削除リクエスト
     * @param deliveryMethodSeq           配送方法SEQ
     * @return 配達不可能エリアリスト list
     */
    public List<DeliveryImpossibleAreaEntity> toDeliveryImpossibleAreaEntity(ImpossibleAreaDeleteRequest impossibleAreaDeleteRequest,
                                                                             Integer deliveryMethodSeq) {
        List<DeliveryImpossibleAreaEntity> deliveryImpossibleAreaList = new ArrayList<>();

        if (CollectionUtils.isNotEmpty(impossibleAreaDeleteRequest.getDeleteList())) {
            for (String zipCodeItem : impossibleAreaDeleteRequest.getDeleteList()) {
                DeliveryImpossibleAreaEntity deliveryImpossibleAreaEntity =
                                ApplicationContextUtility.getBean(DeliveryImpossibleAreaEntity.class);
                deliveryImpossibleAreaEntity.setDeliveryMethodSeq(deliveryMethodSeq);
                deliveryImpossibleAreaEntity.setZipCode(zipCodeItem);
                deliveryImpossibleAreaList.add(deliveryImpossibleAreaEntity);
            }
        }

        return deliveryImpossibleAreaList;
    }

    /**
     * 配送不可能エリアレスポンスリストに変換
     *
     * @param deliveryImpossibleAreaResultDto 配信不可能な領域の結果Dto
     * @return リスト不可能な領域のレスポンス
     */
    private List<ImpossibleAreaResponse> toImpossibleAreaResponseList(List<DeliveryImpossibleAreaResultDto> deliveryImpossibleAreaResultDto) {
        List<ImpossibleAreaResponse> listImpossibleAreaResponse = new ArrayList<>();
        if (deliveryImpossibleAreaResultDto.size() > 0) {
            for (DeliveryImpossibleAreaResultDto deliveryImpossibleAreaResult : deliveryImpossibleAreaResultDto) {
                ImpossibleAreaResponse impossibleAreaResponse = new ImpossibleAreaResponse();
                impossibleAreaResponse.setDeliveryMethodSeq(deliveryImpossibleAreaResult.getDeliveryMethodSeq());
                impossibleAreaResponse.setZipcode(deliveryImpossibleAreaResult.getZipcode());
                impossibleAreaResponse.setPrefecture(deliveryImpossibleAreaResult.getPrefecture());
                impossibleAreaResponse.setCity(deliveryImpossibleAreaResult.getCity());
                impossibleAreaResponse.setTown(deliveryImpossibleAreaResult.getTown());
                impossibleAreaResponse.setNumbers(deliveryImpossibleAreaResult.getNumbers());
                impossibleAreaResponse.setAddressList(deliveryImpossibleAreaResult.getAddressList());
                listImpossibleAreaResponse.add(impossibleAreaResponse);
            }
        }
        return listImpossibleAreaResponse;
    }
}