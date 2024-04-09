package jp.co.itechh.quad.core.base.utility;

import jp.co.itechh.quad.core.base.util.common.DiffUtil;
import org.apache.tools.zip.ZipEntry;
import org.apache.tools.zip.ZipFile;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Component;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.Enumeration;
import java.util.List;

@Component
public class UnzipUtility {

    /**
     * コンストラクタ
     */
    public UnzipUtility() {
    }

    /**
     * ロガー
     */
    private static final Logger LOGGER = LoggerFactory.getLogger(UnzipUtility.class);

    /**
     * ZIP ファイルのマジックナンバー
     */
    protected static final byte[] ZIP_MAGIC_NUMBER =
                    ("PK" + Character.toString((char) 3) + Character.toString((char) 4)).getBytes();

    /**
     * ファイル名のエンコーディング
     */
    protected static final String ENCODING = "MS932";

    /**
     * ZIP ファイルを解凍する。
     *
     * @param file            ファイル
     * @param filePutPath     解凍先ディレクトリ ※'/' で終わること
     * @param isConvExtension 拡張子の変換有無（true:拡張子を小文字に変換、false：拡張子の変換なし）
     * @return 解凍されたファイルのパス一覧
     */
    public List<File> unzip(final File file, final String filePutPath, boolean isConvExtension) throws IOException {

        final List<File> extractList = new ArrayList<>();

        if (file == null || !file.exists() || filePutPath == null) {
            LOGGER.warn("ファイルが存在しない " + (file != null ? file.getAbsolutePath() : ""));
            throw new IllegalArgumentException("ファイルが存在しない " + (file != null ? file.getAbsolutePath() : ""));
        }

        ZipFile zipFile = null;

        try {

            LOGGER.debug("zip ファイルの格納パス：" + file.getAbsolutePath());

            // マジックナンバーの確認
            if (!confirmMagicNumber(file)) {
                LOGGER.warn("解凍しようとしたファイルは zip ファイルではありません。");
                file.delete();
                throw new IOException(new IOException("File is not a ZIP archive."));
            }

            LOGGER.debug("zip ファイルの解凍を開始します。" + file.getAbsolutePath());

            zipFile = new ZipFile(file, ENCODING);

            //
            // ZIPエントリ毎の処理
            //

            for (final Enumeration<?> enu = zipFile.getEntries(); enu.hasMoreElements(); ) {

                final ZipEntry entry = (ZipEntry) enu.nextElement();

                // ZIPエントリがディレクトリの場合は処理をスキップ
                if (entry.isDirectory()) {
                    continue;
                }

                InputStream inStream = null;
                FileOutputStream outStream = null;

                try {

                    //
                    // ZIPエントリのファイルを受け入れる空ファイルの作成
                    //

                    String fileName = entry.getName();
                    int extIndex = fileName.lastIndexOf(".");
                    if (isConvExtension && extIndex > 0) {
                        // ファイル拡張子を小文字に変換する
                        fileName = fileName.substring(0, extIndex) + "." + fileName.substring(extIndex + 1,
                                                                                              fileName.length()
                                                                                             ).toLowerCase();
                    }
                    final File outFile = new File(filePutPath + fileName);
                    outFile.getParentFile().mkdirs();
                    outStream = new FileOutputStream(outFile);
                    inStream = zipFile.getInputStream(entry);

                    // ストリーム連結Helper取得
                    StreamJointUtility streamJointUtility = ApplicationContextUtility.getBean(StreamJointUtility.class);

                    // 空ファイルに ZIPエントリファイル情報を書き込む
                    streamJointUtility.joint(outStream, inStream);

                    extractList.add(outFile);

                } finally {

                    //
                    // 後始末
                    //

                    if (outStream != null) {
                        outStream.close();
                    }

                    if (inStream != null) {
                        inStream.close();
                    }
                }
            }

        } catch (final IOException e) {

            LOGGER.warn("ZIP ファイルの解凍に失敗しました。", e);

            throw new IOException(e);

        } catch (final Exception e) {

            LOGGER.warn("ZIP ファイルの解凍に失敗しました。", e);

            throw new RuntimeException(e);

        } finally {

            if (zipFile != null) {

                try {
                    zipFile.close();
                } catch (IOException e) {
                    LOGGER.warn(zipFile + " を開放できません。");
                }

            }

            file.delete();

        }

        return extractList;
    }

    /**
     * ファイルのマジックナンバーが ZIP ファイルのマジックナンバーと一致するかどうかを確認する。
     *
     * @param zipFile zip か同かを確認するファイル。
     * @return true - zip ファイルのマジックナンバーである。
     * @throws IOException 発生した例外
     */
    public boolean confirmMagicNumber(final File zipFile) throws IOException {

        FileInputStream inStream = null;

        try {

            inStream = new FileInputStream(zipFile);
            final byte[] magicNumber = new byte[4];
            inStream.read(magicNumber);

            return DiffUtil.diff(ZIP_MAGIC_NUMBER, magicNumber).isEmpty();

        } finally {

            if (inStream != null) {
                inStream.close();
            }

        }

    }
}