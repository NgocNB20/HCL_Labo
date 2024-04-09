package jp.co.itechh.quad.ddd.infrastructure.sales.repository;

import jp.co.itechh.quad.ddd.domain.sales.entity.SalesSlipForRevisionEntity;
import jp.co.itechh.quad.ddd.domain.sales.repository.ISalesSlipForRevisionRepository;
import jp.co.itechh.quad.ddd.domain.sales.valueobject.SalesSlipRevisionId;
import jp.co.itechh.quad.ddd.infrastructure.sales.dao.AdjustmentAmountForRevisionDao;
import jp.co.itechh.quad.ddd.infrastructure.sales.dao.ItemPurchasePriceSubTotalForRevisionDao;
import jp.co.itechh.quad.ddd.infrastructure.sales.dao.SalesSlipForRevisionDao;
import jp.co.itechh.quad.ddd.infrastructure.sales.dbentity.AdjustmentAmountForRevisionDbEntity;
import jp.co.itechh.quad.ddd.infrastructure.sales.dbentity.ItemPurchasePriceSubTotalForRevisionDbEntity;
import jp.co.itechh.quad.ddd.infrastructure.sales.dbentity.SalesSlipForRevisionDbEntity;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.util.CollectionUtils;

import java.util.HashSet;
import java.util.List;
import java.util.Set;

/**
 * 改訂用販売伝票リポジトリ実装クラス
 *
 * @author kimura
 */
@Component
public class SalesSlipForRevisionRepositoryImpl implements ISalesSlipForRevisionRepository {

    /** 販売伝票エンドポイントDaoクラス */
    private final SalesSlipForRevisionDao salesSlipForRevisionDao;

    /** 改訂用クーポンDaoクラス */
    private final AdjustmentAmountForRevisionDao adjustmentAmountForRevisionDao;

    /** 改訂用商品購入価格小計 Daoクラス */
    private final ItemPurchasePriceSubTotalForRevisionDao itemPurchasePriceSubTotalForRevisionDao;

    /** 改訂用販売伝票 リポジトリHelperクラス */
    private final SalesSlipForRevisionRepositoryHelper salesSlipForRevisionRepositoryHelper;

    /**
     * コンストラクタ
     *
     * @param salesSlipForRevisionDao                 販売伝票エンドポイントDaoクラス
     * @param adjustmentAmountForRevisionDao          改訂用クーポンDaoクラス
     * @param itemPurchasePriceSubTotalForRevisionDao 改訂用商品購入価格小計 Daoクラス
     * @param salesSlipForRevisionRepositoryHelper    改訂用販売伝票 リポジトリHelperクラス
     */
    @Autowired
    public SalesSlipForRevisionRepositoryImpl(SalesSlipForRevisionDao salesSlipForRevisionDao,
                                              AdjustmentAmountForRevisionDao adjustmentAmountForRevisionDao,
                                              ItemPurchasePriceSubTotalForRevisionDao itemPurchasePriceSubTotalForRevisionDao,
                                              SalesSlipForRevisionRepositoryHelper salesSlipForRevisionRepositoryHelper) {
        this.salesSlipForRevisionDao = salesSlipForRevisionDao;
        this.adjustmentAmountForRevisionDao = adjustmentAmountForRevisionDao;
        this.itemPurchasePriceSubTotalForRevisionDao = itemPurchasePriceSubTotalForRevisionDao;
        this.salesSlipForRevisionRepositoryHelper = salesSlipForRevisionRepositoryHelper;
    }

    /**
     * 改訂用販売伝票登録
     *
     * @param salesSlipForRevisionEntity 改訂用販売伝票エンティティ
     */
    @Override
    public void save(SalesSlipForRevisionEntity salesSlipForRevisionEntity) {

        SalesSlipForRevisionDbEntity salesSlipForRevisionDbEntity =
                        salesSlipForRevisionRepositoryHelper.toSalesSlipForRevisionDbEntity(salesSlipForRevisionEntity);
        salesSlipForRevisionDao.insert(salesSlipForRevisionDbEntity);

        List<AdjustmentAmountForRevisionDbEntity> adjustmentAmountForRevisionDbEntityList =
                        salesSlipForRevisionRepositoryHelper.toAdjustmentAmountForRevisionDbEntityList(
                                        salesSlipForRevisionEntity.getAdjustmentAmountList(),
                                        salesSlipForRevisionEntity.getSalesSlipRevisionId().getValue()
                                                                                                      );

        if (!CollectionUtils.isEmpty(adjustmentAmountForRevisionDbEntityList)) {
            for (AdjustmentAmountForRevisionDbEntity adjustmentAmountForRevisionDbEntity : adjustmentAmountForRevisionDbEntityList) {
                adjustmentAmountForRevisionDao.insert(adjustmentAmountForRevisionDbEntity);
            }
        }

        List<ItemPurchasePriceSubTotalForRevisionDbEntity> itemPurchasePriceSubTotalForRevisionDbEntityList =
                        salesSlipForRevisionRepositoryHelper.toItemPurchasePriceSubTotalForRevisionDbEntityList(
                                        salesSlipForRevisionEntity.getItemPurchasePriceSubTotalList(),
                                        salesSlipForRevisionEntity.getSalesSlipRevisionId().getValue()
                                                                                                               );

        for (ItemPurchasePriceSubTotalForRevisionDbEntity itemPurchasePriceSubTotalForRevisionDbEntity : itemPurchasePriceSubTotalForRevisionDbEntityList) {
            itemPurchasePriceSubTotalForRevisionDao.insert(itemPurchasePriceSubTotalForRevisionDbEntity);
        }

    }

    /**
     * 改訂用販売伝票更新
     *
     * @param salesSlipForRevisionEntity 改訂用販売伝票エンティティ
     * @return 更新件数
     */
    @Override
    public int update(SalesSlipForRevisionEntity salesSlipForRevisionEntity) {

        SalesSlipForRevisionDbEntity salesSlipForRevisionDbEntity =
                        salesSlipForRevisionRepositoryHelper.toSalesSlipForRevisionDbEntity(salesSlipForRevisionEntity);
        int result = salesSlipForRevisionDao.update(salesSlipForRevisionDbEntity);

        List<AdjustmentAmountForRevisionDbEntity> adjustmentAmountForRevisionDbEntityRequestList =
                        salesSlipForRevisionRepositoryHelper.toAdjustmentAmountForRevisionDbEntityList(
                                        salesSlipForRevisionEntity.getAdjustmentAmountList(),
                                        salesSlipForRevisionEntity.getSalesSlipRevisionId().getValue()
                                                                                                      );

        Set<String> salesSlipForRevisionAdjustNameSet = new HashSet<>();
        Set<Integer> orderItemSeqRevisionSet = new HashSet<>();

        if (!CollectionUtils.isEmpty(adjustmentAmountForRevisionDbEntityRequestList)) {
            for (AdjustmentAmountForRevisionDbEntity adjustmentAmountForRevisionDbEntityRequest : adjustmentAmountForRevisionDbEntityRequestList) {

                // 新規登録or更新or削除対象の配送商品IDをセット
                salesSlipForRevisionAdjustNameSet.add(adjustmentAmountForRevisionDbEntityRequest.getAdjustName());

                this.adjustmentAmountForRevisionDao.insertOrUpdate(adjustmentAmountForRevisionDbEntityRequest);
            }

            // 配送商品情報の一覧を取得する
            List<AdjustmentAmountForRevisionDbEntity> adjustmentAmountForRevisionInDb =
                            this.adjustmentAmountForRevisionDao.getBySalesSlipRevisionId(
                                            salesSlipForRevisionDbEntity.getSalesSlipRevisionId());

            // 対象の配送商品IDが、配送商品テーブルに存在しない場合、該当レコードを削除
            if (!CollectionUtils.isEmpty(adjustmentAmountForRevisionInDb)) {
                adjustmentAmountForRevisionInDb.forEach(item -> {
                    if (!salesSlipForRevisionAdjustNameSet.contains(item.getAdjustName())) {
                        this.adjustmentAmountForRevisionDao.delete(item);
                    }
                });
            }
        }

        // 商品購入価格小計Db登録用リスト
        List<ItemPurchasePriceSubTotalForRevisionDbEntity> itemPurchasePriceSubTotalForRevisionDbEntityRequestList =
                        salesSlipForRevisionRepositoryHelper.toItemPurchasePriceSubTotalForRevisionDbEntityList(
                                        salesSlipForRevisionEntity.getItemPurchasePriceSubTotalList(),
                                        salesSlipForRevisionEntity.getSalesSlipRevisionId().getValue()
                                                                                                               );

        if (!CollectionUtils.isEmpty(itemPurchasePriceSubTotalForRevisionDbEntityRequestList)) {
            for (ItemPurchasePriceSubTotalForRevisionDbEntity itemPurchasePriceSubTotalForRevisionDbEntityRequest : itemPurchasePriceSubTotalForRevisionDbEntityRequestList) {

                // 新規登録or更新対象の注文商品連番をセット
                orderItemSeqRevisionSet.add(itemPurchasePriceSubTotalForRevisionDbEntityRequest.getOrderItemSeq());

                this.itemPurchasePriceSubTotalForRevisionDao.insertOrUpdate(
                                itemPurchasePriceSubTotalForRevisionDbEntityRequest);
            }

            // 既存 商品購入価格小計DB一覧を取得する
            List<ItemPurchasePriceSubTotalForRevisionDbEntity> itemPurchasePriceSubTotalForRevisionDbEntityListInDb =
                            this.itemPurchasePriceSubTotalForRevisionDao.getBySalesSlipForRevisionId(
                                            salesSlipForRevisionDbEntity.getSalesSlipRevisionId());

            // 対象の注文商品連番が、購入商品小計テーブルに存在しない場合、該当レコードを削除
            if (!CollectionUtils.isEmpty(itemPurchasePriceSubTotalForRevisionDbEntityListInDb)) {
                itemPurchasePriceSubTotalForRevisionDbEntityListInDb.forEach(item -> {
                    if (!orderItemSeqRevisionSet.contains(item.getOrderItemSeq())) {
                        this.itemPurchasePriceSubTotalForRevisionDao.delete(item);
                    }
                });
            }
        }

        return result;
    }

    /**
     * 改訂用販売伝票取得
     *
     * @param salesSlipRevisionId 改訂用販売伝票ID 値オブジェクト
     * @return SalesSlipForRevisionEntity 改訂用販売伝票エンティティ
     */
    @Override
    public SalesSlipForRevisionEntity get(SalesSlipRevisionId salesSlipRevisionId) {

        SalesSlipForRevisionDbEntity salesSlipForRevisionDbEntity =
                        salesSlipForRevisionDao.getBySalesSlipRevisionId(salesSlipRevisionId.getValue());

        List<ItemPurchasePriceSubTotalForRevisionDbEntity> itemSubTotalForRevDbEntityList =
            itemPurchasePriceSubTotalForRevisionDao.getBySalesSlipForRevisionId(
                salesSlipForRevisionDbEntity.getSalesSlipRevisionId());

        List<AdjustmentAmountForRevisionDbEntity> adjustmentForRevDbEntityList =
            adjustmentAmountForRevisionDao.getBySalesSlipRevisionId(
                salesSlipForRevisionDbEntity.getSalesSlipRevisionId());

        return salesSlipForRevisionRepositoryHelper.toSalesSlipForRevisionEntity(
            salesSlipForRevisionDbEntity, itemSubTotalForRevDbEntityList, adjustmentForRevDbEntityList);
    }

    /**
     * 改訂用取引IDで販売伝票取得
     *
     * @param transactionRevisionId 改訂用取取引ID
     * @return SalesSlipForRevisionEntity
     */
    @Override
    public SalesSlipForRevisionEntity getByTransactionRevisionId(String transactionRevisionId) {

        SalesSlipForRevisionDbEntity salesSlipForRevisionDbEntity =
                        salesSlipForRevisionDao.getByTransactionRevisionId(transactionRevisionId);

        List<ItemPurchasePriceSubTotalForRevisionDbEntity> itemSubTotalForRevDbEntityList =
            itemPurchasePriceSubTotalForRevisionDao.getBySalesSlipForRevisionId(
                salesSlipForRevisionDbEntity.getSalesSlipRevisionId());

        List<AdjustmentAmountForRevisionDbEntity> adjustmentForRevDbEntityList =
            adjustmentAmountForRevisionDao.getBySalesSlipRevisionId(
                salesSlipForRevisionDbEntity.getSalesSlipRevisionId());

        return salesSlipForRevisionRepositoryHelper.toSalesSlipForRevisionEntity(
            salesSlipForRevisionDbEntity, itemSubTotalForRevDbEntityList, adjustmentForRevDbEntityList);
    }
}