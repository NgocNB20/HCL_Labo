package jp.co.itechh.quad.ddd.usecase.query.reports.registreports;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.springframework.data.mongodb.core.mapping.Field;

/**
 * 処理ステータス
 */

@Getter
@Setter
@NoArgsConstructor
public class ReportsCustomer {

    /**
     * 顧客ID
     */
    private Integer customerId;

    /**
     * 会員氏名
     */
    private String customerName;

    /**
     * 会員メールアドレス
     */
    private String customerMail;

    /**
     * 会員年齢
     */
    private Integer customerAge;

    /**
     * 会員生年月日
     */
    private String customerBirthday;

    /**
     * 会員性別
     */
    private String customerSex;

    /**
     * 会員郵便番号
     */
    private String customerZipCode;

    /**
     * 会員都道府県
     */
    private String customerPrefecture;

    /**
     * 会員住所1
     */
    private String customerAddress1;

    /**
     * 会員住所2
     */
    private String customerAddress2;

    /**
     * 会員住所3
     */
    @Field(write = Field.Write.ALWAYS)
    private String customerAddress3;

    /**
     * リピート購入種別
     */
    private String repeatPurchaseType;

    /**
     * メールマガジン購読フラグ
     */
    private String magazineSubscribeType;

}
