package jp.co.itechh.quad.admin.pc.web.admin.order.examresults.validation;

import jp.co.itechh.quad.admin.constant.type.HTypeExamResultsUploadType;
import jp.co.itechh.quad.admin.dto.order.ExamResultsCsvDto;
import jp.co.itechh.quad.admin.pc.web.admin.common.validation.group.UploadGroup;
import jp.co.itechh.quad.admin.pc.web.admin.order.examresults.ExamResultsUploadModel;
import jp.co.itechh.quad.admin.validator.HCsvHeaderValidator;
import org.springframework.stereotype.Component;
import org.springframework.util.StringUtils;
import org.springframework.validation.Errors;
import org.springframework.validation.SmartValidator;
import org.springframework.web.multipart.MultipartFile;

import javax.validation.Validation;
import java.util.Arrays;
import java.util.List;
import java.util.Objects;

/**
 * 検査結果一括アップロード画面の動的バリデータ
 *
 */

@Component
public class ExamResultsUploadValidator implements SmartValidator {

    /**
     * フィールド名
     */
    public static final String FILED_NAME_UPLOAD = "registUploadFile";

    /**
     * ファイル名チェックNG時のメッセージ
     */
    public static final String EXAM_RESULTS_INVALID_EXTENSION_MESSAGE_ID = "HMultipartFileValidator.INVALID_detail";

    /**
     * CSVヘッダバリデーションNG時メッセージ
     */
    public static final String CSV_HEADER_VALIDATOR_MESSAGE_ID = "HCsvHeaderValidator.INVALID_detail";

    /**
     * エラーコード：入力必須
     */
    public static final String MSGCD_REQUIRED = "javax.validation.constraints.NotNull.message";

    /**
     * CSVファイル名の拡張子
     */
    public static final String[] CSV_UPLOAD_TARGET_EXTENSION = new String[]{"csv"};

    /**
     * PDF、ZIP ファイル名拡張子
     */
    public static final String[] PDF_UPLOAD_TARGET_EXTENSION = new String[]{"pdf", "zip"};

    /**
     * @param target
     * @param errors
     * @param validationHints
     */
    @Override
    public void validate(Object target, Errors errors, Object... validationHints) {
        if (Objects.equals(validationHints, null)) {
            // 対象groupがない場合、チェックしない
            return;
        }

        if (!UploadGroup.class.equals(validationHints[0])) {
            // バリデータ対象のgroupが、UploadGroup、チェックしない
            return;
        }

        ExamResultsUploadModel model = (ExamResultsUploadModel) target;

        MultipartFile uploadFile = model.getRegistUploadFile();

        // null or 空の場合は未実施
        if (uploadFile == null || !StringUtils.hasLength(uploadFile.getOriginalFilename())) {
            errors.rejectValue(FILED_NAME_UPLOAD, MSGCD_REQUIRED, new String[]{}, null);
        }

        if (HTypeExamResultsUploadType.CSV.getValue().equals(model.getUploadTarget())) {
            checkValidExtension(uploadFile.getOriginalFilename(), CSV_UPLOAD_TARGET_EXTENSION, errors);
            validateHeader(uploadFile, errors);
        } else if (HTypeExamResultsUploadType.PDF.getValue().equals(model.getUploadTarget())) {
            checkValidExtension(uploadFile.getOriginalFilename(), PDF_UPLOAD_TARGET_EXTENSION, errors);
        }
    }

    /**
     * ファイル拡張子のバリデーション<br/>
     */
    private void checkValidExtension(String fileName, String[] validExtensionArr, Errors errors) {
        if (StringUtils.hasLength(fileName)) {
            int lastIndexOf = fileName.lastIndexOf(".");

            // 拡張子なしの場合
            if (lastIndexOf == -1) {
                errors.rejectValue(FILED_NAME_UPLOAD, EXAM_RESULTS_INVALID_EXTENSION_MESSAGE_ID, new String[]{}, null);
            }

            // 不当な拡張子の場合
            List<String> fileExtensionList = Arrays.asList(validExtensionArr);
            String fileExtension = fileName.substring(lastIndexOf + 1);
            if (!fileExtensionList.contains(fileExtension)) {
                errors.rejectValue(FILED_NAME_UPLOAD, EXAM_RESULTS_INVALID_EXTENSION_MESSAGE_ID, new String[]{}, null);
            }
        }
    }

    /**
     *  CSVヘッダ行の確認を行うバリデータ
     * @param value
     * @param errors
     * @return
     */
    private void validateHeader(MultipartFile value, Errors errors) {
        // 数値チェックバリデータ
        HCsvHeaderValidator validator = Validation.buildDefaultValidatorFactory().getConstraintValidatorFactory()
                .getInstance(HCsvHeaderValidator.class);

        validator.setCsvDtoClass(ExamResultsCsvDto.class);
        validator.setCsvCharset("MS932");

        boolean result = validator.isValid(value, null);
        if (!result) {
            errors.rejectValue(FILED_NAME_UPLOAD, CSV_HEADER_VALIDATOR_MESSAGE_ID, new String[]{}, null);
        }

    }

    /**
     * @param clazz
     * @return
     */
    @Override
    public boolean supports(Class<?> clazz) {
        return ExamResultsUploadModel.class.isAssignableFrom(clazz);
    }

    /**
     * 未使用
     */
    @Override
    public void validate(Object target, Errors errors) {
        // 未使用
    }
}
