package jp.co.itechh.quad.ddd.usecase.batchmanagement;

import jp.co.itechh.quad.ddd.usecase.query.batchmanagement.BatchManagementQuery;
import jp.co.itechh.quad.ddd.usecase.query.batchmanagement.BatchManagementQueryCondition;
import jp.co.itechh.quad.ddd.usecase.query.batchmanagement.BatchManagementQueryModel;
import org.springframework.stereotype.Service;

import java.util.List;

/**
 * バッチ管理ユーザケース
 *
 * @author Pham Quang Dieu (VTI Japan Co., Ltd.)
 */
@Service
public class BatchManagementUseCase {

    /**
     * バッチ管理クエリ―
     */
    private final BatchManagementQuery batchManagementQuery;

    /**
     * コンストラクタ
     *
     * @param batchManagementQuery バッチ管理クエリ―
     */
    public BatchManagementUseCase(BatchManagementQuery batchManagementQuery) {
        this.batchManagementQuery = batchManagementQuery;
    }

    /**
     * バッチ管理検索一覧
     *
     * @param condition バッチ管理クエリ―条件
     * @return バッチ管理クエリ―モデルリスト
     */
    public List<BatchManagementQueryModel> get(BatchManagementQueryCondition condition) {
        return batchManagementQuery.find(condition);
    }

    /**
     * 合計レコーダーを数える
     *
     * @param condition バッチ管理クエリ―条件
     */
    public long count(BatchManagementQueryCondition condition) {
        return batchManagementQuery.count(condition);
    }
}
