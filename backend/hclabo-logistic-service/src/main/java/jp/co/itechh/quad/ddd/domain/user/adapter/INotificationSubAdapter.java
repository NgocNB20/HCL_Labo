/*
 * Copyright (C) 2022 i-TEC HANKYU HANSHIN INC. All Rights Reserved.
 */

package jp.co.itechh.quad.ddd.domain.user.adapter;

/**
 * 通知アダプター
 */
public interface INotificationSubAdapter {

    /**
     * ユーザーマイクロサービス<br/>
     * 在庫解放エラーメール<br/>
     *
     * @param exceptionName 例外エラー名
     */
    void inventoryReleaseError(String exceptionName);

}
