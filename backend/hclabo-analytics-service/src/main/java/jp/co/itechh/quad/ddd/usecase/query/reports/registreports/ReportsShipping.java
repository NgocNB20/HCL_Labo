package jp.co.itechh.quad.ddd.usecase.query.reports.registreports;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.springframework.data.mongodb.core.mapping.Field;

/**
 * 配送先情報
 */

@Setter
@Getter
@NoArgsConstructor
public class ReportsShipping {

    /**
     * 配送方法ID
     */
    private Integer shippingMethodId;

    /**
     * 配送方法名
     */
    private String shippingMethodName;

    /**
     * 配送先氏名
     */
    private String shippingName;

    /**
     * 配送先郵便番号
     */
    private String shippingZipCode;

    /**
     * 配送先都道府県
     */
    private String shippingPrefecture;

    /**
     * 配送先住所1
     */
    private String shippingAddress1;

    /**
     * 配送先住所2
     */
    private String shippingAddress2;

    /**
     * 配送先住所3
     */
    @Field(write = Field.Write.ALWAYS)
    private String shippingAddress3;

    /**
     * 配送先住所録ID
     */
    private String shippingAddressId;

}
