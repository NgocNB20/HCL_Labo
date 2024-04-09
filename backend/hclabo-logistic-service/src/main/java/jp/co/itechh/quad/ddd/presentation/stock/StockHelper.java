package jp.co.itechh.quad.ddd.presentation.stock;

import jp.co.itechh.quad.core.base.utility.ApplicationContextUtility;
import jp.co.itechh.quad.core.constant.type.HTypeStockManagementFlag;
import jp.co.itechh.quad.core.dto.goods.stock.StockDto;
import jp.co.itechh.quad.core.entity.goods.stock.StockSettingEntity;
import jp.co.itechh.quad.core.util.common.EnumTypeUtil;
import jp.co.itechh.quad.core.utility.AdministratorUtility;
import jp.co.itechh.quad.stock.presentation.api.param.StockDetailListResponse;
import jp.co.itechh.quad.stock.presentation.api.param.StockDetailsResponse;
import jp.co.itechh.quad.stock.presentation.api.param.StockRegistUpdateRequest;
import org.apache.commons.collections.CollectionUtils;
import org.apache.commons.lang3.ObjectUtils;
import org.apache.commons.lang3.StringUtils;
import org.springframework.stereotype.Component;

import java.sql.Timestamp;
import java.util.ArrayList;
import java.util.List;

/**
 * 在庫 Helper
 */
@Component
public class StockHelper {

    /**
     * StockDtoをレスポンスに変換
     *
     * @param stockDetailsList 在庫詳細Dtoリスト
     * @return 在庫詳細一覧レスポンス
     */
    public StockDetailListResponse toStockDetailListResponse(List<StockDto> stockDetailsList) {

        StockDetailListResponse stockDetailListResponse = new StockDetailListResponse();
        List<StockDetailsResponse> stockDetailsResponses = new ArrayList<>();

        for (StockDto stockDto : stockDetailsList) {
            StockDetailsResponse StockDetailsResponse = this.toStockResponse(stockDto);
            stockDetailsResponses.add(StockDetailsResponse);
        }

        stockDetailListResponse.setStockDetailsList(stockDetailsResponses);

        return stockDetailListResponse;
    }

    /**
     * レスポンスに変換
     *
     * @param stockDto 在庫DTO
     * @return 商品グループ検索条件
     */
    private StockDetailsResponse toStockResponse(StockDto stockDto) {
        StockDetailsResponse stockResponse = new StockDetailsResponse();

        stockResponse.setGoodsSeq(stockDto.getGoodsSeq());
        stockResponse.setSalesPossibleStock(stockDto.getSalesPossibleStock());
        stockResponse.setRealStock(stockDto.getRealStock());
        stockResponse.setOrderReserveStock(stockDto.getOrderReserveStock());
        stockResponse.setRemainderFewStock(stockDto.getRemainderFewStock());
        stockResponse.setOrderPointStock(stockDto.getOrderPointStock());
        stockResponse.setSafetyStock(stockDto.getSafetyStock());

        return stockResponse;
    }

    /**
     * 在庫登録更新リクエストを在庫設定エンティティリストに変換
     *
     * @param stockRegistUpdateRequest 在庫登録更新リクエスト
     * @return List<StockSettingEntity>
     */
    public List<StockSettingEntity> toStockSettingEntityList(StockRegistUpdateRequest stockRegistUpdateRequest) {

        if (ObjectUtils.isEmpty(stockRegistUpdateRequest) || CollectionUtils.isEmpty(
                        stockRegistUpdateRequest.getGoodsStockInfoList())) {
            return null;
        }

        List<StockSettingEntity> stockSettingEntityList = new ArrayList<>();
        stockRegistUpdateRequest.getGoodsStockInfoList().forEach(item -> {
            StockSettingEntity stockSettingEntity = new StockSettingEntity();
            stockSettingEntity.setGoodsSeq(item.getGoodsSeq());
            stockSettingEntity.setShopSeq(1001);
            stockSettingEntity.setRemainderFewStock(item.getRemainderFewStock());
            stockSettingEntity.setOrderPointStock(item.getOrderPointStock());
            stockSettingEntity.setSafetyStock(item.getSafetyStock());
            stockSettingEntity.setStockManagementFlag(item.getStockManagementFlag());
            stockSettingEntityList.add(stockSettingEntity);
        });
        return stockSettingEntityList;
    }

    /**
     * 在庫登録更新リクエストを在庫Dtoリストに変換
     *
     * @param stockRegistUpdateRequest 在庫登録更新リクエスト
     * @param administratorSeq         管理者SEQ
     * @return List<StockDto>
     */
    public List<StockDto> toStockDtoList(StockRegistUpdateRequest stockRegistUpdateRequest, Integer administratorSeq) {

        if (ObjectUtils.isEmpty(stockRegistUpdateRequest) || CollectionUtils.isEmpty(
                        stockRegistUpdateRequest.getGoodsStockInfoList())) {
            return null;
        }

        AdministratorUtility administratorUtility = ApplicationContextUtility.getBean(AdministratorUtility.class);

        List<StockDto> stockDtoList = new ArrayList<>();
        stockRegistUpdateRequest.getGoodsStockInfoList().forEach(item -> {
            StockDto stockDto = new StockDto();
            stockDto.setGoodsSeq(item.getGoodsSeq());
            if (StringUtils.isNotBlank(item.getStockManagementFlag())) {
                stockDto.setStockManagementFlag(EnumTypeUtil.getEnumFromValue(HTypeStockManagementFlag.class,
                                                                              item.getStockManagementFlag()
                                                                             ));
            }
            stockDto.setShopSeq(1001);
            stockDto.setRemainderFewStock(item.getRemainderFewStock());
            stockDto.setOrderPointStock(item.getOrderPointStock());
            stockDto.setSafetyStock(item.getSafetyStock());
            stockDto.setSupplementCount(item.getSupplementCount());
            if (ObjectUtils.isNotEmpty(item.getSupplementTime())) {
                stockDto.setSupplementTime(new Timestamp(item.getSupplementTime().getTime()));
            }
            stockDto.setProcessPersonName(administratorUtility.getAdministratorName(administratorSeq));
            stockDtoList.add(stockDto);
        });
        return stockDtoList;
    }

}