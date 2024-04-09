/*
 * Project Name : HIT-MALL4
 *
 * Copyright (C) 2021 i-TEC HANKYU HANSHIN INC. All Rights Reserved.
 *
 */

package jp.co.itechh.quad.core.dao.goods.goodsgroup;

import jp.co.itechh.quad.core.dto.icon.GoodsInformationIconDetailsDto;
import jp.co.itechh.quad.core.entity.goods.goodsgroup.GoodsInformationIconEntity;
import org.seasar.doma.Dao;
import org.seasar.doma.Delete;
import org.seasar.doma.Insert;
import org.seasar.doma.Script;
import org.seasar.doma.Select;
import org.seasar.doma.Update;
import org.seasar.doma.boot.ConfigAutowireable;

import java.sql.Timestamp;
import java.util.List;

/**
 * 商品インフォメーションアイコンDaoクラス
 *
 * @author thang
 * @version $Revision: 1.0 $
 */
@Dao
@ConfigAutowireable
public interface GoodsInformationIconDao {

    /**
     * インサート
     *
     * @param goodsInformationIconEntity 商品インフォメーションアイコンエンティティ
     * @return 処理件数
     */
    @Insert(excludeNull = true)
    int insert(GoodsInformationIconEntity goodsInformationIconEntity);

    /**
     * アップデート
     *
     * @param goodsInformationIconEntity 商品インフォメーションアイコンエンティティ
     * @return 処理件数
     */
    @Update
    int update(GoodsInformationIconEntity goodsInformationIconEntity);

    /**
     * デリート
     *
     * @param goodsInformationIconEntity 商品インフォメーションアイコンエンティティ
     * @return 処理件数
     */
    @Delete
    int delete(GoodsInformationIconEntity goodsInformationIconEntity);

    /**
     * エンティティ取得
     *
     * @param iconSeq アイコンSEQ
     * @return 商品インフォメーションアイコンエンティティ
     */
    @Select
    GoodsInformationIconEntity getEntity(Integer iconSeq);

    // 追加メソッド

    /**
     * アイコンSEQ採番
     *
     * @return アイコンSEQ
     */
    @Select
    int getIconSeqNextVal();

    /**
     * 新規登録(表示順セット)
     *
     * @param goodsInformationIconEntity 商品インフォメーションアイコンエンティティ
     * @return 登録件数
     */
    @Insert(sqlFile = true, excludeNull = true)
    int insertAddOrderDisplay(GoodsInformationIconEntity goodsInformationIconEntity);

    /**
     * 指定ショップに紐付く全アイコンを表示順昇順で取得する
     *
     * @param shopSeq ショップSEQ
     * @return アイコン詳細Dto
     */
    @Select
    List<GoodsInformationIconDetailsDto> getGoodsInformationIconDetailsDtoList(Integer shopSeq);

    /**
     * エンティティ取得(ショップSEQ指定)
     *
     * @param iconSeq アイコンSEQ
     * @param shopSeq ショップSEQ
     * @return 商品インフォメーションアイコンエンティティ
     */
    @Select
    GoodsInformationIconEntity getEntityByShopSeq(Integer iconSeq, Integer shopSeq);

    /**
     * 表示順更新
     *
     * @param iconSeq      アイコンSEQ(更新条件)
     * @param shopSeq      ショップSEQ(更新条件)
     * @param orderDisplay 表示順(更新値)
     * @param updateTime   更新日時(更新値)
     * @return 処理件数
     */
    @Update(sqlFile = true)
    int updateOrderDisplay(Integer iconSeq, Integer shopSeq, Integer orderDisplay, Timestamp updateTime);

    /**
     * 商品インフォメーションアイコンリスト取得
     *
     * @param informationIconSeqList 商品インフォメーションアイコンSEQリスト
     * @return アイコン詳細DTOリスト
     */
    @Select
    List<GoodsInformationIconDetailsDto> getInformationIconList(List<Integer> informationIconSeqList);

    /**
     * 指定アイコンに対し、行ロック
     *
     * @param iconSeq アイコンSEQ
     * @return 商品インフォメーションアイコンエンティティ
     */
    @Select
    GoodsInformationIconEntity getGoodsInformationIconBySeqForUpdate(Integer iconSeq);

    /**
     * テーブルロック
     */
    @Script
    void updateLockTableShareModeNowait();

    /**
     * アイコンSEQのリストからエンティティのリストを取得
     *
     * @param iconSeqList アイコンSEQのリスト
     * @return エンティティのリスト
     */
    @Select
    List<GoodsInformationIconEntity> getEntityListBySeqList(List<Integer> iconSeqList);

    /**
     * アイコン名のリストからエンティティのリストを取得
     *
     * @param iconNameList アイコン名のリスト
     * @return エンティティのリスト
     */
    @Select
    List<GoodsInformationIconEntity> getEntityListByNameList(List<String> iconNameList);
}
