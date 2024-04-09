/*
 * Project Name : HIT-MALL4
 *
 * Copyright (C) 2021 i-TEC HANKYU HANSHIN INC. All Rights Reserved.
 *
 */

package jp.co.itechh.quad.core.dao.zipcode;

import jp.co.itechh.quad.core.dto.shop.ZipCodeAddressDto;
import jp.co.itechh.quad.core.entity.zipcode.ZipCodeEntity;
import org.seasar.doma.Dao;
import org.seasar.doma.Insert;
import org.seasar.doma.Select;
import org.seasar.doma.Update;
import org.seasar.doma.boot.ConfigAutowireable;

import java.util.List;

/**
 * 郵便番号住所情報Daoクラス<br/>
 *
 * @author thang
 * @version $Revision: 1.0 $
 */
@Dao
@ConfigAutowireable
public interface ZipCodeDao {

    /**
     * インサート
     * @param zipCodeEntity 郵便番号住所情報
     * @return 登録した数
     */
    @Insert(excludeNull = true)
    int insert(ZipCodeEntity zipCodeEntity);

    /**
     * アップデート
     * @param zipCodeEntity 郵便番号住所情報
     * @return 更新した数
     */
    @Update
    int update(ZipCodeEntity zipCodeEntity);

    /**
     * 郵便番号リストの取得<br/>
     * 郵便番号マスタと事業所郵便番号マスタより、郵便番号に該当する住所を取得する。<br/>
     *
     * @param zipCode 郵便番号
     * @return 郵便番号住所情報Dto
     */
    @Select
    List<ZipCodeAddressDto> getAddressList(String zipCode);

    /**
     * 郵便番号エンティティを取得する<br/>
     *
     * @param localCode 全国地方公共団体コード
     * @param oldZipCode (旧)郵便番号
     * @param zipCode 郵便番号
     * @param prefectureName 都道府県名
     * @param cityName 市区町村名
     * @param townName 町域名
     *
     * @return 郵便番号エンティティ
     */
    @Select
    ZipCodeEntity getEntity(String localCode,
                            String oldZipCode,
                            String zipCode,
                            String prefectureName,
                            String cityName,
                            String townName);
}