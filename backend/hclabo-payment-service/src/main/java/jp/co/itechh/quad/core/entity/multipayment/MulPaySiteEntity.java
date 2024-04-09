/*
 * Project Name : HIT-MALL4
 *
 * Copyright (C) 2021 i-TEC HANKYU HANSHIN INC. All Rights Reserved.
 *
 */

package jp.co.itechh.quad.core.entity.multipayment;

import lombok.Data;
import org.seasar.doma.Column;
import org.seasar.doma.Entity;
import org.seasar.doma.Id;
import org.seasar.doma.Table;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Component;

import java.io.Serializable;

/**
 * マルチペイメント用サイト設定
 * @author EntityGenerator
 *
 */
@Entity
@Table(name = "MulPaySite")
@Data
@Component
@Scope("prototype")
public class MulPaySiteEntity implements Serializable {

    /** シリアル */
    private static final long serialVersionUID = -899850354566448635L;

    /** マルチペイメント接続用サイトID */
    @Column(name = "siteId")
    @Id
    private String siteId;

    /** マルチペイメント接続用サイトパスワード */
    @Column(name = "sitePassword")
    private String sitePassword;

    /** マルチペイメント提供サイト管理画面URL ※hitmall では直接使用しない */
    @Column(name = "siteAccessUrl")
    private String siteAccessUrl;

}
