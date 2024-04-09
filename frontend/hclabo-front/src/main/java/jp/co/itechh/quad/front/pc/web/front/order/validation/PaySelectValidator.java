package jp.co.itechh.quad.front.pc.web.front.order.validation;

import jp.co.itechh.quad.front.constant.type.HTypeEffectiveFlag;
import jp.co.itechh.quad.front.dto.shop.settlement.SettlementDetailsDto;
import jp.co.itechh.quad.front.dto.shop.settlement.SettlementDto;
import jp.co.itechh.quad.front.pc.web.front.order.PaySelectModel;
import jp.co.itechh.quad.front.pc.web.front.order.PaySelectModelItem;
import jp.co.itechh.quad.front.pc.web.front.order.common.OrderCommonModel;
import jp.co.itechh.quad.front.pc.web.front.order.common.SalesSlipModel;
import jp.co.itechh.quad.front.pc.web.front.order.validation.group.PaySelectGroup;
import jp.co.itechh.quad.front.validator.HCreditCardExpirationDateValidator;
import jp.co.itechh.quad.front.validator.HCreditCardNumberValidator;
import jp.co.itechh.quad.front.validator.HSecurityCodeValidator;
import lombok.Data;
import org.apache.commons.lang3.StringUtils;
import org.springframework.stereotype.Component;
import org.springframework.validation.Errors;
import org.springframework.validation.SmartValidator;

import java.util.List;
import java.util.Map;
import java.util.Objects;

import static jp.co.itechh.quad.front.constant.type.HTypePaymentType.INSTALLMENT;
import static jp.co.itechh.quad.front.constant.type.HTypePaymentType.REVOLVING;
import static jp.co.itechh.quad.front.constant.type.HTypeSettlementMethodType.CREDIT;

@Data
@Component
public class PaySelectValidator implements SmartValidator {

    /** paymentModelItems フィールド名 **/
    protected static final String FILED_NAME_ITEMS = "paySelectModelItems[";

    /** カード番号 フィールド名 **/
    protected static final String FILED_NAME_CARD_NUMBER = "].cardNumber";

    /** 有効期限：月 フィールド名 **/
    protected static final String FILED_NAME_EXPIRATION_DATE_MONTH = "].expirationDateMonth";

    /** 有効期限：年 フィールド名 **/
    protected static final String FILED_NAME_EXPIRATION_DATE_YEAR = "].expirationDateYear";

    /** セキュリティコード フィールド名 **/
    protected static final String FILED_NAME_SECURITY_CODE = "].securityCode";

    /** 決済種別 フィールド名 **/
    protected static final String FILED_NAME_PAYMENT_TYPE = "].paymentType";

    /** 分割回数 フィールド名 **/
    protected static final String FILED_NAME_DIVIDED_NUMBER = "].dividedNumber";

    /** 決済方法選択値 フィールド名 **/
    protected static final String FILED_NAME_SETTLEMENT_METHOD = "settlementMethod";

    /** メッセージID:トークン不備エラー  */
    public static final String MSGCD_TOKEN = "TOKEN-001-001-A-E";

    /** メッセージID:入力必須チェック **/
    public static final String MSGCD_REQUIRED_INPUT = "ORDER-PAYMENT-001";

    /** メッセージID:選択必須チェック **/
    public static final String MSGCD_REQUIRED_SELECT = "ORDER-PAYMENT-010";

    /** メッセージID:区分値チェック用 */
    public static final String MSGCD_INVALID = "ORDER-PAYMENT-002";

    /** メッセージID:カード番号イレギュラーチェック */
    public static final String MSGCD_CREDIT_CARD_NUMBER_INVALID = "ORDER-PAYMENT-003";

    /** メッセージID:カード番号桁数誤りチェック */
    public static final String MSGCD_CREDIT_CARD_NUMBER_LENGTH = "ORDER-PAYMENT-004";

    /** メッセージID:カード番号数値以外チェック */
    public static final String MSGCD_CREDIT_CARD_NUMBER_NOT_NUMBER = "ORDER-PAYMENT-005";

    /** メッセージID:有効期限の期限切れチェック */
    public static final String MSGCD_EXPIRED_CARD = "ORDER-PAYMENT-006";

    /** メッセージID:有効期限の日付不正チェック */
    public static final String MSGCD_NOT_DATE = "ORDER-PAYMENT-007";

    /** メッセージID:セキュリティコード桁数誤りチェック */
    public static final String MSGCD_SECURITY_CODE_LENGTH = "ORDER-PAYMENT-008";

    /** メッセージID:セキュリティコード数値以外チェック */
    public static final String MSGCD_SECURITY_CODE_NOT_NUMBER = "ORDER-PAYMENT-009";

    /** カード番号イレギュラーチェック用エラーパターン */
    public static final String VALID_PATTERN_CREDIT_CARD_NUMBER_INVALID = "{HCreditCardNumberValidator.INVALID_detail}";

    /** カード番号桁数誤りチェック用エラーパターン */
    public static final String VALID_PATTERN_CREDIT_CARD_NUMBER_LENGTH = "{HCreditCardNumberValidator.LENGTH_detail}";

    /** カード番号数値以外チェック用エラーパターン */
    public static final String VALID_PATTERN_CREDIT_CARD_NUMBER_NOT_NUMBER =
                    "{HCreditCardNumberValidator.NOT_NUMBER_detail}";

    /** 有効期限の期限切れチェック用エラーパターン */
    public static final String VALID_PATTERN_EXPIRED_CARD = "{HCreditCardExpirationDateValidator.EXPIRED_CARD_detail}";

    /** 有効期限の日付不正チェック用エラーパターン */
    public static final String VALID_PATTERN_NOT_DATE = "{HDateValidator.NOT_DATE_detail}";

    /** セキュリティコードの桁数誤りチェック用エラーパターン */
    public static final String VALID_PATTERN_SECURITY_CODE_LENGTH = "{HSecurityCodeValidator.LENGTH_detail}";

    /** セキュリティコードが数値以外チェック用エラーパターン */
    public static final String VALID_PATTERN_SECURITY_CODE_NOT_NUMBER = "{HSecurityCodeValidator.NOT_NUMBER_detail}";

    /** エラーメッセージコード：分割選択不可 */
    public static final String MSGCD_ENABLE_INSTALLMENT = "AOX000404W";

    /** エラーメッセージコード：リボ選択不可 */
    public static final String MSGCD_ENABLE_REVOLVING = "AOX000405W";

    /**
     * 引数で渡ったクラスが、バリデーション対象か否か
     *
     * @param clazz
     * @return boolean
     */
    @Override
    public boolean supports(Class<?> clazz) {
        // OrderModelがサポート対象
        // 加えて、画面間引き渡し項目である「CartDto」も対象に含める
        //
        // ＜CartDtoを対象に含める理由＞
        // CartDtoをFlashAttribute渡しにした場合、OrderControllerのInitBinderで落ちてしまうため・・・
        // supports対象でないDtoが渡されると、「バリデータチェックスルー」ではなく、「不正なオブジェクトが渡された」ということで例外発生する
        // ⇒CartDtoも一旦supports対象にしつつ、validateをスルーさせる必要があるようだ・・・
        return PaySelectModel.class.isAssignableFrom(clazz) || OrderCommonModel.class.isAssignableFrom(clazz)
               || SalesSlipModel.class.isAssignableFrom(clazz);
    }

    /**
     * クレジットカード及びコンビニ用の動的バリデータ
     *
     * @param target
     * @param errors
     */
    @Override
    public void validate(Object target, Errors errors, Object... validationHints) {

        if (Objects.equals(validationHints, null)) {
            // 対象groupがない場合、チェックしない
            return;
        }

        if (!PaySelectGroup.class.equals(validationHints[0])) {
            // バリデータ対象のgroupが、PaymentGroup以外の場合、チェックしない
            return;
        }

        if (errors.hasErrors()) {
            return;
        }

        PaySelectModel model = PaySelectModel.class.cast(target);

        String settlementMethod = model.getSettlementMethod();
        Map<String, SettlementDto> settlementDtoMap = model.getSettlementDtoMap();

        // 決済方法のチェック
        if (settlementMethod == null) {
            errors.rejectValue(FILED_NAME_SETTLEMENT_METHOD, MSGCD_REQUIRED_SELECT);
        }

        if (settlementDtoMap == null || !settlementDtoMap.containsKey(settlementMethod)) {
            // 不正操作のため、ここではチェックしない
            return;
        }

        // エラーメッセージ対象フィールドのindexを作成
        PaySelectModelItem checkItem = null;
        int index = 0;
        List<PaySelectModelItem> paymentModelItems = model.getPaySelectModelItems();
        for (PaySelectModelItem curItem : paymentModelItems) {
            if (StringUtils.isNotEmpty(curItem.getSettlementMethodValue()) && curItem.getSettlementMethodValue()
                                                                                     .equals(settlementMethod)) {
                // 選択した決済方法と利用可能決済手段が一致する場合、取得
                checkItem = curItem;

                break;
            }
            index++;
        }

        if (checkItem == null) {
            // 対象決済がない場合、チェックしない
            return;
        }

        SettlementDetailsDto settlementDetailsDto = settlementDtoMap.get(settlementMethod).getSettlementDetailsDto();

        // クレジットカードのチェック
        if (CREDIT.equals(settlementDetailsDto.getSettlementMethodType())) {
            errors = checkCredit(model, checkItem, index, errors);
            return;
        }

        return;
    }

    /**
     * 決済方法がクレジットカードの場合のチェック<br/>
     *
     * @param model
     * @param checkItem
     * @param index
     * @param errors
     * @return errors
     */
    public Errors checkCredit(PaySelectModel model, PaySelectModelItem checkItem, int index, Errors errors) {

        // チェック対象項目を取得
        // items項目を取得
        String cardNumber = checkItem.getCardNumber();
        String expirationDateYear = checkItem.getExpirationDateYear();
        String expirationDateMonth = checkItem.getExpirationDateMonth();
        String securityCode = checkItem.getSecurityCode();// 新規カード入力時はnull
        String paymentType = checkItem.getPaymentType();
        String dividedNumber = checkItem.getDividedNumber();

        // model項目を取得
        String token = model.getToken();
        Map<String, String> expirationDateYearItems = model.getExpirationDateYearItems();
        Map<String, String> expirationDateMonthItems = model.getExpirationDateMonthItems();
        Map<String, String> dividedNumberItems = model.getDividedNumberItems();

        // トークンチェック開始（登録済みカードの場合。チェックしない）
        if (!model.isDisplayCredit()) {

            // 必須
            if (StringUtils.isEmpty(token)) {
                errors.rejectValue(FILED_NAME_ITEMS + index + FILED_NAME_CARD_NUMBER, MSGCD_TOKEN);
            }
        }

        // カード番号チェック開始（登録済みカードの場合。チェックしない）
        if (!model.isDisplayCredit()) {
            // トークンが設定されている場合はチェックしない
            if (!isTokenSet(token)) {

                // 必須
                if (StringUtils.isEmpty(cardNumber)) {
                    errors.rejectValue(FILED_NAME_ITEMS + index + FILED_NAME_CARD_NUMBER, MSGCD_REQUIRED_INPUT);
                }

                // カード妥当性チェック
                HCreditCardNumberValidator creditCardNumberValidator = new HCreditCardNumberValidator();
                if (!creditCardNumberValidator.isValid(cardNumber)) {
                    if (VALID_PATTERN_CREDIT_CARD_NUMBER_INVALID.equals(creditCardNumberValidator.getMessageId())) {
                        // カード番号として不適合の場合
                        errors.rejectValue(
                                        FILED_NAME_ITEMS + index + FILED_NAME_CARD_NUMBER,
                                        MSGCD_CREDIT_CARD_NUMBER_INVALID
                                          );
                    }
                    if (VALID_PATTERN_CREDIT_CARD_NUMBER_LENGTH.equals(creditCardNumberValidator.getMessageId())) {
                        // カード番号の桁数誤りの場合
                        errors.rejectValue(
                                        FILED_NAME_ITEMS + index + FILED_NAME_CARD_NUMBER,
                                        MSGCD_CREDIT_CARD_NUMBER_LENGTH
                                          );
                    }
                    if (VALID_PATTERN_CREDIT_CARD_NUMBER_NOT_NUMBER.equals(creditCardNumberValidator.getMessageId())) {
                        // カード番号が数値以外の場合
                        errors.rejectValue(
                                        FILED_NAME_ITEMS + index + FILED_NAME_CARD_NUMBER,
                                        MSGCD_CREDIT_CARD_NUMBER_NOT_NUMBER
                                          );
                    }
                }
            }
        }

        // 有効期限チェック開始

        // 必須
        if (StringUtils.isEmpty(expirationDateYear)) {
            errors.rejectValue(FILED_NAME_ITEMS + index + FILED_NAME_EXPIRATION_DATE_YEAR, MSGCD_REQUIRED_INPUT);
        }

        // 必須
        if (StringUtils.isEmpty(expirationDateMonth)) {
            errors.rejectValue(FILED_NAME_ITEMS + index + FILED_NAME_EXPIRATION_DATE_MONTH, MSGCD_REQUIRED_INPUT);
        }

        // 何らかの操作で有効期限-年が不正に書き換えた場合を想定しチェック処理を追加
        // 不正値が設定され、以降の処理で想定外の動作が発生するのを防ぐ為。
        if (!StringUtils.isEmpty(expirationDateYear) && !expirationDateYearItems.containsKey(expirationDateYear)) {
            // 有効期限の年が不正
            errors.rejectValue(FILED_NAME_ITEMS + index + FILED_NAME_EXPIRATION_DATE_YEAR, MSGCD_INVALID);
        }

        // 何らかの操作で有効期限-月が不正に書き換えた場合を想定しチェック処理を追加
        // 不正値が設定され、以降の処理で想定外の動作が発生するのを防ぐ為。
        if (!StringUtils.isEmpty(expirationDateMonth) && !expirationDateMonthItems.containsKey(expirationDateMonth)) {
            // 有効期限の月が不正
            errors.rejectValue(FILED_NAME_ITEMS + index + FILED_NAME_EXPIRATION_DATE_MONTH, MSGCD_INVALID);
        }

        // 有効期限妥当性チェック
        HCreditCardExpirationDateValidator creditCardExpirationDateValidator = new HCreditCardExpirationDateValidator();
        if (!creditCardExpirationDateValidator.isValid(expirationDateMonth, expirationDateYear)) {
            if (VALID_PATTERN_EXPIRED_CARD.equals(creditCardExpirationDateValidator.getMessageId())) {
                // 有効期限が切れている場合
                errors.rejectValue(FILED_NAME_ITEMS + index + FILED_NAME_EXPIRATION_DATE_YEAR, MSGCD_EXPIRED_CARD);
            }
            if (VALID_PATTERN_NOT_DATE.equals(creditCardExpirationDateValidator.getMessageId())) {
                // 有効期限が数値以外の場合
                errors.rejectValue(FILED_NAME_ITEMS + index + FILED_NAME_EXPIRATION_DATE_YEAR, MSGCD_NOT_DATE);
            }
        }

        // セキュリティコードチェック開始（トークンが設定されている場合はチェックしない）
        if (!isTokenSet(token)) {

            // 必須
            if (StringUtils.isEmpty(securityCode)) {
                errors.rejectValue(FILED_NAME_ITEMS + index + FILED_NAME_SECURITY_CODE, MSGCD_REQUIRED_INPUT);
            }

            // セキュリティコード妥当性チェック
            HSecurityCodeValidator securityCodeValidator = new HSecurityCodeValidator();
            if (!securityCodeValidator.isValid(securityCode)) {
                if (VALID_PATTERN_SECURITY_CODE_LENGTH.equals(securityCodeValidator.getMessageId())) {
                    // セキュリティコードの桁数誤りの場合
                    errors.rejectValue(FILED_NAME_ITEMS + index + FILED_NAME_SECURITY_CODE, MSGCD_SECURITY_CODE_LENGTH);
                }
                if (VALID_PATTERN_SECURITY_CODE_NOT_NUMBER.equals(securityCodeValidator.getMessageId())) {
                    // セキュリティコードが数値以外の場合
                    errors.rejectValue(FILED_NAME_ITEMS + index + FILED_NAME_SECURITY_CODE,
                                       MSGCD_SECURITY_CODE_NOT_NUMBER
                                      );
                }
            }
        }

        // 支払区分チェック開始
        // 必須
        if (StringUtils.isEmpty(paymentType)) {
            errors.rejectValue(FILED_NAME_ITEMS + index + FILED_NAME_PAYMENT_TYPE, MSGCD_REQUIRED_INPUT);
        }

        // 支払区分に「分割」を選択している場合、分割回数は必須
        if (!StringUtils.isEmpty(paymentType) && paymentType.equals(INSTALLMENT.getValue())) {

            // 必須
            if (StringUtils.isEmpty(dividedNumber)) {
                errors.rejectValue(FILED_NAME_ITEMS + index + FILED_NAME_DIVIDED_NUMBER, MSGCD_REQUIRED_SELECT);
            }

            // 何らかの操作で分割回数が不正に書き換えた場合を想定しチェック処理を追加
            // 不正値が設定され、以降の処理で想定外の動作が発生するのを防ぐ為。
            if (!StringUtils.isEmpty(dividedNumber) && !dividedNumberItems.containsKey(dividedNumber)) {
                // 分割回数が不正
                errors.rejectValue(FILED_NAME_ITEMS + index + FILED_NAME_DIVIDED_NUMBER, MSGCD_INVALID);
            }

            // カードの分割選択可能チェック
            SettlementDetailsDto settlementDetailsDto =
                            model.getSettlementDtoMap().get(model.getSettlementMethod()).getSettlementDetailsDto();
            if (HTypeEffectiveFlag.INVALID == settlementDetailsDto.getEnableInstallment()) {
                String[] arg = new String[1];
                arg[0] = settlementDetailsDto.getSettlementMethodName();
                errors.rejectValue(FILED_NAME_ITEMS + index + FILED_NAME_PAYMENT_TYPE, MSGCD_ENABLE_INSTALLMENT, arg,
                                   null
                                  );
            }
        }

        // カードのリボ選択可能チェック
        if (!StringUtils.isEmpty(paymentType) && paymentType.equals(REVOLVING.getValue())) {

            SettlementDetailsDto settlementDetailsDto =
                            model.getSettlementDtoMap().get(model.getSettlementMethod()).getSettlementDetailsDto();
            if (HTypeEffectiveFlag.INVALID == settlementDetailsDto.getEnableRevolving()) {
                String[] arg = new String[1];
                arg[0] = settlementDetailsDto.getSettlementMethodName();
                errors.rejectValue(
                                FILED_NAME_ITEMS + index + FILED_NAME_PAYMENT_TYPE, MSGCD_ENABLE_REVOLVING, arg, null);
            }
        }
        return errors;
    }

    /**
     * トークンがセットされているか<br/>
     *
     * @param token
     * @return true = tokenがセットされている
     */
    public boolean isTokenSet(String token) {
        if (StringUtils.isEmpty(token)) {
            return false;
        }
        return true;
    }

    /** 未使用 */
    @Override
    public void validate(Object target, Errors errors) {
        // 未使用
    }
}