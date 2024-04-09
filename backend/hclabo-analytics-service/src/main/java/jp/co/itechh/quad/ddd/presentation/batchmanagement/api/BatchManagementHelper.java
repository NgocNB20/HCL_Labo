package jp.co.itechh.quad.ddd.presentation.batchmanagement.api;

import jp.co.itechh.quad.batchmanagement.presentation.api.param.BatchManagementGetRequest;
import jp.co.itechh.quad.batchmanagement.presentation.api.param.BatchManagementListResponse;
import jp.co.itechh.quad.batchmanagement.presentation.api.param.BatchManagementResponse;
import jp.co.itechh.quad.batchmanagement.presentation.api.param.InputData;
import jp.co.itechh.quad.batchmanagement.presentation.api.param.PageInfoRequest;
import jp.co.itechh.quad.batchmanagement.presentation.api.param.PageInfoResponse;
import jp.co.itechh.quad.ddd.infrastructure.utility.PageInfoForResponse;
import jp.co.itechh.quad.ddd.infrastructure.utility.PageInfoModule;
import jp.co.itechh.quad.ddd.usecase.query.batchmanagement.BatchManagementQueryCondition;
import jp.co.itechh.quad.ddd.usecase.query.batchmanagement.BatchManagementQueryModel;
import org.apache.commons.beanutils.BeanUtils;
import org.apache.commons.lang3.StringUtils;
import org.springframework.stereotype.Component;

import java.lang.reflect.InvocationTargetException;
import java.util.ArrayList;
import java.util.List;

/**
 * バッチ管理検索一覧ヘルパー
 *
 * @author Pham Quang Dieu (VTI Japan Co., Ltd.)
 */
@Component
public class BatchManagementHelper {

    /**
     * ページ情報モジュール
     */
    private final PageInfoModule pageInfoModule;

    /**
     * サブドキュメント
     */
    private final static String SUB_DOCUMENT = "batch-log.";

    /**
     * コンストラクタ
     *
     * @param pageInfoModule ページ情報モジュール
     */
    public BatchManagementHelper(PageInfoModule pageInfoModule) {
        this.pageInfoModule = pageInfoModule;
    }

    /**
     * バッチ管理検索一覧レスポンスに変換
     *
     * @param queryModelList  バッチ管理クエリ―モデル
     * @param pageInfoRequest ページ情報リクエスト（ページネーションのため）
     * @param count           合計レコーダー
     * @return バッチ管理検索一覧レスポンス
     */
    public BatchManagementListResponse toBatchManagementListResponse(List<BatchManagementQueryModel> queryModelList,
                                                                     PageInfoRequest pageInfoRequest,
                                                                     int count)
                    throws InvocationTargetException, IllegalAccessException {
        BatchManagementListResponse listResponse = new BatchManagementListResponse();
        if (queryModelList != null) {
            listResponse.setBatchManagementList(new ArrayList<>());
            for (BatchManagementQueryModel queryModel : queryModelList) {
                BatchManagementResponse response = new BatchManagementResponse();
                response.setBatchId(queryModel.getBatchLogData().getBatchId());
                response.setBatchName(queryModel.getBatchLogData().getBatchName());

                if (queryModel.getBatchLogData().getInputData() != null) {
                    InputData inputData = new InputData();
                    inputData.setAdminId(queryModel.getBatchLogData().getInputData().getAdminId());
                    inputData.setFilePath(queryModel.getBatchLogData().getInputData().getFilePath());
                    inputData.setStartType(queryModel.getBatchLogData().getInputData().getStartType());
                    response.setInputData(inputData);
                }

                response.setStartTime(queryModel.getBatchLogData().getStartTime());
                response.setEndTime(queryModel.getBatchLogData().getEndTime());
                if (queryModel.getBatchLogData().getProcessCount() != null) {
                    response.setProcessCount(queryModel.getBatchLogData().getProcessCount());
                }
                response.setReport(queryModel.getBatchLogData().getReport());
                response.setResult(queryModel.getBatchLogData().getResult());

                listResponse.getBatchManagementList().add(response);
            }

            PageInfoForResponse pageInfoForResponse =
                            pageInfoModule.setPageInfoForResponse(pageInfoRequest.getPage(), pageInfoRequest.getLimit(),
                                                                  count
                                                                 );
            PageInfoResponse pageInfoResponse = new PageInfoResponse();
            BeanUtils.copyProperties(pageInfoResponse, pageInfoForResponse);

            listResponse.setPageInfo(pageInfoResponse);
        }
        return listResponse;
    }

    /**
     * バッチ管理クエリ―条件に変換
     *
     * @param getRequest バッチ管理検索一覧リクエスト
     * @param pageInfo   ページ情報リクエスト（ページネーションのため）
     * @return バッチ管理クエリ―条件
     */
    public BatchManagementQueryCondition toBatchManagementQueryCondition(BatchManagementGetRequest getRequest,
                                                                         PageInfoRequest pageInfo) {
        BatchManagementQueryCondition queryCondition = new BatchManagementQueryCondition();
        if (!StringUtils.isEmpty(getRequest.getBatchName())) {
            queryCondition.setBatchName(getRequest.getBatchName());
        }

        if (getRequest.getCreateTime() != null) {
            queryCondition.setProcessDateFrom(getRequest.getCreateTime());
        }

        if (getRequest.getEndTime() != null) {
            queryCondition.setProcessDateTo(getRequest.getEndTime());
        }

        if (!StringUtils.isEmpty(getRequest.getStatus())) {
            queryCondition.setStatus(getRequest.getStatus());
        }

        if (pageInfo != null) {
            if (pageInfo.getOrderBy() != null) {
                String orderBy = SUB_DOCUMENT + convertToLowerCase(pageInfo.getOrderBy());
                pageInfo.setOrderBy(orderBy);
            }
            queryCondition.setPageInfoForQuery(
                            pageInfoModule.setPageInfoForQuery(pageInfo.getPage(), pageInfo.getLimit(),
                                                               pageInfo.getOrderBy(), pageInfo.getSort()
                                                              ));
        }

        return queryCondition;
    }

    /**
     * 小文字に変換
     *
     * @param input インプット
     * @return 変換した小文字
     */
    private String convertToLowerCase(String input) {
        String regex = "([a-z])([A-Z]+)";
        String replacement = "$1-$2";
        return input.replaceAll(regex, replacement).toLowerCase();
    }
}