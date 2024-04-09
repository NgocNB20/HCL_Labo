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
import jp.co.itechh.quad.core.dto.common.FileRegistDto;
import jp.co.itechh.quad.core.dto.goods.goodsgroup.GoodsGroupImageRegistUpdateDto;
import jp.co.itechh.quad.core.entity.goods.goodsgroup.GoodsGroupImageEntity;
import jp.co.itechh.quad.core.logic.AbstractShopLogic;
import jp.co.itechh.quad.core.logic.goods.goodsgroup.GoodsGroupImageRegistLogic;
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
 *
 */
@Component
public class GoodsGroupImageRegistLogicImpl extends AbstractShopLogic implements GoodsGroupImageRegistLogic {

    /** 商品グループ画像DAO */
    private final GoodsGroupImageDao goodsGroupImageDao;

    /** 商品グループ画像登録件数 */
    protected int goodsGroupImageRegist;

    /** 商品グループ画像更新件数 */
    protected int goodsGroupImageUpdate;

    /** 商品グループ画像削除件数 */
    protected int goodsGroupImageDelete;

    /** 削除画像ファイルパスリスト */
    protected List<String> deleteImageFilePathList;

    /** 登録画像ファイルパスリスト */
    protected List<FileRegistDto> registImageFilePathList;

    /** （削除用）商品グループ画像エンティティリスト */
    protected List<GoodsGroupImageEntity> entityListForDelete;

    /** （更新用）商品グループ画像エンティティリスト */
    protected List<GoodsGroupImageEntity> entityListForUpdate;

    /** （登録用）商品グループ画像エンティティリスト */
    protected List<GoodsGroupImageEntity> entityListForRegist;

    /** 商品グループ画像情報リスト */
    protected List<GoodsGroupImageEntity> masterGoodsGroupImageEntityList;

    /** 処理件数マップ */
    protected Map<String, Object> resultMap;

    @Autowired
    public GoodsGroupImageRegistLogicImpl(GoodsGroupImageDao goodsGroupImageDao) {
        this.goodsGroupImageDao = goodsGroupImageDao;
    }

    /**
     *
     * 商品グループ画像登録
     *
     * @param goodsGroupSeq 商品グループSEQ
     * @param goodsGroupCode 商品グループコード
     * @param goodsGroupImageRegistUpdateDtoList 商品グループ画像登録更新用DTOリスト
     * @return 処理件数マップ
     */
    @Override
    public Map<String, Object> execute(Integer goodsGroupSeq,
                                       String goodsGroupCode,
                                       List<GoodsGroupImageRegistUpdateDto> goodsGroupImageRegistUpdateDtoList) {

        // (1) 初期値設定
        initValue();

        // (2) パラメータチェック
        checkParam(goodsGroupSeq, goodsGroupCode, goodsGroupImageRegistUpdateDtoList);

        // (3) 商品グループ画像情報リスト取得
        masterGoodsGroupImageEntityList = goodsGroupImageDao.getGoodsGroupImageListByGoodsGroupSeq(goodsGroupSeq);

        // (4) （登録用/更新用/削除用）商品グループ画像エンティティリストの編集
        makeGoodsGroupImageEntityList(goodsGroupImageRegistUpdateDtoList);

        // (5) 商品グループ画像情報の削除処理
        deleteGoodsGroupImageEntityList();

        // (6) 商品グループ画像情報の更新処理
        updateGoodsGroupImageEntityList();

        // (7) 商品グループ画像情報の登録処理
        registGoodsGroupImageEntityList();

        // (8) 処理件数マップ生成
        makeResultMap();

        return resultMap;
    }

    /**
     * パラメータチェック処理
     *
     * @param goodsGroupSeq 商品グループSEQ
     * @param goodsGroupCode 商品グループコード
     * @param goodsGroupImageRegistUpdateDtoList 商品グループ画像登録更新用DTOリスト
     * @param customParams 案件用引数
     */
    protected void checkParam(Integer goodsGroupSeq,
                              String goodsGroupCode,
                              List<GoodsGroupImageRegistUpdateDto> goodsGroupImageRegistUpdateDtoList,
                              Object... customParams) {

        // 商品グループSEQが null でないかをチェック
        ArgumentCheckUtil.assertNotNull("goodsGroupSeq", goodsGroupSeq);
        // 商品グループ画像登録更新用DTOリストが null でないかをチェック
        ArgumentCheckUtil.assertNotNull("goodsGroupImageRegistUpdateDtoList", goodsGroupImageRegistUpdateDtoList);
        // 商品グループコードが nullまたは空文字 でないかをチェック
        ArgumentCheckUtil.assertNotEmpty("goodsGroupCode", goodsGroupCode);
        // (注意) 画像ファイル名が固定でない場合は、商品グループ内ファイル名の重複チェックをここかActionで行う！
        for (GoodsGroupImageRegistUpdateDto goodsGroupImageRegistUpdateDto : goodsGroupImageRegistUpdateDtoList) {
            if (!goodsGroupSeq.equals(goodsGroupImageRegistUpdateDto.getGoodsGroupImageEntity().getGoodsGroupSeq())) {
                // 商品グループSEQ不一致エラーを投げる。
                throwMessage(MSGCD_GOODSGROUP_MISMATCH_FAIL);
            }
        }
    }

    /**
     * 初期値設定処理
     *
     * @param customParams 案件用引数
     */
    protected void initValue(Object... customParams) {

        // 商品グループ画像登録件数
        goodsGroupImageRegist = 0;
        // 商品グループ画像更新件数
        goodsGroupImageUpdate = 0;
        // 商品グループ画像削除件数
        goodsGroupImageDelete = 0;
        // 削除画像ファイルパスリスト
        deleteImageFilePathList = new ArrayList<>();
        // 登録画像ファイルパスリスト
        registImageFilePathList = new ArrayList<>();
        // （削除用）商品グループ画像エンティティリスト
        entityListForDelete = new ArrayList<>();
        // （更新用）商品グループ画像エンティティリスト
        entityListForUpdate = new ArrayList<>();
        // （登録用）商品グループ画像エンティティリスト
        entityListForRegist = new ArrayList<>();
        // 処理件数マップ
        resultMap = new HashMap<>();
        // エラーリスト
        clearErrorList();
    }

    /**
     * （登録用/更新用/削除用）商品グループ画像エンティティリストの編集処理
     *
     * @param goodsGroupImageRegistUpdateDtoList 商品グループ画像登録更新用DTOリスト
     * @param customParams 案件用引数
     */
    protected void makeGoodsGroupImageEntityList(List<GoodsGroupImageRegistUpdateDto> goodsGroupImageRegistUpdateDtoList,
                                                 Object... customParams) {

        // 日付関連Helper取得
        DateUtility dateUtility = ApplicationContextUtility.getBean(DateUtility.class);

        for (GoodsGroupImageRegistUpdateDto goodsGroupImageRegistUpdateDto : goodsGroupImageRegistUpdateDtoList) {

            boolean registFlg = true;
            for (GoodsGroupImageEntity goodsGroupImageEntity : masterGoodsGroupImageEntityList) {

                // 商品グループ画像登録更新用DTO．商品グループ画像エンティティ．画像種別内連番
                // が一致する商品グループ画像エンティティに対する処理
                if (goodsGroupImageEntity.getImageTypeVersionNo()
                                         .equals(goodsGroupImageRegistUpdateDto.getGoodsGroupImageEntity()
                                                                               .getImageTypeVersionNo())) {

                    // (a) 商品グループ画像登録更新用DTO．削除フラグ ＝ "削除"
                    if (goodsGroupImageRegistUpdateDto.getDeleteFlg()) {
                        // 削除ファイルパスリストに追加
                        deleteImageFilePathList.add(
                                        PropertiesUtil.getSystemPropertiesValue("real.images.path.goods") + "/"
                                        + goodsGroupImageEntity.getImageFileName());
                        // 削除リストに追加
                        entityListForDelete.add(goodsGroupImageEntity);
                        registFlg = false;
                        continue;
                    }

                    // (b) 商品グループ画像登録更新用DTO．削除フラグ ≠ "削除" の場合
                    if (goodsGroupImageRegistUpdateDto.getTmpImageFilePath() != null) {
                        // 一時ファイルがある場合
                        // 削除ファイルパスリストに追加
                        deleteImageFilePathList.add(
                                        PropertiesUtil.getSystemPropertiesValue("real.images.path.goods") + "/"
                                        + goodsGroupImageEntity.getImageFileName());
                        // 登録ファイルパスリストに追加
                        FileRegistDto registFilePath = ApplicationContextUtility.getBean(FileRegistDto.class);
                        registFilePath.setFromFilePath(goodsGroupImageRegistUpdateDto.getTmpImageFilePath());
                        registFilePath.setToFilePath(
                                        PropertiesUtil.getSystemPropertiesValue("real.images.path.goods") + "/"
                                        + goodsGroupImageRegistUpdateDto.getImageFileName());
                        registImageFilePathList.add(registFilePath);
                        // ファイル名の設定
                        goodsGroupImageRegistUpdateDto.getGoodsGroupImageEntity()
                                                      .setImageFileName(
                                                                      goodsGroupImageRegistUpdateDto.getImageFileName());
                    }

                    // 更新日時の設定
                    Timestamp currentTime = dateUtility.getCurrentTime();
                    goodsGroupImageRegistUpdateDto.getGoodsGroupImageEntity().setUpdateTime(currentTime);
                    // 更新リストに追加
                    entityListForUpdate.add(goodsGroupImageRegistUpdateDto.getGoodsGroupImageEntity());
                    registFlg = false;
                }
            }

            if (!registFlg || goodsGroupImageRegistUpdateDto.getDeleteFlg()) {
                continue;
            }

            // (c) 商品グループ画像登録更新用DTO．削除フラグ ≠ "削除" かつ
            // 商品グループ画像登録更新用DTO．商品グループ画像エンティティ．画像種別 と
            // 商品グループ画像登録更新用DTO．商品グループ画像エンティティ．画像種別内連番
            // が一致する商品グループ画像エンティティがDBに存在しない場合
            // 登録
            // 登録ファイルパスリストに追加
            FileRegistDto registFilePath = ApplicationContextUtility.getBean(FileRegistDto.class);
            registFilePath.setFromFilePath(goodsGroupImageRegistUpdateDto.getTmpImageFilePath());
            registFilePath.setToFilePath(PropertiesUtil.getSystemPropertiesValue("real.images.path.goods") + "/"
                                         + goodsGroupImageRegistUpdateDto.getImageFileName());
            registImageFilePathList.add(registFilePath);
            // ファイル名の設定
            goodsGroupImageRegistUpdateDto.getGoodsGroupImageEntity()
                                          .setImageFileName(goodsGroupImageRegistUpdateDto.getImageFileName());
            // 登録・更新日時の設定
            Timestamp currentTime = dateUtility.getCurrentTime();
            goodsGroupImageRegistUpdateDto.getGoodsGroupImageEntity().setRegistTime(currentTime);
            goodsGroupImageRegistUpdateDto.getGoodsGroupImageEntity().setUpdateTime(currentTime);
            // 登録リストに追加
            entityListForRegist.add(goodsGroupImageRegistUpdateDto.getGoodsGroupImageEntity());
        }
    }

    /**
     * 商品グループ画像情報の削除処理
     *
     * @param customParams 案件用引数
     */
    protected void deleteGoodsGroupImageEntityList(Object... customParams) {

        for (GoodsGroupImageEntity goodsGroupImageEntity : entityListForDelete) {
            int ret = goodsGroupImageDao.delete(goodsGroupImageEntity);
            goodsGroupImageDelete += ret;
        }
    }

    /**
     * 商品グループ画像情報の更新処理
     *
     * @param customParams 案件用引数
     */
    protected void updateGoodsGroupImageEntityList(Object... customParams) {

        for (GoodsGroupImageEntity goodsGroupImageEntity : entityListForUpdate) {
            int ret = goodsGroupImageDao.update(goodsGroupImageEntity);
            goodsGroupImageUpdate += ret;
        }
    }

    /**
     * 商品グループ画像情報の登録処理
     *
     * @param customParams 案件用引数
     */
    protected void registGoodsGroupImageEntityList(Object... customParams) {

        for (GoodsGroupImageEntity goodsGroupImageEntity : entityListForRegist) {
            int ret = goodsGroupImageDao.insert(goodsGroupImageEntity);
            goodsGroupImageRegist += ret;
        }
    }

    /**
     * 処理結果マップを生成
     *
     * @param customParams 案件用引数
     */
    protected void makeResultMap(Object... customParams) {

        resultMap.put(GOODS_GROUP_IMAGE_REGIST, goodsGroupImageRegist);
        resultMap.put(GOODS_GROUP_IMAGE_UPDATE, goodsGroupImageUpdate);
        resultMap.put(GOODS_GROUP_IMAGE_DELETE, goodsGroupImageDelete);
        resultMap.put(DELETE_IMAGE_FILE_PATH_LIST, deleteImageFilePathList);
        resultMap.put(REGIST_IMAGE_FILE_PATH_LIST, registImageFilePathList);
    }
}