package jp.co.itechh.quad.front.web;

import org.springframework.web.servlet.mvc.method.annotation.StreamingResponseBody;

import java.io.BufferedInputStream;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;

/**
 * 新HIT-MALLのダウンロード機能
 * ファイルダウンロードストリーミングレスポンスボディー
 * 作成日：2024/02/06
 *
 * @author Hoang Khanh Nam (VTI Japan Co., Ltd.)
 */
public class DownloadFileStreamingResponseBody implements StreamingResponseBody {

    /**
     * バッファサイズ：１MB
     */
    private static final int BUFFER_SIZE = 1 * 1024 * 1024;

    /**
     * ファイルのパス
     */
    private final String filePath;

    /**
     * コンストラクタ
     *
     * @param filePath ファイルのパス
     */
    public DownloadFileStreamingResponseBody(String filePath) {
        this.filePath = filePath;
    }

    /**
     * ファイルをStreamingresponsebodyに書き込みます
     *
     * @param outputStream 出力ストリーム
     * @throws IOException 発生した例外
     */
    @Override
    public void writeTo(OutputStream outputStream) throws IOException {
        try (InputStream input = new BufferedInputStream(
                        new FileInputStream(filePath))) {
            byte[] buffer = new byte[BUFFER_SIZE];
            int len = 0;
            while ((len = input.read(buffer)) != -1) {
                outputStream.write(buffer, 0, len);
                outputStream.flush();
            }
        }
    }

}
