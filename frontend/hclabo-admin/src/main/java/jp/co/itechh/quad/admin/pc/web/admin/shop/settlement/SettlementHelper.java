package jp.co.itechh.quad.admin.pc.web.admin.shop.settlement;

import jp.co.itechh.quad.admin.base.utility.ApplicationContextUtility;
import jp.co.itechh.quad.admin.constant.type.HTypeBillType;
import jp.co.itechh.quad.admin.constant.type.HTypeOpenDeleteStatus;
import jp.co.itechh.quad.admin.constant.type.HTypeSettlementMethodCommissionType;
import jp.co.itechh.quad.admin.constant.type.HTypeSettlementMethodType;
import jp.co.itechh.quad.admin.entity.shop.settlement.SettlementMethodEntity;
import jp.co.itechh.quad.admin.util.common.EnumTypeUtil;
import jp.co.itechh.quad.method.presentation.api.param.PaymentMethodListUpdateRequest;
import jp.co.itechh.quad.method.presentation.api.param.PaymentMethodResponse;
import jp.co.itechh.quad.method.presentation.api.param.PaymentUpdateRequest;
import org.springframework.stereotype.Component;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

/**
 * SettlementHelper Class
 *
 * @author Pham Quang Dieu (VTI Japan Co., Ltd.)
 *
 */
@Component
public class SettlementHelper {

    /**
     * 検索結果画面反映<br/>
     *
     * @param paymentMethodResponses 決済方法設定ページ
     */
    public List<SettlementMethodEntity> toSettlementMethodEntity(List<PaymentMethodResponse> paymentMethodResponses) {
        List<SettlementMethodEntity> resultItems = new ArrayList<>();

        Integer orderDisplay = 1;
        for (PaymentMethodResponse settlementMethod : paymentMethodResponses) {
            SettlementMethodEntity resultItem = ApplicationContextUtility.getBean(SettlementMethodEntity.class);
            resultItem.setBillType(EnumTypeUtil.getEnumFromValue(HTypeBillType.class, settlementMethod.getBillType()));
            resultItem.setDeliveryMethodSeq(settlementMethod.getDeliveryMethodSeq());
            resultItem.setOpenStatusPC(EnumTypeUtil.getEnumFromValue(HTypeOpenDeleteStatus.class,
                                                                     settlementMethod.getOpenStatusPC()
                                                                    ));
            resultItem.setOrderDisplay(orderDisplay);
            resultItem.setSettlementMethodCommissionType(
                            EnumTypeUtil.getEnumFromValue(HTypeSettlementMethodCommissionType.class,
                                                          settlementMethod.getSettlementMethodCommissionType()
                                                         ));
            resultItem.setSettlementMethodName(settlementMethod.getSettlementMethodName());
            resultItem.setSettlementMethodSeq(settlementMethod.getSettlementMethodSeq());
            resultItem.setSettlementMethodType(EnumTypeUtil.getEnumFromValue(HTypeSettlementMethodType.class,
                                                                             settlementMethod.getSettlementMethodType()
                                                                            ));
            resultItem.setDeliveryMethodSeq((settlementMethod.getDeliveryMethodSeq()));
            resultItems.add(resultItem);
            orderDisplay++;
        }
        return resultItems;
    }

    /**
     * 表示順変更<br/>
     *
     * @param settlementModel 決済方法設定ページ
     * @param actiontype 移動アクションタイプ 0=1つ上へ移動、1=1つ下へ移動、2=先頭へ移動、3=末尾へ移動
     */
    public void changeDisplay(SettlementModel settlementModel, int actiontype) {
        // 移動対象表示順
        Integer orderDisplay = settlementModel.getOrderDisplay();

        // 決済方法アイテムリスト
        List<SettlementModelItem> resultItems = settlementModel.getResultItems();

        // 移動先表示順を設定
        int nextOrderDisplay = 0;
        switch (actiontype) {
            case 0:
                nextOrderDisplay = orderDisplay - 1;
                break;
            case 1:
                nextOrderDisplay = orderDisplay + 1;
                break;
            case 2:
                nextOrderDisplay = 1;
                break;
            default:
                nextOrderDisplay = resultItems.size();
                break;
        }

        // リストサイズより大きな表示順は存在しないので処理終了
        if (orderDisplay > resultItems.size() || nextOrderDisplay > resultItems.size() || orderDisplay < 1
            || nextOrderDisplay < 1) {
            return;
        }

        // 決済方法アイテム移動
        SettlementModelItem moveItem = resultItems.remove(orderDisplay - 1);
        resultItems.add(nextOrderDisplay - 1, moveItem);

        // 表示順再設定
        int orderIndex = 1;
        for (SettlementModelItem resultItem : resultItems) {
            resultItem.setOrderDisplayValue(orderIndex);
            orderIndex++;
        }
        settlementModel.setOrderDisplay(nextOrderDisplay);
    }

    /**
     * 決済方法リスト取得<br />
     * @param settlementModel 決済方法設定ページ
     * @return 決済方法リスト
     */
    public PaymentMethodListUpdateRequest getSettlementMethodEntityList(SettlementModel settlementModel) {
        List<PaymentUpdateRequest> paymentUpdateRequests = new ArrayList<>();

        // 決済方法アイテムリスト
        List<SettlementModelItem> resultItems = settlementModel.getResultItems();

        for (SettlementModelItem resultItem : resultItems) {
            PaymentUpdateRequest paymentMethodResponse = new PaymentUpdateRequest();
            paymentMethodResponse.setOrderDisplayValue(resultItem.getOrderDisplayValue());
            paymentMethodResponse.setSettlementMethodSeq(resultItem.getSettlementMethodSeq());
            paymentUpdateRequests.add(paymentMethodResponse);
        }
        PaymentMethodListUpdateRequest paymentMethodListUpdateRequest = new PaymentMethodListUpdateRequest();
        paymentMethodListUpdateRequest.setPaymentMethodListUpdate(paymentUpdateRequests);
        return paymentMethodListUpdateRequest;
    }

    /**
     * 検索結果画面反映<br/>
     *
     * @param settlementModel 決済方法設定ページ
     * @param resultList 決済方法リスト
     */
    public void toPage(SettlementModel settlementModel, List<SettlementMethodEntity> resultList,
                        Map<Integer, String> deliveryMethodMap) {
        List<SettlementModelItem> resultItems = new ArrayList<>();

        Integer orderDisplay = 1;
        for (SettlementMethodEntity settlementMethod : resultList) {
            SettlementModelItem resultItem = ApplicationContextUtility.getBean(SettlementModelItem.class);
            resultItem.setBillType(settlementMethod.getBillType());
            resultItem.setDeliveryMethodSeq(settlementMethod.getDeliveryMethodSeq());
            resultItem.setOpenStatusPc(settlementMethod.getOpenStatusPC());
            resultItem.setOrderDisplayValue(orderDisplay);
            resultItem.setSettlementMethodCommissionType(settlementMethod.getSettlementMethodCommissionType());
            resultItem.setSettlementMethodName(settlementMethod.getSettlementMethodName());
            resultItem.setSettlementMethodSeq(settlementMethod.getSettlementMethodSeq());
            resultItem.setSettlementMethodType(settlementMethod.getSettlementMethodType());
            resultItem.setDeliveryMethodName(deliveryMethodMap.get(settlementMethod.getDeliveryMethodSeq()));
            resultItems.add(resultItem);
            orderDisplay++;
        }

        settlementModel.setResultItems(resultItems);
    }
}