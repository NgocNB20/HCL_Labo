/*
 * Project Name : HIT-MALL3
 *
 * Copyright (C) 2008 i-TEC HANKYU HANSHIN INC. All Rights Reserved.
 *
 */

package jp.co.itechh.quad.core.service.noveltypresent.impl;

import jp.co.itechh.quad.core.constant.type.HTypeGoodsSaleStatus;
import jp.co.itechh.quad.core.constant.type.HTypeOpenDeleteStatus;
import jp.co.itechh.quad.core.constant.type.HTypeStockManagementFlag;
import jp.co.itechh.quad.core.dto.goods.goods.GoodsSearchResultDto;
import jp.co.itechh.quad.core.entity.goods.category.CategoryEntity;
import jp.co.itechh.quad.core.entity.goods.goods.GoodsEntity;
import jp.co.itechh.quad.core.entity.goods.goodsgroup.GoodsGroupDisplayEntity;
import jp.co.itechh.quad.core.entity.goods.goodsgroup.GoodsGroupEntity;
import jp.co.itechh.quad.core.entity.goods.goodsgroup.GoodsInformationIconEntity;
import jp.co.itechh.quad.core.logic.goods.category.CategoryListGetLogic;
import jp.co.itechh.quad.core.logic.goods.goods.GoodsGetLogic;
import jp.co.itechh.quad.core.logic.goods.goodsgroup.GoodsGroupDisplayGetLogic;
import jp.co.itechh.quad.core.logic.goods.goodsgroup.GoodsGroupGetLogic;
import jp.co.itechh.quad.core.logic.goods.goodsgroup.GoodsInformationIconGetLogic;
import jp.co.itechh.quad.core.service.AbstractShopService;
import jp.co.itechh.quad.core.service.noveltypresent.NoveltyPresentCheckService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;

/**
 *
 * ノベルティプレゼント登録更新用チェックサービス<br/>
 *
 * @author Tanizaki (Itec) 2013/09/03
 *
 */
@Service
public class NoveltyPresentCheckServiceImpl extends AbstractShopService implements NoveltyPresentCheckService {

    /** 商品取得Logic */
    public GoodsGetLogic goodsGetLogic;

    /** 商品グループ取得Logic */
    public GoodsGroupGetLogic goodsGroupGetLogic;

    /** カテゴリ取得Logic */
    public CategoryListGetLogic categoryListGetLogic;

    /** 商品インフォメーションアイコン取得Logic */
    public GoodsInformationIconGetLogic goodsInformationIconGetLogic;

    /** 商品グループ表示取得Logic */
    public GoodsGroupDisplayGetLogic goodsGroupDisplayGetLogic;

    /**
     * 変換ユーティリティクラス
     */
    @Autowired
    public NoveltyPresentCheckServiceImpl(GoodsGetLogic goodsGetLogic,
                                          GoodsGroupGetLogic goodsGroupGetLogic,
                                          CategoryListGetLogic categoryListGetLogic,
                                          GoodsInformationIconGetLogic goodsInformationIconGetLogic,
                                          GoodsGroupDisplayGetLogic goodsGroupDisplayGetLogic) {
        this.goodsGetLogic = goodsGetLogic;
        this.goodsGroupGetLogic = goodsGroupGetLogic;
        this.categoryListGetLogic = categoryListGetLogic;
        this.goodsInformationIconGetLogic = goodsInformationIconGetLogic;
        this.goodsGroupDisplayGetLogic = goodsGroupDisplayGetLogic;
    }

    /**
     * ノベルティ商品番号のチェックを行う<br/>
     *
     * ノベルティ商品番号の商品マスタが存在しない<br/>
     * or　公開状態=削除<br/>
     * or　販売状態=削除の場合はエラー<br/>
     *
     * @param shopSeq ショップSEQ
     * @param noveltyGoodsCodeList ノベルティ商品番号のList
     * @return エラーとなったノベルティ商品番号
     */
    @Override
    public List<String> checkNoveltyGoods(Integer shopSeq, List<String> noveltyGoodsCodeList) {
        List<GoodsSearchResultDto> resultDtoList =
                        goodsGetLogic.getStatusByGoodsCodeList(shopSeq, noveltyGoodsCodeList);

        if (resultDtoList == null || resultDtoList.isEmpty()) {
            return noveltyGoodsCodeList;
        }

        List<String> retList = new ArrayList<>();

        for (String param : noveltyGoodsCodeList) {
            boolean judge = false;
            for (GoodsSearchResultDto dto : resultDtoList) {
                String goodsCode = dto.getGoodsCode();

                if (goodsCode.equals(param)) {
                    if (HTypeOpenDeleteStatus.NO_OPEN.equals(dto.getGoodsOpenStatusPC())
                        && HTypeGoodsSaleStatus.NO_SALE.equals(dto.getSaleStatusPC()) && (
                                        (BigDecimal.ZERO).compareTo(dto.getGoodsPrice()) == 0)
                        && HTypeStockManagementFlag.ON.equals(dto.getStockmanagementflag())) {
                        judge = true;

                    }
                }
            }

            if (!judge) {
                retList.add(param);
            }
        }

        return retList;
    }

    /**
     * 商品管理番号の存在チェックを行う<br/>
     *
     * ※公開状態、販売状態は判定しない
     *
     * @param shopSeq ショップSEQ
     * @param goodsGroupCodeList 商品管理番号のList
     * @return True:OK、False:NG
     */
    @Override
    public List<String> checkGoodsGroupCode(Integer shopSeq, List<String> goodsGroupCodeList) {

        List<GoodsGroupEntity> entityDtoList =
                        goodsGroupGetLogic.getEntityListByGoodsGroupCodeList(shopSeq, goodsGroupCodeList);

        if (entityDtoList == null || entityDtoList.isEmpty()) {
            return goodsGroupCodeList;
        }

        List<String> retList = new ArrayList<>();
        for (String param : goodsGroupCodeList) {
            boolean judge = false;
            for (GoodsGroupEntity dto : entityDtoList) {
                String goodsGroupCode = dto.getGoodsGroupCode();

                if (goodsGroupCode.equals(param)) {
                    judge = true;
                }
            }

            if (!judge) {
                retList.add(param);
            }
        }

        return retList;
    }

    /**
     * 商品番号の存在チェックを行う<br/>
     *
     * ※公開状態、販売状態は判定しない
     *
     * @param shopSeq ショップSEQ
     * @param goodsCodeList 商品番号のList
     * @return True:OK、False:NG
     */
    @Override
    public List<String> checkGoodsCode(Integer shopSeq, List<String> goodsCodeList) {

        List<GoodsEntity> goodsList = goodsGetLogic.getEntityByGoodsCodeList(shopSeq, goodsCodeList);

        if (goodsList == null || goodsList.isEmpty()) {
            return goodsCodeList;
        }

        List<String> retList = new ArrayList<>();
        for (String param : goodsCodeList) {
            boolean judge = false;
            for (GoodsEntity dto : goodsList) {
                String goodsCode = dto.getGoodsCode();

                if (goodsCode.equals(param)) {
                    judge = true;
                }
            }

            if (!judge) {
                retList.add(param);
            }
        }

        return retList;
    }

    /**
     * カテゴリＩＤの存在チェックを行う<br/>
     *
     * ※公開状態は判定しない
     *
     * @param shopSeq ショップSEQ
     * @param categoryIdList カテゴリＩＤのList
     * @return True:OK、False:NG
     */
    @Override
    public List<String> checkCategoryId(Integer shopSeq, List<String> categoryIdList) {

        List<CategoryEntity> entityDtoList = categoryListGetLogic.getEntityListByIdList(shopSeq, categoryIdList);

        if (entityDtoList == null || entityDtoList.isEmpty()) {
            return categoryIdList;
        }

        List<String> retList = new ArrayList<>();
        for (String param : categoryIdList) {
            boolean judge = false;
            for (CategoryEntity dto : entityDtoList) {
                String categoryId = dto.getCategoryId();

                if (categoryId.equals(param)) {
                    judge = true;
                }
            }

            if (!judge) {
                retList.add(param);
            }
        }

        return retList;
    }

    /**
     * アイコンＩＤの存在チェックを行う<br/>
     *
     * @param iconSeqList アイコンＩＤのList
     * @return True:OK、False:NG
     */
    @Override
    public List<Integer> checkIconId(List<Integer> iconSeqList) {
        List<GoodsInformationIconEntity> entityDtoList =
                        goodsInformationIconGetLogic.getEntityListBySeqList(iconSeqList);

        if (entityDtoList == null || entityDtoList.isEmpty()) {
            return iconSeqList;
        }

        List<Integer> retList = new ArrayList<>();
        for (Integer param : iconSeqList) {
            boolean judge = false;
            for (GoodsInformationIconEntity dto : entityDtoList) {
                Integer iconSeq = dto.getIconSeq();

                if (iconSeq.equals(param)) {
                    judge = true;
                }
            }

            if (!judge) {
                retList.add(param);
            }
        }

        return retList;
    }

    /**
     * 商品名の存在チェックを行う<br/>
     *
     * ※部分一致、公開状態、販売状態は判定不要
     *
     * @param shopSeq ショップSEQ
     * @param goodsNameList 商品名のList
     * @return True:OK、False:NG
     */
    @Override
    public List<String> checkGoodsName(Integer shopSeq, List<String> goodsNameList) {

        List<String> retList = new ArrayList<>();
        for (String goodsName : goodsNameList) {
            List<GoodsEntity> goodsList = goodsGetLogic.getEntityByGoodsName(shopSeq, goodsName);
            if (goodsList == null || goodsList.isEmpty()) {
                retList.add(goodsName);
            }
        }

        return retList;
    }

    /**
     * 検索キーワードの存在チェック<br/>
     *
     * ※部分一致、公開状態、販売状態は判定不要
     *
     * @param shopSeq ショップSEQ
     * @param keywordList 検索キーワードのList
     * @return True:OK、False:NG
     */
    @Override
    public List<String> checkKeyword(Integer shopSeq, List<String> keywordList) {

        List<String> retList = new ArrayList<>();
        for (String searchKeyword : keywordList) {
            List<GoodsGroupDisplayEntity> entityList =
                            goodsGroupDisplayGetLogic.getEntityListByKeyword(shopSeq, searchKeyword);
            if (entityList == null || entityList.isEmpty()) {
                retList.add(searchKeyword);
            }
        }

        return retList;
    }

}