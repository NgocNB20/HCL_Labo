package jp.co.itechh.quad.ddd.domain.customize.adapter;

import java.util.List;

/**
 * 検査アダプター
 *
 * @author Hoang Khanh Nam (VTI Japan Co., Ltd.)
 */
public interface IExaminationAdapter {

    /**
     * 検査キット登録
     *
     * @param orderCode       受注番号
     * @param orderItemIdList 注文商品IDリスト
     */
    void registExamKit(String orderCode, List<String> orderItemIdList);
}
