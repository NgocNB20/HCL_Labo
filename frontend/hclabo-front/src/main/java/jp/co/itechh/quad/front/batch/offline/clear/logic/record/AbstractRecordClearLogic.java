/*
 * Project Name : HIT-MALL3
 *
 * Copyright (C) 2008 i-TEC HANKYU HANSHIN INC. All Rights Reserved.
 *
 */

package jp.co.itechh.quad.front.batch.offline.clear.logic.record;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.HashMap;
import java.util.Map;

/**
 * レコードクリアロジック基底クラス
 * @author MN7017
 * @version $Revision:$
 *
 */
public abstract class AbstractRecordClearLogic {

    /** ロガー */
    protected static final Logger LOGGER = LoggerFactory.getLogger(AbstractRecordClearLogic.class);

    /** 正常文言　*/
    protected static final String RECORDCLEAR_COMPLETE = "正常終了";

    /** 異常文言　*/
    protected static final String RECORDCLEAR_ERROR = "異常終了";

    /**
     * 結果MAP作成
     * @param result            成功:0, 失敗：1
     * @param allFileCount      ファイル総数
     * @param clearFileCount    削除ファイル数
     * @return 結果MAP
     */
    protected Map<String, String> makeResultMap(final boolean result,
                                                final int allFileCount,
                                                final int clearFileCount) {

        /** 結果格納Map */
        final Map<String, String> valueMap = new HashMap<>();

        if (result) {
            valueMap.put("STATUS", "0");
            valueMap.put("STATUS_MESSAGE", RECORDCLEAR_COMPLETE);
        } else {
            valueMap.put("STATUS", "1");
            valueMap.put("STATUS_MESSAGE", RECORDCLEAR_ERROR);
        }

        valueMap.put("RECORD_COUNT", String.valueOf(allFileCount));
        valueMap.put("RECORD_CLEAR_COUNT", String.valueOf(clearFileCount));

        return valueMap;

    }

    /**
     * 指定日数値の有効チェック
     * @param days  指定日数（dicon設定）
     * @return true:有効 false：無効
     */
    protected boolean isEffectiveTime(Integer days) {

        // 指定日数の有効チェック
        if (days == null || days >= 0) {
            return false;
        }

        return true;
    }

}