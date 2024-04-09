/*
 * Project Name : HIT-MALL3
 *
 * Copyright (C) 2008 i-TEC HANKYU HANSHIN INC. All Rights Reserved.
 *
 */

package jp.co.itechh.quad.core.logic.goods.goodsgroup.impl;

import jp.co.itechh.quad.core.base.util.common.ArgumentCheckUtil;
import jp.co.itechh.quad.core.base.util.common.CollectionUtil;
import jp.co.itechh.quad.core.dao.goods.goodsgroup.GoodsGroupDao;
import jp.co.itechh.quad.core.dto.goods.goods.GoodsDto;
import jp.co.itechh.quad.core.dto.goods.goodsgroup.GoodsGroupDto;
import jp.co.itechh.quad.core.dto.icon.GoodsInformationIconDetailsDto;
import jp.co.itechh.quad.core.entity.goods.goods.GoodsEntity;
import jp.co.itechh.quad.core.entity.goods.goodsgroup.GoodsGroupDisplayEntity;
import jp.co.itechh.quad.core.entity.goods.goodsgroup.GoodsGroupEntity;
import jp.co.itechh.quad.core.entity.goods.goodsgroup.GoodsGroupImageEntity;
import jp.co.itechh.quad.core.entity.goods.goodsgroup.stock.StockStatusDisplayEntity;
import jp.co.itechh.quad.core.logic.AbstractShopLogic;
import jp.co.itechh.quad.core.logic.goods.goodsgroup.GoodsGroupDisplayMapGetLogic;
import jp.co.itechh.quad.core.logic.goods.goodsgroup.GoodsGroupImageGetLogic;
import jp.co.itechh.quad.core.logic.goods.goodsgroup.GoodsGroupMapGetLogic;
import jp.co.itechh.quad.core.logic.goods.goodsgroup.GoodsInformationIconGetLogic;
import jp.co.itechh.quad.core.logic.goods.stock.StockStatusDisplayMapGetLogic;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.Iterator;
import java.util.LinkedHashMap;
import java.util.LinkedHashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;

/**
 * 商品グループマップ取得
 *
 * @author ozaki
 */
@Component
public class GoodsGroupMapGetLogicImpl extends AbstractShopLogic implements GoodsGroupMapGetLogic {

    /** 商品グループDAO */
    private final GoodsGroupDao goodsGroupDao;

    /** 商品グループ表示情報取得ロジッククラス */
    private final GoodsGroupDisplayMapGetLogic goodsGroupDisplayMapGetLogic;

    /** 商品グループ画像取得ロジッククラス */
    private final GoodsGroupImageGetLogic goodsGroupImageGetLogic;

    /** 商品インフォメーションアイコン情報取得ロジッククラス */
    private final GoodsInformationIconGetLogic goodsInformationIconGetLogic;

    /** 在庫状態表示リスト取得ロジッククラス */
    private final StockStatusDisplayMapGetLogic stockStatusDisplayMapGetLogic;

    @Autowired
    public GoodsGroupMapGetLogicImpl(GoodsGroupDao goodsGroupDao,
                                     GoodsGroupDisplayMapGetLogic goodsGroupDisplayMapGetLogic,
                                     GoodsGroupImageGetLogic goodsGroupImageGetLogic,
                                     GoodsInformationIconGetLogic goodsInformationIconGetLogic,
                                     StockStatusDisplayMapGetLogic stockStatusDisplayMapGetLogic) {
        this.goodsGroupDao = goodsGroupDao;
        this.goodsGroupDisplayMapGetLogic = goodsGroupDisplayMapGetLogic;
        this.goodsGroupImageGetLogic = goodsGroupImageGetLogic;
        this.goodsInformationIconGetLogic = goodsInformationIconGetLogic;
        this.stockStatusDisplayMapGetLogic = stockStatusDisplayMapGetLogic;
    }

    /**
     * 商品グループマップ取得
     * 商品グループDTOを保持するマップを取得する。
     *
     * @param goodsGroupSeqList 商品グループSEQリスト
     * @return 商品グループMAP
     */
    @Override
    public Map<Integer, GoodsGroupDto> execute(List<Integer> goodsGroupSeqList) {

        // (1) パラメータチェック
        checkParameter(goodsGroupSeqList);

        // (2) 商品グループエンティティリスト取得
        List<GoodsGroupEntity> goodsGroupEntityList = getGoodsGroupEntityList(goodsGroupSeqList);

        // 商品SEQリストの作成
        List<Integer> newGoodsGroupSeqList = createNewGoodsGroupSeqList(goodsGroupEntityList);

        // (3) 商品グループ表示情報取得
        Map<Integer, GoodsGroupDisplayEntity> goodsGroupDisplayDtoMap = getGoodsGroupDisplayDtoMap(goodsGroupSeqList);

        // (4) 商品グループ画像情報取得
        Map<Integer, List<GoodsGroupImageEntity>> imageMap = getImageMap(newGoodsGroupSeqList);

        // 商品アイコンSEQリストの作成
        List<Integer> iconSeqList = createIconSeqList(goodsGroupDisplayDtoMap);

        // (5) 商品アイコンMAP取得
        Map<String, GoodsInformationIconDetailsDto> iconDetailsDtoMap =
                        getGoodsInformationIconDetailsDtoMap(iconSeqList);

        // (6) 商品グループ在庫表示MAP取得
        Map<Integer, StockStatusDisplayEntity> stockStatusDisplayMap = getStockStatusDisplayMap(newGoodsGroupSeqList);

        // (7) 商品グループMapを作成する。
        Map<Integer, GoodsGroupDto> goodsGroupMap =
                        createGoodsGroupMap(goodsGroupEntityList, goodsGroupDisplayDtoMap, imageMap, iconDetailsDtoMap,
                                            stockStatusDisplayMap
                                           );

        // (8)商品グループマップを返す。
        return goodsGroupMap;
    }

    /**
     * 商品グループエンティティリスト取得
     *
     * @param goodsGroupSeqList 商品グループSEQリスト
     * @param customParams 案件用引数
     * @return 商品グループエンティティリスト
     */
    protected List<GoodsGroupEntity> getGoodsGroupEntityList(List<Integer> goodsGroupSeqList, Object... customParams) {
        return goodsGroupDao.getGoodsGroupList(goodsGroupSeqList);
    }

    /**
     * 商品SEQリストの再作成
     *
     * @param goodsGroupEntityList 商品グループエンティティlリスト
     * @param customParams 案件用引数
     * @return 再作成した商品SEQリスト
     */
    protected List<Integer> createNewGoodsGroupSeqList(List<GoodsGroupEntity> goodsGroupEntityList,
                                                       Object... customParams) {
        List<Integer> newGoodsGroupSeqList = new ArrayList<>();
        for (GoodsGroupEntity goodsGroupEntity : goodsGroupEntityList) {
            newGoodsGroupSeqList.add(goodsGroupEntity.getGoodsGroupSeq());
        }
        return newGoodsGroupSeqList;
    }

    /**
     * 商品グループ表示情報取得
     *
     * @param goodsGroupSeqList 商品グループSEQリスト
     * @param customParams 案件用引数
     * @return 商品グループ表示情報
     */
    protected Map<Integer, GoodsGroupDisplayEntity> getGoodsGroupDisplayDtoMap(List<Integer> goodsGroupSeqList,
                                                                               Object... customParams) {
        // goodsGroupDisplayDtoMapには、シーケンスとエンティティのペアが格納されている。
        // シーケンスに対応するエンティティが存在しない場合、mapにそのシーケンスは存在しない。
        // したがって、goodsGroupDisplayDtoMapからシーケンスをキーに取得したエンティティが null ということはあり得ない。
        return goodsGroupDisplayMapGetLogic.execute(goodsGroupSeqList);
    }

    /**
     * 商品グループ画像情報取得
     *
     * @param newGoodsGroupSeqList 再作成した商品SEQリスト
     * @param customParams 案件用引数
     * @return 商品グループ画像情報
     */
    protected Map<Integer, List<GoodsGroupImageEntity>> getImageMap(List<Integer> newGoodsGroupSeqList,
                                                                    Object... customParams) {
        return goodsGroupImageGetLogic.execute(newGoodsGroupSeqList);
    }

    /**
     * 商品アイコンSEQリスト作成
     *
     * @param goodsGroupDisplayDtoMap 商品グループ表示DTOマップ
     * @param customParams 案件用引数
     * @return 商品アイコンSEQリスト
     */
    protected List<Integer> createIconSeqList(Map<Integer, GoodsGroupDisplayEntity> goodsGroupDisplayDtoMap,
                                              Object... customParams) {

        Set<Integer> seqSet = new LinkedHashSet<>();

        for (GoodsGroupDisplayEntity display : goodsGroupDisplayDtoMap.values()) {

            // サイトに対応したインフォメーションアイコン情報(スラッシュ区切りのシーケンス列挙文字列)を取り出す
            String icons = null;

            icons = display.getInformationIconPC();

            if (icons == null) {
                // サイトに対応したシーケンス列挙文字列が設定されていない場合は処理しない
                continue;
            }

            // シーケンス列挙文字列を個々のシーケンスにバラして Integer 型で Set に保持する。
            // ※空もしくは Integer 変換可能文字列でない場合はそのまま例外を発生させる。
            for (String seq : Arrays.asList(icons.split("/+"))) {
                seqSet.add(Integer.parseInt(seq));
            }
        }

        // シーケンスは List として返却する
        return new ArrayList<>(seqSet);
    }

    /**
     * 商品アイコンMAP取得
     *
     * @param iconSeqList アイコンSEQリスト
     * @param customParams 案件用引数
     * @return 商品アイコンMAP
     */
    protected Map<String, GoodsInformationIconDetailsDto> getGoodsInformationIconDetailsDtoMap(List<Integer> iconSeqList,
                                                                                               Object... customParams) {
        Map<String, GoodsInformationIconDetailsDto> iconDetailsDtoMap = new LinkedHashMap<>();
        if (CollectionUtil.isNotEmpty(iconSeqList)) {
            List<GoodsInformationIconDetailsDto> iconDetailsDtoList = null;

            iconDetailsDtoList = goodsInformationIconGetLogic.execute(iconSeqList);

            for (GoodsInformationIconDetailsDto iconDetailsDto : iconDetailsDtoList) {
                iconDetailsDtoMap.put(iconDetailsDto.getIconSeq().toString(), iconDetailsDto);
            }
        }
        return iconDetailsDtoMap;
    }

    /**
     * 在庫状態表示MAP取得
     * @param goodsGroupSeqList 商品グループシーケンスリスト
     * @param customParams 案件用引数
     * @return 在庫状態表示MAP
     */
    protected Map<Integer, StockStatusDisplayEntity> getStockStatusDisplayMap(List<Integer> goodsGroupSeqList,
                                                                              Object... customParams) {
        return stockStatusDisplayMapGetLogic.execute(goodsGroupSeqList);
    }

    /**
     * パラメータチェック
     *
     * @param goodsGroupSeqList 商品グループSEQリスト
     * @param customParams 案件用引数
     */
    protected void checkParameter(List<Integer> goodsGroupSeqList, Object... customParams) {
        ArgumentCheckUtil.assertNotNull("goodsGroupSeqList", goodsGroupSeqList);
    }

    /**
     * 商品グループMap作成
     *
     * @param goodsGroupEntityList 商品グループエンティティリスト
     * @param goodsGroupDisplayDtoMap 商品グループ表示DTOマップ
     * @param imageMap 画像マップ
     * @param iconDetailsDtoMap アイコン詳細DTOマップ
     * @param stockStatusDisplayMap 在庫状態表示マップ
     * @param customParams 案件用引数
     * @return 商品グループMap
     */
    protected Map<Integer, GoodsGroupDto> createGoodsGroupMap(List<GoodsGroupEntity> goodsGroupEntityList,
                                                              Map<Integer, GoodsGroupDisplayEntity> goodsGroupDisplayDtoMap,
                                                              Map<Integer, List<GoodsGroupImageEntity>> imageMap,
                                                              Map<String, GoodsInformationIconDetailsDto> iconDetailsDtoMap,
                                                              Map<Integer, StockStatusDisplayEntity> stockStatusDisplayMap,
                                                              Object... customParams) {
        // 商品グループ、商品グループ表示、商品グループ画像、商品インフォメーションアイコン情報、在庫上表表示情報が設定されている
        Map<Integer, GoodsGroupDto> goodsGroupMap = new HashMap<>();
        for (GoodsGroupEntity goodsGroupEntity : goodsGroupEntityList) {
            GoodsGroupDisplayEntity goodsGroupDisplayEntity =
                            goodsGroupDisplayDtoMap.get(goodsGroupEntity.getGoodsGroupSeq());
            List<GoodsGroupImageEntity> imageEntityList = imageMap.get(goodsGroupEntity.getGoodsGroupSeq());
            List<GoodsInformationIconDetailsDto> iconDtoList =
                            getIconDtoList(goodsGroupDisplayEntity, iconDetailsDtoMap);
            List<GoodsDto> goodsDtoList = getGoodsDtoList();
            StockStatusDisplayEntity stockStatusDisplayEntity =
                            stockStatusDisplayMap.get(goodsGroupEntity.getGoodsGroupSeq());

            // 商品グループ情報DTOの作成
            GoodsGroupDto goodsGroupDto = getComponent(GoodsGroupDto.class);
            goodsGroupDto.setGoodsGroupEntity(goodsGroupEntity);
            goodsGroupDto.setGoodsGroupDisplayEntity(goodsGroupDisplayEntity);
            goodsGroupDto.setGoodsDtoList(goodsDtoList);
            goodsGroupDto.setGoodsGroupImageEntityList(imageEntityList);
            goodsGroupDto.setGoodsInformationIconDetailsDtoList(iconDtoList);
            goodsGroupDto.setBatchUpdateStockStatus(stockStatusDisplayEntity);

            goodsGroupMap.put(goodsGroupEntity.getGoodsGroupSeq(), goodsGroupDto);
        }
        return goodsGroupMap;
    }

    /**
     *
     * 商品DTOリスト作成
     * フロント展示用商品情報Dto、商品DTOリストを作成する
     *
     * @return 商品DTOリスト
     */
    protected List<GoodsDto> getGoodsDtoList() {
        GoodsEntity goodsEntity = getComponent(GoodsEntity.class);
        GoodsDto goodsDto = getComponent(GoodsDto.class);
        List<GoodsDto> goodsDtoList = new ArrayList<>();

        goodsDto.setGoodsEntity(goodsEntity);

        goodsDtoList.add(goodsDto);

        return goodsDtoList;
    }

    /**
     *
     * 商品インフォメーションアイコンDTOリスト作成
     * 商品グループ表示エンティティより、対象の商品インフォメーションアイコンDTOリストを作成する
     *
     * @param goodsGroupDisplayEntity 商品グループ表示エンティティ
     * @param iconDetailsDtoMap 商品インフォメーションアイコン詳細MAP
     * @return 商品インフォメーションアイコンDTOリスト
     */
    protected List<GoodsInformationIconDetailsDto> getIconDtoList(GoodsGroupDisplayEntity goodsGroupDisplayEntity,
                                                                  Map<String, GoodsInformationIconDetailsDto> iconDetailsDtoMap) {
        // アイコンSEQより、商品インフォメーションアイコンリストの作成
        List<GoodsInformationIconDetailsDto> iconDtoList = new ArrayList<>();
        if (StringUtils.isEmpty(goodsGroupDisplayEntity.getInformationIconPC())) {
            return iconDtoList;
        }

        // 対象となるアイコンSEQの配列を作成
        String[] iconSeqArray = goodsGroupDisplayEntity.getInformationIconPC().split("/");

        if (iconSeqArray != null) {

            for (Iterator<String> it = iconDetailsDtoMap.keySet().iterator(); it.hasNext(); ) {
                String key = it.next();

                for (int i = 0; i < iconSeqArray.length; i++) {
                    if (key.equals(iconSeqArray[i])) {
                        iconDtoList.add(iconDetailsDtoMap.get(iconSeqArray[i]));
                    }
                }
            }
        }

        return iconDtoList;
    }

}