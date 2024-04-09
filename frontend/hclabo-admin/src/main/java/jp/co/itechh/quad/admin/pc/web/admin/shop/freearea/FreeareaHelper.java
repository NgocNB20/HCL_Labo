package jp.co.itechh.quad.admin.pc.web.admin.shop.freearea;

import jp.co.itechh.quad.admin.base.util.seasar.StringUtil;
import jp.co.itechh.quad.admin.base.utility.ApplicationContextUtility;
import jp.co.itechh.quad.admin.base.utility.ConversionUtility;
import jp.co.itechh.quad.admin.base.utility.DateUtility;
import jp.co.itechh.quad.admin.dto.shop.freearea.FreeAreaSearchDaoConditionDto;
import jp.co.itechh.quad.admin.util.common.EnumTypeUtil;
import jp.co.itechh.quad.freearea.presentation.api.param.FreeAreaListGetRequest;
import jp.co.itechh.quad.freearea.presentation.api.param.FreeAreaListResponse;
import jp.co.itechh.quad.freearea.presentation.api.param.FreeAreaResponse;
import org.springframework.stereotype.Component;

import java.sql.Timestamp;
import java.text.ParseException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

/**
 * フリーエリア検索HELPER
 *
 * @author Pham Quang Dieu (VTI Japan Co., Ltd.)
 */
@Component
public class FreeareaHelper {

    /**
     * 検索条件を画面に反映
     * 再検索用
     *
     * @param conditionDto 検索条件Dto
     */
    public void toPageForLoad(FreeAreaSearchDaoConditionDto conditionDto, FreeareaModel freeareaModel) {

        // 変換Helper取得
        ConversionUtility conversionUtility = ApplicationContextUtility.getBean(ConversionUtility.class);

        /* 各検索条件を画面に反映する */

        // キー
        freeareaModel.setSearchFreeAreaKey(conditionDto.getFreeAreaKey());

        // タイトル
        freeareaModel.setSearchFreeAreaTitle(conditionDto.getFreeAreaTitle());

        // 公開開始日 From
        freeareaModel.setSearchOpenStartTimeFrom(conversionUtility.toYmd(conditionDto.getOpenStartTimeFrom()));

        // 公開開始日 To
        freeareaModel.setSearchOpenStartTimeTo(conversionUtility.toYmd(conditionDto.getOpenStartTimeTo()));

        // 表示状態-日時タイプ
        freeareaModel.setSearchDateType(conditionDto.getDateType());

        // 表示状態-日付（日）
        freeareaModel.setSearchTargetDate(conditionDto.getTargetDate());

        // 表示状態-日付（時間）
        freeareaModel.setSearchTargetTime(conditionDto.getTargetTime());

        // 表示状態
        freeareaModel.setSearchOpenStateArray(conditionDto.getOpenStatusList().toArray(new String[] {}));

        // サイトマップ出力
        freeareaModel.setSearchSiteMapFlag(EnumTypeUtil.getValue(conditionDto.getSiteMapFlag()));

    }

    /**
     * 検索条件作成
     *
     * @return フリーエリア検索条件
     */
    public FreeAreaListGetRequest toFreeAreaListGetRequestForSearch(FreeareaModel freeareaModel) throws ParseException {
        FreeAreaListGetRequest freeAreaListGetRequest = new FreeAreaListGetRequest();
        // 変換Helper取得 / 日付関連Helper取得
        ConversionUtility conversionUtility = ApplicationContextUtility.getBean(ConversionUtility.class);
        DateUtility dateUtility = ApplicationContextUtility.getBean(DateUtility.class);

        // キー
        freeAreaListGetRequest.setFreeAreaKey(freeareaModel.getSearchFreeAreaKey());
        // タイトル
        freeAreaListGetRequest.setFreeAreaTitle(freeareaModel.getSearchFreeAreaTitle());
        // 公開開始日時From
        freeAreaListGetRequest.setOpenStartTimeFrom(
                        conversionUtility.toTimeStamp(freeareaModel.getSearchOpenStartTimeFrom()));
        // 公開開始日時To
        if (freeareaModel.getSearchOpenStartTimeTo() != null) {
            freeAreaListGetRequest.setOpenStartTimeTo(dateUtility.getEndOfDate(
                            conversionUtility.toTimeStamp(freeareaModel.getSearchOpenStartTimeTo())));
        }

        // 基準日 /日付タイプ / 日付
        Timestamp baseDate = dateUtility.getCurrentTime();
        if (FreeareaModel.SEARCH_DATE_TYPE_TARGETDATE.equals(freeareaModel.getSearchDateType())) {
            // 初期時間 00:00:00
            if (StringUtil.isEmpty(freeareaModel.getSearchTargetTime())) {
                freeareaModel.setSearchTargetTime(ConversionUtility.DEFAULT_START_TIME);
            }
            baseDate = conversionUtility.toTimeStamp(freeareaModel.getSearchTargetDate(),
                                                     freeareaModel.getSearchTargetTime()
                                                    );
        }
        freeAreaListGetRequest.setDateType(freeareaModel.getSearchDateType());
        if (freeareaModel.getSearchTargetDate() != null) {
            freeAreaListGetRequest.setTargetDate(freeareaModel.getSearchTargetDate());
        }
        if (freeareaModel.getSearchTargetTime() != null) {
            freeAreaListGetRequest.setTargetTime(freeareaModel.getSearchTargetTime());
        }
        freeAreaListGetRequest.setBaseDate(baseDate);
        freeAreaListGetRequest.setSiteMapFlag(freeareaModel.getSearchSiteMapFlag());

        // 公開状態
        freeAreaListGetRequest.setOpenStatusList(Arrays.asList(freeareaModel.getSearchOpenStateArray()));

        return freeAreaListGetRequest;
    }

    /**
     * 検索結果をページに反映
     *
     * @param freeAreaListResponse   フリーエリアエンティティ
     * @param freeAreaListGetRequest 検索条件
     */
    public void toPageForSearch(FreeAreaListResponse freeAreaListResponse,
                                FreeareaModel freeareaModel,
                                FreeAreaListGetRequest freeAreaListGetRequest) {

        ConversionUtility conversionUtility = ApplicationContextUtility.getBean(ConversionUtility.class);
        DateUtility dateUtility = ApplicationContextUtility.getBean(DateUtility.class);

        // オフセット + 1をNoにセット
        int index = ((freeAreaListResponse.getPageInfo().getPage() - 1) * freeAreaListResponse.getPageInfo()
                                                                                              .getLimit()) + 1;

        List<FreeareaResultItem> resultItemList = new ArrayList<>();
        for (FreeAreaResponse freeAreaResponse : freeAreaListResponse.getFreeareaList()) {
            FreeareaResultItem indexPageItem = ApplicationContextUtility.getBean(FreeareaResultItem.class);
            indexPageItem.setResultNo(index++);
            indexPageItem.setFreeAreaSeq(freeAreaResponse.getFreeAreaSeq());
            indexPageItem.setFreeAreaKey(freeAreaResponse.getFreeAreaKey());
            indexPageItem.setFreeAreaTitle(freeAreaResponse.getFreeAreaTitle());
            indexPageItem.setOpenStartTime(conversionUtility.toTimestamp(freeAreaResponse.getOpenStartTime()));
            indexPageItem.setFreeAreaOpenStatus(freeAreaResponse.getFreeAreaOpenStatus());
            indexPageItem.setSiteMapFlag(freeAreaResponse.getSiteMapFlag());
            resultItemList.add(indexPageItem);
        }
        freeareaModel.setResultItems(resultItemList);
    }
}