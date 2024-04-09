package jp.co.itechh.quad.ddd.infrastructure.shipping.repository;

import jp.co.itechh.quad.ddd.domain.shipping.entity.ShippingSlipForRevisionEntity;
import jp.co.itechh.quad.ddd.domain.shipping.repository.IShippingSlipForRevisionRepository;
import jp.co.itechh.quad.ddd.domain.shipping.valueobject.SecuredShippingItem;
import jp.co.itechh.quad.ddd.domain.shipping.valueobject.ShippingSlipRevisionId;
import jp.co.itechh.quad.ddd.infrastructure.shipping.dao.SecuredShippingItemForRevisionDao;
import jp.co.itechh.quad.ddd.infrastructure.shipping.dao.ShippingSlipForRevisionDao;
import jp.co.itechh.quad.ddd.infrastructure.shipping.dbentity.SecuredShippingItemForRevisionDbEntity;
import jp.co.itechh.quad.ddd.infrastructure.shipping.dbentity.ShippingSlipForRevisionDbEntity;
import org.apache.commons.collections.CollectionUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.HashSet;
import java.util.List;
import java.util.Set;

/**
 * 配送伝票リポジトリ実装クラス
 *
 * @author Pham Quang Dieu (VTI Japan Co., Ltd.)
 */
@Component
public class ShippingSlipForRevisionRepositoryImpl implements IShippingSlipForRevisionRepository {
    /**
     * 改訂用配送伝票Daoクラス
     */
    private final ShippingSlipForRevisionDao shippingSlipForRevisionDao;

    /**
     * 改訂用配送商品Daoクラス
     */
    private final SecuredShippingItemForRevisionDao securedShippingItemForRevisionDao;

    /**
     * 注文票リポジトリHelperクラス
     */
    private final ShippingSlipForRevisionRepositoryHelper shippingSlipForRevisionRepositoryHelper;

    /**
     * コンストラクタ
     *
     * @param shippingSlipForRevisionDao              改訂用配送伝票Daoクラス
     * @param securedShippingItemForRevisionDao       改訂用配送商品Daoクラス
     * @param shippingSlipForRevisionRepositoryHelper 注文票リポジトリHelperクラス
     */
    @Autowired
    public ShippingSlipForRevisionRepositoryImpl(ShippingSlipForRevisionDao shippingSlipForRevisionDao,
                                                 SecuredShippingItemForRevisionDao securedShippingItemForRevisionDao,
                                                 ShippingSlipForRevisionRepositoryHelper shippingSlipForRevisionRepositoryHelper) {
        this.shippingSlipForRevisionDao = shippingSlipForRevisionDao;
        this.securedShippingItemForRevisionDao = securedShippingItemForRevisionDao;
        this.shippingSlipForRevisionRepositoryHelper = shippingSlipForRevisionRepositoryHelper;
    }

    /**
     * 改訂用配送伝票登録
     *
     * @param shippingSlipForRevisionEntity 改訂用配送伝票
     */
    @Override
    public void save(ShippingSlipForRevisionEntity shippingSlipForRevisionEntity) {

        ShippingSlipForRevisionDbEntity shippingSlipForRevisionDbEntity =
                        shippingSlipForRevisionRepositoryHelper.toShippingSlipForRevisionDbEntityFromShippingSlipForRevisionEntity(
                                        shippingSlipForRevisionEntity);
        shippingSlipForRevisionDao.insert(shippingSlipForRevisionDbEntity);

        List<SecuredShippingItemForRevisionDbEntity> securedShippingItemForRevisionDbEntities =
                        shippingSlipForRevisionRepositoryHelper.toShippingItemForRevisionDbEntityList(
                                        shippingSlipForRevisionEntity.getSecuredShippingItemList());

        for (SecuredShippingItemForRevisionDbEntity securedShippingItemForRevisionDbEntity : securedShippingItemForRevisionDbEntities) {
            securedShippingItemForRevisionDbEntity.setShippingSlipRevisionId(
                            shippingSlipForRevisionEntity.getShippingSlipRevisionId().getValue());
            securedShippingItemForRevisionDao.insert(securedShippingItemForRevisionDbEntity);
        }

    }

    /**
     * 改訂用配送伝票更新
     *
     * @param shippingSlipForRevisionEntity 改訂用配送伝票
     * @return 更新件数
     */
    @Override
    public int update(ShippingSlipForRevisionEntity shippingSlipForRevisionEntity) {

        ShippingSlipForRevisionDbEntity shippingSlipForRevisionDbEntity =
                        shippingSlipForRevisionRepositoryHelper.toShippingSlipForRevisionDbEntityFromShippingSlipForRevisionEntity(
                                        shippingSlipForRevisionEntity);
        int result = shippingSlipForRevisionDao.update(shippingSlipForRevisionDbEntity);

        // 改訂用確保済み商品リスト 登録用DBエンティティ
        List<SecuredShippingItemForRevisionDbEntity> securedShippingItemForRevisionDbEntityListRequest =
                        shippingSlipForRevisionRepositoryHelper.toShippingItemForRevisionDbEntityList(
                                        shippingSlipForRevisionEntity.getSecuredShippingItemList());

        Set<Integer> shippingItemSeqRevisionSet = new HashSet<>();

        if (!CollectionUtils.isEmpty(securedShippingItemForRevisionDbEntityListRequest)) {
            for (SecuredShippingItemForRevisionDbEntity shippingItemForRevisionDbEntity : securedShippingItemForRevisionDbEntityListRequest) {

                // 新規登録or更新or削除対象の配送商品IDをセット
                shippingItemSeqRevisionSet.add(shippingItemForRevisionDbEntity.getShippingItemSeq());

                shippingItemForRevisionDbEntity.setShippingSlipRevisionId(
                                shippingSlipForRevisionEntity.getShippingSlipRevisionId().getValue());

                this.securedShippingItemForRevisionDao.insertOrUpdate(shippingItemForRevisionDbEntity);
            }

            // 現在のDBにある改訂用配送商品DB値一覧を取得する
            List<SecuredShippingItemForRevisionDbEntity> shippingItemListInDb =
                            securedShippingItemForRevisionDao.getByShippingSlipRevisionId(
                                            shippingSlipForRevisionEntity.getShippingSlipRevisionId().getValue());

            // 更新対象の配送商品SEQが、配送商品DBテーブルに存在しない場合、該当レコードをDBより削除
            if (!CollectionUtils.isEmpty(shippingItemListInDb)) {
                shippingItemListInDb.forEach(item -> {
                    if (!shippingItemSeqRevisionSet.contains(item.getShippingItemSeq())) {
                        securedShippingItemForRevisionDao.delete(item);
                    }
                });
            }
        }

        return result;
    }

    /**
     * 改訂用配送伝票取得
     *
     * @param shippingSlipRevisionId 改訂用配送伝票ID
     * @return ShippingSlipForRevisionEntity 改訂用配送伝票
     */
    @Override
    public ShippingSlipForRevisionEntity get(ShippingSlipRevisionId shippingSlipRevisionId) {

        ShippingSlipForRevisionDbEntity shippingSlipForRevisionDbEntity =
                        shippingSlipForRevisionDao.getByShippingSlipRevisionId(shippingSlipRevisionId.getValue());

        List<SecuredShippingItemForRevisionDbEntity> shippingItemForRevisionDbEntities =
            securedShippingItemForRevisionDao.getByShippingSlipRevisionId(
                shippingSlipForRevisionDbEntity.getShippingSlipRevisionId());

        ShippingSlipForRevisionEntity shippingSlipForRevisionEntity =
                        shippingSlipForRevisionRepositoryHelper.toShippingSlipForRevisionEntityFromShippingSlipForRevisionDbEntity(
                                        shippingSlipForRevisionDbEntity, shippingItemForRevisionDbEntities);

        return shippingSlipForRevisionEntity;

    }

    /**
     * 改訂用配送伝票IDで改訂用配送伝票取得
     *
     * @param transactionRevisionId 改訂用配送伝票ID
     * @return ShippingSlipForRevisionEntity 改訂用配送伝票
     */
    @Override
    public ShippingSlipForRevisionEntity getByTransactionRevisionId(String transactionRevisionId) {

        ShippingSlipForRevisionDbEntity shippingSlipForRevisionDbEntity =
                        shippingSlipForRevisionDao.getByTransactionRevisionId(transactionRevisionId);

        List<SecuredShippingItemForRevisionDbEntity> shippingItemForRevisionDbEntities =
            securedShippingItemForRevisionDao.getByShippingSlipRevisionId(
                shippingSlipForRevisionDbEntity.getShippingSlipRevisionId());

        ShippingSlipForRevisionEntity shippingSlipForRevisionEntity =
            shippingSlipForRevisionRepositoryHelper.toShippingSlipForRevisionEntityFromShippingSlipForRevisionDbEntity(
                shippingSlipForRevisionDbEntity, shippingItemForRevisionDbEntities);

        return shippingSlipForRevisionEntity;
    }
}