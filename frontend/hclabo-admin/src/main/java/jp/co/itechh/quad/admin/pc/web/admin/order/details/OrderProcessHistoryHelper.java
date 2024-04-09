/*
 * Project Name : HIT-MALL3
 *
 * Copyright (C) 2008 i-TEC HANKYU HANSHIN INC. All Rights Reserved.
 *
 */

package jp.co.itechh.quad.admin.pc.web.admin.order.details;

import jp.co.itechh.quad.admin.base.utility.ApplicationContextUtility;
import jp.co.itechh.quad.admin.base.utility.ConversionUtility;
import jp.co.itechh.quad.admin.constant.type.HTypeProcessType;
import jp.co.itechh.quad.transaction.presentation.api.param.ProcessHistory;
import org.springframework.stereotype.Component;

import java.util.ArrayList;
import java.util.List;

/**
 * 処理履歴一覧Helper
 *
 * @author kimura
 */
@Component
public class OrderProcessHistoryHelper {

    /** 変換ユーティリティクラス */
    private final ConversionUtility conversionUtility;

    /** コンストラクタ */
    public OrderProcessHistoryHelper(ConversionUtility conversionUtility) {
        this.conversionUtility = conversionUtility;
    }

    /**
     * モデルに設定<br/>
     *
     * @param orderProcesshistoryModel   処理履歴一覧モデル
     * @param processHistoryResponseList 受注処理履歴レスポンスリストレスポンス
     */
    public void toPageItems(OrderProcessHistoryModel orderProcesshistoryModel,
                            List<ProcessHistory> processHistoryResponseList) {

        List<OrderProcessHistoryModelItem> itemList = new ArrayList<>();
        Integer orderVersionNo = 1;
        for (ProcessHistory response : processHistoryResponseList) {
            OrderProcessHistoryModelItem item = ApplicationContextUtility.getBean(OrderProcessHistoryModelItem.class);

            // 日付昇順でソートされたリストが渡されるので、一覧情報はリスト順にセット
            item.setOrderVersionNo(orderVersionNo);
            item.setProcessPersonName(response.getProcessPersonName());
            item.setProcessTime(this.conversionUtility.toTimestamp(response.getProcessTime()));
            item.setProcessType(judgeProcessType(response.getProcessType()));
            itemList.add(item);
            orderVersionNo++;
        }
        orderProcesshistoryModel.setProcessHistoryModelItems(itemList);
    }

    /**
     * 処理種別：列挙型を判定し返却
     *
     * @param processType 処理種別
     * @return HTypeProcessType 処理種別区分値
     */
    private HTypeProcessType judgeProcessType(String processType) {

        if (HTypeProcessType.ORDER.getValue().equals(processType)) {
            return HTypeProcessType.ORDER;
        } else if (HTypeProcessType.CHANGE.getValue().equals(processType)) {
            return HTypeProcessType.CHANGE;
        } else if (HTypeProcessType.CANCELLATION.getValue().equals(processType)) {
            return HTypeProcessType.CANCELLATION;
        } else if (HTypeProcessType.SHIPMENT.getValue().equals(processType)) {
            return HTypeProcessType.SHIPMENT;
        } else if (HTypeProcessType.PAYMENT.getValue().equals(processType)) {
            return HTypeProcessType.PAYMENT;
        } else if (HTypeProcessType.REPAYMENT.getValue().equals(processType)) {
            return HTypeProcessType.REPAYMENT;
        } else if (HTypeProcessType.SEND_MAIL.getValue().equals(processType)) {
            return HTypeProcessType.SEND_MAIL;
        } else if (HTypeProcessType.REMINDER_SEND_MAIL.getValue().equals(processType)) {
            return HTypeProcessType.REMINDER_SEND_MAIL;
        } else if (HTypeProcessType.EXPIRED_SEND_MAIL.getValue().equals(processType)) {
            return HTypeProcessType.EXPIRED_SEND_MAIL;
        } else if (HTypeProcessType.NOVELTY_BATCH.getValue().equals(processType)) {
            return HTypeProcessType.NOVELTY_BATCH;
        }

        return null;
    }

}