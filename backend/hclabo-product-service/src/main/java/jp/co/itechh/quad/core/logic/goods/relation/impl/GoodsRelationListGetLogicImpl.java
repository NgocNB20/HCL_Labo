/*
 * Project Name : HIT-MALL3
 *
 * Copyright (C) 2008 i-TEC HANKYU HANSHIN INC. All Rights Reserved.
 *
 */

package jp.co.itechh.quad.core.logic.goods.relation.impl;

import jp.co.itechh.quad.core.base.util.common.ArgumentCheckUtil;
import jp.co.itechh.quad.core.base.util.common.CollectionUtil;
import jp.co.itechh.quad.core.base.utility.ApplicationContextUtility;
import jp.co.itechh.quad.core.dao.goods.goodsgroup.GoodsRelationDao;
import jp.co.itechh.quad.core.dto.goods.goodsgroup.GoodsGroupDto;
import jp.co.itechh.quad.core.dto.goods.goodsgroup.GoodsGroupSearchForDaoConditionDto;
import jp.co.itechh.quad.core.dto.goods.goodsgroup.GoodsRelationDto;
import jp.co.itechh.quad.core.dto.goods.goodsgroup.GoodsRelationSearchForDaoConditionDto;
import jp.co.itechh.quad.core.entity.goods.goodsgroup.GoodsRelationEntity;
import jp.co.itechh.quad.core.logic.AbstractShopLogic;
import jp.co.itechh.quad.core.logic.goods.goodsgroup.GoodsGroupListGetLogic;
import jp.co.itechh.quad.core.logic.goods.relation.GoodsRelationListGetLogic;
import jp.co.itechh.quad.core.web.PageInfoModule;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.ArrayList;
import java.util.List;

/**
 *
 * 関連商品詳細情報リスト取得
 * 対象商品の関連商品リスト（商品Dtoリスト）を取得する。
 *
 * @author ozaki
 * @version $Revision: 1.5 $
 *
 */
@Component
public class GoodsRelationListGetLogicImpl extends AbstractShopLogic implements GoodsRelationListGetLogic {

    /** 関連商品DAO */
    private final GoodsRelationDao goodsRelationDao;

    /** 商品グループリスト取得ロジッククラス */
    private final GoodsGroupListGetLogic goodsGroupListGetLogic;

    @Autowired
    public GoodsRelationListGetLogicImpl(GoodsRelationDao goodsRelationDao,
                                         GoodsGroupListGetLogic goodsGroupListGetLogic) {
        this.goodsRelationDao = goodsRelationDao;
        this.goodsGroupListGetLogic = goodsGroupListGetLogic;
    }

    /**
     * 関連商品詳細情報リスト取得
     *
     * @param goodsRelationSearchForDaoConditionDto 関連商品Dao用検索条件Dto
     * @return 関連商品情報リスト
     */
    @Override
    public List<GoodsRelationDto> execute(GoodsRelationSearchForDaoConditionDto goodsRelationSearchForDaoConditionDto) {

        // (1) パラメータチェック
        // 関連商品Dao用検索条件DTOが null でないことをチェック
        ArgumentCheckUtil.assertNotNull("goodsRelationSearchForDaoConditionDto", goodsRelationSearchForDaoConditionDto);

        // (2) 関連商品リスト取得
        // 関連商品Daoの関連商品マップ検索処理を実行する。
        List<GoodsRelationEntity> goodsRelationList =
                        goodsRelationDao.getSearchGoodsRelation(goodsRelationSearchForDaoConditionDto,
                                                                goodsRelationSearchForDaoConditionDto.getPageInfo()
                                                                                                     .getSelectOptions()
                                                               );
        if (CollectionUtil.isEmpty(goodsRelationList)) {
            return null;
        }

        // (3) 商品詳細DTO取得
        // (2) で取得した関連商品エンティティリストから関連商品エンティティ．商品SEQのリストを作成する。
        List<Integer> goodsGroupSeqList = new ArrayList<>();
        for (GoodsRelationEntity entity : goodsRelationList) {
            goodsGroupSeqList.add(entity.getGoodsRelationGroupSeq());
        }

        // 共通ロジックを使用するので、コンディションを入れ替え
        GoodsGroupSearchForDaoConditionDto conditionDto =
                        changeCondition(goodsRelationSearchForDaoConditionDto, goodsGroupSeqList);
        // PageInfoモジュール取得
        PageInfoModule pageInfoModule = ApplicationContextUtility.getBean(PageInfoModule.class);
        // ページングセットアップ
        conditionDto = pageInfoModule.setupPageInfoForSkipCount(conditionDto,
                                                                goodsRelationSearchForDaoConditionDto.getPageInfo()
                                                                                                     .getLimit()
                                                               );
        // 商品の詳細情報取得
        List<GoodsGroupDto> goodsGroupDtoList = goodsGroupListGetLogic.execute(conditionDto);

        if (CollectionUtil.isEmpty(goodsGroupDtoList)) {
            return null;
        }

        // (5) 戻り値
        // 商品情報が取得できた場合は、関連商品の表示順に並び変えて関連商品DTOリストと商品グループDTOリストを返却する
        return sortRelationOrderDisplay(goodsRelationList, goodsGroupDtoList);
    }

    /**
     * コンディションを入れ替え
     * ※商品グループDTOリスト取得のためのロジックが異なるコンディションクラスなので入れ替える
     *
     * @param goodsRelationSearchForDaoConditionDto 関連商品Dao用検索条件Dto
     * @param goodsGroupSeqList 商品グループSEQリスト
     * @return 商品グループDAO用検索条件DTO
     */
    protected GoodsGroupSearchForDaoConditionDto changeCondition(GoodsRelationSearchForDaoConditionDto goodsRelationSearchForDaoConditionDto,
                                                                 List<Integer> goodsGroupSeqList) {
        GoodsGroupSearchForDaoConditionDto conditionDto =
                        ApplicationContextUtility.getBean(GoodsGroupSearchForDaoConditionDto.class);
        conditionDto.setGoodsGroupSeqList(goodsGroupSeqList);
        conditionDto.setSiteType(goodsRelationSearchForDaoConditionDto.getSiteType());
        conditionDto.setOpenStatus(goodsRelationSearchForDaoConditionDto.getOpenStatus());
        conditionDto.setFrontDisplayReferenceDate(goodsRelationSearchForDaoConditionDto.getFrontDisplayReferenceDate());
        return conditionDto;
    }

    /**
     * 関連商品情報と商品グループ情報を関連商品の並び順でソートし、返却用Dtoにセット
     *
     * @param goodsRelationEntityList 関連商品エンティティリスト
     * @param goodsGroupDtoList 並び変え前の商品グループDtoリスト
     * @return 関連商品の並び順に並び替えた関連商品Dtoリスト
     */
    protected List<GoodsRelationDto> sortRelationOrderDisplay(List<GoodsRelationEntity> goodsRelationEntityList,
                                                              List<GoodsGroupDto> goodsGroupDtoList) {
        List<GoodsRelationDto> sortGoodsGroupDtoList = new ArrayList<>();
        for (GoodsRelationEntity goodsRelationEntity : goodsRelationEntityList) {
            for (GoodsGroupDto goodsGroupDto : goodsGroupDtoList) {
                // 並び順を保持した関連商品グループSEQ と 商品グループDto.商品グループSEQ が一致する場合は、返却用の商品グループDtoリストに追加する
                if (goodsRelationEntity.getGoodsRelationGroupSeq() != null &&
                    goodsRelationEntity.getGoodsRelationGroupSeq()
                                       .compareTo(goodsGroupDto.getGoodsGroupEntity().getGoodsGroupSeq()) == 0) {
                    GoodsRelationDto dto = ApplicationContextUtility.getBean(GoodsRelationDto.class);
                    // 関連商品エンティティテーブル外項目を設定
                    goodsRelationEntity.setGoodsGroupCode(goodsGroupDto.getGoodsGroupEntity().getGoodsGroupCode());
                    goodsRelationEntity.setGoodsGroupName(goodsGroupDto.getGoodsGroupEntity().getGoodsGroupName());
                    goodsRelationEntity.setGoodsOpenStatusPC(
                                    goodsGroupDto.getGoodsGroupEntity().getGoodsOpenStatusPC());
                    dto.setGoodsRelationEntity(goodsRelationEntity);
                    dto.setGoodsGroupDto(goodsGroupDto);
                    sortGoodsGroupDtoList.add(dto);
                    break;
                }
            }
        }
        return sortGoodsGroupDtoList;
    }
}