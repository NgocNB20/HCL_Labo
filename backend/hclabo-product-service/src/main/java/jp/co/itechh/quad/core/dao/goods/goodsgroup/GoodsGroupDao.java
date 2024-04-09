/*
 * Project Name : HIT-MALL4
 *
 * Copyright (C) 2021 i-TEC HANKYU HANSHIN INC. All Rights Reserved.
 *
 */

package jp.co.itechh.quad.core.dao.goods.goodsgroup;

import jp.co.itechh.quad.core.constant.type.HTypeOpenDeleteStatus;
import jp.co.itechh.quad.core.constant.type.HTypeSiteType;
import jp.co.itechh.quad.core.dto.goods.goodsgroup.GoodsGroupDetailsDto;
import jp.co.itechh.quad.core.dto.goods.goodsgroup.GoodsGroupSearchForDaoConditionDto;
import jp.co.itechh.quad.core.entity.goods.category.CategoryConditionDetailEntity;
import jp.co.itechh.quad.core.entity.goods.goodsgroup.GoodsGroupEntity;
import org.seasar.doma.Dao;
import org.seasar.doma.Delete;
import org.seasar.doma.Insert;
import org.seasar.doma.Script;
import org.seasar.doma.Select;
import org.seasar.doma.Update;
import org.seasar.doma.boot.ConfigAutowireable;
import org.seasar.doma.jdbc.SelectOptions;

import java.sql.Timestamp;
import java.util.List;

/**
 * 商品グループDaoクラス
 *
 * @author thang
 * @version $Revision: 1.0 $
 */
@Dao
@ConfigAutowireable
public interface GoodsGroupDao {

    /**
     * 固定文字列 "円"
     */
    public static final String YEN = "円";
    /**
     * 固定文字列 "～"
     */
    public static final String MARK = "～";

    /**
     * 商品グループSEQ採番
     *
     * @return 商品グループSEQ
     */
    @Select
    int getGoodsGroupSeqNextVal();

    /**
     * インサート
     *
     * @param goodsGroup 商品グループエンティティ
     * @return 処理件数
     */
    @Insert(excludeNull = true)
    int insert(GoodsGroupEntity goodsGroup);

    /**
     * アップデート
     *
     * @param goodsGroup 商品グループエンティティ
     * @return 処理件数
     */
    @Update
    int update(GoodsGroupEntity goodsGroup);

    /**
     * デリート
     *
     * @param goodsGroup 商品グループエンティティ
     * @return 処理件数
     */
    @Delete
    int delete(GoodsGroupEntity goodsGroup);

    /**
     * エンティティ取得
     *
     * @param goodsGroupSeq 商品グループSEQ
     * @return 商品グループエンティティ
     */
    @Select
    GoodsGroupEntity getEntity(Integer goodsGroupSeq);

    /**
     * 商品グループ詳細リスト取得
     * <pre>
     * 商品を取得するとともに、セール情報を取得する。
     * 取得するセールは一意となる。
     * ●取得セール
     * 通常          ：開催中かつ公開中
     * シークレット ：(開催中かつ公開中)or通常商品公開状態が「非公開」…未認証のセール対象商品を表示しないため
     * 予約          ：公開中かつ（開催前or開催中）
     * </pre>
     *
     * @param conditionDto  商品グループ検索条件DTO
     * @param selectOptions 検索オプション
     * @return 商品グループ詳細DTOリスト
     */
    @Select
    List<GoodsGroupDetailsDto> getSearchGoodsGroupDetailsList(GoodsGroupSearchForDaoConditionDto conditionDto,
                                                              SelectOptions selectOptions);

    /**
     * 商品グループリスト取得
     *
     * @param goodsGroupSeqList 商品グループSEQリスト
     * @return 商品グループエンティティリスト
     */
    @Select
    List<GoodsGroupEntity> getGoodsGroupList(List<Integer> goodsGroupSeqList);

    /**
     * 商品グループレコード排他取得
     *
     * @param goodsGroupSeqList 商品グループSEQリスト
     * @param versionNo         更新カウンタ
     * @return 商品グループエンティティリスト
     */
    @Select
    List<GoodsGroupEntity> getGoodsGroupBySeqForUpdate(List<Integer> goodsGroupSeqList, Integer versionNo);

    /**
     * 商品グループ情報取得(商品グループコード）
     *
     * @param shopSeq        ショップSEQ
     * @param goodsGroupCode 商品グループコード
     * @param goodsCode      商品コード
     * @param siteType       サイト区分
     * @param openStatus     公開状態
     * @param frontDisplayReferenceDate フロント表示基準日時（管理サイトからの場合未設定。プレビュー画面の制御用日時）
     * @return 商品グループエンティティ
     */
    @Select
    GoodsGroupEntity getGoodsGroupByCode(Integer shopSeq,
                                         String goodsGroupCode,
                                         String goodsCode,
                                         HTypeSiteType siteType,
                                         HTypeOpenDeleteStatus openStatus,
                                         Timestamp frontDisplayReferenceDate);

    /**
     * 商品グループ情報取得(ショップSEQ、商品グループコードリスト）
     *
     * @param shopSeq            ショップSEQ
     * @param goodsGroupCodeList 商品グループコードリスト
     * @return 商品グループエンティティリスト
     */
    @Select
    List<GoodsGroupEntity> getGoodsGroupByCodeList(Integer shopSeq, List<String> goodsGroupCodeList);

    /**
     * 商品グループ画像テーブルロック
     */
    @Script
    void updateLockTableShareModeNowait();

    /**
     * 商品グループ情報取得(商品グループコード）
     * ※商品グループコードの重複チェック用
     *
     * @param shopSeq        ショップSEQ
     * @param goodsGroupCode 商品グループコード
     * @return 商品グループエンティティ
     */
    @Select
    GoodsGroupEntity checkDuplicateByCode(Integer shopSeq, String goodsGroupCode);

    /**
     * 商品管理番号のリストからエンティティを取得
     *
     * @param shopSeq ショップSEQ
     * @param goodsGroupCodeList 商品管理番号のリスト
     * @return エンティティのリスト
     */
    @Select
    List<GoodsGroupEntity> getEntityByGoodsGroupCodeList(Integer shopSeq, List<String> goodsGroupCodeList);

    /**
     * 商品グループSEQのリストを取得する
     *
     * @param goodsGroupSeq                     商品グループSEQ
     * @param conditionType                     条件種別
     * @param categoryConditionDetailEntityList カテゴリ条件詳細エンティティクラスのリスト
     * @return 商品グループSEQのリスト
     */
    @Select
    List<Integer> getGoodsGroupSeqListByCondition(Integer goodsGroupSeq,
                                                  String conditionType,
                                                  List<CategoryConditionDetailEntity> categoryConditionDetailEntityList);
}