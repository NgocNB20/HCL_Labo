/*
 * Project Name : HIT-MALL4
 *
 * Copyright (C) 2021 i-TEC HANKYU HANSHIN INC. All Rights Reserved.
 *
 */
package jp.co.itechh.quad.core.entity.previewaccess;

import lombok.Data;
import org.seasar.doma.Column;
import org.seasar.doma.Entity;
import org.seasar.doma.Id;
import org.seasar.doma.Table;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Component;

import java.io.Serializable;
import java.sql.Timestamp;

/**
 * プレビューアクセス制御エンティティ
 *
 * @author kimura
 */
@Entity
@Table(name = "PreviewAccessControl")
@Data
@Component
@Scope("prototype")
public class PreviewAccessControlEntity implements Serializable {

    /** serialVersionUID */
    private static final long serialVersionUID = 1L;

    /** プレビューアクセス制御SEQ */
    @Column(name = "previewAccessControlSeq")
    @Id
    private Integer previewAccessControlSeq;

    /** プレビューアクセスキー */
    @Column
    private String previewAccessKey;

    /** プレビューアクセス有効期限 */
    @Column(name = "effectiveTime")
    private Timestamp effectiveTime;

    /** 管理者SEQ */
    @Column(name = "administratorSeq")
    private Integer administratorSeq;

    /** 登録日時 */
    @Column(name = "registTime")
    private Timestamp registTime;

    /** 更新日時 */
    @Column(name = "updateTime")
    private Timestamp updateTime;

}
