package jp.co.itechh.quad.admin.pc.web.admin.shop.freearea.validation;

import jp.co.itechh.quad.admin.pc.web.admin.common.validation.group.DisplayChangeGroup;
import jp.co.itechh.quad.admin.pc.web.admin.shop.freearea.FreeareaModel;
import jp.co.itechh.quad.admin.pc.web.admin.shop.freearea.validation.group.FreeAreaSearchGroup;
import jp.co.itechh.quad.admin.validator.HDateValidator;
import lombok.Data;
import org.apache.commons.lang3.StringUtils;
import org.springframework.stereotype.Component;
import org.springframework.validation.Errors;
import org.springframework.validation.SmartValidator;

import javax.validation.Validation;
import java.util.Objects;

@Data
@Component
public class TargetDateTimeValidator implements SmartValidator {
    /** エラーコード：必須 */
    public static final String MSGCD_REQUIRED_INPUT =
                    "jp.co.hankyuhanshin.itec.hmbase.validator.HRequiredValidator.INPUT_detail";

    /** エラーコード：日付フォーマット */
    public static final String MSGCD_NOT_DATE =
                    "jp.co.hankyuhanshin.itec.hmbase.validator.HDateValidator.NOT_DATE_detail";

    /** 表示状態-日時タイプ:現在日時  */
    public static final String SEARCH_DATE_TYPE_CURRENTDATE = "0";

    /** 表示状態-日時タイプ:指定日時  */
    public static final String SEARCH_DATE_TYPE_TARGETDATE = "1";

    /**  表示状態-日付（日） */
    public static final String FILED_NAME_TARGET_DATE = "searchTargetDate";

    /**  表示状態-日付（時間） */
    public static final String FILED_NAME_TARGET_TIME = "searchTargetTime";

    /** デフォルト（日） のフォーマットパターン */
    public static final String DEFAULT_DATE_PATTERN = "yyyy/MM/dd";

    /** デフォルト（時間） のフォーマットパターン */
    public static final String DEFAULT_TIME_PATTERN = "HH:mm:ss";

    @Override
    public boolean supports(Class<?> clazz) {
        return FreeareaModel.class.isAssignableFrom(clazz);
    }

    @Override
    public void validate(Object target, Errors errors, Object... validationHints) {
        if (Objects.equals(validationHints, null)) {
            // 対象groupがない場合、チェックしない
            return;
        }

        if (!FreeAreaSearchGroup.class.equals(validationHints[0]) && !DisplayChangeGroup.class.equals(
                        validationHints[0])) {
            // バリデータ対象のgroupが、FreeAreaSearchGroupとDisplayChangeGroup以外の場合、チェックしない
            return;
        }

        FreeareaModel model = (FreeareaModel) target;

        if (SEARCH_DATE_TYPE_TARGETDATE.equals(model.getSearchDateType())) {

            // 必須
            if (StringUtils.isEmpty(model.getSearchTargetDate())) {
                errors.rejectValue(FILED_NAME_TARGET_DATE, MSGCD_REQUIRED_INPUT, new String[] {"表示状態-日付（日）"}, null);
            } else {
                // 表示状態-日付（日） 入力チェック
                errors = checkDate(model, errors);
            }

            // 表示状態-日付（日） 入力チェック
            if (!StringUtils.isEmpty(model.getSearchTargetTime())) {
                errors = checkTime(model, errors);
            }
        }

        return;
    }

    /**
     *
     * @param model FreeareaModel
     * @param errors Errors
     * @return Errors
     */
    private Errors checkTime(FreeareaModel model, Errors errors) {

        // パターンはカンマで分割
        HDateValidator validator = Validation.buildDefaultValidatorFactory()
                                             .getConstraintValidatorFactory()
                                             .getInstance(HDateValidator.class);

        validator.setPattern(DEFAULT_TIME_PATTERN);
        boolean result = validator.isValid(model.getSearchTargetTime(), null);
        if (!result) {
            errors.rejectValue(FILED_NAME_TARGET_TIME, MSGCD_NOT_DATE, new String[] {"表示状態-日付（時間）"}, null);
        }

        return errors;
    }

    /**
     *
     * @param model FreeareaModel
     * @param errors Errors
     * @return Errors
     */
    private Errors checkDate(FreeareaModel model, Errors errors) {

        // パターンはカンマで分割
        HDateValidator validator = Validation.buildDefaultValidatorFactory()
                                             .getConstraintValidatorFactory()
                                             .getInstance(HDateValidator.class);

        validator.setPattern(DEFAULT_DATE_PATTERN);
        boolean result = validator.isValid(model.getSearchTargetDate(), null);
        if (!result) {
            errors.rejectValue(FILED_NAME_TARGET_DATE, MSGCD_NOT_DATE, new String[] {"表示状態-日付（日）"}, null);
        }

        return errors;
    }

    /** 未使用 */
    @Override
    public void validate(Object target, Errors errors) {
        // 未使用
    }
}