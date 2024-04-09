/*
 * Copyright (C) 2022 i-TEC HANKYU HANSHIN INC. All Rights Reserved.
 */

package jp.co.itechh.quad.admin.web;

import jp.co.itechh.quad.admin.base.utility.ApplicationContextUtility;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Component;
import org.springframework.ui.Model;

import java.lang.reflect.Field;

/**
 * PageInfoモジュール<br/>
 * PageInfoの設定を行うの便利メソッドを提供する
 *
 * @author kimura
 */
@Component
public class PageInfoModule {

    /**
     * ロガー
     */
    private static final Logger LOGGER = LoggerFactory.getLogger(PageInfoModule.class);

    /**
     * ページングセットアップ【共通】<br/>
     *
     * @param model      検索画面のModel
     * @param page       ページ番号
     * @param limit      最大表示件数
     * @param nextPage   次のページ番号
     * @param prevPage   前のページ番号
     * @param total      全件数
     * @param totalPages ページ数
     * @return ページングセットアップ後の検索画面のModel
     */
    public <T extends AbstractModel> T setupPageInfo(T model,
                                                     Integer page,
                                                     Integer limit,
                                                     Integer nextPage,
                                                     Integer prevPage,
                                                     Integer total,
                                                     Integer totalPages) {

        // ----------------------------
        // PageInfo生成
        // ----------------------------
        PageInfo pageInfo = ApplicationContextUtility.getBean(PageInfo.class);

        // ----------------------------
        // 各種値をセット
        // -----------------------------
        // 現在のページ番号
        if (page != null) {
            pageInfo.setPnum(page);
        }
        // 最大表示件数
        if (limit != null) {
            pageInfo.setLimit(limit);
        }
        // 次のページ番号
        if (nextPage != null) {
            pageInfo.setNextPage(nextPage);
        }
        // 前のページ番号
        if (prevPage != null) {
            pageInfo.setPrevPage(limit);
        }
        // 全件数
        if (total != null) {
            pageInfo.setTotal(total);
        }
        // ページ数
        if (totalPages != null) {
            pageInfo.setTotalPages(totalPages);
        }

        pageInfo.setupOffset(page, limit);
        // ----------------------------
        // Modelにセット
        // ----------------------------
        model.setPageInfo(pageInfo);

        // ----------------------------
        // Modelを返却
        // ----------------------------
        return model;
    }

    /**
     * ページャーセットアップ<br/>
     * - PageInfo内部をページャーHTMLで利用できるようセットアップする
     * - ModelにPageInfoをセットする
     *
     * @param pageInfo Page情報
     * @param model    Model
     */
    public void setupViewPager(PageInfo pageInfo, Model model) {
        // Pagerセットアップ
        pageInfo.setupViewPager();
        // ModelにpageInfoをセット
        model.addAttribute(PageInfo.ATTRIBUTE_NAME_KEY, pageInfo);
    }

    /**
     * ページャーセットアップ（AbstractModelに保持する場合）<br/>
     * - PageInfo内部をページャーHTMLで利用できるようセットアップする
     * - AbstractModelにPageInfoをセットする
     *
     * @param pageInfo Page情報
     * @param model    AbstractModel
     */
    public void setupViewPager(PageInfo pageInfo, AbstractModel model) {
        // Pagerセットアップ
        pageInfo.setupViewPager();
        // AbstractModelにpageInfoをセット
        model.setPageInfo(pageInfo);
    }

    /**
     * ページリクエストを設定
     *
     * @param pageRequest ページ情報リクエスト
     * @param page        ページリクエストを設定
     * @param limit       ページサイズ
     * @param orderBy     ソート項目
     * @param sort        ソート条件（true：昇順、false：降順
     */
    public void setupPageRequest(Object pageRequest, Integer page, Integer limit, String orderBy, Boolean sort) {
        try {
            Field[] fields = pageRequest.getClass().getDeclaredFields();
            for (Field field : fields) {
                switch (field.getName()) {
                    case "page":
                        field.setAccessible(true);
                        field.set(pageRequest, page);
                        break;
                    case "limit":
                        field.setAccessible(true);
                        field.set(pageRequest, limit);
                        break;
                    case "orderBy":
                        field.setAccessible(true);
                        field.set(pageRequest, orderBy);
                        break;
                    case "sort":
                        field.setAccessible(true);
                        field.set(pageRequest, sort);
                        break;
                    default:
                        break;
                }
            }
        } catch (Exception e) {
            LOGGER.error(e.getMessage());
        }
    }

    /**
     * ページリクエストを設定
     *
     * @param pageRequest   ページ情報リクエスト
     * @param page          ページリクエストを設定
     * @param limit         ページサイズ
     * @param orderBy       ソート項目
     * @param sort          ソート条件（true：昇順、false：降順
     * @param default_limit デフォルトページサイズ(limitが不適切な値の場合採用)
     */
    public void setupPageRequest(Object pageRequest,
                                 Integer page,
                                 Integer limit,
                                 String orderBy,
                                 Boolean sort,
                                 Integer default_limit) {
        if (limit <= 0) {
            limit = default_limit;
        }
        setupPageRequest(pageRequest, page, limit, orderBy, sort);
    }
}