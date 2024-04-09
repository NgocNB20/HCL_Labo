/*
 * Project Name : HIT-MALL3
 *
 * Copyright (C) 2008 i-TEC HANKYU HANSHIN INC. All Rights Reserved.
 *
 */

package jp.co.itechh.quad.admin.pc.web.admin.administrator.authupdate;

import jp.co.itechh.quad.admin.entity.administrator.AdminAuthGroupEntity;
import jp.co.itechh.quad.admin.web.AbstractModel;
import lombok.Data;
import org.hibernate.validator.constraints.Length;

import javax.validation.constraints.NotEmpty;
import java.util.List;
import java.util.Map;

/**
 * 権限グループ登録画面用 Page クラス
 *
 * @author tomo (itec) HM3.2 管理者権限対応（サービス＆ロジック統合及び DTO 改修含む)
 */
@Data
public class AuthUpdateModel extends AbstractModel {

    /**
     * 変種画面を開いた際に取得したエンティティ
     */
    private AdminAuthGroupEntity originalEntity;

    /**
     * 修正後差分エンティティ
     */
    private AdminAuthGroupEntity modifiedEntity;

    /**
     * 差分リスト
     */
    private List<String> modifiedList;

    /**
     * 入力項目：権限グループ名称
     */
    @NotEmpty
    @Length(min = 1, max = 40)
    private String authGroupDisplayName;

    /**
     * 権限種別アイテム
     */

    private List<AuthUpdateModelItem> authItems;
    /**
     * 変更前権限種別アイテム
     */
    private List<AuthUpdateModelItem> originalAuthItems;

    /**
     * 表示情報：権限種別表示名称(メタデータ)
     */
    private String typeDisplayName;

    /**
     * 表示情報：権限レベル表示名称(メタデータ)
     */
    private String level;

    /**
     * 権限レベル情報
     */
    private Map<String, String> levelItems;

    /**
     * 表示情報
     */
    private AuthUpdateModelItem auth;

    private Integer seq;

    private Integer scSeq;

    private Integer dbSeq;

    /**
     * 処理中の権限種別配列番号
     */
    private int authIndex;
}
