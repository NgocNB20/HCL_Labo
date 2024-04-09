/*
 * Project Name : HIT-MALL4
 *
 * Copyright (C) 2021 i-TEC HANKYU HANSHIN INC. All Rights Reserved.
 *
 */
package jp.co.itechh.quad.core.dao.shop.delivery;

import jp.co.itechh.quad.core.dto.shop.delivery.HolidayCsvDto;
import jp.co.itechh.quad.core.dto.shop.delivery.HolidaySearchForDaoConditionDto;
import jp.co.itechh.quad.core.entity.shop.delivery.HolidayEntity;
import org.seasar.doma.Dao;
import org.seasar.doma.Delete;
import org.seasar.doma.Insert;
import org.seasar.doma.Select;
import org.seasar.doma.Update;
import org.seasar.doma.boot.ConfigAutowireable;
import org.seasar.doma.jdbc.SelectOptions;

import java.sql.Timestamp;
import java.util.Date;
import java.util.List;
import java.util.stream.Stream;

/**
 *
 * 休日Daoクラス
 *
 * @author thang
 *
 */
@Dao
@ConfigAutowireable
public interface HolidayDao {

    /**
     * インサート<br/>
     *
     * @param holiday 休日エンティティ
     * @return 処理件数
     */
    @Insert(excludeNull = true)
    int insert(HolidayEntity holiday);

    /**
     * アップデート<br/>
     *
     * @param holiday 休日エンティティ
     * @return 処理件数
     */
    @Update
    int update(HolidayEntity holiday);

    /**
     * デリート<br/>
     *
     * @param holiday 休日エンティティ
     * @return 処理件数
     */
    @Delete
    int delete(HolidayEntity holiday);

    /**
     * 休日情報取得<br/>
     *
     * @param year 年
     * @param date 年月日
     * @param deliveryMethodSeq 配送方法SEQ
     * @return 休日エンティティDTO
     */
    @Select
    HolidayEntity getHolidayByYearAndDate(Integer year, Date date, Integer deliveryMethodSeq);

    /**
     * 休日情報リスト取得<br/>
     *
     * @param holidaySearchForDaoConditionDto 休日検索条件DTO
     * @param selectOptions 検索オプション
     * @return 休日エンティティDTOリスト
     */
    @Select
    List<HolidayEntity> getSearchHolidayList(HolidaySearchForDaoConditionDto holidaySearchForDaoConditionDto,
                                             SelectOptions selectOptions);

    /**
     * 休日CSVダウンロード<br/>
     *
     * @param holidaySearchForDaoConditionDto 休日検索条件DTO
     * @return Stream<HolidayCsvDto> ダウンロード取得
     */
    @Select
    Stream<HolidayCsvDto> exportCsvSearchHolidayList(HolidaySearchForDaoConditionDto holidaySearchForDaoConditionDto);

    /**
     * 休日情報の削除<br/>
     *
     * @param holidayEntity 休日エンティティ
     * @return 更新件数
     */
    @Delete(sqlFile = true)
    int deleteHolidayByYear(HolidayEntity holidayEntity);

    /**
     * 休日情報を日付から件数取得
     *
     * @param date              指定日付
     * @param deliveryMethodSeq 配送方法SEQ
     * @return 件数
     */
    @Select
    int getCountByDate(Date date, Integer deliveryMethodSeq);

    /**
     * 指定日付を含むそれ以降の休日を取得
     *
     * @param date              指定日付
     * @param deliveryMethodSeq 配送方法SEQ
     * @return 休日リスト
     */
    @Select
    List<Timestamp> getHolidayByDeliveryMethodSeq(Date date, Integer deliveryMethodSeq);

}