package jp.co.itechh.quad.ddd.usecase.query.batchmanagement;

import java.util.List;

/**
 * バッチ管理クエリ―クラス
 *
 * @author Pham Quang Dieu (VTI Japan Co., Ltd.)
 */
public interface BatchManagementQuery {

    /**
     * バッチ管理検索
     *
     * @param condition バッチ管理クエリ―条件
     * @return バッチ管理クエリ―モデルリスト
     */
    List<BatchManagementQueryModel> find(BatchManagementQueryCondition condition);

    /**
     * 合計レコーダーを数える
     *
     * @param condition バッチ管理クエリ―条件
     * @return 合計記録
     */
    long count(BatchManagementQueryCondition condition);
}
