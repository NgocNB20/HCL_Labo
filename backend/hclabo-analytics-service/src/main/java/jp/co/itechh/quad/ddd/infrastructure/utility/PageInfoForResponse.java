package jp.co.itechh.quad.ddd.infrastructure.utility;

import lombok.Data;

/**
 * ページ情報レスポンスクラス
 *
 * @author Pham Quang Dieu (VTI Japan Co., Ltd.)
 */
@Data
public class PageInfoForResponse {

    /**
     * ページ番号
     */
    private Integer page;

    /**
     * 表示最大件数
     */
    private Integer limit;

    /**
     * 次ページを表示するか
     */
    private Integer nextPage;

    /**
     * 前ページを表示するか
     */
    private Integer prevPage;

    /**
     * 総レコード数
     */
    private Integer total;

    /**
     * 総ページ数
     */
    private Integer totalPages;

    /**
     * コンストラクタ
     */
    public PageInfoForResponse() {
    }
}
