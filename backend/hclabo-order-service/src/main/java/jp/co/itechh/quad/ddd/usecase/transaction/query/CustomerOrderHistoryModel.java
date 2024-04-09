package jp.co.itechh.quad.ddd.usecase.transaction.query;

import jp.co.itechh.quad.orderreceived.presentation.api.param.PageInfoResponse;
import lombok.Data;

import java.util.List;

/**
 * 顧客注文履歴一覧用 Model
 */
@Data
public class CustomerOrderHistoryModel {

    /** 顧客注文履歴一覧用 QueryModelリスト */
    private List<CustomerOrderHistoryQueryModel> customerOrderHistoryQueryModelList;

    /** ページング情報 */
    private PageInfoResponse pageInfoResponse;

}
