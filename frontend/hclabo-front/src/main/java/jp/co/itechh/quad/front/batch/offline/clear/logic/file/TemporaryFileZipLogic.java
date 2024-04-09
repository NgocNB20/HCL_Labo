/*
 * Project Name : HIT-MALL3
 *
 * Copyright (C) 2008 i-TEC HANKYU HANSHIN INC. All Rights Reserved.
 *
 */

package jp.co.itechh.quad.front.batch.offline.clear.logic.file;

import java.io.IOException;
import java.util.Map;

/**
 *
 * テンポラリファイルZip圧縮ロジックI/F
 *
 * @author am37200
 *
 */
public interface TemporaryFileZipLogic {

    /** 実行モード（日単位） */
    String ZIP_MODE_YMD = "YMD";
    /** 実行モード（月単位） */
    String ZIP_MODE_YM = "YM";

    /**
     * ファイルZip圧縮処理
     * @param mode 実行モード
     * @param folderPath Zip圧縮対象ファイルが含まれるディレクトリ配列
     * @param specifiedDays Zip圧縮対象となる日付
     * @param batchLogOutputPath 圧縮後のファイルを出力するディレクトリ
     * @param targetMonthlyTerm Zip圧縮対象となる月
     *
     * @return 処理結果マップ
     * @throws IOException 例外
     */
    Map<String, String> execute(final String mode,
                                final String[] folderPath,
                                final Integer specifiedDays,
                                final String batchLogOutputPath,
                                final Integer targetMonthlyTerm) throws IOException;

}