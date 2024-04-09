package jp.co.itechh.quad.product.presentation.api;

import io.swagger.annotations.ApiParam;
import jp.co.itechh.quad.batch.queue.BatchQueueMessage;
import jp.co.itechh.quad.batch.queue.MessagePublisherService;
import jp.co.itechh.quad.core.base.util.AppLevelFacesMessageUtil;
import jp.co.itechh.quad.core.base.util.common.PropertiesUtil;
import jp.co.itechh.quad.core.base.utility.ApplicationContextUtility;
import jp.co.itechh.quad.core.constant.type.HTypeBatchResult;
import jp.co.itechh.quad.core.dto.goods.goodstag.GoodsTagDto;
import jp.co.itechh.quad.core.entity.goods.goods.GoodsTagEntity;
import jp.co.itechh.quad.core.service.goods.tag.GoodsTagClearService;
import jp.co.itechh.quad.core.service.goods.tag.GoodsTagGetService;
import jp.co.itechh.quad.core.web.AbstractController;
import jp.co.itechh.quad.core.web.HeaderParamsUtility;
import jp.co.itechh.quad.core.web.PageInfoModule;
import jp.co.itechh.quad.tag.presentation.api.ProductsApi;
import jp.co.itechh.quad.tag.presentation.api.param.BatchExecuteRequest;
import jp.co.itechh.quad.tag.presentation.api.param.BatchExecuteResponse;
import jp.co.itechh.quad.tag.presentation.api.param.PageInfoRequest;
import jp.co.itechh.quad.tag.presentation.api.param.PageInfoResponse;
import jp.co.itechh.quad.tag.presentation.api.param.ProductTagListGetRequest;
import jp.co.itechh.quad.tag.presentation.api.param.ProductTagListResponse;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.amqp.AmqpException;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

import javax.validation.Valid;
import java.lang.reflect.InvocationTargetException;
import java.util.List;

/**
 * タグ Controller
 *
 * @author PHAM QUANG DIEU (VJP)
 */
@RestController
public class TagsController extends AbstractController implements ProductsApi {

    /** ロガー */
    protected static final Logger LOGGER = LoggerFactory.getLogger(TagsController.class);

    /** レスポンス用メッセージパラメーター */
    private final static String messageResponseParam = "バッチの手動起動";

    /** ログ用メッセージパラメーター */
    private final static String messageLogParam = "起動の失敗： ";

    /** タグ Helper */
    private final TagsHelper tagsHelper;

    /** タグ商品ロサービス */
    private final GoodsTagGetService goodsTagGetService;

    /** 商品タグクリア */
    private final GoodsTagClearService goodsTagClearService;

    /** キューパブリッシャーサービス */
    private final MessagePublisherService messagePublisherService;

    /** ヘッダパラメーターユーティル */
    private final HeaderParamsUtility headerParamsUtil;

    /**
     * コンストラクター
     * @param tagsHelper               タグ Helper
     * @param goodsTagGetService       タグ商品ロサービス
     * @param goodsTagClearService     商品タグクリア
     * @param messagePublisherService  キューパブリッシャーサービス
     * @param headerParamsUtil         ヘッダパラメーターユーティル
     */
    public TagsController(TagsHelper tagsHelper,
                          GoodsTagGetService goodsTagGetService,
                          GoodsTagClearService goodsTagClearService,
                          MessagePublisherService messagePublisherService,
                          HeaderParamsUtility headerParamsUtil) {
        this.tagsHelper = tagsHelper;
        this.goodsTagGetService = goodsTagGetService;
        this.goodsTagClearService = goodsTagClearService;
        this.messagePublisherService = messagePublisherService;
        this.headerParamsUtil = headerParamsUtil;
    }

    /**
     * GET /products/tags : タグ一覧取得
     * タグ一覧取得
     *
     * @param productTagListGetRequest 商品タグ一覧取得リクエスト (optional)
     * @param pageInfoRequest ページ情報リクエスト (optional)
     * @return 商品タグ一覧レスポンス (status code 200)
     *         or Bad Request（バリデーションエラー、業務ルールエラー、妥当性チェックエラーの場合） (status code 400)
     *         or Internal Server Error（アサートエラー、予想外エラーの場合） (status code 500)
     *         or システムエラー (status code 200)
     */
    @Override
    public ResponseEntity<ProductTagListResponse> get(
                    @ApiParam("商品タグ一覧取得リクエスト") @Valid ProductTagListGetRequest productTagListGetRequest,
                    @ApiParam("ページ情報リクエスト") @Valid PageInfoRequest pageInfoRequest) {

        GoodsTagDto dto = new GoodsTagDto();
        dto.setTag(productTagListGetRequest.getTagSearchKeyword());

        // ページング検索セットアップ
        PageInfoModule pageInfoModule = ApplicationContextUtility.getBean(PageInfoModule.class);
        pageInfoModule.setupPageInfo(dto, pageInfoRequest.getPage(), pageInfoRequest.getLimit(),
                                     pageInfoRequest.getOrderBy(), pageInfoRequest.getSort()
                                    );

        List<GoodsTagEntity> goodsTagEntities = goodsTagGetService.execute(dto);

        // ページ情報レスポンスを設定
        PageInfoResponse pageInfoResponse = new PageInfoResponse();
        try {
            pageInfoModule.setupResponsePager(dto, pageInfoResponse);
        } catch (InvocationTargetException | IllegalAccessException e) {
            LOGGER.info("ページ情報レスポンスの設定異常： " + e.getMessage());
        }

        ProductTagListResponse productTagListResponse = tagsHelper.toProductTagListResponse(goodsTagEntities);
        productTagListResponse.setPageInfo(pageInfoResponse);

        return new ResponseEntity<>(productTagListResponse, HttpStatus.OK);
    }

    /**
     * POST /products/tags/clear : タグクリアバッチ
     * タグクリアバッチ
     *
     * @param batchExecuteRequest タグクリアバッチリクエスト (required)
     * @return 結果レスポンス (status code 200)
     *         or Bad Request（バリデーションエラー、業務ルールエラー、妥当性チェックエラーの場合） (status code 400)
     *         or Internal Server Error（アサートエラー、予想外エラーの場合） (status code 500)
     *         or その他エラー (status code 200)
     */
    @Override
    public ResponseEntity<BatchExecuteResponse> execute(
                    @ApiParam(value = "タグクリアバッチリクエスト", required = true) @Valid @RequestBody
                                    BatchExecuteRequest batchExecuteRequest) {

        // ルーティングキー取得
        String routing = PropertiesUtil.getSystemPropertiesValue("queue.tagClear.routing");

        // メッセージ作成
        BatchQueueMessage message = new BatchQueueMessage();
        message.setAdministratorSeq(getAdminId());
        message.setStartType(batchExecuteRequest.getStartType());

        // レスポンス
        BatchExecuteResponse response = new BatchExecuteResponse();
        String messageResponse;

        try {
            // キューにパブリッシュー
            messagePublisherService.publish(routing, message);

            // レスポンス作成
            response.setExecuteCode(HTypeBatchResult.COMPLETED.getValue());
            messageResponse = AppLevelFacesMessageUtil.getAllMessage("AEB000102", new Object[] {messageResponseParam})
                                                      .getMessage();
            response.setExecuteMessage(messageResponse);

            return new ResponseEntity<>(response, HttpStatus.OK);
        } catch (AmqpException e) {
            LOGGER.error("タグクリアバッチ" + messageLogParam + e.getMessage());

            // レスポンス作成
            response.setExecuteCode(HTypeBatchResult.FAILED.getValue());
            messageResponse = AppLevelFacesMessageUtil.getAllMessage("AEB000101", new Object[] {messageResponseParam})
                                                      .getMessage();
            response.setExecuteMessage(messageResponse);
            return new ResponseEntity<>(response, HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    /**
     * 管理者SEQを取得する
     *
     * @return customerId 顧客ID
     */
    private Integer getAdminId() {
        if (this.headerParamsUtil.getAdministratorSeq() == null) {
            return null;
        } else {
            return Integer.valueOf(this.headerParamsUtil.getAdministratorSeq());
        }
    }
}