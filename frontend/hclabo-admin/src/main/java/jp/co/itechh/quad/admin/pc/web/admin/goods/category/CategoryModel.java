/*
 * Project Name : HIT-MALL4
 */

package jp.co.itechh.quad.admin.pc.web.admin.goods.category;

import jp.co.itechh.quad.admin.annotation.converter.HCDate;
import jp.co.itechh.quad.admin.annotation.validator.HVDate;
import jp.co.itechh.quad.admin.annotation.validator.HVItems;
import jp.co.itechh.quad.admin.annotation.validator.HVRDateGreaterEqual;
import jp.co.itechh.quad.admin.annotation.validator.HVRSeparateDateTime;
import jp.co.itechh.quad.admin.constant.type.HTypeCategoryType;
import jp.co.itechh.quad.admin.constant.type.HTypeConditionColumnType;
import jp.co.itechh.quad.admin.constant.type.HTypeFrontDisplayStatus;
import jp.co.itechh.quad.admin.constant.type.HTypeOpenStatus;
import jp.co.itechh.quad.admin.pc.web.admin.common.validation.group.DisplayChangeGroup;
import jp.co.itechh.quad.admin.pc.web.admin.common.validation.group.PreviewGroup;
import jp.co.itechh.quad.admin.pc.web.admin.common.validation.group.SearchGroup;
import jp.co.itechh.quad.admin.validator.HDateValidator;
import jp.co.itechh.quad.admin.web.AbstractModel;
import jp.co.itechh.quad.category.presentation.api.param.CategoryResponse;
import lombok.Data;
import lombok.EqualsAndHashCode;
import org.hibernate.validator.constraints.Length;

import javax.validation.constraints.NotEmpty;
import java.util.List;
import java.util.Map;

/**
 * カテゴリー検索
 *
 * @author Pham Quang Dieu (VTI Japan Co., Ltd.)
 */
@Data
@EqualsAndHashCode(callSuper = false)
@HVRDateGreaterEqual(target = "searchCategoryOpenStartTimeTo", comparison = "searchCategoryOpenStartTimeFrom",
                     groups = {SearchGroup.class, DisplayChangeGroup.class})
@HVRDateGreaterEqual(target = "searchCategoryOpenEndTimeTo", comparison = "searchCategoryOpenEndTimeFrom",
                     groups = {SearchGroup.class, DisplayChangeGroup.class})
@HVRSeparateDateTime(targetDate = "previewDate", targetTime = "previewTime",
                     groups = {SearchGroup.class, DisplayChangeGroup.class})
public class CategoryModel extends AbstractModel {

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

    /** カテゴリキーワード集計 */
    @Length(min = 0, max = 120, groups = {SearchGroup.class, DisplayChangeGroup.class})
    private String categorySearchKeyword;

    /** 公開状態 */
    @HVItems(target = HTypeOpenStatus.class)
    private String openStatus;

    /** 公開開始日時(From) */
    @HCDate
    @HVDate(groups = {SearchGroup.class, DisplayChangeGroup.class})
    private String searchCategoryOpenStartTimeFrom;

    /** 公開開始日時(To) */
    @HCDate
    @HVDate(groups = {SearchGroup.class, DisplayChangeGroup.class})
    private String searchCategoryOpenStartTimeTo;

    /** 公開終了日時(From) */
    @HCDate
    @HVDate(groups = {SearchGroup.class, DisplayChangeGroup.class})
    private String searchCategoryOpenEndTimeFrom;

    /** 公開終了日時(To) */
    @HCDate
    @HVDate(groups = {SearchGroup.class, DisplayChangeGroup.class})
    private String searchCategoryOpenEndTimeTo;

    /** 期間選択リスト */
    private Map<String, String> openStatusItems;

    /** 表示状態 */
    @HVItems(target = HTypeCategoryType.class, groups = {SearchGroup.class, DisplayChangeGroup.class})
    private String[] searchCategoryTypeArray;

    /** カテゴリ種別 */
    private Map<String, String> searchCategoryTypeItems;

    /** 条件項目 */
    private Map<String, String> conditionColumnItems;

    /** 条件項目 */
    @HVItems(target = HTypeConditionColumnType.class, groups = {SearchGroup.class, DisplayChangeGroup.class})
    private String conditionColumn1;

    /** 条件値 */
    @Length(min = 0, max = 100, groups = {SearchGroup.class, DisplayChangeGroup.class})
    private String conditionValue1;

    /** 条件項目 */
    @HVItems(target = HTypeConditionColumnType.class, groups = {SearchGroup.class, DisplayChangeGroup.class})
    private String conditionColumn2;

    /** 条件値 */
    @Length(min = 0, max = 100, groups = {SearchGroup.class, DisplayChangeGroup.class})
    private String conditionValue2;

    /** 条件項目 */
    @HVItems(target = HTypeConditionColumnType.class, groups = {SearchGroup.class, DisplayChangeGroup.class})
    private String conditionColumn3;

    /** 条件値 */
    @Length(min = 0, max = 100, groups = {SearchGroup.class, DisplayChangeGroup.class})
    private String conditionValue3;

    /** 条件項目 */
    @HVItems(target = HTypeConditionColumnType.class, groups = {SearchGroup.class, DisplayChangeGroup.class})
    private String conditionColumn4;

    /** 条件値 */
    @Length(min = 0, max = 100, groups = {SearchGroup.class, DisplayChangeGroup.class})
    private String conditionValue4;

    /** 条件項目 */
    @HVItems(target = HTypeConditionColumnType.class, groups = {SearchGroup.class, DisplayChangeGroup.class})
    private String conditionColumn5;

    /** 条件値 */
    @Length(min = 0, max = 100, groups = {SearchGroup.class, DisplayChangeGroup.class})
    private String conditionValue5;

    /** フロント表示Items */
    private Map<String, String> frontDisplayItems;

    /** フロント表示 */
    @HVItems(target = HTypeFrontDisplayStatus.class)
    private String[] frontDisplayArray;

    /** プレビュー日付 */
    @HCDate
    @HVDate(groups = {SearchGroup.class, DisplayChangeGroup.class})
    private String previewDate;

    /** プレビュー時間 */
    @HCDate(pattern = "HH:mm:ss")
    @HVDate(pattern = "HH:mm:ss", message = HDateValidator.NOT_DATE_TIME_MESSAGE_ID,
            groups = {SearchGroup.class, DisplayChangeGroup.class})
    private String previewTime;

    /** プレビュー日付（ダイアログ用） */
    @HCDate
    @HVDate(groups = {PreviewGroup.class})
    @NotEmpty(message = "{HSeparateDateTimeValidator.NOT_DATE_detail}", groups = {PreviewGroup.class})
    private String dialogPreviewDate;

    /** プレビュー時間（ダイアログ用） */
    @HCDate(pattern = "HH:mm:ss")
    @HVDate(pattern = "HH:mm:ss", message = HDateValidator.NOT_DATE_TIME_MESSAGE_ID, groups = {PreviewGroup.class})
    private String dialogPreviewTime;

    /**プレビューアクセスキー */
    private String preKey;

    /** プレビュー日時 */
    private String preTime;

    /** カテゴリレスポンス */
    private List<CategoryResponse> categoryList;

    /** 一覧 */
    private List<CategoryItem> resultItems;

    /** 選択された行 */
    private String resultIndex;

    /**
     * 検索結果表示判定<br/>
     *
     * @return true=検索結果がnull以外(0件リスト含む), false=検索結果がnull
     */
    public boolean isResult() {
        return getResultItems() != null;
    }

}