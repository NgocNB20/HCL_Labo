/*
 * Project Name : HIT-MALL4
 *
 * Copyright (C) 2021 i-TEC HANKYU HANSHIN INC. All Rights Reserved.
 */

package jp.co.itechh.quad.admin.validator;

import jp.co.itechh.quad.admin.base.util.common.DiffUtil;
import lombok.Data;
import org.apache.commons.lang3.StringUtils;
import org.springframework.util.ObjectUtils;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.io.InputStream;
import java.util.List;

/**
 * <span class="logicName">【Gifファイル】</span>アップロードされたファイルが GIF
 * イメージファイルであることの検証を行うクラス。<br />
 * <br />
 * GIF イメージファイルでない場合エラー<br />
 * <p>
 * このアノテーションバリデーションは未使用のため、バリデートの継承はしていない<br />
 * 内部メソッドのみ呼ばれていたので作成
 *
 * @author kimura
 */
@Data
public class HImageGifFileValidator {

    /**
     * 拡張子
     */
    protected static final String EXTENSION1 = ".GIF";

    /**
     * マジックナンバー
     */
    protected static final byte[] MAGIC_NUMBER1 = "GIF".getBytes();

    /**
     * 検証する
     *
     * @param uploaded アップロードされたファイル
     * @return 検証結果
     * @throws IOException 発生した例外
     */
    static boolean confirm(MultipartFile uploaded) throws IOException {
        // 検証
        if (confirmExtension(uploaded, EXTENSION1) && confirmMagicNumber(uploaded, MAGIC_NUMBER1)) {
            return true;
        }
        return false;
    }

    /**
     * 拡張子の検証
     *
     * @param file      アップロードされたファイル
     * @param extension あるべき拡張子
     * @return 検証結果
     */
    protected static boolean confirmExtension(MultipartFile file, String extension) {
        if (!ObjectUtils.isEmpty(file)) {
            String fileName = file.getOriginalFilename();
            return !StringUtils.isEmpty(fileName) && fileName.toUpperCase().endsWith(extension);
        } else {
            return false;
        }
    }

    /**
     * マジックナンバーの検証
     *
     * @param file        アップロードされたファイル
     * @param magicNumber あるべきマジックナンバー
     * @return 検証結果
     * @throws IOException 発生した例外
     */
    protected static boolean confirmMagicNumber(MultipartFile file, final byte[] magicNumber) throws IOException {
        // マジックナンバーすら確認できない場合
        if (file.getSize() < magicNumber.length) {
            return false;
        }
        InputStream inStream = null;
        try {
            inStream = file.getInputStream();
            byte[] fileMagicNumber = new byte[magicNumber.length];
            inStream.read(fileMagicNumber);
            final List<String> comp = DiffUtil.diff(magicNumber, fileMagicNumber);
            return comp.isEmpty();
        } finally {
            if (inStream != null) {
                inStream.close();
            }
        }
    }
}
