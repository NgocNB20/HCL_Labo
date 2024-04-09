/*
 * Project Name : HIT-MALL3
 *
 * Copyright (C) 2008 i-TEC HANKYU HANSHIN INC. All Rights Reserved.
 *
 */

package jp.co.itechh.quad.admin.pc.web.admin.administrator;

import jp.co.itechh.quad.admin.base.util.common.PropertiesUtil;
import jp.co.itechh.quad.admin.base.utility.ApplicationContextUtility;
import jp.co.itechh.quad.admin.base.utility.ConversionUtility;
import jp.co.itechh.quad.admin.base.utility.DateUtility;
import jp.co.itechh.quad.admin.constant.type.HTypeAdministratorStatus;
import jp.co.itechh.quad.admin.constant.type.HTypePasswordNeedChangeFlag;
import jp.co.itechh.quad.admin.constant.type.HTypePasswordSHA256EncryptedFlag;
import jp.co.itechh.quad.admin.entity.administrator.AdminAuthGroupDetailEntity;
import jp.co.itechh.quad.admin.entity.administrator.AdminAuthGroupEntity;
import jp.co.itechh.quad.admin.entity.administrator.AdministratorEntity;
import jp.co.itechh.quad.admin.util.common.EnumTypeUtil;
import jp.co.itechh.quad.administrator.presentation.api.param.AdminAuthGroupDetailResponse;
import jp.co.itechh.quad.administrator.presentation.api.param.AdminAuthGroupResponse;
import jp.co.itechh.quad.administrator.presentation.api.param.AdministratorListResponse;
import jp.co.itechh.quad.administrator.presentation.api.param.AdministratorResponse;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.sql.Timestamp;
import java.util.ArrayList;
import java.util.List;

/**
 * 運営者検索・詳細・削除確認Helper
 *
 * @author Pham Quang Dieu (VTI Japan Co., Ltd.)
 */
@Component
public class AdministratorHelper {

    /**
     * 日付関連Helper取得
     */
    private final DateUtility dateUtility;

    /**
     * 変換ユーティリティクラス
     */
    private final ConversionUtility conversionUtility;

    /**
     * コントローラー
     *
     * @param dateUtility       日付関連Helper取得
     * @param conversionUtility 変換ユーティリティクラス
     */
    @Autowired
    public AdministratorHelper(DateUtility dateUtility, ConversionUtility conversionUtility) {
        this.dateUtility = dateUtility;
        this.conversionUtility = conversionUtility;
    }

    /**
     * 検索結果をページに反映
     *
     * @param administratorListResponse 運営者一覧レスポンス
     * @param model                     model
     */
    public void toPageForSearch(AdministratorListResponse administratorListResponse, AdministratorModel model) {

        List<AdministratorResponse> administratorList = administratorListResponse.getAdministratorList();

        if (administratorList != null) {
            // オフセット + 1をNoにセット
            int index = 1;
            List<AdministratorModelItem> resultItemList = new ArrayList<>();
            for (AdministratorResponse administratorResponse : administratorList) {
                AdministratorModelItem item = ApplicationContextUtility.getBean(AdministratorModelItem.class);
                item.setResultNo(index++);
                item.setAdministratorSeq(administratorResponse.getAdministratorSeq());
                item.setResultAdministratorId(administratorResponse.getAdministratorId());
                item.setResultAdministratorLastName(administratorResponse.getAdministratorLastName());
                item.setResultAdministratorFirstName(administratorResponse.getAdministratorFirstName());
                item.setResultAdministratorLastKana(administratorResponse.getAdministratorLastKana());
                item.setResultAdministratorFirstKana(administratorResponse.getAdministratorFirstKana());
                item.setResultMail(administratorResponse.getMail());
                item.setResultAdministratorStatus(EnumTypeUtil.getEnumFromValue(HTypeAdministratorStatus.class,
                                                                                administratorResponse.getAdministratorStatus()
                                                                               )
                                                              .getLabel());
                item.setResultUseStartDate(conversionUtility.toTimestamp(administratorResponse.getUseStartDate()));
                item.setResultUseEndDate(conversionUtility.toTimestamp(administratorResponse.getUseEndDate()));
                // 運営者には必ず所属権限グループがあるため null チェックは行わない
                item.setResultAdministratorGroupName(
                                administratorResponse.getAdminAuthGroup().getAuthGroupDisplayName());
                resultItemList.add(item);
            }
            model.setResultItems(resultItemList);
        }
    }

    /**
     * ページに反映
     *
     * @param administratorEntity 運営者エンティティ
     * @param model               model
     */
    public void toPageForLoadDetails(AdministratorEntity administratorEntity, AdministratorModel model) {

        if (administratorEntity == null) {
            return;
        }

        String administratorName = administratorEntity.getAdministratorLastName();
        if (administratorEntity.getAdministratorFirstName() != null) {
            administratorName = administratorName + " " + administratorEntity.getAdministratorFirstName();
        }
        String administratorKana = administratorEntity.getAdministratorLastKana();
        if (administratorEntity.getAdministratorFirstKana() != null) {
            administratorKana = administratorKana + " " + administratorEntity.getAdministratorFirstKana();
        }

        model.setAdministratorSeq(administratorEntity.getAdministratorSeq());
        model.setAdministratorId(administratorEntity.getAdministratorId());
        model.setAdministratorName(administratorName);
        model.setAdministratorKana(administratorKana);
        model.setAdministratorMail(administratorEntity.getMail());
        model.setAdministratorStatus(administratorEntity.getAdministratorStatus());
        model.setUseStartDate(administratorEntity.getUseStartDate());
        model.setUseEndDate(administratorEntity.getUseEndDate());

        // admin オブジェクトがあるのであれば権限グループ情報は必ずあるため null チェックは行わない
        model.setAdministratorGroupName(administratorEntity.getAdminAuthGroup().getAuthGroupDisplayName());
        model.setAdministratorEntity(administratorEntity);

        model.setPasswordChangeTime(administratorEntity.getPasswordChangeTime());
        model.setAccountLockTime(administratorEntity.getAccountLockTime());
        model.setLoginFailureCount(administratorEntity.getLoginFailureCount());
        model.setPasswordExpiryDate(dateUtility.formatYmdWithSlash(administratorEntity.getPasswordExpiryDate()));

        // Get the maximum count for entering the invalid password
        int maxLoginFailureCount = PropertiesUtil.getSystemPropertiesValueToInt("admin.account.lock.count");

        // Check whether account is locked or not
        if (administratorEntity.getAccountLockTime() != null
            && administratorEntity.getLoginFailureCount() >= maxLoginFailureCount) {
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
        HTypePasswordNeedChangeFlag passwordNeedChangeFlag = administratorEntity.getPasswordNeedChangeFlag();
        model.setPasswordNeedChangeFlag(
                        passwordNeedChangeFlag == null ? HTypePasswordNeedChangeFlag.OFF : passwordNeedChangeFlag);
    }

    /**
     * ページに反映
     *
     * @param administratorEntity 運営者エンティティ
     * @param model               運営者削除確認ページ
     */
    public void toPageForLoadDeleteConfirm(AdministratorEntity administratorEntity, AdministratorModel model) {

        if (administratorEntity != null) {

            String administratorName = administratorEntity.getAdministratorLastName();
            if (administratorEntity.getAdministratorFirstName() != null) {
                administratorName = administratorName + " " + administratorEntity.getAdministratorFirstName();
            }
            String administratorKana = administratorEntity.getAdministratorLastKana();
            if (administratorEntity.getAdministratorFirstKana() != null) {
                administratorKana = administratorKana + " " + administratorEntity.getAdministratorFirstKana();
            }

            model.setAdministratorSeq(administratorEntity.getAdministratorSeq());
            model.setAdministratorId(administratorEntity.getAdministratorId());
            model.setAdministratorFirstName(administratorEntity.getAdministratorFirstName());
            model.setAdministratorLastName(administratorEntity.getAdministratorLastName());
            model.setAdministratorName(administratorName);
            model.setAdministratorFirstKana(administratorEntity.getAdministratorFirstKana());
            model.setAdministratorLastKana(administratorEntity.getAdministratorLastKana());
            model.setAdministratorKana(administratorKana);
            model.setAdministratorMail(administratorEntity.getMail());
            model.setAdministratorStatus(administratorEntity.getAdministratorStatus());
            model.setUseStartDate(administratorEntity.getUseStartDate());
            model.setUseEndDate(administratorEntity.getUseEndDate());
            model.setAdministratorGroupName(administratorEntity.getAdminAuthGroup().getAuthGroupDisplayName());
        }

        // 運営者エンティティ
        model.setAdministratorEntity(administratorEntity);

    }

    /**
     * 運営者エンティティに反映
     *
     * @param administratorResponse 運営者レスポンス
     * @return 運営者エンティティ
     */
    public AdministratorEntity toAdministratorEntityFromAdministratorResponse(AdministratorResponse administratorResponse) {

        AdministratorEntity administratorEntity = new AdministratorEntity();

        administratorEntity.setAdministratorSeq(administratorResponse.getAdministratorSeq());
        administratorEntity.setAdministratorStatus(EnumTypeUtil.getEnumFromValue(HTypeAdministratorStatus.class,
                                                                                 administratorResponse.getAdministratorStatus()
                                                                                ));
        administratorEntity.setAdministratorId(administratorResponse.getAdministratorId());
        administratorEntity.setAdministratorPassword(administratorResponse.getAdministratorPassword());
        administratorEntity.setMail(administratorResponse.getMail());
        administratorEntity.setAdministratorLastName(administratorResponse.getAdministratorLastName());
        administratorEntity.setAdministratorFirstName(administratorResponse.getAdministratorFirstName());
        administratorEntity.setAdministratorFirstKana(administratorResponse.getAdministratorFirstKana());
        administratorEntity.setAdministratorLastKana(administratorResponse.getAdministratorLastKana());
        administratorEntity.setUseStartDate(conversionUtility.toTimestamp(administratorResponse.getUseStartDate()));
        administratorEntity.setUseEndDate(conversionUtility.toTimestamp(administratorResponse.getUseEndDate()));
        administratorEntity.setAdminAuthGroupSeq(administratorResponse.getAdminAuthGroupSeq());
        administratorEntity.setRegistTime(conversionUtility.toTimestamp(administratorResponse.getRegistTime()));
        administratorEntity.setUpdateTime(conversionUtility.toTimestamp(administratorResponse.getUpdateTime()));
        administratorEntity.setPasswordChangeTime(
                        conversionUtility.toTimestamp(administratorResponse.getPasswordChangeTime()));
        administratorEntity.setPasswordExpiryDate(
                        conversionUtility.toTimestamp(administratorResponse.getPasswordExpiryDate()));
        administratorEntity.setLoginFailureCount(administratorResponse.getLoginFailureCount());
        administratorEntity.setAccountLockTime(
                        conversionUtility.toTimestamp(administratorResponse.getAccountLockTime()));
        administratorEntity.setPasswordNeedChangeFlag(EnumTypeUtil.getEnumFromValue(HTypePasswordNeedChangeFlag.class,
                                                                                    administratorResponse.getPasswordNeedChangeFlag()
                                                                                   ));
        administratorEntity.setPasswordSHA256EncryptedFlag(
                        EnumTypeUtil.getEnumFromValue(HTypePasswordSHA256EncryptedFlag.class,
                                                      administratorResponse.getPasswordSHA256EncryptedFlag()
                                                     ));
        administratorEntity.setAdminAuthGroup(
                        toAdminAuthGroupEntityFromAdminAuthGroupResponse(administratorResponse.getAdminAuthGroup()));

        return administratorEntity;
    }

    /**
     * 権限グループエンティティに反映
     *
     * @param adminAuthGroupResponse AdminAuthGroupResponse
     * @return 権限グループ
     */
    private AdminAuthGroupEntity toAdminAuthGroupEntityFromAdminAuthGroupResponse(AdminAuthGroupResponse adminAuthGroupResponse) {

        if (adminAuthGroupResponse == null) {
            return null;
        }

        AdminAuthGroupEntity adminAuthGroupEntity = new AdminAuthGroupEntity();

        adminAuthGroupEntity.setAdminAuthGroupSeq(adminAuthGroupResponse.getAdminAuthGroupSeq());
        adminAuthGroupEntity.setAuthGroupDisplayName(adminAuthGroupResponse.getAuthGroupDisplayName());
        adminAuthGroupEntity.setRegistTime(conversionUtility.toTimestamp(adminAuthGroupResponse.getRegistTime()));
        adminAuthGroupEntity.setUpdateTime(conversionUtility.toTimestamp(adminAuthGroupResponse.getUpdateTime()));
        adminAuthGroupEntity.setAdminAuthGroupDetailList(toAdminAuthGroupDetailListFromAdminAuthGroupDetailResponseList(
                        adminAuthGroupResponse.getAdminAuthGroupDetailList()));
        adminAuthGroupEntity.setUnmodifiableGroup(adminAuthGroupResponse.getUnmodifiableGroup());

        return adminAuthGroupEntity;
    }

    /**
     * 運営者権限グループ詳細エンティティリストに反映
     *
     * @param adminAuthGroupDetailList AdminAuthGroupDetailResponseList
     * @return 運営者権限グループ詳細エンティティリスト
     */
    private List<AdminAuthGroupDetailEntity> toAdminAuthGroupDetailListFromAdminAuthGroupDetailResponseList(List<AdminAuthGroupDetailResponse> adminAuthGroupDetailList) {

        if (adminAuthGroupDetailList == null) {
            return null;
        }

        List<AdminAuthGroupDetailEntity> adminAuthGroupDetailEntities = new ArrayList<>();

        for (AdminAuthGroupDetailResponse adminAuthGroupDetailResponse : adminAuthGroupDetailList) {

            AdminAuthGroupDetailEntity adminAuthGroupDetailEntity = new AdminAuthGroupDetailEntity();

            adminAuthGroupDetailEntity.setAdminAuthGroupSeq(adminAuthGroupDetailResponse.getAdminAuthGroupSeq());
            adminAuthGroupDetailEntity.setAuthTypeCode(adminAuthGroupDetailResponse.getAuthTypeCode());
            adminAuthGroupDetailEntity.setAuthLevel(adminAuthGroupDetailResponse.getAuthLevel());
            adminAuthGroupDetailEntity.setRegistTime(
                            conversionUtility.toTimestamp(adminAuthGroupDetailResponse.getRegistTime()));
            adminAuthGroupDetailEntity.setUpdateTime(
                            conversionUtility.toTimestamp(adminAuthGroupDetailResponse.getUpdateTime()));

            adminAuthGroupDetailEntities.add(adminAuthGroupDetailEntity);

        }

        return adminAuthGroupDetailEntities;
    }

}