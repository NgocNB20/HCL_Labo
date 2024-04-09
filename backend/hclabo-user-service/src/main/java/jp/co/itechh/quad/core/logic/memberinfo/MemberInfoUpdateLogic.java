/*
 * Project Name : HIT-MALL3
 *
 * Copyright (C) 2008 i-TEC HANKYU HANSHIN INC. All Rights Reserved.
 *
 */

package jp.co.itechh.quad.core.logic.memberinfo;

import jp.co.itechh.quad.core.entity.memberinfo.MemberInfoEntity;

import java.sql.Timestamp;

/**
 * 会員情報更新<br/>
 *
 * @author natume
 *
 */
public interface MemberInfoUpdateLogic {

    /**
     * 会員情報更新処理<br/>
     *
     * @param memberInfoEntity 会員エンティティ
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

    /**
     * 会員ログイン情報更新処理<br/>
     *
     * @param memberInfoSeq 会員SEQ
     * @param userAgent ユーザーエージェント
     * @return 更新件数
     */
    int execute(Integer memberInfoSeq, String userAgent);
}