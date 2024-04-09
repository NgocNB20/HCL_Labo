/*
 * Project Name : HIT-MALL4
 *
 * Copyright (C) 2021 i-TEC HANKYU HANSHIN INC. All Rights Reserved.
 *
 */

package jp.co.itechh.quad.core.dao.conveni;

import jp.co.itechh.quad.core.entity.conveni.ConvenienceEntity;
import org.seasar.doma.Dao;
import org.seasar.doma.Select;
import org.seasar.doma.boot.ConfigAutowireable;

import java.util.List;

/**
 * コンビニ名称Daoクラス
 *
 * @author thang
 * @version $Revision: 1.0 $
 */
@Dao
@ConfigAutowireable
public interface ConvenienceDao {

    /**
     * コンビニ名称リスト取得
     *
     * @param shopSeq ショップSEQ
     * @param limitToUseConveni 使うコンビニに取得を制限するか true:制限する
     * @return コンビニ名称エンティティリスト
     */
    @Select
    List<ConvenienceEntity> getConvenienceList(Integer shopSeq, boolean limitToUseConveni);

}
