package jp.co.itechh.quad.ddd.domain.customize.adapter;

import jp.co.itechh.quad.ddd.domain.customize.adapter.model.ExamKit;

import java.util.List;

/**
 * 検査アダプター
 *
 * @author Hoang Khanh Nam (VTI Japan Co., Ltd.)
 */
public interface IExaminationAdapter {

    /**
     * 検査キット取得
     *
     * @param orderItemIdList 注文商品IDリスト
     * @return 検査キットリスト
     */
    List<ExamKit> getExamKitList(List<String> orderItemIdList);
}
