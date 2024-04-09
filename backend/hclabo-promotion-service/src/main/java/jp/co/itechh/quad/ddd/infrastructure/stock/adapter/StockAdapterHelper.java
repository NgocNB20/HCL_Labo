package jp.co.itechh.quad.ddd.infrastructure.stock.adapter;

import jp.co.itechh.quad.core.base.util.common.CollectionUtil;
import jp.co.itechh.quad.core.base.utility.ConversionUtility;
import jp.co.itechh.quad.core.dto.goods.stock.StockDto;
import jp.co.itechh.quad.stock.presentation.api.param.StockDetailListResponse;
import jp.co.itechh.quad.stock.presentation.api.param.StockDetailsResponse;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

/**
 * 在庫アダプターHelperクラス
 *
 * @author yt23807
 */
@Component
public class StockAdapterHelper {

    /** 変換Helper */
    private final ConversionUtility conversionUtility;

    /**
     * コンストラクタ
     *
     * @param conversionUtility
     */
    @Autowired
    public StockAdapterHelper(ConversionUtility conversionUtility) {
        this.conversionUtility = conversionUtility;
    }

    /**
     * 文字型に変換
     *
     * @param stringList
     * @return
     */
    public List<Integer> toIntegerList(List<String> stringList) {
        return stringList.stream().map(conversionUtility::toInteger).collect(Collectors.toList());
    }

    /**
     * 在庫DTOリストに変換
     *
     * @param stockDetailsResponseList 在庫詳細レスポンスクラスリスト
     * @return 在庫DTOリスト
     */
    public List<StockDto> toStockList(StockDetailListResponse stockDetailListResponse) {

        if (stockDetailListResponse == null || CollectionUtil.isEmpty(stockDetailListResponse.getStockDetailsList())) {
            return null;
        }
        List<StockDetailsResponse> stockDetailsResponseList = stockDetailListResponse.getStockDetailsList();

        List<StockDto> stockDtoList = new ArrayList<>();
        stockDetailsResponseList.forEach(stockResponse -> {
            StockDto stockDto = new StockDto();
            stockDto.setGoodsSeq(stockResponse.getGoodsSeq());
            stockDto.setShopSeq(1001);
            stockDto.setSalesPossibleStock(stockResponse.getSalesPossibleStock());
            stockDto.setRealStock(stockResponse.getRealStock());
            stockDto.setOrderReserveStock(stockResponse.getOrderReserveStock());
            stockDto.setRemainderFewStock(stockResponse.getRemainderFewStock());
            stockDto.setOrderPointStock(stockResponse.getOrderPointStock());
            stockDto.setSafetyStock(stockResponse.getSafetyStock());
            stockDtoList.add(stockDto);
        });

        return stockDtoList;
    }
}