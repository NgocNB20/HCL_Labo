/*
 * Project Name : HIT-MALL4
 *
 * Copyright (C) 2021 i-TEC HANKYU HANSHIN INC. All Rights Reserved.
 *
 */

package jp.co.itechh.quad.front.pc.web.front.member.wishlist;

import jp.co.itechh.quad.front.annotation.exception.HEHandler;
import jp.co.itechh.quad.front.base.exception.AppLevelListException;
import jp.co.itechh.quad.front.base.util.common.CollectionUtil;
import jp.co.itechh.quad.front.base.utility.ApplicationContextUtility;
import jp.co.itechh.quad.front.web.AbstractController;
import jp.co.itechh.quad.front.web.PageInfoModule;
import jp.co.itechh.quad.wishlist.presentation.api.WishlistApi;
import jp.co.itechh.quad.wishlist.presentation.api.param.PageInfoRequest;
import jp.co.itechh.quad.wishlist.presentation.api.param.WishlistListResponse;
import jp.co.itechh.quad.wishlist.presentation.api.param.WishlistResponse;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.util.ObjectUtils;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.client.HttpClientErrorException;
import org.springframework.web.client.HttpServerErrorException;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * 会員お気に入り Controller<br/>
 *
 * @author PHAM QUANG DIEU (VJP)
 * @version $Revision: 1.0 $
 *
 */
@RequestMapping("/member/wishlist")
@Controller
public class MemberWishlistController extends AbstractController {

    /** ロガー */
    private static final Logger LOGGER = LoggerFactory.getLogger(MemberWishlistController.class);

    /** お気に入り一覧：１ページ当たりのデフォルトページ番号 */
    public static final String DEFAULT_MYLIST_PNUM = "1";

    /** お気に入り一覧：１ページ当たりのデフォルト最大表示件数 */
    public static final String DEFAULT_MYLIST_LIMIT = "10";

    /** 削除モード */
    public static final String DELETE_MODE = "d";

    /** 会員お気に入り Helper */
    public MemberWishlistHelper memberWishlistHelper;

    /** お気に入りAPI */
    public WishlistApi wishlistApi;

    /** 表示形式：サムネイル表示 キー */
    public static final String VIEW_TYPE_THUMBNAIL_KEY = "thumbs";

    /** 会員お気に入り : ソート項目 */
    private static final String DEFAULT_MEMBERWISHLISTSEARCH_ORDER_FIELD = "updateTime";

    /**
     * コンストラクタ
     *
     * @param memberWishlistHelper
     * @param wishlistApi
     */
    @Autowired
    public MemberWishlistController(MemberWishlistHelper memberWishlistHelper, WishlistApi wishlistApi) {
        this.memberWishlistHelper = memberWishlistHelper;
        this.wishlistApi = wishlistApi;
    }

    /**
     * お気に入り画面：初期処理
     *
     * @param memberWishlistModel 会員お気に入りModel
     * @param pnum ページ番号(デフォルト1）
     * @param limit 最大表示件数（デフォルト10）
     * @param model Model
     * @return お気に入り画面
     */
    @GetMapping(value = "/")
    @HEHandler(exception = AppLevelListException.class, returnView = "redirect:/error")
    protected String doLoadIndex(MemberWishlistModel memberWishlistModel,
                                 BindingResult error,
                                 RedirectAttributes redirectAttributes,
                                 @RequestParam(required = false, defaultValue = DEFAULT_MYLIST_PNUM) String pnum,
                                 @RequestParam(required = false, defaultValue = DEFAULT_MYLIST_LIMIT) int limit,
                                 Model model) {

        // 商品詳細orカートでゲストがお気に入り追加押した場合
        if (model.containsAttribute("gcd")) {
            Object gcd = model.getAttribute("gcd");
            if (gcd != null) {
                memberWishlistModel.setGcd(gcd.toString());
            }
        }

        try {
            if (memberWishlistModel.getMd() == null) {
                // 追加モード
                if (memberWishlistModel.getGcd() != null) {
                    wishlistApi.regist(memberWishlistModel.getGcd());
                }
            } else {
                // 削除モード
                if ((memberWishlistModel.getMd().equals(DELETE_MODE) && memberWishlistModel.getGcd() != null)) {
                    wishlistApi.delete(memberWishlistModel.getGcd());
                    memberWishlistModel.setMd(null);
                }
            }
            // クライアント例外の場合
        } catch (HttpClientErrorException ce) {
            LOGGER.error("例外処理が発生しました", ce);
            // アイテム名調整マップ
            Map<String, String> itemNameAdjust = new HashMap<>();
            handleClientErrorWithRedirectAttributes(
                            ce.getResponseBodyAsString(), error, itemNameAdjust, redirectAttributes);
            if (hasErrorMessage()) {
                return "redirect:/member/wishlist/";
            }

            // サーバ例外の場合
        } catch (HttpServerErrorException se) {
            LOGGER.error("例外処理が発生しました", se);
            handleServerError(se.getResponseBodyAsString());
        } finally {

            // お気に入り一覧の検索
            searchWishlistList(memberWishlistModel, error, pnum, limit, model);
        }

        // エラーが発生した場合は、エラー画面へ遷移する
        if (error.hasErrors()) {
            return "redirect:/error";
        }

        return "member/wishlist/index";
    }

    /**
     * お気に入り一覧の検索
     * @param memberWishlistModel 会員お気に入り Model
     * @param pnum ページ番号(デフォルト1）
     * @param limit 最大表示件数（デフォルト10）
     * @param model Model
     */
    protected void searchWishlistList(MemberWishlistModel memberWishlistModel,
                                      BindingResult error,
                                      String pnum,
                                      int limit,
                                      Model model) {
        // PageInfoモジュール取得
        PageInfoModule pageInfoModule = ApplicationContextUtility.getBean(PageInfoModule.class);

        // ページングセットアップ
        PageInfoRequest pageInfoRequest = new PageInfoRequest();
        pageInfoModule.setupPageRequest(
                        pageInfoRequest, Integer.valueOf(pnum), limit, DEFAULT_MEMBERWISHLISTSEARCH_ORDER_FIELD, false);

        WishlistListResponse wishlistListResponse = null;
        try {
            // 検索実行
            wishlistListResponse = wishlistApi.get(pageInfoRequest);

        } catch (HttpClientErrorException | HttpServerErrorException e) {
            LOGGER.error("例外処理が発生しました", e);
            // アイテム名調整マップ
            Map<String, String> itemNameAdjust = new HashMap<>();
            handleError(e.getStatusCode(), e.getResponseBodyAsString(), error, itemNameAdjust);
            return;
        }

        List<WishlistResponse> stockWishlistDtoList = new ArrayList<>();
        if (!ObjectUtils.isEmpty(wishlistListResponse) && CollectionUtil.isNotEmpty(
                        wishlistListResponse.getWishListList())) {
            stockWishlistDtoList = wishlistListResponse.getWishListList();
        }

        if (!ObjectUtils.isEmpty(wishlistListResponse)) {
            // 検索前ページャーセットアップ
            pageInfoModule.setupPageInfo(memberWishlistModel, wishlistListResponse.getPageInfo().getPage(),
                                         wishlistListResponse.getPageInfo().getLimit(),
                                         wishlistListResponse.getPageInfo().getNextPage(),
                                         wishlistListResponse.getPageInfo().getPrevPage(),
                                         wishlistListResponse.getPageInfo().getTotal(),
                                         wishlistListResponse.getPageInfo().getTotalPages(), null, null, null, true,
                                         VIEW_TYPE_THUMBNAIL_KEY
                                        );
        }

        // 検索結果を画面に設定
        memberWishlistHelper.toPageForLoad(stockWishlistDtoList, memberWishlistModel);
        // ぺージャーセットアップ
        pageInfoModule.setupViewPager(memberWishlistModel.getPageInfo(), model);
    }

}