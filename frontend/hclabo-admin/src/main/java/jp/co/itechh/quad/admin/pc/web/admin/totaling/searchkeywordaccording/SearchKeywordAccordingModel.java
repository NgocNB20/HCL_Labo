package jp.co.itechh.quad.admin.pc.web.admin.totaling.searchkeywordaccording;

import jp.co.itechh.quad.admin.annotation.converter.HCDate;
import jp.co.itechh.quad.admin.annotation.converter.HCNumber;
import jp.co.itechh.quad.admin.annotation.validator.HVDate;
import jp.co.itechh.quad.admin.annotation.validator.HVNumber;
import jp.co.itechh.quad.admin.annotation.validator.HVRDateGreaterEqual;
import jp.co.itechh.quad.admin.annotation.validator.HVRNumberGreaterEqual;
import jp.co.itechh.quad.admin.annotation.validator.HVSpecialCharacter;
import jp.co.itechh.quad.admin.base.constant.ValidatorConstants;
import jp.co.itechh.quad.admin.dto.totaling.AccessSearchKeywordTotalDto;
import jp.co.itechh.quad.admin.web.AbstractModel;
import lombok.Data;
import org.hibernate.validator.constraints.Length;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Component;

import javax.validation.constraints.Digits;
import javax.validation.constraints.NotEmpty;
import java.math.BigDecimal;
import java.util.List;

/**
 * 検索キーワード集計ページクラス。
 * <p>
 * 検索キーワード集計画面表示項目。
 *
 * @author Doan Thang (VTI Japan Co., Ltd.)
 */
@Data
@HVRDateGreaterEqual(target = "processDateTo", comparison = "processDateFrom")
@HVRNumberGreaterEqual(target = "searchResultCountTo", comparison = "searchResultCountFrom")
public class SearchKeywordAccordingModel extends AbstractModel {

    /** ページ番号 */
    private String pageNumber;

    /** 最大表示件数 */
    private int limit;

    /**
     * 検索条件 期間－From
     */
    @NotEmpty
    @HVDate
    @HCDate
    private String processDateFrom;

    /**
     * 検索条件 期間－To
     */
    @NotEmpty
    @HVDate
    @HCDate
    private String processDateTo;

    /**
     * 検索条件 キーワード
     */
    @Length(min = 0, max = ValidatorConstants.LENGTH_GOODS_KEYWORD_MAXIMUM)
    @HVSpecialCharacter(allowPunctuation = true)
    private String keyword;

    /**
     * 検索条件 検索回数－From
     */
    @HVNumber
    @Digits(integer = 10, fraction = 0)
    @HCNumber
    private String searchResultCountFrom;

    /**
     * 検索条件 検索回数－To
     */
    @HVNumber
    @Digits(integer = 10, fraction = 0)
    @HCNumber
    private String searchResultCountTo;

    /**
     * 検索条件 ショップSEQ
     */
    private Integer shopSeq;

    /**
     * ソート条件
     */
    private String orderByCondition;

    /**
     * 検索結果 リスト
     */
    private List<AccessSearchKeywordTotalDto> resultDataItems;

    /**
     * 検索結果 オブジェクト
     */
    private AccessSearchKeywordTotalDto resultData;

    /**
     * 検索結果 行番号
     */
    private int resultDataIndex;

    /**
     * 検索条件 キーワード
     */
    private String searchKeyword;

    /**
     * 検索結果 検索回数
     */
    @HCNumber
    private Integer searchCount;

    /**
     * 検索結果 検索結果数
     */
    private BigDecimal searchResultCount;

    /**
     * 検索結果 合計行フラグ
     */
    private boolean totalFlag = false;

    /**
     * 行のClass
     */
    private String lineClass;

    /**
     * Csv出力限界値<br/>
     * ※デフォルト-1=無制限とする
     */
    private Integer csvLimit;

    /**
     * Gets result data index.
     *
     * @return the resultDataIndex
     */
    public int getResultDataIndex() {
        return resultDataIndex + 1;
    }

    /* 画面表示判定 */

    /**
     * 検索結果表示判定<br/>
     *
     * @return true =検索結果がnull以外(0件リスト含む), false=検索結果がnull
     */
    public boolean isResult() {
        return getResultDataItems() != null;
    }
}