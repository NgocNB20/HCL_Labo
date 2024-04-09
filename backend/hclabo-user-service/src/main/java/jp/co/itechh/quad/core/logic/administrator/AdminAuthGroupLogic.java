/*
 * Project Name : HIT-MALL3
 *
 * Copyright (C) 2008 i-TEC HANKYU HANSHIN INC. All Rights Reserved.
 *
 */

package jp.co.itechh.quad.core.logic.administrator;

import jp.co.itechh.quad.core.entity.administrator.AdminAuthGroupEntity;

import java.util.List;

/**
 * 権限グループ操作ロジック
 * @author tomo (itec) HM3.2 管理者権限対応（サービス＆ロジック統合及び DTO 改修含む)
 */
public interface AdminAuthGroupLogic {

    /**
     * 権限グループ詳細を含む権限グループを登録する。
     * @param group 権限グループ
     */
    void register(AdminAuthGroupEntity group);

    /**
     * 権限グループ詳細を含む権限グループを更新する。
     * @param group 権限グループ
     */
    void update(AdminAuthGroupEntity group);

    /**
     * 権限グループを削除する。
     * @param group 権限グループ
     */
    void delete(AdminAuthGroupEntity group);

    /**
     * 権限グループ詳細を含む権限グループを取得する。
     * @param adminAuthGroupSeq 管理者権限グループSEQ
     * @return 権限グループ
     */
    AdminAuthGroupEntity getAdminAuthGroup(Integer adminAuthGroupSeq);

    /**
     * 権限グループ詳細を含む権限グループを取得する。
     * @param shopSeq ショップSEQ
     * @param authGroupDisplayName 権限グループ名称
     * @return 権限グループ
     */
    AdminAuthGroupEntity getAdminAuthGroup(Integer shopSeq, String authGroupDisplayName);

    /**
     * 指定されたショップの権限グループ一覧を取得する。<br />
     * 各権限グループには権限詳細が含まれる。
     * @param shopSeq ショップSEQ
     * @return 権限グループ一覧
     */
    List<AdminAuthGroupEntity> getAdminAuthGroupList(Integer shopSeq);
}