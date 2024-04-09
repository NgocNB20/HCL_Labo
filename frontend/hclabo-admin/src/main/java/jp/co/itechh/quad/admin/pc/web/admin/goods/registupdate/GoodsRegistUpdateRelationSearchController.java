/*
 * Project Name : HIT-MALL4
 *
 * Copyright (C) 2008 i-TEC HANKYU HANSHIN INC. All Rights Reserved.
 *
 */

package jp.co.itechh.quad.admin.pc.web.admin.goods.registupdate;

import jp.co.itechh.quad.admin.base.util.common.CollectionUtil;
import jp.co.itechh.quad.admin.base.utility.ApplicationContextUtility;
import jp.co.itechh.quad.admin.dto.goods.goods.GoodsSearchForBackDaoConditionDto;
import jp.co.itechh.quad.admin.dto.goods.goods.GoodsSearchResultDto;
import jp.co.itechh.quad.admin.entity.goods.goodsgroup.GoodsRelationEntity;
import jp.co.itechh.quad.admin.pc.web.admin.common.ajax.MessageUtils;
import jp.co.itechh.quad.admin.pc.web.admin.common.ajax.ValidatorMessage;
import jp.co.itechh.quad.admin.pc.web.admin.common.dto.CategoryAndTagForGoodsRegistUpdateDto;
import jp.co.itechh.quad.admin.pc.web.admin.common.validation.group.SearchGroup;
import jp.co.itechh.quad.admin.pc.web.admin.goods.AbstractGoodsRegistUpdateController;
import jp.co.itechh.quad.admin.pc.web.admin.goods.AbstractGoodsRegistUpdateModel;
import jp.co.itechh.quad.admin.web.PageInfoModule;
import jp.co.itechh.quad.category.presentation.api.CategoryApi;
import jp.co.itechh.quad.category.presentation.api.param.CategoryListGetRequest;
import jp.co.itechh.quad.category.presentation.api.param.CategoryListResponse;
import jp.co.itechh.quad.product.presentation.api.ProductApi;
import jp.co.itechh.quad.product.presentation.api.param.PageInfoRequest;
import jp.co.itechh.quad.product.presentation.api.param.ProductItemListGetRequest;
import jp.co.itechh.quad.product.presentation.api.param.ProductItemListResponse;
import jp.co.itechh.quad.relation.presentation.api.RelationApi;
import org.apache.commons.lang3.ObjectUtils;
import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.util.CollectionUtils;
import org.springframework.validation.BindingResult;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.SessionAttributes;
import org.springframework.web.client.HttpClientErrorException;
import org.springframework.web.client.HttpServerErrorException;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Objects;

/**
 * 商品管理：商品登録更新（関連商品設定検索）アクション
 *
 * @author Pham Quang Dieu (VTI Japan Co., Ltd.)
 */
@Controller
@RequestMapping(value = "/goods/registupdate/relationsearch")
@SessionAttributes(value = "goodsRegistUpdateRelationSearchModel")
@PreAuthorize("hasAnyAuthority('GOODS:8')")
public class GoodsRegistUpdateRelationSearchController extends AbstractGoodsRegistUpdateController {

    /**
     * Logger
     */
    private static final Logger LOGGER = LoggerFactory.getLogger(GoodsRegistUpdateRelationSearchController.class);

    /**
     * 商品API
     */
    private final ProductApi productApi;

    /**
     * 関連商品API
     */
    private final RelationApi relationApi;

    /**
     * カテゴリAPI
     */
    private final CategoryApi categoryApi;

    /**
     * Helper
     */
    private final GoodsRegistUpdateRelationSearchHelper goodsRegUpRelationHelper;

    /** 商品登録更新（関連商品設定検索）: デフォルトページ番号 */
    private static final Integer DEFAULT_PNUM = 1;

    /** 商品登録更新（関連商品設定検索）: ソート項目 */
    private static final String DEFAULT_GOODSREGISTUPDATERELATIONSEARCH_ORDER_FIELD = "goodsGroupCode";

    /** カテゴリ検索: ソート項目 */
    private static final String DEFAULT_CATEGORY_ORDER_FIELD = "categoryId";

    /**
     * コンストラクター
     */
    public GoodsRegistUpdateRelationSearchController(ProductApi productApi,
                                                     RelationApi relationApi,
                                                     GoodsRegistUpdateRelationSearchHelper goodsRegUpRelationHelper,
                                                     CategoryApi categoryApi) {
        super(productApi, relationApi);
        this.productApi = productApi;
        this.relationApi = relationApi;
        this.goodsRegUpRelationHelper = goodsRegUpRelationHelper;
        this.categoryApi = categoryApi;
    }

    /**
     * 関連商品追加・初期表示用メソッド（Ajax用）<br/>
     *
     * @param goodsRegistUpdateModel               商品管理：商品登録更新ページ
     * @param goodsRegistUpdateRelationSearchModel 商品管理：商品登録更新（関連商品設定検索）ページ
     * @param redirectAttributes
     * @param model
     * @return 関連商品情報
     */
    @PostMapping("/ajax")
    @ResponseBody
    public ResponseEntity<GoodsRegistUpdateRelationSearchModel> doLoadPopupRelationSearch(
                    @RequestBody GoodsRegistUpdateModel goodsRegistUpdateModel,
                    GoodsRegistUpdateRelationSearchModel goodsRegistUpdateRelationSearchModel,
                    RedirectAttributes redirectAttributes,
                    Model model) {

        // コンポーネント値初期化
        initComponentValue(goodsRegistUpdateRelationSearchModel);
        if (ObjectUtils.isNotEmpty(goodsRegistUpdateModel)) {
            goodsRegistUpdateRelationSearchModel.setGoodsGroupCode(goodsRegistUpdateModel.getGoodsGroupCode());
            goodsRegistUpdateRelationSearchModel.setRedirectGoodsRelationEntityList(
                            goodsRegistUpdateModel.getRedirectGoodsRelationEntityList());
            goodsRegistUpdateRelationSearchModel.setTmpGoodsRelationEntityList(
                            goodsRegistUpdateModel.getTmpGoodsRelationEntityList());
            goodsRegistUpdateRelationSearchModel.setGoodsGroupDto(goodsRegistUpdateModel.getGoodsGroupDto());
            goodsRegistUpdateRelationSearchModel.setGoodsRelationEntityList(
                            goodsRegistUpdateModel.getGoodsRelationEntityList());
            goodsRegistUpdateRelationSearchModel.setScGoodsGroupSeq(goodsRegistUpdateModel.getScGoodsGroupSeq());
        }

        goodsRegUpRelationHelper.toPageForLoad(goodsRegistUpdateRelationSearchModel);
        // 検索条件がセッションに残っている場合は検索する
        if (goodsRegistUpdateRelationSearchModel.getGoodsGroupSearchForDaoConditionDto() != null
            && goodsRegistUpdateRelationSearchModel.isResultGoodsReration()) {
            GoodsSearchForBackDaoConditionDto conditionDto =
                            goodsRegistUpdateRelationSearchModel.getGoodsGroupSearchForDaoConditionDto();

            ProductItemListGetRequest productItemListGetRequest =
                            goodsRegUpRelationHelper.toProductItemListGetRequest(conditionDto);

            PageInfoRequest pageInfoRequest = new PageInfoRequest();
            // ページング検索セットアップ
            PageInfoModule pageInfoHelper = ApplicationContextUtility.getBean(PageInfoModule.class);
            pageInfoHelper.setupPageRequest(pageInfoRequest, DEFAULT_PNUM, Integer.MAX_VALUE,
                                            DEFAULT_GOODSREGISTUPDATERELATIONSEARCH_ORDER_FIELD, true
                                           );

            productItemListGetRequest.setRelationGoodsSearchFlag(true);

            try {
                ProductItemListResponse productItemListResponse =
                                productApi.getItems(productItemListGetRequest, pageInfoRequest);

                // 検索
                List<GoodsSearchResultDto> resultDtoList = goodsRegUpRelationHelper.toGoodsSearchResultDto(
                                productItemListResponse.getGoodsDetailsList());

                // ページにセット
                goodsRegUpRelationHelper.toPageForSearch(
                                resultDtoList, goodsRegistUpdateRelationSearchModel, conditionDto);
                goodsRegistUpdateRelationSearchModel.setResultGoodsRelationFlg(true);
            } catch (HttpServerErrorException se) {
                LOGGER.error("例外処理が発生しました", se);
                handleServerError(se.getResponseBodyAsString());
            } catch (HttpClientErrorException cs) {
                LOGGER.error("関連商品追加・初期表示用メソッド（Ajax用）に例外が発生した", cs);
                return null;
            }
        }
        // 実行前処理
        String check = preDoAction(goodsRegistUpdateRelationSearchModel, redirectAttributes, model);
        if (StringUtils.isNotEmpty(check)) {
            ResponseEntity.badRequest().body(check);
        }
        return ResponseEntity.ok(goodsRegistUpdateRelationSearchModel);
    }

    /**
     * 関連商品追加処理（Ajax用）<br/>
     *
     * @param goodsRegistUpdateRelationSearchModel 商品管理：商品登録更新（関連商品設定検索）ページ
     * @param redirectAttributes
     * @param model
     * @return 関連商品情報
     */
    @PostMapping(value = "doSelectRelationAjax")
    public ResponseEntity<?> doSelectRelationGoodsAjax(GoodsRegistUpdateRelationSearchModel goodsRegistUpdateRelationSearchModel,
                                                       RedirectAttributes redirectAttributes,
                                                       Model model) {
        // 実行前処理
        String check = preDoAction(goodsRegistUpdateRelationSearchModel, redirectAttributes, model);
        if (StringUtils.isNotEmpty(check)) {
            return ResponseEntity.badRequest().body(check);
        }
        // 検索結果チェック
        List<ValidatorMessage> validatorMessages1 = resultListCheckAjax(goodsRegistUpdateRelationSearchModel);
        if (!CollectionUtils.isEmpty(validatorMessages1)) {
            return ResponseEntity.badRequest().body(validatorMessages1);
        }
        // 関連商品追加前チェック
        List<ValidatorMessage> validatorMessages =
                        checkDataBeforeAddRelationGoodsAjax(goodsRegistUpdateRelationSearchModel);
        if (!CollectionUtils.isEmpty(validatorMessages)) {
            return ResponseEntity.badRequest().body(validatorMessages);
        }
        goodsRegUpRelationHelper.toPageForAddRelationGoods(goodsRegistUpdateRelationSearchModel);
        goodsRegUpRelationHelper.toPageForNext(goodsRegistUpdateRelationSearchModel);

        return ResponseEntity.ok(goodsRegistUpdateRelationSearchModel);
    }

    /**
     * 検索イベント(Ajax用)<br/>
     *
     * @param goodsRegistUpdateRelationSearchModel 関連商品設定検索
     * @param error
     * @param redirectAttributes
     * @param model
     * @return 関連商品情報
     */
    @PostMapping(value = "/post/ajax")
    public ResponseEntity<?> doSearchAjax(@Validated(SearchGroup.class)
                                                          GoodsRegistUpdateRelationSearchModel goodsRegistUpdateRelationSearchModel,
                                          BindingResult error,
                                          RedirectAttributes redirectAttributes,
                                          Model model) {

        if (error.hasErrors()) {
            List<ValidatorMessage> mapError = MessageUtils.getMessageErrorFromBindingResult(error);
            return ResponseEntity.badRequest().body(mapError);
        }
        // 実行前処理
        String check = preDoAction(goodsRegistUpdateRelationSearchModel, redirectAttributes, model);
        if (StringUtils.isNotEmpty(check)) {
            return ResponseEntity.badRequest().body(check);
        }

        // 検索前チェック
        List<ValidatorMessage> list = checkDataBeforeSearchAjax(goodsRegistUpdateRelationSearchModel);
        if (!CollectionUtils.isEmpty(list)) {
            return ResponseEntity.badRequest().body(list);
        }

        // 検索条件作成
        GoodsSearchForBackDaoConditionDto conditionDto =
                        goodsRegUpRelationHelper.toGoodsGroupSearchForDaoConditionDtoForSearch(
                                        goodsRegistUpdateRelationSearchModel);

        // ページング検索セットアップ
        // リクエスト用のページャーを生成
        PageInfoRequest pageInfoRequest = new PageInfoRequest();
        // ページング検索セットアップ
        PageInfoModule pageInfoHelper = ApplicationContextUtility.getBean(PageInfoModule.class);
        // リクエスト用のページャー項目をセット
        pageInfoHelper.setupPageRequest(pageInfoRequest, DEFAULT_PNUM, Integer.MAX_VALUE,
                                        DEFAULT_GOODSREGISTUPDATERELATIONSEARCH_ORDER_FIELD, true
                                       );
        // 検索条件をセッションに保存
        goodsRegistUpdateRelationSearchModel.setGoodsGroupSearchForDaoConditionDto(conditionDto);

        ProductItemListGetRequest request = goodsRegUpRelationHelper.toProductItemListGetRequest(conditionDto);

        request.setRelationGoodsSearchFlag(true);

        try {
            ProductItemListResponse response = productApi.getItems(request, pageInfoRequest);

            // 検索
            List<GoodsSearchResultDto> resultDtoList =
                            goodsRegUpRelationHelper.toGoodsSearchResultDto(response.getGoodsDetailsList());

            // ページにセット
            goodsRegUpRelationHelper.toPageForSearch(resultDtoList, goodsRegistUpdateRelationSearchModel, conditionDto);

            goodsRegistUpdateRelationSearchModel.setResultGoodsRelationFlg(true);

            // 画面再表示
            goodsRegUpRelationHelper.toPageForLoad(goodsRegistUpdateRelationSearchModel);

            return ResponseEntity.ok(resultDtoList);

        } catch (HttpServerErrorException | HttpClientErrorException e) {
            LOGGER.error("検索イベント(Ajax用)に例外が発生した", e);
            handleError(e.getStatusCode(), e.getResponseBodyAsString(), error, new HashMap<>());
            return null;
        }
    }

    /**
     * 商品検索結果リストが空でないことをチェックする<br/>
     * (ブラウザバック後の選択出力などでの不具合防止のため)<br/>
     *
     * @param goodsRegistUpdateRelationSearchModel 関連商品設定検索
     * @return
     */
    private List<ValidatorMessage> resultListCheckAjax(GoodsRegistUpdateRelationSearchModel goodsRegistUpdateRelationSearchModel) {
        List<ValidatorMessage> validatorMessages = new ArrayList<>();
        if (!goodsRegistUpdateRelationSearchModel.isResultGoodsReration()) {
            return validatorMessages;
        }
        if (goodsRegistUpdateRelationSearchModel.getResultItems().get(0).getResultGoodsGroupCode() == null || "".equals(
                        goodsRegistUpdateRelationSearchModel.getResultItems().get(0).getResultGoodsGroupCode())) {
            goodsRegistUpdateRelationSearchModel.setResultGoodsRelationFlg(false);
            goodsRegistUpdateRelationSearchModel.setResultItems(null);
            MessageUtils.getAllMessage(validatorMessages, "AGG000906", null);
        }
        return validatorMessages;
    }

    /**
     * アクション前処理<br/>
     * inteceptorより呼出し<br/>
     *
     * @param abstModel
     * @param redirectAttributes
     * @param model
     */
    public String preDoAction(AbstractGoodsRegistUpdateModel abstModel,
                              RedirectAttributes redirectAttributes,
                              Model model) {
        // 不正操作チェック
        return checkIllegalOperation(abstModel, redirectAttributes, model);
    }

    /**
     * 検索前 入力情報チェック（Ajax用）<br/>
     * Validatorで対応できないもの
     *
     * @param goodsRegistUpdateRelationSearchModel 関連商品設定検索
     */
    private List<ValidatorMessage> checkDataBeforeSearchAjax(GoodsRegistUpdateRelationSearchModel goodsRegistUpdateRelationSearchModel) {
        List<ValidatorMessage> list = new ArrayList<>();
        // 検索キーワード10件超エラー
        if (goodsRegistUpdateRelationSearchModel.getSearchRelationGoodsKeyword() != null) {
            String[] searchKeywordArray =
                            goodsRegistUpdateRelationSearchModel.getSearchRelationGoodsKeyword().split("[\\s|　]+");
            if (searchKeywordArray.length > 10) {
                MessageUtils.getAllMessage(list, "AGG000904", new Object[] {searchKeywordArray.length});
            }
        }
        return list;
    }

    /**
     * 関連商品追加前 入力情報チェック(Ajax用)<br/>
     * Validatorで対応できないもの
     *
     * @param goodsRegistUpdateRelationSearchModel 関連商品設定検索
     */
    private List<ValidatorMessage> checkDataBeforeAddRelationGoodsAjax(GoodsRegistUpdateRelationSearchModel goodsRegistUpdateRelationSearchModel) {
        int selectedCount = 0;
        List<ValidatorMessage> validatorMessages = new ArrayList<>();
        if (goodsRegistUpdateRelationSearchModel.getTmpGoodsRelationEntityList() != null) {
            int count = goodsRegistUpdateRelationSearchModel.getTmpGoodsRelationEntityList().size();
            boolean overMaxRelGoods = false;
            for (Iterator<GoodsRegistUpdateRelationSearchItem> it =
                 goodsRegistUpdateRelationSearchModel.getResultItems().iterator(); it.hasNext(); ) {
                GoodsRegistUpdateRelationSearchItem item = it.next();
                if (item.isResultCheck()) {
                    count++;
                    selectedCount++;

                    // 関連商品保持可能上限を超えるとエラー
                    if (!overMaxRelGoods && count > goodsRegistUpdateRelationSearchModel.getGoodsRelationAmount()) {
                        MessageUtils.getAllMessage(validatorMessages, "AGG000902",
                                                   new Object[] {goodsRegistUpdateRelationSearchModel.getGoodsRelationAmount()}
                                                  );
                        overMaxRelGoods = true;
                    }

                    // 更新時、自身の商品グループでないことを確認する
                    if (goodsRegistUpdateRelationSearchModel.getGoodsGroupDto().getGoodsGroupEntity() != null &&
                        goodsRegistUpdateRelationSearchModel.getGoodsGroupDto()
                                                            .getGoodsGroupEntity()
                                                            .getGoodsGroupCode() != null &&
                        goodsRegistUpdateRelationSearchModel.getGoodsGroupDto().getGoodsGroupEntity().getGoodsGroupSeq()
                        != null && (StringUtils.isNotEmpty(item.getResultGoodsGroupCode())
                                    && item.getResultGoodsGroupCode()
                                           .equals(goodsRegistUpdateRelationSearchModel.getGoodsGroupDto()
                                                                                       .getGoodsGroupEntity()
                                                                                       .getGoodsGroupCode()))) {
                        MessageUtils.getAllMessage(validatorMessages, "AGG000901",
                                                   new Object[] {goodsRegistUpdateRelationSearchModel.getGoodsRelationAmount()}
                                                  );
                    }

                    // 同一商品グループがtmp関連商品リストにないことを確認する
                    for (GoodsRelationEntity goodsRelationEntity : goodsRegistUpdateRelationSearchModel.getTmpGoodsRelationEntityList()) {
                        if (StringUtils.isNotEmpty(item.getResultGoodsGroupCode()) && item.getResultGoodsGroupCode()
                                                                                          .equals(goodsRelationEntity.getGoodsGroupCode())) {
                            // 関連商品重複チェックエラー
                            MessageUtils.getAllMessage(validatorMessages, "AGG000903",
                                                       new Object[] {item.getResultGoodsGroupCode()}
                                                      );
                        }
                    }
                }
            }
        }

        // 選択してない場合はエラー
        if (selectedCount == 0) {
            MessageUtils.getAllMessage(validatorMessages, "AGG000907", null);
        }
        return validatorMessages;
    }

    /**
     * コンポーネント値初期化
     *
     * @param goodsRegistUpdateRelationSearchModel
     */
    private void initComponentValue(GoodsRegistUpdateRelationSearchModel goodsRegistUpdateRelationSearchModel) {
        jp.co.itechh.quad.category.presentation.api.param.PageInfoRequest pageInfoRequest =
                        new jp.co.itechh.quad.category.presentation.api.param.PageInfoRequest();
        // ページング検索セットアップ
        PageInfoModule pageInfoHelper = ApplicationContextUtility.getBean(PageInfoModule.class);
        pageInfoHelper.setupPageRequest(
                        pageInfoRequest, DEFAULT_PNUM, Integer.MAX_VALUE, DEFAULT_CATEGORY_ORDER_FIELD, true);
        try {
            CategoryListResponse categoryListResponse = categoryApi.get(new CategoryListGetRequest(), pageInfoRequest);

            Map<String, String> categoryMapList = goodsRegUpRelationHelper.convertCategoryMapStr(
                            Objects.requireNonNull(categoryListResponse.getCategoryList()));

            goodsRegistUpdateRelationSearchModel.setInputingFlg(true);
            goodsRegistUpdateRelationSearchModel.setSearchCategoryIdItems(categoryMapList);
        } catch (HttpServerErrorException se) {
            LOGGER.error("例外処理が発生しました", se);
            handleServerError(se.getResponseBodyAsString());
        } catch (HttpClientErrorException ce) {
            LOGGER.error("コンポーネント値初期化", ce);
        }
    }

    /**
     * 選択されたカテゴリをモデルに更新 （Ajax）<br/>
     *
     * @return カテゴリーリスト
     */
    @PostMapping(value = "/category/update/ajax")
    @ResponseBody
    public ResponseEntity<?> updateSelectedCategoryList(
                    @RequestBody CategoryAndTagForGoodsRegistUpdateDto categoryChosenDto,
                    GoodsRegistUpdateRelationSearchModel goodsRegistUpdateRelationSearchModel) {

        if (CollectionUtil.isNotEmpty(categoryChosenDto.getCategoryChosenList())) {
            goodsRegistUpdateRelationSearchModel.setSearchCategoryIdList(categoryChosenDto.getCategoryChosenList());
        } else if (CollectionUtil.isEmpty(categoryChosenDto.getCategoryChosenList())) {
            goodsRegistUpdateRelationSearchModel.setSearchCategoryIdList(new ArrayList<>());
        }

        return ResponseEntity.ok("Success");
    }
}