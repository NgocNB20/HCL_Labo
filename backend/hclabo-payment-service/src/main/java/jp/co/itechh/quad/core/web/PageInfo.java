/*
 * Project Name : HIT-MALL4
 *
 * Copyright (C) 2021 i-TEC HANKYU HANSHIN INC. All Rights Reserved.
 *
 */

package jp.co.itechh.quad.core.web;

import lombok.Getter;
import org.apache.commons.lang3.StringUtils;
import org.seasar.doma.jdbc.SelectOptions;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Component;

import java.io.Serializable;
import java.util.Map;

/**
 * ページ情報クラス
 *
 * @author kn23834
 * @version $Revision: 1.0 $
 *
 */
@Getter
@Component
@Scope("prototype")
public class PageInfo implements Serializable {

    // ----------------------------
    // 定数
    // ----------------------------
    /** シリアルバージョンUID */
    private static final long serialVersionUID = 1L;
    /** デフォルトページ番号表示数 */
    private static final int DEFAULT_PAGE_LINK_COUNT = 10;
    /** デフォルト最大表示件数 */
    public static final int DEFAULT_LIMIT = 10;
    /** デフォルトページ番号 */
    public static final int DEFAULT_PAGE = 1;

    /** 表示形式：サムネイル表示 キー */
    public static final String VIEW_TYPE_THUMBNAIL_KEY = "thumbs";
    /** 表示形式：リスト表示 キー */
    public static final String VIEW_TYPE_LIST_KEY = "list";
    /** 並び順タイプ：標準 キー*/
    public static final String SORT_TYPE_NORMAL_KEY = "normal";
    /** 並び順タイプ：新着順 キー*/
    public static final String SORT_TYPE_REGISTDATE_KEY = "new";
    /** 並び順タイプ：価格順 キー*/
    public static final String SORT_TYPE_GOODSPRICE_KEY = "price";
    /** 並び順タイプ：売れ筋順 キー*/
    public static final String SORT_TYPE_SALECOUNT_KEY = "salableness";
    /** 昇降順：昇順 キー*/
    public static final String ASC_TYPE_TRUE_KEY = "true";
    /** 昇降順：降順 キー*/
    public static final String ASC_TYPE_FALSE_KEY = "false";

    /** 属性名 キー */
    public static final String ATTRIBUTE_NAME_KEY = "pageInfo";

    // ----------------------------
    // Doma2依存オブジェクト
    // ----------------------------
    /** 検索オプション */
    private SelectOptions selectOptions;

    // ----------------------------
    // 外部から設定されるフィールド
    // ----------------------------
    /** 表示最大件数 */
    private int limit;
    /** オフセット */
    private int offset;
    /** ページ番号 */
    private int page;
    /** 並替項目 */
    private String orderField;
    /** 並替昇順フラグ */
    private boolean orderAsc;
    /**
     * サムネイルかリストか
     * @see PageInfo#VIEW_TYPE_THUMBNAIL_KEY
     * @see PageInfo#VIEW_TYPE_LIST_KEY
     */
    private String vtype;
    /**
     * 並替項目マップ
     *
     * 　KEY:stype
     * 　VALUE:orderField
     * のマップ
     *
     * ※並替項目について、フロント商品検索などでGETパラメータ指定されることがあるが、
     * 　できるだけ短い文字列でパラメータ指定したいという要件がある
     * 　（逆に言うと、orderFieldで指定される文字列はSQLを意識しているため、文字数が少々長い。。）
     * これを解消するために、本Mapをもたせ
     * SQL実行前、画面ページャ取得前のタイミングでKEY⇔VALUEの変換を行う
     */
    private Map<String, String> stypeMap;
    /**
     * スキップカウントフラグ
     * true: 総件数取得をスキップ、false: 総件数取得を実行 [デフォルト]
     */
    private boolean skipCountFlg;

    // ----------------------------
    // 内部処理で算出されるフィールド
    // ----------------------------
    /** 前ページを表示するか */
    private int prevPage;
    /** 次ページを表示するか */
    private int nextPage;
    /** 最終ページ番号 */
    private int lastPageNo;
    /** 開始ページ番号 */
    private int startPageNo;
    /** 総ページ数 */
    private int totalPages;
    /** 総レコード数 */
    private int total;

    /**
     * コンストラクタ
     */
    public PageInfo() {

        // 属性の初期化
        this.offset = 0;
        this.limit = DEFAULT_LIMIT;
        this.page = DEFAULT_PAGE;

        this.skipCountFlg = false;

        // 【ポイント1】SelectOptions.get()でSelectOptionsのインスタンスを取得
        this.selectOptions = SelectOptions.get();
    }

    // ----------------------------
    // セットアップメソッド
    // ----------------------------

    /**
     * 検索実行用セットアップ
     * offsetを計算し、SelectOpitonsをセットアップする
     */
    public void setupSelectOptions() {
        // page、offsetを設定する
        if (this.page == DEFAULT_PAGE) {
            this.offset = 0;
        } else {
            this.offset = this.limit * (this.page - 1);
        }
        // 【ポイント2】SelectOptions.offset()でoffsetを設定
        // 【ポイント3】SelectOptions.limit()でlimitを設定
        selectOptions.offset(this.offset).limit(this.limit);

        // カウントスキップフラグがfalseの場合
        if (!skipCountFlg) {
            // 【ポイント4】SelectOptions.count()で集計件数を取得できるようになる
            selectOptions.count();
        }
    }

    /**
     * ページャ表示用にpageInfoのセットアップを行う
     */
    public void setupViewPager() {
        // 総件数取得をスキップする設定としている場合、設定値不備によりExceptionスロー
        if (skipCountFlg) {
            throw new IllegalStateException("skipCountFlgがtrueの状態では、ページング表示は実施できません");
        }

        this.prevPage = -1;
        this.nextPage = -1;

        // 集計件数を取得
        this.total = (int) this.selectOptions.getCount();
        // offsetが0より大きければ
        if (this.offset > 0) {
            // 前ページを表示
            this.prevPage = this.page - 1;
        }
        // limit+offsetが集計件数より小さければ
        if ((this.limit + this.offset) < this.total) {
            // 次ページを表示
            this.nextPage = this.page + 1;
        }
        // 最終ページ番号
        if (this.total == 0) {
            this.lastPageNo = 0;
        } else {
            this.lastPageNo = ((this.total - 1) / this.limit) + 1;
        }
        // 開始、終了ページの番号を取得する
        int[] startEndPageNumberArray =
                        getStartEndPageNumberArray(page, this.total, this.limit, PageInfo.DEFAULT_PAGE_LINK_COUNT,
                                                   lastPageNo
                                                  );
        this.startPageNo = startEndPageNumberArray[0];
        this.totalPages = lastPageNo;
    }

    /**
     * 開始、終了ページの番号を返すメソッド
     * @param page ページ番号
     * @param count 集計件数
     * @param limit 最大表示件数
     * @param pageLinkCount ページ番号表示数
     * @param lastPageNo 最終ページ番号
     * @return 開始、終了ページ番号の配列
     */
    private int[] getStartEndPageNumberArray(int page, int count, int limit, int pageLinkCount, int lastPageNo) {

        // 初期ページ番号
        int firstPageNo = 1;
        // ページ番号表示数の半分
        int pageLinkCountHalf = pageLinkCount / 2;
        // 前ページ表示数
        int prevPageCount = page - pageLinkCountHalf;
        // 次ページ表示数
        int nextPageCount = page + pageLinkCountHalf;
        // 偶数の場合 次ページ表示数=次ページ表示数-1 をする
        if (pageLinkCount % 2 == 0) {
            nextPageCount = nextPageCount - 1;
        }

        // 開始ページ番号
        int startPageNo = 0;
        // 終了ページ番号
        int totalPages = 0;

        // 最終ページ番号が、ページ表示件数以下の場合
        if (lastPageNo <= pageLinkCount) {
            startPageNo = firstPageNo;
            totalPages = lastPageNo;
            // 最終ページ番号が、ページ表示件数より多い場合
        } else {
            if (prevPageCount <= firstPageNo) {
                startPageNo = firstPageNo;
                totalPages = pageLinkCount;
            } else {
                if (nextPageCount > lastPageNo) {
                    startPageNo = lastPageNo - pageLinkCount + 1;
                    totalPages = lastPageNo;
                } else {
                    startPageNo = prevPageCount;
                    totalPages = nextPageCount;
                }
            }
        }

        int startEndPageNumberArray[] = {startPageNo, totalPages};
        return startEndPageNumberArray;
    }

    // ----------------------------
    // Getter系メソッド
    // ※Lombok自動生成以外のメソッド
    // ----------------------------

    /**
     * 集計件数を取得
     * @return 集計件数
     */
    public int getCount() {
        return (int) selectOptions.getCount();
    }

    /**
     * ソートタイプを取得
     * ※ページャHTMLから、${pageInfo.stype}で該当項目を取得できるようにするための対処
     *   orderFieldに設定する文字列はDB項目名であるため、文字数が長い
     *   ⇒ソート用のGetパラメータからは省略文字列を使いたいので、このstypeを追加する
     * @return ソートタイプ（ソート条件）
     */
    public String getStype() {
        // stypeMapをループ
        if (stypeMap != null) {
            for (String stype : stypeMap.keySet()) {
                String orderField = stypeMap.get(stype);

                // SQLで使われたorderFieldと一致するか判定
                if (StringUtils.equals(orderField, this.orderField)) {
                    return stype;
                }
            }
        }
        // Mapに無ければnull返却
        return null;
    }

    /**
     * 昇順降順フラグを取得
     * ※ページャHTMLから、${pageInfo.asc}で該当項目を取得できるようにするための対処
     * @return
     */
    public boolean isAsc() {
        return this.orderAsc;
    }

    /**
     * サムネイル表示か判定
     * @return true..サムネイル表示
     */
    public boolean isViewTypeThumbnail() {
        return VIEW_TYPE_THUMBNAIL_KEY.equals(this.vtype);
    }

    /**
     * リスト表示か判定
     * @return true..リスト表示
     */
    public boolean isViewTypeList() {
        return VIEW_TYPE_LIST_KEY.equals(this.vtype);
    }

    /**
     * 並び順タイプが標準順か判定
     * @return true..標準順
     */
    public boolean isSortTypeNormal() {
        return SORT_TYPE_NORMAL_KEY.equals(getStype());
    }

    /**
     * 並び順タイプが新着順か判定
     * @return true..新着順
     */
    public boolean isSortTypeNew() {
        return SORT_TYPE_REGISTDATE_KEY.equals(getStype());
    }

    /**
     * 並び順タイプが価格順か判定
     * @return true..価格順
     */
    public boolean isSortTypePrice() {
        return SORT_TYPE_GOODSPRICE_KEY.equals(getStype());
    }

    /**
     * 並び順タイプが売れ筋順か判定
     * @return true..売れ筋順
     */
    public boolean isSortTypeSalableness() {
        return SORT_TYPE_SALECOUNT_KEY.equals(getStype());
    }

    // ----------------------------
    // Setter系メソッド
    // ----------------------------

    /**
     * ページ番号の設定
     * @param page ページ番号
     */
    public void setPage(String page) {
        if (page != null) {
            this.page = Integer.parseInt(page);
        }
    }

    /**
     * ページ番号の設定
     * @param page ページ番号
     */
    public void setPage(int page) {
        this.page = page;
    }

    /**
     * 最大表示件数の設定
     * @param limit 最大表示件数
     */
    public void setLimit(int limit) {
        this.limit = limit;
    }

    /**
     * 並替条件マップの設定
     * @param stypeMap 並替条件マップ
     */
    public void setStypeMap(Map<String, String> stypeMap) {
        this.stypeMap = stypeMap;
    }

    /**
     * 並替条件の設定
     * @param field フィールド
     * @param asc 昇順フラグ
     */
    public void setOrder(String field, boolean asc) {
        this.orderField = field;
        this.orderAsc = asc;
    }

    /**
     * 並替項目の設定
     * @param orderField 並替項目
     */
    public void setOrderField(String orderField) {
        this.orderField = orderField;
    }

    /**
     * 並替昇順フラグの設定
     * @param orderAsc 並替昇順フラグ
     */
    public void setOrderAsc(boolean orderAsc) {
        this.orderAsc = orderAsc;
    }

    /**
     * 画像表示形式の設定
     * @param vtype 画像表示形式
     */
    public void setVtype(String vtype) {
        this.vtype = vtype;
    }

    /**
     * スキップカウントフラグの設定
     * @param skipCountFlg スキップカウントフラグ
     */
    public void setSkipCountFlg(boolean skipCountFlg) {
        this.skipCountFlg = skipCountFlg;
    }
}
