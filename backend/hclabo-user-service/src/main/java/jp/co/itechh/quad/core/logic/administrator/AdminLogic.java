/*
 * Project Name : HIT-MALL3
 *
 * Copyright (C) 2008 i-TEC HANKYU HANSHIN INC. All Rights Reserved.
 *
 */

package jp.co.itechh.quad.core.logic.administrator;

import jp.co.itechh.quad.core.dto.administrator.AdministratorSearchForDaoConditionDto;
import jp.co.itechh.quad.core.entity.administrator.AdministratorEntity;

import java.util.List;

/**
 * 運営者情報操作ロジック
 * @author tomo (itec) HM3.2 管理者権限対応（サービス＆ロジック統合及び DTO 改修含む)
 */
public interface AdminLogic {

    /**
     * 運営者ID重複エラー<br/>
     * <code>MSGCD_ADMINISTRATORINFO_ID_MULTI</code>
     */
    String MSGCD_ADMINISTRATOR_UPDATE_FAIL = "LKA000802";

    /**
     * 運営者情報を登録する
     * @param entity 運営者エンティティもしくはそれを継承したクラスオブジェクト
     */
    void register(AdministratorEntity entity);

    /**
     * 運営者情報を更新する
     * @param entity 運営者エンティティもしくはそれを継承したクラスオブジェクト
     */
    void update(AdministratorEntity entity);

    /**
     * 運営者を削除する
     * @param entity 運営者エンティティもしくはそれを継承したクラスオブジェクト
     */
    void delete(AdministratorEntity entity);

    /**
     * 運営者情報を取得する
     * @param administratorSeq 運営者SEQ
     * @return 運営者情報
     */
    AdministratorEntity getAdministrator(Integer administratorSeq);

    /**
     * 運営者情報を取得する
     * @param shopSeq ショップSEQ
     * @param userId 運営者ユーザID
     * @return 運営者情報
     */
    AdministratorEntity getAdministrator(Integer shopSeq, String userId);

    /**
     * 検索条件に一致する運営者一覧を取得する
     * @param condition 検索条件
     * @return 運営者一覧
     */
    List<AdministratorEntity> getList(AdministratorSearchForDaoConditionDto condition);

    /**
     * 指定された運営者アカウントが存在するかどうか確認する
     * @param userId 運営者ユーザID
     * @return true - 存在する<br /> false - 存在しない
     */
    boolean isExistedAdmin(String userId);

    /**
     * 指定された運営者情報が同一ユーザかどうか確認する。（想定用途：運営者更新）
     * @param administratorSeq 比較元運営者SEQ
     * @param shopSeq 比較先運営者所属ショップSEQ
     * @param userId 比較先運営者ユーザID
     * @return true - 同一運営者でない<br /> false - 同一運営者でない
     */
    boolean isSameAdmin(Integer administratorSeq, Integer shopSeq, String userId);

    /**
     * 引数の運営者情報エンティティリストの運営者が所属する権限グループ情報を取得し、各エンティティのフィールドへ権限情報を設定する。
     * @param entityList 権限情報を取得したい運営者情報エンティティリスト
     */
    void addAdminAuthGroupToAdministratorEntityList(List<AdministratorEntity> entityList);

    /**
     * 引数の運営者情報エンティティの運営者が所属する権限グループ情報を取得し、エンティティのフィールドへ権限情報を設定する。
     * @param entity 権限情報を取得したい運営者情報エンティティ
     */
    void addAdminAuthGroupToAdministratorEntity(AdministratorEntity entity);
}