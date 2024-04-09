package jp.co.itechh.quad.ddd.presentation.batchmanagement.api;

import io.swagger.annotations.ApiParam;
import jp.co.itechh.quad.batchmanagement.presentation.api.BatchesApi;
import jp.co.itechh.quad.batchmanagement.presentation.api.param.BatchManagementGetRequest;
import jp.co.itechh.quad.batchmanagement.presentation.api.param.BatchManagementListResponse;
import jp.co.itechh.quad.batchmanagement.presentation.api.param.PageInfoRequest;
import jp.co.itechh.quad.ddd.exception.DomainException;
import jp.co.itechh.quad.ddd.usecase.batchmanagement.BatchManagementUseCase;
import jp.co.itechh.quad.ddd.usecase.query.batchmanagement.BatchManagementQueryCondition;
import jp.co.itechh.quad.ddd.usecase.query.batchmanagement.BatchManagementQueryModel;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.RestController;

import javax.validation.Valid;
import java.util.List;

/**
 * バッチ管理検索一覧エンドポイント Controller
 *
 * @author Pham Quang Dieu (VTI Japan Co., Ltd.)
 */
@RestController
public class BatchManagementController implements BatchesApi {

    /**
     * ロガー
     */
    private static final Logger LOGGER = LoggerFactory.getLogger(BatchManagementController.class);

    /**
     * バッチ管理ユーザケース
     */
    private final BatchManagementUseCase useCase;

    /**
     * バッチ管理検索一覧ヘルパー
     */
    private final BatchManagementHelper helper;

    /**
     * コンストラクタ
     *
     * @param useCase バッチ管理ユーザケース
     * @param helper  バッチ管理検索一覧ヘルパー
     */
    @Autowired
    public BatchManagementController(BatchManagementUseCase useCase, BatchManagementHelper helper) {
        this.useCase = useCase;
        this.helper = helper;
    }

    /**
     * GET /batches/management : バッチ管理検索一覧
     * バッチ管理検索一覧
     *
     * @param batchManagementGetRequest バッチ管理検索一覧リクエスト (optional)
     * @param pageInfoRequest           ページ情報リクエスト（ページネーションのため） (optional)
     * @return 成功 (status code 200)
     * or その他エラー (status code 500)
     */
    @Override
    public ResponseEntity<BatchManagementListResponse> batchManagement(@ApiParam("バッチ管理検索一覧リクエスト") @Valid
                                                                       BatchManagementGetRequest batchManagementGetRequest,
                                                                       @ApiParam("ページ情報リクエスト（ページネーションのため）")
                                                                       @Valid PageInfoRequest pageInfoRequest) {
        BatchManagementQueryCondition queryCondition =
                        helper.toBatchManagementQueryCondition(batchManagementGetRequest, pageInfoRequest);
        try {
            List<BatchManagementQueryModel> queryModelList = useCase.get(queryCondition);

            long totalRecord = useCase.count(queryCondition);

            BatchManagementListResponse response =
                            helper.toBatchManagementListResponse(queryModelList, pageInfoRequest, (int) totalRecord);
            return new ResponseEntity<>(response, HttpStatus.OK);
        } catch (Exception e) {
            LOGGER.error(e.getMessage());
            throw new DomainException("ANALYTICS-BMQC0001-E");
        }
    }
}