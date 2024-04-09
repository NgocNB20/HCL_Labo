package jp.co.itechh.quad.ddd.usecase.order;

import jp.co.itechh.quad.core.dto.csv.OptionContentDto;
import jp.co.itechh.quad.ddd.usecase.query.ordersearch.OrderSearchCsvOptionQueryModel;
import jp.co.itechh.quad.ordersearch.presentation.api.param.OptionContent;
import jp.co.itechh.quad.ordersearch.presentation.api.param.OrderSearchCsvOptionUpdateRequest;
import org.apache.commons.collections.CollectionUtils;
import org.springframework.stereotype.Component;

import java.util.ArrayList;
import java.util.List;

/**
 * 受注検索CSVオプション ユースケースヘルパー
 *
 * @author Doan Thang (VTI Japan Co., Ltd.)
 */
@Component
public class OrderSearchCsvOptionUseCaseHelper {

    /**
     * 注検索CSVオプションクエリモデルへ変換
     *
     * @param updateRequest 受注検索CSVDLオプションの更新リクエスト
     * @return 注検索CSVオプションクエリモデル
     */
    protected OrderSearchCsvOptionQueryModel toOrderSearchOptionCsvQueryModel(OrderSearchCsvOptionUpdateRequest updateRequest) {
        OrderSearchCsvOptionQueryModel orderSearchCsvOptionQueryModel = new OrderSearchCsvOptionQueryModel();

        // クエリモデルに設定
        orderSearchCsvOptionQueryModel.set_id(updateRequest.getOptionId());
        orderSearchCsvOptionQueryModel.setDefaultOptionName(updateRequest.getDefaultOptionName());
        orderSearchCsvOptionQueryModel.setOptionName(updateRequest.getOptionName());
        orderSearchCsvOptionQueryModel.setOutHeader(updateRequest.getOutHeader());

        if (CollectionUtils.isNotEmpty(updateRequest.getOptionContent())) {
            List<OptionContentDto> optionContentList = new ArrayList<>();
            for (OptionContent optionContentRequest : updateRequest.getOptionContent()) {
                OptionContentDto optionContentModel = new OptionContentDto();
                optionContentModel.setItemName(optionContentRequest.getItemName());
                optionContentModel.setDefaultColumnLabel(optionContentRequest.getDefaultColumnLabel());
                optionContentModel.setColumnLabel(optionContentRequest.getColumnLabel());
                optionContentModel.setOutFlag(optionContentRequest.getOutFlag());
                optionContentModel.setOrder(optionContentRequest.getOrder());
                optionContentModel.setDefaultOrder(optionContentRequest.getDefaultOrder());
                optionContentList.add(optionContentModel);
            }
            orderSearchCsvOptionQueryModel.setOptionContent(optionContentList);
        }
        return orderSearchCsvOptionQueryModel;
    }
}