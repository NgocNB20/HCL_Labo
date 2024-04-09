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
import jp.co.itechh.quad.authorization.presentation.api.param.AuthorizationSubResponse;
import org.springframework.stereotype.Component;

import java.util.ArrayList;
import java.util.List;

/**
 * 権限グループ一覧画面 Helper クラス
 *
 * @author Pham Quang Dieu (VTI Japan Co., Ltd.)
 */
@Component
public class AdministratorAuthHelper {

    /**
     * 権限レベル 0 の別表示名
     */
    protected static final String LEVEL_ZERO_ALIAS = "－";

    /**
     * リストから配列へ変換する際に使用する型の定数
     */
    protected static final String[] STRING_ARRAY_TYPE = new String[] {};

    /**
     * 定数ゼロ
     */
    protected static final Integer ZERO = 0;

    /**
     * 変換ユーティリティクラス
     */
    private final ConversionUtility conversionUtility;

    /**
     * コンストラクター
     *
     * @param conversionUtility 変換ユーティリティクラス
     */
    public AdministratorAuthHelper(ConversionUtility conversionUtility) {
        this.conversionUtility = conversionUtility;
    }

    /**
     * 縦横可変サイズテーブルデータ作成
     *
     * @param authList               権限グループ一覧
     * @param metaList               権限種別一覧
     * @param administratorAuthModel 変換後データ設定先ページ
     */
    public void toPageForLoad(List<AdminAuthGroupEntity> authList,
                              List<MetaAuthType> metaList,
                              AdministratorAuthModel administratorAuthModel) {

        //
        // テーブルヘッダ行の列名称配列作成
        //

        List<String> authNameList = new ArrayList<>();
        authNameList.add(null); // ROW:0 かつ COL:0 のセルは行番号のためブランク
        authNameList.add(null); // ROW:0 かつ COL:1 のセルは権限グループ名称のためブランク

        for (MetaAuthType meta : metaList) {
            authNameList.add(meta.getTypeDisplayName());
        }

        administratorAuthModel.setLabelItems(authNameList.toArray(STRING_ARRAY_TYPE));

        //
        // テーブル本体配列の作成
        //

        // リンクのパラメータに使用する権限グループシーケンス
        List<Integer> adminSeqList = new ArrayList<>();

        // 行数
        int rows = authList.size();

        // 列数
        int cols = metaList.size();

        String[][] table = new String[rows][cols + 2];// +2 は 行番号,権限グループ名

        for (int row = 0; row < rows; row++) {

            AdminAuthGroupEntity auth = authList.get(row);

            // アンカ用パラメータ(権限グループseq)
            adminSeqList.add(auth.getAdminAuthGroupSeq());

            // 行番号列
            table[row][0] = Integer.toString(row + 1);

            // 権限グループ名称列
            table[row][1] = auth.getAuthGroupDisplayName();

            // 設定された権限レベルの名称を取得設定
            for (int col = 0; col < cols; col++) {
                table[row][col + 2] = getAuthName(metaList.get(col), auth.getAdminAuthGroupDetailList());
            }

        }

        administratorAuthModel.setAdminSeqList(adminSeqList);
        administratorAuthModel.setAuthItemsItems(table);

    }

    /**
     * 権限グループの特定権限種別に設定された権限レベル名称を取得する。
     *
     * @param meta       権限レベル名称を取得する対象のメタ権限種別データ
     * @param detailList 権限グループに設定された権限詳細                   何番目に目的の権限種別情報があるか不定のため、権限グループの詳細情報を全て取得する。
     * @return 権限レベルに対応した権限名称 auth name
     */
    protected String getAuthName(MetaAuthType meta, List<AdminAuthGroupDetailEntity> detailList) {

        // 権限グループ内の指定された権限種別の情報を見つけ出して対応する名称を返す
        for (AdminAuthGroupDetailEntity detail : detailList) {

            String metaCode = meta.getAuthTypeCode();
            String detailCode = detail.getAuthTypeCode();

            if (!metaCode.equals(detailCode)) {
                continue;
            }

            // 権限グループ詳細に設定された権限レベルを取得して…
            Integer authLevel = detail.getAuthLevel();

            if (ZERO.equals(authLevel)) {
                // 権限グループ一覧では権限レベル０は名称でなく別名を返す
                return LEVEL_ZERO_ALIAS;
            }

            // 該当するメタ権限レベル設定を取得して…
            for (MetaAuthLevel metaLevel : meta.getMetaAuthLevelList()) {

                if (authLevel.equals(metaLevel.getMetaLevel())) {
                    // 設定されている権限レベル名称を返す
                    return metaLevel.getLevelDisplayName();
                }

            }

            break;

        }

        // 取得できない場合
        return LEVEL_ZERO_ALIAS;
    }

    /**
     * 運営者権限グループエンティティのリストに変換
     *
     * @param authorizationSubResponseList 権限グループレスポンス
     * @param shopSeq                      ショップSEQ
     * @return 運営者権限グループエンティティのリスト
     */
    public List<AdminAuthGroupEntity> toAdminAuthGroupEntityList(List<AuthorizationSubResponse> authorizationSubResponseList,
                                                                 Integer shopSeq) {
        List<AdminAuthGroupEntity> adminAuthGroupEntityList = new ArrayList<>();

        if (CollectionUtil.isNotEmpty(authorizationSubResponseList)) {
            authorizationSubResponseList.forEach(item -> {
                AdminAuthGroupEntity adminAuthGroupEntity = new AdminAuthGroupEntity();

                adminAuthGroupEntity.setAdminAuthGroupSeq(item.getAdminAuthGroupSeq());
                adminAuthGroupEntity.setShopSeq(shopSeq);
                adminAuthGroupEntity.setAuthGroupDisplayName(item.getAuthGroupDisplayName());
                adminAuthGroupEntity.setRegistTime(conversionUtility.toTimestamp(item.getRegistTime()));
                adminAuthGroupEntity.setUpdateTime(conversionUtility.toTimestamp(item.getUpdateTime()));
                adminAuthGroupEntity.setAdminAuthGroupDetailList(
                                toAdminAuthGroupDetailList(item.getAdminAuthGroupDetailList()));
                adminAuthGroupEntity.setUnmodifiableGroup(item.getUnmodifiableGroup());

                adminAuthGroupEntityList.add(adminAuthGroupEntity);
            });
        }

        return adminAuthGroupEntityList;
    }

    /**
     * 運営者権限グループ詳細エンティティのリストに変換
     *
     * @param adminAuthGroupDetailList 権限グループレスポンスのリスト
     * @return 運営者権限グループ詳細エンティティのリスト
     */
    public List<AdminAuthGroupDetailEntity> toAdminAuthGroupDetailList(List<AdminAuthGroupDetailResponse> adminAuthGroupDetailList) {
        List<AdminAuthGroupDetailEntity> adminAuthGroupDetailEntityList = new ArrayList<>();

        if (CollectionUtil.isNotEmpty(adminAuthGroupDetailList)) {
            adminAuthGroupDetailList.forEach(item -> {
                AdminAuthGroupDetailEntity adminAuthGroupDetailEntity = new AdminAuthGroupDetailEntity();

                adminAuthGroupDetailEntity.setAdminAuthGroupSeq(item.getAdminAuthGroupSeq());
                adminAuthGroupDetailEntity.setAuthTypeCode(item.getAuthTypeCode());
                adminAuthGroupDetailEntity.setAuthLevel(item.getAuthLevel());
                adminAuthGroupDetailEntity.setRegistTime(conversionUtility.toTimestamp(item.getRegistTime()));
                adminAuthGroupDetailEntity.setUpdateTime(conversionUtility.toTimestamp(item.getUpdateTime()));

                adminAuthGroupDetailEntityList.add(adminAuthGroupDetailEntity);
            });
        }

        return adminAuthGroupDetailEntityList;
    }
}