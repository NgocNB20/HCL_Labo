/*
 * Copyright (C) 2022 i-TEC HANKYU HANSHIN INC. All Rights Reserved.
 */

package jp.co.itechh.quad.admin.pc.web.admin.shop.news;

import jp.co.itechh.quad.admin.annotation.converter.HCDate;
import jp.co.itechh.quad.admin.annotation.validator.HVBothSideSpace;
import jp.co.itechh.quad.admin.annotation.validator.HVDate;
import jp.co.itechh.quad.admin.annotation.validator.HVItems;
import jp.co.itechh.quad.admin.annotation.validator.HVRDateGreaterEqual;
import jp.co.itechh.quad.admin.annotation.validator.HVSpecialCharacter;
import jp.co.itechh.quad.admin.constant.type.HTypeOpenStatus;
import jp.co.itechh.quad.admin.constant.type.SpaceValidateMode;
import jp.co.itechh.quad.admin.web.AbstractModel;
import lombok.Data;
import lombok.EqualsAndHashCode;
import org.hibernate.validator.constraints.Length;

import java.util.List;
import java.util.Map;

/**
 * ニュース検索
 *
 * @author kimura
 *
 */
@Data
@EqualsAndHashCode(callSuper = false)
@HVRDateGreaterEqual(target = "searchNewsTimeTo", comparison = "searchNewsTimeFrom")
@HVRDateGreaterEqual(target = "searchNewsOpenStartTimeTo", comparison = "searchNewsOpenStartTimeFrom")
@HVRDateGreaterEqual(target = "searchNewsOpenEndTimeTo", comparison = "searchNewsOpenEndTimeFrom")
public class NewsModel extends AbstractModel {

    /**
     * ニュース表示状態（非表示）
     */
    public static final String NEWS_DISPLAY_STATUS_HIDE = "0";
    /**
     * ニュース表示状態（表示中）
     */
    public static final String NEWS_DISPLAY_STATUS_DISPLAY = "1";

    /** ページ番号 */
    private String pageNumber;

    /** 最大表示件数 */
    private int limit;

    /** ソート項目 */
    private String orderField;

    /** ソート条件 */
    private boolean orderAsc;

    /** 検索結果総件数 */
    private int totalCount;

    /**
     * 検索一覧<br/>
     */
    private List<NewsModelItem> resultItems;

    /**
     * 行番号<br/>
     */
    private int resultIndex;

    /**
     * limit動的Validator target<br/>
     *
     * @return target
     */
    protected String getLimitValidatorTarget() {
        return "doNewsSearch,doDisplayChange";
    }

    /************************************
     ** 検索条件
     ************************************/
    /**
     * ニュース日時(From)
     */
    @HCDate
    @HVDate
    private String searchNewsTimeFrom;
    /**
     * ニュース日時(To)
     */
    @HCDate
    @HVDate
    private String searchNewsTimeTo;
    /**
     * タイトル
     */
    @HVSpecialCharacter(allowPunctuation = true)
    @Length(min = 0, max = 200)
    private String searchTitle;
    /**
     * URL
     */
    @HVSpecialCharacter(allowPunctuation = true)
    @Length(min = 0, max = 200)
    private String searchURL;
    /**
     * 本文・詳細
     */
    @HVSpecialCharacter(allowPunctuation = true)
    @HVBothSideSpace(startWith = SpaceValidateMode.ALLOW_SPACE, endWith = SpaceValidateMode.ALLOW_SPACE)
    @Length(min = 0, max = 200)
    private String searchBodyNote;
    /**
     * ニュース公開状態
     */
    @HVItems(target = HTypeOpenStatus.class)
    private String searchNewsOpenStatus;
    /**
     * ニュース公開開始日時(From)
     */
    @HCDate
    @HVDate
    private String searchNewsOpenStartTimeFrom;
    /**
     * ニュース公開開始日時(To)
     */
    @HCDate
    @HVDate
    private String searchNewsOpenStartTimeTo;
    /**
     * ニュース公開終了日時(From)
     */
    @HCDate
    @HVDate
    private String searchNewsOpenEndTimeFrom;
    /**
     * ニュース公開終了日時(To)
     */
    @HCDate
    @HVDate
    private String searchNewsOpenEndTimeTo;

    /************************************
     ** 選択肢
     ************************************/
    /**
     * ニュース公開状態
     */
    private Map<String, String> searchNewsOpenStatusItems;

    /************************************
     ** 削除条件
     ************************************/
    /**
     * 削除対象ニュースSEQ
     */
    private Integer deleteNewsSeq;

    /**
     * 検索結果表示判定<br/>
     *
     * @return true=検索結果がnull以外(0件リスト含む), false=検索結果がnull
     */
    public boolean isResult() {
        return getResultItems() != null;
    }
}
