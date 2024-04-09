/*
 * Project Name : HIT-MALL3
 *
 * Copyright (C) 2008 i-TEC HANKYU HANSHIN INC. All Rights Reserved.
 *
 */

package jp.co.itechh.quad.core.service.goods.group.impl;

import jp.co.itechh.quad.core.base.util.common.ArgumentCheckUtil;
import jp.co.itechh.quad.core.constant.type.HTypeSiteType;
import jp.co.itechh.quad.core.dto.goods.goodsgroup.GoodsGroupDto;
import jp.co.itechh.quad.core.dto.goods.goodsgroup.GoodsGroupSearchForDaoConditionDto;
import jp.co.itechh.quad.core.logic.goods.goodsgroup.GoodsGroupListGetLogic;
import jp.co.itechh.quad.core.service.AbstractShopService;
import jp.co.itechh.quad.core.service.goods.group.OpenGoodsGroupSearchService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

/**
 * 公開関商品グループ情報検索
 * 検索条件に該当する公開中の商品情報の商品グループのリストを取得する
 *
 * @author ozaki
 * @version $Revision: 1.3 $
 */
@Service
public class OpenGoodsGroupSearchServiceImpl extends AbstractShopService implements OpenGoodsGroupSearchService {

    /**
     * 商品グループリスト取得ロジッククラス
     */
    private final GoodsGroupListGetLogic goodsGroupListGetLogic;

    @Autowired
    public OpenGoodsGroupSearchServiceImpl(GoodsGroupListGetLogic goodsGroupListGetLogic) {
        this.goodsGroupListGetLogic = goodsGroupListGetLogic;
    }

    /**
     * 公開関商品グループ情報検索
     * 検索条件に該当する公開中の商品情報の商品グループのリストを取得する
     *
     * @param siteType     サイト種別
     * @param conditionDto 商品グループ検索条件DTO
     * @return 商品グループDTOリスト
     */
    @Override
    public List<GoodsGroupDto> execute(HTypeSiteType siteType, GoodsGroupSearchForDaoConditionDto conditionDto) {
        //
        // (1) パラメータチェック
        ArgumentCheckUtil.assertNotNull("conditionDto", conditionDto);

        // (2) 共通情報チェック
        // ・ショップSEQ ： null(or空文字) の場合 エラーとして処理を終了する
        // ・サイト区分 ： null(or空文字) の場合 エラーとして処理を終了する
        Integer shopSeq = 1001;

        // (3) 商品情報検索処理実行
        // ・商品グループDao用検索条件Dtoを作成する
        // ・商品グループDao用検索条件Dto
        // ‥ショップSEQ =共通情報.ショップSEQ
        // ‥サイト区分 =共通情報.サイト区分
        conditionDto.setShopSeq(shopSeq);
        conditionDto.setSiteType(siteType);

        // ･商品情報検索処理実行
        // ※『logic基本設計書（商品グループリスト取得（検索））.xls』参照
        // Logic GoodsGroupListGetLogic
        // パラメータ 商品グループDao用検索条件Dto
        // (公開状態=公開中)
        // 戻り値 商品グループDTOリスト
        List<GoodsGroupDto> goodsGroupDtoList = goodsGroupListGetLogic.execute(conditionDto);

        return goodsGroupDtoList;
    }

}
