/*
 * Project Name : HIT-MALL4
 */

package jp.co.itechh.quad.admin.pc.web.admin.goods.category;

import jp.co.itechh.quad.admin.base.utility.ApplicationContextUtility;
import jp.co.itechh.quad.admin.base.utility.ConversionUtility;
import jp.co.itechh.quad.admin.base.utility.DateUtility;
import jp.co.itechh.quad.admin.constant.type.HTypeCategoryType;
import jp.co.itechh.quad.admin.constant.type.HTypeConditionColumnType;
import jp.co.itechh.quad.admin.constant.type.HTypeConditionOperatorType;
import jp.co.itechh.quad.admin.constant.type.HTypeFrontDisplayStatus;
import jp.co.itechh.quad.admin.util.common.EnumTypeUtil;
import jp.co.itechh.quad.category.presentation.api.param.CategoryConditionDetailResponse;
import jp.co.itechh.quad.category.presentation.api.param.CategoryListGetRequest;
import jp.co.itechh.quad.category.presentation.api.param.CategoryListResponse;
import jp.co.itechh.quad.category.presentation.api.param.CategoryResponse;
import org.apache.commons.lang3.ObjectUtils;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

/**
 * カテゴリー検索Helper
 *
 * @author Pham Quang Dieu (VTI Japan Co., Ltd.)
 */
@Component
public class CategoryHelper {

    /** 問い合わせ者表示用スペース */
    private static final String SPACE = "　";

    /** 改行記号: RoleHierarchyImplクラスで "\n" に基づいて階層的な権限リストをsplitしているため */
    private static final String LINE_SEPARATOR_SYMBOL = "<br>";

    /** 手動ひもづけ */
    private static final String MANUAL = "手動ひもづけ";

    /** 日付のフォーマットパターン */
    public static final String DATE_PATTERN = "yyyy/MM/dd";

    /** フォーマット定数：時分秒 */
    public static final String TIME_FORMAT = "HH:mm:ss";

    /** 変換ユーティリティクラス */
    private final ConversionUtility conversionUtility;

    /** 日付関連ユーティリティ */
    private final DateUtility dateUtility;

    /**
     * コンストラクター
     *
     * @param conversionUtility
     */
    @Autowired
    public CategoryHelper(ConversionUtility conversionUtility, DateUtility dateUtility) {
        this.conversionUtility = conversionUtility;
        this.dateUtility = dateUtility;
    }

    /**
     * 検索条件作成
     *
     * @param categoryModel カテゴリー検索
     * @return カテゴリ登録リクエスト
     */
    public CategoryListGetRequest toCategoryListGetRequestForSearch(CategoryModel categoryModel) {
        CategoryListGetRequest categoryListGetRequest = new CategoryListGetRequest();
        // 変換Helper取得 / 日付関連Helper取得
        ConversionUtility conversionUtility = ApplicationContextUtility.getBean(ConversionUtility.class);
        DateUtility dateUtility = ApplicationContextUtility.getBean(DateUtility.class);

        categoryListGetRequest.setCategorySearchKeyword(categoryModel.getCategorySearchKeyword());
        categoryListGetRequest.setOpenStatus(categoryModel.getOpenStatus());
        // 公開開始日時From
        if (dateUtility.isDate(categoryModel.getSearchCategoryOpenStartTimeFrom(), DATE_PATTERN)) {
            categoryListGetRequest.setOpenStartTimeFrom(
                            conversionUtility.toDate(categoryModel.getSearchCategoryOpenStartTimeFrom()));
        }

        // 公開開始日時To
        if (dateUtility.isDate(categoryModel.getSearchCategoryOpenStartTimeTo(), DATE_PATTERN)) {
            categoryListGetRequest.setOpenStartTimeTo(conversionUtility.toDate(dateUtility.getEndOfDate(
                            conversionUtility.toTimeStamp(categoryModel.getSearchCategoryOpenStartTimeTo()))));
        }

        // 公開終了日時From
        if (dateUtility.isDate(categoryModel.getSearchCategoryOpenEndTimeFrom(), DATE_PATTERN)) {
            categoryListGetRequest.setOpenEndTimeFrom(
                            conversionUtility.toDate(categoryModel.getSearchCategoryOpenEndTimeFrom()));
        }

        // 公開終了日時To
        if (dateUtility.isDate(categoryModel.getSearchCategoryOpenEndTimeTo(), DATE_PATTERN)) {
            categoryListGetRequest.setOpenEndTimeTo(conversionUtility.toDate(dateUtility.getEndOfDate(
                            conversionUtility.toTimeStamp(categoryModel.getSearchCategoryOpenEndTimeTo()))));
        }

        if (ObjectUtils.isNotEmpty(categoryModel.getSearchCategoryTypeArray())) {
            categoryListGetRequest.setCategoryType(Arrays.asList(categoryModel.getSearchCategoryTypeArray()));
        }
        categoryListGetRequest.setConditionColumn1(categoryModel.getConditionColumn1());
        categoryListGetRequest.setConditionValue1(categoryModel.getConditionValue1());
        categoryListGetRequest.setConditionColumn2(categoryModel.getConditionColumn2());
        categoryListGetRequest.setConditionValue2(categoryModel.getConditionValue2());
        categoryListGetRequest.setConditionColumn3(categoryModel.getConditionColumn3());
        categoryListGetRequest.setConditionValue3(categoryModel.getConditionValue3());
        categoryListGetRequest.setConditionColumn4(categoryModel.getConditionColumn4());
        categoryListGetRequest.setConditionValue4(categoryModel.getConditionValue4());
        categoryListGetRequest.setConditionColumn5(categoryModel.getConditionColumn5());
        categoryListGetRequest.setConditionValue5(categoryModel.getConditionValue5());

        // フロント表示状態
        if (categoryModel.getFrontDisplayArray() != null && categoryModel.getFrontDisplayArray().length > 0) {
            categoryListGetRequest.setFrontDisplayList(Arrays.asList(categoryModel.getFrontDisplayArray()));
        } else {
            categoryListGetRequest.setFrontDisplayList(Arrays.asList(HTypeFrontDisplayStatus.NO_OPEN.getValue(),
                                                                     HTypeFrontDisplayStatus.OPEN.getValue()
                                                                    ));
        }

        // フロント表示基準日時（プレビュー日時欄の入力条件）
        if (StringUtils.isNotBlank(categoryModel.getPreviewDate())) {
            if (StringUtils.isBlank(categoryModel.getPreviewTime())) {
                //　日付のみ入力の場合、時間を設定
                categoryModel.setPreviewTime(this.conversionUtility.DEFAULT_START_TIME);
            }
            if (dateUtility.isDate(categoryModel.getPreviewDate(), DATE_PATTERN) && dateUtility.isDate(
                            categoryModel.getPreviewTime(), TIME_FORMAT)) {
                categoryListGetRequest.setFrontDisplayReferenceDate(this.conversionUtility.toDate(
                                this.conversionUtility.toTimeStamp(categoryModel.getPreviewDate(),
                                                                   categoryModel.getPreviewTime()
                                                                  )));
            }
        }

        return categoryListGetRequest;
    }

    /**
     * 検索結果をモデルに反映<br/>
     *
     * @param categoryListResponse      カテゴリ一覧レスポンス
     * @param categoryModel             カテゴリー検索
     */
    public void toPageForSearch(CategoryListResponse categoryListResponse, CategoryModel categoryModel) {

        int index = ((categoryListResponse.getPageInfo().getPage() - 1) * categoryListResponse.getPageInfo()
                                                                                              .getLimit()) + 1;
        List<CategoryItem> resultItemList = new ArrayList<>();

        if (categoryListResponse.getCategoryList() != null) {
            for (CategoryResponse categoryResponse : categoryListResponse.getCategoryList()) {
                CategoryItem categoryItem = ApplicationContextUtility.getBean(CategoryItem.class);
                categoryItem.setResultNo(index++);
                categoryItem.setCategoryId(categoryResponse.getCategoryId());
                categoryItem.setCategoryName(categoryResponse.getCategoryName());
                categoryItem.setOpenGoodsCount(categoryResponse.getOpenGoodsCount());
                if (HTypeCategoryType.NORMAL.getValue().equals(categoryResponse.getCategoryType())) {
                    categoryItem.setCategoryCondition(MANUAL);
                } else {
                    if (categoryResponse.getCategoryCondition() != null
                        && categoryResponse.getCategoryCondition().getConditionDetailList() != null) {
                        StringBuilder categoryCondition = new StringBuilder();
                        for (CategoryConditionDetailResponse categoryConditionDetailResponse : categoryResponse.getCategoryCondition()
                                                                                                               .getConditionDetailList()) {

                            if (ObjectUtils.isNotEmpty(categoryCondition)) {
                                categoryCondition.append(LINE_SEPARATOR_SYMBOL);
                            }

                            categoryCondition.append(EnumTypeUtil.getLabelValue(
                                            EnumTypeUtil.getEnumFromValue(HTypeConditionColumnType.class,
                                                                          categoryConditionDetailResponse.getConditionColumn()
                                                                         )));
                            if (StringUtils.isNotEmpty(categoryConditionDetailResponse.getConditionValue())) {
                                categoryCondition.append(SPACE)
                                                 .append(categoryConditionDetailResponse.getConditionValue());
                            }
                            categoryCondition.append(SPACE)
                                             .append(EnumTypeUtil.getLabelValue(EnumTypeUtil.getEnumFromValue(
                                                             HTypeConditionOperatorType.class,
                                                             categoryConditionDetailResponse.getConditionOperator()
                                                                                                             )));
                        }
                        categoryItem.setCategoryCondition(categoryCondition.toString());
                    }
                }
                categoryItem.setFrontDisplay(categoryResponse.getFrontDisplay());
                if (StringUtils.isNotBlank(categoryModel.getPreviewDate())) {
                    if (dateUtility.isDate(categoryModel.getPreviewDate(), DATE_PATTERN) && dateUtility.isDate(
                                    categoryModel.getPreviewTime(), TIME_FORMAT)) {
                        categoryItem.setFrontDisplayReferenceDate(conversionUtility.toTimestamp(
                                        this.conversionUtility.toDate(this.conversionUtility.toTimeStamp(
                                                        categoryModel.getPreviewDate(),
                                                        categoryModel.getPreviewTime()
                                                                                                        ))));
                    }
                }

                resultItemList.add(categoryItem);
            }
        }

        categoryModel.setResultItems(resultItemList);
    }
}