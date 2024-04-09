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
import jp.co.itechh.quad.front.batch.offline.clear.logic.file.TemporaryFileZipLogic;
import org.springframework.stereotype.Component;

import java.io.BufferedInputStream;
import java.io.BufferedOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;
import java.util.Map;
import java.util.zip.ZipEntry;
import java.util.zip.ZipOutputStream;

/**
 *
 * テンポラリファイルZip圧縮ロジック実装クラス
 *
 * @author am37200
 *
 */
@Component
public class TemporaryFileZipLogicImpl extends AbstractTemporaryFileClearLogic implements TemporaryFileZipLogic {

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
    @Override
    public Map<String, String> execute(final String mode,
                                       final String[] folderPath,
                                       final Integer specifiedDays,
                                       final String batchLogOutputPath,
                                       final Integer targetMonthlyTerm) throws IOException {

        Calendar calc = Calendar.getInstance();
        calc.add(Calendar.DATE, specifiedDays);
        SimpleDateFormat sdf = new SimpleDateFormat("yyyyMMdd");
        String tmpZipFileName = sdf.format(calc.getTime());
        String fileStart = "";
        if (mode.equals(ZIP_MODE_YM)) {
            calc = Calendar.getInstance();
            calc.add(Calendar.MONTH, targetMonthlyTerm);
            sdf = new SimpleDateFormat("yyyyMM");
            fileStart = sdf.format(calc.getTime());
            tmpZipFileName = fileStart + ".zip";
        }

        /* ファイル総数 */
        int allFileCount = 0;
        /* 削除ファイル数 */
        int clearFileCount = 0;

        // 圧縮失敗ファイル一覧
        List<File> zipFailFileList = new ArrayList<>();

        // 指定日数の有効チェック
        if (!this.isEffectiveTime(specifiedDays)) {
            // 処理終了
            LOGGER.warn("指定された日数（" + specifiedDays + "日）は、有効な日数ではありません。");
            return this.makeResultMap(true, allFileCount, clearFileCount, zipFailFileList);
        }

        // ファイル操作Utilityクラス
        FileOperationUtility fileOperationUtility = ApplicationContextUtility.getBean(FileOperationUtility.class);

        // 削除対象フォルダの数分、以下の処理を繰り返し実行
        int dirCount = 0;
        String zipFileName = null;
        for (String tempDirPath : folderPath) {
            LOGGER.info("[" + tempDirPath + "]内のファイルをチェックを開始します。");

            // ""を処理対象外とする
            if ("".equals(tempDirPath)) {
                continue;
            }
            File tempDirectory = new File(tempDirPath);
            // ディレクトリチェック
            if (!tempDirectory.exists() || !tempDirectory.isDirectory()) {
                // ディレクトリではない。 －ここで処理終了
                LOGGER.warn("[" + tempDirPath + "]は、有効なディレクトリではありません。");
                continue;
            }

            dirCount++;
            // ファイル存在チェック
            File[] files = tempDirectory.listFiles();
            if (null != files) {
                LOGGER.debug("[" + tempDirPath + "]内のファイル数＝" + files.length + "ファイル");
            }
            if (files == null || files.length == 0) {
                // ファイル存在無し －ここで処理終了
                continue;
            } else {
                // ファイル総数に、ファイル数を加算。
                allFileCount = allFileCount + files.length;
            }

            List<File> zipFileList = new ArrayList<>();
            // １ファイルずつ処理対象チェックとクリア実行
            for (File tempFile : files) {
                // フォルダの場合エラーとなるためSKIP
                if (!tempFile.isFile()) {
                    LOGGER.warn("[" + tempDirPath + "]内のファイル＝[" + tempFile.getName() + "]はファイルではありません。");
                    continue;
                }
                if (mode.equals(ZIP_MODE_YMD)) {
                    if (isClearFileCheckForUpdateDate(tempFile, specifiedDays) && !tempFile.getName()
                                                                                           .endsWith(".zip")) {
                        zipFileList.add(tempFile);
                    } else {
                        LOGGER.debug(tempFile.getName() + "は、ファイル圧縮対象外です。");
                    }
                } else {
                    if (tempFile.getName().startsWith(fileStart) && tempFile.getName().endsWith(".zip")) {
                        zipFileList.add(tempFile);
                    } else {
                        LOGGER.debug(tempFile.getName() + "は、ファイル圧縮対象外です。");
                    }
                }
            }

            // 圧縮ファイル名の作成
            if (mode.equals(ZIP_MODE_YM)) {
                zipFileName = tmpZipFileName;
            } else {
                if (batchLogOutputPath.isEmpty()) {
                    zipFileName = tmpZipFileName + ".zip";
                } else {
                    // 圧縮後のログ出力先ディレクトリが指定されている場合
                    // ・圧縮ファイル出力ディレクトリに指定のディレクトリを指定する
                    // ・圧縮後のファイル名に連番を付与する（日次の場合のみ）
                    zipFileName = tmpZipFileName + "_" + dirCount + ".zip";
                    tempDirPath = batchLogOutputPath;
                }
            }

            createZip(tempDirPath, zipFileName, zipFileList);
            for (File deleteFile : zipFileList) {
                // ファイル削除実行
                try {
                    fileOperationUtility.removeDir(deleteFile);
                } catch (IOException e) {
                    // ファイル削除失敗。
                    LOGGER.info("ファイル削除に失敗しました。（" + deleteFile.getName() + "）");
                    zipFailFileList.add(deleteFile);
                    continue;
                }
                LOGGER.info("[" + deleteFile.getName() + "]を削除しました。");

                // 削除処理済みファイルカウントアップ(サブディレクトリとサブファイルはカウントしない)
                clearFileCount++;
            }
        }

        return makeResultMap(true, allFileCount, clearFileCount, zipFailFileList);
    }

    /**
     * 指定されたファイルをZip形式で圧縮
     *
     * @param tempDirPath 圧縮ファイル出力ディレクトリ
     * @param zipFileName 圧縮ファイル名
     * @param zipFailFileList 圧縮対象ファイル一覧
     * @throws IOException 例外
     */
    protected void createZip(String tempDirPath, String zipFileName, List<File> zipFailFileList) throws IOException {
        if (zipFailFileList.size() == 0) {
            return;
        }

        ZipOutputStream zos = null;
        try {
            byte[] buf = new byte[1024];
            zos = new ZipOutputStream(new BufferedOutputStream(
                            new FileOutputStream(new File(tempDirPath + File.separator + zipFileName))));

            for (File file : zipFailFileList) {
                zos.putNextEntry(new ZipEntry(file.getName()));
                try (InputStream is = new BufferedInputStream(new FileInputStream(file))) {
                    int len = 0;
                    while ((len = is.read(buf)) != -1) {
                        zos.write(buf, 0, len);
                    }
                }
            }

        } finally {
            if (zos != null) {
                zos.close();
            }
        }

    }
}