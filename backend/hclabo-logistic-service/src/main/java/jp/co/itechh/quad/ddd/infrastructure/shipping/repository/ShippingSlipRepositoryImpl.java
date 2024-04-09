/*
 * Copyright (C) 2022 i-TEC HANKYU HANSHIN INC. All Rights Reserved.
 */

package jp.co.itechh.quad.ddd.infrastructure.shipping.repository;

import jp.co.itechh.quad.ddd.domain.shipping.entity.ShippingSlipEntity;
import jp.co.itechh.quad.ddd.domain.shipping.repository.IShippingSlipRepository;
import jp.co.itechh.quad.ddd.domain.shipping.valueobject.ShippingSlipId;
import jp.co.itechh.quad.ddd.infrastructure.shipping.dao.SecuredShippingItemDao;
import jp.co.itechh.quad.ddd.infrastructure.shipping.dao.ShippingSlipDao;
import jp.co.itechh.quad.ddd.infrastructure.shipping.dbentity.SecuredShippingItemDbEntity;
import jp.co.itechh.quad.ddd.infrastructure.shipping.dbentity.ShippingSlipDbEntity;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.util.CollectionUtils;
import org.springframework.util.ObjectUtils;

import java.sql.Timestamp;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

/**
 * 配送伝票リポジトリ実装クラス
 *
 * @author kimura
 */
@Component
public class ShippingSlipRepositoryImpl implements IShippingSlipRepository {

    /**
     * 配送伝票Daoクラス
     */
    private final ShippingSlipDao shippingSlipDao;

    /**
     * 配送商品Daoクラス
     */
    private final SecuredShippingItemDao securedShippingItemDao;

    /**
     * 注文票リポジトリHelperクラス
     */
    private final ShippingSlipRepositoryHelper shippingSlipRepositoryHelper;

    /**
     * コンストラクタ
     *
     * @param shippingSlipDao              配送伝票Daoクラス
     * @param securedShippingItemDao       配送商品Daoクラス
     * @param shippingSlipRepositoryHelper 注文票リポジトリHelperクラス
     */
    @Autowired
    public ShippingSlipRepositoryImpl(ShippingSlipDao shippingSlipDao,
                                      SecuredShippingItemDao securedShippingItemDao,
                                      ShippingSlipRepositoryHelper shippingSlipRepositoryHelper) {
        this.shippingSlipDao = shippingSlipDao;
        this.securedShippingItemDao = securedShippingItemDao;
        this.shippingSlipRepositoryHelper = shippingSlipRepositoryHelper;
    }

    /**
     * 配送伝票登録
     *
     * @param shippingSlipEntity 配送伝票
     */
    @Override
    public void save(ShippingSlipEntity shippingSlipEntity) {

        ShippingSlipDbEntity shippingSlipDbEntity =
                        shippingSlipRepositoryHelper.toShippingSlipDbEntityFromShippingSlipEntity(shippingSlipEntity);
        shippingSlipDao.insert(shippingSlipDbEntity);

        List<SecuredShippingItemDbEntity> securedShippingItemDbEntities =
                        shippingSlipRepositoryHelper.toShippingItemDbEntity(
                                        shippingSlipEntity.getSecuredShippingItemList());
        for (SecuredShippingItemDbEntity securedShippingItemDbEntity : securedShippingItemDbEntities) {
            securedShippingItemDbEntity.setShippingSlipId(shippingSlipEntity.getShippingSlipId().getValue());
            securedShippingItemDao.insert(securedShippingItemDbEntity);
        }

    }

    /**
     * 配送伝票更新
     *
     * @param shippingSlipEntity 配送伝票
     * @return 更新件数
     */
    @Override
    public int update(ShippingSlipEntity shippingSlipEntity) {

        ShippingSlipDbEntity shippingSlipDbEntity =
                        shippingSlipRepositoryHelper.toShippingSlipDbEntityFromShippingSlipEntity(shippingSlipEntity);
        int result = shippingSlipDao.update(shippingSlipDbEntity);

        List<SecuredShippingItemDbEntity> shippingItemListRequest = shippingSlipRepositoryHelper.toShippingItemDbEntity(
                        shippingSlipEntity.getSecuredShippingItemList());

        // 配送伝票IDによる分割した配送商品リスト（確保済み配送商品）を削除する
        this.securedShippingItemDao.deleteByShippingSlipId(shippingSlipEntity.getShippingSlipId().getValue());

        for (SecuredShippingItemDbEntity shippingItemRequest : shippingItemListRequest) {

            shippingItemRequest.setShippingSlipId(shippingSlipEntity.getShippingSlipId().getValue());

            // 確保済み配送商品に登録する
            this.securedShippingItemDao.insert(shippingItemRequest);
        }

        return result;
    }

    /**
     * 配送伝票取得
     *
     * @param shippingSlipId 配送伝票ID
     * @return ShippingSlipEntity 配送伝票
     */
    @Override
    public ShippingSlipEntity get(ShippingSlipId shippingSlipId) {

        ShippingSlipDbEntity shippingSlipDbEntity = shippingSlipDao.getByShippingSlipId(shippingSlipId.getValue());

        List<SecuredShippingItemDbEntity> shippingItemDbEntities =
            securedShippingItemDao.getByShippingSlipId(shippingSlipDbEntity.getShippingSlipId());

        ShippingSlipEntity shippingSlipEntity =
                    shippingSlipRepositoryHelper.toShippingSlipEntityFromShippingSlipDbEntity(shippingSlipDbEntity, shippingItemDbEntities);

        return shippingSlipEntity;

    }

    /**
     * 取引IDで配送伝票取得
     *
     * @param transactionId 取引ID
     * @return ShippingSlipEntity 配送伝票
     */
    @Override
    public ShippingSlipEntity getByTransactionId(String transactionId) {

        ShippingSlipDbEntity shippingSlipDbEntity = shippingSlipDao.getByTransactionId(transactionId);
        if (ObjectUtils.isEmpty(shippingSlipDbEntity)) {
            return null;
        }

        List<SecuredShippingItemDbEntity> shippingItemDbEntities =
            securedShippingItemDao.getByShippingSlipId(shippingSlipDbEntity.getShippingSlipId());

        ShippingSlipEntity shippingSlipEntity =
                        shippingSlipRepositoryHelper.toShippingSlipEntityFromShippingSlipDbEntity(shippingSlipDbEntity,
                            shippingItemDbEntities);

        return shippingSlipEntity;

    }

    /**
     * 一定期間が経過した在庫確保状態の配送伝票リスト取得
     *
     * @param thresholdTime 期間閾値
     * @return ShippingSlipEntityList 配送伝票
     */
    @Override
    public List<ShippingSlipEntity> getSecuredInventoryShippingSlipListTargetElapsedPeriod(Timestamp thresholdTime) {

        List<ShippingSlipDbEntity> shippingSlipDbEntityList =
                        this.shippingSlipDao.getSecuredInventoryShippingSlipListTargetElapsedPeriod(thresholdTime);

        List<ShippingSlipEntity> entityList = new ArrayList<>();

        if (CollectionUtils.isEmpty(shippingSlipDbEntityList)) {
            return entityList;
        }

        for (ShippingSlipDbEntity dbEntity : shippingSlipDbEntityList) {

            List<SecuredShippingItemDbEntity> shippingItemDbEntities =
                securedShippingItemDao.getByShippingSlipId(dbEntity.getShippingSlipId());

            entityList.add(
                shippingSlipRepositoryHelper.toShippingSlipEntityFromShippingSlipDbEntity(dbEntity, shippingItemDbEntities));
        }

        return entityList;
    }

    /**
     * 不要データを削除する
     *
     * @param transactionId 取引ID
     */
    @Override
    public int deleteUnnecessaryByTransactionId(String transactionId) {

        securedShippingItemDao.deleteSecuredShippingItem(transactionId);
        int result = shippingSlipDao.deleteShippingSlip(transactionId);

        return result;
    }
}