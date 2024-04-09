package jp.co.itechh.quad.ddd.domain.user.adapter;

import jp.co.itechh.quad.ddd.domain.user.adapter.model.Customer;

/**
 * 会員 アダプター
 */
public interface ICustomerAdapter {

    /**
     * 会員情報取得情報取得
     *
     * @param memberInfoSeq
     * @return Customer
     */
    Customer getByMemberInfoSeq(Integer memberInfoSeq);
}
