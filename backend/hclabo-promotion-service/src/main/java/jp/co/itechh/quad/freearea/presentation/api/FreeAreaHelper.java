package jp.co.itechh.quad.freearea.presentation.api;

import jp.co.itechh.quad.core.base.utility.ApplicationContextUtility;
import jp.co.itechh.quad.core.base.utility.ConversionUtility;
import jp.co.itechh.quad.core.base.utility.DateUtility;
import jp.co.itechh.quad.core.constant.type.HTypeFreeAreaOpenStatus;
import jp.co.itechh.quad.core.constant.type.HTypeSiteMapFlag;
import jp.co.itechh.quad.core.dto.shop.freearea.FreeAreaSearchDaoConditionDto;
import jp.co.itechh.quad.core.entity.shop.FreeAreaEntity;
import jp.co.itechh.quad.core.util.common.EnumTypeUtil;
import jp.co.itechh.quad.freearea.presentation.api.param.FreeAreaListGetRequest;
import jp.co.itechh.quad.freearea.presentation.api.param.FreeAreaListResponse;
import jp.co.itechh.quad.freearea.presentation.api.param.FreeAreaRegistRequest;
import jp.co.itechh.quad.freearea.presentation.api.param.FreeAreaResponse;
import jp.co.itechh.quad.freearea.presentation.api.param.FreeAreaUpdateRequest;
import org.apache.commons.lang3.ObjectUtils;
import org.springframework.stereotype.Component;

import java.sql.Timestamp;
import java.util.ArrayList;
import java.util.List;

/**
 * フリーエリア Helper
 *
 * @author PHAM QUANG DIEU (VJP)
 */
@Component
public class FreeAreaHelper {

    /**
     * 表示状態-日時タイプ:指定日時
     */
    public static final String SEARCH_DATE_TYPE_TARGETDATE = "1";

    /**
     * フリーエリア登録更新時の処理
     *
     * @param freeAreaUpdateRequest フリーエリア更新リクエスト
     * @return フリーエリアクラス
     */
    public FreeAreaEntity toFreeAreaEntityForUpdate(FreeAreaUpdateRequest freeAreaUpdateRequest, Integer freeAreaSeq) {
        FreeAreaEntity freeAreaEntity = new FreeAreaEntity();

        freeAreaEntity.setFreeAreaSeq(freeAreaSeq);
        freeAreaEntity.setShopSeq(1001);
        freeAreaEntity.setFreeAreaKey(freeAreaUpdateRequest.getFreeAreaKey());
        freeAreaEntity.setOpenStartTime(this.ymdhmsToTimestamp(freeAreaUpdateRequest.getOpenStartDate(),
                                                               freeAreaUpdateRequest.getOpenStartTime()
                                                              ));
        freeAreaEntity.setFreeAreaTitle(freeAreaUpdateRequest.getFreeAreaTitle());
        freeAreaEntity.setFreeAreaBodyPc(freeAreaUpdateRequest.getFreeAreaBodyPc());
        HTypeFreeAreaOpenStatus freeAreaOpenStatus = EnumTypeUtil.getEnumFromValue(HTypeFreeAreaOpenStatus.class,
                                                                                   freeAreaUpdateRequest.getFreeAreaOpenStatus()
                                                                                  );
        freeAreaEntity.setFreeAreaOpenStatus(freeAreaOpenStatus);
        HTypeSiteMapFlag siteMapFlag =
                        EnumTypeUtil.getEnumFromValue(HTypeSiteMapFlag.class, freeAreaUpdateRequest.getSiteMapFlag());
        freeAreaEntity.setSiteMapFlag(siteMapFlag);

        return freeAreaEntity;
    }

    /**
     * フリーエリア登録更新時の処理
     *
     * @param freeAreaRegistRequest フリーエリア登録リクエスト
     * @return フリーエリアクラス
     */
    public FreeAreaEntity toFreeAreaEntityForRegist(FreeAreaRegistRequest freeAreaRegistRequest) {
        FreeAreaEntity freeAreaEntity = new FreeAreaEntity();

        freeAreaEntity.setShopSeq(1001);
        freeAreaEntity.setFreeAreaSeq(freeAreaRegistRequest.getFreeAreaSeq());
        freeAreaEntity.setFreeAreaKey(freeAreaRegistRequest.getFreeAreaKey());
        freeAreaEntity.setOpenStartTime(this.ymdhmsToTimestamp(freeAreaRegistRequest.getOpenStartDate(),
                                                               freeAreaRegistRequest.getOpenStartTime()
                                                              ));
        freeAreaEntity.setFreeAreaTitle(freeAreaRegistRequest.getFreeAreaTitle());
        freeAreaEntity.setFreeAreaBodyPc(freeAreaRegistRequest.getFreeAreaBodyPc());
        HTypeFreeAreaOpenStatus freeAreaOpenStatus = EnumTypeUtil.getEnumFromValue(HTypeFreeAreaOpenStatus.class,
                                                                                   freeAreaRegistRequest.getFreeAreaOpenStatus()
                                                                                  );
        freeAreaEntity.setFreeAreaOpenStatus(freeAreaOpenStatus);
        HTypeSiteMapFlag siteMapFlag =
                        EnumTypeUtil.getEnumFromValue(HTypeSiteMapFlag.class, freeAreaRegistRequest.getSiteMapFlag());
        freeAreaEntity.setSiteMapFlag(siteMapFlag);

        return freeAreaEntity;
    }

    /**
     * 年月日・時分秒→タイムスタンプ
     *
     * @param ymd 日付項目値
     * @param hms 時刻項目値
     * @return 引数から取得されるタイムスタンプ
     */
    protected Timestamp ymdhmsToTimestamp(String ymd, String hms) {

        Timestamp ret = null;
        if (ymd != null && hms != null) {
            // 変換Helper取得
            ConversionUtility conversionUtility = ApplicationContextUtility.getBean(ConversionUtility.class);
            ret = conversionUtility.toTimeStamp(ymd, hms);
        }
        return ret;
    }

    /**
     * 検索条件作成
     *
     * @param freeAreaListGetRequest ページ情報
     * @return フリーエリアDao用検索条件Dtoクラス
     */
    public FreeAreaSearchDaoConditionDto toFreeAreaSearchDaoConditionDtoForSearch(FreeAreaListGetRequest freeAreaListGetRequest) {

        FreeAreaSearchDaoConditionDto conditionDto =
                        ApplicationContextUtility.getBean(FreeAreaSearchDaoConditionDto.class);

        // 変換Helper取得 / 日付関連Helper取得
        ConversionUtility conversionUtility = ApplicationContextUtility.getBean(ConversionUtility.class);
        DateUtility dateUtility = ApplicationContextUtility.getBean(DateUtility.class);

        // キー
        conditionDto.setFreeAreaKey(freeAreaListGetRequest.getFreeAreaKey());
        // タイトル
        conditionDto.setFreeAreaTitle(freeAreaListGetRequest.getFreeAreaTitle());
        // 公開開始日時From
        if (freeAreaListGetRequest.getOpenStartTimeFrom() != null) {
            conditionDto.setOpenStartTimeFrom(new Timestamp(freeAreaListGetRequest.getOpenStartTimeFrom().getTime()));
        }
        // 公開開始日時To
        if (freeAreaListGetRequest.getOpenStartTimeTo() != null) {
            conditionDto.setOpenStartTimeTo(dateUtility.getEndOfDate(
                            new Timestamp(freeAreaListGetRequest.getOpenStartTimeTo().getTime())));
        }

        // 基準日 /日付タイプ / 日付
        Timestamp baseDate = dateUtility.getCurrentTime();
        if (SEARCH_DATE_TYPE_TARGETDATE.equals(freeAreaListGetRequest.getDateType())) {
            // 初期時間 00:00:00
            if (ObjectUtils.isEmpty(freeAreaListGetRequest.getTargetTime())) {
                freeAreaListGetRequest.setTargetTime(ConversionUtility.DEFAULT_START_TIME);
            }
            baseDate = conversionUtility.toTimeStamp(freeAreaListGetRequest.getTargetDate(),
                                                     freeAreaListGetRequest.getTargetTime()
                                                    );
        }
        conditionDto.setDateType(freeAreaListGetRequest.getDateType());
        conditionDto.setTargetDate(freeAreaListGetRequest.getTargetDate());
        conditionDto.setTargetTime(freeAreaListGetRequest.getTargetTime());
        conditionDto.setBaseDate(baseDate);
        conditionDto.setSiteMapFlag(
                        EnumTypeUtil.getEnumFromValue(HTypeSiteMapFlag.class, freeAreaListGetRequest.getSiteMapFlag()));

        if (freeAreaListGetRequest.getOpenStatusList() != null
            && freeAreaListGetRequest.getOpenStatusList().size() == 0) {
            conditionDto.setOpenStatusList(null);
        } else {
            // 公開状態
            conditionDto.setOpenStatusList(freeAreaListGetRequest.getOpenStatusList());
        }

        return conditionDto;
    }

    /*
     * 検索結果をページに反映
     *
     * @param freeAreaEntityList フリーエリア一覧
     * @return フリーエリア一覧レスポンス
     */
    public FreeAreaListResponse toFreeAreaListResponse(List<FreeAreaEntity> freeAreaEntityList) {

        FreeAreaListResponse freeAreaListResponse = new FreeAreaListResponse();
        List<FreeAreaResponse> freeAreaResponseList = new ArrayList<>();

        for (FreeAreaEntity freeAreaEntity : freeAreaEntityList) {
            FreeAreaResponse freeareaResponse = toFreeAreaResponse(freeAreaEntity);
            freeAreaResponseList.add(freeareaResponse);
        }

        freeAreaListResponse.setFreeareaList(freeAreaResponseList);

        return freeAreaListResponse;
    }

    /*
     * レスポンスに変換
     *
     * @param freeAreaEntity   フリーエリアクラス
     */
    public FreeAreaResponse toFreeAreaResponse(FreeAreaEntity freeAreaEntity) {
        // 処理前は存在しないためnullを返す
        if (ObjectUtils.isEmpty(freeAreaEntity)) {
            return null;
        }

        FreeAreaResponse freeAreaResponse = new FreeAreaResponse();

        freeAreaResponse.setFreeAreaSeq(freeAreaEntity.getFreeAreaSeq());
        freeAreaResponse.setFreeAreaKey(freeAreaEntity.getFreeAreaKey());
        freeAreaResponse.setOpenStartTime(freeAreaEntity.getOpenStartTime());
        freeAreaResponse.setFreeAreaTitle(freeAreaEntity.getFreeAreaTitle());
        freeAreaResponse.setFreeAreaBodyPc(freeAreaEntity.getFreeAreaBodyPc());
        freeAreaResponse.setRegistTime(freeAreaEntity.getRegistTime());
        freeAreaResponse.setUpdateTime(freeAreaEntity.getUpdateTime());
        freeAreaResponse.setFreeAreaOpenStatus(EnumTypeUtil.getValue(freeAreaEntity.getFreeAreaOpenStatus()));
        freeAreaResponse.setSiteMapFlag(EnumTypeUtil.getValue(freeAreaEntity.getSiteMapFlag()));

        return freeAreaResponse;
    }
}