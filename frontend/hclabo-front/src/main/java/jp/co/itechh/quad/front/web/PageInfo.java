/*
 * Project Name : HIT-MALL4
 *
 * Copyright (C) 2021 i-TEC HANKYU HANSHIN INC. All Rights Reserved.
 *
 */

package jp.co.itechh.quad.front.web;

import lombok.Getter;
import org.apache.commons.lang3.StringUtils;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Component;

import java.io.Serializable;
import java.util.Map;

/**
 * ページ情報クラス<br/>
 *
 * @author kimura
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
    public static final int DEFAULT_PNUM = 1;
    /** 属性名 キー */
    public static final String ATTRIBUTE_NAME_KEY = "pageInfo";

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

    // ----------------------------
    // 内部処理で設定されるフィールド
    // ----------------------------
    /** 前ページを表示するか ※pager.htmlで使用 */
    private boolean isDisplayPrev;
    /** 次ページを表示するか ※pager.htmlで使用 */
    private boolean isDisplayNext;
    /** 開始ページ番号 ※pager.htmlで使用 */
    private Integer startPageNo;
    /** 終了ページ番号 ※pager.htmlで使用 */
    private Integer endPageNo;

    // ----------------------------
    // レスポンスから設定されるフィールド
    // ----------------------------
    /** 現在のページ番号 ※pager.htmlで使用 */
    private Integer pnum;
    /** 表示最大件数 */
    private Integer limit;
    /** 次のページ番号 **/
    private Integer nextPage;
    /** 前のページ番号 **/
    private Integer prevPage;
    /** 全件数 ※pager.htmlで使用 **/
    private Integer total;
    /** ページ数 ※pager.htmlで使用 */
    private Integer totalPages;

    /** 並替項目 */
    private String orderField;
    /** 並替昇順フラグ */
    private boolean orderAsc;
    /**
     * サムネイルかリストか
     * @see PageInfo#
     * @see PageInfo#VIEW_TYPE_LIST_KEY
     */
    private String vtype;

    /**
     * 並替項目マップ<br/>
     * <br/>
     * 　KEY:stype<br/>
     * 　VALUE:orderField<br/>
     * のマップ<br/>
     * <br/>
     * ※並替項目について、フロント商品検索などでGETパラメータ指定されることがあるが、
     * 　できるだけ短い文字列でパラメータ指定したいという要件がある<br/>
     * 　（逆に言うと、orderFieldで指定される文字列はSQLを意識しているため、文字数が少々長い。。）<br/>
     * これを解消するために、本Mapをもたせ<br/>
     * SQL実行前、画面ページャ取得前のタイミングでKEY⇔VALUEの変換を行う
     */
    private Map<String, String> stypeMap;

    /**
     * ソートタイプを取得<br/>
     * ※ページャHTMLから、${pageInfo.stype}で該当項目を取得できるようにするための対処<br/>
     *   orderFieldに設定する文字列はDB項目名であるため、文字数が長い<br/>
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
     * ページャ表示用にPageInfoのセットアップを行う<br/>
     */
    public void setupViewPager() {

        this.isDisplayPrev = false;
        this.isDisplayNext = false;

        // 集計件数を取得
        // ページ番号が1より大きく、前ページ番号が存在すれば
        if (this.pnum > 1 && this.prevPage != null) {
            // 前ページを表示
            this.isDisplayPrev = true;
        }

        // ページ数が1より大きく、ページ番号と一致しない場合
        if (this.totalPages > 1 && !this.pnum.equals(this.totalPages)) {
            // 次ページを表示
            this.isDisplayNext = true;
        }

        // 開始、終了ページの番号を取得する
        int[] startEndPageNumberArray =
                        getStartEndPageNumberArray(pnum, this.total, this.limit, PageInfo.DEFAULT_PAGE_LINK_COUNT,
                                                   totalPages
                                                  );
        this.startPageNo = startEndPageNumberArray[0];
        this.endPageNo = startEndPageNumberArray[1];
    }

    /**
     * 開始、終了ページの番号を返すメソッド
     *
     * @param pnum          現在のページ番号
     * @param total         集計件数
     * @param limit         最大表示件数
     * @param pageLinkCount ページ番号表示数
     * @param lastPageNo    最終ページ番号
     * @return 開始、終了ページ番号の配列
     */
    private int[] getStartEndPageNumberArray(int pnum, int total, int limit, int pageLinkCount, int lastPageNo) {

        // 初期ページ番号
        int firstPageNo = 1;
        // ページ番号表示数の半分
        int pageLinkCountHalf = pageLinkCount / 2;
        // 前ページ表示数
        int prevPageCount = pnum - pageLinkCountHalf;
        // 次ページ表示数
        int nextPageCount = pnum + pageLinkCountHalf;
        // 偶数の場合 次ページ表示数=次ページ表示数-1 をする
        if (pageLinkCount % 2 == 0) {
            nextPageCount = nextPageCount - 1;
        }

        // 開始ページ番号
        int startPageNo = 0;
        // 終了ページ番号
        int endPageNo = 0;

        // 最終ページ番号が、ページ表示件数以下の場合
        if (lastPageNo <= pageLinkCount) {
            startPageNo = firstPageNo;
            endPageNo = lastPageNo;
            // 最終ページ番号が、ページ表示件数より多い場合
        } else {
            if (prevPageCount <= firstPageNo) {
                startPageNo = firstPageNo;
                endPageNo = pageLinkCount;
            } else {
                if (nextPageCount > lastPageNo) {
                    startPageNo = lastPageNo - pageLinkCount + 1;
                    endPageNo = lastPageNo;
                } else {
                    startPageNo = prevPageCount;
                    endPageNo = nextPageCount;
                }
            }
        }

        int startEndPageNumberArray[] = {startPageNo, endPageNo};
        return startEndPageNumberArray;
    }

    // ----------------------------
    // Getter系メソッド
    // ※Lombok自動生成以外のメソッド
    // ----------------------------

    /** 全件数を取得 */
    public int getCount() {
        return this.total;
    }

    // ----------------------------
    // Setter系メソッド
    // ----------------------------

    /**
     * 現在のページ番号の設定<br/>
     *
     * @param pnum 現在のページ番号
     */
    public void setPnum(String pnum) {
        if (pnum != null) {
            this.pnum = Integer.parseInt(pnum);
        }
    }

    /**
     * 現在のページ番号の設定<br/>
     *
     * @param pnum 現在のページ番号
     */
    public void setPnum(Integer pnum) {
        this.pnum = pnum;
    }

    /**
     * 最大表示件数の設定<br/>
     *
     * @param limit 最大表示件数
     */
    public void setLimit(Integer limit) {
        this.limit = limit;
    }

    /**
     * 次のページ番号<br/>
     *
     * @param nextPage 次のページ番号
     */
    public void setNextPage(Integer nextPage) {
        this.nextPage = nextPage;
    }

    /**
     * 前のページ番号<br/>
     *
     * @param prevPage 前のページ番号
     */
    public void setPrevPage(Integer prevPage) {
        this.prevPage = prevPage;
    }

    /**
     * 全件数<br/>
     *
     * @param total 全件数
     */
    public void setTotal(Integer total) {
        this.total = total;
    }

    /**
     * ページ数<br/>
     *
     * @param totalPages ページ数
     */
    public void setTotalPages(Integer totalPages) {
        this.totalPages = totalPages;
    }

    /**
     * 並替条件マップの設定<br/>
     * @param stypeMap 並替条件マップ
     */
    public void setStypeMap(Map<String, String> stypeMap) {
        this.stypeMap = stypeMap;
    }

    /**
     * 並替条件の設定<br/>
     * @param field フィールド
     * @param asc 昇順フラグ
     */
    public void setOrder(String field, boolean asc) {
        this.orderField = field;
        this.orderAsc = asc;
    }

    /**
     * 並替項目の設定<br/>
     * @param orderField 並替項目
     */
    public void setOrderField(String orderField) {
        this.orderField = orderField;
    }

    /**
     * 並替昇順フラグの設定<br/>
     * @param orderAsc 並替昇順フラグ
     */
    public void setOrderAsc(boolean orderAsc) {
        this.orderAsc = orderAsc;
    }

    /**
     * 画像表示形式の設定<br/>
     * @param vtype 画像表示形式
     */
    public void setVtype(String vtype) {
        this.vtype = vtype;
    }

    /**
     * 昇順降順フラグを取得<br/>
     * ※ページャHTMLから、${pageInfo.asc}で該当項目を取得できるようにするための対処
     * @return
     */
    public boolean isAsc() {
        return this.orderAsc;
    }

    /**
     * サムネイル表示か判定<br/>
     * @return true..サムネイル表示
     */
    public boolean isViewTypeThumbnail() {
        return VIEW_TYPE_THUMBNAIL_KEY.equals(this.vtype);
    }

    /**
     * リスト表示か判定<br/>
     * @return true..リスト表示
     */
    public boolean isViewTypeList() {
        return VIEW_TYPE_LIST_KEY.equals(this.vtype);
    }

    /**
     * 並び順タイプが標準順か判定<br/>
     * @return true..標準順
     */
    public boolean isSortTypeNormal() {
        return SORT_TYPE_NORMAL_KEY.equals(getStype());
    }

    /**
     * 並び順タイプが新着順か判定<br/>
     * @return true..新着順
     */
    public boolean isSortTypeNew() {
        return SORT_TYPE_REGISTDATE_KEY.equals(getStype());
    }

    /**
     * 並び順タイプが価格順か判定<br/>
     * @return true..価格順
     */
    public boolean isSortTypePrice() {
        return SORT_TYPE_GOODSPRICE_KEY.equals(getStype());
    }

    /**
     * 並び順タイプが売れ筋順か判定<br/>
     * @return true..売れ筋順
     */
    public boolean isSortTypeSalableness() {
        return SORT_TYPE_SALECOUNT_KEY.equals(getStype());
    }

}