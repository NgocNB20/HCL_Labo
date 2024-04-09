/*
 * Project Name : HIT-MALL3
 *
 * Copyright (C) 2008 i-TEC HANKYU HANSHIN INC. All Rights Reserved.
 *
 */

package jp.co.itechh.quad.core.logic.goods.goodsgroup.impl;

import jp.co.itechh.quad.core.constant.type.HTypeSiteType;
import jp.co.itechh.quad.core.dao.goods.goodsgroup.GoodsGroupDisplayDao;
import jp.co.itechh.quad.core.dao.goods.goodsgroup.GoodsInformationIconDao;
import jp.co.itechh.quad.core.dto.icon.GoodsInformationIconDetailsDto;
import jp.co.itechh.quad.core.entity.goods.goodsgroup.GoodsGroupDisplayEntity;
import jp.co.itechh.quad.core.logic.AbstractShopLogic;
import jp.co.itechh.quad.core.logic.goods.goodsgroup.GoodsInformationIconMapGetLogic;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.util.CollectionUtils;

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
 *
 * 商品アイコン表示情報Mapを取得する。
 *
 * @author ozaki
 *
 */
@Component
public class GoodsInformationIconMapGetLogicImpl extends AbstractShopLogic implements GoodsInformationIconMapGetLogic {

    /** 商品グループ表示DAO */
    private final GoodsGroupDisplayDao goodsGroupDisplayDao;

    /** 商品インフォメーションアイコンDAO */
    private final GoodsInformationIconDao goodsInformationIconDao;

    @Autowired
    public GoodsInformationIconMapGetLogicImpl(GoodsGroupDisplayDao goodsGroupDisplayDao,
                                               GoodsInformationIconDao goodsInformationIconDao) {
        this.goodsGroupDisplayDao = goodsGroupDisplayDao;
        this.goodsInformationIconDao = goodsInformationIconDao;
    }

    /**
     * 商品インフォメーションアイコンMap取得
     * 商品グループSEQごとにアイコン情報を格納した、MAPを取得する。
     *
     * @param goodsGroupSeqList 商品グループSEQリスト
     * @param siteType サイト区分
     * @return 商品インフォメーションアイコンDTOMap
     */
    @Override
    public Map<Integer, List<GoodsInformationIconDetailsDto>> execute(List<Integer> goodsGroupSeqList,
                                                                      HTypeSiteType siteType) {

        // 商品グループ表示リストを取得
        List<GoodsGroupDisplayEntity> goodsGroupDisplayEntityList = getGoodsGroupDisplayEntityList(goodsGroupSeqList);

        // 商品アイコンSEQリストの作成
        List<Integer> iconSeqList = createIconSeqList(siteType, goodsGroupDisplayEntityList);

        // 商品アイコン詳細MAP作成
        Map<String, GoodsInformationIconDetailsDto> iconDetailsDtoMap = createIconDetailsMap(iconSeqList);

        // アイコン詳細DTOリストマップを作成する
        Map<Integer, List<GoodsInformationIconDetailsDto>> iconDetailsListMap =
                        createIconDetailsListMap(siteType, goodsGroupDisplayEntityList, iconDetailsDtoMap);

        return iconDetailsListMap;
    }

    /**
     * 商品グループ表示リストを取得
     *
     * @param goodsGroupSeqList 商品グループSEQリスト
     * @param customParams 案件用引数
     * @return 商品グループ表示リスト
     */
    protected List<GoodsGroupDisplayEntity> getGoodsGroupDisplayEntityList(List<Integer> goodsGroupSeqList,
                                                                           Object... customParams) {
        return goodsGroupDisplayDao.getGoodsGroupDisplayList(goodsGroupSeqList);
    }

    /**
     * 商品アイコンSEQリストの作成
     *
     * @param siteType サイト区分
     * @param goodsGroupDisplayEntityList 商品グループ表示リスト
     * @param customParams 案件用引数
     * @return 商品アイコンSEQリスト
     */
    protected List<Integer> createIconSeqList(HTypeSiteType siteType,
                                              List<GoodsGroupDisplayEntity> goodsGroupDisplayEntityList,
                                              Object... customParams) {

        Set<Integer> seqSet = new LinkedHashSet<>();

        for (GoodsGroupDisplayEntity display : goodsGroupDisplayEntityList) {

            // サイトに対応したインフォメーションアイコン情報(スラッシュ区切りのシーケンス列挙文字列)を取り出す
            String icons = display.getInformationIconPC();

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
     * 商品アイコン詳細MAP作成
     *
     * @param iconSeqList 商品アイコンSEQリスト
     * @param customParams 案件用引数
     * @return 商品アイコン詳細MAP
     */
    protected Map<String, GoodsInformationIconDetailsDto> createIconDetailsMap(List<Integer> iconSeqList,
                                                                               Object... customParams) {
        Map<String, GoodsInformationIconDetailsDto> iconDetailsDtoMap = new LinkedHashMap<>();
        if (iconSeqList != null && iconSeqList.size() > 0) {
            List<GoodsInformationIconDetailsDto> iconDetailsDtoList = null;

            // 商品アイコン詳細DTOリスト取得
            iconDetailsDtoList = getIconDetailsDtoList(iconSeqList);

            for (GoodsInformationIconDetailsDto iconDetailsDto : iconDetailsDtoList) {
                iconDetailsDtoMap.put(iconDetailsDto.getIconSeq().toString(), iconDetailsDto);
            }
        }
        return iconDetailsDtoMap;
    }

    /**
     * 商品アイコン詳細DTOリスト取得
     *
     * @param iconSeqList 商品アイコンSEQリスト
     * @param customParams 案件用引数
     * @return 商品アイコン詳細DTOリスト
     */
    protected List<GoodsInformationIconDetailsDto> getIconDetailsDtoList(List<Integer> iconSeqList,
                                                                         Object... customParams) {
        return goodsInformationIconDao.getInformationIconList(iconSeqList);
    }

    /**
     * アイコン詳細DTOリストマップを作成する
     *
     * @param siteType サイト区分
     * @param goodsGroupDisplayEntityList 商品グループ表示エンティティリスト
     * @param iconDetailsDtoMap アイコン詳細DTOマップ
     * @param customParams 案件用引数
     * @return アイコン詳細DTOリストマップ
     */
    protected Map<Integer, List<GoodsInformationIconDetailsDto>> createIconDetailsListMap(HTypeSiteType siteType,
                                                                                          List<GoodsGroupDisplayEntity> goodsGroupDisplayEntityList,
                                                                                          Map<String, GoodsInformationIconDetailsDto> iconDetailsDtoMap,
                                                                                          Object... customParams) {
        Map<Integer, List<GoodsInformationIconDetailsDto>> iconMap = new HashMap<>();
        for (GoodsGroupDisplayEntity goodsGroupDisplayEntity : goodsGroupDisplayEntityList) {
            List<GoodsInformationIconDetailsDto> iconDtoList =
                            getIconDtoList(goodsGroupDisplayEntity, iconDetailsDtoMap, siteType);
            if (!CollectionUtils.isEmpty(iconDtoList)) {
                iconMap.put(goodsGroupDisplayEntity.getGoodsGroupSeq(), iconDtoList);
            }
        }
        return iconMap;
    }

    /**
     *
     * 商品インフォメーションアイコンDTOリスト作成
     * 商品グループ表示エンティティより、対象の商品インフォメーションアイコンDTOリストを作成する
     *
     * @param goodsGroupDisplayEntity 商品グループ表示エンティティ
     * @param iconDetailsDtoMap 商品インフォメーションアイコン詳細MAP
     * @param siteType サイト区分
     * @return 商品インフォメーションアイコンDTOリスト
     */
    protected List<GoodsInformationIconDetailsDto> getIconDtoList(GoodsGroupDisplayEntity goodsGroupDisplayEntity,
                                                                  Map<String, GoodsInformationIconDetailsDto> iconDetailsDtoMap,
                                                                  HTypeSiteType siteType) {

        // 対象となるアイコンSEQの配列を作成
        String[] iconSeqArray = null;
        if ((siteType == HTypeSiteType.FRONT_PC) && goodsGroupDisplayEntity.getInformationIconPC() != null) {
            iconSeqArray = goodsGroupDisplayEntity.getInformationIconPC().split("/");
        }

        // アイコンSEQより、商品インフォメーションアイコンリストの作成
        List<GoodsInformationIconDetailsDto> iconDtoList = new ArrayList<>();
        if (iconSeqArray != null) {
            for (Iterator<String> it = iconDetailsDtoMap.keySet().iterator(); it.hasNext(); ) {
                // key:アイコンSEQ
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