/*
 * Project Name : HIT-MALL3
 *
 * Copyright (C) 2008 i-TEC HANKYU HANSHIN INC. All Rights Reserved.
 *
 */

package jp.co.itechh.quad.core.logic.administrator.impl;

import jp.co.itechh.quad.core.base.util.common.ArgumentCheckUtil;
import jp.co.itechh.quad.core.base.utility.ApplicationContextUtility;
import jp.co.itechh.quad.core.base.utility.DateUtility;
import jp.co.itechh.quad.core.dao.administrator.AdminAuthGroupDao;
import jp.co.itechh.quad.core.dao.administrator.AdminAuthGroupDetailDao;
import jp.co.itechh.quad.core.dto.administrator.MetaAuthLevel;
import jp.co.itechh.quad.core.dto.administrator.MetaAuthType;
import jp.co.itechh.quad.core.entity.administrator.AdminAuthGroupDetailEntity;
import jp.co.itechh.quad.core.entity.administrator.AdminAuthGroupEntity;
import jp.co.itechh.quad.core.logic.administrator.AdminAuthGroupLogic;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.util.CollectionUtils;

import java.sql.Timestamp;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

/**
 * 管理者権限グループ操作ロジック実装<br />
 * 次の機能を提供する
 *
 * <ul>
 * <li>権限グループ登録</li>
 * <li>権限グループ更新</li>
 * <li>権限グループ削除</li>
 * <li>権限グループ取得</li>
 * <li>権限グループ一覧取得</li>
 * <li>権限グループ詳細内容の補正</li>
 * </ul>
 *
 * <pre>
 * 権限グループ取得時には次の動作をする。
 * ・設定された権限グループ詳細がある場合、それらの情報を取得する
 * ・権限グループ詳細にメタ権限データ設定で定義されていない内容が含まれる場合、適切な内容に補正する。（詳細はメソッドコメントを確認)
 *
 * 権限グループ登録時には次の動作をする。
 * ・権限グループのほかに権限グループ詳細の情報を登録する。
 *
 * 権限グループ更新時には次の動作をする。
 * ・権限グループ更新後、権限グループ詳細を DELETE&INSERT する。
 *
 * 権限グループ削除時には次の動作をする。
 * ・権限グループ詳細を物理削除後、権限グループを削除する。
 * 　権限グループは運営者テーブルから外部キーで参照されているため、参照が残っている場合は削除されない。
 * </pre>
 * @author tomo (itec) HM3.2 管理者権限対応（サービス＆ロジック統合及び DTO 改修含む)
 * @author Kaneko (itec) 2012/08/21 UtilからHelperへ変更　Util使用箇所をgetComponentで取得するように変更
 *
 */
@Component
public class AdminAuthGroupLogicImpl implements AdminAuthGroupLogic {

    /** 権限グループ分類リスト用valueカラム名 */
    protected static final String VALUE_COLNAME = "adminauthgroupseq";

    /** 権限グループ分類リスト用ラベルカラム名 */
    protected static final String LABEL_COLNAME = "authgroupdisplayname";

    /** ロガー */
    private static final Logger LOGGER = LoggerFactory.getLogger(AdminAuthGroupLogicImpl.class);

    /** 定数ゼロ */
    protected static final Integer ZERO = 0;

    /** 運営者権限グループDAO */
    private final AdminAuthGroupDao groupDao;

    /** 運営者権限グループ詳細DAO */
    private final AdminAuthGroupDetailDao detailDao;

    @Autowired
    public AdminAuthGroupLogicImpl(AdminAuthGroupDao groupDao, AdminAuthGroupDetailDao detailDao) {
        this.groupDao = groupDao;
        this.detailDao = detailDao;
    }

    /**
     * 権限グループ詳細を含む権限グループを登録する。
     * @param group 権限グループ
     */
    @Override
    public void register(AdminAuthGroupEntity group) {

        ArgumentCheckUtil.assertNotNull("group", group);

        // 日付関連Helper取得
        DateUtility dateUtility = ApplicationContextUtility.getBean(DateUtility.class);
        Timestamp now = dateUtility.getCurrentTime();

        // 親レコードを登録
        group.setRegistTime(now);
        group.setUpdateTime(now);
        groupDao.insert(group);

        // 子レコードを登録
        for (AdminAuthGroupDetailEntity detail : group.getAdminAuthGroupDetailList()) {
            detail.setAdminAuthGroupSeq(group.getAdminAuthGroupSeq());
            detail.setRegistTime(now);
            detail.setUpdateTime(now);
            detailDao.insert(detail);
        }

    }

    /**
     * 権限グループ詳細を含む権限グループを更新する。
     * @param group 権限グループ
     */
    @Override
    public void update(AdminAuthGroupEntity group) {

        ArgumentCheckUtil.assertNotNull("group", group);

        // 日付関連Helper取得
        DateUtility dateUtility = ApplicationContextUtility.getBean(DateUtility.class);
        Timestamp now = dateUtility.getCurrentTime();

        // 親レコードを更新
        group.setUpdateTime(now);
        groupDao.update(group);

        // 既存子レコードを削除
        detailDao.deleteAll(group.getAdminAuthGroupSeq());

        // 子レコードを登録
        for (AdminAuthGroupDetailEntity detail : group.getAdminAuthGroupDetailList()) {
            detail.setAdminAuthGroupSeq(group.getAdminAuthGroupSeq());
            detail.setRegistTime(now);
            detail.setUpdateTime(now);
            detailDao.insert(detail);
        }
    }

    /**
     * 権限グループを削除する。
     * @param group 権限グループ
     */
    @Override
    public void delete(AdminAuthGroupEntity group) {

        ArgumentCheckUtil.assertNotNull("group", group);

        detailDao.deleteAll(group.getAdminAuthGroupSeq());
        groupDao.delete(group);
    }

    /**
     * 権限グループ詳細を含む権限グループを取得する。
     * @param adminAuthGroupSeq 管理者権限グループSEQ
     * @return 権限グループ
     */
    @Override
    public AdminAuthGroupEntity getAdminAuthGroup(Integer adminAuthGroupSeq) {

        ArgumentCheckUtil.assertNotNull("adminAuthGroupSeq", adminAuthGroupSeq);

        AdminAuthGroupEntity group = groupDao.getEntityBySeq(adminAuthGroupSeq);
        this.addAdminAuthGroupDetail(group);

        return group;
    }

    /**
     * 権限グループ詳細を含む権限グループを取得する。
     * @param shopSeq ショップSEQ
     * @param authGroupDisplayName 権限グループ名称
     * @return 権限グループ
     */
    @Override
    public AdminAuthGroupEntity getAdminAuthGroup(Integer shopSeq, String authGroupDisplayName) {

        ArgumentCheckUtil.assertNotNull("shopSeq", shopSeq);
        ArgumentCheckUtil.assertNotNull("authGroupDisplayName", authGroupDisplayName);

        AdminAuthGroupEntity group = groupDao.getEntityByName(shopSeq, authGroupDisplayName);
        this.addAdminAuthGroupDetail(group);

        return group;
    }

    /**
     * 指定されたショップの権限グループ一覧を取得する。<br />
     * 各権限グループには権限詳細が含まれる。
     * @param shopSeq ショップSEQ
     * @return 権限グループ一覧
     */
    @Override
    public List<AdminAuthGroupEntity> getAdminAuthGroupList(Integer shopSeq) {

        ArgumentCheckUtil.assertNotNull("shopSeq", shopSeq);

        List<AdminAuthGroupEntity> groupList = groupDao.getEntityList(shopSeq);
        for (AdminAuthGroupEntity group : groupList) {
            this.addAdminAuthGroupDetail(group);
        }

        return groupList;

    }

    /**
     * 権限レベルを適切な内容に補正する。
     * <ul>
     * <li>存在しない権限種別の権限が設定されている場合、その権限種別の情報は無効にする。</li>
     * <li>設定不能な権限レベルが設定されている場合、設定可能な下位権限レベルを設定しなおす。</li>
     * <li>無効な権限レベルが設定されている場合、無権限レベルを設定しなおす。</li>
     * </ul>
     * @param detailList 補正対象権限設定
     * @param adminAuthGroupSeq 即席で権限詳細を作成しなければならないときに使用する権限グループSEQ
     * @return 補正された権限設定
     */
    public List<AdminAuthGroupDetailEntity> adjustAuthLevel(List<AdminAuthGroupDetailEntity> detailList,
                                                            Integer adminAuthGroupSeq) {

        ArgumentCheckUtil.assertNotNull("detailList", detailList);
        ArgumentCheckUtil.assertNotNull("adminAuthGroupSeq", adminAuthGroupSeq);

        // 返却用リスト
        List<AdminAuthGroupDetailEntity> returnList = new ArrayList<>();

        // metAuthList が設定されていない場合（バッチを想定）
        List<MetaAuthType> metaAuthList = (List<MetaAuthType>) ApplicationContextUtility.getApplicationContext()
                                                                                        .getBean("metaAuthTypeList");
        if (CollectionUtils.isEmpty(metaAuthList)) {
            return detailList;
        }

        //
        // 個別権限種別の補正処理
        //
        for (MetaAuthType meta : metaAuthList) {

            boolean found = false;

            // メタ権限種別に合致する権限グループ権限種別を探す
            for (AdminAuthGroupDetailEntity detail : detailList) {

                String authTypeCode = detail.getAuthTypeCode();

                if (!authTypeCode.equals(meta.getAuthTypeCode())) {
                    continue;
                }

                found = true;

                // 適切な権限レベルの再設定
                detail.setAuthLevel(getEquivalentOrLowerLevel(detail.getAuthLevel(), meta));

                returnList.add(detail);

                break;
            }

            // メタ権限種別に登録されているが、権限グループに登録されていない場合は
            // 権限レベル 0 の権限グループ詳細を設定する
            if (!found) {
                AdminAuthGroupDetailEntity detail = ApplicationContextUtility.getBean(AdminAuthGroupDetailEntity.class);
                detail.setAdminAuthGroupSeq(adminAuthGroupSeq);
                detail.setAuthTypeCode(meta.getAuthTypeCode());
                detail.setAuthLevel(ZERO);
                returnList.add(detail);
            }
        }

        return returnList;
    }

    /**
     * 権限グループに権限グループ詳細情報を設定する。<br />
     * 規定権限グループ設定 DICON で上書き指定されている権限グループは、その設定内容を適用する。
     * @param group 権限グループ
     */
    protected void addAdminAuthGroupDetail(AdminAuthGroupEntity group) {

        // 権限グループが取得できていない場合は、詳細情報は取得できない
        if (group == null) {
            return;
        }

        // 規定権限グループとして上書き設定がされている場合はその設定を取得する
        List<AdminAuthGroupDetailEntity> detailList = null;

        // バッチでは adminConstantAuthGroupMap が null になる。
        Map<Integer, List<AdminAuthGroupDetailEntity>> adminConstantAuthGroupMap =
                        (Map<Integer, List<AdminAuthGroupDetailEntity>>) ApplicationContextUtility.getApplicationContext()
                                                                                                  .getBean("adminConstantAuthGroupMap");
        if (adminConstantAuthGroupMap != null) {
            detailList = adminConstantAuthGroupMap.get(group.getAdminAuthGroupSeq());
        }

        if (detailList != null) {

            // 上書き設定を適用した権限グループを返す
            group.setAdminAuthGroupDetailList(adjustAuthLevel(detailList, group.getAdminAuthGroupSeq()));

            // 規定権限グループが適応された場合、「変更不可」とする。
            group.setUnmodifiableGroup(Boolean.TRUE);

            return;
        }

        // 権限レベルの適正化
        detailList = detailDao.getDetailList(group.getAdminAuthGroupSeq());

        // 上書き設定がない場合、DB より詳細を取得してグループに適用する
        group.setAdminAuthGroupDetailList(adjustAuthLevel(detailList, group.getAdminAuthGroupSeq()));

        // 通常権限グループが適応された場合、「変更可能」とする。
        group.setUnmodifiableGroup(Boolean.FALSE);
    }

    /**
     * 選択可能な同等レベルもしくは下位権限レベルを返す。<br />
     * 選択できない権限グループが設定されている場合は、選択可能な下位レベルを返す。
     * @param authLevel 権限レベル
     * @param meta メタ権限データ
     * @return 権限レベル名称
     */
    protected Integer getEquivalentOrLowerLevel(Integer authLevel, MetaAuthType meta) {

        // 値が設定されていないもしくは、1-9 に値が収まらない場合は 0 を返す。
        if (authLevel == null || authLevel <= 0 || authLevel > 9) {
            return 0;
        }

        // 権限レベルに該当するメタ権限レベル設定を取得する
        for (MetaAuthLevel metaLevel : meta.getMetaAuthLevelList()) {

            // 指定されたレベルが設定可能な場合、そのレベルを返す
            if (authLevel.equals(metaLevel.getMetaLevel())) {
                return authLevel;
            }

        }

        LOGGER.info("権限種別 " + meta.getAuthTypeCode() + " に権限レベル " + authLevel + " は適用できません。下位権限を設定します。");

        // 下位権限が設定可能か確認
        return getEquivalentOrLowerLevel(authLevel - 1, meta);
    }
}