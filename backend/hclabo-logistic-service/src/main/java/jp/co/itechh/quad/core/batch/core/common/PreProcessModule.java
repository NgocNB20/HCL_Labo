package jp.co.itechh.quad.core.batch.core.common;

import jp.co.itechh.quad.core.base.utility.ApplicationContextUtility;
import jp.co.itechh.quad.core.base.utility.UnzipUtility;
import org.apache.commons.lang3.StringUtils;
import org.apache.tomcat.util.http.fileupload.FileUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Component;

import java.io.BufferedInputStream;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.net.URL;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.List;

/**
 * 機能概要：前処理共通機能
 * 作成日：2021/10/20
 *
 * @author Doan Thang (VTI Japan Co., Ltd.)
 */
@Component
public class PreProcessModule {

    /** ロガー */
    private static final Logger LOGGER = LoggerFactory.getLogger(PreProcessModule.class);

    /** ダウンロード先 */
    private String downloadDirectory;

    /** データフルパス */
    private String downloadUrl;

    public PreProcessModule() {
    }

    /**
     * 初回の郵便番号前処理 （ダウンロード&解凍の処理）
     *
     * @param registData 登録データ
     * @param zipFile ファイル
     * @param dlDirectory ダウンロードディレクトリ
     * @param dlUrl ダウンロードURL
     * @return ターゲットファイルパス
     * @throws IOException
     */
    public String zipCodeRegistPreProcess(String registData, String zipFile, String dlDirectory, String dlUrl)
                    throws IOException {

        // ダウンロード先
        this.downloadDirectory = dlDirectory;

        // データフルパス
        this.downloadUrl = dlUrl;

        // 文字コード変換後データフルパス
        String registPath = this.downloadDirectory + "/";

        // Zipファイル解凍ユーティリティ
        UnzipUtility unzipUtility = ApplicationContextUtility.getBean(UnzipUtility.class);

        // ダウンロードファイル(廃止)名
        String regCsvFile = null;

        if (downloadZipFile(zipFile)) {
            List<File> addFileList = unzipUtility.unzip(new File(registPath + zipFile), registPath, true);
            if (addFileList.size() > 0) {
                regCsvFile = addFileList.get(0).getPath();
            }
        }

        // Zip解凍失敗の場合
        List<Path> paths = new ArrayList<>();
        if (StringUtils.isEmpty(regCsvFile)) {
            return null;
        } else {
            paths.add(Paths.get(regCsvFile));
        }

        // 文字コード変換後データフルパス
        String registCsvFile = this.downloadDirectory + "/" + registData;

        return mergeCsv(null, regCsvFile, paths, registCsvFile, false);
    }

    /**
     * 郵便番号前処理 （ダウンロード&解凍の処理）
     *
     * @param updateData 更新データ
     * @param addZipFile 追加ファイル
     * @param delZipFile 削除ファイル
     * @param dlDirectory ダウンロードディレクトリ
     * @param dlUrl ダウンロードURL
     * @return ターゲットファイルパス
     * @throws IOException
     */
    public String zipCodeUpdatePreProcess(String updateData,
                                          String addZipFile,
                                          String delZipFile,
                                          String dlDirectory,
                                          String dlUrl) throws IOException {

        // ダウンロード先
        this.downloadDirectory = dlDirectory;

        // データフルパス
        this.downloadUrl = dlUrl;

        // 文字コード変換後データフルパス
        String updatePath = this.downloadDirectory + "/" + updateData + "/";

        // Zipファイル解凍ユーティリティ
        UnzipUtility unzipUtility = ApplicationContextUtility.getBean(UnzipUtility.class);

        // ダウンロードファイル(新規追加)名
        String addCsvFile = null;

        // ダウンロードファイル(廃止)名
        String delCsvFile = null;

        if (downloadZipFile(addZipFile)) {
            List<File> addFileList =
                            unzipUtility.unzip(new File(this.downloadDirectory + "/" + addZipFile), updatePath, true);
            if (addFileList.size() > 0) {
                addCsvFile = addFileList.get(0).getPath();
            }
        }

        if (downloadZipFile(delZipFile)) {
            List<File> delFileList =
                            unzipUtility.unzip(new File(this.downloadDirectory + "/" + delZipFile), updatePath, true);
            if (delFileList.size() > 0) {
                delCsvFile = delFileList.get(0).getPath();
            }
        }

        // Zip解凍失敗の場合
        if (StringUtils.isEmpty(addCsvFile) && StringUtils.isEmpty(delCsvFile)) {
            return null;
        }

        // 文字コード変換後データフルパス
        String updateCsvFile = this.downloadDirectory + "/" + updateData + ".csv";

        List<Path> paths = new ArrayList<>();
        if (!StringUtils.isEmpty(delCsvFile)) {
            paths.add(Paths.get(delCsvFile));
        }
        if (!StringUtils.isEmpty(addCsvFile)) {
            paths.add(Paths.get(addCsvFile));
        }

        return mergeCsv(updatePath, null, paths, updateCsvFile, false);

    }

    /**
     *
     * @param localDate LocalDate
     * @return 対象日
     */
    public String getDayString(LocalDate localDate) {
        return DateTimeFormatter.ofPattern("dd").format(localDate);
    }

    /**
     *
     * @param localDate LocalDate
     * @return 対象月
     */
    public String getMonthString(LocalDate localDate) {
        return DateTimeFormatter.ofPattern("MM").format(localDate);
    }

    /**
     *
     * @param localDate LocalDate
     * @return 対象年
     */
    public String getYearString(LocalDate localDate) {
        return DateTimeFormatter.ofPattern("yy").format(localDate);
    }

    /**
     *
     * @param fileName ファイル名
     * @return true: 成功 / false: 失敗
     * @throws IOException
     */
    private boolean downloadZipFile(String fileName) {
        try (BufferedInputStream in = new BufferedInputStream(new URL(this.downloadUrl + fileName).openStream());
                        FileOutputStream fileOutputStream = new FileOutputStream(
                                        this.downloadDirectory + "/" + fileName)) {
            byte[] dataBuffer = new byte[1024];
            int bytesRead;
            while ((bytesRead = in.read(dataBuffer, 0, 1024)) != -1) {
                fileOutputStream.write(dataBuffer, 0, bytesRead);
            }
            return true;
        } catch (IOException e) {
            LOGGER.error(fileName + "ダウンロード失敗: " + e.getCause());
            return false;
        }
    }

    /**
     *
     * @param workPath 作業パス
     * @param workFile 作業ファイル
     * @param paths ファイルパス
     * @param dstFile ターゲットファイル
     * @param header ヘッダー
     * @return ターゲットファイルパス
     * @throws IOException
     */
    private static String mergeCsv(String workPath, String workFile, List<Path> paths, String dstFile, boolean header)
                    throws IOException {
        if (paths.size() > 0) {
            try {
                List<String> mergedLines = getMergedLines(paths, header);
                Path target = Paths.get(dstFile);
                Files.write(target, mergedLines, StandardCharsets.ISO_8859_1);
                if (!StringUtils.isEmpty(workPath)) {
                    FileUtils.deleteDirectory(new File(workPath));
                }
                if (!StringUtils.isEmpty(workFile)) {
                    FileUtils.forceDelete(new File(workFile));
                }
                return dstFile;
            } catch (IOException e) {
                if (!StringUtils.isEmpty(workPath)) {
                    LOGGER.error(workPath + "CSVマージ失敗: " + e.getCause());
                }
                if (!StringUtils.isEmpty(workFile)) {
                    LOGGER.error(workFile + "マージ失敗: " + e.getCause());
                }
                throw e;
            }
        }
        return null;
    }

    /**
     *
     * @param paths List<Path>
     * @param header boolean
     * @return CSV内容
     * @throws IOException
     */
    private static List<String> getMergedLines(List<Path> paths, boolean header) throws IOException {
        List<String> mergedLines = new ArrayList<>();
        for (Path p : paths) {
            List<String> lines = Files.readAllLines(p, StandardCharsets.ISO_8859_1);
            if (header) {
                if (!lines.isEmpty()) {
                    if (mergedLines.isEmpty()) {
                        mergedLines.add(lines.get(0));
                    }
                    mergedLines.addAll(lines.subList(1, lines.size()));
                }
            } else {
                mergedLines.addAll(lines.subList(0, lines.size()));
            }
        }
        return mergedLines;
    }
}