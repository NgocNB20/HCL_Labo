/*
 * Copyright (C) 2022 i-TEC HANKYU HANSHIN INC. All Rights Reserved.
 */

package jp.co.itechh.quad.admin.pc.web.admin.shop.novelty;

import jp.co.itechh.quad.admin.base.utility.ApplicationContextUtility;
import jp.co.itechh.quad.admin.base.utility.ConversionUtility;
import jp.co.itechh.quad.admin.constant.type.HTypeNoveltyPresentState;
import jp.co.itechh.quad.admin.util.common.EnumTypeUtil;
import jp.co.itechh.quad.novelty.presentation.api.param.NoveltyPresentConditionListGetRequest;
import jp.co.itechh.quad.novelty.presentation.api.param.NoveltyPresentConditionListResponse;
import jp.co.itechh.quad.novelty.presentation.api.param.NoveltyPresentSearchResultResponse;
import jp.co.itechh.quad.novelty.presentation.api.param.NoveltySearchResultGoodsResponse;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.sql.Timestamp;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Calendar;
import java.util.List;

/**
 * ノベルティプレゼント検索画面Helper
 *
 * @author Pham Quang Dieu (VTI Japan Co., Ltd.)
 */
@Component
public class NoveltyHelper {

    /** 変換Helper */
    private final ConversionUtility conversionUtility;

    @Autowired
    public NoveltyHelper(ConversionUtility conversionUtility) {
        this.conversionUtility = conversionUtility;
    }

    /**
     * ノベルティプレゼントDao用検索条件Dto(管理機能用）クラス
     *
     * @param noveltyModel ノベルティプレゼント検索
     * @return ノベルティプレゼントDao用検索条件Dto(管理機能用 ） クラス
     */
    public NoveltyPresentConditionListGetRequest toNoveltyPresentConditionListGetRequest(NoveltyModel noveltyModel) {
        NoveltyPresentConditionListGetRequest noveltyPresentConditionListGetRequest =
                        new NoveltyPresentConditionListGetRequest();

        noveltyPresentConditionListGetRequest.setNoveltyPresentName(noveltyModel.getSearchNoveltyPresentName());
        if (noveltyModel.getNoveltyPresentState() != null && noveltyModel.getNoveltyPresentState().length > 0) {
            noveltyPresentConditionListGetRequest.setNoveltyPresentState(
                            Arrays.asList(noveltyModel.getNoveltyPresentState()));
        }
        if (noveltyModel.getSearchNoveltyPresentStartTimeFrom() != null) {
            noveltyPresentConditionListGetRequest.setNoveltyPresentStartTimeFrom(conversionUtility.toDate(
                            conversionUtility.toTimeStamp(noveltyModel.getSearchNoveltyPresentStartTimeFrom())));
        }

        if (noveltyModel.getSearchNoveltyPresentStartTimeTo() != null) {
            noveltyPresentConditionListGetRequest.setNoveltyPresentStartTimeTo(conversionUtility.toDate(
                            this.getEndOfDate(conversionUtility.toTimeStamp(
                                            noveltyModel.getSearchNoveltyPresentStartTimeTo()))));
        }

        if (noveltyModel.getSearchNoveltyPresentEndTimeFrom() != null) {
            noveltyPresentConditionListGetRequest.setNoveltyPresentEndTimeFrom(conversionUtility.toDate(
                            conversionUtility.toTimeStamp(noveltyModel.getSearchNoveltyPresentEndTimeFrom())));
        }

        if (noveltyModel.getSearchNoveltyPresentEndTimeTo() != null) {
            noveltyPresentConditionListGetRequest.setNoveltyPresentEndTimeTo(conversionUtility.toDate(this.getEndOfDate(
                            conversionUtility.toTimeStamp(noveltyModel.getSearchNoveltyPresentEndTimeTo()))));
        }
        noveltyPresentConditionListGetRequest.setNoveltyPresentGoodsCode(
                        noveltyModel.getSearchNoveltyPresentGoodsCode());
        noveltyPresentConditionListGetRequest.setNoveltyPresentGoodsCountFrom(
                        conversionUtility.toInteger(noveltyModel.getSearchNoveltyPresentGoodsCountFrom()));
        noveltyPresentConditionListGetRequest.setNoveltyPresentGoodsCountTo(
                        conversionUtility.toInteger(noveltyModel.getSearchNoveltyPresentGoodsCountTo()));

        return noveltyPresentConditionListGetRequest;
    }

    /**
     * 検索結果の格納
     *
     * @param noveltyModel ノベルティプレゼント検索
     * @param noveltyPresentConditionListResponse ノベルティプレゼント条件リストレスポンス
     */
    public void toPageForSearch(NoveltyPresentConditionListResponse noveltyPresentConditionListResponse,
                                NoveltyModel noveltyModel) {

        int index = ((noveltyPresentConditionListResponse.getPageInfo().getPage() - 1)
                     * noveltyPresentConditionListResponse.getPageInfo().getLimit()) + 1;
        List<NoveltyModelItem> resultItemList = new ArrayList<>();

        for (NoveltyPresentSearchResultResponse NoveltyPresentSearchResultResponse : noveltyPresentConditionListResponse.getNoveltyPresentConditionList()) {
            NoveltyModelItem noveltyModelItem = ApplicationContextUtility.getBean(NoveltyModelItem.class);

            noveltyModelItem.setResultNo(index++);
            noveltyModelItem.setResultNoveltyPresentName(NoveltyPresentSearchResultResponse.getNoveltyPresentName());
            noveltyModelItem.setResultNoveltyPresentStartTime(conversionUtility.toTimestamp(
                            NoveltyPresentSearchResultResponse.getNoveltyPresentStartTime()));
            noveltyModelItem.setResultNoveltyPresentEndTime(conversionUtility.toTimestamp(
                            NoveltyPresentSearchResultResponse.getNoveltyPresentEndTime()));
            noveltyModelItem.setResultNoveltyPresentState(NoveltyPresentSearchResultResponse.getNoveltyPresentState());
            noveltyModelItem.setResultNoveltyPresentGoods(
                            createGoodsInfoStr(NoveltyPresentSearchResultResponse.getNoveltyGoodsList()));
            noveltyModelItem.setNoveltyPresentConditionSeq(
                            NoveltyPresentSearchResultResponse.getNoveltyPresentConditionSeq());
            noveltyModelItem.setResultNoveltyPresentStateType(EnumTypeUtil.getEnumFromValue(
                            HTypeNoveltyPresentState.class,
                            NoveltyPresentSearchResultResponse.getNoveltyPresentState()
                                                                                           ));

            resultItemList.add(noveltyModelItem);
        }

        noveltyModel.setResultItems(resultItemList);
    }

    /**
     * 指定日付の最終時刻を取得(23時59分59秒999ミリ秒)
     *
     * @param timestamp 指定日付
     * @return 指定日付の最終時刻
     */
    private Timestamp getEndOfDate(Timestamp timestamp) {
        if (timestamp == null) {
            return null;
        }
        Calendar cal = Calendar.getInstance();
        cal.setTime(timestamp);
        cal.set(Calendar.HOUR_OF_DAY, 23);
        cal.set(Calendar.MINUTE, 59);
        cal.set(Calendar.SECOND, 59);
        cal.set(Calendar.MILLISECOND, 999);
        return new Timestamp(cal.getTimeInMillis());
    }

    /**
     * 商品情報の文字列を作成する
     *
     * @param noveltyGoodsList ノベルティプレゼント商品レスポンスリスト
     * @return 商品情報の文字列
     */
    private String createGoodsInfoStr(List<NoveltySearchResultGoodsResponse> noveltyGoodsList) {
        StringBuilder buf = new StringBuilder();
        for (NoveltySearchResultGoodsResponse noveltyGoodsResponse : noveltyGoodsList) {
            buf.append(createGoodsInfoStr(noveltyGoodsResponse));
        }
        return buf.toString();
    }

    /**
     * 商品情報の文字列を作成する
     *
     * @param noveltyGoodsResponse ノベルティプレゼント商品レスポンス
     * @return 商品情報の文字列
     */
    private String createGoodsInfoStr(NoveltySearchResultGoodsResponse noveltyGoodsResponse) {
        StringBuilder buf = new StringBuilder();

        if (noveltyGoodsResponse.getNoveltyGoodsName() != null) {
            String unit1 = noveltyGoodsResponse.getUnitValue1();
            String unit2 = noveltyGoodsResponse.getUnitValue2();

            Integer salesPossibleStock = noveltyGoodsResponse.getSalesPossibleStock();

            boolean unitExist = false;
            if (unit1 != null || unit2 != null) {
                unitExist = true;
            }

            buf.append(noveltyGoodsResponse.getNoveltyGoodsName());

            if (unitExist) {
                String unitDelim = "";
                buf.append("＜");
                if (unit1 != null) {
                    buf.append(unit1);
                    unitDelim = "／";
                }
                if (unit2 != null) {
                    buf.append(unitDelim);
                    buf.append(unit2);
                }
                buf.append("＞");
            }

            buf.append("(");
            buf.append(salesPossibleStock);
            buf.append(")");
            buf.append("<br/>");
        }

        return buf.toString();
    }
}