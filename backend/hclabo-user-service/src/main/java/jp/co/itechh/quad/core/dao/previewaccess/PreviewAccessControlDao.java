/*
 * Project Name : HIT-MALL3
 *
 * Copyright (C) 2008 i-TEC HANKYU HANSHIN INC. All Rights Reserved.
 *
 */
package jp.co.itechh.quad.core.dao.previewaccess;

import jp.co.itechh.quad.core.entity.previewaccess.PreviewAccessControlEntity;
import org.seasar.doma.Dao;
import org.seasar.doma.Insert;
import org.seasar.doma.Select;
import org.seasar.doma.boot.ConfigAutowireable;

import java.sql.Timestamp;

/**
 * プレビューアクセス制御DAOクラス
 *
 * @author kimura
 */
@Dao
@ConfigAutowireable
public interface PreviewAccessControlDao {

    /**
     * インサート
     *
     * @param previewAccessControlEntity プレビューアクセス制御エンティティ
     * @return 処理件数
     */
    @Insert(excludeNull = true)
    int insert(PreviewAccessControlEntity previewAccessControlEntity);

    /**
     * プレビューアクセスキーに一致し、現在日時が有効期限内にあるプレビューアクセス制御情報を取得
     *
     * @param previewAccessKey プレビューアクセスキー
     * @param currentTime      現在日時
     * @return エンティティ
     */
    @Select
    PreviewAccessControlEntity getEntityByPreviewAccessKey(String previewAccessKey, Timestamp currentTime);

    /**
     * プレビューアクセス制御SEQを取得
     *
     * @return プレビューアクセス制御SEQ
     */
    @Select
    Integer getPreviewAccessControlSeqNextVal();
}
