/*
 * Copyright (C) 2022 i-TEC HANKYU HANSHIN INC. All Rights Reserved.
 *
 */

package jp.co.itechh.quad.core.dto.csv;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.util.List;

/**
 * 商品CSVDLオプションDTOクラス
 */
@Getter
@Setter
@NoArgsConstructor
public class ProductCsvDownloadOptionDto {

    /** Id */
    private Integer optionId;

    /** オプション名 */
    private String optionName;

    /** オプション名のデフォルト */
    private String defaultOptionName;

    /** ヘッダー出力フラグ */
    private boolean outHeader;

    /** オプションコンテンツDto */
    private List<OptionContentDto> optionContent;
}
