/*
 * Project Name : HIT-MALL4
 */

package jp.co.itechh.quad.admin.pc.web.admin.goods.stock;

import jp.co.itechh.quad.admin.base.utility.ApplicationContextUtility;
import jp.co.itechh.quad.admin.base.utility.ConversionUtility;
import jp.co.itechh.quad.admin.constant.type.HTypeStockManagementFlag;
import jp.co.itechh.quad.admin.dto.goods.stock.StockResultSearchForDaoConditionDto;
import jp.co.itechh.quad.admin.entity.goods.stock.StockResultEntity;
import jp.co.itechh.quad.admin.util.common.EnumTypeUtil;
import jp.co.itechh.quad.inventory.presentation.api.param.InventoryResultListGetRequest;
import jp.co.itechh.quad.inventory.presentation.api.param.InventoryResultListResponse;
import jp.co.itechh.quad.inventory.presentation.api.param.InventoryResultResponse;
import jp.co.itechh.quad.product.presentation.api.param.ProductDetailsResponse;
import org.springframework.stereotype.Component;

import java.sql.Timestamp;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.List;

/**
 * 在庫詳細Helper<br/>
 *
 * @author Pham Quang Dieu (VTI Japan Co., Ltd.)
 */
@Component
public class StockDetailsHelper {

    /** 変換ユーティリティクラス */
    private final ConversionUtility conversionUtility;

    /** 日付のフォーマット */
    private static final DateTimeFormatter FORMATTER = DateTimeFormatter.ofPattern("yyyy/MM/dd HH:mm:ss");

    /**
     * コンストラクタ
     *
     * @param conversionUtility
     */
    public StockDetailsHelper(ConversionUtility conversionUtility) {
        this.conversionUtility = conversionUtility;
    }

    /**
     * 入庫実績リスト取得条件の作成<br/>
     *
     * @param stockDetailsModel 在庫詳細ページ
     * @return 入庫実績Dao用検索条件DTO
     */
    public StockResultSearchForDaoConditionDto toStockResultSearchForDaoConditionDtoForSearch(StockDetailsModel stockDetailsModel) {

        StockResultSearchForDaoConditionDto stockResultSearchForDaoConditionDto =
                        ApplicationContextUtility.getBean(StockResultSearchForDaoConditionDto.class);

        // 入庫実績
        stockResultSearchForDaoConditionDto.setGoodsSeq(stockDetailsModel.getGoodsSeq());

        return stockResultSearchForDaoConditionDto;
    }

    /**
     * 在庫詳細画面編集<br/>
     *
     * @param stockResultEntityList 入庫実績エンティティリスト
     * @param productDetailsResponse   商品詳細レスポンスクラス
     * @param stockDetailsModel 在庫詳細ページ
     */
    public void toPageForStockResult(List<StockResultEntity> stockResultEntityList,
                                     ProductDetailsResponse productDetailsResponse,
                                     StockDetailsModel stockDetailsModel) {

        // 入庫実績画面設定
        List<StockDetailsModelItem> resultItemList = new ArrayList<>();
        for (StockResultEntity stockResultEntity : stockResultEntityList) {
            StockDetailsModelItem detailsPageItem = ApplicationContextUtility.getBean(StockDetailsModelItem.class);
            detailsPageItem.setResultSupplementTime(stockResultEntity.getSupplementTime());
            detailsPageItem.setResultPersonSeq(stockResultEntity.getProcessPersonName());
            detailsPageItem.setResultNote(stockResultEntity.getNote());
            detailsPageItem.setResultRealStock(stockResultEntity.getRealStock());
            detailsPageItem.setResultSupplementCount(stockResultEntity.getSupplementCount());
            detailsPageItem.setResultSupplementTimeStr(convertTime(stockResultEntity.getSupplementTime()));
            resultItemList.add(detailsPageItem);
        }

        stockDetailsModel.setStockResultItems(resultItemList);

        // 商品詳細画面設定
        stockDetailsModel.setGoodsGroupCode(productDetailsResponse.getGoodsGroupCode());
        stockDetailsModel.setGoodsCode(productDetailsResponse.getGoodsCode());
        stockDetailsModel.setUnitTitle1(productDetailsResponse.getUnitTitle1());
        stockDetailsModel.setUnitTitle2(productDetailsResponse.getUnitTitle2());
        stockDetailsModel.setUnitValue1(productDetailsResponse.getUnitValue1());
        stockDetailsModel.setUnitValue2(productDetailsResponse.getUnitValue2());
    }

    /**
     * 入庫実績一覧リクエストクラスに変換<br/>
     *
     * @param stockDetailsModel 在庫詳細ページ
     * @return 入庫実績一覧リクエスト
     */
    public InventoryResultListGetRequest toInventoryResultListGetRequestFromStockDetailsModel(StockDetailsModel stockDetailsModel) {

        InventoryResultListGetRequest inventoryResultListGetRequest = new InventoryResultListGetRequest();

        inventoryResultListGetRequest.setGoodsSeq(stockDetailsModel.getGoodsSeq());

        return inventoryResultListGetRequest;
    }

    /**
     * 入庫実績エンティティリストクラスに変換<br/>
     *
     * @param inventoryResultListResponse 入庫実績一覧レスポンス
     * @return 入庫実績エンティティリスト
     */
    public List<StockResultEntity> toStockResultEntitiesFromInventoryResultListResponse(InventoryResultListResponse inventoryResultListResponse) {

        List<InventoryResultResponse> inventoryResultList = inventoryResultListResponse.getInventoryResultList();
        List<StockResultEntity> stockResultEntities = new ArrayList<>();

        for (InventoryResultResponse inventoryResultResponse : inventoryResultList) {

            StockResultEntity stockResultEntity = new StockResultEntity();

            stockResultEntity.setStockResultSeq(inventoryResultResponse.getStockResultSeq());
            stockResultEntity.setGoodsSeq(inventoryResultResponse.getGoodsSeq());
            stockResultEntity.setSupplementTime(
                            conversionUtility.toTimestamp(inventoryResultResponse.getSupplementTime()));
            stockResultEntity.setSupplementCount(inventoryResultResponse.getSupplementCount());
            stockResultEntity.setRealStock(inventoryResultResponse.getRealStock());
            stockResultEntity.setProcessPersonName(inventoryResultResponse.getProcessPersonName());
            stockResultEntity.setNote(inventoryResultResponse.getNote());
            stockResultEntity.setStockManagementFlag(EnumTypeUtil.getEnumFromValue(HTypeStockManagementFlag.class,
                                                                                   inventoryResultResponse.getStockManagementFlag()
                                                                                  ));
            stockResultEntity.setRegistTime(conversionUtility.toTimestamp(inventoryResultResponse.getRegistTime()));
            stockResultEntity.setUpdateTime(conversionUtility.toTimestamp(inventoryResultResponse.getUpdateTime()));

            stockResultEntities.add(stockResultEntity);
        }

        return stockResultEntities;
    }

    /**
     * 日付のフォーマット
     *
     * @param  ts
     * @return String formattedDate
     */
    private String convertTime(Timestamp ts) {
        return ts.toLocalDateTime().format(FORMATTER);
    }

}