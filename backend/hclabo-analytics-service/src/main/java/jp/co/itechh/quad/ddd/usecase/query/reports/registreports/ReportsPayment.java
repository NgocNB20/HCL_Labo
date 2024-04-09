package jp.co.itechh.quad.ddd.usecase.query.reports.registreports;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.springframework.data.mongodb.core.mapping.Field;

/**
 * 決済情報
 */

@Getter
@Setter
@NoArgsConstructor
public class ReportsPayment {

    /**
     * 決済方法ID
     */
    private Integer paymentMethodId;

    /**
     * 決済方法名
     */
    private String paymentMethodName;

    /**
     * 決済手段識別子
     */
    @Field(write = Field.Write.ALWAYS)
    private String payMethod;

    /**
     * リンク決済：決済方法（GMO）
     */
    @Field(write = Field.Write.ALWAYS)
    private Integer payType;

    /**
     * 決済手段識別子
     */
    @Field(write = Field.Write.ALWAYS)
    private String payTypeName;

    /**
     * 仕向先コード
     */
    @Field(write = Field.Write.ALWAYS)
    private String forward;

    /**
     * 支払先コンビニコード
     */
    @Field(write = Field.Write.ALWAYS)
    private String cvsCode;

    /**
     * 集計決済方法ID
     */
    private Integer aggregationPayId;

    /**
     * 集計決済方法名
     */
    private String aggregationPayName;

}
