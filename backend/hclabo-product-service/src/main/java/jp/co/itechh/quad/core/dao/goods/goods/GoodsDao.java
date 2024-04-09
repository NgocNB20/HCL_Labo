/*
 * Project Name : HIT-MALL4
 *
 * Copyright (C) 2021 i-TEC HANKYU HANSHIN INC. All Rights Reserved.
 *
 */

package jp.co.itechh.quad.core.dao.goods.goods;

import jp.co.itechh.quad.core.constant.type.HTypeOpenDeleteStatus;
import jp.co.itechh.quad.core.constant.type.HTypeSiteType;
import jp.co.itechh.quad.core.dto.goods.goods.GoodsCsvDto;
import jp.co.itechh.quad.core.dto.goods.goods.GoodsDetailsDto;
import jp.co.itechh.quad.core.dto.goods.goods.GoodsSearchForBackDaoConditionDto;
import jp.co.itechh.quad.core.dto.goods.goods.GoodsSearchForDaoConditionDto;
import jp.co.itechh.quad.core.dto.goods.goods.GoodsSearchResultDto;
import jp.co.itechh.quad.core.dto.goods.goods.GoodsSearchResultForOrderRegistDto;
import jp.co.itechh.quad.core.dto.goods.goods.GoodsUnitDto;
import jp.co.itechh.quad.core.dto.shop.noveltypresent.NoveltyGoodsCountConditionDto;
import jp.co.itechh.quad.core.entity.goods.goods.GoodsEntity;
import org.seasar.doma.Dao;
import org.seasar.doma.Delete;
import org.seasar.doma.Insert;
import org.seasar.doma.Script;
import org.seasar.doma.Select;
import org.seasar.doma.Update;
import org.seasar.doma.boot.ConfigAutowireable;
import org.seasar.doma.jdbc.SelectOptions;

import java.util.List;
import java.util.stream.Stream;

/**
 * 商品Daoクラス
 *
 * @author Pham Quang Dieu (VTI Japan Co., Ltd.)
 */
@Dao
@ConfigAutowireable
public interface GoodsDao {

    /**
     * 商品SEQ採番
     *
     * @return 商品SEQ
     */
    @Select
    int getGoodsSeqNextVal();

    /**
     * デリート
     *
     * @param goodsEntity 商品エンティティ
     * @return 処理件数
     */
    @Delete
    int delete(GoodsEntity goodsEntity);

    /**
     * インサート
     *
     * @param goodsEntity 商品エンティティ
     * @return 処理件数
     */
    @Insert(excludeNull = true)
    int insert(GoodsEntity goodsEntity);

    /**
     * アップデート
     *
     * @param goodsEntity 商品エンティティ
     * @return 処理件数
     */
    @Update
    int update(GoodsEntity goodsEntity);

    /**
     * エンティティ取得
     *
     * @param goodsSeq 商品SEQ
     * @return 商品エンティティ
     */
    @Select
    GoodsEntity getEntity(Integer goodsSeq);

    /**
     * 商品情報取得
     *
     * @param shopSeq   ショップSEQ
     * @param goodsCode 商品コード
     * @return 商品エンティティ
     */
    @Select
    GoodsEntity getGoodsByCode(Integer shopSeq, String goodsCode);

    /**
     * 商品情報一覧取得
     *
     * @param conditionDto 商品検索条件DTO
     * @return 商品エンティティリスト
     */
    @Select
    List<GoodsEntity> getSearchGoodsList(GoodsSearchForDaoConditionDto conditionDto);

    /**
     * 商品検索結果一覧取得
     *
     * @param conditionDto  商品検索条件DTO(管理機能用)
     * @param selectOptions 検索オプション
     * @return 商品検索結果DTOリスト
     */
    @Select
    List<GoodsSearchResultDto> getSearchGoodsForBackList(GoodsSearchForBackDaoConditionDto conditionDto,
                                                         SelectOptions selectOptions);

    /**
     * 商品詳細情報一覧取得
     *
     * @param goodsSeqList 商品SEQリスト
     * @return 商品詳細DTOリスト
     */
    @Select
    List<GoodsDetailsDto> getGoodsDetailsList(List<Integer> goodsSeqList);

    /**
     * 新規受注登録用の商品検索結果一覧取得
     *
     * @param conditionDto  商品検索条件DTO(管理機能用)
     * @param selectOptions 検索オプション
     * @return 商品検索結果DTOリスト
     */
    @Select
    List<GoodsSearchResultForOrderRegistDto> getSearchGoodsForOrderRegist(GoodsSearchForBackDaoConditionDto conditionDto,
                                                                          SelectOptions selectOptions);

    /**
     * 商品情報リスト取得（商品グループSEQ）
     *
     * @param goodsGroupSeq 商品グループSEQリスト
     * @return 商品エンティティリスト
     */
    @Select
    List<GoodsEntity> getGoodsListByGoodsGroupSeq(Integer goodsGroupSeq);

    /**
     * 商品SEQリスト取得（商品グループSEQ）
     *
     * @param goodsGroupSeq 商品グループSEQリスト
     * @return 商品SEQリスト
     */
    @Select
    List<Integer> getGoodsSeqListByGoodsGroupSeq(Integer goodsGroupSeq);

    /**
     * 商品SEQ指定、商品詳細情報の取得
     *
     * @param goodsSeq 商品SEQ
     * @return 商品詳細Dtoクラス
     */
    @Select
    GoodsDetailsDto getGoodsDetailsByGoodsSeq(Integer goodsSeq);

    /**
     * 商品コード指定、商品詳細情報の取得
     *
     * @param shopSeq         ショップSEQ
     * @param goodsCode       商品コード
     * @param siteType        サイト区分
     * @param goodsOpenStatus 商品公開状態
     * @return 商品詳細DTO
     */
    @Select
    GoodsDetailsDto getGoodsDetailsByShopSeqAndCode(Integer shopSeq,
                                                    String goodsCode,
                                                    HTypeSiteType siteType,
                                                    HTypeOpenDeleteStatus goodsOpenStatus);

    /**
     * 商品一括CSVダウンロード
     *
     * @param conditionDto 商品検索条件DTO(管理機能用)
     * @return ダウンロード取得
     */
    @Select
    Stream<GoodsCsvDto> exportCsvByGoodsSearchForBackDaoConditionDto(GoodsSearchForBackDaoConditionDto conditionDto);

    /**
     * 商品CSVダウンロード
     *
     * @param goodsSeqList 商品SEQリスト
     * @return ダウンロード取得
     */
    @Select
    Stream<GoodsCsvDto> exportCsvByGoodsSeqList(List<Integer> goodsSeqList);

    /**
     * 商品表示順更新
     *
     * @param goodsGroupSeq 商品グループSEQ
     * @param goodsSeq      商品SEQ
     * @return 処理件数
     */
    @Update(sqlFile = true)
    int updateOrderDisplay(Integer goodsGroupSeq, Integer goodsSeq);

    /**
     * 商品テーブルロック
     */
    @Script
    void updateLockTableShareModeNowait();

    /**
     * 公開状態&販売状態をチェック
     *
     * @param shopSeq   ショップSEQ
     * @param goodsCode 商品コード
     * @return 件数
     */
    @Select
    Integer getStatus(Integer shopSeq, String goodsCode);

    /**
     * 規格表示順重複番号リストを取得
     *
     * @param shopSeq       ショップSEQ
     * @param goodsGroupSeq 商品グループコード
     * @return 規格表示順重複番号リスト
     */
    @Select
    List<Integer> getDupulicateOrderDisplayList(Integer shopSeq, Integer goodsGroupSeq);

    /**
     * 規格値2リスト取得処理
     *
     * @param ggcd 商品グループコード
     * @param gcd  商品コード
     * @return 規格値2リスト
     */
    @Select
    List<GoodsUnitDto> getUnit2ListByGoodsCode(String ggcd, String gcd);

    /**
     * 商品情報リスト取得（商品コード）
     *
     * @param goodsCodeList 商品コードリスト
     * @return 商品エンティティリスト
     */
    @Select
    List<String> getEntityListByGoodsCodeList(List<String> goodsCodeList);

    /**
     * ノベルティプレゼント商品の対象件数を取得する
     *
     * @param conditionDto ノベルティプレゼント商品数確認用検索条件
     * @return 商品件数
     */
    @Select
    int getNoveltyGoodsCount(NoveltyGoodsCountConditionDto conditionDto);

    /**
     * 商品コードのリストからエンティティを取得
     *
     * @param shopSeq ショップSEQ
     * @param goodsCodeList 商品コードのリスト
     * @return エンティティのリスト
     */
    @Select
    List<GoodsEntity> getEntityByGoodsCodeList(Integer shopSeq, List<String> goodsCodeList);

    /**
     * 商品名(部分一致)からエンティティを取得
     *
     * @param shopSeq ショップSEQ
     * @param goodsName 商品名
     * @return エンティティのリスト
     */
    @Select
    List<GoodsEntity> getEntityByLikeGoodsName(Integer shopSeq, String goodsName);

    /**
     * 商品コードのリストから商品グループを結合して公開状態、販売状態を取得する
     *
     * @param shopSeq ショップSEQ
     * @param goodsCodeList 商品コードのリスト
     * @return 検索結果
     */
    @Select
    List<GoodsSearchResultDto> getStatusByGoodsCodeList(Integer shopSeq, List<String> goodsCodeList);

    /**
     * 商品グループSEQリストリスト取得
     *
     * @param goodsSeqList 商品SEQリスト
     * @return 商品グループSEQ
     */
    @Select
    List<Integer> getGoodsGroupSeqListByGoodsSeqList(List<Integer> goodsSeqList);
}