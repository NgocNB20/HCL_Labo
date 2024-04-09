package jp.co.itechh.quad.admin.pc.web.admin.shop.delivery.sca;

import lombok.Data;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Component;

import java.io.Serializable;
import java.math.BigDecimal;

/**
 * 配送方法設定：特別料金エリア設定検索画面検索結果用アイテム
 *
 * @author Pham Quang Dieu (VTI Japan Co., Ltd.)
 */
@Data
@Component
@Scope("prototype")
public class DeliveryScaModelItem implements Serializable {
    /**
     * シリアルバージョンID
     */
    private static final long serialVersionUID = 1L;

    /** 配送方法SEQ */
    private Integer deliveryMethodSeq;

    /** 郵便番号 */
    private String zipcode;

    /** 送料 */
    private BigDecimal carriage;

    /** 住所一覧 */
    public String addressList;

    /** 選択チェックボックス */
    private boolean check;

    /** 行番号 */
    private int resultDataIndex;
}