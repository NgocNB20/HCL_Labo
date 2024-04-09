/*
 * Project Name : HIT-MALL4
 *
 * Copyright (C) 2021 i-TEC HANKYU HANSHIN INC. All Rights Reserved.
 *
 */
package jp.co.itechh.quad.core.logic.previewaccess;

import jp.co.itechh.quad.core.entity.previewaccess.PreviewAccessControlEntity;

/**
 * プレビューアクセス制御ロジック
 *
 * @author kimura
 */
public interface PreviewAccessLogic {

    /** プレビューアクセス制御有効期限 */
    public static final String EFFECTIVETIME_ADD_MINUTE = "preview.access.effectivetime.add.minute";

    /**
     * プレビューアクセスキー登録
     *
     * @param administratorSeq 管理者SEQ
     * @return プレビューアクセスキー
     */
    public String insertPreviewAccess(Integer administratorSeq);

    /**
     * プレビューアクセス制御チェック
     *
     * @param preViewAccessKey プレビューアクセスキー
     * @return エンティティ
     */
    public PreviewAccessControlEntity checkPreviewAccess(String preViewAccessKey);
}