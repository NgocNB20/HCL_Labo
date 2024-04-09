/*
 * Project Name : HIT-MALL3
 *
 * Copyright (C) 2008 i-TEC HANKYU HANSHIN INC. All Rights Reserved.
 *
 */

package jp.co.itechh.quad.core.service.memberinfo;

import jp.co.itechh.quad.core.entity.memberinfo.MemberInfoEntity;

import java.sql.Timestamp;

/**
 * 会員情報更新サービス<br/>
 *
 * @author negishi
 * @version $Revision: 1.2 $
 *
 */
public interface MemberInfoUpdateService {

    /**
     * 会員情報更新失敗エラー<br/>
     * <code>MSGCD_MEMBERINFO_UPDATE_FAIL</code>
     */
    String MSGCD_MEMBERINFO_UPDATE_FAIL = "SMM000301";

    /**
     * 会員情報更新処理
     *
     * @param memberInfoEntity 会員情報エンティティ
     * @return 更新件数
     */
    int execute(MemberInfoEntity memberInfoEntity);

    /**
     * 更新日時指定付き会員情報更新処理<br/>
     *
     * @param memberInfoEntity 会員エンティティ
     * @param currentTime 現在日時
     * @return 更新件数
     */
    int execute(MemberInfoEntity memberInfoEntity, Timestamp currentTime);
}