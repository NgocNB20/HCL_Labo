/*
 * Project Name : HIT-MALL3
 *
 * Copyright (C) 2008 i-TEC HANKYU HANSHIN INC. All Rights Reserved.
 *
 */

package jp.co.itechh.quad.admin.pc.web.admin.administrator.regist;

import jp.co.itechh.quad.admin.base.utility.DateUtility;
import jp.co.itechh.quad.admin.constant.type.HTypeAdministratorStatus;
import jp.co.itechh.quad.admin.constant.type.HTypePasswordNeedChangeFlag;
import jp.co.itechh.quad.admin.entity.administrator.AdministratorEntity;
import jp.co.itechh.quad.administrator.presentation.api.param.AdministratorRegistRequest;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.sql.Timestamp;
import java.util.Map;

/**
 * 新規運営者登録入力・確認画面Helperクラス
 *
 * @author Pham Quang Dieu (VTI Japan Co., Ltd.)
 */
@Component
public class AdministratorRegistHelper {

    /**
     * 日付関連Helper取得
     */
    public DateUtility dateUtility;

    /**
     * コンストラクタ
     *
     * @param dateUtility 日付関連Helper取得
     */
    @Autowired
    public AdministratorRegistHelper(DateUtility dateUtility) {
        this.dateUtility = dateUtility;
    }

    /**
     * 画面から運営者情報エンティティに変換
     *
     * @param administratorRegistModel 新規運営者登録入力画面
     * @return 運営者情報エンティティ
     */
    public void buildAdministratorRegistModelForConfirm(AdministratorRegistModel administratorRegistModel) {
        for (Map.Entry<String, String> entry : administratorRegistModel.getAdministratorGroupSeqItems().entrySet()) {
            if (entry.getKey().equalsIgnoreCase(administratorRegistModel.getAdministratorGroupSeq())) {
                administratorRegistModel.setAdministratorGroupName(entry.getValue());
                break;
            }
        }

        administratorRegistModel.setPasswordNeedChangeFlag(HTypePasswordNeedChangeFlag.ON);
    }

    /**
     * ページ情報の初期処理
     *
     * @param administratorRegistModel 新規運営者登録画面
     */
    public void clearPage(AdministratorRegistModel administratorRegistModel) {

        administratorRegistModel.setInputMd(null);
        administratorRegistModel.setAdministratorId(null);
        administratorRegistModel.setAdministratorPassword(null);
        administratorRegistModel.setAdministratorLastName(null);
        administratorRegistModel.setAdministratorFirstName(null);
        administratorRegistModel.setAdministratorLastKana(null);
        administratorRegistModel.setAdministratorFirstKana(null);
        administratorRegistModel.setAdministratorMail(null);
        administratorRegistModel.setAdministratorStatus(null);
        administratorRegistModel.setAdministratorGroupSeq(null);
        administratorRegistModel.setAdministratorGroupName(null);
    }

    /**
     * 運営者詳細詳細DTOからページに変換
     *
     * @param admin                    運営者詳細DTO
     * @param administratorRegistModel 運営者登録確認画面
     */
    public void toPageForLoad(AdministratorEntity admin, AdministratorRegistModel administratorRegistModel) {

        // 利用開始/終了日時セット
        Timestamp currentTime = dateUtility.getCurrentTime();
        administratorRegistModel.setUseStartDate(currentTime);
        if (administratorRegistModel.getAdministratorStatus().equals(HTypeAdministratorStatus.STOP.getValue())) {
            administratorRegistModel.setUseEndDate(currentTime);
        }
    }

    /**
     * 登録する運営者情報の作成
     *
     * @param administratorRegistModel 運営者登録確認画面
     * @return 登録する運営者情報
     */
    public AdministratorRegistRequest toAdministratorRegistRequestForRegist(AdministratorRegistModel administratorRegistModel) {

        AdministratorRegistRequest administratorRegistRequest = new AdministratorRegistRequest();

        administratorRegistRequest.setAdministratorId(administratorRegistModel.getAdministratorId());
        administratorRegistRequest.setAdministratorPassword(administratorRegistModel.getAdministratorPassword());
        administratorRegistRequest.setAdministratorLastName(administratorRegistModel.getAdministratorLastName());
        administratorRegistRequest.setAdministratorFirstName(administratorRegistModel.getAdministratorFirstName());
        administratorRegistRequest.setAdministratorLastKana(administratorRegistModel.getAdministratorLastKana());
        administratorRegistRequest.setAdministratorFirstKana(administratorRegistModel.getAdministratorFirstKana());
        administratorRegistRequest.setAdministratorMail(administratorRegistModel.getAdministratorMail());
        administratorRegistRequest.setAdministratorStatus(administratorRegistModel.getAdministratorStatus());
        administratorRegistRequest.setAdministratorGroupSeq(administratorRegistModel.getAdministratorGroupSeq());
        administratorRegistRequest.setUseStartDate(administratorRegistModel.getUseStartDate());
        administratorRegistRequest.setUseEndDate(administratorRegistModel.getUseEndDate());
        administratorRegistRequest.setPasswordNeedChangeFlag(
                        administratorRegistModel.getPasswordNeedChangeFlag().getValue());

        return administratorRegistRequest;
    }

    /**
     * 画面初期描画時に任意必須項目のデフォルト値を設定<br/>
     * 新規登録/更新に関わらず、モデルに設定されていない場合はデフォルト値を設定
     *
     * @param administratorRegistModel
     */
    public void setDefaultValueForLoad(AdministratorRegistModel administratorRegistModel) {
        // 管理者状態プルダウンのデフォルト値を設定
        if (StringUtils.isEmpty(administratorRegistModel.getAdministratorStatus())) {
            administratorRegistModel.setAdministratorStatus(HTypeAdministratorStatus.USE.getValue());
        }
    }

}