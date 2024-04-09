package jp.co.itechh.quad.ddd.infrastructure.user.adapter;

import jp.co.itechh.quad.mailmagazine.presentation.api.param.MailmagazineMemberListResponse;
import jp.co.itechh.quad.mailmagazine.presentation.api.param.MailmagazineMemberResponse;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.util.ObjectUtils;

import java.util.ArrayList;
import java.util.List;

/**
 * メールマガアダプタHelperクラス
 */
@Component
public class MailMagazineAdapterHelper {

    /**
     * コンストラクタ
     */
    @Autowired
    public MailMagazineAdapterHelper() {
    }

    public List<String> toSendStatusList(MailmagazineMemberListResponse listResponse) {

        if (ObjectUtils.isEmpty(listResponse) || ObjectUtils.isEmpty(listResponse.getMailmagazineMemberList())) {
            return null;
        }

        List<String> ret = new ArrayList<>();
        for (MailmagazineMemberResponse response : listResponse.getMailmagazineMemberList()) {
            ret.add(response.getSendStatus());
        }

        return ret;
    }
}