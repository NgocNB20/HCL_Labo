package jp.co.itechh.quad.admin.pc.web.admin.totaling.goodssales;

import jp.co.itechh.quad.admin.annotation.converter.HCDate;
import jp.co.itechh.quad.admin.annotation.validator.HVDate;
import jp.co.itechh.quad.admin.annotation.validator.HVItems;
import jp.co.itechh.quad.admin.annotation.validator.HVRDateGreaterEqual;
import jp.co.itechh.quad.admin.annotation.validator.HVRDateRange;
import jp.co.itechh.quad.admin.constant.type.HTypeAggregateUnit;
import jp.co.itechh.quad.admin.constant.type.HTypeTargetData;
import jp.co.itechh.quad.admin.entity.goods.category.CategoryEntity;
import jp.co.itechh.quad.admin.web.AbstractModel;
import lombok.Data;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Component;

import javax.validation.constraints.NotEmpty;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

/**
 * 商品別販売価格別集計モデル
 *
 * @author Pham Quang Dieu (VTI Japan Co., Ltd.)
 */
@Data
@HVRDateGreaterEqual(target = "aggregateTimeTo", comparison = "aggregateTimeFrom")
@HVRDateRange(target = "aggregateTimeTo", comparison = "aggregateTimeFrom", unit = "aggregateUnit")
public class GoodsSalesModel extends AbstractModel {

    /** ページ番号 */
    private String pageNumber;

    /** 最大表示件数 */
    private int limit;

    /** ソート項目 */
    private String orderField = "goodsGroupCode";

    /** ソート条件  */
    private boolean orderAsc = true;

    /** 検索結果総件数 */
    private int totalCount;

    /** 商品別販売価格別集計結果 */
    private List<GoodsSalesItem> resultItems;

    /** 商品別販売価格別集計の合計 */
    private GoodsSalesSummary resultSummary;

    /** 集計期間 */
    private List<String> rangeDateStr;

    /** 集計期間From */
    @NotEmpty
    @HVDate
    @HCDate
    private String aggregateTimeFrom;

    /** 集計期間To */
    @NotEmpty
    @HVDate
    @HCDate
    private String aggregateTimeTo;

    /** 集計単位 */
    @HVItems(target = HTypeAggregateUnit.class)
    private String aggregateUnit = HTypeAggregateUnit.MONTH.getValue();

    /** 対象データ */
    @HVItems(target = HTypeTargetData.class)
    private String orderStatus = HTypeTargetData.ORDER.getValue();

    /** 受注デバイス */
    private Map<String, String> deviceItems;

    /** 受注デバイス */
    private String[] deviceArray = new String[] {};

    /** 商品名 */
    private String searchGoodsName;

    /** 商品番号 */
    private String searchGoodsCode;

    /** カテゴリーIDリスト */
    private List<String> categoryIdList = new ArrayList<>();

    /** カテゴリーNameリスト */
    private List<String> categoryNameList = new ArrayList<>();

    /** カテゴリーリスト */
    private List<CategoryEntity> categoryList;

    /** 商品販売個数集計CSVの出力ボタン押下フラグ */
    private boolean selectGoodsSalesCSVFlag;

    /**
     * 商品販売個数集計CSVの出力ボタンを押したかどうか<br/>
     * このメソッド内部でフラグを初期化
     *
     * @return true=押下
     */
    public boolean isSelectGoodsSalesCSV() {

        if (selectGoodsSalesCSVFlag) {
            // 初期化
            selectGoodsSalesCSVFlag = false;
            return true;
        }
        return false;
    }

    /**
     * 検索結果表示項目.
     *
     * @return true=検索結果がnull以外(0件リスト含む), false=検索結果がnull
     */
    public boolean isResult() {
        return getResultItems() != null;
    }

}
