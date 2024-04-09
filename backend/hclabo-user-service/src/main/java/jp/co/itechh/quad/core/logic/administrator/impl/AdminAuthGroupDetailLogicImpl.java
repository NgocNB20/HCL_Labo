/*
 * Project Name : HIT-MALL4
 *
 * Copyright (C) 2021 i-TEC HANKYU HANSHIN INC. All Rights Reserved.
 *
 */

package jp.co.itechh.quad.core.logic.administrator.impl;

import jp.co.itechh.quad.core.base.utility.ApplicationContextUtility;
import jp.co.itechh.quad.core.dao.administrator.AdminAuthGroupDetailDao;
import jp.co.itechh.quad.core.entity.administrator.AdminAuthGroupDetailEntity;
import jp.co.itechh.quad.core.logic.administrator.AdminAuthGroupDetailLogic;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

/**
 * 権限グループ詳細ロジック
 * @author Doan Thang (VTI Japan Co., Ltd.)
 */
@Component
public class AdminAuthGroupDetailLogicImpl implements AdminAuthGroupDetailLogic {

    /** フルアクセス */
    private static final Integer ADMIN_AUTH_GROUP_FULL_ACCESS = 1001;

    /** ユーザメンテナンス */
    private static final Integer ADMIN_AUTH_GROUP_MAINTENANCE = 1002;

    /** 規定権限グループマップ */
    private static final String ADMIN_CONSTANT_AUTH_GROUP_MAP = "adminConstantAuthGroupMap";

    /** 権限グループ詳細Dao */
    private final AdminAuthGroupDetailDao adminAuthGroupDetailDao;

    @Autowired
    public AdminAuthGroupDetailLogicImpl(AdminAuthGroupDetailDao adminAuthGroupDetailDao) {
        this.adminAuthGroupDetailDao = adminAuthGroupDetailDao;
    }

    /**
     * 権限リストを取得する
     *
     * @param adminAuthGroupSeq 権限グループSeq
     * @return 権限リスト
     */
    @Override
    public List<String> getAuthorityList(Integer adminAuthGroupSeq) {

        // 運営者権限グループ詳細リストの宣言
        List<AdminAuthGroupDetailEntity> detailList = null;

        if (ADMIN_AUTH_GROUP_FULL_ACCESS.equals(adminAuthGroupSeq) || ADMIN_AUTH_GROUP_MAINTENANCE.equals(
                        adminAuthGroupSeq)) {
            // 規定権限グループとして上書き設定がされている場合はその設定を取得する
            Map<Integer, List<AdminAuthGroupDetailEntity>> adminConstantAuthGroupMap =
                            (Map<Integer, List<AdminAuthGroupDetailEntity>>) ApplicationContextUtility.getApplicationContext()
                                                                                                      .getBean(ADMIN_CONSTANT_AUTH_GROUP_MAP);
            if (adminConstantAuthGroupMap != null) {
                detailList = adminConstantAuthGroupMap.get(adminAuthGroupSeq);
            }
        } else {
            // データベースから取得する
            detailList = adminAuthGroupDetailDao.getDetailList(adminAuthGroupSeq);
        }

        List<String> resultAuthorityList = new ArrayList<>();
        String authGroup;
        for (AdminAuthGroupDetailEntity entity : detailList) {
            authGroup = entity.getAuthTypeCode() + ":" + entity.getAuthLevel();
            resultAuthorityList.add(authGroup);
        }

        return resultAuthorityList;
    }

}