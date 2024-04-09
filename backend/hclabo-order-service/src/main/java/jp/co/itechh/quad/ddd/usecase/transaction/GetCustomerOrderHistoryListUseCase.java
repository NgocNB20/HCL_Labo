/*
 * Copyright (C) 2022 i-TEC HANKYU HANSHIN INC. All Rights Reserved.
 */

package jp.co.itechh.quad.ddd.usecase.transaction;

import jp.co.itechh.quad.core.base.utility.ApplicationContextUtility;
import jp.co.itechh.quad.core.base.utility.ApplicationLogUtility;
import jp.co.itechh.quad.core.web.PageInfoModule;
import jp.co.itechh.quad.ddd.exception.AssertChecker;
import jp.co.itechh.quad.ddd.exception.DomainException;
import jp.co.itechh.quad.ddd.usecase.transaction.query.CustomerOrderHistoryModel;
import jp.co.itechh.quad.ddd.usecase.transaction.query.CustomerOrderHistoryQueryCondition;
import jp.co.itechh.quad.ddd.usecase.transaction.query.CustomerOrderHistoryQueryModel;
import jp.co.itechh.quad.ddd.usecase.transaction.query.IGetCustomerOrderHistoryListQuery;
import jp.co.itechh.quad.orderreceived.presentation.api.param.PageInfoRequest;
import jp.co.itechh.quad.orderreceived.presentation.api.param.PageInfoResponse;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.lang.reflect.InvocationTargetException;
import java.util.List;

/**
 * 顧客注文履歴一覧取得 ユースケース
 */
@Service
public class GetCustomerOrderHistoryListUseCase {

    /** 顧客注文履歴一覧取得クエリ */
    private final IGetCustomerOrderHistoryListQuery orderProcessHistoryListQuery;

    /** コンストラクタ */
    @Autowired
    public GetCustomerOrderHistoryListUseCase(IGetCustomerOrderHistoryListQuery orderProcessHistoryListQuery) {
        this.orderProcessHistoryListQuery = orderProcessHistoryListQuery;
    }

    /**
     * 顧客注文履歴一覧取得
     *
     * @param customerId      顧客ID
     * @param pageInfoRequest ページ情報リクエスト
     * @return 顧客注文履歴一覧用Model
     */
    public CustomerOrderHistoryModel getCustomerOrderHistoryList(String customerId, PageInfoRequest pageInfoRequest) {

        // アサートチェック
        AssertChecker.assertNotEmpty("customerId is null", customerId);

        // コンディションを設定
        CustomerOrderHistoryQueryCondition conditionDto = new CustomerOrderHistoryQueryCondition();

        // ページャー情報がリクエストに設定されている場合
        PageInfoResponse pageInfoResponse = null;

        // 顧客注文履歴一覧取得
        List<CustomerOrderHistoryQueryModel> orderHistoryList = null;

        if (pageInfoRequest != null) {
            // ページング検索セットアップ
            PageInfoModule pageInfoModule = ApplicationContextUtility.getBean(PageInfoModule.class);
            pageInfoModule.setupPageInfo(conditionDto, pageInfoRequest.getPage(), pageInfoRequest.getLimit(),
                                         pageInfoRequest.getOrderBy(), pageInfoRequest.getSort()
                                        );
            orderHistoryList = orderProcessHistoryListQuery.getCustomerOrderHistoryList(customerId,
                                                                                        conditionDto.getPageInfo()
                                                                                                    .getSelectOptions()
                                                                                       );
            // ページ情報レスポンスを設定
            pageInfoResponse = new PageInfoResponse();
            try {
                pageInfoModule.setupResponsePager(conditionDto, pageInfoResponse);
            } catch (InvocationTargetException | IllegalAccessException e) {
                // 例外ログを出力しておく
                ApplicationLogUtility appLogUtility = ApplicationContextUtility.getBean(ApplicationLogUtility.class);
                appLogUtility.writeExceptionLog(e);
                // 業務エラー
                throw new DomainException("ORDER-PGER0001-E");
            }

        }

        CustomerOrderHistoryModel result = new CustomerOrderHistoryModel();
        result.setCustomerOrderHistoryQueryModelList(orderHistoryList);
        result.setPageInfoResponse(pageInfoResponse);

        return result;
    }
}