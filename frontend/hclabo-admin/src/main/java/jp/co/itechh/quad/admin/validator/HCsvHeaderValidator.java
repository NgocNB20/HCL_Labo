package jp.co.itechh.quad.admin.validator;

import jp.co.itechh.quad.admin.annotation.validator.HVCsvHeader;
import jp.co.itechh.quad.admin.base.util.common.DiffUtil;
import jp.co.itechh.quad.admin.core.module.csvupload.CsvReaderModule;
import jp.co.itechh.quad.admin.base.utility.ApplicationContextUtility;
import jp.co.itechh.quad.admin.utility.CsvUtility;
import lombok.Data;
import org.apache.commons.csv.CSVFormat;
import org.apache.commons.csv.CSVParser;
import org.apache.commons.csv.QuoteMode;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.util.StringUtils;
import org.springframework.web.multipart.MultipartFile;

import javax.validation.ConstraintValidator;
import javax.validation.ConstraintValidatorContext;
import java.io.IOException;
import java.io.Reader;
import java.nio.charset.Charset;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

/**
 * <span class="logicName">【CSVヘッダ】</span>CSVヘッダ行の確認を行うバリデータ。<br />
 * <br />
 * CSVファイルのヘッダが指定のヘッダ情報と異なる場合エラー<br />
 *
 * @author Phan Tien VU (VTI Japan Co., Ltd.)
 */
@Data
public class HCsvHeaderValidator implements ConstraintValidator<HVCsvHeader, MultipartFile> {

    /** ロガー */
    private static final Logger LOGGER = LoggerFactory.getLogger(HCsvHeaderValidator.class);

    /** CSVヘッダバリデーションNG時メッセージ */
    public static final String CSV_HEADER_VALIDATOR_MESSAGE_ID = "{HCsvHeaderValidator.INVALID_detail}";

    /** ファイルアップロード失敗メッセージ */
    public static final String UPLOAD_FAILED_MESSAGE_ID = "{AYH000107E}";

    /** ヘッダ行情報を収集する対象のクラス */
    private Class<?> csvDtoClass;

    /** 部分アップロードを禁止する:true 許可する:false */
    private boolean restrict;

    /** CSVファイルの文字セット */
    private String csvCharset;

    @Override
    public void initialize(HVCsvHeader annotation) {
        this.csvDtoClass = annotation.csvDtoClass();
        this.restrict = annotation.restrict();
        this.csvCharset = annotation.csvCharset();
    }

    @Override
    public boolean isValid(MultipartFile value, ConstraintValidatorContext context) {
        // null or 空の場合は未実施
        if (value == null || !StringUtils.hasLength(value.getOriginalFilename())) {
            return true;
        }

        // CSVではないファイルの場合は未実施
        if (!checkFileExtension(value.getOriginalFilename())) {
            return true;
        }

        // Object class は無効とする
        if (csvDtoClass == Object.class) {
            return true;
        }

        // ----------------------------------------------
        // CSVヘッダーの検証を行う
        // ----------------------------------------------
        CsvReaderModule csvReaderModule = ApplicationContextUtility.getBean(CsvReaderModule.class);
        List<String> expectedCsvHeader = csvReaderModule.getCsvHeader(csvDtoClass);

        // CSVHelper取得
        CsvUtility csvUtility = ApplicationContextUtility.getBean(CsvUtility.class);
        // アップロードファイルをテンプファイルとして保存
        String tmpFileName = csvUtility.getUploadCsvTmpFileName("temp");

        try {
            Files.write(Paths.get(tmpFileName), value.getBytes());

            // Apache Common CSV のリーダーの宣言
            try (Reader reader = Files.newBufferedReader(Paths.get(tmpFileName), Charset.forName(csvCharset))) {

                // Apache Common CSV のCSVフォーマッターの宣言
                CSVFormat readerCsvFormat = CSVFormat.DEFAULT.withIgnoreHeaderCase()
                                                             .withTrim()
                                                             .withQuote('"')
                                                             .withRecordSeparator("\r\n")
                                                             .withQuoteMode(QuoteMode.ALL)
                                                             .withFirstRecordAsHeader();

                // Apache Common CSV のパーサーの宣言
                CSVParser csvParser = new CSVParser(reader, readerCsvFormat);

                // アップロードされたCSVのヘッダー行の取得
                List<String> uploadedCsvHeader = csvReaderModule.filterHeaderByOnlyDownload(new ArrayList<>(csvParser.getHeaderNames()), csvDtoClass);

                if (restrict) {
                    return validateRestricted(expectedCsvHeader, uploadedCsvHeader);
                } else {
                    return validateUnrestricted(expectedCsvHeader, uploadedCsvHeader);
                }
            }
        } catch (IOException e) {
            // アップロード時に予想外のエラーが発生した場合
            makeContext(context, UPLOAD_FAILED_MESSAGE_ID);
            return false;
        }
    }

    /**
     * エラーメッセージを構成<br/>
     * メッセージセット＋エラーメッセージを表示する項目を指定
     */
    private void makeContext(ConstraintValidatorContext context, String messageId) {
        context.disableDefaultConstraintViolation();
        context.buildConstraintViolationWithTemplate(messageId).addConstraintViolation();
    }

    /**
     * 部分アップロードを禁止する場合のヘッダー検証
     *
     * @param expectedCsvHeader 完全なヘッダーリスト
     * @param uploadedCsvHeader アップロードされたCSVのヘッダーリスト
     * @return false - ヘッダが一致しない場合
     */
    private boolean validateRestricted(List<String> expectedCsvHeader, List<String> uploadedCsvHeader) {

        List<String> diff = DiffUtil.diff(expectedCsvHeader, uploadedCsvHeader);

        if (!diff.isEmpty()) {

            for (String d : diff) {
                LOGGER.debug("HEADER-Csv Header diferrence:" + d);
            }

            return false;
        }

        return true;
    }

    /**
     * 部分アップロードを禁止しない場合のヘッダー検証
     *
     * @param expectedCsvHeader 完全なヘッダーリスト
     * @param uploadedCsvHeader アップロードされたCSVのヘッダーリスト
     * @return false - アップロード可能なヘッダが一つも含まれていない
     */
    private boolean validateUnrestricted(List<String> expectedCsvHeader, List<String> uploadedCsvHeader) {

        if (!validateDuplicateHeader(uploadedCsvHeader)) {
            return false;
        }

        boolean found = false;

        for (String shouldExist : uploadedCsvHeader) {
            if (expectedCsvHeader.contains(shouldExist)) {
                found = true;
                break;
            }
        }

        return found;
    }

    /**
     * ファイル拡張子のバリデーション<br/>
     */
    private boolean checkFileExtension(String fileName) {
        if (StringUtils.hasLength(fileName)) {
            int lastIndexOf = fileName.lastIndexOf(".");

            // 拡張子なしの場合
            if (lastIndexOf == -1) {
                return false;
            }

            // 不当な拡張子の場合
            String fileExtension = fileName.substring(lastIndexOf + 1);
            if (!"csv".equalsIgnoreCase(fileExtension)) {
                return false;
            }
        }

        return true;
    }

    /**
     * validateDuplicateHeader.
     *
     * @param uploadedCsvHeader アップロードされたCSVのヘッダーリスト
     * @return the boolean
     */
    public boolean validateDuplicateHeader(List<String> uploadedCsvHeader) {
        Set<String> uploadedCsvHeaderSet = new HashSet<>(uploadedCsvHeader);
        return uploadedCsvHeaderSet.size() == uploadedCsvHeader.size();
    }

}