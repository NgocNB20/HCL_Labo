/*
 * Project Name : HIT-MALL4
 *
 * Copyright (C) 2021 i-TEC HANKYU HANSHIN INC. All Rights Reserved.
 *
 */

package jp.co.itechh.quad.ddd.usecase.card.query.model;

import lombok.Data;
import org.seasar.doma.Entity;

import java.sql.Timestamp;

/**
 * オーソリ期限切れ間近取引クエリーモデル
 *
 * @author kimura
 */
@Entity
@Data
public class AuthExpirationApproachingTransactionQueryModel {

    /** 注文決済.受注番号 */
    private String orderCode;

    /** オーソリ期限日（決済日付＋オーソリ保持期間） */
    private Timestamp authoryLimitDate;

}
