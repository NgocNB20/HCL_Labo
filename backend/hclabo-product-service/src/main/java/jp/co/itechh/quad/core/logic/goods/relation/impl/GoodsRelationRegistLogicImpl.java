/*
 * Project Name : HIT-MALL3
 *
 * Copyright (C) 2008 i-TEC HANKYU HANSHIN INC. All Rights Reserved.
 *
 */

package jp.co.itechh.quad.core.logic.goods.relation.impl;

import jp.co.itechh.quad.core.base.util.common.ArgumentCheckUtil;
import jp.co.itechh.quad.core.base.utility.ApplicationContextUtility;
import jp.co.itechh.quad.core.base.utility.DateUtility;
import jp.co.itechh.quad.core.dao.goods.goodsgroup.GoodsRelationDao;
import jp.co.itechh.quad.core.entity.goods.goodsgroup.GoodsRelationEntity;
import jp.co.itechh.quad.core.logic.AbstractShopLogic;
import jp.co.itechh.quad.core.logic.goods.relation.GoodsRelationRegistLogic;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.sql.Timestamp;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * 関連商品登録
 *
 * @author hirata
 * @author Kaneko (itec) 2012/08/21 UtilからHelperへ変更　Util使用箇所をgetComponentで取得するように変更
 *
 */
@Component
public class GoodsRelationRegistLogicImpl extends AbstractShopLogic implements GoodsRelationRegistLogic {

    /**
     * 関連商品DAO
     */
    private final GoodsRelationDao goodsRelationDao;

    @Autowired
    public GoodsRelationRegistLogicImpl(GoodsRelationDao goodsRelationDao) {
        this.goodsRelationDao = goodsRelationDao;
    }

    /**
     * 関連商品登録
     *
     * @param goodsGroupSeq 商品グループSEQ
     * @param goodsRelationEntityList 関連商品エンティティリスト
     * @return 処理件数マップ
     */
    @Override
    public Map<String, Integer> execute(Integer goodsGroupSeq, List<GoodsRelationEntity> goodsRelationEntityList) {

        // (1) パラメータチェック
        checkParameter(goodsGroupSeq, goodsRelationEntityList);

        // (2) 関連商品情報リスト取得
        List<GoodsRelationEntity> masterGoodsRelationEntityList = getMasterGoodsRelationEntityList(goodsGroupSeq);

        // 関連商品情報リストマップ（KEY:関連商品グループSEQ）を作成する
        Map<Integer, GoodsRelationEntity> masterGoodsRelationEntityMap =
                        createMasterGoodsRelationEntityMap(masterGoodsRelationEntityList);

        // （削除用）関連商品エンティティリスト
        List<GoodsRelationEntity> entityListForDelete = masterGoodsRelationEntityList;
        // （更新用）関連商品エンティティリスト
        List<GoodsRelationEntity> entityListForUpdate = new ArrayList<>();
        // （登録用）関連商品エンティティリスト
        List<GoodsRelationEntity> entityListForRegist = new ArrayList<>();

        // (3) （登録用/更新用/削除用）関連商品エンティティリストの編集
        setGoodsRelationEntityList(goodsRelationEntityList, masterGoodsRelationEntityMap, entityListForDelete,
                                   entityListForUpdate, entityListForRegist
                                  );

        // 処理件数マップ生成
        Map<String, Integer> resultMap = new HashMap<>();

        // (4) 関連商品情報の削除処理
        deleteGoodsRelation(entityListForDelete, resultMap);

        // (5) 関連商品情報の更新処理
        updateGoodsRelation(entityListForUpdate, resultMap);

        // (6) 関連商品情報の登録処理
        registGoodsRelation(entityListForRegist, resultMap);

        // (7) 戻り値
        return resultMap;
    }

    /**
     *  パラメータチェック
     *
     * @param goodsGroupSeq 商品グループSEQ
     * @param goodsRelationEntityList 関連商品エンティティリスト
     * @param customParams 案件用引数
     */
    protected void checkParameter(Integer goodsGroupSeq,
                                  List<GoodsRelationEntity> goodsRelationEntityList,
                                  Object... customParams) {
        // 商品グループSEQが null でないかをチェック
        ArgumentCheckUtil.assertNotNull("goodsGroupSeq", goodsGroupSeq);
        // 関連商品エンティティリストが null でないかをチェック
        ArgumentCheckUtil.assertNotNull("goodsRelationEntityList", goodsRelationEntityList);

        for (GoodsRelationEntity goodsRelationEntity : goodsRelationEntityList) {
            if (!goodsGroupSeq.equals(goodsRelationEntity.getGoodsGroupSeq())) {
                // 商品グループSEQ不一致エラーを投げる。
                throwMessage(MSGCD_GOODSGROUP_MISMATCH_FAIL);
            }
        }
    }

    /**
     * 関連商品情報リスト取得
     *
     * @param goodsGroupSeq 商品グループSEQ
     * @param customParams 案件用引数
     * @return 関連商品情報リスト
     */
    protected List<GoodsRelationEntity> getMasterGoodsRelationEntityList(Integer goodsGroupSeq,
                                                                         Object... customParams) {
        return goodsRelationDao.getGoodsRelationEntityListByGoodsGroupSeq(goodsGroupSeq);
    }

    /**
     * 関連商品情報リストマップ（KEY:関連商品グループSEQ）を作成
     *
     * @param masterGoodsRelationEntityList （DB）関連商品情報リスト
     * @param customParams 案件用引数
     * @return 関連商品情報リストマップ
     */
    protected Map<Integer, GoodsRelationEntity> createMasterGoodsRelationEntityMap(List<GoodsRelationEntity> masterGoodsRelationEntityList,
                                                                                   Object... customParams) {
        Map<Integer, GoodsRelationEntity> masterGoodsRelationEntityMap = new HashMap<>();
        for (GoodsRelationEntity goodsRelationEntity : masterGoodsRelationEntityList) {
            masterGoodsRelationEntityMap.put(goodsRelationEntity.getGoodsRelationGroupSeq(), goodsRelationEntity);
        }
        return masterGoodsRelationEntityMap;
    }

    /**
     * （登録用/更新用/削除用）関連商品エンティティリストの編集
     *
     * @param goodsRelationEntityList 関連商品情報リスト
     * @param masterGoodsRelationEntityMap （DB）関連商品情報リスト
     * @param entityListForDelete （削除用）関連商品エンティティリスト
     * @param entityListForUpdate （更新用）関連商品エンティティリスト
     * @param entityListForRegist （登録用）関連商品エンティティリスト
     * @param customParams 案件用引数
     */
    protected void setGoodsRelationEntityList(List<GoodsRelationEntity> goodsRelationEntityList,
                                              Map<Integer, GoodsRelationEntity> masterGoodsRelationEntityMap,
                                              List<GoodsRelationEntity> entityListForDelete,
                                              List<GoodsRelationEntity> entityListForUpdate,
                                              List<GoodsRelationEntity> entityListForRegist,
                                              Object... customParams) {

        // 日付関連Helper取得
        DateUtility dateUtility = ApplicationContextUtility.getBean(DateUtility.class);

        for (GoodsRelationEntity goodsRelationEntity : goodsRelationEntityList) {
            // （パラメータ）関連商品の関連商品グループSEQが存在する場合
            if (masterGoodsRelationEntityMap.get(goodsRelationEntity.getGoodsRelationGroupSeq()) != null) {
                // （削除用）リストから削除
                entityListForDelete.remove(
                                masterGoodsRelationEntityMap.get(goodsRelationEntity.getGoodsRelationGroupSeq()));

                if (masterGoodsRelationEntityMap.get(goodsRelationEntity.getGoodsRelationGroupSeq()).getOrderDisplay()
                    != null && !masterGoodsRelationEntityMap.get(goodsRelationEntity.getGoodsRelationGroupSeq())
                                                            .getOrderDisplay()
                                                            .equals(goodsRelationEntity.getOrderDisplay())) {
                    // 表示順が異なる場合⇒更新リストに追加
                    // 登録・更新日時の設定
                    Timestamp currentTime = dateUtility.getCurrentTime();
                    goodsRelationEntity.setUpdateTime(currentTime);
                    goodsRelationEntity.setRegistTime(
                                    masterGoodsRelationEntityMap.get(goodsRelationEntity.getGoodsRelationGroupSeq())
                                                                .getRegistTime());
                    // （更新用）リストに追加
                    entityListForUpdate.add(goodsRelationEntity);
                }
            }
            // （パラメータ）関連商品の関連商品グループSEQが存在しない場合
            else {
                // 登録・更新日時の設定
                Timestamp currentTime = dateUtility.getCurrentTime();
                goodsRelationEntity.setRegistTime(currentTime);
                goodsRelationEntity.setUpdateTime(currentTime);
                // （登録用）リストに追加
                entityListForRegist.add(goodsRelationEntity);
            }
        }
    }

    /**
     * 関連商品情報の削除処理
     *
     * @param entityListForDelete （削除用）関連商品エンティティリスト
     * @param resultMap 処理件数マップ
     * @param customParams 案件用引数
     */
    protected void deleteGoodsRelation(List<GoodsRelationEntity> entityListForDelete,
                                       Map<String, Integer> resultMap,
                                       Object... customParams) {
        // 関連商品削除件数
        int goodsRelationDelete = 0;
        for (GoodsRelationEntity goodsRelationEntity : entityListForDelete) {
            int ret = goodsRelationDao.delete(goodsRelationEntity);
            goodsRelationDelete += ret;
        }
        resultMap.put(GOODS_RELATION_DELETE, goodsRelationDelete);
    }

    /**
     * 関連商品情報の更新処理
     *
     * @param entityListForUpdate （更新用）関連商品エンティティリスト
     * @param resultMap 処理件数マップ
     * @param customParams 案件用引数
     */
    protected void updateGoodsRelation(List<GoodsRelationEntity> entityListForUpdate,
                                       Map<String, Integer> resultMap,
                                       Object... customParams) {
        // 関連商品更新件数
        int goodsRelationUpdate = 0;
        for (GoodsRelationEntity goodsRelationEntity : entityListForUpdate) {
            int ret = goodsRelationDao.update(goodsRelationEntity);
            goodsRelationUpdate += ret;
        }
        resultMap.put(GOODS_RELATION_UPDATE, goodsRelationUpdate);
    }

    /**
     * 関連商品情報の登録処理
     *
     * @param entityListForRegist （登録用）関連商品エンティティリスト
     * @param resultMap 処理件数マップ
     * @param customParams 案件用引数
     */
    protected void registGoodsRelation(List<GoodsRelationEntity> entityListForRegist,
                                       Map<String, Integer> resultMap,
                                       Object... customParams) {
        // 関連商品登録件数
        int goodsRelationRegist = 0;
        for (GoodsRelationEntity goodsRelationEntity : entityListForRegist) {
            int ret = goodsRelationDao.insert(goodsRelationEntity);
            goodsRelationRegist += ret;
        }
        resultMap.put(GOODS_RELATION_REGIST, goodsRelationRegist);
    }

}