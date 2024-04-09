/*
 * Project Name : HIT-MALL3
 *
 * Copyright (C) 2008 i-TEC HANKYU HANSHIN INC. All Rights Reserved.
 *
 */

package jp.co.itechh.quad.admin.pc.web.admin.administrator.auth;

import jp.co.itechh.quad.admin.base.util.common.CollectionUtil;
import jp.co.itechh.quad.admin.base.utility.ConversionUtility;
import jp.co.itechh.quad.admin.dto.administrator.MetaAuthLevel;
import jp.co.itechh.quad.admin.dto.administrator.MetaAuthType;
import jp.co.itechh.quad.admin.entity.administrator.AdminAuthGroupDetailEntity;
import jp.co.itechh.quad.admin.entity.administrator.AdminAuthGroupEntity;
import jp.co.itechh.quad.authorization.presentation.api.param.AdminAuthGroupDetailResponse;
import jp.co.itechh.quad.authorization.presentation.api.param.AuthorizationResponse;
import org.apache.commons.lang3.ObjectUtils;
import org.springframework.stereotype.Component;

import java.util.ArrayList;
import java.util.List;

/**
 * 権限グループ詳細画面 Helper クラス
 *
 * @author Pham Quang Dieu (VTI Japan Co., Ltd.)
 */
@Component
public class AdministratorAuthDetailHelper {

    /**
     * 変換ユーティリティクラス
     */
    private final ConversionUtility conversionUtility;

    /**
     * コンストラクター
     *
     * @param conversionUtility 変換ユーティリティクラス
     */
    public AdministratorAuthDetailHelper(ConversionUtility conversionUtility) {
        this.conversionUtility = conversionUtility;
    }

    /**
     * 初期表示用変換
     *
     * @param entity                       権限グループ情報
     * @param metaList                     メタ権限データ
     * @param administratorAuthDetailModel 変換後データ格納先ページ
     */
    public void toPageForLoad(AdminAuthGroupEntity entity,
                              List<MetaAuthType> metaList,
                              AdministratorAuthDetailModel administratorAuthDetailModel) {

        administratorAuthDetailModel.setAdminAuthGroupSeq(entity.getAdminAuthGroupSeq().toString());
        administratorAuthDetailModel.setAuthGroupDisplayName(entity.getAuthGroupDisplayName());
        administratorAuthDetailModel.setUnmodifiableGroup(entity.getUnmodifiableGroup());

        //
        // 権限グループに設定されている権限種別毎の表示情報を作成する
        //

        List<AdministratorAuthDetailModelItem> items = new ArrayList<>();

        for (AdminAuthGroupDetailEntity detail : entity.getAdminAuthGroupDetailList()) {

            String authTypeCode = detail.getAuthTypeCode();
            Integer authLevel = detail.getAuthLevel();

            //
            // authTypeCode に該当するメタ権限情報を探す
            //

            for (MetaAuthType meta : metaList) {

                if (!meta.getAuthTypeCode().equals(authTypeCode)) {
                    continue;
                }

                //
                // メタ権限情報を見つけた場合
                //

                AdministratorAuthDetailModelItem item = new AdministratorAuthDetailModelItem();

                // 権限種別名称を取得
                item.setAuthTypeName(meta.getTypeDisplayName());

                // 権限レベル名称を取得
                item.setAuthLevelName(getAuthLevelName(authLevel, meta));

                items.add(item);

                break;
            }

        }

        administratorAuthDetailModel.setDetailPageItems(items);

        // 不正操作対策の情報をセットする
        administratorAuthDetailModel.setScSeq(entity.getAdminAuthGroupSeq());
        administratorAuthDetailModel.setDbSeq(entity.getAdminAuthGroupSeq());
    }

    /**
     * 権限レベルの名称を取得する
     *
     * @param authLevel 権限レベル
     * @param meta      メタ権限設定
     * @return 権限レベル名称
     */
    protected String getAuthLevelName(Integer authLevel, MetaAuthType meta) {

        //
        // 対応する権限レベル設定名称を取得する
        //

        for (MetaAuthLevel dto : meta.getMetaAuthLevelList()) {

            if (!authLevel.equals(dto.getMetaLevel())) {
                continue;
            }

            return dto.getLevelDisplayName();
        }

        // AdminAuthGroupLogic#adjustAuthLevel() で不正な設定が入り込まないように制御されているため、
        // 通常ではここに来ることはない
        return "Level" + authLevel;
    }

    /**
     * 運営者権限グループエンティティに変換
     *
     * @param authorizationResponse 権限グループレスポンス
     * @param shopSeq               ショップSEQ
     * @return 運営者権限グループエンティティ
     */
    public AdminAuthGroupEntity toAdminAuthGroupEntity(AuthorizationResponse authorizationResponse, Integer shopSeq) {
        if (ObjectUtils.isEmpty(authorizationResponse)) {
            return null;
        }

        AdminAuthGroupEntity adminAuthGroupEntity = new AdminAuthGroupEntity();

        adminAuthGroupEntity.setAdminAuthGroupSeq(authorizationResponse.getAdminAuthGroupSeq());
        adminAuthGroupEntity.setShopSeq(shopSeq);
        adminAuthGroupEntity.setAuthGroupDisplayName(authorizationResponse.getAuthGroupDisplayName());
        adminAuthGroupEntity.setRegistTime(conversionUtility.toTimestamp(authorizationResponse.getRegistTime()));
        adminAuthGroupEntity.setUpdateTime(conversionUtility.toTimestamp(authorizationResponse.getUpdateTime()));
        adminAuthGroupEntity.setAdminAuthGroupDetailList(
                        toAdminAuthGroupDetailEntityList(authorizationResponse.getAdminAuthGroupDetailList()));
        adminAuthGroupEntity.setUnmodifiableGroup(authorizationResponse.getUnmodifiableGroup());

        return adminAuthGroupEntity;
    }

    /**
     * 運営者権限グループ詳細エンティティのリストに変換
     *
     * @param adminAuthGroupDetailResponseList 権限グループレスポンスのリスト
     * @return 運営者権限グループ詳細エンティティのリスト
     */
    public List<AdminAuthGroupDetailEntity> toAdminAuthGroupDetailEntityList(List<AdminAuthGroupDetailResponse> adminAuthGroupDetailResponseList) {
        List<AdminAuthGroupDetailEntity> adminAuthGroupDetailList = new ArrayList<>();

        if (CollectionUtil.isNotEmpty(adminAuthGroupDetailResponseList)) {
            adminAuthGroupDetailResponseList.forEach(item -> {
                AdminAuthGroupDetailEntity adminAuthGroupDetailEntity = new AdminAuthGroupDetailEntity();

                adminAuthGroupDetailEntity.setAdminAuthGroupSeq(item.getAdminAuthGroupSeq());
                adminAuthGroupDetailEntity.setAuthTypeCode(item.getAuthTypeCode());
                adminAuthGroupDetailEntity.setAuthLevel(item.getAuthLevel());
                adminAuthGroupDetailEntity.setRegistTime(conversionUtility.toTimestamp(item.getRegistTime()));
                adminAuthGroupDetailEntity.setUpdateTime(conversionUtility.toTimestamp(item.getUpdateTime()));

                adminAuthGroupDetailList.add(adminAuthGroupDetailEntity);
            });
        }

        return adminAuthGroupDetailList;
    }
}