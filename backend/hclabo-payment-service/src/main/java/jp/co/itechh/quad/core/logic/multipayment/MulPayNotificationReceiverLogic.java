/*
 * Project Name : HIT-MALL3
 *
 * Copyright (C) 2008 i-TEC HANKYU HANSHIN INC. All Rights Reserved.
 *
 */

package jp.co.itechh.quad.core.logic.multipayment;

import jp.co.itechh.quad.core.dto.multipayment.MulPayNotificationReceiverDto;

/**
 * マルチペイメント決済結果通知受付サーブレットロジック<br/>
 *
 * @author na65101 STS Nakamura 2020/03/11
 *
 */
public interface MulPayNotificationReceiverLogic {

    /**
     * 実行処理<br/>
     * HTTPリクエストのパラメータチェックやマルペイ請求やマルペイ決済結果への登録・更新処理を行う。<br/>
     *
     * @param dto マルチペイメント決済結果通知受付サーブレットDto
     */
    void execute(MulPayNotificationReceiverDto dto);

}
