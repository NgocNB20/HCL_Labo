/*
 * Copyright (C) 2022 i-TEC HANKYU HANSHIN INC. All Rights Reserved.
 */

package jp.co.itechh.quad.admin.pc.web.admin.shop.novelty;

import jp.co.itechh.quad.admin.annotation.exception.HEHandler;
import jp.co.itechh.quad.admin.base.exception.AppLevelListException;
import jp.co.itechh.quad.admin.base.utility.ApplicationContextUtility;
import jp.co.itechh.quad.admin.base.utility.ConversionUtility;
import jp.co.itechh.quad.admin.constant.type.HTypeNoveltyPresentState;
import jp.co.itechh.quad.admin.util.common.EnumTypeUtil;
import jp.co.itechh.quad.admin.web.AbstractController;
import jp.co.itechh.quad.admin.web.PageInfoModule;
import jp.co.itechh.quad.novelty.presentation.api.LogisticNoveltyApi;
import jp.co.itechh.quad.novelty.presentation.api.param.NoveltyPresentConditionListGetRequest;
import jp.co.itechh.quad.novelty.presentation.api.param.NoveltyPresentConditionListResponse;
import jp.co.itechh.quad.novelty.presentation.api.param.PageInfoRequest;
import jp.co.itechh.quad.novelty.presentation.api.param.PageInfoResponse;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.SessionAttributes;
import org.springframework.web.client.HttpClientErrorException;
import org.springframework.web.client.HttpServerErrorException;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import java.util.HashMap;
import java.util.Map;
import java.util.Optional;

/**
 * ノベルティプレゼント検索画面Controller
 *
 * @author Pham Quang Dieu (VTI Japan Co., Ltd.)
 */
@RequestMapping("/novelty")
@Controller
@SessionAttributes(value = "noveltyModel")
@PreAuthorize("hasAnyAuthority('SHOP:4')")
public class NoveltyController extends AbstractController {

    /** ロガー */
    private static final Logger LOGGER = LoggerFactory.getLogger(NoveltyController.class);

    /**
     * デフォルトページ番号
     */
    private static final String DEFAULT_PNUM = "1";

    /**
     * デフォルト：ソート項目
     */
    private static final String DEFAULT_ORDER_FIELD = "noveltyPresentStartTime";

    /**
     * デフォルト：ソート条件(昇順/降順)
     */
    private static final boolean DEFAULT_ORDER_ASC = false;

    /**
     * 表示モード:「list」の場合 再検索
     */
    public static final String FLASH_MD = "md";

    /**
     * 表示モード(md):list 検索画面の再検索実行
     */
    public static final String MODE_LIST = "list";

    /**
     * ニュース検索ページDxo
     */
    private final NoveltyHelper noveltyHelper;

    /**
     * ノベルティAPI
     */
    private final LogisticNoveltyApi logisticNoveltyApi;

    /**
     * 変換ユーティリティクラス
     */
    private final ConversionUtility conversionUtility;

    @Autowired
    public NoveltyController(NoveltyHelper noveltyHelper,
                             ConversionUtility conversionUtility,
                             LogisticNoveltyApi logisticNoveltyApi) {
        this.noveltyHelper = noveltyHelper;
        this.conversionUtility = conversionUtility;
        this.logisticNoveltyApi = logisticNoveltyApi;
    }

    /**
     * 初期表示
     *
     * @return 自画面
     */
    @GetMapping("/")
    @HEHandler(exception = AppLevelListException.class, returnView = "novelty/index")
    public String doLoadIndex(@RequestParam(required = false) Optional<String> md,
                              NoveltyModel noveltyModel,
                              BindingResult error,
                              Model model) {

        if (md.isPresent() || model.containsAttribute(FLASH_MD)) {
            // 再検索を実行
            search(noveltyModel, error, model);
        } else {
            clearModel(NoveltyModel.class, noveltyModel, model);
        }

        // プルダウンアイテム情報を取得
        initComponentValue(noveltyModel);

        return "novelty/index";
    }

    /**
     * ノベルティ検索
     *
     * @return 自画面
     */
    @PostMapping(value = "/", params = "doNoveltySearch")
    @HEHandler(exception = AppLevelListException.class, returnView = "novelty/index")
    public String doNoveltySearch(@Validated NoveltyModel noveltyModel,
                                  BindingResult error,
                                  RedirectAttributes redirectAttributes,
                                  Model model) {
        if (error.hasErrors()) {
            return "novelty/index";
        }

        // ページング関連項目初期化（limitは画面プルダウンで指定されてくる）
        noveltyModel.setPageNumber(DEFAULT_PNUM);
        noveltyModel.setOrderField(DEFAULT_ORDER_FIELD);
        noveltyModel.setOrderAsc(DEFAULT_ORDER_ASC);

        // 検索
        this.search(noveltyModel, error, model);

        return "novelty/index";
    }

    /**
     * 表示順変更
     *
     * @return 自画面
     */
    @PostMapping(value = "/", params = "doDisplayChange")
    @HEHandler(exception = AppLevelListException.class, returnView = "novelty/index")
    public String doDisplayChange(@Validated NoveltyModel noveltyModel, BindingResult error, Model model) {

        if (error.hasErrors()) {
            return "novelty/index";
        }

        // 検索
        search(noveltyModel, error, model);
        return "novelty/index";
    }

    /**
     * 検索
     *
     * @param noveltyModel
     * @param error
     * @param model
     */
    protected void search(NoveltyModel noveltyModel, BindingResult error, Model model) {
        try {
            NoveltyPresentConditionListGetRequest noveltyPresentConditionListGetRequest =
                            noveltyHelper.toNoveltyPresentConditionListGetRequest(noveltyModel);

            // ページング検索セットアップ
            PageInfoModule pageInfoHelper = ApplicationContextUtility.getBean(PageInfoModule.class);

            // リクエスト用のページャーを生成
            PageInfoRequest pageInfoRequest = new PageInfoRequest();
            // リクエスト用のページャー項目をセット
            pageInfoHelper.setupPageRequest(pageInfoRequest, conversionUtility.toInteger(noveltyModel.getPageNumber()),
                                            noveltyModel.getLimit(), noveltyModel.getOrderField(),
                                            noveltyModel.isOrderAsc()
                                           );

            // 検索
            NoveltyPresentConditionListResponse noveltyPresentConditionListResponse =
                            logisticNoveltyApi.get(noveltyPresentConditionListGetRequest, pageInfoRequest);

            // ショップセッションに保持
            noveltyHelper.toPageForSearch(noveltyPresentConditionListResponse, noveltyModel);

            // ページャーにレスポンス情報をセット
            PageInfoResponse pageInfo = noveltyPresentConditionListResponse.getPageInfo();
            pageInfoHelper.setupPageInfo(noveltyModel, pageInfo.getPage(), pageInfo.getLimit(), pageInfo.getNextPage(),
                                         pageInfo.getPrevPage(), pageInfo.getTotal(), pageInfo.getTotalPages()
                                        );
            // ページャーをセット
            pageInfoHelper.setupViewPager(noveltyModel.getPageInfo(), noveltyModel);
        } catch (HttpServerErrorException | HttpClientErrorException e) {
            LOGGER.error("例外処理が発生しました", e);
            Map<String, String> itemNameAdjust = new HashMap<>();
            itemNameAdjust.put("noveltyPresentName", "searchNoveltyPresentName");
            handleError(e.getStatusCode(), e.getResponseBodyAsString(), error, itemNameAdjust);
        }
    }

    /**
     * ノベルティプレゼント登録更新画面へ遷移(登録)
     *
     * @return ノベルティプレゼント登録更新画面
     */
    @PreAuthorize("hasAnyAuthority('SHOP:8')")
    @PostMapping(value = "/", params = "doRegist")
    public String doRegist() {
        return "redirect:/novelty/registupdate/";
    }

    /**
     * コンポーネント値初期化
     *
     * @param noveltyModel
     */
    private void initComponentValue(NoveltyModel noveltyModel) {
        noveltyModel.setNoveltyPresentStateItems(EnumTypeUtil.getEnumMap(HTypeNoveltyPresentState.class));
    }
}