/*
 * Project Name : HIT-MALL3
 *
 * Copyright (C) 2008 i-TEC HANKYU HANSHIN INC. All Rights Reserved.
 *
 */

package jp.co.itechh.quad.admin.pc.web.admin.administrator.authdelete;

import jp.co.itechh.quad.admin.web.AbstractModel;
import lombok.Data;

import java.util.List;

/**
 * 権限グループ削除確認画面 Page クラス
 *
 * @author Pham Quang Dieu (VTI Japan Co., Ltd.)
 */
@Data
public class AdministratorAuthDeleteModel extends AbstractModel {
    /**
     * 権限グループSEQ (詳細画面から遷移して来た直後に F5 を押しても消えないようにするためサブアプリスコープ)
     */
    private String adminAuthGroupSeq;

    /**
     * 権限グループ名称
     */
    private String authGroupDisplayName;

    /**
     * 権限内容繰り返しアイテム
     */
    private List<AdministratorAuthDeleteModelItem> deletePageItems;

    /**
     * 権限種別表示名
     */
    private String authTypeName;

    /**
     * 権限レベル名
     */
    private String authLevelName;

    /**
     * 変更不可権限グループかどうか
     */
    private boolean unmodifiableGroup;

    /**
     * 変更不可権限グループかどうか
     *
     * @return true - 変更不能
     */
    public boolean isUnmodifiableGroup() {
        return unmodifiableGroup;
    }

    /**
     * 登録更新画面描画時にパラメータで渡されたSeq<br />
     * 　画面描画時の画面情報と登録情報に差異がないかをチェックするのに利用する
     */
    private Integer scSeq;

    /**
     * 登録内容描画時にDBから取得したSeq<br />
     * 画面描画時の画面情報と登録情報に差異がないかをチェックするのに利用する
     */
    private Integer dbSeq;

    /**
     * 登録更新画面に表示するデータのシーケンス<br/>
     * <p>
     * [注意]実装時に@GetParameters("seq")を各実装pageクラスでつける必要がある<br />
     */
    public Integer seq;
}
