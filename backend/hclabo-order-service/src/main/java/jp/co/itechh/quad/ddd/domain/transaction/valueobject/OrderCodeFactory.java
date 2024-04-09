/*
 * Copyright (C) 2022 i-TEC HANKYU HANSHIN INC. All Rights Reserved.
 */

package jp.co.itechh.quad.ddd.domain.transaction.valueobject;

import jp.co.itechh.quad.core.base.util.common.PropertiesUtil;
import jp.co.itechh.quad.core.base.utility.ApplicationContextUtility;
import jp.co.itechh.quad.core.base.utility.ConversionUtility;
import jp.co.itechh.quad.ddd.domain.transaction.repository.IOrderReceivedRepository;
import jp.co.itechh.quad.ddd.exception.AssertChecker;
import org.hashids.Hashids;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.sql.Timestamp;

/**
 * 受注番号 値オブジェクト ファクトリ
 */
@Service
public class OrderCodeFactory {

    /** 受注リポジトリリポジトリ */
    private final IOrderReceivedRepository iOrderReceivedRepository;

    /** 受注番号生成ハッシュ化ソルト */
    private static final String ORDER_CODE_SALT = "order.code.salt";

    /** 受注番号生成ハッシュ化文字 */
    private static final String ORDER_CODE_CHAR = "order.code.char";

    /** 受注番号桁数 */
    private static final int ORDER_CODE_LENGTH = 14;

    /** コンストラクタ */
    @Autowired
    public OrderCodeFactory(IOrderReceivedRepository iOrderReceivedRepository) {
        this.iOrderReceivedRepository = iOrderReceivedRepository;
    }

    /**
     * 受注番号 値オブジェクト生成
     *
     * @param currentTime
     * @return
     */
    public OrderCode constructOrderCode(Timestamp currentTime) {

        //アサートチェック
        AssertChecker.assertNotNull("currentTime is null", currentTime);

        // 受注番号用連番取得
        Integer orderCodeSeq = iOrderReceivedRepository.getOrderCodeSeq();
        AssertChecker.assertNotNull("orderCodeSeq is null", orderCodeSeq);

        /** 受注番号生成ハッシュ化ソルト */
        String orderCodeSalt = PropertiesUtil.getSystemPropertiesValue(ORDER_CODE_SALT);
        /** 受注番号生成ハッシュ化文字 */
        String orderCodeChar = PropertiesUtil.getSystemPropertiesValue(ORDER_CODE_CHAR);

        Hashids hashids = new Hashids(orderCodeSalt, ORDER_CODE_LENGTH, orderCodeChar);
        String orderCode = hashids.encode(generateOrderCodeOfHashInput(orderCodeSeq, currentTime));

        return new OrderCode(orderCode);
    }

    /**
     * 受注コードハッシュ元生成/>
     *
     * @param orderSeq    受注SEQ
     * @param currentTime 現在日時
     * @return YYMMDDHH + 受注SEQ（桁数オーバーの場合は後ろから切り出す）
     */
    public static Long generateOrderCodeOfHashInput(Integer orderSeq, Timestamp currentTime) {

        ConversionUtility conversionUtility = ApplicationContextUtility.getBean(ConversionUtility.class);

        String date = conversionUtility.toYmdHms(currentTime);
        date = date.replaceAll("/| |:", "").substring(2, 10);

        String orderCode = date + orderSeq.toString();
        if (orderCode.length() > ORDER_CODE_LENGTH) {
            // 後ろから切り出す
            orderCode = orderCode.substring(orderCode.length() - ORDER_CODE_LENGTH);
        }

        return Long.parseLong(orderCode);
    }
}
