package jp.co.itechh.quad.admin.pc.web.admin.goods.category;

import lombok.Data;

import java.io.Serializable;

/**
 * サイドメニュー設定カテゴリーアイテム
 *
 * @author Doan Thang (VJP)
 */
@Data
public class SideMenuSettingsCategoryItem implements Serializable {

    /** serialVersionUID */
    private static final long serialVersionUID = 1L;

    /** カテゴリーId */
    private String categoryId;

    /** カテゴリー名 */
    private String categoryName;

    /** 表示名 */
    private String displayName;
}
