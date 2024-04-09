/*
 * Project Name : HIT-MALL3
 *
 * Copyright (C) 2008 i-TEC HANKYU HANSHIN INC. All Rights Reserved.
 *
 */

package jp.co.itechh.quad.core.logic.administrator.impl;

import jp.co.itechh.quad.core.base.util.common.ArgumentCheckUtil;
import jp.co.itechh.quad.core.base.util.common.PropertiesUtil;
import jp.co.itechh.quad.core.base.utility.DateUtility;
import jp.co.itechh.quad.core.constant.type.HTypeAdministratorStatus;
import jp.co.itechh.quad.core.constant.type.HTypePasswordSHA256EncryptedFlag;
import jp.co.itechh.quad.core.dao.administrator.AdministratorDao;
import jp.co.itechh.quad.core.dto.administrator.AdministratorSearchForDaoConditionDto;
import jp.co.itechh.quad.core.entity.administrator.AdministratorEntity;
import jp.co.itechh.quad.core.logic.AbstractShopLogic;
import jp.co.itechh.quad.core.logic.administrator.AdminAuthGroupLogic;
import jp.co.itechh.quad.core.logic.administrator.AdminLogic;
import jp.co.itechh.quad.core.utility.AdministratorUtility;
import org.apache.commons.lang3.ObjectUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.sql.Timestamp;
import java.util.List;

/**
 * 運営者情報操作ロジック実装
 * @author tomo (itec) HM3.2 管理者権限対応（サービス＆ロジック統合及び DTO 改修含む)
 * @author Kaneko (itec) 2012/08/21 UtilからHelperへ変更　Util使用箇所をgetComponentで取得するように変更
 *
 */
@Component
public class AdminLogicImpl extends AbstractShopLogic implements AdminLogic {

    /** ロガー */
    private static final Logger LOGGER = LoggerFactory.getLogger(AdminLogicImpl.class);

    /** 運営者DAO */
    private final AdministratorDao adminDao;

    /** 権限グループ操作用ロジック */
    private final AdminAuthGroupLogic groupLogic;

    /** Date utility */
    private final DateUtility dateUtility;

    /** 管理者Utility */
    private final AdministratorUtility administratorUtility;

    @Autowired
    public AdminLogicImpl(AdministratorDao adminDao,
                          AdminAuthGroupLogic groupLogic,
                          DateUtility dateUtility,
                          AdministratorUtility administratorUtility) {
        this.adminDao = adminDao;
        this.groupLogic = groupLogic;
        this.dateUtility = dateUtility;
        this.administratorUtility = administratorUtility;
    }

    /**
     * 運営者情報を登録する
     * @param entity 運営者エンティティもしくはそれを継承したクラスオブジェクト
     */
    @Override
    public void register(AdministratorEntity entity) {
        ArgumentCheckUtil.assertNotNull("entity", entity);

        // 管理者SEQ取得
        Integer administratorSeq = adminDao.getAdministratorSeqNextVal();
        entity.setAdministratorSeq(administratorSeq);

        entity.setPasswordSHA256EncryptedFlag(HTypePasswordSHA256EncryptedFlag.ENCRYPTED);

        // 登録/更新時刻の設定
        Timestamp currentTime = dateUtility.getCurrentTime();
        entity.setRegistTime(currentTime);
        entity.setUpdateTime(currentTime);

        entity.setPasswordChangeTime(currentTime);
        // Get the number of days to calculate password expiry date.
        int passwordExpiryDays = PropertiesUtil.getSystemPropertiesValueToInt("admin.password.expiry.days");
        entity.setPasswordExpiryDate(
                        dateUtility.getAmountDayTimestamp(passwordExpiryDays, true, dateUtility.getCurrentDate()));
        entity.setLoginFailureCount(0);
        entity.setAccountLockTime(null);

        adminDao.insert(entity);
    }

    /**
     * 運営者情報を更新する
     * @param entity 運営者エンティティもしくはそれを継承したクラスオブジェクト
     */
    @Override
    public void update(AdministratorEntity entity) {
        ArgumentCheckUtil.assertNotNull("entity", entity);

        AdministratorEntity oldEntity = adminDao.getEntity(entity.getAdministratorSeq());

        // 対象が存在しない（削除されている）場合、エラー
        if (ObjectUtils.isEmpty(oldEntity)) {
            throwMessage(MSGCD_ADMINISTRATOR_UPDATE_FAIL);
        }
        // 登録/更新時刻の設定
        Timestamp currentTime = dateUtility.getCurrentTime();
        if (entity.getAdministratorPassword() == null) {
            // パスワードが変更されない場合は、旧パスワード（暗号化済）を再設定する
            entity.setAdministratorPassword(oldEntity.getAdministratorPassword());
        } else {
            // パスワードが変更された場合は暗号化しなおす
            // SHA-256ハッシュ値計算用文字列生成
            String sha256HashText = administratorUtility.createSHA256HashValue(entity.getShopSeq(),
                                                                               entity.getAdministratorSeq(),
                                                                               entity.getAdministratorPassword()
                                                                              );
            entity.setPasswordChangeTime(currentTime);
            // Get the number of days to calculate password expiry date.
            int passwordExpiryDays = PropertiesUtil.getSystemPropertiesValueToInt("admin.password.expiry.days");
            entity.setPasswordExpiryDate(
                            dateUtility.getAmountDayTimestamp(passwordExpiryDays, true, dateUtility.getCurrentDate()));
            entity.setLoginFailureCount(0);
            entity.setAccountLockTime(null);
        }
        entity.setUpdateTime(currentTime);
        adminDao.update(entity);
    }

    /**
     * 運営者を削除する
     * @param entity 運営者エンティティもしくはそれを継承したクラスオブジェクト
     */
    @Override
    public void delete(AdministratorEntity entity) {
        ArgumentCheckUtil.assertNotNull("entity", entity);

        adminDao.delete(entity);
    }

    /**
     * 運営者情報を取得する
     * @param adminSeq 運営者SEQ
     * @return 運営者情報
     */
    @Override
    public AdministratorEntity getAdministrator(Integer adminSeq) {
        ArgumentCheckUtil.assertNotNull("adminSeq", adminSeq);

        AdministratorEntity admin = adminDao.getEntity(adminSeq);

        // 管理者権限グループの情報を肉付けする
        addAdminAuthGroupToAdministratorEntity(admin);

        if (admin == null) {
            LOGGER.info(String.format("ログイン中の運営者が他者によって削除。SEQ[%s]", adminSeq));
        }

        return admin;
    }

    /**
     * 運営者情報を取得する
     * @param shopSeq ショップSEQ
     * @param userId 運営者ユーザID
     * @return 運営者情報
     */
    @Override
    public AdministratorEntity getAdministrator(Integer shopSeq, String userId) {
        ArgumentCheckUtil.assertNotNull("shopSeq", shopSeq);
        ArgumentCheckUtil.assertNotNull("userId", userId);

        AdministratorEntity admin = adminDao.getEntityById(shopSeq, userId);

        // ステータスを確認する
        if (ObjectUtils.isEmpty(admin) || HTypeAdministratorStatus.STOP == admin.getAdministratorStatus()) {
            return null;
        }
        // 管理者権限グループの情報を肉付けする
        addAdminAuthGroupToAdministratorEntity(admin);

        return admin;
    }

    /**
     * 検索条件に一致する運営者一覧を取得する
     * @param condition 検索条件
     * @return 運営者一覧
     */
    @Override
    public List<AdministratorEntity> getList(AdministratorSearchForDaoConditionDto condition) {
        ArgumentCheckUtil.assertNotNull("condition", condition);

        List<AdministratorEntity> list = adminDao.getAdminList(condition);

        // 管理者権限グループの情報を肉付けする
        addAdminAuthGroupToAdministratorEntityList(list);

        return list;
    }

    /**
     * 指定された運営者アカウントが存在するかどうか確認する
     * @param userId 運営者ユーザID
     * @return true - 存在する<br /> false - 存在しない
     */
    @Override
    public boolean isExistedAdmin(String userId) {
        ArgumentCheckUtil.assertNotNull("userId", userId);

        Integer shopSeq = 1001;
        AdministratorEntity entity = adminDao.getEntityById(shopSeq, userId);
        return (entity != null);
    }

    /**
     * 指定された運営者情報が同一ユーザかどうか確認する。（想定用途：運営者更新）
     * @param administratorSeq 比較元運営者SEQ
     * @param shopSeq 比較先運営者所属ショップSEQ
     * @param userId 比較先運営者ユーザID
     * @return true - 同一運営者でない<br /> false - 同一運営者でない
     */
    @Override
    public boolean isSameAdmin(Integer administratorSeq, Integer shopSeq, String userId) {
        ArgumentCheckUtil.assertNotNull("administratorSeq", administratorSeq);
        ArgumentCheckUtil.assertNotNull("shopSeq", shopSeq);
        ArgumentCheckUtil.assertNotNull("userId", userId);

        AdministratorEntity entity = adminDao.getEntityById(shopSeq, userId);
        if (entity == null) {
            return false;
        }
        return entity.getAdministratorSeq().equals(administratorSeq);
    }

    /**
     * 引数の運営者情報エンティティの運営者が所属する権限グループ情報を取得し、エンティティのフィールドへ権限情報を設定する。
     * @param entity 権限情報を取得したい運営者情報エンティティ
     */
    @Override
    public void addAdminAuthGroupToAdministratorEntity(AdministratorEntity entity) {
        // 処理対象運営者情報がない場合は処理を行わずに抜ける
        if (entity == null) {
            return;
        }

        Integer groupSeq = entity.getAdminAuthGroupSeq();
        entity.setAdminAuthGroup(groupLogic.getAdminAuthGroup(groupSeq));
    }

    /**
     * 引数の運営者情報エンティティリストの運営者が所属する権限グループ情報を取得し、各エンティティのフィールドへ権限情報を設定する。
     * @param entityList 権限情報を取得したい運営者情報DTOリスト
     */
    @Override
    public void addAdminAuthGroupToAdministratorEntityList(List<AdministratorEntity> entityList) {
        // 処理対象運営者情報がない場合は処理を行わずに抜ける
        if (entityList == null || entityList.isEmpty()) {
            return;
        }

        for (AdministratorEntity entity : entityList) {
            addAdminAuthGroupToAdministratorEntity(entity);
        }
    }
}