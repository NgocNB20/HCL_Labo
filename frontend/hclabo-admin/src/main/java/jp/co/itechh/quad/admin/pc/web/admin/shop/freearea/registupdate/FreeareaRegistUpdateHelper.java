/*
 * Copyright (C) 2022 i-TEC HANKYU HANSHIN INC. All Rights Reserved.
 */

package jp.co.itechh.quad.admin.pc.web.admin.shop.freearea.registupdate;

import jp.co.itechh.quad.admin.base.utility.ApplicationContextUtility;
import jp.co.itechh.quad.admin.base.utility.ConversionUtility;
import jp.co.itechh.quad.admin.constant.type.HTypeFreeAreaOpenStatus;
import jp.co.itechh.quad.admin.constant.type.HTypeSiteMapFlag;
import jp.co.itechh.quad.admin.entity.shop.FreeAreaEntity;
import jp.co.itechh.quad.admin.util.common.EnumTypeUtil;
import jp.co.itechh.quad.freearea.presentation.api.param.FreeAreaRegistRequest;
import jp.co.itechh.quad.freearea.presentation.api.param.FreeAreaResponse;
import jp.co.itechh.quad.freearea.presentation.api.param.FreeAreaUpdateRequest;
import org.apache.commons.lang3.ObjectUtils;
import org.springframework.stereotype.Component;

import java.sql.Timestamp;

/**
 * フリーエリア登録・更新画面のHelper
 *
 * @author Pham Quang Dieu (VTI Japan Co., Ltd.)
 *
 */
@Component
public class FreeareaRegistUpdateHelper {

    /**
     * 初期処理時の画面反映
     *
     * @param freeareaRegistUpdateModel      フリーエリア登録更新画面
     * @param freeAreaEntity フリーエリアエンティティ
     */
    public void toPageForLoad(FreeareaRegistUpdateModel freeareaRegistUpdateModel, FreeAreaEntity freeAreaEntity) {

        // 変換Helper取得
        ConversionUtility conversionUtility = ApplicationContextUtility.getBean(ConversionUtility.class);

        freeareaRegistUpdateModel.setNormality(true);
        // 反映情報が指定されている場合
        if (freeAreaEntity != null) {

            // 画面へ反映
            freeareaRegistUpdateModel.setFreeAreaSeq(freeAreaEntity.getFreeAreaSeq());
            freeareaRegistUpdateModel.setFreeAreaKey(freeAreaEntity.getFreeAreaKey());
            freeareaRegistUpdateModel.setFreeAreaTitle(freeAreaEntity.getFreeAreaTitle());
            freeareaRegistUpdateModel.setFreeAreaBodyPc(freeAreaEntity.getFreeAreaBodyPc());
            freeareaRegistUpdateModel.setTargetGoods(freeAreaEntity.getTargetGoods());
            freeareaRegistUpdateModel.setSiteMapFlag(freeAreaEntity.getSiteMapFlag().getValue());
            freeareaRegistUpdateModel.setOpenStartDate(conversionUtility.toYmd(freeAreaEntity.getOpenStartTime()));
            freeareaRegistUpdateModel.setOpenStartTime(conversionUtility.toHms(freeAreaEntity.getOpenStartTime()));
            freeareaRegistUpdateModel.setFreeAreaEntity(freeAreaEntity);

        }

    }

    /**
     * 確認画面へ遷移前の画面反映
     *
     * @param freeareaRegistUpdateIndexModel フリーエリア登録更新画面
     */
    public void toPageForConfirm(FreeareaRegistUpdateModel freeareaRegistUpdateIndexModel) {

        // 変換Helper取得
        ConversionUtility conversionUtility = ApplicationContextUtility.getBean(ConversionUtility.class);

        // 時刻反映
        freeareaRegistUpdateIndexModel.setOpenStartTime(
                        conversionUtility.toDefaultHms(freeareaRegistUpdateIndexModel.getOpenStartDate(),
                                                       freeareaRegistUpdateIndexModel.getOpenStartTime(),
                                                       ConversionUtility.DEFAULT_START_TIME
                                                      ));
    }

    /**
     * フリーエリアクラス
     *
     * @param freeAreaResponse   フリーエリアレスポンス
     * @return フリーエリアクラス
     */
    public FreeAreaEntity toFreeAreaEntity(FreeAreaResponse freeAreaResponse) {
        // 変換Helper取得
        ConversionUtility conversionUtility = ApplicationContextUtility.getBean(ConversionUtility.class);
        FreeAreaEntity freeAreaEntity = new FreeAreaEntity();

        if (ObjectUtils.isNotEmpty(freeAreaEntity)) {
            freeAreaEntity.setFreeAreaSeq(freeAreaResponse.getFreeAreaSeq());
            freeAreaEntity.setFreeAreaKey(freeAreaResponse.getFreeAreaKey());
            freeAreaEntity.setOpenStartTime(conversionUtility.toTimestamp(freeAreaResponse.getOpenStartTime()));
            freeAreaEntity.setFreeAreaTitle(freeAreaResponse.getFreeAreaTitle());
            freeAreaEntity.setFreeAreaBodyPc(freeAreaResponse.getFreeAreaBodyPc());
            freeAreaEntity.setRegistTime(conversionUtility.toTimestamp(freeAreaResponse.getRegistTime()));
            freeAreaEntity.setUpdateTime(conversionUtility.toTimestamp(freeAreaResponse.getUpdateTime()));
            freeAreaEntity.setFreeAreaOpenStatus(EnumTypeUtil.getEnumFromValue(HTypeFreeAreaOpenStatus.class,
                                                                               freeAreaResponse.getFreeAreaOpenStatus()
                                                                              ));
            freeAreaEntity.setSiteMapFlag(
                            EnumTypeUtil.getEnumFromValue(HTypeSiteMapFlag.class, freeAreaResponse.getSiteMapFlag()));
        }

        return freeAreaEntity;
    }

    /**
     * フリーエリア登録更新時の処理
     *
     * @param freeareaRegistUpdateModel   フリーエリア登録・更新画面
     * @return フリーエリアエンティティ
     */
    public FreeAreaEntity toFreeAreaEntityForNewsRegist(FreeareaRegistUpdateModel freeareaRegistUpdateModel) {

        FreeAreaEntity freeAreaEntity = freeareaRegistUpdateModel.getFreeAreaEntity();

        freeAreaEntity.setFreeAreaKey(freeareaRegistUpdateModel.getFreeAreaKey());
        freeAreaEntity.setFreeAreaTitle(freeareaRegistUpdateModel.getFreeAreaTitle());
        freeAreaEntity.setFreeAreaBodyPc(freeareaRegistUpdateModel.getFreeAreaBodyPc());
        freeAreaEntity.setTargetGoods(freeareaRegistUpdateModel.getTargetGoods());
        freeAreaEntity.setSiteMapFlag(EnumTypeUtil.getEnumFromValue(HTypeSiteMapFlag.class,
                                                                    freeareaRegistUpdateModel.getSiteMapFlag()
                                                                   ));
        freeAreaEntity.setOpenStartTime(this.ymdhmsToTimestamp(freeareaRegistUpdateModel.getOpenStartDate(),
                                                               freeareaRegistUpdateModel.getOpenStartTime()
                                                              ));

        return freeAreaEntity;
    }

    /**
     * フリーエリア登録リクエスト
     *
     * @param freeareaRegistUpdateModel   フリーエリア登録・更新画面
     */
    public FreeAreaRegistRequest toFreeAreaRequestRegist(FreeareaRegistUpdateModel freeareaRegistUpdateModel) {

        FreeAreaRegistRequest requestRegist = new FreeAreaRegistRequest();

        requestRegist.setFreeAreaKey(freeareaRegistUpdateModel.getFreeAreaKey());
        requestRegist.setFreeAreaTitle(freeareaRegistUpdateModel.getFreeAreaTitle());
        requestRegist.setFreeAreaBodyPc(freeareaRegistUpdateModel.getFreeAreaBodyPc());
        requestRegist.setSiteMapFlag(freeareaRegistUpdateModel.getSiteMapFlag());
        requestRegist.setOpenStartTime(freeareaRegistUpdateModel.getOpenStartTime());
        requestRegist.setOpenStartDate(freeareaRegistUpdateModel.getOpenStartDate());

        return requestRegist;
    }

    /**
     * フリーエリア更新リクエスト
     *
     * @param freeareaRegistUpdateModel   フリーエリア登録・更新画面
     */
    public FreeAreaUpdateRequest toFreeAreaRequestUpdate(FreeareaRegistUpdateModel freeareaRegistUpdateModel) {

        FreeAreaUpdateRequest requestUpdate = new FreeAreaUpdateRequest();

        requestUpdate.setFreeAreaKey(freeareaRegistUpdateModel.getFreeAreaKey());
        requestUpdate.setFreeAreaTitle(freeareaRegistUpdateModel.getFreeAreaTitle());
        requestUpdate.setFreeAreaBodyPc(freeareaRegistUpdateModel.getFreeAreaBodyPc());
        requestUpdate.setSiteMapFlag(freeareaRegistUpdateModel.getSiteMapFlag());
        requestUpdate.setOpenStartTime(freeareaRegistUpdateModel.getOpenStartTime());
        requestUpdate.setOpenStartDate(freeareaRegistUpdateModel.getOpenStartDate());

        return requestUpdate;
    }

    /**
     * 年月日・時分秒→タイムスタンプ
     *
     * @param ymd 日付項目値
     * @param hms 時刻項目値
     * @return 引数から取得されるタイムスタンプ
     */
    protected Timestamp ymdhmsToTimestamp(String ymd, String hms) {

        Timestamp ret = null;
        if (ymd != null && hms != null) {
            // 変換Helper取得
            ConversionUtility conversionUtility = ApplicationContextUtility.getBean(ConversionUtility.class);

            ret = conversionUtility.toTimeStamp(ymd, hms);
        }
        return ret;

    }

}