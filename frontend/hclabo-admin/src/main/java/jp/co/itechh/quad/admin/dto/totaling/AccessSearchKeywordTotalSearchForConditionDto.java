/*
 * Copyright (C) 2022 i-TEC HANKYU HANSHIN INC. All Rights Reserved.
 */

package jp.co.itechh.quad.admin.dto.totaling;

import jp.co.itechh.quad.admin.base.dto.AbstractConditionDto;
import lombok.Data;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Component;

import java.sql.Timestamp;
import java.util.List;

/**
 * 検索キーワード集計用検索条件Dtoクラス
 *
 * @author DtoGenerator
 * @version $Revision: 1.0 $
 */
@Data
@Component
@Scope("prototype")
public class AccessSearchKeywordTotalSearchForConditionDto extends AbstractConditionDto {
    /** serialVersionUID */
    private static final long serialVersionUID = 1L;

    /** 期間－From */
    private Timestamp processDateFrom;

    /** 期間－To */
    private Timestamp processDateTo;

    /** 受付場所 */
    private List<String> orderSiteTypeList;

    /** キーワード */
    private String searchKeyword;

    /** 検索回数－From */
    private Integer searchResultCountFrom;

    /** 検索回数－To */
    private Integer searchResultCountTo;

    /** ショップSEQ */
    private Integer shopSeq;

    /** ソート条件 */
    private String orderByCondition;
}