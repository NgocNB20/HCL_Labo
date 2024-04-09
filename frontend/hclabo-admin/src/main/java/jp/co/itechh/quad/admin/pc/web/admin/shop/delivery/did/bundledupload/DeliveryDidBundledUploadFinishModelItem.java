package jp.co.itechh.quad.admin.pc.web.admin.shop.delivery.did.bundledupload;

import lombok.Data;
import org.apache.commons.lang.StringUtils;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Component;

import java.io.Serializable;

/**
 * 一括アップロード完了<br/>
 *
 * @author Pham Quang Dieu (VTI Japan Co., Ltd.)
 *
 */
@Data
@Component
@Scope("prototype")
public class DeliveryDidBundledUploadFinishModelItem implements Serializable {

    /** serialVersionUID */
    private static final long serialVersionUID = 1L;

    /** エラー行 */
    private Integer row;

    /** エラーカラムNo */
    private String columnName;

    /** エラー内容 */
    private String message;

    /**
     * 項目名判定
     *
     * @return true=有、false=無
     */
    public boolean isColumn() {
        return StringUtils.isNotEmpty(columnName);
    }

}