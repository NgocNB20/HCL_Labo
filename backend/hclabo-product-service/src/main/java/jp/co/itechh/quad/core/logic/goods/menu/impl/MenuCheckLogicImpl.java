/*
 * Project Name : HIT-MALL4
 *
 * Copyright (C) 2023 i-TEC HANKYU HANSHIN INC. All Rights Reserved.
 *
 */

package jp.co.itechh.quad.core.logic.goods.menu.impl;

import jp.co.itechh.quad.core.base.utility.ConversionUtility;
import jp.co.itechh.quad.core.dto.goods.category.CategoryTreeJsonDto;
import jp.co.itechh.quad.core.logic.AbstractShopLogic;
import jp.co.itechh.quad.core.logic.goods.menu.MenuCheckLogic;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

/**
 * メニュー入力バリデータ
 *
 * @author PHAM QUANG DIEU (VJP)
 */
@Component
public class MenuCheckLogicImpl extends AbstractShopLogic implements MenuCheckLogic {

    /** 変換Helper */
    private final ConversionUtility conversionUtility;

    /** エラーコード：入力必須 */
    public static final String MSGCD_REQUIRED = "javax.validation.constraints.NotNull.message";

    /** 最小入力文字数 */
    private static final int MIN_LENGTH = 1;

    /** 最大入力文字数 */
    private static final int MAX_LENGTH = 120;

    /** エラーコード：入力文字数 */
    private static final String LENGTH_MSG_ID = "HNumberLengthValidator.MIN_MAX";

    /**
     * コンストラクタ
     *
     * @param conversionUtility 変換Helper
     */
    @Autowired
    public MenuCheckLogicImpl(ConversionUtility conversionUtility) {
        this.conversionUtility = conversionUtility;
    }

    /**
     * メニュー入力バリデータ
     *
     * @param categoryTreeJson カテゴリーツリーJSON
     */
    @Override
    public void checkForUpdate(String categoryTreeJson) {

        if (StringUtils.isNotEmpty(categoryTreeJson)) {

            CategoryTreeJsonDto categoryTreeDto =
                            conversionUtility.toObject(categoryTreeJson, CategoryTreeJsonDto.class);

            if (categoryTreeDto != null) {
                // 必須チェック
                if (StringUtils.isEmpty(categoryTreeDto.getDisplayName())) {
                    throwMessage(MSGCD_REQUIRED);
                } else {
                    // 入力文字数チェック
                    if (categoryTreeDto.getDisplayName().length() > MAX_LENGTH) {
                        throwMessage(LENGTH_MSG_ID, new Object[] {MIN_LENGTH, MAX_LENGTH});
                    }
                }
            }
        }
    }
}