package jp.co.itechh.quad.ddd.infrastructure.utility;

import lombok.Data;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;

/**
 * ページ情報クエリ―クラス
 *
 * @author Pham Quang Dieu (VTI Japan Co., Ltd.)
 */
@Data
public class PageInfoForQuery {

    /**
     * ページネーション情報
     */
    private Pageable pageable;

    /**
     * ソートオプション
     */
    private Sort sort;

    /**
     * コンストラクタ
     */
    public PageInfoForQuery() {
    }
}
