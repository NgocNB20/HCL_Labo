package jp.co.itechh.quad.admin.pc.web.admin.shop.settlement;

import jp.co.itechh.quad.admin.constant.type.HTypeBillType;
import jp.co.itechh.quad.admin.constant.type.HTypeOpenDeleteStatus;
import jp.co.itechh.quad.admin.constant.type.HTypeSettlementMethodCommissionType;
import jp.co.itechh.quad.admin.constant.type.HTypeSettlementMethodType;
import lombok.Data;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Component;

import java.io.Serializable;

@Data
@Component
@Scope("prototype")
public class SettlementModelItem implements Serializable {

    /**
     * シリアルNo<br/>
     */
    private static final long serialVersionUID = 1L;

    /**
     * 表示順値
     */
    private Integer orderDisplayValue;

    /**
     * 決済方法SEQ
     */
    private Integer settlementMethodSeq;

    /**
     * 決済方法名称
     */
    private String settlementMethodName;

    /**
     * 公開状態PC
     */
    private HTypeOpenDeleteStatus openStatusPc;

    /**
     * 決済方法種別
     */
    private HTypeSettlementMethodType settlementMethodType;

    /**
     * 請求種別
     */
    private HTypeBillType billType;

    /**
     * 配送方法SEQ
     */
    private Integer deliveryMethodSeq;

    /**
     * 配送方法名
     */
    private String deliveryMethodName;

    /**
     * 決済方法手数料種別
     */
    private HTypeSettlementMethodCommissionType settlementMethodCommissionType;

    public boolean isDiscountSettlement() {
        return HTypeSettlementMethodType.DISCOUNT.equals(getSettlementMethodType());
    }

    public String getOrderDisplayRadioValue() {
        return orderDisplayValue.toString();
    }
}
