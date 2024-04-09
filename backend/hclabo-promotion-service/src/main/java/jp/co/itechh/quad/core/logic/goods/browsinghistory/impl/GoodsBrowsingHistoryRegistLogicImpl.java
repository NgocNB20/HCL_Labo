/*
 * Project Name : HIT-MALL3
 *
 * Copyright (C) 2008 i-TEC HANKYU HANSHIN INC. All Rights Reserved.
 *
 */

package jp.co.itechh.quad.core.logic.goods.browsinghistory.impl;

import jp.co.itechh.quad.core.base.util.common.ArgumentCheckUtil;
import jp.co.itechh.quad.core.base.utility.ApplicationContextUtility;
import jp.co.itechh.quad.core.base.utility.DateUtility;
import jp.co.itechh.quad.core.dao.goods.browsinghistory.BrowsinghistoryDao;
import jp.co.itechh.quad.core.entity.goods.browsinghistory.BrowsinghistoryEntity;
import jp.co.itechh.quad.core.logic.AbstractShopLogic;
import jp.co.itechh.quad.core.logic.goods.browsinghistory.GoodsBrowsingHistoryRegistLogic;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

/**
 *
 * あしあと商品情報登録<br/>
 * あしあと商品情報を登録する。<br/>
 *
 * @author ozaki
 * @author Kaneko (itec) 2012/08/21 UtilからHelperへ変更　Util使用箇所をgetComponentで取得するように変更
 *
 */
@Component
public class GoodsBrowsingHistoryRegistLogicImpl extends AbstractShopLogic implements GoodsBrowsingHistoryRegistLogic {

    /** あしあと商品DAO */
    private final BrowsinghistoryDao browsinghistoryDao;

    @Autowired
    public GoodsBrowsingHistoryRegistLogicImpl(BrowsinghistoryDao browsinghistoryDao) {
        this.browsinghistoryDao = browsinghistoryDao;
    }

    /**
     *
     * あしあと商品情報登録<br/>
     * あしあと商品情報を登録する。<br/>
     *
     * @param browsinghistoryEntity あしあと情報
     * @return 登録・更新件数
     */
    @Override
    public int execute(BrowsinghistoryEntity browsinghistoryEntity) {

        // (1) パラメータチェック
        // あしあと情報Dtoが null でないかをチェック
        // 端末識別情報が null でないかをチェック
        ArgumentCheckUtil.assertNotNull("browsinghistoryEntity", browsinghistoryEntity);

        // (2) あしあと情報取得
        // あしあと情報Daoのあしあと情報取得処理により、既に同一代表商品のあしあと情報があれば取得する。
        // DAO BrowsinghistoryDao
        // メソッド あしあと情報 getEntity( ショップSEQ, 端末識別情報, 会員SEQ)
        BrowsinghistoryEntity newBrowsinghistoryEntity =
                        browsinghistoryDao.getEntity(browsinghistoryEntity.getAccessUid(),
                                                     browsinghistoryEntity.getGoodsGroupSeq()
                                                    );

        // 日付関連Helper取得
        DateUtility dateUtility = ApplicationContextUtility.getBean(DateUtility.class);

        // (4) あしあと情報の登録・更新
        int count = 0;
        if (newBrowsinghistoryEntity != null) {
            // ・(3)であしあと商品エンティティを取得した場合
            // （(3)で取得した）あしあと商品エンティティ．更新日時 ＝ サーバ現在日時 をセット
            newBrowsinghistoryEntity.setUpdateTime(dateUtility.getCurrentTime());

            // あしあとDaoのあしあと情報更新処理を実行する。
            // DAO BrowsinghistoryDao
            // メソッド 更新した件数 update( （(3)で取得した）あしあと商品エンティティ)
            count = browsinghistoryDao.update(newBrowsinghistoryEntity);
        } else {
            // ・(3)であしあと情報DTOを取得できなかった場合
            // （パラメータ）あしあと商品エンティティ．登録日時 ＝ サーバ現在日時 をセット
            // （パラメータ）あしあと商品エンティティ．更新日時 ＝ サーバ現在日時 をセット
            browsinghistoryEntity.setRegistTime(dateUtility.getCurrentTime());
            browsinghistoryEntity.setUpdateTime(dateUtility.getCurrentTime());

            // あしあと情報Daoのあしあと情報登録処理を実行する。
            // DAO BrowsinghistoryDao
            // メソッド 登録した件数 insert( （パラメータ）あしあと商品エンティティ)
            count = browsinghistoryDao.insert(browsinghistoryEntity);
        }

        // (4) 戻り値
        // 更新した件数 または 登録した件数 を返す
        return count;
    }
}