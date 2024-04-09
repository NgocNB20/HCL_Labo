package jp.co.itechh.quad.admin.pc.web.admin.goods.registupdate;

import lombok.Data;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Component;

import java.io.Serializable;

/**
 * 商品管理：商品カテゴリーアイテム(ツリー形)<br/>
 *
 * @author Pham Quang Dieu (VTI Japan Co., Ltd.)
 *
 */
@Data
@Component
@Scope("prototype")
public class GoodsCategoryTreeItem implements Serializable {

    /** シリアルバージョンID */
    private static final long serialVersionUID = 1L;

    /** カテゴリ登録チェック */
    private boolean registCategoryCheck;

    /** カテゴリSEQ */
    private Integer categorySeq;

    /** ID */
    private String id;

    /** 親ノード */
    private String parentNode;

    /** 表示ラベル */
    private String label;

    /** ノードレベル */
    private Integer level;
}