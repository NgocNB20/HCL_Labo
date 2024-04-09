package jp.co.itechh.quad.ddd.infrastructure.customize.adapter;

import jp.co.itechh.quad.ddd.domain.customize.adapter.IExaminationAdapter;
import jp.co.itechh.quad.ddd.domain.customize.adapter.model.ExamKit;
import jp.co.itechh.quad.examination.presentation.api.ExaminationApi;
import jp.co.itechh.quad.examination.presentation.api.param.ExamKitListResponse;
import jp.co.itechh.quad.examination.presentation.api.param.ExamKitRequest;
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

    /**
     * 検査API
     */
    private final ExaminationApi examinationApi;

    /**
     * 検査アダプターHelper
     */
    private final ExaminationAdapterHelper examinationAdapterHelper;

    /**
     * コンストラクタ
     */
    @Autowired
    public ExaminationAdapterImpl(ExaminationApi examinationApi, ExaminationAdapterHelper examinationAdapterHelper) {
        this.examinationApi = examinationApi;
        this.examinationAdapterHelper = examinationAdapterHelper;
    }

    /**
     * 検査キット取得
     *
     * @param orderItemIdList 注文商品IDリスト
     */
    @Override
    public List<ExamKit> getExamKitList(List<String> orderItemIdList) {
        ExamKitRequest examKitRequest = new ExamKitRequest();
        examKitRequest.setOrderItemIdList(orderItemIdList);

        ExamKitListResponse examKitListResponse = examinationApi.getExamKitList(examKitRequest);
        return examinationAdapterHelper.toExamKitList(examKitListResponse.getExamKitList());
    }
}
