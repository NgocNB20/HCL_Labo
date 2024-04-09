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
 * マルチペイメント用ショップ設定
 * @author EntityGenerator
 *
 */
@Entity
@Table(name = "MulPayShop")
@Data
@Component
@Scope("prototype")
public class MulPayShopEntity implements Serializable {

    /** シリアル */
    private static final long serialVersionUID = 4258226645549343245L;

    /** ショップSEQ */
    @Column(name = "shopSeq")
    @Id
    private Integer shopSeq;

    /** ショップID */
    @Column(name = "shopId")
    private String shopId;

    /** ショップパスワード */
    @Column(name = "shopPass")
    private String shopPass;

    /** アクセスURL */
    @Column(name = "shopAccessUrl")
    private String shopAccessUrl;

    /** 3Dセキュア表示店舗名 */
    @Column(name = "tdTenantName")
    private String tdTenantName;

    /** HTTP_ACCEPT */
    @Column(name = "httpAccept")
    private String httpAccept;

    /** HTTP_USER_AGENT */
    @Column(name = "httpUserAgent")
    private String httpUserAgent;

    /** 加盟店舗自由項目１ */
    @Column(name = "clientField1")
    private String clientField1;

    /** 加盟店舗自由項目２ */
    @Column(name = "clientField2")
    private String clientField2;

    /** 加盟店舗自由項目３ */
    @Column(name = "clientField3")
    private String clientField3;

    /** 加盟店自由項目返却フラグ */
    @Column(name = "clientFieldFlag")
    private String clientFieldFlag;

    /** 加盟店メールアドレス */
    @Column(name = "shopMailAddress")
    private String shopMailAddress;

    /** POS レジ表示欄-ショップ名 */
    @Column(name = "registerDisp1")
    private String registerDisp1;

    /** POS レジ表示欄2 */
    @Column(name = "registerDisp2")
    private String registerDisp2;

    /** POS レジ表示欄3 */
    @Column(name = "registerDisp3")
    private String registerDisp3;

    /** POS レジ表示欄4  */
    @Column(name = "registerDisp4")
    private String registerDisp4;

    /** POS レジ表示欄5 */
    @Column(name = "registerDisp5")
    private String registerDisp5;

    /** POS レジ表示欄6 */
    @Column(name = "registerDisp6")
    private String registerDisp6;

    /** POS レジ表示欄7 */
    @Column(name = "registerDisp7")
    private String registerDisp7;

    /** POS レジ表示欄8 */
    @Column(name = "registerDisp8")
    private String registerDisp8;

    /** レシート表示欄1 */
    @Column(name = "receiptsDisp1")
    private String receiptsDisp1;

    /** レシート表示欄2 */
    @Column(name = "receiptsDisp2")
    private String receiptsDisp2;

    /** レシート表示欄3 */
    @Column(name = "receiptsDisp3")
    private String receiptsDisp3;

    /** レシート表示欄4 */
    @Column(name = "receiptsDisp4")
    private String receiptsDisp4;

    /** レシート表示欄5 */
    @Column(name = "receiptsDisp5")
    private String receiptsDisp5;

    /** レシート表示欄6 */
    @Column(name = "receiptsDisp6")
    private String receiptsDisp6;

    /** レシート表示欄7 */
    @Column(name = "receiptsDisp7")
    private String receiptsDisp7;

    /** レシート表示欄8 */
    @Column(name = "receiptsDisp8")
    private String receiptsDisp8;

    /** レシート表示欄9 */
    @Column(name = "receiptsDisp9")
    private String receiptsDisp9;

    /** レシート表示欄10 */
    @Column(name = "receiptsDisp10")
    private String receiptsDisp10;

    /** レシート表示欄11 */
    @Column(name = "receiptsDisp11")
    private String receiptsDisp11;

    /** レシート表示欄12 */
    @Column(name = "receiptsDisp12")
    private String receiptsDisp12;

    /** レシート表示欄13 */
    @Column(name = "receiptsDisp13")
    private String receiptsDisp13;

    /** 商品・サービス名 */
    @Column(name = "itemName")
    private String itemName;

}
