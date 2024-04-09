/*
 * Project Name : HIT-MALL4
 *
 * Copyright (C) 2021 i-TEC HANKYU HANSHIN INC. All Rights Reserved.
 *
 */

package jp.co.itechh.quad.core.web;

import jp.co.itechh.quad.core.base.dto.AbstractConditionDto;
import jp.co.itechh.quad.core.base.utility.ApplicationContextUtility;
import org.apache.commons.beanutils.BeanUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Component;

import java.lang.reflect.Field;
import java.lang.reflect.InvocationTargetException;
import java.util.Map;

/**
 * PageInfoモジュール
 * PageInfoの設定を行うの便利メソッドを提供する
 *
 * @author yt23807
 * @version $Revision: 1.0 $
 *
 */
@Component
public class PageInfoModule {

    /** ロガー */
    private static final Logger LOGGER = LoggerFactory.getLogger(PageInfoModule.class);

    // -----------------------------------------------------------
    //  ページング検索セットアップ用メソッド（SQL検索処理前に呼び出す）
    // -----------------------------------------------------------

    /**
     * ページングセットアップ
     * @param conditionDto 検索条件Dto
     * @param page ページ番号
     * @return ページングセットアップ後の検索条件Dto
     */
    public <T extends AbstractConditionDto> T setupPageInfo(T conditionDto, Integer page) {
        // デフォルト値設定・・・LIMIT=10／ソート条件Map=指定無し(null)／ソート条件（省略）=指定無し(null)／ソート条件=指定無し(null)／画像表示条件=指定無し(null)／件数スキップフラグ=スキップしない
        return setupPageInfoCommon(conditionDto, page, PageInfo.DEFAULT_LIMIT, null, null, null, true, null, false);
    }

    /**
     * ページングセットアップ
     * @param conditionDto 検索条件Dto
     * @param page ページ番号
     * @param limit 画面最大表示件数
     * @return ページングセットアップ後の検索条件Dto
     */
    public <T extends AbstractConditionDto> T setupPageInfo(T conditionDto, Integer page, int limit) {
        // デフォルト値設定・・・ソート条件Map=指定無し(null)／ソート条件（省略）=指定無し(null)／ソート条件=指定無し(null)／画像表示条件=指定無し(null)／件数スキップフラグ=スキップしない
        return setupPageInfoCommon(conditionDto, page, limit, null, null, null, true, null, false);
    }

    /**
     * ページングセットアップ
     * @param conditionDto 検索条件Dto
     * @param page ページ番号
     * @param limit 画面最大表示件数
     * @param orderField ソート項目
     * @param orderAsc 昇順/降順フラグ
     * @return ページングセットアップ後の検索条件Dto
     */
    public <T extends AbstractConditionDto> T setupPageInfo(T conditionDto,
                                                            Integer page,
                                                            int limit,
                                                            String orderField,
                                                            boolean orderAsc) {
        // デフォルト値設定・・・ソート条件Map=指定無し(null)／ソート条件（省略）=指定無し(null)／画像表示条件=指定無し(null)／件数スキップフラグ=スキップしない
        return setupPageInfoCommon(conditionDto, page, limit, null, null, orderField, orderAsc, null, false);
    }

    /**
     * ページングセットアップ
     * @param conditionDto 検索条件Dto
     * @param page ページ番号
     * @param limit 画面最大表示件数
     * @param orderField ソート項目
     * @param orderAsc 昇順/降順フラグ
     * @param vtype 画像表示条件
     * @return ページングセットアップ後の検索条件Dto
     */
    public <T extends AbstractConditionDto> T setupPageInfo(T conditionDto,
                                                            Integer page,
                                                            int limit,
                                                            String orderField,
                                                            boolean orderAsc,
                                                            String vtype) {
        // デフォルト値設定・・・ソート条件Map=指定無し(null)／ソート条件（省略）=指定無し(null)／件数スキップフラグ=スキップしない
        return setupPageInfoCommon(conditionDto, page, limit, null, null, orderField, orderAsc, vtype, false);
    }

    public <T extends AbstractConditionDto> T setupPageInfo(T conditionDto,
                                                            Integer page,
                                                            int limit,
                                                            Map<String, String> stypeMap,
                                                            String stype,
                                                            boolean orderAsc,
                                                            String vtype) {
        // デフォルト値設定・・・ソート条件=指定無し(null)／件数スキップフラグ=スキップしない
        return setupPageInfoCommon(conditionDto, page, limit, stypeMap, stype, null, orderAsc, vtype, false);
    }

    /**
     * 【総件数取得無し】ページングセットアップ
     * @param conditionDto 検索条件Dto
     * @return ページングセットアップ後の検索条件Dto
     */
    public <T extends AbstractConditionDto> T setupPageInfoForSkipCount(T conditionDto) {
        // デフォルト値設定・・・LIMIT=10／ソート条件Map=指定無し(null)／ソート条件（省略）=指定無し(null)／ソート条件=指定無し(null)／画像表示条件=指定無し(null)／件数スキップフラグ=★スキップする★
        return setupPageInfoCommon(conditionDto, null, PageInfo.DEFAULT_LIMIT, null, null, null, true, null, true);
    }

    /**
     * 【総件数取得無し】ページングセットアップ
     * @param conditionDto 検索条件Dto
     * @param limit 画面最大表示件数
     * @return ページングセットアップ後の検索条件Dto
     */
    public <T extends AbstractConditionDto> T setupPageInfoForSkipCount(T conditionDto, int limit) {
        // デフォルト値設定・・・LIMIT=10／ソート条件Map=指定無し(null)／ソート条件（省略）=指定無し(null)／ソート条件=指定無し(null)／画像表示条件=指定無し(null)／件数スキップフラグ=スキップしない
        return setupPageInfoCommon(conditionDto, null, limit, null, null, null, true, null, true);
    }

    /**
     * 【総件数取得無し】ページングセットアップ
     * @param conditionDto 検索条件Dto
     * @param limit 画面最大表示件数
     * @param orderField ソート項目
     * @param orderAsc 昇順/降順フラグ
     * @return ページングセットアップ後の検索条件Dto
     */
    public <T extends AbstractConditionDto> T setupPageInfoForSkipCount(T conditionDto,
                                                                        int limit,
                                                                        String orderField,
                                                                        boolean orderAsc) {
        // デフォルト値設定・・・ソート条件Map=指定無し(null)／ソート条件（省略）=指定無し(null)／画像表示条件=指定無し(null)／件数スキップフラグ=★スキップする★
        return setupPageInfoCommon(conditionDto, null, limit, null, null, orderField, orderAsc, null, true);
    }

    /**
     * 【総件数取得無し】ページングセットアップ
     * @param conditionDto 検索条件Dto
     * @param limit 画面最大表示件数
     * @param orderField ソート項目
     * @param orderAsc 昇順/降順フラグ
     * @param vtype 画像表示条件
     * @return ページングセットアップ後の検索条件Dto
     */
    public <T extends AbstractConditionDto> T setupPageInfoForSkipCount(T conditionDto,
                                                                        int limit,
                                                                        String orderField,
                                                                        boolean orderAsc,
                                                                        String vtype) {
        // デフォルト値設定・・・ソート条件Map=指定無し(null)／ソート条件（省略）=指定無し(null)／件数スキップフラグ=★スキップする★
        return setupPageInfoCommon(conditionDto, null, limit, null, null, orderField, orderAsc, vtype, true);
    }

    /**
     * ページングセットアップ【共通】
     *
     * ※ソート条件について補足※
     * 　・stypeMap,stype⇔orderField　は互いに排他
     * 　・stypeMap,stypeは２つでワンセット
     * 　・stypeMapはソート条件（略字）とソート条件（項目名）を対応付けがされたMapである
     * 　　＜stypeMapの具体例＞
     * <pre>
     * 　　┌─────────────┬──────────────────────┐
     * 　　│KEY(stype)   │VALUE(orderField)     │
     * 　　├─────────────┼──────────────────────┤
     * 　　├─────────────┼──────────────────────┤
     * 　　│"price"      │"goodsGroupMinPrice"  │
     * 　　├─────────────┼──────────────────────┤
     * 　　│"new"        │"whatsnewdate"        │
     * 　　├─────────────┼──────────────────────┤
     * 　　│"salableness"│"popularlityCount"    │
     * 　　└─────────────┴──────────────────────┘
     * </pre>
     * 　・処理内部でstypeMapにstypeを引き当てて、orderFieldを取得する
     * 　・stypeMap,stypeを指定して場合でも、SQLで最終的に使われるのはorderFieldである
     * @param conditionDto 検索条件Dto
     * @param page ページ番号
     * @param limit 画面最大表示件数
     * @param stypeMap ソート項目マップ（stype⇔orderFieldの対応を持つマップ）
     * @param stype ソート項目（省略文字）
     * @param orderField ソート項目
     * @param orderAsc 昇順/降順フラグ
     * @param vtype 画像表示条件
     * @param skipCountFlg 件数カウント取得スキップフラグ
     * @return ページングセットアップ後の検索条件Dto
     */
    protected <T extends AbstractConditionDto> T setupPageInfoCommon(T conditionDto,
                                                                     Integer page,
                                                                     int limit,
                                                                     Map<String, String> stypeMap,
                                                                     String stype,
                                                                     String orderField,
                                                                     boolean orderAsc,
                                                                     String vtype,
                                                                     boolean skipCountFlg) {
        // ----------------------------
        // PageInfo生成
        // ----------------------------
        PageInfo pageInfo = ApplicationContextUtility.getBean(PageInfo.class);

        // ----------------------------
        // 各種値をセット
        // -----------------------------
        // ページ番号
        if (page != null) {
            pageInfo.setPage(page);
        }
        // 最大表示件数
        if (limit > 0) {
            pageInfo.setLimit(limit);
        }

        // ソート条件Mapがある場合
        if (stypeMap != null) {
            // stype⇒orderFieldに変換
            // ※注意※stypeはこれ以降利用しない
            orderField = stypeMap.get(stype);

            // stypeMapはPageInfoに保持させる
            // ※ページャリンクで使うため（orderField⇒stypeへの変換用に）
            pageInfo.setStypeMap(stypeMap);
        }

        // ソート条件（ソート項目が渡されていれば、セット)
        if (orderField != null) {
            pageInfo.setOrder(orderField, orderAsc);
        }
        // 画像表示区分
        if (vtype != null) {
            pageInfo.setVtype(vtype);
        }
        // 件数スキップフラグ
        pageInfo.setSkipCountFlg(skipCountFlg);

        // ----------------------------
        // SelectOptionセットアップ
        // ----------------------------
        pageInfo.setupSelectOptions();

        // ----------------------------
        // ConditionDtoにセット
        // ----------------------------
        conditionDto.setPageInfo(pageInfo);

        // ----------------------------
        // ConditionDtoを返却
        // ----------------------------
        return conditionDto;
    }

    // -----------------------------------------------------------
    //  ページャーセットアップ用メソッド（SQL検索処理後、画面表示前に呼び出す）
    // -----------------------------------------------------------

    /**
     * ページャーセットアップ
     * - PageInfo内部をページャーHTMLで利用できるようセットアップする
     * - ModelにPageInfoをセットする
     * @param conditionDto 検索条件Dto
     * @param pageResponse ページ情報レスポンス
     */
    public <T extends AbstractConditionDto> void setupResponsePager(T conditionDto, Object pageResponse)
                    throws InvocationTargetException, IllegalAccessException {
        // Pagerセットアップ
        conditionDto.getPageInfo().setupViewPager();

        // Pagerセットアップ
        BeanUtils.copyProperties(pageResponse, conditionDto.getPageInfo());
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
}