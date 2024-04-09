package jp.co.itechh.quad.admin.pc.web.admin.shop.freearea;

import jp.co.itechh.quad.admin.annotation.converter.HCDate;
import jp.co.itechh.quad.admin.annotation.validator.HVDate;
import jp.co.itechh.quad.admin.annotation.validator.HVItems;
import jp.co.itechh.quad.admin.annotation.validator.HVRDateGreaterEqual;
import jp.co.itechh.quad.admin.annotation.validator.HVSpecialCharacter;
import jp.co.itechh.quad.admin.constant.type.HTypeFreeAreaOpenStatus;
import jp.co.itechh.quad.admin.constant.type.HTypeSiteMapFlag;
import jp.co.itechh.quad.admin.pc.web.admin.common.validation.group.DisplayChangeGroup;
import jp.co.itechh.quad.admin.pc.web.admin.shop.freearea.validation.group.FreeAreaSearchGroup;
import jp.co.itechh.quad.admin.web.AbstractModel;
import lombok.Data;
import lombok.EqualsAndHashCode;
import org.hibernate.validator.constraints.Length;

import javax.validation.constraints.NotEmpty;
import javax.validation.constraints.Pattern;
import java.util.List;
import java.util.Map;

/**
 * フリーエリア検索モデル
 *
 * @author Pham Quang Dieu (VTI Japan Co., Ltd.)
 */
@Data
@EqualsAndHashCode(callSuper = false)
@HVRDateGreaterEqual(target = "searchOpenStartTimeTo", comparison = "searchOpenStartTimeFrom",
                     groups = {FreeAreaSearchGroup.class, DisplayChangeGroup.class})
public class FreeareaModel extends AbstractModel {

    /**
     * 表示状態-日時タイプ:現在日時
     */
    public static final String SEARCH_DATE_TYPE_CURRENTDATE = "0";

    /**
     * 表示状態-日時タイプ:指定日時
     */
    public static final String SEARCH_DATE_TYPE_TARGETDATE = "1";

    /**
     * 正規表現エラー
     */
    public static final String MSGCD_REGULAR_EXPRESSION_ERR = "{ASF000101W}";

    /**
     * ページ番号
     */
    private String pageNumber;

    /**
     * 最大表示件数
     */
    private int limit;

    /**
     * ソート項目
     */
    private String orderField;

    /**
     * ソート条件
     */
    private boolean orderAsc;

    /**
     * 検索結果総件数
     */
    private int totalCount;

    /**
     * 検索一覧<br/>
     */
    private List<FreeareaResultItem> resultItems;

    /**
     * 行番号<br/>
     */
    private int resultIndex;

    /**
     * limit動的Validator target<br/>
     *
     * @return target
     */
    /************************************
     **  検索条件
     ************************************/

    @Pattern(regexp = "^[\\w]+$", message = MSGCD_REGULAR_EXPRESSION_ERR,
             groups = {FreeAreaSearchGroup.class, DisplayChangeGroup.class})
    @Length(min = 0, max = 50, groups = {FreeAreaSearchGroup.class, DisplayChangeGroup.class})
    private String searchFreeAreaKey;

    /**
     * タイトル
     */
    @HVSpecialCharacter(allowPunctuation = true, groups = {FreeAreaSearchGroup.class, DisplayChangeGroup.class})
    @Length(min = 0, max = 100, groups = {FreeAreaSearchGroup.class, DisplayChangeGroup.class})
    private String searchFreeAreaTitle;

    /**
     * 公開開始日時(FROM)
     */
    @HCDate
    @HVDate(groups = {FreeAreaSearchGroup.class, DisplayChangeGroup.class})
    private String searchOpenStartTimeFrom;

    /**
     * 公開開始日時(TO)
     */
    @HCDate
    @HVDate(groups = {FreeAreaSearchGroup.class, DisplayChangeGroup.class})
    private String searchOpenStartTimeTo;

    /**
     * 表示状態-日時タイプ
     */
    @NotEmpty(groups = {FreeAreaSearchGroup.class, DisplayChangeGroup.class})
    @HVItems(targetArray = {"0", "1"}, groups = {FreeAreaSearchGroup.class, DisplayChangeGroup.class})
    private String searchDateType;

    /**
     * 表示状態-日付（日）
     */
    private String searchTargetDate;

    /**
     * 表示状態-日付（時間）
     */
    private String searchTargetTime;

    /**
     * 表示状態
     */
    @NotEmpty(groups = {FreeAreaSearchGroup.class, DisplayChangeGroup.class})
    @HVItems(target = HTypeFreeAreaOpenStatus.class, groups = {FreeAreaSearchGroup.class, DisplayChangeGroup.class})
    private String[] searchOpenStateArray = {"0", "1", "2"};

    /**
     * 表示状態 アイテムズ
     */
    private Map<String, String> searchOpenStateArrayItems;

    /**
     * サイトマップ出力
     */
    @HVItems(target = HTypeSiteMapFlag.class, groups = {FreeAreaSearchGroup.class, DisplayChangeGroup.class})
    private String searchSiteMapFlag = HTypeSiteMapFlag.OFF.getValue();

    /************************************
     ** 選択肢
     ************************************/

    private Map<String, String> searchSiteMapFlagItems;

    /************************************
     ** 削除条件
     ************************************/
    /**
     * 削除対象フリーエリアSEQ
     */
    private Integer deleteFreeAreaSeq;

    /**
     * 検索結果表示判定<br/>
     *
     * @return true=検索結果がnull以外(0件リスト含む), false=検索結果がnull
     */
    public boolean isResult() {
        return getResultItems() != null;
    }
}