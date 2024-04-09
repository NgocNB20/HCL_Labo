/*
 * Copyright (C) 2022 i-TEC HANKYU HANSHIN INC. All Rights Reserved.
 */

package jp.co.itechh.quad.ddd.infrastructure.transaction.query;

import jp.co.itechh.quad.core.constant.type.HTypeProcessType;
import jp.co.itechh.quad.core.util.common.EnumTypeUtil;
import jp.co.itechh.quad.ddd.infrastructure.transaction.dto.OrderProcessHistoryDto;
import jp.co.itechh.quad.ddd.usecase.transaction.query.OrderProcessHistoryQueryModel;
import org.apache.commons.collections.CollectionUtils;
import org.springframework.stereotype.Component;

import java.util.ArrayList;
import java.util.List;

/**
 * 受注処理履歴一覧 Helperクラス
 */
@Component
public class GetOrderProcessHistoryListQueryHelper {

    /**
     * 受注処理履歴一覧用 QueryModelに変換
     *
     * @param orderProcessHistoryDtoList
     * @return List<CustomerOrderHistoryQueryModel>
     */
    public List<OrderProcessHistoryQueryModel> toOrderProcessHistoryQueryModelList(List<OrderProcessHistoryDto> orderProcessHistoryDtoList) {

        // 戻り値
        List<OrderProcessHistoryQueryModel> orderProcessHistoryQueryModelList = new ArrayList<>();

        if (CollectionUtils.isNotEmpty(orderProcessHistoryDtoList)) {
            for (OrderProcessHistoryDto orderProcessHistoryDto : orderProcessHistoryDtoList) {

                OrderProcessHistoryQueryModel orderProcessHistoryQueryModel = new OrderProcessHistoryQueryModel();
                orderProcessHistoryQueryModel.setTransactionId(orderProcessHistoryDto.getTransactionId());
                orderProcessHistoryQueryModel.setProcessTime(orderProcessHistoryDto.getProcessTime());
                orderProcessHistoryQueryModel.setProcessType(EnumTypeUtil.getEnumFromValue(HTypeProcessType.class,
                                                                                           orderProcessHistoryDto.getProcessType()
                                                                                          ));
                orderProcessHistoryQueryModel.setProcessPersonName(orderProcessHistoryDto.getProcessPersonName());

                orderProcessHistoryQueryModelList.add(orderProcessHistoryQueryModel);
            }
        }

        return orderProcessHistoryQueryModelList;
    }

}