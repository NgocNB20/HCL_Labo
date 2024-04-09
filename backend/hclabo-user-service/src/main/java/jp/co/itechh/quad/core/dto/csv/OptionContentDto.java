package jp.co.itechh.quad.core.dto.csv;

import lombok.Data;
import org.seasar.doma.Entity;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Component;

/**
 * オプションコンテンツDTO
 *
 * @author Pham Quang Dieu (VTI Japan Co., Ltd.)
 */
@Entity
@Data
@Component
@Scope("prototype")
public class OptionContentDto {

    /** アイテム名 */
    private String itemName;

    /** デフォルトの出力順序 */
    private int defaultOrder;

    /** 出力順序 */
    private int order;

    /** デフォルカラム名称 */
    private String defaultColumnLabel;

    /** カラム名称 */
    private String columnLabel;

    /** 出力フラグ */
    private boolean outFlag;
}

