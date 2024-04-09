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

/**
 *オプションコンテンツDtoクラス
 *
 * @author Doan Thang (VJP)
 */
@Data
@Component
@Scope("prototype")
public class OptionDto {

    /** field名 */
    private String itemName;

    /** デフォルトの出力順序 */
    private Integer defaultOrder;

    /** 出力順序 */
    private Integer order;

    /** デフォルト注文番号 */
    private String defaultColumnLabel;

    /** カラム名称 */
    @Length(min = 0, max = 15, groups = OptionDownloadGroup.class)
    private String columnLabel;

    /** 該当ヘッダ出力する（true:出力する/false:出力するない） */
    private Boolean outFlag = true;
}
