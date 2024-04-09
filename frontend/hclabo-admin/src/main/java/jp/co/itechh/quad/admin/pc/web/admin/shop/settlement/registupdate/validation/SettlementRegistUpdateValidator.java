package jp.co.itechh.quad.admin.pc.web.admin.shop.settlement.registupdate.validation;

import jp.co.itechh.quad.admin.base.util.seasar.BigDecimalConversionUtil;
import jp.co.itechh.quad.admin.base.utility.ApplicationContextUtility;
import jp.co.itechh.quad.admin.base.utility.NumberUtility;
import jp.co.itechh.quad.admin.constant.type.HTypeSettlementMethodCommissionType;
import jp.co.itechh.quad.admin.constant.type.HTypeSettlementMethodType;
import jp.co.itechh.quad.admin.pc.web.admin.common.validation.group.ConfirmGroup;
import jp.co.itechh.quad.admin.pc.web.admin.shop.settlement.registupdate.SettlementRegistUpdateModel;
import jp.co.itechh.quad.admin.pc.web.admin.shop.settlement.registupdate.SettlementRegistUpdateModelItem;
import lombok.Data;
import org.apache.commons.lang3.StringUtils;
import org.springframework.stereotype.Component;
import org.springframework.validation.Errors;
import org.springframework.validation.SmartValidator;

import java.math.BigDecimal;
import java.util.Objects;

@Data
@Component
public class SettlementRegistUpdateValidator implements SmartValidator {

    /** 一律手数料（円） */
    public static final String FILED_NAME_EQUALS_COMMISSION_YEN = "equalsCommissionYen";

    /** 高額割引下限金額 */
    public static final String FILED_NAME_LARGE_AMOUNT_DISCOUNT_PRICE_YEN = "largeAmountDiscountPriceYen";

    /** 高額割引手数料 */
    public static final String FILED_NAME_LARGE_AMOUNT_DISCOUNT_COMMISSION_YEN = "largeAmountDiscountCommissionYen";

    /** 決済方法種別 */
    public static final String FILED_NAME_SETTLEMENT_TYPE = "settlementType";

    /** 最大購入金額 */
    public static final String FILED_MAX_PURCHASE_PRICE_YEN = "maxPurchasedPriceYen";

    /** 決済方法金額別手数料リスト（円） */
    public static final String FILED_NAME_PRICE_COMMISSION_YEN = "priceCommissionYen[";

    /** 決済方法金額別手数料リスト（円）の上限金額 */
    public static final String FILED_NAME_PRICE_COMMISSION_YEN_MAX_PRICE = "].maxPrice";

    /** 決済方法金額別手数料リスト（円）の手数料 */
    public static final String FILED_NAME_PRICE_COMMISSION_YEN_COMMISSION = "].commission";

    /** エラーコード：必須 */
    public static final String MSGCD_REQUIRED_INPUT =
                    "jp.co.hankyuhanshin.itec.hmbase.validator.HRequiredValidator.INPUT_detail";

    /** エラーコード：よりも大きい値 */
    public static final String MSGCD_GREATER_THAN_CONSTANT =
                    "jp.co.hankyuhanshin.itec.hmbase.validator.HNumberGreaterThanConstantValidator.GREATER_THAN_CONSTANT_detail";

    /** エラーコード：必須 */
    public static final String MSGCD_REQUIRED_INPUT_NO_PARAM = "javax.validation.constraints.NotEmpty.message";

    /** 対象数値が比較数値以下の場合 */
    public static final String NOT_GREATER_MESSAGE_ID = "HNumberGreaterValidator.GREATER_detail";

    /** 対象数値が比較数値より大きい場合 */
    public static final String NOT_LESS_EQUAL_MESSAGE_ID = "HNumberLessEqualValidator.LESS_EQUAL_detail";

    @Override
    public boolean supports(Class<?> clazz) {
        return SettlementRegistUpdateModel.class.isAssignableFrom(clazz);
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

        SettlementRegistUpdateModel model = (SettlementRegistUpdateModel) target;

        // 数値関連Helper取得
        NumberUtility numberUtility = ApplicationContextUtility.getBean(NumberUtility.class);

        if (!StringUtils.isEmpty(model.getSettlementMethodCommissionType())) {
            // 一律（円）の場合、最大購入金額（円）と一律手数料（円）の必須チェック
            if (model.getSettlementMethodCommissionType()
                     .equals(HTypeSettlementMethodCommissionType.FLAT_YEN.getValue())) {

                if (StringUtils.isEmpty(model.getEqualsCommissionYen())) {
                    errors.rejectValue(FILED_NAME_EQUALS_COMMISSION_YEN, MSGCD_REQUIRED_INPUT_NO_PARAM);
                }
            }

            // 一律（円）の場合、最大購入金額（円）と一律手数料（円）の必須チェック
            if (model.getSettlementMethodCommissionType()
                     .equals(HTypeSettlementMethodCommissionType.EACH_AMOUNT_YEN.getValue())) {
                for (int i = 0; i < model.getPriceCommissionYen().size(); i++) {
                    SettlementRegistUpdateModelItem item = model.getPriceCommissionYen().get(i);
                    if (!StringUtils.isEmpty(item.getMaxPrice()) && StringUtils.isEmpty(item.getCommission())) {
                        errors.rejectValue(FILED_NAME_PRICE_COMMISSION_YEN + i
                                           + FILED_NAME_PRICE_COMMISSION_YEN_COMMISSION, MSGCD_REQUIRED_INPUT_NO_PARAM);
                    }

                    if (StringUtils.isEmpty(item.getMaxPrice()) && !StringUtils.isEmpty(item.getCommission())) {
                        errors.rejectValue(
                                        FILED_NAME_PRICE_COMMISSION_YEN + i + FILED_NAME_PRICE_COMMISSION_YEN_MAX_PRICE,
                                        MSGCD_REQUIRED_INPUT_NO_PARAM
                                          );
                    }
                }
            }
        }

        // 高額割引手数料入力する場合、高額割引下限金額の必須チェック
        if (!StringUtils.isEmpty(model.getLargeAmountDiscountCommissionYen())) {
            if (StringUtils.isEmpty(model.getLargeAmountDiscountPriceYen())) {
                errors.rejectValue(FILED_NAME_LARGE_AMOUNT_DISCOUNT_PRICE_YEN, MSGCD_REQUIRED_INPUT_NO_PARAM);
            }
        }

        // 高額割引下限金額入力する場合、高額割引手数料の必須チェック
        if (!StringUtils.isEmpty(model.getLargeAmountDiscountPriceYen())) {
            if (StringUtils.isEmpty(model.getLargeAmountDiscountCommissionYen())) {
                errors.rejectValue(FILED_NAME_LARGE_AMOUNT_DISCOUNT_COMMISSION_YEN, MSGCD_REQUIRED_INPUT_NO_PARAM);
            }
        }

        if (!StringUtils.isEmpty(model.getSettlementMethodCommissionType()) && model.getSettlementMethodCommissionType()
                                                                                    .equals(HTypeSettlementMethodCommissionType.FLAT_YEN.getValue())
            && !StringUtils.isEmpty(model.getSettlementMethodType())
            && !HTypeSettlementMethodType.LINK_PAYMENT.getValue().equals(model.getSettlementMethodType())) {
            if (numberUtility.isNumber(model.getMaxPurchasedPriceYen()) && numberUtility.isNumber(
                            model.getEqualsCommissionYen())) {

                if (!isGreaterThan(model.getMaxPurchasedPriceYen(), model.getEqualsCommissionYen())) {
                    errors.rejectValue(FILED_MAX_PURCHASE_PRICE_YEN, NOT_GREATER_MESSAGE_ID);
                }
            }

            if (numberUtility.isNumber(model.getLargeAmountDiscountCommissionYen()) && numberUtility.isNumber(
                            model.getMaxPurchasedPriceYen())) {

                if (!isLessThan(model.getLargeAmountDiscountCommissionYen(), model.getMaxPurchasedPriceYen())) {
                    errors.rejectValue(FILED_NAME_LARGE_AMOUNT_DISCOUNT_COMMISSION_YEN, NOT_LESS_EQUAL_MESSAGE_ID);
                }
            }

            if (numberUtility.isNumber(model.getLargeAmountDiscountPriceYen()) && numberUtility.isNumber(
                            model.getMaxPurchasedPriceYen())) {

                if (!isLessThan(model.getLargeAmountDiscountPriceYen(), model.getMaxPurchasedPriceYen())) {
                    errors.rejectValue(FILED_NAME_LARGE_AMOUNT_DISCOUNT_PRICE_YEN, NOT_LESS_EQUAL_MESSAGE_ID);
                }
            }
        }

    }

    private boolean compareToTarget(BigDecimal inputValue, BigDecimal targetValue) {
        return inputValue.compareTo(targetValue) > 0;
    }

    /**
     * targetValue が comparisonValue より大きいか。
     *
     * @param targetValue     対象の値
     * @param comparisonValue 比較対象の値
     * @return true..OK / false..NG
     */
    private boolean isGreaterThan(Object targetValue, Object comparisonValue) {

        BigDecimal bigDecimal = BigDecimalConversionUtil.toBigDecimal(targetValue);
        BigDecimal comparisonBigDecimal = BigDecimalConversionUtil.toBigDecimal(comparisonValue);

        return bigDecimal.compareTo(comparisonBigDecimal) > 0;
    }

    /**
     * targetValue が comparisonValue 以下か。
     *
     * @param targetValue     対象の値
     * @param comparisonValue 比較対象の値
     * @return true..OK / false..NG
     */
    private boolean isLessThan(Object targetValue, Object comparisonValue) {

        BigDecimal bigDecimal = BigDecimalConversionUtil.toBigDecimal(targetValue);
        BigDecimal comparisonBigDecimal = BigDecimalConversionUtil.toBigDecimal(comparisonValue);

        return bigDecimal.compareTo(comparisonBigDecimal) <= 0;
    }

    /** 未使用 */
    @Override
    public void validate(Object target, Errors errors) {
        // 未使用
    }
}