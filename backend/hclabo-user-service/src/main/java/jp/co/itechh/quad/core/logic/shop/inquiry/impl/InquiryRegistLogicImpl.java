/*
 * Project Name : HIT-MALL3
 *
 * Copyright (C) 2008 i-TEC HANKYU HANSHIN INC. All Rights Reserved.
 *
 */

package jp.co.itechh.quad.core.logic.shop.inquiry.impl;

import jp.co.itechh.quad.core.base.util.common.ArgumentCheckUtil;
import jp.co.itechh.quad.core.base.utility.ApplicationContextUtility;
import jp.co.itechh.quad.core.base.utility.DateUtility;
import jp.co.itechh.quad.core.dao.inquiry.InquiryDao;
import jp.co.itechh.quad.core.dao.inquiry.InquiryDetailDao;
import jp.co.itechh.quad.core.dto.inquiry.InquiryDetailsDto;
import jp.co.itechh.quad.core.entity.inquiry.InquiryDetailEntity;
import jp.co.itechh.quad.core.entity.inquiry.InquiryEntity;
import jp.co.itechh.quad.core.logic.AbstractShopLogic;
import jp.co.itechh.quad.core.logic.shop.inquiry.InquiryRegistLogic;
import jp.co.itechh.quad.core.logic.shop.inquiry.NewInquirySeqGetLogic;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.sql.Timestamp;

/**
 * 問い合わせ登録ロジック
 *
 * @author kimura
 * @author Kaneko (itec) 2012/08/21 UtilからHelperへ変更　Util使用箇所をgetComponentで取得するように変更
 *
 */
@Component
public class InquiryRegistLogicImpl extends AbstractShopLogic implements InquiryRegistLogic {

    /** お問い合わせDao */
    private final InquiryDao inquiryDao;

    /** お問い合わせ内容Dao */
    private final InquiryDetailDao inquiryDetailDao;

    /** 新規問い合わせSEQ取得ロジック */
    private final NewInquirySeqGetLogic newInquirySeqGetLogic;

    /** お問い合わせSEQの桁数 */
    private static final int STATUS_6 = 6;

    /** お問い合わせSEQの先頭埋め文字 */
    private static final String HEAD_CHARACTER = "0";

    /** ご連絡番号　プレフィックス */
    private static final String INQUIRYCODE_PREFIX = "Q";

    /** 日付フォーマット yyMMdd */
    private static final String YMD = "yyMMdd";

    /** 改行コード CRLF */
    //    protected static final String CRLF = "\r\n";
    @Autowired
    public InquiryRegistLogicImpl(InquiryDao inquiryDao,
                                  InquiryDetailDao inquiryDetailDao,
                                  NewInquirySeqGetLogic newInquirySeqGetLogic) {

        this.inquiryDao = inquiryDao;
        this.inquiryDetailDao = inquiryDetailDao;
        this.newInquirySeqGetLogic = newInquirySeqGetLogic;
    }

    /**
     * 一般・注文用問い合わせ登録<br/>
     * 問い合わせ・問い合わせ内容を登録する<br/>
     * @param inquiryDetailsDto お問い合わせ詳細DTO
     */
    @Override
    public void executeInquiryRegist(InquiryDetailsDto inquiryDetailsDto) {
        // パラメータチェック
        ArgumentCheckUtil.assertNotNull("inquiryDetailsDto", inquiryDetailsDto);

        DateUtility dateUtility = ApplicationContextUtility.getBean(DateUtility.class);

        // 現在日時の取得
        Timestamp currentTime = dateUtility.getCurrentTime();

        // 問い合わせ情報の取得
        InquiryEntity inquiryEntity = inquiryDetailsDto.getInquiryEntity();

        // 新規問い合わせ登録の場合
        if (inquiryEntity.getInquirySeq() == null) {
            insertInquiry(inquiryDetailsDto, currentTime);

            // 返信・完了報告の場合
        } else {
            // 問い合わせ情報の更新
            updateInquiry(inquiryDetailsDto, currentTime);
        }

        // 問い合わせ内容の登録
        insertInquiryDetail(inquiryDetailsDto, currentTime);

    }

    /**
     * 問い合わせ情報を登録する<br/>
     *
     * @param inquiryDetailsDto お問い合わせ詳細DTO
     * @param currentTime 現在日時
     */
    protected void insertInquiry(InquiryDetailsDto inquiryDetailsDto, Timestamp currentTime) {
        // 共通情報の取得
        Integer shopSeq = 1001;
        // 新規問い合わせSEQの取得(ご連絡番号を作成する必要がある為)
        Integer inquirySeq = newInquirySeqGetLogic.execute();
        // ご連絡番号の生成
        String inquiryCode = generateInquiryCode(inquirySeq, currentTime);
        // 問い合わせ情報の取得
        InquiryEntity inquiryEntity = inquiryDetailsDto.getInquiryEntity();
        // 問い合わせSeqの設定
        inquiryEntity.setInquirySeq(inquirySeq);
        // ご連絡番号の設定
        inquiryEntity.setInquiryCode(inquiryCode);
        // ショップSeqの設定
        inquiryEntity.setShopSeq(shopSeq);
        // 問い合わせ日時
        inquiryEntity.setInquiryTime(currentTime);
        // 登録日付の設定
        inquiryEntity.setRegistTime(currentTime);
        // 更新日付の設定
        inquiryEntity.setUpdateTime(currentTime);

        // 問い合わせ情報の登録
        int retResult = inquiryDao.insert(inquiryEntity);
        // 処理件数が0件の場合エラー
        if (retResult == 0) {
            throwMessage(MSGCD_INQUIRY_REGIST_ERROR);
        }
        // 問い合わせ内容情報の取得(最後に追加されている問い合わせ内容エンティティを取得する。)
        InquiryDetailEntity inquiryDetail = inquiryDetailsDto.getInquiryDetailEntityList()
                                                             .get(inquiryDetailsDto.getInquiryDetailEntityList().size()
                                                                  - 1);
        // ご連絡番号を登録用に、問い合わせ内容エンティティに設定
        inquiryDetail.setInquirySeq(inquirySeq);
    }

    /**
     * 問い合わせ情報を更新する<br/>
     *
     * @param inquiryDetailsDto お問い合わせ詳細DTO
     * @param currentTime 現在日時
     */
    protected void updateInquiry(InquiryDetailsDto inquiryDetailsDto, Timestamp currentTime) {
        // 共通情報の取得
        Integer shopSeq = 1001;
        // 問い合わせ情報の取得
        InquiryEntity inquiryEntity = inquiryDetailsDto.getInquiryEntity();
        // 更新日付の設定
        inquiryEntity.setUpdateTime(currentTime);
        inquiryEntity.setShopSeq(shopSeq);
        // 問い合わせ情報の更新
        int retResult = inquiryDao.update(inquiryEntity);
        // 処理件数が0件の場合エラー
        if (retResult == 0) {
            throwMessage(MSGCD_INQUIRY_REGIST_ERROR);
        }
    }

    /**
     * 問い合わせ内容を登録する<br/>
     *
     * @param inquiryDetailsDto お問い合わせ詳細DTO
     * @param currentTime 現在日時
     */
    protected void insertInquiryDetail(InquiryDetailsDto inquiryDetailsDto, Timestamp currentTime) {
        // 問い合わせ内容情報の取得(最後に追加されている問い合わせ内容エンティティを取得する。)
        InquiryDetailEntity inquiryDetail = inquiryDetailsDto.getInquiryDetailEntityList()
                                                             .get(inquiryDetailsDto.getInquiryDetailEntityList().size()
                                                                  - 1);

        // 登録日付の設定
        inquiryDetail.setRegistTime(currentTime);
        // 更新日付の設定
        inquiryDetail.setUpdateTime(currentTime);

        // 問い合わせ内容の登録
        int retResult = inquiryDetailDao.insert(inquiryDetail);
        // 処理件数が0件の場合エラー
        if (retResult == 0) {
            throwMessage(MSGCD_INQUIRY_REGIST_ERROR);
        }
    }

    /**
     * 問い合わせコードを生成します
     *
     * @param inquirySeq 問い合わせSEQ
     * @param currentTime 現在日時
     * @return 問い合わせコード（T + YMMDD + 問い合わせSEQの下6桁）
     */
    private String generateInquiryCode(Integer inquirySeq, Timestamp currentTime) {

        DateUtility dateUtility = ApplicationContextUtility.getBean(DateUtility.class);

        // 現在日時をフォーマット
        String yMMdd = dateUtility.format(currentTime, YMD).substring(1);
        // 問い合わせSEQの下6桁を取得
        String inquirySeqStr = Integer.toString(inquirySeq);
        // SEQ下6桁作成
        // 桁数6桁より多い場合
        if (inquirySeqStr.length() > STATUS_6) {
            inquirySeqStr = inquirySeqStr.substring((inquirySeqStr.length() - STATUS_6));
        } else {
            // 桁数6桁より少ない場合、先頭0埋め
            inquirySeqStr = StringUtils.leftPad(inquirySeqStr, STATUS_6, HEAD_CHARACTER);
        }

        // ご連絡番号生成。プレフィックスはT
        StringBuilder inquiryCode = new StringBuilder(INQUIRYCODE_PREFIX);
        return inquiryCode.append(yMMdd).append(inquirySeqStr).toString();
    }

}