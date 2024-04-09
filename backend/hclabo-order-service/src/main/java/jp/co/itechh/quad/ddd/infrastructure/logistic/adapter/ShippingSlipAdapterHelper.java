package jp.co.itechh.quad.ddd.infrastructure.logistic.adapter;

import jp.co.itechh.quad.shippingslip.presentation.api.param.ShippingSlipCheckResponse;
import jp.co.itechh.quad.transaction.presentation.api.param.WarningContent;
import org.apache.commons.collections.MapUtils;
import org.apache.commons.lang3.ObjectUtils;
import org.springframework.stereotype.Component;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * 配送アダプターHelperクラス<br/>
 *
 * @author Pham Quang Dieu (VTI Japan Co., Ltd.)
 */
@Component
public class ShippingSlipAdapterHelper {

    /**
     * 警告メッセージの取引全体をチェックレスポンス
     *
     * @param shippingSlipCheckResponse 改訂用取引全体チェック警告メッセージ用レスポンス
     * @return 警告メッセージマップ
     */
    public Map<String, List<WarningContent>> setWarningMessageForTransactionCheckResponse(ShippingSlipCheckResponse shippingSlipCheckResponse) {

        if (ObjectUtils.isEmpty(shippingSlipCheckResponse) || MapUtils.isEmpty(
                        shippingSlipCheckResponse.getWarningMessage())) {
            return null;
        }

        Map<String, List<WarningContent>> warningMapForTransactionCheckResponse = new HashMap<>();

        shippingSlipCheckResponse.getWarningMessage().forEach((key, messageList) -> {
            List<WarningContent> warningContentList = new ArrayList<>();
            for (jp.co.itechh.quad.shippingslip.presentation.api.param.WarningContent content : messageList) {
                WarningContent transactionContent = new WarningContent();
                transactionContent.setCode(content.getCode());
                transactionContent.setMessage(content.getMessage());

                warningContentList.add(transactionContent);
            }
            warningMapForTransactionCheckResponse.put(key, warningContentList);
        });
        return warningMapForTransactionCheckResponse;
    }
}