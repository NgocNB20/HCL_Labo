/*
 * Project Name : HIT-MALL4
 *
 * Copyright (C) 2021 i-TEC HANKYU HANSHIN INC. All Rights Reserved.
 *
 */

package jp.co.itechh.quad.admin.dto.mail;

import lombok.Data;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Component;

import java.io.Serializable;

/**
 * 機能概要：新メール送信サービス｜添付ファイルDto
 * 作成日：2021/12/07
 *
 * @author Phan Tien VU (VTI Japan Co., Ltd.)
 */
@Data
@Component
@Scope("prototype")
public class AttachFileDto implements Serializable {

    /** シリアル */
    private static final long serialVersionUID = 1L;

    /** 添付ファイル名（ファイル名拡張子含む） */
    private String attachFileName;
    /** 添付ファイル：パス方式で渡す場合（複数添付ファイルの対応が余力があれば追加） */
    private String attachFilePath;
    /** 添付ファイル：byte方式で渡す場合（複数添付ファイルの対応が余力があれば追加） */
    private byte[] attachFileByte;

}
