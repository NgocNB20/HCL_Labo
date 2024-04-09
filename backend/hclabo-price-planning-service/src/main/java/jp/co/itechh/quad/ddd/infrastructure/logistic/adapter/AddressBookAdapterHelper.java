package jp.co.itechh.quad.ddd.infrastructure.logistic.adapter;

import jp.co.itechh.quad.addressbook.presentation.api.param.AddressBookAddressResponse;
import org.springframework.stereotype.Component;

/**
 * 住所録アダプターHelperクラス
 *
 * @author Pham Quang Dieu (VTI Japan Co., Ltd.)
 */
@Component
public class AddressBookAdapterHelper {

    /**
     * toZipcodeへ
     *
     * @param addressBookAddressResponse
     */
    public String toZipcode(AddressBookAddressResponse addressBookAddressResponse) {

        if (addressBookAddressResponse == null) {
            return null;
        }

        return addressBookAddressResponse.getZipCode();
    }
}
