/*
 * Project Name : HIT-MALL3
 *
 * Copyright (C) 2008 i-TEC HANKYU HANSHIN INC. All Rights Reserved.
 *
 */

package jp.co.itechh.quad.admin.pc.web.admin.administrator.authdelete;

import jp.co.itechh.quad.admin.dto.administrator.MetaAuthLevel;
import jp.co.itechh.quad.admin.dto.administrator.MetaAuthType;
import jp.co.itechh.quad.admin.entity.administrator.AdminAuthGroupDetailEntity;
import jp.co.itechh.quad.admin.entity.administrator.AdminAuthGroupEntity;
import org.springframework.stereotype.Component;

import java.util.ArrayList;
import java.util.List;

/**
 * 権限グループ削除確認画面 Dxo クラス
 *
 * @author Pham Quang Dieu (VTI Japan Co., Ltd.)
 */
@Component
public class AdministratorAuthDeleteHelper {

    /**
     * 初期表示用変換
     *
     * @param entity                       権限グループ情報
     * @param metaList                     メタ権限データ
     * @param administratorAuthDeleteModel 権限グループ削除確認画面
     */
    public void toPageForLoad(AdminAuthGroupEntity entity,
                              List<MetaAuthType> metaList,
                              AdministratorAuthDeleteModel administratorAuthDeleteModel) {

        administratorAuthDeleteModel.setAuthGroupDisplayName(entity.getAuthGroupDisplayName());
        administratorAuthDeleteModel.setUnmodifiableGroup(entity.getUnmodifiableGroup());

        List<AdministratorAuthDeleteModelItem> items = new ArrayList<>();

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

                // メタ権限情報を見つけた場合
                AdministratorAuthDeleteModelItem item = new AdministratorAuthDeleteModelItem();

                // 権限種別名称を取得
                item.setAuthTypeName(meta.getTypeDisplayName());

                // 権限レベル名称を取得
                item.setAuthLevelName(getAuthLevelName(authLevel, meta));

                items.add(item);

                break;
            }

        }

        administratorAuthDeleteModel.setDeletePageItems(items);

        // 不正操作対策の情報をセットする
        administratorAuthDeleteModel.setScSeq(Integer.valueOf(administratorAuthDeleteModel.getAdminAuthGroupSeq()));
        administratorAuthDeleteModel.setDbSeq(entity.getAdminAuthGroupSeq());

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
        // 対応する権限レベル設定を取得する
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
}