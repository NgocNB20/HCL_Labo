package jp.co.itechh.quad.front.pc.web.front.order;

import jp.co.itechh.quad.front.web.AbstractModel;
import lombok.Data;

import java.util.List;

/**
 * 選択可能配送方法
 * @author PHAM QUANG DIEU (VJP)
 */
@Data
public class SelectableShippingMethodItem extends AbstractModel {

    /** 配送方法ID(配送方法SEQ) */
    private String shippingMethodId;

    /** 配送方法名 */
    private String shippingMethodName;

    /** 配送方法説明文 */
    private String shippingMethodNote;

    /** お届け希望日リスト */
    private List<String> receiverDateList = null;

    /** お届け希望時間帯リスト */
    private List<String> receiverTimeZoneList = null;

}

