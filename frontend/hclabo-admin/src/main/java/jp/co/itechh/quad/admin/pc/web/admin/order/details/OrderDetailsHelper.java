/*
 * Project Name : HIT-MALL3
 *
 * Copyright (C) 2008 i-TEC HANKYU HANSHIN INC. All Rights Reserved.
 *
 */

package jp.co.itechh.quad.admin.pc.web.admin.order.details;

import jp.co.itechh.quad.admin.base.utility.ConversionUtility;
import jp.co.itechh.quad.admin.base.utility.DateUtility;
import jp.co.itechh.quad.examination.presentation.api.param.ExamKitRequest;
import jp.co.itechh.quad.orderslip.presentation.api.param.OrderSlipResponseItemList;
import org.springframework.stereotype.Component;
import org.springframework.util.CollectionUtils;

import java.util.ArrayList;
import java.util.List;

/**
 * 受注詳細系ページクラス
 *
 * @author Pham Quang Dieu (VTI Japan Co., Ltd.)
 */
@Component
public class OrderDetailsHelper extends AbstractOrderDetailsHelper {

    /** 変換ユーティリティクラス */
    private final ConversionUtility conversionUtility;

    /** コンストラクタ */
    public OrderDetailsHelper(DateUtility dateUtility,
                              ConversionUtility conversionUtility,
                              ConversionUtility conversionUtility1) {
        super(dateUtility, conversionUtility);
        this.conversionUtility = conversionUtility1;
    }

    /**
     * 検査キットリクエストに変換
     *
     * @param itemList
     * @return 検査キットリクエスト
     */
    ExamKitRequest toExamKitRequest(List<OrderSlipResponseItemList> itemList) {

        if (CollectionUtils.isEmpty(itemList)) {
            return null;
        }

        ExamKitRequest examKitRequest = new ExamKitRequest();
        List<String> orderItemIdList = new ArrayList<>();

        itemList.forEach(item -> {
            orderItemIdList.add(item.getOrderItemId());
        });

        examKitRequest.setOrderItemIdList(orderItemIdList);

        return examKitRequest;
    }

}