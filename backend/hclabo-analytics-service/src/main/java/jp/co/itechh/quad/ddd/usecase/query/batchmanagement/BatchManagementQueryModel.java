package jp.co.itechh.quad.ddd.usecase.query.batchmanagement;

import lombok.Data;
import org.springframework.data.mongodb.core.mapping.Field;

/**
 * バッチ管理クエリ―モデル
 *
 * @author Pham Quang Dieu (VTI Japan Co., Ltd.)
 */
@Data
public class BatchManagementQueryModel {
    @Field("batch-log")
    private BatchLogData batchLogData;
}
