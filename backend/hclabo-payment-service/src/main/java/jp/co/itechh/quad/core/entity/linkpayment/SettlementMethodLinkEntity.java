/*
 * Project Name : HIT-MALL4
 *
 * Copyright (C) 2022 i-TEC HANKYU HANSHIN INC. All Rights Reserved.
 *
 */

package jp.co.itechh.quad.core.entity.linkpayment;

import lombok.Data;
import org.seasar.doma.Column;
import org.seasar.doma.Entity;
import org.seasar.doma.Id;
import org.seasar.doma.Table;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Component;

import java.io.Serializable;

/**
 * リンク決済個別決済手段 マスタ
 *
 * @author EntityGenerator
 * @version $Revision: 1.0 $
 */
@Entity
@Table(name = "SettlementMethodLink")
@Data
@Component
@Scope("prototype")
public class SettlementMethodLinkEntity implements Serializable {

    /** serialVersionUID */
    private static final long serialVersionUID = 1L;

    /** 決済手段識別子 */
    @Column(name = "payMethod")
    @Id
    private String payMethod;

    /** 決済方法(GMO) */
    @Column(name = "payType")
    private String payType;

    /** 決済手段名 */
    @Column(name = "payTypeName")
    private String payTypeName;

    /** キャンセル期限(日数) */
    @Column(name = "cancelLimitDay")
    private Integer cancelLimitDay;

    /** キャンセル期限(月末) */
    @Column(name = "cancelLimitMonth")
    private Integer cancelLimitMonth;

}
