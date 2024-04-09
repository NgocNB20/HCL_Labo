package jp.co.itechh.quad.ddd.usecase.query.accesskeywords;

import org.springframework.data.util.CloseableIterator;

import java.util.List;

/**
 * 検索キーワード集計クエリ―クラス
 *
 * @author Pham Quang Dieu (VTI Japan Co., Ltd.)
 */
public interface AccessKeywordsQuery {

    /**
     * 検索キーワード集計最大
     */
    int MAX_ACCESS_KEYWORD = 100;

    /**
     * 検索キーワード集計検索
     *
     * @param condition 検索キーワード集計
     * @return 検索キーワード集計クエリーモデルリスト
     */
    List<AccessKeywordsQueryModel> find(AccessKeywordsQueryCondition condition);

    /**
     * 検索キーワード集計ダウンロード
     *
     * @param condition 検索キーワード集計
     * @return 検索キーワード集計クエリーモデル CloseableIterator
     */
    CloseableIterator<AccessKeywordsQueryModel> download(AccessKeywordsQueryCondition condition);
}
