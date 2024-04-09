package jp.co.itechh.quad.authentication.presentation.api;

import jp.co.itechh.quad.authentication.presentation.api.param.AdminAuthGroupDetailtResponse;
import jp.co.itechh.quad.authentication.presentation.api.param.AdminAuthGroupResponse;
import jp.co.itechh.quad.authentication.presentation.api.param.AdminLoginResponse;
import jp.co.itechh.quad.authentication.presentation.api.param.MemberLoginResponse;
import jp.co.itechh.quad.authentication.presentation.api.param.MemberLoginUpdateResponse;
import jp.co.itechh.quad.authentication.presentation.api.param.RememberMeGetResponse;
import jp.co.itechh.quad.authentication.presentation.api.param.RememberMeRegistResponse;
import jp.co.itechh.quad.authentication.presentation.api.param.RememberMeUpdateResponse;
import jp.co.itechh.quad.core.entity.administrator.AdminAuthGroupDetailEntity;
import jp.co.itechh.quad.core.entity.administrator.AdministratorEntity;
import jp.co.itechh.quad.core.entity.authentication.PersistentRememberMeTokenEntity;
import jp.co.itechh.quad.core.entity.memberinfo.MemberInfoEntity;
import jp.co.itechh.quad.core.util.common.EnumTypeUtil;
import org.apache.commons.beanutils.BeanUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Component;

import java.lang.reflect.InvocationTargetException;
import java.util.ArrayList;
import java.util.List;

/**
 * 認証ヘルパー
 *
 * @author Doan Thang (VJP)
 */
@Component
public class AuthenticationHelper {

    /** ロガー */
    private static final Logger LOGGER = LoggerFactory.getLogger(AuthenticationHelper.class);

    /**
     * 管理者のログインレスポンスへの変換
     *
     * @param administratorEntity 管理者クラスエンティティ
     * @return 管理者のログインレスポンス
     */
    public AdminLoginResponse toAdminLoginResponse(AdministratorEntity administratorEntity) {
        AdminLoginResponse response = new AdminLoginResponse();
        response.setAdministratorSeq(administratorEntity.getAdministratorSeq());
        response.setAdministratorStatus(EnumTypeUtil.getValue(administratorEntity.getAdministratorStatus()));
        response.setAdministratorId(administratorEntity.getAdministratorId());
        response.setAdministratorPassword(administratorEntity.getAdministratorPassword());
        response.setMail(administratorEntity.getMail());
        response.setAdministratorLastName(administratorEntity.getAdministratorLastName());
        response.setAdministratorFirstName(administratorEntity.getAdministratorFirstName());
        response.setAdministratorLastKana(administratorEntity.getAdministratorLastKana());
        response.setAdministratorFirstKana(administratorEntity.getAdministratorFirstKana());
        response.setUseStartDate(administratorEntity.getUseStartDate());
        response.setUseEndDate(administratorEntity.getUseEndDate());
        response.setAdminAuthGroupSeq(administratorEntity.getAdminAuthGroupSeq());
        response.setRegistTime(administratorEntity.getRegistTime());
        response.setUpdateTime(administratorEntity.getUpdateTime());
        response.setPasswordChangeTime(administratorEntity.getPasswordChangeTime());
        response.setPasswordExpiryDate(administratorEntity.getPasswordExpiryDate());
        response.setLoginFailureCount(administratorEntity.getLoginFailureCount());
        response.setAccountLockTime(administratorEntity.getAccountLockTime());
        response.setPasswordNeedChangeFlag(EnumTypeUtil.getValue(administratorEntity.getPasswordNeedChangeFlag()));
        if (administratorEntity.getAdminAuthGroup() != null) {
            AdminAuthGroupResponse adminAuthGroupResponse = new AdminAuthGroupResponse();
            adminAuthGroupResponse.setAdminAuthGroupSeq(administratorEntity.getAdminAuthGroup().getAdminAuthGroupSeq());
            adminAuthGroupResponse.setAuthGroupDisplayName(
                            administratorEntity.getAdminAuthGroup().getAuthGroupDisplayName());
            adminAuthGroupResponse.setRegistTime(administratorEntity.getAdminAuthGroup().getRegistTime());
            adminAuthGroupResponse.setUpdateTime(administratorEntity.getAdminAuthGroup().getUpdateTime());
            adminAuthGroupResponse.setUnmodifiableGroup(administratorEntity.getAdminAuthGroup().getUnmodifiableGroup());

            if (administratorEntity.getAdminAuthGroup().getAdminAuthGroupDetailList() != null) {
                List<AdminAuthGroupDetailtResponse> adminAuthGroupDetailList = new ArrayList<>();
                for (AdminAuthGroupDetailEntity adminAuthGroupDetailEntity : administratorEntity.getAdminAuthGroup()
                                                                                                .getAdminAuthGroupDetailList()) {
                    AdminAuthGroupDetailtResponse adminAuthGroupDetailtResponse = new AdminAuthGroupDetailtResponse();
                    adminAuthGroupDetailtResponse.setAdminAuthGroupSeq(
                                    adminAuthGroupDetailEntity.getAdminAuthGroupSeq());
                    adminAuthGroupDetailtResponse.setAuthTypeCode(adminAuthGroupDetailEntity.getAuthTypeCode());
                    adminAuthGroupDetailtResponse.setAuthLevel(adminAuthGroupDetailEntity.getAuthLevel());
                    adminAuthGroupDetailtResponse.setRegistTime(adminAuthGroupDetailEntity.getRegistTime());
                    adminAuthGroupDetailtResponse.setUpdateTime(adminAuthGroupDetailEntity.getUpdateTime());
                }
                adminAuthGroupResponse.setAdminAuthGroupDetailList(adminAuthGroupDetailList);
            }
            response.setAdminAuthGroup(adminAuthGroupResponse);
        }

        return response;
    }

    /**
     * 会員のログインレスポンスへの変換
     *
     * @param memberInfoEntity 会員
     * @return 会員のログインレスポンス
     * @throws InvocationTargetException InvocationTargetException
     * @throws IllegalAccessException IllegalAccessException
     */
    public MemberLoginResponse toMemberLoginResponse(MemberInfoEntity memberInfoEntity) {
        MemberLoginResponse response = new MemberLoginResponse();

        try {
            BeanUtils.copyProperties(response, memberInfoEntity);
        } catch (IllegalAccessException | InvocationTargetException e) {
            LOGGER.error("例外処理が発生しました", e);
            return null;
        }

        return response;
    }

    /**
     * 会員のログイン日時の更新レスポンスへの変換
     *
     * @param memberInfoEntity 会員
     * @return 会員のログイン日時の更新レスポンス
     */
    public MemberLoginUpdateResponse toMemberLoginUpdateResponse(MemberInfoEntity memberInfoEntity) {
        MemberLoginUpdateResponse response = new MemberLoginUpdateResponse();
        response.setMemberInfoSeq(memberInfoEntity.getMemberInfoSeq());
        response.setUserAgent(memberInfoEntity.getLastLoginUserAgent());
        return response;
    }

    /**
     * RememberMe取得レスポンスへの変換
     *
     * @param entity PersistentRememberMeTokenEntity
     * @return RememberMe取得レスポンス
     */
    public RememberMeGetResponse toRememberMeGetResponse(PersistentRememberMeTokenEntity entity) {

        if (entity == null) {
            return null;
        }

        RememberMeGetResponse response = new RememberMeGetResponse();
        response.setUsername(entity.getUsername());
        response.setSeriesId(entity.getSeries());
        response.setToken(entity.getToken());
        response.setLastUsed(entity.getLast_used());

        return response;
    }

    /**
     * RememberMe登録レスポンスへの変換
     *
     * @param entity PersistentRememberMeTokenEntity
     * @return RememberMe登録レスポンス
     */
    public RememberMeRegistResponse toRememberMeRegistResponse(PersistentRememberMeTokenEntity entity) {
        RememberMeRegistResponse response = new RememberMeRegistResponse();
        response.setUsername(entity.getUsername());
        response.setSeriesId(entity.getSeries());
        response.setToken(entity.getToken());
        response.setLastUsed(entity.getLast_used());

        return response;
    }

    /**
     * RememberMe更新レスポンスへの変換
     *
     * @param entity PersistentRememberMeTokenEntity
     * @return RememberMe更新レスポンス
     */
    public RememberMeUpdateResponse toRememberMeUpdateResponse(PersistentRememberMeTokenEntity entity) {
        RememberMeUpdateResponse response = new RememberMeUpdateResponse();
        response.setUsername(entity.getUsername());
        response.setSeriesId(entity.getSeries());
        response.setToken(entity.getToken());
        response.setLastUsed(entity.getLast_used());

        return response;
    }
}