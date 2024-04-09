/*
 * Project Name : HIT-MALL3
 *
 * Copyright (C) 2008 i-TEC HANKYU HANSHIN INC. All Rights Reserved.
 *
 */

package jp.co.itechh.quad.core.logic.goods.category.impl;

import jp.co.itechh.quad.core.base.util.common.ArgumentCheckUtil;
import jp.co.itechh.quad.core.base.utility.ApplicationContextUtility;
import jp.co.itechh.quad.core.base.utility.DateUtility;
import jp.co.itechh.quad.core.constant.type.HTypeCategoryType;
import jp.co.itechh.quad.core.dao.goods.category.CategoryGoodsDao;
import jp.co.itechh.quad.core.entity.goods.category.CategoryGoodsEntity;
import jp.co.itechh.quad.core.logic.AbstractShopLogic;
import jp.co.itechh.quad.core.logic.goods.category.CategoryGoodsRegistLogic;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.sql.Timestamp;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * 商品グループ登録
 *
 * @author hirata
 * @author Kaneko (itec) 2012/08/21 UtilからHelperへ変更　Util使用箇所をgetComponentで取得するように変更
 */
@Component
public class CategoryGoodsRegistLogicImpl extends AbstractShopLogic implements CategoryGoodsRegistLogic {

    /** カテゴリ登録商品DAO */
    private final CategoryGoodsDao categoryGoodsDao;

    @Autowired
    public CategoryGoodsRegistLogicImpl(CategoryGoodsDao categoryGoodsDao) {
        this.categoryGoodsDao = categoryGoodsDao;
    }

    /**
     *
     * カテゴリ登録商品登録
     *
     * @param goodsGroupSeq 商品グループSEQ
     * @param categoryGoodsEntityList カテゴリ登録商品エンティティリスト
     * @return 処理件数マップ
     */
    @Override
    public Map<String, Integer> execute(Integer goodsGroupSeq, List<CategoryGoodsEntity> categoryGoodsEntityList) {

        // (1) パラメータチェック
        // 商品グループSEQが null でないかをチェック
        ArgumentCheckUtil.assertNotNull("goodsGroupSeq", goodsGroupSeq);
        // カテゴリ登録商品エンティティリストが null でないかをチェック
        ArgumentCheckUtil.assertNotNull("categoryGoodsEntityList", categoryGoodsEntityList);

        // (2) 初期値設定
        // カテゴリ登録商品登録件数
        int categoryGoodsRegist = 0;
        // カテゴリ登録商品削除件数
        int categoryGoodsDelete = 0;
        for (CategoryGoodsEntity categoryGoodsEntity : categoryGoodsEntityList) {
            if (!goodsGroupSeq.equals(categoryGoodsEntity.getGoodsGroupSeq())) {
                // 商品グループSEQ不一致エラーを投げる。
                throwMessage(MSGCD_GOODSGROUP_MISMATCH_FAIL);
            }
        }

        // (3) カテゴリ登録商品情報リスト取得
        List<CategoryGoodsEntity> masterCategoryGoodsEntityList =
                        categoryGoodsDao.getCategoryGoodsListByGoodsGroupSeq(goodsGroupSeq);

        // カテゴリ登録商品情報リストマップ（KEY:カテゴリSEQ）を作成する
        Map<Integer, CategoryGoodsEntity> masterCategoryGoodsEntityMap = new HashMap<>();
        for (CategoryGoodsEntity categoryGoodsEntity : masterCategoryGoodsEntityList) {
            masterCategoryGoodsEntityMap.put(categoryGoodsEntity.getCategorySeq(), categoryGoodsEntity);
        }

        // (4) （登録用/削除用）カテゴリ登録商品エンティティリストの編集
        // （削除用）カテゴリ登録商品エンティティリスト
        List<CategoryGoodsEntity> entityListForDelete = masterCategoryGoodsEntityList;
        // （登録用）カテゴリ登録商品エンティティリスト
        List<CategoryGoodsEntity> entityListForRegist = new ArrayList<>();
        for (CategoryGoodsEntity categoryGoodsEntity : categoryGoodsEntityList) {
            // （パラメータ）カテゴリ登録商品のカテゴリSEQが存在する場合
            if (masterCategoryGoodsEntityMap.get(categoryGoodsEntity.getCategorySeq()) != null) {
                // （削除用）リストから削除
                entityListForDelete.remove(masterCategoryGoodsEntityMap.get(categoryGoodsEntity.getCategorySeq()));
            }
            // （パラメータ）カテゴリ登録商品のカテゴリSEQが存在しない場合
            else {

                // 日付関連Helper取得
                DateUtility dateUtility = ApplicationContextUtility.getBean(DateUtility.class);

                // 登録・更新日時の設定
                Timestamp currentTime = dateUtility.getCurrentTime();
                categoryGoodsEntity.setRegistTime(currentTime);
                categoryGoodsEntity.setUpdateTime(currentTime);
                // （登録用）リストに追加
                entityListForRegist.add(categoryGoodsEntity);
            }
        }

        // (5) カテゴリ登録商品情報の削除処理
        for (CategoryGoodsEntity categoryGoodsEntity : entityListForDelete) {
            if (categoryGoodsEntity.getCategoryType() == HTypeCategoryType.NORMAL) {
                int ret = categoryGoodsDao.delete(categoryGoodsEntity);
                categoryGoodsDelete += ret;
            }

        }

        // (6) カテゴリ登録商品情報の登録処理
        for (CategoryGoodsEntity categoryGoodsEntity : entityListForRegist) {
            int ret = categoryGoodsDao.insert(categoryGoodsEntity);
            if (ret > 0) {
                categoryGoodsDao.updateOrderDisplay(
                                categoryGoodsEntity.getCategorySeq(), categoryGoodsEntity.getGoodsGroupSeq());
            }
            categoryGoodsRegist += ret;
        }

        // (7) 戻り値
        // 処理件数マップを生成して、返す。
        Map<String, Integer> resultMap = new HashMap<>();
        resultMap.put(CATEGORY_GOODS_REGIST, categoryGoodsRegist);
        resultMap.put(CATEGORY_GOODS_DELETE, categoryGoodsDelete);
        return resultMap;
    }
}