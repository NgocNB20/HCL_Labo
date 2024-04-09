package jp.co.itechh.quad.admin.pc.web.admin.shop.delivery.did;

import jp.co.itechh.quad.admin.base.util.common.CollectionUtil;
import jp.co.itechh.quad.admin.base.utility.ApplicationContextUtility;
import jp.co.itechh.quad.admin.base.utility.ConversionUtility;
import jp.co.itechh.quad.admin.constant.type.HTypeDeliveryMethodType;
import jp.co.itechh.quad.admin.constant.type.HTypeOpenDeleteStatus;
import jp.co.itechh.quad.admin.constant.type.HTypeShortfallDisplayFlag;
import jp.co.itechh.quad.admin.dto.shop.delivery.DeliveryImpossibleDaySearchForDaoConditionDto;
import jp.co.itechh.quad.admin.entity.shop.delivery.DeliveryImpossibleDayEntity;
import jp.co.itechh.quad.admin.entity.shop.delivery.DeliveryMethodEntity;
import jp.co.itechh.quad.admin.util.common.EnumTypeUtil;
import jp.co.itechh.quad.method.presentation.api.param.DeliveryMethodResponse;
import jp.co.itechh.quad.method.presentation.api.param.ShippingMethodResponse;
import jp.co.itechh.quad.receiverimpossibledate.presentation.api.param.ReceiverImpossibleDateListRequest;
import jp.co.itechh.quad.receiverimpossibledate.presentation.api.param.ReceiverImpossibleDateListResponse;
import jp.co.itechh.quad.receiverimpossibledate.presentation.api.param.ReceiverImpossibleDateRegistRequest;
import jp.co.itechh.quad.receiverimpossibledate.presentation.api.param.ReceiverImpossibleDateResponse;
import org.apache.commons.lang3.time.DateUtils;
import org.springframework.stereotype.Component;

import java.text.ParseException;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;

/**
 *
 * お届け不可日検索Helper
 *
 * @author Pham Quang Dieu (VTI Japan Co., Ltd.)
 *
 */
@Component
public class DeliveryDidHelper {

    /**
     * 変換Utility取得
     */
    private final ConversionUtility conversionUtility;

    /**
     * コンストラクタ
     *
     * @param conversionUtility 変換ユーティリティクラス
     */
    public DeliveryDidHelper(ConversionUtility conversionUtility) {
        this.conversionUtility = conversionUtility;
    }

    /**
     * 検索条件生成<br/>
     *
     * @param deliveryDidModel ページ
     * @return お届け不可日一覧リクエスト
     */
    public ReceiverImpossibleDateListRequest toReceiverImpossibleDateListRequest(DeliveryDidModel deliveryDidModel) {

        ReceiverImpossibleDateListRequest receiverImpossibleDateListRequest = new ReceiverImpossibleDateListRequest();

        receiverImpossibleDateListRequest.setYear(deliveryDidModel.getYear());

        return receiverImpossibleDateListRequest;
    }

    /**
     * 検索条件設定<br/>
     *
     * @param conditionDto     検索条件Dto
     * @param deliveryDidModel ページ
     */
    public void toPageForLoad(DeliveryImpossibleDaySearchForDaoConditionDto conditionDto,
                              DeliveryDidModel deliveryDidModel) {
        deliveryDidModel.setYear(conditionDto.getYear());
        deliveryDidModel.setDmcd(conditionDto.getDeliveryMethodSeq());
    }

    /**
     * 検索結果生成<br/>
     *
     * @param list             リスト
     * @param deliveryDidModel ページ
     */
    public void toPageIndex(List<DeliveryImpossibleDayEntity> list, DeliveryDidModel deliveryDidModel) {
        List<DeliveryDidModelItem> resultItemList = new ArrayList<>();
        if (list != null) {
            for (DeliveryImpossibleDayEntity deliveryImpossibleDayEntity : list) {
                DeliveryDidModelItem deliveryDidModelItem =
                                ApplicationContextUtility.getBean(DeliveryDidModelItem.class);
                deliveryDidModelItem.setDate(deliveryImpossibleDayEntity.getDate());
                deliveryDidModelItem.setReason(deliveryImpossibleDayEntity.getReason());

                resultItemList.add(deliveryDidModelItem);
            }
        }
        deliveryDidModel.setResultItems(resultItemList);
    }

    /**
     * Pageの値をエンティティへコピーします
     *
     * @param deliveryDidModel ページ
     * @return entity ReceiverImpossibleDateRegistRequest お届け不可日登録リクエスト
     * @throws ParseException
     */
    public ReceiverImpossibleDateRegistRequest toDeliveryImpossibleDayEntityForRegistUpdate(DeliveryDidModel deliveryDidModel)
                    throws ParseException {

        ReceiverImpossibleDateRegistRequest receiverImpossibleDateRegistRequest =
                        new ReceiverImpossibleDateRegistRequest();

        receiverImpossibleDateRegistRequest.setDate(
                        DateUtils.parseDate(deliveryDidModel.getInputDate(), new String[] {"yyyy/MM/dd"}));

        /** 年 */
        // 入力値から年を得るため、Date型をCalendar型に変換
        Calendar calendar = Calendar.getInstance();

        calendar.setTime(DateUtils.parseDate(deliveryDidModel.getInputDate(), new String[] {"yyyy/MM/dd"}));
        receiverImpossibleDateRegistRequest.setYear(calendar.get(Calendar.YEAR));

        /** 名前 */
        receiverImpossibleDateRegistRequest.setReason(deliveryDidModel.getInputReason());

        return receiverImpossibleDateRegistRequest;

    }

    /**
     * 検索結果をIndexPageに反映します
     *
     * @param deliveryDidModel IndexPage
     * @param resultEntity     DeliveryMethodEntity
     */
    public void convertToRegistPageForResult(DeliveryDidModel deliveryDidModel, DeliveryMethodEntity resultEntity) {
        deliveryDidModel.setDeliveryMethodName(resultEntity.getDeliveryMethodName());
        deliveryDidModel.setDeliveryMethodType(resultEntity.getDeliveryMethodType());
        deliveryDidModel.setOpenStatusPC(resultEntity.getOpenStatusPC());
    }

    /**
     * 配送方法エンティティに変換
     *
     * @param shippingMethodResponse 配送方法レスポンス
     * @return 配送方法クラス
     */
    public DeliveryMethodEntity toDeliveryMethodEntity(ShippingMethodResponse shippingMethodResponse) {
        DeliveryMethodEntity deliveryMethodEntity = new DeliveryMethodEntity();
        DeliveryMethodResponse deliveryMethodResponse = shippingMethodResponse.getDeliveryMethodResponse();

        if (deliveryMethodResponse != null) {
            deliveryMethodEntity.setDeliveryMethodSeq(deliveryMethodResponse.getDeliveryMethodSeq());
            deliveryMethodEntity.setShopSeq(1001);
            deliveryMethodEntity.setDeliveryMethodName(deliveryMethodResponse.getDeliveryMethodName());
            deliveryMethodEntity.setDeliveryMethodDisplayNamePC(
                            deliveryMethodResponse.getDeliveryMethodDisplayNamePC());
            deliveryMethodEntity.setOpenStatusPC(EnumTypeUtil.getEnumFromValue(HTypeOpenDeleteStatus.class,
                                                                               deliveryMethodResponse.getOpenStatusPC()
                                                                              ));
            deliveryMethodEntity.setDeliveryNotePC(deliveryMethodResponse.getDeliveryNotePC());
            deliveryMethodEntity.setDeliveryMethodType(EnumTypeUtil.getEnumFromValue(HTypeDeliveryMethodType.class,
                                                                                     deliveryMethodResponse.getDeliveryMethodType()
                                                                                    ));
            deliveryMethodEntity.setEqualsCarriage(deliveryMethodResponse.getEqualsCarriage());
            deliveryMethodEntity.setLargeAmountDiscountPrice(deliveryMethodResponse.getLargeAmountDiscountPrice());
            deliveryMethodEntity.setLargeAmountDiscountCarriage(
                            deliveryMethodResponse.getLargeAmountDiscountCarriage());
            deliveryMethodEntity.setShortfallDisplayFlag(EnumTypeUtil.getEnumFromValue(HTypeShortfallDisplayFlag.class,
                                                                                       deliveryMethodResponse.getShortfallDisplayFlag()
                                                                                      ));
            deliveryMethodEntity.setDeliveryLeadTime(deliveryMethodResponse.getDeliveryLeadTime());
            deliveryMethodEntity.setDeliveryChaseURL(deliveryMethodResponse.getDeliveryChaseURL());
            deliveryMethodEntity.setDeliveryChaseURLDisplayPeriod(
                            deliveryMethodResponse.getDeliveryChaseURLDisplayPeriod());
            deliveryMethodEntity.setPossibleSelectDays(deliveryMethodResponse.getPossibleSelectDays());
            deliveryMethodEntity.setReceiverTimeZone1(deliveryMethodResponse.getReceiverTimeZone1());
            deliveryMethodEntity.setReceiverTimeZone2(deliveryMethodResponse.getReceiverTimeZone2());
            deliveryMethodEntity.setReceiverTimeZone3(deliveryMethodResponse.getReceiverTimeZone3());
            deliveryMethodEntity.setReceiverTimeZone4(deliveryMethodResponse.getReceiverTimeZone4());
            deliveryMethodEntity.setReceiverTimeZone5(deliveryMethodResponse.getReceiverTimeZone5());
            deliveryMethodEntity.setReceiverTimeZone6(deliveryMethodResponse.getReceiverTimeZone6());
            deliveryMethodEntity.setReceiverTimeZone7(deliveryMethodResponse.getReceiverTimeZone7());
            deliveryMethodEntity.setReceiverTimeZone8(deliveryMethodResponse.getReceiverTimeZone8());
            deliveryMethodEntity.setReceiverTimeZone9(deliveryMethodResponse.getReceiverTimeZone9());
            deliveryMethodEntity.setReceiverTimeZone10(deliveryMethodResponse.getReceiverTimeZone10());
            deliveryMethodEntity.setOrderDisplay(deliveryMethodResponse.getOrderDisplay());
            deliveryMethodEntity.setRegistTime(conversionUtility.toTimestamp(deliveryMethodResponse.getRegistTime()));
            deliveryMethodEntity.setUpdateTime(conversionUtility.toTimestamp(deliveryMethodResponse.getUpdateTime()));
        }
        return deliveryMethodEntity;
    }

    /**
     * お届け不可日エンティティリストに変換
     *
     * @param receiverImpossibleDateListResponse お届け不可日一覧レスポンス
     * @return お届け不可日クラス
     */
    public List<DeliveryImpossibleDayEntity> toListDeliveryImpossibleDayEntity(ReceiverImpossibleDateListResponse receiverImpossibleDateListResponse) {
        if (CollectionUtil.isEmpty(receiverImpossibleDateListResponse.getImpossibleAreaList())) {
            return null;
        }

        List<DeliveryImpossibleDayEntity> deliveryImpossibleDayEntities = new ArrayList<>();

        List<ReceiverImpossibleDateResponse> receiverImpossibleDateResponses =
                        receiverImpossibleDateListResponse.getImpossibleAreaList();
        if (receiverImpossibleDateResponses != null) {
            receiverImpossibleDateResponses.forEach(impossibleDateResponse -> {
                DeliveryImpossibleDayEntity deliveryImpossibleDayEntity = new DeliveryImpossibleDayEntity();
                deliveryImpossibleDayEntity.setDeliveryMethodSeq(impossibleDateResponse.getDeliveryMethodSeq());
                deliveryImpossibleDayEntity.setReason(impossibleDateResponse.getReason());
                deliveryImpossibleDayEntity.setYear(impossibleDateResponse.getYear());
                deliveryImpossibleDayEntity.setDate(impossibleDateResponse.getDate());
                deliveryImpossibleDayEntity.setRegistTime(
                                conversionUtility.toTimestamp(impossibleDateResponse.getRegistTime()));

                deliveryImpossibleDayEntities.add(deliveryImpossibleDayEntity);
            });
        }

        return deliveryImpossibleDayEntities;
    }

    /**
     * お届け不可日クラスに変換
     *
     * @param receiverImpossibleDateResponse お届け不可日レスポンス
     * @return お届け不可日クラス
     */
    public DeliveryImpossibleDayEntity toDeliveryImpossibleDayEntity(ReceiverImpossibleDateResponse receiverImpossibleDateResponse) {
        DeliveryImpossibleDayEntity deliveryImpossibleDayEntity = new DeliveryImpossibleDayEntity();
        deliveryImpossibleDayEntity.setReason(receiverImpossibleDateResponse.getReason());
        deliveryImpossibleDayEntity.setDate(receiverImpossibleDateResponse.getDate());
        deliveryImpossibleDayEntity.setDeliveryMethodSeq(receiverImpossibleDateResponse.getDeliveryMethodSeq());
        deliveryImpossibleDayEntity.setYear(receiverImpossibleDateResponse.getYear());
        deliveryImpossibleDayEntity.setRegistTime(
                        conversionUtility.toTimestamp(receiverImpossibleDateResponse.getRegistTime()));

        return deliveryImpossibleDayEntity;
    }
}