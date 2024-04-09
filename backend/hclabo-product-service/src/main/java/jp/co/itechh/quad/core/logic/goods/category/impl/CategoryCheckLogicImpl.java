/*
 * Project Name : HIT-MALL4
 *
 * Copyright (C) 2023 i-TEC HANKYU HANSHIN INC. All Rights Reserved.
 *
 */

package jp.co.itechh.quad.core.logic.goods.category.impl;

import jp.co.itechh.quad.core.base.constant.ValidatorConstants;
import jp.co.itechh.quad.core.base.util.common.PropertiesUtil;
import jp.co.itechh.quad.core.base.util.seasar.StringUtil;
import jp.co.itechh.quad.core.base.utility.ApplicationContextUtility;
import jp.co.itechh.quad.core.base.utility.DateUtility;
import jp.co.itechh.quad.core.base.utility.NumberUtility;
import jp.co.itechh.quad.core.constant.type.HTypeCategoryType;
import jp.co.itechh.quad.core.constant.type.HTypeConditionColumnType;
import jp.co.itechh.quad.core.constant.type.HTypeConditionOperatorType;
import jp.co.itechh.quad.core.dto.goods.category.CategoryDto;
import jp.co.itechh.quad.core.entity.goods.category.CategoryConditionDetailEntity;
import jp.co.itechh.quad.core.logic.AbstractShopLogic;
import jp.co.itechh.quad.core.logic.goods.category.CategoryCheckLogic;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.Arrays;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.regex.Pattern;

/**
 * カテゴリ入力バリデータ
 *
 * @author PHAM QUANG DIEU (VJP)
 */
@Component
public class CategoryCheckLogicImpl extends AbstractShopLogic implements CategoryCheckLogic {

    /** 最小入力文字数 */
    private static final int MIN_LENGTH = 1;
    /** 最大入力文字数 */
    private static final int MAX_LENGTH = 100;
    /** エラーコード：選択必須 */
    public static final String MSGCD_SELECT_REQUIRED = "HRequiredValidator.REQUIRED_detail";
    /** 商品検索条件未入力 */
    public static final String MSGCD_MIN = "AGC000601E";
    /** 商品検索条件の件数は上限を超える */
    public static final String MSGCD_MAX = "AGC000602E";
    /** 条件項目と条件式が一致しません */
    public static final String MSGCD_NOT_MATCHING = "AGC000603E";
    /** エラーコード：入力文字数 */
    private static final String LENGTH_MSG_ID = "AGC000604E";
    /** 商品管理番号　正規表現　メッセージ */
    public static final String MSGCD_REGEX_GOODS_GROUP_CODE = "CGG000101E";
    /** デフォルトのフォーマットパターン */
    public static final String DEFAULT_DATE_PATTERN = "yyyy/MM/dd";
    /** フォーマットパターン（カンマで区切って複数指定可能） */
    private String pattern = DEFAULT_DATE_PATTERN;
    /** エラーコード：入力必須 */
    public static final String MSGCD_REQUIRED = "javax.validation.constraints.NotNull.message";
    /** 数値じゃなかった場合 */
    public static final String NOT_NUMBER_MESSAGE_ID = "HNumberValidator.NOT_NUMBER_detail";
    /** 日付じゃなかった場合 */
    public static final String NOT_DATE_MESSAGE_ID = "HDateValidator.NOT_DATE_detail";
    /** 制御文字 0x80 - 0x9F \p{Cntrl}には含まれない為独自に定義する */
    public static final String CONTROL_CHARCTER_0X80_0X9F = "\\x80\\x81\\x82\\x83\\x84\\x85\\x86"
                                                            + "\\x87\\x88\\x89\\x8a\\x8b\\x8c\\x8d\\x8e\\x8f\\x90\\x91\\x92\\x93\\x94"
                                                            + "\\x95\\x96\\x97\\x98\\x99\\x9a\\x9b\\x9c\\x9d\\x9e\\x9f";
    /** 特殊文字が含まれない正規表現 */
    public static final String NO_SPECIAL_CHARACTER_REGEX =
                    "[^\\p{Cntrl}\\p{Punct}" + CONTROL_CHARCTER_0X80_0X9F + "]*";
    /** 項目名 */
    public static final String FILED_NAME_CONDITION_ITEMS_LIST = "categoryConditionItems";

    /** 商品管理番号 */
    private static final List<String> CONDITION_GOODS_CODE = Arrays.asList(HTypeConditionOperatorType.MATCH.getValue(),
                                                                           HTypeConditionOperatorType.NOT_MATCH.getValue(),
                                                                           HTypeConditionOperatorType.START_ON.getValue(),
                                                                           HTypeConditionOperatorType.END_ON.getValue(),
                                                                           HTypeConditionOperatorType.CONTAINS.getValue(),
                                                                           HTypeConditionOperatorType.NOT_CONTAINS.getValue()
                                                                          );
    /** 商品名 */
    private static final List<String> CONDITION_GOODS_NAME = Arrays.asList(HTypeConditionOperatorType.MATCH.getValue(),
                                                                           HTypeConditionOperatorType.NOT_MATCH.getValue(),
                                                                           HTypeConditionOperatorType.START_ON.getValue(),
                                                                           HTypeConditionOperatorType.END_ON.getValue(),
                                                                           HTypeConditionOperatorType.CONTAINS.getValue(),
                                                                           HTypeConditionOperatorType.NOT_CONTAINS.getValue()
                                                                          );
    /** 商品タグ */
    private static final List<String> CONDITION_GOODS_TAG =
                    Collections.singletonList(HTypeConditionOperatorType.MATCH.getValue());
    /** アイコン */
    private static final List<String> CONDITION_ICON = Arrays.asList(HTypeConditionOperatorType.MATCH.getValue(),
                                                                     HTypeConditionOperatorType.SETTING.getValue(),
                                                                     HTypeConditionOperatorType.NO_SETTING.getValue()
                                                                    );
    /** 納期 */
    private static final List<String> CONDITION_TYPE = Arrays.asList(HTypeConditionOperatorType.MATCH.getValue(),
                                                                     HTypeConditionOperatorType.NOT_MATCH.getValue(),
                                                                     HTypeConditionOperatorType.START_ON.getValue(),
                                                                     HTypeConditionOperatorType.END_ON.getValue(),
                                                                     HTypeConditionOperatorType.CONTAINS.getValue(),
                                                                     HTypeConditionOperatorType.NOT_CONTAINS.getValue()
                                                                    );
    /** 価格（税抜） */
    private static final List<String> CONDITION_PRICE = Arrays.asList(HTypeConditionOperatorType.MATCH.getValue(),
                                                                      HTypeConditionOperatorType.NOT_MATCH.getValue(),
                                                                      HTypeConditionOperatorType.GREATER.getValue(),
                                                                      HTypeConditionOperatorType.LESS.getValue()
                                                                     );
    /** 新着日付 */
    private static final List<String> CONDITION_ARRIVAL_DATE =
                    Arrays.asList(HTypeConditionOperatorType.MATCH.getValue(),
                                  HTypeConditionOperatorType.NOT_MATCH.getValue(),
                                  HTypeConditionOperatorType.GREATER.getValue(),
                                  HTypeConditionOperatorType.LESS.getValue()
                                 );
    /** 販売可能在庫数 */
    private static final List<String> CONDITION_SELLABLE_STOCK =
                    Arrays.asList(HTypeConditionOperatorType.MATCH.getValue(),
                                  HTypeConditionOperatorType.GREATER.getValue(),
                                  HTypeConditionOperatorType.LESS.getValue()
                                 );
    /** 規格１表示名・設定値 */
    private static final List<String> CONDITION_STANDARD_1 = Arrays.asList(HTypeConditionOperatorType.MATCH.getValue(),
                                                                           HTypeConditionOperatorType.NOT_MATCH.getValue(),
                                                                           HTypeConditionOperatorType.START_ON.getValue(),
                                                                           HTypeConditionOperatorType.END_ON.getValue(),
                                                                           HTypeConditionOperatorType.CONTAINS.getValue(),
                                                                           HTypeConditionOperatorType.NOT_CONTAINS.getValue()
                                                                          );
    /** 規格２表示名・設定値 */
    private static final List<String> CONDITION_STANDARD_2 = Arrays.asList(HTypeConditionOperatorType.MATCH.getValue(),
                                                                           HTypeConditionOperatorType.NOT_MATCH.getValue(),
                                                                           HTypeConditionOperatorType.START_ON.getValue(),
                                                                           HTypeConditionOperatorType.END_ON.getValue(),
                                                                           HTypeConditionOperatorType.CONTAINS.getValue(),
                                                                           HTypeConditionOperatorType.NOT_CONTAINS.getValue()
                                                                          );
    /** 整合性チェックマップ */
    private static final Map<String, List<String>> CONSISTENCY_CHECK_MAP = initConsistencyCheckMap();

    @Autowired
    public CategoryCheckLogicImpl() {
    }

    /**
     * カテゴリ入力バリデータ
     *
     * @param categoryDto カテゴリDtoクラス
     */
    @Override
    public void checkForRegistUpdate(CategoryDto categoryDto) {
        // カテゴリー種別＝自動の場合
        if (HTypeCategoryType.AUTO.equals(categoryDto.getCategoryEntity().getCategoryType())) {
            // 商品の条件合致の条件付き必須チェック
            if (StringUtils.isEmpty(categoryDto.getCategoryConditionEntity().getConditionType())) {
                throwMessage(MSGCD_SELECT_REQUIRED);
            }

            // 条件項目ー条件式ー条件値の最小件数チェック
            if (categoryDto.getCategoryConditionDetailEntityList() == null
                || categoryDto.getCategoryConditionDetailEntityList().size() == 0) {
                throwMessage(MSGCD_MIN);
            }

            if (categoryDto.getCategoryConditionDetailEntityList() != null) {
                // 条件項目ー条件式ー条件値の最大件数チェック
                int maxSize = Integer.parseInt(
                                PropertiesUtil.getSystemPropertiesValue("auto.category.condition.items.length"));
                if (categoryDto.getCategoryConditionDetailEntityList().size() > maxSize) {
                    throwMessage(MSGCD_MAX, new Object[] {maxSize});
                }

                // 組合せ必須チェック
                checkCombinationRequired(categoryDto.getCategoryConditionDetailEntityList());

                // 整合性チェック
                checkConsistency(categoryDto.getCategoryConditionDetailEntityList());
            }
        }

        // エラーを表示
        if (hasErrorList()) {
            throwMessage();
        }
    }

    /**
     * 組合せ必須チェック
     *
     * @param categoryConditionDetailEntityList カテゴリ条件詳細エンティティリスト
     */
    private void checkCombinationRequired(List<CategoryConditionDetailEntity> categoryConditionDetailEntityList) {
        for (int i = 0; i < categoryConditionDetailEntityList.size(); i++) {
            // カテゴリー条件項目取得
            CategoryConditionDetailEntity item = categoryConditionDetailEntityList.get(i);
            String column = item.getConditionColumn();
            String operator = item.getConditionOperator();
            String value = item.getConditionValue();
            if (column == null && operator == null && value == null) {
                addErrorMessage(MSGCD_SELECT_REQUIRED);
                addErrorMessage(MSGCD_REQUIRED);
            } else if (column != null && operator != null && value == null
                       && !HTypeConditionOperatorType.SETTING.getValue().equals(operator)
                       && !HTypeConditionOperatorType.NO_SETTING.getValue().equals(operator)) {
                addErrorMessage(MSGCD_REQUIRED);
            } else if (column != null && operator == null && value == null) {
                addErrorMessage(MSGCD_SELECT_REQUIRED);
                addErrorMessage(MSGCD_REQUIRED);
            } else if (column != null && operator == null) {
                addErrorMessage(MSGCD_SELECT_REQUIRED);
            } else if (column == null && operator != null && value != null) {
                addErrorMessage(MSGCD_SELECT_REQUIRED);
            } else if (column == null && operator != null) {
                addErrorMessage(MSGCD_SELECT_REQUIRED);
                addErrorMessage(MSGCD_REQUIRED);
            } else if (column == null) {
                addErrorMessage(MSGCD_SELECT_REQUIRED);
                addErrorMessage(MSGCD_SELECT_REQUIRED);
            }
        }
    }

    /**
     * 整合性チェック
     *
     * @param categoryConditionDetailEntityList カテゴリ条件詳細エンティティリスト
     */
    private void checkConsistency(List<CategoryConditionDetailEntity> categoryConditionDetailEntityList) {
        for (int i = 0; i < categoryConditionDetailEntityList.size(); i++) {
            CategoryConditionDetailEntity item = categoryConditionDetailEntityList.get(i);
            String column = item.getConditionColumn();
            String operator = item.getConditionOperator();
            String value = item.getConditionValue();

            if (StringUtil.isEmpty(value)) {
                value = null;
            }

            if (column == null) {
                return;
            }

            // 整合性チェック
            if (!CONSISTENCY_CHECK_MAP.get(column).contains(operator)) {
                addErrorMessage(MSGCD_NOT_MATCHING);
            }
            // 桁数チェック
            if (value != null && (MIN_LENGTH > value.length() || value.length() > MAX_LENGTH)) {
                addErrorMessage(LENGTH_MSG_ID, new Object[] {String.valueOf(MIN_LENGTH), String.valueOf(MAX_LENGTH)});
            }
            // 新着日付チェック
            if (column.equals(HTypeConditionColumnType.ARRIVAL_DATE.getValue())) {

                // 日付関連Helper取得
                DateUtility dateUtility = ApplicationContextUtility.getBean(DateUtility.class);

                // パターンはカンマで分割
                String[] patterns = this.pattern.split(",");

                if (!dateUtility.isDate(value, patterns)) {
                    addErrorMessage(NOT_DATE_MESSAGE_ID);
                }

            }
            // 価格（税抜）・販売可能在庫数チェック
            else if (column.equals(HTypeConditionColumnType.PRICE.getValue()) || column.equals(
                            HTypeConditionColumnType.SELLABLE_STOCK.getValue())) {

                // 数値関連Helper取得
                NumberUtility numberUtility = ApplicationContextUtility.getBean(NumberUtility.class);

                if (!numberUtility.isNumber(value)) {
                    addErrorMessage(NOT_NUMBER_MESSAGE_ID);
                }
            }
            // 商品管理番号チェック
            else if (column.equals(HTypeConditionColumnType.GOODS_GROUP_SEQ.getValue())) {
                String regex = ValidatorConstants.REGEX_GOODS_GROUP_CODE;
                Pattern pattern = Pattern.compile(regex);
                if (value != null && !pattern.matcher(value).matches()) {
                    addErrorMessage(MSGCD_REGEX_GOODS_GROUP_CODE);
                }
            }
            // 商品名・商品タグ・アイコン・規格１表示名・規格１設定値・規格２表示名・規格２設定値チェック
            else if (column.equals(HTypeConditionColumnType.GOOD_NAME.getValue()) || column.equals(
                            HTypeConditionColumnType.GOOD_TAG.getValue()) || column.equals(
                            HTypeConditionColumnType.ICON.getValue()) || column.equals(
                            HTypeConditionColumnType.STANDARD_NAME_1.getValue()) || column.equals(
                            HTypeConditionColumnType.STANDARD_VALUE_1.getValue()) || column.equals(
                            HTypeConditionColumnType.STANDARD_NAME_2.getValue()) || column.equals(
                            HTypeConditionColumnType.STANDARD_VALUE_2.getValue())) {
                // 記号を含んでいた場合エラー
                if (StringUtil.isNotEmpty(value) && !Pattern.matches(NO_SPECIAL_CHARACTER_REGEX, value)) {
                    addErrorMessage("SGP001109");
                }
            }
            // 納期チェック
            else if (column.equals(HTypeConditionColumnType.TYPE.getValue())) {
                // 何もしない
            }
        }
    }

    /**
     * 初期整合性チェックマップ。
     *
     * @return 整合性チェックマップ
     */
    private static final Map<String, List<String>> initConsistencyCheckMap() {
        Map<String, List<String>> initMap = new HashMap<>();
        initMap.put(HTypeConditionColumnType.GOODS_GROUP_SEQ.getValue(), CONDITION_GOODS_CODE);
        initMap.put(HTypeConditionColumnType.GOOD_NAME.getValue(), CONDITION_GOODS_NAME);
        initMap.put(HTypeConditionColumnType.GOOD_TAG.getValue(), CONDITION_GOODS_TAG);
        initMap.put(HTypeConditionColumnType.ICON.getValue(), CONDITION_ICON);
        initMap.put(HTypeConditionColumnType.TYPE.getValue(), CONDITION_TYPE);
        initMap.put(HTypeConditionColumnType.PRICE.getValue(), CONDITION_PRICE);
        initMap.put(HTypeConditionColumnType.ARRIVAL_DATE.getValue(), CONDITION_ARRIVAL_DATE);
        initMap.put(HTypeConditionColumnType.SELLABLE_STOCK.getValue(), CONDITION_SELLABLE_STOCK);
        initMap.put(HTypeConditionColumnType.STANDARD_NAME_1.getValue(), CONDITION_STANDARD_1);
        initMap.put(HTypeConditionColumnType.STANDARD_VALUE_1.getValue(), CONDITION_STANDARD_1);
        initMap.put(HTypeConditionColumnType.STANDARD_NAME_2.getValue(), CONDITION_STANDARD_2);
        initMap.put(HTypeConditionColumnType.STANDARD_VALUE_2.getValue(), CONDITION_STANDARD_2);
        return initMap;
    }
}