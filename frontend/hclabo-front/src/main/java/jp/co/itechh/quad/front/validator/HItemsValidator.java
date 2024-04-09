/*
 * Project Name : HIT-MALL4
 *
 * Copyright (C) 2021 i-TEC HANKYU HANSHIN INC. All Rights Reserved.
 */

package jp.co.itechh.quad.front.validator;

import jp.co.itechh.quad.front.annotation.validator.HVItems;
import jp.co.itechh.quad.front.constant.type.EnumType;
import jp.co.itechh.quad.front.util.common.EnumTypeUtil;
import lombok.Data;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.validation.ConstraintValidator;
import javax.validation.ConstraintValidatorContext;
import java.util.Arrays;
import java.util.List;
import java.util.Objects;

/**
 * HTypeに値が含まれるかの入力チェックバリデータ<br />
 *
 * @author kaneda
 */
@Data
public class HItemsValidator implements ConstraintValidator<HVItems, Object> {

    /** 含まれていない場合 */
    public static final String INVALID_MESSAGE_ID = "{HItemsValidator.INVALID_detail}";

    /** ロガー */
    private static final Logger LOGGER = LoggerFactory.getLogger(HItemsValidator.class);

    private Class<? extends EnumType> targetHType;
    private String[] targetArray;

    /**
     * アノテーションの情報設定
     */
    @Override
    public void initialize(HVItems annotation) {
        targetHType = annotation.target();
        targetArray = annotation.targetArray();
    }

    /**
     * バリデーション実行
     *
     * @return チェックエラーの場合 false
     */
    @Override
    public boolean isValid(Object value, ConstraintValidatorContext context) {

        // null かつ 空でない場合実施
        if (!Objects.equals(value, null) && !Objects.equals(value, "")) {
            // HTYPEではなく任意String[]で検証したい場合
            if (!Objects.equals(targetArray, null) && targetArray.length > 0) {
                return isValidArray(value);
            }
            if (value instanceof String && !EnumTypeUtil.isExist(targetHType, (String) value)) {
                // 型がStringで対象Enumに値無しの場合
                return false;
            } else if (value instanceof String[]) {
                // 型がString[]で対象Enumに値無しの場合
                for (String curValue : (String[]) value) {
                    if (!EnumTypeUtil.isExist(targetHType, curValue)) {
                        return false;
                    }
                }
            }
        }
        return true;
    }

    /**
     * HTYPEではなく任意String[]で検証したい場合
     *
     * @param value
     * @return 妥当な区分値であるかどうか
     */
    private boolean isValidArray(Object value) {
        List<String> targetList = Arrays.asList(targetArray);
        if (value instanceof String) {
            return targetList.contains(value);
        } else if (value instanceof String[]) {
            // 型がString[]で対象Enumに値無しの場合
            for (String curValue : (String[]) value) {
                if (!targetList.contains(curValue)) {
                    return false;
                }
            }
        }
        return true;
    }
}