package jp.co.itechh.quad.admin.pc.web.admin.totaling.searchkeywordaccording;

import jp.co.itechh.quad.accesskeywords.presentation.api.param.AccessKeywordResponse;
import jp.co.itechh.quad.accesskeywords.presentation.api.param.AccessKeywordsCsvGetRequest;
import jp.co.itechh.quad.accesskeywords.presentation.api.param.AccessKeywordsGetRequest;
import jp.co.itechh.quad.admin.base.util.common.CollectionUtil;
import jp.co.itechh.quad.admin.base.utility.ApplicationContextUtility;
import jp.co.itechh.quad.admin.base.utility.ConversionUtility;
import jp.co.itechh.quad.admin.base.utility.DateUtility;
import jp.co.itechh.quad.admin.base.utility.ZenHanConversionUtility;
import jp.co.itechh.quad.admin.dto.totaling.AccessSearchKeywordTotalDto;
import jp.co.itechh.quad.admin.dto.totaling.AccessSearchKeywordTotalSearchForConditionDto;
import org.apache.commons.lang3.StringUtils;
import org.springframework.stereotype.Component;

import java.sql.Timestamp;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;

/**
 * 検索キーワード集計画面用Helperクラス。
 *
 * @author Doan Thang (VTI Japan Co., Ltd.)
 */
@Component
public class SearchKeywordAccordingHelper {

    /**
     * 全角、半角の変換を行うヘルパークラス
     **/
    private final ZenHanConversionUtility zenHanConversionUtility;

    /**
     * 変換ユーティリティクラス
     **/
    private final ConversionUtility conversionUtility;

    /**
     * Instantiates a new Search keyword according helper.
     *
     * @param zenHanConversionUtility the zen han conversion utility
     * @param conversionUtility       the conversion utility
     */
    public SearchKeywordAccordingHelper(ZenHanConversionUtility zenHanConversionUtility,
                                        ConversionUtility conversionUtility) {
        this.zenHanConversionUtility = zenHanConversionUtility;
        this.conversionUtility = conversionUtility;
    }

    /**
     * 終日時間を設定します
     *
     * @param time TimeStamp
     */
    protected void setEndTime(Timestamp time) {
        Calendar cal = Calendar.getInstance();
        cal.setTimeInMillis(time.getTime());
        cal.set(Calendar.HOUR, 23);
        cal.set(Calendar.MINUTE, 59);
        cal.set(Calendar.SECOND, 59);
        cal.set(Calendar.MILLISECOND, 999);
        time.setTime(cal.getTimeInMillis());
    }

    /**
     * 終日時間を設定します
     *
     * @param conditionDto 検索キーワード集計用検索条件Dtoクラス
     */
    public void setEndTime(AccessSearchKeywordTotalSearchForConditionDto conditionDto) {
        if (conditionDto.getProcessDateTo() != null) {
            setEndTime(conditionDto.getProcessDateTo());
        } else {
            Timestamp clone = (Timestamp) conditionDto.getProcessDateFrom().clone();
            setEndTime(clone);
            conditionDto.setProcessDateTo(clone);
        }
    }

    /**
     * 検索キーワード集計リクエストに変換
     *
     * @param model the model
     * @return 検索キーワード集計リクエスト
     */
    public AccessKeywordsGetRequest toAccessKeywordsGetRequest(SearchKeywordAccordingModel model) {
        AccessKeywordsGetRequest accessKeywordsGetRequest = new AccessKeywordsGetRequest();
        // 日付関連Helper取得
        DateUtility dateUtility = ApplicationContextUtility.getBean(DateUtility.class);

        // 変換Helper取得
        ConversionUtility conversionUtility = ApplicationContextUtility.getBean(ConversionUtility.class);

        if (model.getProcessDateFrom() != null) {
            accessKeywordsGetRequest.setProcessDateFrom(conversionUtility.toTimeStamp(model.getProcessDateFrom()));
        }
        if (model.getProcessDateTo() != null) {
            accessKeywordsGetRequest.setProcessDateTo(
                            dateUtility.getEndOfDate(conversionUtility.toTimeStamp(model.getProcessDateTo())));
        }

        /* 全角半角変換ユーティリティ */
        ZenHanConversionUtility zenHanConversionUtility =
                        ApplicationContextUtility.getBean(ZenHanConversionUtility.class);

        /* キーワード */
        // 大文字変換
        String keyword = StringUtils.upperCase(model.getKeyword());

        // 全角変換
        accessKeywordsGetRequest.setSearchKeyword(zenHanConversionUtility.toZenkaku(keyword));

        /* 検索回数－From */
        accessKeywordsGetRequest.setSearchCountFrom(conversionUtility.toInteger(model.getSearchResultCountFrom()));

        /* 検索回数－To */
        accessKeywordsGetRequest.setSearchCountTo(conversionUtility.toInteger(model.getSearchResultCountTo()));

        /* ソート条件 */
        accessKeywordsGetRequest.setOrderByCondition(model.getOrderByCondition());

        return accessKeywordsGetRequest;
    }

    /**
     * 検索キーワード集計Dtoクラスのリストに変換
     *
     * @param accessKeywordResponseList 検索キーワードレスポンスのリスト
     * @return 検索キーワード集計Dtoクラスのリスト
     */
    public List<AccessSearchKeywordTotalDto> toAccessSearchKeyWordTotalDtoList(List<AccessKeywordResponse> accessKeywordResponseList) {
        List<AccessSearchKeywordTotalDto> accessSearchKeywordTotalDtoList = new ArrayList<>();

        if (CollectionUtil.isNotEmpty(accessKeywordResponseList)) {
            accessKeywordResponseList.forEach(item -> {
                AccessSearchKeywordTotalDto accessSearchKeywordTotalDto = new AccessSearchKeywordTotalDto();

                accessSearchKeywordTotalDto.setSearchKeyword(item.getSearchKeyword());
                accessSearchKeywordTotalDto.setSearchCount(item.getSearchCount());
                accessSearchKeywordTotalDto.setSearchResultCount(item.getSearchResultCount());

                accessSearchKeywordTotalDtoList.add(accessSearchKeywordTotalDto);
            });
        }

        return accessSearchKeywordTotalDtoList;
    }

    /**
     * キーワード集計CSVDLリクエストに変換
     *
     * @param model the model
     * @return キーワード集計CSVDLリクエスト
     */
    public AccessKeywordsCsvGetRequest toAccessKeywordsCsvGetRequest(SearchKeywordAccordingModel model) {
        AccessKeywordsCsvGetRequest accessKeywordsCsvGetRequest = new AccessKeywordsCsvGetRequest();

        // 日付関連Helper取得
        DateUtility dateUtility = ApplicationContextUtility.getBean(DateUtility.class);

        // 変換Helper取得
        ConversionUtility conversionUtility = ApplicationContextUtility.getBean(ConversionUtility.class);

        if (model.getProcessDateFrom() != null) {
            accessKeywordsCsvGetRequest.setProcessDateFrom(conversionUtility.toTimeStamp(model.getProcessDateFrom()));
        }
        if (model.getProcessDateTo() != null) {
            accessKeywordsCsvGetRequest.setProcessDateTo(
                            dateUtility.getEndOfDate(conversionUtility.toTimeStamp(model.getProcessDateTo())));
        }

        /* キーワード */
        // 大文字変換
        String keyword = StringUtils.upperCase(model.getKeyword());

        // 全角変換
        accessKeywordsCsvGetRequest.setSearchKeyword(zenHanConversionUtility.toZenkaku(keyword));

        /* 検索回数－From */
        accessKeywordsCsvGetRequest.setSearchCountFrom(conversionUtility.toInteger(model.getSearchResultCountFrom()));

        /* 検索回数－To */
        accessKeywordsCsvGetRequest.setSearchCountTo(conversionUtility.toInteger(model.getSearchResultCountTo()));

        /* ソート条件 */
        accessKeywordsCsvGetRequest.setOrderByCondition(model.getOrderByCondition());

        return accessKeywordsCsvGetRequest;
    }

}