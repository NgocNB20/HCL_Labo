/*
 * Project Name : HIT-MALL3
 *
 * Copyright (C) 2008 i-TEC HANKYU HANSHIN INC. All Rights Reserved.
 *
 */

package jp.co.itechh.quad.core.service.goods.goods.impl;

import jp.co.itechh.quad.core.service.goods.goods.GoodsCsvCodeCheckService;
import org.apache.commons.lang3.StringUtils;
import org.springframework.stereotype.Service;

import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;
import java.util.Set;

/**
 * 商品CSVアップロードコードチェック実装
 * <pre>
 * </pre>
 *
 * @author tezuka-mk 2012/11/20
 */
@Service
public class GoodsCsvCodeCheckServiceImpl implements GoodsCsvCodeCheckService {

    /**
     * 直前の商品管理番号
     */
    private String lastGoodsGroupCode;

    /**
     * チェック済み商品管理番号群
     */
    private Map<String, Integer> goodsGroupCodeCounts;

    /**
     * チェック済み商品番号群
     */
    private Set<String> goodsCodes;

    /**
     * 現在のKey
     */
    private String currentKey;

    /**
     * 規格値
     */
    private Set<String> unitvalues;

    /**
     * attributeを初期化
     */
    @Override
    public void init() {
        lastGoodsGroupCode = null;
        goodsGroupCodeCounts = new HashMap<>();
        goodsCodes = new HashSet<>();
        currentKey = null;
        unitvalues = new HashSet<>();
    }

    /**
     * @param code 商品管理番号
     * @return 直前のコードと異なる and 2回目以上出現 の場合はfalse
     * @see GoodsCsvCodeCheckService#canSaveGoodsGroupCode(String)
     */
    @Override
    public boolean canSaveGoodsGroupCode(String code) {
        try {
            // 出現回数を取得
            Integer count = goodsGroupCodeCounts.get(code);
            if (count == null) {
                count = 1;
            } else {
                count++;
            }
            goodsGroupCodeCounts.put(code, count);
            // 1件目は重複無しのためtrue
            if (count == 1) {
                return true;
            } else {
                // 直前のコードと異なる and 2回目以上出現 の場合はエラー
                return !((StringUtils.isNotEmpty(lastGoodsGroupCode) && !lastGoodsGroupCode.equals(code)) && count > 1);
            }
        } finally {
            lastGoodsGroupCode = code;
        }
    }

    /**
     * @param code 商品番号
     * @return 重複の場合はfalse
     * @see GoodsCsvCodeCheckService#canSaveGoodsCode(String)
     */
    @Override
    public boolean canSaveGoodsCode(String code) {
        if (goodsCodes.contains(code)) {
            return false;
        } else {
            goodsCodes.add(code);
            return true;
        }
    }

    /**
     * @param unitValue1 規格1
     * @param unitValue2 規格2
     * @param breakKey   ブレイクキー（商品管理番号）
     * @return 規格値が重複している場合はfalse
     * @see GoodsCsvCodeCheckService#canSaveUnitvalue(String, String, String)
     */
    @Override
    public boolean canSaveUnitvalue(String unitValue1, String unitValue2, String breakKey) {
        boolean result = false;
        String unitvalue = unitValue1 + (unitValue2 != null ? " " + unitValue2 : "");

        // 1行目 or キーブレイク
        if (currentKey == null || !currentKey.equals(breakKey)) {
            unitvalues = new HashSet<>();
            result = true;
        } else {
            result = !unitvalues.contains(unitvalue);
        }

        if (result) {
            unitvalues.add(unitvalue);
            currentKey = breakKey;
        }

        return result;
    }
}
