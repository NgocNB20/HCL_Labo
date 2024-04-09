/*
 * Project Name : HIT-MALL3
 *
 * Copyright (C) 2008 i-TEC HANKYU HANSHIN INC. All Rights Reserved.
 *
 */

package jp.co.itechh.quad.admin.pc.web.admin.administrator.update;

import jp.co.itechh.quad.admin.base.util.common.CopyUtil;
import jp.co.itechh.quad.admin.base.util.common.PropertiesUtil;
import jp.co.itechh.quad.admin.base.util.seasar.StringUtil;
import jp.co.itechh.quad.admin.base.utility.ApplicationContextUtility;
import jp.co.itechh.quad.admin.base.utility.ConversionUtility;
import jp.co.itechh.quad.admin.base.utility.DateUtility;
import jp.co.itechh.quad.admin.constant.type.HTypeAdministratorStatus;
import jp.co.itechh.quad.admin.constant.type.HTypePasswordNeedChangeFlag;
import jp.co.itechh.quad.admin.entity.administrator.AdminAuthGroupDetailEntity;
import jp.co.itechh.quad.admin.entity.administrator.AdminAuthGroupEntity;
import jp.co.itechh.quad.admin.entity.administrator.AdministratorEntity;
import jp.co.itechh.quad.admin.util.common.EnumTypeUtil;
import jp.co.itechh.quad.administrator.presentation.api.param.AdminAuthGroupDetailRequest;
import jp.co.itechh.quad.administrator.presentation.api.param.AdminAuthGroupRequest;
import jp.co.itechh.quad.administrator.presentation.api.param.AdministratorUpdateRequest;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.sql.Timestamp;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

/**
 * 運営者情報変更入力・確認画面Helperクラス
 *
 * @author Pham Quang Dieu (VTI Japan Co., Ltd.)
 */
@Component
public class AdministratorUpdateHelper {

    /**
     * 日付関連Helper取得
     */
    private final DateUtility dateUtility;

    /**
     * 変換ユーティリティクラス
     */
    private final ConversionUtility conversionUtility;

    /**
     * Reset value of login failure count
     */
    private static final Integer RESET_LOGIN_FAILURE_COUNT = 0;

    /**
     * コンストラクター
     *
     * @param dateUtility       日付関連Helper取得
     * @param conversionUtility 変換ユーティリティクラス
     */
    @Autowired
    public AdministratorUpdateHelper(DateUtility dateUtility, ConversionUtility conversionUtility) {
        this.dateUtility = dateUtility;
        this.conversionUtility = conversionUtility;
    }

    /**
     * ページに反映
     *
     * @param administratorEntity 運営者詳細エンティティ
     * @param model               運営者情報修正入力画面
     */
    public void toPageForLoad(AdministratorEntity administratorEntity, AdministratorUpdateModel model) {

        if (administratorEntity != null) {

            model.setAdministratorSeq(administratorEntity.getAdministratorSeq());
            model.setAdministratorId(administratorEntity.getAdministratorId());
            model.setAdministratorFirstName(administratorEntity.getAdministratorFirstName());
            model.setAdministratorLastName(administratorEntity.getAdministratorLastName());
            model.setAdministratorFirstKana(administratorEntity.getAdministratorFirstKana());
            model.setAdministratorLastKana(administratorEntity.getAdministratorLastKana());
            model.setAdministratorMail(administratorEntity.getMail());

            model.setAdministratorStatus(administratorEntity.getAdministratorStatus().getValue());
            model.setUseStartDate(administratorEntity.getUseStartDate());
            model.setUseEndDate(administratorEntity.getUseEndDate());

            if (!model.isEditFlag()) {
                model.setOldAdministratorStatus(administratorEntity.getAdministratorStatus().getValue());
                model.setOldUseStartDate(administratorEntity.getUseStartDate());
                model.setOldUseEndDate(administratorEntity.getUseEndDate());
            }

            model.setAdministratorGroupSeq(administratorEntity.getAdminAuthGroupSeq().toString());
            model.setAdministratorGroupName(administratorEntity.getAdminAuthGroup().getAuthGroupDisplayName());

            model.setPasswordChangeTime(administratorEntity.getPasswordChangeTime());
            model.setPasswordExpiryDate(dateUtility.formatYmdWithSlash(administratorEntity.getPasswordExpiryDate()));

            // 確認画面からの遷移時はロック解除で値が変わっている可能性があるため、元の値から取得する
            if (model.isEditFlag()) {
                model.setAccountLockTime(model.getOriginalEntity().getAccountLockTime());
                model.setLoginFailureCount(model.getOriginalEntity().getLoginFailureCount());
            } else {
                model.setAccountLockTime(administratorEntity.getAccountLockTime());
                model.setLoginFailureCount(administratorEntity.getLoginFailureCount());
            }

            // Get the maximum count for entering the invalid password
            int maxLoginFailureCount = PropertiesUtil.getSystemPropertiesValueToInt("admin.account.lock.count");

            // Check whether account is locked or not
            if (model.getAccountLockTime() != null && model.getLoginFailureCount() >= maxLoginFailureCount) {
                model.setLoginFailureAccountLock(true);
            } else if (administratorEntity.getPasswordExpiryDate() != null) {
                // Get the next day of password expiry date
                Timestamp nextDayOfPwdExpiryDate =
                                dateUtility.getAmountDayTimestamp(1, true, administratorEntity.getPasswordExpiryDate());
                if (dateUtility.isBeforeCurrentDate(nextDayOfPwdExpiryDate)) {
                    model.setPwdExpiredAccountLock(true);
                }
            } else {
                model.setLoginFailureAccountLock(false);
                model.setPwdExpiredAccountLock(false);
            }

            // パスワード変更要求フラグ
            model.setPasswordNeedChangeFlag(administratorEntity.getPasswordNeedChangeFlag());
            if (model.getPasswordNeedChangeFlag() == HTypePasswordNeedChangeFlag.ON) {
                model.setPasswordNeedChange(true);
            }
        }

        // 運営者詳細DTO
        model.setModifiedEntity(administratorEntity);

        // 確認画面から遷移した場合は作成しない、初回のみ
        if (!model.isEditFlag()) {
            // 運営者エンティティ。修正箇所比較用に持っておく。
            AdministratorEntity administratorEntityCopy = CopyUtil.deepCopy(administratorEntity);
            model.setOriginalEntity(administratorEntityCopy);
        }
    }

    /**
     * 画面から運営者情報エンティティに変換
     *
     * @param model 運営者情報変更入力画面
     * @return 運営者情報エンティティ
     */
    public AdministratorEntity toAdministratorEntityFromAdministratorUpdateModel(AdministratorUpdateModel model) {

        for (Map.Entry<String, String> entry : model.getAdministratorGroupSeqItems().entrySet()) {
            if (entry.getKey().equalsIgnoreCase(model.getAdministratorGroupSeq())) {
                model.setAdministratorGroupName(entry.getValue());
                break;
            }
        }

        AdministratorEntity admin = ApplicationContextUtility.getBean(AdministratorEntity.class);
        admin.setShopSeq(model.getOriginalEntity().getShopSeq());
        admin.setAdministratorSeq(model.getOriginalEntity().getAdministratorSeq());
        admin.setAdministratorStatus(
                        EnumTypeUtil.getEnumFromValue(HTypeAdministratorStatus.class, model.getAdministratorStatus()));
        admin.setAdministratorId(model.getAdministratorId());
        admin.setAdministratorPassword(model.getAdministratorPassword());
        admin.setMail(model.getAdministratorMail());
        admin.setAdministratorLastName(model.getAdministratorLastName());
        admin.setAdministratorFirstName(model.getAdministratorFirstName());
        admin.setAdministratorLastKana(model.getAdministratorLastKana());
        admin.setAdministratorFirstKana(model.getAdministratorFirstKana());

        if (!model.getAdministratorStatus().equals(model.getOldAdministratorStatus())) {

            // 日付関連Helper取得
            DateUtility dateUtility = ApplicationContextUtility.getBean(DateUtility.class);

            Timestamp currentTime = dateUtility.getCurrentTime();
            if (HTypeAdministratorStatus.USE.equals(admin.getAdministratorStatus())) {
                admin.setUseStartDate(currentTime);
                admin.setUseEndDate(null);
            } else {
                admin.setUseStartDate(model.getOldUseStartDate());
                admin.setUseEndDate(currentTime);
            }
        } else {
            admin.setUseStartDate(model.getOldUseStartDate());
            admin.setUseEndDate(model.getOldUseEndDate());
        }

        admin.setRegistTime(model.getOriginalEntity().getRegistTime());
        admin.setUpdateTime(model.getOriginalEntity().getUpdateTime());

        admin.setAdminAuthGroupSeq(Integer.parseInt(model.getAdministratorGroupSeq()));
        admin.setAdminAuthGroup(ApplicationContextUtility.getBean(AdminAuthGroupEntity.class));
        admin.getAdminAuthGroup().setAdminAuthGroupSeq(Integer.parseInt(model.getAdministratorGroupSeq()));
        admin.getAdminAuthGroup().setAuthGroupDisplayName(model.getAdministratorGroupName());

        // パスワード変更要求フラグ
        model.setPasswordNeedChangeFlag(HTypePasswordNeedChangeFlag.getFlagByBoolean(model.isPasswordNeedChange()));

        // ロック情報
        if (!(StringUtil.isEmpty(model.getAdministratorPassword()))) {
            admin.setLoginFailureCount(RESET_LOGIN_FAILURE_COUNT);
            admin.setAccountLockTime(null);
            admin.setPasswordNeedChangeFlag(HTypePasswordNeedChangeFlag.ON);
        } else {
            admin.setAccountLockTime(model.getAccountLockTime());
            admin.setLoginFailureCount(model.getLoginFailureCount());
            admin.setPasswordNeedChangeFlag(model.getPasswordNeedChangeFlag());
        }

        admin.setPasswordChangeTime(model.getPasswordChangeTime());
        admin.setPasswordExpiryDate(model.getOriginalEntity().getPasswordExpiryDate());

        // パスワードSHA256暗号化済みフラグの取得追加
        admin.setPasswordSHA256EncryptedFlag(model.getOriginalEntity().getPasswordSHA256EncryptedFlag());

        return admin;
    }

    /**
     * 運営者更新リクエストに反映
     *
     * @param administratorEntity 運営者エンティティ
     * @return 運営者更新リクエスト
     */
    public AdministratorUpdateRequest toAdministratorUpdateRequestFromAdministratorEntity(AdministratorEntity administratorEntity) {

        AdministratorUpdateRequest administratorUpdateRequest = new AdministratorUpdateRequest();

        administratorUpdateRequest.setAdministratorStatus(administratorEntity.getAdministratorStatus().getValue());
        administratorUpdateRequest.setAdministratorId(administratorEntity.getAdministratorId());
        administratorUpdateRequest.setAdministratorPassword(administratorEntity.getAdministratorPassword());
        administratorUpdateRequest.setMail(administratorEntity.getMail());
        administratorUpdateRequest.setAdministratorLastName(administratorEntity.getAdministratorLastName());
        administratorUpdateRequest.setAdministratorFirstName(administratorEntity.getAdministratorFirstName());
        administratorUpdateRequest.setAdministratorLastKana(administratorEntity.getAdministratorLastKana());
        administratorUpdateRequest.setAdministratorFirstKana(administratorEntity.getAdministratorFirstKana());
        administratorUpdateRequest.setUseStartDate(
                        conversionUtility.toTimestamp(administratorEntity.getUseStartDate()));
        administratorUpdateRequest.setUseEndDate(conversionUtility.toTimestamp(administratorEntity.getUseEndDate()));
        administratorUpdateRequest.setAdminAuthGroupSeq(administratorEntity.getAdminAuthGroupSeq());
        administratorUpdateRequest.setPasswordChangeTime(
                        conversionUtility.toTimestamp(administratorEntity.getPasswordChangeTime()));
        administratorUpdateRequest.setPasswordExpiryDate(
                        conversionUtility.toTimestamp(administratorEntity.getPasswordExpiryDate()));
        administratorUpdateRequest.setLoginFailureCount(administratorEntity.getLoginFailureCount());
        administratorUpdateRequest.setAccountLockTime(
                        conversionUtility.toTimestamp(administratorEntity.getAccountLockTime()));
        administratorUpdateRequest.setPasswordNeedChangeFlag(
                        administratorEntity.getPasswordNeedChangeFlag().getValue());
        administratorUpdateRequest.setPasswordSHA256EncryptedFlag(
                        administratorEntity.getPasswordSHA256EncryptedFlag().getValue());
        administratorUpdateRequest.setAdminAuthGroup(
                        toAdminAuthGroupRequestFromAdminAuthGroupEntity(administratorEntity.getAdminAuthGroup()));

        return administratorUpdateRequest;
    }

    /**
     * ページに反映
     *
     * @param adminAuthGroupEntity 権限グループ
     * @return 権限グループ
     */
    private AdminAuthGroupRequest toAdminAuthGroupRequestFromAdminAuthGroupEntity(AdminAuthGroupEntity adminAuthGroupEntity) {

        AdminAuthGroupRequest adminAuthGroupRequest = new AdminAuthGroupRequest();

        adminAuthGroupRequest.setAdminAuthGroupSeq(adminAuthGroupEntity.getAdminAuthGroupSeq());
        adminAuthGroupRequest.setAuthGroupDisplayName(adminAuthGroupEntity.getAuthGroupDisplayName());
        adminAuthGroupRequest.setAdminAuthGroupDetailList(
                        toAdminAuthGroupDetailList(adminAuthGroupEntity.getAdminAuthGroupDetailList()));
        adminAuthGroupRequest.setUnmodifiableGroup(adminAuthGroupEntity.getUnmodifiableGroup());

        return adminAuthGroupRequest;
    }

    /**
     * ページに反映
     *
     * @param adminAuthGroupDetailList 運営者権限グループ詳細エンティティリスト
     * @return 運営者権限グループ詳細リスト
     */
    private List<AdminAuthGroupDetailRequest> toAdminAuthGroupDetailList(List<AdminAuthGroupDetailEntity> adminAuthGroupDetailList) {

        if (adminAuthGroupDetailList == null) {
            return null;
        }

        List<AdminAuthGroupDetailRequest> adminAuthGroupDetailEntities = new ArrayList<>();

        for (AdminAuthGroupDetailEntity adminAuthGroupDetailEntity : adminAuthGroupDetailList) {

            AdminAuthGroupDetailRequest adminAuthGroupDetailRequest = new AdminAuthGroupDetailRequest();

            adminAuthGroupDetailRequest.setAdminAuthGroupSeq(adminAuthGroupDetailEntity.getAdminAuthGroupSeq());
            adminAuthGroupDetailRequest.setAuthTypeCode(adminAuthGroupDetailEntity.getAuthTypeCode());
            adminAuthGroupDetailRequest.setAuthLevel(adminAuthGroupDetailEntity.getAuthLevel());

            adminAuthGroupDetailEntities.add(adminAuthGroupDetailRequest);

        }

        return adminAuthGroupDetailEntities;
    }

}