package jp.co.itechh.quad.admin.pc.web.admin.mailtemplate.send.validation;

import jp.co.itechh.quad.admin.pc.web.admin.common.validation.group.ConfirmGroup;
import jp.co.itechh.quad.admin.pc.web.admin.mailtemplate.send.MailtemplateSendModel;
import jp.co.itechh.quad.admin.pc.web.admin.mailtemplate.send.validation.group.SendTestGroup;
import lombok.Data;
import org.apache.commons.lang3.StringUtils;
import org.springframework.stereotype.Component;
import org.springframework.validation.Errors;
import org.springframework.validation.SmartValidator;

import java.util.Objects;
import java.util.regex.Pattern;

@Data
@Component
public class MailTitleValidator implements SmartValidator {

    /** エラーコード：必須入力、タブ・半角スペースのみ入力時エラー */
    public static final String MSGCD_REQUIRED_EMPTY_FAIL = "AYM001702W";

    public static final String MSGCD_MAXIMUM_LENGTH =
                    "jp.co.hankyuhanshin.itec.hmbase.validator.HLengthValidator.MAXIMUM_detail";

    private static final String FILED_NAME_MAIL_TITLE = "mailTitle";

    @Override
    public boolean supports(Class<?> clazz) {
        return MailtemplateSendModel.class.isAssignableFrom(clazz);
    }

    @Override
    public void validate(Object target, Errors errors, Object... validationHints) {
        if (Objects.equals(validationHints, null)) {
            // 対象groupがない場合、チェックしない
            return;
        }

        if (!SendTestGroup.class.equals(validationHints[0]) && !ConfirmGroup.class.equals(validationHints[0])) {
            // バリデータ対象のgroupが、ConfirmGroupとSendTestGroup以外の場合、チェックしない
            return;
        }

        MailtemplateSendModel model = MailtemplateSendModel.class.cast(target);

        // 必須チェック
        errors = checkEmpty(model, errors);
        if (errors.hasErrors()) {
            return;
        }

        // 文字列桁数チェック
        errors = checkLength(model, errors);
        if (errors.hasErrors()) {
            return;
        }

        // スペースのみ入力された場合エラー
        errors = checkRegularExpression(model, errors);
        if (errors.hasErrors()) {
            return;
        }
        return;
    }

    /**
     *
     * @param model
     * @param errors
     * @return
     */
    private Errors checkRegularExpression(MailtemplateSendModel model, Errors errors) {
        Pattern pattern = Pattern.compile("^(?s:.*[^　\\s\\r\\n].*)$");
        if (!pattern.matcher(model.getMailTitle()).matches()) {
            errors.rejectValue(FILED_NAME_MAIL_TITLE, MSGCD_REQUIRED_EMPTY_FAIL, new String[] {"メール件名"}, null);
        }
        return errors;
    }

    /**
     *
     * @param model
     * @param errors
     * @return
     */
    private Errors checkEmpty(MailtemplateSendModel model, Errors errors) {
        if (StringUtils.isEmpty(model.getMailTitle())) {
            errors.rejectValue(FILED_NAME_MAIL_TITLE, MSGCD_REQUIRED_EMPTY_FAIL, new String[] {"メール件名"}, null);
        }
        return errors;
    }

    /**
     *
     * @param model
     * @param errors
     * @return
     */
    private Errors checkLength(MailtemplateSendModel model, Errors errors) {

        if (model.getMailTitle().length() > 200) {
            errors.rejectValue(FILED_NAME_MAIL_TITLE, MSGCD_MAXIMUM_LENGTH, new String[] {String.valueOf(200), "メール件名"},
                               null
                              );
        }
        return errors;
    }

    /** 未使用 */
    @Override
    public void validate(Object target, Errors errors) {
        // 未使用
    }
}