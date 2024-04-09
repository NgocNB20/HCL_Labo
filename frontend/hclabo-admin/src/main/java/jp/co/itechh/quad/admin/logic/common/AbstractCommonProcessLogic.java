/*
 * Project Name : HIT-MALL3
 *
 * Copyright (C) 2008 i-TEC HANKYU HANSHIN INC. All Rights Reserved.
 *
 */

package jp.co.itechh.quad.admin.logic.common;

import jp.co.itechh.quad.admin.base.util.common.PropertiesUtil;
import jp.co.itechh.quad.admin.base.utility.ApplicationLogUtility;
import jp.co.itechh.quad.admin.utility.AccessDeviceUtility;
import jp.co.itechh.quad.admin.utility.CommonInfoUtility;
import lombok.Data;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

/**
 * 共通処理ロジックの抽象クラス<br/>
 *
 * @author natume
 *
 */
@Component
@Data
public abstract class AbstractCommonProcessLogic {
    /** ログ */
    private static final Logger LOG = LoggerFactory.getLogger(AbstractCommonProcessLogic.class);

    /** 共通情報ユーティリティ */
    protected final CommonInfoUtility commonInfoUtility;

    /** ApplicationLog ユーティリティ */
    public ApplicationLogUtility applicationLogUtility;

    /** アクセスデバイスの解析用Utility */
    private final AccessDeviceUtility accessDeviceUtility;

    /**
     * コンストラクタ<br/>
     * @param commonInfoUtility
     * @param commonProcessUtility
     * @param applicationLogUtility
     * @param applicationUtility
     * @param accessDeviceUtility
     */
    @Autowired
    public AbstractCommonProcessLogic(CommonInfoUtility commonInfoUtility,
                                      ApplicationLogUtility applicationLogUtility,
                                      AccessDeviceUtility accessDeviceUtility) {
        this.commonInfoUtility = commonInfoUtility;
        this.applicationLogUtility = applicationLogUtility;
        this.accessDeviceUtility = accessDeviceUtility;
    }

    /**
     * 起動アプリのショップSEQを取得<br/>
     * ※ショップSEQをどこで指定するかは変更される可能性があるので、
     * 専用メソッドを作成しておく
     *
     * @return ショップSEQ
     */
    public Integer getShopSeq() {
        // 現行ApplicationUtilityから移植
        // system.propertiesから取得
        String shopSeqStr = PropertiesUtil.getSystemPropertiesValue("shopseq");
        if (shopSeqStr == null || !shopSeqStr.matches("^[1-9]$|^[1-9][0-9]{1,3}$")) {
            throw new RuntimeException("system.properties shopseqの値が不正です。");
        }
        return Integer.valueOf(shopSeqStr);
    }

}