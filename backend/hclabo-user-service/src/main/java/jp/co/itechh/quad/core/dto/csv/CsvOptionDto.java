package jp.co.itechh.quad.core.dto.csv;

import lombok.Data;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Component;

import java.io.Serializable;
import java.util.List;

/**
 * CSVオプションDtoクラス
 *
 * @author Pham Quang Dieu (VTI Japan Co., Ltd.)
 *
 */
@Data
@Component
@Scope("prototype")
public class CsvOptionDto implements Serializable {

    /** serialVersionUID */
    private static final long serialVersionUID = 1L;

    /** オプションSEQ（必須） */
    private Integer optionId;

    /** オプション名 */
    private String optionName;

    /** オプション名のデフォルト */
    private String defaultOptionName;

    /** ヘッダー出力フラグ */
    private boolean outHeader;

    /** オプションコンテンツDto */
    private List<OptionContentDto> optionContent;
}
