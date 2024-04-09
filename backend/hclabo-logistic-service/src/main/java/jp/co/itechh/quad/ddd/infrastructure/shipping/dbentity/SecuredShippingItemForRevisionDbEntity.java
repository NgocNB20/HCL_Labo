/*
 * Copyright (C) 2022 i-TEC HANKYU HANSHIN INC. All Rights Reserved.
 */

package jp.co.itechh.quad.ddd.infrastructure.shipping.dbentity;

import lombok.Data;
import org.seasar.doma.Entity;
import org.seasar.doma.GeneratedValue;
import org.seasar.doma.GenerationType;
import org.seasar.doma.Id;
import org.seasar.doma.SequenceGenerator;
import org.seasar.doma.Table;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Component;

import java.io.Serializable;

/**
 * 改訂用配送商品DB値オブジェクト
 */
@Entity
@Table(name = "SecuredShippingItemForRevision")
@Data
@Component
@Scope("prototype")
public class SecuredShippingItemForRevisionDbEntity implements Serializable {

    /** serialVersionUID */
    private static final long serialVersionUID = 1L;

    /** ID */
    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE)
    @SequenceGenerator(sequence = "securedshippingitemforrevision_id_seq")
    private Long id;

    /** 商品ID（商品サービスの商品SEQ） */
    private String itemId;

    /** 配送商品連番（注文商品連番） */
    private int shippingItemSeq;

    /** 商品名 */
    private String itemName;

    /** 規格タイトル1 */
    private String unitTitle1;

    /** 規格値1 */
    private String unitValue1;

    /** 規格タイトル2 */
    private String unitTitle2;

    /** 規格値2 */
    private String unitValue2;

    /** 配送数量 */
    private double shippingCount;

    /** 改訂用配送伝票ID */
    private String shippingSlipRevisionId;
}
