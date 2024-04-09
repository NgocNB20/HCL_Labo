package jp.co.itechh.quad.admin.pc.web.admin.goods.category;

import lombok.Data;

import java.io.Serializable;

/**
 * カテゴリーツリーエラーアイテム
 *
 * @author Doan Thang (VJP)
 */
@Data
public class CategoryTreeErrorItem implements Serializable {

    /** serialVersionUID */
    private static final long serialVersionUID = 1L;

    /** インデックス */
    private int index;

    /** メッセージ */
    private String msg;
}
