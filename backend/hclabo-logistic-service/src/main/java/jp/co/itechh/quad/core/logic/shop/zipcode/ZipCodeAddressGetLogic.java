/*
 * Project Name : HIT-MALL3
 *
 * Copyright (C) 2008 i-TEC HANKYU HANSHIN INC. All Rights Reserved.
 *
 */

package jp.co.itechh.quad.core.logic.shop.zipcode;

import jp.co.itechh.quad.core.dto.shop.ZipCodeAddressDto;

import java.util.List;

/**
 * 郵便番号住所情報取得Logic<br/>
 *
 * @author negishi
 * @version $Revision: 1.3 $
 *
 */
public interface ZipCodeAddressGetLogic {

    /** 定数 **/

    /**
     * 以下に掲載がない場合：〒番号下2桁「00」
     */
    String ZIPCODE_LAST_DIGIT = "00";

    /**
     * 数値7桁の正規表現：[0-9]{7}
     */
    String ZIPCODE_MATCHREGIX = "[0-9]{7}";

    /**
     * 半角空白
     */
    String BLANK = " ";

    /** エラーコード **/

    /** 郵便番号が不正<br>
     * <code>ERROR_ZIPCODE_FAIL</code>
     */
    String MSGCD_ZIPCODE_FAIL = "LZZ000101";

    /** 郵便番号に該当する住所が存在しない場合<br>
     * <code>ERROR_GETOPENZIPCODELIST_ZERO</code>
     */
    String MSGCD_ZIPCODELIST_ZERO = "LZZ000102";

    /**
     * 郵便番号住所情報取得処理<br/>
     *
     * @param zipCode 郵便番号
     * @return 郵便番号住所情報Dto
     */
    ZipCodeAddressDto execute(String zipCode);

    /**
     * 郵便番号住所情報取得処理<br/>
     *
     * @param zipCode 郵便番号
     * @return 郵便番号に一致する住所を格納したList
     */
    List<ZipCodeAddressDto> getAddressList(String zipCode);
}