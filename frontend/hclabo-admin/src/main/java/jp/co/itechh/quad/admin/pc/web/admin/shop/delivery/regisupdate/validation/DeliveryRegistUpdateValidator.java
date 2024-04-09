package jp.co.itechh.quad.admin.pc.web.admin.shop.delivery.regisupdate.validation;

import jp.co.itechh.quad.admin.pc.web.admin.common.validation.group.ConfirmGroup;
import jp.co.itechh.quad.admin.pc.web.admin.shop.delivery.regisupdate.DeliveryAmountCarriageItem;
import jp.co.itechh.quad.admin.pc.web.admin.shop.delivery.regisupdate.DeliveryPrefectureCarriageItem;
import jp.co.itechh.quad.admin.pc.web.admin.shop.delivery.regisupdate.DeliveryRegistUpdateModel;
import jp.co.itechh.quad.admin.validator.HNumberValidator;
import lombok.Data;
import org.apache.commons.lang3.StringUtils;
import org.springframework.stereotype.Component;
import org.springframework.validation.Errors;
import org.springframework.validation.SmartValidator;

import javax.validation.Validation;
import java.math.BigDecimal;
import java.util.Objects;

@Data
@Component
public class DeliveryRegistUpdateValidator implements SmartValidator {

    /** エラーコード：必須 */
    public static final String MSGCD_REQUIRED_INPUT =
                    "jp.co.hankyuhanshin.itec.hmbase.validator.HRequiredValidator.INPUT_detail";

    /** エラーコード：数字 */
    public static final String MSGCD_NOT_NUMBER =
                    "jp.co.hankyuhanshin.itec.hmbase.validator.HNumberValidator.NOT_NUMBER_detail";

    /** エラーコード：最大値 */
    public static final String MSGCD_MAXIMUM =
                    "jp.co.hankyuhanshin.itec.hmbase.validator.HLengthValidator.MAXIMUM_detail";

    /** エラーコード：整数 */
    public static final String MSGCD_FRACTION_MAX_ZERO =
                    "jp.co.hankyuhanshin.itec.hmbase.validator.HNumberLengthValidator.FRACTION_MAX_ZERO_detail";

    /** エラーコード：比較 */
    public static final String MSGCD_GREATER_THAN_CONSTANT =
                    "jp.co.hankyuhanshin.itec.hmbase.validator.HNumberGreaterThanConstantValidator.GREATER_THAN_CONSTANT_detail";

    /** 入力数字最大値 */
    public static final Integer DISCOUNT_MAX_LENGTH = 8;

    /** FRACTION数 */
    public static final Integer DISCOUNT_MAX_FRACTION = 0;

    /** 比較対象値 */
    public static final BigDecimal DISCOUNT_PRICE_GREATER_THAN = new BigDecimal(0);

    /** 高額割引送料 */
    public static final String FILED_NAME_DISCOUNT_CARRIAGE = "largeAmountDiscountCarriage";

    /** 高額割引送料 */
    public static final String MSG_PARAM_DISCOUNT_CARRIAGE = "高額割引送料";

    /** 高額割引下限金額 */
    public static final String FILED_NAME_DISCOUNT_PRICE = "largeAmountDiscountPrice";

    /** 高額割引下限金額 */
    public static final String MSG_PARAM_DISCOUNT_PRICE = "高額割引下限金額";

    /** 都道府県別送料アイテムリスト */
    public static final String FILED_NAME_PREFECTURE_CARRIAGE_ITEM = "deliveryPrefectureCarriageItems[";

    /** 都道府県別送料の送料 */
    public static final String FILED_NAME_PREFECTURE_CARRIAGE = "].prefectureCarriage";

    /** 都道府県別送料の送料 */
    public static final String MSG_PREFECTURE_CARRIAGE = "送料";

    /** 金額別送料アイテムリスト */
    public static final String FILED_NAME_AMOUNT_CARRIAGE_ITEM = "deliveryAmountCarriageItems[";

    /** 金額別送料の上限金額 */
    public static final String FILED_NAME_AMOUNT_CARRIAGE_MAX_PRICE = "].maxPrice";

    /** 金額別送料の送料 */
    public static final String FILED_NAME_AMOUNT_CARRIAGE_AMOUNT_CARRIAGE = "].amountCarriage";

    /** 金額別送料の上限金額 */
    public static final String MSG_AMOUNT_CARRIAGE_MAX_PRICE = "上限金額";

    /** 金額別送料の送料 */
    public static final String MSG_AMOUNT_CARRIAGE_AMOUNT_CARRIAGE = "送料";

    /** 一律送料 */
    public static final String FILED_NAME_EQUALS_CARRIAGE = "equalsCarriage";

    /** 一律送料 */
    public static final String MSG_PARAM_EQUALS_CARRIAGE = "一律送料";

    @Override
    public boolean supports(Class<?> clazz) {
        return DeliveryRegistUpdateModel.class.isAssignableFrom(clazz);
    }

    @Override
    public void validate(Object target, Errors errors, Object... validationHints) {
        if (Objects.equals(validationHints, null)) {
            // 対象groupがない場合、チェックしない
            return;
        }

        if (!ConfirmGroup.class.equals(validationHints[0])) {
            // バリデータ対象のgroupが、ConfirmGroup以外の場合、チェックしない
            return;
        }

        DeliveryRegistUpdateModel model = (DeliveryRegistUpdateModel) target;

        // 高額割引下限金額は高額割引送料に入力がある場合必須
        if (StringUtils.isEmpty(model.getLargeAmountDiscountPrice()) && !StringUtils.isEmpty(
                        model.getLargeAmountDiscountCarriage())) {
            // 高額割引下限金額必須チェック
            errors.rejectValue(FILED_NAME_DISCOUNT_PRICE, MSGCD_REQUIRED_INPUT,
                               new String[] {FILED_NAME_DISCOUNT_PRICE}, null
                              );

            // 高額割引送料チェック
            validate(model.getLargeAmountDiscountCarriage(), errors, FILED_NAME_DISCOUNT_CARRIAGE,
                     MSG_PARAM_DISCOUNT_CARRIAGE
                    );
        }
        // 高額割引送料は高額割引下限金額に入力がある場合必須
        else if (!StringUtils.isEmpty(model.getLargeAmountDiscountPrice()) && StringUtils.isEmpty(
                        model.getLargeAmountDiscountCarriage())) {
            // 高額割引送料必須チェック
            errors.rejectValue(FILED_NAME_DISCOUNT_CARRIAGE, MSGCD_REQUIRED_INPUT,
                               new String[] {MSG_PARAM_DISCOUNT_CARRIAGE}, null
                              );

            // 高額割引下限金額チェック
            validate(model.getLargeAmountDiscountPrice(), errors, FILED_NAME_DISCOUNT_PRICE, MSG_PARAM_DISCOUNT_PRICE);
        }
        // 高額割引送料および高額割引下限金額に両方とも入力がある場合
        else if (!StringUtils.isEmpty(model.getLargeAmountDiscountPrice()) && !StringUtils.isEmpty(
                        model.getLargeAmountDiscountCarriage())) {
            validate(model.getLargeAmountDiscountCarriage(), errors, FILED_NAME_DISCOUNT_CARRIAGE,
                     MSG_PARAM_DISCOUNT_CARRIAGE
                    );
            validate(model.getLargeAmountDiscountPrice(), errors, FILED_NAME_DISCOUNT_PRICE, MSG_PARAM_DISCOUNT_PRICE);
        }

        // 都道府県別送料は有効フラグがONの場合必須
        if (model.isPrefectureType()) {
            for (int i = 0; i < model.getDeliveryPrefectureCarriageItems().size(); i++) {
                DeliveryPrefectureCarriageItem item = model.getDeliveryPrefectureCarriageItems().get(i);
                if (item.isActiveFlag()) {
                    String fieldName = FILED_NAME_PREFECTURE_CARRIAGE_ITEM + i + FILED_NAME_PREFECTURE_CARRIAGE;

                    if (StringUtils.isEmpty(item.getPrefectureCarriage())) {
                        errors.rejectValue(fieldName, "javax.validation.constraints.NotEmpty.message");
                    } else {
                        validate(item.getPrefectureCarriage(), errors, fieldName, MSG_PREFECTURE_CARRIAGE);
                    }
                }
            }
        }

        // 金額別送料の場合必須
        if (model.isAmountType()) {
            for (int i = 0; i < model.getDeliveryAmountCarriageItems().size(); i++) {
                DeliveryAmountCarriageItem item = model.getDeliveryAmountCarriageItems().get(i);

                String fieldMaxPrice = FILED_NAME_AMOUNT_CARRIAGE_ITEM + i + FILED_NAME_AMOUNT_CARRIAGE_MAX_PRICE;
                String fieldAmountCarriage =
                                FILED_NAME_AMOUNT_CARRIAGE_ITEM + i + FILED_NAME_AMOUNT_CARRIAGE_AMOUNT_CARRIAGE;

                if (StringUtils.isEmpty(item.getMaxPrice()) && !StringUtils.isEmpty(item.getAmountCarriage())) {
                    // 上限金額必須チェック
                    errors.rejectValue(fieldMaxPrice, MSGCD_REQUIRED_INPUT,
                                       new String[] {MSG_AMOUNT_CARRIAGE_MAX_PRICE}, null
                                      );

                    // 送料チェック
                    validate(item.getAmountCarriage(), errors, fieldAmountCarriage,
                             MSG_AMOUNT_CARRIAGE_AMOUNT_CARRIAGE
                            );
                } else if (!StringUtils.isEmpty(item.getMaxPrice()) && StringUtils.isEmpty(item.getAmountCarriage())) {
                    // 送料必須チェック
                    errors.rejectValue(fieldAmountCarriage, MSGCD_REQUIRED_INPUT,
                                       new String[] {MSG_AMOUNT_CARRIAGE_AMOUNT_CARRIAGE}, null
                                      );

                    // 上限金額チェック
                    validate(item.getMaxPrice(), errors, fieldMaxPrice, MSG_AMOUNT_CARRIAGE_MAX_PRICE);
                } else if (!StringUtils.isEmpty(item.getMaxPrice()) && !StringUtils.isEmpty(item.getAmountCarriage())) {
                    // 上限金額チェック
                    validate(item.getAmountCarriage(), errors, fieldAmountCarriage,
                             MSG_AMOUNT_CARRIAGE_AMOUNT_CARRIAGE
                            );

                    // 送料チェック
                    validate(item.getMaxPrice(), errors, fieldMaxPrice, MSG_AMOUNT_CARRIAGE_MAX_PRICE);
                }
            }
        }

        // 金額別送料の場合必須
        if (model.isFlatType()) {
            if (StringUtils.isEmpty(model.getEqualsCarriage())) {
                errors.rejectValue(FILED_NAME_EQUALS_CARRIAGE, MSGCD_REQUIRED_INPUT,
                                   new String[] {MSG_PARAM_EQUALS_CARRIAGE}, null
                                  );
            }
        }

    }

    /**
     *
     * @param value
     * @param errors
     * @param fieldName
     * @param msgParam
     * @return Errors
     */
    private void validate(String value, Errors errors, String fieldName, String msgParam) {
        // 数値チェックバリデータ
        HNumberValidator validator = Validation.buildDefaultValidatorFactory()
                                               .getConstraintValidatorFactory()
                                               .getInstance(HNumberValidator.class);

        validator.setMinus(false);
        boolean result = validator.isValid(value, null);
        if (!result) {
            errors.rejectValue(fieldName, MSGCD_NOT_NUMBER, new String[] {msgParam}, null);
            return;
        }

        // 文字数チェック
        if (value.length() > DISCOUNT_MAX_LENGTH) {
            errors.rejectValue(fieldName, MSGCD_MAXIMUM, new String[] {String.valueOf(DISCOUNT_MAX_LENGTH), msgParam},
                               null
                              );
            return;
        }

        // 整数チェック
        if (value.indexOf('.') != -1) {
            String fraction = value.substring(value.indexOf('.') + 1);
            if (fraction.length() > DISCOUNT_MAX_FRACTION) {
                errors.rejectValue(fieldName, MSGCD_FRACTION_MAX_ZERO, new String[] {msgParam}, null);
                return;
            }
        }

        // 数値比較（対象項目 > 設定値）
        if (fieldName.equals(FILED_NAME_DISCOUNT_PRICE)) {
            if (new BigDecimal(value).compareTo(DISCOUNT_PRICE_GREATER_THAN) <= 0) {
                errors.rejectValue(fieldName, MSGCD_GREATER_THAN_CONSTANT, new String[] {msgParam}, null);
            }
        }
    }

    /** 未使用 */
    @Override
    public void validate(Object target, Errors errors) {
        // 未使用
    }
}