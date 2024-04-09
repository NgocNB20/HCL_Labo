package jp.co.itechh.quad.admin.pc.web.admin.shop.settlement.registupdate;

import jp.co.itechh.quad.admin.annotation.converter.HCNumber;
import jp.co.itechh.quad.admin.annotation.validator.HVNumber;
import jp.co.itechh.quad.admin.base.utility.ApplicationContextUtility;
import jp.co.itechh.quad.admin.base.utility.ConversionUtility;
import jp.co.itechh.quad.admin.base.utility.NumberUtility;
import jp.co.itechh.quad.admin.base.utility.ZenHanConversionUtility;
import jp.co.itechh.quad.admin.pc.web.admin.common.validation.group.ConfirmGroup;
import lombok.Data;
import org.hibernate.validator.constraints.Length;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Component;

import javax.validation.constraints.DecimalMin;
import javax.validation.constraints.Digits;
import java.io.Serializable;
import java.sql.Timestamp;

@Data
@Component
@Scope("prototype")
public class SettlementRegistUpdateModelItem implements Serializable {

    /** シリアル */
    private static final long serialVersionUID = 1L;

    /**
     * 決済方法SEQ
     */
    private Integer settlementmethodseq;

    /**
     * 上限金額
     */
    @HCNumber
    @HVNumber(groups = {ConfirmGroup.class})
    @Length(min = 0, max = 8, groups = {ConfirmGroup.class})
    @Digits(integer = 8, fraction = 0, groups = {ConfirmGroup.class})
    @DecimalMin(value = "0", inclusive = false, groups = {ConfirmGroup.class})
    private String maxPrice;

    /**
     * 手数料
     */
    @HCNumber
    @HVNumber(groups = {ConfirmGroup.class})
    @Length(min = 0, max = 8, groups = {ConfirmGroup.class})
    @Digits(integer = 8, fraction = 0, groups = {ConfirmGroup.class})
    private String commission;

    /**
     * 登録日時
     */
    private Timestamp registtime;

    /**
     * 更新日時
     */
    private Timestamp updatetime;

    /**
     * @param maxPrice the maxPrice to set
     */
    public void setMaxPrice(String maxPrice) {
        this.maxPrice = toNumber(maxPrice);
    }

    /**
     * @param commission the commission to set
     */
    public void setCommission(String commission) {
        this.commission = toNumber(commission);
    }

    /**
     * @param value 文字列
     * @return 数字の場、合数値として返す
     */
    protected String toNumber(String value) {
        // 全角、半角の変換Helper取得
        ZenHanConversionUtility zenHanConversionUtility =
                        ApplicationContextUtility.getBean(ZenHanConversionUtility.class);

        value = zenHanConversionUtility.toHankaku(value);

        // 数値関連Helper取得
        NumberUtility numberUtility = ApplicationContextUtility.getBean(NumberUtility.class);

        if (value != null && value.indexOf('.') < 0 && numberUtility.isNumber(value)) {
            // 変換Helper取得
            ConversionUtility conversionUtility = ApplicationContextUtility.getBean(ConversionUtility.class);

            return conversionUtility.toBigDecimal(value).toString();
        }
        return value;
    }
}