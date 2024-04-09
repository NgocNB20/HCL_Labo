/*
 * Project Name : HIT-MALL4
 *
 * Copyright (C) 2021 i-TEC HANKYU HANSHIN INC. All Rights Reserved.
 *
 */

package jp.co.itechh.quad.core.logic.administrator;

import java.util.List;

/**
 * 権限グループ詳細ロジック
 * @author Doan Thang (VTI Japan Co., Ltd.)
 */
public interface AdminAuthGroupDetailLogic {

    /**
     * 権限リストを取得する
     *
     * @param adminAuthGroupSeq 権限グループSeq
     * @return 権限リスト
     */
    List<String> getAuthorityList(Integer adminAuthGroupSeq);

}
