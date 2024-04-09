package jp.co.itechh.quad.ddd.infrastructure.customize.adapter;

import jp.co.itechh.quad.core.web.HeaderParamsUtility;
import jp.co.itechh.quad.ddd.domain.customize.adapter.IExaminationAdapter;
import jp.co.itechh.quad.examination.presentation.api.ExaminationApi;
import jp.co.itechh.quad.examination.presentation.api.param.RegistExamKitRequest;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.List;

/**
 * 検査アダプター
 *
 * @author Hoang Khanh Nam (VTI Japan Co., Ltd.)
 */
@Component
public class ExaminationAdapterImpl implements IExaminationAdapter {

    private final ExaminationApi examinationApi;

    /** コンストラクタ */
    @Autowired
    public ExaminationAdapterImpl(ExaminationApi examinationApi, HeaderParamsUtility headerParamsUtil) {
        this.examinationApi = examinationApi;

        // サービス間のAPI呼び出す時、ヘッダーにパラメーターを設定する
        headerParamsUtil.setHeader(this.examinationApi.getApiClient());
    }

    /**
     * 検査キット登録
     *
     * @param orderCode       受注番号
     * @param orderItemIdList 注文商品IDリスト
     */
    @Override
    public void registExamKit(String orderCode, List<String> orderItemIdList) {
        RegistExamKitRequest registExamKitRequest = new RegistExamKitRequest();
        registExamKitRequest.setOrderCode(orderCode);
        registExamKitRequest.setOrderItemIdList(orderItemIdList);

        examinationApi.registExamKit(registExamKitRequest);
    }
}
