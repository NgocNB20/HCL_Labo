/*
 * Project Name : HIT-MALL4
 *
 * Copyright (C) 2022 i-TEC HANKYU HANSHIN INC. All Rights Reserved.
 *
 */

package jp.co.itechh.quad.front.pc.web.front.mail;

import jp.co.itechh.quad.front.entity.memberinfo.MemberInfoEntity;
import jp.co.itechh.quad.front.web.AbstractModel;
import lombok.Data;

/**
 * メールアドレス変更 Model
 *
 * @author Pham Quang Dieu (VTI Japan Co., Ltd.)
 *
 */
@Data
public class MailModel extends AbstractModel {

    /**
     * 会員エンティティ<br/>
     */
    private MemberInfoEntity memberInfoEntity;

    /**
     * メールURLパラメータ<br/>
     */
    private String mid;

    /**
     * 変更前メールアドレス<br/>
     */
    private String preMemberInfoMail;

    /**
     * 変更後メールアドレス<br/>
     */
    private String memberInfoMail;

    /**
     * 変更前メールマガジン購読希望<br/>
     */
    private boolean preMailMagazine;

    /**
     * 変更後メールマガジン購読希望<br/>
     */
    private boolean mailMagazine;

    /**
     * 無効なURLからの遷移かどうか<br/>
     * ※デフォルトtrue。Controllerで正常処理実行時のみfalseが設定
     */
    private boolean errorUrl = true;

}
