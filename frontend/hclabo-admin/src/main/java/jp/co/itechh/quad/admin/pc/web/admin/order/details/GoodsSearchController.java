/*
 * Project Name : HIT-MALL3
 *
 * Copyright (C) 2008 i-TEC HANKYU HANSHIN INC. All Rights Reserved.
 *
 */

package jp.co.itechh.quad.admin.pc.web.admin.order.details;

import jp.co.itechh.quad.admin.base.utility.ApplicationContextUtility;
import jp.co.itechh.quad.admin.pc.web.admin.common.ajax.MessageUtils;
import jp.co.itechh.quad.admin.pc.web.admin.common.ajax.ValidatorMessage;
import jp.co.itechh.quad.admin.pc.web.admin.order.details.validation.goodssearch.group.GoodsSearchGroup;
import jp.co.itechh.quad.admin.web.PageInfoModule;
import jp.co.itechh.quad.product.presentation.api.ProductApi;
import jp.co.itechh.quad.product.presentation.api.param.PageInfoRequest;
import jp.co.itechh.quad.product.presentation.api.param.ProductOrderItemListGetRequest;
import jp.co.itechh.quad.product.presentation.api.param.ProductOrderItemListResponse;
import jp.co.itechh.quad.transaction.presentation.api.TransactionApi;
import jp.co.itechh.quad.transaction.presentation.api.param.AddOrderItemToTransactionForRevisionRequest;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.SessionAttributes;
import org.springframework.web.bind.support.SessionStatus;
import org.springframework.web.client.HttpClientErrorException;
import org.springframework.web.client.HttpServerErrorException;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import java.util.ArrayList;
import java.util.List;

/**
 * 「新規受注：商品検索」画面のアクション
 */
@RequestMapping("/order/details/goodssearch")
@Controller
@SessionAttributes({"goodsSearchModel", "detailsUpdateCommonModel"})
@PreAuthorize("hasAnyAuthority('ORDER:8')")
public class GoodsSearchController extends AbstractOrderDetailsController {

    /** ロガー */
    private static final Logger LOGGER = LoggerFactory.getLogger(GoodsSearchController.class);

    /** フロントチェック：非同期追加失敗*/
    private static final String MSGCD_AJAX_ADD_GOODS = "AOX002305";

    /** 商品検索：デフォルトページ番号 */
    private static final Integer DEFAULT_GOODSSEARCH_PNUM = 1;

    /** 商品検索：デフォルト：ソート項目 */
    private static final String DEFAULT_GOODSSEARCH_ORDER_FIELD = "goodsGroupCode";

    /** 商品検索：デフォルト：ソート条件(昇順/降順) */
    private static final boolean DEFAULT_GOODSSEARCH_ORDER_ASC = true;

    /** 商品検索：デフォルト1ページ件数 */
    private static final Integer DEFAULT_GOODSSEARCH_LIMIT = 100;

    /** helper */
    private final GoodsSearchHelper goodsSearchHelper;

    /** 商品API */
    private final ProductApi productApi;

    /** 取引API */
    private final TransactionApi transactionApi;

    @Autowired
    public GoodsSearchController(GoodsSearchHelper goodsSearchHelper,
                                 ProductApi productApi,
                                 TransactionApi transactionApi) {
        this.goodsSearchHelper = goodsSearchHelper;
        this.productApi = productApi;
        this.transactionApi = transactionApi;
    }

    /**
     * 「検索」ボタン押下時の処理<br/>
     *
     * @return 自画面
     */
    @PostMapping(value = "/doGoodsSearchAjax")
    public ResponseEntity<?> doGoodsSearchAjax(@Validated(GoodsSearchGroup.class) GoodsSearchModel goodsSearchModel,
                                               BindingResult error,
                                               RedirectAttributes redirectAttributes,
                                               Model model) {

        // エラーチェック
        if (error.hasErrors()) {
            List<ValidatorMessage> mapError = MessageUtils.getMessageErrorFromBindingResult(error);
            return ResponseEntity.badRequest().body(mapError);
        }

        // 検索条件作成
        ProductOrderItemListGetRequest productOrderItemListGetRequest =
                        goodsSearchHelper.toGoodsSearchForBackDaoConditionDtoForGoodsSearchAjax(goodsSearchModel);
        // ページング条件作成
        PageInfoRequest pageInfoRequest = new PageInfoRequest();
        // ページング検索セットアップ
        PageInfoModule pageInfoHelper = ApplicationContextUtility.getBean(PageInfoModule.class);
        // リクエスト用のページャー項目をセット
        pageInfoHelper.setupPageRequest(pageInfoRequest, DEFAULT_GOODSSEARCH_PNUM, DEFAULT_GOODSSEARCH_LIMIT,
                                        DEFAULT_GOODSSEARCH_ORDER_FIELD, DEFAULT_GOODSSEARCH_ORDER_ASC
                                       );

        ProductOrderItemListResponse productOrderItemListResponse =
                        productApi.getOrderItems(productOrderItemListGetRequest, pageInfoRequest);
        if (productOrderItemListResponse == null) {
            return null;
        }

        // 画面Modelにセット
        Integer startNo = pageInfoRequest.getPage() * pageInfoRequest.getLimit() + 1;
        goodsSearchModel.setResultItems(goodsSearchHelper.toPageForSearch(
                        productOrderItemListResponse.getGoodsSearchResultForOrderRegistList(), startNo));

        return ResponseEntity.ok(productOrderItemListResponse.getGoodsSearchResultForOrderRegistList());
    }

    /**
     * 「追加」ボタン押下時の処理<br/>
     *
     * @return 商品選択画面
     */
    @PostMapping(value = "doOrderGoodsAddAjax")
    @ResponseBody
    public ResponseEntity<?> doOrderGoodsAddAjax(GoodsSearchModel goodsSearchModel,
                                                 DetailsUpdateCommonModel detailsUpdateCommonModel,
                                                 RedirectAttributes redirectAttrs,
                                                 SessionStatus sessionStatus,
                                                 Model model) {

        List<ValidatorMessage> messageList = new ArrayList<>();

        // チェックされた商品の注文商品IDリストを取得
        List<String> addItemIdList = goodsSearchHelper.toOrderItemSeqList(goodsSearchModel.getResultItems());

        // リストが空の場合はエラー
        if (addItemIdList.isEmpty()) {
            MessageUtils.getAllMessage(messageList, "AOX001012", null);
            return ResponseEntity.badRequest().body(messageList);
        }

        AddOrderItemToTransactionForRevisionRequest addOrderItemToTransactionForRevisionRequest =
                        new AddOrderItemToTransactionForRevisionRequest();
        addOrderItemToTransactionForRevisionRequest.setTransactionRevisionId(
                        detailsUpdateCommonModel.getTransactionRevisionId());
        addOrderItemToTransactionForRevisionRequest.setItemIdList(addItemIdList);
        try {
            transactionApi.addOrderItemToTransactionForRevision(addOrderItemToTransactionForRevisionRequest);
        } catch (HttpClientErrorException ce) {
            LOGGER.error("例外処理が発生しました", ce);
            // 想定内のエラー（400番台）の場合
            messageList = new ArrayList<>();
            MessageUtils.getAllMessage(messageList, MSGCD_AJAX_ADD_GOODS, null);
            return ResponseEntity.badRequest().body(messageList);
        } catch (HttpServerErrorException se) {
            LOGGER.error("例外処理が発生しました", se);
            // 想定外のエラー（500番台）の場合
            return ResponseEntity.internalServerError().body(getServerErrorMessage(se.getResponseBodyAsString()));
        }

        // Modelをクリア
        clearModel(GoodsSearchModel.class, goodsSearchModel, model);

        return ResponseEntity.ok(messageList);
    }
}