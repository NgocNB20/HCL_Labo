/*
 * Project Name : HIT-MALL3
 *
 * Copyright (C) 2008 i-TEC HANKYU HANSHIN INC. All Rights Reserved.
 *
 */

package jp.co.itechh.quad.front.batch.offline.clear.logic.file;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.File;
import java.util.Calendar;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * テンポラリファイルクリアロジック基底クラス
 * @author MN7017
 * @author Tomo (itec) 2010/06/18 チケット #2532 対応
 */
public abstract class AbstractTemporaryFileClearLogic {

    /** ロガー */
    protected static final Logger LOGGER = LoggerFactory.getLogger(AbstractTemporaryFileClearLogic.class);

    /** 正常文言　*/
    protected static final String FILE_CLEAR_COMPLETE = "正常終了";

    /** 異常文言　*/
    protected static final String FILE_CLEAR_ERROR = "異常終了";

    /**
     * 処理結果マップの作成
     * @param result 処理結果 true:処理成功 false:処理失敗
     * @param totalCount 削除対象ファイル総数
     * @param clearCount 削除成功ファイル数
     * @param failFileList 削除失敗ファイルリスト
     * @return 処理結果マップ
     */
    protected Map<String, String> makeResultMap(boolean result,
                                                int totalCount,
                                                int clearCount,
                                                List<File> failFileList) {

        Map<String, String> resultMap = new HashMap<>();

        resultMap.put("FILE_COUNT", String.valueOf(totalCount));
        resultMap.put("FILE_CLEAR_COUNT", String.valueOf(clearCount));

        if (result) {

            // 処理成功
            resultMap.put("STATUS", "0");
            resultMap.put("STATUS_MESSAGE", FILE_CLEAR_COMPLETE);

        } else {

            // 処理失敗
            resultMap.put("STATUS", "1");
            resultMap.put("STATUS_MESSAGE", FILE_CLEAR_ERROR);
        }

        // 削除失敗ファイルの一覧を作成
        for (int i = 0; i < failFileList.size(); i++) {
            File file = failFileList.get(i);
            resultMap.put("DELETE_FAIL_FILE_" + (i + 1), file.getAbsolutePath());
        }

        return resultMap;
    }

    /**
     * ファイル更新日チェック
     * @param file  チェック対象ファイル
     * @param days  指定日数（○日経過）
     * @return true:クリア対象、false:クリア対象外
     */
    protected boolean isClearFileCheckForUpdateDate(final File file, final Integer days) {

        long updateDate = file.lastModified();
        Calendar todayBeforeDaysCal = Calendar.getInstance();
        todayBeforeDaysCal.add(Calendar.DAY_OF_MONTH, days + 1);

        todayBeforeDaysCal.set(Calendar.HOUR_OF_DAY, 0);
        todayBeforeDaysCal.set(Calendar.MINUTE, 0);
        todayBeforeDaysCal.set(Calendar.SECOND, 0);

        // 現在日と更新日との比較（指定日数経過後かどうか）
        if (updateDate < todayBeforeDaysCal.getTimeInMillis()) {
            return true;
        }

        return false;

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