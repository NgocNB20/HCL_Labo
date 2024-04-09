/*
 * Project Name : HIT-MALL4
 *
 * Copyright (C) 2021 i-TEC HANKYU HANSHIN INC. All Rights Reserved.
 *
 */

package jp.co.itechh.quad.core.dto.shop;

import jp.co.itechh.quad.core.base.dto.AbstractConditionDto;
import jp.co.itechh.quad.core.constant.type.HTypeOpenStatus;
import lombok.Data;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Component;

import java.sql.Timestamp;

/**
 * ニュースDao用検索条件Dto(管理機能用）クラス
 *
 * @author DtoGenerator
 * @version $Revision: 1.0 $
 */
@Data
@Component
@Scope("prototype")
public class NewsSearchForBackDaoConditionDto extends AbstractConditionDto {

    /** serialVersionUID */
    private static final long serialVersionUID = 1L;

    /** ショップSEQ */
    private Integer shopSeq;

    /** サイト区分 */
    private String site;

    /** ニュース日時From */
    private Timestamp newsTimeFrom;

    /** ニュース日時To */
    private Timestamp newsTimeTo;

    /** 公開状態 */
    private HTypeOpenStatus openStatus;

    /** 公開開始日時From */
    private Timestamp openStartTimeFrom;

    /** 公開開始日時To */
    private Timestamp openStartTimeTo;

    /** 公開終了日時From */
    private Timestamp openEndTimeFrom;

    /** 公開終了日時To */
    private Timestamp openEndTimeTo;

    /** タイトル */
    private String title;

    /** URL */
    private String url;

    /** 本文・詳細 */
    private String bodyNote;
}