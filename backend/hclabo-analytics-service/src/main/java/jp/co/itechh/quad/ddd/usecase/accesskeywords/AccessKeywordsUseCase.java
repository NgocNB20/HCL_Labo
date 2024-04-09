package jp.co.itechh.quad.ddd.usecase.accesskeywords;

import jp.co.itechh.quad.ddd.usecase.query.accesskeywords.AccessKeywordsQuery;
import jp.co.itechh.quad.ddd.usecase.query.accesskeywords.AccessKeywordsQueryCondition;
import jp.co.itechh.quad.ddd.usecase.query.accesskeywords.AccessKeywordsQueryModel;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Stream;

/**
 * 検索キーワード集計ユースケース
 *
 * @author Pham Quang Dieu (VTI Japan Co., Ltd.)
 */
@Service
public class AccessKeywordsUseCase {

    /**
     * 検索キーワード集計クエリ―
     */
    private final AccessKeywordsQuery accessKeywordsQuery;

    /**
     * コンストラクタ
     *
     * @param accessKeywordsQuery 検索キーワード集計クエリ―
     */
    @Autowired
    public AccessKeywordsUseCase(AccessKeywordsQuery accessKeywordsQuery) {
        this.accessKeywordsQuery = accessKeywordsQuery;
    }

    /**
     * 検索キーワード集計検索
     *
     * @param condition 検索キーワード集計
     * @return 検索キーワード集計クエリーモデルリスト
     */
    public List<AccessKeywordsQueryModel> get(AccessKeywordsQueryCondition condition) {
        return accessKeywordsQuery.find(condition);
    }

    /**
     * 検索キーワード集計ダウンロード
     *
     * @param condition 検索キーワード集計
     * @return 検索キーワード集計クエリーモデルのStream
     */
    public Stream<AccessKeywordsQueryModel> download(AccessKeywordsQueryCondition condition) {
        return accessKeywordsQuery.download(condition).stream();
    }

}
