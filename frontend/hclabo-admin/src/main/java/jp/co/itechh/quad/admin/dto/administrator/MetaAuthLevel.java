/*
 * Project Name : HIT-MALL3
 *
 * Copyright (C) 2008 i-TEC HANKYU HANSHIN INC. All Rights Reserved.
 *
 */

package jp.co.itechh.quad.admin.dto.administrator;

import lombok.Data;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Component;

/**
 * メタ権限レベル設定クラス<br />
 * <pre>
 * 各権限種別で選択可能な権限レベルを設定するために使用する。
 * DICON 内でインスタンス化される。クラス自体はコンテナ登録しないため、DTO とは名乗らない。
 * </pre>
 * @author tomo (itec) HM3.2 管理者権限対応（サービス＆ロジック統合及び DTO 改修含む)
 */
@Data
@Component
@Scope("prototype")
public class MetaAuthLevel {

    /** 権限レベル */
    private Integer metaLevel;

    /** 権限レベル表示名 */
    private String levelDisplayName;
}
