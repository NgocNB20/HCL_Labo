package jp.co.itechh.quad.front.pc.web.front.order;

import jp.co.itechh.quad.front.web.AbstractModel;
import lombok.Data;

import java.util.List;

/**
 * 配送方法Model
 * @author PHAM QUANG DIEU (VJP)
 */
@Data
public class ShipSelectModel extends AbstractModel {

    /** 選択可能配送方法一覧 */
    private List<SelectableShippingMethodItem> selectableShippingMethodList;

    /** 配送伝票ID */
    private String shippingSlipId;

    /** 配送先住所ID（住所ID）*/
    private String shippingAddressId;

    /** 配送方法ID(配送方法SEQ) */
    private String shippingMethodId;

    /** お届け希望日 */
    private String receiverDate;

    /** お届け時間帯 */
    private String receiverTimeZone;

    /** 納品書要否フラグ */
    private Boolean invoiceNecessaryFlag;
}

