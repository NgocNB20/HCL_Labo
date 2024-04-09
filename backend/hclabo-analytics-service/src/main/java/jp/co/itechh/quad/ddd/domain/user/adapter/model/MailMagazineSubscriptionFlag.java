package jp.co.itechh.quad.ddd.domain.user.adapter.model;

import jp.co.itechh.quad.core.constant.type.HTypeMagazineSubscribeType;
import lombok.Data;

/**
 * メールマガジン購読フラグ
 */
@Data
public class MailMagazineSubscriptionFlag {

    /**
     * メールマガジン購読フラグ
     */
    private HTypeMagazineSubscribeType mailMagazine;

}
