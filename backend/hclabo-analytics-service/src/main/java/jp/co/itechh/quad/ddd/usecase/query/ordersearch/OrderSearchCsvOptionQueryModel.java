package jp.co.itechh.quad.ddd.usecase.query.ordersearch;

import jp.co.itechh.quad.core.dto.csv.OptionContentDto;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.springframework.data.annotation.Id;

import java.util.List;

/**
 * 注検索CSVオプションクエリモデル
 */
@Getter
@Setter
@NoArgsConstructor
public class OrderSearchCsvOptionQueryModel {

    /**
     * Id
     */
    @Id
    private String _id;

    /**
     * オプション名のデフォルト
     */
    private String defaultOptionName;

    /**
     * オプション名
     */
    private String optionName;

    /**
     * ヘッダー出力フラグ
     */
    private boolean outHeader;

    /**
     * オプションコンテンツDto
     */
    private List<OptionContentDto> optionContent;
}
