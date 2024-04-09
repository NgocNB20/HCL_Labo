package jp.co.itechh.quad.admin.dto.goods.category;

import lombok.Data;
import lombok.EqualsAndHashCode;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Component;

import java.io.Serializable;
import java.util.List;

/**
 * カテゴリー木構造Dtoクラス
 *
 * @author Doan Thang (VJP)
 */
@Data
@EqualsAndHashCode(callSuper = false)
@Component
@Scope("prototype")
public class CategoryTreeDto implements Serializable {
    /** serialVersionUID */
    private static final long serialVersionUID = 1L;

    /** カテゴリID */
    private String categoryId;

    /** カテゴリ名 */
    private String categoryName;

    /** 表示名 */
    private String displayName;

    /** エラー内容 */
    private String error;

    /** 通番（同一階層内の通番） */
    private int serialNumber;

    /** カテゴリツリーDTOリスト */
    private List<CategoryTreeDto> categoryTreeDtoList;
}
