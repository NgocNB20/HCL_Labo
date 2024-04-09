/*
 * Copyright (C) 2022 i-TEC HANKYU HANSHIN INC. All Rights Reserved.
 */

package jp.co.itechh.quad.ddd.presentation.inventory;

import io.swagger.annotations.ApiParam;
import jp.co.itechh.quad.core.base.util.AppLevelFacesMessageUtil;
import jp.co.itechh.quad.core.base.util.common.PropertiesUtil;
import jp.co.itechh.quad.core.base.utility.ApplicationContextUtility;
import jp.co.itechh.quad.core.base.utility.ConversionUtility;
import jp.co.itechh.quad.core.batch.queue.BatchQueueMessage;
import jp.co.itechh.quad.core.batch.queue.MessagePublisherService;
import jp.co.itechh.quad.core.constant.type.HTypeBatchResult;
import jp.co.itechh.quad.core.constant.type.HTypeOpenDeleteStatus;
import jp.co.itechh.quad.core.constant.type.HTypeStockStatusType;
import jp.co.itechh.quad.core.dto.goods.stock.StockResultSearchForDaoConditionDto;
import jp.co.itechh.quad.core.dto.goods.stock.StockStatusDisplayConditionDto;
import jp.co.itechh.quad.core.entity.goods.stock.StockResultEntity;
import jp.co.itechh.quad.core.logic.goods.stock.StockStatusDisplayGetRealStatusLogic;
import jp.co.itechh.quad.core.service.goods.stock.StockResultListGetService;
import jp.co.itechh.quad.core.util.common.EnumTypeUtil;
import jp.co.itechh.quad.core.web.AbstractController;
import jp.co.itechh.quad.core.web.HeaderParamsUtility;
import jp.co.itechh.quad.core.web.PageInfoModule;
import jp.co.itechh.quad.inventory.presentation.api.ShippingsApi;
import jp.co.itechh.quad.inventory.presentation.api.param.BatchExecuteResponse;
import jp.co.itechh.quad.inventory.presentation.api.param.InventoryReleaseRequest;
import jp.co.itechh.quad.inventory.presentation.api.param.InventoryResultListGetRequest;
import jp.co.itechh.quad.inventory.presentation.api.param.InventoryResultListResponse;
import jp.co.itechh.quad.inventory.presentation.api.param.InventoryStatusDisplayGetRequest;
import jp.co.itechh.quad.inventory.presentation.api.param.InventoryStatusDisplayResponse;
import jp.co.itechh.quad.inventory.presentation.api.param.PageInfoRequest;
import jp.co.itechh.quad.inventory.presentation.api.param.PageInfoResponse;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.amqp.AmqpException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

import javax.validation.Valid;
import javax.validation.constraints.NotNull;
import java.lang.reflect.InvocationTargetException;
import java.sql.Timestamp;
import java.util.List;

/**
 * 在庫エンドポイント Controller
 *
 * @author kimura
 */
@RestController
public class InventoryController extends AbstractController implements ShippingsApi {

    /** レスポンス用メッセージパラメーター */
    private final static String messageResponseParam = "バッチの手動起動";

    /** ログ用メッセージパラメーター */
    private final static String messageLogParam = "起動の失敗： ";

    /** キューパブリッシャーサービス */
    private final MessagePublisherService messagePublisherService;

    /** ヘッダパラメーターユーティル */
    private final HeaderParamsUtility headerParamsUtil;

    /** 変換Utility */
    private final ConversionUtility conversionUtility;

    /** ここからボトムアップ定義のフィールド項目 */

    /*** インベントリヘルパー */
    private final InventoryHelper inventoryHelper;

    /*** 入庫実績リスト取得サービス */
    private final StockResultListGetService stockResultListGetService;

    /** リアルタイム在庫状況判定ロジック */
    private final StockStatusDisplayGetRealStatusLogic stockStatusDisplayGetRealStatusLogic;

    /** ロガー */
    private static final Logger LOGGER = LoggerFactory.getLogger(InventoryController.class);

    /**  ここまでボトムアップ定義のコントローラーメソッド */

    /** コンストラクタ */
    @Autowired
    public InventoryController(MessagePublisherService messagePublisherService,
                               HeaderParamsUtility headerParamsUtil,
                               ConversionUtility conversionUtility,
                               InventoryHelper inventoryHelper,
                               StockResultListGetService stockResultListGetService,
                               StockStatusDisplayGetRealStatusLogic stockStatusDisplayGetRealStatusLogic) {
        this.messagePublisherService = messagePublisherService;
        this.headerParamsUtil = headerParamsUtil;
        this.conversionUtility = conversionUtility;
        this.inventoryHelper = inventoryHelper;
        this.stockResultListGetService = stockResultListGetService;
        this.stockStatusDisplayGetRealStatusLogic = stockStatusDisplayGetRealStatusLogic;
    }

    /**
     * POST /shippings/inventories/release : 在庫開放
     *
     * @param inventoryReleaseRequest 在庫開放リクエスト (required)
     * @return バッチ起動結果レスポンス (status code 200)or その他エラー (status code 500)
     */
    @Override
    public ResponseEntity<BatchExecuteResponse> release(
                    @ApiParam(value = "在庫開放リクエスト", required = true) @Valid @RequestBody
                                    InventoryReleaseRequest inventoryReleaseRequest) {

        // ルーティングキー取得
        String routing = PropertiesUtil.getSystemPropertiesValue("queue.inventoryrelease.routing");

        // メッセージ作成
        BatchQueueMessage message = new BatchQueueMessage();
        message.setAdministratorSeq(getAdminSeq());
        message.setStartType(inventoryReleaseRequest.getStartType());

        // レスポンス
        BatchExecuteResponse response = new BatchExecuteResponse();
        String messageResponse;
        try {
            // キューにパブリッシュー
            this.messagePublisherService.publish(routing, message);

            response.setExecuteCode(HTypeBatchResult.COMPLETED.getValue());
            response.setExecuteMessage("バッチの手動起動を受け付けました。");

            return new ResponseEntity<>(response, HttpStatus.OK);
        } catch (AmqpException e) {
            LOGGER.error("在庫解放" + messageLogParam + e.getMessage());

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
     * @return AdminSeq 管理者SEQ
     */
    private Integer getAdminSeq() {
        return this.conversionUtility.toInteger(this.headerParamsUtil.getAdministratorSeq());
    }

    /**  ここからボトムアップ定義のコントローラーメソッド */

    /**
     * GET /shippings/inventories/results : 入庫実績一覧取得
     * 入庫実績一覧取得
     *
     * @param inventoryResultListGetRequest 入庫実績一覧リクエスト (required)
     * @param pageInfoRequest               ページ情報リクエスト（ページネーションのため） (optional)
     * @return 入庫実績一覧レスポンス (status code 200)
     * or その他エラー (status code 500)
     */
    @Override
    public ResponseEntity<InventoryResultListResponse> get(
                    @NotNull @ApiParam(value = "入庫実績一覧リクエスト", required = true) @Valid
                                    InventoryResultListGetRequest inventoryResultListGetRequest,
                    @ApiParam("ページ情報リクエスト（ページネーションのため）") @Valid PageInfoRequest pageInfoRequest) {

        StockResultSearchForDaoConditionDto stockResultSearchForDaoConditionDto =
                        inventoryHelper.toStockResultSearchForDaoConditionDto(inventoryResultListGetRequest);

        // ページング検索セットアップ
        PageInfoModule pageInfoModule = ApplicationContextUtility.getBean(PageInfoModule.class);
        pageInfoModule.setupPageInfo(stockResultSearchForDaoConditionDto, pageInfoRequest.getPage(),
                                     pageInfoRequest.getLimit(), pageInfoRequest.getOrderBy(), pageInfoRequest.getSort()
                                    );

        List<StockResultEntity> stockResultEntityList =
                        stockResultListGetService.execute(stockResultSearchForDaoConditionDto);
        InventoryResultListResponse inventoryResultListResponse =
                        inventoryHelper.toInventoryResultListResponse(stockResultEntityList);

        // ページ情報レスポンスを設定
        PageInfoResponse pageInfoResponse = new PageInfoResponse();
        try {
            pageInfoModule.setupResponsePager(stockResultSearchForDaoConditionDto, pageInfoResponse);
        } catch (InvocationTargetException | IllegalAccessException e) {
            LOGGER.info("ページ情報レスポンスの設定異常： " + e.getMessage());
        }
        inventoryResultListResponse.setPageInfo(pageInfoResponse);

        return new ResponseEntity<>(inventoryResultListResponse, HttpStatus.OK);
    }

    /**
     * GET /shippings/inventories/goods-status : 在庫状況表示取得
     * 在庫状況表示取得
     *
     * @param inventoryStatusDisplayGetRequest 在庫状況表示取得リクエスト (required)
     * @return 在庫状況表示レスポンス (status code 200)
     * or その他エラー (status code 500)
     */
    @Override
    public ResponseEntity<InventoryStatusDisplayResponse> getGoodsStatus(
                    @NotNull @ApiParam(value = "在庫状況表示取得リクエスト", required = true) @Valid
                                    InventoryStatusDisplayGetRequest inventoryStatusDisplayGetRequest) {

        StockStatusDisplayConditionDto condition =
                        ApplicationContextUtility.getBean(StockStatusDisplayConditionDto.class);
        // 公開状態
        HTypeOpenDeleteStatus openStatus = null;
        // 公開開始日
        Timestamp openStartTime = null;
        // 公開終了日
        Timestamp openEndTime = null;

        condition = inventoryHelper.toStockStatusDisplayConditionDto(inventoryStatusDisplayGetRequest);
        openStatus = EnumTypeUtil.getEnumFromValue(HTypeOpenDeleteStatus.class,
                                                   inventoryStatusDisplayGetRequest.getOpenStatus()
                                                  );
        openStartTime = conversionUtility.toTimestamp(inventoryStatusDisplayGetRequest.getOpenStartTime());
        openEndTime = conversionUtility.toTimestamp(inventoryStatusDisplayGetRequest.getOpenEndTime());

        HTypeStockStatusType currentStatus =
                        stockStatusDisplayGetRealStatusLogic.execute(condition, openStatus, openStartTime, openEndTime);

        InventoryStatusDisplayResponse inventoryStatusDisplayResponse =
                        inventoryHelper.toInventoryStatusDisplayResponse(EnumTypeUtil.getValue(currentStatus));

        return new ResponseEntity<>(inventoryStatusDisplayResponse, HttpStatus.OK);
    }

    /**  ここまでボトムアップ定義のコントローラーメソッド */

}