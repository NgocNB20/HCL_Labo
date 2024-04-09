/*
 * Project Name : HIT-MALL4
 *
 * Copyright (C) 2021 i-TEC HANKYU HANSHIN INC. All Rights Reserved.
 *
 */
package jp.co.itechh.quad.authorization.presentation.api;

import jp.co.itechh.quad.authorization.presentation.api.param.AdminAuthGroupDetailRequest;
import jp.co.itechh.quad.authorization.presentation.api.param.AdminAuthGroupDetailResponse;
import jp.co.itechh.quad.authorization.presentation.api.param.AdminAuthGroupRequest;
import jp.co.itechh.quad.authorization.presentation.api.param.AuthPageItemRequest;
import jp.co.itechh.quad.authorization.presentation.api.param.AuthorizationRegistRequest;
import jp.co.itechh.quad.authorization.presentation.api.param.AuthorizationResponse;
import jp.co.itechh.quad.authorization.presentation.api.param.AuthorizationSubResponse;
import jp.co.itechh.quad.authorization.presentation.api.param.AuthorizationUpdateRequest;
import jp.co.itechh.quad.core.base.util.common.CollectionUtil;
import jp.co.itechh.quad.core.entity.administrator.AdminAuthGroupDetailEntity;
import jp.co.itechh.quad.core.entity.administrator.AdminAuthGroupEntity;
import org.apache.commons.lang3.ObjectUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Component;

import java.util.ArrayList;
import java.util.List;

/**
 * 権限エンドポイント Helper
 *
 * @author Nguyen Hong Son (VTI)
 */
@Component
public class AuthorizationHelper {

    /** ロガー */
    private static final Logger LOGGER = LoggerFactory.getLogger(AuthorizationHelper.class);

    /**
     * レスポンスに変換
     *
     * @param adminAuthGroupEntity 運営者権限グループエンティティ
     * @return 権限グループレスポンス
     */
    public AuthorizationResponse toAuthorizationResponse(AdminAuthGroupEntity adminAuthGroupEntity) {
        if (ObjectUtils.isEmpty(adminAuthGroupEntity)) {
            return null;
        }
        AuthorizationResponse authorizationResponse = new AuthorizationResponse();

        authorizationResponse.setAdminAuthGroupSeq(adminAuthGroupEntity.getAdminAuthGroupSeq());
        authorizationResponse.setAuthGroupDisplayName(adminAuthGroupEntity.getAuthGroupDisplayName());
        authorizationResponse.setRegistTime(adminAuthGroupEntity.getRegistTime());
        authorizationResponse.setUpdateTime(adminAuthGroupEntity.getUpdateTime());
        authorizationResponse.setAdminAuthGroupDetailList(
                        toAdminAuthGroupDetailList(adminAuthGroupEntity.getAdminAuthGroupDetailList()));
        authorizationResponse.setUnmodifiableGroup(adminAuthGroupEntity.getUnmodifiableGroup());

        return authorizationResponse;
    }

    /**
     * リスト応答に変換
     *
     * @param adminAuthGroupDetailList 運営者権限グループ詳細エンティティのリスト
     * @return 運営者権限グループ詳細 list
     */
    public List<AdminAuthGroupDetailResponse> toAdminAuthGroupDetailList(List<AdminAuthGroupDetailEntity> adminAuthGroupDetailList) {
        List<AdminAuthGroupDetailResponse> adminAuthGroupDetailListResponse = new ArrayList<>();

        for (AdminAuthGroupDetailEntity adminAuthGroupDetailEntity : adminAuthGroupDetailList) {
            AdminAuthGroupDetailResponse adminAuthGroupDetailResponse = new AdminAuthGroupDetailResponse();

            adminAuthGroupDetailResponse.setAdminAuthGroupSeq(adminAuthGroupDetailEntity.getAdminAuthGroupSeq());
            adminAuthGroupDetailResponse.setAuthTypeCode(adminAuthGroupDetailEntity.getAuthTypeCode());
            adminAuthGroupDetailResponse.setAuthLevel(adminAuthGroupDetailEntity.getAuthLevel());
            adminAuthGroupDetailResponse.setRegistTime(adminAuthGroupDetailEntity.getRegistTime());
            adminAuthGroupDetailResponse.setUpdateTime(adminAuthGroupDetailEntity.getUpdateTime());

            adminAuthGroupDetailListResponse.add(adminAuthGroupDetailResponse);
        }
        return adminAuthGroupDetailListResponse;
    }

    /**
     * エンティティに変換
     *
     * @param authorizationRegistRequest 権限グループ登録リクエスト
     * @return 運営者権限グループエンティティ
     */
    public AdminAuthGroupEntity toAdminAuthGroupEntityForRegist(AuthorizationRegistRequest authorizationRegistRequest) {
        AdminAuthGroupEntity group = new AdminAuthGroupEntity();

        group.setAuthGroupDisplayName(authorizationRegistRequest.getAuthGroupDisplayName());
        group.setAdminAuthGroupDetailList(new ArrayList<AdminAuthGroupDetailEntity>());

        for (AuthPageItemRequest item : authorizationRegistRequest.getAuthPageItemRegistRequest()) {
            AdminAuthGroupDetailEntity detail = new AdminAuthGroupDetailEntity();

            try {
                detail.setAuthTypeCode(item.getAuthTypeCode());
                detail.setAuthLevel(Integer.parseInt(item.getLevel()));
            } catch (NumberFormatException nfe) {
                LOGGER.error("例外処理が発生しました", nfe);
                // 数値に変換出来ない場合は権限レベル 0 が適用される
                detail.setAuthLevel(0);
                detail.setAuthTypeCode((String) (authorizationRegistRequest.getLevelItems().get(0).get("label")));
            }
            group.getAdminAuthGroupDetailList().add(detail);
        }
        return group;
    }

    /**
     * エンティティに変換
     *
     * @param authorizationUpdateRequest 権限グループ更新リクエスト
     * @return 運営者権限グループエンティティ
     */
    public AdminAuthGroupEntity toAdminAuthGroupEntityForUpdate(AuthorizationUpdateRequest authorizationUpdateRequest) {

        AdminAuthGroupRequest adminAuthGroupRequest = authorizationUpdateRequest.getAdminAuthGroupUpdateRequest();
        AdminAuthGroupEntity group = copyDataAdminAuthGroupRequest(adminAuthGroupRequest);

        group.setAuthGroupDisplayName(authorizationUpdateRequest.getAuthGroupDisplayName());
        group.setAdminAuthGroupDetailList(new ArrayList<AdminAuthGroupDetailEntity>());

        for (AuthPageItemRequest item : authorizationUpdateRequest.getAuthPageItemUpdateRequest()) {
            AdminAuthGroupDetailEntity detail = new AdminAuthGroupDetailEntity();

            try {
                detail.setAuthTypeCode(item.getAuthTypeCode());
                detail.setAuthLevel(Integer.parseInt(item.getLevel()));
            } catch (NumberFormatException nfe) {
                LOGGER.error("例外処理が発生しました", nfe);
                // 数値に変換出来ない場合は権限レベル 0 が適用される
                detail.setAuthLevel(0);
                detail.setAuthTypeCode((String) (authorizationUpdateRequest.getLevelItems().get(0).get("label")));
            }

            group.getAdminAuthGroupDetailList().add(detail);
        }
        return group;
    }

    /**
     * データをコピーする
     *
     * @param adminAuthGroupRequest 運営者権限グループリクエスト
     * @return 運営者権限グループエンティティ
     */
    public AdminAuthGroupEntity copyDataAdminAuthGroupRequest(AdminAuthGroupRequest adminAuthGroupRequest) {
        AdminAuthGroupEntity adminAuthGroupEntity = new AdminAuthGroupEntity();

        adminAuthGroupEntity.setShopSeq(1001);

        adminAuthGroupEntity.setAdminAuthGroupSeq(adminAuthGroupRequest.getAdminAuthGroupSeq());
        adminAuthGroupEntity.setAuthGroupDisplayName(adminAuthGroupRequest.getAuthGroupDisplayName());
        adminAuthGroupEntity.setAdminAuthGroupDetailList(
                        copyDataAdminAuthGroupDetailEntityList(adminAuthGroupRequest.getAdminAuthGroupDetailList()));
        adminAuthGroupEntity.setUnmodifiableGroup(adminAuthGroupRequest.getUnmodifiableGroup());

        return adminAuthGroupEntity;
    }

    /**
     * データをコピーする
     *
     * @param adminAuthGroupDetailList 権限グループ登録リクエスト
     * @return オペレーター権限グループリスト詳細エンティティ
     */
    public List<AdminAuthGroupDetailEntity> copyDataAdminAuthGroupDetailEntityList(List<AdminAuthGroupDetailRequest> adminAuthGroupDetailList) {
        List<AdminAuthGroupDetailEntity> adminAuthGroupDetailEntityList = new ArrayList<>();

        for (AdminAuthGroupDetailRequest adminAuthGroupDetailRequest : adminAuthGroupDetailList) {

            AdminAuthGroupDetailEntity adminAuthGroupDetailEntity = new AdminAuthGroupDetailEntity();

            adminAuthGroupDetailEntity.setAdminAuthGroupSeq(adminAuthGroupDetailRequest.getAdminAuthGroupSeq());
            adminAuthGroupDetailEntity.setAuthTypeCode(adminAuthGroupDetailRequest.getAuthTypeCode());
            adminAuthGroupDetailEntity.setAuthLevel(adminAuthGroupDetailRequest.getAuthLevel());
            adminAuthGroupDetailEntityList.add(adminAuthGroupDetailEntity);
        }
        return adminAuthGroupDetailEntityList;
    }

    /**
     * 権限グループレスポンスのリストに変換
     *
     * @param authList 運営者権限グループエンティティのリスト
     * @return 権限グループレスポンスのリスト
     */
    public List<AuthorizationSubResponse> toAuthorizationResponseList(List<AdminAuthGroupEntity> authList) {
        List<AuthorizationSubResponse> authorizationSubResponseList = new ArrayList<>();

        if (CollectionUtil.isNotEmpty(authList)) {
            authList.forEach(item -> {
                AuthorizationSubResponse authorizationSubResponse = new AuthorizationSubResponse();

                authorizationSubResponse.setAdminAuthGroupSeq(item.getAdminAuthGroupSeq());
                authorizationSubResponse.setAuthGroupDisplayName(item.getAuthGroupDisplayName());
                authorizationSubResponse.setRegistTime(item.getRegistTime());
                authorizationSubResponse.setUpdateTime(item.getUpdateTime());
                authorizationSubResponse.setAdminAuthGroupDetailList(
                                toAdminAuthGroupDetailResponseList(item.getAdminAuthGroupDetailList()));
                authorizationSubResponse.setUnmodifiableGroup(item.getUnmodifiableGroup());

                authorizationSubResponseList.add(authorizationSubResponse);
            });
        }

        return authorizationSubResponseList;
    }

    /**
     * 権限グループレスポンスのリストに変換
     *
     * @param adminAuthGroupDetailList 運営者権限グループ詳細エンティティのリスト
     * @return 権限グループレスポンスのリスト
     */
    public List<AdminAuthGroupDetailResponse> toAdminAuthGroupDetailResponseList(List<AdminAuthGroupDetailEntity> adminAuthGroupDetailList) {
        List<AdminAuthGroupDetailResponse> adminAuthGroupDetailResponseList = new ArrayList<>();

        if (CollectionUtil.isNotEmpty(adminAuthGroupDetailList)) {
            adminAuthGroupDetailList.forEach(item -> {
                AdminAuthGroupDetailResponse adminAuthGroupDetailResponse = new AdminAuthGroupDetailResponse();

                adminAuthGroupDetailResponse.setAdminAuthGroupSeq(item.getAdminAuthGroupSeq());
                adminAuthGroupDetailResponse.setAuthTypeCode(item.getAuthTypeCode());
                adminAuthGroupDetailResponse.setAuthLevel(item.getAuthLevel());
                adminAuthGroupDetailResponse.setRegistTime(item.getRegistTime());
                adminAuthGroupDetailResponse.setUpdateTime(item.getUpdateTime());

                adminAuthGroupDetailResponseList.add(adminAuthGroupDetailResponse);
            });
        }

        return adminAuthGroupDetailResponseList;
    }
}