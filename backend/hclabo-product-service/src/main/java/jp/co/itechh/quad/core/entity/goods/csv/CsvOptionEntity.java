/*
 * Copyright (C) 2022 i-TEC HANKYU HANSHIN INC. All Rights Reserved.
 *
 */

package jp.co.itechh.quad.core.entity.goods.csv;

import lombok.Data;
import org.seasar.doma.Column;
import org.seasar.doma.Entity;
import org.seasar.doma.Id;
import org.seasar.doma.Table;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Component;

import java.io.Serializable;

/**
 * 商品CSVDLオプションクラス
 *
 */
@Entity
@Data
@Table(name = "csvoption")
@Component
@Scope("prototype")
public class CsvOptionEntity implements Serializable {

    /** serialVersionUID */
    private static final long serialVersionUID = 1L;

    /** オプションID */
    @Column(name = "optionId")
    @Id
    private Integer optionId;

    /** CSVDLオプション情報 */
    @Column(name = "optionInfo")
    private String optionInfo;

}
