/*
 * Copyright (C) 2022 i-TEC HANKYU HANSHIN INC. All Rights Reserved.
 */

package jp.co.itechh.quad.hclabo.ddd.domain.user.adapter;

import java.util.List;

/**
 * ユーザーマイクロサービス
 * 通知アダプター
 */
public interface INotificationSubAdapter {

    void examkitReceivedEntry(String message);

    void examResultsEntry(String message);

    void examResultsNotice(List<String> orderCodeList);
}
