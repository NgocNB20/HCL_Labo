package jp.co.itechh.quad.admin.pc.web.admin.memberinfo.validation;

import jp.co.itechh.quad.admin.pc.web.admin.common.validation.group.AllDownloadGroup;
import jp.co.itechh.quad.admin.pc.web.admin.common.validation.group.DisplayChangeGroup;
import jp.co.itechh.quad.admin.pc.web.admin.common.validation.group.SearchGroup;
import jp.co.itechh.quad.admin.pc.web.admin.memberinfo.MemberInfoModel;
import lombok.Data;
import org.apache.commons.lang3.StringUtils;
import org.springframework.stereotype.Component;
import org.springframework.validation.Errors;
import org.springframework.validation.SmartValidator;

import java.util.Objects;

@Data
@Component
public class PeriodValidator implements SmartValidator {

    /** エラーコード：必須 */
    public static final String MSGCD_REQUIRED = "HRequiredValidator.REQUIRED_detail";

    /** 期間プルダウンが選択されていて、日付に入力がない時：期間FROMにAMX000104Wを表示 */
    public static final String MSGCD_PERIOD_REQUIRED = "AMX000104W";

    /** 期間プルダウンが選択されていて、日付に入力がない時：期間TOに空白を表示 */
    public static final String MSGCD_PERIOD_REQUIRED_EMPTY_MSG = "REQUIRED-EMPTY-MSG";

    /** 期間選択 */
    public static final String FILED_NAME_PERIOD_TYPE = "periodType";

    /** 開始時間 */
    public static final String FILED_NAME_START_DATE = "startDate";

    /** 終了時間 */
    public static final String FILED_NAME_END_DATE = "endDate";

    @Override
    public boolean supports(Class<?> clazz) {
        return MemberInfoModel.class.isAssignableFrom(clazz);
    }

    @Override
    public void validate(Object target, Errors errors, Object... validationHints) {
        if (Objects.equals(validationHints, null)) {
            // 対象groupがない場合、チェックしない
            return;
        }

        if (!SearchGroup.class.equals(validationHints[0]) && !AllDownloadGroup.class.equals(validationHints[0])
            && !DisplayChangeGroup.class.equals(validationHints[0])) {
            // バリデータ対象のgroupが、SearchGroup、AllDownloadGroupとDisplayChangeGroup以外の場合、チェックしない
            return;
        }

        MemberInfoModel model = (MemberInfoModel) target;

        // 日付に入力がある場合は必須
        if (!StringUtils.isEmpty(model.getStartDate()) || !StringUtils.isEmpty(model.getEndDate())) {
            if (StringUtils.isEmpty(model.getPeriodType())) {
                errors.rejectValue(FILED_NAME_PERIOD_TYPE, MSGCD_REQUIRED, new String[] {"期間選択"}, null);
            }
        }

        // 期間プルダウンが選択されている場合は、開始日付、終了日付のどちらかが必須
        if (!StringUtils.isEmpty(model.getPeriodType())) {
            if (StringUtils.isEmpty(model.getStartDate()) && StringUtils.isEmpty(model.getEndDate())) {
                errors.rejectValue(FILED_NAME_START_DATE, MSGCD_PERIOD_REQUIRED);
                errors.rejectValue(FILED_NAME_END_DATE, MSGCD_PERIOD_REQUIRED_EMPTY_MSG);
            }
        }
    }

    /** 未使用 */
    @Override
    public void validate(Object target, Errors errors) {
        // 未使用
    }
}