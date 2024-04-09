package jp.co.itechh.quad.front.pc.web.front.order.validation;

import jp.co.itechh.quad.front.pc.web.front.order.ConfirmModel;
import jp.co.itechh.quad.front.pc.web.front.order.common.OrderCommonModel;
import jp.co.itechh.quad.front.validator.HCreditCardExpirationDateValidator;
import jp.co.itechh.quad.front.validator.HSecurityCodeValidator;
import lombok.Data;
import org.apache.commons.lang3.StringUtils;
import org.springframework.stereotype.Component;
import org.springframework.validation.Errors;
import org.springframework.validation.SmartValidator;

import static jp.co.itechh.quad.front.constant.type.HTypeSettlementMethodType.CREDIT;

@Data
@Component
public class ConfirmValidator implements SmartValidator {

    /** セキュリティコード フィールド名 **/
    protected static final String FILED_NAME_SECURITY_CODE = "securityCode";

    /** セキュリティコード フィールド名 **/
    protected static final String FIELD_NAME_EXPIRED_DATE = "expirationDateYear";

    /** メッセージID:入力必須チェック **/
    public static final String MSGCD_REQUIRED_INPUT = "ORDER-PAYMENT-001";

    /** メッセージID:有効期限の期限切れチェック */
    public static final String MSGCD_EXPIRED_CARD = "ORDER-PAYMENT-006";

    /** メッセージID:セキュリティコード桁数誤りチェック */
    public static final String MSGCD_SECURITY_CODE_LENGTH = "ORDER-PAYMENT-008";

    /** メッセージID:セキュリティコード数値以外チェック */
    public static final String MSGCD_SECURITY_CODE_NOT_NUMBER = "ORDER-PAYMENT-009";

    /** セキュリティコードの桁数誤りチェック用エラーパターン */
    public static final String VALID_PATTERN_SECURITY_CODE_LENGTH = "{HSecurityCodeValidator.LENGTH_detail}";

    /** セキュリティコードが数値以外チェック用エラーパターン */
    public static final String VALID_PATTERN_SECURITY_CODE_NOT_NUMBER = "{HSecurityCodeValidator.NOT_NUMBER_detail}";

    /** 有効期限の期限切れチェック用エラーパターン */
    public static final String VALID_PATTERN_EXPIRED_CARD = "{HCreditCardExpirationDateValidator.EXPIRED_CARD_detail}";

    /**
     * 引数で渡ったクラスが、バリデーション対象か否か
     *
     * @param clazz
     * @return boolean
     */
    @Override
    public boolean supports(Class<?> clazz) {
        return ConfirmModel.class.isAssignableFrom(clazz) || OrderCommonModel.class.isAssignableFrom(clazz);
    }

    /** validationHintsは不使用（Controllerに合わせる） */
    @Override
    public void validate(Object target, Errors errors, Object... validationHints) {
    }

    /**
     * クレジットカード用の動的バリデータ
     *
     * @param target the object that is to be validated
     * @param errors contextual state about the validation process
     */
    @Override
    public void validate(Object target, Errors errors) {

        if (errors.hasErrors()) {
            return;
        }

        // 注文内容確認 Model
        ConfirmModel confirmModel = ConfirmModel.class.cast(target);

        // クレジットカードのチェック
        if (confirmModel.getSettlementMethodEntity() != null
            && confirmModel.getSettlementMethodEntity().getSettlementMethodType() == CREDIT) {
            errors = checkCredit(confirmModel, errors);
        }
    }

    /**
     * クレジットカードのチェック<br/>
     *
     * @param confirmModel
     * @param errors
     * @return errors
     */
    private Errors checkCredit(ConfirmModel confirmModel, Errors errors) {

        String expirationDateYear = confirmModel.getExpirationDateYear();
        String expirationDateMonth = confirmModel.getExpirationDateMonth();

        // セキュリティコードチェック開始（トークンが設定されている場合はチェックしない）
        if (!confirmModel.isToken()) {

            // 必須
            if (StringUtils.isEmpty(confirmModel.getSecurityCode())) {
                errors.rejectValue(FILED_NAME_SECURITY_CODE, MSGCD_REQUIRED_INPUT);
            }

            // セキュリティコード妥当性チェック
            HSecurityCodeValidator securityCodeValidator = new HSecurityCodeValidator();
            if (!securityCodeValidator.isValid(confirmModel.getSecurityCode())) {
                if (VALID_PATTERN_SECURITY_CODE_LENGTH.equals(securityCodeValidator.getMessageId())) {
                    // セキュリティコードの桁数誤りの場合
                    errors.rejectValue(FILED_NAME_SECURITY_CODE, MSGCD_SECURITY_CODE_LENGTH);
                }
                if (VALID_PATTERN_SECURITY_CODE_NOT_NUMBER.equals(securityCodeValidator.getMessageId())) {
                    // セキュリティコードが数値以外の場合
                    errors.rejectValue(FILED_NAME_SECURITY_CODE, MSGCD_SECURITY_CODE_NOT_NUMBER);
                }
            }

            // 有効期限妥当性チェック
            HCreditCardExpirationDateValidator creditCardExpirationDateValidator =
                            new HCreditCardExpirationDateValidator();
            if (!creditCardExpirationDateValidator.isValid(expirationDateMonth, expirationDateYear)) {
                if (VALID_PATTERN_EXPIRED_CARD.equals(creditCardExpirationDateValidator.getMessageId())) {
                    // 有効期限が切れている場合
                    errors.rejectValue(FIELD_NAME_EXPIRED_DATE, MSGCD_EXPIRED_CARD);
                }
            }
        }

        return errors;
    }
}