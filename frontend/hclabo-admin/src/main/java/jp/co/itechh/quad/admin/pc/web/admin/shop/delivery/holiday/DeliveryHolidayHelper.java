package jp.co.itechh.quad.admin.pc.web.admin.shop.delivery.holiday;

import jp.co.itechh.quad.admin.base.utility.ApplicationContextUtility;
import jp.co.itechh.quad.admin.base.utility.ConversionUtility;
import jp.co.itechh.quad.admin.constant.type.HTypeDeliveryMethodType;
import jp.co.itechh.quad.admin.constant.type.HTypeOpenDeleteStatus;
import jp.co.itechh.quad.admin.constant.type.HTypeShortfallDisplayFlag;
import jp.co.itechh.quad.admin.dto.shop.delivery.HolidaySearchForDaoConditionDto;
import jp.co.itechh.quad.admin.entity.shop.delivery.DeliveryMethodEntity;
import jp.co.itechh.quad.admin.entity.shop.delivery.HolidayEntity;
import jp.co.itechh.quad.admin.util.common.EnumTypeUtil;
import jp.co.itechh.quad.holiday.presentation.api.param.HolidayDownloadCsvRequest;
import jp.co.itechh.quad.holiday.presentation.api.param.HolidayListResponse;
import jp.co.itechh.quad.holiday.presentation.api.param.HolidayRegistRequest;
import jp.co.itechh.quad.holiday.presentation.api.param.HolidayResponse;
import jp.co.itechh.quad.method.presentation.api.param.DeliveryMethodResponse;
import jp.co.itechh.quad.method.presentation.api.param.ShippingMethodResponse;
import org.apache.commons.lang3.ObjectUtils;
import org.apache.commons.lang3.time.DateUtils;
import org.springframework.stereotype.Component;

import java.text.ParseException;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;

/**
 * 休日検索Helper
 *
 * @author Pham Quang Dieu (VTI Japan Co., Ltd.)
 */
@Component
public class DeliveryHolidayHelper {

    /**
     * 変換Utility取得
     */
    private final ConversionUtility conversionUtility;

    /**
     * コンストラクタ
     *
     * @param conversionUtility 変換ユーティリティクラス
     */
    public DeliveryHolidayHelper(ConversionUtility conversionUtility) {
        this.conversionUtility = conversionUtility;
    }

    /**
     * 検索条件生成<br/>
     *
     * @param deliveryHolidayModel ページ
     * @return 休日一括CSVDLリクエスト
     */
    public HolidayDownloadCsvRequest toHolidayDownloadCsvRequest(DeliveryHolidayModel deliveryHolidayModel) {
        // 検索条件Dto取得
        HolidayDownloadCsvRequest holidayDownloadCsvRequest = new HolidayDownloadCsvRequest();

        holidayDownloadCsvRequest.setYear(deliveryHolidayModel.getYear());

        return holidayDownloadCsvRequest;
    }

    /**
     * 検索条件設定<br/>
     *
     * @param conditionDto         検索条件Dto
     * @param deliveryHolidayModel ページ
     */
    public void toPageForLoad(HolidaySearchForDaoConditionDto conditionDto, DeliveryHolidayModel deliveryHolidayModel) {
        deliveryHolidayModel.setYear(conditionDto.getYear());
        deliveryHolidayModel.setDmcd(conditionDto.getDeliveryMethodSeq());
    }

    /**
     * 検索結果生成<br/>
     *
     * @param list                 リスト
     * @param deliveryHolidayModel ページ
     */
    public void toPageIndex(List<HolidayEntity> list, DeliveryHolidayModel deliveryHolidayModel) {

        List<DeliveryHolidayModelItem> resultItemList = new ArrayList<>();

        for (HolidayEntity holidayEntity : list) {
            DeliveryHolidayModelItem indexPageItem = ApplicationContextUtility.getBean(DeliveryHolidayModelItem.class);
            indexPageItem.setDate(holidayEntity.getDate());
            indexPageItem.setName(holidayEntity.getName());

            resultItemList.add(indexPageItem);
        }
        deliveryHolidayModel.setResultItems(resultItemList);
    }

    /**
     * Pageの値をエンティティへコピーします
     *
     * @param deliveryHolidayModel ページ
     * @return 倉庫休日登録リクエスト
     * @throws ParseException
     */
    public HolidayRegistRequest toHolidayEntityForRegistUpdate(DeliveryHolidayModel deliveryHolidayModel)
                    throws ParseException {

        HolidayRegistRequest request = new HolidayRegistRequest();

        request.setDate(DateUtils.parseDate(deliveryHolidayModel.getInputDate(), new String[] {"yyyy/MM/dd"}));

        /** 年 */
        // 入力値から年を得るため、Date型をCalendar型に変換
        Calendar calendar = Calendar.getInstance();

        calendar.setTime(DateUtils.parseDate(deliveryHolidayModel.getInputDate(), new String[] {"yyyy/MM/dd"}));
        request.setYear(calendar.get(Calendar.YEAR));

        /** 名前 */
        request.setName(deliveryHolidayModel.getInputName());

        return request;

    }

    /**
     * 検索結果をDeliveryHolidayModelに反映します
     *
     * @param deliveryHolidayModel DeliveryHolidayModel
     * @param resultEntity         DeliveryMethodEntity
     */
    public void convertToRegistPageForResult(DeliveryHolidayModel deliveryHolidayModel,
                                             DeliveryMethodEntity resultEntity) {
        deliveryHolidayModel.setDeliveryMethodName(resultEntity.getDeliveryMethodName());
        deliveryHolidayModel.setDeliveryMethodType(resultEntity.getDeliveryMethodType());
        deliveryHolidayModel.setOpenStatusPC(resultEntity.getOpenStatusPC());
    }

    /**
     * @param shippingMethodResponse 配送方法レスポンス
     * @return 配送方法クラス
     */
    public DeliveryMethodEntity toDeliveryMethodEntity(ShippingMethodResponse shippingMethodResponse) {

        if (ObjectUtils.isEmpty(shippingMethodResponse.getDeliveryMethodResponse())) {
            return null;
        }

        DeliveryMethodEntity deliveryMethodEntity = new DeliveryMethodEntity();
        DeliveryMethodResponse deliveryMethodResponse = shippingMethodResponse.getDeliveryMethodResponse();

        deliveryMethodEntity.setDeliveryMethodSeq(deliveryMethodResponse.getDeliveryMethodSeq());
        deliveryMethodEntity.setShopSeq(1001);
        deliveryMethodEntity.setDeliveryMethodName(deliveryMethodResponse.getDeliveryMethodName());
        deliveryMethodEntity.setDeliveryMethodDisplayNamePC(deliveryMethodResponse.getDeliveryMethodDisplayNamePC());
        deliveryMethodEntity.setOpenStatusPC(EnumTypeUtil.getEnumFromValue(HTypeOpenDeleteStatus.class,
                                                                           deliveryMethodResponse.getOpenStatusPC()
                                                                          ));
        deliveryMethodEntity.setDeliveryNotePC(deliveryMethodResponse.getDeliveryNotePC());
        deliveryMethodEntity.setDeliveryMethodType(EnumTypeUtil.getEnumFromValue(HTypeDeliveryMethodType.class,
                                                                                 deliveryMethodResponse.getDeliveryMethodType()
                                                                                ));
        deliveryMethodEntity.setEqualsCarriage(deliveryMethodResponse.getEqualsCarriage());
        deliveryMethodEntity.setLargeAmountDiscountPrice(deliveryMethodResponse.getLargeAmountDiscountPrice());
        deliveryMethodEntity.setLargeAmountDiscountCarriage(deliveryMethodResponse.getLargeAmountDiscountCarriage());
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

        return deliveryMethodEntity;
    }

    /**
     * @param holidayListResponse 倉庫休日一覧レスポンス
     * @return 休日クラス
     */
    public List<HolidayEntity> toHolidayEntityList(HolidayListResponse holidayListResponse) {
        List<HolidayEntity> list = new ArrayList<>();

        List<HolidayResponse> holidayList = holidayListResponse.getHolidayList();
        if (holidayList != null) {
            holidayList.forEach(holidayResponse -> {
                HolidayEntity holidayEntity = new HolidayEntity();
                holidayEntity.setDeliveryMethodSeq(holidayResponse.getDeliveryMethodSeq());
                holidayEntity.setName(holidayResponse.getName());
                holidayEntity.setYear(holidayResponse.getYear());
                holidayEntity.setDate(holidayResponse.getDate());
                holidayEntity.setRegistTime(conversionUtility.toTimestamp(holidayResponse.getRegistTime()));

                list.add(holidayEntity);
            });
        }

        return list;
    }

    /**
     * @param holidayResponse 倉庫休日レスポンス
     * @return 休日クラス
     */
    public HolidayEntity toHolidayEntity(HolidayResponse holidayResponse) {
        HolidayEntity holidayEntity = new HolidayEntity();
        holidayEntity.setDeliveryMethodSeq(holidayResponse.getDeliveryMethodSeq());
        holidayEntity.setDate(holidayResponse.getDate());
        holidayEntity.setYear(holidayResponse.getYear());
        holidayEntity.setName(holidayResponse.getName());
        holidayEntity.setRegistTime(conversionUtility.toTimestamp(holidayResponse.getRegistTime()));

        return holidayEntity;
    }
}