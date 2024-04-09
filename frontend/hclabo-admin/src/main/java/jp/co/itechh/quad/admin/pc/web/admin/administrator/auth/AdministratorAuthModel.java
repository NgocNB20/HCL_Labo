/*
 * Project Name : HIT-MALL3
 *
 * Copyright (C) 2008 i-TEC HANKYU HANSHIN INC. All Rights Reserved.
 *
 */

package jp.co.itechh.quad.admin.pc.web.admin.administrator.auth;

import jp.co.itechh.quad.admin.web.AbstractModel;
import lombok.Data;

import java.util.List;

/**
 * 権限グループ一覧画面 Page クラス
 *
 * @author Pham Quang Dieu (VTI Japan Co., Ltd.)
 */
@Data
public class AdministratorAuthModel extends AbstractModel {

    /**
     * 表本体データ
     */
    private String[][] authItemsItems;

    /**
     * 表１行分データ
     */
    private String[] authItems;

    /**
     * 表 権限レベル表示名データ
     */
    private String auth;

    /**
     * 表 行番号
     */
    private int authItemsIndex;

    /**
     * 表 列番号
     */
    private int authIndex;

    /**
     * ヘッダ行出力用アイテム
     */
    private String[] labelItems;

    /**
     * ヘッダ行名称
     */
    private String label;

    /**
     * 処理中ヘッダ行の列番号
     */
    private int labelIndex;

    /**
     * リンク用権限グループSEQリスト
     */
    private List<Integer> adminSeqList;

    //    /**
    //     * レンダリング中のカラムがヘッダ行かどうか
    //     * @return true - そうである
    //     */
    //    public boolean isHeaderColumn() {
    //        return labelIndex >= 2;
    //    }
    //
    //    /**
    //     * ヘッダ行の列数を取得する
    //     * @return 列数
    //     */
    //    public String getLabelHeadColspan() {
    //        return Integer.toString(this.labelItems.length - 1);
    //    }
    //
    //    /**
    //     * レンダリング中のカラムが行番号列かどうか
    //     * @return true - そうである
    //     */
    //    public boolean isNumberCol() {
    //        return authIndex == 0;
    //    }
    //
    //    /**
    //     * レンダリング中のカラムが権限グループ名称列かどうか
    //     * @return true - そうである
    //     */
    //    public boolean isGroupNameCol() {
    //        return authIndex == 1;
    //    }
    //
    //    /**
    //     * レンダリング中のカラムが権限設定列かどうか
    //     * @return true - そうである
    //     */
    //    public boolean isSettingCol() {
    //        return authIndex >= 2;
    //    }
    //
    //    /**
    //     * リンク内パラメータの権限グループSEQを返す
    //     * @return 権限グループSEQ
    //     */
    //    public Integer getSeq() {
    //        return adminSeqList.get(authItemsIndex);
    //    }

    private int seq;
}