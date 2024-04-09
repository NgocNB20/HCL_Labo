/*
 * Project Name : HIT-MALL3
 *
 * Copyright (C) 2008 i-TEC HANKYU HANSHIN INC. All Rights Reserved.
 *
 */

package jp.co.itechh.quad.admin.pc.web.admin.order;

import lombok.Data;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Component;

import java.io.Serializable;

/**
 * 一括アップロード完了<br/>
 *
 * @author Pham Quang Dieu (VTI Japan Co., Ltd.)
 *
 */
@Data
@Component
@Scope("prototype")
public class OrderShipmentUploadModelItem implements Serializable {

    /** serialVersionUID */
    private static final long serialVersionUID = 1L;

    /** エラー内容 */
    private String errContent;

    /** 行番号 */
    private Integer rowNumber;

    /** 列名称 */
    private String columnLabel;

}
