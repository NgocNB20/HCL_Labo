/*
 * Project Name : HIT-MALL3
 *
 * Copyright (C) 2008 i-TEC HANKYU HANSHIN INC. All Rights Reserved.
 *
 */

package jp.co.itechh.quad.core.logic.memberinfo.confirmmail.impl;

import jp.co.itechh.quad.core.module.crypto.MD5Module;
import jp.co.itechh.quad.core.base.util.common.ArgumentCheckUtil;
import jp.co.itechh.quad.core.base.util.common.PropertiesUtil;
import jp.co.itechh.quad.core.base.utility.ApplicationContextUtility;
import jp.co.itechh.quad.core.base.utility.DateUtility;
import jp.co.itechh.quad.core.dao.memberinfo.confirmmail.ConfirmMailDao;
import jp.co.itechh.quad.core.entity.memberinfo.confirmmail.ConfirmMailEntity;
import jp.co.itechh.quad.core.logic.AbstractShopLogic;
import jp.co.itechh.quad.core.logic.memberinfo.confirmmail.ConfirmMailRegistLogic;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.sql.Timestamp;
import java.text.DecimalFormat;

/**
 * 確認メール情報登録<br/>
 *
 * @author natume
 * @author Kaneko (itec) 2012/08/21 UtilからHelperへ変更　Util使用箇所をgetComponentで取得するように変更
 *
 */
@Component
public class ConfirmMailRegistLogicImpl extends AbstractShopLogic implements ConfirmMailRegistLogic {

    /**
     * ConfirmMailDao<br/>
     */
    private final ConfirmMailDao confirmMailDao;

    @Autowired
    public ConfirmMailRegistLogicImpl(ConfirmMailDao confirmMailDao) {
        this.confirmMailDao = confirmMailDao;
    }

    /**
     * 確認メール情報登録処理<br/>
     *
     * @param confirmMailEntity 確認メールエンティティ
     * @return 登録件数
     */
    @Override
    public int execute(ConfirmMailEntity confirmMailEntity) {

        // パラメータチェック
        checkParameter(confirmMailEntity);

        // 確認メール情報の設定
        setConfirmMailEntity(confirmMailEntity);

        // 登録
        return confirmMailDao.insert(confirmMailEntity);
    }

    /**
     * パラメータチェック<br/>
     *
     * @param confirmMailEntity 確認メールエンティティ
     */
    protected void checkParameter(ConfirmMailEntity confirmMailEntity) {
        ArgumentCheckUtil.assertNotNull("confirmMailEntity", confirmMailEntity);
    }

    /**
     * 確認メール情報のセット<br/>
     *
     * @param confirmMailEntity 確認メールエンティティ
     */
    protected void setConfirmMailEntity(ConfirmMailEntity confirmMailEntity) {

        // 日付関連Helper取得
        DateUtility dateUtility = ApplicationContextUtility.getBean(DateUtility.class);
        // 現在日時取得
        Timestamp currentTime = dateUtility.getCurrentTime();

        // 確認メールSEQ取得
        Integer confirmMailSeq = confirmMailDao.getConfirmMailSeqNextVal();

        /* パスワード作成 ハッシュ */

        // SEQ 8桁
        String seq = new DecimalFormat("00000000").format(confirmMailSeq);

        // ハッシュ化 最高10回まで
        int count = 1;
        String password = null;
        while (true) {

            // 現日日時 ミリ秒20桁
            String time = new DecimalFormat("00000000000000000000").format(System.currentTimeMillis());

            // ハッシュ化
            MD5Module helper = ApplicationContextUtility.getBean(MD5Module.class);
            password = helper.createHash(seq + time);

            // 既存のハッシュパスワードと重複チェック
            if (password != null) {
                if (getConfimrMailEntity(password) == null) {
                    break;
                }
            }

            // Max回数設定
            count++;
            if (count >= 10) {
                throwMessage(MSGCD_MAKE_CONFIRMMAILPASSWORD_FAIL);
            }
        }

        /* 各情報セット */
        confirmMailEntity.setConfirmMailSeq(confirmMailSeq);
        confirmMailEntity.setConfirmMailPassword(password);
        confirmMailEntity.setEffectiveTime(getExpiresTime());
        confirmMailEntity.setUpdateTime(currentTime);
        confirmMailEntity.setRegistTime(currentTime);
    }

    /**
     * パスワードで確認メール情報取得<br/>
     *
     * @param password 確認メールパスワード
     * @return 確認メールエンティティ
     */
    protected ConfirmMailEntity getConfimrMailEntity(String password) {
        return confirmMailDao.getEntityByPassword(password);
    }

    /**
     * 有効期限時間を作成する<br/>
     *
     * @return 有効期限時間
     */
    protected Timestamp getExpiresTime() {
        // 日付関連Helper取得
        DateUtility dateUtility = ApplicationContextUtility.getBean(DateUtility.class);

        // システムプロパティの有効期限を取得
        String expiresTime = PropertiesUtil.getSystemPropertiesValue("effective.time");
        return dateUtility.getAmountHourTimestamp(Integer.parseInt(expiresTime), true, dateUtility.getCurrentTime());
    }

}