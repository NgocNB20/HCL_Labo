package jp.co.itechh.quad.administrator.presentation.api;

import jp.co.itechh.quad.administrator.presentation.api.param.AdminAuthGroupDetailRequest;
import jp.co.itechh.quad.administrator.presentation.api.param.AdminAuthGroupDetailResponse;
import jp.co.itechh.quad.administrator.presentation.api.param.AdminAuthGroupRequest;
import jp.co.itechh.quad.administrator.presentation.api.param.AdminAuthGroupResponse;
import jp.co.itechh.quad.administrator.presentation.api.param.AdministratorExistCheckResponse;
import jp.co.itechh.quad.administrator.presentation.api.param.AdministratorListGetRequest;
import jp.co.itechh.quad.administrator.presentation.api.param.AdministratorListResponse;
import jp.co.itechh.quad.administrator.presentation.api.param.AdministratorRegistRequest;
import jp.co.itechh.quad.administrator.presentation.api.param.AdministratorResponse;
import jp.co.itechh.quad.administrator.presentation.api.param.AdministratorSameCheckResponse;
import jp.co.itechh.quad.administrator.presentation.api.param.AdministratorUpdateRequest;
import jp.co.itechh.quad.core.base.utility.ApplicationContextUtility;
import jp.co.itechh.quad.core.base.utility.DateUtility;
import jp.co.itechh.quad.core.constant.type.HTypeAdministratorStatus;
import jp.co.itechh.quad.core.constant.type.HTypePasswordNeedChangeFlag;
import jp.co.itechh.quad.core.constant.type.HTypePasswordSHA256EncryptedFlag;
import jp.co.itechh.quad.core.dto.administrator.AdministratorSearchForDaoConditionDto;
import jp.co.itechh.quad.core.entity.administrator.AdminAuthGroupDetailEntity;
import jp.co.itechh.quad.core.entity.administrator.AdminAuthGroupEntity;
import jp.co.itechh.quad.core.entity.administrator.AdministratorEntity;
import jp.co.itechh.quad.core.util.common.EnumTypeUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Component;

import java.util.ArrayList;
import java.util.List;

/**
 * 運営者検索・詳細・削除確認Helper
 *
 * @author Pham Quang Dieu (VTI Japan Co., Ltd.)
 *
 */
@Component
public class AdministratorHelper {

    /** 日付関連Helper取得 */
    private final DateUtility dateUtility;

    /** パスワード暗号化 */
    private final PasswordEncoder passwordEncoder;

    /**
     * コンストラクタ
     *
     * @param dateUtility
     * @param passwordEncoder
     */
    @Autowired
    public AdministratorHelper(DateUtility dateUtility, PasswordEncoder passwordEncoder) {
        this.dateUtility = dateUtility;
        this.passwordEncoder = passwordEncoder;
    }

    public AdministratorResponse toAdministratorResponse(AdministratorEntity administratorEntity) {
        AdministratorResponse administratorResponse = new AdministratorResponse();
        administratorResponse.setAdministratorSeq(administratorEntity.getAdministratorSeq());
        administratorResponse.setAdministratorStatus(
                        EnumTypeUtil.getValue(administratorEntity.getAdministratorStatus()));
        administratorResponse.setAdministratorId(administratorEntity.getAdministratorId());
        administratorResponse.setAdministratorPassword(administratorEntity.getAdministratorPassword());
        administratorResponse.setMail(administratorEntity.getMail());
        administratorResponse.setAdministratorLastName(administratorEntity.getAdministratorLastName());
        administratorResponse.setAdministratorFirstName(administratorEntity.getAdministratorFirstName());
        administratorResponse.setAdministratorLastKana(administratorEntity.getAdministratorLastKana());
        administratorResponse.setAdministratorFirstKana(administratorEntity.getAdministratorFirstKana());
        administratorResponse.setUseStartDate(administratorEntity.getUseStartDate());
        administratorResponse.setUseEndDate(administratorEntity.getUseEndDate());
        administratorResponse.setAdminAuthGroupSeq(administratorEntity.getAdminAuthGroupSeq());
        administratorResponse.setRegistTime(administratorEntity.getRegistTime());
        administratorResponse.setUpdateTime(administratorEntity.getUpdateTime());
        administratorResponse.setPasswordChangeTime(administratorEntity.getPasswordChangeTime());
        administratorResponse.setPasswordExpiryDate(administratorEntity.getPasswordExpiryDate());
        administratorResponse.setLoginFailureCount(administratorEntity.getLoginFailureCount());
        administratorResponse.setAccountLockTime(administratorEntity.getAccountLockTime());
        administratorResponse.setPasswordNeedChangeFlag(
                        EnumTypeUtil.getValue(administratorEntity.getPasswordNeedChangeFlag()));
        administratorResponse.setPasswordSHA256EncryptedFlag(
                        EnumTypeUtil.getValue(administratorEntity.getPasswordSHA256EncryptedFlag()));
        administratorResponse.setAdminAuthGroup(toAdminAuthGroup(administratorEntity.getAdminAuthGroup()));
        return administratorResponse;
    }

    public AdministratorExistCheckResponse toAdministratorExistCheckResponse(Boolean administratorFlag) {
        AdministratorExistCheckResponse administratorExistCheckResponse = new AdministratorExistCheckResponse();
        administratorExistCheckResponse.setAdministratorFlag(administratorFlag);
        return administratorExistCheckResponse;
    }

    /**
     * 登録する運営者情報の作成<br/>
     *
     * @param administratorRegistRequest 運営者登録リクエスト
     * @return 登録する運営者情報
     */
    public AdministratorEntity toEntityForRegist(AdministratorRegistRequest administratorRegistRequest) {
        AdministratorEntity admin = ApplicationContextUtility.getBean(AdministratorEntity.class);

        admin.setShopSeq(1001);
        admin.setAdministratorId(administratorRegistRequest.getAdministratorId());

        if (administratorRegistRequest.getAdministratorPassword() != null) {
            admin.setAdministratorPassword(
                            passwordEncoder.encode(administratorRegistRequest.getAdministratorPassword()));
        }

        admin.setAdministratorLastName(administratorRegistRequest.getAdministratorLastName());
        admin.setAdministratorFirstName(administratorRegistRequest.getAdministratorFirstName());
        admin.setAdministratorLastKana(administratorRegistRequest.getAdministratorLastKana());
        admin.setAdministratorFirstKana(administratorRegistRequest.getAdministratorFirstKana());
        admin.setMail(administratorRegistRequest.getAdministratorMail());
        admin.setAdministratorStatus(EnumTypeUtil.getEnumFromValue(HTypeAdministratorStatus.class,
                                                                   administratorRegistRequest.getAdministratorStatus()
                                                                  ));
        admin.setAdminAuthGroupSeq(Integer.parseInt(administratorRegistRequest.getAdministratorGroupSeq()));
        admin.setUseStartDate(dateUtility.convertDateToTimestamp(administratorRegistRequest.getUseStartDate()));
        admin.setUseEndDate(dateUtility.convertDateToTimestamp(administratorRegistRequest.getUseEndDate()));
        HTypePasswordNeedChangeFlag passwordNeedChangeFlag =
                        EnumTypeUtil.getEnumFromValue(HTypePasswordNeedChangeFlag.class,
                                                      administratorRegistRequest.getPasswordNeedChangeFlag()
                                                     );
        admin.setPasswordNeedChangeFlag(passwordNeedChangeFlag);

        return admin;
    }

    private AdminAuthGroupResponse toAdminAuthGroup(AdminAuthGroupEntity adminAuthGroupEntity) {
        AdminAuthGroupResponse adminAuthGroupResponse = new AdminAuthGroupResponse();
        adminAuthGroupResponse.setAdminAuthGroupSeq(adminAuthGroupEntity.getAdminAuthGroupSeq());
        adminAuthGroupResponse.setAuthGroupDisplayName(adminAuthGroupEntity.getAuthGroupDisplayName());
        adminAuthGroupResponse.setRegistTime(adminAuthGroupEntity.getRegistTime());
        adminAuthGroupResponse.setUpdateTime(adminAuthGroupEntity.getUpdateTime());
        adminAuthGroupResponse.setAdminAuthGroupDetailList(
                        toListAdminAuthGroupDetailResponse(adminAuthGroupEntity.getAdminAuthGroupDetailList()));
        adminAuthGroupResponse.setUnmodifiableGroup(adminAuthGroupEntity.getUnmodifiableGroup());

        return adminAuthGroupResponse;
    }

    private List<AdminAuthGroupDetailResponse> toListAdminAuthGroupDetailResponse(List<AdminAuthGroupDetailEntity> adminAuthGroupDetailEntityList) {
        List<AdminAuthGroupDetailResponse> groupDetailResponseList = new ArrayList<>();
        adminAuthGroupDetailEntityList.forEach(groupDetailEntity -> {
            AdminAuthGroupDetailResponse groupDetailResponse = new AdminAuthGroupDetailResponse();
            groupDetailResponse.setAdminAuthGroupSeq(groupDetailEntity.getAdminAuthGroupSeq());
            groupDetailResponse.setAuthTypeCode(groupDetailEntity.getAuthTypeCode());
            groupDetailResponse.setAuthLevel(groupDetailEntity.getAuthLevel());
            groupDetailResponse.setRegistTime(groupDetailEntity.getRegistTime());
            groupDetailResponse.setUpdateTime(groupDetailEntity.getUpdateTime());
            groupDetailResponseList.add(groupDetailResponse);
        });
        return groupDetailResponseList;
    }

    public AdministratorSearchForDaoConditionDto getAdministratorSearchForDaoConditionDto(AdministratorListGetRequest administratorListGetRequest) {
        AdministratorSearchForDaoConditionDto administratorSearchForDaoConditionDto =
                        new AdministratorSearchForDaoConditionDto();
        administratorSearchForDaoConditionDto.setOrderField(administratorListGetRequest.getOrderField());
        administratorSearchForDaoConditionDto.setOrderAsc(administratorListGetRequest.getOrderAsc());
        administratorSearchForDaoConditionDto.setShopSeq(1001);
        return administratorSearchForDaoConditionDto;
    }

    public AdministratorListResponse toAdministratorListResponse(List<AdministratorEntity> administratorEntities) {
        AdministratorListResponse administratorListResponse = new AdministratorListResponse();
        for (AdministratorEntity administratorEntity : administratorEntities) {
            AdministratorResponse administratorResponse = toAdministratorResponse(administratorEntity);
            administratorListResponse.addAdministratorListItem(administratorResponse);
        }
        return administratorListResponse;
    }

    public AdministratorSameCheckResponse toAdministratorSameCheckResponse(Boolean administratorFlag) {
        AdministratorSameCheckResponse administratorSameCheckResponse = new AdministratorSameCheckResponse();
        administratorSameCheckResponse.setAdministratorFlag(administratorFlag);
        return administratorSameCheckResponse;
    }

    public void toAdministratorEntity(AdministratorEntity administratorEntity,
                                      AdministratorUpdateRequest administratorUpdateRequest) {
        administratorEntity.setAdministratorId(administratorUpdateRequest.getAdministratorId());

        if (administratorUpdateRequest.getAdministratorPassword() != null) {
            administratorEntity.setAdministratorPassword(
                            passwordEncoder.encode(administratorUpdateRequest.getAdministratorPassword()));
        }

        administratorEntity.setMail(administratorUpdateRequest.getMail());
        administratorEntity.setAdministratorLastName(administratorUpdateRequest.getAdministratorLastName());
        administratorEntity.setAdministratorFirstName(administratorUpdateRequest.getAdministratorFirstName());
        administratorEntity.setAdministratorLastKana(administratorUpdateRequest.getAdministratorLastKana());
        administratorEntity.setAdministratorFirstKana(administratorUpdateRequest.getAdministratorFirstKana());
        administratorEntity.setAdminAuthGroupSeq(administratorUpdateRequest.getAdminAuthGroupSeq());
        administratorEntity.setLoginFailureCount(administratorUpdateRequest.getLoginFailureCount());
        administratorEntity.setUseStartDate(
                        dateUtility.convertDateToTimestamp(administratorUpdateRequest.getUseStartDate()));
        administratorEntity.setUseEndDate(
                        dateUtility.convertDateToTimestamp(administratorUpdateRequest.getUseEndDate()));
        administratorEntity.setPasswordChangeTime(
                        dateUtility.convertDateToTimestamp(administratorUpdateRequest.getPasswordChangeTime()));
        administratorEntity.setPasswordExpiryDate(
                        dateUtility.convertDateToTimestamp(administratorUpdateRequest.getPasswordExpiryDate()));
        administratorEntity.setAccountLockTime(
                        dateUtility.convertDateToTimestamp(administratorUpdateRequest.getAccountLockTime()));
        HTypeAdministratorStatus administratorStatus = EnumTypeUtil.getEnumFromValue(HTypeAdministratorStatus.class,
                                                                                     administratorUpdateRequest.getAdministratorStatus()
                                                                                    );
        administratorEntity.setAdministratorStatus(administratorStatus);
        HTypePasswordNeedChangeFlag passwordNeedChangeFlag =
                        EnumTypeUtil.getEnumFromValue(HTypePasswordNeedChangeFlag.class,
                                                      administratorUpdateRequest.getPasswordNeedChangeFlag()
                                                     );
        administratorEntity.setPasswordNeedChangeFlag(passwordNeedChangeFlag);
        HTypePasswordSHA256EncryptedFlag passwordSHA256EncryptedFlag =
                        EnumTypeUtil.getEnumFromValue(HTypePasswordSHA256EncryptedFlag.class,
                                                      administratorUpdateRequest.getPasswordSHA256EncryptedFlag()
                                                     );
        administratorEntity.setPasswordSHA256EncryptedFlag(passwordSHA256EncryptedFlag);
        administratorEntity.setAdminAuthGroup(toAdminAuthGroupEntity(administratorUpdateRequest.getAdminAuthGroup()));
    }

    public AdminAuthGroupEntity toAdminAuthGroupEntity(AdminAuthGroupRequest adminAuthGroupRequest) {
        AdminAuthGroupEntity adminAuthGroupEntity = new AdminAuthGroupEntity();
        adminAuthGroupEntity.setAdminAuthGroupSeq(adminAuthGroupRequest.getAdminAuthGroupSeq());
        adminAuthGroupEntity.setAuthGroupDisplayName(adminAuthGroupRequest.getAuthGroupDisplayName());
        adminAuthGroupEntity.setUnmodifiableGroup(adminAuthGroupRequest.getUnmodifiableGroup());
        adminAuthGroupEntity.setAdminAuthGroupDetailList(
                        toAdminAuthGroupDetailEntityList(adminAuthGroupRequest.getAdminAuthGroupDetailList()));
        return adminAuthGroupEntity;
    }

    public List<AdminAuthGroupDetailEntity> toAdminAuthGroupDetailEntityList(List<AdminAuthGroupDetailRequest> adminAuthGroupDetailList) {

        if (adminAuthGroupDetailList == null) {
            return null;
        }

        List<AdminAuthGroupDetailEntity> adminAuthGroupDetailEntities = new ArrayList<>();
        for (AdminAuthGroupDetailRequest adminAuthGroupDetailRequest : adminAuthGroupDetailList) {
            AdminAuthGroupDetailEntity adminAuthGroupDetailEntity = new AdminAuthGroupDetailEntity();
            adminAuthGroupDetailEntity.setAdminAuthGroupSeq(adminAuthGroupDetailRequest.getAdminAuthGroupSeq());
            adminAuthGroupDetailEntity.setAuthTypeCode(adminAuthGroupDetailRequest.getAuthTypeCode());
            adminAuthGroupDetailEntity.setAuthLevel(adminAuthGroupDetailRequest.getAuthLevel());
            adminAuthGroupDetailEntities.add(adminAuthGroupDetailEntity);
        }
        return adminAuthGroupDetailEntities;
    }
}