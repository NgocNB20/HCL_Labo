/*
 * Project Name : HIT-MALL3
 *
 * Copyright (C) 2008 i-TEC HANKYU HANSHIN INC. All Rights Reserved.
 *
 */

package jp.co.itechh.quad.core.service.memberinfo;

import jp.co.itechh.quad.core.entity.memberinfo.MemberInfoEntity;

/**
 * 会員メールアドレス変更サービス(本登録)<br/>
 *
 * @author natume
 *
 */
public interface MemberInfoMailUpdateService {

    /* errorcode */

    /**
     * 会員情報更新エラー<br/>
     * <code>MSGCD_MEMBERINFO_UPDATE_ERROR</code>
     */
    String MSGCD_MEMBERINFO_UPDATE_ERROR = "SMM001301";

    /**
     * 会員メールアドレス変更処理<br/>
     *
     * @param shopSeq ショップSEQ
     * @param memberInfoEntity 会員エンティティ
     * @param mailMagazine メルマガ購読フラグ
     * @param preMailMagazine 変更前メルマガ購読フラグ
     * @param confirmMailPassword 確認メールパスワード
     */
    void execute(Integer shopSeq,
                 MemberInfoEntity memberInfoEntity,
                 boolean mailMagazine,
                 boolean preMailMagazine,
                 String confirmMailPassword);
}