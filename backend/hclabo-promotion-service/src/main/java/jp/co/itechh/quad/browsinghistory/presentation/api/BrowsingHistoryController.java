/*
 * Project Name : HIT-MALL4
 *
 * Copyright (C) 2021 i-TEC HANKYU HANSHIN INC. All Rights Reserved.
 *
 */

package jp.co.itechh.quad.browsinghistory.presentation.api;

import io.swagger.annotations.ApiParam;
import jp.co.itechh.quad.browsinghistory.presentation.api.param.BrowsingHistoryListGetRequest;
import jp.co.itechh.quad.browsinghistory.presentation.api.param.BrowsingHistoryListResponse;
import jp.co.itechh.quad.browsinghistory.presentation.api.param.BrowsingHistoryRegistRequest;
import jp.co.itechh.quad.browsinghistory.presentation.api.param.PageInfoRequest;
import jp.co.itechh.quad.core.base.utility.ApplicationContextUtility;
import jp.co.itechh.quad.core.constant.type.HTypeSiteType;
import jp.co.itechh.quad.core.dto.goods.goodsgroup.GoodsGroupDto;
import jp.co.itechh.quad.core.entity.goods.browsinghistory.BrowsinghistoryEntity;
import jp.co.itechh.quad.core.logic.goods.browsinghistory.GoodsBrowsingHistoryRegistLogic;
import jp.co.itechh.quad.core.service.goods.browsinghistory.GoodsBrowsingHistoryClearService;
import jp.co.itechh.quad.core.service.goods.browsinghistory.OpenBrowsingHistoryListGetService;
import jp.co.itechh.quad.core.service.process.AsyncService;
import jp.co.itechh.quad.core.utility.AsyncTaskUtility;
import jp.co.itechh.quad.core.web.AbstractController;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

import javax.validation.Valid;
import javax.validation.constraints.NotNull;
import java.util.List;

/**
 * あしあとスエンドポイント Controller
 *
 * @author PHAM QUANG DIEU (VJP)
 *
 */
@RestController
public class BrowsingHistoryController extends AbstractController implements PromotionsApi {

    /** あしあと商品情報取得サービス */
    private final OpenBrowsingHistoryListGetService openBrowsingHistoryListGetService;

    /** あしあとクリアサービス */
    private final GoodsBrowsingHistoryClearService goodsBrowsingHistoryClearService;

    /** あしあとスエンドポイント Helper */
    private final BrowsingHistoryHelper browsingHistoryHelper;

    /** 非同期処理サービス */
    private AsyncService asyncService;

    /** ロガー */
    private static final Logger LOGGER = LoggerFactory.getLogger(BrowsingHistoryController.class);

    /**
     * コンストラクタ
     *
     * @param openBrowsingHistoryListGetService 公開あしあと商品情報取得
     * @param goodsBrowsingHistoryClearService あしあと商品クリアクラス
     * @param browsingHistoryHelper あしあとスエンドポイント Helper
     * @param asyncService 非同期処理サービス
     */
    public BrowsingHistoryController(OpenBrowsingHistoryListGetService openBrowsingHistoryListGetService,
                                     GoodsBrowsingHistoryClearService goodsBrowsingHistoryClearService,
                                     BrowsingHistoryHelper browsingHistoryHelper,
                                     AsyncService asyncService) {
        this.openBrowsingHistoryListGetService = openBrowsingHistoryListGetService;
        this.goodsBrowsingHistoryClearService = goodsBrowsingHistoryClearService;
        this.browsingHistoryHelper = browsingHistoryHelper;
        this.asyncService = asyncService;
    }

    /**
     * GET /promotions/{accessUid}/browsing-history : あしあと商品情報一覧取得
     * あしあと商品情報一覧取得
     *
     * @param accessUid 端末識別情報 (required)
     * @param browsingHistoryListGetRequest あしあと商品情報一覧取得リクエスト (required)
     * @param pageInfoRequest ページ情報リクエスト（ページネーションのため） (optional)
     * @return あしあと商品情報一覧レスポンス (status code 200)
     * or システムエラー (status code 500)
     */

    @Override
    public ResponseEntity<BrowsingHistoryListResponse> get(String accessUid,
                                                           @NotNull @Valid
                                                                           BrowsingHistoryListGetRequest browsingHistoryListGetRequest,
                                                           @Valid PageInfoRequest pageInfoRequest) {

        // 公開あしあと商品情報取得
        List<GoodsGroupDto> goodsGroupDtoList =
                        openBrowsingHistoryListGetService.execute(accessUid, HTypeSiteType.FRONT_PC,
                                                                  browsingHistoryListGetRequest.getBrowsingHistoryGoodsLimit(),
                                                                  browsingHistoryListGetRequest.getExceptGoodsGroupSeq()
                                                                 );

        // あしあと商品情報一覧レスポンス
        BrowsingHistoryListResponse browsingHistoryListResponse =
                        browsingHistoryHelper.toBrowsingHistoryListResponse(goodsGroupDtoList);

        return new ResponseEntity<>(browsingHistoryListResponse, HttpStatus.OK);
    }

    /**
     * DELETE /promotions/{accessUid}/browsing-history : あしあとクリア
     * あしあとクリア
     *
     * @param accessUid 端末識別情報 (required)
     * @return 削除成功 (status code 200)
     * or システムエラー (status code 500)
     */
    @Override
    public ResponseEntity<Void> delete(String accessUid) {
        goodsBrowsingHistoryClearService.execute(accessUid);
        return new ResponseEntity<>(HttpStatus.OK);
    }

    /**
     * POST /promotions/{accessUid}/browsing-history : あしあと商品情報登録
     * あしあと商品情報登録
     *
     * @param accessUid 端末識別情報 (required)
     * @param browsingHistoryRegistRequest あしあと商品情報登録リクエスト (required)
     * @return 削除成功 (status code 200)
     *         or システムエラー (status code 500)
     */
    @Override
    public ResponseEntity<Void> regist(
                    @ApiParam(value = "端末識別情報", required = true) @PathVariable("accessUid") String accessUid,
                    @ApiParam(value = "あしあと商品情報登録リクエスト", required = true) @Valid @RequestBody
                                    BrowsingHistoryRegistRequest browsingHistoryRegistRequest) {

        BrowsinghistoryEntity entity = ApplicationContextUtility.getBean(BrowsinghistoryEntity.class);
        entity.setShopSeq(1001);
        entity.setAccessUid(accessUid);
        entity.setGoodsGroupSeq(browsingHistoryRegistRequest.getGoodsGroupSeq());

        // サービス登録
        Object[] args = new Object[] {entity};
        Class<?>[] argsClass = new Class<?>[] {BrowsinghistoryEntity.class};
        GoodsBrowsingHistoryRegistLogic service =
                        ApplicationContextUtility.getBean(GoodsBrowsingHistoryRegistLogic.class);

        // 非同期処理を登録
        AsyncTaskUtility.executeAfterTransactionCommit(() -> {
            asyncService.asyncService(service, "execute", args, argsClass);
        });

        return new ResponseEntity<>(HttpStatus.OK);
    }
}