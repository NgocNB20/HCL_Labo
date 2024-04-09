/*
 * Project Name : HIT-MALL3
 *
 * Copyright (C) 2008 i-TEC HANKYU HANSHIN INC. All Rights Reserved.
 *
 */

package jp.co.itechh.quad.front.batch.offline.clear.logic.file.impl;

import jp.co.itechh.quad.front.base.utility.ApplicationContextUtility;
import jp.co.itechh.quad.front.base.utility.FileOperationUtility;
import jp.co.itechh.quad.front.batch.offline.clear.logic.file.AbstractTemporaryFileClearLogic;
import jp.co.itechh.quad.front.batch.offline.clear.logic.file.TemporaryFileClearLogic;
import org.springframework.stereotype.Component;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

/**
 * テンポラリファイルクリアロジック実装クラス
 * @author MN7017
 * @author Tomo (itec) 2010/06/18 チケット #2532 対応
 */
@Component
public class TemporaryFileClearLogicImpl extends AbstractTemporaryFileClearLogic implements TemporaryFileClearLogic {

    /**
     * 一時ファイル削除処理
     * @param folderPath 削除対象ファイルが含まれるディレクトリ配列
     * @param specifiedDays 削除対象となる日付
     * @return 処理結果マップ
     */
    @Override
    public Map<String, String> execute(final String[] folderPath, final Integer specifiedDays) {

        /* ファイル総数 */
        int allFileCount = 0;
        /* 削除ファイル数 */
        int clearFileCount = 0;

        // 削除失敗ファイル一覧
        List<File> deleteFailFileList = new ArrayList<>();

        // ファイル操作Utilityクラス
        FileOperationUtility fileOperationUtility = ApplicationContextUtility.getBean(FileOperationUtility.class);

        // 指定日数の有効チェック
        if (!this.isEffectiveTime(specifiedDays)) {
            // 処理終了
            LOGGER.warn("指定された日数（" + specifiedDays + "日）は、有効な日数ではありません。");
            return this.makeResultMap(true, allFileCount, clearFileCount, deleteFailFileList);
        }

        // 削除対象フォルダの数分、以下の処理を繰り返し実行
        for (String tempDirPath : folderPath) {

            LOGGER.info("[" + tempDirPath + "]内のファイルをチェックを開始します。");

            // ""を処理対象外とする
            // プロパティファイルで指定しているディレクトが未指定の場合は""を設定している
            // ""の設定が無い場合、ClearBatch.diconにて形式エラーとなる
            if ("".equals(tempDirPath)) {
                continue;
            }

            final File tempDirectory = new File(tempDirPath);
            // ①ディレクトリチェック
            if (!tempDirectory.exists() || !tempDirectory.isDirectory()) {
                // ディレクトリではない。 －ここで処理終了
                LOGGER.warn("[" + tempDirPath + "]は、有効なディレクトリではありません。");
                continue;
            }
            // ②ファイル存在チェック
            final File[] files = tempDirectory.listFiles();
            if (files != null) {
                LOGGER.debug("[" + tempDirPath + "]内のファイル数＝" + files.length + "ファイル");
            }
            if (files == null || files.length == 0) {
                // ファイル存在無し －ここで処理終了
                continue;
            } else {
                // ファイル総数に、ファイル数を加算。
                allFileCount = allFileCount + files.length;
            }

            // １ファイルずつ処理対象チェックとクリア実行
            for (File tempFile : files) {

                // ④ファイルの更新日付チェック
                if (!isClearFileCheckForUpdateDate(tempFile, specifiedDays)) {
                    // クリア対象外。 －次ファイルへ
                    LOGGER.debug(tempFile.getName() + "は、ファイル削除対象外です。");
                    continue;
                }

                // ⑤ファイル削除実行
                try {
                    fileOperationUtility.removeDir(tempFile);
                } catch (IOException e) {
                    // ファイル削除失敗。
                    LOGGER.info("ファイル削除に失敗しました。（" + tempFile.getName() + "）");
                    deleteFailFileList.add(tempFile);
                    continue;
                }
                LOGGER.info("[" + tempFile.getName() + "]を削除しました。");

                // クリア処理済みファイルカウントアップ(サブディレクトリとサブファイルはカウントしない)
                clearFileCount++;

            }

        }

        return makeResultMap(true, allFileCount, clearFileCount, deleteFailFileList);

    }

}