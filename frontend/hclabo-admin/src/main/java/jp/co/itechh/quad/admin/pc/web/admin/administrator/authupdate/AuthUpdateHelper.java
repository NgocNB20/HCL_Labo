/*
 * Project Name : HIT-MALL3
 *
 * Copyright (C) 2008 i-TEC HANKYU HANSHIN INC. All Rights Reserved.
 *
 */

package jp.co.itechh.quad.admin.pc.web.admin.administrator.authupdate;

import jp.co.itechh.quad.admin.base.util.common.CopyUtil;
import jp.co.itechh.quad.admin.base.utility.ApplicationContextUtility;
import jp.co.itechh.quad.admin.dto.administrator.MetaAuthLevel;
import jp.co.itechh.quad.admin.dto.administrator.MetaAuthType;
import jp.co.itechh.quad.admin.entity.administrator.AdminAuthGroupDetailEntity;
import jp.co.itechh.quad.admin.entity.administrator.AdminAuthGroupEntity;
import jp.co.itechh.quad.authorization.presentation.api.param.AdminAuthGroupDetailRequest;
import jp.co.itechh.quad.authorization.presentation.api.param.AdminAuthGroupRequest;
import jp.co.itechh.quad.authorization.presentation.api.param.AuthPageItemRequest;
import jp.co.itechh.quad.authorization.presentation.api.param.AuthorizationUpdateRequest;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Component;

import java.util.ArrayList;
import java.util.Comparator;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * 権限グループ登録画面用 Dxo クラス
 *
 * @author tomo (itec) HM3.2 管理者権限対応（サービス＆ロジック統合及び DTO 改修含む)
 */
@Component
public class AuthUpdateHelper {

    /** ロガー */
    private static final Logger LOGGER = LoggerFactory.getLogger(AuthUpdateHelper.class);

    /**
     * 権限設定がない権限種別がある場合に適用する権限レベル
     */
    protected static final String DEFAULT_AUTH_LEVEL = "0";

    /**
     * doLoad 用ページ作成処理
     *
     * @param group    権限グループ
     * @param metaList メタ権限情報
     * @param model    ページ
     */
    public void toPageForLoad(AdminAuthGroupEntity group, List<MetaAuthType> metaList, AuthUpdateModel model) {

        // autItems が初期化済みの場合は初期化を行わない。
        if (model.getAuthItems() != null) {
            return;
        }

        model.setAuthGroupDisplayName(group.getAuthGroupDisplayName());
        model.setAuthItems(new ArrayList<>());

        // 変更前情報
        model.setOriginalEntity(group);
        model.setOriginalAuthItems(new ArrayList<>());

        // 権限種別毎のアイテムを作成する
        for (MetaAuthType dto : metaList) {

            // 権限種別アイテム
            AuthUpdateModelItem authItem = ApplicationContextUtility.getBean(AuthUpdateModelItem.class);

            // 権限種別名称
            authItem.setTypeDisplayName(dto.getTypeDisplayName());
            authItem.setAuthTypeCode(dto.getAuthTypeCode());
            authItem.setLevel(getSetLevelAsString(authItem.getAuthTypeCode(), group));

            //
            // 選択可能な権限レベル一覧を設定する
            //

            authItem.setLevelItems(new ArrayList<Map<String, ?>>());

            for (MetaAuthLevel level : dto.getMetaAuthLevelList()) {
                Map<String, ? super Object> map = new HashMap<>();
                map.put("label", level.getLevelDisplayName() + " ");
                map.put("value", level.getMetaLevel());
                authItem.getLevelItems().add(map);
            }

            //
            // 権限レベル強度順に権限レベルをソートする
            // ※DICON に設定されてる選択可能権限レベル一覧はレベル順にソートされてない可能性がある
            //

            authItem.getLevelItems().sort(new Comparator<Map<String, ?>>() {

                /**
                 * オブジェクトを比較する
                 * @param o1 比較オブジェクト1
                 * @param o2 比較オブジェクト2
                 * @return 比較結果
                 */
                @Override
                public int compare(Map<String, ?> o1, Map<String, ?> o2) {
                    Integer value1 = (Integer) o1.get("value");
                    Integer value2 = (Integer) o2.get("value");
                    return value1.compareTo(value2);
                }
            });

            model.getAuthItems().add(authItem);
            //            model.getOriginalAuthItems().add(CopyUtil.deepCopy(authItem));
            model.getOriginalAuthItems().add(authItem);

        }

    }

    /**
     * 権限グループに登録されている指定権限種別の設定権限レベルを（文字列として）取得する。
     *
     * @param authTypeCode 権限種別コード
     * @param group        対象の権限グループ
     * @return 設定権限レベル
     */
    protected String getSetLevelAsString(String authTypeCode, AdminAuthGroupEntity group) {

        for (AdminAuthGroupDetailEntity detail : group.getAdminAuthGroupDetailList()) {
            if (!detail.getAuthTypeCode().equals(authTypeCode)) {
                continue;
            }
            return detail.getAuthLevel().toString();
        }

        // 権限グループ詳細に設定されていない権限レベルが設定されていた場合は、権限レベル 0 を返す
        return DEFAULT_AUTH_LEVEL;
    }

    /**
     * 登録に使用する AdminAuthGroupEntity を作成する
     *
     * @param model ConfirmPage
     * @return 登録用 AdminAuthGroupEntity
     */
    public AdminAuthGroupEntity toAdminAuthGroupEntityForUpdate(AuthUpdateModel model) {

        AdminAuthGroupEntity group = CopyUtil.deepCopy(model.getOriginalEntity());

        group.setAuthGroupDisplayName(model.getAuthGroupDisplayName());
        group.setAdminAuthGroupDetailList(new ArrayList<AdminAuthGroupDetailEntity>());

        for (AuthUpdateModelItem item : model.getAuthItems()) {

            AdminAuthGroupDetailEntity detail = ApplicationContextUtility.getBean(AdminAuthGroupDetailEntity.class);

            try {
                detail.setAuthTypeCode(item.getAuthTypeCode());
                detail.setAuthLevel(Integer.parseInt(item.getLevel()));
            } catch (NumberFormatException nfe) {
                LOGGER.error("例外処理が発生しました", nfe);
                // 数値に変換出来ない場合は権限レベル 0 が適用される
                detail.setAuthLevel(0);
                detail.setAuthTypeCode((String) (item.getLevelItems().get(0).get("label")));
            }

            group.getAdminAuthGroupDetailList().add(detail);

        }

        return group;
    }

    /**
     * 権限レベルを設定
     *
     * @param authUpdateModel 権限グループ登録画面用
     */
    public void setLevelName(AuthUpdateModel authUpdateModel) {
        for (int i = 0; i < authUpdateModel.getAuthItems().size(); i++) {
            for (Map<String, ?> levelItem : authUpdateModel.getAuthItems().get(i).getLevelItems()) {
                String level = authUpdateModel.getAuthItems().get(i).getLevel();
                String itemLevel = levelItem.get("value").toString();
                String label = levelItem.get("label").toString();

                if (level.equals(itemLevel)) {
                    authUpdateModel.getAuthItems().get(i).setLevelName(label);
                }
            }
        }
    }

    /**
     * 権限グループ更新リクエストに変換
     *
     * @param adminAuthGroupEntity 運営者権限グループエンティティ
     * @return 権限グループ更新リクエスト
     */
    public AuthorizationUpdateRequest toAuthorizationUpdateRequest(AdminAuthGroupEntity adminAuthGroupEntity) {
        AuthorizationUpdateRequest authorizationUpdateRequest = new AuthorizationUpdateRequest();

        authorizationUpdateRequest.setAuthGroupDisplayName(adminAuthGroupEntity.getAuthGroupDisplayName());
        authorizationUpdateRequest.setAdminAuthGroupUpdateRequest(toAdminAuthGroupRequest(adminAuthGroupEntity));
        authorizationUpdateRequest.setAuthPageItemUpdateRequest(
                        toAuthPageItemRequestList(adminAuthGroupEntity.getAdminAuthGroupDetailList()));

        return authorizationUpdateRequest;
    }

    /**
     * 運営者権限グループリクエストに変換
     *
     * @param adminAuthGroupEntity 運営者権限グループエンティティ
     * @return 運営者権限グループリクエスト
     */
    public AdminAuthGroupRequest toAdminAuthGroupRequest(AdminAuthGroupEntity adminAuthGroupEntity) {
        AdminAuthGroupRequest adminAuthGroupRequest = new AdminAuthGroupRequest();

        adminAuthGroupRequest.setAdminAuthGroupSeq(adminAuthGroupEntity.getAdminAuthGroupSeq());
        adminAuthGroupRequest.setAuthGroupDisplayName(adminAuthGroupEntity.getAuthGroupDisplayName());
        adminAuthGroupRequest.setAdminAuthGroupDetailList(
                        toAdminAuthGroupDetailRequestList(adminAuthGroupEntity.getAdminAuthGroupDetailList()));
        adminAuthGroupRequest.setUnmodifiableGroup(adminAuthGroupEntity.getUnmodifiableGroup());

        return adminAuthGroupRequest;
    }

    /**
     * 権限グループ登録リクエストのリストに変換
     *
     * @param adminAuthGroupDetailList 運営者権限グループ詳細エンティティのリスト
     * @return 権限グループ登録リクエストのリスト
     */
    public List<AdminAuthGroupDetailRequest> toAdminAuthGroupDetailRequestList(List<AdminAuthGroupDetailEntity> adminAuthGroupDetailList) {
        List<AdminAuthGroupDetailRequest> adminAuthGroupDetailRequestList = new ArrayList<>();

        adminAuthGroupDetailList.forEach(item -> {
            AdminAuthGroupDetailRequest adminAuthGroupDetailRequest = new AdminAuthGroupDetailRequest();

            adminAuthGroupDetailRequest.setAdminAuthGroupSeq(item.getAdminAuthGroupSeq());
            adminAuthGroupDetailRequest.setAuthTypeCode(item.getAuthTypeCode());
            adminAuthGroupDetailRequest.setAuthLevel(item.getAuthLevel());

            adminAuthGroupDetailRequestList.add(adminAuthGroupDetailRequest);
        });

        return adminAuthGroupDetailRequestList;
    }

    /**
     * 権限グループ登録画面 PageItem クラスのリストに変換
     *
     * @param adminAuthGroupDetailList 運営者権限グループ詳細エンティティのリスト
     * @return 権限グループ登録画面 PageItem クラスのリスト
     */
    public List<AuthPageItemRequest> toAuthPageItemRequestList(List<AdminAuthGroupDetailEntity> adminAuthGroupDetailList) {
        List<AuthPageItemRequest> authPageItemRequestList = new ArrayList<>();

        adminAuthGroupDetailList.forEach(item -> {
            AuthPageItemRequest authPageItemRequest = new AuthPageItemRequest();

            authPageItemRequest.setLevel(String.valueOf(item.getAuthLevel()));
            authPageItemRequest.setAuthTypeCode(item.getAuthTypeCode());

            authPageItemRequestList.add(authPageItemRequest);
        });

        return authPageItemRequestList;
    }

}