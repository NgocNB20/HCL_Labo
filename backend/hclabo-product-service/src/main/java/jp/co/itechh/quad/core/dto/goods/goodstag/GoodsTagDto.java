package jp.co.itechh.quad.core.dto.goods.goodstag;

import jp.co.itechh.quad.core.base.dto.AbstractConditionDto;
import lombok.Data;
import org.seasar.doma.Entity;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Component;

/**
 * タグ商品Dtoクラス
 *
 * @author Pham Quang Dieu
 */
@Entity
@Data
@Component
@Scope("prototype")
public class GoodsTagDto extends AbstractConditionDto {

    /** タグ*/
    private String tag;

}
