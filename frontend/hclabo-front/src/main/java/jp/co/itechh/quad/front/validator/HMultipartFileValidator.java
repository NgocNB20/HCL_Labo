/*
 * Project Name : HIT-MALL4
 *
 * Copyright (C) 2021 i-TEC HANKYU HANSHIN INC. All Rights Reserved.
 *
 */

package jp.co.itechh.quad.front.validator;

import jp.co.itechh.quad.front.annotation.validator.HVMultipartFile;
import lombok.Data;
import org.springframework.util.StringUtils;
import org.springframework.web.multipart.MultipartFile;

import javax.validation.ConstraintValidator;
import javax.validation.ConstraintValidatorContext;
import java.util.Arrays;
import java.util.List;

/**
 * <span class="logicName">【ファイル】</span>MultipartFileファイルのチェックバリデータ。<br />
 * <br />
 * ファイルがアップロードされなかった場合 or 不当なファイルがアップロードされた場合はエラー<br />
 *
 * @author Phan Tien VU (VTI Japan Co., Ltd.)
 */
@Data
public class HMultipartFileValidator implements ConstraintValidator<HVMultipartFile, MultipartFile> {

    /** ファイルがアップロードされなかった場合 */
    public static final String FILE_EMPTY_MESSAGE_ID = "{javax.validation.constraints.NotEmpty.message}";

    /** ファイルがアップロードされたファイル拡張子が不適切な場合 */
    public static final String FILE_EXTENSION_MESSAGE_ID = "{HMultipartFileValidator.INVALID_detail}";

    /** デフォルト値のファイル拡張子 */
    public static final String[] DEFAULT_FILE_EXTENSION = {"csv"};

    /** 妥当なファイル拡張子一覧 */
    private String[] fileExtensionArr = DEFAULT_FILE_EXTENSION;

    /** アノテーションの情報設定 */
    @Override
    public void initialize(HVMultipartFile annotation) {
        this.fileExtensionArr = annotation.fileExtension();
    }

    /**
     * バリデーション実行
     *
     * @return チェックエラーの場合 false
     */
    @Override
    public boolean isValid(MultipartFile value, ConstraintValidatorContext context) {
        // ファイルがアップロードされなかった場合
        if (value == null || !StringUtils.hasLength(value.getOriginalFilename())) {
            return false;
        }
        // ファイル拡張子のバリデーション
        if (!checkFileExtension(value.getOriginalFilename())) {
            makeContext(context, FILE_EXTENSION_MESSAGE_ID);
            return false;
        }
        return true;
    }

    /**
     * ファイル拡張子のバリデーション<br/>
     */
    private boolean checkFileExtension(String fileName) {
        if (StringUtils.hasLength(fileName)) {
            int lastIndexOf = fileName.lastIndexOf(".");

            // 拡張子なしの場合
            if (lastIndexOf == -1) {
                return false;
            }

            // 不当な拡張子の場合
            List<String> fileExtensionList = Arrays.asList(this.fileExtensionArr);
            String fileExtension = fileName.substring(lastIndexOf + 1);
            if (!fileExtensionList.contains(fileExtension)) {
                return false;
            }
        } else {
            return false;
        }

        return true;
    }

    /**
     * エラーメッセージを構成<br/>
     * メッセージセット＋エラーメッセージを表示する項目を指定
     */
    protected void makeContext(ConstraintValidatorContext context, String messageId) {
        context.disableDefaultConstraintViolation();
        context.buildConstraintViolationWithTemplate(messageId).addConstraintViolation();
    }

}
