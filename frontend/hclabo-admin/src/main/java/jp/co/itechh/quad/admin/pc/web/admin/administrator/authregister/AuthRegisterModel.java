/*
 * Project Name : HIT-MALL3
 *
 * Copyright (C) 2008 i-TEC HANKYU HANSHIN INC. All Rights Reserved.
 *
 */

package jp.co.itechh.quad.admin.pc.web.admin.administrator.authregister;

import jp.co.itechh.quad.admin.web.AbstractModel;
import lombok.Data;
import org.hibernate.validator.constraints.Length;

import javax.validation.constraints.NotEmpty;
import java.util.List;

/**
 * 権限グループ登録画面用 Page クラス
 * @author tomo (itec) HM3.2 管理者権限対応（サービス＆ロジック統合及び DTO 改修含む)
 */
@Data
public class AuthRegisterModel extends AbstractModel {

    /** 入力項目：権限グループ名称 */
    @NotEmpty
    @Length(min = 1, max = 40)
    private String authGroupDisplayName;

    /** 権限種別アイテム(確認画面でも使い回しするためサブアプリスコープ) */
    private List<RegisterModelItem> authItems;

}
