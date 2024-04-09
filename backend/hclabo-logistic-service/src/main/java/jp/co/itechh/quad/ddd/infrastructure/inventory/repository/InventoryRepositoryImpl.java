/*
 * Copyright (C) 2022 i-TEC HANKYU HANSHIN INC. All Rights Reserved.
 */

package jp.co.itechh.quad.ddd.infrastructure.inventory.repository;

import jp.co.itechh.quad.core.base.util.common.ArgumentCheckUtil;
import jp.co.itechh.quad.core.base.utility.ConversionUtility;
import jp.co.itechh.quad.core.dao.goods.stock.StockDao;
import jp.co.itechh.quad.ddd.domain.inventory.repository.IInventoryRepository;
import jp.co.itechh.quad.product.presentation.api.ProductApi;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

/**
 * 在庫リポジトリ実装クラス
 *
 * @author kimura
 */
@Component
public class InventoryRepositoryImpl implements IInventoryRepository {

    /** 在庫Dao */
    private final StockDao stockDao;

    /** 変換Helper */
    private final ConversionUtility conversionUtility;

    /** コンストラクタ */
    @Autowired
    public InventoryRepositoryImpl(StockDao stockDao, ConversionUtility conversionUtility, ProductApi productApi) {
        this.stockDao = stockDao;
        this.conversionUtility = conversionUtility;
    }

    /**
     * 在庫引当<br/>
     *
     * @param itemId    商品ID（goodsSeq）
     * @param itemCount 商品数量
     * @return 更新件数
     */
    @Override
    public int secureStock(String itemId, int itemCount) {
        // 在庫引当更新処理
        return stockDao.updateStock(this.conversionUtility.toInteger(itemId), itemCount);
    }

    /**
     * 在庫引当戻し<br/>
     *
     * @param itemId    商品ID（goodsSeq）
     * @param itemCount 商品数量
     * @return 更新件数
     */
    @Override
    public int updateSecuredStockRollback(String itemId, int itemCount) {
        // 在庫引当戻し更新処理
        return stockDao.updateStockRollback(this.conversionUtility.toInteger(itemId), itemCount);
    }

    /**
     * 在庫出荷<br/>
     *
     * @param itemId    商品ID（goodsSeq）
     * @param itemCount 商品数量
     * @return 更新件数
     */
    @Override
    public int updateStockShipping(String itemId, int itemCount) {
        // 在庫出荷更新処理
        return stockDao.updateStockShipment(this.conversionUtility.toInteger(itemId), itemCount);
    }

    /**
     * 出荷後在庫戻し<br/>
     * ※在庫管理商品の場合、在庫を戻す（stockDao.updateStockShipmentRollback）
     *
     * @param itemId    商品ID（goodsSeq）
     * @param itemCount 商品数量
     * @return 更新件数
     */
    @Override
    public int updateStockShippingRollback(String itemId, int itemCount) {
        ArgumentCheckUtil.assertNotNull("itemId", itemId);
        return stockDao.updateStockShipmentRollback(this.conversionUtility.toInteger(itemId), itemCount);
    }

}