package jp.co.itechh.quad.ddd.infrastructure.user.adapter;

import jp.co.itechh.quad.customer.presentation.api.param.CustomerResponse;
import jp.co.itechh.quad.ddd.domain.user.adapter.model.Customer;
import org.springframework.stereotype.Component;
import org.springframework.util.ObjectUtils;

/**
 * 顧客アダプタHelperクラス
 */
@Component
public class CustomerAdapterHelper {

    /**
     * 会員に変換
     *
     * @param response 会員レスポンス
     * @return 会員
     */
    public Customer toCustomer(CustomerResponse response) {

        if (ObjectUtils.isEmpty(response)) {
            return null;
        }

        Customer customer = new Customer();

        customer.setCustomerId(response.getMemberInfoSeq());
        customer.setCustomerLastName(response.getMemberInfoLastName());
        customer.setCustomerFirstName(response.getMemberInfoFirstName());
        customer.setCustomerLastKana(response.getMemberInfoLastKana());
        customer.setCustomerFirstKana(response.getMemberInfoFirstKana());
        customer.setCustomerPhoneNumber(response.getMemberInfoTel());
        customer.setCustomerMail(response.getMemberInfoMail());
        customer.setCustomerBirthday(response.getMemberInfoBirthday());
        customer.setCustomerSex(response.getMemberInfoSex());
        customer.setCustomerInfoAddressId(response.getMemberInfoAddressId());

        return customer;
    }
}
