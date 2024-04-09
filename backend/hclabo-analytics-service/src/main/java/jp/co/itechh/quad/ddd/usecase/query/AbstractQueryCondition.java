package jp.co.itechh.quad.ddd.usecase.query;

import jp.co.itechh.quad.ddd.infrastructure.utility.PageInfoForQuery;
import lombok.Data;

import java.io.Serializable;

/**
 * 基底クエリ―クラス
 *
 * @author Pham Quang Dieu (VTI Japan Co., Ltd.)
 */
@Data
public abstract class AbstractQueryCondition implements Serializable {

    /**
     * シリアルバージョンUID
     */
    private static final long serialVersionUID = 1L;

    /**
     * ページ情報クエリ―クラス
     */
    private PageInfoForQuery pageInfoForQuery;
}
