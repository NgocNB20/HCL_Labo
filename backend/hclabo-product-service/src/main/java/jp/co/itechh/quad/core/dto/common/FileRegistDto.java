/*
 * Project Name : HIT-MALL3
 *
 * Copyright (C) 2008 i-TEC HANKYU HANSHIN INC. All Rights Reserved.
 *
 */

package jp.co.itechh.quad.core.dto.common;

import lombok.Data;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Component;

import java.io.Serializable;

/**
 * ファイル登録Dtoクラス
 *
 * @author DtoGenerator
 * @version $Revision: 1.1 $
 */
@Data
@Component
@Scope("prototype")
public class FileRegistDto implements Serializable {

    /** serialVersionUID */
    private static final long serialVersionUID = 1L;

    /** 登録元ファイルパス */
    private String fromFilePath;

    /** 登録先ファイルパス */
    private String toFilePath;
}
