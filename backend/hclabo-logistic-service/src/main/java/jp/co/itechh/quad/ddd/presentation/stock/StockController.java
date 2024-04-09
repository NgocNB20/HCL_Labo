package jp.co.itechh.quad.ddd.presentation.stock;

import io.swagger.annotations.ApiParam;
import jp.co.itechh.quad.core.base.utility.ConversionUtility;
import jp.co.itechh.quad.core.dto.goods.stock.StockDto;
import jp.co.itechh.quad.core.entity.goods.stock.StockSettingEntity;
import jp.co.itechh.quad.core.logic.goods.stock.StockListGetLogic;
import jp.co.itechh.quad.core.logic.goods.stock.StockResultRegistByStockManagementFlagLogic;
import jp.co.itechh.quad.core.logic.goods.stock.StockSettingUpdateStockManagementFlagLogic;
import jp.co.itechh.quad.core.service.goods.stock.StockRegistUpdateService;
import jp.co.itechh.quad.core.web.AbstractController;
import jp.co.itechh.quad.core.web.HeaderParamsUtility;
import jp.co.itechh.quad.stock.presentation.api.StockApi;
import jp.co.itechh.quad.stock.presentation.api.param.GoodsRequest;
import jp.co.itechh.quad.stock.presentation.api.param.StockDetailListGetRequest;
import jp.co.itechh.quad.stock.presentation.api.param.StockDetailListResponse;
import jp.co.itechh.quad.stock.presentation.api.param.StockRegistUpdateRequest;
import jp.co.itechh.quad.stock.presentation.api.param.StockSettingUpdateManagementFlagRequest;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.RestController;

import javax.validation.Valid;
import javax.validation.constraints.NotNull;
import java.util.List;

/**
 * 住所録エンドポイント Controller
 *
 * @author PHAM QUANG DIEU
 */
@RestController
public class StockController extends AbstractController implements StockApi {

    /** ロガー */
    private static final Logger LOGGER = LoggerFactory.getLogger(StockController.class);

    /** 在庫登録更新サービス */
    private final StockRegistUpdateService stockRegistUpdateService;

    /** 在庫一覧取得 */
    private final StockListGetLogic stockListGetLogic;

    /** 在庫管理フラグ変更の入庫実績を登録 */
    private final StockResultRegistByStockManagementFlagLogic stockResultRegistByStockManagementFlagLogic;

    /** 在庫Helperクラス */
    private final StockHelper stockHelper;

    /** ヘッダパラメーターユーティル */
    private final HeaderParamsUtility headerParamsUtil;

    /** 変換ユーティリティクラス */
    private final ConversionUtility conversionUtility;

    /** 変換ユーティリティクラス */
    private final StockSettingUpdateStockManagementFlagLogic stockSettingUpdateStockManagementFlagLogic;

    /** コンストラクタ */
    @Autowired
    public StockController(StockListGetLogic stockListGetLogic,
                           StockResultRegistByStockManagementFlagLogic stockResultRegistByStockManagementFlagLogic,
                           StockRegistUpdateService stockRegistUpdateService,
                           StockHelper stockHelper,
                           HeaderParamsUtility headerParamsUtil,
                           ConversionUtility conversionUtility,
                           StockSettingUpdateStockManagementFlagLogic stockSettingUpdateStockManagementFlagLogic) {
        this.stockListGetLogic = stockListGetLogic;
        this.stockResultRegistByStockManagementFlagLogic = stockResultRegistByStockManagementFlagLogic;
        this.stockRegistUpdateService = stockRegistUpdateService;
        this.stockHelper = stockHelper;
        this.headerParamsUtil = headerParamsUtil;
        this.conversionUtility = conversionUtility;
        this.stockSettingUpdateStockManagementFlagLogic = stockSettingUpdateStockManagementFlagLogic;
    }

    /**
     * GET /stock/details : 在庫詳細一覧取得
     * 在庫詳細一覧取得
     *
     * @param stockDetailListGetRequest 在庫詳細一覧取得リクエスト (required)
     * @return 在庫詳細一覧レスポンス (status code 200)
     * or その他エラー (status code 500)
     */
    @Override
    public ResponseEntity<StockDetailListResponse> getDetails(
                    @NotNull @ApiParam(value = "在庫詳細一覧取得リクエスト", required = true) @Valid
                                    StockDetailListGetRequest stockDetailListGetRequest) {

        List<StockDto> stockDetailList = stockListGetLogic.execute(stockDetailListGetRequest.getGoodsSeqList());

        StockDetailListResponse StockDetailListResponse = stockHelper.toStockDetailListResponse(stockDetailList);

        return new ResponseEntity<>(StockDetailListResponse, HttpStatus.OK);
    }

    /**
     * POST /stock : 在庫登録更新
     * 商品サービスの「商品在庫表示」テーブルへの連携は行わない。商品サービス側は同期処理で本APIを呼び出し、APIレスポンスで「商品在庫表示」テーブルを更新すること
     *
     * @param stockRegistUpdateRequest 在庫登録更新リクエスト (required)
     * @return 在庫詳細一覧レスポンス (status code 200)
     * or Bad Request（バリデーションエラー、業務ルールエラー、妥当性チェックエラーの場合） (status code 400)
     * or Internal Server Error（アサートエラー、予想外エラーの場合） (status code 500)
     * or その他エラー (status code 200)
     */
    @Override
    public ResponseEntity<StockDetailListResponse> registUpdate(
                    @Valid StockRegistUpdateRequest stockRegistUpdateRequest) {

        List<StockSettingEntity> stockSettingEntityList =
                        this.stockHelper.toStockSettingEntityList(stockRegistUpdateRequest);
        List<StockDto> stockDtoList = this.stockHelper.toStockDtoList(stockRegistUpdateRequest,
                                                                      this.conversionUtility.toInteger(
                                                                                      this.headerParamsUtil.getAdministratorSeq())
                                                                     );
        // 新規トランザクションで処理
        List<Integer> goodsSeqResultList = this.stockRegistUpdateService.execute(stockSettingEntityList, stockDtoList);

        List<StockDto> stockDetailList = stockListGetLogic.execute(goodsSeqResultList);

        StockDetailListResponse stockDetailListResponse = stockHelper.toStockDetailListResponse(stockDetailList);

        return new ResponseEntity<>(stockDetailListResponse, HttpStatus.OK);
    }

    /**
     * POST /stock/supplement/history : 在庫管理フラグ変更の入庫実績を登録
     *
     * @param goodsRequest 商品リクエスト (required)
     * @return 成功 (status code 200)
     * or システムエラー (status code 200)
     */
    @Override
    public ResponseEntity<Integer> supplementHistory(@Valid GoodsRequest goodsRequest) {
        int shopSeq = 1001;
        Integer administratorSeq = this.conversionUtility.toInteger(this.headerParamsUtil.getAdministratorSeq());
        int result = stockResultRegistByStockManagementFlagLogic.execute(administratorSeq, shopSeq,
                                                                         goodsRequest.getGoodsSeq(),
                                                                         goodsRequest.getStockManagementFlag()
                                                                        );
        return new ResponseEntity<>(result, HttpStatus.OK);
    }

    /**
     * POST /stock/setting/management-flag : 在庫設定登在庫管理フラグ更新録
     * 在庫管理フラグ更新
     *
     * @param stockSettingUpdateManagementFlagRequest 在庫管理フラグ更新リクエスト (required)
     * @return 住所レスポンス (status code 200)
     * or Bad Request（バリデーションエラー、業務ルールエラー、妥当性チェックエラーの場合） (status code 400)
     * or Internal Server Error（アサートエラー、予想外エラーの場合） (status code 500)
     * or その他エラー (status code 200)
     */
    @Override
    public ResponseEntity<Integer> stockSettingUpdateManagementFlag(
                    @Valid StockSettingUpdateManagementFlagRequest stockSettingUpdateManagementFlagRequest) {
        Integer result = stockSettingUpdateStockManagementFlagLogic.execute(
                        stockSettingUpdateManagementFlagRequest.getGoodsSeq(),
                        stockSettingUpdateManagementFlagRequest.getStockManagementFlag()
                                                                           );
        return new ResponseEntity<>(result, HttpStatus.OK);
    }
}