package jp.co.itechh.quad.ddd.usecase.order;

import jp.co.itechh.quad.core.dto.csv.OptionContentDto;
import jp.co.itechh.quad.core.dto.order.OrderCSVDto;
import jp.co.itechh.quad.core.util.common.CsvOptionUtil;
import jp.co.itechh.quad.ddd.usecase.query.ordersearch.IOrderSearchCsvOptionQuery;
import jp.co.itechh.quad.ddd.usecase.query.ordersearch.OrderSearchCsvOptionQueryModel;
import jp.co.itechh.quad.ordersearch.presentation.api.param.OrderSearchCsvOptionUpdateRequest;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

/**
 * 受注検索CSVオプション ユースケース
 *
 * @author Doan Thang (VTI Japan Co., Ltd.)
 */
@Service
public class OrderSearchCsvOptionUseCase {

    /**
     * 注検索CSVオプション クエリ
     */
    private final IOrderSearchCsvOptionQuery csvOptionQuery;

    /**
     * 注検索CSVオプション ユースケースヘルパー
     */
    private final OrderSearchCsvOptionUseCaseHelper helper;

    /**
     * コンストラクタ
     *
     * @param csvOptionQuery 注検索CSVオプション クエリ
     * @param helper         注検索CSVオプション ユースケースヘルパー
     */
    @Autowired
    public OrderSearchCsvOptionUseCase(IOrderSearchCsvOptionQuery csvOptionQuery,
                                       OrderSearchCsvOptionUseCaseHelper helper) {
        this.csvOptionQuery = csvOptionQuery;
        this.helper = helper;
    }

    /**
     * 注検索CSVオプション更新
     *
     * @param optionRegistRequest 受注検索CSVDLオプションの更新リクエスト
     */
    public void updateOrderSearchCsvOption(OrderSearchCsvOptionUpdateRequest optionRegistRequest) {

        OrderSearchCsvOptionQueryModel orderSearchCsvOptionQueryModel =
                        helper.toOrderSearchOptionCsvQueryModel(optionRegistRequest);

        csvOptionQuery.update(orderSearchCsvOptionQueryModel);
    }

    /**
     * 注検索CSVオプション取得
     *
     * @return 注検索CSVオプションクエリモデルリスト
     */
    public List<OrderSearchCsvOptionQueryModel> getOrderSearchCsvOption() {
        return csvOptionQuery.get();
    }

    /**
     * オプションIdで注検索CSVオプション取得
     *
     * @param optionId 注検索CSVオプションId
     * @return 注検索CSVオプションクエリモデル
     */
    public OrderSearchCsvOptionQueryModel getOrderSearchCsvOptionById(String optionId) {
        return csvOptionQuery.getById(optionId);
    }

    /**
     * 注検索CSVオプション初期化（立ち上がり時のみ使用）
     */
    public void initialOrderSearchCsvOption() {
        List<OptionContentDto> contentDtoList = CsvOptionUtil.getListOptionContentDto(OrderCSVDto.class);
        for (int i = 1; i <= 10; i++) {
            OrderSearchCsvOptionQueryModel queryModel = new OrderSearchCsvOptionQueryModel();
            queryModel.setDefaultOptionName("テンプレート " + i);
            queryModel.setOptionName("テンプレート " + i);
            queryModel.setOutHeader(true);
            queryModel.setOptionContent(contentDtoList);
            csvOptionQuery.initial(queryModel);
        }
    }
}