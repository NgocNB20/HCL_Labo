package jp.co.itechh.quad.admin.pc.web.admin.shop.delivery.did;

import lombok.Data;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Component;

import java.io.Serializable;
import java.util.Date;

/**
 *
 * お届け不可日検索
 *
 * @author Pham Quang Dieu (VTI Japan Co., Ltd.)
 *
 */
@Data
@Component
@Scope("prototype")
public class DeliveryDidModelItem implements Serializable {

    /** serialVersionUID */
    private static final long serialVersionUID = 1L;

    /** 年月日 */
    private Date date;

    /** 理由 */
    private String reason;

}