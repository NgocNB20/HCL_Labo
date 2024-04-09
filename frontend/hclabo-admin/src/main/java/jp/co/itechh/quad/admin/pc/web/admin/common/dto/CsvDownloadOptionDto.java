/*
 * Copyright (C) 2022 i-TEC HANKYU HANSHIN INC. All Rights Reserved.
 *
 */

package jp.co.itechh.quad.admin.pc.web.admin.common.dto;

import jp.co.itechh.quad.admin.pc.web.admin.common.validation.group.OptionDownloadGroup;
import lombok.Data;
import org.hibernate.validator.constraints.Length;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Component;

import javax.validation.Valid;
import java.io.Serializable;
import java.util.List;

/**
 * CSVDLオプショDtoクラス
 *
 * @author Doan Thang (VJP)
 */

@Data
@Component
@Scope("prototype")
public class CsvDownloadOptionDto implements Serializable {

    /** serialVersionUID */
    private static final long serialVersionUID = 1L;

    /** モデルの CSVオプションをリセットする(true:リセット/false:リセットしない) */
    private Boolean resetFlg;

    /** CSV 更新オプション (true: 更新する/false: 更新しない) */
    private Boolean updateFlg;

    /** オプションId */
    private String optionId;

    /** オプション名のデフォルト */
    private String defaultOptionName;

    /** オプション名 */
    @Length(min = 1, max = 15, groups = OptionDownloadGroup.class)
    private String optionName;

    /** ヘッダー行を出力する(true:出力する/false:出力するない) */
    private Boolean outHeader;

    /** オプションコンテンツ */
    @Valid
    private List<OptionDto> optionContent;
}