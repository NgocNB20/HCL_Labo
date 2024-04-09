package jp.co.itechh.quad.admin.pc.web.admin.goods.category.validation;

import jp.co.itechh.quad.admin.base.constant.ValidatorConstants;
import jp.co.itechh.quad.admin.base.util.common.PropertiesUtil;
import jp.co.itechh.quad.admin.constant.type.HTypeCategoryType;
import jp.co.itechh.quad.admin.constant.type.HTypeConditionColumnType;
import jp.co.itechh.quad.admin.constant.type.HTypeConditionOperatorType;
import jp.co.itechh.quad.admin.pc.web.admin.goods.category.CategoryConditionItem;
import jp.co.itechh.quad.admin.pc.web.admin.goods.category.CategoryInputModel;
import jp.co.itechh.quad.admin.pc.web.admin.goods.category.validation.group.NextGroup;
import jp.co.itechh.quad.admin.validator.HDateValidator;
import jp.co.itechh.quad.admin.validator.HNumberValidator;
import jp.co.itechh.quad.admin.validator.HSpecialCharacterValidator;
import lombok.Data;
import org.apache.commons.lang3.StringUtils;
import org.springframework.stereotype.Component;
import org.springframework.validation.Errors;
import org.springframework.validation.FieldError;
import org.springframework.validation.SmartValidator;

import javax.validation.Validation;
import java.util.Arrays;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Objects;
import java.util.regex.Pattern;

/**
 * カテゴリ入力バリデータ
 *
 * @author Doan Thang (VJP)
 */
@Data
@Component
public class CategoryInputValidator implements SmartValidator {
    /** 最小入力文字数 */
    private static final int MIN_LENGTH = 1;

    /** 最大入力文字数 */
    private static final int MAX_LENGTH = 100;

    /** エラーコード：入力必須 */
    public static final String MSGCD_REQUIRED = "javax.validation.constraints.NotNull.message";
    /** エラーコード：選択必須 */
    public static final String MSGCD_SELECT_REQUIRED = "HRequiredValidator.REQUIRED_detail";
    /** 数値じゃなかった場合 */
    public static final String NOT_NUMBER_MESSAGE_ID = "HNumberValidator.NOT_NUMBER_detail";
    /** 日付じゃなかった場合 */
    public static final String NOT_DATE_MESSAGE_ID = "HDateValidator.NOT_DATE_detail";
    /** 特殊文字エラー */
    public static final String MSGCD_SPEC_CHARACTER = "HSpecialCharacterValidator.INVALID_detail";
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

    /** 項目名 */
    public static final String FILED_NAME_CONDITION_TYPE = "categoryConditionType";
    public static final String FILED_NAME_CONDITION_ITEMS_LIST = "categoryConditionItems";
    public static final String FILED_NAME_CONDITION_ITEMS = "categoryConditionItems[";
    public static final String FILED_NAME_CONDITION_ITEMS_COLUMN = "].conditionColumn";
    public static final String FILED_NAME_CONDITION_ITEMS_OPERATOR = "].conditionOperator";
    public static final String FILED_NAME_CONDITION_ITEMS_VALUE = "].conditionValue";

    /** 数値チェックバリデータ */
    private HNumberValidator hNumberValidator;
    /** 日付チェックバリデータ */
    private HDateValidator hDateValidator;
    /** 特殊文字チェックバリデータ */
    private HSpecialCharacterValidator hSpecialCharacterValidator;

    @Override
    public boolean supports(Class<?> clazz) {
        return CategoryInputModel.class.isAssignableFrom(clazz);
    }

    @Override
    public void validate(Object target, Errors errors, Object... validationHints) {
        if (Objects.equals(validationHints, null)) {
            // 対象groupがない場合、チェックしない
            return;
        }

        if (!NextGroup.class.equals(validationHints[0])) {
            // バリデータ対象のgroupが、NextGroup以外の場合、チェックしない
            return;
        }

        // チェック対象取得
        CategoryInputModel model = (CategoryInputModel) target;

        // カテゴリー種別＝自動の場合
        if (HTypeCategoryType.AUTO.getValue().equals(model.getCategoryType())) {
            // 商品の条件合致の条件付き必須チェック
            if (StringUtils.isEmpty(model.getCategoryConditionType())) {
                errors.rejectValue(FILED_NAME_CONDITION_TYPE, MSGCD_SELECT_REQUIRED);
                return;
            }

            // 条件項目ー条件式ー条件値の最小件数チェック
            if (model.getCategoryConditionItems() == null || model.getCategoryConditionItems().size() == 0) {
                errors.rejectValue(FILED_NAME_CONDITION_ITEMS_LIST, MSGCD_MIN);
                return;
            }

            if (model.getCategoryConditionItems() != null) {
                // 条件項目ー条件式ー条件値の最大件数チェック
                int maxSize = Integer.parseInt(
                                PropertiesUtil.getSystemPropertiesValue("auto.category.condition.items.length"));
                if (model.getCategoryConditionItems().size() > maxSize) {
                    errors.rejectValue(FILED_NAME_CONDITION_ITEMS_LIST, MSGCD_MAX);
                    return;
                }

                // 組合せ必須チェック
                checkCombinationRequired(model.getCategoryConditionItems(), errors);
                for (FieldError fieldError : errors.getFieldErrors()) {
                    if (fieldError.getField().contains("categoryConditionItems")) {
                        return;
                    }
                }

                // 整合性チェック
                hNumberValidator = Validation.buildDefaultValidatorFactory()
                                             .getConstraintValidatorFactory()
                                             .getInstance(HNumberValidator.class);
                hDateValidator = Validation.buildDefaultValidatorFactory()
                                           .getConstraintValidatorFactory()
                                           .getInstance(HDateValidator.class);
                hSpecialCharacterValidator = Validation.buildDefaultValidatorFactory()
                                                       .getConstraintValidatorFactory()
                                                       .getInstance(HSpecialCharacterValidator.class);
                checkConsistency(model.getCategoryConditionItems(), errors);
            }
        }
    }

    /** 未使用 */
    @Override
    public void validate(Object target, Errors errors) {
        // 未使用
    }

    /**
     * 組合せ必須チェック
     *
     * @param categoryConditionItemList カテゴリー条件項目リスト
     * @param errors エラー
     */
    private void checkCombinationRequired(List<CategoryConditionItem> categoryConditionItemList, Errors errors) {
        for (int i = 0; i < categoryConditionItemList.size(); i++) {
            // カテゴリー条件項目取得
            CategoryConditionItem item = categoryConditionItemList.get(i);
            String column = item.getConditionColumn();
            String operator = item.getConditionOperator();
            String value = item.getConditionValue();
            if (column == null && operator == null && value == null) {
                errors.rejectValue(
                                FILED_NAME_CONDITION_ITEMS + i + FILED_NAME_CONDITION_ITEMS_COLUMN,
                                MSGCD_SELECT_REQUIRED
                                  );
                errors.rejectValue(
                                FILED_NAME_CONDITION_ITEMS + i + FILED_NAME_CONDITION_ITEMS_OPERATOR,
                                MSGCD_SELECT_REQUIRED
                                  );
                errors.rejectValue(FILED_NAME_CONDITION_ITEMS + i + FILED_NAME_CONDITION_ITEMS_VALUE, MSGCD_REQUIRED);
            } else if (column != null && operator != null && value == null
                       && !HTypeConditionOperatorType.SETTING.getValue().equals(operator)
                       && !HTypeConditionOperatorType.NO_SETTING.getValue().equals(operator)) {
                errors.rejectValue(FILED_NAME_CONDITION_ITEMS + i + FILED_NAME_CONDITION_ITEMS_VALUE, MSGCD_REQUIRED);
            } else if (column != null && operator == null && value == null) {
                errors.rejectValue(
                                FILED_NAME_CONDITION_ITEMS + i + FILED_NAME_CONDITION_ITEMS_OPERATOR,
                                MSGCD_SELECT_REQUIRED
                                  );
                errors.rejectValue(FILED_NAME_CONDITION_ITEMS + i + FILED_NAME_CONDITION_ITEMS_VALUE, MSGCD_REQUIRED);
            } else if (column != null && operator == null) {
                errors.rejectValue(
                                FILED_NAME_CONDITION_ITEMS + i + FILED_NAME_CONDITION_ITEMS_OPERATOR,
                                MSGCD_SELECT_REQUIRED
                                  );
            } else if (column == null && operator != null && value != null) {
                errors.rejectValue(
                                FILED_NAME_CONDITION_ITEMS + i + FILED_NAME_CONDITION_ITEMS_COLUMN,
                                MSGCD_SELECT_REQUIRED
                                  );
            } else if (column == null && operator != null) {
                errors.rejectValue(
                                FILED_NAME_CONDITION_ITEMS + i + FILED_NAME_CONDITION_ITEMS_COLUMN,
                                MSGCD_SELECT_REQUIRED
                                  );
                errors.rejectValue(FILED_NAME_CONDITION_ITEMS + i + FILED_NAME_CONDITION_ITEMS_VALUE, MSGCD_REQUIRED);
            } else if (column == null) {
                errors.rejectValue(
                                FILED_NAME_CONDITION_ITEMS + i + FILED_NAME_CONDITION_ITEMS_COLUMN,
                                MSGCD_SELECT_REQUIRED
                                  );
                errors.rejectValue(
                                FILED_NAME_CONDITION_ITEMS + i + FILED_NAME_CONDITION_ITEMS_OPERATOR,
                                MSGCD_SELECT_REQUIRED
                                  );
            }
        }
    }

    /**
     * 整合性チェック
     *
     * @param categoryConditionItemList カテゴリー条件項目リスト
     * @param errors エラー
     */
    private void checkConsistency(List<CategoryConditionItem> categoryConditionItemList, Errors errors) {
        for (int i = 0; i < categoryConditionItemList.size(); i++) {
            CategoryConditionItem item = categoryConditionItemList.get(i);
            String column = item.getConditionColumn();
            String operator = item.getConditionOperator();
            String value = item.getConditionValue();
            if (value != null && (value.length() == 0)) {
                value = null;
            }

            // 整合性チェック
            if (!CONSISTENCY_CHECK_MAP.get(column).contains(operator)) {
                errors.rejectValue(FILED_NAME_CONDITION_ITEMS + i + FILED_NAME_CONDITION_ITEMS_OPERATOR,
                                   MSGCD_NOT_MATCHING
                                  );
            }
            // 桁数チェック
            if (value != null && (MIN_LENGTH > value.length() || value.length() > MAX_LENGTH)) {
                errors.rejectValue(FILED_NAME_CONDITION_ITEMS + i + FILED_NAME_CONDITION_ITEMS_VALUE, LENGTH_MSG_ID,
                                   new String[] {String.valueOf(MIN_LENGTH), String.valueOf(MAX_LENGTH)}, null
                                  );
            }
            // 新着日付チェック
            if (column.equals(HTypeConditionColumnType.ARRIVAL_DATE.getValue())) {
                if (!hDateValidator.isValid(value, null)) {
                    errors.rejectValue(
                                    FILED_NAME_CONDITION_ITEMS + i + FILED_NAME_CONDITION_ITEMS_VALUE,
                                    NOT_DATE_MESSAGE_ID
                                      );
                }
            }
            // 価格（税抜）・販売可能在庫数チェック
            else if (column.equals(HTypeConditionColumnType.PRICE.getValue()) || column.equals(
                            HTypeConditionColumnType.SELLABLE_STOCK.getValue())) {
                if (!hNumberValidator.isValid(value, null)) {
                    errors.rejectValue(
                                    FILED_NAME_CONDITION_ITEMS + i + FILED_NAME_CONDITION_ITEMS_VALUE,
                                    NOT_NUMBER_MESSAGE_ID
                                      );
                }
            }
            // 商品管理番号チェック
            else if (column.equals(HTypeConditionColumnType.GOODS_GROUP_SEQ.getValue())) {
                String regex = ValidatorConstants.REGEX_GOODS_GROUP_CODE;
                Pattern pattern = Pattern.compile(regex);
                if (!pattern.matcher(value).matches()) {
                    errors.rejectValue(
                                    FILED_NAME_CONDITION_ITEMS + i + FILED_NAME_CONDITION_ITEMS_VALUE,
                                    MSGCD_REGEX_GOODS_GROUP_CODE
                                      );
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
                hSpecialCharacterValidator.setAllowPunctuation(true);
                if (!hSpecialCharacterValidator.isValid(value, null)) {
                    errors.rejectValue(
                                    FILED_NAME_CONDITION_ITEMS + i + FILED_NAME_CONDITION_ITEMS_VALUE,
                                    MSGCD_SPEC_CHARACTER
                                      );
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
    private static Map<String, List<String>> initConsistencyCheckMap() {
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