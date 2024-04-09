package jp.co.itechh.quad.ddd.usecase.query.accesskeywords;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.math.BigDecimal;

/**
 * 検索キーワード集計クエリーモデル
 */
@Getter
@Setter
@NoArgsConstructor
public class AccessKeywordsQueryModel {

    /**
     * 検索キーワード
     */
    private String searchKeyword;

    /**
     * 検索回数
     */
    private Integer searchCount;

    /**
     * 検索結果数
     */
    private BigDecimal searchResultCount;
}
