/*
 * Project Name : HIT-MALL3
 *
 * Copyright (C) 2008 i-TEC HANKYU HANSHIN INC. All Rights Reserved.
 *
 */

package jp.co.itechh.quad.core.base.utility;

import org.springframework.stereotype.Component;

import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;

/**
 * 出力ストリームと入力ストリームを連結するヘルパークラス。
 *
 * @author tm27400
 * @author Kaneko (itec) 2012/08/17 UtilからHelperへ変更
 */
@Component
public class StreamJointUtility {

    /**
     * コンストラクタの説明・概要
     */
    public StreamJointUtility() {
    }

    /**
     * 引数の出力ストリームから引数の入力ストリームへ出力する。
     *
     * @param outStream 出力ストリーム
     * @param inStream  入力ストリーム
     * @throws IOException 発生した例外
     */
    public void joint(final OutputStream outStream, final InputStream inStream) throws IOException {

        final byte[] buffer = new byte[1024];

        try {

            while (true) {

                int size = inStream.read(buffer);
                if (size == -1) {
                    break;
                }

                outStream.write(buffer, 0, size);
            }

        } catch (final IOException e) {
            throw new IOException(e);
        }

    }
}
