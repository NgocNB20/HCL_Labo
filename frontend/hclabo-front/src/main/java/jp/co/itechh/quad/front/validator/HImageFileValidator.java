/*
 * Project Name : HIT-MALL4
 *
 * Copyright (C) 2021 i-TEC HANKYU HANSHIN INC. All Rights Reserved.
 */

package jp.co.itechh.quad.front.validator;

import jp.co.itechh.quad.front.annotation.validator.HVImageFile;
import lombok.Data;
import org.springframework.web.multipart.MultipartFile;

import javax.validation.ConstraintValidator;
import javax.validation.ConstraintValidatorContext;
import java.io.IOException;

/**
 * <span class=
 * "logicName">【イメージファイル】</span>アップロードされたファイルがイメージファイルであることの検証を行うクラス。<br />
 * <br />
 * JPEG イメージファイル、GIF イメージファイル、PNG イメージファイルのいずれかでない場合エラー<br />
 *
 * @author kimura
 */
@Data
public class HImageFileValidator implements ConstraintValidator<HVImageFile, Object> {

    /**
     * イメージファイルじゃなかった場合のエラーメッセージ
     */
    public static final String NOT_IMG_MESSAGE_ID = "{HImageFileValidator.INVALID_detail}";

    /**
     * ファイルが存在しなかった場合のエラーメッセージ
     */
    public static final String NOT_FOUND_MESSAGE_ID = "{HImageFileValidator.NOTFOUND_detail}";

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
                if (HImageJpegFileValidator.confirm(uploaded) || HImageGifFileValidator.confirm(uploaded)
                    || HImagePngFileValidator.confirm(uploaded)) {
                    imageFile = true;
                }
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
                makeContext(context, NOT_IMG_MESSAGE_ID);
                return false;
            }
        } else {
            uploadeds = (MultipartFile[]) value;
            for (int i = 0; i < uploadeds.length; i++) {
                boolean imageFile = false;
                try {
                    if (HImageJpegFileValidator.confirm(uploadeds[i]) || HImageGifFileValidator.confirm(uploadeds[i])
                        || HImagePngFileValidator.confirm(uploadeds[i])) {
                        imageFile = true;
                    }
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
                    makeContext(context, NOT_IMG_MESSAGE_ID);
                    return false;
                }
            }
        }
        return true;
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