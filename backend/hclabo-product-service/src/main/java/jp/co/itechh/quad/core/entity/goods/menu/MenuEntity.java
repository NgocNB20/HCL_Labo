/*
 * Project Name : HIT-MALL4
 *
 * Copyright (C) 2022 i-TEC HANKYU HANSHIN INC. All Rights Reserved.
 *
 */

package jp.co.itechh.quad.core.entity.goods.menu;

import lombok.Data;
import org.seasar.doma.Column;
import org.seasar.doma.Entity;
import org.seasar.doma.Id;
import org.seasar.doma.Table;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Component;

import java.io.Serializable;
import java.sql.Timestamp;

/**
 * メニュークラス
 *
 * @author EntityGenerator
 * @version $Revision: 1.0 $
 */
@Entity
@Table(name = "Menu")
@Data
@Component
@Scope("prototype")
public class MenuEntity implements Serializable {

    /** serialVersionUID */
    private static final long serialVersionUID = 1L;

    /** メニューID */
    @Column(name = "menuId")
    @Id
    private Integer menuId;

    /** カテゴリーツリー */
    @Column(name = "categoryTree")
    private String categoryTree;

    /** 登録日時 */
    @Column(name = "registTime", updatable = false)
    private Timestamp registTime;

    /** 更新日時 */
    @Column(name = "updateTime")
    private Timestamp updateTime;
}
