/*
 * Copyright (C) 2022 i-TEC HANKYU HANSHIN INC. All Rights Reserved.
 */

package jp.co.itechh.quad.front.pc.web.front.goods.common;

import lombok.Data;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Component;

/**
 * カテゴリItem
 *
 * @author kn23834
 * @version $Revision: 1.0 $
 *
 */
@Data
@Component
@Scope("prototype")
public class CategoryItem {

    /** カテゴリID */
    private String cid;
    /** カテゴリ表示名PC */
    private String categoryName;
    /** フロント表示状態　※プレビュー日時を加味して判定したステータス */
    private String frontDisplayStatus;
    /** 階層付き通番 */
    private String hierarchicalSerialNumber;
}