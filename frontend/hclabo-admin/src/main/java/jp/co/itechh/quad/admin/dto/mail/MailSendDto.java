/*
 * Project Name : HIT-MALL3
 *
 * Copyright (C) 2008 i-TEC HANKYU HANSHIN INC. All Rights Reserved.
 *
 */

package jp.co.itechh.quad.admin.dto.mail;

import jp.co.itechh.quad.admin.constant.type.HTypeMailTemplateType;
import lombok.Data;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Component;

import java.io.Serializable;
import java.util.List;

/**
 * テンプレートメール送信サブページへ渡す DTO
 *
 * @author tm27400
 * @version $Revision: 1.5 $
 *
 */
@Data
@Component
@Scope("prototype")
public class MailSendDto implements Serializable {

    /** シリアル */
    private static final long serialVersionUID = -2105850278894944530L;

    /** 呼び出し元アプリケーション定数 */
    private static enum AppType {
        /** 呼び出し元アプリケーション：カテゴリ */
        CATEGORY,
        /** 呼び出し元アプリケーション：商品 */
        GOODS,
        /** 呼び出し元アプリケーション：会員 */
        MEMBER,
        /** 呼び出し元アプリケーション：注文 */
        ORDER,
        /** 呼び出し元アプリケーション：ショップ */
        SHOP,
        /** 呼び出し元アプリケーション：システム */
        SYSTEM,
        /** 呼び出し元アプリケーション：集計 */
        TOTALING
    }

    /** 呼び出し元アプリケーション：カテゴリ */
    public static final AppType CATEGORY = AppType.CATEGORY;

    /** 呼び出し元アプリケーション：商品 */
    public static final AppType GOODS = AppType.GOODS;

    /** 呼び出し元アプリケーション：会員 */
    public static final AppType MEMBER = AppType.MEMBER;

    /** 呼び出し元アプリケーション：注文 */
    public static final AppType ORDER = AppType.ORDER;

    /** 呼び出し元アプリケーション：ショップ */
    public static final AppType SHOP = AppType.SHOP;

    /** 呼び出し元アプリケーション：システム */
    public static final AppType SYSTEM = AppType.SYSTEM;

    /** 呼び出し元アプリケーション：集計 */
    public static final AppType TOTALING = AppType.TOTALING;

    /** 表示用名称 */
    private String displayName;

    /** 呼び出し元アプリケーション（サイドバーの決定に使用する）*/
    private AppType application;

    /** 送信情報 */
    private List<MailDto> mailDtoList;

    /** 選択可能テンプレート一覧 */
    private List<HTypeMailTemplateType> availableTemplateTypeList;

    /** 非同期メールとして送信するか */
    private boolean asyncRequest;

    /** 非同期処理のバッチ種別 */
    private String asyncBatchType;

    /** 非同期処理のバッチ名称 */
    private String asyncBatchName;

    /** 非同期送信時のタスクオーナ */
    private String asyncBatchTaskOwner;

    /** 非同期送信時のタスクオーナ */
    private String asyncVisibleGroup;

    /** 非同期送信時のタスクオーナ */
    private String asyncInterruptibleGroup;

    /** 汎用メール送信では完了画面を表示せず、送信処理終了時に呼び出し元へ戻るフラグ */
    private boolean skipCompletePage;

    /** 受注番号 */
    private String orderCode;

    /** 取引ID */
    private String transactionId;
}
