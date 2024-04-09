/*
 * Project Name : HIT-MALL4
 *
 * Copyright (C) 2021 i-TEC HANKYU HANSHIN INC. All Rights Reserved.
 *
 */

package jp.co.itechh.quad.core.dao.zipcode;

import jp.co.itechh.quad.core.entity.zipcode.OfficeZipCodeEntity;
import org.seasar.doma.Dao;
import org.seasar.doma.Insert;
import org.seasar.doma.Select;
import org.seasar.doma.Update;
import org.seasar.doma.boot.ConfigAutowireable;

/**
 * 事業所の個別郵便番号住所情報Daoクラス<br/>
 *
 * @author thang
 * @version $Revision: 1.0 $
 */
@Dao
@ConfigAutowireable
public interface OfficeZipCodeDao {

    /**
     * インサート
     * @param officeZipCodeEntity 事業所の個別郵便番号住所情報
     * @return 登録した数
     */
    @Insert(excludeNull = true)
    int insert(OfficeZipCodeEntity officeZipCodeEntity);

    /**
     * アップデート
     * @param officeZipCodeEntity 事業所の個別郵便番号住所情報
     * @return 更新した数
     */
    @Update
    int update(OfficeZipCodeEntity officeZipCodeEntity);

    /**
     * 事業所郵便番号エンティティを取得する<br/>
     *
     * @param officeCode 大口事業所の所在地
     * @param officeKana 大口事業所名（カナ）
     * @param officeName 大口事業所名（漢字）
     * @param prefectureName 都道府県名（漢字）
     * @param cityName 市区町村名（漢字）
     * @param townName 町域名（漢字）
     * @param numbers 小字名、丁目、番地等（漢字）
     * @param zipCode 大口事業所個別番号
     * @param oldZipCode 旧郵便番号
     * @return 事業所郵便番号エンティティ
     */
    @Select
    OfficeZipCodeEntity getEntity(String officeCode,
                                  String officeKana,
                                  String officeName,
                                  String prefectureName,
                                  String cityName,
                                  String townName,
                                  String numbers,
                                  String zipCode,
                                  String oldZipCode);
}