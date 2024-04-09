package jp.co.itechh.quad.ddd.presentation.accesskeywords.api;

import jp.co.itechh.quad.accesskeywords.presentation.api.param.AccessKeywordListResponse;
import jp.co.itechh.quad.accesskeywords.presentation.api.param.AccessKeywordResponse;
import jp.co.itechh.quad.accesskeywords.presentation.api.param.AccessKeywordsCsvGetRequest;
import jp.co.itechh.quad.accesskeywords.presentation.api.param.AccessKeywordsGetRequest;
import jp.co.itechh.quad.accesskeywords.presentation.api.param.PageInfoRequest;
import jp.co.itechh.quad.core.base.utility.ZenHanConversionUtility;
import jp.co.itechh.quad.core.dto.accesskeywords.AccessKeywordsCSVDto;
import jp.co.itechh.quad.ddd.infrastructure.utility.PageInfoModule;
import jp.co.itechh.quad.ddd.usecase.query.accesskeywords.AccessKeywordsQueryCondition;
import jp.co.itechh.quad.ddd.usecase.query.accesskeywords.AccessKeywordsQueryModel;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.math.BigDecimal;
import java.text.DecimalFormat;
import java.util.List;

import static jp.co.itechh.quad.ddd.usecase.query.accesskeywords.AccessKeywordsQuery.MAX_ACCESS_KEYWORD;

/**
 * 検索キーワード集計ヘルパー
 *
 * @author Pham Quang Dieu (VTI Japan Co., Ltd.)
 */
@Component
public class AccessKeywordsHelper {

    /**
     * ページ情報モジュール
     */
    private final PageInfoModule pageInfoModule;

    /**
     * 全角、半角の変換を行うヘルパークラス
     */
    private final ZenHanConversionUtility zenHanConversionUtility;

    /**
     * Decimalフォーマット
     */
    private final DecimalFormat decimalFormat;

    private final static String DEFAULT_VALUE_INTEGER = "0";

    /**
     * コンストラクタ
     *
     * @param pageInfoModule          ページ情報モジュール
     * @param zenHanConversionUtility 全角、半角の変換を行うヘルパークラス
     */
    @Autowired
    public AccessKeywordsHelper(PageInfoModule pageInfoModule, ZenHanConversionUtility zenHanConversionUtility) {
        this.pageInfoModule = pageInfoModule;
        this.zenHanConversionUtility = zenHanConversionUtility;
        this.decimalFormat = new DecimalFormat("##0.00");
    }

    /**
     * 検索キーワード集計に変換
     *
     * @param request キーワード集計CSVDLリクエスト
     * @return 検索キーワード集計条件
     */
    public AccessKeywordsQueryCondition toDownloadQueryCondition(AccessKeywordsCsvGetRequest request) {
        AccessKeywordsQueryCondition downloadQueryCondition = new AccessKeywordsQueryCondition();
        if (request.getProcessDateFrom() != null) {
            downloadQueryCondition.setProcessDateFrom(request.getProcessDateFrom());
        }
        if (request.getProcessDateTo() != null) {
            downloadQueryCondition.setProcessDateTo(request.getProcessDateTo());
        }
        if (StringUtils.isNotEmpty(request.getSearchKeyword())) {
            String keyword = StringUtils.upperCase(request.getSearchKeyword());
            downloadQueryCondition.setSearchKeyword(zenHanConversionUtility.toZenkaku(keyword));
        }
        if (request.getSearchCountFrom() != null) {
            downloadQueryCondition.setSearchCountFrom(request.getSearchCountFrom());
        }
        if (request.getSearchCountTo() != null) {
            downloadQueryCondition.setSearchCountTo(request.getSearchCountTo());
        }

        return downloadQueryCondition;
    }

    /**
     * 検索キーワード集計に変換
     *
     * @param accessKeyword 検索キーワード集計リクエスト
     * @param pageInfo      ページ情報リクエスト（ページネーションのため）
     * @return 検索キーワード集計条件
     */
    public AccessKeywordsQueryCondition toGetQueryCondition(AccessKeywordsGetRequest accessKeyword,
                                                            PageInfoRequest pageInfo) {
        AccessKeywordsQueryCondition getQueryCondition = new AccessKeywordsQueryCondition();
        if (accessKeyword.getProcessDateFrom() != null) {
            getQueryCondition.setProcessDateFrom(accessKeyword.getProcessDateFrom());
        }
        if (accessKeyword.getProcessDateTo() != null) {
            getQueryCondition.setProcessDateTo(accessKeyword.getProcessDateTo());
        }
        if (StringUtils.isNotEmpty(accessKeyword.getSearchKeyword())) {
            String keyword = StringUtils.upperCase(accessKeyword.getSearchKeyword());
            getQueryCondition.setSearchKeyword(zenHanConversionUtility.toZenkaku(keyword));
        }
        if (accessKeyword.getSearchCountFrom() != null) {
            getQueryCondition.setSearchCountFrom(accessKeyword.getSearchCountFrom());
        }
        if (accessKeyword.getSearchCountTo() != null) {
            getQueryCondition.setSearchCountTo(accessKeyword.getSearchCountTo());
        }
        if (pageInfo != null) {
            getQueryCondition.setPageInfoForQuery(
                            pageInfoModule.setPageInfoForQuery(pageInfo.getPage(), pageInfo.getLimit() + 1,
                                                               pageInfo.getOrderBy(), pageInfo.getSort()
                                                              ));
        }

        return getQueryCondition;
    }

    /**
     * 検索キーワード一覧レスポンスに変換
     *
     * @param queryModelList  検索キーワード集計クエリーモデル
     * @param pageInfoRequest ページ情報リクエスト（ページネーションのため）
     * @return 検索キーワード一覧レスポンス
     */
    public AccessKeywordListResponse toAccessKeywordListResponse(List<AccessKeywordsQueryModel> queryModelList,
                                                                 PageInfoRequest pageInfoRequest) {
        AccessKeywordListResponse listResponse = new AccessKeywordListResponse();
        if (queryModelList == null || queryModelList.size() == 0) {
            return listResponse;
        }

        int size;
        if (pageInfoRequest.getLimit() != null) {
            if (queryModelList.size() > pageInfoRequest.getLimit()) {
                listResponse.setOverFlag(true);
                size = pageInfoRequest.getLimit();
            } else {
                listResponse.setOverFlag(false);
                size = queryModelList.size();
            }
        } else {
            if (queryModelList.size() > MAX_ACCESS_KEYWORD) {
                listResponse.setOverFlag(true);
                size = MAX_ACCESS_KEYWORD;
            } else {
                listResponse.setOverFlag(false);
                size = queryModelList.size();
            }
        }

        for (int i = 0; i < size; i++) {
            AccessKeywordsQueryModel queryModel = queryModelList.get(i);
            AccessKeywordResponse accessKeywordResponse = new AccessKeywordResponse();
            accessKeywordResponse.setSearchKeyword(queryModel.getSearchKeyword());
            accessKeywordResponse.setSearchCount(queryModel.getSearchCount());
            accessKeywordResponse.setSearchResultCount(
                            new BigDecimal(decimalFormat.format(queryModel.getSearchResultCount())));
            listResponse.addAccessKeywordListItem(accessKeywordResponse);
        }

        return listResponse;
    }

    /**
     * 検索キーワード集計Dtoクラスに変換
     *
     * @param queryModel 受注検索クエリーモデル
     * @return 検索キーワード集計Dtoクラス
     */
    public AccessKeywordsCSVDto toAccessKeywordsCSVDto(AccessKeywordsQueryModel queryModel) {

        AccessKeywordsCSVDto accessKeywordsCSVDto = new AccessKeywordsCSVDto();

        String searchCount = queryModel.getSearchCount() != null ?
                        String.valueOf(queryModel.getSearchCount()) :
                        DEFAULT_VALUE_INTEGER;
        String searchResultCount = decimalFormat.format(queryModel.getSearchResultCount());

        accessKeywordsCSVDto.setSearchKeyword(queryModel.getSearchKeyword());
        accessKeywordsCSVDto.setSearchCount(searchCount);
        accessKeywordsCSVDto.setSearchResultCount(searchResultCount);

        return accessKeywordsCSVDto;
    }

}