/*
 * Project Name : HIT-MALL4
 *
 * Copyright (C) 2021 i-TEC HANKYU HANSHIN INC. All Rights Reserved.
 *
 */

package jp.co.itechh.quad.core.dto.shop;

import lombok.Data;
import org.seasar.doma.Entity;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Component;

import java.io.Serializable;

/**
 * 郵便番号住所情報Dtoクラス
 *
 * @author PHAM QUANG DIEU (VJP)
 */
@Entity
@Data
@Component
@Scope("prototype")
public class ZipCodeAddressDto implements Serializable {

    /** serialVersionUID */
    private static final long serialVersionUID = 1L;

    /** 郵便番号 */
    private String zipCode;

    /** 都道府県名 */
    private String prefectureName;

    /**
     * 都道府県名カナ<br/>
     * 住所の郵便番号データの場合、郵便番号マスタに登録されている値<br/>
     * 事業所の個別郵便番号の場合、常に空文字<br/>
     */
    private String prefectureNameKana;

    /** 市区町村名 */
    private String cityName;

    /**
     * 市区町村名カナ<br/>
     * 住所の郵便番号データの場合、郵便番号マスタに登録されている値<br/>
     * 事業所の個別郵便番号の場合、常に空文字<br/>
     */
    private String cityNameKana;

    /** 町域名 */
    private String townName;

    /**
     * 町域名カナ<br/>
     * 住所の郵便番号データの場合、郵便番号マスタに登録されている値<br/>
     * 事業所の個別郵便番号の場合、常に空文字<br/>
     */
    private String townNameKana;

    /**
     * 小字名、丁目、番地等<br/>
     * 住所の郵便番号データの場合、常に空文字<br/>
     * 事業所の個別郵便番号の場合、事業所郵便番号マスタに登録されている値<br/>
     */
    private String numbers;

    /**
     * 郵便番号タイプ<br/>
     * 0:住所の郵便番号
     * 1:事業所の郵便番号
     */
    private String zipCodeType;
}
