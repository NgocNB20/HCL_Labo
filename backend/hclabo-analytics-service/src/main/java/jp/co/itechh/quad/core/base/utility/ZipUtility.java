package jp.co.itechh.quad.core.base.utility;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Component;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.zip.ZipEntry;
import java.util.zip.ZipOutputStream;

/**
 * ZIPファイルの生成
 *
 * @author Doan Thang (VJP)
 */
@Component
public class ZipUtility {
    /**
     * ロガー
     */
    private static final Logger LOGGER = LoggerFactory.getLogger(ZipUtility.class);

    /**
     * ZIPファイルの生成
     *
     * @param folder      フォルダー
     * @param csvFileName csvファイル名
     * @return zipファイル名
     * @throws IOException 入出力例外
     */
    public String zip(String folder, String csvFileName) throws IOException {

        // csvファイル
        String csvFilePath = folder + csvFileName;
        try (FileInputStream fileInputStream = new FileInputStream(csvFilePath)) {

            // zipファイル
            String zipFilePath = folder + csvFileName.replaceAll("(?i)csv", "zip");
            try (ZipOutputStream zipOutputStream = new ZipOutputStream(new FileOutputStream(zipFilePath))) {

                // zipファイル内のcsvファイル名を設定
                zipOutputStream.putNextEntry(new ZipEntry(csvFileName));

                // zipファイル出力
                byte[] b = new byte[1024];
                int count;
                while ((count = fileInputStream.read(b)) > 0) {
                    zipOutputStream.write(b, 0, count);
                }
                zipOutputStream.close();
                fileInputStream.close();

                // CSVファイル削除
                File file = new File(csvFilePath);
                if (file.delete()) {
                    LOGGER.info("CSVファイル削除成功：" + csvFileName);
                } else {
                    LOGGER.error("CSVファイル削除失敗：" + csvFileName);
                }
            }
        }

        return csvFileName.replaceAll("(?i)csv", "zip");
    }
}
