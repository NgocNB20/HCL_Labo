/*
 * Copyright (C) 2022 i-TEC HANKYU HANSHIN INC. All Rights Reserved.
 */

package jp.co.itechh.quad.admin.pc.web.admin.shop.news.registupdate.validation;

import jp.co.itechh.quad.admin.base.util.common.PropertiesUtil;
import jp.co.itechh.quad.admin.constant.type.HTypeOpenStatus;
import jp.co.itechh.quad.admin.pc.web.admin.common.validation.group.ConfirmGroup;
import jp.co.itechh.quad.admin.pc.web.admin.shop.news.registupdate.NewsRegistUpdateModel;
import lombok.Data;
import org.apache.commons.lang3.StringUtils;
import org.springframework.stereotype.Component;
import org.springframework.validation.Errors;
import org.springframework.validation.SmartValidator;

import java.util.Objects;
import java.util.regex.Pattern;

@Data
@Component
public class NewsRegistUpdateValidator implements SmartValidator {

    /**  タイトル */
    public static final String FILED_NAME_TITLE = "titlePC";

    /**  URL */
    public static final String FILED_NAME_URL = "newsUrlPC";

    /** エラーコード：必須 */
    public static final String MSGCD_REQUIRED_INPUT =
                    "jp.co.hankyuhanshin.itec.hmbase.validator.HRequiredValidator.INPUT_detail";

    /** エラーコード：最大値 */
    public static final String MSGCD_MAXIMUM_INPUT =
                    "jp.co.hankyuhanshin.itec.hmbase.validator.HLengthValidator.MAXIMUM_detail";

    /** エラーコード：不正 */
    public static final String MSGCD_INVALID_INPUT =
                    "jp.co.hankyuhanshin.itec.hmbase.validator.HRegularExpressionValidator.INVALID_detail";

    @Override
    public boolean supports(Class<?> clazz) {
        return NewsRegistUpdateModel.class.isAssignableFrom(clazz);
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

        NewsRegistUpdateModel model = (NewsRegistUpdateModel) target;

        if (!StringUtils.isEmpty(model.getNewsOpenStatusPC())) {
            if (HTypeOpenStatus.OPEN.getValue().equals(model.getNewsOpenStatusPC())) {
                if (StringUtils.isEmpty(model.getTitlePC())) {
                    errors.rejectValue(FILED_NAME_TITLE, MSGCD_REQUIRED_INPUT, new String[] {"ニュースタイトル"}, null);
                }
            }
        }

        if (!StringUtils.isEmpty(model.getNewsUrlPC())) {
            String regex = PropertiesUtil.getSystemPropertiesValue("news.url.validatePattern");
            Pattern pattern = Pattern.compile(regex);
            if (!pattern.matcher(model.getNewsUrlPC()).matches()) {
                errors.rejectValue(FILED_NAME_URL, MSGCD_INVALID_INPUT, new String[] {"URL"}, null);
                return;
            }
            if (model.getNewsUrlPC().length() > 200) {
                errors.rejectValue(FILED_NAME_URL, MSGCD_MAXIMUM_INPUT, new String[] {"200", "URL"}, null);
            }
        }
    }

    /** 未使用 */
    @Override
    public void validate(Object target, Errors errors) {
        // 未使用
    }
}