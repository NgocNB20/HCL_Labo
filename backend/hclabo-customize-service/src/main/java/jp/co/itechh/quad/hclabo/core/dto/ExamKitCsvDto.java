package jp.co.itechh.quad.hclabo.core.dto;

import jp.co.itechh.quad.hclabo.core.annotation.csv.CsvColumn;
import jp.co.itechh.quad.hclabo.core.base.constant.ValidatorConstants;
import lombok.Data;
import org.hibernate.validator.constraints.Length;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Component;

import javax.validation.constraints.NotEmpty;
import javax.validation.constraints.Pattern;
import java.io.Serializable;

@Data
@Component
@Scope("prototype")
public class ExamKitCsvDto implements Serializable {

    /** シリアルバージョンID */
    private static final long serialVersionUID = 1L;

    /** 正規表現エラー */
    public static final String MSGCD_REGULAR_EXPRESSION_EXAM_KIT_CODE_ERR = "{AOX000811W}";

    /** 受注番号 */
    @CsvColumn(order = 10, columnLabel = "検査キット番号")
    @NotEmpty
    @Pattern(regexp = ValidatorConstants.REGEX_EXAM_KIT_CODE, message = MSGCD_REGULAR_EXPRESSION_EXAM_KIT_CODE_ERR)
    @Length(min = 1, max = ValidatorConstants.LENGTH_EXAM_KIT_CODE_MAXIMUM)
    private String examKitCode;
}
