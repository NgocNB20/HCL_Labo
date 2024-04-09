/*
 * Copyright (C) 2022 i-TEC HANKYU HANSHIN INC. All Rights Reserved.
 */

package jp.co.itechh.quad.ddd.infrastructure.transaction.query;

import jp.co.itechh.quad.billingslip.presentation.api.BillingSlipApi;
import jp.co.itechh.quad.billingslip.presentation.api.param.BillingSlipGetRequest;
import jp.co.itechh.quad.billingslip.presentation.api.param.BillingSlipResponse;
import jp.co.itechh.quad.ddd.domain.transaction.entity.TransactionEntity;
import jp.co.itechh.quad.ddd.domain.transaction.repository.ITransactionRepository;
import jp.co.itechh.quad.ddd.domain.transaction.valueobject.TransactionId;
import jp.co.itechh.quad.ddd.exception.DomainException;
import jp.co.itechh.quad.ddd.infrastructure.transaction.dao.OrderReceivedDao;
import jp.co.itechh.quad.ddd.infrastructure.transaction.dto.CustomerOrderHistoryBaseInfoDto;
import jp.co.itechh.quad.ddd.usecase.transaction.query.CustomerOrderHistoryQueryModel;
import jp.co.itechh.quad.ddd.usecase.transaction.query.IGetCustomerOrderHistoryListQuery;
import org.apache.commons.collections.CollectionUtils;
import org.seasar.doma.jdbc.SelectOptions;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.ArrayList;
import java.util.List;

/**
 * 受注処理履歴一覧取得クエリ 実装クラス
 */
@Component
public class GetCustomerOrderHistoryListQueryImpl implements IGetCustomerOrderHistoryListQuery {

    /** 受注Daoクラス */
    private final OrderReceivedDao orderReceivedDao;

    /** 請求伝票API */
    private final BillingSlipApi billingSlipApi;

    /** Helper */
    private final GetCustomerOrderHistoryListQueryHelper helper;

    /** 取引リポジトリ */
    private final ITransactionRepository transactionRepository;

    /** コンストラクタ */
    @Autowired
    public GetCustomerOrderHistoryListQueryImpl(OrderReceivedDao orderReceivedDao,
                                                BillingSlipApi billingSlipApi,
                                                GetCustomerOrderHistoryListQueryHelper helper,
                                                ITransactionRepository transactionRepository) {
        this.orderReceivedDao = orderReceivedDao;
        this.billingSlipApi = billingSlipApi;
        this.helper = helper;
        this.transactionRepository = transactionRepository;
    }

    /**
     * 顧客注文履歴一覧取得
     *
     * @param customerId
     * @return CustomerOrderHistoryQueryModelリスト
     */
    @Override
    public List<CustomerOrderHistoryQueryModel> getCustomerOrderHistoryList(String customerId,
                                                                            SelectOptions selectOptions) {

        // 戻り値
        List<CustomerOrderHistoryQueryModel> customerOrderHistoryQueryModelList = new ArrayList<>();

        // 受注/注文情報リスト取得
        List<CustomerOrderHistoryBaseInfoDto> customerOrderHistoryBaseInfoDtoList =
                        orderReceivedDao.getCustomerOrderHistoryList(customerId, selectOptions);

        if (CollectionUtils.isNotEmpty(customerOrderHistoryBaseInfoDtoList)) {
            for (CustomerOrderHistoryBaseInfoDto customerOrderHistoryBaseInfoDto : customerOrderHistoryBaseInfoDtoList) {

                BillingSlipGetRequest billingSlipGetRequest = new BillingSlipGetRequest();
                billingSlipGetRequest.setTransactionId(customerOrderHistoryBaseInfoDto.getTransactionId());
                // 請求伝票取得
                BillingSlipResponse billingSlipResponse = billingSlipApi.get(billingSlipGetRequest);
                if (billingSlipResponse == null) {
                    throw new DomainException("ORDER-BILS0001-E",
                                              new String[] {customerOrderHistoryBaseInfoDto.getTransactionId()}
                    );
                }

                CustomerOrderHistoryQueryModel customerOrderHistoryQueryModel = null;

                // 取引を取得
                TransactionEntity transactionEntity = this.transactionRepository.get(
                    new TransactionId(customerOrderHistoryBaseInfoDto.getTransactionId()));

                if (transactionEntity != null) {
                    customerOrderHistoryQueryModel =
                        helper.toCustomerOrderHistoryQueryModel(customerOrderHistoryBaseInfoDto,
                            billingSlipResponse,
                            transactionEntity.isPreClaimFlag());
                }

                customerOrderHistoryQueryModelList.add(customerOrderHistoryQueryModel);
            }
        }

        return customerOrderHistoryQueryModelList;
    }
}