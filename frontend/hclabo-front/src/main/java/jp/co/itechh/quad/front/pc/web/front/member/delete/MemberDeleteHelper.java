/*
 * Copyright (C) 2022 i-TEC HANKYU HANSHIN INC. All Rights Reserved.
 */

package jp.co.itechh.quad.front.pc.web.front.member.delete;

import jp.co.itechh.quad.customer.presentation.api.param.CustomerDeleteRequest;
import org.springframework.stereotype.Component;

/**
 * メンバー削除 helper.
 * @author Pham Quang Dieu
 */
@Component
public class MemberDeleteHelper {
    /**
     * 会員登録解除リクエストに変換
     *
     * @param memberDeleteModel
     * @param accessUid
     * @return 会員登録解除リクエスト
     */
    public CustomerDeleteRequest toCustomerDeleteRequest(MemberDeleteModel memberDeleteModel, String accessUid) {
        CustomerDeleteRequest customerDeleteRequest = new CustomerDeleteRequest();

        customerDeleteRequest.setMemberInfoId(memberDeleteModel.getMemberInfoId());
        customerDeleteRequest.setMemberInfoPassWord(memberDeleteModel.getMemberInfoPassWord());
        customerDeleteRequest.setAccessUid(accessUid);

        return customerDeleteRequest;
    }

}