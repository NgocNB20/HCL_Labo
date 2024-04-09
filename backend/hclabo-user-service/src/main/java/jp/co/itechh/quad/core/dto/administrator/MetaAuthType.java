/*
 * Project Name : HIT-MALL3
 *
 * Copyright (C) 2008 i-TEC HANKYU HANSHIN INC. All Rights Reserved.
 *
 */

package jp.co.itechh.quad.core.dto.administrator;

import lombok.Data;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Component;

import java.util.List;

/**
 * メタ権限種別設定クラス<br />
 * <pre>
 * 権限グループで設定可能な権限種別を設定するために使用する。
 * DICON 内でインスタンス化される。クラス自体はコンテナ登録しないため、DTO とは名乗らない。
 * </pre>
 * @author tomo (itec) HM3.2 管理者権限対応（サービス＆ロジック統合及び DTO 改修含む)
 */
@Data
@Component
@Scope("prototype")
public class MetaAuthType {

    /** 権限種別コード */
    private String authTypeCode;

    /** 表示名 */
    private String typeDisplayName;

    /** 設定可能権限レベル設定  */
    private List<MetaAuthLevel> metaAuthLevelList;

    /**
     * コンストラクタ
     * @param authTypeCode 権限種別コード
     * @param typeDisplayName 表示名
     */
    public MetaAuthType(String authTypeCode, String typeDisplayName) {
        this.authTypeCode = authTypeCode;
        this.typeDisplayName = typeDisplayName;
    }
}
