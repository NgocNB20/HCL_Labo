/*
 * Project Name : HIT-MALL3
 *
 * Copyright (C) 2008 i-TEC HANKYU HANSHIN INC. All Rights Reserved.
 *
 */

package jp.co.itechh.quad.core.logic.goods.goodsgroup.impl;

import jp.co.itechh.quad.core.base.util.common.ArgumentCheckUtil;
import jp.co.itechh.quad.core.base.util.common.PropertiesUtil;
import jp.co.itechh.quad.core.base.utility.ApplicationContextUtility;
import jp.co.itechh.quad.core.base.utility.DateUtility;
import jp.co.itechh.quad.core.dao.goods.goodsgroup.GoodsGroupImageDao;
import jp.co.itechh.quad.core.entity.goods.goodsgroup.GoodsGroupImageEntity;
import jp.co.itechh.quad.core.logic.AbstractShopLogic;
import jp.co.itechh.quad.core.logic.goods.goodsgroup.GoodsGroupImageRegistDataOnlyLogic;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.sql.Timestamp;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * 商品グループ画像登録
 *
 * @author hirata
 * @author Kaneko (itec) 2012/08/21 UtilからHelperへ変更　Util使用箇所をgetComponentで取得するように変更
 */
@Component
public class GoodsGroupImageRegistDataOnlyLogicImpl extends AbstractShopLogic
                implements GoodsGroupImageRegistDataOnlyLogic {

    /** 商品グループ画像DAO */
    private final GoodsGroupImageDao goodsGroupImageDao;

    @Autowired
    public GoodsGroupImageRegistDataOnlyLogicImpl(GoodsGroupImageDao goodsGroupImageDao) {
        this.goodsGroupImageDao = goodsGroupImageDao;
    }

    /**
     * 商品グループ画像登録
     *
     * @param goodsGroupSeq 商品グループSEQ
     * @param goodsGroupCode 商品グループコード
     * @param goodsGroupImageEntityList 商品グループ画像エンティティリスト
     * @return 処理件数マップ
     */
    @Override
    public Map<String, Object> execute(Integer goodsGroupSeq,
                                       String goodsGroupCode,
                                       List<GoodsGroupImageEntity> goodsGroupImageEntityList) {

        // (1) パラメータチェック
        // (注意) 画像ファイル名が固定でない場合は、商品グループ内ファイル名の重複チェックをここかActionで行う！
        argumentCheck(goodsGroupSeq, goodsGroupCode, goodsGroupImageEntityList);

        // (2) 商品グループ画像情報リスト取得
        List<GoodsGroupImageEntity> masterGoodsGroupImageEntityList = getMasterGoodsGroupImageList(goodsGroupSeq);

        // (注意) 画像ファイル名が固定である前提の処理になります。任意に設定可能な場合は仕様修正が必要
        // (3) （登録用/更新用/削除用）商品グループ画像エンティティリストの編集

        // 現在日付取得
        Timestamp currentTime = ApplicationContextUtility.getBean(DateUtility.class).getCurrentTime();

        // （削除用）商品グループ画像エンティティリスト
        List<GoodsGroupImageEntity> entityListForDelete = new ArrayList<>();
        // 削除画像ファイルパスリスト
        List<String> deleteImageFilePathList = new ArrayList<>();
        // （更新用）商品グループ画像エンティティリスト
        List<GoodsGroupImageEntity> entityListForUpdate = new ArrayList<>();
        // （登録用）商品グループ画像エンティティリスト
        List<GoodsGroupImageEntity> entityListForRegist = new ArrayList<>();

        for (GoodsGroupImageEntity goodsGroupImageEntity : goodsGroupImageEntityList) {
            boolean registFlg = true;
            for (GoodsGroupImageEntity masterGoodsGroupImageEntity : masterGoodsGroupImageEntityList) {
                // 更新
                // （更新用）商品グループ画像エンティティリスト設定
                registFlg = setGoodsGroupImageUpdateList(entityListForUpdate, deleteImageFilePathList,
                                                         goodsGroupImageEntity, masterGoodsGroupImageEntity, registFlg,
                                                         currentTime
                                                        );
            }
            if (registFlg) {
                // 商品グループ画像エンティティ(CSV)．画像種別 と
                // 商品グループ画像エンティティ(CSV)．画像種別内連番に一致する
                // 商品グループ画像エンティティがDBに存在しない場合、登録
                // （登録用）商品グループ画像エンティティリスト設定
                setGoodsGroupImageRegistList(entityListForRegist, goodsGroupImageEntity, currentTime);
            }
        }

        // 処理件数マップを生成
        Map<String, Object> resultMap = new HashMap<>();

        // 削除画像ファイルパスリスト設定
        // 商品グループ画像エンティティ(CSV)．画像種別 と
        // 商品グループ画像エンティティ(CSV)．画像種別内連番が一致しない
        // 商品グループ画像エンティティがDBに存在した場合、削除
        for (GoodsGroupImageEntity masterGoodsGroupImageEntity : masterGoodsGroupImageEntityList) {
            // （削除用）商品グループ画像エンティティリストの設定
            setGoodsGroupImageDeleteList(entityListForDelete, deleteImageFilePathList, goodsGroupImageEntityList,
                                         masterGoodsGroupImageEntity
                                        );
        }
        resultMap.put(DELETE_IMAGE_FILE_PATH_LIST, deleteImageFilePathList);

        // (4) 商品グループ画像情報の削除処理
        deleteGoodsGroupImage(entityListForDelete, resultMap);

        // (5) 商品グループ画像情報の更新処理
        updateGoodsGroupImage(entityListForUpdate, resultMap);

        // (6) 商品グループ画像情報の登録処理
        registGoodsGroupImage(entityListForRegist, resultMap);

        return resultMap;
    }

    /**
     * 入力パラメータチェック
     *
     * @param goodsGroupSeq 商品グループSEQ
     * @param goodsGroupCode 商品グループコード
     * @param goodsGroupImageEntityList 商品グループ画像登録更新用エンティティリスト
     * @param customParams 案件用引数
     */
    protected void argumentCheck(Integer goodsGroupSeq,
                                 String goodsGroupCode,
                                 List<GoodsGroupImageEntity> goodsGroupImageEntityList,
                                 Object... customParams) {
        // 商品グループSEQが null でないかをチェック
        ArgumentCheckUtil.assertNotNull("goodsGroupSeq", goodsGroupSeq);
        // 商品グループコードが nullまたは空文字 でないかをチェック
        ArgumentCheckUtil.assertNotEmpty("goodsGroupCode", goodsGroupCode);
        // 商品グループ画像登録更新用エンティティリストが null でないかをチェック
        ArgumentCheckUtil.assertNotNull("goodsGroupImageEntityList", goodsGroupImageEntityList);

        // 商品グループSEQ不一致チェック
        for (GoodsGroupImageEntity goodsGroupImageEntity : goodsGroupImageEntityList) {
            if (!goodsGroupSeq.equals(goodsGroupImageEntity.getGoodsGroupSeq())) {
                // 商品グループSEQ不一致エラーを投げる。
                throwMessage(MSGCD_GOODSGROUP_MISMATCH_FAIL);
            }
        }
    }

    /**
     * 商品グループ画像情報リスト取得
     * 商品グループSEQを基に商品グループ画像情報を取得する
     *
     * @param goodsGroupSeq 商品グループSEQ
     * @param customParams 案件用引数
     * @return 商品グループ画像エンティティリスト
     */
    protected List<GoodsGroupImageEntity> getMasterGoodsGroupImageList(Integer goodsGroupSeq, Object... customParams) {
        return goodsGroupImageDao.getGoodsGroupImageListByGoodsGroupSeq(goodsGroupSeq);
    }

    /**
     * （登録用）商品グループ画像エンティティリスト設定
     *
     * @param entityListForRegist （登録用）商品グループ画像エンティティリスト
     * @param goodsGroupImageEntity 商品グループ画像エンティティ
     * @param currentTime 現在時刻
     * @param customParams 案件用引数
     */
    protected void setGoodsGroupImageRegistList(List<GoodsGroupImageEntity> entityListForRegist,
                                                GoodsGroupImageEntity goodsGroupImageEntity,
                                                Timestamp currentTime,
                                                Object... customParams) {
        // 登録リストに追加
        // 登録・更新日時の設定
        goodsGroupImageEntity.setRegistTime(currentTime);
        goodsGroupImageEntity.setUpdateTime(currentTime);
        entityListForRegist.add(goodsGroupImageEntity);
    }

    /**
     * （更新用）商品グループ画像エンティティリスト設定
     *
     * @param entityListForUpdate （更新用）商品グループ画像エンティティリスト
     * @param deleteImageFilePathList 削除画像ファイルパスリスト
     * @param goodsGroupImageEntity 商品グループ画像エンティティ
     * @param registFlg 登録フラグ
     * @param masterGoodsGroupImageEntity 商品グループ画像情報
     * @param currentTime 現在時刻
     * @param customParams 案件用引数
     * @return 登録フラグ
     */
    protected boolean setGoodsGroupImageUpdateList(List<GoodsGroupImageEntity> entityListForUpdate,
                                                   List<String> deleteImageFilePathList,
                                                   GoodsGroupImageEntity goodsGroupImageEntity,
                                                   GoodsGroupImageEntity masterGoodsGroupImageEntity,
                                                   boolean registFlg,
                                                   Timestamp currentTime,
                                                   Object... customParams) {
        if (masterGoodsGroupImageEntity.getImageTypeVersionNo().equals(goodsGroupImageEntity.getImageTypeVersionNo())) {
            if (StringUtils.isNotEmpty(masterGoodsGroupImageEntity.getImageFileName())
                && !masterGoodsGroupImageEntity.getImageFileName().equals(goodsGroupImageEntity.getImageFileName())) {
                // 削除ファイルパスリストに追加
                deleteImageFilePathList.add(PropertiesUtil.getSystemPropertiesValue("real.images.path.goods") + "/"
                                            + masterGoodsGroupImageEntity.getImageFileName());
                // 更新リストに追加
                // 更新日時の設定
                goodsGroupImageEntity.setUpdateTime(currentTime);
                entityListForUpdate.add(goodsGroupImageEntity);
            }
            // 更新日時の設定
            goodsGroupImageEntity.setUpdateTime(currentTime);
            entityListForUpdate.add(goodsGroupImageEntity);
            registFlg = false;
        }
        return registFlg;
    }

    /**
     * （削除用）商品グループ画像エンティティリストの設定
     *
     * @param entityListForDelete （削除用）商品グループ画像エンティティリスト
     * @param deleteImageFilePathList 削除画像ファイルパスリスト
     * @param goodsGroupImageEntityList 商品グループ画像エンティティ
     * @param masterGoodsGroupImageEntity 商品グループ画像情報
     * @param customParams 案件用引数
     */
    protected void setGoodsGroupImageDeleteList(List<GoodsGroupImageEntity> entityListForDelete,
                                                List<String> deleteImageFilePathList,
                                                List<GoodsGroupImageEntity> goodsGroupImageEntityList,
                                                GoodsGroupImageEntity masterGoodsGroupImageEntity,
                                                Object... customParams) {
        boolean deleteFlg = true;
        for (GoodsGroupImageEntity goodsGroupImageEntity : goodsGroupImageEntityList) {
            if (masterGoodsGroupImageEntity.getImageTypeVersionNo()
                                           .equals(goodsGroupImageEntity.getImageTypeVersionNo())) {
                deleteFlg = false;
            }
        }
        if (deleteFlg) {
            // 削除ファイルパスリストに追加
            deleteImageFilePathList.add(PropertiesUtil.getSystemPropertiesValue("real.images.path.goods") + "/"
                                        + masterGoodsGroupImageEntity.getImageFileName());
            // 削除リストに追加
            entityListForDelete.add(masterGoodsGroupImageEntity);
        }
    }

    /**
     * 商品グループ画像情報の削除処理
     *
     * @param entityListForDelete （削除用）商品グループ画像エンティティリスト
     * @param resultMap 処理件数マップ
     * @param customParams 案件用引数
     */
    protected void deleteGoodsGroupImage(List<GoodsGroupImageEntity> entityListForDelete,
                                         Map<String, Object> resultMap,
                                         Object... customParams) {
        // 商品グループ画像削除件数
        int goodsGroupImageDelete = 0;
        for (GoodsGroupImageEntity masterGoodsGroupImageEntity : entityListForDelete) {
            int ret = goodsGroupImageDao.delete(masterGoodsGroupImageEntity);
            goodsGroupImageDelete += ret;
        }
        resultMap.put(GOODS_GROUP_IMAGE_DELETE, goodsGroupImageDelete);
    }

    /**
     * 商品グループ画像情報の更新処理
     *
     * @param entityListForUpdate （更新用）商品グループ画像エンティティリスト
     * @param resultMap 処理件数マップ
     * @param customParams 案件用引数
     */
    protected void updateGoodsGroupImage(List<GoodsGroupImageEntity> entityListForUpdate,
                                         Map<String, Object> resultMap,
                                         Object... customParams) {
        // 商品グループ画像更新件数
        int goodsGroupImageUpdate = 0;
        for (GoodsGroupImageEntity masterGoodsGroupImageEntity : entityListForUpdate) {
            int ret = goodsGroupImageDao.update(masterGoodsGroupImageEntity);
            goodsGroupImageUpdate += ret;
        }
        resultMap.put(GOODS_GROUP_IMAGE_UPDATE, goodsGroupImageUpdate);
    }

    /**
     * 商品グループ画像情報の登録処理
     *
     * @param entityListForRegist （登録用）商品グループ画像エンティティリスト
     * @param resultMap 処理件数マップ
     * @param customParams 案件用引数
     */
    protected void registGoodsGroupImage(List<GoodsGroupImageEntity> entityListForRegist,
                                         Map<String, Object> resultMap,
                                         Object... customParams) {
        // 商品グループ画像登録件数
        int goodsGroupImageRegist = 0;
        for (GoodsGroupImageEntity masterGoodsGroupImageEntity : entityListForRegist) {
            int ret = goodsGroupImageDao.insert(masterGoodsGroupImageEntity);
            goodsGroupImageRegist += ret;
        }
        resultMap.put(GOODS_GROUP_IMAGE_REGIST, goodsGroupImageRegist);
    }
}