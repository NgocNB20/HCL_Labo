/*
 * Project Name : HIT-MALL4
 *
 * Copyright (C) 2021 i-TEC HANKYU HANSHIN INC. All Rights Reserved.
 *
 */
package jp.co.itechh.quad.core.logic.previewaccess.impl;

import jp.co.itechh.quad.core.module.crypto.MD5Module;
import jp.co.itechh.quad.core.base.util.common.ArgumentCheckUtil;
import jp.co.itechh.quad.core.base.util.common.PropertiesUtil;
import jp.co.itechh.quad.core.base.utility.ApplicationContextUtility;
import jp.co.itechh.quad.core.base.utility.DateUtility;
import jp.co.itechh.quad.core.dao.previewaccess.PreviewAccessControlDao;
import jp.co.itechh.quad.core.entity.previewaccess.PreviewAccessControlEntity;
import jp.co.itechh.quad.core.logic.AbstractShopLogic;
import jp.co.itechh.quad.core.logic.previewaccess.PreviewAccessLogic;
import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.sql.Timestamp;
import java.text.DecimalFormat;

/**
 * プレビューアクセス制御ロジック実装クラス
 *
 * @author kimura
 */
@Component
public class PreviewAccessLogicImpl extends AbstractShopLogic implements PreviewAccessLogic {

    /** ロガー */
    private static final Logger LOGGER = LoggerFactory.getLogger(PreviewAccessLogicImpl.class);

    /** 日付ユーティリティ **/
    private DateUtility dateUtility;

    /** プレビューアクセス制御Dao */
    private PreviewAccessControlDao previewAccessControlDao;

    @Autowired
    public PreviewAccessLogicImpl(DateUtility dateUtility, PreviewAccessControlDao previewAccessControlDao) {
        this.dateUtility = dateUtility;
        this.previewAccessControlDao = previewAccessControlDao;
    }

    /**
     * プレビューアクセスキー登録
     *
     * @param administratorSeq 管理者SEQ
     * @return プレビューアクセスキー
     */
    @Override
    public String insertPreviewAccess(Integer administratorSeq) {

        ArgumentCheckUtil.assertNotNull("administratorSeq", administratorSeq);
        // プレビューアクセス情報生成後、プレビューアクセス制御TBLに登録
        PreviewAccessControlEntity previewAccessControlEntity = createPreviewAccessControlEntity(administratorSeq);
        this.previewAccessControlDao.insert(previewAccessControlEntity);
        return previewAccessControlEntity.getPreviewAccessKey();
    }

    /**
     * プレビューアクセス情報の生成
     *
     * @param administratorSeq 管理者SEQ
     * @return プレビューアクセス制御エンティティ
     */
    protected PreviewAccessControlEntity createPreviewAccessControlEntity(Integer administratorSeq) {

        // 現在日時取得
        Timestamp currentTime = this.dateUtility.getCurrentTime();

        // プレビューアクセス制御SEQを取得
        Integer previewAccessControlSeq = this.previewAccessControlDao.getPreviewAccessControlSeqNextVal();

        // アクセスキーを発行
        String seq = new DecimalFormat("00000000").format(previewAccessControlSeq);

        // ハッシュ化 最高10回まで
        int count = 1;
        String preViewAccessHashKey = null;
        while (true) {
            // 現日日時 ミリ秒20桁
            String time = new DecimalFormat("00000000000000000000").format(System.currentTimeMillis());

            // ハッシュ化
            preViewAccessHashKey = ApplicationContextUtility.getBean(MD5Module.class).createHash(seq + time);

            // 既存のハッシュパスワードと重複チェック
            if (StringUtils.isNotEmpty(preViewAccessHashKey)) {
                if (getPreviewAccessControlEntity(preViewAccessHashKey, currentTime) == null) {
                    break;
                }
            }

            // Max回数設定
            count++;
            if (count >= 10) {
                LOGGER.error("プレビューキーの生成に失敗しました。");
                throwMessage();
            }
        }

        // プレビューアクセス制御情報設定
        PreviewAccessControlEntity entity = ApplicationContextUtility.getBean(PreviewAccessControlEntity.class);
        entity.setPreviewAccessControlSeq(previewAccessControlSeq);
        // キーを設定
        entity.setPreviewAccessKey(preViewAccessHashKey);
        // 有効期限をプロパティファイルから取得し、設定
        entity.setEffectiveTime(this.dateUtility.getAmountMinuteTimestamp(
                        PropertiesUtil.getSystemPropertiesValueToInt(EFFECTIVETIME_ADD_MINUTE), true, currentTime));
        entity.setAdministratorSeq(administratorSeq);
        entity.setRegistTime(currentTime);
        entity.setUpdateTime(currentTime);

        return entity;
    }

    /**
     * パスワードでプレビューアクセス制御情報取得
     *
     * @param prevewAccessHashKey プレビューアクセスハッシュキー
     * @param currentTime          現在日時
     * @return プレビューアクセス制御エンティティ
     */
    protected PreviewAccessControlEntity getPreviewAccessControlEntity(String prevewAccessHashKey,
                                                                       Timestamp currentTime) {
        return this.previewAccessControlDao.getEntityByPreviewAccessKey(prevewAccessHashKey, currentTime);
    }

    /**
     * プレビューアクセス制御チェック
     *
     * @param preViewAccessKey プレビューアクセスキー
     * @return エンティティ
     */
    @Override
    public PreviewAccessControlEntity checkPreviewAccess(String preViewAccessKey) {
        if (StringUtils.isEmpty(preViewAccessKey)) {
            return null;
        }
        return this.previewAccessControlDao.getEntityByPreviewAccessKey(
                        preViewAccessKey, this.dateUtility.getCurrentTime());
    }

}