package jp.co.itechh.quad.ddd.infrastructure.user.adapter;

import jp.co.itechh.quad.core.constant.type.HTypeMagazineSubscribeType;
import jp.co.itechh.quad.core.constant.type.HTypeSendStatus;
import jp.co.itechh.quad.ddd.domain.user.adapter.model.MailMagazineSubscriptionFlag;
import jp.co.itechh.quad.mailmagazine.presentation.api.param.MailmagazineMemberResponse;
import org.apache.commons.lang3.StringUtils;
import org.springframework.stereotype.Component;
import org.springframework.util.ObjectUtils;

/**
 * メルマガアダプタHelperクラス
 */
@Component
public class MailMagazineAdapterHelper {

    /**
     * メールマガジン購読フラグに変換
     *
     * @param originMemberInfoSeq  会員SEQ
     * @param originMemberInfoMail メールアドレス
     * @param response             メルマガ会員レスポンス
     * @return メールマガジン購読フラグ
     */
    public MailMagazineSubscriptionFlag toMailMagazineSubscriptionFlag(Integer originMemberInfoSeq,
                                                                       String originMemberInfoMail,
                                                                       MailmagazineMemberResponse response) {
        MailMagazineSubscriptionFlag mailMagazineSubscriptionFlag = new MailMagazineSubscriptionFlag();

        if (ObjectUtils.isEmpty(response)) {
            mailMagazineSubscriptionFlag.setMailMagazine(HTypeMagazineSubscribeType.NOT_SUBSCRIBE);
            return mailMagazineSubscriptionFlag;
        }

        if (HTypeSendStatus.SENDING.getValue().equals(response.getSendStatus()) && (response.getMemberinfoSeq() != null
                                                                                    && response.getMemberinfoSeq()
                                                                                               .equals(originMemberInfoSeq))
            && (StringUtils.isNotEmpty(response.getMail()) && response.getMail().equals(originMemberInfoMail))) {
            mailMagazineSubscriptionFlag.setMailMagazine(HTypeMagazineSubscribeType.SUBSCRIBE);
        } else {
            mailMagazineSubscriptionFlag.setMailMagazine(HTypeMagazineSubscribeType.NOT_SUBSCRIBE);
        }

        return mailMagazineSubscriptionFlag;
    }
}