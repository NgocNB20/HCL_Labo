/*
 * Project Name : HIT-MALL4
 */

package jp.co.itechh.quad.admin.pc.web.admin.goods.category;

import com.fasterxml.jackson.annotation.JsonIgnore;
import jp.co.itechh.quad.admin.annotation.converter.HCDate;
import jp.co.itechh.quad.admin.annotation.validator.HVBothSideSpace;
import jp.co.itechh.quad.admin.annotation.validator.HVDate;
import jp.co.itechh.quad.admin.annotation.validator.HVImageFile;
import jp.co.itechh.quad.admin.annotation.validator.HVItems;
import jp.co.itechh.quad.admin.annotation.validator.HVRDateCombo;
import jp.co.itechh.quad.admin.annotation.validator.HVSpecialCharacter;
import jp.co.itechh.quad.admin.base.constant.ValidatorConstants;
import jp.co.itechh.quad.admin.constant.type.HTypeCategoryConditionType;
import jp.co.itechh.quad.admin.constant.type.HTypeCategoryType;
import jp.co.itechh.quad.admin.constant.type.HTypeManualDisplayFlag;
import jp.co.itechh.quad.admin.constant.type.HTypeSiteMapFlag;
import jp.co.itechh.quad.admin.constant.type.SpaceValidateMode;
import jp.co.itechh.quad.admin.dto.goods.category.CategoryDto;
import jp.co.itechh.quad.admin.entity.goods.category.CategoryDisplayEntity;
import jp.co.itechh.quad.admin.entity.goods.category.CategoryEntity;
import jp.co.itechh.quad.admin.pc.web.admin.common.validation.group.ImageUploadGroup;
import jp.co.itechh.quad.admin.pc.web.admin.common.validation.group.PreviewGroup;
import jp.co.itechh.quad.admin.pc.web.admin.goods.category.validation.group.NextGroup;
import jp.co.itechh.quad.admin.validator.HDateValidator;
import jp.co.itechh.quad.admin.web.AbstractModel;
import lombok.Data;
import lombok.EqualsAndHashCode;
import org.apache.commons.lang.StringUtils;
import org.hibernate.validator.constraints.Length;
import org.springframework.web.multipart.MultipartFile;

import javax.validation.Valid;
import javax.validation.constraints.NotEmpty;
import javax.validation.constraints.Pattern;
import java.util.List;
import java.util.Map;

/**
 * カテゴリ管理 : カテゴリ入力
 *
 * @author Pham Quang Dieu (VTI Japan Co., Ltd.)
 */

@Data
@EqualsAndHashCode(callSuper = false)
@HVRDateCombo(dateLeftTarget = "categoryOpenFromDatePC", timeLeftTarget = "categoryOpenFromTimePC",
              dateRightTarget = "categoryOpenToDatePC", timeRightTarget = "categoryOpenToTimePC",
              groups = {NextGroup.class})
public class CategoryInputModel extends AbstractModel {

    /** メッセージコード：不正操作 */
    protected static final String MSGCD_ILLEGAL_OPERATION = "AGC000026";

    /** ファイル削除失敗メッセージ */
    protected static final String MSGCD_FAIL_DELETE = "AGC000504";

    /** カテゴリ情報(画面受渡し用) */
    private CategoryDto categoryDto;

    /** カテゴリ情報(DBの値を保持用) */
    private CategoryDto categoryDtoDB;

    /** ターゲット親カテゴリ */
    private CategoryTreeItem targetParentCategory;

    /** カテゴリツリー */
    private List<CategoryTreeItem> categoryTreeItems;

    /** 登録カテゴリ */
    private Integer categoryTree;

    /** カテゴリSEQ(画面用) */
    private Integer scCategorySeq;

    /** 一覧画面遷移フラグ */
    private boolean listScreen;

    /** 確認画面遷移フラグ */
    private boolean confirmScreen;

    /** カテゴリ確認用連結文字列(一覧からのみ利用) */
    private String categoryPathName;

    /** PCテンプ画像をアップロードしてるかどうか */
    private boolean tmpImagePC;

    /** ターゲットカテゴリ */
    private Integer target;

    /** PCアップロードファイル名 */
    private String fileNamePC;

    /** 変更前カテゴリエンティティ */
    private CategoryEntity originalCategoryEntity;

    /** 変更前カテゴリ表示エンティティ */
    private CategoryDisplayEntity originalCategoryDisplayEntity;

    /** ######################### */
    /** ########Category######### */
    /** ######################### */

    /** カテゴリID */
    @NotEmpty(groups = {NextGroup.class})
    @Length(min = 1, max = ValidatorConstants.LENGTH_CATEGORY_ID_MAXIMUM, groups = {NextGroup.class})
    @Pattern(regexp = ValidatorConstants.REGEX_CATEGORY_ID, message = ValidatorConstants.MSGCD_REGEX_CATEGORY_ID,
             groups = {NextGroup.class})
    private String categoryId;

    /** カテゴリ名 */
    @HVSpecialCharacter(allowPunctuation = true, groups = {NextGroup.class})
    @NotEmpty(groups = {NextGroup.class})
    @Length(min = 1, max = 120, groups = {NextGroup.class})
    @HVBothSideSpace(startWith = SpaceValidateMode.ALLOW_SPACE, endWith = SpaceValidateMode.ALLOW_SPACE,
                     groups = {NextGroup.class})
    private String categoryName;

    /** カテゴリ公開状態PC */
    @NotEmpty(message = "{HRequiredValidator.REQUIRED_detail}", groups = {NextGroup.class})
    private String categoryOpenStatusPC;

    /** カテゴリ公開開始日付PC */
    @HCDate
    @HVDate(groups = {NextGroup.class})
    private String categoryOpenFromDatePC;

    /** カテゴリ公開開始時間PC */
    @HCDate(pattern = "HH:mm:ss")
    @HVDate(pattern = "HH:mm:ss", groups = {NextGroup.class})
    private String categoryOpenFromTimePC;

    /** カテゴリ公開終了日付PC */
    @HCDate
    @HVDate(groups = {NextGroup.class})
    private String categoryOpenToDatePC;

    /** カテゴリ公開終了時間PC */

    @HCDate(pattern = "HH:mm:ss")
    @HVDate(pattern = "HH:mm:ss", groups = {NextGroup.class})
    private String categoryOpenToTimePC;

    /** カテゴリ種別 */
    @HVItems(target = HTypeCategoryType.class)
    private String categoryType = HTypeCategoryType.AUTO.getValue();

    /** 手動表示フラグ */
    @HVItems(target = HTypeManualDisplayFlag.class)
    private String manualDisplayFlag = HTypeManualDisplayFlag.OFF.getValue();

    /** カテゴリ公開状態PCのコンボボックス */
    private Map<String, String> categoryOpenStatusPCItems;

    /** サイトマップ出力フラグ */
    @HVItems(target = HTypeSiteMapFlag.class)
    private String siteMapFlag = HTypeSiteMapFlag.OFF.getValue();

    /** ######################### */
    /** #####CategoryDisplay##### */
    /** ######################### */

    /** フリーテキストPC */
    //    TODO-QUAD-787
    //    @HVHtml(innerHtml = true)
    @Length(min = 0, max = 20000, message = "{HTextAreaValidator.LENGTH_detail}", groups = {NextGroup.class})
    @HVBothSideSpace(startWith = SpaceValidateMode.ALLOW_SPACE, endWith = SpaceValidateMode.ALLOW_SPACE,
                     groups = {NextGroup.class})
    @HVSpecialCharacter(allowPunctuation = true, groups = {NextGroup.class})
    private String freeTextPC;

    /** meta-description */
    @HVSpecialCharacter(
                    allowCharacters = {'!', '#', '%', '(', ')', '*', '+', ',', '-', '.', '/', ':', ';', '=', '@', '[',
                                    ']', '_', '|'}, groups = {NextGroup.class})
    @Length(min = 0, max = 400, groups = {NextGroup.class})
    @HVBothSideSpace(startWith = SpaceValidateMode.ALLOW_SPACE, endWith = SpaceValidateMode.ALLOW_SPACE,
                     groups = {NextGroup.class})
    private String metaDescription;

    /** meta-keyword */
    @HVSpecialCharacter(
                    allowCharacters = {'!', '#', '%', '(', ')', '*', '+', ',', '-', '.', '/', ':', ';', '=', '@', '[',
                                    ']', '_', '|'}, groups = {NextGroup.class})
    @Length(min = 0, max = 400, groups = {NextGroup.class})
    @HVBothSideSpace(startWith = SpaceValidateMode.ALLOW_SPACE, endWith = SpaceValidateMode.ALLOW_SPACE,
                     groups = {NextGroup.class})
    private String metaKeyword;

    /** カテゴリ画像PCアップロード用 */
    @JsonIgnore
    @HVImageFile(groups = ImageUploadGroup.class)
    private MultipartFile uploadCategoryImagePC;

    /** カテゴリ画像PC */
    private String categoryImagePC;

    /** カテゴリ画像PCパス */
    private String categoryImagePathPC;

    /**
     * カテゴリSEQパス(画面受渡し用)
     * (カテゴリーボタンの展開保持に使用)
     */
    private String categorySeqPathTarget;

    /** 抽出カテゴリー名(プルダウン値画面受渡し用) */
    private String extractCategoryName;

    /** 初期表示(カテゴリー一覧) */
    private boolean initialDisplayList;

    /** goodsModelItems */
    private List<GoodsModelItem> goodsModelItems;

    /** 商品並び順項目 */
    private String goodsSortColumn;

    /** 商品並び順 */
    private Boolean goodsSortOrder;

    /** 品並び順表示 */
    private String goodsSortDisplay;

    /** 商品グループSEQソート */
    private List<Integer> goodsGroupSeqSort;

    /** テゴリ商品ソート */
    private Map<String, String> categoryGoodsSortItems;

    /** カテゴリ商品手動ソート */
    private Map<String, String> categoryGoodsSortManualItems;

    /** カテゴリ条件種別 */
    private String categoryConditionType = HTypeCategoryConditionType.AND.getValue();

    /** 条件項目 */
    private Map<String, String> categoryConditionColumnItems;

    /** 条件演算子 */
    private Map<String, String> categoryConditionOperatorItems;

    /** カテゴリ条件 */
    @Valid
    private List<CategoryConditionItem> categoryConditionItems;

    /**
     * カテゴリ画像PCの有無判定<br/>
     *
     * @return boolean
     */
    public boolean isExistCategoryImagePathPC() {
        if (StringUtils.isEmpty(categoryImagePathPC)) {
            return false;
        }
        return true;
    }

    /**
     * @return 公開期間PC設定あり = true
     */
    public boolean isCategoryOpenFromToPcExist() {
        if ((this.categoryOpenFromDatePC != null && !categoryOpenFromDatePC.isEmpty()) || (
                        this.categoryOpenToDatePC != null && !categoryOpenToDatePC.isEmpty())) {
            return true;
        }
        return false;
    }

    /***********************************************************
     ** カテゴリ確認
     ***********************************************************/
    /** コンストラクタ */
    public CategoryInputModel() {
        super();
    }

    /** カテゴリエンティティの変更箇所リスト */
    public List<String> modifiedCategoryList;

    /** カテゴリ表示エンティティの変更箇所リスト */
    public List<String> modifiedCategoryDisplayList;

    /** ######################### */
    /** ########Category######### */
    /** ######################### */

    /** 表示順 */
    private Integer orderDisplay;

    /** ルートカテゴリSEQ */
    private Integer rootCategorySeq;

    /** カテゴリパス */
    private String categoryPath;

    /** ターゲット親カテゴリID */
    private String targetParentCategoryId;

    /**
     * 画面リロード後の自動スクロールのターゲットポジション<br/>
     */
    private String targetAutoScrollTagId;

    /***********************************************************
     ** プレビュー用項目
     ***********************************************************/

    /** カテゴリーID */
    private String cid;

    /** フロント表示 */
    private String frontDisplay;

    /** プレビュー日付 */
    @NotEmpty(message = "{HSeparateDateTimeValidator.NOT_DATE_detail}", groups = {PreviewGroup.class})
    @HCDate
    @HVDate(groups = {PreviewGroup.class})
    private String previewDate;

    /** プレビュー時間 */
    @HCDate(pattern = "HH:mm:ss")
    @HVDate(pattern = "HH:mm:ss", message = HDateValidator.NOT_DATE_TIME_MESSAGE_ID, groups = {PreviewGroup.class})
    private String previewTime;

    /** プレビューアクセスキー */
    private String preKey;

    /** プレビュー日時 */
    private String preTime;

}