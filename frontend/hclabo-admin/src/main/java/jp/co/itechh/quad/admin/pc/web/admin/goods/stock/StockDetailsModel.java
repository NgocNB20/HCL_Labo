/*
 * Project Name : HIT-MALL4
 */

package jp.co.itechh.quad.admin.pc.web.admin.goods.stock;

import jp.co.itechh.quad.admin.web.AbstractModel;
import lombok.Data;
import lombok.EqualsAndHashCode;

import java.util.List;
import java.util.Map;

/**
 * 商品管理 在庫詳細画面<br/>
 *
 * @author Pham Quang Dieu (VTI Japan Co., Ltd.)
 */
@Data
@EqualsAndHashCode(callSuper = false)
public class StockDetailsModel extends AbstractModel {

    /**
     * コンストラクタ<br/>
     * 初期値の設定<br/>
     *
     */
    public StockDetailsModel() {
        super();
    }

    /**
     * 入庫実績一覧<br/>
     */
    private List<StockDetailsModelItem> stockResultItems;

    /**
     * 商品SEQ<br/>
     */
    private Integer goodsSeq;

    /* 商品基本情報 */

    /**
     * 商品管理番号<br/>
     */
    private String goodsGroupCode;

    /* 商品規格情報 */

    /**
     * 商品コード<br/>
     */
    private String goodsCode;

    /**
     * 規格タイトル１<br/>
     */
    private String unitTitle1;

    /**
     * 規格タイトル２<br/>
     */
    private String unitTitle2;

    /**
     * 規格１<br/>
     */
    private String unitValue1;

    /**
     * 規格２<br/>
     */
    private String unitValue2;

    /**
     * 入庫実績履歴の有無
     *
     * @return true=有、false=無
     */
    public boolean isStockResultHistoryExist() {
        return stockResultItems != null && !stockResultItems.isEmpty();
    }

    /* ページング用 */

    /**
     * 入庫実績リストインデックス<br/>
     */
    private int itemsIndex;

    /**
     * 表示件数アイテム<br/>
     */
    //    @HUItemList(component = "dispalyListCount")
    private Map<String, String> limitItems;

    /** 総件数 */
    private int totalCount;

    /** 表示件数 */
    private int limit;

    /** オフセット */
    private int offset;

    /** ソート項目名 */
    private String orderField;

    /** ソート昇順フラグ */
    private boolean orderAsc;

    /* アクセサ */

    /**
     * @return 検索アイテム名
     */
    public String getItemsName() {
        return "stockResultItems";
    }

    /**
     * 規格タイトル１の有無
     *
     * @return true=有、false=無
     */
    public boolean isUnitTitleFlg1() {
        if (this.unitTitle1 != null && !this.unitTitle1.isEmpty()) {
            return true;
        }
        return false;
    }

    /**
     * 規格タイトル2の有無
     *
     * @return true=有、false=無
     */
    public boolean isUnitTitleFlg2() {
        if (this.unitTitle2 != null && !this.unitTitle2.isEmpty()) {
            return true;
        }
        return false;
    }
}