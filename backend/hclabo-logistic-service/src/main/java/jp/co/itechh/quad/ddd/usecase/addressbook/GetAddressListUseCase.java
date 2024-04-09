/*
 * Copyright (C) 2022 i-TEC HANKYU HANSHIN INC. All Rights Reserved.
 */

package jp.co.itechh.quad.ddd.usecase.addressbook;

import jp.co.itechh.quad.addressbook.presentation.api.param.PageInfoRequest;
import jp.co.itechh.quad.addressbook.presentation.api.param.PageInfoResponse;
import jp.co.itechh.quad.core.base.utility.ApplicationContextUtility;
import jp.co.itechh.quad.core.web.PageInfoModule;
import jp.co.itechh.quad.customer.presentation.api.CustomerApi;
import jp.co.itechh.quad.customer.presentation.api.param.CustomerResponse;
import jp.co.itechh.quad.ddd.exception.DomainException;
import jp.co.itechh.quad.ddd.usecase.addressbook.query.IAddressBookQuery;
import jp.co.itechh.quad.ddd.usecase.addressbook.query.model.AddressModel;
import jp.co.itechh.quad.ddd.usecase.addressbook.query.model.AddressQueryCondition;
import jp.co.itechh.quad.ddd.usecase.addressbook.query.model.AddressQueryModel;
import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.lang.reflect.InvocationTargetException;
import java.util.List;

/**
 * 住所リスト取得ユースケース
 */
@Service
public class GetAddressListUseCase {

    /** ロガー */
    private static final Logger LOGGER = LoggerFactory.getLogger(GetAddressListUseCase.class);

    /** 住所録クエリー */
    private final IAddressBookQuery addressBookQuery;

    /** 会員API **/
    private final CustomerApi customerApi;

    /** コンストラクタ */
    @Autowired
    public GetAddressListUseCase(IAddressBookQuery addressBookQuery, CustomerApi customerApi) {
        this.addressBookQuery = addressBookQuery;
        this.customerApi = customerApi;
    }

    /**
     * 住所リストを取得する
     *
     * @param customerId      顧客ID
     * @param pageInfoRequest ページ情報リクエスト（ページネーションのため） (optional)
     * @return 存在する ... 住所リスト / 存在しない ... null
     */
    public AddressModel getAddressList(Boolean addressBookCustomerExclusionFlag,
                                       String customerId,
                                       PageInfoRequest pageInfoRequest) {

        // コンディションを設定
        AddressQueryCondition conditionDto = ApplicationContextUtility.getBean(AddressQueryCondition.class);
        conditionDto.setCustomerId(customerId);

        if (Boolean.TRUE.equals(addressBookCustomerExclusionFlag)) {
            CustomerResponse customerResponse = customerApi.getByMemberinfoSeq(Integer.valueOf(customerId));
            if (StringUtils.isNotEmpty(customerResponse.getMemberInfoAddressId())) {
                conditionDto.setExclusionAddressId(customerResponse.getMemberInfoAddressId());
            }
        }

        // ページャー情報がリクエストに設定されている場合
        PageInfoResponse pageInfoResponse = null;
        // 住所リストを取得
        List<AddressQueryModel> addressList = null;
        if (pageInfoRequest != null) {
            // ページング検索セットアップ
            PageInfoModule pageInfoModule = ApplicationContextUtility.getBean(PageInfoModule.class);
            pageInfoModule.setupPageInfo(conditionDto, pageInfoRequest.getPage(), pageInfoRequest.getLimit(),
                                         pageInfoRequest.getOrderBy(), pageInfoRequest.getSort()
                                        );
            addressList = this.addressBookQuery.getOpenAddressListByCustomerId(conditionDto);
            // ページ情報レスポンスを設定
            pageInfoResponse = new PageInfoResponse();
            try {
                pageInfoModule.setupResponsePager(conditionDto, pageInfoResponse);
            } catch (InvocationTargetException | IllegalAccessException e) {
                LOGGER.error("例外処理が発生しました", e);
                throw new DomainException("LOGISTIC-PAGI0001-E");
            }

        }

        AddressModel result = new AddressModel();
        result.setAddressList(addressList);
        result.setPageInfoResponse(pageInfoResponse);

        return result;
    }

}