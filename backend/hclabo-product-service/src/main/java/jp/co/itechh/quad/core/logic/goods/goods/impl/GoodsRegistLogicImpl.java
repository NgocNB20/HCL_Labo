/*
 * Project Name : HIT-MALL3
 *
 * Copyright (C) 2008 i-TEC HANKYU HANSHIN INC. All Rights Reserved.
 *
 */

package jp.co.itechh.quad.core.logic.goods.goods.impl;

import jp.co.itechh.quad.core.base.util.common.ArgumentCheckUtil;
import jp.co.itechh.quad.core.base.util.common.DiffUtil;
import jp.co.itechh.quad.core.base.utility.ApplicationContextUtility;
import jp.co.itechh.quad.core.base.utility.DateUtility;
import jp.co.itechh.quad.core.constant.type.HTypeGoodsSaleStatus;
import jp.co.itechh.quad.core.dao.goods.goods.GoodsDao;
import jp.co.itechh.quad.core.entity.goods.goods.GoodsEntity;
import jp.co.itechh.quad.core.logic.AbstractShopLogic;
import jp.co.itechh.quad.core.logic.goods.goods.GoodsRegistLogic;
import jp.co.itechh.quad.core.web.HeaderParamsUtility;
import jp.co.itechh.quad.stock.presentation.api.StockApi;
import jp.co.itechh.quad.stock.presentation.api.param.GoodsRequest;
import jp.co.itechh.quad.stock.presentation.api.param.StockSettingUpdateManagementFlagRequest;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.sql.Timestamp;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * 商品登録
 *
 * @author hirata
 * @author Kaneko (itec) 2012/08/21 UtilからHelperへ変更　Util使用箇所をgetComponentで取得するように変更
 *
 */
@Component
public class GoodsRegistLogicImpl extends AbstractShopLogic implements GoodsRegistLogic {

    /** ロガー */
    private static final Logger LOGGER = LoggerFactory.getLogger(GoodsRegistLogicImpl.class);

    /**
     * 商品DAO
     */
    private final GoodsDao goodsDao;

    /**
     * 在庫サブドメイン
     */
    private final StockApi stockApi;

    /**
     * ヘッダパラメーターユーティル
     */
    private final HeaderParamsUtility headerParamsUtil;

    @Autowired
    public GoodsRegistLogicImpl(GoodsDao goodsDao,
                                StockApi stockApi,
                                HeaderParamsUtility headerParamsUtil,
                                HeaderParamsUtility headerParamsUtil1) {
        this.goodsDao = goodsDao;
        this.stockApi = stockApi;
        this.headerParamsUtil = headerParamsUtil1;

        // サービス間のAPI呼び出す時、ヘッダーにパラメーターを設定する
        headerParamsUtil.setHeader(this.stockApi.getApiClient());
    }

    /**
     *
     * 商品登録
     *
     * @param administratorSeq 管理者SEQ
     * @param goodsGroupSeq 商品グループSEQ
     * @param goodsEntityList 商品エンティティリスト
     * @return 処理件数マップ
     */
    @Override
    public Map<String, Integer> execute(Integer administratorSeq,
                                        Integer goodsGroupSeq,
                                        List<GoodsEntity> goodsEntityList) {

        // (1) パラメータチェック
        checkParameter(goodsGroupSeq, goodsEntityList);

        // (2) 商品情報リストマップ（KEY:商品SEQ）を作成する
        Map<Integer, GoodsEntity> masterGoodsEntityMap = createMasterGoodsEntityMap(goodsGroupSeq);

        // （更新用）商品エンティティリスト
        List<GoodsEntity> entityListForUpdate = new ArrayList<>();
        // （登録用）商品エンティティリスト
        List<GoodsEntity> entityListForRegist = new ArrayList<>();

        // (3) （登録用/更新用）商品エンティティリストの編集
        setEntityList(goodsEntityList, masterGoodsEntityMap, entityListForUpdate, entityListForRegist);

        // 処理件数マップ
        Map<String, Integer> resultMap = new HashMap<>();

        // (4) 商品情報の更新処理
        updateGoodsEntity(administratorSeq, entityListForUpdate, resultMap);

        // (5) 商品情報の登録処理
        registGoodsEntity(entityListForRegist, resultMap);

        updateStock(administratorSeq, entityListForUpdate, resultMap);

        // (6) 戻り値
        return resultMap;
    }

    /**
     * パラメータチェック
     *
     * @param goodsGroupSeq 商品グループSEQ
     * @param goodsEntityList 商品エンティティリスト
     * @param customParams 案件用引数
     */
    protected void checkParameter(Integer goodsGroupSeq, List<GoodsEntity> goodsEntityList, Object... customParams) {
        // 商品グループSEQが null でないかをチェック
        ArgumentCheckUtil.assertNotNull("goodsGroupSeq", goodsGroupSeq);
        // 商品エンティティリストが null でないかをチェック
        ArgumentCheckUtil.assertNotNull("goodsEntityList", goodsEntityList);
        // 商品グループSEQが不一致でないかをチェック
        for (GoodsEntity goodsEntity : goodsEntityList) {
            if (!goodsGroupSeq.equals(goodsEntity.getGoodsGroupSeq())) {
                // 商品グループSEQ不一致エラーを投げる。
                throwMessage(MSGCD_GOODSGROUP_MISMATCH_FAIL);
            }
        }
    }

    /**
     * 商品情報リストマップ（KEY:商品SEQ）を作成する
     *
     * @param goodsGroupSeq 商品グループSEQ
     * @param customParams 案件用引数
     * @return 商品情報リストマップ
     */
    protected Map<Integer, GoodsEntity> createMasterGoodsEntityMap(Integer goodsGroupSeq, Object... customParams) {
        // 商品情報リスト取得
        List<GoodsEntity> masterGoodsEntityList = getMasterGoodsEntityList(goodsGroupSeq);

        // 商品情報リストマップ（KEY:商品SEQ）を作成する
        Map<Integer, GoodsEntity> masterGoodsEntityMap = new HashMap<>();
        for (GoodsEntity goodsEntity : masterGoodsEntityList) {
            masterGoodsEntityMap.put(goodsEntity.getGoodsSeq(), goodsEntity);
        }
        return masterGoodsEntityMap;
    }

    /**
     * 商品情報リスト取得
     *
     * @param goodsGroupSeq 商品グループSEQ
     * @param customParams 案件用引数
     * @return 商品情報リスト
     */
    protected List<GoodsEntity> getMasterGoodsEntityList(Integer goodsGroupSeq, Object... customParams) {
        return goodsDao.getGoodsListByGoodsGroupSeq(goodsGroupSeq);
    }

    /**
     * （登録用/更新用）商品エンティティリストの編集
     *
     * @param goodsEntityList 商品エンティティリスト
     * @param masterGoodsEntityMap （DB）商品エンティティリスト
     * @param entityListForUpdate （更新用）商品エンティティリスト
     * @param entityListForRegist （登録用）商品エンティティリスト
     * @param customParams 案件用引数
     */
    protected void setEntityList(List<GoodsEntity> goodsEntityList,
                                 Map<Integer, GoodsEntity> masterGoodsEntityMap,
                                 List<GoodsEntity> entityListForUpdate,
                                 List<GoodsEntity> entityListForRegist,
                                 Object... customParams) {
        for (GoodsEntity goodsEntity : goodsEntityList) {
            // （パラメータ）商品の商品SEQが存在する場合
            if (masterGoodsEntityMap.get(goodsEntity.getGoodsSeq()) != null) {
                // （更新用）リストに追加
                setEntityListForUpdate(masterGoodsEntityMap, entityListForUpdate, goodsEntity);
            }
            // （パラメータ）関連商品の関連商品グループSEQが存在しない場合
            else {
                // （登録用）リストに追加
                setEntityListForRegist(entityListForRegist, goodsEntity);
            }
        }
    }

    /**
     * （更新用）リストに追加
     *
     * @param masterGoodsEntityMap （DB）商品エンティティリスト
     * @param entityListForUpdate （更新用）商品エンティティリスト
     * @param goodsEntity 商品エンティティ
     * @param customParams 案件用引数
     */
    protected void setEntityListForUpdate(Map<Integer, GoodsEntity> masterGoodsEntityMap,
                                          List<GoodsEntity> entityListForUpdate,
                                          GoodsEntity goodsEntity,
                                          Object... customParams) {
        List<String> diffResult = DiffUtil.diff(masterGoodsEntityMap.get(goodsEntity.getGoodsSeq()), goodsEntity);

        boolean updateFlg = false;
        for (int i = 0; !updateFlg && i < diffResult.size(); i++) {
            // 登録日時・更新日時以外が異なる場合
            if (!diffResult.get(i).endsWith(".registTime") && !diffResult.get(i).endsWith(".updateTime")) {
                // 公開状態が削除の場合、商品番号を再利用可能とするため、商品番号を書き換える
                if (goodsEntity.getSaleStatusPC().equals(HTypeGoodsSaleStatus.DELETED)) {
                    // 書き換えた商品番号が被らないよう、画面から入力不可である「_」+商品テーブル内で一意の「商品SEQ」を設定する
                    goodsEntity.setGoodsCode("_" + goodsEntity.getGoodsSeq());
                }
                // 更新日時の設定
                Timestamp currentTime = ApplicationContextUtility.getBean(DateUtility.class).getCurrentTime();
                goodsEntity.setUpdateTime(currentTime);
                entityListForUpdate.add(goodsEntity);
                updateFlg = true;

            }
        }
    }

    /**
     * （登録用）リストに追加
     *
     * @param entityListForRegist （登録用）商品エンティティリスト
     * @param goodsEntity 商品エンティティ
     * @param customParams 案件用引数
     */
    protected void setEntityListForRegist(List<GoodsEntity> entityListForRegist,
                                          GoodsEntity goodsEntity,
                                          Object... customParams) {
        // 登録・更新日時の設定
        Timestamp currentTime = ApplicationContextUtility.getBean(DateUtility.class).getCurrentTime();
        goodsEntity.setRegistTime(currentTime);
        goodsEntity.setUpdateTime(currentTime);
        // （登録用）リストに追加
        entityListForRegist.add(goodsEntity);
    }

    /**
     * 商品情報の更新処理
     *
     * @param administratorSeq 管理者SEQ
     * @param entityListForUpdate （更新用）商品エンティティリスト
     * @param resultMap 処理件数マップ
     * @param customParams 案件用引数
     */
    protected void updateGoodsEntity(Integer administratorSeq,
                                     List<GoodsEntity> entityListForUpdate,
                                     Map<String, Integer> resultMap,
                                     Object... customParams) {
        // 商品更新件数
        int goodsUpdate = 0;
        for (GoodsEntity goodsEntity : entityListForUpdate) {
            // 更新前の商品情報を取得する
            GoodsEntity tmpGoodsEntity = goodsDao.getEntity(goodsEntity.getGoodsSeq());

            if (HTypeGoodsSaleStatus.DELETED == goodsEntity.getSaleStatusPC()) {
                goodsUpdate += goodsDao.update(getGoodsEntityByOverwritedRequiredItem(goodsEntity));
            } else {
                goodsUpdate += goodsDao.update(goodsEntity);
            }

            if (!tmpGoodsEntity.getStockManagementFlag().equals(goodsEntity.getStockManagementFlag())
                && !goodsEntity.getSaleStatusPC().equals(HTypeGoodsSaleStatus.DELETED)) {
                // 在庫管理フラグが「あり⇔なし」に更新され、かつ更新後の商品販売状態が削除以外の場合、在庫管理フラグ変更の入庫実績を登録

                resultMap.put(SUPPLEMENT_HISTORY_UPDATE.concat(String.valueOf(goodsEntity.getGoodsSeq())), 1);

            }
        }
        resultMap.put(GOODS_UPDATE, goodsUpdate);
    }

    /**
     * 商品情報の登録処理
     *
     * @param entityListForRegist （登録用）商品エンティティリスト
     * @param resultMap 処理件数マップ
     * @param customParams 案件用引数
     */
    protected void registGoodsEntity(List<GoodsEntity> entityListForRegist,
                                     Map<String, Integer> resultMap,
                                     Object... customParams) {
        // 商品登録件数
        int goodsRegist = 0;
        for (GoodsEntity goodsEntity : entityListForRegist) {
            goodsRegist += goodsDao.insert(goodsEntity);
            // 表示順が設定されていない場合(CSVアップロード時)用に表示順をアップデートする
            if (goodsEntity.getOrderDisplay() == null) {
                goodsDao.updateOrderDisplay(goodsEntity.getGoodsGroupSeq(), goodsEntity.getGoodsSeq());
            }
        }
        resultMap.put(GOODS_REGIST, goodsRegist);
    }

    /**
     * Update stock.
     *
     * @param adminId             adminId
     * @param entityListForUpdate the entity list for update
     * @param resultMap           the result map
     */
    protected void updateStock(Integer adminId, List<GoodsEntity> entityListForUpdate, Map<String, Integer> resultMap) {
        for (GoodsEntity goodsEntity : entityListForUpdate) {
            StockSettingUpdateManagementFlagRequest stockSettingUpdateManagementFlagRequest =
                            new StockSettingUpdateManagementFlagRequest();
            stockSettingUpdateManagementFlagRequest.setGoodsSeq(goodsEntity.getGoodsSeq());
            stockSettingUpdateManagementFlagRequest.setStockManagementFlag(
                            goodsEntity.getStockManagementFlag().getValue());

            Integer result = stockApi.stockSettingUpdateManagementFlag(stockSettingUpdateManagementFlagRequest);
            if (result != 1) {
                throwMessage(MSGCD_LOGISTIC_SYNC_MANAGEMENT_FLAG_FAIL);
            }

            if (resultMap.containsKey(SUPPLEMENT_HISTORY_UPDATE.concat(String.valueOf(goodsEntity.getGoodsSeq())))) {
                // 在庫管理フラグが「あり⇔なし」に更新され、かつ更新後の商品販売状態が削除以外の場合、在庫管理フラグ変更の入庫実績を登録
                GoodsRequest goodsRequest = toGoodsRequest(goodsEntity);

                headerParamsUtil.setAdminId(stockApi.getApiClient(), String.valueOf(adminId));

                stockApi.supplementHistory(goodsRequest);
            }
        }
    }

    /**
     * パラメータのエンティティを必須項目をDBの値で上書きして返却する
     * <pre>
     * 本来は新たにエンティティを取得して、販売状態を削除＆更新日時を現在日時で設定し直したエンティティを返却した方が
     * 良いのだが、他への影響が調査し切れないため、エラーを回避するためだけの対応としている
     * </pre>
     * @param goodsEntity 画面で入力した値
     * @return 商品管理：商品登録更新：規格設定画面（admin/goods/registupdate/unit.html）で入力可能な必須項目（最大注文可能数・価格・仕入れ値）の値をDB値で更新したエンティティ
     */
    private GoodsEntity getGoodsEntityByOverwritedRequiredItem(GoodsEntity goodsEntity) {
        GoodsEntity selected = goodsDao.getEntity(goodsEntity.getGoodsSeq());

        if (LOGGER.isDebugEnabled()) {
            LOGGER.debug(String.format("GoodsEntity GoodsCode[%s] GoodsSeq[%s]", goodsEntity.getGoodsCode(),
                                       goodsEntity.getGoodsSeq()
                                      ));
            LOGGER.debug(String.format("更新前GoodsEntity PurchasedMax[%s]", goodsEntity.getPurchasedMax()));
            LOGGER.debug(String.format("更新後GoodsEntity PurchasedMax[%s]", selected.getPurchasedMax()));
        }

        goodsEntity.setPurchasedMax(selected.getPurchasedMax());

        return goodsEntity;
    }

    private GoodsRequest toGoodsRequest(GoodsEntity goodsEntity) {
        GoodsRequest goodsRequest = new GoodsRequest();
        goodsRequest.setGoodsSeq(goodsEntity.getGoodsSeq());
        goodsRequest.setGoodsGroupSeq(goodsEntity.getGoodsGroupSeq());
        goodsRequest.setGoodsCode(goodsEntity.getGoodsCode());
        goodsRequest.setJanCode(goodsEntity.getJanCode());
        goodsRequest.setSaleStatusPC(goodsEntity.getSaleStatusPC().getValue());
        goodsRequest.setSaleStartTimePC(goodsEntity.getSaleStartTimePC());
        goodsRequest.setSaleEndTimePC(goodsEntity.getSaleEndTimePC());
        goodsRequest.setIndividualDeliveryType(goodsEntity.getIndividualDeliveryType().getValue());
        goodsRequest.setFreeDeliveryFlag(goodsEntity.getFreeDeliveryFlag().getValue());
        goodsRequest.setUnitManagementFlag(goodsEntity.getUnitManagementFlag().getValue());
        goodsRequest.setUnitValue1(goodsEntity.getUnitValue1());
        goodsRequest.setUnitValue2(goodsEntity.getUnitValue2());
        goodsRequest.setStockManagementFlag(goodsEntity.getStockManagementFlag().getValue());
        goodsRequest.setPurchasedMax(goodsEntity.getPurchasedMax());
        goodsRequest.setOrderDisplay(goodsEntity.getOrderDisplay());
        goodsRequest.setVersionNo(goodsEntity.getVersionNo());
        goodsRequest.setRegistTime(goodsEntity.getRegistTime());
        return goodsRequest;
    }
}