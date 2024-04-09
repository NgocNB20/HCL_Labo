package jp.co.itechh.quad.core.dto.accesskeywords;

import jp.co.itechh.quad.core.annotation.csv.CsvColumn;
import lombok.Data;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Component;

/**
 * 検索キーワード集計Dtoクラス
 *
 * @author Pham Quang Dieu (VTI Japan Co., Ltd.)
 */
@Data
@Component
@Scope("prototype")
public class AccessKeywordsCSVDto {

    /**
     * 検索キーワード
     */
    @CsvColumn(order = 10, columnLabel = "検索キーワード")
    private String searchKeyword;

    /**
     * 検索回数
     */
    @CsvColumn(order = 20, columnLabel = "検索回数")
    private String searchCount;

    /**
     * 検索結果数
     */
    @CsvColumn(order = 30, columnLabel = "検索結果数")
    private String searchResultCount;

}