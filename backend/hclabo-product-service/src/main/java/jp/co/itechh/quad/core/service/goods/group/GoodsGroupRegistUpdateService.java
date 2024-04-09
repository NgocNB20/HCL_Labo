/*
 * Project Name : HIT-MALL3
 *
 * Copyright (C) 2008 i-TEC HANKYU HANSHIN INC. All Rights Reserved.
 *
 */

package jp.co.itechh.quad.core.service.goods.group;

import jp.co.itechh.quad.core.dto.goods.goodsgroup.GoodsGroupDto;
import jp.co.itechh.quad.core.dto.goods.goodsgroup.GoodsGroupImageRegistUpdateDto;
import jp.co.itechh.quad.core.entity.goods.goodsgroup.GoodsRelationEntity;

import java.util.List;
import java.util.Map;

/**
 * 商品グループ登録更新
 *
 * @author kimura
 */
public interface GoodsGroupRegistUpdateService {

    /**
     * 処理種別（画面からの登録更新）
     * <code>PROCESS_TYPE_FROM_SCREEN</code>
     */
    public static final int PROCESS_TYPE_FROM_SCREEN = 0;

    /**
     * 処理種別（CSVからの登録更新）
     * <code>PROCESS_TYPE_FROM_CSV</code>
     */
    public static final int PROCESS_TYPE_FROM_CSV = 1;

    /**
     * 処理件数マップ　商品グループSEQ
     * <code>GOODS_GROUP_SEQ</code>
     */
    public static final String GOODS_GROUP_SEQ = "GoodsGroupSeq";

    /**
     * 処理件数マップ　商品グループ登録件数
     * <code>GOODS_GROUP_REGIST</code>
     */
    public static final String GOODS_GROUP_REGIST = "GoodsGroupRegist";

    /**
     * 処理件数マップ　商品グループ表示登録件数
     * <code>GOODS_GROUP_DISPLAY_REGIST</code>
     */
    public static final String GOODS_GROUP_DISPLAY_REGIST = "GoodsGroupDisplayRegist";

    /**
     * 処理件数マップ　商品グループ人気登録件数
     * <code>GOODS_GROUP_POPULARITY_REGIST</code>
     */
    public static final String GOODS_GROUP_POPULARITY_REGIST = "GoodsGroupPopularityRegist";

    /**
     * 処理件数マップ　商品グループ更新件数
     * <code>GOODS_GROUP_UPDATE</code>
     */
    public static final String GOODS_GROUP_UPDATE = "GoodsGroupUpdate";

    /**
     * 処理件数マップ　商品グループ表示更新件数
     * <code>GOODS_GROUP_DISPLAY_UPDATE</code>
     */
    public static final String GOODS_GROUP_DISPLAY_UPDATE = "GoodsGroupDisplayUpdate";

    /**
     * 処理件数マップ　商品グループ画像ファイル登録件数
     * <code>GOODS_GROUP_IMAGE_FILE_DELETE</code>
     */
    public static final String GOODS_GROUP_IMAGE_FILE_DELETE = "GoodsGroupImageFileDelete";

    /**
     * 処理件数マップ　商品グループ画像ファイル削除件数
     * <code>GOODS_GROUP_IMAGE_FILE_REGIST</code>
     */
    public static final String GOODS_GROUP_IMAGE_FILE_REGIST = "GoodsGroupImageFileRegist";

    /**
     * 処理件数マップ　在庫登録件数
     * <code>STOCK_REGIST</code>
     */
    public static final String STOCK_REGIST = "StockRegist";

    /**
     * 処理件数マップ　ワーニングメッセージ
     * <code>WARNING_MESSAGE</code>
     */
    public static final String WARNING_MESSAGE = "WarningMessage";

    /**
     * 処理件数マップ　在庫設定登録件数
     * <code>STOCK_SETTING_REGIST</code>
     */
    public static final String STOCK_SETTING_REGIST = "StockSettingRegist";

    /**
     * 処理件数マップ　在庫設定更新件数
     * <code>STOCK_SETTING_UPDATE</code>
     */
    public static final String STOCK_SETTING_UPDATE = "StockSettingUpdate";

    /**
     * CSVアップロード用の実行メソッド（トランザクション指定）
     *
     * @param administratorSeq               管理者SEQ
     * @param goodsGroupDto                  商品グループDto
     * @param goodsRelationEntityList        関連商品エンティティリスト
     * @param processType                    処理種別（画面/CSV）
     * @param goodsGroupSeqSuccessList 商品登録更新成功SEQリスト
     */
    void executeForCsv(Integer administratorSeq,
                       GoodsGroupDto goodsGroupDto,
                       List<? extends GoodsRelationEntity> goodsRelationEntityList,
                       int processType,
                       List<Integer> goodsGroupSeqSuccessList) throws Exception;

    /**
     * 実行メソッド
     *
     * @param administratorSeq               管理者SEQ
     * @param goodsGroupDto                  商品グループDto
     * @param goodsRelationEntityList        関連商品エンティティリスト
     * @param goodsGroupImageRegistUpdateDto 商品グループ画像登録更新用DTOリスト（処理種別=CSV時はnull）
     * @param processType                    処理種別（画面/CSV）
     * @return 処理件数マップ
     */
    Map<String, Object> execute(Integer administratorSeq,
                                GoodsGroupDto goodsGroupDto,
                                List<? extends GoodsRelationEntity> goodsRelationEntityList,
                                List<GoodsGroupImageRegistUpdateDto> goodsGroupImageRegistUpdateDto,
                                int processType) throws Exception;
}