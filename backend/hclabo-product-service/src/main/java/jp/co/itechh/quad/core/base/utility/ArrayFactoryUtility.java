/*
 * Project Name : HIT-MALL3
 *
 * Copyright (C) 2008 i-TEC HANKYU HANSHIN INC. All Rights Reserved.
 *
 */
package jp.co.itechh.quad.core.base.utility;

import jp.co.itechh.quad.core.dao.ArrayFactoryDao;
import org.apache.commons.lang3.ObjectUtils;
import org.springframework.stereotype.Component;

import java.sql.Array;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

/**
 * 配列工場ユーティリティクラス
 *
 * @author Doan Thang (VJP)
 */

@Component
public class ArrayFactoryUtility {

    /** ArrayFactoryDao */
    private final ArrayFactoryDao arrayFactoryDao;

    /**
     * コンストラクター
     */
    public ArrayFactoryUtility(ArrayFactoryDao arrayFactoryDao) {
        this.arrayFactoryDao = arrayFactoryDao;
    }

    /**
     * 配列に変換
     *
     * @param strings 配列
     * @return 配列
     */
    public Array createTextArray(String[] strings) throws Exception {
        return arrayFactoryDao.createStringArray(strings);
    }

    /**
     * 配列に変換
     *
     * @param stringList 配列
     * @return 配列
     */
    public Array createTextArrayFromList(List<String> stringList) throws Exception {
        String[] strings = stringList.stream().toArray(String[]::new);

        return this.createTextArray(strings);
    }

    /**
     * リストに変換
     *
     * @param array 配列
     * @return リスト
     */
    public List<String> arrayToList(Array array) throws Exception {
        if (ObjectUtils.isEmpty(array)) {
            return new ArrayList<>();
        }
        String[] strings = (String[]) array.getArray();
        List<String> responseList = Arrays.asList(strings);
        return responseList;
    }

}
