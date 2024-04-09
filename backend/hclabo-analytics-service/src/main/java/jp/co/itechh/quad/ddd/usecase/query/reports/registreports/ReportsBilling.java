package jp.co.itechh.quad.ddd.usecase.query.reports.registreports;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.springframework.data.mongodb.core.mapping.Field;

/**
 * 請求先情報
 */

@Getter
@Setter
@NoArgsConstructor
public class ReportsBilling {

    /**
     * 請求先氏名
     */
    private String billingName;

    /**
     * 請求先郵便番号
     */
    private String billingZipCode;

    /**
     * 請求先都道府県
     */
    private String billingPrefecture;

    /**
     * 請求先住所1
     */
    private String billingAddress1;

    /**
     * 請求先住所2
     */
    private String billingAddress2;

    /**
     * 請求先住所3
     */
    @Field(write = Field.Write.ALWAYS)
    private String billingAddress3;

    /**
     * 請求先住所録ID
     */
    private String billingAddressId;

}
