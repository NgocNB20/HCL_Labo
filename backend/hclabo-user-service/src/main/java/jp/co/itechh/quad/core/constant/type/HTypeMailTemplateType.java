/*
 * Project Name : HIT-MALL4
 *
 * Copyright (C) 2021 i-TEC HANKYU HANSHIN INC. All Rights Reserved.
 *
 */

package jp.co.itechh.quad.core.constant.type;

import jp.co.itechh.quad.core.util.common.EnumTypeUtil;
import lombok.AllArgsConstructor;
import lombok.Getter;
import org.seasar.doma.Domain;

/**
 * メールテンプレートタイプ
 *
 * @author kaneda
 */
@Domain(valueType = String.class, factoryMethod = "of")
@Getter
@AllArgsConstructor
public enum HTypeMailTemplateType implements EnumType {

    /** 仮会員登録 */
    MEMBER_PREREGISTRATION("仮会員登録", "01"),

    /** 会員登録完了 */
    MEMBER_REGISTRATION("会員登録完了", "02"),

    /** 会員退会完了 */
    MEMBER_UNREGISTRATION("会員退会完了", "03"),

    /** パスワード再設定 */
    PASSWORD_NOTIFICATION("パスワード再設定", "04"),

    /** メールアドレス変更確認 */
    EMAIL_MODIFICATION("メールアドレス変更確認", "05"),

    /** メールアドレス変更完了 */
    EMAIL_MODIFICATION_COMPLATE("メールアドレス変更完了", "06"),

    /** 注文確認 */
    ORDER_CONFIRMATION("注文確認", "07"),

    /** 出荷完了 */
    SHIPMENT_NOTIFICATION("出荷完了", "08"),

    /** 入荷のお知らせ */
    GOODS_ARRIVAL_NOTIFICATION("入荷のお知らせ", "09"),

    /** 受注決済督促 */
    SETTLEMENT_REMINDER("受注決済督促", "10"),

    /** 受注決済期限切れ */
    SETTLEMENT_EXPIRATION_NOTIFICATION("受注決済期限切れ", "11"),

    /** お問い合わせ受付 */
    INQUIRY_RECEPTION("お問い合わせ受付", "12"),

    /** お問い合わせ回答 */
    INQUIRY_ANSWER("お問い合わせ回答", "13"),

    /** メルマガ登録完了 */
    MAILMAGAZINE_REGISTRATION("メルマガ登録完了", "14"),

    /** メルマガ解除完了 */
    MAILMAGAZINE_UNREGISTRATION("メルマガ解除完了", "15"),

    /** メルマガ雛形 */
    MAILMAGAZINE_TEMPLATE("一括メール雛形", "16"),

    // TODO 不要のため削除されていたが、DBのカラムは削除されていなかったので一旦復元。DMLも作成しつつ、区分値も削除すること
    /** 期限切れポイント告知1 */
    POINT_INVALID_01("期限切れポイント告知1", "17"),

    // TODO 不要のため削除されていたが、DBのカラムは削除されていなかったので一旦復元。DMLも作成しつつ、区分値も削除すること
    /** 期限切れポイント告知2 */
    POINT_INVALID_02("期限切れポイント告知2", "18"),

    // TODO 不要のため削除されていたが、DBのカラムは削除されていなかったので一旦復元。DMLも作成しつつ、区分値も削除すること
    /** 期限切れポイント告知3 */
    POINT_INVALID_03("期限切れポイント告知3", "19"),

    // TODO 不要のため削除されていたが、DBのカラムは削除されていなかったので一旦復元。DMLも作成しつつ、区分値も削除すること
    /** 会員ランク告知1 */
    MEMBER_RANK_NOTIFICATION_01("会員ランク告知1", "20"),

    // TODO 不要のため削除されていたが、DBのカラムは削除されていなかったので一旦復元。DMLも作成しつつ、区分値も削除すること
    /** 会員ランク告知2 */
    MEMBER_RANK_NOTIFICATION_02("会員ランク告知2", "21"),

    // TODO 不要のため削除されていたが、DBのカラムは削除されていなかったので一旦復元。DMLも作成しつつ、区分値も削除すること
    /** 会員ランク告知3 */
    MEMBER_RANK_NOTIFICATION_03("会員ランク告知3", "22"),

    // TODO 不要のため削除されていたが、DBのカラムは削除されていなかったので一旦復元。DMLも作成しつつ、区分値も削除すること
    /** 定期注文確認（初回） */
    PERIODIC_ORDER_CONFIRMATION_FIRST("定期注文確認（初回）", "27"),

    // TODO 不要のため削除されていたが、DBのカラムは削除されていなかったので一旦復元。DMLも作成しつつ、区分値も削除すること
    /** 定期注文確認 */
    PERIODIC_ORDER_CONFIRMATION("定期注文確認", "28"),

    // TODO 不要のため削除されていたが、DBのカラムは削除されていなかったので一旦復元。DMLも作成しつつ、区分値も削除すること
    /** 定期注文作成エラー */
    PERIODIC_ORDER_ERROR("定期注文作成エラー", "29"),

    /** 運用者お問い合わせ */
    INQUIRY_OPERATOR("運用者お問い合わせ", "30"),

    /** 運営者パスワード再設定 */
    ADMIN_PASSWORD_NOTIFICATION("運営者パスワード再設定", "31"),

    /** フォローメール（カート投入後1） */
    CART_RESIDUAL_FIRST("カート放棄フォローメール1回目", "32"),

    /** フォローメール（カート投入後2） */
    CART_RESIDUAL_SECOND("カート放棄フォローメール2回目", "33"),

    // TODO 不要のため削除されていたが、DBのカラムは削除されていなかったので一旦復元。DMLも作成しつつ、区分値も削除すること
    /** 誕生月ポイント付与 */
    POINT_ADD_BIRTH("誕生月ポイント付与", "34"),

    /** 在庫状況 */
    BATCH_MAIL_STOCK_REPORT("在庫状況を管理者にメール送信", "35"),

    /** 支払督促メ／支払期限切れ処理結果メール */
    SETTLEMENT_ADMINISTRATOR_MAIL("支払督促メ／支払期限切れ処理結果メール", "36"),

    /** 支払督促／支払期限切れ処理結果エラーメール */
    SETTLEMENT_ADMINISTRATOR_ERROR_MAIL("支払督促／支払期限切れ処理結果エラーメール", "37"),

    /** オーソリ期限切れ間近注文通知 */
    AUTH_TIME_LIMIT_ADMINISTRATOR_MAIL("オーソリ期限切れ間近注文通知", "38"),

    /** オーソリ期限切れ間近注文異常 */
    AUTH_TIME_LIMIT_ADMINISTRATOR_ERROR_MAIL("オーソリ期限切れ間近注文異常", "39"),

    /** 出荷登録異常 */
    SHIPMENT_REGIST_ADMINISTRATOR_ERROR_MAIL("出荷登録異常", "40"),

    /** 郵便番号マスタ更新通知 */
    ZIPCODE_ADMINISTRATOR_MAIL("郵便番号マスタ更新通知", "41"),

    /** 郵便番号マスタ更新異常 */
    ZIPCODE_ADMINISTRATOR_ERROR_MAIL("郵便番号マスタ更新異常", "42"),

    /** クリア通知 */
    CLEAR_RESULT_ADMINISTRATOR_MAIL("クリア通知", "43"),

    /** クリア異常 */
    CLEAR_RESULT_ADMINISTRATOR_ERROR_MAIL("クリア異常", "44"),

    /** 入金結果受付予備処理結果メール */
    MULPAY_NOTIFICATION_RECOVERY_ADMINISTRATOR_MAIL("入金結果受付予備処理結果メール", "45"),

    /** 入金結果受付予備処理結果異常メール */
    MULPAY_NOTIFICATION_RECOVERY_ADMINISTRATOR_ERROR_MAIL("入金結果受付予備処理結果異常メール", "46"),

    /** 在庫開放異常 */
    STOCK_RELEASE_ADMINISTRATOR_ERROR_MAIL("在庫開放異常", "47"),

    /** クレジット決済通知 */
    CREDIT_LINE_REPORT_MAIL("クレジット決済通知", "48"),

    /** クレジット決済作動通知 */
    CREDIT_LINE_OPERATION_REPORT_MAIL("クレジット決済作動通知", "49"),

    /** クレジット決済異常 */
    CREDIT_LINE_REPORT_ERROR_MAIL("クレジット決済異常", "50"),

    /** 商品グループ在庫状態更新異常 */
    STOCK_STATUS_ADMINISTRATOR_ERROR_MAIL("商品グループ在庫状態更新異常", "51"),

    /** サイトマップXML出力通知 */
    SITEMAP_XML_OUTPUT_ADMINISTRATOR_MAIL("サイトマップXML出力通知", "52"),

    /** サイトマップXML出力異常 */
    SITEMAP_XML_OUTPUT_ADMINISTRATOR_ERROR_MAIL("サイトマップXML出力異常", "53"),

    /** 商品グループ規格画像更新（商品一括アップロード）通知 */
    GOODS_ASYNCHRONOUS_MAIL("商品グループ規格画像更新（商品一括アップロード）通知", "54"),

    /** 商品グループ規格画像更新（商品一括アップロード）異常 */
    GOODS_ASYNCHRONOUS_ERROR_MAIL("商品グループ規格画像更新（商品一括アップロード）異常", "55"),

    /** 商品グループ規格画像更新通知 */
    GOODSIMAGE_UPDATE_MAIL("商品グループ規格画像更新（商品画像更新）通知", "56"),

    /** 商品グループ規格画像更新異常 */
    GOODSIMAGE_UPDATE_ERROR_MAIL("商品グループ規格画像更新（商品画像更新）異常", "57"),

    /** GMO会員カードアラート */
    GMO_MEMBER_CARD_ALERT("GMO会員カードアラート", "56"),

    /** マルチペイメントアラート */
    MULTI_PAYMENT_ALERT("マルチペイメントアラート", "57"),

    /** 注文データ作成アラート */
    ORDER_REGIST_ALERT("注文データ作成アラート", "58"),

    /** 請求不整合報告 */
    SETTLEMENT_MISMATCH("請求不整合報告", "59"),

    /** CSVダウンロード完了報告 */
    DOWNLOAD_CSV_ADMINISTRATOR_MAIL("CSVダウンロード完了報告", "60"),

    /** CSVダウンロードエラー報告 */
    DOWNLOAD_CSV_ADMINISTRATOR_ERROR_MAIL("CSVダウンロードエラー報告", "61"),

    /** 受注向け汎用 */
    GENERAL_ORDER("受注向け汎用", "97"),

    /** 会員向け汎用 */
    GENERAL_MEMBER("会員向け汎用", "98"),

    /** 汎用 */
    GENERAL("汎用", "99"),

    /** GMO決済キャンセル漏れ */
    GMO_PAYMENT_CANCEL_FORGOT("GMO決済キャンセル漏れ", "100"),

    /** 入金完了 */
    PAYMENT_DEPOSITED("入金完了", "62"),

    /** タグクリアバッチ */
    TAG_CLEAR_ERROR_MAIL("タグクリア異常", "63"),

    /** カテゴリ商品更新バッチ */
    CATEGORY_GOODS_REGISTER_UPDATE_ERROR_MAIL("カテゴリ商品更新異常", "64"),

    /** 入金過不足 */
    PAYMENT_EXCESS_ALERT("入金過不足", "101"),

    /** 非同期処理(MQ)エラー通知 */
    BATCH_MQ_ERROR_NOTIFICATION("非同期処理(MQ)エラー通知", "102"),

    /** 認証コードメール */
    CERTIFICATION_CODE("認証コードメール", "65"),

    /** 検査結果登録異常 */
    EXAM_RESULTS_ENTRY_ADMINISTRATOR_ERROR_MAIL("検査結果登録異常", "66"),

    /** 検査結果通知 */
    EXAM_RESULTS_NOTIFICATION("検査結果通知", "67"),

    /** 検査キット受領登録異常 */
    EXAMKIT_RECEIVED_ENTRY_ADMINISTRATOR_ERROR_MAIL("検査キット受領登録異常", "68");

    /** doma用ファクトリメソッド */
    public static HTypeMailTemplateType of(String value) {

        HTypeMailTemplateType hType = EnumTypeUtil.getEnumFromValue(HTypeMailTemplateType.class, value);

        if (hType == null) {
            throw new IllegalArgumentException(value);
        } else {
            return hType;
        }
    }

    /**
     * ラベル<br/>
     */
    private String label;

    /**
     * 区分値<br/>
     */
    private String value;

}
