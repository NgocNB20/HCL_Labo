/*
 * Project Name : HIT-MALL3
 *
 * Copyright (C) 2008 i-TEC HANKYU HANSHIN INC. All Rights Reserved.
 *
 */

package jp.co.itechh.quad.core.service.memberinfo;

import jp.co.itechh.quad.core.entity.memberinfo.MemberInfoEntity;

/**
 * 会員情報データチェックサービス
 *
 * @author negishi
 * @version $Revision: 1.1 $
 *
 */
public interface MemberInfoDataCheckService {

    /**
     * サービス実行
     *
     * @param memberInfoEntity 会員情報エンティティ
     */
    void execute(MemberInfoEntity memberInfoEntity);

}
