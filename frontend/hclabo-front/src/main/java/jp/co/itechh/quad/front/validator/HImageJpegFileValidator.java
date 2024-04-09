/*
 * Project Name : HIT-MALL4
 *
 * Copyright (C) 2021 i-TEC HANKYU HANSHIN INC. All Rights Reserved.
 */

package jp.co.itechh.quad.front.validator;

import jp.co.itechh.quad.front.annotation.validator.HVImageJpegFile;
import jp.co.itechh.quad.front.base.util.common.DiffUtil;
import lombok.Data;
import org.apache.commons.lang3.StringUtils;
import org.springframework.util.ObjectUtils;
import org.springframework.web.multipart.MultipartFile;

import javax.validation.ConstraintValidator;
import javax.validation.ConstraintValidatorContext;
import java.io.IOException;
import java.io.InputStream;
import java.util.List;

/**
 * <span class="logicName">【Jpegファイル】</span>アップロードされたファイルが JPEG
 * イメージファイルであることの検証を行うクラス。<br />
 * <br />
 * JPEG イメージファイルでない場合エラー<br />
 *
 * @author kimura
 */
@Data
public class HImageJpegFileValidator implements ConstraintValidator<HVImageJpegFile, Object> {

    /**
     * jpegファイルじゃなかった場合
     */
    public static final String NOT_JPEG_MESSAGE_ID = "{HImageJpegFileValidator.INVALID_detail}";

    /**
     * ファイルが存在しなかった場合のエラーメッセージ
     */
    public static final String NOT_FOUND_MESSAGE_ID = "{HImageJpegFileValidator.NOTFOUND_detail}";

    /**
     * ファイルマジックナンバー
     */
    protected static final byte[] MAGIC_NUMBER1 = {(byte) 0xFF, (byte) 0xD8};

    /**
     * ファイル拡張子
     */
    protected static final String EXTENSION1 = ".JPEG";

    /**
     * ファイル拡張子
     */
    protected static final String EXTENSION2 = ".JPG";

    /**
     * ファイル拡張子
     */
    protected static final String EXTENSION3 = ".JPE";

    /**
     * ファイル拡張子
     */
    protected static final String EXTENSION4 = ".JFIF";

    /**
     * ファイル拡張子
     */
    protected static final String EXTENSION5 = ".JFI";

    /**
     * ファイル拡張子
     */
    protected static final String EXTENSION6 = ".JIF";

    /**
     * バリデーション実行
     *
     * @return チェックエラーの場合 false
     */
    @Override
    public boolean isValid(Object value, ConstraintValidatorContext context) {
        if (value == null || (!(value instanceof MultipartFile) && !(value instanceof MultipartFile[]))) {
            return false;
        }
        MultipartFile[] uploadeds = null;
        MultipartFile uploaded = null;
        if (value instanceof MultipartFile) {
            uploaded = (MultipartFile) value;
            boolean imageFile = false;
            try {
                imageFile = confirm(uploaded);
            } catch (IOException e) {
                return false;
            }
            // ファイルが存在しない場合
            if (uploaded.getSize() == 0) {
                makeContext(context, NOT_FOUND_MESSAGE_ID);
                return false;
            }
            // イメージファイルでない場合
            if (!imageFile) {
                makeContext(context, NOT_JPEG_MESSAGE_ID);
                return false;
            }
        } else {
            uploadeds = (MultipartFile[]) value;
            for (int i = 0; i < uploadeds.length; i++) {
                boolean imageFile = false;
                try {
                    imageFile = confirm(uploadeds[i]);
                } catch (IOException e) {
                    return false;
                }
                // ファイルが存在しない場合
                if (uploadeds[i].getSize() == 0) {
                    makeContext(context, NOT_FOUND_MESSAGE_ID);
                    return false;
                }
                // イメージファイルでない場合
                if (!imageFile) {
                    makeContext(context, NOT_JPEG_MESSAGE_ID);
                    return false;
                }
            }
        }
        return true;
    }

    /**
     * 検証する
     *
     * @param uploaded アップロードされたファイル
     * @return 検証結果
     * @throws IOException 発生した例外
     */
    static boolean confirm(MultipartFile uploaded) throws IOException {
        // 検証
        if ((confirmMagicNumber(uploaded, MAGIC_NUMBER1)) && (confirmExtension(uploaded, EXTENSION1)
                                                              || confirmExtension(uploaded, EXTENSION2)
                                                              || confirmExtension(uploaded, EXTENSION3)
                                                              || confirmExtension(uploaded, EXTENSION4)
                                                              || confirmExtension(uploaded, EXTENSION5)
                                                              || confirmExtension(uploaded, EXTENSION6))) {
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
            String originalFilename = file.getOriginalFilename();
            return StringUtils.isNotEmpty(originalFilename) && originalFilename.toUpperCase().endsWith(extension);
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

    /**
     * エラーメッセージを構成<br/>
     */
    protected void makeContext(ConstraintValidatorContext context, String messageId) {
        // 動的バリデーションによる手動バリデータの発火はこちらの設定は不要
        if (context == null) {
            return;
        }
        context.disableDefaultConstraintViolation();
        context.buildConstraintViolationWithTemplate(messageId).addConstraintViolation();
    }
}