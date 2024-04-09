package jp.co.itechh.quad.ddd.infrastructure.utility;

import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Component;

/**
 * ページ情報モジュール
 * ページ情報の設定を行うの便利メソッドを提供する
 *
 * @author Pham Quang Dieu (VTI Japan Co., Ltd.)
 */
@Component
public class PageInfoModule {

    /**
     * デフォルトページサイズ番号
     */
    public static final int DEFAULT_SIZE = 100;

    /**
     * デフォルトページ番号
     */
    public static final int DEFAULT_PAGE = 1;

    /**
     * ページングセットアップQuery
     *
     * @param page     ページ番号
     * @param size     ページサイズ
     * @param orderBy  ソート項目
     * @param sortType 昇順/降順フラグ
     * @return ページ情報クラスQuery
     */
    public PageInfoForQuery setPageInfoForQuery(Integer page, Integer size, String orderBy, Boolean sortType) {
        PageInfoForQuery pageInfoForQuery = new PageInfoForQuery();

        if (!orderBy.isEmpty() && sortType != null) {
            Sort.Direction sortDirection;
            if (sortType) {
                sortDirection = Sort.Direction.ASC;
            } else {
                sortDirection = Sort.Direction.DESC;
            }
            Sort sort = Sort.by(sortDirection, orderBy);
            pageInfoForQuery.setSort(sort);
        }

        if (size == null) {
            size = DEFAULT_SIZE;
        }

        if (page != null) {
            if (page == 0) {
                page = DEFAULT_PAGE;
            }
            Pageable pageable = PageRequest.of(page - 1, size);
            pageInfoForQuery.setPageable(pageable);
        } else {
            Pageable pageable = PageRequest.of(1, size);
            pageInfoForQuery.setPageable(pageable);
        }
        return pageInfoForQuery;
    }

    /**
     * ページングセットアップResponse
     *
     * @param page  ページ番号
     * @param size  ページサイズ
     * @param count 総レコード数
     * @return ページ情報クラスResponse
     */
    public PageInfoForResponse setPageInfoForResponse(Integer page, Integer size, Integer count) {
        PageInfoForResponse pageInfoForResponse = new PageInfoForResponse();

        if (page != null && size != null) {
            pageInfoForResponse.setPage(page);
            pageInfoForResponse.setLimit(size);

            if (page == 1) {
                pageInfoForResponse.setPrevPage(-1);
            } else {
                pageInfoForResponse.setPrevPage(page - 1);
            }

            pageInfoForResponse.setTotal(count);

            int totalPages = count / size + ((count % size == 0) ? 0 : 1);
            pageInfoForResponse.setTotalPages(totalPages);

            if (page >= totalPages) {
                pageInfoForResponse.setNextPage(-1);
            } else {
                pageInfoForResponse.setNextPage(page + 1);
            }
        }

        return pageInfoForResponse;
    }
}
