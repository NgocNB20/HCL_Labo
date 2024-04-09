/*
 * Project Name : HIT-MALL4
 *
 * Copyright (C) 2021 i-TEC HANKYU HANSHIN INC. All Rights Reserved.
 *
 */

package jp.co.itechh.quad.admin.web;

import lombok.Getter;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Component;

import java.io.Serializable;

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

    private int offset;

    /**
     * offsetを計算し<br/>
     */
    public void setupOffset(Integer page, Integer limit) {
        if (page == 1) {
            this.offset = 0;
        } else {
            this.offset = limit * (page - 1);
        }
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

}