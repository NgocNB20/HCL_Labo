/*
 * Project Name : HIT-MALL3
 *
 * Copyright (C) 2008 i-TEC HANKYU HANSHIN INC. All Rights Reserved.
 *
 */

package jp.co.itechh.quad.core.service.goods.group;

import jp.co.itechh.quad.core.dto.goods.goodsgroup.GoodsGroupDto;
import jp.co.itechh.quad.core.entity.goods.goodsgroup.GoodsRelationEntity;

import java.util.List;
import java.util.Map;

/**
 * 商品グループDto相関チェックサービス
 */
public interface GoodsGroupCorrelationDataCheckService {

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
     * 正規表現チェック(商品グループコード・商品コード用)
     */
    public static final String regPatternForCode = "[a-zA-Z0-9_-]+";
    /**
     * 正規表現チェック(JANコード用)
     */
    public static final String regPatternForJanCode = "[0-9]+";

    /**
     * 0～99999999円の範囲外の場合エラー：SGP001053E
     */
    public static final String MSGCD_PRICE_LIMIT_OUT = "SGP001053E";

    /**
     * 税率未入力エラー
     */
    public static final String MSGCD_NOSET_TAX_RATE = "PKG-3680-019-S-";
    /**
     * 税率設定値エラー
     */
    public static final String MSGCD_TAX_RATE_OUT = "PKG-3680-021-S-";
    /**
     * 税率
     */
    public static final String MSG_ARGS_TAX_RATE = "税率";

    /**
     * 実行メソッド
     *
     * @param goodsGroupDto           商品グループDto
     * @param goodsRelationEntityList 関連商品エンティティリスト
     * @param processType             処理種別（画面/CSV）
     * @param commonCsvInfoMap        CSVアップロード共通情報
     */
    void execute(GoodsGroupDto goodsGroupDto,
                 List<GoodsRelationEntity> goodsRelationEntityList,
                 int processType,
                 Map<String, Object> commonCsvInfoMap) throws Exception;
}