/*
 * Project Name : HIT-MALL3
 *
 * Copyright (C) 2008 i-TEC HANKYU HANSHIN INC. All Rights Reserved.
 *
 */

package jp.co.itechh.quad.hclabo.core.base.utility;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.mock.web.MockMultipartFile;
import org.springframework.stereotype.Component;
import org.springframework.util.FileCopyUtils;
import org.springframework.util.ObjectUtils;
import org.springframework.web.multipart.MultipartFile;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;

/**
 * ファイルの コピー / 移動 / 削除 用ヘルパークラス<br />
 *
 * <code>put</code> … ファイル/ディレクトリをコピー/移動させる
 * <code>get</code> … ファイル内容を取得する
 * <code>remove</code> … ファイルもしくは内容が空のディレクトリを削除する
 * <code>removeDir</code> … 指定ディレクトリを再帰的に削除する
 *
 * @author tm27400
 * @author Kaneko (itec) 2012/08/16 UtilからHelperへ変更
 *
 */
@Component
public class FileOperationUtility {

    /** コンストラクタ */
    public FileOperationUtility() {
    }

    /** ロガー */
    private static final Logger LOGGER = LoggerFactory.getLogger(FileOperationUtility.class);

    /**
     * アップロードされたファイルを指定されたディレクトリ（またはそのファイル名として）移動させる。<br />
     * 元のアップロードされたファイルは処分される。
     *
     * @param src   アップロードされたファイル
     * @param dst   移動先。<br />
     *              移動先がディレクトリの場合はオリジナルファイル名のまま移動<br />
     *              移動先がファイルもしくは存在しないファイルの場合はその名前でファイルを移動。
     * @return false 移動に失敗
     * @throws IOException
     */
    public boolean put(final MultipartFile src, final String dst) throws IOException {

        // 引数が無効な場合は処理を行わない
        if (src == null || dst == null) {
            return false;
        }

        // SpringのMultipartFileに置き換えるとともに、以下の処理を修正する
        //        return put(src, new File(dst));
        File dstFile = new File(dst);
        src.transferTo(dstFile);
        return true;
    }

    /**
     * ファイルまたはディレクトリをコピー/移動させる
     *
     * @param src   元対象のファイル/ディレクトリ
     * @param dest  先対象のファイル/ディレクトリ
     * @param removeSrc    処理後に元ファイル/ディレクトリを消すかどうか
     * @throws IOException
     */
    public void put(final String src, final String dest, final boolean removeSrc) throws IOException {
        put(new File(src), new File(dest), removeSrc);
    }

    /**
     * ファイルまたはディレクトリをコピー/移動させる。<br />
     * 特定ディレクトリの中身をそのままコピーした別ディレクトリを作成する場合は<br />
     * <code>put("c:/target/<b>*</b>", "c:/target_copy/", booleanValue);</code> とする。<br />
     * <code>put("c:/target/",  "c:/target_copy/", booleanValue);</code> とした場合、<br />
     * ファイル構造が <code>c:/target_copy/target/</code> となるので注意。
     *
     * @param src   元対象のファイル/ディレクトリ
     * @param dest  先対象のファイル/ディレクトリ
     * @param removeSrc    処理後に元ファイル/ディレクトリを消すかどうか
     * @throws IOException
     */
    public void put(File src, File dest, boolean removeSrc) throws IOException {

        try {

            // 同一ファイルを操作しようとした場合
            if (src.compareTo(dest) == 0) {
                LOGGER.warn("[ERROR] プット元とプット先が同じです。" + src.getAbsolutePath() + " => " + dest.getAbsolutePath());
                return;
            }

            // ディレクトリ内全ファイルをコピーする場合
            if ("*".equals(src.getName())) {

                if (!dest.exists()) {
                    LOGGER.debug("[MKDIR] " + dest.getAbsolutePath());
                    dest.mkdirs();
                }

                if (null != src.getParentFile()) {
                    File[] fileList = src.getParentFile().listFiles();
                    if (null != fileList) {
                        for (final File child : fileList) {
                            put(child, dest, false);
                        }
                    }
                    return;
                }
            }

            // ディレクトリをファイルにコピーしようとした場合
            if (src.isDirectory() && dest.isFile()) {
                LOGGER.warn("[ERROR] ディレクトリをファイルにプットすることはできません。");
                return;
            }

            // ディレクトリを存在しない場所にコピー使用とした場合
            if (src.isDirectory() && !dest.exists()) {
                LOGGER.debug("[MKDIR] " + dest.getAbsolutePath());
                dest.mkdirs();
            }

            // ディレクトリを同名ディレクトリにコピーしようとした場合
            if (src.isDirectory() && dest.isDirectory() && src.getName().equals(dest.getName())) {
                File[] fileList2 = src.listFiles();
                if (null != fileList2) {
                    for (final File child : fileList2) {
                        put(child, dest, false);
                    }
                    return;
                }
            }

            // ディレクトリを別名ディレクトリにコピーしようとした場合
            if (src.isDirectory() && dest.isDirectory() && !src.getName().equals(dest.getName())) {
                dest = new File(dest.getAbsolutePath() + File.separator + src.getName());
                LOGGER.debug("[MKDIR] " + dest.getAbsolutePath());
                dest.mkdirs();
                put(src, dest, false);
                return;
            }

            // ファイルを存在するファイルに上書きする場合
            if (src.isFile() && dest.exists() && dest.isFile()) {
                LOGGER.debug("[COPY] " + src.getAbsolutePath() + " => " + dest.getAbsolutePath());
                copy(src, dest);
                return;
            }

            // ファイルを不明な場所にプットする場合、不明な場所はファイルとする
            if (src.isFile() && !dest.exists()) {

                try {
                    dest.createNewFile();
                } catch (IOException e) {
                    throw e;
                }

                put(src, dest, false);
                return;
            }

            // ファイルをディレクトリにプットする場合
            if (src.isFile() && dest.isDirectory()) {
                dest = new File(dest.getAbsolutePath() + File.separator + src.getName());
                put(src, dest, false);
                return;
            }

        } catch (final Exception e) {

            // 例外が発生した場合は削除予定をキャンセルする
            removeSrc = false;

            if (e instanceof RuntimeException) {
                throw (RuntimeException) e;
            }

            LOGGER.warn("ファイルのプット処理に失敗しました。");
            throw new IOException(e);

        } finally {

            //
            // 移動元を削除する場合
            //

            if (removeSrc && (src.isDirectory() || "*".equals(src.getName()))) {

                if ("*".equals(src.getName())) {
                    src = src.getParentFile();
                }

                final boolean ok = removeDir(src);
                if (!ok) {
                    LOGGER.warn("ディレクトリを削除できませんでした。");
                }

            } else if (removeSrc && src.isFile()) {

                final boolean ok = remove(src);
                if (!ok) {
                    LOGGER.warn("ファイルを削除できませんでした。");
                }
            }

        }

    }

    /**
     * 指定ファイルを取り除く
     *
     * @param file  ファイル
     * @return ファイル内容
     * @throws IOException
     */
    public boolean remove(final String file) throws IOException {
        return remove(new File(file));
    }

    /**
     * 指定ファイルを取り除く
     *
     * @param file  ファイル
     * @return ファイル内容
     * @throws IOException
     */
    public boolean remove(final File file) throws IOException {
        LOGGER.debug("[RM] " + file.getAbsolutePath());
        if (!file.delete()) {
            throw new IOException();
        }
        return true;
    }

    /**
     * 指定ディレクトリ以下を削除する。
     * ファイルが指定されていた場合はそのファイルを削除する。
     *
     * @param dir   ディレクトリ
     * @return 処理結果
     * @throws IOException
     */
    public boolean removeDir(final String dir) throws IOException {
        return removeDir(new File(dir));
    }

    /**
     * 指定ディレクトリ以下を削除する。
     * ファイルが指定されていた場合はそのファイルを削除する。
     *
     * @param dir   ディレクトリ
     * @return 処理結果
     * @throws IOException
     */
    public boolean removeDir(final File dir) throws IOException {

        if (dir.isDirectory() && dir.canWrite()) {

            // 削除対象がディレクトリの場合
            return recursiveDelete(dir);

        } else if (dir.isFile() && dir.canWrite()) {

            // 削除対象がファイルの場合
            return remove(dir);
        }

        return false;
    }

    /**
     * 再帰的にファイルを削除する
     *
     * @param dir   削除対象のディレクトリ
     * @return 処理結果
     * @throws IOException
     */
    protected boolean recursiveDelete(final File dir) throws IOException {

        File[] fileList = dir.listFiles();
        if (null != fileList) {
            for (final File child : fileList) {

                if (child.isFile()) {
                    remove(child);
                }

                if (child.isDirectory()) {
                    recursiveDelete(child);
                }
            }
            LOGGER.debug("[RMDIR] " + dir.getAbsolutePath());
        }
        return dir.delete();
    }

    /**
     * 対象のファイルの文字セットを変更する
     *
     * @param target        対象ファイル
     * @param oldCharset    変更前の文字セット
     * @param newCharset    変更後の文字セット
     * @return true - 処理成功
     * @throws IOException
     */
    public boolean changeCharset(File target, String oldCharset, String newCharset) throws IOException {

        final int bufferSize = 10000;

        char[] charBuf = new char[bufferSize];

        InputStreamReader reader = null;
        OutputStreamWriter writer = null;
        File tmp = null;

        try {

            //
            // オブジェクトの準備
            //

            tmp = File.createTempFile("FileOperationUtil_changeCharset", null);
            reader = new InputStreamReader(new FileInputStream(target), oldCharset);
            writer = new OutputStreamWriter(new FileOutputStream(tmp), newCharset);

            //
            // オリジナルファイルを読み込んで一時ファイルに文字セット変換後の情報を書き出す
            //

            while (true) {
                int len = reader.read(charBuf);

                if (len <= 0) {
                    break;
                }

                writer.write(charBuf, 0, len);
            }

            writer.close();
            reader.close();

            //
            // オリジナルファイルを削除する
            //

            remove(target);

            // 一時ファイルをコピーしてオリジナルファイルの場所へ配置する
            put(tmp, target, false);

            LOGGER.debug("[CONV] " + target.getAbsolutePath() + " " + oldCharset + " => " + newCharset);

            // 一時ファイルな finally ブロックで削除される
            return true;

        } catch (IOException e) {
            throw e;
        } catch (Exception e) {

            LOGGER.warn("ファイルの文字コード変更に失敗しました", e);

            return false;

        } finally {

            try {

                if (writer != null) {
                    writer.close();
                }

            } catch (Exception e) {
                LOGGER.warn("ファイル書き込みオブジェクトのクローズに失敗しました", e);
            }

            try {
                if (reader != null) {
                    reader.close();
                }
            } catch (Exception e) {
                LOGGER.warn("ファイル読み込みオブジェクトのクローズに失敗しました", e);
            }

            try {
                if (!ObjectUtils.isEmpty(tmp)) {
                    tmp.delete();
                }
            } catch (Exception e2) {
                LOGGER.warn("一時ファイルの削除に失敗しました", e2);
            }

        }
    }

    /**
     * 権限チェックを削除し、例外を出力するように変更
     *
     * @param src コピー元のファイル
     * @param dest コピー先のファイル
     */
    protected void copy(File src, File dest) throws IOException {
        FileCopyUtils.copy(src, dest);
    }

    /**
     * ファイルに変換
     *
     * @param fileUrl ファイルパス
     * @return MultipartFile アップロードされたファイル
     */
    public MultipartFile toMultiPartFile(String fileUrl) {
        File file = new File(fileUrl);
        MultipartFile multipartFile = null;
        try {
            FileInputStream input = new FileInputStream(file);
            multipartFile = new MockMultipartFile("file", file.getName(), "text/plain", input);
        } catch (FileNotFoundException e) {
            LOGGER.error("ファイルが見つかりません: ", e.getMessage());
        } catch (IOException e) {
            LOGGER.error("エラー: ", e.getMessage());
        }
        return multipartFile;
    }

}